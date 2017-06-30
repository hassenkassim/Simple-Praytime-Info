package com.SimplePrayTimeInfo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.SimplePrayTimeInfo.Alarm.AlarmReceiver;
import com.SimplePrayTimeInfo.Message.Toasten;
import com.SimplePrayTimeInfo.Notify.Notify;
import com.SimplePrayTimeInfo.Prefs.Prefs_Activity;
import com.SimplePrayTimeInfo.Setup.Setup0Activity;
import com.SimplePrayTimeInfo.Setup.Setup3Activity;
import com.SimplePrayTimeInfo.Tools.DateHijri;
import com.simplepraytimeinfofree.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends Activity {

    final static private long ONE_SECOND = 1000;
    final static private long FIVE_SECONDS = ONE_SECOND * 5;
    private PendingIntent pendingIntent;

    public static Activity handleToClose;
    boolean threadstop = false;
    TableLayout tl2;
    Boolean sunrise = false;
    Boolean sunset = false;
    Boolean imsaak = false;
    private TextView nextpraytime;
    private TextView nextpraytime2;
    private Thread thread;

    public static int getcurYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getcurMonth() {
        Calendar calendar = Calendar.getInstance();
        return (calendar.get(Calendar.MONTH) + 1);
    }

    public static int getcurDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static String getcurtimestr() {
        Time time = new Time();
        time.setToNow();
        return time.hour + ":" + time.minute + ":" + time.second;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("set_sunrise", false)) {
            sunrise = true;
        }
        if (prefs.getBoolean("set_sunset", false)) {
            sunset = true;
        }
        if (prefs.getBoolean("set_imsaak", false)) {
            imsaak = true;
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!Thread.interrupted())
                    try {
                        runOnUiThread(new Runnable() // start actions in UI
                                // thread
                        {

                            @Override
                            public void run() {
                                if (threadstop)
                                    return;
                                else
                                    setcounter(); // this action have to be in
                                // UI thread
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // ooops
                    }
            }
        }); // the while thread will start in BG thread
        thread.start();

        tl2 = (TableLayout) findViewById(R.id.TABLE2);


        handleToClose = this;

        if (Setup3Activity.handleToClose != null)
            Setup3Activity.handleToClose.finish();

        if (getSharedPreferences("Setup", MODE_PRIVATE).getBoolean("firstrun",
                true)) {
            startActivity(new Intent(MainActivity.this, Setup0Activity.class));
        }

        if (getSharedPreferences("Setup", MODE_PRIVATE).getInt("curyear",
                getcurYear()) != getcurYear()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setTitle(getResources().getString(R.string.achtung))
                    .setMessage(
                            getResources().getString(R.string.datenbestand))
                    .setCancelable(false)
                    .setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    startActivity(new Intent(MainActivity.this,
                                            Setup3Activity.class));
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        TextView main_txt1 = (TextView) findViewById(R.id.main_txt1);
        main_txt1.setText(getResources().getString(R.string.today) + " " + DateHijri.writeIslamicDate());

        nextpraytime = (TextView) findViewById(R.id.main_txt2);
        nextpraytime2 = (TextView) findViewById(R.id.main_txt3);
        nextpraytime2.setText(getResources().getString(R.string.praytimetoday));

    }

    public void setcounter() {
        Calendar calendar = Calendar.getInstance();
        JSONObject monthjson;
        JSONObject cur = new JSONObject();
        int month = getcurMonth();
        int day = getcurDay();

        String curtimestr = getcurtimestr();

        String monthdata = getSharedPreferences("Data", MODE_PRIVATE)
                .getString("" + month, "{}");

        try {
            monthjson = new JSONObject(monthdata);
            cur = monthjson.getJSONObject(day + "");

            ArrayList<String> timesofdayas = new ArrayList<String>();
            if (imsaak) timesofdayas.add(cur.getString("Imsaak"));
            timesofdayas.add(cur.getString("Fajr") + ":00");
            if (sunrise) timesofdayas.add(cur.getString("Sunrise"));
            timesofdayas.add(cur.getString("Dhuhr") + ":00");
            timesofdayas.add(cur.getString("Asr") + ":00");
            timesofdayas.add(cur.getString("Maghrib") + ":00");
            if (sunset) timesofdayas.add(cur.getString("Sunset"));
            timesofdayas.add(cur.getString("Isha") + ":00");

            String nextprayingtime = getNextPrayingtime(timesofdayas, curtimestr);
            //toasten(getApplicationContext(),getNextPrayingtime(timesofdayas, curtimestr));
            //getNextPrayingtime(timesofdayas, curtimestr);
            if (nextprayingtime.compareTo("TF") == 0) {

                int daysInMonth = calendar
                        .getActualMaximum(Calendar.DAY_OF_MONTH); // 28

                if (day == daysInMonth && month == 12) {
                    day = 1;
                    month = 1;
                } else if (day == daysInMonth) {
                    day = 1;
                    month += 1;
                } else {
                    day += 1;
                }

                monthdata = getSharedPreferences("Data", MODE_PRIVATE)
                        .getString("" + month, "{}");
                monthjson = new JSONObject(monthdata);
                cur = monthjson.getJSONObject(day + "");
                nextprayingtime = cur.getString("Fajr");
            }


            tl2.removeAllViews();

            ArrayList<String> times = new ArrayList<String>();
            if (imsaak) times.add(cur.getString("Imsaak"));
            times.add(cur.getString("Fajr"));
            if (sunrise) times.add(cur.getString("Sunrise"));
            times.add(cur.getString("Dhuhr"));
            times.add(cur.getString("Asr"));
            if (sunset) times.add(cur.getString("Sunset"));
            times.add(cur.getString("Maghrib"));
            times.add(cur.getString("Isha"));

            ArrayList<String> timenames = new ArrayList<String>();
            if (imsaak) timenames.add("Imsaak");
            timenames.add("Fajr");
            if (sunrise) timenames.add(getResources().getString(R.string.sunrise));
            timenames.add("Dhuhr");
            timenames.add("Asr");
            if (sunset) timenames.add(getResources().getString(R.string.sunset));
            timenames.add("Maghrib");
            timenames.add("Isha");


            //String[] times = {cur.getString("Fajr") + ":00", cur.getString("Dhuhr") + ":00",cur.getString("Asr") + ":00", cur.getString("Maghrib") + ":00", cur.getString("Isha") + ":00" };
            //String[] timenames = {"Fajr","Dhuhr","Asr","Maghrib","Isha" };


            int pointer;
            for (pointer = 0; pointer < times.size(); pointer++) {
                if (nextprayingtime.contains(times.get(pointer))) break;
            }

            for (int i = 0; i < times.size(); i++) {
                if (i == pointer) addTableRow(timenames.get(i), times.get(i), true);
                else addTableRow(timenames.get(i), times.get(i), false);
            }

            int[] curtimetmp = {
                    Integer.parseInt(
                            curtimestr.substring(0, curtimestr.indexOf(":")),
                            10),
                    Integer.parseInt(curtimestr.substring(
                            curtimestr.indexOf(":") + 1,
                            curtimestr.lastIndexOf(":")), 10),
                    Integer.parseInt(curtimestr.substring(curtimestr
                            .lastIndexOf(":") + 1), 10)};

            int[] nextprayingtimetmp = {
                    Integer.parseInt(nextprayingtime.substring(0, 2), 10),
                    Integer.parseInt(nextprayingtime.substring(3, 5), 10)};

            int leftsecondsint = 0 - curtimetmp[2];
            if (leftsecondsint < 0) {
                leftsecondsint += 60;
                if (curtimetmp[1] < 60) {
                    curtimetmp[1]++;
                } else {
                    curtimetmp[1] = 0;
                    curtimetmp[0]++;
                }
            }

            int leftminint = nextprayingtimetmp[1] - curtimetmp[1];

            if (leftminint < 0) {
                leftminint += 60;
                curtimetmp[0]++;
            }

            int lefthint = nextprayingtimetmp[0] - curtimetmp[0];
            if (lefthint < 0)
                lefthint += 24;

            String leftsecstr = "" + leftsecondsint;
            String leftminstr = "" + leftminint;
            String lefthstr = "" + lefthint;

            if (leftsecstr.length() < 2)
                leftsecstr = "0" + leftsecstr;
            if (leftminstr.length() < 2)
                leftminstr = "0" + leftminstr;
            if (lefthstr.length() < 2)
                lefthstr = "0" + lefthstr;

            nextpraytime.setText(Html.fromHtml(getResources().getString(R.string.nextpraytime) + " <b>" + lefthstr + ":"
                    + leftminstr + ":" + leftsecstr + "</b>"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getNextPrayingtime(ArrayList<String> timesofday, String curtime) {

        int i;
        String[] cur = curtime.split(":");

        String[][] times = new String[timesofday.size()][3];
        for (int j = 0; j < timesofday.size(); j++) {
            times[j] = timesofday.get(j).split(":");
        }

        int[] curint = {Integer.parseInt(cur[0], 10), Integer.parseInt(cur[1], 10)};

        for (i = 0; i < timesofday.size(); ) {

            if (curint[0] > Integer.parseInt(times[i][0], 10)) {
                i++;
                continue;
            } else if (curint[0] < Integer.parseInt(times[i][0], 10)) {
                break;
            } else {
                if (curint[1] >= Integer.parseInt(times[i][1], 10)) {
                    i++;
                    break;
                } else
                    break;
            }
        }

        if (i > (timesofday.size() - 1))
            return "TF";

        return timesofday.get(i);
    }

    private void addTableRow(String a, String b, boolean marker) {

        LayoutInflater inflater = getLayoutInflater();
        TableRow tr2 = (TableRow) inflater.inflate(R.layout.row_main, tl2, false);

        // Add First Column
        TextView one2 = (TextView) tr2.findViewById(R.id.one2);
        one2.setText(a);
        TextView two2 = (TextView) tr2.findViewById(R.id.two2);
        two2.setText(b);

        if (marker) {
            one2.setBackground(getResources().getDrawable(R.drawable.cell_shapeyellow));
            two2.setBackground(getResources().getDrawable(R.drawable.cell_shapeyellow));
            one2.setTypeface(one2.getTypeface(), Typeface.BOLD);
            two2.setTypeface(two2.getTypeface(), Typeface.BOLD);
        }

        tl2.addView(tr2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        thread.interrupt();
        threadstop = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        threadstop = false;
        super.onResume();
    }

    @Override
    protected void onStop() {
        thread.interrupt();
        threadstop = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        thread.interrupt();
        // thread = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_monthly:
                startActivity(new Intent(MainActivity.this, MonthlyActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, Prefs_Activity.class));
                return true;
            case R.id.action_qibla:
                //startActivity(new Intent(MainActivity.this, Qibla_Activity.class));
                //Notify.notifying(getApplicationContext(),"Notification!", "Qibla started!", 0);
                return true;
            case R.id.action_Alarm:
                setAlarm();
                return true;
            case R.id.action_UnAlarm:
                UnsetAlarm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setAlarm(){

        int interval = 1000 * 60 * 60 * 24;
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 123123456, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis() + 10000); // + 10 Sek

        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.HOUR_OF_DAY, hours);
        //calendar.set(Calendar.MINUTE, minutes);

        /* Repeating on every 24 hours interval */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pendingIntent);
        String text = "Alarm set in 10 Seconds";
        Toasten.toasten(this, text);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

    }

    public void UnsetAlarm(){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void startAlert() {
        int i = 5;
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
        //        + (i * 1000), pendingIntent);
        /* Repeating on every 24 hours interval */

        /* Set the alarm to start at XX:XX AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Calendar.HOUR_OF_DAY);
        calendar.set(Calendar.MINUTE, Calendar.MINUTE + 2);

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        //        3000, pendingIntent);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm set in one Minute",
                Toast.LENGTH_LONG).show();
    }

}