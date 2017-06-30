package com.SimplePrayTimeInfo;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.simplepraytimeinfofree.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MonthlyActivity extends Activity {

    TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        //setTitle(getResources().getString(R.string.action_monthly));
        tl = (TableLayout) findViewById(R.id.TABLE);

        addTableRow("Day", "Fajr", "Dhuhr", "Asr", "Maghrib", "Isha");

        fillTablewithdata();

    }

    private void fillTablewithdata() {
        JSONObject monthjson = new JSONObject();
        JSONObject cur = new JSONObject();
        int month = getcurMonth();

        String day = "";
        String faj = "";
        String dhu = "";
        String asr = "";
        String mag = "";
        String ish = "";

        String monthdata = getSharedPreferences("Data", MODE_PRIVATE).getString("" + month, "{}");


        try {
            monthjson = new JSONObject(monthdata);
            int counter = 1;

            while (((cur = monthjson.getJSONObject("" + counter)) != null)) {
                day = counter + "";
                faj = cur.getString("Fajr");
                dhu = cur.getString("Dhuhr");
                asr = cur.getString("Asr");
                mag = cur.getString("Maghrib");
                ish = cur.getString("Isha");
                addTableRow(day, faj, dhu, asr, mag, ish);
                counter++;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addTableRow(String a, String b, String c, String d, String e, String f) {

        LayoutInflater inflater = getLayoutInflater();
        TableRow tr = (TableRow) inflater.inflate(R.layout.row, tl, false);

        TextView one = (TextView) tr.findViewById(R.id.one);
        one.setText(a);
        TextView two = (TextView) tr.findViewById(R.id.two);
        two.setText(b);
        TextView three = (TextView) tr.findViewById(R.id.thr);
        three.setText(c);
        TextView four = (TextView) tr.findViewById(R.id.fou);
        four.setText(d);
        TextView five = (TextView) tr.findViewById(R.id.fiv);
        five.setText(e);
        TextView six = (TextView) tr.findViewById(R.id.six);
        six.setText(f);

        int curday = getcurDay();
        if (a.compareTo(curday + "") == 0) {
            one.setBackground(getResources().getDrawable(R.drawable.cell_shapeyellow));
            two.setBackground(getResources().getDrawable(R.drawable.cell_shapeyellow));
            three.setBackground(getResources().getDrawable(R.drawable.cell_shapeyellow));
            four.setBackground(getResources().getDrawable(R.drawable.cell_shapeyellow));
            five.setBackground(getResources().getDrawable(R.drawable.cell_shapeyellow));
            six.setBackground(getResources().getDrawable(R.drawable.cell_shapeyellow));
            one.setTypeface(one.getTypeface(), Typeface.BOLD);
            two.setTypeface(two.getTypeface(), Typeface.BOLD);
            three.setTypeface(three.getTypeface(), Typeface.BOLD);
            four.setTypeface(four.getTypeface(), Typeface.BOLD);
            five.setTypeface(five.getTypeface(), Typeface.BOLD);
            six.setTypeface(six.getTypeface(), Typeface.BOLD);
        }
        if (a.compareTo("Day") == 0) {
            one.setBackground(getResources().getDrawable(R.drawable.cell_shapegray));
            two.setBackground(getResources().getDrawable(R.drawable.cell_shapegray));
            three.setBackground(getResources().getDrawable(R.drawable.cell_shapegray));
            four.setBackground(getResources().getDrawable(R.drawable.cell_shapegray));
            five.setBackground(getResources().getDrawable(R.drawable.cell_shapegray));
            six.setBackground(getResources().getDrawable(R.drawable.cell_shapegray));
            one.setTypeface(one.getTypeface(), Typeface.BOLD);
            two.setTypeface(two.getTypeface(), Typeface.BOLD);
            three.setTypeface(three.getTypeface(), Typeface.BOLD);
            four.setTypeface(four.getTypeface(), Typeface.BOLD);
            five.setTypeface(five.getTypeface(), Typeface.BOLD);
            six.setTypeface(six.getTypeface(), Typeface.BOLD);
        }

        tl.addView(tr);
    }

    private int getcurMonth() {
        Calendar calendar = Calendar.getInstance();
        return (calendar.get(Calendar.MONTH) + 1);
    }

    private int getcurDay() {
        Calendar calendar = Calendar.getInstance();
        return (calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_monthly, menu);
        return true;
    }
}