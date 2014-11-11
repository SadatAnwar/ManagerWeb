package de.fraunhofer.iao.muvi.managerweb.domain;

import java.net.MalformedURLException;
import java.net.URL;

import com.mysql.jdbc.Util;

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
	
	public Visualization(String id, String dataSet, String category, String title, String type) {
		this.id = id;
		this.dataSetId = dataSet;
		this.category = category;
		this.title = title;
		this.type = type;
	}
	
	public boolean isChart() {
		return this.category.matches("chart");
	}
	
	public void init() throws MalformedURLException {
		String appUrlBase = "http://137.251.35.100:3636/manager/apps/";
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
			
			String URL = appUrlBase+Utils.encodeUrl("url="+Utils.encodeUrl(requestUrl)+"&title="+this.title);
			this.url = new URL(URL);
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
	
}
