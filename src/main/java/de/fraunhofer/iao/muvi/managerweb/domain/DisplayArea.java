package de.fraunhofer.iao.muvi.managerweb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

@XmlRootElement(name = "displayarea")
@XmlAccessorType(XmlAccessType.FIELD)
public class DisplayArea {
	
	private static final Log log = LogFactory.getLog(DisplayArea.class);
	
	@XmlElement
	private String list;
	
	@XmlElement
	private Rectangle rectangle;
	
	@XmlTransient
	private List<ScreenID> screenIDList;
	
	public DisplayArea() {
		// empty constructor
	}
	
	public DisplayArea(Rectangle rectangle) {
		setRectangle(rectangle);
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
		
		if (Utils.isNotEmpty(list)) {
			String[] ids = list.split(",");
			screenIDList = new ArrayList<>();
			for (String id : ids) {
				try {
					screenIDList.add(new ScreenID(Integer.parseInt(id)));
				} catch (Exception e) {
					log.error("Invalid screen ID: " + id);
				}
			}
		}
		
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	
	public List<ScreenID> getScreenIDList() {
		return screenIDList;
	}
	
	public void setScreenIDList(List<ScreenID> screenIDList) {
		this.screenIDList = screenIDList;
		if (Utils.isNotEmpty(screenIDList)) {
			this.list = "";
			for (ScreenID screenID : screenIDList) {
				this.list += screenID.getId() + ",";
			}
		}
	}
	
	public static void main(String[] args) {
		DisplayArea da = new DisplayArea();
		da.setList("2,3,4,10,11,12,52,36,30,,,");
		for (ScreenID sid : da.getScreenIDList()) {
			log.debug(sid.getId());
		}
	}

}
