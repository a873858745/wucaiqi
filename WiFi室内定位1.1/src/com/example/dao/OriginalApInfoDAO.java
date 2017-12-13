package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import com.exmaple.model.WifiInfomation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OriginalApInfoDAO {
	private WifiDatabase helper;
	private SQLiteDatabase db;

	public OriginalApInfoDAO(Context context) {
		helper = new WifiDatabase(context);
	}
	public void deleteById(long id){
		db = helper.getWritableDatabase();
		db.execSQL("delete from originalApInfo where LID = ?", new Object[]{String.valueOf(id)});
	}
	public List<WifiInfomation> getOriginalApInfoById(long id) {
		List<WifiInfomation> list = new ArrayList<WifiInfomation>();
		db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from originalApInfo limit ?",
				new String[] {String.valueOf(id)});
		while (cursor.moveToNext()) {
			WifiInfomation WifiInfomation = new WifiInfomation(
					cursor.getInt(0), cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getDouble(4));
			list.add(WifiInfomation);
		}
		cursor.close();
		db.close();
		return list;
	}
	public List<WifiInfomation> getOriginalApInfoByIdApNum(long id,String ap) {
		List<WifiInfomation> list = new ArrayList<WifiInfomation>();
		db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from originalApInfo where LID=? and APNUM=?",
				new String[] {String.valueOf(id), ap });
		while (cursor.moveToNext()) {
			WifiInfomation WifiInfomation = new WifiInfomation(
					cursor.getInt(0), cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getDouble(4));
			list.add(WifiInfomation);
		}
		cursor.close();
		db.close();
		return list;
	}
}
