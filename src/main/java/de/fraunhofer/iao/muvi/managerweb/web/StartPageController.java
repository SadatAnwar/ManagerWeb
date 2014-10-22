package de.fraunhofer.iao.muvi.managerweb.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.DisplayComputer;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.logic.DisplayComputerManager;

@Controller
public class StartPageController {
	
	private DisplayComputerManager dcManager;
	private Database database;
	

	@RequestMapping(value = "customStart.do")
	public String newSimpleURL(HttpServletRequest request, ModelMap model) {
		
		Integer dc = Integer.parseInt(request.getParameter("DC"));
		DisplayComputer displayComputer = null;
		if (dc==1)		{
			displayComputer= dcManager.getDC1();
		}		
		else if (dc==2) {
			displayComputer= dcManager.getDC2();
		}
		else if (dc==3) {
			displayComputer= dcManager.getDC3();
		}
		else if (dc==4) {
			displayComputer= dcManager.getDC4();
		}
				
		Integer screen = Integer.parseInt(request.getParameter("Screen"));
		ScreenID globalID= dcManager.getGlobalScreenIDForLocalScreenID(screen, displayComputer);
		int factor = ((globalID.getId()-1)/6);
		float red= 255*((5.f-factor)/5.f)/5;
		float redNext= 255*((5.f-(factor+1))/5.f)/5;
		float green= 25;
		float blue= 255*((factor)/5.f)/5;
		float blueNext= 255*((factor+1)/5.f)/5;
		String background1 = "rgb("+(int)red+","+(int)green+","+(int)blue+")";
		String background2 = "rgb("+(int)redNext+","+(int)green+","+(int)blueNext+")";
		String text = "MuVi";
		int col = globalID.getColumn();
		int row = globalID.getRow();

		String style= "color:white;background:linear-gradient("+background1+","+background2+")";
		return "apps/textcolorpart.jsp?text="+text+"&style="+style+"&col="+col+"&row="+row;
		
	}
	
	@RequestMapping(value = "customStartWebsocket.do")
	public String newStartRedirection(HttpServletRequest request, ModelMap model) {
		
		String redirectURL = request.getParameter("url");
		Integer dc = Integer.parseInt(request.getParameter("DC"));
		DisplayComputer displayComputer = null;
		if (dc==1)		{
			displayComputer= dcManager.getDC1();
		}		
		else if (dc==2) {
			displayComputer= dcManager.getDC2();
		}
		else if (dc==3) {
			displayComputer= dcManager.getDC3();
		}
		else if (dc==4) {
			displayComputer= dcManager.getDC4();
		}
				
		Integer screen = Integer.parseInt(request.getParameter("Screen"));
		ScreenID globalID = dcManager.getGlobalScreenIDForLocalScreenID(screen, displayComputer);	
		
		String managerURL = database.readConfigValue("managerURL");
		redirectURL = managerURL + "screen.do?id=" + globalID.getId();
		//"javascript: window.location = \""+redirectURL+"\";")
		model.addAttribute("url", redirectURL);
		return "apps/redirect.jsp";
		
	}
	
	public DisplayComputerManager getDcManager() {
		return dcManager;
	}

	public void setDcManager(DisplayComputerManager dcManager) {
		this.dcManager = dcManager;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}
