package tp.skt.onem2m.api;

import tp.skt.onem2m.binder.mqtt_v1_1.control.execInstanceControl;
import tp.skt.onem2m.net.mqtt.MQTTConfiguration;

/**
 * 메시지 serialization 과 parsing 을 처리하는 추상 클래스이며, MQTTListener 추상 클래스를 가지고있다.
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public abstract class MQTTProcessor {

    public abstract <T extends oneM2MResource> String serialization(T request);

    public abstract <T extends oneM2MResource> String serialization(T request, MQTTConfiguration config);

    public abstract <T extends oneM2MResource> T parsing(final Class<T> clz, String result) throws Exception;

    /**
     * MQTT 통신 관련 각종 이벤트 함수를 가지고 있다.
     */
    public abstract static class MQTTListener {

        /**
         * 제어 요청
         *
         * @param control execInstance
         */
        public abstract void onPush(execInstanceControl control);

        /**
         * 서버와 연결됨
         */
        public abstract void onConnected();

        /**
         * 서버와 연결 해제됨
         */
        public abstract void onDisconnected();

        /**
         * subscribe 성공
         */
        public abstract void onSubscribed();

        /**
         * subscribe 실패
         */
        public abstract void onSubscribeFailure();

        /**
         * 서버와 연결 실패
         */
        public abstract void onConnectFailure();

        /**
         * 서버와 연결 해제 실패
         */
        public abstract void onDisconnectFailure();

        /**
         * 서버와 연결을 잃음
         */
        public abstract void onConnectionLost();

        /**
         * 메시지 전달됨
         */
        public abstract void onDelivered();
    }
}
