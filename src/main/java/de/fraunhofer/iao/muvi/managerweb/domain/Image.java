package de.fraunhofer.iao.muvi.managerweb.domain;

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "image")
@XmlAccessorType(XmlAccessType.FIELD)
public class Image {
	
	@XmlElement
	private URL url;

	@XmlElement
	private Scale scale;
	
	public Image() {
		
	}
	
	public Image(URL url) {
		this.url = url;
	}
	
	public Image(URL url, Scale scale) {
		this.url = url;
		this.scale = scale;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Scale getScale() {
		return scale;
	}

	public void setScale(Scale scale) {
		this.scale = scale;
	}
	
}
