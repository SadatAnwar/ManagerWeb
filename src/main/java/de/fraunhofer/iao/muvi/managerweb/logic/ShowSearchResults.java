package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.domain.SearchResults;
import de.fraunhofer.iao.muvi.managerweb.domain.Text;
import de.fraunhofer.iao.muvi.managerweb.utils.HttpResponse;
import de.fraunhofer.iao.muvi.managerweb.utils.HttpSender;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ShowSearchResults {
	
	private static final String GOOGLE_CX = "001513186094135611815:pjmjqf8nlbu";

	private static final String GOOGLE_KEY = "AIzaSyC4VUlY1UPeoOuPR5GFMEEDDX65nLp4T8g";

	private static final Log log = LogFactory.getLog(ShowSearchResults.class);

	private ShowURLOnScreen showURLOnScreen;

	private ShowTextOnScreen showTextOnScreen;

	private HttpSender httpSender;

	public void showSearchResults(SearchResults searchResults) {
		Map<ScreenID, URL> map = getMapForSearchResults(searchResults);
		showURLOnScreen.showUrlsOnScreens(map, ScreenState.SearchResults);
	}
	
	private Map<ScreenID, URL> getMapForGoogleImages(SearchResults searchResults) {
		Map<ScreenID, URL> map = new HashMap<>();
	
//		String url = "https://www.googleapis.com/customsearch/v1";
//		Map<String, String> parameters = new HashMap<>();
//		parameters.put("key", GOOGLE_KEY);
//		parameters.put("q", searchResults.getQuery());
//		parameters.put("searchType", "image");
//		parameters.put("imgSize", "medium");
//		parameters.put("cx", GOOGLE_CX);
//		HttpResponse httpResponse = httpSender.doGet(url, parameters);
//		String content = httpResponse.getContent();
//		Map<ScreenID, URL> urls = new HashMap<>();
//		if (Utils.isNotEmpty(content)) {
//			log.debug(content);
//		}
		
		
		String url = "https://www.google.com/search";
		Map<String, String> parameters = new HashMap<>();
		parameters.put("q", searchResults.getQuery() + " filetype:jpg");
//		parameters.put("site", "imghp");
//		parameters.put("tbm", "isch");
		HttpResponse httpResponse = httpSender.doGet(url, parameters);
		String content = httpResponse.getContent();
		log.debug(content);
		
		/* TODO Implement */
		
		map.put(new ScreenID(1), showTextOnScreen.getURLForText(new Text("Google Images", Text.STYLE_WHITE_ON_FRAUNHOFER)));
		map.put(new ScreenID(2), showTextOnScreen.getURLForText(new Text(searchResults.getQuery(), Text.STYLE_WHITE_ON_FRAUNHOFER)));
		
		return map;
	}
	
	private Map<ScreenID, URL> getMapForWebMiningCockpit(SearchResults searchResults) {
		Map<ScreenID, URL> map = new HashMap<>();
		
		/* TODO: Implement */
		
		map.put(new ScreenID(1), showTextOnScreen.getURLForText(new Text("Web Mining Cockpit", Text.STYLE_WHITE_ON_FRAUNHOFER)));
		map.put(new ScreenID(2), showTextOnScreen.getURLForText(new Text(searchResults.getQuery(), Text.STYLE_WHITE_ON_FRAUNHOFER)));
		
		
		return map;
	}
	
	private Map<ScreenID, URL> getMapForAmazon(SearchResults searchResults) {
		Map<ScreenID, URL> map = new HashMap<>();
		
		String url = "http://www.amazon.com/s/ref=nb_sb_noss_1";
		Map<String, String> parameters = new HashMap<>();
		parameters.put("url", "search-alias");
		parameters.put("field-keywords", searchResults.getQuery());
		HttpResponse httpResponse = httpSender.doGet(url, parameters);
		String content = httpResponse.getContent();
	
		int counter = 7;
		
		if (Utils.isNotEmpty(content)) {
			String[] lines = content.split("\n");
			for (String line : lines) {
				if (line.indexOf("<div class=\"productImage\"><a href=\"") >= 0) {
					String link = line.substring(35, line.indexOf("<img") - 3);
					log.debug("Amaton link: " + link);
					try {
						map.put(new ScreenID(counter), new URL(link));
						counter++;
					} catch (MalformedURLException e) {
						log.error(e);
					}
				}
			}
		}
		

		map.put(new ScreenID(1), showTextOnScreen.getURLForText(new Text("Amazon", Text.STYLE_WHITE_ON_FRAUNHOFER)));
		map.put(new ScreenID(2), showTextOnScreen.getURLForText(new Text(searchResults.getQuery(), Text.STYLE_WHITE_ON_FRAUNHOFER)));
		
		
		if (searchResults.getSearchscreen() != null) {
			String link = "http://www.amazon.com/s/ref=nb_sb_noss_1?field-keywords=" + Utils.encodeUrl(searchResults.getQuery());
			try {
				map.put(searchResults.getSearchscreen(), new URL(link));
			} catch (MalformedURLException e) {
				log.error(e.getMessage());
			}
		}
		
			
		return map;
	}
	
	private Map<ScreenID, URL> getMapForPRSearch(SearchResults searchResults) {
		Map<ScreenID, URL> map = new HashMap<>();
		
		String url = "http://prsearch.swm.iao.fraunhofer.de/search";
		Map<String, String> parameters = new HashMap<>();
		parameters.put("q", searchResults.getQuery());
		parameters.put("of", "rss");
		parameters.put("n", "34");
		HttpResponse httpResponse = httpSender.doGet(url, parameters);
		String content = httpResponse.getContent();

		int counter = 3;

		if (Utils.isNotEmpty(content)) {
			String[] lines = content.split("\n");
			for (String line : lines) {
				if (line.indexOf("<link>") >= 0) {
					String link = line.substring(12, line.length() - 7);
					log.debug("PRSearch link: " + link);
					try {
						map.put(new ScreenID(counter), new URL(link));
						counter++;
					} catch (MalformedURLException e) {
						log.error(e);
					}
				}
			}
		}
		
		map.put(new ScreenID(1), showTextOnScreen.getURLForText(new Text("PRSearch", Text.STYLE_WHITE_ON_FRAUNHOFER)));
		map.put(new ScreenID(2), showTextOnScreen.getURLForText(new Text(searchResults.getQuery(), Text.STYLE_WHITE_ON_FRAUNHOFER)));

		if (searchResults.getSearchscreen() != null) {
			String link = "http://prsearch.swm.iao.fraunhofer.de/search?q="
					+ Utils.encodeUrl(searchResults.getQuery());
			try {
				URL prURL = new URL(link);
				map.put(searchResults.getSearchscreen(), prURL);
			} catch (MalformedURLException e) {
				log.error(e);
			}
		}
	
		return map;
	}

	private Map<ScreenID, URL> getMapForGoogle(SearchResults searchResults) {
		Map<ScreenID, URL> map = new HashMap<>();
		
		String url = "https://www.googleapis.com/customsearch/v1";
		Map<String, String> parameters = new HashMap<>();
		parameters.put("key", GOOGLE_KEY);
		parameters.put("q", searchResults.getQuery());
		parameters.put("cx", GOOGLE_CX);
		HttpResponse httpResponse = httpSender.doGet(url, parameters);
		String content = httpResponse.getContent();
		int counter = 7;
		if (Utils.isNotEmpty(content)) {
			String[] lines = content.split("\n");
			for (String line : lines) {
				if (line.indexOf("\"link\"") >= 0) {
					String link = line.substring(12, line.length() - 2);
					log.debug("Google link: " + link);
					try {
						map.put(new ScreenID(counter), new URL(link));
						counter++;
					} catch (MalformedURLException e) {
						log.error(e);
					}
				}
			}
		}
		
		map.put(new ScreenID(1), showTextOnScreen.getURLForText(new Text("Google", Text.STYLE_WHITE_ON_FRAUNHOFER)));
		map.put(new ScreenID(2), showTextOnScreen.getURLForText(new Text(searchResults.getQuery(), Text.STYLE_WHITE_ON_FRAUNHOFER)));

		if (searchResults.getSearchscreen() != null) {
			String googleLink = "http://www.google.com/search?q="
					+ Utils.encodeUrl(searchResults.getQuery());
			try {
				URL googleURL = new URL(googleLink);
				map.put(searchResults.getSearchscreen(), googleURL);
			} catch (MalformedURLException e) {
				log.error(e);
			}
		}

		return map;
	}
	
	public Map<ScreenID, URL> getMapForSearchResults(SearchResults searchResults) {
		Map<ScreenID, URL> map = new HashMap<>();
		
		switch (searchResults.getType()) {
		case Amazon:
			map = getMapForAmazon(searchResults);
			break;

		case Google:
			map = getMapForGoogle(searchResults);
			break;

		case GoogleImages:
			map = getMapForGoogleImages(searchResults);
			break;

		case PRSearch:
			map = getMapForPRSearch(searchResults);
			break;

		case WebMiningCockpit:
			map = getMapForWebMiningCockpit(searchResults);
			break;

		default:
			break;
		}
		
		return map;
	}

	public ShowURLOnScreen getShowURLOnScreen() {
		return showURLOnScreen;
	}

	public void setShowURLOnScreen(ShowURLOnScreen showURLOnScreen) {
		this.showURLOnScreen = showURLOnScreen;
	}

	public HttpSender getHttpSender() {
		return httpSender;
	}

	public void setHttpSender(HttpSender httpSender) {
		this.httpSender = httpSender;
	}

	public ShowTextOnScreen getShowTextOnScreen() {
		return showTextOnScreen;
	}

	public void setShowTextOnScreen(ShowTextOnScreen showTextOnScreen) {
		this.showTextOnScreen = showTextOnScreen;
	}

}
