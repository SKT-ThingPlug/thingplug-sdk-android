package tp.skt.onem2m.binder.mqtt_v1_1.response;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.net.mqtt.MQTTUtils;

/**
 * CSEBase response
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(strict = false)
public class CSEBaseResponse extends ResponseBase {

    @Expose
    @Element(required = false)
    private Pc pc;

    /**
     * get resource type
     *
     * @return ty
     */
    public String getTy() {
        return pc.cb.ty;
    }

    /**
     * get resource id
     *
     * @return ri
     */
    public String getRi() {
        return pc.cb.ri;
    }

    /**
     * get resource name
     *
     * @return rn
     */
    public String getRn() {
        return pc.cb.rn;
    }

    /**
     * get creation time
     *
     * @return ct
     */
    public String getCt() {
        return pc.cb.ct;
    }

    /**
     * get last modified time
     *
     * @return lt
     */
    public String getLt() {
        return pc.cb.lt;
    }

    /**
     * get expiration time
     *
     * @return et
     */
    public String getEt() {
        return pc.cb.et;
    }

    /**
     * get CSE-ID
     *
     * @return csi
     */
    public String getCsi() {
        return pc.cb.csi;
    }

    /**
     * get supported resource type
     *
     * @return srt
     */
    public String getSrt() {
        return pc.cb.srt;
    }

    /**
     * get point of access
     *
     * @return poa
     */
    public String getPoa() {
        return pc.cb.poa;
    }

    @Root
    private static class Pc {

        @Element(required = false)
        private Cb cb;

        @Root
        private static class Cb {
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
            private String ct;

            @Expose
            @Element(required = false)
            private String lt;

            @Expose
            @Element(required = false)
            private String et;

            @Expose
            @Element(required = false)
            private String csi;

            @Expose
            @Element(required = false)
            private String srt;

            @Expose
            @Element(required = false)
            private String poa;
        }
    }

    @Override
    public String getRequestIdentifier() {
        return null;
    }

    @Override
    public void print() {
        MQTTUtils.log("[" + Definitions.getResourceName(Definitions.ResourceType.CSEBase) + "]");
        super.print();
        if (pc.cb == null) return;
        MQTTUtils.log("ty : " + pc.cb.ty);
        MQTTUtils.log("ri : " + pc.cb.ri);
        MQTTUtils.log("rn : " + pc.cb.rn);
        MQTTUtils.log("ct : " + pc.cb.ct);
        MQTTUtils.log("lt : " + pc.cb.lt);
        MQTTUtils.log("et : " + pc.cb.et);
        MQTTUtils.log("csi : " + pc.cb.csi);
        MQTTUtils.log("srt : " + pc.cb.srt);
        MQTTUtils.log("poa : " + pc.cb.poa);
    }
}
