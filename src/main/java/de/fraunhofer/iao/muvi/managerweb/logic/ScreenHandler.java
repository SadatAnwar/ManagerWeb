package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;

public class ScreenHandler {
	private static final Log log = LogFactory.getLog(ScreenHandler.class);
	
	private boolean useWebsocketFacade = false;
	
	private ChromeServiceWindowFacade chromeServiceFacade;
	private WebsocketWindowFacade websocketFacade;
	private IScreenHandler screenHandlerFacade;
	
	private Database database;
	private MuViState muViState;
	
	public ScreenHandler() {
		
	}
	
	public void init() {
		log.debug("init technology facade");
		setWebsocketEnabled(database.readConfigValue("websocketEnabled").equals("websocketEnabled"));
	}
	
	public ChromeServiceWindowFacade getChromeServiceFacade() {
		return chromeServiceFacade;
	}
	public void setChromeServiceFacade(ChromeServiceWindowFacade chromeServiceFacade) {
		this.chromeServiceFacade = chromeServiceFacade;
	}
	public WebsocketWindowFacade getWebsocketFacade() {
		return websocketFacade;
	}
	public void setWebsocketFacade(WebsocketWindowFacade websocketFacade) {
		this.websocketFacade = websocketFacade;
	}
	
	public void setWebsocketEnabled(boolean enabled) {
		useWebsocketFacade = enabled;	
		if(useWebsocketFacade) {
			log.debug("Using Websockets");
			switchToWebsocketFacade();
		} else {
			log.debug("Using Chrome Web Services");
			switchToChromeServiceFacade();
		}
	}
	
	public boolean websocketEnabled() {
		return useWebsocketFacade;
	}
	
	public String getPlainStartURL() {
	 return database.readConfigValue("customURL");
	}
	
	public String getChromeStartURL() {
		return getPlainStartURL();
	}
	
	public String getWebsocketStartURL() {
		String startupURL = "";
		String managerURL = database.readConfigValue("managerURL");
		startupURL = managerURL+"customStartWebsocket.do?DC=";
		return startupURL;
	}
	
	public void switchToWebsocketFacade() {
		screenHandlerFacade = websocketFacade;
	}
	
	public void switchToChromeServiceFacade() {
		screenHandlerFacade = chromeServiceFacade;
	}

	public void showUrlMapOnScreens(Map<ScreenID, URL> urls,
			ScreenState screenState) {
		screenHandlerFacade.showUrlMapOnScreens(urls, screenState);
	}

	public void showUrlOnScreen(URL url, ScreenID screen,
			ScreenState screenState) {
		screenHandlerFacade.showUrlOnScreen(url, screen, screenState);
	}

	public void interrupt() {
		screenHandlerFacade.interrupt();
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}	
		
	public MuViState getMuViState() {
		return muViState;
	}

	public void setMuViState(MuViState muViState) {
		this.muViState = muViState;
	}

	public void performSwitch() {
		try {
			String managerURL = database.readConfigValue("managerURL");
			
			Map<ScreenID, URL> screenMap = muViState.getScreenURLMap();
			if(websocketEnabled()) {
				for(Entry<ScreenID, URL> entry: screenMap.entrySet()) {
					ScreenID screenId = entry.getKey();
					URL url = entry.getValue();
					if(url != null) {
						String transferURL = URLEncoder.encode(url.toString(), "UTF-8");
						String startupURL = managerURL+"screen.do?id=" + screenId.getId() + "&url=" + transferURL;
						url = new URL(startupURL);
					}
					else {
						String startupURL = managerURL+"screen.do?id=" + screenId.getId();
						url = new URL(startupURL);						
					}
					entry.setValue(url);
				}
			}
			chromeServiceFacade.showUrlMapOnScreens(screenMap, ScreenState.URL);
		}
		catch (Exception e) {
			log.error("Error during performSwitch", e);
		}
	}
}
