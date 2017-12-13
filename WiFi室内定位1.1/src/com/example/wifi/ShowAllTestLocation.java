package com.example.wifi;

import java.util.ArrayList;
import java.util.List;
import com.example.wifi.R;
import com.example.dao.ExperimentLocDAO;
import com.example.dao.WifiDatabase;
import com.exmaple.model.Location;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAllTestLocation extends ListActivity implements
		OnItemClickListener {
	private ListView lv;
	private WifiDatabase database;
	ExperimentLocDAO experimentLocDAO = new ExperimentLocDAO(
			ShowAllTestLocation.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("实验数据");
		database = new WifiDatabase(ShowAllTestLocation.this);
		List<Location> testLocList = experimentLocDAO.getScrollData(0,
				experimentLocDAO.getCount());

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getString(testLocList));
		setListAdapter(adapter);
		lv = getListView();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(ShowAllTestLocation.this);
	}

	public String[] getString(List<Location> locList) {
		ArrayList<String> listStr = new ArrayList<String>();
		int num = locList.size();
		for (int i = 0; i < num; i++) {
			listStr.add((i + 1) + ":\n位置信息：\n" + "x坐标：" + locList.get(i).getX()
					+ "\ny坐标：" + locList.get(i).getY());
		}
		return listStr.toArray(new String[0]);

	}

	public void onItemClick(AdapterView<?> arg0, View v, int arg2, int  id) {
//		String strInfo = String.valueOf(((TextView) v).getText());
//		String strId = strInfo.substring(0, strInfo.indexOf(":"));
//		Toast.makeText(ShowAllTestLocation.this, strId.toString(), 500).show();
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(ShowAllTestLocation.this, HandleTestData.class);
		Bundle bundle = new Bundle();
		bundle.putLong("id", id);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
