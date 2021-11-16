package com.example.uitest

import android.app.Application
import android.content.Context
import leakcanary.LeakCanary

class MyApp : Application() {

    companion object{
        lateinit var context:Context;
    }
    override fun onCreate() {
        super.onCreate()
        context = this;
    }

}