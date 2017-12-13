package com.example.wifi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChooseArithmetic extends Activity{

	private Button btn_triangle,btn_knn,btn_KDWKNN,btn_kmeans,btn_KWKNN,btn_nn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("—°‘ÒÀ„∑®");
		setContentView(R.layout.choosearithmetic);
		btn_knn = (Button)findViewById(R.id.btn_knn);
		btn_KDWKNN = (Button)findViewById(R.id.btn_KDWKNN);
		btn_triangle = (Button)findViewById(R.id.btn_triangle);
		btn_kmeans = (Button)findViewById(R.id.btn_kmeans);
		btn_KWKNN = (Button)findViewById(R.id.btn_KWKNN);
		btn_nn = (Button)findViewById(R.id.btn_nn);
		btn_knn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ChooseArithmetic.this,ShowPosKNN.class);
				startActivity(intent);
			}
		});
		btn_triangle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ChooseArithmetic.this,ShowPosTriangle.class);
				startActivity(intent);
			}
		});
		btn_KDWKNN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ChooseArithmetic.this,ShowPosKDWKNN.class);
				startActivity(intent);
			}
		});
		btn_kmeans.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ChooseArithmetic.this,ShowPositionKmeans.class);
				startActivity(intent);
			}
		});
		btn_KWKNN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ChooseArithmetic.this,ShowPositionKWKNN.class);
				startActivity(intent);
			}
		});
		btn_nn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ChooseArithmetic.this,ShowPositionNN.class);
				startActivity(intent);
			}
		});
	}

}
