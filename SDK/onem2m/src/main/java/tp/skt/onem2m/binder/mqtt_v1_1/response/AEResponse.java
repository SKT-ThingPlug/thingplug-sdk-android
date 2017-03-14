package tp.skt.onem2m.binder.mqtt_v1_1.response;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.net.mqtt.MQTTUtils;

/**
 * AE response
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(strict = false)
public class AEResponse extends ResponseBase {

    @Element(required = false)
    private Pc pc;

    /**
     * get resource type
     *
     * @return ty
     */
    public String getTy() {
        return pc.ae.ty;
    }

    /**
     * get resource id
     *
     * @return ri
     */
    public String getRi() {
        return pc.ae.ri;
    }

    /**
     * get resource name
     *
     * @return rn
     */
    public String getRn() {
        return pc.ae.rn;
    }

    /**
     * get parent id
     *
     * @return pi
     */
    public String getPi() {
        return pc.ae.pi;
    }

    /**
     * get creation time
     *
     * @return ct
     */
    public String getCt() {
        return pc.ae.ct;
    }

    /**
     * get last modified time
     *
     * @return lt
     */
    public String getLt() {
        return pc.ae.lt;
    }

    /**
     * get appName
     *
     * @return apn
     */
    public String getApn() {
        return pc.ae.apn;
    }

    /**
     * get App-ID
     *
     * @return api
     */
    public String getApi() {
        return pc.ae.api;
    }

    /**
     * get AE-ID
     *
     * @return aei
     */
    public String getAei() {
        return pc.ae.aei;
    }

    @Root
    private static class Pc {

        @Element(required = false)
        private Ae ae;

        @Root
        private static class Ae {
            @Expose
            @Element(required = false)
            private String ty;

            @Expose
            @Element(required = false)
            private String ri;

            @Expose
            @Element(required = false)
            private String rn;

            @Expose
            @Element(required = false)
            private String pi;

            @Expose
            @Element(required = false)
            private String ct;

            @Expose
            @Element(required = false)
            private String lt;

            @Expose
            @Element(required = false)
            private String apn;

            @Expose
            @Element(required = false)
            private String api;

            @Expose
            @Element(required = false)
            private String aei;
        }
    }

    @Override
    public String getRequestIdentifier() {
        return null;
    }

    @Override
    public void print() {
        MQTTUtils.log("[" + Definitions.getResourceName(Definitions.ResourceType.AE) + "]");
        super.print();
        if (pc.ae == null) return;
        MQTTUtils.log("ty : " + pc.ae.ty);
        MQTTUtils.log("ri : " + pc.ae.ri);
        MQTTUtils.log("rn : " + pc.ae.rn);
        MQTTUtils.log("ct : " + pc.ae.ct);
        MQTTUtils.log("lt : " + pc.ae.lt);
        MQTTUtils.log("apn : " + pc.ae.apn);
        MQTTUtils.log("api : " + pc.ae.api);
        MQTTUtils.log("aei : " + pc.ae.aei);
    }
}
