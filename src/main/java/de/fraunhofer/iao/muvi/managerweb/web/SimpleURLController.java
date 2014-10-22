/**
 * 
 */
package de.fraunhofer.iao.muvi.managerweb.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.fraunhofer.iao.muvi.managerweb.domain.DisplayArea;
import de.fraunhofer.iao.muvi.managerweb.domain.LargeURL;
import de.fraunhofer.iao.muvi.managerweb.domain.Rectangle;
import de.fraunhofer.iao.muvi.managerweb.domain.Scale;
import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.domain.Screen;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.logic.ScreenIDCalculator;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

@Controller
public class SimpleURLController extends MainController {
	
	private static final Log log = LogFactory
			.getLog(SimpleURLController.class);
	
	@RequestMapping(value = "newURLList.do")
	public String newURLList(HttpServletRequest request, ModelMap model) {
		int paramScenarioId = Integer.parseInt(request
				.getParameter("scenarioId"));
		int paramSceneNumber = Integer.parseInt(request
				.getParameter("sceneNumber"));
		String paramXml = "";

		int paramScreenID = getFirstEmptyScreenID(paramScenarioId,
				paramSceneNumber);
		if (isPost(request)) {
			setAction(request, model);
			String urlsListString = request.getParameter("urls");
			if (Utils.isNotEmpty(urlsListString)) {
				String[] urls = urlsListString.split("\n");
				List<Screen> screens = new ArrayList<>();
				for (String url : urls) {
					if (Utils.isNotEmpty(url)) {
						try {
							url = url.trim();
							log.debug("Add URL " + url);
							Screen screen = new Screen();
							screen.setId(new ScreenID(paramScreenID));
							screen.setUrl(new URL(url));
							screens.add(screen);							
							paramScreenID++;
						} catch (Exception e) {
							log.error("Error while adding URL " + url + ": " + e.getMessage(), e);
						}
					}
				}
				
				Scenario scenario = database
						.getScenario(paramScenarioId);
				Scene scene = scenario.getScenes().get(
						paramSceneNumber - 1);

				List<Screen> iList = scene.getScreens();
				if (iList == null) {
					iList = new ArrayList<Screen>();
				}
				iList.addAll(screens);
				scene.setScreens(iList);

				
				if (ACTION_SAVE.equals(action)) {
					database.saveOrUpdateScenario(scenario);
					return "showScenario.do?id=" + scenario.getId();
				}
				
				paramXml = Utils.getXml(scene);
				model.addAttribute("paramURLsString", urlsListString);
				
			}
		}
		
		model.addAttribute("paramXml", paramXml);
		model.addAttribute("paramScreenID", paramScreenID);
		model.addAttribute("paramScenarioId", paramScenarioId);
		model.addAttribute("paramSceneNumber", paramSceneNumber);
		model.addAttribute("screenSelectorScreenList", paramScreenID);
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		
		return "newURLList.jsp";
	}

