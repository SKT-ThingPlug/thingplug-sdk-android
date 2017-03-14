package tp.skt.onem2m.binder.mqtt_v1_1.request;


import android.support.annotation.CheckResult;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.common.MQTTConst;

/**
 * contentInstance request
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(name = "req")
@Namespace(prefix = "m2m", reference = "http://www.onem2m.org/xml/protocols")
@Default(DefaultType.FIELD)
public class contentInstance extends RequestBase {

    /**
     * contentInstance constructor
     *
     * @param builder
     */
    private contentInstance(Builder builder) {
        super(builder);
    }

    /**
     * contentInstance Builder
     */
    public static class Builder extends RequestBase.Builder {
        /**
         * contentInfo
         */
        private String cnf;
        /**
         * content
         */
        private String con;
        /**
         * containerName
         */
        private String containerName;

        /**
         * Builder constructor
         *
         * @param op
         */
        public Builder(@Definitions.Operation int op) {
            super(op, Definitions.ResourceType.contentInstance);
        }


        public Builder cnf(String cnf) {
            this.cnf = cnf;
            return this;
        }

        public Builder con(String con) {
            this.con = con;
            return this;
        }

        public Builder containerName(String containerName) {
            this.containerName = containerName;
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
        String getTo() {
            if (op == Definitions.Operation.Create) {
                return "{" + MQTTConst.CSEBASE_ID + "}/remoteCSE-{" + MQTTConst.RESOURCE_ID + "}/container-{" + MQTTConst.CONTAINER_NAME + "}";
            } else if (op == Definitions.Operation.Retrieve) {
                return "{" + MQTTConst.CSEBASE_ID + "}/remoteCSE-{" + MQTTConst.RESOURCE_ID + "}/container-{" + MQTTConst.CONTAINER_NAME + "}/";
            } else if (op == Definitions.Operation.Delete) {
                return "{" + MQTTConst.CSEBASE_ID + "}/remoteCSE-{" + MQTTConst.RESOURCE_ID + "}/container-{" + MQTTConst.CONTAINER_NAME + "}/contentInstance-";
            }
            return null;
        }

        @Override
        String getDefaultResourceName() {
            return null;
        }

        @Override
        Content getContent() {
            Content content = new Content();
            content.cin = new Content.Cin(cnf, con);
            return content;
        }

        @CheckResult
        public contentInstance build() { // throws Exception {
            if (containerName != null) {
                this.to = to.replace("{" + MQTTConst.CONTAINER_NAME + "}", this.containerName);
                this.containerName = null;
            }
            if (nm != null) {
                this.to += nm;
                this.nm = null;
            }
            contentInstance build = new contentInstance(this);
            return build;
        }
    }
}
