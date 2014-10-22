package de.fraunhofer.iao.muvi.managerweb.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "largetext")
@XmlAccessorType(XmlAccessType.FIELD)
public class LargeText {
	
	@XmlElement
	private String text;
	
	@XmlElement
	private String style;
	
	@XmlElement
	private DisplayArea displayarea;
	
	@XmlAttribute(name = "ignorebezels")
	private String ignoreBezels;
	
	public LargeText() {
	}
	
	public LargeText(String text, String style, ScreenID start, int width, int height) {
		setText(text);
		setStyle(style);
		setDisplayarea(new DisplayArea(new Rectangle(start, height, width)));
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public DisplayArea getDisplayarea() {
		return displayarea;
	}
	public void setDisplayarea(DisplayArea displayarea) {
		this.displayarea = displayarea;
	}
	public String getIgnoreBezels() {
		return ignoreBezels;
	}
	public void setIgnoreBezels(String ignoreBezels) {
		this.ignoreBezels = ignoreBezels;
	}

}
