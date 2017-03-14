package tp.skt.onem2m.binder.mqtt_v1_1.request;


import android.support.annotation.CheckResult;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.common.MQTTConst;

/**
 * remoteCSE request
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(name = "req")
@Namespace(prefix = "m2m", reference = "http://www.onem2m.org/xml/protocols")
@Default(DefaultType.FIELD)
public class remoteCSE extends RequestBase {
    @Element(required = false)
    /** passcode **/
    private String passCode;

    /**
     * remoteCSE constructor
     *
     * @param builder
     */
    private remoteCSE(Builder builder) {
        super(builder);
        this.passCode = builder.passCode;
    }

    /**
     * remoteCSE Builder
     */
    public static class Builder extends RequestBase.Builder {
        /**
         * passCode
         **/
        private String passCode;
        /**
         * CSEtype
         **/
        private String cst;
        /**
         * CSE-ID
         **/
        private String csi;
        /**
         * pointOfAccess
         **/
        private String poa;
        /**
         * requestRechability
         **/
        private String rr;
        /**
         * nodelink
         **/
        private String nl;

        /**
         * Builder constructor
         */
        public Builder(@Definitions.Operation int op) {
            super(op, Definitions.ResourceType.remoteCSE);
            nm = "{" + MQTTConst.RESOURCE_ID + "}";
            csi = "{" + MQTTConst.RESOURCE_ID + "}";
        }

        public Builder passCode(String passCode) {
            this.passCode = passCode;
            return this;
        }

        public Builder cst(String cst) {
            this.cst = cst;
            return this;
        }

        public Builder csi(String csi) {
            this.csi = csi;
            return this;
        }

        public Builder poa(String poa) {
            this.poa = poa;
            return this;
        }

        public Builder rr(String rr) {
            this.rr = rr;
            return this;
        }

        public Builder nl(String nl) {
            this.nl = nl;
            return this;
        }

        public Builder nm(String nm) {
            this.nm = nm;
            return this;
        }

        public Builder dKey(String dKey) {
            this.dKey = dKey;
            return this;
        }

        public Builder uKey(String uKey) {
            this.uKey = uKey;
            return this;
        }

        @Override
        Content getContent() {
            Content content = new Content();
            content.csr = new Content.Csr(cst, csi, poa, rr, nl);
            return content;
        }

        @Override
        String getTo() {
            if (op == Definitions.Operation.Create) {
                return "{" + MQTTConst.CSEBASE_ID + "}";
            } else {
                return "{" + MQTTConst.CSEBASE_ID + "}/" + Definitions.getResourceName(resourceType) + "-{" + MQTTConst.RESOURCE_ID + "}";
            }
        }

        @Override
        String getDefaultResourceName() {
            return null;
        }

        @CheckResult
        public remoteCSE build() {
            remoteCSE build = new remoteCSE(this);
            return build;
        }
    }
}
