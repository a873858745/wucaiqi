package com.example.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.example.dao.WifiDatabase;
import com.exmaple.model.WifiInfomation;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class RefreshedLoc extends Activity{
	private static WifiTester wifiTester;
	private WifiDatabase database;
	private WifiManager wm;
	private List<ScanResult> wifiList;
	private List<WifiInfomation> wifiInfoList;
	private Map<Integer, Double> map;
	private TextView tv1;
	private double x;
	private double y;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("��ǰλ��");
		this.setContentView(R.layout.refreshedpos);
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		wifiTester = app.getWifiTester();
		tv1 = (TextView) findViewById(R.id.tv1);
//		wifiTester.CloseWifi();
		tv1.setText(getAccurateAddress());
	}
	
	public String getAccurateAddress() {
		// TODO Auto-generated method stub
		wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!wm.isWifiEnabled()) {
			wm.setWifiEnabled(true);
		}
		wm.startScan();
		wifiList = wm.getScanResults();
		database = new WifiDatabase(this);
		map = new TreeMap<Integer, Double>();
		List<Integer> locIdList = database.getAllPositionId();

		/** ������㵽���ο���֮���RSS�����������Map */
		for (Integer id : locIdList) {
			double dis = 0;
			wifiInfoList = database.getWifiInfomationById(id);
			for (ScanResult scanResult : wifiList) {
				Boolean flag = false;
				for (WifiInfomation wifiInfo : wifiInfoList) {
					if (scanResult.BSSID.equals(wifiInfo.bssid)) {
						Log.v(scanResult.level + "", wifiInfo.level + "");
						dis += ((scanResult.level - wifiInfo.level) * (scanResult.level - wifiInfo.level));
						flag = true;
					}

				}
				if (flag.equals(false)) {
					dis += ((scanResult.level + 100) * (scanResult.level + 100));
					Log.v(id + "", dis + "");
				}
			}

			map.put(id, dis);
		}

		/** ��Map��ֵ�������� */
		List<Map.Entry<Integer, Double>> infoIds = new ArrayList<Map.Entry<Integer, Double>>(
				map.entrySet());//entrySet����map�����еļ�ֵ��
		Log.v("infoId", infoIds.size() + "");
		Collections.sort(infoIds,
				new Comparator<Map.Entry<Integer, Double>>() {
					public int compare(Map.Entry<Integer, Double> o1,
							Map.Entry<Integer, Double> o2){
								return (int)(o1.getValue()-o2.getValue());
						
					}
				});
		
		double sumx = 0;
		double sumy = 0;
		Integer k = 4;
		
		/** ȡ���������4���ο���ľ�ֵ����������ȫȡ*/
		if(infoIds.size() ==0){
			x = 0;
			y = 0;
		}else if(infoIds.size() < k){
			for(Entry<Integer, Double> iterator : infoIds){
				sumx +=database.getPositionInfoById(iterator.getKey()).x;
				sumy +=database.getPositionInfoById(iterator.getKey()).y;
			}
			Log.v("sumx" + sumx, "sumy" + sumy);
			x = (double) sumx / infoIds.size();
			y = (double) sumy / infoIds.size();
			
		}else {
			for(int i =0;i<k;i++){
				sumx +=database.getPositionInfoById(infoIds.get(i).getKey()).x;
				sumy +=database.getPositionInfoById(infoIds.get(i).getKey()).y;
			}
			x = (double) sumx / k;
			y = (double) sumy / k;
		}
		String result = "��ȷλ�ã�\n�����꣺" + x + "\n������λ�ã�" + y;
		return result;
	}
	
}
