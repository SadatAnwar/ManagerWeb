package de.fraunhofer.iao.muvi.managerweb.web;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.domain.Text;
import de.fraunhofer.iao.muvi.managerweb.logic.DisplayComputerManager;
import de.fraunhofer.iao.muvi.managerweb.logic.ScreenHandler;
import de.fraunhofer.iao.muvi.managerweb.logic.ShowTextOnScreen;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

@Controller
public class ConfigurationController {

	private static final Log log = LogFactory
			.getLog(ConfigurationController.class);

	private Database database;

	private ScreenHandler screenHandler;
	private DisplayComputerManager dcManager;
	private ShowTextOnScreen showTextOnScreen;

	private static final String[] COLORS = { "background: rgb(238,232,170);",
			"background: rgb(255,255,0);", "background: rgb(255,192,203);",
			"background: rgb(64,224,208);", "background: rgb(224,255,255);",
			"background: rgb(102,205,170);", "background: rgb(245,245,220);",
			"background: rgb(216,191,216);" };
	private int index = 0;

	@RequestMapping(value = "config.do")
	public String getConfiguration(HttpServletRequest request, ModelMap model) {

		model.addAttribute("powerOutletIP",
				database.readConfigValue("powerOutletIP"));

		model.addAttribute("managerIP", database.readConfigValue("managerIP"));
		model.addAttribute("managerURL", database.readConfigValue("managerURL"));

		model.addAttribute("customURL", database.readConfigValue("customURL"));
		
		model.addAttribute("DC1IP", database.readConfigValue("DC1IP"));
		model.addAttribute("DC2IP", database.readConfigValue("DC2IP"));
		model.addAttribute("DC3IP", database.readConfigValue("DC3IP"));
		model.addAttribute("DC4IP", database.readConfigValue("DC4IP"));

		model.addAttribute("DC1screens", database.readConfigValue("DC1screens"));
		model.addAttribute("DC2screens", database.readConfigValue("DC2screens"));
		model.addAttribute("DC3screens", database.readConfigValue("DC3screens"));
		model.addAttribute("DC4screens", database.readConfigValue("DC4screens"));
		
		model.addAttribute("DC1Status", checkDCStatusFlag("DC1Status"));
		model.addAttribute("DC2Status", checkDCStatusFlag("DC2Status"));
		model.addAttribute("DC3Status", checkDCStatusFlag("DC3Status"));
		model.addAttribute("DC4Status", checkDCStatusFlag("DC4Status"));
		
		model.addAttribute("dcUser", database.readConfigValue("dcUser"));
		model.addAttribute("dcPassword", database.readConfigValue("dcPassword"));
		
		String checked = "debugMode".equals(database.readConfigValue("debugMode")) ? "checked" : "";
		
		model.addAttribute("debugModeChecked", checked);
		
		String websocketEnabled = "websocketEnabled".equals(database.readConfigValue("websocketEnabled")) ? "checked" : "";
		
		model.addAttribute("websocketEnabled", websocketEnabled);		

		/* Initialize all 36 screen mappings */

		for (int i = 1; i <= 36; i++) {
			model.addAttribute("localID" + i,
					database.readConfigValue("localID" + i));
		}

		return "config.jsp";
	}
	private String checkDCStatusFlag (String DC){
		
		if(Integer.parseInt(database.readConfigValue(DC)) == 1){
			return "checked";
		}
		else
			return "";
	}
	
	private void setDCStatusFlag(HttpServletRequest request){
		boolean DC1 = request.getParameter("DC1Status")!=null;
		boolean DC2 = request.getParameter("DC2Status")!=null;
		boolean DC3 = request.getParameter("DC3Status")!=null;
		boolean DC4 = request.getParameter("DC4Status")!=null;
		
		database.setStatusFlag("DC1Status", DC1);
		database.setStatusFlag("DC2Status", DC2);
		database.setStatusFlag("DC3Status", DC3);
		database.setStatusFlag("DC4Status", DC4);
	}

