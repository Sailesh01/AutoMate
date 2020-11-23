package com.example.omniauto;

import android.app.Notification;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
   // private NotificationManagerCompat notificationManagerCompat;

    private LinearLayout layoutWifi, layoutBluetooth,layoutAirplane,layoutSilent,layoutMessage,layoutCall,layoutPower,layoutMobData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutWifi=(LinearLayout) findViewById(R.id.layoutWifi);
        layoutBluetooth=(LinearLayout) findViewById(R.id.layoutBluetooth);
        layoutAirplane=(LinearLayout)findViewById(R.id.layoutAirplane);
        layoutSilent=(LinearLayout)findViewById(R.id.layoutSilent);
        layoutMessage=(LinearLayout)findViewById(R.id.layoutMessage);
        layoutCall=(LinearLayout)findViewById(R.id.layoutCall);
        layoutPower=(LinearLayout)findViewById(R.id.layoutPower);
        layoutMobData=(LinearLayout)findViewById(R.id.layoutMobData);

     overridePendingTransition(R.anim.in,R.anim.out);

      /*  notificationManagerCompat=NotificationManagerCompat.from(this);

        Notification notification=new NotificationCompat.Builder(this,Notify.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_airplanemode)
                .setContentTitle("Reminder")
                .setContentText("You had this time to change your flight mode setting. Click here to continue.")
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_REMINDER)
                .build();
        notificationManagerCompat.notify(2,notification);*/
        wifiClicked();
        bluetoothClicked();
        airplaneCLicked();
        silentClicked();
        messageClicked();
        callClicked();
        powerClicked();
        mobdataClicked();
    }
    public void wifiClicked()
    {
       layoutWifi.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(MainActivity.this,ActivityWifi1.class);
               startActivity(intent);
           }
       });
    }
    public void bluetoothClicked()
    {
        layoutBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ActivityBluetooth.class);
                startActivity(intent);
            }
        });
    }
    public void airplaneCLicked(){
        layoutAirplane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivityAirplane.class);
                startActivity(intent);
            }
        });
    }
    public void silentClicked()
    {
        layoutSilent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivitySilent.class);
                startActivity(intent);
            }
        });
    }
    public void messageClicked()
    {
        layoutMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivityMessage.class);
                startActivity(intent);
            }
        });
    }
    public void callClicked()
    {
        layoutCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ActivityCall.class);
                startActivity(intent);
            }
        });
    }
    public void powerClicked()
    {
        layoutPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ActivityPower.class);
                startActivity(intent);

            }
        });
    }
    public void mobdataClicked()
    {
        layoutMobData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ActivityMobData.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.closein,R.anim.closeout);
    }

    /*@Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }*/
}
