package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.Image;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ShowImageOnScreen {

	private static final Log log = LogFactory.getLog(ShowImageOnScreen.class);

	private ShowURLOnScreen showURLOnScreen;
	private Database database;
	
	public void showImageOnScreen(Image image, ScreenID screenID) {
		showURLOnScreen.showUrlOnScreen(getURLForImage(image), screenID, ScreenState.Image);
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
	
	public URL getURLForImage(Image image) {
		
		try {
		
			URL imgUrl = Utils.replaceManagerCode(image.getUrl(), database.readConfigValue("managerURL"));
	
			String link = "?img=" + imgUrl;
			
			if (image.getScale() != null) {
				link += "&scale=" + image.getScale();
			}
					
			link = Utils.encodeUrl(link);
			link = database.readConfigValue("managerURL") + "apps/img.jsp" + link;

			return new URL(link);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
}
