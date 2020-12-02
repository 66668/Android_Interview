package com.android.interview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *@author:sjy
 *@date: 2020/9/27
 *Description
 */
class MapAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_map)
//        testMap()
    }

    fun testMap() {
        var maps: Map<String, String> = mapOf(
                "android.app.Application"
                        to "com.tencent.shadow.core.runtime.ShadowApplication",
                "android.app.Application\$ActivityLifecycleCallbacks"
                        to "com.tencent.shadow.core.runtime.ShadowActivityLifecycleCallbacks"
        )

        for (item in maps) {
            Log.d("SJY", "item key=${item.key}  val=${item.value}")
        }
    }

}