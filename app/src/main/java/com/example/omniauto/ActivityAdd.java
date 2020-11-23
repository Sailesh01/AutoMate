package com.example.omniauto;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ActivityAdd extends AppCompatActivity  implements TimePickerDialog.OnTimeSetListener{
    private Toolbar toolbar;
    private Button btnTime;
    private TextView textView;
    private WifiManager wifiManager;
    private CountDownTimer countDownTimer;
    private long diffMilli;
    private static String msg;
    DbHandler_wifi myDb;
    Vibrator vibrator;
    private Calendar c;
    RadioButton radioButton;
    RadioGroup radioGroup;
    private static String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        btnTime=(Button)findViewById(R.id.btnTime);
        textView=(TextView)findViewById(R.id.textView);
        myDb=new DbHandler_wifi(this);
        overridePendingTransition(R.anim.in,R.anim.out);
        radioGroup=(RadioGroup)findViewById(R.id.radioGrp);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CHANGE_WIFI_STATE}, PackageManager.PERMISSION_GRANTED);


        btnTimePressed();

        wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);

        /*Intent intent=getIntent();
        if (intent.hasExtra("StopIt"))
        {

        }*/
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radioOn:
                        check="on";
                       // Toast.makeText(ActivityAdd.this,check,Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.radioOff:
                        check="off";
                       // Toast.makeText(ActivityAdd.this,check,Toast.LENGTH_SHORT).show();

                        break;

                }

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
        if (id==R.id.btnDone) {
          /*  if (check == "") {
                Toast.makeText(ActivityAdd.this, "Error! Radio button is empty", Toast.LENGTH_LONG).show();

            } else {*/
              //  Toast.makeText(ActivityAdd.this, check, Toast.LENGTH_SHORT).show();

                countDownTimer = new CountDownTimer(diffMilli, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        diffMilli = millisUntilFinished;
                        //updateCountDownText();
                    }

                    @Override
                    public void onFinish() {

                        if (check == "off") {
                            wifiManager.setWifiEnabled(false);

                        }
                        if (check == "on") {
                            wifiManager.setWifiEnabled(true);

                        }

                        // Toast.makeText(ActivityAdd.this,check,Toast.LENGTH_LONG).show();

                        vibrator.vibrate(500);

                    }
                }.start();
                Intent intentCheck = getIntent();

                if (intentCheck.hasExtra("editIt_wifi")) {
                    // String sent=intentCheck.getStringExtra("editIt");
                    String intentId = intentCheck.getStringExtra("primeId_wifi");
                    String intentPos = intentCheck.getStringExtra("primePos_wifi");
                    //int primePos=Integer.parseInt(intentPos);
                    DbHandler_wifi myDb = new DbHandler_wifi(this);
                    myDb.updateTime(intentId, msg);
                    Intent intent = new Intent(ActivityAdd.this, ActivityWifi1.class);
                    intent.putExtra("Position_wifi", intentPos);
                    intent.putExtra("Data_wifi", msg);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ActivityAdd.this, ActivityWifi1.class);
                    intent.putExtra("message_wifi", msg);
                    startActivity(intent);

                }

            }







        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        Intent intentCheck=getIntent();
        if (intentCheck.hasExtra("editIt_wifi"))
        {
            String sent = intentCheck.getStringExtra("editIt_wifi");
            textView.setText(sent);

        }
        else{

        c=Calendar.getInstance();
        int hrs=c.get(Calendar.HOUR_OF_DAY);
        int mins=c.get(Calendar.MINUTE);

        textView.setText(hrs+":"+mins);}
        super.onStart();
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
     //   Toast.makeText(ActivityAdd.this,String.valueOf(diffMilli),Toast.LENGTH_SHORT).show();



        //tv.setText(msg);
    }

    public void showAll(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.create().show();
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
