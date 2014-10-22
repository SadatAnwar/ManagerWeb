package de.fraunhofer.iao.muvi.managerweb.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

@XmlRootElement(name = "scenario")
@XmlAccessorType(XmlAccessType.FIELD)
public class Scenario {

	@XmlAttribute
	private Integer id;

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String description;

	@XmlAttribute(name = "tags")
	private String tagString;

	@XmlTransient
	private List<String> tags;

	@XmlAttribute
	private Date date;

	@XmlElement(name = "scene")
	private List<Scene> scenes;
	
	@XmlTransient
	private int scenesCount; 


	public Scenario() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Scene> getScenes() {
		if(scenes == null) {
			return new ArrayList<Scene>();
		}
		return scenes;
	}
	
	public Scene getSceneById(int id) {
		if(!scenes.isEmpty()) {
			if(scenes.size()>id){
				return scenes.get(id);
			}
		}
		return null;
	}

	public void setScenes(List<Scene> scenes) {
		this.scenes = scenes;
	}

	public int getScenesCount() {
		return scenes.size();
	}

	public void setScenesCount(int scenesCount) {
		this.scenesCount = scenesCount;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<String> getTags() {
		if (Utils.isNotEmpty(tagString)) {
			String[] tags = tagString.split(",");
			setTags(Arrays.asList(tags));
		}
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
		if (Utils.isNotEmpty(tags)) {
			tagString = "";
			for (String tag : tags) {
				tagString += tag + ",";
			}
		}
	}

	public void addTag(String tag) {
		if (Utils.isEmpty(tags)) {
			tags = new ArrayList<>();
		}
		tags.add(tag);
	}

	public String getTagString() {
		return tagString;
	}
	
	public void setTagString(String tagString) {
		this.tagString = tagString;
	}

}
