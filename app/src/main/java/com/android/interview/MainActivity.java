package com.android.interview;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends BaseAct {
    ActivityManager manager;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        appWidgetTest();
    }

    private void appWidgetTest() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String start = bundle.getString("start");
            String stop = bundle.getString("stop");
            String play = bundle.getString("play");
            //
            Log.d("SJY", "start=" + start + "\nstop=" + stop + "\npaly=" + play);
        }
    }
}
