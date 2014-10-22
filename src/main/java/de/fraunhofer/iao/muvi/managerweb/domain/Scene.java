package de.fraunhofer.iao.muvi.managerweb.domain;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "scene")
@XmlAccessorType(XmlAccessType.FIELD)
public class Scene {

	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private String description;
	
	@XmlAttribute
	private Date date;
	
	@XmlAttribute(name = "resetcolor")
	private String resetColor;
	
	@XmlAttribute
	private Integer duration;
	
	@XmlAttribute
	private URL screenshot;
	
	@XmlElement(name = "screen")
	private List<Screen> screens;
	
	@XmlElement(name = "largeimage")
	private List<LargeImage> largeimages;
	
	@XmlElement(name = "largevideo")
	private List<LargeVideo> largevideos;
	
	@XmlElement
	private List<SearchResults> searchresults;
	
	@XmlElement(name = "largetext")
	private List<LargeText> largetexts;
	
	@XmlElement(name = "largeurl")
	private List<LargeURL> largeURLs;
	
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
	public List<LargeImage> getLargeimages() {
		if(largeimages == null) {
			return new ArrayList<LargeImage>();
		}
		return largeimages;
	}
	public void setLargeimages(List<LargeImage> largeimages) {
		this.largeimages = largeimages;
	}
	public List<LargeVideo> getLargevideos() {
		return largevideos;
	}
	public void setLargevideos(List<LargeVideo> largevideos) {
		this.largevideos = largevideos;
	}
	public List<SearchResults> getSearchresults() {
		return searchresults;
	}
	public void setSearchresults(List<SearchResults> searchresults) {
		this.searchresults = searchresults;
	}
	public List<Screen> getScreens() {
		return screens;
	}
	public void setScreens(List<Screen> screens) {
		this.screens = screens;
	}
	public void setResetColor(String resetColor) {
		this.resetColor = resetColor;
	}
	public String getResetColor() {
		return resetColor;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public List<LargeText> getLargetexts() {
		return largetexts;
	}
	public void setLargetexts(List<LargeText> largetexts) {
		this.largetexts = largetexts;
	}
	public List<LargeURL> getLargeURLs() {
		return largeURLs;
	}
	public void setLargeURLs(List<LargeURL> largeURLs) {
		this.largeURLs = largeURLs;
	}
	public URL getScreenshot() {
		return screenshot;
	}
	public void setScreenshot(URL screenshot) {
		this.screenshot = screenshot;
	}
}
