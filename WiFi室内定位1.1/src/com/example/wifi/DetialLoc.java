package com.example.wifi;

import java.util.ArrayList;
import java.util.List;

import com.example.dao.DblocDAO;
import com.example.dao.OriginalApInfoDAO;
import com.example.dao.WifiDatabase;
import com.example.dao.WifiInfoDAO;
import com.exmaple.model.Location;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DetialLoc extends Activity {
	private Button btn_refresh, btn_edit,btn_w;
	private EditText etx;
	private EditText ety;
	private EditText etLoc;
	private WifiDatabase wifiDatabase;
	private WifiManager wm;
	DblocDAO dblocDAO = new DblocDAO(DetialLoc.this);
	WifiInfoDAO wifiInfoDAO = new WifiInfoDAO(DetialLoc.this);
	OriginalApInfoDAO originalApInfoDAO = new OriginalApInfoDAO(DetialLoc.this);

	private String[] ap = new String[5];
	private String[] ssid = new String[5];
	private String[] bssid = new String[5];
	private double[] RSSI = new double[5];
	private double[] regRSSI = new double[5];
	private String[] mac = new String[5];
	WifiInfomation wifiInfomation;
	private Thread thread;
	private Handler handler;
	long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detialloc);
		wifiDatabase = new WifiDatabase(DetialLoc.this);
		Intent intent = getIntent();
		final Bundle bundle = intent.getExtras();
		id = bundle.getLong("id") + 1;
		Log.e("id", String.valueOf(id));
		wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		etx = (EditText) findViewById(R.id.loc_detialX);
		ety = (EditText) findViewById(R.id.loc_detialY);
		etLoc = (EditText) findViewById(R.id.et_detialLoc);
		btn_edit = (Button) findViewById(R.id.btn_editLocDetial);
		Location location = dblocDAO.find(id);
		etx.setText(String.valueOf(location.getX()));
		ety.setText(String.valueOf(location.getY()));
		etLoc.setText(String.valueOf(location.getLocation()));
		btn_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (etx.getText().toString().equals("")
						|| ety.getText().toString().equals("")) {
					Builder builder = new AlertDialog.Builder(DetialLoc.this);
					builder.setTitle("提示信息");
					builder.setMessage("信息填写不完整！");
					builder.show();
				} else {
					Builder builder = new AlertDialog.Builder(DetialLoc.this);
					builder.setTitle("修改");
					builder.setMessage("你确定修改？");
					builder.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									wm.startScan();
									wifiDatabase = new WifiDatabase(
											DetialLoc.this);
									DblocDAO dblocDAO = new DblocDAO(
											DetialLoc.this);
									WifiInfoDAO wifiInfoDAO = new WifiInfoDAO(
											DetialLoc.this);
									Location location = new Location(id, Double
											.parseDouble(String.valueOf(etx
													.getText())), Double
											.parseDouble(String.valueOf(ety
													.getText())), etLoc
											.getText().toString());
									dblocDAO.update(location);
									List<ScanResult> list = wm.getScanResults();
									int number = list.size();
									List<WifiInfomation> wifiInfoList = wifiDatabase
											.getWifiInfomationById(id);
									for (int i = 0; i < wifiInfoList.size(); i++) {
										wifiInfoDAO.deleteById(id);
									}

									mac[0] = "5c:63:bf:d0:09:c0";
									mac[1] = "d4:94:e8:1a:3c:a0";
									mac[2] = "bc:46:99:12:8e:3e";
									mac[3] = "d4:94:e8:02:28:c0";
									mac[4] = "d4:94:e8:1a:3e:80";
									for (int i = 0; i < 5; i++) {
										for (ScanResult scanResult : list) {
											if (scanResult.BSSID.equals(mac[i])) {
												wifiDatabase.insertWifi(id,
														scanResult.SSID,
														scanResult.BSSID,
														scanResult.level);
											}
										}
									}

									Toast.makeText(DetialLoc.this, "修改成功", 500)
											.show();

								}
							});
					builder.setNegativeButton("否",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub

								}
							});
					builder.show();
				}
			}
		});
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				int num = 0;
				if (msg.what == 1) {
					num = msg.arg2;
				}
				List<ScanResult> scanList = wm.getScanResults();
				List<WifiInfomation> wifiInfoList = wifiDatabase
						.getWifiInfomationById(id);
				int wifiSize = wifiInfoList.size();
				for (ScanResult scanResult : scanList) {
					if (scanResult.BSSID.equals("5c:63:bf:d0:09:c0")) {
						Log.e("scanList", String.valueOf(scanResult.level));
					} else if (scanResult.BSSID.equals("d4:94:e8:1a:3c:a0")) {
						Log.e("scanList", String.valueOf(scanResult.level));
					} else if (scanResult.BSSID.equals("bc:46:99:12:8e:3e")) {
						Log.e("scanList", String.valueOf(scanResult.level));
					} else if (scanResult.BSSID.equals("d4:94:e8:02:28:c0")) {
						Log.e("scanList", String.valueOf(scanResult.level));
					} else if (scanResult.BSSID.equals("d4:94:e8:1a:3e:80")) {
						Log.e("scanList", String.valueOf(scanResult.level));
					}
				}
				for (WifiInfomation infomation : wifiInfoList) {
					Log.e("wifiList", String.valueOf(infomation.level));
				}
				ap[0] = "ap1";
				ap[1] = "ap2";
				ap[2] = "ap3";
				ap[3] = "ap4";
				ap[4] = "ap5";
				for (int i = 0; i < wifiInfoList.size(); i++) {
					for (ScanResult scanResult : scanList) {
						if (scanResult.BSSID.equals(wifiInfoList.get(i).bssid)) {
							ssid[i] = wifiInfoList.get(i).ssid;
							bssid[i] = wifiInfoList.get(i).bssid;
							RSSI[i] = scanResult.level;
							regRSSI[i] = (scanResult.level + (wifiInfoList
									.get(i).level) * num)
									/ (num + 1);
							// Log.e("num", String.valueOf(num));
							Log.e("rssiAvg", String.valueOf(regRSSI[i]));
						}
					}
				}

				for (int i = 0; i < wifiInfoList.size(); i++) {
					wifiInfoDAO.deleteById(id);
				}
				if(num==1){ 
					List<WifiInfomation> originalApInfoList = wifiDatabase
							.getOriginalApInfoById(id);
					for (int i = 0; i < originalApInfoList.size(); i++) {
						originalApInfoDAO.deleteById(i);
					}
				}
				for (int i = 0; i < 5; i++) {
					wifiDatabase.insertOriginalApInfo(id, ap[i], ssid[i],
							bssid[i], RSSI[i]);
				}
				for (int i = 0; i < 5; i++) {
					wifiDatabase.insertWifi(id, ssid[i], bssid[i], regRSSI[i]);
				}
				Toast.makeText(DetialLoc.this,
						"修改成功" + "[" + String.valueOf(num) + "]", 500).show();

			}

		};
		btn_refresh = (Button) findViewById(R.id.btn_refreshLocDetial);
		btn_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				thread = new Thread(new Runnable() {

					@Override
					public void run() {
						int num = 50;
						while (num != 0) {

							try {
								Thread.sleep(2500);
								wm.reassociate();
								Message msg = new Message();
								msg.what = 1;
								msg.arg2 = 51 - num;
								handler.sendMessage(msg);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							num--;
						}

					}
				});
				thread.start();
			}
		});
		btn_w = (Button)findViewById(R.id.buttonW);
		btn_w.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
