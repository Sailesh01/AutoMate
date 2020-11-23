package com.example.omniauto;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
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

public class ActivityAddBlue extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private Toolbar toolbar;
    private Button btnTime;
    private TextView textView;
    private CountDownTimer countDownTimer;
    private long diffMilli;
    private static String msg;
    DbHandler_bluetooth myDb;
    private Calendar c;
    Vibrator vibrator;
    RadioGroup radioGroup;
    private static String check;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        btnTime=(Button)findViewById(R.id.btnTime);
        textView=(TextView)findViewById(R.id.textView);
        myDb=new DbHandler_bluetooth(this);
        overridePendingTransition(R.anim.in,R.anim.out);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        radioGroup=(RadioGroup)findViewById(R.id.radioGrp);

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
           // Toast.makeText(ActivityAddBlue.this,String.valueOf(diffMilli),Toast.LENGTH_SHORT).show();
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

                    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

                    if (check=="off")
                    {
                        bluetoothAdapter.disable();

                    }
                    if (check=="on")
                    {
                        bluetoothAdapter.enable();
                    }
                    /*if (bluetoothAdapter.isEnabled())
                        bluetoothAdapter.disable();*/

                    vibrator.vibrate(500);


                }
            }.start();
            Intent intentCheck=getIntent();

            if (intentCheck.hasExtra("editIt_bluetooth"))
            {
                // String sent=intentCheck.getStringExtra("editIt");
                String intentId=intentCheck.getStringExtra("primeId_bluetooth");
                String intentPos=intentCheck.getStringExtra("primePos_bluetooth");
                //int primePos=Integer.parseInt(intentPos);
                DbHandler_bluetooth myDb = new DbHandler_bluetooth(this);
                myDb.updateTime(intentId,msg);
                Intent intent=new Intent(ActivityAddBlue.this,ActivityBluetooth.class);
                intent.putExtra("Position_bluetooth",intentPos);
                intent.putExtra("Data_bluetooth",msg);
                startActivity(intent);
            }
            else {
                Intent intent=new Intent(ActivityAddBlue.this,ActivityBluetooth.class);
                intent.putExtra("message_bluetooth",msg);
                startActivity(intent);

            }

        }




        return super.onOptionsItemSelected(item);

    }
    @Override
    protected void onStart() {
        Intent intentCheck=getIntent();
        if (intentCheck.hasExtra("editIt_bluetooth"))
        {
            String sent = intentCheck.getStringExtra("editIt_bluetooth");
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
        //Toast.makeText(ActivityAddBlue.this,String.valueOf(diffMilli),Toast.LENGTH_SHORT).show();



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
