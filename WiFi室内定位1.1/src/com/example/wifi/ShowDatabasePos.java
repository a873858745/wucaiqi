package com.example.wifi;

import java.util.ArrayList;
import java.util.List;

import com.example.dao.DblocDAO;
import com.example.dao.WifiDatabase;
import com.exmaple.model.Location;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ShowDatabasePos extends ListActivity implements
		OnItemClickListener, OnItemLongClickListener {

	private ListView lv;
	private WifiDatabase database;
	private static WifiTester wifiTester;
	DblocDAO dblocDAO = new DblocDAO(ShowDatabasePos.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("��ǰλ���б�");
		WifiTesterApp app = (WifiTesterApp) this.getApplication();
		wifiTester = app.getWifiTester();

		database = new WifiDatabase(ShowDatabasePos.this);
		List<Integer> locIdList = database.getAllPositionId();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getString(locIdList));
		setListAdapter(adapter);
		lv = getListView();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);

	}

	public String[] getString(List<Integer> locIdList) {
		ArrayList<String> listStr = new ArrayList<String>();
		int num = locIdList.size();
		for (int i = 0; i < num; i++) {
			listStr.add((i + 1) + ":\nλ����Ϣ��\n"
					+ database.getPositionInfoById(i + 1));
		}
		return listStr.toArray(new String[0]);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		Toast.makeText(ShowDatabasePos.this, String.valueOf(id), 1000).show();
		bundle.putLong("id", id);
		intent.putExtras(bundle);
		intent.setClass(ShowDatabasePos.this, DetialLoc.class);
		ShowDatabasePos.this.startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			final long id) {
		Builder builder = new AlertDialog.Builder(ShowDatabasePos.this);
		builder.setTitle("ToWifiList");
		builder.setMessage("��ȷ����");
		builder.setPositiveButton("��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Log.e("id", String.valueOf(id));
				// Toast.makeText(ShowDatabasePos.this, String.valueOf(id),
				// 500);
				Intent intent = new Intent();
				intent.setClass(ShowDatabasePos.this, DetialWifi.class);
				Bundle bundle = new Bundle();
				bundle.putLong("id", id);
				intent.putExtras(bundle);
				ShowDatabasePos.this.startActivity(intent);
			}
		});

		builder.setNeutralButton("����ap����", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.e("id", String.valueOf(id));
				// Toast.makeText(ShowDatabasePos.this, String.valueOf(id),
				// 500);
				Intent intent = new Intent();
				intent.setClass(ShowDatabasePos.this,
						ShowOriginalInfoByApNum.class);
				Bundle bundle = new Bundle();
				bundle.putLong("id", id);
				intent.putExtras(bundle);
				ShowDatabasePos.this.startActivity(intent);
			}
		});

		builder.setNeutralButton("ԭʼap��Ϣ", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Log.e("id", String.valueOf(id));
				// Toast.makeText(ShowDatabasePos.this, String.valueOf(id),
				// 500);
				Intent intent = new Intent();
				intent.setClass(ShowDatabasePos.this, ShowOriginalApInfo.class);
				Bundle bundle = new Bundle();
				bundle.putLong("id", id);
				intent.putExtras(bundle);
				ShowDatabasePos.this.startActivity(intent);
			}
		});

		builder.setNegativeButton("��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		builder.show();
		return true;
	}
}
