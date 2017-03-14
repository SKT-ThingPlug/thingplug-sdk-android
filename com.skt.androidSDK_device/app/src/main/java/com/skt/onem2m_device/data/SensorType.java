package com.skt.onem2m_device.data;

import android.hardware.Sensor;

import com.skt.onem2m_device.Const;
import com.skt.onem2m_device.R;

/**
 * sensor type
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public enum SensorType {
    // type                     category                    nickname                        image                               value type          tlv types                       tdv types                       decimal place           value infos {name, measure}
    BATTERY                    (Category.BROADCAST,         "Battery",                      R.mipmap.icon_batterygauge_big,     ValueType.NUMBER,   new int[]{0x04, 0x05},          new int[]{0x04, 0x05},          new int[]{2, 0},        new String[][]{{"Temperature", "℃"}, {"Charge", "％"}}),                    // A constant describing a current battery status. {temperature, chargeLevel}

    AMBIENT_TEMPERATURE        (Category.SENSOR_MANAGER,    "Temperature",                  R.mipmap.icon_temperature_big,      ValueType.NUMBER,   new int[]{0x11},                new int[]{0x11},                new int[]{2},           new String[][]{{"Ambient temperature", "℃"}}),                              // This constant was deprecated in API level 14. use Sensor.TYPE_AMBIENT_TEMPERATURE instead.
    RELATIVE_HUMIDITY          (Category.SENSOR_MANAGER,    "Humidity",                     R.mipmap.icon_humidity_big,         ValueType.NUMBER,   new int[]{0x12},                new int[]{0x12},                new int[]{2},           new String[][]{{"Air humidity", "%"}}),                                     // A constant describing a relative humidity sensor type.
    NOISE                      (Category.MEDIA_RECORDER,    "Noise",                        R.mipmap.icon_noise_big,            ValueType.NUMBER,   new int[]{0x13},                new int[]{0x13},                new int[]{2},           new String[][]{{"Noise", "㏈"}}),

    GPS                        (Category.LOCATION_MANAGER,  "GPS",                          R.mipmap.icon_actuator_big,         ValueType.NUMBER,   new int[]{0x20, 0x21, 0x22},    new int[]{0x20, 0x21, 0x22},    new int[]{5, 5, 0},     new String[][]{{"Latitude", "˚"}, {"Longitude","˚"}, {"Altitude","˚"}}),    // A constant describing a GPS sensor status. {latitude, longitude, altitude}
    PRESSURE                   (Category.SENSOR_MANAGER,    "Air Pressure",                 R.mipmap.icon_pressure_big,         ValueType.NUMBER,   new int[]{0x24},                new int[]{0x24},                new int[]{1},           new String[][]{{"Pressure", "h㎩"}}),                                       // A constant describing a pressure sensor type.
    LIGHT                      (Category.SENSOR_MANAGER,    "Light",                        R.mipmap.icon_light_big,            ValueType.NUMBER,   new int[]{0x25},                new int[]{0x25},                new int[]{0},           new String[][]{{"Level", "㏓"}}),                                           // A constant describing a light sensor type.
    BUZZER                     (Category.ACTUATOR,          "Buzzer",                       R.mipmap.icon_buzzer_big,           ValueType.NUMBER,   new int[]{0x27},                new int[]{0x27},                new int[]{0},           new String[][]{{"", ""}}),
    LED                        (Category.ACTUATOR,          "Led",                          R.mipmap.icon_color_big,            ValueType.NUMBER,   new int[]{0x28},                new int[]{0x28},                new int[]{0},           new String[][]{{"", ""}}),

    PROXIMITY                  (Category.SENSOR_MANAGER,    "Proximity",                    R.mipmap.icon_peoplecount_big,      ValueType.NUMBER,   new int[]{0x31},                new int[]{0x31},                new int[]{0},           new String[][]{{"Distance", "㎝"}}),                                        // A constant describing a proximity sensor type.
    CAMERA                     (Category.ACTUATOR,          "Camera",                       R.mipmap.icon_camera_big,           ValueType.NUMBER,   new int[]{0x34},                new int[]{0x34},                new int[]{0},           new String[][]{{"", ""}}),
    ACCELEROMETER              (Category.SENSOR_MANAGER,    "Accelerometer",                R.mipmap.icon_accelerometer_big,    ValueType.NUMBER,   new int[]{0x38, 0x00, 0x00},    new int[]{0x41, 0x42, 0x43},    new int[]{2, 2, 2},     new String[][]{{"X", "㎨"}, {"Y", "㎨"}, {"Z", "㎨"}}),                     // A constant describing an accelerometer sensor type.
    ORIENTATION                (Category.SENSOR_MANAGER,    "Orientation",                  R.mipmap.icon_windvane_big,         ValueType.NUMBER,   new int[]{0x39, 0x00, 0x00},    new int[]{0x44, 0x45, 0x46},    new int[]{0, 0, 0},     new String[][]{{"Azimuth", "˚"}, {"Pitch", "˚"}, {"Roll", "˚"}}),           // This constant was deprecated in API level 8. use SensorManager.getOrientation() instead.
    GRAVITY                    (Category.SENSOR_MANAGER,    "Gravity",                      R.mipmap.icon_weight_big,           ValueType.NUMBER,   new int[]{0x3A, 0x00, 0x00},    new int[]{0x47, 0x48, 0x49},    new int[]{2, 2, 2},     new String[][]{{"X", "㎨"}, {"Y", "㎨"}, {"Z", "㎨"}}),                     // A constant describing a gravity sensor type.
    GYROSCOPE                  (Category.SENSOR_MANAGER,    "Gyroscope",                    R.mipmap.icon_rotaryangle_big,      ValueType.NUMBER,   new int[]{0x3B, 0x00, 0x00},    new int[]{0x4A, 0x4B, 0x4C},    new int[]{2, 2, 2},     new String[][]{{"X", "˚"}, {"Y", "˚"}, {"Z", "˚"}}),                        // A constant describing a gyroscope sensor type.
    MAGNETIC_FIELD             (Category.SENSOR_MANAGER,    "Magnetic Field",               R.mipmap.icon_vibration_big,        ValueType.NUMBER,   new int[]{0x3C, 0x00, 0x00},    new int[]{0x4D, 0x4E, 0x4F},    new int[]{2, 2, 2},     new String[][]{{"X", "µT"}, {"Y", "µT"}, {"Z", "µT"}}),                     // A constant describing a magnetic field sensor type.
    STEP_DETECTOR              (Category.SENSOR_MANAGER,    "Step Detector",                R.mipmap.icon_motion_on_big,        ValueType.NUMBER,   new int[]{0x3D},                new int[]{0x32},                new int[]{0},           new String[][]{{"Detection", ""}}),                                         // A constant describing a step detector sensor.
    STEP_COUNTER               (Category.SENSOR_MANAGER,    "Step Count",                   R.mipmap.icon_motion_on_big,        ValueType.NUMBER,   new int[]{0x3E},                new int[]{0x33},                new int[]{0},           new String[][]{{"Count", "steps"}}),                                        // A constant describing a step counter sensor.

//    GAME_ROTATION_VECTOR       (Category.SENSOR_MANAGER,    "GAME_ROTATION_VECTOR",         R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing an uncalibrated rotation vector sensor type.
//    GEOMAGNETIC_ROTATION_VECTOR(Category.SENSOR_MANAGER,    "GEOMAGNETIC_ROTATION_VECTOR",  R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing a geo-magnetic rotation vector.
//    GYROSCOPE_UNCALIBRATED     (Category.SENSOR_MANAGER,    "GYROSCOPE_UNCALIBRATED",       R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing an uncalibrated gyroscope sensor type.
//    HEART_BEAT                 (Category.SENSOR_MANAGER,    "HEART_BEAT",                   R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing a motion detect sensor.
//    HEART_RATE                 (Category.SENSOR_MANAGER,    "HEART_RATE",                   R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing a heart rate monitor.
//    LINEAR_ACCELERATION        (Category.SENSOR_MANAGER,    "LINEAR_ACCELERATION",          R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing a linear acceleration sensor type.
//    MAGNETIC_FIELD_UNCALIBRATED(Category.SENSOR_MANAGER,    "MAGNETIC_FIELD_UNCALIBRATED",  R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing an uncalibrated magnetic field sensor type.
//    MOTION_DETECT              (Category.SENSOR_MANAGER,    "MOTION_DETECT",                R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing a motion detect sensor.
//    POSE_6DOF                  (Category.SENSOR_MANAGER,    "POSE_6DOF",                    R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing a pose sensor with 6 degrees of freedom.
//    ROTATION_VECTOR            (Category.SENSOR_MANAGER,    "ROTATION_VECTOR",              R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing a rotation vector sensor type.
//    SIGNIFICANT_MOTION         (Category.SENSOR_MANAGER,    "SIGNIFICANT_MOTION",           R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing a significant motion trigger sensor.
//    STATIONARY_DETECT          (Category.SENSOR_MANAGER,    "STATIONARY_DETECT",            R.mipmap.ic_launcher,               ValueType.NUMBER,   new int[]{0x00},                new int[]{0x00},                new int[]{0},           new String[][]{{"", ""}}),                                                  // A constant describing a stationary detect sensor.

    NONE                       (Category.SENSOR_MANAGER,    "NONE",                         -1,                                 ValueType.NUMBER,   null,                           null,                           null,                   null);                                                                      // none

    public static final int    MAX_VALUE_NUMBERS = 3;

    /**
     * sensor detection category list
     */
    public static enum Category {
        SENSOR_MANAGER,
        BROADCAST,
        LOCATION_MANAGER,
        MEDIA_RECORDER,
        ACTUATOR,
    };

    /**
     * sensor value type
     */
    public enum ValueType {
        NUMBER,
        STRING,
    }

    private Category    category;           // sensor category
    private String      nickname;           // sensor nickname
    private int         image;              // sensor image
    private ValueType   valueType;          // sensor value type
    private int[]       valueTLVTypes;      // sensor TLV types
    private int[]       valueTDVTypes;      // sensor TDV types
    private int[]       valueDecimalPlaces; // sensor value decimal places
    private String[][]  valueInfos;         // sensor value informations

    /**
     * constructor
     * @param category             category
     * @param nickname             nickname
     * @param image                image
     * @param valueType            value type
     * @param tlvTypes             TLV types
     * @param valueDecimalPlaces   value decimal places
     * @param valueInfos           value informations
     */
    SensorType(Category category, String nickname, int image, ValueType valueType, int[] tlvTypes, int[] tdvTypes, int[] valueDecimalPlaces, String[][] valueInfos) {
        this.category = category;
        this.nickname = nickname;
        this.image = image;
        this.valueType = valueType;
        this.valueTLVTypes = tlvTypes;
        this.valueTDVTypes = tdvTypes;
        this.valueDecimalPlaces = valueDecimalPlaces;
        this.valueInfos = valueInfos;
    }

    /**
     * get sensor category
     * @return category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * get sensor nickname
     * @return nickname
     */
    public String getNickname() { return nickname; }

    /**
     * get sensor image
     * @return image
     */
    public int getImage() { return image; }

    /**
     * get sensor value type
     * @return value type
     */
    public ValueType getValueType() { return valueType; }

    /**
     * get sensor value numbers
     * @return value numbers
     */
    public int getValueNumbers() { return (valueTLVTypes != null ? valueTLVTypes.length : 0); }

    /**
     * get sensor TLV types
     * @return TLV types
     */
    public int[] getValueTLVTypes() { return valueTLVTypes; }

    /**
     * get sensor TDV types
     * @return TDV types
     */
    public int[] getValueTDVTypes() { return valueTDVTypes; }

    /**
     * get value decimal places
     * @return value decimal places
     */
    public int[] getValueDecimalPlaces() { return valueDecimalPlaces; }

    /**
     * get sensor value informations
     * @return value informations
     */
    public String[][] getValueInfos() { return valueInfos; }

    /**
     * convert detection category and type to sensor type
     * @param category    detection category
     * @param type        type
     * @return sensor type
     */
    public static SensorType getType(Category category, int type) {
        switch(category) {
            case SENSOR_MANAGER:
                switch (type) {
                    case Sensor.TYPE_AMBIENT_TEMPERATURE:           return AMBIENT_TEMPERATURE;
                    case Sensor.TYPE_TEMPERATURE:                   return AMBIENT_TEMPERATURE;
                    case Sensor.TYPE_RELATIVE_HUMIDITY:             return RELATIVE_HUMIDITY;

                    case Sensor.TYPE_PRESSURE:                      return PRESSURE;
                    case Sensor.TYPE_LIGHT:                         return LIGHT;

                    case Sensor.TYPE_PROXIMITY:                     return PROXIMITY;
                    case Sensor.TYPE_ACCELEROMETER:                 return ACCELEROMETER;
                    case Sensor.TYPE_ORIENTATION:                   return ORIENTATION;
                    case Sensor.TYPE_GRAVITY:                       return GRAVITY;
                    case Sensor.TYPE_GYROSCOPE:                     return GYROSCOPE;
                    case Sensor.TYPE_MAGNETIC_FIELD:                return MAGNETIC_FIELD;
                    case Sensor.TYPE_STEP_DETECTOR:                 return STEP_DETECTOR;
                    case Sensor.TYPE_STEP_COUNTER:                  return STEP_COUNTER;

//                    case Sensor.TYPE_GAME_ROTATION_VECTOR:          return GAME_ROTATION_VECTOR;
//                    case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:   return GEOMAGNETIC_ROTATION_VECTOR;
//                    case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:        return GYROSCOPE_UNCALIBRATED;
//                    case Sensor.TYPE_HEART_BEAT:                    return HEART_BEAT;
//                    case Sensor.TYPE_HEART_RATE:                    return HEART_RATE;
//                    case Sensor.TYPE_LINEAR_ACCELERATION:           return LINEAR_ACCELERATION;
//                    case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:   return MAGNETIC_FIELD_UNCALIBRATED;
//                    case Sensor.TYPE_MOTION_DETECT:                 return MOTION_DETECT;
//                    case Sensor.TYPE_POSE_6DOF:                     return POSE_6DOF;
//                    case Sensor.TYPE_ROTATION_VECTOR:               return ROTATION_VECTOR;
//                    case Sensor.TYPE_SIGNIFICANT_MOTION:            return SIGNIFICANT_MOTION;
//                    case Sensor.TYPE_STATIONARY_DETECT:             return STATIONARY_DETECT;
                }
                break;
            case BROADCAST:
                switch (type) {
                    case Const.SENSOR_TYPE_BATTERY:                 return BATTERY;
                }
                break;
            case LOCATION_MANAGER:
                switch (type) {
                    case Const.SENSOR_TYPE_GPS:                     return GPS;
                }
                break;
            case MEDIA_RECORDER:
                switch (type) {
                    case Const.SENSOR_TYPE_NOISE:                   return NOISE;
                }
                break;
            case ACTUATOR:
                switch (type) {
                    case Const.SENSOR_TYPE_BUZZER:                  return BUZZER;
                    case Const.SENSOR_TYPE_LED:                     return LED;
                    case Const.SENSOR_TYPE_CAMERA:                  return CAMERA;
                }
                break;
        }
        return NONE;
    }
}
