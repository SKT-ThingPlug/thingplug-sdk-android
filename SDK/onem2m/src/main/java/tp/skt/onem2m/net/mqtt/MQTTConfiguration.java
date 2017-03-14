package tp.skt.onem2m.net.mqtt;

/**
 * values for MQTT message
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class MQTTConfiguration {

    /**
     * CSEBase ID
     **/
    private String CSEBaseID;
    /**
     * Resource ID
     **/
    private String resourceID;
    /**
     * CSEBase
     **/
    private String CSEBase;
    /**
     * Client ID
     **/
    private String clientID;

    /**
     * MQTTConfiguration constructor
     *
     * @param CSEBase    CSEBase
     * @param CSEBaseID  CSEBase ID
     * @param resourceID Resource ID
     * @param clientID   Client ID
     */
    public MQTTConfiguration(String CSEBase, String CSEBaseID, String resourceID, String clientID) {
        this.CSEBase = CSEBase;
        this.CSEBaseID = CSEBaseID;
        this.resourceID = resourceID;
        this.clientID = clientID;
    }

    /**
     * get CSEBase
     *
     * @return CSEBase
     */
    public String getCSEBase() {
        return CSEBase;
    }

    /**
     * get CSEBase ID
     *
     * @return CSEBase ID
     */
    public String getCSEBaseID() {
        return CSEBaseID;
    }

    /**
     * get Resource ID
     *
     * @return Resource ID
     */
    public String getResourceID() {
        return resourceID;
    }

    /**
     * get Client ID
     *
     * @return Client ID
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * set Resource ID
     *
     * @param resourceID
     */
    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    /**
     * set Client ID
     *
     * @param clientID
     */
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    /**
     * set CSEBase
     *
     * @param CSEBase
     */
    public void setCSEBase(String CSEBase) {
        this.CSEBase = CSEBase;
    }
}
