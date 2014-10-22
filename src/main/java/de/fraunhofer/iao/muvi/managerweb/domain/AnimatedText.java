package de.fraunhofer.iao.muvi.managerweb.domain;

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "animatedtext")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimatedText {

	@XmlElement
	private Text text;
	
	@XmlElement
	private Image image;
	
	@XmlAttribute
	private Integer speed;
	
	public AnimatedText() {
		
	}
	
	public AnimatedText(String text, String style, URL imageURL, Integer speed) {
		this.text = new Text(text, style);
		this.image = new Image(imageURL);
		this.speed = speed;
	}
	
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	
}
