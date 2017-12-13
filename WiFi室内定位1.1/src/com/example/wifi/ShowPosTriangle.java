package com.example.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

import com.example.dao.ExperimentLocDAO;
import com.example.dao.WifiDatabase;
import com.exmaple.model.Coordinate;
import com.exmaple.model.Round;
import com.exmaple.model.WifiInfomation;

public class ShowPosTriangle extends Activity {
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
	private Double[] dis = new Double[5];// 定位节点与参考点之间的距离
	private double R1, R2, R3;
	private double[] xCoor = new double[3];// 存x坐标
	private double[] yCoor = new double[3];// 存x坐标

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpos);
		wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!wm.isWifiEnabled()) {
			wm.setWifiEnabled(true);
		}
		wm.startScan();
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		wifiTester = app.getWifiTester();
		tv1 = (TextView) findViewById(R.id.tv1);
		tv1.setText(getAccurateAddress());
		// new Thread(new ThreadShow()).start();
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
			while (true) {
				try {
					Thread.sleep(1100);
					Message msg = new Message();
					msg.what = 1;
					Bundle bundle = new Bundle();
					bundle.putString("location", getAccurateAddress());
					msg.setData(bundle);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("thread error...");
				}
			}
		}
	}

	public String getAccurateAddress() {
		
		wifiList = wm.getScanResults();
		database = new WifiDatabase(ShowPosTriangle.this);
		map = new TreeMap<Integer, Double>();
		List<Integer> locIdList = database.getAllPositionId();
		/** 将待测点到到参考点之间的RSS向量距离存入Map */
		for (ScanResult scanResult : wifiList) {
			if(scanResult.BSSID.equals("5c:63:bf:d0:09:c0")){
				dis[0] = Math.pow(10, (double) (-30 - scanResult.level) / 30);
				map.put(1, dis[0]);
			}else if(scanResult.BSSID.equals("c8:3a:35:c1:28:97")){
				dis[1] = Math.pow(10, (double) (-38 - scanResult.level) / 30);
				map.put(2, dis[1]);
			}else if(scanResult.BSSID.equals("c8:3a:35:c4:79:54")){
				dis[2] = Math.pow(10, (double) (-35 - scanResult.level) / 30);
				map.put(3, dis[2]);
			}
//			else if(scanResult.BSSID.equals("da:df:9a:9d:2f:e5")){
//				dis[3] = Math.pow(10, (double) (-50 - scanResult.level) / 30);
//				map.put(4, dis[3]);
//			}
//			else if(scanResult.BSSID.equals("c8:3a:35:cf:71:81")){
//				dis[4] = Math.pow(10, (double) (-50 - scanResult.level) / 30);
//				map.put(5, dis[4]);
//			}
		}
		/** 将Map按值升序排列 */
//		List<Map.Entry<Integer, Double>> infoIds = new ArrayList<Map.Entry<Integer, Double>>(
//				map.entrySet());// entrySet返回map中所有的键值对
//		Log.e("infoId", infoIds.size() + "");
//		Collections.sort(infoIds,
//				new Comparator<Map.Entry<Integer, Double>>() {
//					public int compare(Map.Entry<Integer, Double> o1,
//							Map.Entry<Integer, Double> o2) {
//						return (int)(o1.getValue() - o2.getValue());
//
//					}
//				});
//		for (int i = 0; i < 3; i++) {
//			xCoor[i] = database.getPositionInfoById(infoIds.get(i).getKey()).x;
//			Log.e("x坐标", String.valueOf(xCoor[i]));
//			yCoor[i] = database.getPositionInfoById(infoIds.get(i).getKey()).y;
//			Log.e("y坐标", String.valueOf(yCoor[i]));
//			wifiInfoList = database.getWifiInfomationById(infoIds.get(i)
//					.getKey());
//			for(WifiInfomation wifiInfomation:wifiInfoList){
//				if ("d4:94:e8:1a:3c:a0".equals(wifiInfomation.bssid)) {
//					RSSI[i] = wifiInfomation.level;
//					dis[i] = Math.pow(10, (double) (-50 - RSSI[i]) / 30);
//					break;
//				} 
////				else if ("d4:94:e8:1a:3e:80".equals(wifiInfomation.bssid)) {
////					RSSI[i] = wifiInfomation.level;
////					dis[i] = Math.pow(10, (-50 - RSSI[i]) / 30);
////					break;
////				} else if ("d4:94:e8:1a:3c:c0".equals(wifiInfomation.bssid)) {
////					RSSI[i] = wifiInfomation.level;
////					dis[i] = Math.pow(10, (-50 - RSSI[i]) / 30);
////					break;
////				} else if ("d4:94:e8:1a:28:a0".equals(wifiInfomation.bssid)) {
////					RSSI[i] = wifiInfomation.level;
////					dis[i] = Math.pow(10, (-50 - RSSI[i]) / 30);
////					break;
////				}
//			}
//			List<ScanResult> list = wm.getScanResults();
//			for (ScanResult scanResult : list) {
//				
//			}
//		}
		R1 = dis[0];
		Log.e("R1", String.valueOf(R1));
		R2 = dis[1];
		Log.e("R2", String.valueOf(R2));
		R3 = dis[2];
		Log.e("R3", String.valueOf(R3));
		Round round1 = new Round(0, 2.5, R1);
		Round round2 = new Round(2, 5, R2);  
		Round round3 = new Round(4, 2, R3);
//		Round round1 = new Round(xCoor[0], yCoor[0], R1);
//		Round round2 = new Round(xCoor[1], yCoor[1], R2);
//		Round round3 = new Round(xCoor[2], yCoor[2], R3);
		String result = tcl(round1, round2, round3);
		
		return result;
	}
	/**
	 * 三角形质心定位算法实现 Triangle centroid location
	 * 
	 * @param r1
	 *            坐标1为圆心,距离为半径
	 * @param r2
	 * @param r3
	 * @return
	 */
	private String tcl(Round r1, Round r2, Round r3) {
		Coordinate p1 = null;// 有效交叉点1
		Coordinate p2 = null;// 有效交叉点2
		Coordinate p3 = null;// 有效交叉点3
		Coordinate zx = new Coordinate();// 计算三点质心
		List<Coordinate> jds1 = jd(r1.getX(), r1.getY(), r1.getR(), r2.getX(),
				r2.getY(), r2.getR());// r1,r2交点
		if (jds1 != null && !jds1.isEmpty()) {
			for (Coordinate jd : jds1) {// 有交点
				if (p1 == null
						&& Math.pow(jd.getX() - r3.getX(), 2)
								+ Math.pow(jd.getY() - r3.getY(), 2) <= Math
									.pow(r3.getR(), 2)) {
					p1 = jd;
				} else if (p1 != null) {
					if (Math.pow(jd.getX() - r3.getX(), 2)
							+ Math.pow(jd.getY() - r3.getY(), 2) <= Math.pow(
							r3.getR(), 2)) {
						if (Math.sqrt(Math.pow(jd.getX() - r3.getX(), 2)
								+ Math.pow(jd.getY() - r3.getY(), 2)) > Math
								.sqrt(Math.pow(p1.getX() - r3.getX(), 2)
										+ Math.pow(p1.getY() - r3.getY(), 2))) {
							p1 = jd;
						}
					}
				}
			}
		} else {// 没有交点定位错误
			return null;
		}
		List<Coordinate> jds2 = jd(r1.getX(), r1.getY(), r1.getR(), r3.getX(),
				r3.getY(), r3.getR());// r1,r2交点
		if (jds2 != null && !jds2.isEmpty()) {
			for (Coordinate jd : jds2) {// 有交点
				if (p2 == null
						&& Math.pow(jd.getX() - r2.getX(), 2)
								+ Math.pow(jd.getY() - r2.getY(), 2) <= Math
									.pow(r2.getR(), 2)) {
					p2 = jd;

				} else if (p2 != null) {
					if (Math.pow(jd.getX() - r2.getX(), 2)
							+ Math.pow(jd.getY() - r2.getY(), 2) <= Math.pow(
							r2.getR(), 2)) {
						if (Math.pow(jd.getX() - r2.getX(), 2)
								+ Math.pow(jd.getY() - r2.getY(), 2) > Math
									.sqrt(Math.pow(p2.getX() - r2.getX(), 2)
											+ Math.pow(p2.getY() - r2.getY(), 2))) {
							p1 = jd;
						}
					}
				}
			}
		} else {// 没有交点定位错误
			return null;
		}
		List<Coordinate> jds3 = jd(r2.getX(), r2.getY(), r2.getR(), r3.getX(),
				r3.getY(), r3.getR());// r1,r2交点
		if (jds3 != null && !jds3.isEmpty()) {
			for (Coordinate jd : jds3) {// 有交点
				if (Math.pow(jd.getX() - r1.getX(), 2)
						+ Math.pow(jd.getY() - r1.getY(), 2) <= Math.pow(
						r1.getR(), 2)) {
					p3 = jd;
				} else if (p3 != null) {
					if (Math.pow(jd.getX() - r1.getX(), 2)
							+ Math.pow(jd.getY() - r1.getY(), 2) <= Math.pow(
							r1.getR(), 2)) {
						if (Math.pow(jd.getX() - r1.getX(), 2)
								+ Math.pow(jd.getY() - r1.getY(), 2) > Math
									.sqrt(Math.pow(p3.getX() - r1.getX(), 2)
											+ Math.pow(p3.getY() - r1.getY(), 2))) {
							p3 = jd;
						}
					}
				}
			}
		} else {// 没有交点定位错误
			return null;
		}

		zx.x = (p1.x + p2.x + p3.x) / 3;// 质心
		zx.y = (p1.y + p2.y + p3.y) / 3;
		ExperimentLocDAO experimentLocDAO = new ExperimentLocDAO(ShowPosTriangle.this);
		database.insertExperimentData(experimentLocDAO.getMaxId() + 1, zx.x, zx.y);
		String result = "精确位置：\n横坐标：" + zx.x + "\n纵坐标位置：" + zx.y;
		return result;

	}

	/**
	 * 求两个圆的交点
	 * 
	 * @param x1
	 *            ,y1 圆心1坐标
	 * @param y1
	 * @param r1
	 *            半径
	 * @param x2
	 * @param y2
	 * @param r2
	 * @return
	 */
	private List<Coordinate> jd(double x2, double y2, double r, double x3,
			double y3, double r2) {
		Map<Integer, double[]> p = new HashMap<Integer, double[]>();
		double d = Math.sqrt(Math.pow(x2 - x3, 2) + Math.pow(y2 - y3, 2));// 两圆心距离
		if (Math.sqrt(Math.pow(x2 - x3, 2) + Math.pow(y2 - y3, 2)) < (r + r2)) {// 两圆向交

		}
		List<Coordinate> points = new ArrayList<Coordinate>();// 交点坐标
		Coordinate coor;
		if (d > r + r2 || d < Math.abs(r - r2)) {// 相离或内含
			return null;
		} else if (x2 == x3 && y2 == y3) {// 同心圆
			return null;// 同心圆 )
		} else if (y2 == y3 && x2 != x3) {
			double a = ((r * r - r2 * r2) - (x2 * x2 - x3 * x3))
					/ (2 * x3 - 2 * x2);
			if (d == Math.abs(r - r2) || d == r + r2) {// 只有一个交点时\
				coor = new Coordinate();
				coor.x = a;
				coor.y = y2;
				points.add(coor);
			} else {// 两个交点
				double t = r * r - (a - x2) * (a - x2);
				coor = new Coordinate();
				coor.x = a;
				coor.y = y2 + Math.sqrt(t);
				points.add(coor);
				coor = new Coordinate();
				coor.x = a;
				coor.y = y2 - Math.sqrt(t);
				points.add(coor);
			}
		} else if (y2 != y3) {
			double k, disp;
			k = (2 * x2 - 2 * x3) / (2 * y3 - 2 * y2);
			disp = ((r * r - r2 * r2) - (x2 * x2 - x3 * x3) - (y2 * y2 - y3
					* y3))
					/ (2 * y3 - 2 * y2);// 直线偏移量
			double a, b, c;
			a = (k * k + 1);
			b = (2 * (disp - y2) * k - 2 * x2);
			c = (disp - y2) * (disp - y2) - r * r + x2 * x2;
			double disc;
			disc = b * b - 4 * a * c;// 一元二次方程判别式
			if (d == Math.abs(r - r2) || d == r + r2) {
				coor = new Coordinate();
				coor.x = (-b) / (2 * a);
				;
				coor.y = k * coor.x + disp;
				points.add(coor);
			} else {
				coor = new Coordinate();
				coor.x = ((-b) + Math.sqrt(disc)) / (2 * a);
				coor.y = k * coor.x + disp;
				points.add(coor);
				coor = new Coordinate();
				coor.x = ((-b) - Math.sqrt(disc)) / (2 * a);
				coor.y = k * coor.x + disp;
				points.add(coor);
			}
		}

		return points;
	}
}
