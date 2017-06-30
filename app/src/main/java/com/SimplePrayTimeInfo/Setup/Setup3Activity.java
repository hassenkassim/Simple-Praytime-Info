package com.SimplePrayTimeInfo.Setup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.SimplePrayTimeInfo.MainActivity;
import com.simplepraytimeinfofree.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;


public class Setup3Activity extends Activity {


    public static Activity handleToClose;
    String suchtxt = "";
    TextView t1;
    TextView t2;
    Button b1;
    boolean success = true;

    private static String getcurYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return "" + year;
    }

    private static String getSpecDataForTimeJSON(String lat, String lon, String tz, String method, String month, String year)
            throws JSONException {
        String link = "http://praytime.info/getprayertimes.php?lat=" + lat + "&lon=" + lon + "&gmt=" + tz + "&m=" + month + "&y=" + year + "&school=" + method;
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection connection = null;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static boolean haveInternet(Context ctx) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        handleToClose = this;

        if (Setup0Activity.handleToClose != null) Setup0Activity.handleToClose.finish();
        if (Setup1Activity.handleToClose != null) Setup1Activity.handleToClose.finish();
        if (Setup2Activity.handleToClose != null) Setup2Activity.handleToClose.finish();

        t1 = (TextView) findViewById(R.id.setup3textView1);
        t2 = (TextView) findViewById(R.id.setup3textView2);
        b1 = (Button) findViewById(R.id.setup3button1);

        new GetData().execute();
    }

    @Override
    public void onBackPressed() {
        if (success) {
            startActivity(new Intent(Setup3Activity.this, MainActivity.class));
            return;
        } else {
            super.onBackPressed(); // allows standard use of backbutton for page 1
        }

    }

    public class GetData extends AsyncTask<String, Void, Void> {

        String JSONDATA = "";
        String lat = getSharedPreferences("Coordinates", MODE_PRIVATE).getString("lat", "0");
        String lon = getSharedPreferences("Coordinates", MODE_PRIVATE).getString("lon", "0");
        String tz = getSharedPreferences("TimeZone", MODE_PRIVATE).getString("tz", "0");
        String method = getSharedPreferences("Method", MODE_PRIVATE).getString("m", "0");
        String year = getcurYear();
        private ProgressDialog Dialog = new ProgressDialog(Setup3Activity.this);

        protected void onPreExecute() {
            Dialog.setMessage(getResources().getString(R.string.aufbereitet));
            Dialog.setCancelable(false);
            Dialog.show();
        }

        protected Void doInBackground(String... urlss) {
            for (int i = 1; i <= 12; i++) {
                if (haveInternet(getApplicationContext())) {
                    try {
                        JSONDATA = getSpecDataForTimeJSON(lat, lon, tz, method, "" + i, year);
                        getSharedPreferences("Data", MODE_PRIVATE).edit().putString("" + i, JSONDATA).commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    success = false;
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            b1.setVisibility(View.VISIBLE);

            getSharedPreferences("Setup", MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();

            if (success) {
                t1.setText(getResources().getString(R.string.setup3_txt1));
                t2.setText(getResources().getString(R.string.setup3_txt2));
                b1.setText(getResources().getString(R.string.setup3_txt3));
                b1.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Setup3Activity.this, MainActivity.class));
                    }
                });


                getSharedPreferences("Setup", MODE_PRIVATE).edit().putInt("curyear", Integer.parseInt(year)).commit();
            } else {
                t1.setText(getResources().getString(R.string.setup3_txt_fail1));
                t2.setText(getResources().getString(R.string.setup3_txt_fail2));
                b1.setText(getResources().getString(R.string.setup3_txt_fail3));

                b1.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Setup3Activity.this, Setup0Activity.class));
                    }
                });
            }
            Dialog.dismiss();
        }
    }
}