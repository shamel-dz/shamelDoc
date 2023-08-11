package com.logicaldoc.gui.frontend.client.document;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.logicaldoc.gui.common.client.Constants;
import com.logicaldoc.gui.common.client.Session;
import com.logicaldoc.gui.common.client.beans.GUIDocument;
import com.logicaldoc.gui.common.client.beans.GUIFolder;
import com.logicaldoc.gui.common.client.data.LinksDS;
import com.logicaldoc.gui.common.client.i18n.I18N;
import com.logicaldoc.gui.common.client.log.GuiLog;
import com.logicaldoc.gui.common.client.observer.DocumentController;
import com.logicaldoc.gui.common.client.observer.FolderController;
import com.logicaldoc.gui.common.client.util.DocUtil;
import com.logicaldoc.gui.common.client.util.LD;
import com.logicaldoc.gui.common.client.util.Util;
import com.logicaldoc.gui.common.client.util.WindowUtils;
import com.logicaldoc.gui.common.client.widgets.grid.ColoredListGridField;
import com.logicaldoc.gui.common.client.widgets.grid.FileNameListGridField;
import com.logicaldoc.gui.common.client.widgets.preview.PreviewPopup;
import com.logicaldoc.gui.frontend.client.services.DocumentService;
import com.logicaldoc.gui.frontend.client.services.FolderService;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * This panel shows the links of a document
 * 
 * @author Marco Meschieri - LogicalDOC
 * @since 6.0
 */
public class LinksPanel extends DocumentDetailTab {

	private static final String DOCUMENT_ID = "documentId";

	private TreeGrid treeGrid;

	public LinksPanel(final GUIDocument document) {
		super(document, null);
	}

	@Override
	protected void onDraw() {
		ListGridField type = new ColoredListGridField("type", I18N.message("type"), 100);
		type.setCanEdit(true);

		ListGridField direction = new ListGridField("direction", I18N.message("direction"), 60);
		direction.setCanSort(false);
		direction.setType(ListGridFieldType.IMAGE);
		direction.setCanSort(false);
		direction.setAlign(Alignment.CENTER);
		direction.setShowDefaultContextMenu(false);
		direction.setImageURLPrefix(Util.imagePrefix() + "document_");
		direction.setImageURLSuffix(".png");
		direction.setCanEdit(false);

		ListGridField fileName = new FileNameListGridField("filename", "noicon", I18N.message("link"), 250);
		fileName.setCanEdit(false);

		GUIFolder folder = FolderController.get().getCurrentFolder();

		addGrid(type, direction, fileName, folder);

		prepareContextMenu();

		treeGrid.addDoubleClickHandler(event -> treeGrid.addCellDoubleClickHandler(evnt -> {
			final ListGridRecord rec = evnt.getRecord();

			FolderService.Instance.get().getFolder(rec.getAttributeAsLong("folderId"), false, false, false,
					new AsyncCallback<GUIFolder>() {

						@Override
						public void onFailure(Throwable caught) {
							GuiLog.serverError(caught);
						}

						@Override
						public void onSuccess(GUIFolder fld) {
							if (fld.isDownload()
									&& "download".equals(Session.get().getInfo().getConfig("gui.doubleclick")))
								onDownload(rec);
							else
								onPreview(rec);
						}
					});
		}));
	}

