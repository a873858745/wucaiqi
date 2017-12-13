package com.example.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dao.ExperimentLocDAO;
import com.example.dao.RssiDAO;
import com.example.dao.WifiDatabase;
import com.exmaple.model.ClusterLocInfo;
import com.exmaple.model.RssiInfo;
import com.exmaple.model.WifiInfomation;

public class ShowPosKDWKNN extends Activity {
//	private static ShowPosKDWKNN ShowPosKDWKNN;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("当前位置");
		this.setContentView(R.layout.show_pos_kdwknn);
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		WifiTester wifiTester = app.getWifiTester();
		rssiDAO = new RssiDAO(ShowPosKDWKNN.this);
		tv1 = (TextView) findViewById(R.id.tv_kdwknn);
		
		long startTime = System.currentTimeMillis();
		tv1.setText(getAccurateAddress1());
		long endTime = System.currentTimeMillis();
		Toast.makeText(ShowPosKDWKNN.this,
				"kknn定位时长：" + (endTime - startTime) + "ms",
				Toast.LENGTH_LONG).show();

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
		database = new WifiDatabase(ShowPosKDWKNN.this);
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
		
		 
		//确定id号
		num = new int[clusterSize];
		mapCluster = new TreeMap<Integer, int[]>();
		locIdList = database.getAllPositionId();
		for (Integer id : locIdList) {
			clusterLocInfo = rssiDAO.getClusterLocInfoById(id);
			mapCluster.put(id, new int[]{clusterLocInfo.i_center,clusterLocInfo.cluster_j});
			
		}
		infoIdDataSet = new ArrayList<Map.Entry<Integer, int[]>>(
				mapCluster.entrySet());// entrySet返回mapCenter中所有的键值对
		for (Entry<Integer, int[]> iterator : infoIdDataSet) {
			for (int k = 0; k < clusterSize; k++) {
				if (iterator.getValue()[0] == numMinCenter
						&& iterator.getValue()[1] == k) {

					num[k] = iterator.getKey();
				}

			}
		}
		for (j = 0; j < clusterSize; j++) {
			
		}
				
		for (j = 0; j < clusterSize; j++) {
			Double sum = (double) 0;
			clusterRssi = rssiDAO.getClusterInfoById(numMinCenter, j);
			String[] apName = {"ap1","ap2","ap3","ap4","ap5"};
			double[] average = new double[5];
			double[] s = new double[5];
			double[] v = new double[5];
			
			double[] w = new double[5];
			long id = num[j];
			for(int k=0;k<5;k++){
				List<WifiInfomation> list = new ArrayList<WifiInfomation>();
				list = database.getOriginalApWifiInfoByIdAndAp(id,apName[k]);
				double sumAvg = 0;
				for(int m=0;m<list.size();m++){
					sumAvg += list.get(m).getLevel();
				}
				average[k] = sumAvg/100;
				double sumS=0;//求标准差用到的和
				for(int n=0;n<list.size();n++){
					sumS += (list.get(n).getLevel()-average[k])*(list.get(n).getLevel()-average[k]);
				}
				s[k] = Math.sqrt(sumS / list.size());
				v[k] = s[k] / average[k];
			}
			double sumV = 0;
			 for (int m = 0; m < 5; m++) {
				 sumV += 1/v[m];
			 }
			 for (int m = 0; m < 5; m++) {
				 w[m] = 1/v[m] / sumV;
			 }
			for (ScanResult scanResult : wifiList) {
				if (scanResult.BSSID.equals("5c:63:bf:d0:09:c0")) {
					Log.e("weight1", String.valueOf(w[0]));
					sum += w[0]*(clusterRssi.getRss1() - scanResult.level)
							* (clusterRssi.getRss1() - scanResult.level);
					Log.e("----->", String.valueOf(j) + " " + scanResult.BSSID
							+ "  sum: " + sum);
				} else if (scanResult.BSSID.equals("d4:94:e8:1a:3c:a0")) {
					Log.e("weight2", String.valueOf(w[1]));
					sum += w[1]*(clusterRssi.getRss2() - scanResult.level)
							* (clusterRssi.getRss2() - scanResult.level);
					Log.e("----->", String.valueOf(j) + " " + scanResult.BSSID
							+ "  sum: " + sum);
				} else if (scanResult.BSSID.equals("bc:46:99:12:8e:3e")) {
					Log.e("weight3", String.valueOf(w[2]));
					sum += w[2]*(clusterRssi.getRss3() - scanResult.level)
							* (clusterRssi.getRss3() - scanResult.level);
					Log.e("----->", String.valueOf(j) + " " + scanResult.BSSID
							+ "  sum: " + sum);
				} else if (scanResult.BSSID.equals("d4:94:e8:02:28:c0")) {
					Log.e("weight4", String.valueOf(w[3]));
					sum += w[3]*(clusterRssi.getRss4() - scanResult.level)
							* (clusterRssi.getRss4() - scanResult.level);
					Log.e("----->", String.valueOf(j) + " " + scanResult.BSSID
							+ "  sum: " + sum);
				} else if (scanResult.BSSID.equals("d4:94:e8:1a:3e:80")) {
					Log.e("weight5", String.valueOf(w[4]));
					sum += w[4]*(clusterRssi.getRss5() - scanResult.level)
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
			mapCluster.put(id, new int[]{clusterLocInfo.i_center,clusterLocInfo.cluster_j});
			
		}
		infoIdDataSet = new ArrayList<Map.Entry<Integer, int[]>>(
				mapCluster.entrySet());// entrySet返回mapCenter中所有的键值对
		for (Entry<Integer, int[]> iterator : infoIdDataSet) {
			for (int k = 0; k < clusterSize; k++) {
				if (iterator.getValue()[0] == numMinCenter
						&& iterator.getValue()[1] == numMin[k]) {

					num[k] = iterator.getKey();
				}

			}
		}

		double sumx = 0;
		double sumy = 0;
		Integer k = 4;
		/** 取距离最近的4个参考点的均值，若不够则全取 */
		if (clusterSize == 0) {
			x1 = 0;
			y1 = 0;
		} else if (clusterSize < k) {
			for (int m = 0; m < clusterSize; m++) {
				sumx += database.getPositionInfoById(num[m]).x;
				sumy += database.getPositionInfoById(num[m]).y;
			}
			Log.v("sumx" + sumx, "sumy" + sumy);
			x1 = (double) sumx / clusterSize;
			y1 = (double) sumy / clusterSize;

		} else {
			for (int n = 0; n < k; n++) {
				double xCoor = database.getPositionInfoById(num[n]).x;
				sumx += xCoor;
				Log.e("x坐标", String.valueOf(xCoor));
				double yCoor = database.getPositionInfoById(num[n]).y;
				sumy += yCoor;
				Log.e("y坐标", String.valueOf(yCoor));
			}
			x1 = (double) sumx / k;
			y1 = (double) sumy / k;
			Log.e("KDWKNN ", "精确位置：横坐标：" + x1 + " 纵坐标位置：" + y1);
		}
		ExperimentLocDAO experimentLocDAO = new ExperimentLocDAO(ShowPosKDWKNN.this);
		database.insertExperimentData(experimentLocDAO.getMaxId() + 1, x1, y1);
		String result = "精确位置：\n横坐标：" + x1 + "\n纵坐标位置：" + y1;
		return result;
	}
}
