package com.example.fang.medicinereminderapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    private final String TAG = "AlarmMe";
    @Override
    public void onReceive(Context context, Intent intent) {
        //Set up alarm receiver
        Log.i("MyAlarm", "Broadcast received");
        Intent newIntent = new Intent(context, AlarmNotificationActivity.class);
        Alarm alarm = new Alarm(context);

        alarm.fromIntent(intent);
        alarm.toIntent(newIntent);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Log.i(TAG, "AlarmReceiver.onReceive('" + alarm.getTitle() + "')");

        context.startActivity(newIntent);
    }
}
