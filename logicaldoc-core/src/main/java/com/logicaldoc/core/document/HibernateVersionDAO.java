package com.logicaldoc.core.document;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import com.logicaldoc.core.HibernatePersistentObjectDAO;
import com.logicaldoc.core.PersistenceException;
import com.logicaldoc.core.PersistentObject;
import com.logicaldoc.core.folder.Folder;
import com.logicaldoc.core.folder.FolderDAO;
import com.logicaldoc.core.store.Storer;
import com.logicaldoc.util.config.ContextProperties;
import com.logicaldoc.util.io.FileUtil;

/**
 * Hibernate implementation of <code>DocumentDAO</code>
 * 
 * @author Marco Meschieri - LogicalDOC
 * @since 3.0
 */
public class HibernateVersionDAO extends HibernatePersistentObjectDAO<Version> implements VersionDAO {

	private static final String DOC_ID = ".docId=";

	@Resource(name = "Storer")
	private Storer storer;

	@Resource(name = "FolderDAO")
	private FolderDAO folderDAO;

	private HibernateVersionDAO() {
		super(Version.class);
		super.log = LoggerFactory.getLogger(HibernateVersionDAO.class);
	}

	@Override
	public List<Version> findByDocId(long docId) {
		try {
			return findByWhere(" " + ENTITY + DOC_ID + docId, "order by " + ENTITY + ".versionDate desc", null);
		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
			return new ArrayList<>();
		}

	}

	@Override
	public Version findByVersion(long docId, String version) {
		List<Version> versions = new ArrayList<>();
		try {
			versions = findByWhere(" " + ENTITY + DOC_ID + docId + " and " + ENTITY + ".version='" + version + "'",
					null, null);
		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		}

		if (!versions.isEmpty())
			return versions.get(0);
		else
			return null;
	}

	@Override
	public Version findByFileVersion(long docId, String fileVersion) {
		List<Version> versions = new ArrayList<>();
		try {
			versions = findByWhere(
					" " + ENTITY + DOC_ID + docId + " and " + ENTITY + ".fileVersion='" + fileVersion + "'",
					"order by " + ENTITY + ".date asc", null);
		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		}

		if (!versions.isEmpty())
			return versions.get(0);
		else
			return null;
	}

	@Override
	public void initialize(Version version) {
		refresh(version);

		if (version.getAttributes() != null)
			log.trace("Initialized {} attributes", version.getAttributes().keySet().size());
	}

	/**
	 * This method persists the given version. Checks if is necessary to delete
	 * some document versions reading the context property
	 * 'document.maxversions' and the maxVersions property of the owning
	 * workspace.
	 * 
	 * @param version version to be stored.
	 * 
	 * @throws PersistenceException error at data layer
	 */
	@Override
	public void store(Version version) throws PersistenceException {
		super.store(version);

		try {
			// Checks the context property 'document.maxversions'
			ContextProperties bean = new ContextProperties();
			int maxVersions = bean.getInt("document.maxversions", 10);
			Folder workspace = folderDAO.findWorkspace(version.getFolderId());
			if (workspace != null && workspace.getMaxVersions() != null && workspace.getMaxVersions() > 0)
				maxVersions = workspace.getMaxVersions();

			if (maxVersions > 0) {
				List<Version> versions = findByDocId(version.getDocId());

				if (versions.size() > maxVersions) {
					// Delete the oldest versions
					deleteOldestVersions(versions, maxVersions);

					// Prepare a list of files(fileVersion) that must be
					// retained
					Set<String> filesToBeRetained = new HashSet<>();
					for (int i = 0; i < versions.size(); i++)
						if (i < maxVersions && !filesToBeRetained.contains(versions.get(i).getFileVersion()))
							filesToBeRetained.add(versions.get(i).getFileVersion());

					// Clean the files no more needed
					cleanUnusedFiles(version, filesToBeRetained);
				}
			}
		} catch (IOException e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	private void cleanUnusedFiles(Version version, Set<String> filesToBeRetained) {
		List<String> resources = storer.listResources(version.getDocId(), null);
		for (String resource : resources) {
			boolean toDelete = true;
			for (String fileVersionToRetain : filesToBeRetained) {
				if (resource.trim().equals(fileVersionToRetain.trim())
						|| resource.trim().startsWith(fileVersionToRetain.trim() + "-")) {
					toDelete = false;
					break;
				}
			}
			if (toDelete) {
				storer.delete(version.getDocId(), resource);
			}
		}
	}

	private void deleteOldestVersions(List<Version> versions, int maxVersions) {
		if (versions.size() <= maxVersions)
			return;

		// Meke sure to sort the versions by descending version spec
		Collections.sort(versions, Collections.reverseOrder());

		List<Version> oldestVersionsToDelete = versions.stream().skip(Math.max(0, versions.size() - maxVersions))
				.toList();
		for (Version versionToDelete : oldestVersionsToDelete)
			deleteVersion(versionToDelete, PersistentObject.DELETED_CODE_DEFAULT);
	}

	@Override
	public void updateDigest(Version version) {
		initialize(version);
		String resource = storer.getResourceName(version.getDocId(), version.getFileVersion(), null);
		if (storer.exists(version.getDocId(), resource)) {

			try (InputStream in = storer.getStream(version.getDocId(), resource);) {
				version.setDigest(FileUtil.computeDigest(in));
			} catch (IOException e) {
				log.error("Cannot retrieve the content of version {}", version, e);
			}
			saveOrUpdate(version);
		}
	}

	public void setStorer(Storer storer) {
		this.storer = storer;
	}

	@Override
	public void delete(long versionId, int delCode) throws PersistenceException {
		if (!checkStoringAspect())
			return;

		Version ver = findById(versionId);
		if (ver != null)
			deleteVersion(ver, delCode);
	}

	protected void deleteVersion(Version versionToDelete, int delCode) {
		if (delCode == 0)
			throw new IllegalArgumentException("delCode cannot be 0");
		versionToDelete.setDeleted(delCode);
		versionToDelete.setLastModified(new Date());
		versionToDelete.setVersion(StringUtils.right(versionToDelete.getId() + "." + versionToDelete.getVersion(), 10));
		saveOrUpdate(versionToDelete);
	}

	public void setFolderDAO(FolderDAO folderDAO) {
		this.folderDAO = folderDAO;
	}
}