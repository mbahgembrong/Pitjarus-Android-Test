package com.pitjarus.pitjarusandroidtest
import android.app.Application
import com.pitjarus.pitjarusandroidtest.utils.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
@HiltAndroidApp
class App:Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        else {
            Timber.plant(ReleaseTree())
        }
    }
}