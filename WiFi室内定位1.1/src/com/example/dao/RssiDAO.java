package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import com.exmaple.model.ClusterLocInfo;
import com.exmaple.model.RssiInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RssiDAO {
	private WifiDatabase helper;
	private SQLiteDatabase db;

	public RssiDAO(Context context) {
		helper = new WifiDatabase(context);
		db = helper.getWritableDatabase();
	}

	public void insertClusterLocInfo(long id, int i, int j) {
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("i_center", i);
		values.put("cluster_j", j);
		db.insert(WifiDatabase.CLUSTER_LOCATION_NAME, null, values);
	}

	public void insertCenter(long id, double rssi1, double rssi2, double rssi3,
			double rssi4, double rssi5) {
		ContentValues values = new ContentValues();
		values.put("CENTER_ID", id);
		values.put("RSSI1", rssi1);
		values.put("RSSI2", rssi2);
		values.put("RSSI3", rssi3);
		values.put("RSSI4", rssi4);
		values.put("RSSI5", rssi5);
		db.insert(WifiDatabase.CENTER_NAME, null, values);
	}

	public void insertCluster(long center_id, long cluster_id, double rssi1,
			double rssi2, double rssi3, double rssi4, double rssi5) {
		ContentValues values = new ContentValues();
		values.put("CENTER_ID", center_id);
		values.put("CLUSTER_ID", cluster_id);
		values.put("RSSI1", rssi1);
		values.put("RSSI2", rssi2);
		values.put("RSSI3", rssi3);
		values.put("RSSI4", rssi4);
		values.put("RSSI5", rssi5);
		db.insert(WifiDatabase.CLUSTER_NAME, null, values);
	}

	public List<RssiInfo> getCenterInfoById(long center_id) {
		List<RssiInfo> rssiList = new ArrayList<RssiInfo>();
		Cursor c = db.query(WifiDatabase.CENTER_NAME, null, "CENTER_ID=?",
				new String[] { String.valueOf(center_id) }, null, null, null);
		while (c.moveToNext()) {
			RssiInfo rssiInfo = new RssiInfo(c.getDouble(c
					.getColumnIndex("RSSI1")), c.getDouble(c
					.getColumnIndex("RSSI2")), c.getDouble(c
					.getColumnIndex("RSSI3")), c.getDouble(c
					.getColumnIndex("RSSI4")), c.getDouble(c
					.getColumnIndex("RSSI5")));
			rssiList.add(rssiInfo);
		}
		return rssiList;
	}

	public ClusterLocInfo getClusterLocInfoById(long id) {
		Cursor c = db.query(WifiDatabase.CLUSTER_LOCATION_NAME, null, "id=?",
				new String[] { String.valueOf(id) }, null, null, null);
		ClusterLocInfo clusterLocInfo = null;
		while (c.moveToNext()) {
			clusterLocInfo = new ClusterLocInfo(c.getInt(c
					.getColumnIndex("i_center")), c.getInt(c
					.getColumnIndex("cluster_j")));
		}
		return clusterLocInfo;
	}

	public RssiInfo getClusterInfoById(long center_id, long cluster_id) {
		// List<RssiInfo> rssiList = new ArrayList<RssiInfo>();
		Cursor c = db.query(
				WifiDatabase.CLUSTER_NAME,
				null,
				"CENTER_ID=? and CLUSTER_ID=?",
				new String[] { String.valueOf(center_id),
						String.valueOf(cluster_id) }, null, null, null);
		RssiInfo rssiInfo = null;
		while (c.moveToNext()) {
			rssiInfo = new RssiInfo(c.getDouble(c.getColumnIndex("RSSI1")),
					c.getDouble(c.getColumnIndex("RSSI2")), c.getDouble(c
							.getColumnIndex("RSSI3")), c.getDouble(c
							.getColumnIndex("RSSI4")), c.getDouble(c
							.getColumnIndex("RSSI5")));
			// rssiList.add(rssiInfo);
		}
		c.close();
		return rssiInfo;
	}

	public long getClusterLocCount() {
		Cursor c = db.rawQuery("select count(id) from clusterlocation", null);
		if (c.moveToNext()) {
			return c.getLong(0);
		}
		return 0;
	}

	public long getCenterCount() {
		Cursor c = db.rawQuery("select count(CENTER_ID) from centers", null);
		if (c.moveToNext()) {
			return c.getLong(0);
		}
		return 0;
	}

	public int getClusterSizeById(long id) {
		Cursor c = db.query(WifiDatabase.CLUSTER_NAME,
				new String[] { "count(*),CENTER_ID" }, "CENTER_ID=?",
				new String[] { String.valueOf(id) }, null, null, null);
		int count = 0;
		while (c.moveToNext()) {
			count = c.getInt(c.getColumnIndex("count(*)"));
		}
		c.close();
		return count;
	}

	public void deleteCenterById(long center_id) {
		db.delete(WifiDatabase.CENTER_NAME, "center_id=?",
				new String[] { String.valueOf(center_id) });
	}

	public void deleteClusterLocInfoById(long id) {
		db.delete(WifiDatabase.CLUSTER_LOCATION_NAME, "id=?",
				new String[] { String.valueOf(id) });
	}

	public void deleteClusterById(long center_id) {
		db.delete(WifiDatabase.CLUSTER_NAME, "CENTER_ID=?",
				new String[] { String.valueOf(center_id) });
	}

}
