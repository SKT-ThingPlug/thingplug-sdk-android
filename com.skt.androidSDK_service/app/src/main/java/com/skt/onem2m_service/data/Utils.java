package com.skt.onem2m_service.data;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * utility functions
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class Utils {
    private static final String     TAG = Utils.class.getSimpleName();
    private static final boolean    isLittleEndian = true; // default true for TLV, false for TDV
    /**
     * get MAC address
     */
    public static String getMacAddress(Context context) {
        String macAddress = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    if (!intf.getName().equalsIgnoreCase("wlan0")) {
                        continue;
                    }

                    byte[] mac = intf.getHardwareAddress();
                    if (mac == null) {
                        break;
                    }

                    StringBuilder buf = new StringBuilder();
                    for (int idx = 0; idx < mac.length; idx++) {
                        buf.append(String.format("%02X", mac[idx]));
                    }
                    macAddress = buf.toString();
                    break;
                }
            } catch (Exception ex) {
            }
        }
        else {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            macAddress = info.getMacAddress().replaceAll(":", "").toUpperCase();
        }
//        macAddress = "C81479F6A4EC";
        Log.i(TAG, "MAC Address : " + macAddress);
        return macAddress;
    }

    public static byte swap(byte x) {
        return x;
    }

    public static short swap(short x) {
        if( isLittleEndian == true)
            return (short) ((x << 8) | ((x >> 8) & 0xff));
        else
            return x;
    }

    public static char swap(char x) {
        if( isLittleEndian == true)
            return (char) ((x << 8) | ((x >> 8) & 0xff));
        else
            return x;
    }

    public static int swap(int x) {
        if( isLittleEndian == true)
            return (int) ((swap((short) x) << 16) | (swap((short) (x >> 16)) & 0xffff));
        else
            return x;
    }

    public static long swap(long x) {
        if( isLittleEndian == true)
            return (long) (((long) swap((int) (x)) << 32) | ((long) swap((int) (x >> 32)) & 0xffffffffL));
        else return x;
    }

    public static float swap(float x) {
        if( isLittleEndian == true)
            return Float.intBitsToFloat(swap(Float.floatToRawIntBits(x)));
        else
            return x;
    }

    public static double swap(double x) {
        if( isLittleEndian == true)
            return Double.longBitsToDouble(swap(Double
                .doubleToRawLongBits(x)));
        else
            return x;
    }
}
