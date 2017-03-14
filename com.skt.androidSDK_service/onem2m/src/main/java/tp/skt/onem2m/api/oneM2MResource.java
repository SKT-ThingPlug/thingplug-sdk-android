package tp.skt.onem2m.api;

/**
 * oneM2M resource 추상 클래스
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public abstract class oneM2MResource {

    /**
     * get request identifier
     *
     * @return ri or rqi
     */
    abstract public String getRequestIdentifier();
}
