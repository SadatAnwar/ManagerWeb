package de.fraunhofer.iao.muvi.managerweb.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "text")
@XmlAccessorType(XmlAccessType.FIELD)
public class Text {
	
	public static final String STYLE_WHITE_ON_BLACK = "color: white; background: black;";
	public static final String STYLE_WHITE_ON_FRAUNHOFER = "color: white; background: rgb(23,156,125);";
	public static final String STYLE_FRAUNHOFER_ON_WHITE = "background: white; color: rgb(23,156,125);";
	
	@XmlValue
	private String text;
	
	@XmlAttribute
	private String style;
	
	public Text() {
		
	}
	
	public Text(String text) {
		this.text = text;
	}
	
	public Text(String text, String style) {
		this.text = text;
		this.style = style;
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

}
