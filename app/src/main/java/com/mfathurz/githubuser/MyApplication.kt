package com.mfathurz.githubuser

import android.app.Application
import com.facebook.stetho.Stetho

class MyApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}