package com.example.omniauto;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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

public class ActivityAirplane extends AppCompatActivity {
    private Toolbar toolbar;
    private static ArrayList<String> dataAir = new ArrayList<>();
    static int flag=0;
CustomAdapterAir adap=new CustomAdapterAir();
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airplane);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar=(Toolbar)findViewById(R.id.toolBar2);
        listView=(ListView)findViewById(R.id.listView);
        registerForContextMenu(listView);
        overridePendingTransition(R.anim.in,R.anim.out);

        Intent intent = getIntent();
        if (intent.hasExtra("message_airplane")) {
            DbHandler_airplane myDb = new DbHandler_airplane(this);

            String sent = intent.getStringExtra("message_airplane");
            myDb.insert(sent);
            Cursor res=myDb.getTime();
            res.moveToLast();
            dataAir.add(res.getString(1));

        }


        if (dataAir != null) {
            listView.setAdapter(adap);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ActivityAirplane.this,ActivityAddAir.class);
                startActivity(intent1);
            }
        });
    }
    @Override
    protected void onStart() {
        flag++;
        Intent intent=getIntent();
        if (flag==1) {
            DbHandler_airplane myDb = new DbHandler_airplane(this);

            Cursor res = myDb.getTime();
            while (res.moveToNext()) {
                dataAir.add(res.getString(1));
            }
        }
        if (intent.hasExtra("Position_airplane"))
        {
            String posyString=intent.getStringExtra("Position_airplane");
            int posyInt=Integer.parseInt(posyString);
            String sentData=intent.getStringExtra("Data_airplane");
            dataAir.set(posyInt,sentData);
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
        DbHandler_airplane myDb = new DbHandler_airplane(this);
        Cursor res=myDb.getTime();
        if (pos==0)
            res.moveToFirst();
        else if(pos==dataAir.size())
            res.moveToLast();
        else
            res.move(pos+1);
        switch (item.getItemId())
        {
            case R.id.menuEdit:
                String editTime=res.getString(1);
                String intentId=res.getString(0);
                Intent intent=new Intent(ActivityAirplane.this,ActivityAddAir.class);
                intent.putExtra("editIt_airplane",editTime);
                intent.putExtra("primeId_airplane",intentId);
                intent.putExtra("primePos_airplane",String.valueOf(pos));
                startActivity(intent);
                return true;
            case R.id.menuDelete:
                String id=res.getString(0);
                myDb.deleteData(id);
                dataAir.remove(pos);
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
  /*  @Override
    public void onBackPressed() {
        Intent intent1=new Intent(ActivityAirplane.this,MainActivity.class);
        startActivity(intent1);
    }*/
    public  class CustomAdapterAir extends BaseAdapter {



        @Override
        public int getCount() {
            return dataAir.size();
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
            imageViewWifi.setImageResource(R.drawable.ic_airplanemode);
            textView.setText(dataAir.get(position));
            return view;

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.closein,R.anim.closeout);
    }
}
