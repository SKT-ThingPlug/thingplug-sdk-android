package tp.skt.onem2m.binder.mqtt_v1_1.request;


import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;

import tp.skt.onem2m.api.oneM2MResource;
import tp.skt.onem2m.binder.mqtt_v1_1.Converter;
import tp.skt.onem2m.binder.mqtt_v1_1.Definitions;
import tp.skt.onem2m.common.MQTTConst;

/**
 * request base
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
@NamespaceList({
        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance")
})
public class RequestBase extends oneM2MResource {

    @Expose
    @Element
    protected final String cty = "application/vnd.onem2m-prsp+xml";

    @Attribute(name = "xsi:schemaLocation")
    protected String schemaLocation = "http://www.onem2m.org/xml/protocols CDT-requestPrimitive-v1_0_0.xsd";

    @Expose
    @Element
    protected int op;

    @Expose
    @Element
    protected String to;

    @Expose
    @Element
    protected String fr;

    @Expose
    @Element
    protected String ri;

    @Expose
    @Element(required = false)
    protected String ty;

    @Expose
    @Element(required = false)
    protected String nm;

    @Expose
    @Element(required = false)
    protected String dKey;

    @Expose
    @Element(required = false)
    protected String uKey;

    @Expose
    @Element(required = false)
    private Content pc;

    /**
     * @param builder
     */
    public RequestBase(Builder builder) {
        this.op = builder.op;
        this.to = builder.to;
        this.fr = builder.fr;
        this.ty = (builder.ty > 0 ? String.valueOf(builder.ty) : null);
//        this.ri = builder.ri;
        this.ri = builder.ri;
        this.nm = builder.nm;
        this.dKey = builder.dKey;
        this.uKey = builder.uKey;
        if (this.op == Definitions.Operation.Create || this.op == Definitions.Operation.Update) {
            this.pc = builder.getContent();
        }
    }

    @Override
    public String getRequestIdentifier() {
        return ri;
    }

    /**
     *
     *
     */
    protected static class Content {
        @Expose
        @Element(required = false)
        protected Lcp lcp;
        @Expose
        @Element(required = false)
        protected Cnt cnt;
        @Expose
        @Element(required = false)
        protected Mgc mgc;
        @Expose
        @Element(required = false)
        protected Nod nod;
        @Expose
        @Element(required = false)
        protected Csr csr;
        @Expose
        @Element(required = false)
        protected Fwr fwr;
        @Expose
        @Element(required = false)
        protected Swr swr;
        @Expose
        @Element(required = false)
        protected Dvi dvi;
        @Expose
        @Element(required = false)
        protected Bat bat;
        @Expose
        @Element(required = false)
        protected Mem mem;
        @Expose
        @Element(required = false)
        protected Rbt rbt;
        @Expose
        @Element(required = false)
        protected Ani ani;
        @Expose
        @Element(required = false)
        protected Ae ae;
        @Expose
        @Element(required = false)
        protected Cin cin;
        @Expose
        @Element(required = false)
        protected Exin exin;

        protected static class Exin {
            @Expose
            @Element(required = false)
            private String exs;
            @Expose
            @Element(required = false)
            private String exr;

            protected Exin(String exs, String exr) {
                this.exs = exs;
                this.exr = exr;
            }

        }

        protected static class Cin {
            @Expose
            @Element(required = false)
            private String cnf;
            @Expose
            @Element(required = false)
            private String con;

            protected Cin(String cnf, String con) {
                this.cnf = cnf;
                this.con = con;
            }
        }

        protected static class Ae {
            @Expose
            @Element(required = false)
            private String api;
            @Expose
            @Element(required = false)
            private String apn;

            protected Ae(String api, String apn) {
                this.api = api;
                this.apn = apn;

            }
        }

        protected static class Ani {
            @Expose
            @Element(required = false)
            private String mgd;
            @Expose
            @Element(required = false)
            private String ant;
            @Expose
            @Element(required = false)
            private String ldv;

            protected Ani(String mgd, String ant, String ldv) {
                this.mgd = mgd;
                this.ant = ant;
                this.ldv = ldv;
            }
        }

        protected static class Rbt {
            @Expose
            @Element(required = false)
            private String mgd;
            @Expose
            @Element(required = false)
            private String rbo;
            @Expose
            @Element(required = false)
            private String far;

            protected Rbt(String mgd, String rbo, String far) {
                this.mgd = mgd;
                this.rbo = rbo;
                this.far = far;
            }
        }

        protected static class Mem {
            @Expose
            @Element(required = false)
            private String mgd;
            @Expose
            @Element(required = false)
            private String mma;
            @Expose
            @Element(required = false)
            private String mmt;

            protected Mem(String mgd, String mma, String mmt) {
                this.mgd = mgd;
                this.mma = mma;
                this.mmt = mmt;
            }
        }

        protected static class Bat {
            @Expose
            @Element(required = false)
            private String mgd;
            @Expose
            @Element(required = false)
            private String dc;
            @Expose
            @Element(required = false)
            private String bts;
            @Expose
            @Element(required = false)
            private String btl;

            protected Bat(String mgd, String dc, String bts, String btl) {
                this.mgd = mgd;
                this.dc = dc;
                this.bts = bts;
                this.btl = btl;
            }
        }

        protected static class Dvi {
            @Expose
            @Element(required = false)
            private String mgd;
            @Expose
            @Element(required = false)
            private String dlb;
            @Expose
            @Element(required = false)
            private String man;
            @Expose
            @Element(required = false)
            private String mod;
            @Expose
            @Element(required = false)
            private String dty;
            @Expose
            @Element(required = false)
            private String fwv;
            @Expose
            @Element(required = false)
            private String swv;
            @Expose
            @Element(required = false)
            private String hwv;

            protected Dvi(String mgd, String dlb, String man, String mod, String dty, String fwv, String swv, String hwv) {
                this.mgd = mgd;
                this.dlb = dlb;
                this.man = man;
                this.mod = mod;
                this.dty = dty;
                this.fwv = fwv;
                this.swv = swv;
                this.hwv = hwv;
            }
        }


        protected static class Swr {
            @Expose
            @Element(required = false)
            private String mgd;
            @Expose
            @Element(required = false)
            private String vr;
            @Expose
            @Element(required = false)
            private String nam;
            @Expose
            @Element(required = false)
            private String url;
            @Expose
            @Element(required = false)
            private String in;
            @Expose
            @Element(required = false)
            private String un;

            protected Swr(String mgd, String vr, String nam, String url, String in, String un) {
                this.mgd = mgd;
                this.vr = vr;
                this.nam = nam;
                this.url = url;
                this.in = in;
                this.un = un;
            }
        }

        protected static class Fwr {
            @Expose
            @Element(required = false)
            private String mgd;
            @Expose
            @Element(required = false)
            private String vr;
            @Expose
            @Element(required = false)
            private String nam;
            @Expose
            @Element(required = false)
            private String url;
            @Expose
            @Element(required = false)
            private String ud;

            protected Fwr(String mgd, String vr, String nam, String url, String ud) {
                this.mgd = mgd;
                this.vr = vr;
                this.nam = nam;
                this.url = url;
                this.ud = ud;
            }
        }

        protected static class Csr {
            @Expose
            @Element(required = false)
            private final String cst;

            @Expose
            @Element(required = false)
            private final String csi;

            @Expose
            @Element(required = false)
            private final String rr;

            @Expose
            @Element(required = false)
            private final String poa;

            @Expose
            @Element(required = false)
            private final String nl;

            protected Csr(String cst, String csi, String poa, String rr, String nl) {
                this.cst = cst;
                this.csi = csi;
                this.poa = poa;
                this.rr = rr;
                this.nl = nl;
            }
        }

        protected static class Nod {
            @Expose
            @Element(required = false)
            private final String ni;

            @Expose
            @Element(required = false)
            private final String hcl;

            @Expose
            @Element(required = false)
            private final String mga;

            public Nod(String ni, String hcl, String mga) {
                this.ni = ni;
                this.hcl = hcl;
                this.mga = mga;
            }
        }

        protected static class Mgc {
            @Expose
            @Element(required = false)
            private final String cmt;
            @Expose
            @Element(required = false)
            private final String lbl;
            @Expose
            @Element(required = false)
            private final String ext;
            @Expose
            @Element(required = false)
            private final String exe;
            @Expose
            @Element(required = false)
            private final String exra;

            public Mgc(String cmt, String lbl, String ext, String exe, String exra) {
                this.cmt = cmt;
                this.lbl = lbl;
                this.ext = ext;
                this.exe = exe;
                this.exra = exra;
            }
        }

        protected static class Cnt {
            @Expose
            @Element(required = false)
            private final String lbl;

            public Cnt(String lbl) {
                this.lbl = lbl;
            }
        }

        protected static class Lcp {
            @Expose
            @Element(required = false)
            private final String los;
            @Expose
            @Element(required = false)
            private final String lbl;

            protected Lcp(String los, String lbl) {
                this.los = los;
                this.lbl = lbl;
            }
        }
    }

    protected static abstract class Builder {
        protected final int resourceType;
        protected int op;
        protected int ty;
        protected String to;
        protected String fr;
        protected String ri;
        protected String nm;
        protected String dKey;
        protected String uKey;

        public Builder(int op, int resourceType) {
            this.resourceType = resourceType;

            this.op = op;
            if (op == Definitions.Operation.Create) {
                ty = resourceType;
            }

            to = getTo();
            nm = getDefaultResourceName();

            ri = String.valueOf(Converter.getRI());
            fr = "{" + MQTTConst.RESOURCE_ID + "}";
        }

        abstract Content getContent();

        abstract String getTo();

        abstract String getDefaultResourceName();
    }
}
