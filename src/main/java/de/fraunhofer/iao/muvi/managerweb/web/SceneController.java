/**
 * 
 */
package de.fraunhofer.iao.muvi.managerweb.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.fraunhofer.iao.muvi.managerweb.domain.LargeImage;
import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.domain.Screen;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

/**
 * @author ivanniko
 * 
 */
@Controller
public class SceneController extends MainController {

	private static final Log log = LogFactory.getLog(SceneController.class);

	private String paramResetColor;

	private int scenarioId;

	@RequestMapping(value = "editscene.do")
	public String editScene(HttpServletRequest request, ModelMap model) {

		scenarioId = Integer.parseInt(request.getParameter("scenarioId"));
		int sceneNumber = Integer.parseInt(request.getParameter("sceneNumber"));

		Scenario scenario = database.getScenario(scenarioId);
		Scene scene = scenario.getScenes().get(sceneNumber - 1);

		if ("delete".equals(request.getParameter("action"))) {
			if (request.getParameter("largeImageNumber") != null) {
				int largeImageNumber = Integer.parseInt(request
						.getParameter("largeImageNumber"));
				scene.getLargeimages().remove(largeImageNumber - 1);
				database.saveOrUpdateScenario(scenario);
				log.info("Deleted large image " + largeImageNumber
						+ " from scene " + sceneNumber + " from scenario "
						+ scenarioId);
			} else if (request.getParameter("screenNumber") != null) {
				int screenNumber = Integer.parseInt(request
						.getParameter("screenNumber"));
				scene.getScreens().remove(screenNumber - 1);
				database.saveOrUpdateScenario(scenario);
				log.info("Deleted screen " + screenNumber + " from scene "
						+ sceneNumber + " from scenario " + scenarioId);
			} else if (request.getParameter("searchresultsNumber") != null) {
				int searchresultsNumber = Integer.parseInt(request
						.getParameter("searchresultsNumber"));
				scene.getSearchresults().remove(searchresultsNumber - 1);
				database.saveOrUpdateScenario(scenario);
				log.info("Deleted search results " + searchresultsNumber
						+ " from scene " + sceneNumber + " from scenario "
						+ scenarioId);
			} else if (request.getParameter("largeVideoNumber") != null) {
				int largeVideoNumber = Integer.parseInt(request
						.getParameter("largeVideoNumber"));
				scene.getLargevideos().remove(largeVideoNumber - 1);
				database.saveOrUpdateScenario(scenario);
				log.info("Deleted large video " + largeVideoNumber
						+ " from scene " + sceneNumber + " from scenario "
						+ scenarioId);
			} else if (request.getParameter("largeTextNumber") != null) {
				int largeTextNumber = Integer.parseInt(request
						.getParameter("largeTextNumber"));
				scene.getLargetexts().remove(largeTextNumber - 1);
				database.saveOrUpdateScenario(scenario);
				log.info("Deleted large text " + largeTextNumber
						+ " from scene " + sceneNumber + " from scenario "
						+ scenarioId);
			} else if (request.getParameter("largeURLNumber") != null) {
				int largeURLNumber = Integer.parseInt(request
						.getParameter("largeURLNumber"));
				scene.getLargeURLs().remove(largeURLNumber - 1);
				database.saveOrUpdateScenario(scenario);
				log.info("Deleted large URL " + largeURLNumber
						+ " from scene " + sceneNumber + " from scenario "
						+ scenarioId);
			} 
		}
			
		if ("deleteScene".equals(request.getParameter("action"))) {
				/* Delete full scene */
				scenario.getScenes().remove(scene);
				database.saveOrUpdateScenario(scenario);
				log.info("Deleted scene " + sceneNumber + " from scenario "
						+ scenarioId);
		}
		if ("editScene".equals(request.getParameter("action"))) {
			/* Open the newScenepage with previously saved data to continue with edit */
			String editPage = editThisScene( request,  model,  scene,  scenario);
			log.info("Edit scene " + sceneNumber + " of scenario "
					+ scenarioId);
			return editPage;
		}
		

		return "showScenario.do?id=" + scenarioId;
	}
	
