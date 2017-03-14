package tp.skt.example;

/**
 * Configuration
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class Configuration {
    public static final String MQTT_HOST = "tcp://thingplugtest.sktiot.com";
    public static final String MQTT_SECURE_HOST = "ssl://thingplugtest.sktiot.com";

    public static final String CSEBASE = "ThingPlug";
    public static final String ONEM2M_TO = "/ThingPlug/v1_0";
    public static final String ONEM2M_NODEID = "<<PUT YOUR DEVICE ID HERE>>";
    public static final String ONEM2M_PASSCODE = "<<PUT YOUR PASSCODE HERE>>";

    public static final String CONTAINER_NAME = "Android";
    public static final String MGMTCMD_NAME = ONEM2M_NODEID + "_DevReset";
    public static final String AREANWKINFO_NAME = ONEM2M_NODEID + "_areaNwkInfo_01";
    public static final String LOCATIONPOLICY_NAME = ONEM2M_NODEID + "_locationPolicy_01";
    public static final String AE_NAME = ONEM2M_NODEID + "_AE_01";

    public static final String CMT_DEVRESET = "DevReset";
    public static final String CMT_REPPERCHANGE = "RepPerChange";
    public static final String CMT_REPIMMEDIATE = "RepImmediate";
    public static final String CMT_TAKEPHOTO = "TakePhoto";
    public static final String CMT_LEDCONTROL = "LEDControl";

    public static final String ACCOUNT_ID = "<<PUT YOUR THINGPLUG ID HERE>>";
    public static final String UKEY = "<<PUT YOUR THINGPLUG UKEY HERE>>";
}
