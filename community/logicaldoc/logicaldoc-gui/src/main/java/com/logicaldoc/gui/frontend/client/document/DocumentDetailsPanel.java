package com.logicaldoc.gui.frontend.client.document;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.logicaldoc.gui.common.client.Constants;
import com.logicaldoc.gui.common.client.Feature;
import com.logicaldoc.gui.common.client.Menu;
import com.logicaldoc.gui.common.client.Session;
import com.logicaldoc.gui.common.client.beans.GUIDocument;
import com.logicaldoc.gui.common.client.i18n.I18N;
import com.logicaldoc.gui.common.client.log.GuiLog;
import com.logicaldoc.gui.common.client.observer.DocumentController;
import com.logicaldoc.gui.common.client.observer.DocumentObserver;
import com.logicaldoc.gui.common.client.util.LD;
import com.logicaldoc.gui.common.client.util.Util;
import com.logicaldoc.gui.common.client.widgets.EditingTabSet;
import com.logicaldoc.gui.frontend.client.document.note.NotesPanel;
import com.logicaldoc.gui.frontend.client.document.signature.SignaturePanel;
import com.logicaldoc.gui.frontend.client.services.DocumentService;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

/**
 * This panel collects all documents details
 * 
 * @author Marco Meschieri - LogicalDOC
 * @since 6.0
 */
public class DocumentDetailsPanel extends VLayout implements DocumentObserver {
	private static final String ID_TAB_ALIASES = "tab-aliases";

	private static final String ID_TAB_SUBSCRIPTIONS = "tab-subscriptions";

	protected GUIDocument document;

	protected String originalExtension;

	protected Layout propertiesTabPanel;

	protected Layout extendedPropertiesTabPanel;

	protected Layout versionsTabPanel;

	protected Layout historyTabPanel;

	protected Layout signatureTabPanel;

	protected Layout aliasesTabPanel;

	protected Layout linksTabPanel;

	protected Layout notesTabPanel;

	protected Layout previewTabPanel;

	protected Layout retentionPoliciesTabPanel;

	protected Layout calendarTabPanel;

	protected Layout subscriptionsTabPanel;

	protected Layout captureTabPanel;

	protected StandardPropertiesPanel propertiesPanel;

	protected DocumentExtendedPropertiesPanel extendedPropertiesPanel;

	protected VersionsPanel versionsPanel;

	protected HistoryPanel historyPanel;

	protected SignaturePanel signaturePanel;

	protected AliasesPanel aliasesPanel;

	protected LinksPanel linksPanel;

	protected NotesPanel notesPanel;

	protected DetailsPreviewPanel previewPanel;

	protected DocumentCalendarPanel calendarPanel;

	protected DocumentSubscriptionsPanel subscriptionsPanel;

	protected PublishingPanel retentionPoliciesPanel;

	protected DocumentCapturePanel ocrPanel;

	protected EditingTabSet tabSet;

	protected Tab propertiesTab;

	protected Tab extendedPropertiesTab;

	protected Tab linksTab;

	protected Tab notesTab;

	protected Tab versionsTab;

	protected Tab historyTab;

	protected Tab signatureTab;

	protected Tab aliasesTab;

	protected Tab previewTab;

	protected Tab retentionPoliciesTab;

	protected Tab calendarTab;

	protected Tab subscriptionsTab;

	protected Tab captureTab;

	public DocumentDetailsPanel() {
		super();

		DocumentController.get().addObserver(this);

		setHeight100();
		setWidth100();
		setMembersMargin(10);

		prepareTabs();
		prepareTabset();
	}

	public void selectDefaultTab() {
		if ("preview".equals(Session.get().getInfo().getConfig("gui.document.tab")))
			tabSet.selectTab(previewTab);
		else
			tabSet.selectTab(propertiesTab);
	}

