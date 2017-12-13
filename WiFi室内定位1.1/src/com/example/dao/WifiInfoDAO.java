package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import com.exmaple.model.WifiInfomation;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;

public class WifiInfoDAO {
	private WifiDatabase helper;
	private SQLiteDatabase db;

	public WifiInfoDAO(Context context) {
		helper = new WifiDatabase(context);
	}
	public void deleteById(long id){
		db = helper.getWritableDatabase();
		db.execSQL("delete from wifi where LID = ?", new Object[]{String.valueOf(id)});
	}
	public void update(WifiInfomation wifiInfo) {
		
		db = helper.getWritableDatabase();
		db.execSQL(
				"update wifi set WIFI_SSID=?,WIFI_BSSID=?,WIFI_LEVEL=? where LID = ?",
				new Object[] { wifiInfo.getSsid().toString(),
						wifiInfo.getBssid().toString(), wifiInfo.getLevel(),
						wifiInfo.getId() });
	}

	public List<WifiInfomation> getScrollData(int start, int count) {
		List<WifiInfomation> wifiList = new ArrayList<WifiInfomation>();
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select * from wifi limit ?,?", new String[] {
				String.valueOf(start), String.valueOf(count) });
		while (c.moveToNext()) {
			wifiList.add(new WifiInfomation(c.getLong(c.getColumnIndex("LID")),
					c.getString(c.getColumnIndex("WIFI_SSID")),null, c.getString(c
							.getColumnIndex("WIFI_BSSID")), c.getDouble(c
							.getColumnIndex("LEVEL"))));
		}
		return wifiList;
	}
	public long getCount(){
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select count(LID) from wifi", null);
		if(c.moveToNext()){
			return c.getLong(0);
		}
		return 0;
	}
}
