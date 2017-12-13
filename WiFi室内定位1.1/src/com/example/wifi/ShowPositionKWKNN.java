package com.example.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.example.dao.RssiDAO;
import com.example.dao.WifiDatabase;
import com.exmaple.model.ClusterLocInfo;
import com.exmaple.model.RssiInfo;
import com.exmaple.model.WifiInfomation;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowPositionKWKNN extends Activity {
	private WifiDatabase database;
	private WifiManager wm;
	private List<ScanResult> wifiList;
	private List<WifiInfomation> wifiInfoList;
	private Map<Integer, Double> mapCluster_i;
	private Map<Integer, Double> mapCenter;
	private Map<Integer, int[]> mapCluster;
	private Button btn_kmeans;
	private TextView tv1;
	private double x, x1, x2;
	private double y, y1, y2;
	private Button btn_locating;
	private Button btn_refresh;
	private Handler handler;
	private Kmeans k;
	private ArrayList<ArrayList<double[]>> cluster;
	private ArrayList<double[]> center;
	private List<Integer> locIdList;
	private List<Entry<Integer, int[]>> infoIdDataSet;
	private int[] numMin;
	private int[] num;
	private RssiDAO rssiDAO;
	private List<RssiInfo> centerRssiList = new ArrayList<RssiInfo>();
	private RssiInfo clusterRssi;
	private ClusterLocInfo clusterLocInfo;
	private double[] rssiAvg = new double[4];// 指纹的均值
	private double[] s = new double[4];
	private double[] v = new double[4];
	private double[] w = new double[4];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("当前位置");
		this.setContentView(R.layout.show_loc_kwknn);
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		WifiTester wifiTester = app.getWifiTester();
		rssiDAO = new RssiDAO(ShowPositionKWKNN.this);
		tv1 = (TextView) findViewById(R.id.tv_kwknn);

		long startTime = System.currentTimeMillis();
		tv1.setText(getAccurateAddress1());
		long endTime = System.currentTimeMillis();
		Toast.makeText(ShowPositionKWKNN.this,
				"kknn定位时长：" + (endTime - startTime) + "ms", Toast.LENGTH_LONG)
				.show();

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
				bundle.putString("location", getAccurateAddress1());
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

	public String getAccurateAddress1() {
		wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!wm.isWifiEnabled()) {
			wm.setWifiEnabled(true);
		}
		wm.reassociate();
		wm.startScan();
		wifiList = wm.getScanResults();
		database = new WifiDatabase(ShowPositionKWKNN.this);
		mapCenter = new TreeMap<Integer, Double>();
		int i, j;
		long centerSize = rssiDAO.getCenterCount();
		/** 将待测点到类中心之间的RSS向量距离存入Map */
		Log.e("center", "------------------->");
		for (i = 0; i < centerSize; i++) {

			centerRssiList = rssiDAO.getCenterInfoById(i);
			Double sum = (double) 0;
			for (ScanResult scanResult : wifiList) {
				if (scanResult.BSSID.equals("5c:63:bf:d0:09:c0")) {
					Log.e("----->", scanResult.BSSID);
					sum += (centerRssiList.get(0).getRss1() - scanResult.level)
							* (centerRssiList.get(0).getRss1() - scanResult.level);
				} else if (scanResult.BSSID.equals("d4:94:e8:1a:3c:a0")) {
					Log.e("----->", scanResult.BSSID);
					sum += (centerRssiList.get(0).getRss2() - scanResult.level)
							* (centerRssiList.get(0).getRss2() - scanResult.level);
				} else if (scanResult.BSSID.equals("bc:46:99:12:8e:3e")) {
					Log.e("----->", scanResult.BSSID);
					sum += (centerRssiList.get(0).getRss3() - scanResult.level)
							* (centerRssiList.get(0).getRss3() - scanResult.level);
				} else if (scanResult.BSSID.equals("d4:94:e8:02:28:c0")) {
					Log.e("----->", scanResult.BSSID);
					sum += (centerRssiList.get(0).getRss4() - scanResult.level)
							* (centerRssiList.get(0).getRss4() - scanResult.level);
				} else if (scanResult.BSSID.equals("d4:94:e8:1a:3e:80")) {
					Log.e("----->", scanResult.BSSID);
					sum += (centerRssiList.get(0).getRss5() - scanResult.level)
							* (centerRssiList.get(0).getRss5() - scanResult.level);
				}
			}
			Log.e("------->", "i: " + i + " sum: " + sum);
			mapCenter.put(i, sum);
		}
		/** 将Map按值升序排列 */
		List<Map.Entry<Integer, Double>> infoIdCenter = new ArrayList<Map.Entry<Integer, Double>>(
				mapCenter.entrySet());// entrySet返回mapCenter中所有的键值对
		Collections.sort(infoIdCenter,
				new Comparator<Map.Entry<Integer, Double>>() {
					public int compare(Map.Entry<Integer, Double> o1,
							Map.Entry<Integer, Double> o2) {
						return (int) (o1.getValue() - o2.getValue());

					}
				});

		int numMinCenter = infoIdCenter.get(0).getKey();
		Log.e("numMinCenter", String.valueOf(numMinCenter));

		mapCluster_i = new TreeMap<Integer, Double>();

		Log.e("icluster", "------------------------->");
		int clusterSize = rssiDAO.getClusterSizeById(numMinCenter);
		Log.e("iclusterSize", String.valueOf(clusterSize));

		for (j = 0; j < clusterSize; j++) {
			Double sum = (double) 0;
			clusterRssi = rssiDAO.getClusterInfoById(numMinCenter, j);

			for (ScanResult scanResult : wifiList) {
				if (scanResult.BSSID.equals("5c:63:bf:d0:09:c0")) {
					sum += (clusterRssi.getRss1() - scanResult.level)
							* (clusterRssi.getRss1() - scanResult.level);
					Log.e("----->", String.valueOf(j) + " " + scanResult.BSSID
							+ "  sum: " + sum);
				} else if (scanResult.BSSID.equals("d4:94:e8:1a:3c:a0")) {
					sum += (clusterRssi.getRss2() - scanResult.level)
							* (clusterRssi.getRss2() - scanResult.level);
					Log.e("----->", String.valueOf(j) + " " + scanResult.BSSID
							+ "  sum: " + sum);
				} else if (scanResult.BSSID.equals("bc:46:99:12:8e:3e")) {
					sum += (clusterRssi.getRss3() - scanResult.level)
							* (clusterRssi.getRss3() - scanResult.level);
					Log.e("----->", String.valueOf(j) + " " + scanResult.BSSID
							+ "  sum: " + sum);
				} else if (scanResult.BSSID.equals("d4:94:e8:02:28:c0")) {
					sum += (clusterRssi.getRss4() - scanResult.level)
							* (clusterRssi.getRss4() - scanResult.level);
					Log.e("----->", String.valueOf(j) + " " + scanResult.BSSID
							+ "  sum: " + sum);
				} else if (scanResult.BSSID.equals("d4:94:e8:1a:3e:80")) {
					sum += (clusterRssi.getRss5() - scanResult.level)
							* (clusterRssi.getRss5() - scanResult.level);
					Log.e("----->", String.valueOf(j) + " " + scanResult.BSSID
							+ "  sum: " + sum);
				}
			}
			Log.e("i_cluster", "第" + j + "指纹" + "sum: " + sum);
			mapCluster_i.put(j, sum);
		}

		/** 将Map按值升序排列 */
		List<Map.Entry<Integer, Double>> infoIdCluster = new ArrayList<Map.Entry<Integer, Double>>(
				mapCluster_i.entrySet());// entrySet返回mapCenter中所有的键值对
		Collections.sort(infoIdCluster,
				new Comparator<Map.Entry<Integer, Double>>() {
					public int compare(Map.Entry<Integer, Double> o1,
							Map.Entry<Integer, Double> o2) {
						return (int) (o1.getValue() - o2.getValue());

					}
				});
		numMin = new int[clusterSize];
		for (int k = 0; k < clusterSize; k++) {

			numMin[k] = infoIdCluster.get(k).getKey();
		}
		num = new int[clusterSize];

		mapCluster = new TreeMap<Integer, int[]>();
		locIdList = database.getAllPositionId();
		for (Integer id : locIdList) {
			clusterLocInfo = rssiDAO.getClusterLocInfoById(id);
			mapCluster.put(id, new int[] { clusterLocInfo.i_center,
					clusterLocInfo.cluster_j });

		}
		infoIdDataSet = new ArrayList<Map.Entry<Integer, int[]>>(
				mapCluster.entrySet());// entrySet返回mapCenter中所有的键值对
		for (Entry<Integer, int[]> iterator : infoIdDataSet) {
			for (int k = 0; k < clusterSize; k++) {
				if (iterator.getValue()[0] == numMinCenter
						&& iterator.getValue()[1] == numMin[k]) {

					num[k] = iterator.getKey();
					Log.e("id--------->", String.valueOf(num[k]));
				}

			}
		}

		double sumx = 0;
		double sumy = 0;
		Integer k = 4;

		for (int i1 = 0; i1 < 4; i1++) {
			wifiInfoList = database.getWifiInfomationById(num[i1]);
			rssiAvg[i1] = (wifiInfoList.get(0).level + wifiInfoList.get(1).level
					+ wifiInfoList.get(2).level + wifiInfoList.get(3).level + wifiInfoList
					.get(4).level) / 5;
			double sum = 0;
			for (int j1 = 0; j1 < 5; j1++) {
				sum += (wifiInfoList.get(j1).level - rssiAvg[i1])
						* (wifiInfoList.get(j1).level - rssiAvg[i1]);
			}
			s[i1] = Math.sqrt(sum / 5);
			v[i1] = s[i1] / rssiAvg[i1];
		}
		double sumV = 0;
		for (int i1 = 0; i1 < 4; i1++) {
			sumV += 1/v[i1];
		}
		for (int i1 = 0; i1 < 4; i1++) {
			w[i1] = 1/v[i1] / sumV;
		}

		/** 取距离最近的4个参考点的均值，若不够则全取 */
		if (clusterSize == 0) {
			x1 = 0;
			y1 = 0;
		} else if (clusterSize < k) {
			for (int m = 0; m < clusterSize; m++) {
				sumx += (database.getPositionInfoById(num[m]).x)*w[m];
				sumy += (database.getPositionInfoById(num[m]).y)*w[m];
			}
			Log.v("sumx" + sumx, "sumy" + sumy);
			x1 = (double) sumx / clusterSize;
			y1 = (double) sumy / clusterSize;

		} else {
			for (int n = 0; n < k; n++) {
				double xCoor = (database.getPositionInfoById(num[n]).x)*w[n];
				sumx += xCoor;
				Log.e("x坐标", String.valueOf(xCoor));
				double yCoor = (database.getPositionInfoById(num[n]).y)*w[n];
				sumy += yCoor;
				Log.e("y坐标", String.valueOf(yCoor));
			}
			x1 = sumx;
			y1 = sumy;
//			x1 = (double) sumx / k;
//			y1 = (double) sumy / k;
			Log.e("kWKNN ", "精确位置：横坐标：" + x1 + " 纵坐标位置：" + y1);
		}
		String result = "精确位置：\n横坐标：" + x1 + "\n纵坐标位置：" + y1;
		return result;
	}
}
