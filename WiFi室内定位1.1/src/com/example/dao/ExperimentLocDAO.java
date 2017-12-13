package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import com.exmaple.model.Location;
import com.exmaple.model.Location;
import com.exmaple.model.PositionInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExperimentLocDAO {
	private WifiDatabase helper;
	private SQLiteDatabase db;
	private double x, y;

	public ExperimentLocDAO(Context context) {
		helper = new WifiDatabase(context);
	}

	public void add(Location location) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"insert into experimentdata (LID,X,Y) values (?,?,?)",
				new Object[] { location.getId(), location.getX(),
						location.getY() });
	}

	public void deleteById(long id){
		db = helper.getWritableDatabase();
		db.execSQL("delete from experimentdata where LID= ?",
				new Object[] { String.valueOf(id) });
	}

	public List<Location> getScrollData(long l, long end) {
		List<Location> locList = new ArrayList<Location>();
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select * from experimentdata limit ?,?",
				new String[] { String.valueOf(l), String.valueOf(end)});
		while (c.moveToNext()) {
			locList.add(new Location(c.getLong(c.getColumnIndex("LID")), c
					.getDouble(c.getColumnIndex("X")), c.getDouble(c
					.getColumnIndex("Y")), null));
		}
		return locList;
	}

	public long getCount() {
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select count(LID)from experimentdata", null);
		if (c.moveToNext()) {
			return c.getLong(0);
		}
		return 0;
	}

	public PositionInfo getTestPosInfoById(long strId) {
		// TODO Auto-generated method stub
		PositionInfo positionInfo = null;
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select X,Y from experimentdata where LID=?",
				new String[] { String.valueOf(strId) });
		if (c.moveToNext()) {
			positionInfo = new PositionInfo(c.getDouble(1), c.getDouble(2), "");
			return positionInfo;
		}
		return null;
	}

	public int getMaxId() {
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select max(LID)from experimentdata", null);
		if (c.moveToNext()) {
			return c.getInt(0);
		}
		return 0;
	}
}
