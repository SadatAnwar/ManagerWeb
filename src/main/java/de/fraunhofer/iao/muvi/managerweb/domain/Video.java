package de.fraunhofer.iao.muvi.managerweb.domain;

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "video")
@XmlAccessorType(XmlAccessType.FIELD)
public class Video {
	
	@XmlElement
	private URL url;
	
	@XmlElement
	private Double starttime;
	
	@XmlElement
	private Double stoptime;
	
	@XmlElement
	private Boolean autostart;
	
	@XmlElement
	private Boolean loop;
	
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public Double getStarttime() {
		return starttime;
	}
	public void setStarttime(Double starttime) {
		this.starttime = starttime;
	}
	public Double getStoptime() {
		return stoptime;
	}
	public void setStoptime(Double stoptime) {
		this.stoptime = stoptime;
	}
	public Boolean getAutostart() {
		return autostart;
	}
	public void setAutostart(Boolean autostart) {
		this.autostart = autostart;
	}
	public Boolean getLoop() {
		return loop;
	}
	public void setLoop(Boolean loop) {
		this.loop = loop;
	}
	
}
