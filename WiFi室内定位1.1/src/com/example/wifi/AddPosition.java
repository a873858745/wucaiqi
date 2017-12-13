package com.example.wifi;

import java.util.List;

import javax.crypto.Mac;

import com.example.dao.OriginalApInfoDAO;
import com.example.dao.WifiDatabase;
import com.example.wifi.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddPosition extends Activity {

	private Button btn_ok;
	private EditText etx;
	private EditText ety;
	private EditText etLoc;
	private WifiDatabase wifiDatabase;
	private OriginalApInfoDAO originalApInfoDAO;
	private WifiManager wm;
	private double[] RSSI = new double[5];
	private String[] ap = new String[5];
	private String[] mac = new String[5];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addloction);
		wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!wm.isWifiEnabled()) {
			wm.setWifiEnabled(true);
		}
		wm.startScan();
		etx = (EditText) findViewById(R.id.loc_x);
		ety = (EditText) findViewById(R.id.loc_y);
		etLoc = (EditText) findViewById(R.id.et_loc);
		btn_ok = (Button) findViewById(R.id.btn_submit);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				wifiDatabase = new WifiDatabase(AddPosition.this);
				if (etx.getText().toString().equals("")
						|| ety.getText().toString().equals("")) {
					Builder builder = new AlertDialog.Builder(AddPosition.this);
					builder.setTitle("提示信息");
					builder.setMessage("信息填写不完整！");
					builder.show();
				} else {
					Builder builder = new AlertDialog.Builder(AddPosition.this);
					builder.setTitle("添加");
					builder.setMessage("你确定添加？");
					builder.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									wifiDatabase = new WifiDatabase(
											AddPosition.this);
									originalApInfoDAO = new OriginalApInfoDAO(
											AddPosition.this);
									ap[0] = "ap1";
									ap[1] = "ap2";
									ap[2] = "ap3";
									ap[3] = "ap4";
									ap[4] = "ap5";
									long lid = wifiDatabase.insertLocation(
											Double.parseDouble(etx.getText()
													.toString()), Double
													.parseDouble(ety.getText()
															.toString()), etLoc
													.getText().toString());
									List<ScanResult> list = wm.getScanResults();
									int number = list.size();
									mac[0] = "5c:63:bf:d0:09:c0";
									mac[1] = "d4:94:e8:1a:3c:a0";
									mac[2] = "bc:46:99:12:8e:3e";
									mac[3] = "d4:94:e8:02:28:c0";
									mac[4] = "d4:94:e8:1a:3e:80";
									for(int i=0;i<5;i++){
										for (ScanResult scanResult : list) {
											if (scanResult.BSSID
													.equals(mac[i])) {
												wifiDatabase.insertWifi(lid,
														scanResult.SSID,
														scanResult.BSSID,
														scanResult.level);
											}
										}
									}
									Intent intent = new Intent();
									intent.setClass(AddPosition.this,
											WifiTester.class);
									AddPosition.this.startActivity(intent);
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
	}
}
