package tp.skt.onem2m.binder.mqtt_v1_1.response;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.net.mqtt.MQTTUtils;

/**
 * locationPolicy response
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(strict = false)
public class locationPolicyResponse extends ResponseBase {

    @Element(required = false)
    private Pc pc;

    /**
     * get resource type
     *
     * @return ty
     */
    public String getTy() {
        return pc.lcp.ty;
    }

    /**
     * get resource id
     *
     * @return ri
     */
    public String getRi() {
        return pc.lcp.ri;
    }

    /**
     * get resource name
     *
     * @return rn
     */
    public String getRn() {
        return pc.lcp.rn;
    }

    /**
     * get parent id
     *
     * @return pi
     */
    public String getPi() {
        return pc.lcp.pi;
    }

    /**
     * get creation time
     *
     * @return ct
     */
    public String getCt() {
        return pc.lcp.ct;
    }

    /**
     * get last modified time
     *
     * @return lt
     */
    public String getLt() {
        return pc.lcp.lt;
    }

    /**
     * get labels
     *
     * @return lbl
     */
    public String getLbl() {
        return pc.lcp.lbl;
    }

    /**
     * get locationSource
     *
     * @return los
     */
    public String getLos() {
        return pc.lcp.los;
    }

    @Root
    private static class Pc {

        @Element(required = false)
        private Lcp lcp;

        @Root
        private static class Lcp {
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
            private String los;
        }
    }

    @Override
    public String getRequestIdentifier() {
        return null;
    }

    @Override
    public void print() {
        MQTTUtils.log("[" + Definitions.getResourceName(Definitions.ResourceType.locationPolicy) + "]");
        super.print();
        if (pc.lcp == null) return;
        MQTTUtils.log("ty : " + pc.lcp.ty);
        MQTTUtils.log("ri : " + pc.lcp.ri);
        MQTTUtils.log("rn : " + pc.lcp.rn);
        MQTTUtils.log("ct : " + pc.lcp.ct);
        MQTTUtils.log("lt : " + pc.lcp.lt);
        MQTTUtils.log("lbl : " + pc.lcp.lbl);
        MQTTUtils.log("los : " + pc.lcp.los);
    }
}
