package com.android.interview.touchevent;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ViewGroup1 extends LinearLayout {
    private static final String TAG = "SJY_ViewGroup1";

    public ViewGroup1(Context context) {
        super(context);
    }

    public ViewGroup1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewGroup1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent=" + ev.getAction());
        return super.dispatchTouchEvent(ev);
//        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent=" + ev.getAction());
        return super.onInterceptTouchEvent(ev);
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
        Log.d(TAG, "ViewGroup1-Canvas验证=" + canvas.hashCode());
    }
}
