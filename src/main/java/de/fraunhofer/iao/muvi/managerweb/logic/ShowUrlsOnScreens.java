package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.domain.DisplayComputer;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ShowUrlsOnScreens extends Thread {

	private static final Log log = LogFactory.getLog(ShowUrlsOnScreens.class);
	
	private Map<ScreenID, URL> map;
	private DisplayComputer dc;
	private boolean debugMode;
	private boolean go = true;
	private ScreenState screenState;
	
	private class ShowOneUrlOnOneScreen extends Thread {
		private ScreenID currentScreen;
		private URL currentUrl;
		private ScreenState screenState;
		
		protected ShowOneUrlOnOneScreen(ScreenID currentScreen, URL currentUrl, ScreenState screenState) {
			this.currentScreen = currentScreen;
			this.currentUrl = currentUrl;
			this.screenState = screenState;
		}
		
		public void run() {
			dc.gotoURL(this.currentScreen, this.currentUrl, this.screenState);
		}
		
	}
	
	public ShowUrlsOnScreens(Map<ScreenID, URL> map, DisplayComputer dc, boolean debugMode, ScreenState screenState) {
		this.map = map;
		this.dc = dc;
		this.debugMode = debugMode;
		this.screenState = screenState;
	}
	
	@Override
	public void run() {
				
		for (ScreenID screen : map.keySet()) {
			if (go && dc.isActive()) {
				if (debugMode) {
					log.debug("Debug mode: show " + Utils.decodeUrl(map.get(screen).toString()) + " on screen " + screen);
				} else {
					ShowOneUrlOnOneScreen souroos = new ShowOneUrlOnOneScreen(screen, map.get(screen), screenState);
					souroos.start();
				}
			}
		}
		
	}
	
	@Override
	public void interrupt() {
		this.go = false;
	}

}
