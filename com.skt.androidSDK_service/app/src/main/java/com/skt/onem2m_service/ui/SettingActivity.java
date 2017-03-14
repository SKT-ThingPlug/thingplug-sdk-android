package com.skt.onem2m_service.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skt.onem2m_service.Const;
import com.skt.onem2m_service.R;
import com.skt.onem2m_service.data.UserInfo;

/**
 * activity for setting
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class SettingActivity extends AppCompatActivity {
    private UserInfo                userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar bar = getSupportActionBar();
        bar.setTitle(R.string.actionbar_setting);

        userInfo = UserInfo.getInstance(this);

        final EditText edtTransfer = (EditText) findViewById(R.id.edit_transfer);
        final EditText edtUpdateList = (EditText) findViewById(R.id.edit_update_list);
        final EditText edtUpdateGraph = (EditText) findViewById(R.id.edit_update_graph);

        edtTransfer.setText(String.valueOf(userInfo.loadTransferInterval()));
        edtUpdateList.setText(String.valueOf(userInfo.loadListInterval()));
        edtUpdateGraph.setText(String.valueOf(userInfo.loadGraphInterval()));

        Button btnSave = (Button) findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int transfer = Integer.parseInt(edtTransfer.getText().toString());
                int updateList = Integer.parseInt(edtUpdateList.getText().toString());
                int updateGraph = Integer.parseInt(edtUpdateGraph.getText().toString());

                String toastMessage = null;
                if (transfer < Const.SENSOR_MIN_TRANSFER_INTERVAL)              { toastMessage = String.format(getResources().getString(R.string.setting_error), Const.SENSOR_MIN_TRANSFER_INTERVAL, getResources().getString(R.string.setting_transfer)); }
                else if (updateList < Const.SENSOR_MIN_LIST_UPDATE_INTERVAL)    { toastMessage = String.format(getResources().getString(R.string.setting_error), Const.SENSOR_MIN_LIST_UPDATE_INTERVAL, getResources().getString(R.string.setting_update_list)); }
                else if (updateGraph < Const.SENSOR_MIN_GRAPH_UPDATE_INTERVAL)  { toastMessage = String.format(getResources().getString(R.string.setting_error), Const.SENSOR_MIN_GRAPH_UPDATE_INTERVAL, getResources().getString(R.string.setting_update_graph)); }

                if (toastMessage != null) {
                    Toast.makeText(SettingActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    userInfo.saveTransferInterval(transfer);
                    userInfo.saveListInterval(updateList);
                    userInfo.saveGraphInterval(updateGraph);
                    setResult(SensorListActivity.SETTING_RESULT_SAVE);
                    finish();
                }
            }
        });
        Button btnCancel = (Button) findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnLogout = (Button) findViewById(R.id.button_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(SensorListActivity.SETTING_RESULT_LOGOUT);
                finish();
            }
        });
    }
}