	public String editThisScene(HttpServletRequest request, ModelMap model, Scene scene, Scenario scenario){
		int sceneNumber = Integer.parseInt(request.getParameter("sceneNumber"));
		
		if (request.getParameter("largeImageNumber") != null) {
			int largeImageNumber = Integer.parseInt(request
					.getParameter("largeImageNumber"));
			LargeImage largeImage = scene.getLargeimages().get(largeImageNumber-1);
			model.addAttribute("largeImage", largeImage);
			model.addAttribute("largeImageNumber", largeImageNumber-1);
			model.addAttribute("editMode", "edit");
			return "newLargeImage.do?scenarioId="+scenarioId+"&sceneNumber="+sceneNumber+"&editMode=edit&largeImageNumber="+(largeImageNumber-1);
		} else if (request.getParameter("screenNumber") != null) {
			int screenNumber = Integer.parseInt(request
					.getParameter("screenNumber"));
			Screen screen = scene.getScreens().get(screenNumber-1);
			if(screen.getImage()!=null){
				model.addAttribute("imageNumber", screenNumber-1);
				model.addAttribute("editMode", "edit");
				return "newImage.do?scenarioId="+scenarioId+"&sceneNumber="+sceneNumber+"&editMode=edit&imageNumber="+(screenNumber-1);
			}
			if(screen.getUrl()!=null){
				model.addAttribute("urlNumber", screenNumber-1);
				model.addAttribute("editMode", "edit");
				return "newSimpleURL.do?scenarioId="+scenarioId+"&sceneNumber="+sceneNumber+"&editMode=edit&urlNumber="+(screenNumber-1);
			}
			if(screen.getText()!=null){
				model.addAttribute("textNumber", screenNumber-1);
				model.addAttribute("editMode", "edit");
				return "newSimpleText.do?scenarioId="+scenarioId+"&sceneNumber="+sceneNumber+"&editMode=edit&textNumber="+(screenNumber-1);
			}
		} else if (request.getParameter("searchresultsNumber") != null) {
			int searchresultsNumber = Integer.parseInt(request
					.getParameter("searchresultsNumber"));
			scene.getSearchresults().remove(searchresultsNumber - 1);
			database.saveOrUpdateScenario(scenario);
			log.info("Deleted search results " + searchresultsNumber
					+ " from scene " + sceneNumber + " from scenario "
					+ scenarioId);
		} else if (request.getParameter("largeVideoNumber") != null) {
			int largeVideoNumber = Integer.parseInt(request
					.getParameter("largeVideoNumber"));
			scene.getLargevideos().remove(largeVideoNumber - 1);
			database.saveOrUpdateScenario(scenario);
			log.info("Deleted large video " + largeVideoNumber
					+ " from scene " + sceneNumber + " from scenario "
					+ scenarioId);
		} else if (request.getParameter("largeTextNumber") != null) {
			int largeTextNumber = Integer.parseInt(request
					.getParameter("largeTextNumber"));
			model.addAttribute("largeTextNumber", largeTextNumber-1);
			model.addAttribute("editMode", "edit");
			return "newLargeText.do?scenarioId="+scenarioId+"&sceneNumber="+sceneNumber+"&editMode=edit&largeTextNumber="+(largeTextNumber-1);
		} else if (request.getParameter("largeURLNumber") != null) {
			int largeURLNumber = Integer.parseInt(request
					.getParameter("largeURLNumber"));
			scene.getLargeURLs().remove(largeURLNumber - 1);
			model.addAttribute("largeURLNumber", largeURLNumber-1);
			model.addAttribute("editMode", "edit");
			return "newLargeURL.do?scenarioId="+scenarioId+"&sceneNumber="+sceneNumber+"&editMode=edit&largeURLNumber="+(largeURLNumber-1);
		
			
		}
		return "";
		
	}
	

	@RequestMapping(value = "newScene.do")
	public String newScene(HttpServletRequest request, ModelMap model) {
		scenarioId = Integer.parseInt(request.getParameter("scenarioId"));
		// default
		paramResetColor = "0,0,0";
		// POST
		if (isPost(request)) {
			Scenario scenario = database.getScenario(scenarioId);
			Scene scene = new Scene();
			scene.setName(request.getParameter("sName"));
			scene.setDescription(request.getParameter("sDesc"));

			paramResetColor = request.getParameter("sResetcolor");

			if (Utils.isNotEmpty(request.getParameter("sDuration"))) {
				try {
					Integer duration = Integer.parseInt(request
							.getParameter("sDuration"));
					scene.setDuration(duration);
				} catch (Exception e) {
					log.error("Scene duration must be an Integer, got: "
							+ request.getParameter("sDuration"));
					error("Scene duration must be an Integer!", model);
					setParamsForView(model);
					return "newScene.jsp";
				}
			}
			if (Utils.isNotEmpty(paramResetColor)) {
				scene.setResetColor("rgb(" + paramResetColor + ");");
			}
			List<Scene> sList = scenario.getScenes();
			sList.add(scene);
			scenario.setScenes(sList);
			database.saveOrUpdateScenario(scenario);
			log.info("Added scene " + scene.getName() + " to scenario ID "
					+ scenarioId);
			return "showScenario.do?id=" + scenario.getId();
		}
		setParamsForView(model);
		return "newScene.jsp";
	}

	private void setParamsForView(ModelMap model) {
		model.addAttribute("paramResetColor", paramResetColor);
		model.addAttribute("scenarioId", scenarioId);
		model.addAttribute("paramColors", paramColors);
	}

}
