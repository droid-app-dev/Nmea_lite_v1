package com.accord.nmea.base

import android.app.Activity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract  class BaseActivity<VM:BaseViewModel>:AppCompatActivity() {


    var mViewDataBinding: ViewDataBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView((this as Activity)!!,provideLayoutId())
        setupView(savedInstanceState)
        setupObservers()

    }




    protected open fun setupObservers() {

    }


    open fun goBack() = onBackPressed()

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStackImmediate()
        else super.onBackPressed()
    }

    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    protected abstract fun injectDependencies()

    protected abstract fun setupView(savedInstanceState: Bundle?)




}