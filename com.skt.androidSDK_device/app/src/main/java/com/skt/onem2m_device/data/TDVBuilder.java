package com.skt.onem2m_device.data;

import android.text.TextUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * TDV builder
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class TDVBuilder {
    /**
     * Value type definition
     */
    public enum ValueType {
        NONE(0x00, 0),
        BOOL(0x01, 1),
        CHAR(0x02, 1),
        UCHAR(0x03, 1),
        SHORT(0x04, 2),
        USHORT(0x05, 2),
        INT(0x06, 4),
        UINT(0x07, 4),
        LONG(0x08, 8),
        ULONG(0x09, 8),
        FLOAT(0x0A, 4),
        DOUBLE(0x0B, 8),
        MAC_ADDRESS(0x0C, 6),
        TIME(0x0D, 8),
        CHAR12(0x0E, 12),
        CHAR2(0x10, 2),
        CHAR3(0x11, 3),
        CHAR4(0x12, 4),
        CHAR8(0x13, 8),
        CHAR16(0x14, 16),
        CHAR32(0x15, 32),
        CHAR64(0x16, 64),
        CHAR128(0x17, 128);

        private int     code;       // type code
        private int     dataLength; // data length

        /**
         * constructor
         * @param code          type code
         * @param dataLength    data length
         */
        ValueType(int code, int dataLength) {
            this.code = code;
            this.dataLength = dataLength;
        }

        /**
         * get type code
         * @return type code
         */
        public int getCode() {
            return code;
        }

        /**
         * get data length
         * @return data length
         */
        public int getDataLength() {
            return dataLength;
        }
    }

    private Formatter   formatter = new Formatter();    // data string formatter

    /**
     * constructor
     */
    public TDVBuilder() {
    }

    /**
     * reset the buffer
     */
    public void reset() {
        formatter.flush();
    }

    /**
     * add integer data to the buffer
     * @param type         data type
     * @param valueType    data value type
     * @param value        data value
     * @return TDV builder instance
     */
    public TDVBuilder add(int type, ValueType valueType, int value) {
        if (valueType.ordinal() < ValueType.MAC_ADDRESS.ordinal()) {
            return this;
        }

        formatter.format("%02x", type);
        formatter.format("%02x", valueType.getCode());
        if (valueType.getDataLength() > 0) {
            formatter.format("%0" + (valueType.getDataLength() * 2) + "x", value);
        }
        return this;
    }

    /**
     * add string data to the buffer
     * @param type         data type
     * @param valueType    data value type
     * @param value        data value
     * @return TDV builder instance
     */
    public TDVBuilder add(int type, ValueType valueType, String value) {
        if (valueType.ordinal() >= ValueType.CHAR12.ordinal()) {
            return this;
        }

        formatter.format("%02x", type);
        formatter.format("%02x", valueType.getCode());
        if (valueType.getDataLength() > 0) {
            formatter.format("%s", value);
        }
        return this;
    }

    /**
     * add sensor data to the buffer
     * @param info    sensor info
     * @return TDV builder instance
     */
    public TDVBuilder addSensorData(SensorInfo info) {
        SensorType type = info.getType();
        float[] values = info.getValues();
        String[] stringValues = info.getStringValues();

        for (int loop1 = 0 ; loop1 < type.getValueNumbers() ; loop1++) {
            // write type
            formatter.format("%02x", type.getValueTDVTypes()[loop1]);

            // write valueType, value
            switch (type.getValueTDVTypes()[loop1]) {
                case 0x04:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // battery temperature
                case 0x05:      formatter.format("%02x", ValueType.UCHAR.getCode());    formatter.format("%02x", (int) values[loop1]);                  break;          // battery level

                case 0x11:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // temperature
                case 0x12:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // humidity
                case 0x13:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // noise

                case 0x20:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // latitude
                case 0x21:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // longitude
                case 0x22:      formatter.format("%02x", ValueType.SHORT.getCode());    formatter.format("%04x", (int) values[loop1]);                  break;          // altitude
                case 0x24:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // air pressure
                case 0x25:      formatter.format("%02x", ValueType.USHORT.getCode());   formatter.format("%04x", (int) values[loop1]);                  break;          // light
                case 0x27:      formatter.format("%02x", ValueType.UCHAR.getCode());    formatter.format("%02x", (int) values[loop1]);                  break;          // buzzer
                case 0x28:      formatter.format("%02x", ValueType.CHAR3.getCode());    formatter.format("%06x", (int) values[loop1]);                  break;          // led

                case 0x31:      formatter.format("%02x", ValueType.USHORT.getCode());   formatter.format("%04x", (int) values[loop1]);                  break;          // proximity
                case 0x32:      formatter.format("%02x", ValueType.BOOL.getCode());     formatter.format("%02x", (int) values[loop1]);                  break;          // step detector
                case 0x33:      formatter.format("%02x", ValueType.USHORT.getCode());   formatter.format("%04x", (int) values[loop1]);                  break;          // step count
                case 0x34:      formatter.format("%02x", ValueType.BOOL.getCode());     formatter.format("%02x", 0);                                    break;          // camera

                case 0x41:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // accelerometer-X
                case 0x42:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // accelerometer-Y
                case 0x43:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // accelerometer-Z
                case 0x44:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // orientation-X
                case 0x45:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // orientation-Y
                case 0x46:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // orientation-Z
                case 0x47:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // gravity-X
                case 0x48:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // gravity-Y
                case 0x49:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // gravity-Z
                case 0x4A:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // gyroscope-X
                case 0x4B:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // gyroscope-Y
                case 0x4C:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // gyroscope-Z
                case 0x4D:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // magnetic field-X
                case 0x4E:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // magnetic field-Y
                case 0x4F:      formatter.format("%02x", ValueType.FLOAT.getCode());    formatter.format("%08x", Float.floatToIntBits(values[loop1]));  break;          // magnetic field-Z
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
     * build TDV string of the sensor activation command
     * @param info     sensor info
     * @param value    command value
     * @return TDV command string
     */
    public static String buildSensorActivateCommand(SensorInfo info, boolean value) {
        return buildSensorControlCommand(info, value ? 1 : 0);
    }

    /**
     * build TDV string of the sensor control command
     * @param info     sensor info
     * @param value    command value
     * @return TDV command string
     */
    public static String buildSensorControlCommand(SensorInfo info, int value) {
        Formatter formatter = new Formatter();
        formatter.format("%02x", info.getType().getValueTDVTypes()[0]);
        switch (info.getType()) {
            case BUZZER:
                formatter.format("%02x", ValueType.UCHAR.getCode());
                formatter.format("%02x", value);
                break;
            case LED:
                formatter.format("%02x", ValueType.CHAR3.getCode());
                formatter.format("%06x", value);
                break;
            default:
                formatter.format("%02x", ValueType.BOOL.getCode());
                formatter.format("%02x", value);
                break;
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * parse TDV string of the sensor data
     * @param tdvString    TDV string
     * @return parsed info
     */
    public static List<SensorInfo> parseSensorData(String tdvString) {
        List<SensorInfo> sensorInfos = new ArrayList<>();

        float[] intValues = new float[SensorType.MAX_VALUE_NUMBERS];
        String[] stringValues = new String[1];

        for (int loop1 = 0 ; loop1 < tdvString.length() ; ) {
            // read type
            int type = Integer.parseInt(tdvString.substring(loop1, loop1 + 2), 16);
            loop1 += 2;

            // read value type
            int valueTypeCode = Integer.parseInt(tdvString.substring(loop1, loop1 + 2), 16);
            loop1 += 2;

            ValueType valueType = ValueType.NONE;
            ValueType[] valueTypes = ValueType.values();
            for (ValueType valueTypeTemp : valueTypes) {
                if (valueTypeTemp.getCode() == valueTypeCode) {
                    valueType = valueTypeTemp;
                    break;
                }
            }

            // read value
            String value = "0";
            if (valueType != ValueType.NONE) {
                value = tdvString.substring(loop1, loop1 + (valueType.getDataLength() * 2));
                loop1 += (valueType.getDataLength() * 2);
            }

            SensorType sensorType = SensorType.NONE;
            int valueBeginIndex = 0;
            int valueNumbers = 1;

            switch (type) {
                case 0x04:  sensorType = SensorType.BATTERY;                intValues[0] = convertStrToFloat(value);                            break;  // battery temperature
                case 0x05:  sensorType = SensorType.BATTERY;                intValues[0] = Long.parseLong(value, 16);   valueBeginIndex = 1;    break;  // battery level

                case 0x11:  sensorType = SensorType.AMBIENT_TEMPERATURE;    intValues[0] = convertStrToFloat(value);                            break;  // temperature
                case 0x12:  sensorType = SensorType.RELATIVE_HUMIDITY;      intValues[0] = convertStrToFloat(value);                            break;  // humidity
                case 0x13:  sensorType = SensorType.NOISE;                  intValues[0] = convertStrToFloat(value);                            break;  // noise

                case 0x20:  sensorType = SensorType.GPS;                    intValues[0] = convertStrToFloat(value);                            break;  // latitude
                case 0x21:  sensorType = SensorType.GPS;                    intValues[0] = convertStrToFloat(value);    valueBeginIndex = 1;    break;  // longitude
                case 0x22:  sensorType = SensorType.GPS;                    intValues[0] = Long.parseLong(value, 16);   valueBeginIndex = 2;    break;  // altitude
                case 0x24:  sensorType = SensorType.PRESSURE;               intValues[0] = convertStrToFloat(value);                            break;  // air pressure
                case 0x25:  sensorType = SensorType.LIGHT;                  intValues[0] = Long.parseLong(value, 16);                           break;  // light
                case 0x27:  sensorType = SensorType.BUZZER;                 intValues[0] = Long.parseLong(value, 16);                           break;  // buzzer
                case 0x28:  sensorType = SensorType.LED;                    intValues[0] = Long.parseLong(value, 16);                           break;  // led

                case 0x31:  sensorType = SensorType.PROXIMITY;              intValues[0] = Long.parseLong(value, 16);                           break;  // proximity
                case 0x32:  sensorType = SensorType.STEP_DETECTOR;          intValues[0] = Long.parseLong(value, 16);                           break;  // step detector
                case 0x33:  sensorType = SensorType.STEP_COUNTER;           intValues[0] = Long.parseLong(value, 16);                           break;  // step count
                case 0x34:  sensorType = SensorType.CAMERA;                 intValues[0] = Long.parseLong(value, 16);                           break;  // camera

                case 0x41:  sensorType = SensorType.ACCELEROMETER;          intValues[0] = convertStrToFloat(value);                            break;  // accelerometer-X
                case 0x42:  sensorType = SensorType.ACCELEROMETER;          intValues[0] = convertStrToFloat(value);    valueBeginIndex = 1;    break;  // accelerometer-Y
                case 0x43:  sensorType = SensorType.ACCELEROMETER;          intValues[0] = convertStrToFloat(value);    valueBeginIndex = 2;    break;  // accelerometer-Z
                case 0x44:  sensorType = SensorType.ORIENTATION;            intValues[0] = convertStrToFloat(value);                            break;  // orientation-X
                case 0x45:  sensorType = SensorType.ORIENTATION;            intValues[0] = convertStrToFloat(value);    valueBeginIndex = 1;    break;  // orientation-Y
                case 0x46:  sensorType = SensorType.ORIENTATION;            intValues[0] = convertStrToFloat(value);    valueBeginIndex = 2;    break;  // orientation-Z
                case 0x47:  sensorType = SensorType.GRAVITY;                intValues[0] = convertStrToFloat(value);                            break;  // gravity-X
                case 0x48:  sensorType = SensorType.GRAVITY;                intValues[0] = convertStrToFloat(value);    valueBeginIndex = 1;    break;  // gravity-Y
                case 0x49:  sensorType = SensorType.GRAVITY;                intValues[0] = convertStrToFloat(value);    valueBeginIndex = 2;    break;  // gravity-Z
                case 0x4A:  sensorType = SensorType.GYROSCOPE;              intValues[0] = convertStrToFloat(value);                            break;  // gyroscope-X
                case 0x4B:  sensorType = SensorType.GYROSCOPE;              intValues[0] = convertStrToFloat(value);    valueBeginIndex = 1;    break;  // gyroscope-Y
                case 0x4C:  sensorType = SensorType.GYROSCOPE;              intValues[0] = convertStrToFloat(value);    valueBeginIndex = 2;    break;  // gyroscope-Z
                case 0x4D:  sensorType = SensorType.MAGNETIC_FIELD;         intValues[0] = convertStrToFloat(value);                            break;  // magnetic field-X
                case 0x4E:  sensorType = SensorType.MAGNETIC_FIELD;         intValues[0] = convertStrToFloat(value);    valueBeginIndex = 1;    break;  // magnetic field-Y
                case 0x4F:  sensorType = SensorType.MAGNETIC_FIELD;         intValues[0] = convertStrToFloat(value);    valueBeginIndex = 2;    break;  // magnetic field-Z
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
     * parse TDV string of the sensor command
     * @param tdvString    TDV string
     * @return parsed info
     */
    public static SensorInfo parseSensorCommand(String tdvString) {
        if (TextUtils.isEmpty(tdvString) == true) {
            return null;
        }

        int readPos = 0;

        // read type
        int type = Integer.parseInt(tdvString.substring(readPos, readPos + 2), 16);
        readPos += 2;

        // read value type
        int valueTypeCode = Integer.parseInt(tdvString.substring(readPos, readPos + 2), 16);
        readPos += 2;

        ValueType valueType = ValueType.NONE;
        ValueType[] valueTypes = ValueType.values();
        for (ValueType valueTypeTemp : valueTypes) {
            if (valueTypeTemp.getCode() == valueTypeCode) {
                valueType = valueTypeTemp;
                break;
            }
        }

        // read value
        int value = 0;
        if (valueType != ValueType.NONE) {
            value = (int) Long.parseLong(tdvString.substring(readPos, readPos + (valueType.getDataLength() * 2)), 16);
            readPos += (valueType.getDataLength() * 2);
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
            case 0x32:  sensorType = SensorType.STEP_DETECTOR;          break;  // step detector
            case 0x33:  sensorType = SensorType.STEP_COUNTER;           break;  // step count
            case 0x34:  sensorType = SensorType.CAMERA;                 break;  // camera

            case 0x41:
            case 0x42:
            case 0x43:  sensorType = SensorType.ACCELEROMETER;          break;  // accelerometer
            case 0x44:
            case 0x45:
            case 0x46:  sensorType = SensorType.ORIENTATION;            break;  // orientation
            case 0x47:
            case 0x48:
            case 0x49:  sensorType = SensorType.GRAVITY;                break;  // gravity
            case 0x4A:
            case 0x4B:
            case 0x4C:  sensorType = SensorType.GYROSCOPE;              break;  // gyroscope
            case 0x4D:
            case 0x4E:
            case 0x4F:  sensorType = SensorType.MAGNETIC_FIELD;         break;  // magnetic field
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

    /**
     * convert string to float
     * @param string    string
     * @return converted float
     */
    private static float convertStrToFloat(String string) {
        if (string.length() < 8) {
            return 0;
        }

        byte[] temp = new byte[4];
        temp[0] = (byte) Integer.parseInt(string.substring(0, 2), 16);
        temp[1] = (byte) Integer.parseInt(string.substring(2, 4), 16);
        temp[2] = (byte) Integer.parseInt(string.substring(4, 6), 16);
        temp[3] = (byte) Integer.parseInt(string.substring(6, 8), 16);
        return ByteBuffer.wrap(temp).getFloat();
    }
}
