package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import com.exmaple.model.PositionInfo;
import com.exmaple.model.WifiInfomation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WifiDatabase extends SQLiteOpenHelper {

	static final String DATABASE_NAME = "wifilocation.db";
	public static final int DATABASE_VERSION = 3;
	public static final String WIFI_TABLE_NAME = "wifi";
	public static final String LOCATION_TABLE_NAME = "location";
	public static final String REGISTER_TABLE_NAME = "register";
	public static final String EXPERIMENTDATA_TABLE_NAME = "experimentdata";
	public static final String ORIGINAL_AP_INFORMATION_TABLE_NAME = "originalApInfo";
	public static final String ONLINE_LOCATION_TABLE_NAME = "onlineLocationInfo";
	public static final String ONLINE_AP_INFO_TABLE_NAME = "onlineApInfo";
	public static final String CENTER_NAME = "centers";
	public static final String CLUSTER_NAME = "cluster";
	public static final String CLUSTER_LOCATION_NAME = "clusterlocation";
	
	private static final String CLUSTER_LOCATION_CREATE_SQL = "CREATE TABLE " + CLUSTER_LOCATION_NAME
			+" ( id integer," + "i_center integer," + "cluster_j integer );";
	private static final String ALTER_CLUSTER_TABLE_SQL = "alter table " + CLUSTER_NAME
			+ " add column weight double";
	

	private static final String LOCATION_CREAT_DLL = "CREATE TABLE "
			+ LOCATION_TABLE_NAME + "( LID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "X Double," + "Y Double," + "LOCATION_DISCRIPTION TEXT);";
	private static final String WIFI_CREAT_DLL = "CREATE TABLE "
			+ WIFI_TABLE_NAME + " ( LID INTEGER," + "WIFI_SSID TEXT,"
			+ "WIFI_BSSID TEXT NOT NULL," + "WIFI_LEVEL Double NOT NULL,"
			+ "FOREIGN KEY (LID) REFERENCES " + LOCATION_TABLE_NAME + ");";
	
	private static final String CENTER_CREAT_DLL = "CREATE TABLE "
			+ CENTER_NAME + " ( CENTER_ID INTEGER PRIMARY KEY ," + "RSSI1 Double,"
			+ "RSSI2 Double," + "RSSI3 Double," + "RSSI4 Double,"
			+ "RSSI5 Double" + ");";
	private static final String CLUSTER_CREAT_DLL = "CREATE TABLE "
			+ CLUSTER_NAME + " ( CENTER_ID INTEGER," +"CLUSTER_ID INTEGER," +  "RSSI1 Double,"
			+ "RSSI2 Double," + "RSSI3 Double," + "RSSI4 Double,"
			+ "RSSI5 Double," + "FOREIGN KEY (CENTER_ID) REFERENCES "
			+ CENTER_NAME + ");";

	private static final String REGISTER_CREAT_DLL = "CREATE TABLE "
			+ REGISTER_TABLE_NAME + " ( USERNAME TEXT," + "PASSWORD TEXT"
			+ ");";
	private static final String EXPERIMENTDATA_CREAT_DLL = "CREATE TABLE "
			+ EXPERIMENTDATA_TABLE_NAME + " (LID INTEGER," + "X Double,"
			+ "Y Double" + ");";

	private static final String ORIGINAL_AP_CREAT_DLL = "CREATE TABLE "
			+ ORIGINAL_AP_INFORMATION_TABLE_NAME + " ( LID INTEGER,"
			+ "APNUM TEXT," + "WIFI_SSID TEXT," + "WIFI_BSSID TEXT NOT NULL,"
			+ "WIFI_LEVEL Double NOT NULL," + "FOREIGN KEY (LID) REFERENCES "
			+ LOCATION_TABLE_NAME + ");";
	private static final String ONLINE_LOCATION_CREAT_DLL = "CREATE TABLE "
			+ ONLINE_LOCATION_TABLE_NAME
			+ "( LID INTEGER PRIMARY KEY AUTOINCREMENT," + "X Double,"
			+ "Y Double," + "LOCATION_DISCRIPTION TEXT);";
	private static final String ONLINE_AP_INFO_CREAT_DLL = "CREATE TABLE "
			+ ONLINE_AP_INFO_TABLE_NAME + " ( LID INTEGER," + "APNUM TEXT,"
			+ "WIFI_SSID TEXT," + "WIFI_BSSID TEXT NOT NULL,"
			+ "WIFI_LEVEL Double NOT NULL," + "FOREIGN KEY (LID) REFERENCES "
			+ ONLINE_LOCATION_TABLE_NAME + ");";

	// private static final String ORIGINAL_AP_CREAT_DLL = "CREATE TABLE "
	// + ORIGINAL_AP_INFORMATION_TABLE_NAME + " (LID INTEGER," + "X Double," +
	// "Y Double," + "WIFI_BSSID TEXT NOT NULL" + ");";
	public WifiDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(WIFI_CREAT_DLL);
		database.execSQL(LOCATION_CREAT_DLL);
		database.execSQL(REGISTER_CREAT_DLL);
		database.execSQL(EXPERIMENTDATA_CREAT_DLL);
		database.execSQL(ORIGINAL_AP_CREAT_DLL);
	}

	public long insertLocation(double x, double y, String locdis) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("X", x);
		values.put("Y", y);
		values.put("LOCATION_DISCRIPTION", locdis);
		long lid = database.insert(LOCATION_TABLE_NAME, null, values);
		database.close();
		return lid;
	}

	public long insertOnlineLocation(double x, double y, String locdis) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("X", x);
		values.put("Y", y);
		values.put("LOCATION_DISCRIPTION", locdis);
		long lid = database.insert(ONLINE_LOCATION_TABLE_NAME, null, values);
		database.close();
		return lid;
	}

	public long insertWifi(Long id, String ssid, String bssid, double level) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("LID", id);
		values.put("WIFI_SSID", ssid);
		values.put("WIFI_BSSID", bssid);
		values.put("WIFI_LEVEL", level);
		return database.insert(WIFI_TABLE_NAME, null, values);
	}

	public long insertOnlineWifi(Long id, String ssid, String bssid,
			double level) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("LID", id);
		values.put("WIFI_SSID", ssid);
		values.put("WIFI_BSSID", bssid);
		values.put("WIFI_LEVEL", level);
		return database.insert(ONLINE_AP_INFO_TABLE_NAME, null, values);
	}

	public long insertOriginalApInfo(Long id, String apNum, String ssid,
			String bssid, double level) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("LID", id);
		values.put("APNUM", apNum);
		values.put("WIFI_SSID", ssid);
		values.put("WIFI_BSSID", bssid);
		values.put("WIFI_LEVEL", level);
		return database
				.insert(ORIGINAL_AP_INFORMATION_TABLE_NAME, null, values);
	}

	public long insertExperimentData(long id, double x, double y) {
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("LID", id);
		values.put("X", x);
		values.put("Y", y);
		return database.insert(EXPERIMENTDATA_TABLE_NAME, null, values);
	}

	public void deleteExperimentData(long strId) {
		SQLiteDatabase database = getWritableDatabase();
		database.delete(EXPERIMENTDATA_TABLE_NAME, "LID=?",
				new String[] { String.valueOf(strId) });
	}

	

	public List<Integer> getAllPositionId() {
		// TODO Auto-generated method stub
		List<Integer> list = new ArrayList<Integer>();
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.query(LOCATION_TABLE_NAME, null, null, null,
				null, null, "LID");
		while (cursor.moveToNext()) {
			Integer lid = cursor.getInt(0);
			list.add(lid);
		}
		return list;
	}

	public PositionInfo getPositionInfoById(Integer id) {
		// TODO Auto-generated method stub
		PositionInfo positionInfo = null;
		SQLiteDatabase database = getReadableDatabase();
		String selection = "LID = ?";
		String[] selectionArgs = { id.toString() };
		Cursor c = database.query(LOCATION_TABLE_NAME, null, selection,
				selectionArgs, null, null, null);
		if (c.moveToNext()) {
			positionInfo = new PositionInfo(c.getDouble(1), c.getDouble(2),
					c.getString(3));

		}
		c.close();
		database.close();
		return positionInfo;
	}

	/** 通过参考点ID获得其Wifi信息 */
	public List<WifiInfomation> getWifiInfomationById(long id) {
		List<WifiInfomation> list = new ArrayList<WifiInfomation>();
		SQLiteDatabase database = getReadableDatabase();
		String selection = "LID = ?";
		String[] selectionArgs = { String.valueOf(id) };
		Cursor cursor = database.query(WIFI_TABLE_NAME, null, selection,
				selectionArgs, null, null, null);
		while (cursor.moveToNext()) {
			WifiInfomation WifiInfomation = new WifiInfomation(
					cursor.getInt(0), null, cursor.getString(1),
					cursor.getString(2), cursor.getDouble(3));
			list.add(WifiInfomation);
		}
		cursor.close();
		database.close();
		return list;
	}

	/** 通过参考点ID获得其原始ap信息 */
	public List<WifiInfomation> getOriginalApInfoById(long id) {
		List<WifiInfomation> list = new ArrayList<WifiInfomation>();
		SQLiteDatabase database = getReadableDatabase();
		String selection = "LID = ?";
		String[] selectionArgs = { String.valueOf(id) };
		Cursor cursor = database.query(ORIGINAL_AP_INFORMATION_TABLE_NAME,
				null, selection, selectionArgs, null, null, null);
		while (cursor.moveToNext()) {
			WifiInfomation WifiInfomation = new WifiInfomation(
					cursor.getInt(0), cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getDouble(4));
			list.add(WifiInfomation);
		}
		cursor.close();
		database.close();
		return list;
	}

	public List<WifiInfomation> getOriginalApWifiInfoByAp(String ap) {
		List<WifiInfomation> list = new ArrayList<WifiInfomation>();
		SQLiteDatabase database = getReadableDatabase();
		String selection = "APNUM = ?";
		String[] selectionArgs = { ap };
		Cursor cursor = database.query(ORIGINAL_AP_INFORMATION_TABLE_NAME,
				null, selection, selectionArgs, null, null, null);
		while (cursor.moveToNext()) {
			WifiInfomation WifiInfomation = new WifiInfomation(
					cursor.getInt(0), cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getDouble(4));
			list.add(WifiInfomation);
		}
		cursor.close();
		database.close();
		return list;
	}

	public List<WifiInfomation> getOriginalApWifiInfoByIdAndAp(long id,
			String ap) {
		List<WifiInfomation> list = new ArrayList<WifiInfomation>();
		SQLiteDatabase database = getReadableDatabase();
		String selection = "LID = ? and APNUM = ?";
		String[] selectionArgs = { String.valueOf(id), ap };
		Cursor cursor = database.query(ORIGINAL_AP_INFORMATION_TABLE_NAME,
				null, selection, selectionArgs, null, null, null, null);
		while (cursor.moveToNext()) {
			WifiInfomation WifiInfomation = new WifiInfomation(
					cursor.getInt(0), cursor.getString(1), cursor.getString(2),
					cursor.getString(3), cursor.getDouble(4));
			list.add(WifiInfomation);
		}
		cursor.close();
		database.close();
		return list;
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		switch(oldVersion){
		case 2:
//			db.execSQL(CENTER_CREAT_DLL);
//			db.execSQL(CLUSTER_CREAT_DLL);
//			db.execSQL(CLUSTER_LOCATION_CREATE_SQL);
//			db.execSQL(ALTER_CLUSTER_TABLE_SQL);
			break;
		}
	}

}
