package com.example.logaccesser;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    public final static ArrayList<String> GET_ROOT = new ArrayList<String>();

    public final static ArrayList<String> READ_LOG = new ArrayList<String>();

    public final static ArrayList<String> CLEAR_LOG = new ArrayList<String>();

    static{
        GET_ROOT.add("su");

        READ_LOG.add("logcat");
        //READ_LOG.add("-d");

        CLEAR_LOG.add("logcat");
        CLEAR_LOG.add("-c");
    }

    private ListView lv;
    private List<Map<String,Object>> list;
    private SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //getRoot();
        //startLogging();
        bindService();
    }

    private void getRoot() {
        ExecuteAsRootBase.canRunRootCommands();
    }

    @Override
    public void onStop(){
        super.onStop();
        unbindService();
    }

    @Override
    public void onResume(){
        super.onResume();
        clearLog();
    }


    public void broadcast(View v){
        if(recordService!=null){
            for(String s:recordService.getIntentList()){
                addLog(s);
            }
        }
        /*
        Log.v("MainActivity","broadcast");
        Intent intent = new Intent("com.readboy.startActivity");
        sendBroadcast(intent);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void init(){

        lv = (ListView) findViewById(R.id.listview);

        list = new ArrayList<Map<String,Object>>();

        sa = new SimpleAdapter(this,list,R.layout.list_text,new String[]{
                "TEXT"
        },new int[]{
                R.id.text
        } );

        lv.setAdapter(sa);
    }

    public void addLog(String log){

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("TEXT",log);

        list.add(map);

        sa.notifyDataSetChanged();
    }

    public void clearLog(){

        list.clear();

        sa.notifyDataSetChanged();
    }

    public void startLogging(){

        new Thread(){
            @Override
            public void run(){
                try {
                    readLog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void readLog() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        //runtime.exec(GET_ROOT.toArray(new String[GET_ROOT.size()]));
        Process process = runtime.exec(READ_LOG.toArray(new String[READ_LOG.size()]));
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String str = null;

        while((str=br.readLine())!=null){

            Runtime.getRuntime().exec(CLEAR_LOG.toArray(new String[CLEAR_LOG.size()]));

            final String log = str;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addLog(log);
                    addLog("logging");
                }
            });
        }

        Log.v("main","read log exit");
    }

    private void bindService(){
        Intent intent = new Intent(this,RecordService.class);
        bindService(intent,sc, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(){
        unbindService(sc);
    }

    RecordService recordService;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            recordService = ((RecordService.InternalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            recordService = null;
        }
    };
}
