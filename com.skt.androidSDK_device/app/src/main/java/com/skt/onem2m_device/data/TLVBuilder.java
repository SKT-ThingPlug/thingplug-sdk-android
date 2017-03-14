package com.skt.onem2m_device.data;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * TLV builder
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class TLVBuilder {
    private Formatter   formatter = new Formatter();    // data string formatter

    /**
     * constructor
     */
    public TLVBuilder() {
    }

    /**
     * reset the buffer
     */
    public void reset() {
        formatter.flush();
    }

    /**
     * add integer data to the buffer
     * @param type      data type
     * @param length    data length
     * @param value     data value
     * @return TLV builder instance
     */
    public TLVBuilder add(int type, int length, int value) {
        formatter.format("%02x", type);
        formatter.format("%02x", length);
        if (length > 0) {
            switch (length) {
                case 1:     formatter.format("%02x", Utils.swap((byte) value));   break;
                case 2:     formatter.format("%04x", Utils.swap((short) value));  break;
                case 4:     formatter.format("%08x", Utils.swap(value));          break;
            }
        }
        return this;
    }

    /**
     * add string data to the buffer
     * @param type      data type
     * @param length    data length
     * @param value     data value
     * @return TLV builder instance
     */
    public TLVBuilder add(int type, int length, String value) {
        formatter.format("%02x", type);
        formatter.format("%02x", length);
        if (length > 0) {
            formatter.format("%s", value);
        }
        return this;
    }

    /**
     * add sensor data to the buffer
     * @param info    sensor info
     * @return TLV builder instance
     */
    public TLVBuilder addSensorData(SensorInfo info) {
        SensorType type = info.getType();
        float[] values = info.getValues();
        String[] stringValues = info.getStringValues();

        for(int loop1 = 0 ; loop1 < type.getValueNumbers() ; loop1++) {
            // write type
            formatter.format("%02x", type.getValueTLVTypes()[loop1]);

            // write length, value
            switch(type.getValueTLVTypes()[loop1]) {
                case 0x04:      formatter.format("%02x", 2);    formatter.format("%04x", Utils.swap((short) (values[loop1] * 100)));      break;          // battery temperature
                case 0x05:      formatter.format("%02x", 1);    formatter.format("%02x", Utils.swap((byte) values[loop1]));               break;          // battery level

                case 0x11:      formatter.format("%02x", 2);    formatter.format("%04x", Utils.swap((short) (values[loop1] * 100)));      break;          // temperature
                case 0x12:      formatter.format("%02x", 2);    formatter.format("%04x", Utils.swap((short) (values[loop1] * 100)));      break;          // humidity
                case 0x13:      formatter.format("%02x", 2);    formatter.format("%04x", Utils.swap((short) (values[loop1] * 100)));        break;          // noise

                case 0x20:      formatter.format("%02x", 4);    formatter.format("%08x", Utils.swap((int) (values[loop1] * 100000)));     break;          // latitude
                case 0x21:      formatter.format("%02x", 4);    formatter.format("%08x", Utils.swap((int) (values[loop1] * 100000)));     break;          // longitude
                case 0x22:      formatter.format("%02x", 2);    formatter.format("%04x", Utils.swap((short) values[loop1]));              break;          // altitude
                case 0x24:      formatter.format("%02x", 2);    formatter.format("%04x", Utils.swap((short) (values[loop1] * 10)));       break;          // air pressure
                case 0x25:      formatter.format("%02x", 2);    formatter.format("%04x", Utils.swap((short) values[loop1]));              break;          // light
                case 0x27:      formatter.format("%02x", 1);    formatter.format("%02x", Utils.swap((byte) values[loop1]));               break;          // buzzer
                case 0x28:      formatter.format("%02x", 1);    formatter.format("%02x", Utils.swap((byte) values[loop1]));               break;          // led

                case 0x31:      formatter.format("%02x", 2);    formatter.format("%04x", Utils.swap((short) values[loop1]));              break;          // proximity
                case 0x34:      formatter.format("%02x", 1);    formatter.format("%02x", Utils.swap((byte) values[loop1]));               break;          // camera
                case 0x38:      formatter.format("%02x", 8);    formatter.format("%04x", Utils.swap((short) 0));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1] * 100)));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1 + 1] * 100)));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1 + 2] * 100)));  return this;    // accelerometer
                case 0x39:      formatter.format("%02x", 8);    formatter.format("%04x", Utils.swap((short) 0));
                                                                formatter.format("%04x", Utils.swap((short) values[loop1]));
                                                                formatter.format("%04x", Utils.swap((short) values[loop1 + 1]));
                                                                formatter.format("%04x", Utils.swap((short) values[loop1 + 2]));          return this;    // orientation
                case 0x3A:      formatter.format("%02x", 8);    formatter.format("%04x", Utils.swap((short) 0));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1] * 100)));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1 + 1] * 100)));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1 + 2] * 100)));  return this;    // gravity
                case 0x3B:      formatter.format("%02x", 8);    formatter.format("%04x", Utils.swap((short) 0));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1] * 100)));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1 + 1] * 100)));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1 + 2] * 100)));  return this;    // gyroscope
                case 0x3C:      formatter.format("%02x", 8);    formatter.format("%04x", Utils.swap((short) 0));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1] * 100)));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1 + 1] * 100)));
                                                                formatter.format("%04x", Utils.swap((short) (values[loop1 + 2] * 100)));  return this;    // magnetic field
                case 0x3D:      formatter.format("%02x", 1);    formatter.format("%02x", Utils.swap((byte) values[loop1]));               break;          // step detector
                case 0x3E:      formatter.format("%02x", 2);    formatter.format("%04x", Utils.swap((short) values[loop1]));              break;          // step count
            }
        }

        return this;
    }

    /**
     * build to string
     * @return string value of the buffer
     */
    public String build() {
        return formatter.toString();
    }

    /**
     * build TLV string of the sensor activation command
     * @param info     sensor info
     * @param value    command value
     * @return TLV command string
     */
    public static String buildSensorActivateCommand(SensorInfo info, boolean value) {
        return buildSensorControlCommand(info, value ? 1 : 0);
    }

    /**
     * build TLV string of the sensor control command
     * @param info     sensor info
     * @param value    command value
     * @return TLV command string
     */
    public static String buildSensorControlCommand(SensorInfo info, int value) {
        Formatter formatter = new Formatter();
        formatter.format("%02x", info.getType().getValueTLVTypes()[0]);
        formatter.format("%02x", 1);
        formatter.format("%02x", value);
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * parse TLV string of the sensor data
     * @param tlvString    TLV string
     * @return parsed info
     */
    public static List<SensorInfo> parseSensorData(String tlvString) {
        List<SensorInfo> sensorInfos = new ArrayList<>();

        float[] intValues = new float[SensorType.MAX_VALUE_NUMBERS];
        String[] stringValues = new String[1];

        for (int loop1 = 0 ; loop1 < tlvString.length() ; ) {
            // read type
            int type = Integer.parseInt(tlvString.substring(loop1, loop1 + 2), 16);
            loop1 += 2;

            // read length
            int length = Integer.parseInt(tlvString.substring(loop1, loop1 + 2), 16);
            loop1 += 2;

            // read value
            String value = "0";
            if (length > 0) {
                value = tlvString.substring(loop1, loop1 + (length * 2));
                loop1 += (length * 2);
            }

            SensorType sensorType = SensorType.NONE;
            int valueBeginIndex = 0;
            int valueNumbers = 1;

            switch (type) {
                case 0x04:  sensorType = SensorType.BATTERY;                intValues[0] = Utils.swap((short) Long.parseLong(value, 16)) / 100f;                              break;  // battery temperature
                case 0x05:  sensorType = SensorType.BATTERY;                intValues[0] = Utils.swap((byte) Long.parseLong(value, 16));              valueBeginIndex = 1;    break;  // battery level

                case 0x11:  sensorType = SensorType.AMBIENT_TEMPERATURE;    intValues[0] = Utils.swap((short) Long.parseLong(value, 16)) / 100f;                              break;  // temperature
                case 0x12:  sensorType = SensorType.RELATIVE_HUMIDITY;      intValues[0] = Utils.swap((short) Long.parseLong(value, 16)) / 100f;                              break;  // humidity
                case 0x13:  sensorType = SensorType.NOISE;                  intValues[0] = Utils.swap((short) Long.parseLong(value, 16)) / 100f;                              break;  // noise

                case 0x20:  sensorType = SensorType.GPS;                    intValues[0] = Utils.swap((int) Long.parseLong(value, 16)) / 100000f;                             break;  // latitude
                case 0x21:  sensorType = SensorType.GPS;                    intValues[0] = Utils.swap((int) Long.parseLong(value, 16)) / 100000f;     valueBeginIndex = 1;    break;  // longitude
                case 0x22:  sensorType = SensorType.GPS;                    intValues[0] = Utils.swap((short) Long.parseLong(value, 16));             valueBeginIndex = 2;    break;  // altitude
                case 0x24:  sensorType = SensorType.PRESSURE;               intValues[0] = Utils.swap((short) Long.parseLong(value, 16)) / 10f;                               break;  // air pressure
                case 0x25:  sensorType = SensorType.LIGHT;                  intValues[0] = Utils.swap((short) Long.parseLong(value, 16));                                     break;  // light
                case 0x27:  sensorType = SensorType.BUZZER;                 intValues[0] = Utils.swap((byte) Long.parseLong(value, 16));                                      break;  // buzzer
                case 0x28:  sensorType = SensorType.LED;                    intValues[0] = Utils.swap((byte) Long.parseLong(value, 16));                                      break;  // led

                case 0x31:  sensorType = SensorType.PROXIMITY;              intValues[0] = Utils.swap((short) Long.parseLong(value, 16));                                     break;  // proximity
                case 0x34:  sensorType = SensorType.CAMERA;                 intValues[0] = Utils.swap((byte) Long.parseLong(value, 16));                                      break;  // camera
                case 0x38:  sensorType = SensorType.ACCELEROMETER;          intValues[0] = Utils.swap((short) Long.parseLong(value.substring(4, 8), 16)) / 100f;
                                                                            intValues[1] = Utils.swap((short) Long.parseLong(value.substring(8, 12), 16)) / 100f;
                                                                            intValues[2] = Utils.swap((short) Long.parseLong(value.substring(12, 16), 16)) / 100f;    valueNumbers = 3;   break;  // accelerometer
                case 0x39:  sensorType = SensorType.ORIENTATION;            intValues[0] = Utils.swap((short) Long.parseLong(value.substring(4, 8), 16));
                                                                            intValues[1] = Utils.swap((short) Long.parseLong(value.substring(8, 12), 16));
                                                                            intValues[2] = Utils.swap((short) Long.parseLong(value.substring(12, 16), 16));           valueNumbers = 3;   break;  // orientation
                case 0x3A:  sensorType = SensorType.GRAVITY;                intValues[0] = Utils.swap((short) Long.parseLong(value.substring(4, 8), 16)) / 100f;
                                                                            intValues[1] = Utils.swap((short) Long.parseLong(value.substring(8, 12), 16)) / 100f;
                                                                            intValues[2] = Utils.swap((short) Long.parseLong(value.substring(12, 16), 16)) / 100f;    valueNumbers = 3;   break;  // gravity
                case 0x3B:  sensorType = SensorType.GYROSCOPE;              intValues[0] = Utils.swap((short) Long.parseLong(value.substring(4, 8), 16)) / 100f;
                                                                            intValues[1] = Utils.swap((short) Long.parseLong(value.substring(8, 12), 16)) / 100f;
                                                                            intValues[2] = Utils.swap((short) Long.parseLong(value.substring(12, 16), 16)) / 100f;    valueNumbers = 3;   break;  // gyroscope
                case 0x3C:  sensorType = SensorType.MAGNETIC_FIELD;         intValues[0] = Utils.swap((short) Long.parseLong(value.substring(4, 8), 16)) / 100f;
                                                                            intValues[1] = Utils.swap((short) Long.parseLong(value.substring(8, 12), 16)) / 100f;
                                                                            intValues[2] = Utils.swap((short) Long.parseLong(value.substring(12, 16), 16)) / 100f;    valueNumbers = 3;   break;  // magnetic field
                case 0x3D:  sensorType = SensorType.STEP_DETECTOR;          intValues[0] = Utils.swap((byte) Long.parseLong(value, 16));                                      break;  // step detector
                case 0x3E:  sensorType = SensorType.STEP_COUNTER;           intValues[0] = Utils.swap((short) Long.parseLong(value, 16));                                     break;  // step count
            }

            if (sensorType != SensorType.NONE) {
                SensorInfo sensorInfo = null;
                for(SensorInfo info : sensorInfos) {
                    if (info.getType() == sensorType) {
                        sensorInfo = info;
                    }
                }
                if (sensorInfo == null) {
                    sensorInfo = new SensorInfo(sensorType);
                    sensorInfos.add(sensorInfo);
                }

                for (int loop2 = 0 ; loop2 < valueNumbers ; loop2++) {
                    switch (sensorType.getValueType()) {
                        case NUMBER:
                            sensorInfo.setValue(valueBeginIndex + loop2, intValues[loop2]);
                            break;
                        case STRING:
                            sensorInfo.setStringValue(valueBeginIndex + loop2, stringValues[loop2]);
                            break;
                    }
                }
            }
        }

        return sensorInfos;
    }

    /**
     * parse TLV string of the sensor command
     * @param tlvString    TLV string
     * @return parsed info
     */
    public static SensorInfo parseSensorCommand(String tlvString) {
        if(TextUtils.isEmpty(tlvString) == true) {
            return null;
        }

        int readPos = 0;

        // read type
        int type = Integer.parseInt(tlvString.substring(readPos, readPos + 2), 16);
        readPos += 2;

        // read length
        int length = Integer.parseInt(tlvString.substring(readPos, readPos + 2), 16);
        readPos += 2;

        // read value
        int value = 0;
        if (length > 0) {
            value = (int) Long.parseLong(tlvString.substring(readPos, readPos + (length * 2)), 16);
            readPos += (length * 2);
        }

        SensorType sensorType = SensorType.NONE;
        switch (type) {
            case 0x04:  sensorType = SensorType.BATTERY;                break;  // battery temperature
            case 0x05:  sensorType = SensorType.BATTERY;                break;  // battery level

            case 0x11:  sensorType = SensorType.AMBIENT_TEMPERATURE;    break;  // temperature
            case 0x12:  sensorType = SensorType.RELATIVE_HUMIDITY;      break;  // humidity
            case 0x13:  sensorType = SensorType.NOISE;                  break;  // noise

            case 0x20:  sensorType = SensorType.GPS;                    break;  // latitude
            case 0x21:  sensorType = SensorType.GPS;                    break;  // longitude
            case 0x22:  sensorType = SensorType.GPS;                    break;  // altitude
            case 0x24:  sensorType = SensorType.PRESSURE;               break;  // air pressure
            case 0x25:  sensorType = SensorType.LIGHT;                  break;  // light
            case 0x27:  sensorType = SensorType.BUZZER;                 break;  // buzzer
            case 0x28:  sensorType = SensorType.LED;                    break;  // led

            case 0x31:  sensorType = SensorType.PROXIMITY;              break;  // proximity
            case 0x34:  sensorType = SensorType.CAMERA;                 break;  // camera
            case 0x38:  sensorType = SensorType.ACCELEROMETER;          break;  // accelerometer
            case 0x39:  sensorType = SensorType.ORIENTATION;            break;  // orientation
            case 0x3A:  sensorType = SensorType.GRAVITY;                break;  // gravity
            case 0x3B:  sensorType = SensorType.GYROSCOPE;              break;  // gyroscope
            case 0x3C:  sensorType = SensorType.MAGNETIC_FIELD;         break;  // magnetic field
            case 0x3D:  sensorType = SensorType.STEP_DETECTOR;          break;  // step detector
            case 0x3E:  sensorType = SensorType.STEP_COUNTER;           break;  // step count
        }

        SensorInfo sensorInfo = null;
        if (sensorType != SensorType.NONE) {
            sensorInfo = new SensorInfo(sensorType);
            if (sensorType.getCategory() != SensorType.Category.ACTUATOR) {
                sensorInfo.setActivated(value > 0);
            }
            else {
                sensorInfo.setValue(0, value);
            }
        }
        return sensorInfo;
    }
}
