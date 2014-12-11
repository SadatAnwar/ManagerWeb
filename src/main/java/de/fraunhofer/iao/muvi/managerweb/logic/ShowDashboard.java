package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

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
		ArrayList<Visualization> visualizationMap = new ArrayList<Visualization>();

		int textCount = 0;
		int chartCount =0;

		// get the XML file from the URL

		try {

			URLConnection connection = visml.openConnection();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(connection.getInputStream());

			//Normalize the XML (recommended)
			doc.getDocumentElement().normalize();
			XPath xPath =  XPathFactory.newInstance().newXPath();

			NodeList visualizations = doc.getElementsByTagName("visualization");

			/* Add title line */
			String style = "background-color: rgb(23,156,125); color: white;";
			String fontSize = "font-size:15em;";

			NodeList metas = doc.getElementsByTagName("meta");
			Element meta = (Element) metas.item(0);
			NodeList titles = meta.getElementsByTagName("title");
			Element dashboardTitle = (Element) titles.item(0);
			String titleString = dashboardTitle.getTextContent();

			log.debug("VisML-Dashboard title is: " + titleString);

			Date updateDate = new Date();

			map.put(new ScreenID(1), showTextOnScreen.getURLForText(new Text(titleString, style)));
			map.put(new ScreenID(2), showTextOnScreen.getURLForText(new Text(" ", style)));
			map.put(new ScreenID(3), showTextOnScreen.getURLForText(new Text("Last update:", style+fontSize)));
			map.put(new ScreenID(4), showTextOnScreen.getURLForText(new Text(Utils.formatDate(updateDate), style+fontSize)));
			map.put(new ScreenID(5), showTextOnScreen.getURLForText(new Text(Utils.formatTime(updateDate), style+fontSize)));
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
					String datasetId = attributes.getNamedItem("dataset").getTextContent();
					Visualization visualization = new Visualization( id,  datasetId,  category,  title, type);
					String formatFunction = "";
					if (!visualization.isChart()) {
						textCount++;
						/* Look for a Javascript format function */
						NodeList parameters = visElement.getElementsByTagName("parameter");
						if (parameters != null && parameters.item(0) != null) {
							Element parameter = (Element) parameters.item(0);
							String fFunction = parameter.getTextContent();
							if (Utils.isNotEmpty(fFunction)) {
								formatFunction = fFunction;
							}
						}
					} else {
						chartCount++;
					}
					String dataSetSelector = "//dataset[@id='"+datasetId+"']";
					Node dataSetNode = (Node) xPath.compile(dataSetSelector).evaluate(doc, XPathConstants.NODE);
					if (dataSetNode.getNodeType() == Node.ELEMENT_NODE) {	
						Element dataSetElement = (Element) dataSetNode;
						VisualizationDataset dataset = new VisualizationDataset(datasetId);

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
						visualization.setDataset(dataset);		
						visualization.setFormatFunction(formatFunction);
						visualization.init(database);
						visualizationMap.add(visualization);
					}
				}
			}
			/*
			 * Now consider 3 broad cases
			 * 1. If the total is greater than 30
			 * 	a. if charts are greater than 18 and text greater than equal to 12 we show 12 text and 18 charts
			 * 	b. if charts are less than 18 and text more than 12 show all charts and 12+(18-chart) text
			 * 	c. if text is less then 12 and chart is more than 18, show all text and 18+(12-text) charts
			 * 2. if the total is less than 30 show all and try to center
			 * */

			//Case 1.a.
			if(textCount+chartCount>30){
				int currentScreen =7;
				if(chartCount>=18){
					int chartsDisplayed =0;
					for(Visualization vis :visualizationMap){
						
						if(!vis.isChart()){
							chartsDisplayed++;
							map.put(new ScreenID(currentScreen), vis.getUrl());
							currentScreen++;
						}
						if(chartsDisplayed>=12){
							break;
						}
					}
					for(Visualization vis :visualizationMap){
						if(vis.isChart()){
							map.put(new ScreenID(currentScreen), vis.getUrl());
							currentScreen++;
						}
						if(currentScreen>36){
							break;
						}
					}
				}
				else {
					int chartDisplayed = 0;
					for(Visualization vis :visualizationMap){
						if(!vis.isChart()){
							chartDisplayed++;
							map.put(new ScreenID(currentScreen), vis.getUrl());
							currentScreen++;
						}
						if(chartDisplayed>= (30-chartCount)){
							break;
						}
					}
					for(Visualization vis :visualizationMap){
						if(vis.isChart()){
							map.put(new ScreenID(currentScreen), vis.getUrl());
							currentScreen++;
						}
						if(currentScreen>36){
							break;
						}
					}
				}
			//case when the total is less than 30, then just put all the charts on the screen
			} else {
				//if we have more then 4 rows of chart or less then 3 (only 2 rows) then dont have a blank row after the title
				int currentScreen =7;
				if((textCount+chartCount)>6*4 || (textCount+chartCount)<6*3){
					//First put all Text
					for(Visualization vis :visualizationMap){
						if(!vis.isChart()){
							map.put(new ScreenID(currentScreen), vis.getUrl());
							currentScreen++;
						}
					}
					for(Visualization vis :visualizationMap){
						if(vis.isChart()){
							map.put(new ScreenID(currentScreen), vis.getUrl());
							currentScreen++;
						}
					}
				//we have enough data for 4 rows put a blank row in the put a blank row in the middle after the title line 	
				} else if((textCount+chartCount)>18 && (textCount+chartCount)<25) {
					currentScreen = 13;
					//First put all Text
					for(Visualization vis :visualizationMap){
						if(!vis.isChart()){
							map.put(new ScreenID(currentScreen), vis.getUrl());
							currentScreen++;
						}
					}
					for(Visualization vis :visualizationMap){
						if(vis.isChart()){
							map.put(new ScreenID(currentScreen), vis.getUrl());
							currentScreen++;
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
	
	
	/**
	 * [NOT COMPLETE] Should distribute the charts in a uniform fashion so that 
	 * a majority of the area on the wall looks occupied
	 * 
	 * @param map with the screen and url
	 * @return a map that is more uniformly distributed
	 * @author anwar
	 * 
	 */
	@SuppressWarnings("unused")
	private Map<ScreenID, URL> distributeScreen(Map<ScreenID, URL> map, int numberOfCharts){
		Map<ScreenID, URL> distributedMap = new HashMap<>();
		Iterator<Map.Entry<ScreenID, URL>> iter = map.entrySet().iterator();
		//If the charts are less then 30, we have at least one row free
		if(numberOfCharts<30){
			
		}
		
		int i =1;
		while(iter.hasNext()){
			Map.Entry<ScreenID, URL> currentMap = iter.next();
			
			distributedMap.put(new ScreenID(i), currentMap.getValue());
			i++;
			if(i>36){
				break;
			}
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