	@RequestMapping(value = "newSimpleURL.do")
	public String newSimpleURL(HttpServletRequest request, ModelMap model) {
		int paramScenarioId = Integer.parseInt(request
				.getParameter("scenarioId"));
		int paramSceneNumber = Integer.parseInt(request
				.getParameter("sceneNumber"));
		String paramXml = "";
		String paramUrl = "";
		int paramScreenID = getFirstEmptyScreenID(paramScenarioId,
				paramSceneNumber);
		String editMode= "";
		if(Utils.isNotEmpty(request.getParameter("editMode"))){
			editMode = request.getParameter("editMode");
			}
		if(!editMode.contains("edit")){
		// POST
		// create default page		
			if (isPost(request)) {
				setAction(request, model);
				paramUrl = request.getParameter("simpleUrl");
				if (Utils.isEmpty(paramUrl)) {
					error("URL can not be empty", model);
				} else {
					if (Utils.isNotEmpty(request.getParameter("paramScreenID"))) {
						paramScreenID = Integer.parseInt(request
								.getParameter("paramScreenID"));
					}
					URL simpleURL = null;
					try {
						simpleURL = new URL(paramUrl);
						Screen screen = new Screen();
						screen.setId(new ScreenID(paramScreenID));
						screen.setUrl(simpleURL);
						log.debug(paramScreenID);
						paramXml = Utils.getXml(screen);
						if (ACTION_SAVE.equals(action)) {
							Scenario scenario = database
									.getScenario(paramScenarioId);
							Scene scene = scenario.getScenes().get(
									paramSceneNumber - 1);
							List<Screen> iList = scene.getScreens();
							if (iList == null) {
								iList = new ArrayList<Screen>();
							}
							iList.add(screen);
							scene.setScreens(iList);
							database.saveOrUpdateScenario(scenario);
							return "showScenario.do?id=" + scenario.getId();
						}
					} catch (MalformedURLException e) {
						error("URL is not valid", model);
					}
				}
			}
				// set params for show
				model.addAttribute("paramXml", paramXml);
				model.addAttribute("paramUrl", paramUrl);
		}
		
		//prepare edit page
		else {
			int urlNumber = Integer.parseInt(request.getParameter("urlNumber"));
			Scenario scenario = database.getScenario(paramScenarioId);
			Scene scene = scenario.getSceneById(paramSceneNumber-1);
			Screen editURL = scene.getScreens().get(urlNumber);
			paramUrl = editURL.getUrl().toString();
			paramXml = Utils.getXml(editURL);
			paramScreenID = editURL.getScreenId();
			model.addAttribute("urlNumber", urlNumber);
			model.addAttribute("screenSelectorScreenList", paramScreenID);
			paramUrl = editURL.getUrl().toString();
			//Rewrite the url
			if (isPost(request)) {
				setAction(request, model);
				paramUrl = request.getParameter("simpleUrl");
				if (Utils.isEmpty(paramUrl)) {
					error("URL can not be empty", model);
				} else {
					if (Utils.isNotEmpty(request.getParameter("paramScreenID"))) {
						paramScreenID = Integer.parseInt(request
								.getParameter("paramScreenID"));
					}
					URL simpleURL = null;
					try {
						simpleURL = new URL(paramUrl);
						Screen screen = new Screen();
						screen.setId(new ScreenID(paramScreenID));
						screen.setUrl(simpleURL);
						log.debug(paramScreenID);
						paramXml = Utils.getXml(screen);
						if (ACTION_SAVE.equals(action)) {
							List<Screen> iList = scene.getScreens();
							iList.remove(urlNumber);
							iList.add(urlNumber, screen);
							scene.setScreens(iList);
							database.saveOrUpdateScenario(scenario);
							return "showScenario.do?id=" + scenario.getId();
						}
					} catch (MalformedURLException e) {
						error("URL is not valid", model);
					}
				}
			}
			
		}
		// delegate variable to view
		model.addAttribute("paramScreenID", paramScreenID);
		model.addAttribute("paramScenarioId", paramScenarioId);
		model.addAttribute("paramSceneNumber", paramSceneNumber);
		model.addAttribute("scaleValues", Scale.values());
		model.addAttribute("screenSelectorScreenList", paramScreenID);
		model.addAttribute("paramUrl", paramUrl);
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		return "newSimpleURL.jsp";
	}
	
