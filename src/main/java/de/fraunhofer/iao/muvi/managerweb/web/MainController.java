/**
 * 
 */
package de.fraunhofer.iao.muvi.managerweb.web;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.domain.Screen;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.logic.MuViScreenshot;
import de.fraunhofer.iao.muvi.managerweb.logic.ShowMuVi;

@Controller
public class MainController {

	public Database database;

	public ShowMuVi showMuVi;
	
	public MuViScreenshot MuViScreenshot;

	public String action = null;

	public static final String ACTION_SHOW = "show";

	public static final String ACTION_SAVE = "save";

	public static final String ACTION_DELETE = "delete";

	public boolean onlyDemo = true;

	protected List<String> paramColors = Arrays.asList("0,0,0", "255,255,255",
			"210,230,244", "136,188,226", "31,130,192", "0,90,148", "0,52,107",
			"199,193,222", "144,133,186", "57,55,139", "41,40,106", "226,0,26",
			"158,28,34", "119,28,44", "254,234,201", "251,203,140",
			"242,148,0", "235,106,10", "255,250,209", "255,243,129",
			"255,220,0", "253,195,0", "238,239,177", "209,221,130",
			"177,200,0", "143,164,2", "106,115,65", "180,220,211",
			"109,191,169", "23,156,125", "215,225,201", "203,175,115",
			"70,14,21", "16,99,111", "51,184,202", "37,186,226", "0,110,146",
			"199,202,204");

	protected boolean isPost(HttpServletRequest request) {
		return "POST".equalsIgnoreCase(request.getMethod());
	}

	protected void setAction(HttpServletRequest request, ModelMap model) {
		if (request.getParameter(ACTION_SHOW) != null) {
			action = ACTION_SHOW;
			model.addAttribute("paramShowXML", true);
		}
		else if (request.getParameter(ACTION_SAVE) != null) {
			action = ACTION_SAVE;
		}
		else if (request.getParameter(ACTION_DELETE) != null) {
			action = ACTION_DELETE;
		}
		else {
			action = null;
		}

	}

	protected void error(String message, ModelMap model) {
		model.addAttribute("error", message);
	}

	protected void message(String message, ModelMap model) {
		model.addAttribute("message", message);
	}
	
	protected void setScreensUsed(int scenarioId, int sceneNumber, ModelMap model) {
		Scenario scenario = database.getScenario(scenarioId);
		Scene scene = scenario.getScenes().get(sceneNumber - 1);
		Map<ScreenID, URL> sceneMap = showMuVi.getMapForScene(scene, false);
		for (int i=1; i<=36; i++) {
			if (sceneMap.containsKey(new ScreenID(i))) {
				model.addAttribute("start" + i, "screenFull");
			} else {
				model.addAttribute("start" + i, "screenEmpty");
			}
		}
	}

	protected String addScreenAndSave(int scenarioId, int sceneNumber,
			Screen screen) {
		Scenario scenario = database.getScenario(scenarioId);
		Scene scene = scenario.getScenes().get(sceneNumber - 1);
		List<Screen> iList = scene.getScreens();
		if (iList == null) {
			iList = new ArrayList<Screen>();
		}
		iList.add(screen);
		scene.setScreens(iList);
		database.saveOrUpdateScenario(scenario);
		return "showScenario.do?id=" + scenario.getId();
	}
	
	protected String editScreenAndSave(int scenarioId, int sceneNumber,
			Screen screen, int screenNumber) {
		Scenario scenario = database.getScenario(scenarioId);
		Scene scene = scenario.getScenes().get(sceneNumber - 1);
		List<Screen> iList = scene.getScreens();
		iList.remove(screenNumber);
		iList.add(screenNumber,screen);
		scene.setScreens(iList);
		database.saveOrUpdateScenario(scenario);
		return "showScenario.do?id=" + scenario.getId();
	}

	protected int getFirstEmptyScreenID(int scenarioId, int sceneNumber) {
		Scenario scenario = database.getScenario(scenarioId);
		Scene scene = scenario.getScenes().get(sceneNumber - 1);
		Map<ScreenID, URL> sceneMap = showMuVi.getMapForScene(scene, false);
		for (int i = 1; i < 37; i++) {
			if (!sceneMap.containsKey(new ScreenID(i))) {
				return i;
			}
		}
		// default
		return 1;
	}
	
	protected List<ScreenID> getListOfUsedScreens(int scenarioId, int sceneNumber) {
		Scenario scenario = database.getScenario(scenarioId);
		Scene scene = scenario.getScenes().get(sceneNumber - 1);
		Map<ScreenID, URL> sceneMap = showMuVi.getMapForScene(scene, false);
		List<ScreenID> list = new ArrayList<>();
		for (int i=1; i<=36; i++) {
			if (sceneMap.containsKey(new ScreenID(i))) {
				list.add(new ScreenID(i));
			}
		}
		return list;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public ShowMuVi getShowMuVi() {
		return showMuVi;
	}

	public void setShowMuVi(ShowMuVi showMuVi) {
		this.showMuVi = showMuVi;
	}

	public MuViScreenshot getMuViScreenshot() {
		return MuViScreenshot;
	}

	public void setMuViScreenshot(MuViScreenshot muViScreenshot) {
		MuViScreenshot = muViScreenshot;
	}
}
