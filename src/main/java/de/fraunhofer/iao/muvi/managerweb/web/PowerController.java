package de.fraunhofer.iao.muvi.managerweb.web;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.fraunhofer.iao.muvi.managerweb.logic.DisplayComputerManager;
import de.fraunhofer.iao.muvi.managerweb.logic.ScreenHandler;
import de.fraunhofer.iao.muvi.managerweb.power.StartStop;

@Controller
public class PowerController {
	
	private static final Log log = LogFactory
			.getLog(PowerController.class);
	private StartStop iPower;
	private ScreenHandler screenHandler;
	private DisplayComputerManager dcManager;
	
	@RequestMapping(value = "interrupt.do")
	public String interrupt() {
		screenHandler.interrupt();
		return "power.jsp";
	}
	
	@RequestMapping(value = "powerStatus.do")
	public String powerStatus(HttpServletRequest request, ModelMap model) throws Exception	{
		
		model.addAttribute("powerOutletIP",iPower.getPowerIp());
		
		String[] sockets= iPower.powerStatus();
		String logMsg = "Sockets:";
		for (int i =0 ; i<sockets.length; i++){
			String socName = "Socket"+(i+1);
			model.addAttribute(socName,sockets[i]);
			logMsg += " (" + (i+1) +"-" + sockets[i] + ")";
		}
		log.info(logMsg);
		return "power2.jsp";
	}
	

	@RequestMapping(value = "powerOffAjax.do")
	@ResponseBody
	public String powerOffAjax(HttpServletRequest request, ModelMap model) throws Exception	{
		log.debug("Started power off");
		if (!isDebugMode()) {
			iPower.shutDown();
		}
		log.info("Power off Complete");
		return "powerStatus.do";
	}
	
