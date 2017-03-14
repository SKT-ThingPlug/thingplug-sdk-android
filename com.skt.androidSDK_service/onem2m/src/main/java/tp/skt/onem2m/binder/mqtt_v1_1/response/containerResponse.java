package tp.skt.onem2m.binder.mqtt_v1_1.response;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.net.mqtt.MQTTUtils;

/**
 * container response
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(strict = false)
public class containerResponse extends ResponseBase {

    @Element(required = false)
    private Pc pc;

    /**
     * get resource type
     *
     * @return ty
     */
    public String getTy() {
        return pc.cnt.ty;
    }

    /**
     * get resource id
     *
     * @return ri
     */
    public String getRi() {
        return pc.cnt.ri;
    }

    /**
     * get resource name
     *
     * @return rn
     */
    public String getRn() {
        return pc.cnt.rn;
    }

    /**
     * get parent id
     *
     * @return pi
     */
    public String getPi() {
        return pc.cnt.pi;
    }

    /**
     * get creation time
     *
     * @return ct
     */
    public String getCt() {
        return pc.cnt.ct;
    }

    /**
     * get last modified time
     *
     * @return lt
     */
    public String getLt() {
        return pc.cnt.lt;
    }

    /**
     * get labels
     *
     * @return lbl
     */
    public String getLbl() {
        return pc.cnt.lbl;
    }

    /**
     * get stateTag
     *
     * @return st
     */
    public String getSt() {
        return pc.cnt.st;
    }

    /**
     * get creator
     *
     * @return cr
     */
    public String getCr() {
        return pc.cnt.cr;
    }

    /**
     * get currentNrOfInstances
     *
     * @return cni
     */
    public String getCni() {
        return pc.cnt.cni;
    }

    /**
     * get currentByteSize
     *
     * @return cbs
     */
    public String getCbs() {
        return pc.cnt.cbs;
    }

    @Root
    private static class Pc {

        @Element(required = false)
        private Cnt cnt;

        @Root
        private static class Cnt {
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
            private String lbl;

            @Expose
            @Element(required = false)
            private String st;

            @Expose
            @Element(required = false)
            private String cr;

            @Expose
            @Element(required = false)
            private String cni;

            @Expose
            @Element(required = false)
            private String cbs;
        }
    }

    @Override
    public String getRequestIdentifier() {
        return null;
    }

    @Override
    public void print() {
        MQTTUtils.log("[" + Definitions.getResourceName(Definitions.ResourceType.container) + "]");
        super.print();
        if (pc.cnt == null) return;
        MQTTUtils.log("ty : " + pc.cnt.ty);
        MQTTUtils.log("ri : " + pc.cnt.ri);
        MQTTUtils.log("rn : " + pc.cnt.rn);
        MQTTUtils.log("ct : " + pc.cnt.ct);
        MQTTUtils.log("lt : " + pc.cnt.lt);
        MQTTUtils.log("lbl : " + pc.cnt.lbl);
        MQTTUtils.log("st : " + pc.cnt.st);
        MQTTUtils.log("cr : " + pc.cnt.cr);
        MQTTUtils.log("cni : " + pc.cnt.cni);
        MQTTUtils.log("cbs : " + pc.cnt.cbs);
    }
}
