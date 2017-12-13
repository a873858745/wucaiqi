package com.example.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dao.ExperimentLocDAO;
import com.example.dao.WifiDatabase;
import com.exmaple.model.Location;
import com.exmaple.model.WifiInfomation;

public class ShowPositionNN extends Activity{
	private Handler handler;
	private static WifiTester wifiTester;
	private WifiDatabase database;
	private WifiManager wm;
	private List<ScanResult> wifiList;
	private List<WifiInfomation> wifiInfoList;
	private Map<Integer, Double> map;
	private TextView tv1;
	Location location = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_pos_nn);
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		wifiTester = app.getWifiTester();
		wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		tv1 = (TextView) findViewById(R.id.tv_nn);
		long startTime = System.currentTimeMillis();
		 tv1.setText(getAccurateAddress());
		 long endTime = System.currentTimeMillis();
			Toast.makeText(ShowPositionNN.this, "knn��λʱ����" + (endTime - startTime)
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
			}
			Log.e("id---->", String.valueOf(dis));
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
		/** ȡ����������Ǹ��ο��������ֵ */
		double  x,y;
		x = database.getPositionInfoById(infoIds.get(0).getKey()).x;
		y = database.getPositionInfoById(infoIds.get(0).getKey()).y;
		Log.e("NN ", "��ȷλ�ã������꣺" + x + " ������λ�ã�" + y);
		ExperimentLocDAO experimentLocDAO = new ExperimentLocDAO(ShowPositionNN.this);
		database.insertExperimentData(experimentLocDAO.getMaxId() + 1, x, y);
		// experimentLocDAO.add(location);
		String result = "��ȷλ�ã�\n�����꣺" + x + "\n������λ�ã�" + y;
		return result;
	}
}
