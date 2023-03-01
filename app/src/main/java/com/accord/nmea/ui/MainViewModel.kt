package com.accord.nmea.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.accord.nmea.R
import com.accord.nmea.base.BaseViewModel
import com.accord.nmea.utils.SharedPref

class MainViewModel() : BaseViewModel() {

     var fab_btnLivedata=MutableLiveData<Boolean>(false)
    override fun onCreate() {
    }


    fun onClickConvert(view:View) {

        fab_btnLivedata.value=true

    }
}