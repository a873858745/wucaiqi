package com.example.wifi;
import com.example.dao.PwdDAO;
import com.exmaple.model.Register;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterInfo extends Activity {

	EditText txtUser, txtPwd;
	Button btnPwdCancel, btnPwdSet;
	PwdDAO pwdDAO = new PwdDAO(RegisterInfo.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		txtUser = (EditText) findViewById(R.id.txtuser);
		txtPwd = (EditText) findViewById(R.id.txtPwd);
		btnPwdSet = (Button) findViewById(R.id.btnPwdSet);
		btnPwdCancel = (Button) findViewById(R.id.btnPwdCancel);
		btnPwdSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Register register = new Register(txtUser.getText().toString(),
						txtPwd.getText().toString());
				if (pwdDAO.getCount() == 0) {
					pwdDAO.add(register);
				} else {
					pwdDAO.update(register);
				}
				Toast.makeText(RegisterInfo.this, "[’À∫≈√‹¬Î…Ë÷√≥…π¶£°]", Toast.LENGTH_SHORT)
						.show();
			}
		});
		btnPwdCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				txtUser.setText("");
				txtPwd.setText("");
			}
		});
	}

}
