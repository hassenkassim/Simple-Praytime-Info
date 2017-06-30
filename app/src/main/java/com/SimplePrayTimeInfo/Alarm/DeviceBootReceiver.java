package com.SimplePrayTimeInfo.Alarm;

/**
 * Created by HassenKassim on 04.01.15.
 */
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.SimplePrayTimeInfo.Message.Toasten;

import java.util.Calendar;

/**
 * @author Neel
 *         <p/>
 *         Broadcast reciever, starts when the device gets starts.
 *         Start your repeating alarm here.
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            int interval = 1000 * 60 * 60 * 24;

            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

            Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();





        }
    }
    private void setAlarm(Context context, int hours, int minutes,int request){
        int interval = 1000 * 60 * 60 * 24;
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, request, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis() + 60000); // + 1 min

        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, hours);
        //calendar.set(Calendar.MINUTE, minutes);

        /* Repeating on every 24 hours interval */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pendingIntent);
        String text = "Alarm set to " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        Toasten.toasten(context, text);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}