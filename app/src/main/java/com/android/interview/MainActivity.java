package com.android.interview;

import android.app.ActivityManager;
import android.arch.lifecycle.Lifecycle;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends BaseAct {
    TextView tv;

    Handler handler;
    Looper looper;
    Bitmap bitmap;
    Thread thread;
    LocalBroadcastManager localBroadcastManager;
    ActivityManager manager;
    Lifecycle lifecycle;
    Class c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
