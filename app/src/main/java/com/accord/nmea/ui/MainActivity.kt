package com.accord.nmea.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.accord.nmea.App.Companion.app
import com.accord.nmea.R
import com.accord.nmea.base.BaseActivity
import com.accord.nmea.databinding.ActivityMainBinding
import com.accord.nmea.service.NmeaService
import com.accord.nmea.ui.live.ViewpagerAdapter
import com.accord.nmea.utils.LibUIUtils
import com.accord.nmea.utils.PermissionUtils
import com.accord.nmea.utils.PermisssionUtils
import com.accord.nmea.utils.SharedPref
import com.accord.nmea_parser.basic.BasicNmeaHandler
import com.accord.nmea_parser.parser.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.*


@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : BaseActivity<MainViewModel>() {

    private var userDeniedPermission = false


    companion object {
        const val TAG = "LoginActivity"
    }
    //hello

    lateinit var locationManger: LocationManager
    lateinit var mainService: NmeaService
    lateinit var mBinding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    // Get a reference to the Job from the Flow so we can stop it from UI events
    private var locationFlow: Job? = null


    override fun provideLayoutId(): Int {
        return  R.layout.activity_main
    }

    override fun injectDependencies() {
    }
    private var isServiceBound = false
    private var service: NmeaService? = null

    private var foregroundOnlyServiceConnection: ServiceConnection = object : ServiceConnection {
        @SuppressLint("SuspiciousIndentation")
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder = iBinder as NmeaService.LocalBinder
            service = binder.service
            isServiceBound = true
            //if (locationFlow?.isActive == true) {
                // Activity started location updates but service wasn't bound yet - tell service to start now
                service?.subscribeToLocationUpdates()
          //  }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            service = null
            isServiceBound = false
        }
    }



    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupView(savedInstanceState: Bundle?) {

        mBinding = mViewDataBinding as ActivityMainBinding


        mBinding.viewpager.adapter = ViewpagerAdapter(supportFragmentManager, lifecycle)

        viewModel = ViewModelProvider(this, defaultViewModelProviderFactory)[MainViewModel::class.java]

        if (SharedPref(this).getValueInt(SharedPref.KEY_NAMES.LOG_Button_Status) == 0) {
            mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
        } else {
            mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)

        }

        mBinding.fabIcon.setOnClickListener(View.OnClickListener {

          //  val datarmc:RMC=NmeaparseManager.parseRMC("\$GNRMC,122507.00,A,1257.45652,N,07738.50299,E,0.042,,130223,,,D,V*18")
            val handler=object : BasicNmeaHandler {
                override fun onNmea(
                    rmc: RMC,
                    vtg: VTG,
                    gga: GGA,
                    gsa: HashMap<Int, GSA>,
                    gsv: HashMap<Int, List<GSV_satList>>
                ) {

                }

                override fun onNmea(rmc: RMC, vtg: VTG, gga: GGA, gsa: HashMap<Int, GSA>, gsv: HashMap<Int, List<GSV_satList>>, gll: GLL) {

                    Log.d("onNmea","$gsa");

                }


            }

            val msgarr= arrayOf("\$GNRMC,035436.00,A,1257.45041,N,07738.49203,E,0.453,,270223,,,D,V*10\n",
                    "\$GNVTG,,T,,M,0.453,N,0.838,K,D*39\n",
                    "\$GNGGA,035436.00,1257.45041,N,07738.49203,E,2,12,1.49,890.4,M,-86.4,M,,0000*69\n",
                    "\$GNGSA,A,3,07,14,09,03,04,27,,,,,,,2.64,1.49,2.18,1*0F\n",
                    "\$GNGSA,A,3,78,67,,,,,,,,,,,2.64,1.49,2.18,2*0B\n",
                    "\$GNGSA,A,3,24,31,04,01,,,,,,,,,2.64,1.49,2.18,3*05\n",
                    "\$GNGSA,A,3,33,23,,,,,,,,,,,2.64,1.49,2.18,4*02\n",
                    "\$GPGSV,4,1,15,01,07,174,,03,34,195,37,04,72,044,25,07,27,321,36,1*6D\n",
                    "\$GPGSV,4,2,15,08,54,107,27,09,48,337,32,14,16,235,40,16,20,032,16,1*6E\n",
                    "\$GPGSV,4,3,15,21,09,151,,27,34,065,26,30,05,292,,36,35,258,,1*6E\n",
                    "\$GPGSV,4,4,15,40,60,242,43,41,74,157,,49,08,266,39,1*59\n",
                    "\$GLGSV,3,1,10,65,07,157,,66,61,166,,67,59,324,29,68,10,333,,1*75\n",
                    "\$GLGSV,3,2,10,76,02,037,,77,30,359,,78,32,291,36,79,03,248,,1*71\n",
                    "\$GLGSV,3,3,10,87,14,104,,88,11,152,,1*70\n",
                    "\$GAGSV,3,1,10,01,51,005,15,04,54,339,32,09,05,324,,12,03,205,,7*77\n",
                    "\$GAGSV,3,2,10,19,58,119,,21,12,036,,24,08,234,41,26,02,112,,7*70\n",
                    "\$GAGSV,3,3,10,31,49,264,33,33,09,157,,7*77\n",
                    "\$GBGSV,4,1,16,01,14,097,,03,49,112,,05,63,236,31,06,42,068,,1*79\n",
                    "\$GBGSV,4,2,16,07,63,050,17,08,04,166,,09,47,045,15,10,76,057,,1*76\n",
                    "\$GBGSV,4,3,16,13,10,173,,14,31,161,19,16,40,071,16,23,33,297,48,1*7A\n",
                    "\$GBGSV,4,4,16,24,08,052,,25,40,001,,28,01,202,,33,55,180,33,1*78\n",
                    "\$GNGLL,1257.45041,N,07738.49203,E,035436.00,A,D*77")

            val parser=NmeaparserManager(handler)

            for(i in msgarr)
            {
              //  parser.parser(i.trim())

            }


            GlobalScope.launch(Dispatchers.IO) {
                val parser=NmeaparserManager(handler)

            }



          //  Log.d("RMC_L","${datagsa.toString()}")
            if (SharedPref(this).getValueInt(SharedPref.KEY_NAMES.LOG_Button_Status) == 0) {
                mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                SharedPref(this).save(SharedPref.KEY_NAMES.LOG_Button_Status, 1)
            } else {
                mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                SharedPref(this).save(SharedPref.KEY_NAMES.LOG_Button_Status, 0)

            }
        })

        // bindService(Intent(applicationContext, NmeaService::class.java), serviceConnection, BIND_AUTO_CREATE)

        mBinding.bottomNavBar.setOnItemSelectedListener { it ->

            when (it.itemId) {
                R.id.page_1 -> {
                    mBinding.viewpager.setCurrentItem(0, true)
                    true
                }
                R.id.page_2 -> {

                    mBinding.viewpager.setCurrentItem(1, true)
                    true
                }
                R.id.page_3 -> {

                    mBinding.viewpager.setCurrentItem(2, true)

                    true
                }
                else -> false
            }
        }


        mBinding.viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        mBinding.bottomNavBar.selectedItemId = R.id.page_1
                    }
                    1 -> {
                        mBinding.bottomNavBar.selectedItemId = R.id.page_2
                    }
                    2 -> {
                        mBinding.bottomNavBar.selectedItemId = R.id.page_3
                    }
                }

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })


        val serviceIntent = Intent(this, NmeaService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, BIND_AUTO_CREATE)

    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.fab_btnLivedata.observe(this, Observer {
            if (it) {
                if (SharedPref(this).getValueInt(SharedPref.KEY_NAMES.LOG_Button_Status) == 0) {
                    mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                    SharedPref(this).save(SharedPref.KEY_NAMES.LOG_Button_Status, 1)


                } else {
                    mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                    SharedPref(this).save(SharedPref.KEY_NAMES.LOG_Button_Status, 0)

                }
            }
        })
    }


    /* val serviceConnection: ServiceConnection = object : ServiceConnection {
         override fun onServiceConnected(name: ComponentName, service: IBinder) {
             mainService = (service as NmeaService.LocalBinder).getService()
             initObserversDependentOnMainService()

         }
         override fun onServiceDisconnected(name: ComponentName) {}
     }
 */

    private fun initObserversDependentOnMainService() {

        mainService.nmeaMessageLiveData.observe(this, Observer<String> { msg: String? ->
         //   Log.d("MainActivity", "", msg.toString());
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.menu_main, menu)


    }


    fun checkPermission() {
        Dexter.withContext(this@MainActivity)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            Log.d(TAG, "OK")
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {

                    if (p1 != null) {
                        p1.continuePermissionRequest()
                    }
                }

            })
            .withErrorListener {
              //  Log.d(TAG, "", it.name)
            }
            .check()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtils.LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userDeniedPermission = false
                checkGPS()
            } else {
                userDeniedPermission = true
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (!userDeniedPermission) {
            requestPermissionAndInit(this)
        } else {
            // Explain permission to user (don't request permission here directly to avoid infinite
            // loop if user selects "Don't ask again") in system permission prompt
            LibUIUtils.showLocationPermissionDialog(this)
        }

    }

    private fun checkGPS()
    {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
        if (provider == null) {
            Log.e(TAG, "Unable to get GPS_PROVIDER")
            Toast.makeText(
                this, getString(R.string.gps_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LibUIUtils.promptEnableGps(app,this)
        }else{
            service?.subscribeToLocationUpdates()

        }

    }

    private fun requestPermissionAndInit(activity: Activity) {
        if (PermissionUtils.hasGrantedPermissions(activity, PermissionUtils.REQUIRED_PERMISSIONS)) {
            checkGPS()
        } else {
            // Request permissions from the user
            ActivityCompat.requestPermissions(
                activity,
                PermissionUtils.REQUIRED_PERMISSIONS,
                PermissionUtils.LOCATION_PERMISSION_REQUEST
            )
        }
    }



}