package com.android.interview;

import android.app.ActivityManager;
import android.app.IntentService;
import android.arch.lifecycle.Lifecycle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.util.LruCache;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends BaseAct {
    EditText et_input;
    Message message;
    Handler handler;
    MessageQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_input = findViewById(R.id.et_input);
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("SJY", "onTextChanged:"+s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


}
