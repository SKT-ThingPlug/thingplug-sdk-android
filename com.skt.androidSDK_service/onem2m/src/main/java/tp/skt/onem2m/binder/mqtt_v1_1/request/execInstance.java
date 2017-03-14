package tp.skt.onem2m.binder.mqtt_v1_1.request;


import android.support.annotation.CheckResult;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.common.MQTTConst;

import static tp.skt.onem2m.binder.mqtt_v1_1.Converter.checkNotFalse;

/**
 * execInstance request
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(name = "req")
@Namespace(prefix = "m2m", reference = "http://www.onem2m.org/xml/protocols")
@Default(DefaultType.FIELD)
public class execInstance extends RequestBase {

    /**
     * execInstance constructor
     *
     * @param builder
     */
    private execInstance(Builder builder) {
        super(builder);
    }

    /**
     * execInstance Builder
     */
    public static class Builder extends RequestBase.Builder {
        /**
         * execResult
         **/
        private String exr;
        /**
         * execStatus
         **/
        private String exs;
        /**
         * execInstance resourceId
         **/
        private String resourceId;

        /**
         * Builder constructor
         *
         * @param op
         */
        public Builder(@Definitions.Operation int op) {
            super(op, Definitions.ResourceType.execInstance);
            checkNotFalse((Definitions.Operation.Update == op || Definitions.Operation.Retrieve == op), "not supported operation!");
        }

        public Builder exs(String exs) {
            this.exs = exs;
            return this;
        }

        public Builder exr(String exr) {
            this.exr = exr;
            return this;
        }

        public Builder nm(String nm) {
            this.nm = nm;
            return this;
        }

        public Builder resourceId(String resourceId) {
            this.resourceId = resourceId;
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
            content.exin = new Content.Exin(exs, exr);
            return content;
        }

        @Override
        String getTo() {
            if (op == Definitions.Operation.Update || op == Definitions.Operation.Retrieve) {
                return "{" + MQTTConst.CSEBASE_ID + "}/mgmtCmd-{nm}/" + Definitions.getResourceName(resourceType) + "-{ri}";
            }
            return null;
        }

        @Override
        String getDefaultResourceName() {
            return null;
        }

        @CheckResult
        public execInstance build() {
            if (nm != null) {
                this.to = to.replace("{nm}", nm);
                this.nm = null;
            }
            if (ri != null) {
                this.to = to.replace("{ri}", resourceId);
                this.resourceId = null;
            }
            execInstance build = new execInstance(this);
            return build;
        }
    }
}
