package tp.skt.onem2m.net.mqtt;

/**
 * MQTT request callback
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public abstract class MQTTCallback<T> {
    public abstract void onResponse(T response);

    public abstract void onFailure(int errorCode, String message);
}
