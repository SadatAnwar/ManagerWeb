package de.fraunhofer.iao.muvi.managerweb.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ChromeWebServiceRemote {
	
	private static final Log log = LogFactory.getLog(ChromeWebServiceRemote.class);

	private String baseUrl;
	private DisplayComputer dc;

	/**
	 * 
	 * @param url
	 */
	public ChromeWebServiceRemote(String url, DisplayComputer dc) {
		this.setBaseUrl(url);
		this.dc = dc;
	}
	
	public ChromeWebServiceRemote(URL url, DisplayComputer dc) {
		if (url != null) {
			this.setBaseUrl(url.toString());
			this.dc = dc;
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * 
	 * @param url
	 */
	private void setBaseUrl(String url) {
		this.baseUrl = url;
	}
	
	public DisplayComputer getDc() {
		return dc;
	}

	public void setDc(DisplayComputer dc) {
		this.dc = dc;
	}

	/**
	 * 
	 * @return
	 */
	public String readFromURL(String appendix) {
		URL url;
		String text = "";
		if(dc.isActive()){
			try {
				url = new URL(this.getBaseUrl() + appendix);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				
				//timeout for lazy connections with the Chrome plugin
				connection.setConnectTimeout(60000);
				connection.setReadTimeout(60000);
				connection.setRequestMethod("GET");

				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					String inputLine;
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),
									"UTF-8"));
					while ((inputLine = reader.readLine()) != null) {
						text += inputLine;
					}
					reader.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}		
		return text;
	}
	
	public boolean checkDC() {
		if(dc.isActive()) {
			URL url;
			boolean status = false;
			String text = "";
			try {
				url = new URL(this.getBaseUrl() + "check");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				
				//timeout for lazy connections with the Chrome plugin
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				connection.setRequestMethod("GET");

				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					String inputLine;
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),
									"UTF-8"));
					while ((inputLine = reader.readLine()) != null) {
						text += inputLine;
					}
					reader.close();
				}
			} catch (Exception e) {
			}
			if(text.contains("STARTED OK")){
				status= true;
			}
			return status;
		}
		return true;	
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public String parseXML(String input) {
		return "";
	}
	
	public String executeCommand(String command){
		
		return "";
	}

	public String startchrome() {
		String temp = "";
		temp = this.readFromURL("InitBrowser");
		log.debug(temp);
		return temp;
	}
	
	public boolean checkChrome() {
		String temp = "";
		temp = this.readFromURL("checkChrome");
		log.debug(temp);
		String positive = "CHROME_ALIVE";
		if(temp.contains(positive)){
			return true;
		}
		else
			return false;
	}
	
	public String startchrome(int DC, URL url) {
		//start Browser only on display with id ?
		String temp = "";
		temp = this.readFromURL("InitBrowserURL?DC="+DC+""+"&url="+url);
		log.debug("startchrome on DC " + DC + " with URL " + url + " result: " + temp);
		return temp;
	}

	public String gotoURL(int displayid, String url) {
		String answer = "";
		answer = this.readFromURL("GoToUrl?display="+displayid+"&url="+url+"");
		log.debug("gotoURL displayid: " + displayid + ", url: " + url + " answered:" + answer);
		return answer;
	}

	public String refreshBrowser(int displayid) {
		String temp = "";
		temp = this.readFromURL("Refresh?display="+displayid+"");
		log.debug(temp);
		return temp;
	}

	public String scrollbrowser(int displayid, String x, String y) {
		// TODO rly needed?
		String temp = "";
		temp = this.readFromURL("?display="+displayid+"");
		log.debug(temp);
		return temp;
	}
	
	public String click(int displayid) {
		String temp = "";
		temp = this.readFromURL("Click?display="+displayid+"");
		log.debug("Click on display " + displayid + " result: " + temp);
		return temp;
	}

	public String zoomBrowser(int displayid, String zoomlevel) {
		/*
		 * }else if(command.contains("zoomin")){
		 * this.zoomBrowser(displayid, "0.1");
		}else if(command.contains("zoomzero")){
			this.zoomBrowser(displayid, "1.0");
		}else if(command.contains("zoomout")){
			this.zoomBrowser(displayid, "-0.1");
		 */
		
		if(zoomlevel.equals("0.1")){
			String temp = "";
			temp = this.readFromURL("ZoomIn?display="+displayid+"");
			log.debug(temp);
			return temp;
		} else if (zoomlevel.equals("1.0")){
			String temp = "";
			temp = this.readFromURL("ZoomZero?display="+displayid+"");
			log.debug(temp);
			return temp;
		} else if (zoomlevel.equals("-0.1")){
			String temp = "";
			temp = this.readFromURL("ZoomOut?display="+displayid+"");
			log.debug(temp);
			return temp;
		} else {
			log.info("Not a supported zoomlevel?");
			return "Not a supported zoomlevel?";
		}
		
	}

	public String zoomBrowserTextEnlarge(int displayid) {
		//NotNeeded?
		String temp = "";
		temp = this.readFromURL("?display="+displayid+"");
		log.debug(temp);
		return temp;
	}

	public String zoomBrowserTextReduce(int displayid) {
		//NotNeeded?
		String temp = "";
		temp = this.readFromURL("?display="+displayid+"");
		log.debug(temp);
		return temp;
	}

	public String zoomBrowserTextReset(int displayid) {
		//NotNeeded?
		String temp = "";
		temp = this.readFromURL("?display="+displayid+"");
		log.debug(temp);
		return temp;
	}

	public String minimizeBrowser(int displayid) {
		String temp = "";
		temp = this.readFromURL("Minimize?display="+displayid+"");
		log.debug(temp);
		return temp;
	}

	public String maximizeBrowser(int displayid) {
		String temp = "";
		temp = this.readFromURL("Maximizie?display="+displayid+"");
		log.debug(temp);
		return temp;
	}

	public String fullscreenBrowser(int displayid) {
		String temp = "";
		temp = this.readFromURL("Fullscreen?display="+displayid+"&status=true");
		log.debug(temp);
		return temp;
	}

	public String scrollBrowserTop(int displayid) {
		String temp = "";
		temp = this.readFromURL("PageTop?display="+displayid+"");
		log.debug(temp);
		return temp;
	}

	public String scrollBrowserBottom(int displayid) {
		String temp = "";
		temp = this.readFromURL("PageBottom?display="+displayid+"");
		log.debug(temp);
		return temp;
	}

	public String scrollBrowserPageUp(int displayid) {
		String temp = "";
		temp = this.readFromURL("PageUp?display="+displayid+"");
		log.debug(temp);
		return temp;
	}

	public String scrollBrowserPageDown(int displayid) {
		String temp = "";
		temp = this.readFromURL("PageDown?display="+displayid+"");
		log.debug(temp);
		return temp;
	}

	public String killchrome(int displayid) {
		String temp = "";
		temp = this.readFromURL("KillBrowser?display="+displayid+"");
		log.debug(temp);
		return temp;
	}
	
	
	public String TakeScreenShot(int dc) {
		String temp = "";
		temp = this.readFromURL("TakeScreenShot?DC="+dc+"");
		log.debug(temp);
		return temp;
		
	}
	
	public String TakeScreenShot(int displayid, String path) {
		String temp = "";
		temp = this.readFromURL("TakeScreenShotPath?display="+displayid+"&path="+path+"");
		log.debug(temp);
		return temp;
		
	}
}
