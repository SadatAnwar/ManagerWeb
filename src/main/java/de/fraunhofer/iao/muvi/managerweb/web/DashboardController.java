package de.fraunhofer.iao.muvi.managerweb.web;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.fraunhofer.iao.muvi.managerweb.logic.DashboardToScenarioHandler;


@Controller
public class DashboardController {
	
	
	@RequestMapping(value = "dashboardUpload.do")
	public String uploadDashboard(HttpServletRequest request, ModelMap model) throws Exception {
		File xmlFile = new File("testXML.xml");		
		DashboardToScenarioHandler dToSceneraio = new DashboardToScenarioHandler(xmlFile);
		return "dashboardUpload.jsp";
	}

}
