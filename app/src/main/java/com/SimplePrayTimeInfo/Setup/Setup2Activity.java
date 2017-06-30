package com.SimplePrayTimeInfo.Setup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import com.simplepraytimeinfofree.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Setup2Activity extends Activity {

    public static Activity handleToClose;
    private Spinner s1;
    private Spinner s2;

    public static boolean haveInternet(Context ctx) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        return true;
    }

    @SuppressLint("SimpleDateFormat")
    public static String timeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        String timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
        return "GMT" + timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5);
    }

    public static String timeZoneToInt(String timez) {
        String timezone = timez.substring(3);
        String vorzeichen = timezone.substring(0, 1);
        String timezone1 = timezone.substring(1, 3);
        if (timezone1.startsWith("0")) timezone1 = timezone1.substring(1);
        String timezone2 = timezone.substring(4, 6);

        int t1 = Integer.parseInt(timezone1);
        int t2 = Integer.parseInt(timezone2);
        int t = t1 * 60 + t2;

        return vorzeichen + t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        handleToClose = this;

        setTimeZoneDefault();

        s2 = (Spinner) findViewById(R.id.setup2spinner2);
    }

    private void setTimeZoneDefault() {
        boolean found = false;
        s1 = (Spinner) findViewById(R.id.setup2spinner1);
        String tzstr = timeZone();
        int i = 0;
        for (i = 0; i < s1.getCount() - 1; i++) {
            if (s1.getItemAtPosition(i).toString().equals(tzstr)) {
                found = true;
                break;
            }
        }
        if (found) s1.setSelection(i);
        else s1.setSelection(27);// set Timezone to GMT
        //s1.setSelection(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_setup2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_next:
                getSharedPreferences("TimeZone", MODE_PRIVATE).edit().putString("tz", timeZoneToInt(s1.getSelectedItem().toString())).commit();
                getSharedPreferences("Method", MODE_PRIVATE).edit().putString("m", "" + s2.getSelectedItemPosition()).commit();
                startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