	protected void prepareTabs() {
		propertiesTab = new Tab(I18N.message("properties"));
		propertiesTabPanel = new HLayout();
		propertiesTabPanel.setWidth100();
		propertiesTabPanel.setHeight100();
		propertiesTab.setPane(propertiesTabPanel);

		extendedPropertiesTab = new Tab(I18N.message("propertiesext"));
		extendedPropertiesTabPanel = new HLayout();
		extendedPropertiesTabPanel.setWidth100();
		extendedPropertiesTabPanel.setHeight100();
		extendedPropertiesTab.setPane(extendedPropertiesTabPanel);

		linksTab = new Tab(I18N.message("links"));
		linksTabPanel = new HLayout();
		linksTabPanel.setWidth100();
		linksTabPanel.setHeight100();
		linksTab.setPane(linksTabPanel);

		notesTab = new Tab(I18N.message("notes"));
		notesTabPanel = new HLayout();
		notesTabPanel.setWidth100();
		notesTabPanel.setHeight100();
		notesTab.setPane(notesTabPanel);

		versionsTab = new Tab(I18N.message("versions"));
		versionsTabPanel = new HLayout();
		versionsTabPanel.setWidth100();
		versionsTabPanel.setHeight100();
		versionsTab.setPane(versionsTabPanel);

		historyTab = new Tab(I18N.message("history"));
		historyTabPanel = new HLayout();
		historyTabPanel.setWidth100();
		historyTabPanel.setHeight100();
		historyTab.setPane(historyTabPanel);

		signatureTab = new Tab(I18N.message("signature"));
		signatureTabPanel = new HLayout();
		signatureTabPanel.setWidth100();
		signatureTabPanel.setHeight100();
		signatureTab.setPane(signatureTabPanel);

		aliasesTab = new Tab(I18N.message("aliases"));
		aliasesTab.setID(ID_TAB_ALIASES);
		aliasesTabPanel = new HLayout();
		aliasesTabPanel.setWidth100();
		aliasesTabPanel.setHeight100();
		aliasesTab.setPane(aliasesTabPanel);

		previewTab = new Tab(I18N.message("preview"));
		previewTabPanel = new HLayout();
		previewTabPanel.setWidth100();
		previewTabPanel.setHeight100();
		previewTab.setPane(previewTabPanel);

		retentionPoliciesTab = new Tab(I18N.message("publishing"));
		retentionPoliciesTabPanel = new HLayout();
		retentionPoliciesTabPanel.setWidth100();
		retentionPoliciesTabPanel.setHeight100();
		retentionPoliciesTab.setPane(retentionPoliciesTabPanel);

		captureTab = new Tab(I18N.message("capture"));
		captureTabPanel = new HLayout();
		captureTabPanel.setWidth100();
		captureTabPanel.setHeight100();
		captureTab.setPane(captureTabPanel);

		calendarTab = new Tab(I18N.message("calendar"));
		calendarTabPanel = new HLayout();
		calendarTabPanel.setWidth100();
		calendarTabPanel.setHeight100();
		calendarTab.setPane(calendarTabPanel);

		subscriptionsTab = new Tab(I18N.message("subscriptions"));
		subscriptionsTab.setID(ID_TAB_SUBSCRIPTIONS);
		subscriptionsTabPanel = new HLayout();
		subscriptionsTabPanel.setWidth100();
		subscriptionsTabPanel.setHeight100();
		subscriptionsTab.setPane(subscriptionsTabPanel);
	}

