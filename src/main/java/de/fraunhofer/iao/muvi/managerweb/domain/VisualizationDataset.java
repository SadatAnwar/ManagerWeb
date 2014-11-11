package de.fraunhofer.iao.muvi.managerweb.domain;

import java.util.ArrayList;
import java.util.List;

public class VisualizationDataset {
	private String id;
	private String parameter;
	private List<String> buckets;
	
	
	public VisualizationDataset (String id) {
		this.id = id;
	}
	
	public void addBucket (String bucket) {
		if(this.buckets==null) {
			buckets = new ArrayList<String>();
		}
		buckets.add(bucket);
	}
	
	public String getId() {
		return id;
	}
	
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public List<String> getBuckets() {
		return buckets;
	}
		
	public boolean hasBuckets() {
		if(buckets!=null && !buckets.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

}
