package de.fraunhofer.iao.muvi.managerweb.domain;

import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "screen")
@XmlAccessorType(XmlAccessType.FIELD)
public class Screen {
	
	private ScreenID id;
	
	/* TODO Use attributes instead of element ScreenID */
	
	@XmlTransient
	private Integer screenId;
	
	@XmlTransient
	private Integer column;
	
	@XmlTransient
	private Integer row;
	
	private URL url;
	
	private Text text;
	
	private Image image;
	
	@XmlElement(name = "animatedtext")
	private AnimatedText animatedText;

	public ScreenID getId() {
		updateScreenId();
		return id;
	}

	public void setId(ScreenID id) {
		this.id = id;
		updateScreenId();
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
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

	public AnimatedText getAnimatedText() {
		return animatedText;
	}

	public void setAnimatedText(AnimatedText animatedText) {
		this.animatedText = animatedText;
	}

	public Integer getScreenId() {
		updateScreenId();
		return screenId;	
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
		updateScreenId();
	}

	public Integer getColumn() {
		updateScreenId();
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
		updateScreenId();
	}

	public Integer getRow() {
		updateScreenId();
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
		updateScreenId();
	}
	
	private void updateScreenId() {
		if (this.screenId != null && this.screenId > 0) {
			this.id = new ScreenID(this.screenId);
		} else if (this.column != null && this.column > 0 && this.row != null && this.row > 0) {
			this.id = new ScreenID(this.column, this.row);
		} else if (this.id != null) {
			this.screenId = this.id.getId();
		}
	}

}
