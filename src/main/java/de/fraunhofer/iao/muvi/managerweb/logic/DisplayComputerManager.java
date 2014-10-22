package de.fraunhofer.iao.muvi.managerweb.logic;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.DisplayComputer;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class DisplayComputerManager {

	private static final Log log = LogFactory.getLog(ShowURLOnScreen.class);

	private DisplayComputer DC1;
	private DisplayComputer DC2;
	private DisplayComputer DC3;
	private DisplayComputer DC4;
	
	private ScreenDCMapper dcMapper;

	private Database database;
	
	private MuViState muViState;
	
	public void init() {
		
		try {
		
			Map<ScreenID, Integer> dc1LocalMap = new HashMap<>();	
			String[] DC1screens = database.readConfigValue("DC1screens").split(",");
			for (String DC1screen : DC1screens) {
				if (Utils.isNotEmpty(DC1screen)) {
					Integer id = Integer.parseInt(DC1screen);
					dc1LocalMap.put(new ScreenID(id), Integer.parseInt(database.readConfigValue("localID" + id)));
				}
			}
			DC1 = new DisplayComputer(database.readConfigValue("DC1IP"), database.readStatusFlag("DC1Status"), 1, dc1LocalMap, muViState);
			
			Map<ScreenID, Integer> dc2LocalMap = new HashMap<>();	
			String[] DC2screens = database.readConfigValue("DC2screens").split(",");
			for (String DC2screen : DC2screens) {
				if (Utils.isNotEmpty(DC2screen)) {
					Integer id = Integer.parseInt(DC2screen);
					dc2LocalMap.put(new ScreenID(id), Integer.parseInt(database.readConfigValue("localID" + id)));
				}
			}
			DC2 = new DisplayComputer(database.readConfigValue("DC2IP"), database.readStatusFlag("DC2Status"), 2, dc2LocalMap, muViState);
			
			Map<ScreenID, Integer> dc3LocalMap = new HashMap<>();	
			String[] DC3screens = database.readConfigValue("DC3screens").split(",");
			for (String DC3screen : DC3screens) {
				if (Utils.isNotEmpty(DC3screen)) {
					Integer id = Integer.parseInt(DC3screen);
					dc3LocalMap.put(new ScreenID(id), Integer.parseInt(database.readConfigValue("localID" + id)));
				}
			}
			DC3 = new DisplayComputer(database.readConfigValue("DC3IP"), database.readStatusFlag("DC3Status"), 3, dc3LocalMap, muViState);
			
			Map<ScreenID, Integer> dc4LocalMap = new HashMap<>();	
			String[] DC4screens = database.readConfigValue("DC4screens").split(",");
			for (String DC4screen : DC4screens) {
				if (Utils.isNotEmpty(DC4screen)) {
					Integer id = Integer.parseInt(DC4screen);
					dc4LocalMap.put(new ScreenID(id), Integer.parseInt(database.readConfigValue("localID" + id)));
				}
			}
			DC4 = new DisplayComputer(database.readConfigValue("DC4IP"), database.readStatusFlag("DC4Status"), 4, dc4LocalMap, muViState);

			dcMapper = new ScreenDCMapper(getScreenDCMapFromDatabase());
			
			log.info("ShowURLOnScreen initialized.");
		
		} catch (Exception e) {
			log.error("Error while readig configuration from database: " + e.getMessage(), e);
		}
	}
	
	private Map<ScreenID, DisplayComputer> getScreenDCMapFromDatabase() {
		Map<ScreenID, DisplayComputer> map = new HashMap<>();
		
		screenMapHelper("DC1screens", map, DC1);
		screenMapHelper("DC2screens", map, DC2);
		screenMapHelper("DC3screens", map, DC3);
		screenMapHelper("DC4screens", map, DC4);
		
		return map;
	}
	
	private void screenMapHelper(String config, Map<ScreenID, DisplayComputer> map, DisplayComputer DC){
		try{
			if(DC.isActive()){
				String Screens = database.readConfigValue(config);
				for (String s : Screens.split(",")) {
					map.put(new ScreenID(Integer.parseInt(s)), DC);
				}
			}
		}
		catch(Exception e){
			log.error("Invalid "+ config);
		}
	}
	
	public DisplayComputer getDC1() {
		return DC1;
	}
	
	public DisplayComputer getDC2() {
		return DC2;
	}
	
	public DisplayComputer getDC3() {
		return DC3;
	}
	
	public DisplayComputer getDC4() {
		return DC4;
	}
	
	public void clickEverywhere(DisplayComputer dc) {
		for (int i=1; i <= ScreenIDCalculator.NUMBER_OF_SCREENS; i++) {
			ScreenID screenID = new ScreenID(i);
			if (dcMapper.getDisplayComputerForScreen(screenID).equals(dc)) {
				dcMapper.getDisplayComputerForScreen(screenID).click(screenID);
			}
		}
	}
	
	public Map<ScreenID, URL> filterForDisplayComputer(Map<ScreenID, URL> urls, DisplayComputer dc) {
		Map<ScreenID, URL> dcMap = new HashMap<>();
		for (ScreenID screen : urls.keySet()) {
			if (dcMapper.getDisplayComputerForScreen(screen).equals(dc)) {
				dcMap.put(screen, urls.get(screen));
			}
		}
		return dcMap;
	}

	public boolean checkConnection() {
		if(DC1.checkDc() && DC2.checkDc() && DC3.checkDc()&& DC4.checkDc()){
			return true;
		}
		return false;
	}
	
	public String checkChrome() {
		String dc1 = "0";
		String dc2 = "0";
		String dc3 = "0";
		String dc4 = "0";
		
		if(DC1.checkChrome()){
			dc1 = "1";
		}
		if(DC2.checkChrome()){
			dc2 = "1";
		}
		if(DC3.checkChrome()){
			dc3 = "1";
		}
		if(DC4.checkChrome()){
			dc4 = "1";
		}
		String status = dc1+dc2+dc3+dc4;
		return status;
	}
	
	public String checkSingleConnection() {
		String dc1 = "0";
		String dc2 = "0";
		String dc3 = "0";
		String dc4 = "0";
		
		if(DC1.checkDc()){
			dc1 = "1";
		}
		if(DC2.checkDc()){
			dc2 = "1";
		}
		if(DC3.checkDc()){
			dc3 = "1";
		}
		if(DC4.checkDc()){
			dc4 = "1";
		}
		String status = dc1+dc2+dc3+dc4;
		log.debug("Chrome service status on DC1-DC2-DC3-DC4: " + status);
		return status;
	}
	
	public String checkDcPower() throws IOException {
		String dc1 = "0";
		String dc2 = "0";
		String dc3 = "0";
		String dc4 = "0";
		
//		if(DC1.checkDcPower()){
//			dc1 = "1";
//		}
//		if(DC2.checkDcPower()){
//			dc2 = "1";
//		}
//		if(DC3.checkDcPower()){
//			dc3 = "1";
//		}
//		if(DC4.checkDcPower()){
//			dc4 = "1";
//		}
		
		if(DC1.pingDC()){
			dc1 = "1";
		}
		if(DC2.pingDC()){
			dc2 = "1";
		}
		if(DC3.pingDC()){
			dc3 = "1";
		}
		if(DC4.pingDC()){
			dc4 = "1";
		}
		
		String status = dc1+dc2+dc3+dc4;
		log.debug("Power status for DCs 1 to 4: " + status);
		return status;
		
	}

	public void gotoURL(ScreenID screen, URL url, ScreenState screenState) {
		dcMapper.getDisplayComputerForScreen(screen).gotoURL(screen, url, screenState);
	}

	
	public ScreenID getGlobalScreenIDForLocalScreenID(Integer localID,
			DisplayComputer dc) {
		return dcMapper.getGlobalScreenIDForLocalScreenID(localID, dc);
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public MuViState getMuViState() {
		return muViState;
	}

	public void setMuViState(MuViState muViState) {
		this.muViState = muViState;
	}
	
	
	
	
}



