package com.skt.onem2m_service.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.skt.onem2m_service.Const;

/**
 * user information
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class UserInfo {
    private static final String SHARED_PREFERENCE_NAME  = "oneM2M";

    private static final String KEY_ID                  = "id";
    private static final String KEY_PASSWORD            = "password";
    private static final String KEY_UKEY                = "uKey";
    private static final String KEY_DKEY                = "dKey";
    private static final String KEY_DEVICE_ID           = "deviceID";
    private static final String KEY_REGISTER_STATE      = "register";
    private static final String KEY_NODE_LINK           = "nodeLink";

    private static final String KEY_SETTING_READ        = "readInterval";
    private static final String KEY_SETTING_REPORT      = "reportInterval";
    private static final String KEY_SETTING_LIST        = "listInterval";
    private static final String KEY_SETTING_GRAPH       = "graphInterval";

    private static final String KEY_SETTING_LOGIN_URL   = "loginURL";
    private static final String KEY_SETTING_SEARCH_URL  = "searchURL";
    private static final String KEY_SETTING_REGISTER_URL= "registerURL";
    private static final String KEY_SETTING_SERVER_URL  = "serverURL";
    private static final String KEY_SETTING_APP_EUI     = "appEUI";
    private static final String KEY_SETTING_USE_TLS     = "useTLS";
    private static final String KEY_SETTING_USE_TLV     = "useTLV";

    private static final String KEY_AGREE_TERMS         = "agreeTerms";

    private SharedPreferences           sharedPreference;
    private SharedPreferences.Editor    editor;

    private static UserInfo userInfo;

    /**
     * get UserInfo
     *
     * @param context
     * @return
     */
    public static UserInfo getInstance(Context context) {
        if (userInfo == null) {
            userInfo = new UserInfo(context);
        }
        return userInfo;
    }

    /**
     * constructor
     * @param context    context
     */
    public UserInfo(Context context) {
        sharedPreference = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }

    /**
     * save ID
     * @param id    ID
     */
    public void saveID(String id) {
        editor.putString(KEY_ID, id);
        editor.commit();
    }

    /**
     * load ID
     * @return ID
     */
    public String loadID() {
        return sharedPreference.getString(KEY_ID, "");
    }

    /**
     * save password
     * @param password    password
     */
    public void savePassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    /**
     * load password
     * @return password
     */
    public String loadPassword() {
        return sharedPreference.getString(KEY_PASSWORD, "");
    }

    /**
     * save UKey
     * @param uKey    UKey
     */
    public void saveUKey(String uKey) {
        editor.putString(KEY_UKEY, uKey);
        editor.commit();
    }

    /**
     * load UKey
     * @return UKey
     */
    public String loadUKey() {
        return sharedPreference.getString(KEY_UKEY, "");
    }

    /**
     * save device key
     *
     * @param dKey  dKey
     */
    public void saveDeviceKey(String dKey) {
        editor.putString(KEY_DKEY, dKey);
        editor.commit();
    }

    /**
     * load device key
     *
     * @return dKey
     */
    public String loadDeviceKey() {
        return sharedPreference.getString(KEY_DKEY, "");
    }

    /**
     * save device ID
     * @param diviceID    device ID
     */
    public void saveDeviceID(String diviceID) {
        editor.putString(KEY_DEVICE_ID, diviceID);
        editor.commit();
    }

    /**
     * load device ID
     * @return device ID
     */
    public String loadDeviceID() {
        return sharedPreference.getString(KEY_DEVICE_ID, "");
    }

    /**
     * set register state
     *
     * @param isRegistered is registered ?
     */
    public void setRegisterState(boolean isRegistered) {
        editor.putBoolean(KEY_REGISTER_STATE, isRegistered);
        editor.commit();
    }

    /**
     * get register state
     *
     * @return true : registered <-> false
     */
    public boolean getRegisterState() {
        return sharedPreference.getBoolean(KEY_REGISTER_STATE, false);
    }

    /**
     * set node link
     *
     * @param nodeLink node link
     */
    public void setNodeLink(String nodeLink) {
        editor.putString(KEY_NODE_LINK, nodeLink);
        editor.commit();
    }

    /**
     * get node link
     *
     * @return node link
     */
    public String getNodeLink() {
        return sharedPreference.getString(KEY_NODE_LINK, "");
    }

    /**
     * save read interval
     * @param interval    read interval
     */
    public void saveReadInterval(int interval) {
        editor.putInt(KEY_SETTING_READ, interval);
        editor.commit();
    }

    /**
     * load read interval
     * @return read interval
     */
    public int loadReadInterval() {
        return sharedPreference.getInt(KEY_SETTING_READ, Const.SENSOR_DEFAULT_READ_PERIOD);
    }

    /**
     * save transfer interval
     * @param interval    transfer interval
     */
    public void saveTransferInterval(int interval) {
        editor.putInt(KEY_SETTING_REPORT, interval);
        editor.commit();
    }

    /**
     * load transfer interval
     * @return transfer interval
     */
    public int loadTransferInterval() {
        return sharedPreference.getInt(KEY_SETTING_REPORT, Const.SENSOR_DEFAULT_TRANSFER_INTERVAL);
    }

    /**
     * save list update interval
     * @param interval    list update interval
     */
    public void saveListInterval(int interval) {
        editor.putInt(KEY_SETTING_LIST, interval);
        editor.commit();
    }

    /**
     * load list update interval
     * @return list update interval
     */
    public int loadListInterval() {
        return sharedPreference.getInt(KEY_SETTING_LIST, Const.SENSOR_DEFAULT_LIST_UPDATE_INTERVAL);
    }

    /**
     * save graph update interval
     * @param interval    graph update interval
     */
    public void saveGraphInterval(int interval) {
        editor.putInt(KEY_SETTING_GRAPH, interval);
        editor.commit();
    }

    /**
     * load graph update interval
     * @return graph update interval
     */
    public int loadGraphInterval() {
        return sharedPreference.getInt(KEY_SETTING_GRAPH, Const.SENSOR_DEFAULT_GRAPH_UPDATE_INTERVAL);
    }

    /**
     * save login URL
     * @param url    login URL
     */
    public void saveLoginURL(String url) {
        editor.putString(KEY_SETTING_LOGIN_URL, url);
        editor.commit();
    }

    /**
     * load login URL
     * @return login URL
     */
    public String loadLoginURL() {
        return sharedPreference.getString(KEY_SETTING_LOGIN_URL, Const.URL_LOGIN_DEFAULT);
    }

    /**
     * save search URL
     * @param url    search URL
     */
    public void saveSearchURL(String url) {
        editor.putString(KEY_SETTING_SEARCH_URL, url);
        editor.commit();
    }

    /**
     * load search URL
     * @return search URL
     */
    public String loadSearchURL() {
        return sharedPreference.getString(KEY_SETTING_SEARCH_URL, Const.URL_SEARCH_DEFAULT);
    }

    /**
     * save register URL
     * @param url    register URL
     */
    public void saveRegisterURL(String url) {
        editor.putString(KEY_SETTING_REGISTER_URL, url);
        editor.commit();
    }

    /**
     * load register URL
     * @return register URL
     */
    public String loadRegisterURL() {
        return sharedPreference.getString(KEY_SETTING_REGISTER_URL, Const.URL_REGISTER_DEFAULT);
    }

    /**
     * save server URL
     * @param url    server URL
     */
    public void saveServerURL(String url) {
        editor.putString(KEY_SETTING_SERVER_URL, url);
        editor.commit();
    }

    /**
     * load server URL
     * @return server URL
     */
    public String loadServerURL() {
        return sharedPreference.getString(KEY_SETTING_SERVER_URL, Const.URL_SERVER_DEFAULT);
    }

    /**
     * save AppEUI
     * @param appEUI    AppEUI
     */
    public void saveAppEUI(String appEUI) {
        editor.putString(KEY_SETTING_APP_EUI, appEUI);
        editor.commit();
    }

    /**
     * load AppEUI
     * @return AppEUI
     */
    public String loadAppEUI() {
        return sharedPreference.getString(KEY_SETTING_APP_EUI, Const.SERVER_APPEUI_DEFAULT);
    }

    /**
     * save useTLS
     * @param useTLS    useTLS
     */
    public void saveUseTLS(boolean useTLS) {
        editor.putBoolean(KEY_SETTING_USE_TLS, useTLS);
        editor.commit();
    }

    /**
     * load useTLS
     * @return useTLS
     */
    public boolean loadUseTLS() {
        return sharedPreference.getBoolean(KEY_SETTING_USE_TLS, Const.USE_TLS_DEFAULT);
    }

    /**
     * save useTLV
     * @param useTLV    useTLV
     */
    public void saveUseTLV(boolean useTLV) {
        editor.putBoolean(KEY_SETTING_USE_TLV, useTLV);
        editor.commit();
    }

    /**
     * load useTLV
     * @return useTLV
     */
    public boolean loadUseTLV() {
        return sharedPreference.getBoolean(KEY_SETTING_USE_TLV, Const.USE_TLV_DEFAULT);
    }

    /**
     * save agreeTerms
     * @param agreeTerms    agreeTerms
     */
    public void saveAgreeTerms(boolean agreeTerms) {
        editor.putBoolean(KEY_AGREE_TERMS, agreeTerms);
        editor.commit();
    }

    /**
     * load agreeTerms
     * @return agreeTerms
     */
    public boolean loadAgreeTerms() {
        return sharedPreference.getBoolean(KEY_AGREE_TERMS, false);
    }

    /**
     * save sensor status
     * @param type      sensor type
     * @param enable    status
     */
    public void saveSensorStatus(SensorType type, boolean enable) {
        editor.putBoolean(type.name(), enable);
        editor.commit();
    }

    /**
     * load sensor status
     * @param type    sensor type
     * @return sensor status
     */
    public boolean loadSensorStatus(SensorType type) {
        return sharedPreference.getBoolean(type.name(), true);
    }

    /**
     * clear
     *
     * @param withServerInfo
     */
    public void clear(boolean withServerInfo) {
        editor.remove(KEY_ID);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_UKEY);
        editor.remove(KEY_DKEY);
        editor.remove(KEY_DEVICE_ID);
        editor.remove(KEY_REGISTER_STATE);
        editor.remove(KEY_NODE_LINK);

        editor.remove(KEY_SETTING_READ);
        editor.remove(KEY_SETTING_REPORT);
        editor.remove(KEY_SETTING_LIST);
        editor.remove(KEY_SETTING_GRAPH);

        editor.remove(KEY_AGREE_TERMS);

        if(withServerInfo == true) {
            editor.remove(KEY_SETTING_LOGIN_URL);
            editor.remove(KEY_SETTING_SEARCH_URL);
            editor.remove(KEY_SETTING_REGISTER_URL);
            editor.remove(KEY_SETTING_SERVER_URL);
            editor.remove(KEY_SETTING_APP_EUI);
            editor.remove(KEY_SETTING_USE_TLS);
            editor.remove(KEY_SETTING_USE_TLV);
        }


        for (SensorType sensorType : SensorType.values()) {
            if (sensorType != SensorType.NONE) {
                editor.remove(sensorType.name());
            }
        }

//        editor.clear();
        editor.commit();
    }
}
