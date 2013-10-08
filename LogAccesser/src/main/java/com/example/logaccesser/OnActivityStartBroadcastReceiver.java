package com.example.logaccesser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by suzuno on 13-10-8.
 */
public class OnActivityStartBroadcastReceiver extends BroadcastReceiver{

    RecordService service;

    public OnActivityStartBroadcastReceiver(RecordService service){
        super();

        this.service = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String record = intent.getStringExtra("data");
        Toast.makeText(context,record,Toast.LENGTH_SHORT).show();

        if(service!=null){
            service.addRecord(record);
        }
    }
}
