package com.example.wifi;

import com.example.dao.PwdDAO;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	private EditText txtUser, txtPwd;
	private Button btn_login;
	private Button btn_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		txtUser = (EditText) findViewById(R.id.txtUser);
		txtPwd = (EditText) findViewById(R.id.txtPwd);
		btn_login = (Button) findViewById(R.id.loginBtn);
		btn_cancel = (Button) findViewById(R.id.canceBtn);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btn_login.setOnClickListener(new OnClickListener() {

			@TargetApi(Build.VERSION_CODES.GINGERBREAD)
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this, WifiTester.class);
				PwdDAO pwdDAO = new PwdDAO(Login.this);
				if (pwdDAO.getCount() == 0
						|| pwdDAO.find(0).getPassword().isEmpty()
						&& txtPwd.getText().toString().isEmpty()
						|| pwdDAO.find(0).getUsername().isEmpty()
						&& txtUser.getText().toString().isEmpty()) {
					startActivity(intent);
				} else if (pwdDAO.find(0).getUsername()
						.equals(txtUser.getText().toString())
						&& pwdDAO.find(0).getPassword()
								.equals(txtPwd.getText().toString())) {
					startActivity(intent);
				} else {
					Toast.makeText(Login.this, "请输入正确的账号、密码", Toast.LENGTH_SHORT)
							.show();
				}
				txtUser.setText("");
				txtPwd.setText("");
			}
		});
	}

	private long mExitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				// Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
//				CloseWifi();

			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
