package com.accord.nmea.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.accord.nmea.R
import com.accord.nmea.base.BaseActivity

class SplashScreenActivity : BaseActivity<MainViewModel>() {

    override fun provideLayoutId(): Int = R.layout.activity_splash_screen

    override fun injectDependencies() {

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setupView(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        supportActionBar?.hide()

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Handler(Looper.getMainLooper()).postDelayed(Runnable { //This method will be executed once the timer is over
            // Start your app main activity
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            // close this activity
            finish()
        }, 2000)


    }


}