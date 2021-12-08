package com.example.mamason.ui.home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    Context mContext;

    PowerManager powerManager;
    private static PowerManager.WakeLock wakeLock;
    SharedPreferences pref;
    String get_state;
    private MediaPlayer mediaPlayer;

    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1,nb.build());

        AlarmReceiverChk(context, intent);
    }

    private void AlarmReceiverChk(final Context context, final Intent intent){
        //Log.d(TAG, "Alarm Receiver started!");
        switch (get_state){
            case "ALARM_ON":
                acquireCPUWakeLock(context, intent);
                // RingtoneService 서비스 intent 생성
                Intent serviceIntent = new Intent(mContext, RingtoneService.class);
                serviceIntent.putExtra("state", get_state);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    context.startForegroundService(serviceIntent);
                } else {
                    context.startService(serviceIntent);
                }
                break;
            case "ALARM_OFF": // stopService 가 동작하지 않아서 startService 로 처리하고 나서....
                releaseCpuLock();
                Intent stopIntent = new Intent(context, RingtoneService.class);
                stopIntent.putExtra("state", get_state);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    context.startForegroundService(stopIntent);
                } else {
                    context.startService(stopIntent);
                }
                break;
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    private void acquireCPUWakeLock(Context context, Intent intent) {
        // 잠든 화면 깨우기
        if (wakeLock != null) {
            return;
        }
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "WAKELOCK");
        wakeLock.acquire();
        Log.e("PushWakeLock", "Acquire cpu WakeLock = " + wakeLock);
    }

    private void releaseCpuLock() {
        Log.e("PushWakeLock", "Releasing cpu WakeLock = " + wakeLock);

        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}