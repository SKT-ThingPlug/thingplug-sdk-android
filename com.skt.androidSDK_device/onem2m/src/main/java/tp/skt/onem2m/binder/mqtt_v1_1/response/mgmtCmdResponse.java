package tp.skt.onem2m.binder.mqtt_v1_1.response;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.net.mqtt.MQTTUtils;

/**
 * mgmtCmd response
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(strict = false)
public class mgmtCmdResponse extends ResponseBase {

    @Element(required = false)
    private Pc pc;

    /**
     * get resource type
     *
     * @return ty
     */
    public String getTy() {
        return pc.mgc.ty;
    }

    /**
     * get resource id
     *
     * @return ri
     */
    public String getRi() {
        return pc.mgc.ri;
    }

    /**
     * get resource name
     *
     * @return rn
     */
    public String getRn() {
        return pc.mgc.rn;
    }

    /**
     * get parent id
     *
     * @return pi
     */
    public String getPi() {
        return pc.mgc.pi;
    }

    /**
     * get creation time
     *
     * @return ct
     */
    public String getCt() {
        return pc.mgc.ct;
    }

    /**
     * get last modified time
     *
     * @return lt
     */
    public String getLt() {
        return pc.mgc.lt;
    }

    /**
     * get labels
     *
     * @return lbl
     */
    public String getLbl() {
        return pc.mgc.lbl;
    }

    /**
     * get cmdType
     *
     * @return cmt
     */
    public String getCmt() {
        return pc.mgc.cmt;
    }

    /**
     * get execEnable
     *
     * @return exe
     */
    public String getExe() {
        return pc.mgc.exe;
    }

    /**
     * get exin resourceID
     *
     * @return
     */
    public String getExinRi() {
        return pc.mgc.exin.ri;
    }

    /**
     * get execTarget
     *
     * @return ext
     */
    public String getExt() {
        return pc.mgc.ext;
    }

    @Root
    private static class Pc {

        @Element(required = false)
        private Mgc mgc;

        @Root
        private static class Mgc {
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
            private String cmt;

            @Expose
            @Element(required = false)
            private String exe;

            @Expose
            @Element(required = false)
            private String ext;

            @Element(required = false)
            private Exin exin;

            @Root
            private static class Exin {
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
                private String exs;

                @Expose
                @Element(required = false)
                private String ext;

                @Expose
                @Element(required = false)
                private String exra;
            }
        }
    }

    @Override
    public String getRequestIdentifier() {
        return null;
    }

    @Override
    public void print() {
        MQTTUtils.log("[" + Definitions.getResourceName(Definitions.ResourceType.mgmtCmd) + "]");
        super.print();
        if (pc.mgc == null) return;
        MQTTUtils.log("ty : " + pc.mgc.ty);
        MQTTUtils.log("ri : " + pc.mgc.ri);
        MQTTUtils.log("rn : " + pc.mgc.rn);
        MQTTUtils.log("ct : " + pc.mgc.ct);
        MQTTUtils.log("lt : " + pc.mgc.lt);
        MQTTUtils.log("lbl : " + pc.mgc.lbl);
        MQTTUtils.log("cmt : " + pc.mgc.cmt);
        MQTTUtils.log("exe : " + pc.mgc.exe);
        MQTTUtils.log("ext : " + pc.mgc.ext);
        if (pc.mgc.exin == null) return;
        MQTTUtils.log("[" + Definitions.getResourceName(Definitions.ResourceType.execInstance) + "]");
        MQTTUtils.log("ty : " + pc.mgc.exin.ty);
        MQTTUtils.log("ri : " + pc.mgc.exin.ri);
        MQTTUtils.log("rn : " + pc.mgc.exin.rn);
        MQTTUtils.log("pi : " + pc.mgc.exin.pi);
        MQTTUtils.log("ct : " + pc.mgc.exin.ct);
        MQTTUtils.log("lt : " + pc.mgc.exin.lt);
        MQTTUtils.log("et : " + pc.mgc.exin.et);
        MQTTUtils.log("exs : " + pc.mgc.exin.exs);
        MQTTUtils.log("ext : " + pc.mgc.exin.ext);
        MQTTUtils.log("exra : " + pc.mgc.exin.exra);
    }
}