	private void prepareContextMenu() {
		treeGrid.addCellContextClickHandler(event -> {
			final Menu contextMenu = new Menu();

			final ListGridRecord[] selection = treeGrid.getSelectedRecords();
			if (selection == null || selection.length == 0)
				return;
			final long[] ids = new long[selection.length];
			for (int i = 0; i < selection.length; i++)
				ids[i] = Long.parseLong(selection[i].getAttribute("linkId"));

			final MenuItem delete = new MenuItem();
			delete.setTitle(I18N.message("ddelete"));
			delete.addClickHandler(evnt ->

			LD.ask(I18N.message("question"), I18N.message("confirmdelete"), answer -> {
				if (Boolean.TRUE.equals(answer)) {
					DocumentService.Instance.get().deleteLinks(ids, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							GuiLog.serverError(caught);
						}

						@Override
						public void onSuccess(Void result) {
							TreeNode parent = treeGrid.getTree().getParent(treeGrid.getSelectedRecord());
							treeGrid.selectRecord(parent);
							treeGrid.getTree().reloadChildren(parent);
							document.setLinks(document.getLinks() - ids.length);
							DocumentController.get().modified(document);
						}
					});
				}
			}));

			final MenuItem preview = new MenuItem();
			preview.setTitle(I18N.message("preview"));
			preview.addClickHandler((MenuItemClickEvent evnt) -> onPreview(treeGrid.getSelectedRecord()));
			preview.setEnabled(
					com.logicaldoc.gui.common.client.Menu.enabled(com.logicaldoc.gui.common.client.Menu.PREVIEW));

			final MenuItem download = new MenuItem();
			download.setTitle(I18N.message("download"));
			download.addClickHandler((MenuItemClickEvent evnt) -> onDownload(treeGrid.getSelectedRecord()));

			final MenuItem downloadPackage = new MenuItem();
			downloadPackage.setTitle(I18N.message("downloadpackage"));
			downloadPackage.addClickHandler((MenuItemClickEvent evnt) -> onDownloadPackage());

			final MenuItem openInFolder = new MenuItem();
			openInFolder.setTitle(I18N.message("openinfolder"));
			openInFolder.addClickHandler((MenuItemClickEvent evnt) -> onOpenInFolder(treeGrid.getSelectedRecord()));

			// Compute the folder ID of the referenced document
			long folderId = selection[0].getAttributeAsLong("folderId1");
			if (document.getFolder().getId() == folderId)
				folderId = selection[0].getAttributeAsLong("folderId2");

			FolderService.Instance.get().getFolder(folderId, false, false, false, new AsyncCallback<GUIFolder>() {

				@Override
				public void onFailure(Throwable caught) {
					GuiLog.serverError(caught);
				}

				@Override
				public void onSuccess(GUIFolder fld) {
					if (fld == null)
						return;
					download.setEnabled(fld.isDownload());
					downloadPackage.setEnabled(fld.isDownload());
					delete.setEnabled(fld.isDelete());
					openInFolder.setEnabled(com.logicaldoc.gui.common.client.Menu
							.enabled(com.logicaldoc.gui.common.client.Menu.DOCUMENTS));
					contextMenu.setItems(preview, download, downloadPackage, openInFolder, delete);
					contextMenu.showContextMenu();
				}
			});

			event.cancel();
		});
	}

	private void addGrid(ListGridField type, ListGridField direction, ListGridField fileName, GUIFolder folder) {
		treeGrid = new TreeGrid() {

			@Override
			protected String getIcon(Record node, boolean defaultState) {
				return Util.imageUrl(node.getAttributeAsString("icon") + ".png");
			}

		};
		treeGrid.setCanEdit(false);
		treeGrid.setClosedIconSuffix("");
		treeGrid.setCanFreezeFields(true);
		treeGrid.setAutoFetchData(true);
		treeGrid.setTitleField("filename");
		treeGrid.setDataSource(new LinksDS(document.getId()));
		treeGrid.setAutoFetchData(true);
		treeGrid.setLoadDataOnDemand(true);
		treeGrid.setEmptyMessage(I18N.message("notitemstoshow"));
		treeGrid.setFields(fileName, direction, type);
		addMember(treeGrid);

		if (folder != null && folder.hasPermission(Constants.PERMISSION_WRITE)) {
			treeGrid.setCanEdit(true);
			treeGrid.setEditEvent(ListGridEditEvent.CLICK);
			treeGrid.setEditByCell(true);
			treeGrid.addEditCompleteHandler((EditCompleteEvent event) -> {
				long id = Long.parseLong(event.getOldValues().getAttribute("linkId"));
				final String typ = (String) event.getNewValues().get("type");
				DocumentService.Instance.get().updateLink(id, typ, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						GuiLog.serverError(caught);
					}

					@Override
					public void onSuccess(Void result) {
						treeGrid.getSelectedRecord().setAttribute("type", typ);
						treeGrid.updateData(treeGrid.getSelectedRecord());
					}
				});
			});
		}
	}

	protected void onOpenInFolder(ListGridRecord rec) {
		String documentId = rec.getAttributeAsString(DOCUMENT_ID);
		long docId = Long.parseLong(documentId.substring(documentId.lastIndexOf('-') + 1));
		DocumentService.Instance.get().getById(docId, new AsyncCallback<GUIDocument>() {

			@Override
			public void onFailure(Throwable caught) {
				GuiLog.serverError(caught.getMessage(), caught);
			}

			@Override
			public void onSuccess(GUIDocument document) {
				DocumentsPanel.get().openInFolder(document.getFolder().getId(), document.getId());
			}
		});
	}

	protected void onDownload(ListGridRecord rec) {
		if (document.getFolder().isDownload()) {
			String documentId = rec.getAttributeAsString(DOCUMENT_ID);
			long docId = Long.parseLong(documentId.substring(documentId.lastIndexOf('-') + 1));
			DocUtil.download(docId, null);
		}
	}

	protected void onDownloadPackage() {
		if (document.getFolder().isDownload()) {
			StringBuilder url = new StringBuilder(GWT.getHostPageBaseURL());
			url.append("zip-export?folderId=");
			url.append(document.getFolder().getId());
			url.append("&docId=" + document.getId());

			treeGrid.getRecords();

			for (ListGridRecord rec : treeGrid.getRecords()) {
				if (Boolean.TRUE.equals(rec.getAttributeAsBoolean("password"))) {
					SC.warn(I18N.message("somedocsprotected"));
					break;
				}

				String docId = rec.getAttribute(DOCUMENT_ID);
				docId = docId.substring(docId.indexOf('-') + 1);
				url.append("&docId=");
				url.append(docId);
			}
			WindowUtils.openUrl(url.toString());
		}
	}

	protected void onPreview(ListGridRecord rec) {
		String documentId = rec.getAttributeAsString(DOCUMENT_ID);
		long docId = Long.parseLong(documentId.substring(documentId.lastIndexOf('-') + 1));
		DocumentService.Instance.get().getById(docId, new AsyncCallback<GUIDocument>() {

			@Override
			public void onFailure(Throwable caught) {
				GuiLog.serverError(caught.getMessage(), caught);
			}

			@Override
			public void onSuccess(GUIDocument document) {
				PreviewPopup iv = new PreviewPopup(document);
				iv.show();
			}
		});
	}
}