	@RequestMapping(value = "newLargeURL.do")
	public String newLargeURL(HttpServletRequest request, ModelMap model) {
		String editMode= "";
		int paramScenarioId = Integer.parseInt(request
				.getParameter("scenarioId"));
		int paramSceneNumber = Integer.parseInt(request
				.getParameter("sceneNumber"));
		String paramXml = "";
		int paramStartID = 1;
		int paramWidth = 1;
		int paramHeight = 1;
		String paramUrl = "";
		if(Utils.isNotEmpty(request.getParameter("editMode"))){
			editMode = request.getParameter("editMode");
			}
		if(!editMode.contains("edit")){
			int paramScreenID = getFirstEmptyScreenID(paramScenarioId,
					paramSceneNumber);
			model.addAttribute("screenSelectorScreenList", paramScreenID);
			if (isPost(request)) {
				setAction(request, model);
	
				try {
					
					LargeURL largeURL = new LargeURL();
					if (Utils.isNotEmpty(request.getParameter("height")) && Utils.isNotEmpty(request.getParameter("width"))) {
						paramWidth = Integer.parseInt(request.getParameter("width"));
						paramHeight = Integer.parseInt(request.getParameter("height"));
					}
					if (Utils.isNotEmpty(request.getParameter("start"))) {
						paramStartID = Integer.parseInt(request.getParameter("start"));
					}
					paramUrl = request.getParameter("simpleUrl");
					largeURL.setUrl(new URL(paramUrl));
					largeURL.setDisplayarea(new DisplayArea(new Rectangle(new ScreenID(paramStartID), paramHeight, paramWidth)));
					paramXml = Utils.getXml(largeURL);
					if (ACTION_SAVE.equals(action)) {
						Scenario scenario = database
								.getScenario(paramScenarioId);
						Scene scene = scenario.getScenes().get(
								paramSceneNumber - 1);
						List<LargeURL> largeURLs = scene.getLargeURLs();
						if (largeURLs == null) {
							largeURLs = new ArrayList<LargeURL>();
						}
						largeURLs.add(largeURL);
						scene.setLargeURLs(largeURLs);
						database.saveOrUpdateScenario(scenario);
						return "showScenario.do?id=" + scenario.getId();
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					error("Large URL parameters not valid: " + e.getMessage(), model);
				}
			}
		}
		
		else {
			
			int largeURLNumber = Integer.parseInt(request.getParameter("largeURLNumber"));
			Scenario scenario = database.getScenario(paramScenarioId);
			Scene scene = scenario.getSceneById(paramSceneNumber-1);
			LargeURL editLargeURL = scene.getLargeURLs().get(largeURLNumber);
			paramWidth = editLargeURL.getDisplayarea().getRectangle().getWidth();
			paramHeight = editLargeURL.getDisplayarea().getRectangle().getHeight();
			paramUrl = editLargeURL.getUrl().toString();
			paramXml = Utils.getXml(editLargeURL);
			paramStartID = editLargeURL.getDisplayarea().getRectangle().getStart().getId();
			model.addAttribute("largeURLNumber",largeURLNumber);
			List<ScreenID> list = ScreenIDCalculator.getScreenIDList(new Rectangle(new ScreenID(paramStartID), paramHeight, paramWidth));
			model.addAttribute("screenSelectorScreenList", Utils.convertToString(list));
			//Rewrite the image
			if (isPost(request)) {
				setAction(request, model);
	
				try {
					
					LargeURL largeURL = new LargeURL();
					if (Utils.isNotEmpty(request.getParameter("height")) && Utils.isNotEmpty(request.getParameter("width"))) {
						paramWidth = Integer.parseInt(request.getParameter("width"));
						paramHeight = Integer.parseInt(request.getParameter("height"));
					}
					if (Utils.isNotEmpty(request.getParameter("start"))) {
						paramStartID = Integer.parseInt(request.getParameter("start"));
					}
					paramUrl = request.getParameter("simpleUrl");
					largeURL.setUrl(new URL(paramUrl));
					largeURL.setDisplayarea(new DisplayArea(new Rectangle(new ScreenID(paramStartID), paramHeight, paramWidth)));
					paramXml = Utils.getXml(largeURL);
					if (ACTION_SAVE.equals(action)) {
						List<LargeURL> largeURLs = scene.getLargeURLs();
						largeURLs.remove(largeURLNumber);
						largeURLs.add(largeURLNumber, largeURL);  
						scene.setLargeURLs(largeURLs);
						database.saveOrUpdateScenario(scenario);
						return "showScenario.do?id=" + scenario.getId();
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					error("Large URL parameters not valid: " + e.getMessage(), model);
				}
			}
		}
		
		model.addAttribute("paramStartID", paramStartID);
		model.addAttribute("paramWidth", paramWidth);
		model.addAttribute("paramHeight", paramHeight);
		model.addAttribute("paramScenarioId", paramScenarioId);
		model.addAttribute("paramSceneNumber", paramSceneNumber);
		model.addAttribute("paramXml", paramXml);
		model.addAttribute("paramUrl", paramUrl);
		model.addAttribute("editMode", editMode);
		
		setScreensUsed(paramScenarioId, paramSceneNumber, model);
		return "newLargeURL.jsp";
		

	}
}
