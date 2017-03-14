package tp.skt.onem2m.binder.mqtt_v1_1.response;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.net.mqtt.MQTTUtils;

/**
 * areaNwkInfo response
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(strict = false)
public class areaNwkInfoResponse extends ResponseBase {

    @Element(required = false)
    private Pc pc;

    /**
     * get resource type
     *
     * @return ty
     */
    public String getTy() {
        return pc.ani.ty;
    }

    /**
     * get resource id
     *
     * @return ri
     */
    public String getRi() {
        return pc.ani.ri;
    }

    /**
     * get resource name
     *
     * @return rn
     */
    public String getRn() {
        return pc.ani.rn;
    }

    /**
     * get parent id
     *
     * @return pi
     */
    public String getPi() {
        return pc.ani.pi;
    }

    /**
     * get creation time
     *
     * @return ct
     */
    public String getCt() {
        return pc.ani.ct;
    }

    /**
     * get last modified time
     *
     * @return lt
     */
    public String getLt() {
        return pc.ani.lt;
    }

    /**
     * get labels
     *
     * @return lbl
     */
    public String getLbl() {
        return pc.ani.lbl;
    }

    /**
     * get mgmtDefinition
     *
     * @return mgd
     */
    public String getMgd() {
        return pc.ani.mgd;
    }

    /**
     * get areaNwkType
     *
     * @return ant
     */
    public String getAnt() {
        return pc.ani.ant;
    }

    /**
     * get listOfDevices
     *
     * @return ldv
     */
    public String getLdv() {
        return pc.ani.ldv;
    }

    @Root
    private static class Pc {

        @Element(required = false)
        private Ani ani;

        @Root
        private static class Ani {
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
            private String mgd;

            @Expose
            @Element(required = false)
            private String ant;

            @Expose
            @Element(required = false)
            private String ldv;
        }
    }

    @Override
    public String getRequestIdentifier() {
        return null;
    }

    @Override
    public void print() {
        MQTTUtils.log("[" + Definitions.getResourceName(Definitions.ResourceType.mgmtObj) + "(areaNwkInfo)]");
        super.print();
        if (pc.ani == null) return;
        MQTTUtils.log("ty : " + pc.ani.ty);
        MQTTUtils.log("ri : " + pc.ani.ri);
        MQTTUtils.log("rn : " + pc.ani.rn);
        MQTTUtils.log("ct : " + pc.ani.ct);
        MQTTUtils.log("lt : " + pc.ani.lt);
        MQTTUtils.log("lbl : " + pc.ani.lbl);
        MQTTUtils.log("mgd : " + pc.ani.mgd);
        MQTTUtils.log("ant : " + pc.ani.ant);
        MQTTUtils.log("ldv : " + pc.ani.ldv);
    }
}
