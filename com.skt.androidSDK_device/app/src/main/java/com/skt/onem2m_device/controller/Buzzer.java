package com.skt.onem2m_device.controller;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Buzzer controller
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class Buzzer {
    /**
     * type list
     */
    public enum TYPE {
        NONE(-1),
        RINGTONE(RingtoneManager.TYPE_RINGTONE),
        NOTIFICATION(RingtoneManager.TYPE_NOTIFICATION),
        ALARM(RingtoneManager.TYPE_ALARM);

        private int     localType;

        /**
         * constructor
         * @param localType    local type
         */
        TYPE(int localType) {
            this.localType = localType;
        }

        /**
         * get local type
         * @return local type
         */
        public int getLocalType() { return localType; }
    }

    private static Ringtone ringtone = null;    // ringtone
    private Context         context;            // context

    /**
     * constructor
     * @param context    context
     */
    public Buzzer(Context context) {
        this.context = context;
    }

    /**
     * buzzer command notification
     * @param type    buzzer type
     * @return running result
     */
    public boolean notifyCommand(TYPE type) {
        if (ringtone != null) {
            ringtone.stop();
            ringtone = null;
        }

        if (type != TYPE.NONE) {
//            Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, type.getLocalType());
//            if (ringtoneUri == null) {
//                return false;
//            }
//            MediaPlayer mediaPlayer = MediaPlayer.create(context, ringtoneUri);
//            mediaPlayer.setLooping(false);
//            mediaPlayer.start();
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    mediaPlayer.release();
//                }
//            });

            Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, type.getLocalType());
            if (ringtoneUri == null) {
                return false;
            }

            ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
            ringtone.play();
        }

        return true;
    }
}
