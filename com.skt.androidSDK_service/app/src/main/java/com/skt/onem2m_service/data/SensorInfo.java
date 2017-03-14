package com.skt.onem2m_service.data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * sensor info
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class SensorInfo {
    private SensorType  type;           // sensor type
    private float[]     values;         // sensor values
    private String[]    stringValues;   // sensor string values
    private boolean     enable;         // sensor enable or disable
    private boolean     activated;      // sensor activated or deactivated

    private boolean     suspend;        // sensor suspending state
    private boolean     controlling;    // sensor controlling state

    /**
     * constructor
     * @param type     type
     */
    public SensorInfo(SensorType type){
        this.type = type;

        switch (type.getValueType()) {
            case NUMBER:
                values = new float[type.getValueNumbers()];
                break;
            case STRING:
                stringValues = new String[type.getValueNumbers()];
                for(int loop1 = 0 ; loop1 < stringValues.length ; loop1++) {
                    stringValues[loop1] = new String();
                }
                break;
        }
        enable = true;
        activated = true;
        suspend = false;
        controlling = false;
    }

    /**
     * get sensor type
     * @return type
     */
    public SensorType getType() { return type; }

    /**
     * get sensor values
     * @return values
     */
    public float[] getValues() { return values; }

    /**
     * get sensor string values
     * @return string values
     */
    public String[] getStringValues() { return stringValues; }

    /**
     * set sensor values
     * @param values    values
     */
    public void setValues(float[] values) {
        if (type.getValueType() != SensorType.ValueType.NUMBER) { return; }

        for(int loop1 = 0 ; loop1 < Math.min(values.length, type.getValueNumbers()) ; loop1++) {
            try {
                BigDecimal bd = BigDecimal.valueOf(values[loop1]);
                this.values[loop1] = bd.setScale(type.getValueDecimalPlaces()[loop1], RoundingMode.HALF_UP).floatValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * set sensor string values
     * @param values    string values
     */
    public void setStringValues(String[] values) {
        if (type.getValueType() != SensorType.ValueType.STRING) { return; }

        for(int loop1 = 0 ; loop1 < Math.min(values.length, type.getValueNumbers()) ; loop1++) {
            this.stringValues[loop1] = values[loop1];
        }
    }

    /**
     * set sensor value
     * @param index    value index
     * @param value    value
     */
    public void setValue(int index, float value) {
        if (type.getValueType() != SensorType.ValueType.NUMBER) { return; }

        try {
            BigDecimal bd = BigDecimal.valueOf(value);
            this.values[index] = bd.setScale(type.getValueDecimalPlaces()[index], RoundingMode.HALF_UP).floatValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set sensor string value
     * @param index    value index
     * @param value    string value
     */
    public void setStringValue(int index, String value) {
        if (type.getValueType() != SensorType.ValueType.STRING) { return; }

        this.stringValues[index] = value;
    }

    /**
     * get sensor enable or disable
     * @return enable or disable
     */
    public boolean isEnable() { return enable; }

    /**
     * set sensor enable or disable
     * @param enable    enable or disable
     */
    public void setEnable(boolean enable) { this.enable = enable; setActivated(enable); }

    /**
     * get sensor activated or deactivated
     * @return activated or deactivated
     */
    public boolean isActivated() { return activated; }

    /**
     * set sensor activated or deactivated
     * @param activated    activated or deactivated
     */
    public void setActivated(boolean activated) { this.activated = activated; }

    /**
     * get sensor suspending state
     * @return suspending state
     */
    public boolean isSuspend() { return suspend; }

    /**
     * set sensor suspending state
     * @param suspend    suspending state
     */
    public void setSuspend(boolean suspend) { this.suspend = suspend; setControlling(suspend); }

    /**
     * get sensor controlling state
     * @return controlling state
     */
    public boolean isControlling() { return controlling; }

    /**
     * set sensor controlling state
     * @param controlling    controlling state
     */
    public void setControlling(boolean controlling) { this.controlling = controlling; }

    /**
     * get sensor value string
     * @return value string
     */
    public String toString() {
        String strValue = "";
        for(int loop1 = 0 ; loop1 < type.getValueNumbers() ; loop1++) {
            switch (type.getValueType()) {
                case NUMBER:
                    strValue += type.getValueInfos()[loop1][0] + " : " + (type.getValueDecimalPlaces()[loop1] > 0 ? values[loop1] : Integer.toString((int) values[loop1])) + type.getValueInfos()[loop1][1];
                    break;
                case STRING:
                    strValue += type.getValueInfos()[loop1][0] + " : " + stringValues[loop1] + type.getValueInfos()[loop1][1];
                    break;
            }
            if(loop1 != type.getValueNumbers() - 1) {
                strValue += "\n";
            }
        }
        return strValue;
    }
}
