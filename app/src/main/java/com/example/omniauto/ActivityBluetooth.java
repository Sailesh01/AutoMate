package com.example.omniauto;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityBluetooth extends AppCompatActivity {
    private Toolbar toolbar;
    private static ArrayList<String> dataBlue = new ArrayList<>();
    static int flag=0;
    CustomAdapterBlue adap=new CustomAdapterBlue();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar=(Toolbar)findViewById(R.id.toolBar1);
        listView=(ListView)findViewById(R.id.listView);
        registerForContextMenu(listView);
        overridePendingTransition(R.anim.in,R.anim.out);

        Intent intent = getIntent();
        if (intent.hasExtra("message_bluetooth")) {
            DbHandler_bluetooth myDb = new DbHandler_bluetooth(this);

            String sent = intent.getStringExtra("message_bluetooth");
            myDb.insertData(sent);
            Cursor res=myDb.getTime();
            res.moveToLast();
            dataBlue.add(res.getString(1));

        }

        if (dataBlue != null) {
            listView.setAdapter(adap);


        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityBluetooth.this, ActivityAddBlue.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        flag++;
        Intent intent=getIntent();
        if (flag==1) {
            DbHandler_bluetooth myDb = new DbHandler_bluetooth(this);

            Cursor res = myDb.getTime();
            while (res.moveToNext()) {
                dataBlue.add(res.getString(1));
            }
        }
        if (intent.hasExtra("Position_bluetooth"))
        {
            String posyString=intent.getStringExtra("Position_bluetooth");
            int posyInt=Integer.parseInt(posyString);
            String sentData=intent.getStringExtra("Data_bluetooth");
            dataBlue.set(posyInt,sentData);
            adap.notifyDataSetChanged();

        }
        super.onStart();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.floating_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int pos=info.position;
        DbHandler_bluetooth myDb = new DbHandler_bluetooth(this);
        Cursor res=myDb.getTime();
        if (pos==0)
            res.moveToFirst();
        else if(pos==dataBlue.size())
            res.moveToLast();
        else
            res.move(pos+1);
        switch (item.getItemId())
        {
            case R.id.menuEdit:
                String editTime=res.getString(1);
                String intentId=res.getString(0);
                Intent intent=new Intent(ActivityBluetooth.this,ActivityAddBlue.class);
                intent.putExtra("editIt_bluetooth",editTime);
                intent.putExtra("primeId_bluetooth",intentId);
                intent.putExtra("primePos_bluetooth",String.valueOf(pos));
                startActivity(intent);
                return true;
            case R.id.menuDelete:
                String id=res.getString(0);
                myDb.deleteData(id);
                dataBlue.remove(pos);
                adap.notifyDataSetChanged();
                //stopTimer();
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
       onBackPressed();
        return true;
    }
    /*@Override
    public void onBackPressed() {
        Intent intent1=new Intent(ActivityBluetooth.this,MainActivity.class);
        startActivity(intent1);
    }*/
    public  class CustomAdapterBlue extends BaseAdapter {



        @Override
        public int getCount() {
            return dataBlue.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            final View view=inflater.inflate(R.layout.layout_cardview,null);
            // final View view = getLayoutInflater().inflate(R.layout.list_view, null);
            ImageView imageViewWifi = (ImageView) view.findViewById(R.id.imageView);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            imageViewWifi.setImageResource(R.drawable.ic_bluetooth_black);
            textView.setText(dataBlue.get(position));
            return view;

        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.closein,R.anim.closeout);
    }

}
