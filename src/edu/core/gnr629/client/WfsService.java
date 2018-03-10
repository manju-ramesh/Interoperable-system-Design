package edu.core.gnr629.client;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.filter.FeatureIdFilter;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocol;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocolOptions;
import org.gwtopenmaps.openlayers.client.strategy.BBoxStrategy;
import org.gwtopenmaps.openlayers.client.strategy.Strategy;

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
import com.google.gwt.user.client.ui.CheckBox;
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
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class WfsService {
	
	
		final FlexTable grid = new FlexTable();
	    final ListBox serverListbox = new ListBox();
	    final ListBox wfsOperationsList = new ListBox();
	    final ListBox featuresList = new ListBox();
	    final CheckBox filterCheck = new CheckBox("Apply Filter");
	    final ListBox fieldListBox = new ListBox();
		final TextBox fieldValue = new TextBox();
		final TextBox minX = new TextBox();
		final TextBox minY = new TextBox();
		final TextBox maxX = new TextBox();
		final TextBox maxY = new TextBox();
		String xmlResponse = new String();
		public Widget wfstab(final Map map) {
		// wfs tab initial GUI
		  
			/*
			Bounds bwfs = map.getExtent();
			minX.setText(String.valueOf(bwfs.getLowerLeftX()));
			minY.setText(String.valueOf(bwfs.getLowerLeftY()));
			maxX.setText(String.valueOf(bwfs.getUpperRightX()));
			maxY.setText(String.valueOf(bwfs.getUpperRightY()));
			*/
			
			final String namespace = null;
			final Button submit = new Button();
			submit.setSize("100px", "40px");
			submit.setText("Submit");
			
					
			grid.setBorderWidth(0);
			grid.setHTML(0,0,"Server:");
			grid.setWidget(0, 1, serverListbox);
			grid.setHTML(1,0,"Request");
			grid.setWidget(1, 1, wfsOperationsList);
			grid.setHTML(2,0,"Feature Type");
			grid.setWidget(2, 1, featuresList);
			grid.setWidget(3,0, filterCheck);
			//grid.setWidget(3, 1, fieldListBox);
			grid.setHTML(4,0,"Fid");
			grid.setWidget(4, 1, fieldValue);
			/*
			grid.setHTML(5,0,"MinX");
			grid.setHTML(5,1,"MinY");
			grid.setWidget(6,0,minX);
			grid.setWidget(6,1,minY);
			grid.setHTML(7,0,"MaxX");
			grid.setHTML(7,1,"MaxY");
			grid.setWidget(8,0,maxX);
			grid.setWidget(8,1,maxY);
			*/
			grid.setWidget(5, 1,submit);

		    final VerticalPanel wfsPanel = new VerticalPanel();
		    wfsPanel.setSize("300px", "400px");
		    wfsPanel.add(grid);
		    
		    	
		    final AbsolutePanel xmlpanel = new AbsolutePanel();
		    xmlpanel.add(new HTML("XML RESPONSE PANEL"),200,1);

	    	xmlpanel.setStyleName("gwt-AbsolutePanel");
	    	xmlpanel.setSize("500px", "400px");
		    wfsPanel.add(xmlpanel);
		    
		    serverListbox.addItem("Select Server");
		    serverListbox.addItem("http://localhost:8080/geoserver/wfs");
		    serverListbox.addItem("https://gs.geoscience.nsw.gov.au/geoserver/ows?service=wfs&version=2.0.0");
		    
		    
		    serverListbox.addChangeHandler(new ChangeHandler() {
 
		        @Override
		        public void onChange(ChangeEvent event) {
		        	  wfsOperationsList.clear();
		        	  featuresList.clear();
		      	      fieldListBox.clear();
		      	      // FormatListbox.clear();
		        	  
		        	xmlResponse= onChangeServerwfs(serverListbox);
           
		        }
           
				public String onChangeServerwfs(ListBox lb) {
					 String url = serverListbox.getItemText(serverListbox.getSelectedIndex());
					 
					 RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url+"?request=getCapabilities");//"http://localhost:8080/geoserver/wfs?request=getCapabilities");
			      		//"http://neowfs.sci.gsfc.nasa.gov/wfs?request=getCapabilities"
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
							    wfsPanel.add(xmlpanel);
							
			      					try {
			      					    // parse the XML document into a DOM
			      					    Document messageDom = XMLParser.parse(xmlResponse);
			      					    // populate the list with requests name
			      					  NodeList requests = messageDom.getElementsByTagName("ows:Operation");

			      					    for(int i=0;i<requests.getLength() 	;i++){
			      					    	if(requests.item(i).getNodeType() == Node.ELEMENT_NODE){
			      					    	 	wfsOperationsList.addItem(((Element)requests.item(i)).getAttribute("name"));
			      					    		//wcsRequestListbox.addItem(((Element)requests.item(i)).getAttribute("name"));	
			      					    	}
			      					    }
			      					    // populate the list with the layers name
			      					  NodeList features = messageDom.getElementsByTagName("FeatureType");
			      					    //wfsLayersListbox.clear();
			  					        for(int i=0;i<features.getLength();i++){
			  					    	Node featureNode = ((Element)features.item(i)).getElementsByTagName("Name").item(0);
			  					    	String featureName = featureNode.getFirstChild().getNodeValue();
			  					    	featuresList.addItem(featureName);
			  					    	
			  					    	
			      					    	    					    	
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
				
					
		    });
		    
		    featuresList.addChangeHandler(new ChangeHandler(){
		    	
		   
				        public void onChange(ChangeEvent event) {
		    			   //CRSListbox.clear();
				            onChangeFeaturewfs(featuresList,xmlResponse,namespace);
				           
				        }
		           
						public void onChangeFeaturewfs(ListBox lb,String xmlResponse, String namespace) { 
												
							 Document messageDom = XMLParser.parse(xmlResponse);
							 NodeList layers = messageDom.getElementsByTagName("FeatureType");
							 String featureName = featuresList.getItemText(featuresList.getSelectedIndex());
							 String ns = "xmlns:"+featureName.split(":")[0];
							 namespace = ((Element)layers.item(lb.getSelectedIndex())).getAttribute(ns);

	      					        /*					    	    					    	
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
     					    		 */
											
     					    	
						
						}
		    });
		    
		    submit.addClickHandler(new ClickHandler(){
		    	public void onClick(ClickEvent event) {
					
					onClickSubmitButton(submit);
				           
				        }
		    	 public void onClickSubmitButton(Button s) {
						//Bounds bWMS = new Bounds(Double.parseDouble(minX.getValue()),Double.parseDouble(minY.getValue()),Double.parseDouble(maxX.getValue()),Double.parseDouble(maxY.getValue()));
						//map.zoomToExtent(bWMS);
						int itemIndex = wfsOperationsList.getSelectedIndex();
						if(wfsOperationsList.getItemText(itemIndex).contains("GetCapabilities")){
							
						}else if(wfsOperationsList.getItemText(itemIndex).contains("DescribeFeatureType")){
							
						} else if(wfsOperationsList.getItemText(itemIndex).contains("GetFeature")){						       
							WFSProtocolOptions wfsProtocolOptions = new WFSProtocolOptions();
							String wfsUrl = serverListbox.getItemText(serverListbox.getSelectedIndex());
							wfsProtocolOptions.setUrl(wfsUrl);
							String featureName = featuresList.getItemText(featuresList.getSelectedIndex());
							String feature = featureName.substring(featureName.indexOf(':')+1);
							wfsProtocolOptions.setFeatureType(feature);
							//wfsProtocolOptions.setFeatureNameSpace("http://www.openplans.org/topp");								
							wfsProtocolOptions.setFeatureNameSpace(namespace);
							WFSProtocol wfsProtocol = new WFSProtocol(wfsProtocolOptions);

							VectorOptions vectorOptions = new VectorOptions();
							vectorOptions.setProtocol(wfsProtocol);
							vectorOptions.setStrategies(new Strategy[] { new BBoxStrategy()});
							//vectorOptions.setProjection(map.getBaseLayer().getProjection().toString());
							
							
							Vector wfsLayer = new Vector(featureName, vectorOptions);
							if ((filterCheck.getValue()) && fieldValue.getValue()!=null) {
								String[] fid = new String[] {feature+"." +fieldValue.getValue()};
							wfsLayer.setFilter(new FeatureIdFilter(fid)); 
						    
							}
							
								
							map.addLayer(wfsLayer);
							
								
		    	 }
		    	 }});
			    
		    		
		return wfsPanel;
	}
	}