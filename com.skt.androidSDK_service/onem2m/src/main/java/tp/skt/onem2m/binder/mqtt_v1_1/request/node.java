package tp.skt.onem2m.binder.mqtt_v1_1.request;

import android.support.annotation.CheckResult;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.common.MQTTConst;

/**
 * node request
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(name = "req")
@Namespace(prefix = "m2m", reference = "http://www.onem2m.org/xml/protocols")
@Default(DefaultType.FIELD)
public class node extends RequestBase {

    /**
     * node constructor
     *
     * @param builder
     */
    private node(Builder builder) {
        super(builder);
    }

    /**
     * node Builder
     */
    public static class Builder extends RequestBase.Builder {
        /**
         * node id
         **/
        private String ni;
        /**
         * hostCSELink
         **/
        private String hcl;
        /**
         * mga
         **/
        private String mga;

        /**
         * Builder constructor
         *
         * @param op operation
         */
        public Builder(@Definitions.Operation int op) {
            super(op, Definitions.ResourceType.node);
            ni = "{" + MQTTConst.RESOURCE_ID + "}";
        }

        public Builder ni(String ni) {
            this.ni = ni;
            return this;
        }

        public Builder hcl(String hcl) {
            this.hcl = hcl;
            return this;
        }

        public Builder mga(String mga) {
            this.mga = mga;
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
            content.nod = new Content.Nod(ni, hcl, mga);
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
            if (op == Definitions.Operation.Create) {
                return "{" + MQTTConst.RESOURCE_ID + "}";
            }
            return null;
        }

        @CheckResult
        public node build() { // throws Exception {
            node node = new node(this);
            return node;
        }
    }
}
