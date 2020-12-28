package com.android.interview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.MessageQueue;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.interview.touchevent.ViewGroup1;
import com.android.interview.touchevent.ViewGroup2;

/**
 * 事件分发 模拟
 */
public class TouchEventActivity extends BaseAct {
    private static final String TAG = "SJY_Act";
    EditText et_input;
    Button btn_1;
    ViewGroup1 ly_1;
    ViewGroup2 ly_2;
    Message message;
    Handler handler;
    MessageQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_touch);
        btn_1 = findViewById(R.id.btn_1);
        ly_1 = findViewById(R.id.ly_1);
        ly_2 = findViewById(R.id.ly_2);

        ly_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ViewGroup1-OnClickListener");
                ly_1.invalidate();
            }
        });

        ly_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ViewGroup2-OnClickListener");
                ly_2.invalidate();
                ly_2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ly_2.requestLayout();
                    }
                },2000);
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClickListener");
            }
        });

        btn_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                Log.d(TAG, "OnTouchListener=" + ev.getAction());
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent=" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent=" + event.getAction());
        return super.onTouchEvent(event);
    }
}
