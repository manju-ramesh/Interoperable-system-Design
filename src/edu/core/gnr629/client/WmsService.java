package edu.core.gnr629.client;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.control.WMSGetFeatureInfo;
import org.gwtopenmaps.openlayers.client.control.WMSGetFeatureInfoOptions;
import org.gwtopenmaps.openlayers.client.event.GetFeatureInfoListener;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.gwtopenmaps.openlayers.client.layer.WMSOptions;
import org.gwtopenmaps.openlayers.client.layer.WMSParams;
import org.gwtopenmaps.openlayers.client.LonLat;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
public class WmsService {
	

	 private static final Projection DEFAULT_PROJECTION = new Projection(
		       "EPSG:4326");
	private FlexTable grid = new FlexTable();
	    final ListBox serverListbox = new ListBox();
	    final ListBox wmsRequestListbox = new ListBox();
	    final ListBox wmsLayersListbox = new ListBox();
	    final ListBox CRSListbox = new ListBox();
	    final ListBox FormatListbox = new ListBox();
	    final ListBox trekList =new ListBox();
		final TextBox minX = new TextBox();
		final TextBox minY = new TextBox();
		final TextBox maxX = new TextBox();
		final TextBox maxY = new TextBox();
		NodeList FORMATNode ;
		final Button submit = new Button();

		 String Url=new String();
		 private WMS wmsLayer;
		   WMSParams wmsParams = new WMSParams();
		private String xmlResponse = new String();
		
		
	public Widget wmstab(final Map map,final MapWidget mapWidget) {
		// WMS tab initial GUI
		  
			/*
			Bounds bWMS = map.getExtent();
			minX.setText(String.valueOf(bWMS.getLowerLeftX()));
			minY.setText(String.valueOf(bWMS.getLowerLeftY()));
			maxX.setText(String.valueOf(bWMS.getUpperRightX()));
			maxY.setText(String.valueOf(bWMS.getUpperRightY()));
			*/
			
			
			//final Button submit = new Button();
			submit.setSize("100px", "40px");
			submit.setText("Submit");
							
			grid.setBorderWidth(0);
			grid.setHTML(0,0,"Server:");
			grid.setWidget(0, 1, serverListbox);
			grid.setHTML(1,0,"Request");
			grid.setWidget(1, 1, wmsRequestListbox);
			grid.setHTML(2,0,"Layers");
			grid.setWidget(2, 1, wmsLayersListbox);
			grid.setHTML(3,0,"CRS");
			grid.setWidget(3, 1, CRSListbox);
			grid.setHTML(4,0,"Format");
			grid.setWidget(4, 1, FormatListbox);
			grid.setHTML(5,0,"MinX");
			grid.setHTML(5,1,"MinY");
			grid.setWidget(6,0,minX);
			grid.setWidget(6,1,minY);
			grid.setHTML(7,0,"MaxX");
			grid.setHTML(7,1,"MaxY");
			grid.setWidget(8,0,maxX);
			grid.setWidget(8,1,maxY);
			grid.setWidget(9, 1,submit);
			grid.setHTML(10,0,"Format");
			grid.setWidget(10, 1, trekList);

			//Url="http://localhost:8080/geoserver/wms";
		    final VerticalPanel wmsPanel = new VerticalPanel();
		    wmsPanel.setSize("300px", "400px");
		    wmsPanel.add(grid);
		    
		    	
		    final AbsolutePanel xmlpanel = new AbsolutePanel();
		    xmlpanel.add(new HTML("XML RESPONSE PANEL"),200,1);
		    xmlpanel.setStyleName("gwt-AbsolutePanel");
		    xmlpanel.setSize("500px", "400px");
		    wmsPanel.add(xmlpanel);
		    
		    serverListbox.addItem("Select Server");
		    serverListbox.addItem("http://localhost:8080/geoserver/wms");
		    serverListbox.addItem("External");
		    serverListbox.addChangeHandler(new ChangeHandler() {
 
		        @Override
		        public void onChange(ChangeEvent event) {
		        	  wmsRequestListbox.clear();
		           wmsLayersListbox.clear();
		      	   CRSListbox.clear();
		      	    FormatListbox.clear();
		        	  
		        	xmlResponse= onChangeServerWMS(serverListbox);
		           
		        }
           
				public String onChangeServerWMS(ListBox lb) {
					Url=lb.getValue(lb.getSelectedIndex());
					 RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "http://localhost:8080/geoserver/wms?request=getCapabilities");//"http://localhost:8080/geoserver/wms?request=getCapabilities");
			      		//"http://neowms.sci.gsfc.nasa.gov/wms?request=getCapabilities"
			      		try {
			      	      builder.sendRequest(null,new RequestCallback() {

			      	        public void onError(Request request, Throwable exception) {
			      	        	//Window.alert("Error Occured while sending requset");
			      	        }

			      	        public void onResponseReceived(Request request, Response response) {
			      	            if (200 == response.getStatusCode()) {
			      	                // Process the response in response.getText()
			      				 xmlResponse = response.getText();
				 		        	xmlpanel.clear();
						       	ScrollPanel scroller = new ScrollPanel((new HTML(xmlResponse)));
						        								    scroller.setSize("500px", "400px");
								    xmlpanel.add(new HTML("XML RESPONSE"),200,1);
								    xmlpanel.add(scroller,10,20);
								    scroller.addStyleName("gwt-ScrollPanel");
								    wmsPanel.add(xmlpanel);
			      					try {
			      					    // parse the XML document into a DOM
			      					    Document messageDom = XMLParser.parse(xmlResponse);
			      					    // populate the list with requests name
			      					    Node r = messageDom.getElementsByTagName("Request").item(0);
			      					    NodeList requests = (NodeList)r.getChildNodes();
			      					    for(int i=0;i<requests.getLength() 	;i++){
			      					    	if(requests.item(i).getNodeType() == Node.ELEMENT_NODE){
			      					    		wmsRequestListbox.addItem(requests.item(i).getNodeName());	
			      					    	}
			      					    }
			      					    // populate the list with the layers name
			      					    NodeList layers = messageDom.getElementsByTagName("Layer");
			      					    for(int i=1;i<layers.getLength();i++){
			      					    	Node layerNameNode = ((Element)layers.item(i)).getElementsByTagName("Name").item(0);
			      					    	String layerName = layerNameNode.getFirstChild().getNodeValue();
			      					    	    					    	
			      					    	      					    	 
			      					    	   
			      					    	 wmsLayersListbox.addItem(layerName);
			      					    }
			      						      					     					    
			      							   						
			      					  } catch (DOMException e) {
			      						  System.out.println("Could not parse XML document.");
			      					  }				
			      	            } else {
			      	              // Handle the error.  Can get the status text from response.getStatusText()
			      	            	//resp.setText("Error occured"+response.getStatusCode());
			      	            	System.out.println("response is not received");
			      	            }
			      	        }
			      	      });
			      	    } catch (RequestException e) {
			      	      System.out.println("Failed to send the request: " + e.getMessage());
			      	    }	
			      		return xmlResponse;
				}
				
				//lb.getValue(lb.getSelectedIndex()) 
			      	 
				

					
					// When server changes
					
					// lb.getValue(lb.getSelectedIndex()) contains the value of the server 
					// Create url for the get capabilities of this server
					
					//lb.getValue(lb.getSelectedIndex())+"?request=getCapabilities");//"http://localhost:8080/geoserver/wms?request=getCapabilities"
		  //		//"http://localhost:8080/geoserver/wms?request=getCapabilities");
										
					// send the get capab request
					
					//
					
		    });
		    
