package com.skt.onem2m_device.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skt.onem2m_device.R;
import com.skt.onem2m_device.data.UserInfo;

/**
 * activity for terms
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        ActionBar bar = getSupportActionBar();
        bar.setTitle(R.string.actionbar_terms);

        TextView viewContent = (TextView) findViewById(R.id.terms_content);
        viewContent.setMovementMethod(new ScrollingMovementMethod());

        Button buttonAgree = (Button) findViewById(R.id.terms_agree);
        buttonAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                moveToNext(true);
            }
        });

        Button buttonDisagree = (Button) findViewById(R.id.terms_disagree);
        buttonDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                moveToNext(false);
            }
        });
    }

    private void moveToNext(boolean agreeTerms) {
        UserInfo.getInstance(this).saveAgreeTerms(agreeTerms);

        Intent intent = new Intent(this, SensorListActivity.class);
        startActivity(intent);
        finish();
    }
}
