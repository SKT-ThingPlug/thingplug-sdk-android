package com.skt.onem2m_device.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.skt.onem2m_device.R;
import com.skt.onem2m_device.controller.Buzzer;
import com.skt.onem2m_device.controller.Camera;
import com.skt.onem2m_device.controller.Led;
import com.skt.onem2m_device.data.GoogleDriveHandler;
import com.skt.onem2m_device.data.SensorInfo;
import com.skt.onem2m_device.data.SensorType;
import com.skt.onem2m_device.data.TDVBuilder;
import com.skt.onem2m_device.data.TLVBuilder;
import com.skt.onem2m_device.data.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tp.skt.onem2m.binder.mqtt_v1_1.control.execInstanceControl;

/**
 * activity for sensor list
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class SensorListActivity extends AppCompatActivity {

    private final static String     TAG = SensorListActivity.class.getSimpleName();

    public static final String      EXTRA_SENSOR_TYPE = "sensorType";

    private final int               SETTING_RESULT = 100;
    public static final int         SETTING_RESULT_SAVE = 1;
    public static final int         SETTING_RESULT_LOGOUT = 2;

    private SensorListener          sensorListener;
    private static List<SensorInfo> sensorInfos = new ArrayList<>();

    private UserInfo                userInfo;
    private OneM2MWorker            oneM2MWorker;
    private GoogleDriveHandler      googleDriveHandler;
    private Timer                   timer;
    private boolean                 sendedOffAll;

    private MediaRecorder           mediaRecorder;

    private execInstanceControl     cameraControl;      // oneM2M control data

    private ListItemClickListener       listItemClickListener = new ListItemClickListener();
    private EnableClickListener         enableClickListener = new EnableClickListener();

    private boolean isFront = false;

    /**
     * get sensor info list
     * @return sensor info list
     */
    public static List<SensorInfo> getSensorInfos() {
        return sensorInfos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);

        ActionBar bar = getSupportActionBar();
        bar.setTitle(R.string.actionbar_list);

        sensorListener = new SensorListener(this, sensorInfos);

        userInfo = UserInfo.getInstance(this);

        oneM2MWorker = OneM2MWorker.getInstance();
        oneM2MWorker.setStateListener(new OneM2MWorkerListener());

        googleDriveHandler = new GoogleDriveHandler(this, new GoogleDriveCommandListener());
        googleDriveHandler.connect();

        // create sensor list
        createSensorList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensor_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, SETTING_RESULT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (googleDriveHandler.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        if (requestCode == SETTING_RESULT) {
            switch (resultCode) {
                case SETTING_RESULT_SAVE:
                    clearSensorList();
                    createSensorList();
                    break;
                case SETTING_RESULT_LOGOUT:
                    clearSensorList();
                    oneM2MWorker.unregisterDevice();
                    userInfo.clear(false);

                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        oneM2MWorker.disconnect();
        googleDriveHandler.disconnect();

        // clear sensor list
        clearSensorList();

        super.onDestroy();
    }

    /**
     * create sensor list
     */
    private void createSensorList() {
        // add sensor item
        for (SensorType sensorType : SensorType.values()) {
            if (sensorType != SensorType.NONE && setSensorListener(sensorType, true)) {
                SensorInfo sensorInfo = new SensorInfo(sensorType);
                if (!userInfo.loadSensorStatus(sensorType)) {
                    sensorInfo.setEnable(false);
                    setSensorListener(sensorType, false);
                }
                sensorInfos.add(sensorInfo);
            }
        }

        // add to listview
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setSmoothScrollbarEnabled(true);
        listView.setAdapter(new SensorListAdapter(this, sensorInfos));

        timer = new Timer();
        sendedOffAll = false;

        // create timer for view
        final Handler handler = new Handler();
        TimerTask viewTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ListView listView = (ListView) findViewById(R.id.listView);
                        listView.invalidateViews();
                    }
                });
            }
        };
        timer.schedule(viewTask, 0, userInfo.loadListInterval());

        // create timer for report
        if(userInfo.getRegisterState() == true) {
            TimerTask reportTask = new TimerTask() {
                public void run() {
                    reportSensorInfo();
                }
            };
            timer.schedule(reportTask, 0, userInfo.loadTransferInterval());
        }
    }

    /**
     * clear sensor list
     */
    private void clearSensorList() {
        timer.cancel();

        // unregist listener
        for(SensorInfo info : sensorInfos) {
            if (info.isActivated()) {
                setSensorListener(info.getType(), false);
            }
        }

        // clear view
        sensorInfos.clear();
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.deferNotifyDataSetChanged();
    }

    /**
     * set sensor listener
     * @param type           sensor type
     * @param isActivated    state of activation
     * @return setting result
     */
    private boolean setSensorListener(SensorType type, boolean isActivated) {
        boolean isSetted = false;
        switch(type.getCategory()) {
            case SENSOR_MANAGER:
                SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
                for (Sensor sensor : sensorList) {
                    SensorType sensorType = SensorType.getType(SensorType.Category.SENSOR_MANAGER, sensor.getType());
                    if (sensorType == type) {
                        if (isActivated) {
                            // regist sensor listener
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_UI, userInfo.loadReadInterval() * 1000);
                            } else {
                                sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_UI);
                            }
                        }
                        else {
                            // unregist sensor listener
                            sensorManager.unregisterListener(sensorListener, sensor);
                        }
                        isSetted = true;
                        break;
                    }
                }
                break;
            case BROADCAST:
                if (isActivated) {
                    // regist BroadcastReceiver for battery
                    registerReceiver(sensorListener, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                }
                else {
                    // unregist BroadcastReceiver for battery
                    unregisterReceiver(sensorListener);
                }
                isSetted = true;
                break;
            case LOCATION_MANAGER:
                if (userInfo.loadAgreeTerms()) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean supportGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean supportNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if (supportGPS || supportNetwork) {
                        if (ActivityCompat.checkSelfPermission(SensorListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(SensorListActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return isSetted;
                        }

                        if (isActivated) {
                            // regist LocationManager listener
                            if (supportGPS) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, userInfo.loadReadInterval(), 0, sensorListener);
                            }
                            if (supportNetwork) {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, userInfo.loadReadInterval(), 0, sensorListener);
                            }
                        } else {
                            // unregist LocationManager listener
                            locationManager.removeUpdates(sensorListener);
                        }
                    }
                    isSetted = true;
                }
                break;
            case MEDIA_RECORDER:
                if (isActivated) {
                    if (mediaRecorder == null) {
                        try {
                            mediaRecorder = new MediaRecorder();
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            mediaRecorder.setOutputFile("/dev/null");

                            mediaRecorder.prepare();
                            mediaRecorder.start();

                            sensorListener.setMediaRecorder(mediaRecorder);
                        } catch (IOException e) {
                            e.printStackTrace();
                            mediaRecorder = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            mediaRecorder = null;
                        }
                    }
                }
                else {
                    if (mediaRecorder != null) {
                        sensorListener.setMediaRecorder(null);

                        try {
                            mediaRecorder.stop();
                            mediaRecorder.release();
                            mediaRecorder = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            mediaRecorder = null;
                        }
                    }
                }
                isSetted = true;
                break;
            case ACTUATOR:
                isSetted = true;
                break;
        }
        return isSetted;
    }

    /**
     * report sensor information to ThingPlug
     */
    private void reportSensorInfo() {
        boolean isAllSensorOff = true;
        String reportContent = "";
        if (userInfo.loadUseTLV()) {
            final TLVBuilder builder = new TLVBuilder();
            for (SensorInfo sensorInfo : sensorInfos) {
                if (sensorInfo.isActivated()) {
                    builder.addSensorData(sensorInfo);
                    isAllSensorOff = false;
                }
            }
            if (isAllSensorOff) {
                builder.add(0x00, 0, 0);
            }
            reportContent = builder.build();
        }
        else {
            final TDVBuilder builder = new TDVBuilder();
            for (SensorInfo sensorInfo : sensorInfos) {
                if (sensorInfo.isActivated()) {
                    builder.addSensorData(sensorInfo);
                    isAllSensorOff = false;
                }
            }
            if (isAllSensorOff) {
                builder.add(0x00, TDVBuilder.ValueType.NONE, 0);
            }
            reportContent = builder.build();
        }

        if (!isAllSensorOff || !sendedOffAll) {
            oneM2MWorker.report(reportContent);
            sendedOffAll = isAllSensorOff;
        }
    }

    /**
     * list adapter
     */
    private class SensorListAdapter extends ArrayAdapter<SensorInfo> {
        private final Context context;
        private final List<SensorInfo> sensorInfos;

        /**
         * constructor
         * @param context        context
         * @param sensorInfos    sensor info list
         */
        public SensorListAdapter(Context context, List<SensorInfo> sensorInfos) {
            super(context, R.layout.sensor_list_item, sensorInfos);

            this.context = context;
            this.sensorInfos = sensorInfos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.sensor_list_item, parent, false);
            LinearLayout itemLayout = (LinearLayout) rowView.findViewById(R.id.item_layout);
            ImageView itemImageView = (ImageView) rowView.findViewById(R.id.item_image);
            TextView itemNameView = (TextView) rowView.findViewById(R.id.item_name);
            TextView itemStatus = (TextView) rowView.findViewById(R.id.item_status);
            ToggleButton itemEnable = (ToggleButton) rowView.findViewById(R.id.item_enable);
            ToggleButton itemActivate = (ToggleButton) rowView.findViewById(R.id.item_activate);
            Button itemActuatorRun = (Button) rowView.findViewById(R.id.item_actuator_run);
            itemActuatorRun.setVisibility(View.GONE);

            final SensorInfo sensorInfo = sensorInfos.get(position);
            itemImageView.setImageResource(sensorInfo.getType().getImage());
            itemNameView.setText(sensorInfo.getType().getNickname());
            itemEnable.setChecked(sensorInfo.isEnable());
            if (sensorInfo.getType().getCategory() == SensorType.Category.ACTUATOR) {
                itemStatus.setVisibility(View.GONE);
                itemActivate.setVisibility(View.GONE);
            }
            else {
                itemActivate.setEnabled(false);
                itemStatus.setText(sensorInfo.toString());
                itemActivate.setChecked(sensorInfo.isActivated());
            }

            // sensor item click
            itemLayout.setTag(sensorInfo);
            itemLayout.setOnClickListener(listItemClickListener);

            // sensor enable button click
            itemEnable.setTag(sensorInfo);
            itemEnable.setOnClickListener(enableClickListener);

            return rowView;
        }
    }

    /**
     * list item click listener
     */
    private class ListItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            final SensorInfo sensorInfo = (SensorInfo) view.getTag();

            if (sensorInfo.getType().getCategory() == SensorType.Category.ACTUATOR) {
            }
            else if (sensorInfo.isActivated()) {
                Intent intent = new Intent(SensorListActivity.this, SensorDetailActivity.class);
                intent.putExtra(EXTRA_SENSOR_TYPE, sensorInfo.getType());
                startActivity(intent);
            }
        }
    }

    /**
     * sensor enable button click listener
     */
    private class EnableClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            SensorInfo sensorInfo = (SensorInfo) view.getTag();
            ToggleButton button = (ToggleButton) view;

            sensorInfo.setEnable(button.isChecked());
            userInfo.saveSensorStatus(sensorInfo.getType(), sensorInfo.isEnable());

            setSensorListener(sensorInfo.getType(), sensorInfo.isActivated());
        }
    }

    /**
     * oneM2M worker state listener
     */
    private class OneM2MWorkerListener implements OneM2MWorker.StateListener {
        @Override
        public void onConnected(boolean result, String accountID, String accountPassword, String uKey) {
        }

        @Override
        public void onDisconnected(boolean result) {
        }

        @Override
        public void onRegistered(boolean result, String dKey, String nodeLink) {
        }

        @Override
        public void onUnregistered(boolean result) {
        }

        @Override
        public RESULT onReceiveCommand(final execInstanceControl control) {
            Log.i(TAG, "onReceiveCommand");

            SensorInfo targetSensor = null;
            if (userInfo.loadUseTLV()) {
                targetSensor = TLVBuilder.parseSensorCommand(control.getExra());
            }
            else {
                targetSensor = TDVBuilder.parseSensorCommand(control.getExra());
            }
            if (targetSensor == null) {
                return RESULT.FAIL;
            }
            Log.i(TAG, "targetSensor.getType():" + targetSensor.getType().getNickname());

            SensorInfo localSensor = null;
            for (SensorInfo sensorInfo : sensorInfos) {
                if (sensorInfo.getType() == targetSensor.getType()) {
                    localSensor = sensorInfo;
                    break;
                }
            }
            if (localSensor == null || !localSensor.isEnable()) {
                return RESULT.FAIL;
            }

            RESULT controlResult = RESULT.SUSPEND;
            switch (localSensor.getType()) {
                case BUZZER:
                    Buzzer.TYPE type = Buzzer.TYPE.NONE;
                    switch ((int) targetSensor.getValues()[0]) {
                        case 0:     type = Buzzer.TYPE.NONE;            break;
                        case 1:     type = Buzzer.TYPE.RINGTONE;        break;
                        case 2:     type = Buzzer.TYPE.NOTIFICATION;    break;
                        case 4:     type = Buzzer.TYPE.ALARM;           break;
                    }
                    Buzzer buzzer = new Buzzer(SensorListActivity.this);
                    if (buzzer.notifyCommand(type)) { controlResult = RESULT.SUCCESS; }
                    else                            { controlResult = RESULT.FAIL; }
                    break;
                case LED:
                    Led.COLOR color = Led.COLOR.NONE;
                    switch ((int) targetSensor.getValues()[0]) {
                        case 0:     color = Led.COLOR.NONE;     break;
                        case 1:     color = Led.COLOR.RED;      break;
                        case 2:     color = Led.COLOR.GREEN;    break;
                        case 3:     color = Led.COLOR.BLUE;     break;
                        case 4:     color = Led.COLOR.MAGENTA;  break;
                        case 5:     color = Led.COLOR.CYAN;     break;
                        case 6:     color = Led.COLOR.YELLOW;   break;
                        case 7:     color = Led.COLOR.WHITE;    break;
                    }
                    Led led = new Led(SensorListActivity.this);
                    if (led.notifyCommand(color))   { controlResult = RESULT.SUCCESS; }
                    else                            { controlResult = RESULT.FAIL; }
                    break;
                case CAMERA:
                    // under LOLLIPOP exception handling
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && isFront == false) {
                        controlResult = RESULT.FAIL;
                        break;
                    }

                    Camera.TYPE cameraType = Camera.TYPE.NONE;
                    switch ((int) targetSensor.getValues()[0]) {
                        case 0:     cameraType = Camera.TYPE.BACK;      break;
                        case 1:     cameraType = Camera.TYPE.FRONT;     break;
                    }

                    Camera camera = new Camera(SensorListActivity.this, new Camera.CapturedListener() {
                        @Override
                        public void onCaptured(byte[] image) {
                            Log.i(TAG, "onCaptured");
                            if (googleDriveHandler.savePicture(image) == false) {
                                oneM2MWorker.controlResult(control.getNm(), control.getRi(), false);
                            }
                            else {
                                cameraControl = control;
                            }
                        }

                        @Override
                        public void onCaptureFailed() {
                            Log.i(TAG, "onCaptureFailed");
                            oneM2MWorker.controlResult(control.getNm(), control.getRi(), false);
                        }
                    });
                    camera.notifyCommand(cameraType, (FrameLayout) findViewById(R.id.camera_preview));
                    break;
                default:
                    // control
                    localSensor.setActivated(targetSensor.isActivated());

                    setSensorListener(localSensor.getType(), localSensor.isActivated());

                    // report
                    reportSensorInfo();

                    controlResult = RESULT.SUCCESS;
                    break;
            }
            return controlResult;
        }
    }

    /**
     * Google drive command listener
     */
    private class GoogleDriveCommandListener implements GoogleDriveHandler.CommandListener {

        @Override
        public void onSavedPicture(boolean result, String resourceId) {
            Log.i(TAG, "onSavedPicture : " + result + ", resourceId:" + resourceId);
            if(result == true && TextUtils.isEmpty(resourceId) == false) {
                oneM2MWorker.sendPhotoURL(resourceId);
            } else {
                result = false;
            }

            if(cameraControl != null) {
                oneM2MWorker.controlResult(cameraControl.getNm(), cameraControl.getRi(), result);
                cameraControl = null;
            }
        }

        @Override
        public void onLoadedPicture(boolean result, byte[] imageData) {
            Log.i(TAG, "onLoadedPicture : " + result);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFront = false;
    }
}
