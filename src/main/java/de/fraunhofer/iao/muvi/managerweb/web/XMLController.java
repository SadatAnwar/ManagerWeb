package de.fraunhofer.iao.muvi.managerweb.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.MuVi;
import de.fraunhofer.iao.muvi.managerweb.domain.Scenario;
import de.fraunhofer.iao.muvi.managerweb.domain.Scene;
import de.fraunhofer.iao.muvi.managerweb.domain.Screen;
import de.fraunhofer.iao.muvi.managerweb.logic.ShowMuVi;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

@Controller
public class XMLController {
	
	public static final String ACTION_SHOW = "show";
	public static final String ACTION_SAVE = "save";
	
	private static final Log log = LogFactory
			.getLog(XMLController.class);
	
	private ShowMuVi showMuVi;
	private Database database;
	
	@RequestMapping(value = "xml.do")
	public String sendXml(HttpServletRequest request, ModelMap model) throws Exception	{
		
		String xml = "";
		
		String action = "";
		if (request.getParameter(ACTION_SHOW) != null) {
			action = ACTION_SHOW;
		}
		if (request.getParameter(ACTION_SAVE) != null) {
			action = ACTION_SAVE;
		}
		
		if (Utils.isNotEmpty(request.getParameter("scenarioId")) && Utils.isEmpty(action)) {
			
			Integer scenarioId = Integer.parseInt(request.getParameter("scenarioId"));
			Scenario scenario = database.getScenario(scenarioId);
			if (scenario != null) {
				xml = Utils.getXml(scenario);
			}
			
		} else {
			
			xml = request.getParameter("xml");
			
			/* Try to deserialize XML. */
			if (Utils.isNotEmpty(xml)) {
				try {
	
					MuVi muvi = null;
					Scenario scenario = null;
					Scene scene = null;
					Screen screen = null;
					
					/* Build object if necessary */
					if (xml.indexOf("<muvi") >= 0) {
						muvi = Utils.getObjectFromXml(xml, MuVi.class);
						
					} else if (xml.indexOf("<scenario") >= 0) {
						scenario = Utils.getObjectFromXml(xml, Scenario.class);
					} else if (xml.indexOf("<scene") >= 0) {
						scene = Utils.getObjectFromXml(xml, Scene.class);
					} else if (xml.indexOf("<screen") >= 0) {
						screen = Utils.getObjectFromXml(xml, Screen.class);
					}
					

						if (ACTION_SHOW.equals(action)) {
							model.addAttribute("message", "Show sequence on wall!");
							if (muvi != null) {
								showMuVi.showMuVi(muvi);
							} else if (scenario != null) {
								showMuVi.showScenario(scenario);
							} else if (scene != null) {
								showMuVi.showScene(scene);
							} else if (screen != null) {
								showMuVi.showScreen(screen);
							}
							
						}
						
						if (ACTION_SAVE.equals(action)) {
							if (scenario != null) {
								database.saveOrUpdateScenario(scenario);
								xml = Utils.getXml(scenario);
							}
						}
					
				} catch (Exception e) {
	
					log.error("Invalid XML: " + e.getMessage(), e);
					model.addAttribute("message", Utils.escapeXml("Invalid XML: " + e.getMessage()));
				}
			}
		
		}
		
		model.addAttribute("xml", Utils.escapeXml(xml));
		
		return "xml.jsp";
		
	}
	
	public void setShowMuVi(ShowMuVi showMuVi) {
		this.showMuVi = showMuVi;
	}
	
	public ShowMuVi getShowMuVi() {
		return showMuVi;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}
