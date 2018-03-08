package edu.core.gnr629.client;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.OpenLayers;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.control.LayerSwitcher;
import org.gwtopenmaps.openlayers.client.control.ModifyFeature;
import org.gwtopenmaps.openlayers.client.control.ModifyFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.OverviewMap;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.layer.TransitionEffect;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSOptions;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocol;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocolOptions;
import org.gwtopenmaps.openlayers.client.strategy.BBoxStrategy;
import org.gwtopenmaps.openlayers.client.strategy.Strategy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;



public class WFS {

       public void buildPanel() {
       
    	   System.out.println("ÖK");

        //create some MapOptions
        MapOptions defaultMapOptions = new MapOptions();
        defaultMapOptions.setNumZoomLevels(16);

        //Create a MapWidget
        MapWidget mapWidget = new MapWidget("500px", "500px", defaultMapOptions);
        //Create a WMS layer as base layer
        WMSParams wmsParams = new WMSParams();
        wmsParams.setFormat("image/png");
        wmsParams.setLayers("topp:tasmania_state_boundaries");
        wmsParams.setStyles("");

        WMSOptions wmsLayerParams = new WMSOptions();
        wmsLayerParams.setUntiled();
        wmsLayerParams.setTransitionEffect(TransitionEffect.RESIZE);

        String wmsUrl = "http://localhost:8080/geoserver/wms";

        WMS wmsLayer = new WMS("Basic WMS", wmsUrl, wmsParams, wmsLayerParams);

        //Add the WMS to the map
        Map map = mapWidget.getMap();
        map.addLayer(wmsLayer);

        //Create a WFS layer
        WFSProtocolOptions wfsProtocolOptions = new WFSProtocolOptions();
        wfsProtocolOptions.setUrl("http://localhost:8080/geoserver/wfs");
        wfsProtocolOptions.setFeatureType("tasmania_roads");
        wfsProtocolOptions.setFeatureNameSpace("topp");
        //if your wms is in a different projection use wfsProtocolOptions.setSrsName(LAMBERT72);

        WFSProtocol wfsProtocol = new WFSProtocol(wfsProtocolOptions);

        VectorOptions vectorOptions = new VectorOptions();
        vectorOptions.setProtocol(wfsProtocol);
        vectorOptions.setStrategies(new Strategy[]{new BBoxStrategy()});
        //if your wms is in a different projection use vectorOptions.setProjection(LAMBERT72);



        Vector wfsLayer = new Vector("wfsExample", vectorOptions);
        map.addLayer(wfsLayer);

        //Create some styles for the wfs
        final Style normalStyle = new Style(); //the normal style
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

        map.addControl(modifyFeatureControl);
        modifyFeatureControl.activate();

        /*
         * Note that for saving back to the WFS you can use
         * final SaveStrategy saveStrategy = new SaveStrategy();
         * saveStrategy.setAuto(true);
         * vectorOptions.setStrategies(new Strategy[] {new BBoxStrategy(), saveStrategy }); // (instead of only BBOXStrategy)
         */

        //Lets add some default controls to the map
        map.addControl(new LayerSwitcher()); //+ sign in the upperright corner to display the layer switcher
        map.addControl(new OverviewMap()); //+ sign in the lowerright to display the overviewmap
        map.addControl(new ScaleLine()); //Display the scaleline

        //Center and zoom to a location
        map.setCenter(new LonLat(146.7, -41.8), 6);
  
        mapWidget.getElement().getFirstChildElement().getStyle().setZIndex(0); //force the map to fall behind popups
    }
}
 
    


