package tp.skt.onem2m.net.mqtt;

import android.content.Context;
import android.text.TextUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import tp.skt.onem2m.annotation.MQTT;
import tp.skt.onem2m.api.IMQTT;
import tp.skt.onem2m.api.MQTTProcessor;
import tp.skt.onem2m.api.oneM2MResource;
import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.binder.mqtt_v1_1.control.execInstanceControl;
import tp.skt.onem2m.binder.mqtt_v1_1.response.ResponseBase;
import tp.skt.onem2m.common.MQTTConst;

/**
 * MQTT client
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class MQTTClient {

    private final Map<Integer, MQTTCallback> mqttCallBackMap = new LinkedHashMap<>();
    private Context context;
    private String baseUrl;
    private String clientId;
    private String userName;
    private String password;
    /* for MQTT */
    private MqttAndroidClient mqttAndroidClient;
    private String publishTopic;
    private String[] subscribeTopics;
    private MQTTConfiguration mqttConfiguration;
    private MQTTProcessor mqttProcessor;
    private MQTTProcessor.MQTTListener mqttListener;

    /**
     * @param context
     * @param baseUrl
     * @param clientId
     */
    MQTTClient(Context context, String baseUrl, String clientId) {
        this(context, baseUrl, clientId, null, null);
    }

    /**
     * @param context
     * @param baseUrl
     * @param clientId
     * @param userName
     * @param password
     */
    MQTTClient(Context context, String baseUrl, String clientId, String userName, String password) {
        this.context = context.getApplicationContext();
        this.baseUrl = baseUrl;
        this.clientId = clientId;
        this.userName = userName;
        this.password = password;
    }


    public void disconnect() {
        if (null != mqttAndroidClient && mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.disconnect(null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        MQTTUtils.log("disconnect success!");
                        mqttListener.onDisconnected();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        MQTTUtils.log(exception.getMessage());
                        mqttListener.onDisconnectFailure();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        mqttAndroidClient = null;
    }

    public <T> T connect(Class<T> clz, MQTTConfiguration config, MQTTProcessor processor, MQTTProcessor.MQTTListener listener) {

        MQTTUtils.checkNull(config, "MQTTConfiguration is null!");
        MQTTUtils.checkNull(processor, "MQTTProcessor is null!");
        MQTTUtils.checkNull(listener, "MQTTListener is null!");

        disconnect();

        this.mqttConfiguration = config;
        this.mqttProcessor = processor;
        this.mqttListener = listener;

        Class<?> mqttClz = clz;

        if (null == mqttClz) {
            mqttClz = IMQTT.class;
        }
        if (mqttClz.isAnnotationPresent(MQTT.class)) {
            MQTT mqtt = mqttClz.getAnnotation(MQTT.class);
            String publishTemp = mqtt.publish();
            String[] subscribeTemps = mqtt.subscribe();
            subscribeTopics = new String[subscribeTemps.length];

            publishTopic = publishTemp.replace("{" + MQTTConst.CLIENT_ID + "}", config.getClientID()).
                    replace("{" + MQTTConst.CSEBASE + "}", config.getCSEBase());
            for (int idx = 0; idx < subscribeTemps.length; idx++) {
                String subscribe = subscribeTemps[idx];
                subscribeTopics[idx] = subscribe.replace("{" + MQTTConst.CLIENT_ID + "}", config.getClientID());
            }

            MQTTUtils.log(publishTopic);
            MQTTUtils.log(Arrays.toString(subscribeTopics));

            // MQTT Client 생성및 설정.
            mqttAndroidClient = new MqttAndroidClient(context, baseUrl, clientId);

            mqttAndroidClient.setCallback(new MqttCallback() { // Extended() {
//                @Override
//                public void connectComplete(boolean reconnect, String serverURI) {
//                    if (reconnect == true) {
//                        MQTTUtils.log("Reconnected to : " + serverURI);
//                        // Because Clean Session is true, we need to re-subscribe
//                        subscribeTopic();
//                    }
//                }

                @Override
                public void connectionLost(Throwable cause) {
                    if (mqttListener != null) {
                        mqttListener.onConnectionLost();
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    if (message.isDuplicate() == true) {
                        MQTTUtils.log("message duplicated!");
                        return;
                    }
                    MQTTUtils.log("messageArrived : " + topic + "-" + message.toString());
                    String result = message.toString();

                    // control received
                    if (topic.startsWith("/oneM2M/req")) {
                        try {
                            execInstanceControl eic = new execInstanceControl();
                            execInstanceControl control = mqttProcessor.parsing(eic.getClass(), result);
                            control.print();
                            mqttListener.onPush(control);
                        } catch (Exception e) {
                            MQTTUtils.log("[exception control] : " + result);
                            e.printStackTrace();
                        }

                    } else {
                        String requestRi = MQTTUtils.getRequestRi(result);
                        if (!TextUtils.isEmpty(requestRi)) {
                            int ri = Integer.valueOf(requestRi);

                            if (mqttCallBackMap.containsKey(ri)) {
                                MQTTCallback cb = mqttCallBackMap.get(ri);
                                mqttCallBackMap.remove(ri);

                                try {
                                    Class genericClass = (Class) ((ParameterizedType) cb.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                                    MQTTUtils.log("genericClass : " + genericClass.getName());
                                    ResponseBase response = (ResponseBase) mqttProcessor.parsing(genericClass, result);
                                    response.print();
                                    cb.onResponse(response);
                                } catch (Exception e) {
                                    MQTTUtils.log("[exception response] : " + result);
                                    e.printStackTrace();
                                }
                            } else {
                                MQTTUtils.log("[no map response] : " + result);
                            }
                        } else {
                            MQTTUtils.log("[exception response] : response identifier is null!");
                            for (Map.Entry<Integer, MQTTCallback> entry : mqttCallBackMap.entrySet()) {
                                MQTTCallback callback = entry.getValue();
                                callback.onFailure(Definitions.ResponseStatusCode.INTERNAL_SDK_ERROR, "response identifier is null!");
                                mqttCallBackMap.remove(entry.getKey());
                            }
                        }
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    MQTTUtils.log("message delivered");
                    mqttListener.onDelivered();
                }
            });

            final MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(false);

            if (this.userName != null) {
                MQTTUtils.log("userName : " + this.userName);
                mqttConnectOptions.setUserName(this.userName);
            }
            if (this.password != null) {
                mqttConnectOptions.setPassword(this.password.toCharArray());
                MQTTUtils.log("password : " + this.password);
            }

            try {
                mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        mqttListener.onConnected();
                        subscribeTopic();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        exception.printStackTrace();
                        mqttListener.onConnectFailure();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
                mqttListener.onConnectFailure();
            }
        }


        return (T) Proxy.newProxyInstance(mqttClz.getClassLoader(), new Class<?>[]{mqttClz},
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object... args)
                            throws Throwable {
                        // If the method is a method from Object then defer to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }

                        if (args.length >= 2) {
                            if (args[0] instanceof oneM2MResource
                                    && args[1] instanceof MQTTCallback) {
                                MQTTUtils.log("publish");
                                return publish((oneM2MResource) args[0], (MQTTCallback) args[1]);
                            }
                        }

                        return -1;
                    }
                });
    }

    /**
     * subscribe topic
     */
    public void subscribeTopic() {
        if (mqttAndroidClient == null || mqttAndroidClient.isConnected() == false || subscribeTopics == null) {
            mqttListener.onSubscribeFailure();
			return;
        }
        try {
            mqttAndroidClient.subscribe(subscribeTopics, new int[]{1, 1}, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MQTTUtils.log(Arrays.toString(asyncActionToken.getTopics()));
                    mqttListener.onSubscribed();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    MQTTUtils.log(Arrays.toString(asyncActionToken.getTopics()));
                    mqttListener.onSubscribeFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mqttListener.onSubscribeFailure();
        }
    }

    private int publish(oneM2MResource api, final MQTTCallback callBack) throws MqttException {
        final Integer requestId = Integer.valueOf(api.getRequestIdentifier());

        MqttMessage message = new MqttMessage();

        message.setPayload(mqttProcessor.serialization(api, mqttConfiguration).getBytes());
        mqttCallBackMap.put(requestId, callBack);

        MQTTUtils.log("requestId : " + requestId);
        MQTTUtils.log("publishTopic : " + publishTopic);
        MQTTUtils.log("message : " + message.toString());

        mqttAndroidClient.publish(publishTopic, message, context, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                MQTTUtils.log("message publish success");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                MQTTUtils.log(exception.getMessage());
                callBack.onFailure(asyncActionToken.getMessageId(), exception.getMessage());
            }
        });

        return requestId;
    }

    public IMQTT connect(MQTTConfiguration config, MQTTProcessor processor, MQTTProcessor.MQTTListener listener) {
        return connect(IMQTT.class, config, processor, listener);
    }

    public boolean isMQTTConnected() {
//        MQTTUtils.checkNull(mqttAndroidClient, "mqttAndroidClient == null");
        if (mqttAndroidClient == null) {
            return false;
        }
        return mqttAndroidClient.isConnected();
    }

    public String baseUrl() {
        return baseUrl;
    }

    public static class Builder {
        private String baseUrl;
        private String clientId;
        private String userName;
        private String password;
        private Context context;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder baseUrl(String baseUrl) {
            MQTTUtils.checkNull(baseUrl, "baseUrl == null");
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder clientId(String clientId) {
            MQTTUtils.checkNull(clientId, "clientId == null");
            this.clientId = clientId;
            return this;
        }

        public Builder userName(String userName) {
            MQTTUtils.checkNull(userName, "userName == null");
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            MQTTUtils.checkNull(password, "password == null");
            this.password = password;
            return this;
        }

        public Builder setLog(boolean enabled) {
            MQTTUtils.setLogEnabled(enabled);
            return this;
        }

        public MQTTClient build() {
            MQTTUtils.checkNull(baseUrl, "baseUrl == null");
            return new MQTTClient(context, baseUrl, clientId, userName, password);
        }
    }
}
