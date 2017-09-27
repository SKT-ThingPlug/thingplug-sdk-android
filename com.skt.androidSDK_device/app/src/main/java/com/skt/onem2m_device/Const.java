package com.skt.onem2m_device;

import android.hardware.Sensor;

/**
 * default values for application
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class Const {

    // ThingPlug URLs
    public static final String URL_JOIN_THINGPLUG = "https://thingplug.sktiot.com/";
    public static final String URL_LOGIN_DEFAULT = "https://thingplugtest.sktiot.com:9443";
    public static final String URL_SEARCH_DEFAULT = "https://thingplugtest.sktiot.com:9443";
    public static final String URL_REGISTER_DEFAULT = "https://thingplug.sktiot.com:443";
    public static final String URL_LOGIN = "/ThingPlug?division=user&function=login";
    public static final String URL_SEARCH_DEVICE = "/ThingPlug?division=searchDevice&function=myDevice&startIndex=1&countPerPage=50";
    public static final String URL_REGISTER_DEVICE = "/ThingPlug?division=device&function=regist";

    public static final String URL_SERVER_DEFAULT = "mqtt.sktiot.com:8883";
    public static final String SERVER_APPEUI_DEFAULT = "Android";
    public static final boolean USE_TLS_DEFAULT = true;
    public static final boolean USE_TLV_DEFAULT = true;

    // read time delay (msec)
    public static final int SENSOR_DEFAULT_READ_PERIOD = 1000;
    public static final int SENSOR_DEFAULT_TRANSFER_INTERVAL = 10000;
    public static final int SENSOR_DEFAULT_LIST_UPDATE_INTERVAL = 1000;
    public static final int SENSOR_DEFAULT_GRAPH_UPDATE_INTERVAL = 1000;
    public static final int SENSOR_MIN_READ_PERIOD = 100;
    public static final int SENSOR_MIN_TRANSFER_INTERVAL = 1000;
    public static final int SENSOR_MIN_LIST_UPDATE_INTERVAL = 1000;
    public static final int SENSOR_MIN_GRAPH_UPDATE_INTERVAL = 100;

    // sensor type definition
    public static final int SENSOR_TYPE_BATTERY = Sensor.TYPE_DEVICE_PRIVATE_BASE + 1;
    public static final int SENSOR_TYPE_GPS = Sensor.TYPE_DEVICE_PRIVATE_BASE + 2;
    public static final int SENSOR_TYPE_BUZZER = Sensor.TYPE_DEVICE_PRIVATE_BASE + 3;
    public static final int SENSOR_TYPE_LED = Sensor.TYPE_DEVICE_PRIVATE_BASE + 4;
    public static final int SENSOR_TYPE_CAMERA = Sensor.TYPE_DEVICE_PRIVATE_BASE + 5;
    public static final int SENSOR_TYPE_NOISE = Sensor.TYPE_DEVICE_PRIVATE_BASE + 6;

    // OneM2MWorker
    public static final String ONEM2M_TO = "/v1_0";

    public static final String CONTAINER_LORA_NAME = "LoRa";
    public static final String CONTAINER_PHOTOURL_NAME = "PhotoURL";

    public static final String MGMTCMD_NAME = "Android";

    public static final String EXECRESULT_SUCCESS = "0";
    public static final String EXECRESULT_DENIED = "2";
}