	@RequestMapping(value = "startChrome.do")
	@ResponseBody
	public String startChrome(HttpServletRequest request, ModelMap model) throws Exception	{
		log.debug("Started start Chrome");
		
		int sysNumber = 0;
		
		boolean debugMode = isDebugMode();
				
		if (request.getParameter("sysNumber") != null) {
			sysNumber = Integer.parseInt(request.getParameter("sysNumber"));
		}
		
		Runnable r1 = new Runnable() {
			public void run() {
				dcManager.getDC1().startChrome();
			}
		};

		Runnable r2 = new Runnable() {
			public void run() {
				dcManager.getDC2().startChrome();
			}
		};

		Runnable r3 = new Runnable() {
			public void run() {
				dcManager.getDC3().startChrome();
			}
		};
		Runnable r4 = new Runnable() {
			public void run() {
				dcManager.getDC4().startChrome();
			}
		};
		if(dcManager.checkConnection()){
			Thread t1 = new Thread(r1);
			Thread t2 = new Thread(r2);
			Thread t3 = new Thread(r3);
			Thread t4 = new Thread(r4);
	
			if (sysNumber == 0 || sysNumber == 1) {
				if (!debugMode) {
					t1.start();
				}
				log.debug("Start Chrome on DC1.");
			}
			if (sysNumber == 0 || sysNumber == 2) {
				if (!debugMode) {
					t2.start();
				}
				log.debug("Start Chrome on DC2.");
			}
			if (sysNumber == 0 || sysNumber == 3) {
				if (!debugMode) {
					t3.start();
				}
				log.debug("Start Chrome on DC3.");
			}
			if (sysNumber == 0 || sysNumber == 4) {
				if (!debugMode) {
					t4.start();
				}
				log.debug("Start Chrome on DC4.");
			}
			log.info("Start Chrome Complete");
						
			return "Success";
		}

		
		log.info("Chrome service not started on some DC(s)");
		return "Error";
	}
	
	
	@RequestMapping(value = "startChromeURL.do")
	@ResponseBody
	public String startChromeURL(HttpServletRequest request, ModelMap model) throws Exception	{
		log.debug("Started start Chrome");
		
		int sysNumber = 0;
		if (request.getParameter("sysNumber") != null) {
			sysNumber = Integer.parseInt(request.getParameter("sysNumber"));
		}
		
		boolean debugMode = isDebugMode();
		boolean status = dcManager.checkConnection();
		if(sysNumber>0){
			status = true;	
		}
		
		String startURL = screenHandler.getChromeStartURL();
		if(screenHandler.websocketEnabled()) {
			startURL = screenHandler.getWebsocketStartURL();
			//screenHandler.switchToWebsocketFacadeURL();
		}

		final URL url= new URL(startURL);
		
		Runnable r1 = new Runnable() {
			public void run() {
				dcManager.getDC1().startChrome(url);
				
				/* Try to give the focus to the windows... */
				dcManager.clickEverywhere(dcManager.getDC1());
				log.info("Clicked everywhere!");
				
			}
		};

		Runnable r2 = new Runnable() {
			public void run() {
				dcManager.getDC2().startChrome(url);
				
				/* Try to give the focus to the windows... */
				dcManager.clickEverywhere(dcManager.getDC2());
				log.info("Clicked everywhere!");
				
			}
		};

		Runnable r3 = new Runnable() {
			public void run() {
				dcManager.getDC3().startChrome(url);
				
				/* Try to give the focus to the windows... */
				dcManager.clickEverywhere(dcManager.getDC3());
				log.info("Clicked everywhere!");
				
			}
		};
		
		Runnable r4 = new Runnable() {
			public void run() {
				dcManager.getDC4().startChrome(url);
				
				/* Try to give the focus to the windows... */
				dcManager.clickEverywhere(dcManager.getDC4());
				log.info("Clicked everywhere!");
				
			}
		};

		if(status){
			Thread t1 = new Thread(r1);
			Thread t2 = new Thread(r2);
			Thread t3 = new Thread(r3);
			Thread t4 = new Thread(r4);
	
			if (sysNumber == 0 || sysNumber == 1) {
				if (!debugMode) {
					t1.start();
				}
				log.debug("Start Chrome on DC1.");
			}
			if (sysNumber == 0 || sysNumber == 2) {
				if (!debugMode) {
					t2.start();
				}
				log.debug("Start Chrome on DC2.");
			}
			if (sysNumber == 0 || sysNumber == 3) {
				if (!debugMode) {
					t3.start();
				}
				log.debug("Start Chrome on DC3.");
			}
			if (sysNumber == 0 || sysNumber == 4) {
				if (!debugMode) {
					t4.start();
				}
				log.debug("Start Chrome on DC4"
						+ ""
						+ ".");
			}
			

			
			log.info("Start Chrome Complete");
			return "Success";
		}

		
		log.info("Chrome service not started on some DC(s)");
		return "Error";
	}
	
	
	@RequestMapping(value = "killChrome.do")
	@ResponseBody
	public String killChrome(HttpServletRequest request, ModelMap model) throws Exception	{
		log.debug("Started start Chrome");
		
		int sysNumber = 0;
		
		boolean debugMode = isDebugMode();
				
		if (request.getParameter("sysNumber") != null) {
			sysNumber = Integer.parseInt(request.getParameter("sysNumber"));
		}
		
		Runnable r1 = new Runnable() {
			public void run() {
				dcManager.getDC1().killChrome();
			}
		};

		Runnable r2 = new Runnable() {
			public void run() {
				dcManager.getDC2().killChrome();
			}
		};

		Runnable r3 = new Runnable() {
			public void run() {
				dcManager.getDC3().killChrome();
			}
		};
		
		Runnable r4 = new Runnable() {
			public void run() {
				dcManager.getDC4().killChrome();
			}
		};

		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		Thread t3 = new Thread(r3);
		Thread t4 = new Thread(r4);

		if (sysNumber == 0 || sysNumber == 1) {
			if (!debugMode) {
				t1.start();
			}
			log.debug("Kill Chrome on DC1.");
		}
		if (sysNumber == 0 || sysNumber == 2) {
			if (!debugMode) {
				t2.start();
			}
			log.debug("Kill Chrome on DC2.");
		}
		if (sysNumber == 0 || sysNumber == 3) {
			if (!debugMode) {
				t3.start();
			}
			log.debug("Kill Chrome on DC3.");
		}
		if (sysNumber == 0 || sysNumber == 4) {
			if (!debugMode) {
				t4.start();
			}
			log.debug("Kill Chrome on DC4.");
		}

		t1.join();
		t2.join();
		t3.join();
		log.info("Kill Chrome Complete");
		return "powerStatus.do";
	}
	
	@RequestMapping(value = "restartChrome.do")
	@ResponseBody
	public String restartChrome(HttpServletRequest request, ModelMap model) throws Exception{
		killChrome(request, model);
		startChromeURL(request, model);
		return null;
	}
	
	
	@RequestMapping(value = "powerOnAjax.do")
	@ResponseBody
	public String powerOnAjax(HttpServletRequest request, ModelMap model) throws Exception	{
		String parameter = request.getParameter("outletNumber");
		int port = (Integer.parseInt(parameter));
		String startSeq="\"";
		String[] sockets= iPower.powerStatus();
		sockets[port]="1";
		for (int i =0 ; i<sockets.length-1; i++){
			startSeq= startSeq+sockets[i]+",";
		}
		startSeq= startSeq+sockets[sockets.length-1]+"\"";
		String ret = "";
		if (!isDebugMode()) {
			ret= iPower.startupSwitchSingle(startSeq,sockets);
		}
		
		log.info(ret);
		
		return ret;
	}
	
