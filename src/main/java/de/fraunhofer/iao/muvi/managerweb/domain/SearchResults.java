package de.fraunhofer.iao.muvi.managerweb.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "searchresults")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchResults {

	@XmlAttribute
	private SearchResultType type;
	
	@XmlElement
	private String query;
	
	@XmlElement
	private ScreenID searchscreen;
	
	@XmlElement
	private DisplayArea displayarea;
	
	public String getDescription() {
		return type + " search for " + query;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public SearchResultType getType() {
		return type;
	}
	
	public void setType(SearchResultType type) {
		this.type = type;
	}

	public ScreenID getSearchscreen() {
		return searchscreen;
	}

	public void setSearchscreen(ScreenID searchscreen) {
		this.searchscreen = searchscreen;
	}

	public DisplayArea getDisplayarea() {
		return displayarea;
	}

	public void setDisplayarea(DisplayArea displayarea) {
		this.displayarea = displayarea;
	}
	
}
