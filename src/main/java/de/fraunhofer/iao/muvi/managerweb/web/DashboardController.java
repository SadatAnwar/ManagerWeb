package de.fraunhofer.iao.muvi.managerweb.web;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class DashboardController extends MainController {
	
	private int paramScenarioId;
	private int paramSceneNumber;
	private String paramXml;
	private String vismlURL;
	
	
	@RequestMapping(value ="newVISMLDashboard.do")
	public String newSimpleText(HttpServletRequest request, ModelMap model) {
		String editMode ="";
		if(Utils.isNotEmpty(request.getParameter("editMode"))){
			editMode = request.getParameter("editMode");
			}
		paramScenarioId = Integer.parseInt(request.getParameter("scenarioId"));
		paramSceneNumber = Integer.parseInt(request.getParameter("sceneNumber"));
		if(!editMode.contains("edit")){
			model.addAttribute("paramScenarioId", paramScenarioId);
			model.addAttribute("paramSceneNumber", paramSceneNumber);
			return "newDashboardVISML.jsp";
		}
		else {
			vismlURL = request.getParameter("vismlUrl");
			paramXml = request.getParameter("paramXml");
			//Map the attributes to the edit page
			model.addAttribute("paramXml", paramXml);
			model.addAttribute("paramScenarioId", paramScenarioId);
			model.addAttribute("paramSceneNumber", paramSceneNumber);
		}
		return "newDashboardVISML.jsp";
	}
	@RequestMapping(value="saveVisMLDashboard.do")
	public String saveVisml(HttpServletRequest request, ModelMap model) {
		
		Scenario scenario = database.getScenario(paramScenarioId);
		Scene scene = scenario.getScenes().get(paramSceneNumber - 1);
		if(!Utils.isEmpty(request.getParameter("vismlUrl"))){
			vismlURL = request.getParameter("vismlUrl");
		} else {
			model.addAttribute("error", "VisML field blank!!");
			model.addAttribute("paramScenarioId", paramScenarioId);
			model.addAttribute("paramSceneNumber", paramSceneNumber);
			return "newDashboardVISML.jsp";
		}
		URL VisML;
		try {
			VisML = new URL (vismlURL);
			scene.setVisml(VisML);
			database.saveOrUpdateScenario(scenario);
		} catch (MalformedURLException e) {
			model.addAttribute("error", "Invalid URL");
			model.addAttribute("paramScenarioId", paramScenarioId);
			model.addAttribute("paramSceneNumber", paramSceneNumber);
			return "newDashboardVISML.jsp";
		}
		
		return "showScenario.do?id="+paramScenarioId;
	}
	
}
