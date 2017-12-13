package com.exmaple.model;

public class WifiInfomation {
	public long id;
	public String apNum;
	public String bssid;
	public String ssid;
	public double  level;
	
	public WifiInfomation(long m,String apNum,String SSID,String BSSID,double d){
		this.id = m;
		this.apNum = apNum;
		bssid = BSSID;
		ssid = SSID;
		level = d;
	}
	
	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApNum() {
		return apNum;
	}

	public void setApNum(String apNum) {
		this.apNum = apNum;
	}

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public double getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String toString(){
		return "SSID" + ssid + "\nBSSID" + bssid+ "\nlevel" + level;
	}
}
