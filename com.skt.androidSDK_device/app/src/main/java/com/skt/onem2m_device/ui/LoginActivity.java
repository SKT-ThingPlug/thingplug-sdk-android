package com.skt.onem2m_device.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.onem2m_device.Const;
import com.skt.onem2m_device.R;
import com.skt.onem2m_device.data.UserInfo;
import com.skt.onem2m_device.data.Utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import tp.skt.onem2m.binder.mqtt_v1_1.control.execInstanceControl;

/**
 * activity for login
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class LoginActivity extends AppCompatActivity {

    private final static String     TAG = LoginActivity.class.getSimpleName();

    private final int               PERMISSION_POPUP = 1000;

    // UI references.
    private AutoCompleteTextView    textviewId;
    private EditText                textviewPassword;

    private OneM2MWorker            oneM2MWorker;
    private UserInfo                userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        oneM2MWorker = OneM2MWorker.getInstance();
        oneM2MWorker.setStateListener(new OneM2MWorkerListener());
        userInfo = UserInfo.getInstance(this);

        ActionBar bar = getSupportActionBar();
        bar.setTitle(R.string.actionbar_login);

        // Set up the login form.
        textviewId = (AutoCompleteTextView) findViewById(R.id.id);

        textviewPassword = (EditText) findViewById(R.id.password);
        textviewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        View changeServerInfoView = findViewById(R.id.change_server_info);
        changeServerInfoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SettingServerInfoActivity.class);
                startActivity(intent);
            }
        });

        View joinThingPlugView = findViewById(R.id.join_thingplug);
        joinThingPlugView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.URL_JOIN_THINGPLUG));
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        else {
            checkAutoLogin();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, PERMISSION_POPUP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_POPUP) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
            }

            checkAutoLogin();
        }
    }

    private void moveToNext(boolean autoLogin) {
        Intent intent;
        if (autoLogin) {
            intent = new Intent(this, SensorListActivity.class);
        }
        else {
            intent = new Intent(this, TermsActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void checkAutoLogin() {
        String id = userInfo.loadID();
        String password = userInfo.loadPassword();
        String uKey = userInfo.loadUKey();
        boolean registerState = userInfo.getRegisterState();
        if (!id.isEmpty() && !password.isEmpty() && !uKey.isEmpty() && registerState == true) {
            showProgress(true);
            oneM2MWorker.setDatas(id, password, uKey);
            oneM2MWorker.connect(this, false);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        textviewId.setError(null);
        textviewPassword.setError(null);

        // Store values at the time of the login attempt.
        String id = textviewId.getText().toString();
        String password = textviewPassword.getText().toString();

        // Check for a valid id
        if (TextUtils.isEmpty(id)) {
            textviewId.setError(getString(R.string.error_field_required));
            textviewId.requestFocus();
            return;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            textviewPassword.setError(getString(R.string.error_field_required));
            textviewPassword.requestFocus();
            return;
        }

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            textviewPassword.setError(getString(R.string.error_invalid_password));
            textviewPassword.requestFocus();
            return;
        }

        showProgress(true);
        // disconnect server
        oneM2MWorker.disconnect();
        new UserLoginTask().execute(id, password);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final View loginFormView = findViewById(R.id.login_form);
                final View progressView = findViewById(R.id.login_progress);

                // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
                // for very easy animations. If available, use these APIs to fade-in
                // the progress spinner.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    loginFormView.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    progressView.animate().setDuration(shortAnimTime).alpha(
                            show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
                } else {
                    // The ViewPropertyAnimator APIs are not available, so simply show
                    // and hide the relevant UI components.
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }

                if (!show) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String id = params[0];
            String password = params[1];

            // login to ThingPlug portal
            String uKey = loginToThingPlug(id, password);
            if (!uKey.isEmpty()) {
                // register
                oneM2MWorker.setDatas(id, password, uKey);
                oneM2MWorker.connect(LoginActivity.this, true);
            }
            else {
                showProgress(false);
            }
            return null;
        }
    }

    private String loginToThingPlug(String id, String pw) {
        String uKey = "";
        try {
            URL url = new URL(userInfo.loadLoginURL() + Const.URL_LOGIN);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("PUT");
            request.setRequestProperty("user_id", id);
            request.setRequestProperty("password", pw);
            request.setRequestProperty("locale", "ko");

            int responseCode = request.getResponseCode();
            Log.i(TAG, "[" + url.toString() + "]" + "responseCode : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = request.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                int nLength = 0;
                while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }

                try {
                    XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlParser = xmlFactoryObject.newPullParser();
                    xmlParser.setInput(new StringReader(new String(baos.toByteArray())));
                    String tagName = null;
                    while (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        if (xmlParser.getEventType() == XmlPullParser.START_TAG) {
                            if (xmlParser.getName().equalsIgnoreCase("uKey") == true) {
                                tagName = xmlParser.getName();
                            }
                        }
                        else if (xmlParser.getEventType() == XmlPullParser.TEXT) {
                            if(tagName != null && tagName.equalsIgnoreCase("uKey") == true) {
                                uKey = xmlParser.getText();
                                Log.d(TAG, "uKey : " + uKey);
                                break;
                            }
                        }
                        xmlParser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                request.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uKey;
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    private class RegisterDevieTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean registered = registerDeviceToPortal(userInfo.loadUKey());
            if (registered) {
                userInfo.setRegisterState(true);
                moveToNext(false);
            } else {
                showProgress(false);
            }
            return null;
        }
    }

    /**
     * register device to ThingPlug portal
     * @param uKey    UKEY
     * @return result to register device
     */
    private boolean registerDeviceToPortal(String uKey) {
        boolean bRet = false;
        try {
            URL url = new URL(userInfo.loadRegisterURL() + Const.URL_REGISTER_DEVICE);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("POST");
            request.setRequestProperty("Content-Type", "application/xml");
            request.setRequestProperty("uKey", uKey);
            request.setRequestProperty("locale", "ko");
            request.setDoOutput(true);

            OutputStream os = request.getOutputStream();
            String body =
                    "<device>\n" +
                            "    <device_id>" + Utils.getMacAddress(this) + "</device_id>\n" +
                            "    <passcode>" + Build.SERIAL + "</passcode>\n" +
                            "    <device_name>" + Build.MODEL + "</device_name>\n" +
                            "    <category_id>10</category_id>\n" +
                            "    <location_alt>37.566441</location_alt>\n" +
                            "    <location_lon>126.985039</location_lon>\n" +
                            "    <location_addr>서울 중구 을지로 65 SKT타워</location_addr>\n" +
                            "    <location_addr_etc></location_addr_etc>\n" +
                            "    <location_height></location_height>\n" +
                            "    <keyword></keyword>\n" +
                            "    <public_yn>N</public_yn>\n" +
                            "    <discovery_yn>N</discovery_yn>\n" +
                            "    <firm_info></firm_info>\n" +
                            "</device>\n";
            os.write(body.getBytes());

            int responseCode = request.getResponseCode();
            Log.i(TAG, "[" + url.toString() + "]" + "responseCode : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = request.getInputStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                int nLength = 0;
                while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                String response = new String(baos.toByteArray());

                try {
                    XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlParser = xmlFactoryObject.newPullParser();
                    xmlParser.setInput(new StringReader(response));
                    String tagName = null;
                    while (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        if (xmlParser.getEventType() == XmlPullParser.START_TAG) {
                            if (xmlParser.getName().equalsIgnoreCase("result_code") == true) {
                                tagName = xmlParser.getName();
                            }
                        } else if (xmlParser.getEventType() == XmlPullParser.TEXT) {
                            if (tagName != null && tagName.equalsIgnoreCase("result_code") == true) {
                                String result_code = xmlParser.getText();
                                Log.d(TAG, "result_code : " + result_code);
                                if (result_code.compareTo("200") == 0 || result_code.compareTo("600") == 0) {
                                    bRet = true;
                                }
                                break;
                            }
                        }
                        xmlParser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                request.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bRet;
    }

    /**
     * oneM2M worker state listener
     */
    private class OneM2MWorkerListener implements OneM2MWorker.StateListener {

        @Override
        public void onConnected(boolean result, String accountID, String accountPassword, String uKey) {
            if (result) {
                if (userInfo.getRegisterState()) {
                    moveToNext(true);
                } else {
                    userInfo.saveID(accountID);
                    userInfo.savePassword(accountPassword);
                    userInfo.saveUKey(uKey);
                    oneM2MWorker.registerDevice();
                }
            }
            else {
                showProgress(false);
            }
        }

        @Override
        public void onDisconnected(boolean result) {
        }

        @Override
        public void onRegistered(boolean result, String dKey, String nodeLink) {
            if(result == true) {
                userInfo.saveDeviceKey(dKey);
                userInfo.setNodeLink(nodeLink);
                new RegisterDevieTask().execute();
            } else {
                showProgress(false);
            }
        }

        @Override
        public void onUnregistered(boolean result) {
        }

        @Override
        public RESULT onReceiveCommand(execInstanceControl control) {
            return RESULT.SUSPEND;
        }
    }
}
