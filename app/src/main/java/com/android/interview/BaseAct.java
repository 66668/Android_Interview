package com.android.interview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class BaseAct extends AppCompatActivity {
    Handler handler = new WeakRefHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    //
    static class WeakRefHandler extends Handler {
        public WeakReference<BaseAct> mAct;

        public WeakRefHandler(BaseAct baseAct) {
            mAct = new WeakReference<BaseAct>(baseAct);
        }

        @Override
        public void handleMessage(Message msg) {
            final BaseAct activity = mAct.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    public void handleMessage(Message msg) {
    }


}
