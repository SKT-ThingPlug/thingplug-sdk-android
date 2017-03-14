package tp.skt.onem2m.binder.mqtt_v1_1.request;


import android.support.annotation.CheckResult;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.common.MQTTConst;

import static tp.skt.onem2m.binder.mqtt_v1_1.Converter.checkNotFalse;

/**
 * CSEBase request
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@Root(name = "req")
@Namespace(prefix = "m2m", reference = "http://www.onem2m.org/xml/protocols")
@Default(DefaultType.FIELD)
public class CSEBase extends RequestBase {
    @Element(required = false)

    /**
     * CSEBase constructor
     *
     * @param builder
     */
    private CSEBase(Builder builder) {
        super(builder);
    }

    /**
     * CSEBase Builder
     */
    public static class Builder extends RequestBase.Builder {

        /**
         * CSEBase constructor
         *
         * @param op
         */
        public Builder(@Definitions.Operation int op) {
            super(op, Definitions.ResourceType.CSEBase);
            checkNotFalse(Definitions.Operation.Retrieve == op, "CSEBase op code not Retrieve"); // FIXME
        }

        public Builder nm(String nm) {
            this.nm = nm;
            return this;
        }

        public Builder dKey(String dKey) {
            this.dKey = dKey;
            return this;
        }

        @Override
        Content getContent() {
            return null;
        }

        @Override
        String getTo() {
            return "{" + MQTTConst.CSEBASE_ID + "}";
        }

        @Override
        String getDefaultResourceName() {
            return null;
        }

        @CheckResult
        public CSEBase build() {
            CSEBase build = new CSEBase(this);
            return build;
        }
    }
}
