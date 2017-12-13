package com.example.wifi;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class WifiList extends ListActivity implements OnItemClickListener {

	private ListView lv;
	private static WifiTester wifiTester;
	private ScanResult scanResult;

	// private WifiConfiguration wc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("当前可用的WiFi列表");
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		wifiTester = app.getWifiTester();
		// wc = new WifiConfiguration();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				getString(wifiTester.getWifiList()));
		setListAdapter(adapter);
		lv = getListView();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);

	}

	private String[] getString(List<ScanResult> wifiList) {
		// TODO Auto-generated method stub
		ArrayList<String> listStr = new ArrayList<String>();
		for (int i = 0; i < wifiList.size(); i++) {
//			listStr.add((i+1) + ":\nSSID:" + wifiList.get(i).SSID + "\nMAC地址：" + wifiList.get(i).BSSID + "\n强度：" 
//					+ wifiList.get(i).level);
			if (wifiList.get(i).BSSID.equals("5c:63:bf:d0:09:c0")) {
				listStr.add((i + 1) + ":SSID:" + wifiList.get(i).SSID
						+ "|MAC地址：" + wifiList.get(i).BSSID + "|强度："
						+ wifiList.get(i).level);
			}else if (wifiList.get(i).BSSID.equals("bc:46:99:12:8e:3e")) {
				listStr.add((i + 1) + ":SSID:" + wifiList.get(i).SSID
						+ "|MAC地址：" + wifiList.get(i).BSSID + "|强度："
						+ wifiList.get(i).level);
			}else if (wifiList.get(i).BSSID.equals("d4:94:e8:1a:3c:a0")) {
				listStr.add((i + 1) + ":SSID:" + wifiList.get(i).SSID
						+ "|MAC地址：" + wifiList.get(i).BSSID + "|强度："
						+ wifiList.get(i).level);
			}else 
				if (wifiList.get(i).BSSID.equals("d4:94:e8:02:28:c0")) {
				listStr.add((i + 1) + ":SSID:" + wifiList.get(i).SSID
						+ "|MAC地址：" + wifiList.get(i).BSSID + "|强度："
						+ wifiList.get(i).level);
			}else if (wifiList.get(i).BSSID.equals("d4:94:e8:1a:3e:80")) {
				listStr.add((i + 1) + ":SSID:" + wifiList.get(i).SSID
						+ "|MAC地址：" + wifiList.get(i).BSSID + "|强度："
						+ wifiList.get(i).level);
			}
		}
		return listStr.toArray(new String[0]);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int postion,
			long id) {
		// TODO Auto-generated method stub
		scanResult = wifiTester.getWifiList().get(postion);
		String ssid = "\"" + scanResult.SSID + "\"";
		// wc.SSID = "\"" + scanResult.SSID + "\"";
		Toast.makeText(WifiList.this, ssid, Toast.LENGTH_SHORT).show();
		// wc.preSharedKey = "\"7675781777\""; // 该热点的密码
		// wc.hiddenSSID = true;
		// wc.status = WifiConfiguration.Status.ENABLED;
		// wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		// wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		// wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		// wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		// wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		// wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

	}

}
