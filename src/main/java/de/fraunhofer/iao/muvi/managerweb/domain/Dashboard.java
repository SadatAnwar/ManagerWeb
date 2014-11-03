package de.fraunhofer.iao.muvi.managerweb.domain;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dashboard")
@XmlAccessorType(XmlAccessType.FIELD)
public class Dashboard {
	
	@XmlAttribute(name = "category")
	private String category;
	
	@XmlAttribute(name = "datasetName")
	private String datasetName;
	
	@XmlAttribute(name = "id")
	private String id;
	
	@XmlAttribute(name = "type")
	private String type;
	
	@XmlAttribute(name = "title")
	private String title;
	
	@XmlElement(name = "dataset")
	private Dataset dataset;

}
