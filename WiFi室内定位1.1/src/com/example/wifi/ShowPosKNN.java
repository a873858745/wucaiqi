package com.example.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.example.dao.ExperimentLocDAO;
import com.example.dao.WifiDatabase;
import com.exmaple.model.Location;
import com.exmaple.model.WifiInfomation;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ShowPosKNN extends Activity {
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
	Location location = null;
	private double[] rssiAvg = new double[4];// ָ�Ƶľ�ֵ
	private double[] s = new double[4];
	private double[] v = new double[4];
	private double[] w = new double[4];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_pos_knn);
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		wifiTester = app.getWifiTester();
		wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		tv1 = (TextView) findViewById(R.id.tv_knn);
		long startTime = System.currentTimeMillis();
		 tv1.setText(getAccurateAddress());
		 long endTime = System.currentTimeMillis();
			Toast.makeText(ShowPosKNN.this, "knn��λʱ����" + (endTime - startTime)
					+ "ms", Toast.LENGTH_SHORT).show();
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
			// while (true) {
			try {
				// Thread.sleep(2000);
				Message msg = new Message();
				msg.what = 1;
				if (!wm.isWifiEnabled()) {
					wm.setWifiEnabled(true);
				}
				wm.reassociate();
				wm.startScan();
				Bundle bundle = new Bundle();
				bundle.putString("location", getAccurateAddress());
				msg.setData(bundle);
				handler.sendMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("thread error...");
			}
			// }
		}
	}

	public String getAccurateAddress() {
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
				// if (flag.equals(false)) {
				// dis += ((scanResult.level + 100) * (scanResult.level + 100));
				// Log.v(id + "", dis + "");
				// }
			}

			map.put(id, dis);
		}

		/** ��Map��ֵ�������� */
		List<Map.Entry<Integer, Double>> infoIds = new ArrayList<Map.Entry<Integer, Double>>(
				map.entrySet());// entrySet����map�����еļ�ֵ��
		Log.v("infoId", infoIds.size() + "");
		Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,
					Map.Entry<Integer, Double> o2) {
				return (int) (o1.getValue() - o2.getValue());

			}
		});

		Double sumx = (double) 0;
		Double sumy = (double) 0;
		Integer k = 4;


		/** ȡ���������4���ο���ľ�ֵ����������ȫȡ */
		if (infoIds.size() == 0) {
			x = 0;
			y = 0;
		} else if (infoIds.size() < k) {
			for (Entry<Integer, Double> iterator : infoIds) {
				sumx += database.getPositionInfoById(iterator.getKey()).x;
				sumy += database.getPositionInfoById(iterator.getKey()).y;
			}
			Log.v("sumx" + sumx, "sumy" + sumy);
			x = (double) sumx / infoIds.size();
			y = (double) sumy / infoIds.size();

		} else {
			for (int i = 0; i < k; i++) {
				double xCoor = database.getPositionInfoById(infoIds.get(i)
						.getKey()).x;
				sumx += xCoor;
				Log.e("x����", String.valueOf(xCoor));
				double yCoor = database.getPositionInfoById(infoIds.get(i)
						.getKey()).y;
				sumy += yCoor;
				Log.e("y����", String.valueOf(yCoor));
			}
			x = (double) sumx / k;
			y = (double) sumy / k;
		}
		Log.e("KNN ", "��ȷλ�ã������꣺" + x + " ������λ�ã�" + y);
		ExperimentLocDAO experimentLocDAO = new ExperimentLocDAO(ShowPosKNN.this);
		database.insertExperimentData(experimentLocDAO.getMaxId() + 1, x, y);
		// experimentLocDAO.add(location);
		String result = "��ȷλ�ã�\n�����꣺" + x + "\n������λ�ã�" + y;
		return result;
	}
}