		   wmsRequestListbox.addChangeHandler(new ChangeHandler(){
		    	
		   
		    		  @Override
				        public void onChange(ChangeEvent event) {
		    			   //CRSListbox.clear();
		    			   FormatListbox.clear();
				            onChangeRequestWMS(wmsRequestListbox,xmlResponse);
				           
				        }
		           
						public void onChangeRequestWMS(ListBox lb,String xmlResponse) { 
												
							
							//   Document messageDom = XMLParser.parse(xmlResponse);
	      					    // populate the list with requests name
	      					  // Node r = messageDom.getElementsByTagName("Request").item(0);
	      					   // NodeList requests = (NodeList)r.getChildNodes();
	      					   // for(int i=0;i<requests.getLength() 	;i++){
	      					    //	if(requests.item(i).getNodeType() == Node.ELEMENT_NODE){
	      					    	//	wmsRequestListbox.addItem(requests.item(i).getNodeName());	
	      					    //	}
	      					   // }
							
							try {
							   Document messageDom = XMLParser.parse(xmlResponse);
	      					  // populate the list with requests name
	      					  Node r = messageDom.getElementsByTagName("Request").item(0);
	      					   NodeList requests = (NodeList)r.getChildNodes();
	      					   
	      					
	      					 
	      					 for(int k =0;k<requests.getLength();k++)
	      					 {
	      						if(lb.getValue(lb.getSelectedIndex())==requests.item(k).getNodeName())
	      						{
	      							FORMATNode= requests.item(k).getChildNodes();
	      						}
	      						}
	      						 
	      					 
     					    	
     					    	  	//	trekList.addItem
     					    	// trekList.addItem(String.valueOf(lb.getSelectedIndex()));
   					    		 //trekList.addItem(String.valueOf(CSRNode.getLength()));

   	      					 trekList.addItem(String.valueOf(FORMATNode.getLength()));
     					    		   		 for( int j=0;j<FORMATNode.getLength();j++)
     					    		     					
     					    		   		 {
     					    		   	//	 trekList.addItem(requests.item(j).getFirstChild().getNodeValue());					    		   			 
     					    		   		 if(FORMATNode.item(j).getNodeName()!="#text")
     					    		   		 
     					    		   		 {
     					    		   			
     					    		   		 if(FORMATNode.item(j).getNodeName()=="Format")
     					    		   		 {
     					    		   			FormatListbox.addItem(FORMATNode.item(j).getFirstChild().getNodeValue());
     					    		   		 }
     					    		   		 }
     					    	  			
     					    		    				    		  					    		 
     					    	  		//	FormatListbox.addItem(FORMATNode.item(j).getFirstChild().getNodeValue());
     					    	  		 //else
     					    	  			// trekList.addItem(FORMATNode.item(j).getNodeName());
     					    		      					    	  		 
     					    		   		 }
							    					    	
							}
							catch (DOMException e) {
	      						  System.out.println("Could not parse XML document.");
	      					  }	
	
						}
						
		    });
		    
