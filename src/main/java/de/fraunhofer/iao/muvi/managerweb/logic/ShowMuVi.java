package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.domain.LargeImage;
import de.fraunhofer.iao.muvi.managerweb.domain.LargeText;
import de.fraunhofer.iao.muvi.managerweb.domain.LargeURL;
import de.fraunhofer.iao.muvi.managerweb.domain.LargeVideo;
import de.fraunhofer.iao.muvi.managerweb.domain.MuVi;
import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.domain.Screen;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.domain.SearchResults;
import de.fraunhofer.iao.muvi.managerweb.domain.Text;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class ShowMuVi {
		
	private static final Log log = LogFactory
			.getLog(ShowMuVi.class);
	
	private ShowURLOnScreen showURLOnScreen;
	
	private ShowTextOnScreen showTextOnScreen;
	
	private ShowImageOnScreen showImageOnScreen;
	
	private ShowLargeImage showLargeImage;
	
	private ShowSearchResults showSearchResults;
	
	private ShowLargeVideo showLargeVideo;
	
	private ShowDashboard showDashboard;
	
	/**
	 * Show a full presentation on the wall.
	 * Start by showing all scenarios as defined,
	 * then all scenes as defined,
	 * then all individual screens as defined.
	 * @param muvi
	 */
	public void showMuVi(MuVi muvi) {
		
		if (Utils.isNotEmpty(muvi.getScenarios())) {
			for (Scenario scenario : muvi.getScenarios()) {
				log.debug("Show scenario " + scenario.getName() + " - " + scenario.getDate());
				showScenario(scenario);
				Utils.waitDefault();
			}
		}
		
		if (Utils.isNotEmpty(muvi.getScenes())) {
			for (Scene scene : muvi.getScenes()) {
				log.debug("Show scene " + scene.getName() + " - " + scene.getDate());
				showScene(scene);
				Utils.waitDefault();
			}
		}

		if (Utils.isNotEmpty(muvi.getScreens())) {
			for (Screen screen : muvi.getScreens()) {
				showScreen(screen);
			}
		}
		
	}
	
	/**
	 * Shows all scenes in a scenario.
	 * Default wait time between scenes: DEFAULT_WAIT_TIME milliseconds.
	 * @param scenario
	 */
	public void showScenario(Scenario scenario) {
		log.debug("Start showing scenario " + scenario.getId() + " - " + scenario.getName());
		if (Utils.isNotEmpty(scenario.getScenes())) {
			for (Scene scene : scenario.getScenes()) {
				showScene(scene);
				if (scenario.getScenes().size() > 1) {
					if (scene.getDuration() != null && scene.getDuration() > 0) {
						log.debug("Wait " + scene.getDuration() + " seconds as specified in scene.");
						Utils.wait(1000 * scene.getDuration());
					} else {
						log.debug("Wait default time.");
						Utils.waitDefault();
					}
				}
			}
		}
		log.debug("Done showing scenario " + scenario.getId() + " - " + scenario.getName());
	}
	//scene to screen map is created
	public void showScene(Scene scene) {
		Map<ScreenID, URL> map = getMapForScene(scene);
		showURLOnScreen.showUrlsOnScreens(map, ScreenState.URL);
	}
	
	public Map<ScreenID, URL> getMapForScene(Scene scene) {
		return getMapForScene(scene, true);
	}
	
	/*Scenes are mapped to displays here
	 * each scene defines the display content to be viewed on the displays
	 *  if a new scene type is added to the list of scenes, the required logic should be updated here
	 */
	public Map<ScreenID, URL> getMapForScene(Scene scene, boolean checkResetColor) {
		Map<ScreenID, URL> map = new HashMap<>();
		//Does this scene contain a large Image? 
		if (Utils.isNotEmpty(scene.getLargeimages())) {
			for (LargeImage largeImage : scene.getLargeimages()) {
				map.putAll(showLargeImage.getMapForLargeImage(largeImage));
			}
		}
		//Does this scene contain a large videos? 
		if (Utils.isNotEmpty(scene.getLargevideos())) {
			for (LargeVideo largeVideo : scene.getLargevideos()) {
				map.putAll(showLargeVideo.getMapForLargeVideo(largeVideo));
			}
		}
		//Does this scene contain search results? 
		if (Utils.isNotEmpty(scene.getSearchresults())) {
			for (SearchResults searchResults : scene.getSearchresults()) {
				map.putAll(showSearchResults
						.getMapForSearchResults(searchResults));
			}
		}
		//Does this scene contain a set of screens? 
		if (Utils.isNotEmpty(scene.getScreens())) {
			for (Screen screen : scene.getScreens()) {
				map.put(screen.getId(), getURLForScreen(screen));
			}
		}
		//Does this scene contain a large TEXT? 
		if (Utils.isNotEmpty(scene.getLargetexts())) {
			for (LargeText largetext : scene.getLargetexts()) {
				map.putAll(showTextOnScreen.getMapForLargeText(largetext));
			}
		}
		//Does this scene contain a large URL? 
		if (Utils.isNotEmpty(scene.getLargeURLs())) {
			for (LargeURL largeURL : scene.getLargeURLs()) {
				map.putAll(showURLOnScreen.getMapForLargeURL(largeURL));
			}
		}
		//Does this scene contain a VISML URL?
		if (Utils.isNotEmpty(scene.getVisml())) {
			//currently we can have only one VSM per scene
			map.putAll(showDashboard.getMapForDashboard(scene.getVisml()));
		
		}
		if (checkResetColor) {
			/* If a reset color is defined, use it to erase the non-defined screens */
			if (Utils.isNotEmpty(scene.getResetColor())) {
				for (int i=1; i<=36; i++) {
					if (!map.containsKey(new ScreenID(i))) {
						Text text = new Text("", "background: " + scene.getResetColor() + "; ");
						map.put(new ScreenID(i), showTextOnScreen.getURLForText(text));
					}
				}
			}
		}
		return map;
	}
	
	private URL getURLForScreen(Screen screen) {
		URL url = null;
		
		if (screen.getUrl() != null) {
			url = screen.getUrl();
		}
		if (screen.getText() != null) {
			url = showTextOnScreen.getURLForText(screen.getText());
		}
		if (screen.getImage() != null) {
			url = showImageOnScreen.getURLForImage(screen.getImage());
		}
		if (screen.getAnimatedText() != null) {
			url = showTextOnScreen.getURLForAnimatedText(screen.getAnimatedText());
		}
		
		return url;
	}
	
	public void showScreen(Screen screen) {
		log.debug("Show screen " + screen.getId());
		if (screen.getUrl() != null) {
			showURLOnScreen.showUrlOnScreen(screen.getUrl(), screen.getId(), ScreenState.URL);
		}
		if (screen.getText() != null) {
			showTextOnScreen.showTextOnScreen(screen.getText(), screen.getId());
		}
		if (screen.getImage() != null) {
			showImageOnScreen.showImageOnScreen(screen.getImage(), screen.getId());
		}
	}
	


	public void setShowURLOnScreen(ShowURLOnScreen showURLOnScreen) {
		this.showURLOnScreen = showURLOnScreen;
	}
	
	public ShowURLOnScreen getShowURLOnScreen() {
		return showURLOnScreen;
	}
	
	public void setShowTextOnScreen(ShowTextOnScreen showTextOnScreen) {
		this.showTextOnScreen = showTextOnScreen;
	}
	
	public ShowTextOnScreen getShowTextOnScreen() {
		return showTextOnScreen;
	}

	public ShowImageOnScreen getShowImageOnScreen() {
		return showImageOnScreen;
	}

	public void setShowImageOnScreen(ShowImageOnScreen showImageOnScreen) {
		this.showImageOnScreen = showImageOnScreen;
	}

	public ShowLargeImage getShowLargeImage() {
		return showLargeImage;
	}

	public void setShowLargeImage(ShowLargeImage showLargeImage) {
		this.showLargeImage = showLargeImage;
	}

	public ShowSearchResults getShowSearchResults() {
		return showSearchResults;
	}

	public void setShowSearchResults(ShowSearchResults showSearchResults) {
		this.showSearchResults = showSearchResults;
	}

	public ShowLargeVideo getShowLargeVideo() {
		return showLargeVideo;
	}

	public void setShowLargeVideo(ShowLargeVideo showLargeVideo) {
		this.showLargeVideo = showLargeVideo;
	}

	public ShowDashboard getShowDashboard() {
		return showDashboard;
	}

	public void setShowDashboard(ShowDashboard showDashboard) {
		this.showDashboard = showDashboard;
	}
		
}
