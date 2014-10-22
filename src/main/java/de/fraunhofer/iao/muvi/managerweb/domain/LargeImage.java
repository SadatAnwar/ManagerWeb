package de.fraunhofer.iao.muvi.managerweb.domain;

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "largeimage")
@XmlAccessorType(XmlAccessType.FIELD)
public class LargeImage {

	@XmlAttribute(name = "ignorebezels")
	private String ignoreBezels;
	
	@XmlElement
	private URL url;

	@XmlElement
	private DisplayArea displayarea;
	
	@XmlAttribute
	private Integer width;
	
	@XmlAttribute
	private Integer height;
	
	@XmlAttribute
	private String background;
	
	public LargeImage() {
		// empty constructor
	}
	
	public LargeImage(URL url, ScreenID start, int width, int height) {
		setUrl(url);
		setDisplayarea(new DisplayArea(new Rectangle(start, height, width)));
	}
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public void setDisplayarea(DisplayArea displayarea) {
		this.displayarea = displayarea;
	}

	public DisplayArea getDisplayarea() {
		return displayarea;
	}
	public void setIgnoreBezels(String ignoreBezels) {
		this.ignoreBezels = ignoreBezels;
	}
	public String getIgnoreBezels() {
		return ignoreBezels;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

}
