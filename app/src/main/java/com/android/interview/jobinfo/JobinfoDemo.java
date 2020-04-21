package com.android.interview.jobinfo;

import android.app.job.JobInfo;
import android.content.ComponentName;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import static android.app.job.JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS;

/**
 * 电量优化对JobScheduler的使用在扩展
 * https://www.jianshu.com/p/00e38ee78734
 */
public class JobinfoDemo {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void myJobinfoTest() {
        JobInfo mJobInfo = new JobInfo.Builder(1, new ComponentName(this, JobWakeUpService.class))//自定义Service
                .setPeriodic(1000)//执行周期
                .setMinimumLatency(1000)//最小延时
                .setOverrideDeadline(1000)//最大执行时间
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)//网络类型 NETWORK_TYPE_UNMETERED（wifi 蓝牙）
                .setRequiresCharging(true) //充电时执行
                //设置重试/退避策略 （重试时间，重试时间间隔）
                .setBackoffCriteria(1000, JobInfo.BACKOFF_POLICY_LINEAR)
                .setPersisted(true)//设备重启，任务是否保留
                .setRequiresDeviceIdle(true)//设备空闲时
                //监听url对应数据变化，触发当前任务执行
                .addTriggerContentUri(new JobInfo.TriggerContentUri(Uri.parse(""),FLAG_NOTIFY_FOR_DESCENDANTS))
                //数据变化------->任务执行 最大延迟
                .setTriggerContentMaxDelay(1000L)
                //更新 延迟
                .setTriggerContentUpdateDelay(1000L)
                .build();
    }

}
