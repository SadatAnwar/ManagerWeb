package de.fraunhofer.iao.muvi.managerweb.domain;

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "largeurl")
@XmlAccessorType(XmlAccessType.FIELD)
public class LargeURL {
	
	@XmlElement
	private URL url;
	
	@XmlElement
	private DisplayArea displayarea;
	
	@XmlAttribute(name = "ignorebezels")
	private String ignoreBezels;

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getIgnoreBezels() {
		return ignoreBezels;
	}

	public void setIgnoreBezels(String ignoreBezels) {
		this.ignoreBezels = ignoreBezels;
	}

	public DisplayArea getDisplayarea() {
		return displayarea;
	}

	public void setDisplayarea(DisplayArea displayarea) {
		this.displayarea = displayarea;
	}

}
