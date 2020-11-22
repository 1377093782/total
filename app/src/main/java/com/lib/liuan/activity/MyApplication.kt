package com.lib.liuan.activity

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.anguomob.total.Anguo

/**
 * Create by: liuan
 * Create date: 2020-11-18 0018
 * Describe:
 */
class MyApplication :MultiDexApplication(){
    override fun onCreate() {
        super.onCreate()
//        Anguo.init(this)
    }
}