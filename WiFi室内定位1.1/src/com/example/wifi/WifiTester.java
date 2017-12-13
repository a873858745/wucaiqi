package com.example.wifi;

import java.util.ArrayList;
import java.util.List;

import com.example.dao.ExperimentLocDAO;
import com.example.dao.WifiDatabase;
import com.exmaple.model.Location;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

public class WifiTester extends Activity {

	private Button btn_scan, btn_kmeans;
	private Button btn_add, btn_addOnlineLoc;
	private Button btn_getDbLoc, btn_testData, btn_deleteTestData,
			btn_originalApInfo;
	private Button btn_getLoc;
	private Button btn_contact;
	private Button btn_exit;
	private WifiManager wm;
	private WifiReceiver receiverWifi;
	private List<ScanResult> wifiList;
	private ProgressDialog dialog;
	private Handler handler;
	private ExperimentLocDAO experimentLocDAO = new ExperimentLocDAO(
			WifiTester.this);
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((WifiTesterApp) getApplication()).setWifiTester(WifiTester.this);
		wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		receiverWifi = new WifiReceiver();
		btn_scan = (Button) findViewById(R.id.btn_scanWifi);
		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				registerReceiver(receiverWifi, new IntentFilter(
						WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
				scanWifi();

			}
		});
		btn_add = (Button) findViewById(R.id.btn_addpostion);
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(WifiTester.this, AddPosition.class);
				WifiTester.this.startActivity(intent);
			}
		});
		btn_getDbLoc = (Button) findViewById(R.id.btn_dbpos);
		btn_getDbLoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(WifiTester.this, ShowDatabasePos.class);
				WifiTester.this.startActivity(intent);
			}
		});
		btn_testData = (Button) findViewById(R.id.btn_testData);
		btn_testData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(WifiTester.this, ShowAllTestLocation.class);
				WifiTester.this.startActivity(intent);
			}
		});
		btn_deleteTestData = (Button) findViewById(R.id.btn_deleteTestData);
		btn_deleteTestData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Builder builder = new AlertDialog.Builder(WifiTester.this);
				builder.setTitle("删除");
				builder.setMessage("你确定？");
				builder.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								long num = experimentLocDAO.getCount();
								for (int i = 1; i <= num; i++) {
									experimentLocDAO.deleteById(i);
								}
							}
						});
				builder.setNegativeButton("否",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub

							}
						});
				builder.show();

			}
		});
		btn_getLoc = (Button) findViewById(R.id.btn_getLoc);
		btn_getLoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(WifiTester.this, ChooseArithmetic.class);
				startActivity(intent);
				
//				Intent intent = new Intent();
//				intent.setClass(WifiTester.this, ShowPos.class);
//				WifiTester.this.startActivity(intent);
				// new Thread(new ThreadShow()).start();
				handler = new Handler() {
					public void handleMessage(Message msg) {
						if (msg.what == 1) {
							Intent intent = new Intent();
							intent.setClass(WifiTester.this, ShowPosKNN.class);
							WifiTester.this.startActivity(intent);
						}
					}
				};

			}
		});
//		btn_getLoc.setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(WifiTester.this, ChooseArithmetic.class);
//				startActivity(intent);
//				return false;
//			}
//		});
		btn_contact = (Button) findViewById(R.id.btn_register);
		btn_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(WifiTester.this, RegisterInfo.class);
				WifiTester.this.startActivity(intent);
			}
		});
		btn_exit = (Button) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(WifiTester.this, Login.class);
				WifiTester.this.startActivity(intent);
			}
		});
		
		// btn_addOnlineLoc = (Button)findViewById(R.id.btn_addOnlineLoc);
		// btn_addOnlineLoc.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(WifiTester.this,AddOnlineLoc.class);
		// WifiTester.this.startActivity(intent);
		// }
		// });
		// btn_originalApInfo = (Button)findViewById(R.id.btn_originalApInfo);
		// btn_originalApInfo.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(WifiTester.this,ShowOriginalApInfo.class);
		// WifiTester.this.startActivity(intent);
		// }
		// });

	}

	public class ThreadShow implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					Thread.sleep(2000);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("thread error...");
				}
			}
		}
	}

	// private ArrayList<ArrayList<double[]>> cluster = k.getCluster();
	// private ArrayList<double[]> center = k.getCenter();

//	public ArrayList<ArrayList<double[]>> getCluster() {
//		return cluster;
//	}
//
//	public ArrayList<double[]> getCenter() {
//		return center;
//	}

	protected void scanWifi() {
		// TODO Auto-generated method stub
		openWifi();
		wm.startScan();
		dialog = ProgressDialog.show(WifiTester.this, "", "正在扫描WiFi，请稍后...");
	}

	private void openWifi() {
		// TODO Auto-generated method stub
		if (!wm.isWifiEnabled()) {
			wm.setWifiEnabled(true);
		}
	}

	public void CloseWifi() {

		if (wm.isWifiEnabled()) {
			wm.setWifiEnabled(false);
		}
	}

	class WifiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
				wifiList = wm.getScanResults();
				unregisterReceiver(receiverWifi);
				dialog.dismiss();
				Toast.makeText(context, "扫描完毕", Toast.LENGTH_SHORT).show();
				Intent intent1 = new Intent();
				intent1.setClass(WifiTester.this, WifiList.class);
				WifiTester.this.startActivity(intent1);

			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wifi_tester, menu);
		return true;
	}

	public List<ScanResult> getWifiList() {
		// TODO Auto-generated method stub
		return wifiList;
	}

	public void setWifiList(List<ScanResult> wifiList) {
		this.wifiList = wifiList;
	}

	private long mExitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				// Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
				// CloseWifi();

			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
