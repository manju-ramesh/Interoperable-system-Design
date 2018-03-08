package edu.core.gnr629.client;

import org.gwtopenmaps.openlayers.client.Map;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class WfsService {
	
	public Widget wfstab(Map map) {
		 AbsolutePanel wfsPanel = new AbsolutePanel();
		 FlexTable grid = new FlexTable();

		    wfsPanel.setSize("500px", "430px");
		    wfsPanel.add(grid, 10,10);
		    return wfsPanel;
	}
}