		   wmsLayersListbox.addChangeHandler(new ChangeHandler(){
		    	
				   
	    		  @Override
			        public void onChange(ChangeEvent event) {
	    			   CRSListbox.clear();
			            onChangeLayerWMS(wmsLayersListbox,xmlResponse);
			           
			        }
	           
					public void onChangeLayerWMS(ListBox lb,String xmlResponse) { 
											
						 Document messageDom = XMLParser.parse(xmlResponse);
						 NodeList layers = messageDom.getElementsByTagName("Layer");
    					        					    	    					    	
					    		 NodeList CSRNode=((Element)layers.item(lb.getSelectedIndex())).getChildNodes();
					    		 wmsParams.setLayers(lb.getValue(lb.getSelectedIndex()));
					    		 for( int j=0;j<CSRNode.getLength();j++)
					    		 {
					    			 
					    	  		 if(CSRNode.item(j).getNodeName()=="CRS")
					    	    				    		 
					    	  			CRSListbox.addItem(String.valueOf(CSRNode.item(j).getFirstChild().getNodeValue()));
					    		 
					    	  		 if( CSRNode.item(j).getNodeName().matches("EX_GeographicBoundingBox"))
					    	  		 {
					    	  			 NodeList X=CSRNode.item(j).getChildNodes();
					    	  			  trekList.addItem(String.valueOf(X.getLength()));
					    	  		 for(int s=0;s<X.getLength();s++)	
					    	  			 {
					    	  				 if(X.item(s).getNodeName().matches("westBoundLongitude"))
					    	  					minX.setText(String.valueOf(X.item(s).getFirstChild().getNodeValue()));
					    	  				 if(X.item(s).getNodeName().matches("eastBoundLongitude"))
					    	  					maxX.setText(String.valueOf(X.item(s).getFirstChild().getNodeValue()));
					    	  				if(X.item(s).getNodeName().matches("southBoundLatitude"))
					    	  					maxY.setText(String.valueOf(X.item(s).getFirstChild().getNodeValue()));
					    	  				if(X.item(s).getNodeName().matches("northBoundLatitude"))
					    	  					minY.setText(String.valueOf(X.item(s).getFirstChild().getNodeValue()));
					    	  				
								    	  			 }
					    	  		 }
					    	  	//	if(CSRNode.item(j).getNodeName()=="FORMAT")
					    	  		//	FormatListbox.addItem(String.valueOf(CSRNode.item(j).getFirstChild().getNodeValue()));
					    		 }
										
					    	
					
					}

					    });    
		    	
		   
		    
