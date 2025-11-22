package com.example.day6task.base

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {
    companion object {
        lateinit var appContext: Context
            private set
    }


    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        DynamicColors.applyToActivitiesIfAvailable(this)
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(hiltWorkerFactory).build())
    }

    object AppContext {
        val context: Context
            get() = appContext
    }
}