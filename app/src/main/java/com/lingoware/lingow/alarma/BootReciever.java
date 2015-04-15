package com.lingoware.lingow.alarma;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReciever extends BroadcastReceiver {
    public BootReciever() {
    }

    AlarmReciever alarm = new AlarmReciever();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            alarm.setAlarm(context);
        }
    }
}
