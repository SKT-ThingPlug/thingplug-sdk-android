package tp.skt.onem2m.binder.mqtt_v1_1;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import tp.skt.onem2m.common.MQTTConst;
import tp.skt.onem2m.net.mqtt.MQTTConfiguration;

/**
 * message converter
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class Converter {
    final static int MAX_RI = 9999;
    final static Serializer serializer = new Persister();
    final static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    private static int ri = 0;

    public static <T> String toXml(T object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            OutputStreamWriter osw = new OutputStreamWriter(byteArrayOutputStream);
            serializer.write(object, osw);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toString();
    }

    public static <T> String toXml(T object, MQTTConfiguration config) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            OutputStreamWriter osw = new OutputStreamWriter(byteArrayOutputStream);
            serializer.write(object, osw);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String xml = byteArrayOutputStream.toString();
        return xml.replaceAll("\\{" + MQTTConst.CSEBASE_ID + "\\}", config.getCSEBaseID()).
                replaceAll("\\{" + MQTTConst.RESOURCE_ID + "\\}", config.getResourceID()).
                replaceAll("\\{" + MQTTConst.CLIENT_ID + "\\}", config.getClientID());
    }

    public static <T> T toObject(final Class<T> clz, String xml) throws Exception {
        return serializer.read(clz, xml);
    }

    public static <T> String toJson(T object) {
        return gson.toJson(object);
    }

    public static <T> String toJson(T object, MQTTConfiguration config) {
        String json = gson.toJson(object);
        return json.replaceAll("\\{" + MQTTConst.CSEBASE_ID + "\\}", config.getCSEBaseID()).
                replaceAll("\\{" + MQTTConst.RESOURCE_ID + "\\}", config.getResourceID()).
                replaceAll("\\{" + MQTTConst.CLIENT_ID + "\\}", config.getClientID());
    }

    /**
     * get Request Identifier
     *
     * @return
     */
    public static int getRI() {
        ri++;
        if (ri > MAX_RI) {
            ri = 0;
        }
        return ri;
    }

    /**
     * null check
     *
     * @param object
     * @param message
     * @param <T>
     * @return
     */
    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * false check
     *
     * @param isTrue
     * @param message
     * @return
     */
    public static boolean checkNotFalse(boolean isTrue, String message) {
        if (!isTrue) {
            throw new IllegalArgumentException(message);
        }
        return isTrue;
    }
}
