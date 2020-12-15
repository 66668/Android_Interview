package com.android.interview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.LruCache;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.interview.kotlin.KotlinDemoAct;

import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseAct {
    EditText et_input;
    Button btn_1;
    Handler handler = new Handler();
    Looper looper = handler.getLooper();

    MessageQueue queue =looper.getQueue();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_input = findViewById(R.id.et_input);
        btn_1 = findViewById(R.id.btn_1);

        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("SJY", "onTextChanged:" + s.toString());
//                et_input.setText(s.toString());
            }
        });
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TouchEventActivity.class));
            }
        });
        LinkedHashMap<String, String> a = new LinkedHashMap<>();
        LruCache cache = new LruCache<String, Bitmap>(4) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return super.sizeOf(key, value);
            }
        };
//        testRxjava();

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapAct.class));
            }
        });
        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KotlinDemoAct.class));
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        return super.dispatchKeyEvent(event);
    }

    private void testRxjava() {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        Log.d("SJY", "subscribe-线程：" + Thread.currentThread().getName());
                        emitter.onNext("a");
                        emitter.onNext("b");
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())//线程调度
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主线程切换
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("SJY", "onSubscribe-线程：" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("SJY", "内容：" + s + "-onNext-线程：" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SJY", "onError-线程：" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("SJY", "onComplete-线程：" + Thread.currentThread().getName());
                    }
                });
    }
}
