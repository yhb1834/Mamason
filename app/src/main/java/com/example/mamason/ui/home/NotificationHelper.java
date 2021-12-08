package com.example.mamason.ui.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.mamason.MainActivity;
import com.example.mamason.R;

public class NotificationHelper extends ContextWrapper {
    int notification_id = (int) (System.currentTimeMillis() % 10000);
    Intent sIntent = new Intent(getApplicationContext(), MainActivity.class);

    public static final String channel1ID = "channel1ID";
    public static final String channel1Name = "channel 1 ";
    public static final String channel2ID = "channel2ID";
    public static final String channel2Name = "channel 2 ";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels(){
        NotificationChannel channel1 = new NotificationChannel(channel1ID,channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.lightgreen);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);

        NotificationChannel channel2 = new NotificationChannel(channel2ID,channel2Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLightColor(R.color.lightgreen);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel2);

    }

    public NotificationManager getManager() {
        if (mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.icon);
    }

    public NotificationCompat.Builder getChannel2Notification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), channel2ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.add);
    }
    PendingIntent sPpendingIntent = PendingIntent.getActivity(getApplicationContext(),notification_id, sIntent,PendingIntent.FLAG_UPDATE_CURRENT);
    public NotificationCompat.Builder getChannelNotification(){
        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setContentTitle("응급 알람")
                .setContentText("응급 구조 요청 메세지를 보내시겠습니까")
                .setSmallIcon(R.drawable.hand)
                .setAutoCancel(false)
                .setFullScreenIntent(sPpendingIntent, true);
    }

    public NotificationCompat.Builder getChannelNotification2(){
        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setContentTitle("약 알람")
                .setContentText("약 먹을 시간이다~ 약 먹어!")
                .setSmallIcon(R.drawable.hand)
                .setFullScreenIntent(sPpendingIntent, true);
    }
}