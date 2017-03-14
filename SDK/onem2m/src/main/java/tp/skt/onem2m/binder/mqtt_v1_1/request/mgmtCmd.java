package tp.skt.onem2m.binder.mqtt_v1_1.request;


import android.support.annotation.CheckResult;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.common.MQTTConst;

/**
 * mgmtCmd request
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(name = "req")
@Namespace(prefix = "m2m", reference = "http://www.onem2m.org/xml/protocols")
@Default(DefaultType.FIELD)
public class mgmtCmd extends RequestBase {

    /**
     * mgmtCmd constructor
     *
     * @param builder
     */
    private mgmtCmd(Builder builder) {
        super(builder);
    }

    /**
     * mgmtCmd Builder
     */
    public static class Builder extends RequestBase.Builder {
        /**
         * cmdType
         **/
        private String cmt;
        /**
         * labels
         **/
        private String lbl;
        /**
         * execEnable
         **/
        private String exe;
        /**
         * execTarget
         **/
        private String ext;
        /**
         * execReqArgs
         **/
        private String exra;

        /**
         * Builder constructor
         *
         * @param op
         */
        public Builder(@Definitions.Operation int op) {
            super(op, Definitions.ResourceType.mgmtCmd);
        }

        public Builder cmt(String cmt) {
            this.cmt = cmt;
            return this;
        }

        public Builder lbl(String lbl) {
            this.lbl = lbl;
            return this;
        }

        public Builder exe(String exe) {
            this.exe = exe;
            return this;
        }

        public Builder ext(String ext) {
            this.ext = ext;
            return this;
        }

        public Builder exra(String exra) {
            this.exra = exra;
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
            content.mgc = new Content.Mgc(cmt, lbl, ext, exe, exra);
            return content;
        }

        @Override
        String getTo() {
            if (op == Definitions.Operation.Create) {
                return "{" + MQTTConst.CSEBASE_ID + "}";
            } else {
                return "{" + MQTTConst.CSEBASE_ID + "}/" + Definitions.getResourceName(resourceType) + "-{nm}";
            }
        }

        @Override
        String getDefaultResourceName() {
            return null;
        }

        @CheckResult
        public mgmtCmd build() {
            if (op != Definitions.Operation.Create) {
                if (nm != null) {
                    this.to = to.replace("{nm}", nm);
                    this.nm = null;
                }
            }
            mgmtCmd build = new mgmtCmd(this);
            return build;
        }
    }
}
