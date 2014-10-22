package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.websockets.EventHandler;

public class WebsocketWindowFacade implements IScreenHandler {
	private static final Log log = LogFactory.getLog(WebsocketWindowFacade.class);
	private EventHandler eventHandler;
	
	public WebsocketWindowFacade() {
		
	}
	
	public void init() {
		//Setup the eventHandler reference after the context is created and the EventHandler instance is setup
		//Why in init since it is a non-spring singleton? Better safe than sorry!
		eventHandler = EventHandler.getInstance();
		log.debug("Initialized WebsocketWindowFacade");
	}
	
	@Override
	public void showUrlMapOnScreens(Map<ScreenID, URL> urls,
			ScreenState screenState) {
		
		for (ScreenID screen : urls.keySet()) {
			this.showUrlOnScreen(urls.get(screen), screen, screenState);
		}
	}

	@Override
	public void showUrlOnScreen(URL url, ScreenID screen,
			ScreenState screenState) {
		try {
			if(eventHandler.isActive(screen.getId())) {
				eventHandler.gotoURL(screen, url, screenState);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void interrupt() {
		// TODO Auto-generated method stub
		
	}

}
