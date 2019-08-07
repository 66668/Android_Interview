package com.android.interview;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;

public class MainActivity extends BaseAct {
    PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler;
        Looper looper;
        Timer timer;
        finish();
        ActivityThread activityThread;
    }

}
