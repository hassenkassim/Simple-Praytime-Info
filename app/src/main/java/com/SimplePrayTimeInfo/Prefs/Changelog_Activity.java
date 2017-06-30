package com.SimplePrayTimeInfo.Prefs;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.simplepraytimeinfofree.R;

public class Changelog_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        WebView myWebView = (WebView) findViewById(R.id.ChangelogWebView);
        String content = "";

        setTitle(getResources().getString(R.string.setchangelog));
        content = "<html><head><title>Changelog: Simple Praytime Info</title></head><body><p>&nbsp;</p><p><strong>09.03.2014: Version 1.2:</strong></p><ul><li>Widget & Lockscreen Widget hinzugef�gt</li></ul><p><strong>16.01.2014: Version 1.1:</strong></p><ul><li>einige Bugs beseitigt</li></ul><p><strong>15.01.2014: Version 1.0:</strong></p><ul><li>erste ver&ouml;ffentlichung</li></ul></body></html>";
        myWebView.loadData(content, "text/html", "utf-8");

    }

}