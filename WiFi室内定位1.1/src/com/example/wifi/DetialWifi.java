package com.example.wifi;

import java.util.ArrayList;
import java.util.List;

import com.example.dao.WifiDatabase;
import com.example.dao.WifiInfoDAO;
import com.exmaple.model.WifiInfomation;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DetialWifi extends ListActivity {
	private ListView lv;
	private WifiDatabase database;
	private static WifiTester wifiTester;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("wifi列表");
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		wifiTester = app.getWifiTester();
		Intent intent  = getIntent();
		long id = intent.getExtras().getLong("id") + 1;
		Log.e("id", String.valueOf(id));
		database = new WifiDatabase(DetialWifi.this);
		WifiInfoDAO wifiInfoDAO = new WifiInfoDAO(DetialWifi.this);
		List<WifiInfomation> wifiInfoList = database.getWifiInfomationById(id);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getString(wifiInfoList));
		setListAdapter(adapter);
		lv = getListView();
		lv.setAdapter(adapter);
//		lv.setOnItemClickListener(this);

	}
	private String[] getString(List<WifiInfomation> wifiList) {
		// TODO Auto-generated method stub
		ArrayList<String> listStr = new ArrayList<String>();
		for(int i =0 ;i<wifiList.size();i++){
//		for(int i = wifiList.size()-1 ;i>=0;i--){
			listStr.add((i+1) + ":\nSSID:" + wifiList.get(i).getSsid() + "\nMAC地址：" + wifiList.get(i).getBssid() + "\n强度：" 
		+ wifiList.get(i).getLevel());
		}
		return listStr.toArray(new String[0]);
	}

	//将搜索到的wifi根据信号强度从强到弱进行排序  
	private void sortByLevel(ArrayList<WifiInfomation> list) {  
	        for(int i=0;i<list.size();i++)  
	            for(int j=1;j<list.size();j++)  
	            {  
	                if(list.get(i).level<list.get(j).level)    //level属性即为强度  
	                {  
	                    WifiInfomation temp = null;  
	                    temp = list.get(i);  
	                    list.set(i, list.get(j));  
	                    list.set(j, temp);  
	                }  
	            }  
	    }  
}
