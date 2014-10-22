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
import de.fraunhofer.iao.muvi.managerweb.domain.LargeImage;
import de.fraunhofer.iao.muvi.managerweb.domain.Rectangle;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ShowLargeImage {
	
	private static final Log log = LogFactory.getLog(ShowLargeImage.class);
	
	private ShowURLOnScreen showURLOnScreen;
	private Database database;
	
	public void showLargeImage(LargeImage largeImage) {
		DisplayArea da = largeImage.getDisplayarea();
		if (da == null) {
			Map<ScreenID, URL> map = getMapForLargeImage(largeImage);
			showURLOnScreen.showUrlsOnScreens(map, ScreenState.LargeImage);
		}
	}
	
	public Map<ScreenID, URL> getMapForLargeImage(LargeImage largeImage) {
		Map<ScreenID, URL> map = new HashMap<>();
		DisplayArea displayArea = largeImage.getDisplayarea();
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

					URL imgUrl = Utils.replaceManagerCode(largeImage.getUrl(), database.readConfigValue("managerURL"));
					
					String param = "img=" + imgUrl + "&cols=" + rectangle.getWidth() + "&rows=" + rectangle.getHeight() + "&col=" + col + "&row=" + row;
				
					if (Utils.isNotEmpty(largeImage.getIgnoreBezels())) {
						param += "&bezels=true";
					}
					if (largeImage.getWidth() != null && largeImage.getWidth() > 0) {
						param += "&width=" + largeImage.getWidth();
					}
					if (largeImage.getHeight() != null && largeImage.getHeight() > 0) {
						param += "&height=" + largeImage.getHeight();
					}
					if (Utils.isNotEmpty(largeImage.getBackground())) {
						param += "&background=" + largeImage.getBackground();
					}
					
					param = Utils.encodeUrl(param);
					
					String app = "imgPart.jsp";
					
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

	public ShowURLOnScreen getShowURLOnScreen() {
		return showURLOnScreen;
	}

	public void setShowURLOnScreen(ShowURLOnScreen showURLOnScreen) {
		this.showURLOnScreen = showURLOnScreen;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}
	

}
