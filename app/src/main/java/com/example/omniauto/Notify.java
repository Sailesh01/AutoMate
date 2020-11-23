package com.example.omniauto;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notify extends Application {
    public static final String CHANNEL_ID="channel9";
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }
    public void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel1=new NotificationChannel(
                    CHANNEL_ID,"airplane", NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Enable or disable airplane mode");
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }

    }

}
