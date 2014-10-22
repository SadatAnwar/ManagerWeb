package de.fraunhofer.iao.muvi.managerweb.logic;

import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fraunhofer.iao.muvi.managerweb.backend.Database;
import de.fraunhofer.iao.muvi.managerweb.domain.DisplayComputer;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;

public class ChromeServiceWindowFacade implements IScreenHandler {

	private static final Log log = LogFactory.getLog(ChromeServiceWindowFacade.class);
	
	private ShowUrlsOnScreens suos1;
	private ShowUrlsOnScreens suos2;
	private ShowUrlsOnScreens suos3;
	private ShowUrlsOnScreens suos4;
	
	private DisplayComputerManager dcManager;
	
	private Database database;


	@Override
	public void showUrlMapOnScreens(Map<ScreenID, URL> urls,
			ScreenState screenState) {
		
		DisplayComputer DC1 = dcManager.getDC1();
		DisplayComputer DC2 = dcManager.getDC2();
		DisplayComputer DC3 = dcManager.getDC3();
		DisplayComputer DC4 = dcManager.getDC4();
		
		
		Map<ScreenID, URL> dc1map = dcManager.filterForDisplayComputer(urls, DC1);
		Map<ScreenID, URL> dc2map = dcManager.filterForDisplayComputer(urls, DC2);
		Map<ScreenID, URL> dc3map = dcManager.filterForDisplayComputer(urls, DC3);
		Map<ScreenID, URL> dc4map = dcManager.filterForDisplayComputer(urls, DC4);
		
		boolean debugMode = "debugMode".equals(database.readConfigValue("debugMode"));
		
		suos1 = new ShowUrlsOnScreens(dc1map, DC1, debugMode, screenState);
		suos2 = new ShowUrlsOnScreens(dc2map, DC2, debugMode, screenState);
		suos3 = new ShowUrlsOnScreens(dc3map, DC3, debugMode, screenState);
		suos4 = new ShowUrlsOnScreens(dc4map, DC4, debugMode, screenState);

		suos1.start();
		suos2.start();
		suos3.start();
		suos4.start();
	}

	@Override
	public void showUrlOnScreen(URL url, ScreenID screen,
			ScreenState screenState) {
		dcManager.gotoURL(screen, url, screenState);
	}

	@Override
	public void interrupt() {
		try {
			log.info("Try to interrupt show URLs on DC1...");
			suos1.interrupt();
			log.info("Try to interrupt show URLs on DC2...");
			suos2.interrupt();
			log.info("Try to interrupt show URLs on DC3...");
			suos3.interrupt();
			log.info("Try to interrupt show URLs on DC4...");
			suos4.interrupt();
			log.info("Interrupted!");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
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
