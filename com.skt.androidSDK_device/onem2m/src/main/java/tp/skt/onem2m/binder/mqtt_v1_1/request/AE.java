package tp.skt.onem2m.binder.mqtt_v1_1.request;


import android.support.annotation.CheckResult;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.common.MQTTConst;

/**
 * AE request
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(name = "req")
@Namespace(prefix = "m2m", reference = "http://www.onem2m.org/xml/protocols")
@Default(DefaultType.FIELD)
public class AE extends RequestBase {

    /**
     * AE constructor
     *
     * @param builder
     */
    private AE(Builder builder) {
        super(builder);
    }

    /**
     * AE Builder
     */
    public static class Builder extends RequestBase.Builder {
        /**
         * App-ID
         **/
        private String api;
        /**
         * appName
         **/
        private String apn;

        /**
         * Builder constructor
         *
         * @param op
         */
        public Builder(@Definitions.Operation int op) {
            super(op, Definitions.ResourceType.AE);
        }

        public Builder api(String api) {
            this.api = api;
            return this;
        }

        public Builder apn(String apn) {
            this.apn = apn;
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
            content.ae = new Content.Ae(api, apn);
            return content;
        }

        @Override
        String getTo() {
            if (op == Definitions.Operation.Create) {
                return "{" + MQTTConst.CSEBASE_ID + "}/remoteCSE-{" + MQTTConst.RESOURCE_ID + "}";
            } else {
                return "{" + MQTTConst.CSEBASE_ID + "}/remoteCSE-{" + MQTTConst.RESOURCE_ID + "}/" + Definitions.getResourceName(resourceType) + "-{nm}";
            }
        }

        @Override
        String getDefaultResourceName() {
            return null;
        }


        @CheckResult
        public AE build() { // throws Exception {
            if (op != Definitions.Operation.Create) {
                if (nm != null) {
                    this.to = to.replace("{nm}", nm);
                    this.nm = null;
                }
            }
            AE build = new AE(this);
            return build;
        }
    }
}
