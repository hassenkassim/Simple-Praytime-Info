package com.SimplePrayTimeInfo.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.SimplePrayTimeInfo.Message.Toasten;
import com.SimplePrayTimeInfo.Notify.Notify;
import com.simplepraytimeinfofree.R;

import java.net.URI;

/**
 * Created by HassenKassim on 04.01.15.
 */
public class NotifyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String prayname = getPrayname(intent);
        boolean vibration = getVibration(context);
        int vibrationbeforetime = getvibrateTime(context);
        int vibrationsduration = 300;

        Notify.notifying(context, "Gebetszeit", "Zeit für das " + prayname + " Gebet", 1, vibration, vibrationbeforetime, vibrationsduration);
        //if(vibration) vibrate(context, 300);
    }





    private String getPrayname(Intent intent){
        String prayname = "";
        try{
            prayname = intent.getStringExtra("prayname");
        }
        catch (Exception e){
            prayname = "Gebet";
        }
        return prayname;
    }

    private boolean getVibration(Context ctx){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean("set_vibration", false);
    }
    private int getvibrateTime(Context ctx){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return Integer.parseInt(prefs.getString("set_vibration_time" , "0"));
    }
}