		    submit.addClickHandler(new ClickHandler(){
		    	public void onClick(ClickEvent event) {
					
					onClickSubmitButton(submit);
				           
				        }
					//Bounds bWMS = new Bounds(Double.parseDouble(minX.getValue()),Double.parseDouble(minY.getValue()),Double.parseDouble(maxX.getValue()),Double.parseDouble(maxY.getValue()));
					//map.zoomToExtent(bWMS);
					
					 public void onClickSubmitButton(Button s)
					 {
					int itemIndex = wmsRequestListbox.getSelectedIndex();
					if(wmsRequestListbox.getItemText(itemIndex).contains("GetCapabilities")){
						
					}else if(wmsRequestListbox.getItemText(itemIndex).contains("GetMap")){
						WMSParams wmsParams = new WMSParams();
						wmsParams.setFormat("image/png");
						String layerName = wmsLayersListbox.getItemText(wmsLayersListbox.getSelectedIndex());
						wmsParams.setLayers(layerName);
						wmsParams.setTransparent(true);
						wmsParams.setStyles("");
						//String crs = CRSListbox.getItemText(CRSListbox.getSelectedIndex());
						
						WMSOptions wmsLayerParams = new WMSOptions();
						wmsLayerParams.setUntiled();
						//wmsLayerParams.setTransitionEffect(TransitionEffect.RESIZE);
						wmsLayerParams.setDisplayOutsideMaxExtent(true);
						wmsLayerParams.setIsBaseLayer(false);
						wmsLayerParams.setLayerOpacity(1.0);
						//wmsLayerParams.setMaxExtent(bBox);
						wmsLayerParams.setProjection(map.getBaseLayer().getProjection().toString());
						//wmsLayerParams.setProjection(crs);
						String wmsUrl = serverListbox.getItemText(serverListbox.getSelectedIndex());
						WMS wmsLayer = new WMS(layerName, wmsUrl, wmsParams,wmsLayerParams);

						map.addLayer(wmsLayer);	
						
						
						/* for centre display of map
						Double lon = (Double.parseDouble(minX.getText()) + Double.parseDouble(maxX.getText()))/2;
						Double lat = (Double.parseDouble(minY.getText()) + Double.parseDouble(maxY.getText()))/2;
						LonLat lonLat1 = new LonLat(lon, lat);				
						//Need to center map here
						
						//LonLat lonLat1 = new LonLat(minX.getText(), 19.0760);
				        lonLat1.transform(DEFAULT_PROJECTION.getProjectionCode(),
				                     map.getProjection());
						
				        map.setCenter(lonLat1, 5);
						*/

						//wmsGetFeatureInfoOptions.setLayers(new WMS[]{wmsLayer});
					} else if(wmsRequestListbox.getItemText(itemIndex).contains("GetFeatureInfo")){						       
				        
						WMSParams wmsParams = new WMSParams();
						wmsParams.setFormat("image/png");
						String layerName = wmsLayersListbox.getItemText(wmsLayersListbox.getSelectedIndex());
						wmsParams.setLayers(layerName);
						wmsParams.setTransparent(true);
						wmsParams.setStyles("");
						
						//float minx = Float.parseFloat(minX.getText()); 
						//float miny = Float.parseFloat(minY.getText());
						//float maxx = Float.parseFloat(maxX.getText());
						//float maxy = Float.parseFloat(maxY.getText());
						//Bounds bBox = new Bounds(minx,miny,maxx,maxy);
						
						WMSOptions wmsLayerParams = new WMSOptions();
						wmsLayerParams.setUntiled();
						//wmsLayerParams.setTransitionEffect(TransitionEffect.RESIZE);
						wmsLayerParams.setDisplayOutsideMaxExtent(true);
						wmsLayerParams.setIsBaseLayer(false);
						wmsLayerParams.setLayerOpacity(1.0);
						//wmsLayerParams.setMaxExtent(bBox);
						wmsLayerParams.setProjection(map.getBaseLayer().getProjection().toString());
						String wmsUrl = serverListbox.getItemText(serverListbox.getSelectedIndex());
						WMS wmsLayer = new WMS(layerName, wmsUrl, wmsParams,wmsLayerParams);
						map.addLayer(wmsLayer);
						
						
						 WMSGetFeatureInfoOptions wmsGetFeatureInfoOptions = new WMSGetFeatureInfoOptions();
					        wmsGetFeatureInfoOptions.setMaxFeaturess(50);
					        wmsGetFeatureInfoOptions.setLayers(new WMS[]{wmsLayer});
					        wmsGetFeatureInfoOptions.setDrillDown(true);
					        //to request a GML string instead of HTML : wmsGetFeatureInfoOptions.setInfoFormat(GetFeatureInfoFormat.GML.toString());
					 
					        WMSGetFeatureInfo wmsGetFeatureInfo = new WMSGetFeatureInfo(wmsGetFeatureInfoOptions);
					 
					       wmsGetFeatureInfo.addGetFeatureListener(new GetFeatureInfoListener()
					       
					       {
					            public void onGetFeatureInfo(GetFeatureInfoEvent eventObject) 
					            {
					                //if you did a wmsGetFeatureInfoOptions.setInfoFormat(GetFeatureInfoFormat.GML.toString()) you can do a VectorFeature[] features = eventObject.getFeatures(); here
					            	DialogBoxWithCloseButton db = new DialogBoxWithCloseButton();						            
					            	HTML html = new HTML(eventObject.getText());
					               db.setWidget(html);
					               db.center(); 
					               }
					           
					       });
					        map.addControl(wmsGetFeatureInfo);
					        wmsGetFeatureInfo.activate();
					        mapWidget.getElement().getFirstChildElement().getStyle().setZIndex(0);
					        wmsGetFeatureInfoOptions.setDrillDown(false);

					 }
		    }
					        });
					    
	

			// more WMS layers
	// lets create WMS base map layer
	            
	        
	      	       	
		return wmsPanel;
	}

}