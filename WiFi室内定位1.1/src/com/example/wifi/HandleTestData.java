package com.example.wifi;

import java.util.List;

import com.example.dao.ExperimentLocDAO;
import com.example.dao.WifiDatabase;
import com.exmaple.model.Location;
import com.exmaple.model.WifiInfomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HandleTestData extends Activity {

	private EditText txtX, txtY;
	private Button btn_delete;
	ExperimentLocDAO experimentLocDAO = new ExperimentLocDAO(
			HandleTestData.this);
	WifiDatabase wifiDatabase = new WifiDatabase(HandleTestData.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.handletestdata);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		final long strId = (Long) bundle.get("id")+1;
		txtX = (EditText)findViewById(R.id.testloc_detialX);
		txtY = (EditText)findViewById(R.id.testloc_detialY);
		btn_delete = (Button)findViewById(R.id.btn_deleteTestLoc);
		txtX.setText(String.valueOf(experimentLocDAO.getScrollData(strId-1,
				strId+1).get(0).getX()));
		txtY.setText(String.valueOf(experimentLocDAO.getScrollData(strId-1,
				strId+1).get(0).getY()));
		btn_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				Builder builder = new AlertDialog.Builder(HandleTestData.this);
				builder.setTitle("删除");
				builder.setMessage("你确定？");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						List<Location>  locList =experimentLocDAO.getScrollData(0, experimentLocDAO.getCount()) ;
						wifiDatabase.deleteExperimentData(strId);
//						experimentLocDAO.deleteById(strId+1);
						for(int i=0;i<locList.size();i++){
							String str = " x:" + locList.get(i).getX()+ "y:" + locList.get(i).getY();
							Log.e("坐标", str);
						}
					}
				});
				builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				});
				builder.show();
			}
		});
	}
}
