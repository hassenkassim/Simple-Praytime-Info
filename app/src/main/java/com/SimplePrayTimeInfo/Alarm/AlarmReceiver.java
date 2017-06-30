package com.SimplePrayTimeInfo.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.SimplePrayTimeInfo.MainActivity;
import com.SimplePrayTimeInfo.Message.Toasten;

import org.json.JSONObject;

import java.util.Calendar;


/**
 * Created by HassenKassim on 03.01.15.
 *
 * If sound playing checked, then set the Alarms here, This class is called every day once
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Set all Alarms!!
        // Get all Day Times
        // Set for all Times where Setting is on an Alarm and put the Prayname in

        JSONObject monthjson;
        JSONObject cur = new JSONObject();
        int month = MainActivity.getcurMonth();
        int day = MainActivity.getcurDay();

        String monthdata = context.getSharedPreferences("Data", context.MODE_PRIVATE)
                .getString("" + month, "{}");

        try {
            String toasttext = "Alarm Times:\n";

            monthjson = new JSONObject(monthdata);
            cur = monthjson.getJSONObject(day + "");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            if (prefs.getBoolean("set_tone_fajr", false)) {
                String time = cur.getString("Fajr");
                int hours = gethours(time);
                int minutes = getminutes(time);
                setAlarm(context,"Fajr",hours, minutes,1);
                toasttext = toasttext + "Fajr: " + hours + ":" + minutes + "\n";
            }
            if (prefs.getBoolean("set_tone_dhuhr", false)) {
                String time = cur.getString("Dhuhr");
                int hours = gethours(time);
                int minutes = getminutes(time);
                setAlarm(context, "Dhuhr", hours, minutes,2);
                toasttext = toasttext + "Fajr: " + hours + ":" + minutes + "\n";
            }
            if (prefs.getBoolean("set_tone_asr", false)) {
                String time = cur.getString("Asr");
                int hours = gethours(time);
                int minutes = getminutes(time);
                setAlarm(context, "Asr", hours, minutes, 3);
                toasttext = toasttext + "Fajr: " + hours + ":" + minutes + "\n";
            }
            if (prefs.getBoolean("set_tone_maghrib", false)) {
                String time = cur.getString("Maghrib");
                int hours = gethours(time);
                int minutes = getminutes(time);
                setAlarm(context, "Maghrib", hours, minutes, 4);
                toasttext = toasttext + "Fajr: " + hours + ":" + minutes + "\n";
            }
            if (prefs.getBoolean("set_tone_isha", false)) {
                String time = cur.getString("Isha");
                int hours = gethours(time);
                int minutes = getminutes(time);
                setAlarm(context, "Isha", hours, minutes, 5);
                toasttext = toasttext + "Fajr: " + hours + ":" + minutes + "\n";
            }
            Toasten.toasten(context,"All Alarms set");
            Toasten.toasten(context,toasttext);


        } catch(Exception e){
            Toasten.toasten(context,"Fehler: " + e.getMessage());
        }


         //Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

    }

    private void setAlarm(Context context, String name, int hours, int minutes,int request){
        Intent intent = new Intent(context, NotifyReceiver.class);
        intent.putExtra("prayname", name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, request, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    private int gethours(String time){
        return Integer.parseInt(time.substring(0,2));
    }

    private int getminutes(String time){
        return Integer.parseInt(time.substring(3,5));
    }

}