package tp.skt.onem2m.binder.mqtt_v1_1.response;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.net.mqtt.MQTTUtils;

/**
 * node response
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(strict = false)
public class nodeResponse extends ResponseBase {

    @Expose
    @Element(required = false)
    private Pc pc;

    /**
     * get resource type
     *
     * @return ty
     */
    public String getTy() {
        return pc.nod.ty;
    }

    /**
     * get resource id
     *
     * @return ri
     */
    public String getRi() {
        return pc.nod.ri;
    }

    /**
     * get resource name
     *
     * @return rn
     */
    public String getRn() {
        return pc.nod.rn;
    }

    /**
     * get parent id
     *
     * @return pi
     */
    public String getPi() {
        return pc.nod.pi;
    }

    /**
     * get creation time
     *
     * @return ct
     */
    public String getCt() {
        return pc.nod.ct;
    }

    /**
     * get last modified time
     *
     * @return lt
     */
    public String getLt() {
        return pc.nod.lt;
    }

    /**
     * get node Id
     *
     * @return ni
     */
    public String getNi() {
        return pc.nod.ni;
    }

    /**
     * get MGA
     *
     * @return mga
     */
    public String getMga() {
        return pc.nod.mga;
    }

    /**
     * get hostedCSELink
     *
     * @return hcl
     */
    public String getHcl() {
        return pc.nod.hcl;
    }

    @Root
    private static class Pc {
        @Expose
        @Element(required = false)
        private Nod nod;

        @Root
        private static class Nod {
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
            private String ni;

            @Expose
            @Element(required = false)
            private String mga;

            @Expose
            @Element(required = false)
            private String hcl;
        }
    }

    @Override
    public String getRequestIdentifier() {
        return null;
    }

    @Override
    public void print() {
        MQTTUtils.log("[" + Definitions.getResourceName(Definitions.ResourceType.node) + "]");
        super.print();
        if (pc.nod == null) return;
        MQTTUtils.log("ty : " + pc.nod.ty);
        MQTTUtils.log("ri : " + pc.nod.ri);
        MQTTUtils.log("rn : " + pc.nod.rn);
        MQTTUtils.log("pi : " + pc.nod.pi);
        MQTTUtils.log("ct : " + pc.nod.ct);
        MQTTUtils.log("lt : " + pc.nod.lt);
        MQTTUtils.log("ni : " + pc.nod.ni);
        MQTTUtils.log("mga : " + pc.nod.mga);
        MQTTUtils.log("hcl : " + pc.nod.hcl);
    }
}
