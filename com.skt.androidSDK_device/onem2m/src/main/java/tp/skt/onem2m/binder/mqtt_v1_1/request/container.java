package tp.skt.onem2m.binder.mqtt_v1_1.request;


import android.support.annotation.CheckResult;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.common.MQTTConst;

/**
 * container request
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(name = "req")
@Namespace(prefix = "m2m", reference = "http://www.onem2m.org/xml/protocols")
@Default(DefaultType.FIELD)
public class container extends RequestBase {

    /**
     * container constructor
     *
     * @param builder
     */
    public container(RequestBase.Builder builder) {
        super(builder);
    }

    /**
     * container Builder
     */
    public static class Builder extends RequestBase.Builder {
        /**
         * labels
         **/
        private String lbl;

        /**
         * Builder constructor
         *
         * @param op
         */
        public Builder(@Definitions.Operation int op) {
            super(op, Definitions.ResourceType.container);
        }

        public Builder lbl(String lbl) {
            this.lbl = lbl;
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
            content.cnt = new Content.Cnt(lbl);
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
        public container build() { // throws Exception {
            if (op != Definitions.Operation.Create) {
                if (nm != null) {
                    this.to = to.replace("{nm}", nm);
                    this.nm = null;
                }
            }
            container build = new container(this);
            return build;
        }
    }
}
