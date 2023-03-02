package com.accord.nmea

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.accord.nmea.service.NmeaService


class App : Application() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        app = this

    }



    companion object {
        lateinit var app: Application
            private set


       /* lateinit var prefs: SharedPreferences
            private set*/
    }


}