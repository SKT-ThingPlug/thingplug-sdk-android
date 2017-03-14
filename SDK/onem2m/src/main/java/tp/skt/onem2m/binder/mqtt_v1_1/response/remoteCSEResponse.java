package tp.skt.onem2m.binder.mqtt_v1_1.response;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.net.mqtt.MQTTUtils;

/**
 * remoteCSE response
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(strict = false)
public class remoteCSEResponse extends ResponseBase {

    @Expose
    @Element(required = false)
    public String dKey;

    @Expose
    @Element(required = false)
    private Pc pc;

    /**
     * get resourceType
     *
     * @return ty
     */
    public String getTy() {
        return pc.csr.ty;
    }

    /**
     * get resourceId
     *
     * @return ri
     */
    public String getRi() {
        return pc.csr.ri;
    }

    /**
     * get resourceName
     *
     * @return rn
     */
    public String getRn() {
        return pc.csr.rn;
    }

    /**
     * get parentId
     *
     * @return pi
     */
    public String getPi() {
        return pc.csr.pi;
    }

    /**
     * get creationTime
     *
     * @return ct
     */
    public String getCt() {
        return pc.csr.ct;
    }

    /**
     * get lastModifiedTime
     *
     * @return lt
     */
    public String getLt() {
        return pc.csr.lt;
    }

    /**
     * get accessControlPolicyIDs
     *
     * @return acpi
     */
    public String getAcpi() {
        return pc.csr.acpi;
    }

    /**
     * get cseType
     *
     * @return cst
     */
    public String getCst() {
        return pc.csr.cst;
    }

    /**
     * get pointOfAccess
     *
     * @return poa
     */
    public String getPoa() {
        return pc.csr.poa;
    }

    /**
     * get CSE-ID
     *
     * @return csi
     */
    public String getCsi() {
        return pc.csr.csi;
    }

    /**
     * get requestReachability
     *
     * @return rr
     */
    public String getRr() {
        return pc.csr.rr;
    }

    /**
     * get nodeLink
     *
     * @return nl
     */
    public String getNl() {
        return pc.csr.nl;
    }

    @Root
    private static class Pc {

        @Expose
        @Element(required = false)
        private Csr csr;

        @Root
        private static class Csr {
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
            private String acpi;

            @Expose
            @Element(required = false)
            private String cst;

            @Expose
            @Element(required = false)
            private String poa;

            @Expose
            @Element(required = false)
            private String csi;

            @Expose
            @Element(required = false)
            private String rr;

            @Expose
            @Element(required = false)
            private String nl;
        }
    }

    @Override
    public String getRequestIdentifier() {
        return null;
    }

    @Override
    public void print() {
        MQTTUtils.log("[" + Definitions.getResourceName(Definitions.ResourceType.remoteCSE) + "]");
        super.print();
        if (pc.csr == null) return;
        MQTTUtils.log("ty : " + pc.csr.ty);
        MQTTUtils.log("ri : " + pc.csr.ri);
        MQTTUtils.log("rn : " + pc.csr.rn);
        MQTTUtils.log("pi : " + pc.csr.pi);
        MQTTUtils.log("ct : " + pc.csr.ct);
        MQTTUtils.log("lt : " + pc.csr.lt);
        MQTTUtils.log("acpi : " + pc.csr.acpi);
        MQTTUtils.log("cst : " + pc.csr.cst);
        MQTTUtils.log("poa : " + pc.csr.poa);
        MQTTUtils.log("csi : " + pc.csr.csi);
        MQTTUtils.log("rr : " + pc.csr.rr);
        MQTTUtils.log("nl : " + pc.csr.nl);
    }
}
