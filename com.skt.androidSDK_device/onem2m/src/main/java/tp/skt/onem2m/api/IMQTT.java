package tp.skt.onem2m.api;

import org.eclipse.paho.client.mqttv3.MqttException;

import tp.skt.onem2m.annotation.MQTT;
import tp.skt.onem2m.net.mqtt.MQTTCallback;

@MQTT(
        subscribe = {
                "/oneM2M/resp/{CLIENT_ID}/+",
                "/oneM2M/req_msg/+/{CLIENT_ID}"
        },
        publish = "/oneM2M/req/{CLIENT_ID}/{CSEBASE}"
)

/**
 * Interface of MQTT service
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public interface IMQTT {

    /**
     * Publish message
     *
     * @param resource Resource object
     * @param callback Response callback
     * @return Request identifier
     * @throws MqttException Thrown if an error occurs communicating with the server
     */
    public int publish(oneM2MResource resource, MQTTCallback callback) throws MqttException;
}
