package com.skt.onem2m_device.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.skt.onem2m_device.R;
import com.skt.onem2m_device.data.UserInfo;

/**
 * activity for server information setting
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class SettingServerInfoActivity extends AppCompatActivity {
    private UserInfo                userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_server_info);

        ActionBar bar = getSupportActionBar();
        bar.setTitle(R.string.actionbar_setting_server_info);

        userInfo = UserInfo.getInstance(this);

        final EditText edtLoginURL = (EditText) findViewById(R.id.edit_login_url);
        final EditText edtRegisterURL = (EditText) findViewById(R.id.edit_register_url);
        final EditText edtServerURL = (EditText) findViewById(R.id.edit_server_url);
        final EditText edtAppEUI = (EditText) findViewById(R.id.edit_appeui);
        final CheckBox chkUseTLS = (CheckBox) findViewById(R.id.check_use_tls);
        final RadioGroup rdgTLV_TDV = (RadioGroup) findViewById(R.id.radiogroup_tlv_tdv);

        edtLoginURL.setText(userInfo.loadLoginURL());
        edtRegisterURL.setText(userInfo.loadRegisterURL());
        edtServerURL.setText(userInfo.loadServerURL());
        edtAppEUI.setText(userInfo.loadAppEUI());
        chkUseTLS.setChecked(userInfo.loadUseTLS());
        rdgTLV_TDV.check(userInfo.loadUseTLV() ? R.id.radio_use_tlv : R.id.radio_use_tdv);

        Button btnLoadDefault = (Button) findViewById(R.id.button_load_default);
        btnLoadDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo.clear(true);
                edtLoginURL.setText(userInfo.loadLoginURL());
                edtRegisterURL.setText(userInfo.loadRegisterURL());
                edtServerURL.setText(userInfo.loadServerURL());
                edtAppEUI.setText(userInfo.loadAppEUI());
                chkUseTLS.setChecked(userInfo.loadUseTLS());
                rdgTLV_TDV.check(userInfo.loadUseTLV() ? R.id.radio_use_tlv : R.id.radio_use_tdv);
            }
        });
        Button btnSave = (Button) findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo.saveLoginURL(edtLoginURL.getText().toString());
                userInfo.saveRegisterURL(edtRegisterURL.getText().toString());
                userInfo.saveServerURL(edtServerURL.getText().toString());
                userInfo.saveAppEUI(edtAppEUI.getText().toString());
                userInfo.saveUseTLS(chkUseTLS.isChecked());
                userInfo.saveUseTLV(rdgTLV_TDV.getCheckedRadioButtonId() == R.id.radio_use_tlv ? true : false);
                finish();
            }
        });
        Button btnCancel = (Button) findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