	protected void prepareTabset() {
		tabSet = new EditingTabSet(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onSave();
			}
		}, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// We have to reload the document because the tags may be
				// reverted to the original tags list.
				// This 'if condition' is necessary to know if the close image
				// has been selected into the Documents list panel or into the
				// Search list panel.
				DocumentService.Instance.get().getById(document.getId(), new AsyncCallback<GUIDocument>() {

					@Override
					public void onFailure(Throwable caught) {
						GuiLog.serverError(caught);
					}

					@Override
					public void onSuccess(GUIDocument doc) {
						DocumentController.get().selected(doc);
					}
				});
			}
		});

		tabSet.addTab(propertiesTab);
		propertiesTab.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				propertiesPanel.onTabSelected();
			}
		});

		tabSet.addTab(extendedPropertiesTab);
		propertiesTab.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				extendedPropertiesPanel.onTabSelected();
			}
		});

		if (Menu.enabled(Menu.VERSIONS)) {
			tabSet.addTab(versionsTab);
			versionsTab.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					versionsPanel.onTabSelected();
				}
			});
		}

		tabSet.addTab(previewTab);
		previewTab.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				previewPanel.onTabSelected();
			}
		});

		if (Feature.visible(Feature.NOTES)) {
			tabSet.addTab(notesTab);
			notesTab.setDisabled(!Feature.enabled(Feature.NOTES));
			notesTab.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					notesPanel.onTabSelected();
				}
			});
		}

		tabSet.addTab(linksTab);
		linksTab.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				linksPanel.onTabSelected();
			}
		});

		if (Menu.enabled(Menu.HISTORY)) {
			tabSet.addTab(historyTab);
			historyTab.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					historyPanel.onTabSelected();
				}
			});
		}

		if (Feature.visible(Feature.DIGITAL_SIGNATURE) && Menu.enabled(Menu.SIGNATURE)) {
			tabSet.addTab(signatureTab);
			signatureTab.setDisabled(!Feature.enabled(Feature.DIGITAL_SIGNATURE));
			signatureTab.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					signaturePanel.onTabSelected();
				}
			});
		}

		if (Session.get().getUser().isMemberOf(Constants.GROUP_ADMIN)
				|| Session.get().getUser().isMemberOf(Constants.GROUP_PUBLISHER))
			if (Feature.visible(Feature.RETENTION_POLICIES))
				tabSet.addTab(retentionPoliciesTab);
		retentionPoliciesTab.setDisabled(!Feature.enabled(Feature.RETENTION_POLICIES));
		retentionPoliciesTab.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				retentionPoliciesPanel.onTabSelected();
			}
		});

		if (Feature.visible(Feature.CALENDAR) && Menu.enabled(Menu.DOCUMENT_CALENDAR)) {
			tabSet.addTab(calendarTab);
			calendarTab.setDisabled(!Feature.enabled(Feature.CALENDAR));
			calendarTab.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					calendarPanel.onTabSelected();
				}
			});
		}

		if (Feature.visible(Feature.AUDIT)) {
			tabSet.addTab(subscriptionsTab);
			subscriptionsTab.setDisabled(!Feature.enabled(Feature.AUDIT));
			subscriptionsTab.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					subscriptionsPanel.onTabSelected();
				}
			});
		}

		if (Menu.enabled(Menu.ALIASES)) {
			tabSet.addTab(aliasesTab);
			aliasesTab.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					aliasesPanel.onTabSelected();
				}
			});
		}

		if (Menu.enabled(Menu.CAPTURE)) {
			tabSet.addTab(captureTab);
			captureTab.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					ocrPanel.onTabSelected();
				}
			});
		}

		addMember(tabSet);
	}

	protected void refresh() {
		hideSave();

		/*
		 * Prepare the standard properties tab
		 */
		if (propertiesPanel != null) {
			propertiesPanel.destroy();
			if (propertiesTabPanel.contains(propertiesPanel))
				propertiesTabPanel.removeMember(propertiesPanel);
		}

		ChangedHandler changeHandler = new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				displaySave();
			}
		};
		propertiesPanel = new StandardPropertiesPanel(document, changeHandler);
		propertiesTabPanel.addMember(propertiesPanel);

		/*
		 * Prepare the extended properties tab
		 */
		if (extendedPropertiesPanel != null) {
			extendedPropertiesPanel.destroy();
			if (extendedPropertiesTabPanel.contains(extendedPropertiesPanel))
				extendedPropertiesTabPanel.removeMember(extendedPropertiesPanel);
		}
		ChangedHandler templateChangedHandler = new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				document.setOcrTemplateId(null);
				document.setBarcodeTemplateId(null);
				ocrPanel.refresh(document.getTemplateId());
			}
		};
		extendedPropertiesPanel = new DocumentExtendedPropertiesPanel(document, changeHandler, templateChangedHandler);
		extendedPropertiesTabPanel.addMember(extendedPropertiesPanel);

		/*
		 * Prepare the retention policies tab
		 */
		if (retentionPoliciesPanel != null) {
			retentionPoliciesPanel.destroy();
			if (retentionPoliciesTabPanel.contains(retentionPoliciesPanel))
				retentionPoliciesTabPanel.removeMember(retentionPoliciesPanel);
		}
		retentionPoliciesPanel = new PublishingPanel(document, changeHandler);
		retentionPoliciesTabPanel.addMember(retentionPoliciesPanel);

		/*
		 * Prepare the OCR tab
		 */
		if (ocrPanel != null) {
			ocrPanel.destroy();
			if (captureTabPanel.contains(ocrPanel))
				captureTabPanel.removeMember(ocrPanel);
		}
		ocrPanel = new DocumentCapturePanel(document, changeHandler, true);
		captureTabPanel.addMember(ocrPanel);

		/*
		 * Prepare the versions tab
		 */
		if (versionsPanel != null) {
			versionsPanel.destroy();
			if (versionsTabPanel.contains(versionsPanel))
				versionsTabPanel.removeMember(versionsPanel);
		}
		versionsPanel = new VersionsPanel(document);
		versionsTabPanel.addMember(versionsPanel);

		/*
		 * Prepare the history tab
		 */
		if (historyPanel != null) {
			historyPanel.destroy();
			if (historyTabPanel.contains(historyPanel))
				historyTabPanel.removeMember(historyPanel);
		}
		historyPanel = new HistoryPanel(document);
		historyTabPanel.addMember(historyPanel);

		/*
		 * Prepare the aliases tab
		 */
		if (aliasesPanel != null) {
			aliasesPanel.destroy();
			if (aliasesTabPanel.contains(aliasesPanel))
				aliasesTabPanel.removeMember(aliasesPanel);
		}

		if (document.getDocRef() == null && Menu.enabled(Menu.ALIASES)) {
			aliasesPanel = new AliasesPanel(document);
			aliasesTabPanel.addMember(aliasesPanel);
			if (tabSet.getTab(ID_TAB_ALIASES) == null)
				tabSet.addTab(aliasesTab);
		} else
			tabSet.removeTab(aliasesTab);

		/*
		 * Prepare the links tab
		 */
		if (linksPanel != null) {
			linksPanel.destroy();
			if (linksTabPanel.contains(linksPanel))
				linksTabPanel.removeMember(linksPanel);
		}
		linksPanel = new LinksPanel(document);
		linksTabPanel.addMember(linksPanel);

		/*
		 * Prepare the signature tab
		 */
		if (signaturePanel != null) {
			signaturePanel.destroy();
			if (signatureTabPanel.contains(signaturePanel))
				signatureTabPanel.removeMember(signaturePanel);
		}
		signaturePanel = new SignaturePanel(document);
		signatureTabPanel.addMember(signaturePanel);

		/*
		 * Prepare the notes tab
		 */
		if (notesPanel != null) {
			notesPanel.destroy();
			if (notesTabPanel.contains(notesPanel))
				notesTabPanel.removeMember(notesPanel);
		}
		notesPanel = new NotesPanel(document);
		notesTabPanel.addMember(notesPanel);

		/*
		 * Prepare the preview tab
		 */
		if (previewPanel != null) {
			previewPanel.destroy();
			if (previewTabPanel.contains(previewPanel))
				previewTabPanel.removeMember(previewPanel);
		}
		previewPanel = new DetailsPreviewPanel(document);
		previewTabPanel.addMember(previewPanel);

		/*
		 * Prepare the calendar tab
		 */
		if (calendarPanel != null) {
			calendarPanel.destroy();
			if (calendarTabPanel.contains(calendarPanel))
				calendarTabPanel.removeMember(calendarPanel);
		}
		calendarPanel = new DocumentCalendarPanel(document);
		calendarTabPanel.addMember(calendarPanel);

		/*
		 * Prepare the subscriptions tab
		 */
		if (Feature.visible(Feature.AUDIT)) {
			if (subscriptionsPanel != null) {
				subscriptionsPanel.destroy();
				if (subscriptionsTabPanel.contains(subscriptionsPanel))
					subscriptionsTabPanel.removeMember(subscriptionsPanel);
			}

			if (document.getFolder().hasPermission(Constants.PERMISSION_SUBSCRIPTION)) {
				subscriptionsPanel = new DocumentSubscriptionsPanel(document);
				subscriptionsTabPanel.addMember(subscriptionsPanel);
				if (tabSet.getTab(ID_TAB_SUBSCRIPTIONS) == null)
					tabSet.addTab(subscriptionsTab);
			} else
				tabSet.removeTab(subscriptionsTab);
		}

		if (tabSet != null && tabSet.getSelectedTab() != null) {
			Tab selectedTab = tabSet.getSelectedTab();
			Canvas pane = selectedTab.getPane();
			((DocumentDetailTab) pane.getChildren()[0]).onTabSelected();
		}
	}

	public GUIDocument getDocument() {
		return document;
	}

	public void setDocument(GUIDocument document) {
		this.document = document;
		this.originalExtension = Util.getExtension(document.getFileName());
		refresh();
	}

	private void disableSave() {
		tabSet.disableSave();
	}

	private void hideSave() {
		tabSet.hideSave();
	}

	private void displaySave() {
		tabSet.displaySave();
	}

	private boolean validate() {
		boolean stdValid = propertiesPanel.validate();
		boolean extValid = extendedPropertiesPanel.validate();
		boolean publishingValid = retentionPoliciesPanel.validate();
		boolean ocrValid = ocrPanel.validate();
		if (!stdValid)
			tabSet.selectTab(0);
		else if (!extValid)
			tabSet.selectTab(1);
		else if (!publishingValid)
			tabSet.selectTab(2);
		else if (!ocrValid)
			tabSet.selectTab(captureTab);
		return stdValid && extValid && publishingValid && ocrValid;
	}

	public void onSave() {
		if (validate()) {
			try {
				// Check if the user has changed the extension and warn him
				if (!originalExtension.equalsIgnoreCase(Util.getExtension(document.getFileName()))) {
					LD.ask(I18N.message("filename"), I18N.message("extchangewarn"), new BooleanCallback() {

						@Override
						public void execute(Boolean value) {
							if (value)
								saveDocument();
						}
					});
				} else {
					saveDocument();
				}
			} catch (Throwable t) {
				SC.warn(t.getMessage());
			}
		}
	}

	private void saveDocument() {
		if (Session.get().getConfigAsBoolean("gui.onsave.askversioncomment")) {
			LD.askForString(I18N.message("versioncomment"), I18N.message("versioncomment"), null, new ValueCallback() {

				@Override
				public void execute(String comment) {
					document.setComment(comment);
					save();
				}
			});
		} else {
			save();
		}
	}

	private void save() {
		disableSave();
		DocumentService.Instance.get().save(document, new AsyncCallback<GUIDocument>() {
			@Override
			public void onFailure(Throwable caught) {
				GuiLog.serverError(caught);
				displaySave();
			}

			@Override
			public void onSuccess(GUIDocument result) {
				hideSave();
				DocumentController.get().modified(result);
			}
		});
	}

	@Override
	public void onDocumentSelected(GUIDocument document) {

	}

	@Override
	public void onDocumentModified(GUIDocument document) {
		if (this.document != null && this.document.getId() == document.getId())
			setDocument(document);
	}

	@Override
	public void onDocumentStored(GUIDocument document) {

	}

	@Override
	public void onDocumentCheckedIn(GUIDocument document) {
		onDocumentModified(document);
	}

	@Override
	public void onDocumentCheckedOut(GUIDocument document) {
		onDocumentModified(document);
	}

	@Override
	public void onDocumentLocked(GUIDocument document) {
		onDocumentModified(document);
	}

	@Override
	public void onDocumentUnlocked(GUIDocument document) {
		onDocumentModified(document);
	}

	@Override
	public void onDocumentsDeleted(GUIDocument[] documents) {
		removeMembers(getMembers());
	}

	@Override
	public void onDocumentMoved(GUIDocument document) {

	}

	@Override
	protected void finalize() throws Throwable {
		destroy();
	}

	@Override
	public void destroy() {
		DocumentController.get().removeObserver(this);
	}

	@Override
	protected void onUnload() {
		destroy();
		super.onUnload();
	}

	@Override
	protected void onDestroy() {
		destroy();
		super.onDestroy();
	}
}