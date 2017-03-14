package com.skt.onem2m_device.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaRecorder;
import android.os.BatteryManager;
import android.os.Bundle;

import com.skt.onem2m_device.data.SensorInfo;
import com.skt.onem2m_device.data.SensorType;
import com.skt.onem2m_device.data.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * sensor listener
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class SensorListener extends BroadcastReceiver implements SensorEventListener, LocationListener {

    private final static String     TAG = SensorListener.class.getSimpleName();

    private Context             context;
    private List<SensorInfo>    sensorInfos = new ArrayList<>();
    private Timer               timer;

    /**
     * constructor
     * @param context        context
     * @param sensorInfos    sensor info list
     */
    public SensorListener(Context context, List<SensorInfo> sensorInfos) {
        this.context = context;
        this.sensorInfos = sensorInfos;
    }

    // for SensorEventListener
    @Override
    public void onSensorChanged(android.hardware.SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float[] sensorValues = sensorEvent.values;

        SensorType type = SensorType.getType(SensorType.Category.SENSOR_MANAGER, sensorType);
        for(SensorInfo sensorInfo : sensorInfos) {
            if(sensorInfo.getType() == type) {
                sensorInfo.setValues(sensorValues);
                break;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    // for BroadcastReceiver
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == Intent.ACTION_BATTERY_CHANGED) {
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            float temperature = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
            float chargeLevel = ((float) (level * 100)) / scale;

            for (SensorInfo sensorInfo : sensorInfos) {
                if (sensorInfo.getType() == SensorType.BATTERY) {
                    sensorInfo.setValues(new float[]{temperature, chargeLevel});
                    break;
                }
            }
        }
    }

    // for LocationListener
    @Override
    public void onLocationChanged(Location location) {
        float latitude = (float) location.getLatitude();
        float longitude = (float) location.getLongitude();
        float altitude = (float) location.getAltitude();

        for (SensorInfo sensorInfo : sensorInfos) {
            if (sensorInfo.getType() == SensorType.GPS) {
                sensorInfo.setValues(new float[]{latitude, longitude, altitude});
                break;
            }
        }
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }

    // for noise sensor
    public void setMediaRecorder(final MediaRecorder mediaRecorder) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (mediaRecorder != null) {
            timer = new Timer();
            TimerTask reportTask = new TimerTask() {
                public void run() {
                    int amplitude = mediaRecorder.getMaxAmplitude();
                    if (amplitude == 0) {
                        return;
                    }

                    double amplitudeDb = 20 * Math.log10((double)Math.abs(amplitude));

                    for (SensorInfo sensorInfo : sensorInfos) {
                        if (sensorInfo.getType() == SensorType.NOISE) {
                            sensorInfo.setValue(0, (float) amplitudeDb);
                            break;
                        }
                    }
                }
            };
            timer.schedule(reportTask, 0, UserInfo.getInstance(context).loadReadInterval());
        }
    }
}