	@RequestMapping(value = "saveConfig.do", method = RequestMethod.POST)
	public String saveConfiguration(HttpServletRequest request) {
		
		String debugMode = request.getParameter("debugMode");
		if ("debugMode".equals(debugMode)) {
			database.saveOrUpdateConfigValue("debugMode", "debugMode");
		} else {
			database.saveOrUpdateConfigValue("debugMode", "");
		}
		
		String websocketEnabled = request.getParameter("websocketEnabled");
		if ("websocketEnabled".equals(websocketEnabled)) {
			
			database.saveOrUpdateConfigValue("websocketEnabled", "websocketEnabled");
		} else {
			database.saveOrUpdateConfigValue("websocketEnabled", "");
		}
		screenHandler.setWebsocketEnabled("websocketEnabled".equals(websocketEnabled));
		screenHandler.performSwitch();
		
		setDCStatusFlag(request);
				
		String powerOutletIP = request.getParameter("powerOutletIP");

		if (Utils.validateIP(powerOutletIP)) {
			database.saveOrUpdateConfigValue("powerOutletIP", powerOutletIP);
		} else {
			log.error("powerOutletIP " + powerOutletIP + " is not valid.");
		}

		String DC1IP = request.getParameter("DC1IP");
		String DC2IP = request.getParameter("DC2IP");
		String DC3IP = request.getParameter("DC3IP");
		String DC4IP = request.getParameter("DC4IP");

		if (Utils.validateIP(DC1IP)||DC1IP.isEmpty()) {
			database.saveOrUpdateConfigValue("DC1IP", DC1IP);
		} else {
			log.error("DC1IP " + DC1IP + " is not valid.");
		}

		if (Utils.validateIP(DC2IP)||DC2IP.isEmpty()) {
			database.saveOrUpdateConfigValue("DC2IP", DC2IP);
		} else {
			log.error("DC2IP " + DC2IP + " is not valid.");
		}

		if (Utils.validateIP(DC3IP)||DC3IP.isEmpty()) {
			database.saveOrUpdateConfigValue("DC3IP", DC3IP);
		} else {
			log.error("DC3IP " + DC3IP + " is not valid.");
		}
		
		if (Utils.validateIP(DC4IP)||DC4IP.isEmpty()) {
			database.saveOrUpdateConfigValue("DC4IP", DC4IP);
		} else {
			log.error("DC4IP " + DC4IP + " is not valid.");
		}


		String DC1screens = request.getParameter("DC1screens");
		String DC2screens = request.getParameter("DC2screens");
		String DC3screens = request.getParameter("DC3screens");
		String DC4screens = request.getParameter("DC4screens");

		database.saveOrUpdateConfigValue("DC1screens", DC1screens);
		database.saveOrUpdateConfigValue("DC2screens", DC2screens);
		database.saveOrUpdateConfigValue("DC3screens", DC3screens);
		database.saveOrUpdateConfigValue("DC4screens", DC4screens);

		String managerURL = request.getParameter("managerURL");
		database.saveOrUpdateConfigValue("managerURL", managerURL);
		
		String customURL = request.getParameter("customURL");
		database.saveOrUpdateConfigValue("customURL", customURL);

		String managerIP = request.getParameter("managerIP");
		database.saveOrUpdateConfigValue("managerIP", managerIP);
		
		String dcPassword = request.getParameter("dcPassword");
		database.saveOrUpdateConfigValue("dcPassword", dcPassword);
		
		String dcUser = request.getParameter("dcUser");
		database.saveOrUpdateConfigValue("dcUser", dcUser);

		/* Save 36 screen mappings */
		for (int i = 1; i <= 36; i++) {
			String mappingI = request.getParameter("localID" + i);
			database.saveOrUpdateConfigValue("localID" + i, mappingI);
		}

		/* Reset settings */
		dcManager.init();

		/* Show numbers if asked to */
		if (Utils.isNotEmpty(request.getParameter("showNumbers"))) {
			log.debug("Show numbers...");
			Map<ScreenID, URL> map = new HashMap<>();
			String background = nextBackground();
			for (int i = 1; i <= 36; i++) {
				map.put(new ScreenID(i), showTextOnScreen.getURLForText(new Text("" + i, background)));
			}
			screenHandler.showUrlMapOnScreens(map, ScreenState.Text);
		}

		log.debug("Configuration saved.");

		return "config.do";
	}

	private String nextBackground() {
		index++;
		return COLORS[index % 8];
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}


	public ShowTextOnScreen getShowTextOnScreen() {
		return showTextOnScreen;
	}

	public void setShowTextOnScreen(ShowTextOnScreen showTextOnScreen) {
		this.showTextOnScreen = showTextOnScreen;
	}
	public ScreenHandler getScreenHandler() {
		return screenHandler;
	}
	public void setScreenHandler(ScreenHandler screenHandler) {
		this.screenHandler = screenHandler;
	}
	public DisplayComputerManager getDcManager() {
		return dcManager;
	}
	public void setDcManager(DisplayComputerManager dcManager) {
		this.dcManager = dcManager;
	}
	
	

}
