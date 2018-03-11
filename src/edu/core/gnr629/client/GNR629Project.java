package edu.core.gnr629.client;

/*
 *  make sure you change this according to your package name
 */

/**
 * Make sure you have the gwt-openlayers library that is available at 
 * http://sourceforge.net/projects/gwt-openlayers/files/gwt-openlayers/
 */
/**
 * 1. put this line in YOURPROJECTNAME.gwt.xml (look for it in the src folder and under your project
 * package name. Open it with the builtin text editor (right click on the .xml file and then openwith ...): 
 * <inherits name='org.gwtopenmaps.openlayers.OpenLayers'/>
 * 
 * 2.put this in the HTMl file <script src="http://openlayers.org/api/2.11/OpenLayers.js"></script>
 * 3. put this in the html file <script src="http://maps.google.com/maps/api/js?v=3&sensor=false"></script>
 * After this step you are ready to use the gwt-openlayers library.
 * 
 */

import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.gwtopenmaps.openlayers.client.control.OverviewMap;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.control.LayerSwitcher;
import org.gwtopenmaps.openlayers.client.control.MouseDefaults;
import org.gwtopenmaps.openlayers.client.control.PanZoomBar;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;

import org.gwtopenmaps.openlayers.client.util.JObjectArray;
import org.gwtopenmaps.openlayers.client.util.JSObject;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3MapType;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3Options;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import com.google.gwt.user.client.ui.TabPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GNR629Project implements EntryPoint {
	
	
	   private static final Projection DEFAULT_PROJECTION = new Projection(
       "EPSG:4326");
	
	 MapWidget mapWidget;
	 Map map;
	 ListBox wmsLayersList=new ListBox();
	 ListBox wmsOperationsList= new ListBox();
	 FlexTable grid;
	 ListBox wfsOperationsList=new ListBox();
	 
	 ListBox wmsCRSList =new ListBox();
	 ListBox trekList= new ListBox();;
	 ListBox featuresList=new ListBox();
	 MapOptions mapOptions = new MapOptions(); 
	
	/**
	 * This is the entry point method.
	 */
	
	public void onModuleLoad() {
		
		mapOptions.setControls(new JObjectArray(new JSObject[] {}));
		mapOptions.setNumZoomLevels(15);
		mapOptions.setProjection("EPSG:4326");
		// let’s create map widget and map objects
		mapWidget = new MapWidget("900px", "600px", mapOptions);
		map = mapWidget.getMap();
		
		WmsService wmsOb =new WmsService();
		WfsService wfsOb=new WfsService();
		WcsServices wcsOb=new WcsServices();
		
		
		   GoogleV3Options gNormalOptions = new GoogleV3Options();
	        gNormalOptions.setIsBaseLayer(true);
	        gNormalOptions.setType(GoogleV3MapType.G_NORMAL_MAP);
	        GoogleV3 gNormal = new GoogleV3("Google Normal", gNormalOptions);
	      	        
   // adding google Base Map layer on map ...............................................................................//
	    
	        map.addLayer(gNormal);
	        gNormal.setIsVisible(true);
	        
	        map.addControl(new LayerSwitcher()); 
	        map.addControl(new OverviewMap()); 
	        map.addControl(new ScaleLine()); 
	        map.addControl(new MouseDefaults());
			map.addControl(new PanZoomBar());
			
			LonLat lonLat1 = new LonLat(78.6569, 22.9734);
	        lonLat1.transform(DEFAULT_PROJECTION.getProjectionCode(),
	                       map.getProjection());
	        map.setCenter(lonLat1, 4);
		
						
		DockPanel dockPanel = new DockPanel();
	   dockPanel.setStyleName("cw-DockPanel");
	    dockPanel.setSpacing(4);
	    dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
	    dockPanel.add(new HTML("Interoperatable Web Service Client"), DockPanel.NORTH);
		dockPanel.setBorderWidth(5);
	            
	    // Create the tab panels for wms, wcs, wfs
	    TabPanel tabs = new TabPanel();
	    
	    
	    tabs.add(wmsOb.wmstab(map,mapWidget), "WMS");
	    tabs.add(wfsOb.wfstab(map), "WFS");
	    tabs.add(wcsOb.wcstab(map), "WCS");
	    tabs.setWidth("100%");
	    tabs.selectTab(0);
	    
	    //Add the tabs to the main dock panel
	    dockPanel.add(tabs, DockPanel.WEST);
	    
	    //Add the map widget to the main dock panel
	    dockPanel.add(mapWidget, DockPanel.NORTH);
   
	    RootPanel.get().add(dockPanel);
	    
	}
	}