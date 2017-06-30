package com.SimplePrayTimeInfo.Setup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.SimplePrayTimeInfo.MainActivity;
import com.simplepraytimeinfofree.R;

public class Setup0Activity extends Activity {

    public static Activity handleToClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup0);

        handleToClose = this;

        MainActivity.handleToClose.finish();

        Button startbtn = (Button) findViewById(R.id.setup0button1);
        startbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(Setup0Activity.this, Setup1Activity.class));

            }
        });
    }

}