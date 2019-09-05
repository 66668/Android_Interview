package com.android.interview;

import android.app.ActivityManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends BaseAct {
    ActivityManager manager;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);

    }
}
