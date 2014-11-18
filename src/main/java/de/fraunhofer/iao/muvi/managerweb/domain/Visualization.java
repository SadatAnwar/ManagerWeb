package de.fraunhofer.iao.muvi.managerweb.domain;

import java.net.MalformedURLException;
import java.net.URL;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class Visualization {

	private String id;
	private String dataSetId;
	private String category;
	private String type;
	private String title;
	private ScreenID screenID;
	private URL url;
	private VisualizationDataset dataset; 
	private String formatFunction;
	
	public Visualization(String id, String dataSetId, String category, String title, String type) {
		this.id = id;
		this.dataSetId = dataSetId;
		this.category = category;
		this.title = title;
		this.type = type;
	}
	
	public boolean isChart() {
		return this.category.matches("chart");
	}
	
	public void init(Database database) throws MalformedURLException {
		String appUrlBase = database.readConfigValue("managerURL")+"apps/";
		String requestUrl ="https://dashboard.iao.fraunhofer.de/arpos/dashboard-values?&";
		if(isChart()) {
			if(this.type.matches("line")) {
				appUrlBase = appUrlBase+"lineChart.jsp?";
				requestUrl = requestUrl+"value="+dataset.getParameter();
				if(dataset.hasBuckets()) {
					for(String bucket : dataset.getBuckets()){
						requestUrl = requestUrl + "&bucket=" + bucket;
					}
				}
				String URL = appUrlBase+Utils.encodeUrl("url="+Utils.encodeUrl(requestUrl)+"&title="+this.title);
				this.url = new URL(URL);
			}
			//handle bar chart
			else {
				appUrlBase = appUrlBase+"barChart.jsp?";
				requestUrl = requestUrl+"value="+dataset.getParameter();
				if(dataset.hasBuckets()) {
					for(String bucket : dataset.getBuckets()){
						requestUrl = requestUrl + "&bucket=" + bucket;
					}
				}
				String URL = appUrlBase+Utils.encodeUrl("url="+Utils.encodeUrl(requestUrl)+"&title="+this.title);
				this.url = new URL(URL);
			}
		}
		//handle text charts
		else {
			appUrlBase = appUrlBase+"textChart.jsp?";
			requestUrl = requestUrl+"value="+dataset.getParameter();
			
			String urlString = appUrlBase+Utils.encodeUrl("url="+Utils.encodeUrl(requestUrl)+"&title="+this.title);
			urlString += Utils.encodeUrl("&formatFunction=" + Utils.encodeUrl(formatFunction));
			this.url = new URL(urlString);
		}

	}
	
	public String getId() {
		return id;
	}
	
	public String getDataSetId() {
		return dataSetId;
	}
	public void setDataSetId(String dataSetId) {
		this.dataSetId = dataSetId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public ScreenID getScreenID() {
		return screenID;
	}
	public void setScreenID(ScreenID screenID) {
		this.screenID = screenID;
	}

	public VisualizationDataset getDataset() {
		return dataset;
	}

	public void setDataset(VisualizationDataset dataset) throws MalformedURLException {
		this.dataset = dataset;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getFormatFunction() {
		return formatFunction;
	}

	public void setFormatFunction(String formatFunction) {
		this.formatFunction = formatFunction;
	}
	
}
