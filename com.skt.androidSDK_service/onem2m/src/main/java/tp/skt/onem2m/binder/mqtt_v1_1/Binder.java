package tp.skt.onem2m.binder.mqtt_v1_1;

import tp.skt.onem2m.api.MQTTProcessor;
import tp.skt.onem2m.api.oneM2MResource;
import tp.skt.onem2m.net.mqtt.MQTTConfiguration;

/**
 * message binder
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class Binder extends MQTTProcessor {
    @Override
    public <T extends oneM2MResource> String serialization(T request) {
        return Converter.toXml(request);
    }

    @Override
    public <T extends oneM2MResource> String serialization(T request, MQTTConfiguration config) {
        return Converter.toXml(request, config);
    }

    @Override
    public <T extends oneM2MResource> T parsing(final Class<T> clz, String result) throws Exception {
        return Converter.toObject(clz, result);
    }
}
