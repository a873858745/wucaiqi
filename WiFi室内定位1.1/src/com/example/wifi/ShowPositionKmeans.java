package com.example.wifi;

import com.example.dao.ExperimentLocDAO;
import com.example.dao.RssiDAO;
import com.example.dao.WifiDatabase;
import com.example.wifi.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.example.wifi.R;
import com.exmaple.model.ClusterLocInfo;
import com.exmaple.model.RssiInfo;
import com.exmaple.model.WifiInfomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowPositionKmeans extends Activity {
	private static ShowPositionKmeans ShowPositionKmeans;
	private WifiDatabase database;
	private WifiManager wm;
	private List<ScanResult> wifiList;
	private List<WifiInfomation> wifiInfoList;
	private Map<Integer, Double> mapCluster_i;
	private Map<Integer, Double> mapCenter;
	private Map<Integer, int[]> mapCluster;
	private Button btn_kmeans;
	private TextView tv1;
	private TextView tv2;
	private double x, x1, x2;
	private double y, y1, y2;
	private Button btn_locating;
	private Button btn_refresh;
	private Handler handler;
	private double[][] a = new double[1][2];
	private double[][] b = new double[1][2];
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
		this.setTitle("kknn定位");
		this.setContentView(R.layout.show_position_kmeans);
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		WifiTester wifiTester = app.getWifiTester();
		rssiDAO = new RssiDAO(ShowPositionKmeans.this);
		tv1 = (TextView) findViewById(R.id.tv1);

		btn_locating = (Button) findViewById(R.id.btn_locating_kmeans);
		btn_locating.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				long startTime = System.currentTimeMillis();
				// new Thread(new ThreadShow()).start();
				handler = new Handler() {
					public void handleMessage(Message msg) {
						if (msg.what == 1) {
							tv1.setText(msg.getData().getString("location"));
						}
					}
				};
				tv1.setText(getAccurateAddress1());
				long endTime = System.currentTimeMillis();
				Toast.makeText(ShowPositionKmeans.this,
						"kknn定位时长：" + (endTime - startTime) + "ms",
						Toast.LENGTH_SHORT).show();
			}
		});
		btn_kmeans = (Button) findViewById(R.id.btn_kmeans);
		btn_kmeans.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				k = new Kmeans(8);
				ArrayList<double[]> dataSet = new ArrayList<double[]>();
				WifiDatabase database = new WifiDatabase(
						ShowPositionKmeans.this);
				List<Integer> locIdList = database.getAllPositionId();
				int idNum = locIdList.size();
				for (int i = 1; i <= idNum; i++) {
					dataSet.add(new double[] {
							database.getWifiInfomationById(i).get(0).level,
							database.getWifiInfomationById(i).get(1).level,
							database.getWifiInfomationById(i).get(2).level,
							database.getWifiInfomationById(i).get(3).level,
							database.getWifiInfomationById(i).get(4).level });
				}
				// 设置原始数据集
				k.setDataSet(dataSet);
				long startTime = System.currentTimeMillis();
				// 执行算法
				k.execute();
				long endTime = System.currentTimeMillis();
				Toast.makeText(ShowPositionKmeans.this,
						"kmeans聚类时长：" + (endTime - startTime) + "ms",
						Toast.LENGTH_LONG).show();
				// 得到聚类结果
				cluster = k.getCluster();
				center = k.getCenter();
				// 查看结果
				for (int i = 0; i < cluster.size(); i++) {
					k.printDataArray(cluster.get(i), "cluster[" + i + "]");
				}
				k.printCenterData(center);
				Toast.makeText(ShowPositionKmeans.this, "聚类结束",
						Toast.LENGTH_SHORT).show();

				long centerNum = rssiDAO.getCenterCount();
				if (centerNum > 0) {
					for (int i = 0; i < center.size(); i++) {
						rssiDAO.deleteCenterById(i);
						for (int j = 0; j < cluster.get(i).size(); j++) {
							rssiDAO.deleteClusterById(i);
						}
					}
				}
				for (int i = 0; i < center.size(); i++) {
					rssiDAO.insertCenter(i, center.get(i)[0], center.get(i)[1],
							center.get(i)[2], center.get(i)[3],
							center.get(i)[4]);
					for (int j = 0; j < cluster.get(i).size(); j++) {
						rssiDAO.insertCluster(i, j, cluster.get(i).get(j)[0],
								cluster.get(i).get(j)[1],
								cluster.get(i).get(j)[2],
								cluster.get(i).get(j)[3],
								cluster.get(i).get(j)[4]);
					}
				}

				
				locIdList = database.getAllPositionId();
				long clusterLocNum = rssiDAO.getClusterLocCount();
				if(clusterLocNum>0){
					for(int i=1;i<=clusterLocNum;i++){
						rssiDAO.deleteClusterLocInfoById(i);
					}
				}
				for (Integer id : locIdList) {
					Log.e("id", String.valueOf(id));
					wifiInfoList = database.getWifiInfomationById(id);
					for (int i = 0; i < cluster.size(); i++) {
						for (int j = 0; j < cluster.get(i).size(); j++) {
							if (cluster.get(i).get(j)[0] == wifiInfoList.get(0).level
									&& cluster.get(i).get(j)[1] == wifiInfoList
											.get(1).level
									&& cluster.get(i).get(j)[2] == wifiInfoList
											.get(2).level
									&& cluster.get(i).get(j)[3] == wifiInfoList
											.get(3).level
									&& cluster.get(i).get(j)[4] == wifiInfoList
											.get(4).level) {
								rssiDAO.insertClusterLocInfo(id, i, j);
							}
						}
					}
				}

			}
		});
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
		database = new WifiDatabase(ShowPositionKmeans.this);
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
				mapCluster_i.entrySet());// entrySet返回mapCluster_i中所有的键值对
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
			Log.e("K-means精确位置： ", "横坐标：" + x1 + "纵坐标位置：" + y1);
			ExperimentLocDAO experimentLocDAO = new ExperimentLocDAO(ShowPositionKmeans.this);
			database.insertExperimentData(experimentLocDAO.getMaxId() + 1, x1, y1);
			
		}
		String result = "精确位置：\n横坐标：" + x1 + "\n纵坐标位置：" + y1;
		return result;
	}
}
