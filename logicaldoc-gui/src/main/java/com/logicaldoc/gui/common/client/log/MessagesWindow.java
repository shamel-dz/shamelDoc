package com.logicaldoc.gui.common.client.log;

import com.logicaldoc.gui.common.client.beans.GUIEvent;
import com.logicaldoc.gui.common.client.i18n.I18N;
import com.logicaldoc.gui.common.client.util.AwesomeFactory;
import com.logicaldoc.gui.common.client.util.LD;
import com.logicaldoc.gui.common.client.widgets.grid.DateListGridField;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Shows the messages list
 * 
 * @author Marco Meschieri - LogicalDOC
 * @since 6.0
 */
public class MessagesWindow extends Window {

	private static final String DETAIL = "detail";

	private static final String SEVERITY = "severity";

	private static MessagesWindow instance = new MessagesWindow();

	private ListGrid grid;

	public MessagesWindow() {
		super();

		HeaderControl trash = new HeaderControl(HeaderControl.TRASH, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				grid.setData(new ListGridRecord[0]);
			}
		});

		setHeaderControls(HeaderControls.HEADER_LABEL, trash, HeaderControls.CLOSE_BUTTON);
		setTitle(AwesomeFactory.getIconHtml("clipboard-list", "lastevents"));
		setCanDragResize(true);
		setIsModal(true);
		setShowModalMask(true);
		setWidth(580);
		setHeight(300);
		centerInPage();

		grid = new ListGrid() {

			@Override
			protected String getCellCSSText(ListGridRecord rec, int rowNum, int colNum) {
				if (GUIEvent.ERROR.equals(rec.getAttribute(SEVERITY)))
					return "color: #EF4A4A";
				if (GUIEvent.WARNING.equals(rec.getAttribute(SEVERITY)))
					return "color: #FF8723";
				else
					return "color: #577ED0";
			}
		};
		grid.setEmptyMessage(I18N.message("notitemstoshow"));
		grid.setWidth100();
		grid.setHeight100();
		grid.setCanReorderFields(false);
		grid.setCanFreezeFields(false);
		grid.setCanGroupBy(false);

		ListGridField date = new DateListGridField("date", "date");

		ListGridField detail = new ListGridField(DETAIL, I18N.message(DETAIL));
		detail.setWidth("*");
		detail.setCanSort(false);

		ListGridField severityLabel = new ListGridField("severityLabel", I18N.message(SEVERITY), 80);

		grid.setFields(date, severityLabel, detail);
		grid.setCanResizeFields(true);
		addItem(grid);
		
		
		grid.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				LD.askForValue(I18N.message(DETAIL), I18N.message(DETAIL),
						grid.getSelectedRecord().getAttributeAsString(DETAIL), new ValueCallback() {
							@Override
							public void execute(final String value) {
								// Nothing to do
							}
						});	
			}
		});
	}

	public void addEvent(GUIEvent event) {
		ListGridRecord rec = new ListGridRecord();
		rec.setAttribute("date", event.getDate());
		rec.setAttribute(DETAIL, event.getDetail());
		rec.setAttribute(SEVERITY, event.getSeverity());
		rec.setAttribute("severityLabel", I18N.message(event.getSeverity()));
		grid.addData(rec);
		grid.sort("date", SortDirection.DESCENDING);
	}

	public static MessagesWindow get() {
		return instance;
	}
}