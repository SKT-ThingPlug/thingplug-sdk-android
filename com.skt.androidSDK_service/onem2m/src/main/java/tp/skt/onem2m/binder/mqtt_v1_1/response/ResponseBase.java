package tp.skt.onem2m.binder.mqtt_v1_1.response;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;

import tp.skt.onem2m.api.oneM2MResource;
import tp.skt.onem2m.net.mqtt.MQTTUtils;

/**
 * response base
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@NamespaceList({
        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance")
})
public abstract class ResponseBase extends oneM2MResource {

    @Attribute(name = "schemaLocation")
    @Namespace(prefix = "xsi")
    protected String schemaLocation;

    @Expose
    @Element
    public String cty;

    @Expose
    @Element
    public String ri;

    @Expose
    @Element
    public String rsc;

    @Expose
    @Element(required = false)
    public String RSM;

    /**
     * print response data
     */
    public void print() {
        MQTTUtils.log("ri : " + ri);
        MQTTUtils.log("rsc : " + rsc);
        MQTTUtils.log("RSM : " + RSM);
    }
}
