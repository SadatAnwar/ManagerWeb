package de.fraunhofer.iao.muvi.managerweb.logic;

import java.util.Map;

import de.fraunhofer.iao.muvi.managerweb.domain.DisplayComputer;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;

public class ScreenDCMapper {

	private Map<ScreenID, DisplayComputer> map;
	
	public ScreenDCMapper(Map<ScreenID, DisplayComputer> map) {
		this.map = map;
	}
	
	public DisplayComputer getDisplayComputerForScreen(ScreenID screen) {
		if (screen != null && map.containsKey(screen)) {
			return map.get(screen);
		} else {
			throw new IllegalArgumentException(screen + " is not a valid screen ID.");
		}
	}
	
	public ScreenID getGlobalScreenIDForLocalScreenID(Integer localID, DisplayComputer dc) {
		Map<ScreenID, Integer> localIDMap = dc.getLocalIDMap();
		for (ScreenID screenID : localIDMap.keySet()) {
			if (localIDMap.get(screenID).equals(localID)) {
				return screenID;
			}
		}
		throw new IllegalArgumentException("LocalID " + localID + " not found on Display Computer " + dc);
	}
	
}
