package com.example.omniauto;

import android.Manifest;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ActivityAddMessage extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private Toolbar toolbar;
    private Button btnTime;
    private TextView textView;
    private CountDownTimer countDownTimer;
    private long diffMilli;
    private static String msg;
    DbHandler_message myDb;
    private Calendar c;
    private EditText editMessage,editNumber;
    Vibrator vibrator;
    private static boolean flag;
    private String message;
    private String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnTime = (Button) findViewById(R.id.btnTime);
        editMessage = (EditText) findViewById(R.id.editMessage);
        editNumber = (EditText) findViewById(R.id.editNumber);
        textView = (TextView) findViewById(R.id.textView);
        myDb = new DbHandler_message(this);
        overridePendingTransition(R.anim.in,R.anim.out);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);


         ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},PackageManager.PERMISSION_GRANTED);



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
            message=editMessage.getText().toString().trim();
            number=editNumber.getText().toString().trim();
            if (!editMessage.getText().toString().equals("") || !editNumber.getText().toString().equals(""))
            {
                flag=true;
            }
            else
            {
                flag=false;
            }
            if (flag==true) {
           // Toast.makeText(ActivityAddMessage.this, String.valueOf(diffMilli), Toast.LENGTH_SHORT).show();
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
                    if (flag==true){

                        try {

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);


                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage(number, null, message, pi, null);

                            Toast.makeText(getApplicationContext(), "SMS Sent successfully..... ",
                                    Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Log.e("MainActivity Error ",e.getMessage());
                            Toast.makeText(getApplicationContext(), "Failed Error :"+e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    vibrator.vibrate(500);
                       }


                }
            }.start();


                Intent intentCheck = getIntent();

                if (intentCheck.hasExtra("editIt_message")) {
                    // String sent=intentCheck.getStringExtra("editIt");
                    String intentId = intentCheck.getStringExtra("primeId_message");
                    String intentPos = intentCheck.getStringExtra("primePos_message");
                    //int primePos=Integer.parseInt(intentPos);
                    DbHandler_message myDb = new DbHandler_message(this);
                    myDb.updateTime(intentId, msg);
                    Intent intent = new Intent(ActivityAddMessage.this, ActivityMessage.class);
                    intent.putExtra("Position_message", intentPos);
                    intent.putExtra("Data_message", msg);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ActivityAddMessage.this, ActivityMessage.class);
                    intent.putExtra("message_message", msg);
                    startActivity(intent);

                }
            }
            else {
                Toast.makeText(ActivityAddMessage.this, "Error! Number or Message field cannot be empty.", Toast.LENGTH_SHORT).show();

            }

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        Intent intentCheck=getIntent();
        if (intentCheck.hasExtra("editIt_message"))
        {
            String sent = intentCheck.getStringExtra("editIt_message");
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
       // Toast.makeText(ActivityAddMessage.this,String.valueOf(diffMilli),Toast.LENGTH_SHORT).show();



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
