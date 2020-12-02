package com.android.interview.kotlin

import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.interview.R
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 *@author:sjy
 *@date: 2020/9/27
 *Description
 */
class KotlinDemoAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_map)
//        test1()
//        test2()
        test3()
    }

    /**
     * 协程学习
     */

    fun test1() {
        //个人封装
        runOnKtUiThread {
            Log.d("SJY", "test1-${Thread.currentThread().name}")
        }
        //个人封装
        runOnKtIoThread {
            Log.d("SJY", "test2-${Thread.currentThread().name}")
        }
        GlobalScope.launch(context = Dispatchers.Unconfined) {
            Log.d("SJY", "test3-${Thread.currentThread().name}")
        }
        GlobalScope.launch(context = Dispatchers.Default) {
            Log.d("SJY", "test4-${Thread.currentThread().name}")
        }
        //自定义线程
        GlobalScope.launch(context = newSingleThreadContext("自定义线程名称")) {
            Log.d("SJY", "test4-${Thread.currentThread().name}")
        }
    }

    fun test2() {
        runBlocking {
            val state1 = GlobalScope.async {
                for (i in 1..10) {
                    delay(20)
                }
                Log.d("SJY", "协程1-${Thread.currentThread().name}")
            }
            val state2 = GlobalScope.async {
                Log.d("SJY", "协程2-${Thread.currentThread().name}")
            }
            delay(200)
            if (state1.isCompleted && state2.isCompleted) {
                Log.d("SJY", "协程完成")
            } else {
                Log.d("SJY", "协程未完成")
            }

        }
    }

    fun test3() {
        runBlocking {
            val time1 = measureTimeMillis {
                val one = doSomeThing1()
                val two = doSomeThing2()
            }
            Log.d("SJY", "time=${time1}")
        }
    }

    suspend fun doSomeThing1(): Int {
        delay(1000)
        return 1;
    }

    suspend fun doSomeThing2(): Int {
        delay(2000)
        return 2;
    }

}