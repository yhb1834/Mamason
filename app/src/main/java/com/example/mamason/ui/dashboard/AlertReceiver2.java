package com.example.mamason.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.mamason.ui.home.NotificationHelper;

public class AlertReceiver2 extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification2();
        notificationHelper.getManager().notify(1,nb.build());
    }
}