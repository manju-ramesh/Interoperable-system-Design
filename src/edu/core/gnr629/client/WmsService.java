package edu.core.gnr629.client;
import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.XMLParser;
import org.gwtopenmaps.openlayers.client.control.OverviewMap;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.control.LayerSwitcher;
import org.gwtopenmaps.openlayers.client.control.MouseDefaults;
import org.gwtopenmaps.openlayers.client.control.PanZoomBar;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.control.WMSGetFeatureInfo;
import org.gwtopenmaps.openlayers.client.control.WMSGetFeatureInfoOptions;
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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ChangeHandler;

public class WmsService {
	

	
		private FlexTable grid = new FlexTable();
	    final ListBox serverListbox = new ListBox();
	    final ListBox wmsRequestListbox = new ListBox();
	    final ListBox wmsLayersListbox = new ListBox();
	    final ListBox CRSListbox = new ListBox();
	    final ListBox FormatListbox = new ListBox();
		final TextBox minX = new TextBox();
		final TextBox minY = new TextBox();
		final TextBox maxX = new TextBox();
		final TextBox maxY = new TextBox();
		private String xmlResponse = new String();
	public Widget wmstab(Map map) {
		// WMS tab initial GUI
		  
			/*
			Bounds bWMS = map.getExtent();
			minX.setText(String.valueOf(bWMS.getLowerLeftX()));
			minY.setText(String.valueOf(bWMS.getLowerLeftY()));
			maxX.setText(String.valueOf(bWMS.getUpperRightX()));
			maxY.setText(String.valueOf(bWMS.getUpperRightY()));
			*/
			
			
			final Button submit = new Button();
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

		    VerticalPanel wmsPanel = new VerticalPanel();
		    wmsPanel.setSize("300px", "400px");
		    wmsPanel.add(grid);
		    
		    	
		    	AbsolutePanel xmlpanel = new AbsolutePanel();
			    xmlpanel.add(new HTML("XML RESPONSE PANEL"),200,1);

		    	xmlpanel.setStyleName("gwt-AbsolutePanel");
		    	xmlpanel.setSize("500px", "400px");
			    wmsPanel.add(xmlpanel);
		    
		    serverListbox.addItem("Select Server");
		    serverListbox.addItem("GEOSERVER/WMS");
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
					        	//xmlpanel.add(new HTML(xmlResponse),20,30);
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
		    
		    wmsLayersListbox.addChangeHandler(new ChangeHandler(){
		    	
		   
		    		  @Override
				        public void onChange(ChangeEvent event) {
		    			   CRSListbox.clear();
				            onChangeServerWMS(wmsLayersListbox,xmlResponse);
				           
				        }
		           
						public void onChangeServerWMS(ListBox lb,String xmlResponse) { 
												
							 Document messageDom = XMLParser.parse(xmlResponse);
							 NodeList layers = messageDom.getElementsByTagName("Layer");
	      					        					    	    					    	
     					    		 NodeList CSRNode=((Element)layers.item(lb.getSelectedIndex())).getChildNodes();
     					    		 
     					    		 for( int j=0;j<CSRNode.getLength();j++)
     					    		 {
     					    			 
     					    	  		 if(CSRNode.item(j).getNodeName()=="CRS")
     					    		 {
     					    			// trekList.addItem(String.valueOf(j));
         					    		// trekList.addItem(String.valueOf(i));
         					    		 //trekList.addItem(String.valueOf(CSRNode.getLength()));
     					    		 
     					    	  			CRSListbox.addItem(String.valueOf(CSRNode.item(j).getFirstChild().getNodeValue()));
     					    		 }
     					    		 }
											
     					    	
						
						}
		    });
		    
			    
		    		
		return wmsPanel;
	}

}
