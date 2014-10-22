package de.fraunhofer.iao.muvi.managerweb.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "largevideo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LargeVideo {
	
	@XmlElement
	private Video video;
	
	@XmlElement
	private DisplayArea displayarea;

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public DisplayArea getDisplayarea() {
		return displayarea;
	}

	public void setDisplayarea(DisplayArea displayarea) {
		this.displayarea = displayarea;
	}

}
