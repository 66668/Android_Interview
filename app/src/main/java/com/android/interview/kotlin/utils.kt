package com.android.interview.kotlin

import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *@author:sjy
 *@date: 2020/12/2
 *Description
 */

val Thread.isUiThread: Boolean
    get() {
        return Looper.getMainLooper() == Looper.myLooper()
    }

fun runOnKtUiThread(block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Dispatchers.Main, block = block)

fun runOnKtIoThread(block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Dispatchers.IO, block = block)

fun runOnKtDefThread(block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Dispatchers.Unconfined, block = block)