package com.example.logaccesser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by suzuno on 13-10-8.
 */
public class AwakeReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("AwakeReceiver", "awake service");
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setClass(context, RecordService.class);
        context.startService(i);
    }
}
