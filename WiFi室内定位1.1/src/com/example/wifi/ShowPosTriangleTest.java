package com.example.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.example.dao.WifiDatabase;
import com.exmaple.model.WifiInfomation;

public class ShowPosTriangleTest extends Activity {
	private Handler handler;
	private static WifiTester wifiTester;
	private WifiDatabase database;
	private WifiManager wm;
	private List<ScanResult> wifiList;
	private List<WifiInfomation> wifiInfoList;
	private Map<Integer, Double> map;
	private TextView tv1;
	private double x;
	private double y;
	private Double[] RSSI = new Double[3];
	private double[] dis = new double[3];// ��λ�ڵ���ο���֮��ľ���
	private double[] xCoor = new double[3];// ��x����
	private double[] yCoor = new double[3];// ��x����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpos);
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		wifiTester = app.getWifiTester();
		tv1 = (TextView) findViewById(R.id.tv1);
		tv1.setText(getAccurateAddress());
//		new Thread(new ThreadShow()).start();
		handler = new Handler() {
	        public void handleMessage(Message msg) {
	            if (msg.what == 1) {
	            	tv1.setText(msg.getData().getString("location"));
	            }
	        }
	    };
	} 

	class ThreadShow implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                	Thread.sleep(2200);
                    Message msg = new Message();
                    msg.what = 1;
//                    tv1.setText("");
                    Bundle bundle = new Bundle();
                    bundle.putString("location", getAccurateAddress());
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("thread error...");
                }
            }
        }
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
			Double dis = (double) 0;
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
				map.entrySet());// entrySet����map�����еļ�ֵ��
		Log.v("infoId", infoIds.size() + "");
		Collections.sort(infoIds,
				new Comparator<Map.Entry<Integer, Double>>() {
					public int compare(Map.Entry<Integer, Double> o1,
							Map.Entry<Integer, Double> o2) {
						return (int)(o1.getValue() - o2.getValue());

					}
				});
		
		Double sumx = (double) 0;
		Double sumy = (double) 0;
		Integer k = 3;

		/** ȡ���������4���ο���ľ�ֵ����������ȫȡ */
		
			for (int i = 0; i < k; i++) {
				double xCoor = database.getPositionInfoById(infoIds.get(i).getKey()).x;
				sumx += xCoor;
				Log.e("x����", String.valueOf(xCoor));
				double yCoor = database.getPositionInfoById(infoIds.get(i).getKey()).y;
				sumy += yCoor;
				Log.e("y����", String.valueOf(yCoor));
				wifiInfoList = database.getWifiInfomationById(infoIds.get(i)
						.getKey());
				for (WifiInfomation wifiInfo : wifiInfoList) {
					if ("dz110".equals(wifiInfo.ssid)) {
						RSSI[i] = wifiInfo.level;
						dis[i] = Math.pow(10, (-30 - RSSI[i]) / 28);
					}
				}
			}
			x = (double) sumx / k;
			y = (double) sumy / k;
		
		String result = "��ȷλ�ã�\n�����꣺" + x + "\n������λ�ã�" + y;
		return result;
	}
}
