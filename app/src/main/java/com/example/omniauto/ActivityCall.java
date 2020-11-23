package com.example.omniauto;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class ActivityCall extends AppCompatActivity {
    private Toolbar toolbar;
    private static ArrayList<String> dataCall = new ArrayList<>();
    static int flag=0;
    CustomAdapterCall adap=new CustomAdapterCall();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar=(Toolbar)findViewById(R.id.toolBar2);
        listView=(ListView)findViewById(R.id.listView);
        registerForContextMenu(listView);
        overridePendingTransition(R.anim.in,R.anim.out);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityCall.this,ActivityAddCall.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra("message_call")) {
            DbHandler_call myDb = new DbHandler_call(this);

            String sent = intent.getStringExtra("message_call");
            myDb.insert(sent);
            Cursor res=myDb.getTime();
            res.moveToLast();
            dataCall.add(res.getString(1));

        }
        if (dataCall != null) {
            listView.setAdapter(adap);
        }
    }
    @Override
    protected void onStart() {
        flag++;
        Intent intent=getIntent();
        if (flag==1) {
            DbHandler_call myDb = new DbHandler_call(this);

            Cursor res = myDb.getTime();
            while (res.moveToNext()) {
                dataCall.add(res.getString(1));
            }
        }
        if (intent.hasExtra("Position_call"))
        {
            String posyString=intent.getStringExtra("Position_call");
            int posyInt=Integer.parseInt(posyString);
            String sentData=intent.getStringExtra("Data_call");
            dataCall.set(posyInt,sentData);
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
        DbHandler_call myDb = new DbHandler_call(this);
        Cursor res=myDb.getTime();
        if (pos==0)
            res.moveToFirst();
        else if(pos==dataCall.size())
            res.moveToLast();
        else
            res.move(pos+1);
        switch (item.getItemId())
        {
            case R.id.menuEdit:
                String editTime=res.getString(1);
                String intentId=res.getString(0);
                Intent intent=new Intent(ActivityCall.this,ActivityAddCall.class);
                intent.putExtra("editIt_call",editTime);
                intent.putExtra("primeId_call",intentId);
                intent.putExtra("primePos_call",String.valueOf(pos));
                startActivity(intent);
                return true;
            case R.id.menuDelete:
                String id=res.getString(0);
                myDb.deleteData(id);
                dataCall.remove(pos);
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
   /* @Override
    public void onBackPressed() {
        Intent intent1=new Intent(ActivityCall.this,MainActivity.class);
        startActivity(intent1);
    }*/
    public  class CustomAdapterCall extends BaseAdapter {



        @Override
        public int getCount() {
            return dataCall.size();
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
            imageViewWifi.setImageResource(R.drawable.ic_hotspot);
            textView.setText(dataCall.get(position));
            return view;

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.closein,R.anim.closeout);
    }


}
