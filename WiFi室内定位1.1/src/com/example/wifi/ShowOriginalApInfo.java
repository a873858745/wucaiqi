package com.example.wifi;

import java.util.ArrayList;
import java.util.List;

import com.example.dao.OriginalApInfoDAO;
import com.example.dao.WifiDatabase;
import com.example.dao.WifiInfoDAO;
import com.exmaple.model.WifiInfomation;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ShowOriginalApInfo extends ListActivity {
	private ListView lv;
	private WifiDatabase database;
	private static WifiTester wifiTester;
	OriginalApInfoDAO originalApInfoDAO = new OriginalApInfoDAO(
			ShowOriginalApInfo.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("原始的ap信息");

		Intent intent = getIntent();
		long id = intent.getExtras().getLong("id") + 1;
		Log.e("id", String.valueOf(id));
		database = new WifiDatabase(ShowOriginalApInfo.this);
		 List<WifiInfomation> wifiInfoList =
		 database.getOriginalApInfoById(id);// 通过id查找
//		List<WifiInfomation> wifiInfoList = originalApInfoDAO
//				.getOriginalApInfoByIdApNum(id, "ap5");
		// List<WifiInfomation> wifiInfoList =
		// database.getOriginalApWifiInfoByIdAndAp(id, "ap1");
		// List<WifiInfomation> wifiInfoList =
		// database.getOriginalApWifiInfoByAp("ap1");//通过ap的序号查找
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getString(wifiInfoList));
		setListAdapter(adapter);
		lv = getListView();
		lv.setAdapter(adapter);
	}

	private String[] getString(List<WifiInfomation> wifiList) {
		// TODO Auto-generated method stub
		ArrayList<String> listStr = new ArrayList<String>();
		for (int i = 0; i < wifiList.size(); i++) {
			listStr.add(wifiList.get(i).getId() + "_" + (i + 1) + ":\napNum:"
					+ wifiList.get(i).getApNum() + "\nSSID:"
					+ wifiList.get(i).getSsid() + "\nMAC地址："
					+ wifiList.get(i).getBssid() + "\n强度："
					+ wifiList.get(i).getLevel());
		}
		return listStr.toArray(new String[0]);
	}

}
