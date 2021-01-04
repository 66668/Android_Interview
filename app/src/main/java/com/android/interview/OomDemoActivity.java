package com.android.interview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.interview.touchevent.ViewGroup1;
import com.android.interview.touchevent.ViewGroup2;

/**
 * 事件分发 模拟
 */
public class OomDemoActivity extends BaseAct {
    private static final String TAG = "SJY_Act";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_touch);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("SJY", "内存泄漏测试" + handler.getClass());
            }
        }, 20000);
    }
}
