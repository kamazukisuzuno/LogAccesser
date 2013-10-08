package com.example.logaccesser;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suzuno on 13-10-8.
 */
public class RecordService extends Service{

    OnActivityStartBroadcastReceiver receiver;
    IntentFilter filter;

    List<String> intentList = new ArrayList<String>();

    private InternalBinder binder = new InternalBinder();

    @Override
    public IBinder onBind(Intent intent) {

        Log.v("RecordService","onBind");
        return binder;
    }

    @Override
    public void onCreate(){
        Log.v("RecordService","onCreate");
    }

    public int onStartCommand(Intent intent,int flags,int startId){

        Log.v("RecordService","onStartCommand");
        receiver = new OnActivityStartBroadcastReceiver(this);
        filter = new IntentFilter();
        filter.addAction("com.readboy.startActivity");
        registerReceiver(receiver,filter);

        return 0;
    }

    public void onDestroy(){

        unregisterReceiver(receiver);
        receiver = null;
        filter = null;
    }

    public void addRecord(String record){
        intentList.add(record);
    }

    public List<String> getIntentList(){
        return intentList;
    }


    public class InternalBinder extends Binder {
        RecordService getService(){
            return RecordService.this;
        }
    }
}
