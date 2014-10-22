package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.AnimatedText;
import de.fraunhofer.iao.muvi.managerweb.domain.DisplayArea;
import de.fraunhofer.iao.muvi.managerweb.domain.LargeText;
import de.fraunhofer.iao.muvi.managerweb.domain.Rectangle;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.domain.Text;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ShowTextOnScreen {

	private static final Log log = LogFactory.getLog(ShowTextOnScreen.class);

	private ShowURLOnScreen showURLOnScreen;
	private Database database;

	public void showTextOnScreen(Text text, ScreenID screenID) {

		try {
			URL url = getURLForText(text);
			if (url != null) {
				showURLOnScreen.showUrlOnScreen(url, screenID, ScreenState.Text);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	public void showAnimatedTextOnScreen(AnimatedText animatedText, ScreenID screenID) {

		try {
			URL url = getURLForAnimatedText(animatedText);
			if (url != null) {
				showURLOnScreen.showUrlOnScreen(url, screenID, ScreenState.AnimatedText);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public ShowURLOnScreen getShowURLOnScreen() {
		return showURLOnScreen;
	}

	public void setShowURLOnScreen(ShowURLOnScreen showURLOnScreen) {
		this.showURLOnScreen = showURLOnScreen;
	}

	public URL getURLForText(Text text) {
		
		String link = "text=";
		
		if (Utils.isNotEmpty(text.getText())) {
			link += text.getText();
		}
		
		if (Utils.isNotEmpty(text.getStyle())) {
			link += "&style=" + Utils.encodeUrl(text.getStyle());
		}
		
		link = Utils.encodeUrl(link);
		link = database.readConfigValue("managerURL") + "apps/textcolor.jsp?" + link;
		try {
			return new URL(link);
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public URL getURLForAnimatedText(AnimatedText animatedText) {
		
		try {
			String link = "";
			
			if (animatedText.getText() != null) {
				link += "?text=";
				if (Utils.isNotEmpty(animatedText.getText().getText())) {
					link += animatedText.getText().getText();
				}
				if (Utils.isNotEmpty(animatedText.getText().getStyle())) {
					link += "&style=" + Utils.encodeUrl(animatedText.getText().getStyle());
				}
			}
			if (animatedText.getImage() != null) {
				

				URL imgUrl = Utils.replaceManagerCode(animatedText.getImage().getUrl(), database.readConfigValue("managerURL"));

				link += "&img=" + imgUrl;
			}
			
			/* Speed in milliseconds, default is ten seconds */
			int speed = 10 * 1000;
			if (animatedText.getSpeed() != null && animatedText.getSpeed() > 0) {
				speed = animatedText.getSpeed();
			}
			link += "&speed=" + speed;
			
			link = Utils.encodeUrl(link);
			link = database.readConfigValue("managerURL") + "apps/animtext.jsp" + link;

			return new URL(link);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	public Map<ScreenID, URL> getMapForLargeText(LargeText largeText) {
		Map<ScreenID, URL> map = new HashMap<>();
		DisplayArea displayArea = largeText.getDisplayarea();
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
					
					String param = "text=" + largeText.getText() + "&style=" + largeText.getStyle() + "&cols=" + rectangle.getWidth() + "&rows=" + rectangle.getHeight() + "&col=" + col + "&row=" + row;
				
					if (Utils.isNotEmpty(largeText.getIgnoreBezels())) {
						param += "&bezels=true";
					}
					
					param = Utils.encodeUrl(param);
					
					String app = "textcolorpart2.jsp";
					
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

}
