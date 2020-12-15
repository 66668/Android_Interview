package com.android.interview.touchevent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;


@SuppressLint("AppCompatCustomView")
public class MyBotton extends Button {
    private static final String TAG = "SJY_View";

    public MyBotton(Context context) {
        super(context);
    }

    public MyBotton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBotton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyBotton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(TAG, "dispatchTouchEvent="+event.getAction());
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d(TAG, "dispatchTouchEvent-requestDisallowInterceptTouchEvent=" + false);
            getParent().requestDisallowInterceptTouchEvent(false);//false拦截,父类处理/true不拦截，子类处理
        }
        return super.dispatchTouchEvent(event);
//        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent=" + event.getAction());
        return super.onTouchEvent(event);
        //        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "MyBotton-Canvas验证=" + canvas);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
