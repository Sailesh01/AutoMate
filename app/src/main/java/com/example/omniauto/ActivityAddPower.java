package com.example.omniauto;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.AudioManager;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ActivityAddPower extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private NotificationManagerCompat notificationManagerCompat;

    private Toolbar toolbar;
    private Button btnTime;
    private TextView textView;
    private CountDownTimer countDownTimer;
    private long diffMilli;
    private static String msg;
    DbHandler_power myDb;
    private Calendar c;
    Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_power);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        btnTime=(Button)findViewById(R.id.btnTime);
        textView=(TextView)findViewById(R.id.textView);
        myDb=new DbHandler_power(this);
        notificationManagerCompat=NotificationManagerCompat.from(this);
        overridePendingTransition(R.anim.in,R.anim.out);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);


        btnTimePressed();
    }
    public void btnTimePressed()
    {
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker=new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.btnDone)
        {
            //Toast.makeText(ActivityAddPower.this,String.valueOf(diffMilli),Toast.LENGTH_SHORT).show();
            //long timeInMilli=diffMilli-60000;

            //myDb.insertData(msg);
            countDownTimer=new CountDownTimer(diffMilli,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    diffMilli=millisUntilFinished;
                    //updateCountDownText();
                }

                @Override
                public void onFinish() {

                    Notification notification=new NotificationCompat.Builder(ActivityAddPower.this,Notify.CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_power)
                            .setContentTitle("Reminder")
                            .setContentText("You had set this time to turn off your device. Click here to continue.")
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setCategory(Notification.CATEGORY_REMINDER)
                            .build();
                    notificationManagerCompat.notify(1,notification);


                    vibrator.vibrate(500);

                 //   Toast.makeText(ActivityAddPower.this,"Done",Toast.LENGTH_SHORT).show();


                }
            }.start();
            Intent intentCheck=getIntent();

            if (intentCheck.hasExtra("editIt_power"))
            {
                // String sent=intentCheck.getStringExtra("editIt");
                String intentId=intentCheck.getStringExtra("primeId_power");
                String intentPos=intentCheck.getStringExtra("primePos_power");
                //int primePos=Integer.parseInt(intentPos);
                DbHandler_power myDb = new DbHandler_power(this);
                myDb.updateTime(intentId,msg);
                Intent intent=new Intent(ActivityAddPower.this,ActivityPower.class);
                intent.putExtra("Position_power",intentPos);
                intent.putExtra("Data_power",msg);
                startActivity(intent);
            }
            else {
                Intent intent=new Intent(ActivityAddPower.this,ActivityPower.class);
                intent.putExtra("message_power",msg);
                startActivity(intent);

            }

        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    protected void onStart() {
        Intent intentCheck=getIntent();
        if (intentCheck.hasExtra("editIt_power"))
        {
            String sent = intentCheck.getStringExtra("editIt_power");
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
//        Toast.makeText(ActivityAddPower.this,String.valueOf(diffMilli),Toast.LENGTH_SHORT).show();



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
