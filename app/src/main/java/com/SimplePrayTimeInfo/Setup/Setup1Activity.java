package com.SimplePrayTimeInfo.Setup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.SimplePrayTimeInfo.Location.MyLocation;
import com.SimplePrayTimeInfo.Message.Toasten;
import com.simplepraytimeinfofree.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class Setup1Activity extends Activity {

    public static Activity handleToClose;
    static Button gpslook;
    private static LocationManager locationManager;
    private static TextView lat;
    private static TextView lon;
    String suchtxt = "";
    ArrayList<String> locations;
    ArrayList<String> locationslat;
    ArrayList<String> locationslon;
    Handler handler;
    EditText e1;
    EditText such_edtxt;
    String searchtext = "";
    int locstate = 1;
    SearchView searchView;
    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;

    private static JSONArray getloc(String suchbegriff)
            throws JSONException {

        URL url = null;
        try {
            url = new URL(
                    "http://nominatim.openstreetmap.org/search?q=" + suchbegriff + "&format=json");
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
        return new JSONArray(builder.toString());
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
        setContentView(R.layout.activity_setup1);


        handleToClose = this;

        locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);


        e1 = (EditText) findViewById(R.id.setup1editText3);

        lat = (EditText) findViewById(R.id.setup1editText1);
        lon = (EditText) findViewById(R.id.setup1editText2);

        lat.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (verifyCoordinateslat(lat.getText().toString())) {
                    lat.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                } else {
                    lat.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        lon.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (verifyCoordinateslon(lon.getText().toString())) {
                    lon.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                } else {
                    lon.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        such_edtxt = (EditText) findViewById(R.id.setup1editText3);

        such_edtxt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (such_edtxt.getText().toString().compareTo("") != 0) {
                    suchtxt = getcleanString(such_edtxt.getText().toString());
                    new SearchLookUp().execute(suchtxt);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mainListView = (ListView) findViewById(R.id.listView2);

        // Create and populate a List of planet names.
        String[] planets = new String[]{""};
        locations = new ArrayList<String>();
        locationslat = new ArrayList<String>();
        locationslon = new ArrayList<String>();
        locations.addAll(Arrays.asList(planets));

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, locations);

        mainListView.setAdapter(listAdapter);

        onCreateloockup();

    }

    public String getcleanString(String input) {
        String str = input;
        str = str.replaceAll(" ", "%20");
        str = str.replaceAll("ß", "ss");
        str = str.replaceAll("ü", "ue");
        str = str.replaceAll("Ü", "ue");
        str = str.replaceAll("ä", "ae");
        str = str.replaceAll("Ä", "ae");
        str = str.replaceAll("ö", "oe");
        str = str.replaceAll("Ö", "oe");
        str = str.replaceAll("Ç", "C");
        str = str.replaceAll("ç", "c");
        str = str.replaceAll("Ğ", "G");
        str = str.replaceAll("ğ", "g");
        str = str.replaceAll("İ", "I");
        str = str.replaceAll("ı", "i");
        str = str.replaceAll("Ş", "S");
        str = str.replaceAll("ş", "s");
        return str;
    }

    // überprüfe ob lat (-90 - 90)
    public boolean verifyCoordinateslat(String lat) {
        Double latD;
        try {
            latD = Double.parseDouble(lat);
        } catch (NumberFormatException e) {
            return false;
        }
        if (latD > -90.0 && latD < 90.0) return true;
        return false;
    }

    // überprüfe ob lon (-180 - 180)
    public boolean verifyCoordinateslon(String lon) {
        Double lonD;
        try {
            lonD = Double.parseDouble(lon);
        } catch (NumberFormatException e) {
            return false;
        }
        if (lonD > -180.0 && lonD < 180.0) return true;
        return false;
    }

    public void onCreateloockup() {
        lat.setText("..");
        lon.setText("..");
        locstate = 2;
        invalidateOptionsMenu();
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                //Got the location!
                lat.setText("" + location.getLatitude());
                lon.setText("" + location.getLongitude());
                locstate = 1;
                invalidateOptionsMenu();
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getApplicationContext(), locationResult);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locstate = 1;
            invalidateOptionsMenu();
        } else {
            buildAlertMessageNoGps();
            locstate = 0;
            invalidateOptionsMenu();
        }
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem locitem = menu.findItem(R.id.action_location);
        if (locstate == 0)
            locitem.setIcon(getResources().getDrawable(R.drawable.ic_action_location_off));
        if (locstate == 1)
            locitem.setIcon(getResources().getDrawable(R.drawable.ic_action_location_found));
        if (locstate == 2)
            locitem.setIcon(getResources().getDrawable(R.drawable.ic_action_location_searching));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_setup1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_next:
                if (verifyCoordinateslat(lat.getText().toString()) && verifyCoordinateslon(lon.getText().toString())) {
                    getSharedPreferences("Coordinates", MODE_PRIVATE).edit().putString("lat", lat.getText().toString()).commit();
                    getSharedPreferences("Coordinates", MODE_PRIVATE).edit().putString("lon", lon.getText().toString()).commit();
                    startActivity(new Intent(Setup1Activity.this, Setup2Activity.class));
                } else {
                    Toasten.toasten(getApplicationContext(), getResources().getString(R.string.nocoordinates));
                }
                return true;

            case R.id.action_location:
                onCreateloockup();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SearchLookUp extends AsyncTask<String, Void, Void> {

        //private ProgressDialog Dialog = new ProgressDialog(Setup1Activity.this);
        JSONArray json;
        boolean found = false;

        protected void onPreExecute() {
            //Dialog.setMessage("searching..");
            //Dialog.show();

        }

        protected Void doInBackground(String... urlss) {
            if (haveInternet(getApplicationContext())) {
                locations.clear();
                locationslat.clear();
                locationslon.clear();
                try {
                    json = getloc(suchtxt);
                    if (json.length() > 0) {
                        for (int i = 0; i < json.length(); i++) {
                            locations.add(json.getJSONObject(i).getString("display_name"));
                            locationslat.add(json.getJSONObject(i).getString("lat"));
                            locationslon.add(json.getJSONObject(i).getString("lon"));
                        }
                        mainListView.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                                if (!(locationslat.get(pos).compareTo("0") == 0 && locationslon.get(pos).compareTo("0") == 0)) {
                                    lat.setText(locationslat.get(pos));
                                    lon.setText(locationslon.get(pos));
                                } else {
                                    locations.clear();
                                    locationslat.clear();
                                    locationslon.clear();
                                    listAdapter.notifyDataSetChanged();
                                }

                            }
                        });
                        found = true;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            if (!found) {

            }

            listAdapter.notifyDataSetChanged();
        }
    }

}