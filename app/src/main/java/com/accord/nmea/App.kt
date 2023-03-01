package com.accord.nmea

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.accord.nmea.service.NmeaService


class App : Application() {

    private var instance: App? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        instance = this

        startService(Intent(applicationContext, NmeaService::class.java))

    }




}