package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.DisplayArea;
import de.fraunhofer.iao.muvi.managerweb.domain.LargeURL;
import de.fraunhofer.iao.muvi.managerweb.domain.Rectangle;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.domain.Text;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ShowURLOnScreen {

	private static final Log log = LogFactory.getLog(ShowURLOnScreen.class);

	
	private ShowTextOnScreen showTextOnScreen;

	private Database database;
	
	private MuViState muViState;
	
	private DisplayComputerManager dcManager;
	
	private ScreenHandler screenHandler;

	public void showUrlOnScreen(URL url, ScreenID screen, ScreenState screenState) {
		if ("debugMode".equals(database.readConfigValue("debugMode"))) {
			log.debug("Debug mode: show " + Utils.decodeUrl(url.toString()) + " on screen " + screen);
		} else {
			screenHandler.showUrlOnScreen(url, screen, screenState);
		}
	}
	
	
	public void showUrlsOnScreens(Map<ScreenID, URL> urls, ScreenState screenState) {
		showUrlsOnScreens(urls, null, screenState);
	}
	
	/**
	 * 
	 * @param urls
	 * @param colorForOtherScreens A CSS color. If not null, all other
	 * screens will be reset to display this color.
	 */
	public void showUrlsOnScreens(Map<ScreenID, URL> urls, String colorForOtherScreens, ScreenState screenState) {
	
		//Reset all other, not used, displays to a certain predefined color
		resetUnusedDisplays(urls, colorForOtherScreens);

		//Open URLs in Map on Monitors
		screenHandler.showUrlMapOnScreens(urls, screenState);
	}
	
	private void resetUnusedDisplays(Map<ScreenID, URL> urls, String colorForOtherScreens) {
		if (Utils.isNotEmpty(colorForOtherScreens)) {
			for (int i=1; i<=36; i++) {
				ScreenID screen = new ScreenID(i);
				if (!urls.containsKey(new ScreenID(i))) {
					showTextOnScreen.showTextOnScreen(new Text("",  "background: " + colorForOtherScreens + ";"), screen);
				}
			}
		}
	}

	public Map<ScreenID, URL> getMapForLargeURL(LargeURL largeURL) {
		Map<ScreenID, URL> map = new HashMap<>();
		DisplayArea displayArea = largeURL.getDisplayarea();
		if (displayArea == null) {
			displayArea = new DisplayArea();
			Rectangle rectangle = new Rectangle(new ScreenID(1), 6, 6);
			displayArea.setRectangle(rectangle);
		}
		Rectangle rectangle = displayArea.getRectangle();
		List<ScreenID> screenIDs = ScreenIDCalculator.getScreenIDList(rectangle);
		int count = 0;
		for (int row = 1; row<=rectangle.getHeight(); row++) {
			for (int col = 1; col<=rectangle.getWidth(); col++) {
				URL url;
				try {
					
					String param = "url=" + largeURL.getUrl() + "&cols=" + rectangle.getWidth() + "&rows=" + rectangle.getHeight() + "&col=" + col + "&row=" + row;
				
					if (Utils.isNotEmpty(largeURL.getIgnoreBezels())) {
						param += "&bezels=true";
					}
					
					param = Utils.encodeUrl(param);
					
					String app = "largeurl.jsp";
					
					url = new URL(database.readConfigValue("managerURL") + "apps/" + app + "?" + param);
//					map.put(new ScreenID(col + 6 * (row - 1)), url);
					map.put(screenIDs.get(count), url);
					count++;
				} catch (MalformedURLException e) {
					log.error(e.getMessage());
				}
			}
		}
		return map;
	}
	
	
	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}
	

	public ShowTextOnScreen getShowTextOnScreen() {
		return showTextOnScreen;
	}

	public void setShowTextOnScreen(ShowTextOnScreen showTextOnScreen) {
		this.showTextOnScreen = showTextOnScreen;
	}

	public MuViState getMuViState() {
		return muViState;
	}

	public void setMuViState(MuViState muViState) {
		this.muViState = muViState;
	}

	public DisplayComputerManager getDcManager() {
		return dcManager;
	}

	public void setDcManager(DisplayComputerManager dcManager) {
		this.dcManager = dcManager;
	}


	public ScreenHandler getScreenHandler() {
		return screenHandler;
	}


	public void setScreenHandler(ScreenHandler screenHandler) {
		this.screenHandler = screenHandler;
	}
	
	
	
	

}
