package com.example.wifi;

import java.util.List;

import com.example.dao.OriginalApInfoDAO;
import com.example.dao.WifiDatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddOnlineLoc extends Activity{
	private Button btn_ok_online;
	private EditText etx_online;
	private EditText ety_online;
	private EditText etLoc_online;
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
		etx_online = (EditText) findViewById(R.id.online_loc_x);
		ety_online = (EditText) findViewById(R.id.online_loc_y);
		etLoc_online = (EditText) findViewById(R.id.et_loc_online);
		btn_ok_online = (Button) findViewById(R.id.btn_submit_online);
		btn_ok_online.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				wifiDatabase = new WifiDatabase(AddOnlineLoc.this);
				if (etx_online.getText().toString().equals("")
						|| ety_online.getText().toString().equals("")) {
					Builder builder = new AlertDialog.Builder(AddOnlineLoc.this);
					builder.setTitle("��ʾ��Ϣ");
					builder.setMessage("��Ϣ��д��������");
					builder.show();
				} else {
					Builder builder = new AlertDialog.Builder(AddOnlineLoc.this);
					builder.setTitle("���");
					builder.setMessage("��ȷ����ӣ�");
					builder.setPositiveButton("��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									wifiDatabase = new WifiDatabase(
											AddOnlineLoc.this);
//									originalApInfoDAO = new OriginalApInfoDAO(
//											AddOnlineLoc.this);
									ap[0] = "ap1";
									ap[1] = "ap2";
									ap[2] = "ap3";
									ap[3] = "ap4";
									ap[4] = "ap5";
									long lid = wifiDatabase.insertOnlineLocation(
											Double.parseDouble(etx_online.getText()
													.toString()), Double
													.parseDouble(ety_online.getText()
															.toString()), etLoc_online
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
												wifiDatabase.insertOnlineWifi(lid,
														scanResult.SSID,
														scanResult.BSSID,
														scanResult.level);
											}
										}
									}
								}
							});
					builder.setNegativeButton("��",
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
