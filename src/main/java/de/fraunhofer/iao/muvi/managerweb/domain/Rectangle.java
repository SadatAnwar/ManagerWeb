package de.fraunhofer.iao.muvi.managerweb.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rectangle")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rectangle {

	private ScreenID start;
	
	private Integer height;
	
	private Integer width;
	
	public Rectangle() {
		
	}
	
	public Rectangle(ScreenID start, Integer height, Integer width) {
		this.start = start;
		this.height = height;
		this.width = width;
	}

	public ScreenID getStart() {
		return start;
	}

	public void setStart(ScreenID start) {
		this.start = start;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
	
	@Override
	public String toString() {
		return "Rectangle: start " + start.toString() + ", width: " + width + ", height: " + height;
	}
	
}
