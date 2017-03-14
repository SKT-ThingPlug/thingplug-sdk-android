package com.skt.onem2m_device.controller;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;

import com.skt.onem2m_device.R;

/**
 * Led controller
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class Led {
    /**
     * color list
     */
    public enum COLOR {
        NONE(Color.TRANSPARENT),
        RED(Color.RED),
        GREEN(Color.GREEN),
        BLUE(Color.BLUE),
        MAGENTA(Color.MAGENTA),
        CYAN(Color.CYAN),
        YELLOW(Color.YELLOW),
        WHITE(Color.WHITE);

        private int     localColor;     // local color

        /**
         * constructor
         * @param localColor    local color
         */
        COLOR(int localColor) {
            this.localColor = localColor;
        }

        /**
         * get local color
         * @return local color
         */
        public int      getLocalColor() { return localColor; }
    }

    private static final int    NOTIFICATION_ID = 1;

    private Context     context;        // context

    /**
     * constructor
     * @param context    context
     */
    public Led(Context context) {
        this.context = context;
    }

    /**
     * led command notification
     * @param color    led color
     * @return running result
     */
    public boolean notifyCommand(COLOR color) {
        return notifyCommand(color.getLocalColor());
    }

    /**
     * led command notification
     * @param argbColor    led color
     * @return running result
     */
    public boolean notifyCommand(int argbColor) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (argbColor == COLOR.NONE.getLocalColor()) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
        else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.mipmap.icon_color_big);
            builder.setContentTitle(context.getResources().getString(R.string.actuator_led_notification));
            builder.setLights(argbColor, 1000, 0);

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }

        return true;
    }
}
