package com.skt.onem2m_device.ui;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.skt.onem2m_device.Const;
import com.skt.onem2m_device.data.UserInfo;
import com.skt.onem2m_device.data.Utils;

import org.eclipse.paho.client.mqttv3.MqttException;

import tp.skt.onem2m.api.IMQTT;
import tp.skt.onem2m.api.MQTTProcessor;
import tp.skt.onem2m.api.oneM2MAPI;
import tp.skt.onem2m.binder.mqtt_v1_1.Binder;
import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.binder.mqtt_v1_1.control.execInstanceControl;
import tp.skt.onem2m.binder.mqtt_v1_1.request.mgmtCmd;
import tp.skt.onem2m.binder.mqtt_v1_1.request.node;
import tp.skt.onem2m.binder.mqtt_v1_1.request.remoteCSE;
import tp.skt.onem2m.binder.mqtt_v1_1.response.containerResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.contentInstanceResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.execInstanceResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.mgmtCmdResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.nodeResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.remoteCSEResponse;
import tp.skt.onem2m.net.mqtt.MQTTCallback;
import tp.skt.onem2m.net.mqtt.MQTTClient;
import tp.skt.onem2m.net.mqtt.MQTTConfiguration;

/**
 * oneM2M SDK handler
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class OneM2MWorker {

    private MQTTClient MQTTClient;
    private IMQTT mqttService;
    private MQTTConfiguration config;

    // client id
    private String mClientID = "";
    // binder
    private Binder mBinder;

    private String accountID;
    private String accountPassword;
    private String uKey;

    private final int MAX_CLIENT_ID_LENGTH = 23;

    //
    private static OneM2MWorker oneM2MWorker;

    private static final String TAG = "TP_ONEM2M_WORKER";

    private Context context;
    private String macAddress;

    private remoteCSEResponse device;

    private StateListener stateListener;

    private final boolean USE_REPORT_DELIVERED_CHECK = false;
    private boolean reportMessageDelivered = true;

    /**
     * working state listener
     */
    public interface StateListener {
        enum RESULT {
            FAIL,
            SUCCESS,
            SUSPEND,
        }

        void onConnected(boolean result, String accountID, String accountPassword, String uKey);
        void onDisconnected(boolean result);
        void onRegistered(boolean result, String dKey, String nodeLink);
        void onUnregistered(boolean result);
        RESULT onReceiveCommand(execInstanceControl control);
    }

    /**
     * get OneM2MWorker
     * @return OneM2MWorker object
     */
    public static OneM2MWorker getInstance() {
        if (oneM2MWorker == null) {
            oneM2MWorker = new OneM2MWorker();
        }
        return oneM2MWorker;
    }

    public void setStateListener(StateListener stateListener) {
        this.stateListener = stateListener;
    }

    /**
     * set datas
     * @param accountID
     * @param accountPassword
     * @param uKey
     */
    public void setDatas(String accountID, String accountPassword, String uKey) {
        this.accountID = accountID;
        this.accountPassword = accountPassword;
        this.uKey = uKey;
    }

    /**
     * set client ID
     */
    private void setClientID() {
        macAddress = Utils.getMacAddress(context);
        if (TextUtils.isEmpty(accountID) == true || TextUtils.isEmpty(macAddress) == true) {
            return;
        }
        mClientID = accountID + "_" + macAddress.substring(macAddress.length() - 4);
        int clientIDLength = mClientID.length();
        if (clientIDLength > MAX_CLIENT_ID_LENGTH) {
            mClientID = mClientID.substring(0, clientIDLength - (clientIDLength - MAX_CLIENT_ID_LENGTH));
        }
    }

    /**
     * connect
     * @param context
     * @param registerDevice true : register device <-> false
     */
    public void connect(Context context, final boolean registerDevice) {
        this.context = context.getApplicationContext();
        setClientID();
        String appEUI = UserInfo.getInstance(context).loadAppEUI();
        String host = UserInfo.getInstance(context).loadServerURL();
        if (TextUtils.isEmpty(mClientID) || TextUtils.isEmpty(appEUI) || TextUtils.isEmpty(host)) {
            Log.e(TAG, "Invalid info!");
            return;
        }
        boolean useTLS = UserInfo.getInstance(context).loadUseTLS();
        if(useTLS == true) {
            host = "ssl://" + host;
        } else {
            host = "tcp://" + host;
        }
        Log.e(TAG, "host : " + host + ", AppEUI : " + appEUI);

        MQTTClient.Builder builder = new MQTTClient.Builder(context)
                .baseUrl(host)
                .clientId(mClientID)
                .userName(accountID)
                .password(uKey)
                .setLog(true);

        MQTTClient = builder.build();

        String to = "/" + appEUI + Const.ONEM2M_TO;

        config = new MQTTConfiguration(appEUI,
                to, //Const.ONEM2M_TO,
                macAddress,
                mClientID);

        mBinder = new Binder();
        mqttService = MQTTClient.connect(IMQTT.class, config, mBinder, new MQTTProcessor.MQTTListener() {
                    @Override
                    public void onPush(execInstanceControl control) {
                        Log.e(TAG, "push!");
                        StateListener.RESULT controlResult = stateListener.onReceiveCommand(control);
                        if (controlResult != StateListener.RESULT.SUSPEND) {
                            oneM2MWorker.controlResult(control.getNm(), control.getRi(), controlResult == StateListener.RESULT.SUCCESS);
                        }
                    }

                    @Override
                    public void onDisconnected() {
                        Log.e(TAG, "disconnect success!");
                    }

                    @Override
                    public void onDisconnectFailure() {
                        Log.e(TAG, "disconnect failure!");
                    }

                    @Override
                    public void onSubscribed() {
                        Log.e(TAG, "subscribe success!");
                        if (stateListener != null) {
                            stateListener.onConnected(true, accountID, accountPassword, uKey);
                        }
                        reportMessageDelivered = true;
                    }

                    @Override
                    public void onSubscribeFailure() {
                        Log.e(TAG, "subscribe failure!");
                        MQTTClient.disconnect();
                        if (stateListener != null) {
                            stateListener.onConnected(false, null, null, null);
                        }
                    }

                    @Override
                    public void onConnected() {
                        Log.e(TAG, "connect success!");
                    }

                    @Override
                    public void onConnectFailure() {
                        Log.e(TAG, "connect fail!");
                        if (stateListener != null) {
                            stateListener.onConnected(false, null, null, null);
                        }
                    }

                    @Override
                    public void onConnectionLost() {
                        Log.e(TAG, "connection lost!");
                    }

                    @Override
                    public void onDelivered() {
                        reportMessageDelivered = true;
                    }
                }
        );
    }

    /**
     * disconnect
     */
    public void disconnect() {
        if (MQTTClient != null) {
            MQTTClient.disconnect();
        }
    }

    /**
     * register device
     */
    public void registerDevice() {
        if (mqttService == null) return;

        String passcode = Build.SERIAL;
        if(Build.SERIAL == null) {
            Log.e(TAG, "Serial is NULL!");
            passcode = "12345"; //default
        }
        oneM2MAPI.getInstance().tpRegisterDevice(mqttService, passcode,
                "3", "true", new MQTTCallback<remoteCSEResponse>() {
                    @Override
                    public void onResponse(remoteCSEResponse response) {
                        device = response;
                        registerSensor();
                        registerControl();

                        if (stateListener != null) {
                            stateListener.onRegistered(true, device.dKey, device.getNl());
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        stateListener.onRegistered(false, null, null);
                    }
                });
    }

    /**
     * register Sensor
     */
    private void registerSensor() {
        if (mqttService == null || device == null) {
            return;
        }

        // LoRa
        oneM2MAPI.getInstance().tpRegisterContainer(mqttService, Const.CONTAINER_LORA_NAME,
                device.dKey, new MQTTCallback<containerResponse>() {
                    @Override
                    public void onResponse(containerResponse response) {
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                    }
                });
        // PhotoURL
        oneM2MAPI.getInstance().tpRegisterContainer(mqttService, Const.CONTAINER_PHOTOURL_NAME,
                device.dKey, new MQTTCallback<containerResponse>() {
                    @Override
                    public void onResponse(containerResponse response) {
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                    }
                });
    }

    /**
     * register control
     */
    private void registerControl() {
        if (mqttService == null || device == null) {
            return;
        }
        String mgmtCmdName = device.getRn() + "_" + Const.MGMTCMD_NAME;
        oneM2MAPI.getInstance().tpRegisterMgmtCmd(mqttService, mgmtCmdName,
                device.dKey, Const.MGMTCMD_NAME, "false", device.getNl(), new MQTTCallback<mgmtCmdResponse>() {
                    @Override
                    public void onResponse(mgmtCmdResponse response) {
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                    }
                });
    }


    /**
     * report sensor infos
     * @param content content for reporting
     */
    public void report(String content) {
        if (mqttService == null || MQTTClient == null || MQTTClient.isMQTTConnected() == false || reportMessageDelivered == false) {
            if(reportMessageDelivered == false) {
                Log.e(TAG, "not delivered!");
            }
            return;
        }
        String dKey = UserInfo.getInstance(context).loadDeviceKey();
        if (TextUtils.isEmpty(dKey) == true) {
            return;
        }

        if(USE_REPORT_DELIVERED_CHECK == true) {
            reportMessageDelivered = false;
        }

        oneM2MAPI.getInstance().tpReport(mqttService, Const.CONTAINER_LORA_NAME,
                dKey, "Lora/Sensor", content, false, new MQTTCallback<contentInstanceResponse>() {
                    @Override
                    public void onResponse(contentInstanceResponse response) {
                        Log.i(TAG, "report success");
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                    }
                });
    }

    /**
     * report photo URL
     * @param photoURL photo URL
     */
    public void sendPhotoURL(String photoURL) {
        if (mqttService == null || MQTTClient == null || MQTTClient.isMQTTConnected() == false) {
            return;
        }

        String dKey = UserInfo.getInstance(context).loadDeviceKey();
        if (TextUtils.isEmpty(dKey) == true) {
            return;
        }

        oneM2MAPI.getInstance().tpReport(mqttService, Const.CONTAINER_PHOTOURL_NAME,
                dKey, "text", photoURL, false, new MQTTCallback<contentInstanceResponse>() {
                    @Override
                    public void onResponse(contentInstanceResponse response) {
                        Log.i(TAG, "sendPhotoURL success");
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                    }
                });
    }

    /**
     * control result
     */
    public void controlResult(String mgmtCmdName, String resourceId, boolean isSuccess) {
        if (mqttService == null || MQTTClient == null || MQTTClient.isMQTTConnected() == false) {
            return;
        }
        String dKey = UserInfo.getInstance(context).loadDeviceKey();
        if (TextUtils.isEmpty(dKey) == true) {
            return;
        }
        String exr = Const.EXECRESULT_SUCCESS;
        if (isSuccess == false) {
            exr = Const.EXECRESULT_DENIED;
        }

        oneM2MAPI.getInstance().tpResult(mqttService, mgmtCmdName,
                dKey, resourceId, exr, "3", new MQTTCallback<execInstanceResponse>() {

                    @Override
                    public void onResponse(execInstanceResponse response) {
//                        showResponseMessage("execInstance UPDATE", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
//                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
    }

    /**
     * unregister device
     */
    public void unregisterDevice() {
        if (mqttService == null || MQTTClient == null || MQTTClient.isMQTTConnected() == false) {
            Log.e(TAG, "No MQTT instances!");
            return;
        }
        String dKey = UserInfo.getInstance(context).loadDeviceKey();
        if (TextUtils.isEmpty(dKey) == true) {
            Log.e(TAG, "No dKey Found!");
            return;
        }

        try {
            mgmtCmd mgmtCmdDelete = new mgmtCmd.Builder(Definitions.Operation.Delete).
                    nm(device.getRn() + "_" + Const.MGMTCMD_NAME).
                    dKey(dKey).build();

            mqttService.publish(mgmtCmdDelete, new MQTTCallback<mgmtCmdResponse>() {
                @Override
                public void onResponse(mgmtCmdResponse response) {
                    int responseCode = 0;
                    if (response.rsc != null) {
                        responseCode = Integer.valueOf(response.rsc);
                    }
                    if (responseCode == Definitions.ResponseStatusCode.DELETED) {
                        Log.e(TAG, "mgmtCmd unregistered!");
                    } else {
                        this.onFailure(responseCode, response.RSM);
                    }
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    Log.e(TAG, errorCode + " : " + message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            remoteCSE remoteCSEDelete = new remoteCSE.Builder(Definitions.Operation.Delete)
                    .dKey(dKey).build();

            mqttService.publish(remoteCSEDelete, new MQTTCallback<remoteCSEResponse>() {
                @Override
                public void onResponse(remoteCSEResponse response) {
                    int responseCode = 0;
                    if (response.rsc != null) {
                        responseCode = Integer.valueOf(response.rsc);
                    }
                    if (responseCode == Definitions.ResponseStatusCode.DELETED) {
                        Log.e(TAG, "remoteCSE unregistered!");
                    } else {
                        this.onFailure(responseCode, response.RSM);
                    }
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    Log.e(TAG, errorCode + ":" + message);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        try {
            node nodeDelete = new node.Builder(Definitions.Operation.Delete)
                    .dKey(dKey).build();

            mqttService.publish(nodeDelete, new MQTTCallback<nodeResponse>() {
                @Override
                public void onResponse(nodeResponse response) {
                    int responseCode = 0;
                    if (response.rsc != null) {
                        responseCode = Integer.valueOf(response.rsc);
                    }
                    if (responseCode == Definitions.ResponseStatusCode.DELETED) {
                        Log.e(TAG, "node unregistered");
                    } else {
                        this.onFailure(responseCode, response.RSM);
                    }
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    Log.e(TAG, errorCode + " : " + message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
