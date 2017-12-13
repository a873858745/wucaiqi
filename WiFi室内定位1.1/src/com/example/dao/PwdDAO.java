package com.example.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.exmaple.model.Register;

public class PwdDAO {
	private WifiDatabase helper;
	private SQLiteDatabase db;

	public PwdDAO(Context context) {
		helper = new WifiDatabase(context);
	}

	public void add(Register register) {
		db = helper.getWritableDatabase();
		db.execSQL("insert into register (USERNAME,PASSWORD) values (?,?)",
				new Object[] { register.getUsername(), register.getPassword() });
	}

	public Register find(int id) {
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select USERNAME,PASSWORD from register", null);
		if (c.moveToNext()) {
			return new Register(c.getString(c.getColumnIndex("USERNAME")),
					c.getString(c.getColumnIndex("PASSWORD")));
		}
		return null;
	}

	public void update(Register register) {
		db = helper.getWritableDatabase();
		db.execSQL("update register set USERNAME = ?,PASSWORD = ?",
				new Object[] { register.getUsername(), register.getPassword() });
	}

	public long getCount() {
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select count(USERNAME) from register", null);
		if (c.moveToNext()) {
			return c.getLong(0);
		}
		return 0;
	}
}
