package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
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
import de.fraunhofer.iao.muvi.managerweb.domain.Text;
import de.fraunhofer.iao.muvi.managerweb.domain.Visualization;
import de.fraunhofer.iao.muvi.managerweb.domain.VisualizationDataset;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ShowDashboard {
	private static final Log log = LogFactory.getLog(ShowLargeImage.class);
	
	private ShowURLOnScreen showURLOnScreen;
	private ShowTextOnScreen showTextOnScreen;

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
			
			/* Add title line */
			String style = "background-color: rgb(23,156,125); color: white;";
			
			NodeList metas = doc.getElementsByTagName("meta");
			Element meta = (Element) metas.item(0);
			NodeList titles = meta.getElementsByTagName("title");
			Element dashboardTitle = (Element) titles.item(0);
			String titleString = dashboardTitle.getTextContent();
			
			log.debug("VisML-Dashboard title is: " + titleString);
			
			Date updateDate = new Date();
			
			map.put(new ScreenID(1), showTextOnScreen.getURLForText(new Text(titleString, style)));
			map.put(new ScreenID(2), showTextOnScreen.getURLForText(new Text(" ", style)));
			map.put(new ScreenID(3), showTextOnScreen.getURLForText(new Text("Last update:", style)));
			map.put(new ScreenID(4), showTextOnScreen.getURLForText(new Text(Utils.formatDate(updateDate), style)));
			map.put(new ScreenID(5), showTextOnScreen.getURLForText(new Text(Utils.formatTime(updateDate), style)));
			map.put(new ScreenID(6), showTextOnScreen.getURLForText(new Text(" ", style)));
						
			for (int visNumber = 0; visNumber < visualizations.getLength(); visNumber++) {
				 
				Node visNode = visualizations.item(visNumber);
		 		 
				if (visNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element visElement = (Element) visNode;
					NamedNodeMap attributes= visElement.getAttributes();
					String id = attributes.getNamedItem("id").getTextContent();
					String title = visElement.getElementsByTagName("title").item(0).getTextContent();
					String category = attributes.getNamedItem("category").getTextContent();
					String type = attributes.getNamedItem("type").getTextContent();
					String dataset = attributes.getNamedItem("dataset").getTextContent();
					Visualization visualization = new Visualization( id,  dataset,  category,  title, type);
					String formatFunction = "";
					if (!visualization.isChart()) {
						/* Look for a Javascript format function */
						NodeList parameters = visElement.getElementsByTagName("parameter");
						if (parameters != null && parameters.item(0) != null) {
							Element parameter = (Element) parameters.item(0);
							String fFunction = parameter.getTextContent();
							if (Utils.isNotEmpty(fFunction)) {
								formatFunction = fFunction;
							}
						}
					}
					visualization.setFormatFunction(formatFunction);
					visualizationMap.put(visualization.getDataSetId(), visualization);
					
				}
				
			}
			
			int textCounter=7;
			int chartCounter=19;
			
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
						visualization.init(database);
						
						if (visualization.isChart()) {
							if (chartCounter < 36) {
								map.put(new ScreenID(chartCounter), visualization.getUrl());
								chartCounter++;
							}
						} else {
							if (textCounter < 36) {
								map.put(new ScreenID(textCounter), visualization.getUrl());
								textCounter++;
							}
						}

					}
					
		 		}
			}
			
		} catch (Exception e) {
			log.error("Trouble reading VISML");
			log.error(e.getMessage());
		}
		
		return map;
	}
	
	
	public ShowTextOnScreen getShowTextOnScreen() {
		return showTextOnScreen;
	}

	public void setShowTextOnScreen(ShowTextOnScreen showTextOnScreen) {
		this.showTextOnScreen = showTextOnScreen;
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
