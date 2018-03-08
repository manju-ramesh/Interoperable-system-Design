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
import org.gwtopenmaps.openlayers.client.filter.FeatureIdFilter;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocol;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocolOptions;
import org.gwtopenmaps.openlayers.client.strategy.BBoxStrategy;
import org.gwtopenmaps.openlayers.client.strategy.Strategy;
import org.gwtopenmaps.openlayers.client.util.JObjectArray;
import org.gwtopenmaps.openlayers.client.util.JSObject;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3MapType;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3Options;
import org.gwtopenmaps.openlayers.client.layer.WMSOptions;
import com.google.gwt.core.client.GWT;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GNR629Project implements EntryPoint {
	
	
	   private static final Projection DEFAULT_PROJECTION = new Projection(
       "EPSG:4326");
	
	private MapWidget mapWidget;
	private Map map;
	private WMS wmsLayer,wmsLayer2,wmsLayer3,wmsLayer4;
	private WMS Getcap;
	/**
	 * This is the entry point method.
	 */
	
	public void onModuleLoad() {
		
		

		
		MapOptions mapOptions = new MapOptions();
		mapOptions.setControls(new JObjectArray(new JSObject[] {}));
		mapOptions.setNumZoomLevels(25);
		mapOptions.setProjection("EPSG:4326");
	
		// let’s create map widget and map objects
		mapWidget = new MapWidget("500px", "500px", mapOptions);	
		map = mapWidget.getMap();
		
        // Google map layers ...........................................................................................//
		     
		        GoogleV3Options gNormalOptions = new GoogleV3Options();
		        gNormalOptions.setIsBaseLayer(true);
		        gNormalOptions.setType(GoogleV3MapType.G_NORMAL_MAP);
		        GoogleV3 gNormal = new GoogleV3("Google Normal", gNormalOptions);
		      	
		        
        // adding google Base Map layer on map ...............................................................................//
		    
	        map.addLayer(gNormal);
	        gNormal.setIsVisible(true);
		       // LonLat lonLat = new LonLat(82.5, 22.5);
	        LonLat lonLat = new LonLat(-100.99, 40.73);
	        lonLat.transform(DEFAULT_PROJECTION.getProjectionCode(),map.getProjection()); 		      
	        map.setCenter(lonLat, 7);
		
		 
		
		
		 //See the various layers in geoserver under "layers preview". you should be able to see
		 // it in http://localhost:8080/geoserver. Make sure the server is running.
		 //Below is an example of adding a layer called "topp:states"
		 //Try to add more layers ( see the geoserver " layers preview" to see
		 //what other layers are available
		 // let’s create WMS map layer
		WMSParams wmsParams = new WMSParams();
		wmsParams.setFormat("image/png");
		wmsParams.setLayers("topp:states");
		
	    wmsParams.setIsTransparent(true);
	    wmsParams.setStyles("");
	    
	    WMSOptions wmsLayerParams = new WMSOptions();
	 	wmsLayerParams.setDisplayOutsideMaxExtent(true);
	    wmsLayerParams.setTransitionEffectResize();
		
		wmsLayer = new WMS ("WMS-topp:states","http://localhost:8080/geoserver/wms",wmsParams,wmsLayerParams);
		wmsLayer.setIsBaseLayer(false);	  
		
		// more WMS layers
// lets create WMS base map layer
        
        WMSParams wmsParams2 = new WMSParams();
        wmsParams2.setIsTransparent(true); // to make base layer remove transperency 
        wmsParams2.setFormat("image/png");
        wmsParams2.setLayers("topp:tasmania_state_boundaries");//Take care here
        wmsParams2.setStyles("");
           
                
        // lets create new WMS map layer
        WMSParams wmsParams3 = new WMSParams();
        wmsParams3.setIsTransparent(true);
        wmsParams3.setFormat("image/png");
        wmsParams3.setLayers("topp:tasmania_water_bodies");
        wmsParams3.setStyles("");
      
        
        
        WMSParams wmsParams4 = new WMSParams();
        wmsParams4.setIsTransparent(true);
        wmsParams4.setFormat("image/png");
        wmsParams4.setLayers("nurc:Arc_Sample");
        wmsParams4.setStyles("");
       
        
        wmsLayer2 = new WMS ("Boundaries","http://localhost:8080/geoserver/wms",wmsParams2,wmsLayerParams);
        wmsLayer3 = new WMS ("Water Bodies","http://localhost:8080/geoserver/wms",wmsParams3,wmsLayerParams);
        wmsLayer4 = new WMS ("Arc_Sample","http://localhost:8080/geoserver/wms",wmsParams4,wmsLayerParams);
	
      //  wmsLayer4.setIsVisible(true);
		// let’s add layers and controls to map
		//map.addLayers(new Layer[] {wmsLayer});
		// 2 layers
		//map.addLayers(new Layer[] {googlemap1,wmsLayer});
      map.addLayer(wmsLayer);
     //  map.addLayer(wmsLayer2);
      //  map.addLayer(wmsLayer3);
        map.addLayer(wmsLayer4);
       
     
	      
	        //Center and zoom to a location
		 
		//map.setCenter(new LonLat(-98, 40));
		// 3 layers example
		//map.addLayers(new Layer[] {googlemap1,wmsLayer2,wmsLayer3});
		// map.addControl(new OverviewMap());
		 //Center for 2 layers example
		//LonLat center = new LonLat(-100.99, 40.73); -commented by G
		 
		 //Center for 3 layers example 
		 // LonLat center = new LonLat(143.835, -39.574);
			//map.setCenter(center, 9);
			//LonLat lonLat1 = new LonLat(-100.99, 40.73);
	      //  lonLat1.transform(DEFAULT_PROJECTION.getProjectionCode(),
	        //                 map.getProjection());
	     // map.setCenter(center, 7);
		
	
		DockPanel dockPanel = new DockPanel();
		//DockPanel dockPanel2= new DockPanel();
		//dockPanel2.add(mapWidget, DockPanel.CENTER);
		//dockPanel2.setBorderWidth(50);
		dockPanel.add(mapWidget, DockPanel.CENTER);
		dockPanel.setBorderWidth(5);
		RootPanel.get().add(dockPanel);
		//RootPanel.get().add(dockPanel2);	
		
	    WFSProtocolOptions wfsProtocolOptions = new WFSProtocolOptions();
        wfsProtocolOptions.setUrl("http://127.0.0.1:8080/geoserver/wfs");
        wfsProtocolOptions.setFeatureType("states");
        wfsProtocolOptions.setFeatureNameSpace("http://www.openplans.org/topp");
        //if your wms is in a different projection use wfsProtocolOptions.setSrsName(LAMBERT72);

        WFSProtocol wfsProtocol = new WFSProtocol(wfsProtocolOptions);

        VectorOptions vectorOptions = new VectorOptions();
        vectorOptions.setProtocol(wfsProtocol);
        vectorOptions.setStrategies(new Strategy[]{new BBoxStrategy()});
        //if your wms is in a different projection use vectorOptions.setProjection(LAMBERT72);

        final Vector wfsLayer = new Vector("wfsExample", vectorOptions);
        wfsLayer.setFilter(new FeatureIdFilter(new String[]{"states.30"})); 
        map.addLayer(wfsLayer);
        
        map.addControl(new MouseDefaults());
     		map.addControl(new PanZoomBar());
     		map.addControl(new LayerSwitcher()); //+ sign in the upperright corner to display the layer switcher
     	    map.addControl(new OverviewMap()); //+ sign in the lowerright to display the overviewmap
     	    map.addControl(new ScaleLine()); //Display the scaleline
        map.setCenter(new LonLat(-100, 40), 4);
        
    /*  final Style normalStyle = new Style(); //the normal style
        normalStyle.setStrokeWidth(3);
        normalStyle.setStrokeColor("#FF0000");
        normalStyle.setFillColor("#FFFF00");
        normalStyle.setFillOpacity(0.8);
        normalStyle.setStrokeOpacity(0.8);
        final Style selectedStyle = new Style(); //the style when a feature is selected, or temporaly selected
        selectedStyle.setStrokeWidth(3);
        selectedStyle.setStrokeColor("#FFFF00");
        selectedStyle.setFillColor("#FF0000");
        selectedStyle.setFillOpacity(0.8);
        selectedStyle.setStrokeOpacity(0.8);
        final StyleMap styleMap = new StyleMap(normalStyle, selectedStyle,
                                               selectedStyle);
        wfsLayer.setStyleMap(styleMap);
        //Create a ModifyFeature control that enables WFS modification
        final ModifyFeatureOptions modifyFeatureControlOptions = new ModifyFeatureOptions();
        modifyFeatureControlOptions.setMode(ModifyFeature.RESHAPE); //options are RESHAPE, RESIZE, ROTATE and DRAG
        final ModifyFeature modifyFeatureControl = new ModifyFeature(wfsLayer,
                                                                     modifyFeatureControlOptions);

   
        modifyFeatureControl.activate();

        /*
         * Note that for saving back to the WFS you can use
         * final SaveStrategy saveStrategy = new SaveStrategy();
         * saveStrategy.setAuto(true);
         * vectorOptions.setStrategies(new Strategy[] {new BBoxStrategy(), saveStrategy }); // (instead of only BBOXStrategy)
         */

        //Lets add some default controls to the map
      
    //    mapWidget.getElement().getFirstChildElement().getStyle().setZIndex(0);
       
        //DockLayoutPanel p =new  DockLayoutPanel(Unit.EM);
      //  p.addNorth(new HTML("<p>This example shows how you can filter out a WFS layer using a Filter. In this example a FeatureIdFilter is used to only show the feature with FID states.30. You will see only a brown WFS overlay on one state."), 10);
       // dockPanel.add(new HTML(
         //               "<p>This example shows how you can filter out a WFS layer using a Filter. In this example a FeatureIdFilter is used to only show the feature with FID states.30. You will see only a brown WFS overlay on one state."),DockPanel.SOUTH);
  //          initWidget(p);  
        
    	
    //	RootPanel.get().add(p);
	}
	
	
      
        public String getSourceCodeURL() 
        {
            return GWT.getModuleBaseURL() + "examples/filter/"
                  + "FeatureIdFilterExample.txt";
        }
      	
		 /*After running it, you will get a URL such as : 
		  * 
		  * 1. http://127.0.0.1:8888/GNR4022013.html?gwt.codesvr=127.0.0.1:9997
		  * copy it and paste it in your chrome or firefox browser. When you run it first time
		  * it will prompt for installing a GWT plugin, accept it and install it. and run again the above url
		  * wait for a few seconds you should see the map! Right now do not worry about the perfect Alignment of the 
		  * Google map with the other layers. This is because Google layer works only in the spherical mercator projection.
		  * 2. Click on the zoom slider to see the  the map layers
		  * 3. Click on the small + buttons on the right side to enable the layer switcher.
		  * 4. Check where you can set the extent and center parameters to align the map to the full extent
		  * 5. Make sure to add this to HTML file if you have not added before <script src="http://maps.google.com/maps/api/js?v=3&sensor=false"></script>
		  */	
		

	}