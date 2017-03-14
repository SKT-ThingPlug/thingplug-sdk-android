package tp.skt.onem2m.binder.mqtt_v1_1.response;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.net.mqtt.MQTTUtils;

/**
 * contentInstance response
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(strict = false)
public class contentInstanceResponse extends ResponseBase {

    @Element(required = false)
    private Pc pc;

    /**
     * get resource type
     *
     * @return ty
     */
    public String getTy() {
        return pc.cin.ty;
    }

    /**
     * get resource id
     *
     * @return ri
     */
    public String getRi() {
        return pc.cin.ri;
    }

    /**
     * get resource name
     *
     * @return rn
     */
    public String getRn() {
        return pc.cin.rn;
    }

    /**
     * get parent id
     *
     * @return pi
     */
    public String getPi() {
        return pc.cin.pi;
    }

    /**
     * get creation time
     *
     * @return ct
     */
    public String getCt() {
        return pc.cin.ct;
    }

    /**
     * get last modified time
     *
     * @return lt
     */
    public String getLt() {
        return pc.cin.lt;
    }

    /**
     * get expirationTime
     *
     * @return et
     */
    public String getEt() {
        return pc.cin.et;
    }

    /**
     * get stateTag
     *
     * @return st
     */
    public String getSt() {
        return pc.cin.st;
    }

    /**
     * get creator
     *
     * @return cr
     */
    public String getCr() {
        return pc.cin.cr;
    }

    /**
     * get contentInfo
     *
     * @return cnf
     */
    public String getCnf() {
        return pc.cin.cnf;
    }

    /**
     * get contentSize
     *
     * @return cs
     */
    public String getCs() {
        return pc.cin.cs;
    }

    /**
     * get content
     *
     * @return con
     */
    public String getCon() {
        return pc.cin.con;
    }

    @Root
    private static class Pc {

        @Element(required = false)
        private Cin cin;

        @Root
        private static class Cin {
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
            private String et;

            @Expose
            @Element(required = false)
            private String st;

            @Expose
            @Element(required = false)
            private String cr;

            @Expose
            @Element(required = false)
            private String cnf;

            @Expose
            @Element(required = false)
            private String cs;

            @Expose
            @Element(required = false)
            private String con;

            @Element(required = false)
            private Ppt ppt;

            @Root
            private static class Ppt {
                @Expose
                @Element(required = false)
                private String gwl;

                @Expose
                @Element(required = false)
                private String geui;
            }

        }
    }

    @Override
    public String getRequestIdentifier() {
        return null;
    }

    @Override
    public void print() {
        MQTTUtils.log("[" + Definitions.getResourceName(Definitions.ResourceType.contentInstance) + "]");
        super.print();
        if (pc.cin == null) return;
        MQTTUtils.log("ty : " + pc.cin.ty);
        MQTTUtils.log("ri : " + pc.cin.ri);
        MQTTUtils.log("rn : " + pc.cin.rn);
        MQTTUtils.log("ct : " + pc.cin.ct);
        MQTTUtils.log("lt : " + pc.cin.lt);
        MQTTUtils.log("et : " + pc.cin.et);
        MQTTUtils.log("st : " + pc.cin.st);
        MQTTUtils.log("cr : " + pc.cin.cr);
        MQTTUtils.log("cnf : " + pc.cin.cnf);
        MQTTUtils.log("cs : " + pc.cin.cs);
        MQTTUtils.log("con : " + pc.cin.con);
        if(pc.cin.ppt == null) return;
        MQTTUtils.log("ppt.gwl : " + pc.cin.ppt.gwl);
        MQTTUtils.log("ppt.geui : " + pc.cin.ppt.geui);
    }
}
