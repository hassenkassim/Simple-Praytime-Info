package com.SimplePrayTimeInfo.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.SimplePrayTimeInfo.MainActivity;
import com.simplepraytimeinfofree.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class PraytimeWidget extends AppWidgetProvider {
    Context con;
    Intent intent;
    PendingIntent pendingIntent;

    @Override
    public void onEnabled(Context context) {
        con = context;
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        con = context;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 900000); //alle 15 Minuten aktualisieren
    }

    private class MyTime extends TimerTask {
        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        ComponentName thisWidget;

        public MyTime(Context context, AppWidgetManager appWidgetManager) {
            this.appWidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            intent = new Intent(con, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(con, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widgetview, pendingIntent);

            thisWidget = new ComponentName(context, PraytimeWidget.class);
        }

        private int getcurMonth() {
            Calendar calendar = Calendar.getInstance();
            return (calendar.get(Calendar.MONTH) + 1);
        }

        private int getcurDay() {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.DAY_OF_MONTH);
        }


        @Override
        public void run() {
            JSONObject monthjson;
            JSONObject cur = new JSONObject();
            int month = getcurMonth();
            int day = getcurDay();
            String monthdata = con.getSharedPreferences("Data", con.MODE_PRIVATE)
                    .getString("" + month, "{}");
            String fajrtime = "";
            String dhuhrtime = "";
            String asrtime = "";
            String maghribtime = "";
            String ishatime = "";

            try {
                monthjson = new JSONObject(monthdata);
                cur = monthjson.getJSONObject(day + "");

                fajrtime = cur.getString("Fajr");
                dhuhrtime = cur.getString("Dhuhr");
                asrtime = cur.getString("Asr");
                maghribtime = cur.getString("Maghrib");
                ishatime = cur.getString("Isha");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            remoteViews.setTextViewText(R.id.widget_textview3, fajrtime);
            remoteViews.setTextViewText(R.id.widget_textview5, dhuhrtime);
            remoteViews.setTextViewText(R.id.widget_textview7, asrtime);
            remoteViews.setTextViewText(R.id.widget_textview9, maghribtime);
            remoteViews.setTextViewText(R.id.widget_textview11, ishatime);

            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        }

    }
}