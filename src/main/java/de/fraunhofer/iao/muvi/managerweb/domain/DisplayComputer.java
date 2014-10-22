package de.fraunhofer.iao.muvi.managerweb.domain;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.fraunhofer.iao.muvi.managerweb.logic.MuViState;
import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

public class DisplayComputer {
	
	private static final Log log = LogFactory.getLog(DisplayComputer.class);

	private String IP;
	
	private Integer id;
	
	private URL cwsrUrl;
	
	private ChromeWebServiceRemote CWSR;
	
	private Map<ScreenID, Integer> localIDMap;
	
	private String screenShotPath ;

	private boolean active ;

	private MuViState muViState;
	
	public DisplayComputer (String IP, boolean status, Integer id, Map<ScreenID, Integer> localIDMap, MuViState muViState) {
	
		this.IP = IP;
		this.id = id;
		this.cwsrUrl = getChromeWebServiceRemoteUrl(IP);
		this.CWSR = new ChromeWebServiceRemote(this.cwsrUrl, this);
		this.localIDMap = localIDMap;
		this.muViState = muViState;
		this.screenShotPath = "C:\\CWM\\ScreenShot\\DC"+String.valueOf(id);
		this.active = status;
		
	
	}
	

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public URL getCwsrUrl() {
		return cwsrUrl;
	}

	public void setCwsrUrl(URL cwsrUrl) {
		this.cwsrUrl = cwsrUrl;
	}
	
	
	public Map<ScreenID, Integer> getLocalIDMap() {
		return localIDMap;
	}

	public void setLocalIDMap(Map<ScreenID, Integer> localIDMap) {
		this.localIDMap = localIDMap;
	}
	
	private static URL getChromeWebServiceRemoteUrl(String dcIP) {
		try {
			return new URL("http://" + dcIP + ":9000/ChromeService/");
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("IP not valid: " + dcIP);
		}
	}

	public ChromeWebServiceRemote getCWSR() {
		return CWSR;
	}

	public void setCWSR(ChromeWebServiceRemote cWSR) {
		CWSR = cWSR;
	}
	
	public String getScreenShotPath() {
		return screenShotPath;
	}

	public void setScreenShotPath(String screenShotPath) {
		this.screenShotPath = screenShotPath;
	}

	private String gotoURL(int localScreenID, URL url) {
		String urlAsString = url.toString();
		
		try {
			return this.CWSR.gotoURL(localScreenID, urlAsString);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	private String click(int localScreenID) {
		
		try {
			return this.CWSR.click(localScreenID);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
		
	public void startChrome() {
		log.debug("Start Chrome on all monitors on " + this.IP + "...");
		this.CWSR.startchrome();
	}
	public boolean checkChrome() {
		log.debug("Checking Chrome " + this.IP + "...");
		return this.CWSR.checkChrome();
	}
	
	public boolean checkDc() {
		boolean dcOK = this.CWSR.checkDC();
		return dcOK;
	}
	
	public boolean checkDcPower() throws IOException {
		if(!active){
			return true;
		}
		else if(Utils.validateIP(IP)){
			InetAddress inet = InetAddress.getByName(IP);
			return inet.isReachable(2000);
		}
		else {
			throw new IllegalArgumentException("IP not valid: " + IP);
		}
	}
	
	public boolean pingDC() {
		if (!active) {
			return true;
		}
		try {
			ProcessBuilder processBuilder = new ProcessBuilder("ping","-n" ,"1", this.IP);
		    Process proc = processBuilder.start();
		    int returnVal = proc.waitFor();
		    return returnVal == 0;
		} catch (Exception e) {
			log.error("Error pinging " + this.IP + ": " + e.getMessage());
			return false;
		}
	}
	
	public void startChrome(URL url) {
		log.debug("Start Chrome on all monitors on " + this.IP + " and show " + url.toString() + "...");
		this.CWSR.startchrome(this.id, url);
	}
	
	public void killChrome() {
		log.debug("Kill Chrome on all monitors on " + this.IP + "...");
		this.CWSR.killchrome(0);
	}
	
	public void startScreenshot() {
		log.debug("Take screenshot of all screens on DC"+this.id);
		this.CWSR.TakeScreenShot(this.id);
	}
	
	public void gotoURL(ScreenID screen, URL url, ScreenState screenState) {
		if (localIDMap != null && localIDMap.containsKey(screen)) {
			log.info("Show " + url.toString() + " on screen " + screen + " on IP " + this.IP + " (local screen number: " + localIDMap.get(screen) + ").");
			String result = gotoURL(localIDMap.get(screen), url);
			if (Utils.isNotEmpty(result)) {
				muViState.updateState(screen, url, screenState);
			} else {
				muViState.updateState(screen, url, ScreenState.Error);
			}
		} else {
			throw new IllegalArgumentException("The given screen ID " + screen + " is not mapped on this display computer (" + this.IP + ").");
		}
	}
	
	public void click(ScreenID screen) {
		if (localIDMap != null && localIDMap.containsKey(screen)) {
			log.info("CLick on screen " + screen + " on IP " + this.IP + " (local screen number: " + localIDMap.get(screen) + ").");
			String result = click(localIDMap.get(screen));
			log.debug("Click result: " + result);
		} else {
			throw new IllegalArgumentException("The given screen ID " + screen + " is not mapped on this display computer (" + this.IP + ").");
		}
	}
	
	public int getDcScreenCount() {
		int count = this.localIDMap.size();
		return count;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DisplayComputer) {
			if (obj != null) {
				DisplayComputer other = (DisplayComputer) obj;
				if (Utils.isNotEmpty(this.IP)) {
					return this.IP.equals(other.getIP());
				}
			}
		}
		return false;
	}

	public MuViState getMuViState() {
		return muViState;
	}

	public void setMuViState(MuViState muViState) {
		this.muViState = muViState;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
		
	}
		
}
