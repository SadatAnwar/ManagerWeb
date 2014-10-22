package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;

public class MuViState {
	
	private static final Log log = LogFactory.getLog(MuViState.class);

	private Map<ScreenID, URL> screenURLMap;
	
	private Map<ScreenID, ScreenState> screenStateMap;
	
	private Database database;
	
	public MuViState() {
		screenURLMap = new HashMap<>();
		screenStateMap = new HashMap<>();
		for (int i=1; i<= ScreenIDCalculator.NUMBER_OF_SCREENS; i++) {
			screenURLMap.put(new ScreenID(i), null);
			screenStateMap.put(new ScreenID(i), ScreenState.Start);
		}
	}
	
	public void updateState(ScreenID screen, URL url, ScreenState screenState) {
		if (screenURLMap.containsKey(screen)) {
			screenURLMap.put(screen, url);
			screenStateMap.put(screen, screenState);
			log.info("Update MuVi state for screen " + screen + " to state " + screenState + " and URL " + url);
		} else {
			throw new IllegalArgumentException("MuVi does not have a screen with the ID " + screen);
		}
	}
	
	public URL getScreenUrl(ScreenID screen) {
		if (screenURLMap.containsKey(screen)) {
			return screenURLMap.get(screen);
		} else {
			throw new IllegalArgumentException("MuVi does not have a screen with the ID " + screen);
		}
	}
	
	public ScreenState getScreenState(ScreenID screen) {
		if (screenStateMap.containsKey(screen)) {
			return screenStateMap.get(screen);
		} else {
			throw new IllegalArgumentException("MuVi does not have a screen with the ID " + screen);
		}
	}

	public Map<ScreenID, URL> getScreenURLMap() {
		return screenURLMap;
	}

	public void setScreenURLMap(Map<ScreenID, URL> screenURLMap) {
		this.screenURLMap = screenURLMap;
	}

	public Map<ScreenID, ScreenState> getScreenStateMap() {
		return screenStateMap;
	}

	public void setScreenStateMap(Map<ScreenID, ScreenState> screenStateMap) {
		this.screenStateMap = screenStateMap;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}
	
}
