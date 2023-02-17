package com.youssef.flickr.framework.utils

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.youssef.flickr.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
