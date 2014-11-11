package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.Visualization;
import de.fraunhofer.iao.muvi.managerweb.domain.VisualizationDataset;

public class ShowDashboard {
	private static final Log log = LogFactory.getLog(ShowLargeImage.class);
	
	private ShowURLOnScreen showURLOnScreen;
	private Database database;
	
	/**
	 * Will extract the visualizations from the visml and then generate required URLs 
	 * that will be used to display the visualization on the wall
	 * 
	 * @author anwar
	 * @param URL to VISML
	 * @return mapping of screenID and url
	 */
	public Map<ScreenID, URL> getMapForDashboard(URL visml) {
		Map<ScreenID, URL> map = new HashMap<>();
		Map<String, Visualization> visualizationMap = new HashMap<>();
		int n=7;
		// get the XML file from the URL
		
		try {
			URLConnection connection = visml.openConnection();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(connection.getInputStream());
			
			//Normalize the XML (recommended)
			doc.getDocumentElement().normalize();
			
			NodeList visualizations = doc.getElementsByTagName("visualization");
			NodeList dataSetNodeList = doc.getElementsByTagName("dataset");
			
			for (int visNumber = 0; visNumber < visualizations.getLength(); visNumber++) {
				 
				Node visNode = visualizations.item(visNumber);
		 		 
				if (visNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) visNode;
					NamedNodeMap attributes= eElement.getAttributes();
					String id = attributes.getNamedItem("id").getTextContent();
					String title = eElement.getElementsByTagName("title").item(0).getTextContent();
					String category = attributes.getNamedItem("category").getTextContent();
					String type = attributes.getNamedItem("type").getTextContent();
					String dataset = attributes.getNamedItem("dataset").getTextContent();
					Visualization visualization = new Visualization( id,  dataset,  category,  title, type);
									
					visualizationMap.put(visualization.getDataSetId(), visualization);
					
				}
				
			}
			for (int datasetnumber = 0; datasetnumber < dataSetNodeList.getLength(); datasetnumber++) {
				 
				Node dataSetNode = dataSetNodeList.item(datasetnumber);
		 		if (dataSetNode.getNodeType() == Node.ELEMENT_NODE) {	
					Element dataSetElement = (Element) dataSetNode;
					NamedNodeMap attributes= dataSetElement.getAttributes();
					VisualizationDataset dataset = new VisualizationDataset(attributes.getNamedItem("id").getTextContent());
					
					if(dataSetElement.getElementsByTagName("parameter").item(0)!=null){
						dataset.setParameter(dataSetElement.getElementsByTagName("parameter").item(0).getTextContent());
					}
					if(dataSetElement.getElementsByTagName("bucket").getLength()>0){
						NodeList buckets = dataSetElement.getElementsByTagName("bucket");
						for (int bucketNumber = 0; bucketNumber < buckets.getLength(); bucketNumber++) {
							Node bucketsNode = buckets.item(bucketNumber);
							Element bucket = (Element) bucketsNode;
							dataset.addBucket(bucket.getTextContent());
							
						}
					}
					Visualization visualization = visualizationMap.get(dataset.getId());
					if(visualization!=null) {
						visualization.setDataset(dataset);
						visualization.init();
						map.put(new ScreenID(n), visualization.getUrl());
						n++;
					}
					
		 		}
			}
			
		} catch (Exception e) {
			log.error("Trouble reading VISML");
			log.error(e.getMessage());
		}
		
		return map;
	}
	
	
	
	
	public ShowURLOnScreen getShowURLOnScreen() {
		return showURLOnScreen;
	}

	public void setShowURLOnScreen(ShowURLOnScreen showURLOnScreen) {
		this.showURLOnScreen = showURLOnScreen;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}
