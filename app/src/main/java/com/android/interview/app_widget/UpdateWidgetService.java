package com.android.interview.app_widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * app 向widget 发送更新服务
 * <p>
 * （Manifest中需要注册）
 */
public class UpdateWidgetService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private AppWidgetManager widgetManager;

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        //
        widgetManager = AppWidgetManager.getInstance(getApplicationContext());
        //
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Test42.servcieUpdateAppWidget(getApplicationContext());
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 5000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("SJY", "服务onDestroy");
        timer.cancel();
        timer = null;
        timerTask = null;
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }
}
