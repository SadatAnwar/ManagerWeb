/**
 * 
 */
package de.fraunhofer.iao.muvi.managerweb.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.logic.ScreenIDCalculator;

/**
 * @author ostermann
 * 
 */
@Controller
public class WebsocketController {
	
	private Database database;
	
	private String getWebsocketServerURL() {
		String port = "3636";
		String url = database.readConfigValue("managerIP");
		if(database.isDebugMode()) {
			url = "localhost";
		}
		return url + ":" + port + "/manager/websocket";
	}

	@RequestMapping(value = "chat.do")
	public String chat(HttpServletRequest request, ModelMap model) throws Exception	{
		return "websocketTestChat.jsp";
	}
	
	@RequestMapping(value = "screen.do")
	public String showWebsocketClientSite(HttpServletRequest request, ModelMap model) throws Exception	{

		model.addAttribute("wsServerURL", getWebsocketServerURL());
		return "websocketClientSite.jsp"; 
	}
	
	@RequestMapping(value = "video.do")
	public String showWebsocketVideoClientSite(HttpServletRequest request, ModelMap model) throws Exception	{
		int id = Integer.parseInt(request.getParameter("id"));
		
		model.addAttribute("wsServerURL", getWebsocketServerURL());
		return "websocketVideoClientSite.jsp"; 
	}
	
	
	@RequestMapping(value = "remote.do")
	public String showWebsocketRemoteSite(HttpServletRequest request, ModelMap model) throws Exception	{
		model.addAttribute("wsServerURL", getWebsocketServerURL());
		return "websocketRemoteSite.jsp";
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}
	
	
}
