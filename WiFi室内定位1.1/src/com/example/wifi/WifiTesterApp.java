package com.example.wifi;

import android.app.Application;

public class WifiTesterApp extends Application {
	private WifiTester wifiTester;
	public WifiTester getWifiTester(){
		return wifiTester;
		
	}
	public void setWifiTester(WifiTester wifiTester){
		this.wifiTester = wifiTester;
		
	}
}
