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
import de.fraunhofer.iao.muvi.managerweb.domain.LargeVideo;
import de.fraunhofer.iao.muvi.managerweb.domain.Rectangle;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ShowLargeVideo {
	private static final Log log = LogFactory.getLog(ShowLargeVideo.class);
	
	private ShowURLOnScreen showURLOnScreen;
	private Database database;
	
	public void showLargeVideo(LargeVideo largeVideo) {
		Map<ScreenID, URL> map = getMapForLargeVideo(largeVideo);
		showURLOnScreen.showUrlsOnScreens(map, ScreenState.LargeVideo);
	}
	
	public Map<ScreenID, URL> getMapForLargeVideo(LargeVideo largeVideo) {
		Map<ScreenID, URL> map = new HashMap<ScreenID, URL>();
		/* TODO: Implement! */
		
		DisplayArea displayArea = largeVideo.getDisplayarea();
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

					URL videoUrl = largeVideo.getVideo().getUrl();//Utils.replaceManagerCode(, database.readConfigValue("managerURL"));
					int id = ScreenIDCalculator.getIDFromColumnAndRow(col, row);
					String param = "video=" + videoUrl + "&cols=" + rectangle.getWidth() + "&rows=" + rectangle.getHeight() + "&col=" + col + "&row=" + row  + "&id=" + id;					
									
					param = Utils.encodeUrl(param);
					
					String app = "video.do";
					
					url = new URL(database.readConfigValue("managerURL") + app + "?" + param);
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

	public void setDatabase(Database database) {
		this.database = database;
	}

	
}
