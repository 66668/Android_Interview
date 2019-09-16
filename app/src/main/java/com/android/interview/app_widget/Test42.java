package com.android.interview.app_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.android.interview.MainActivity;
import com.android.interview.R;

/**
 * 模拟桌面音乐播放
 */
public class Test42 extends AppWidgetProvider {

    private final static String MYAPP_START = "android.appwidget.action.MYAPP_START";
    private final static String MYAPP_STOP = "android.appwidget.action.MYAPP_STOP";
    private final static String MYAPP_PLAY = "android.appwidget.action.MYAPP_PLAY";


    /**
     * 接收广播的回调函数
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        //接收广播，触发界面跳转
        if (action.equals(MYAPP_START)) {//播放
            Intent intent1 = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("start", "start" + "--onReceive");
            bundle.putString("stop", "stop");
            bundle.putString("play", "play");
            intent1.putExtras(bundle);
            context.startActivity(intent1);

            Log.d("SJY", "onReceive--MYAPP_START--startActivity");
        }

        if (action.equals(MYAPP_STOP)) {
            Intent intent1 = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("start", "start");
            bundle.putString("stop", "stop" + "--onReceive");
            bundle.putString("play", "play");
            intent1.putExtras(bundle);
            context.startActivity(intent1);
            Log.d("SJY", "onReceive--MYAPP_STOP--startActivity");
        }

        if (action.equals(MYAPP_PLAY)) {
            Intent intent1 = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("start", "start");
            bundle.putString("stop", "stop");
            bundle.putString("play", "play" + "--onReceive");
            intent1.putExtras(bundle);
            context.startActivity(intent1);
            Log.d("SJY", "onReceive--MYAPP_PLAY--startActivity");
        }

        super.onReceive(context, intent);
    }


    /**
     * 更新所有的 widget
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_42);
        views.setTextViewText(R.id.tv_title, "模拟音乐小部件——标题");
        views.setTextViewText(R.id.tv_start, "开始");
        views.setTextViewText(R.id.tv_stop, "结束");
        views.setTextViewText(R.id.tv_play, "播放");
        //添加跳转事件
        //（1）start点击发送广播
        Intent startIntent = new Intent(context, MainActivity.class);//目标act
        startIntent.setAction(MYAPP_START);
        Bundle starBundle = new Bundle();
        starBundle.putString("start", "start" + "--updateAppWidget");
        starBundle.putString("stop", "stop");
        starBundle.putString("play", "play");
        startIntent.putExtras(starBundle);
        PendingIntent startPendingIntent = PendingIntent.getActivity(context, 0, startIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_start, startPendingIntent);

        //（2）stop点击发送广播
        Intent stopIntent = new Intent(context, MainActivity.class);//目标act
        stopIntent.setAction(MYAPP_STOP);
        Bundle stopBundle = new Bundle();
        stopBundle.putString("start", "start");
        stopBundle.putString("stop", "stop" + "--updateAppWidget");
        stopBundle.putString("play", "play");
        stopIntent.putExtras(stopBundle);
        PendingIntent stopPendingIntent = PendingIntent.getActivity(context, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_stop, stopPendingIntent);

        //（3）play点击发送广播
        Intent playIntent = new Intent(context, MainActivity.class);//目标act
        startIntent.setAction(MYAPP_PLAY);
        Bundle playBundle = new Bundle();
        playBundle.putString("start", "start" );
        playBundle.putString("stop", "stop");
        playBundle.putString("play", "play"+ "--updateAppWidget");
        playIntent.putExtras(playBundle);
        PendingIntent playPendingIntent = PendingIntent.getActivity(context, 0, playIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_play, playPendingIntent);

        //（4）title点击发送广播
        Intent titleIntent = new Intent(context, MainActivity.class);//目标act
        startIntent.setAction(MYAPP_PLAY);
        Bundle titleBundle = new Bundle();
        titleBundle.putString("start", "start" + "--updateAppWidget");
        titleBundle.putString("stop", "stop");
        titleBundle.putString("play", "play");
        titleIntent.putExtras(playBundle);
        PendingIntent titlePendingIntent = PendingIntent.getBroadcast(context, 0, playIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_title, titlePendingIntent);

        Log.d("SJY", "onUpdate--updateAppWidget");
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * service更新AppWidget
     *
     * @return
     */
    public static RemoteViews servcieUpdateAppWidget(Context context) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_42);

        /***************************更新控件*************************************/
        remoteViews.setTextViewText(R.id.tv_title, "模拟后台数据更新");

        remoteViews.setViewVisibility(R.id.tv_play, View.VISIBLE);

        remoteViews.setViewVisibility(R.id.tv_start, View.VISIBLE);

        remoteViews.setViewVisibility(R.id.tv_stop, View.VISIBLE);

        int[] appIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, Test42.class));

        appWidgetManager.updateAppWidget(appIds, remoteViews);
        Log.d("SJY", "service更新AppWidget");
        return remoteViews;

    }


    /**
     * 部件拖到桌面触发
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        //
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        Intent intent = new Intent(context, UpdateWidgetService.class);
        context.startService(intent);
    }

    /**
     * 当 widget 被初次添加 或者 当 widget 的大小被改变时，被调用
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param newOptions
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * widget被删除时调用
     *
     * @param context
     * @param appWidgetIds
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d("SJY", "onDeleted");
    }

    /**
     * 第一个widget被创建时调用
     *
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        //开启service
        Intent intent = new Intent(context, UpdateWidgetService.class);
        context.startService(intent);
        Log.d("SJY", "onEnabled--开启service");
    }

    /**
     * 最后一个widget被删除时调用
     *
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        //终止开启的服务
        Intent intent = new Intent(context, UpdateWidgetService.class);
        context.stopService(intent);
        Log.d("SJY", "onDisabled--终止开启的服务");
    }

}

