package tp.skt.example;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Arrays;
import java.util.Random;

import tp.skt.onem2m.api.IMQTT;
import tp.skt.onem2m.api.MQTTProcessor;
import tp.skt.onem2m.api.oneM2MAPI;
import tp.skt.onem2m.binder.mqtt_v1_1.Binder;
import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.binder.mqtt_v1_1.Definitions.Operation;
import tp.skt.onem2m.binder.mqtt_v1_1.control.execInstanceControl;
import tp.skt.onem2m.binder.mqtt_v1_1.request.AE;
import tp.skt.onem2m.binder.mqtt_v1_1.request.CSEBase;
import tp.skt.onem2m.binder.mqtt_v1_1.request.areaNwkInfo;
import tp.skt.onem2m.binder.mqtt_v1_1.request.container;
import tp.skt.onem2m.binder.mqtt_v1_1.request.contentInstance;
import tp.skt.onem2m.binder.mqtt_v1_1.request.execInstance;
import tp.skt.onem2m.binder.mqtt_v1_1.request.locationPolicy;
import tp.skt.onem2m.binder.mqtt_v1_1.request.mgmtCmd;
import tp.skt.onem2m.binder.mqtt_v1_1.request.node;
import tp.skt.onem2m.binder.mqtt_v1_1.request.remoteCSE;
import tp.skt.onem2m.binder.mqtt_v1_1.response.AEResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.CSEBaseResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.ResponseBase;
import tp.skt.onem2m.binder.mqtt_v1_1.response.areaNwkInfoResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.containerResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.contentInstanceResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.execInstanceResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.locationPolicyResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.mgmtCmdResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.nodeResponse;
import tp.skt.onem2m.binder.mqtt_v1_1.response.remoteCSEResponse;
import tp.skt.onem2m.net.mqtt.MQTTCallback;
import tp.skt.onem2m.net.mqtt.MQTTClient;
import tp.skt.onem2m.net.mqtt.MQTTConfiguration;

