package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;
import java.util.Map;

import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;

public interface IScreenHandler {
	public void showUrlMapOnScreens(Map<ScreenID, URL> urls, ScreenState screenState);
	public void showUrlOnScreen(URL url, ScreenID screen, ScreenState screenState);
	public void interrupt();
}
