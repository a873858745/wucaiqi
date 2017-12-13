package com.example.dao;

import com.exmaple.model.Location;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OnlineLocDAO {
	private WifiDatabase helper;
	private SQLiteDatabase db;
	public OnlineLocDAO(Context context){
		helper = new WifiDatabase(context);
	}
	
	public void deleteLocationByID(long id) {
		db = helper.getWritableDatabase();
		db.execSQL("delete from onlineLocationInfo where LID = ?",
				new Object[] { String.valueOf(id) });
	}

	public Location find(long id) {
		db = helper.getWritableDatabase();
		Cursor c = db
				.rawQuery(
						"select LID,X,Y,LOCATION_DISCRIPTION from onlineLocationInfo where LID =?",
						new String[]{String.valueOf(id)});
		if (c.moveToNext()) {
			return new Location(c.getLong(c.getColumnIndex("LID")), 
					c.getDouble(c.getColumnIndex("X")), c.getDouble(c.getColumnIndex("Y")),
					c.getString(c.getColumnIndex("LOCATION_DISCRIPTION")));
		}
		return null;
	}

	public void update(Location location) {
		db = helper.getWritableDatabase();
		db.execSQL(
				"update onlineLocationInfo set X=?,Y=?,LOCATION_DISCRIPTION =? where LID =?",
				new Object[] { location.getX(), location.getY(),
						location.getLocation(),location.getId()});
	}
}
