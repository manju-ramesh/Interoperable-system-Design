package edu.core.gnr629.client;

import org.gwtopenmaps.openlayers.client.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class WfsService {
	
	
		final FlexTable grid = new FlexTable();
	    final ListBox serverListbox = new ListBox();
	    final ListBox wfsRequestListbox = new ListBox();
	    final ListBox wfsLayersListbox = new ListBox();
	    final ListBox CRSListbox = new ListBox();
	    final ListBox fieldListBox = new ListBox();
		final TextBox fieldValue = new TextBox();
		final TextBox minX = new TextBox();
		final TextBox minY = new TextBox();
		final TextBox maxX = new TextBox();
		final TextBox maxY = new TextBox();
		String xmlResponse = new String();
		public Widget wfstab(Map map) {
		// wfs tab initial GUI
		  
			/*
			Bounds bwfs = map.getExtent();
			minX.setText(String.valueOf(bwfs.getLowerLeftX()));
			minY.setText(String.valueOf(bwfs.getLowerLeftY()));
			maxX.setText(String.valueOf(bwfs.getUpperRightX()));
			maxY.setText(String.valueOf(bwfs.getUpperRightY()));
			*/
			
			
			final Button submit = new Button();
			submit.setSize("100px", "40px");
			submit.setText("Submit");
			
					
			grid.setBorderWidth(0);
			grid.setHTML(0,0,"Server:");
			grid.setWidget(0, 1, serverListbox);
			grid.setHTML(1,0,"Request");
			grid.setWidget(1, 1, wfsRequestListbox);
			grid.setHTML(2,0,"Layers");
			grid.setWidget(2, 1, wfsLayersListbox);
			grid.setHTML(3,0,"Field");
			grid.setWidget(3, 1, fieldListBox);
			grid.setHTML(4,0,"Value");
			grid.setWidget(4, 1, fieldValue);
			grid.setHTML(5,0,"MinX");
			grid.setHTML(5,1,"MinY");
			grid.setWidget(6,0,minX);
			grid.setWidget(6,1,minY);
			grid.setHTML(7,0,"MaxX");
			grid.setHTML(7,1,"MaxY");
			grid.setWidget(8,0,maxX);
			grid.setWidget(8,1,maxY);
			grid.setWidget(9, 1,submit);

		    VerticalPanel wfsPanel = new VerticalPanel();
		    wfsPanel.setSize("300px", "400px");
		    wfsPanel.add(grid);
		    
		    	
		    AbsolutePanel xmlpanel = new AbsolutePanel();
		    xmlpanel.add(new HTML("XML RESPONSE PANEL"),200,1);

	    	xmlpanel.setStyleName("gwt-AbsolutePanel");
	    	xmlpanel.setSize("500px", "400px");
		    wfsPanel.add(xmlpanel);
		    
		    serverListbox.addItem("Select Server");
		    serverListbox.addItem("GEOSERVER/WFS");
		    serverListbox.addItem("External");
		    serverListbox.addChangeHandler(new ChangeHandler() {
 
		        @Override
		        public void onChange(ChangeEvent event) {
		        	  wfsRequestListbox.clear();
		        	  wfsLayersListbox.clear();
		      	      fieldListBox.clear();
		      	      // FormatListbox.clear();
		        	  
		        	xmlResponse= onChangeServerwfs(serverListbox);
           
		        }
           
				public String onChangeServerwfs(ListBox lb) {
					 RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "http://localhost:8080/geoserver/wfs?request=getCapabilities");//"http://localhost:8080/geoserver/wfs?request=getCapabilities");
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
			      					    Node r = messageDom.getElementsByTagName("Request").item(0);
			      					    NodeList requests = (NodeList)r.getChildNodes();
			      					    for(int i=0;i<requests.getLength() 	;i++){
			      					    	if(requests.item(i).getNodeType() == Node.ELEMENT_NODE){
			      					    		wfsRequestListbox.addItem(requests.item(i).getNodeName());	
			      					    	}
			      					    }
			      					    // populate the list with the layers name
			      					    NodeList layers = messageDom.getElementsByTagName("Layer");
			      					    for(int i=1;i<layers.getLength();i++){
			      					    	Node layerNameNode = ((Element)layers.item(i)).getElementsByTagName("Name").item(0);
			      					    	String layerName = layerNameNode.getFirstChild().getNodeValue();
			      					    	    					    	
			      					    	      					    	 
			      					    	   
			      					    	 wfsLayersListbox.addItem(layerName);
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
					
					//lb.getValue(lb.getSelectedIndex())+"?request=getCapabilities");//"http://localhost:8080/geoserver/wfs?request=getCapabilities"
		  //		//"http://localhost:8080/geoserver/wfs?request=getCapabilities");
										
					// send the get capab request
					
					//
					
		    });
		    
		    wfsLayersListbox.addChangeHandler(new ChangeHandler(){
		    	
		   
		    		  @Override
				        public void onChange(ChangeEvent event) {
		    			   CRSListbox.clear();
				            onChangeServerwfs(wfsLayersListbox,xmlResponse);
				           
				        }
		           
						public void onChangeServerwfs(ListBox lb,String xmlResponse) { 
												
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
		    
			    
		    		
		return wfsPanel;
	}
	}

