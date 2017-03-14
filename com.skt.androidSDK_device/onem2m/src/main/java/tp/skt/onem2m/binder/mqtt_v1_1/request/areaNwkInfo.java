package tp.skt.onem2m.binder.mqtt_v1_1.request;


import android.support.annotation.CheckResult;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.common.MQTTConst;

/**
 * areaNwkInfo request
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(name = "req")
@Namespace(prefix = "m2m", reference = "http://www.onem2m.org/xml/protocols")
@Default(DefaultType.FIELD)
public class areaNwkInfo extends RequestBase {

    /**
     * areaNwkInfo constructor
     *
     * @param builder
     */
    private areaNwkInfo(Builder builder) {
        super(builder);
    }

    /**
     * areaNwkInfo Builder
     */
    public static class Builder extends RequestBase.Builder {
        /**
         * mgmtDefinition
         **/
        private String mgd;
        /**
         * areaNwkType
         **/
        private String ant;
        /**
         * listOfDevices
         **/
        private String ldv;

        /**
         * Builder constructor
         *
         * @param op
         */
        public Builder(@Definitions.Operation int op) {
            super(op, Definitions.ResourceType.mgmtObj);
        }

        public Builder mgd(String mgd) {
            this.mgd = mgd;
            return this;
        }

        public Builder ant(String ant) {
            this.ant = ant;
            return this;
        }

        public Builder ldv(String ldv) {
            this.ldv = ldv;
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
            content.ani = new Content.Ani(mgd, ant, ldv);
            return content;
        }

        @Override
        String getTo() {
            if (op == Definitions.Operation.Create) {
                return "{" + MQTTConst.CSEBASE_ID + "}/node-{" + MQTTConst.RESOURCE_ID + "}";
            } else {
                return "{" + MQTTConst.CSEBASE_ID + "}/node-{" + MQTTConst.RESOURCE_ID + "}/areaNwkInfo-{nm}";
            }
        }

        @Override
        String getDefaultResourceName() {
            return null;
        }

        @CheckResult
        public areaNwkInfo build() { //throws Exception {
            if (op != Definitions.Operation.Create) {
                if (nm != null) {
                    this.to = to.replace("{nm}", nm);
                    this.nm = null;
                }
            }
            areaNwkInfo build = new areaNwkInfo(this);
            return build;
        }
    }
}