/**
 * MainActivity
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class MainActivity extends AppCompatActivity {
    private MQTTClient MQTTClient;

    private IMQTT mqttService;
    MQTTConfiguration config;

    // values
    private nodeResponse nodeRes;
    private remoteCSEResponse device;
    private areaNwkInfoResponse areaNwkInfoRes;
    private containerResponse sensor;
    private contentInstanceResponse sensorInfo;
    private mgmtCmdResponse control;
    private locationPolicyResponse locationPolicyRes;
    private AEResponse AERes;

    private final String TAG = "TP_SDK_SAMPLE_APP";

    private long mLastClickTime;

    // status
    private final int DISCONNECTED = 0;
    private final int SUBSCRIBED = 1;
    private final int REGISTERED = 2;
    private int mStatus = DISCONNECTED;

    // client id
    private String mClientID = "";

    // binder
    private Binder mBinder;

    /**
     * @return 0 ~ 99999
     */
    private int getRandomNumber() {
        Random id = new Random();
        id.setSeed(System.currentTimeMillis());
        return id.nextInt(100000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mClientID = Configuration.ACCOUNT_ID + "_" + getRandomNumber();

        MQTTClient.Builder builder = new MQTTClient.Builder(MainActivity.this)
                .baseUrl(Configuration.MQTT_SECURE_HOST)
                .clientId(mClientID)
                .userName(Configuration.ACCOUNT_ID)
                .password(Configuration.UKEY)
                .setLog(true);

        MQTTClient = builder.build();
    }

    public void onClick(View view) {
        // prevent double tap
        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000 || MQTTClient == null) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        int viewId = view.getId();

        switch (viewId) {
            case R.id.register:
                if (mStatus == SUBSCRIBED) {
                    registerDevice();
                }
                break;
            case R.id.deregister:
                if (mStatus == REGISTERED) {
                    unregisterDevice();
//                    unregister();
                }
                break;
            case R.id.sendData:
                if (mStatus == REGISTERED) {
                    report();
                }
                break;
            case R.id.disconnect:
                if (mStatus > DISCONNECTED) {
                    MQTTClient.disconnect();
                }
                break;
//            case R.id.update:
//                if (mStatus == REGISTERED) {
//                    update();
//                }
//                break;
            case R.id.connect:
                if (mStatus > DISCONNECTED) {
                    return;
                }
                config = new MQTTConfiguration(Configuration.CSEBASE,
                        Configuration.ONEM2M_TO,
                        Configuration.ONEM2M_NODEID,
                        mClientID);
                mBinder = new Binder();
                mqttService = MQTTClient.connect(IMQTT.class, config, mBinder, new MQTTProcessor.MQTTListener() {
                            @Override
                            public void onPush(execInstanceControl control) {
                                StringBuilder message = new StringBuilder();
                                message.append("[execInstance(control)]\n").
                                        append("ri : ").append(control.getRi()).append("\n").
                                        append("cmt : ").append(control.getCmt()).append("\n").
                                        append("ext : ").append(control.getExt()).append("\n").
                                        append("exra : ").append(control.getExra());
                                showToast(message.toString(), Toast.LENGTH_LONG);
                                controlResult(control.getNm(), control.getRi());
                            }

                            @Override
                            public void onDisconnected() {
                                setStatus(DISCONNECTED);
                                showToast("disconnected!", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onDisconnectFailure() {
                                showToast("disconnect fail!", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onSubscribed() {
                                setStatus(SUBSCRIBED);
                                showToast("subscribed!", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onSubscribeFailure() {
                                MQTTClient.disconnect();
                                showToast("subscribe fail!", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onConnected() {
                                showToast("connected!", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onConnectFailure() {
                                showToast("connect fail!", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onConnectionLost() {
                                setStatus(DISCONNECTED);
                                showToast("connection lost!", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onDelivered() {

                            }
                        }
                );
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * @param response
     */
    private void showResponseMessage(String title, ResponseBase response) {
        StringBuilder message = new StringBuilder();
        message.append(title == null ? "[response]\n" : "[" + title + "]\n").
                append("ri : ").append(response.ri).append("\n").
                append("rsc : ").append(response.rsc);
        if (TextUtils.isEmpty(response.RSM) == false) {
            message.append("\nRSM : ").append(response.RSM);
        }
        showToast(message.toString(), Toast.LENGTH_LONG);
    }

    /**
     * show toast
     *
     * @param message
     */
    private void showToast(String message, int duration) {
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }

    private void setStatus(int status) {
        mStatus = status;

        String text = "";
        switch (status) {
            case SUBSCRIBED:
                String subscribeTopics[] = {"/oneM2M/resp/" + Configuration.ONEM2M_NODEID + "/+", "/oneM2M/req/+/" + Configuration.ONEM2M_NODEID};
                StringBuilder topics = new StringBuilder();
                topics.append("SUBSCRIBED\n").
                        append("ST : ").append(Arrays.toString(subscribeTopics)).append("\n").
                        append("PT : ").append("/oneM2M/req/" + Configuration.ONEM2M_NODEID + "/ThingPlug");
                text = topics.toString();
                break;
            case REGISTERED:
                text = "REGISTERED(nl : " + device.getNl() + ")";
                break;
            default:
                text = "DISCONNECTED";
                break;
        }
        setStatus(text);
    }

    private void setStatus(String text) {
        TextView display = (TextView) findViewById(R.id.status);
        display.setText(text);
    }

//    private void setDisplay(ResponseBase base) {
//        String xml = mBinder.serialization(base);
//        TextView display = (TextView) findViewById(R.id.display);
//        display.setText(xml);
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Simple API guide
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void unregisterDevice() {
        if (device == null) return;
        try {
            node nodeDelete = new node.Builder(Operation.Delete)
                    .dKey(device.dKey).build();

            mqttService.publish(nodeDelete, new MQTTCallback<nodeResponse>() {
                @Override
                public void onResponse(nodeResponse response) {
                    showResponseMessage("node DELETE", response);
                    int responseCode = 0;
                    if (response.rsc != null) {
                        responseCode = Integer.valueOf(response.rsc);
                    }
                    if (responseCode == Definitions.ResponseStatusCode.DELETED) {
                        remoteCSE remoteCSEDelete = new remoteCSE.Builder(Operation.Delete)
                                .dKey(device.dKey).build();
                        try {
                            mqttService.publish(remoteCSEDelete, new MQTTCallback<remoteCSEResponse>() {
                                @Override
                                public void onResponse(remoteCSEResponse response) {
                                    int responseCode = 0;
                                    if (response.rsc != null) {
                                        responseCode = Integer.valueOf(response.rsc);
                                    }
                                    if (responseCode == Definitions.ResponseStatusCode.DELETED) {
                                        setStatus(SUBSCRIBED);
                                    }
                                    showResponseMessage("remoteCSE DELETE", response);
                                }

                                @Override
                                public void onFailure(int errorCode, String message) {
                                    Log.e(TAG, message);
                                    showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                                }
                            });
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    } else {
                        this.onFailure(responseCode, response.RSM);
                    }
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    Log.e(TAG, errorCode + " : " + message);
                    showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (control != null) {
            try {
                mgmtCmd mgmtCmdDelete = new mgmtCmd.Builder(Operation.Delete).
                        nm(control.getRn()).
                        dKey(device.dKey).build();

                mqttService.publish(mgmtCmdDelete, new MQTTCallback<mgmtCmdResponse>() {
                    @Override
                    public void onResponse(mgmtCmdResponse response) {
                        showResponseMessage("mgmtCmd DELETE", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * register device
     */
    private void registerDevice() {
        if (mqttService == null) return;
        oneM2MAPI.getInstance().tpRegisterDevice(mqttService, Configuration.ONEM2M_PASSCODE,
                "3", "true", new MQTTCallback<remoteCSEResponse>() {
                    @Override
                    public void onResponse(remoteCSEResponse response) {
                        MainActivity.this.device = response;

                        showResponseMessage("node & remoteCSE CREATE", response);
//                        setDisplay(response);
                        registerSensor();
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
    }


    /**
     * registerSensor
     */
    private void registerSensor() {
        if (device == null) return;
        oneM2MAPI.getInstance().tpRegisterContainer(mqttService, Configuration.CONTAINER_NAME,
                device.dKey, new MQTTCallback<containerResponse>() {
                    @Override
                    public void onResponse(containerResponse response) {
                        MainActivity.this.sensor = response;
                        showResponseMessage("container CREATE", response);
                        registerControl();
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
    }

    private void report() {
        if (device == null && sensor != null) return;

        oneM2MAPI api = oneM2MAPI.getInstance();

        api.tpAddData("111");
        api.tpAddData("222");
        api.tpAddData("333");

        api.tpReport(mqttService, Configuration.CONTAINER_NAME,
                device.dKey, "text", null, true, new MQTTCallback<contentInstanceResponse>() {
                    @Override
                    public void onResponse(contentInstanceResponse response) {
                        MainActivity.this.sensorInfo = response;
                        showResponseMessage("contentInstance CREATE", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
    }


    /**
     * register control
     */
    private void registerControl() {
        if (device == null) return;

        oneM2MAPI.getInstance().tpRegisterMgmtCmd(mqttService, Configuration.MGMTCMD_NAME,
                device.dKey, Configuration.CMT_DEVRESET, "true", device.getNl(), new MQTTCallback<mgmtCmdResponse>() {
                    @Override
                    public void onResponse(mgmtCmdResponse response) {
                        MainActivity.this.control = response;
                        showResponseMessage("mgmtCmd CREATE", response);
                        setStatus(REGISTERED);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
    }

    /**
     * control result
     */
    private void controlResult(String mgmtCmdName, String resourceId) {
        if (device == null) return;

        oneM2MAPI.getInstance().tpResult(mqttService, mgmtCmdName,
                device.dKey, resourceId, "0", "3", new MQTTCallback<execInstanceResponse>() {

                    @Override
                    public void onResponse(execInstanceResponse response) {
                        showResponseMessage("execInstance UPDATE", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // other API guide
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * CSEBase Retrieve
     */
    private void CSEBaseRetrieve() {
        if (mqttService == null) return;

        try {
            CSEBase CSEBaseRetrieve = new CSEBase.Builder(Operation.Retrieve).build();

            mqttService.publish(CSEBaseRetrieve, new MQTTCallback<CSEBaseResponse>() {
                @Override
                public void onResponse(CSEBaseResponse response) {
                    nodeCreate();
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    Log.e(TAG, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * node CREATE
     */
    private void nodeCreate() {
        if (mqttService == null) return;

        try {
            node nodeCreate = new node.Builder(Operation.Create).
                    mga("MQTT|" + Configuration.ONEM2M_NODEID).build();

            mqttService.publish(nodeCreate, new MQTTCallback<nodeResponse>() {
                @Override
                public void onResponse(nodeResponse response) {
                    nodeRes = response;
                    nodeRetrieve();
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    Log.e(TAG, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * node Retrieve
     */
    private void nodeRetrieve() {
        if (mqttService == null) return;

        try {
            node nodeRetrieve = new node.Builder(Operation.Retrieve)
                    .uKey(Configuration.UKEY).build();

            mqttService.publish(nodeRetrieve, new MQTTCallback<nodeResponse>() {
                @Override
                public void onResponse(nodeResponse response) {
                    remoteCSECreate();
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

    /**
     * remoteCSE CREATE
     */
    private void remoteCSECreate() {
        if (nodeRes == null) return;

        try {
            remoteCSE remoteCSECreate = new remoteCSE.Builder(Operation.Create).
                    passCode(Configuration.ONEM2M_PASSCODE).
                    cst("3").
                    poa("MQTT|" + Configuration.ONEM2M_NODEID).
                    rr("true").
                    nl(nodeRes.getRi()).build();

            mqttService.publish(remoteCSECreate, new MQTTCallback<remoteCSEResponse>() {
                @Override
                public void onResponse(remoteCSEResponse response) {
                    device = response;
                    remoteCSERetrieve();
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    Log.e(TAG, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * remoteCSE RETRIEVE
     */
    private void remoteCSERetrieve() {
        if (nodeRes == null) return;

        remoteCSE remoteCSERetrieve = new remoteCSE.Builder(Operation.Retrieve)
                .uKey(Configuration.UKEY).build();
        try {
            mqttService.publish(remoteCSERetrieve, new MQTTCallback<remoteCSEResponse>() {
                @Override
                public void onResponse(remoteCSEResponse response) {
                    AECreate();
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    Log.e(TAG, message);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * AE CREATE
     */
    private void AECreate() {
        if (device == null) return;

        try {
            AE AECreate = new AE.Builder(Operation.Create).
                    dKey(device.dKey).
                    api(Configuration.ONEM2M_NODEID).
                    apn(Configuration.AE_NAME).build();

            mqttService.publish(AECreate, new MQTTCallback<AEResponse>() {
                @Override
                public void onResponse(AEResponse response) {
                    AERes = response;
                    AERetrieve();
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    Log.e(TAG, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * AE RETRIEVE
     */
    private void AERetrieve() {
        if (AERes == null) return;

        try {
            AE AERetrieve = new AE.Builder(Operation.Retrieve).
                    nm(AERes.getRn()). //Configuration.AE_NAME).
                    uKey(Configuration.UKEY).build();

            mqttService.publish(AERetrieve, new MQTTCallback<AEResponse>() {
                @Override
                public void onResponse(AEResponse response) {
                    containerCreate();
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

    /**
     * container CREATE
     */
    private void containerCreate() {
        if (device == null) return;

        try {
            container containerCreate = new container.Builder(Operation.Create).
                    nm(Configuration.CONTAINER_NAME).
                    dKey(device.dKey).
                    lbl("con").build();

            mqttService.publish(containerCreate, new MQTTCallback<containerResponse>() {
                @Override
                public void onResponse(containerResponse response) {
                    sensor = response;
                    containerRetrieve();
//                    areaNwkInfoCreate();
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

    /**
     * container RETRIEVE
     */
    private void containerRetrieve() {
        if (device == null) return;

        try {
            container containerRetrieve = new container.Builder(Operation.Retrieve).
                    nm(Configuration.CONTAINER_NAME).
                    uKey(Configuration.UKEY).build();

            mqttService.publish(containerRetrieve, new MQTTCallback<containerResponse>() {
                @Override
                public void onResponse(containerResponse response) {
                    //
                    mgmtCmdCreate();
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    Log.e(TAG, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * mgmtCmd CREATE
     */
    private void mgmtCmdCreate() {
        if (device == null) return;

        try {
            mgmtCmd mgmtCmdCreate = new mgmtCmd.Builder(Operation.Create).
                    nm(Configuration.ONEM2M_NODEID + "_" + Configuration.CMT_DEVRESET).
                    dKey(device.dKey).
                    cmt(Configuration.CMT_DEVRESET).
                    exe("true").
                    ext(device.getNl()).
                    lbl("con").build();

            mqttService.publish(mgmtCmdCreate, new MQTTCallback<mgmtCmdResponse>() {
                @Override
                public void onResponse(mgmtCmdResponse response) {
                    control = response;
                    mgmtCmdRetrieve();
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

    /**
     * mgmtCmd RETRIEVE
     */
    private void mgmtCmdRetrieve() {
        if (device == null) return;

        try {
            mgmtCmd mgmtCmdRetrieve = new mgmtCmd.Builder(Operation.Retrieve).
                    nm(Configuration.ONEM2M_NODEID + "_" + Configuration.CMT_DEVRESET).
                    uKey(Configuration.UKEY).build();

            mqttService.publish(mgmtCmdRetrieve, new MQTTCallback<mgmtCmdResponse>() {
                @Override
                public void onResponse(mgmtCmdResponse response) {
                    //
                    areaNwkInfoCreate();
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

    /**
     * contentInstace CREATE
     */
    private void contentInstanceCreate() {
        if (device == null) return;

        try {
            contentInstance contentInstanceCreate = new contentInstance.Builder(Operation.Create).
                    containerName(Configuration.CONTAINER_NAME).
                    dKey(device.dKey).
                    cnf("text").
                    con("45").build();

            mqttService.publish(contentInstanceCreate, new MQTTCallback<contentInstanceResponse>() {
                @Override
                public void onResponse(contentInstanceResponse response) {
                    sensorInfo = response;
//                    contentInstanceRD(Operation.Retrieve, "oldest");
                    contentInstanceRD(Operation.Retrieve, "contentInstance-" + response.getRi());

//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    contentInstanceRD(Operation.Delete, "contentInstance-" + response.getRi());
//                    contentInstanceRD(Operation.Retrieve, "latest");
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

    /**
     * contentInstance RETRIEVE
     */
    private void contentInstanceRD(@Definitions.Operation int op, String suffix) {
        if (device == null) return;

        try {
            contentInstance contentInstanceRetrieve = new contentInstance.Builder(op).
                    containerName(Configuration.CONTAINER_NAME).
                    uKey(Configuration.UKEY).
                    dKey(device.dKey).
                    nm(suffix).build();

            mqttService.publish(contentInstanceRetrieve, new MQTTCallback<contentInstanceResponse>() {
                @Override
                public void onResponse(contentInstanceResponse response) {
                    //
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

    /**
     * areaNwkInfo CREATE
     */
    private void areaNwkInfoCreate() {
        if (device == null) return;

        try {
            areaNwkInfo areaNwkInfoCreate = new areaNwkInfo.Builder(Operation.Create).
                    nm(Configuration.AREANWKINFO_NAME).
                    dKey(device.dKey).
                    mgd("1004").
                    ant("type").
                    ldv("").build();

            mqttService.publish(areaNwkInfoCreate, new MQTTCallback<areaNwkInfoResponse>() {
                @Override
                public void onResponse(areaNwkInfoResponse response) {
                    areaNwkInfoRes = response;
                    areaNwkInfoRetrieve();
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

    /**
     * areaNwkInfo Retrieve
     */
    private void areaNwkInfoRetrieve() {
        if (device == null) return;

        try {
            areaNwkInfo areaNwkInfoRetrieve = new areaNwkInfo.Builder(Operation.Retrieve).
                    nm(Configuration.AREANWKINFO_NAME).
                    uKey(Configuration.UKEY).build();

            mqttService.publish(areaNwkInfoRetrieve, new MQTTCallback<areaNwkInfoResponse>() {
                @Override
                public void onResponse(areaNwkInfoResponse response) {
                    locationPolicyCreate();
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

    /**
     * locationPolicy CREATE
     */
    private void locationPolicyCreate() {
        if (device == null) return;

        try {
            locationPolicy locationPolicyCreate = new locationPolicy.Builder(Operation.Create).
                    nm(Configuration.LOCATIONPOLICY_NAME).
                    dKey(device.dKey).
                    los("2").build();

            mqttService.publish(locationPolicyCreate, new MQTTCallback<locationPolicyResponse>() {
                @Override
                public void onResponse(locationPolicyResponse response) {
                    locationPolicyRes = response;
                    locationPolicyRetrieve();
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

    /**
     * locationPolicy Retrieve
     */
    private void locationPolicyRetrieve() {
        if (device == null) return;

        try {
            locationPolicy locationPolicyRetrieve = new locationPolicy.Builder(Operation.Retrieve).
                    nm(Configuration.LOCATIONPOLICY_NAME).
                    uKey(Configuration.UKEY).build();

            mqttService.publish(locationPolicyRetrieve, new MQTTCallback<locationPolicyResponse>() {
                @Override
                public void onResponse(locationPolicyResponse response) {
                    contentInstanceCreate();
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

    /**
     * execInstance UPDATE
     */
    private void execInstanceUpdate(String nm, String ri) {
        if (device == null) return;

        try {
            execInstance execInstanceUpdate = new execInstance.Builder(Operation.Update).
                    nm(nm).
                    resourceId(ri).
                    exr("0").
                    exs("3").
                    dKey(device.dKey).build();

            mqttService.publish(execInstanceUpdate, new MQTTCallback<execInstanceResponse>() {
                @Override
                public void onResponse(execInstanceResponse response) {
                    //
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

    private void update() {
        if (sensor != null) {
            try {
                container containerUpdate = new container.Builder(Operation.Update).
                        nm(sensor.getRn()).
                        lbl("event").
                        dKey(device.dKey).build();

                mqttService.publish(containerUpdate, new MQTTCallback<containerResponse>() {
                    @Override
                    public void onResponse(containerResponse response) {
                        showResponseMessage("container Update", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (control != null) {
            try {
                mgmtCmd mgmtCmdUpdate = new mgmtCmd.Builder(Operation.Update).
                        nm(control.getRn()).
                        exe("true").
                        uKey(Configuration.UKEY).build();

                mqttService.publish(mgmtCmdUpdate, new MQTTCallback<mgmtCmdResponse>() {
                    @Override
                    public void onResponse(mgmtCmdResponse response) {
                        showResponseMessage("mgmtCmd Update", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (AERes != null) {
            try {
                AE AEUpdate = new AE.Builder(Operation.Update).
                        nm(AERes.getRn()).
                        apn(Configuration.ONEM2M_NODEID + "_AE_02").
                        dKey(device.dKey).build();

                mqttService.publish(AEUpdate, new MQTTCallback<AEResponse>() {
                    @Override
                    public void onResponse(AEResponse response) {
                        showResponseMessage("AE Update", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (device != null) {
            remoteCSE remoteCSEUpdate = new remoteCSE.Builder(Operation.Update)
                    .rr("false")
                    .dKey(device.dKey).build();
            try {
                mqttService.publish(remoteCSEUpdate, new MQTTCallback<remoteCSEResponse>() {
                    @Override
                    public void onResponse(remoteCSEResponse response) {
                        showResponseMessage("remoteCSE Update", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        if (nodeRes != null) {
            try {
                node nodeUpdate = new node.Builder(Operation.Update)
                        .mga("HTTP|" + Configuration.ONEM2M_NODEID)
                        .dKey(device.dKey).build();

                mqttService.publish(nodeUpdate, new MQTTCallback<nodeResponse>() {
                    @Override
                    public void onResponse(nodeResponse response) {

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

        if (areaNwkInfoRes != null) {
            try {
                areaNwkInfo areaNwkInfoUpdate = new areaNwkInfo.Builder(Operation.Update).
                        nm(areaNwkInfoRes.getRn()).
                        ant("type2").
                        ldv("1").
                        dKey(device.dKey).build();

                mqttService.publish(areaNwkInfoUpdate, new MQTTCallback<areaNwkInfoResponse>() {
                    @Override
                    public void onResponse(areaNwkInfoResponse response) {
                        showResponseMessage("areaNwkInfo Update", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (locationPolicyRes != null) {
            try {
                locationPolicy locationPolicyUpdate = new locationPolicy.Builder(Operation.Update).
                        nm(locationPolicyRes.getRn()).
                        los("3").
                        dKey(device.dKey).build();

                mqttService.publish(locationPolicyUpdate, new MQTTCallback<locationPolicyResponse>() {
                    @Override
                    public void onResponse(locationPolicyResponse response) {
                        showResponseMessage("locationPolicy Update", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void unregister() {
        if (sensorInfo != null) {
            sensorInfo.print();
            try {
                contentInstance contentInstanceDelete = new contentInstance.Builder(Operation.Delete).
                        nm(sensorInfo.getRn()).
                        containerName(Configuration.CONTAINER_NAME).
                        dKey(device.dKey).build();

                mqttService.publish(contentInstanceDelete, new MQTTCallback<contentInstanceResponse>() {
                    @Override
                    public void onResponse(contentInstanceResponse response) {
                        showResponseMessage("contentInstance DELETE", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (sensor != null) {
            try {
                container containerDelete = new container.Builder(Operation.Delete).
                        nm(sensor.getRn()).
                        dKey(device.dKey).build();

                mqttService.publish(containerDelete, new MQTTCallback<containerResponse>() {
                    @Override
                    public void onResponse(containerResponse response) {
                        showResponseMessage("container DELETE", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (AERes != null) {
            try {
                AE AEDelete = new AE.Builder(Operation.Delete).
                        nm(AERes.getRn()).
                        dKey(device.dKey).build();

                mqttService.publish(AEDelete, new MQTTCallback<AEResponse>() {
                    @Override
                    public void onResponse(AEResponse response) {
                        showResponseMessage("AE DELETE", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (areaNwkInfoRes != null) {
            try {
                areaNwkInfo areaNwkInfoDelete = new areaNwkInfo.Builder(Operation.Delete).
                        nm(areaNwkInfoRes.getRn()).
                        dKey(device.dKey).build();

                mqttService.publish(areaNwkInfoDelete, new MQTTCallback<areaNwkInfoResponse>() {
                    @Override
                    public void onResponse(areaNwkInfoResponse response) {
                        showResponseMessage("areaNwkInfo DELETE", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (locationPolicyRes != null) {
            try {
                locationPolicy locationPolicyDelete = new locationPolicy.Builder(Operation.Delete).
                        nm(locationPolicyRes.getRn()).
                        dKey(device.dKey).build();

                mqttService.publish(locationPolicyDelete, new MQTTCallback<locationPolicyResponse>() {
                    @Override
                    public void onResponse(locationPolicyResponse response) {
                        showResponseMessage("locationPolicy DELETE", response);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        Log.e(TAG, errorCode + " : " + message);
                        showToast("fail - " + errorCode + ":" + message, Toast.LENGTH_LONG);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        unregisterDevice();

    }

}