	@RequestMapping(value = "restartComputer.do")
	@ResponseBody
	public String restartComputer(HttpServletRequest request, ModelMap model) throws Exception	{
		String sysNumber = request.getParameter("sysNumber");
		log.debug("restarting "+sysNumber);
		if (!isDebugMode()) {
			log.info(iPower.shutdownDC("restart", sysNumber));
		}
		return "powerStatus.do";
	}
	
	
	@RequestMapping(value = "powerOnComputer.do")
	@ResponseBody
	public String powerOnComputer(HttpServletRequest request, ModelMap model) throws Exception	{
		log.debug("Powering on all computers");
		String response = iPower.dCStartUp();
		if (!isDebugMode()) {
			log.info(response);
		}
		return response;
	}
	
	/*
	 * Beta function to startup the wall with one click
	 * first check if computers are already switched on? 
	 * Yes? Proceed with Startup
	 * No? Stop here and ask user to manually startup computers first
	 * 
	 * Startup:
	 * 1. Start up the displays
	 * 2. simultaneously restart computers
	 * 3. wait for all computers to become active
	 * 4. once all computers are active check if chrome service is running on all 
	 * 5. when chrome service starts, start chrome and give feedback
	 * 
	 */
	@RequestMapping(value = "oneClickStartUp.do")
	@ResponseBody
	public String oneClickStartUp(HttpServletRequest request, ModelMap model){
	
		String command = null;
		command = request.getParameter("command");
		String dcSwitchedOff =" ";
		
		if(command.equals("checkDCs")){
			String status = "0000";
				try {
					status = dcManager.checkDcPower();
				} catch (IOException e) {
					return "Error Communicating with DCs, Please try again";
				}
				if(status.equals("1111")){
					return "OK";
				}
				else 
					{
					if(status.matches("0[1,0][1,0][1,0]")){
						dcSwitchedOff = dcSwitchedOff+"DC1;";
					}
					if(status.matches("[1,0]0[1,0][1,0]")){
						dcSwitchedOff = dcSwitchedOff+"DC2;";
					}
					if(status.matches("[1,0][1,0]0[1,0]")){
						dcSwitchedOff = dcSwitchedOff+"DC3;";
					}
					if(status.matches("[1,0][1,0][1,0]0")){
						dcSwitchedOff = dcSwitchedOff+"DC4;";
					}
					return "Error :"+dcSwitchedOff;
				}
		}
		if(command.equals("waitForDC")){
			while(!dcManager.checkConnection()){
				try {
					Thread.sleep(5000);
				} catch(Exception e) {
				}
			}
			if(dcManager.checkConnection()){
				return "OK";
			}
		}
		
		return "powerStatus.do";
	}
	
	@RequestMapping(value = "checkDcPower.do")
	@ResponseBody
	public String checkDcPower() throws JSONException{
		JSONObject reply = new JSONObject();
		String status = "";
		try {
			status =iPower.dCPowerCheck();
		} catch (Exception e) {
			reply.put("Error", String.valueOf(true));
			reply.put("Error msg", "Error Communicating with DCs, Please try again");
			return reply.toString();
		}
		return status;
	}
	
	@RequestMapping(value = "checkChromeService.do")
	@ResponseBody
	public String checkChromeService(){
		
		String status = "0000";
		status = dcManager.checkSingleConnection();
		return status;
	}
	
	@RequestMapping(value = "checkChromeWindow.do")
	@ResponseBody
	public String checkChrome(){
		
		String status = "0000";
		status = dcManager.checkChrome();
		return status;
	}
	
	
	@RequestMapping(value = "shutdownComputer.do")
	@ResponseBody
	public String shutdownComputer(HttpServletRequest request, ModelMap model) throws Exception	{
		String sysNumber = request.getParameter("sysNumber");
		log.debug("Shutting Donw "+sysNumber);
		if (!isDebugMode()) {
			log.info(iPower.shutdownDC("shutdown", sysNumber));
		}
		return "powerStatus.do";
	}
	
	private boolean isDebugMode() {
		return "debugMode".equals(dcManager.getDatabase().readConfigValue("debugMode"));
	}
	

	public StartStop getiPower() {
		return iPower;
	}

	public void setiPower(StartStop iPower) {
		this.iPower = iPower;
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
