package com.SimplePrayTimeInfo.Prefs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.SimplePrayTimeInfo.Calculation_Activity;
import com.SimplePrayTimeInfo.Setup.Setup1Activity;
import com.simplepraytimeinfofree.R;


/**
 * Demonstration of PreferenceFragment, showing a single fragment in an
 * activity.
 */
public class Prefs_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.activity_prefs);

            /*ListPreference dataPref = (ListPreference) findPreference("set_schriftgroesse");

            if(dataPref.getValue() == null){
                dataPref.setValueIndex(1); //set to index of your default value
            }
            //dataPref.setValueIndex(0);
*/
            Preference preferecesBenTon = findPreference("set_tone");
            Preference preferecesBenFajr = findPreference("set_tone_fajr");
            Preference preferecesBenDhuhr = findPreference("set_tone_dhuhr");
            Preference preferecesBenAsr = findPreference("set_tone_asr");
            Preference preferecesBenMaghrib = findPreference("set_tone_maghrib");
            Preference preferecesBenIsha = findPreference("set_tone_isha");
            Preference preferecesBenVibration = findPreference("set_vibration");
            Preference preferecesBenVibrationtime = findPreference("set_vibration_time");

            Preference preferecesChangelog = findPreference("changelog");
            preferecesChangelog.setIntent(new Intent(getActivity(), Changelog_Activity.class).putExtra("section", 0));

            Preference preferecesSetup = findPreference("setup");
            preferecesSetup.setIntent(new Intent(getActivity(), Setup1Activity.class));


            Preference preferecesCalculation = findPreference("set_calculation");
            preferecesCalculation.setIntent(new Intent(getActivity(), Calculation_Activity.class));


            Preference preferecesFeedback = findPreference("feedback");
            preferecesFeedback
                    .setOnPreferenceClickListener(new OnPreferenceClickListener() {

                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            final EditText input = new EditText(getActivity());
                            input.setMinimumHeight(250);
                            input.setHint("Feedback");
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    getActivity())
                                    .setView(input)
                                            // Add action buttons
                                    .setPositiveButton(
                                            "Senden",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {
                                                    if (input.getText()
                                                            .toString()
                                                            .length() > 10) {
                                                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                                                        String[] recipients = new String[]{"hass3n@gmail.com", "",};
                                                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
                                                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Eslam f�r Android - App-Feedback");
                                                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, input.getText().toString() + "\n---\n" + android.os.Build.MODEL + ", " + android.os.Build.VERSION.RELEASE);
                                                        emailIntent.setType("text/plain");
                                                        startActivity(Intent.createChooser(emailIntent, "Sende Feedback mit: "));
                                                    } else if (input.getText()
                                                            .toString()
                                                            .length() < 11 && input.getText()
                                                            .toString()
                                                            .length() > 0) {
                                                        Toast toast = Toast.makeText(getActivity(), "Schreibe doch bitte etwas konstruktives :)", Toast.LENGTH_LONG);
                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                        toast.show();
                                                    } else {
                                                        Toast toast = Toast.makeText(getActivity(), "Willst du mir einen leere Feedback schicken? :), �berlege es dir doch nochmal.", Toast.LENGTH_LONG);
                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                        toast.show();
                                                    }

                                                }
                                            })
                                    .setNegativeButton(
                                            "Abbrechen",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {

                                                }
                                            }).setTitle("Feedback senden");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return false;
                        }
                    });

            Preference preferecesBewerten = findPreference("bewerten");
            preferecesBewerten
                    .setIntent(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.simplepraytimeinfofree")));
        }
    }

}