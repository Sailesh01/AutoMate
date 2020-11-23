package com.example.omniauto;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ActivityAddCall extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private Toolbar toolbar;
    private Button btnTime;
    private TextView textView;
    private CountDownTimer countDownTimer;
    private long diffMilli;
    private static String msg;
    DbHandler_call myDb;
    private Calendar c;
    Vibrator vibrator;
    private NotificationManagerCompat notificationManagerCompat;
    RadioGroup radioGroup;
    private static String check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_call);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnTime = (Button) findViewById(R.id.btnTime);
        textView = (TextView) findViewById(R.id.textView);
        myDb = new DbHandler_call(this);
        notificationManagerCompat=NotificationManagerCompat.from(this);
        radioGroup=(RadioGroup)findViewById(R.id.radioGrp);


        overridePendingTransition(R.anim.in,R.anim.out);

        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radioOn:
                        check="enable";
                        // Toast.makeText(ActivityAdd.this,check,Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.radioOff:
                        check="diable";
                        // Toast.makeText(ActivityAdd.this,check,Toast.LENGTH_SHORT).show();

                        break;
                }

            }
        });


        btnTimePressed();

    }
    public void btnTimePressed() {
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnDone) {
            //Toast.makeText(ActivityAddCall.this, String.valueOf(diffMilli), Toast.LENGTH_SHORT).show();
            //long timeInMilli=diffMilli-60000;

            //myDb.insertData(msg);
            countDownTimer = new CountDownTimer(diffMilli, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    diffMilli = millisUntilFinished;
                    //updateCountDownText();
                }

                @Override
                public void onFinish() {
                    Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    PendingIntent pendingIntent=PendingIntent.getActivity(ActivityAddCall.this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification notification=new NotificationCompat.Builder(ActivityAddCall.this,Notify.CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_hotspot)
                            .setContentTitle("Reminder")
                            .setContentText("You had set this time to "+check+" your hotspot setting. Click here to continue.")
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setCategory(Notification.CATEGORY_REMINDER)
                            .build();
                    notificationManagerCompat.notify(1,notification);

                    vibrator.vibrate(500);

                   // Toast.makeText(ActivityAddCall.this, "Done", Toast.LENGTH_SHORT).show();

                }
            }.start();
            Intent intentCheck = getIntent();

            if (intentCheck.hasExtra("editIt_call")) {
                // String sent=intentCheck.getStringExtra("editIt");
                String intentId = intentCheck.getStringExtra("primeId_call");
                String intentPos = intentCheck.getStringExtra("primePos_call");
                //int primePos=Integer.parseInt(intentPos);
                DbHandler_message myDb = new DbHandler_message(this);
                myDb.updateTime(intentId, msg);
                Intent intent = new Intent(ActivityAddCall.this, ActivityCall.class);
                intent.putExtra("Position_call", intentPos);
                intent.putExtra("Data_call", msg);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ActivityAddCall.this, ActivityCall.class);
                intent.putExtra("message_call", msg);
                startActivity(intent);

            }

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        Intent intentCheck=getIntent();
        if (intentCheck.hasExtra("editIt_call"))
        {
            String sent = intentCheck.getStringExtra("editIt_call");
            textView.setText(sent);

        }
        else{

            c=Calendar.getInstance();
            int hrs=c.get(Calendar.HOUR_OF_DAY);
            int mins=c.get(Calendar.MINUTE);

            textView.setText(hrs+":"+mins);}
        super.onStart();
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        int currentMin = (c.get(Calendar.HOUR_OF_DAY)) * 60 + (c.get(Calendar.MINUTE));
        int selectedMin = (hourOfDay * 60 + minute);
        int diff = selectedMin - currentMin;
        diffMilli = diff * 60000;
        String hrs="";
        String mins="";
        for (int i=0;i<10;i++)
        {
            if (hourOfDay==i)
            {
                hrs="0"+hourOfDay;
                break;
            }
            else
                hrs=""+hourOfDay;

        }
        for (int i=0;i<10;i++)
        {
            if (minute==i)
            {
                mins="0"+minute;
                break;
            }
            else mins=""+minute;
        }
        textView.setText(hrs+":"+mins);
        msg=hrs+":"+mins;
        //Toast.makeText(ActivityAddCall.this,String.valueOf(diffMilli),Toast.LENGTH_SHORT).show();



        //tv.setText(msg);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.closein,R.anim.closeout);
    }
}
