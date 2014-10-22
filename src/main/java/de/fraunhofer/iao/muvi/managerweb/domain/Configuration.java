package de.fraunhofer.iao.muvi.managerweb.domain;

public class Configuration {

	private String powerOutletIP;
	
	private String DC1IP;
	private String DC2IP;
	private String DC3IP;
	private String DC4IP;

	public String getPowerOutletIP() {
		return powerOutletIP;
	}

	public void setPowerOutletIP(String powerOutletIP) {
		this.powerOutletIP = powerOutletIP;
	}

	public String getDC1IP() {
		return DC1IP;
	}

	public void setDC1IP(String dC1IP) {
		DC1IP = dC1IP;
	}

	public String getDC2IP() {
		return DC2IP;
	}

	public void setDC2IP(String dC2IP) {
		DC2IP = dC2IP;
	}

	public String getDC3IP() {
		return DC3IP;
	}

	public void setDC3IP(String dC3IP) {
		DC3IP = dC3IP;
	}
	
	public String getDC4IP() {
		return DC4IP;
	}

	public void setDC4IP(String dC4IP) {
		DC4IP = dC4IP;
	}
}
