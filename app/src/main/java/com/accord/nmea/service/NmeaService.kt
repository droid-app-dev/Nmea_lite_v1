package com.accord.nmea.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.accord.nmea.R
import com.accord.nmea.parser.NmeaParsingManager
import com.accord.nmea.utils.Constants
import org.greenrobot.eventbus.EventBus


@RequiresApi(Build.VERSION_CODES.N)
class NmeaService : LifecycleService(), OnNmeaMessageListener,LocationListener {

    var nmeaMessageLiveData=MutableLiveData<String>("")

    private val mBinder=LocalBinder()

    lateinit var locationManger:LocationManager

    val nmeaparsemanager=NmeaParsingManager(this)

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(applicationContext,"Premission denied", Toast.LENGTH_LONG).show()
        }

        try {
            locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,0.0f,this)
            locationManger.addNmeaListener(this, Handler(Looper.myLooper()!!))
            locationManger.registerGnssStatusCallback(mStatusCallback,Handler(Looper.myLooper()!!))
        } catch (e: Exception) {
            e.stackTrace
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
      //  Log.d(TAG, "onBind()")
      //  configurationChange = false
     //   handleBind()
        return mBinder
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        locationManger=getSystemService(LOCATION_SERVICE)as LocationManager


        var allproviders=locationManger.allProviders
        var gnssYearOfHardware= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManger.gnssYearOfHardware
        } else {

        }
        var gnssHardwareModelName= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManger.gnssHardwareModelName
        } else {

        }
        var gnssCapabilitiesl= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            locationManger.gnssCapabilities
        } else {

        }




        Log.d("LocationManager",""+allproviders)
        Log.d("LocationManager",""+gnssCapabilitiesl)
        Log.d("LocationManager",""+gnssHardwareModelName)
        Log.d("LocationManager",""+gnssYearOfHardware)


        EventBus.getDefault().post(gnssCapabilitiesl)


        showNotification()
    }



    private val mStatusCallback: GnssStatus.Callback = object : GnssStatus.Callback() {
        override fun onStarted() {

        }

        override fun onStopped() {

        }

        override fun onFirstFix(ttffMillis: Int) {}

        override fun onSatelliteStatusChanged(status: GnssStatus) {
           // Log.v(TAG, "GNSS Status: " + status.satelliteCount + " satellites.")


        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNmeaMessage(message: String?, timestamp: Long) {

      //  getNmeaMessage().value=message

        /*GnssNavigationMessage
        GnssStatus
        GnssClock
        GnssMeasurement
        GnssMeasurementsEvent
        GnssCapabilities*/

        val str= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManger.gnssHardwareModelName
        } else {
            TODO("VERSION.SDK_INT < P")
        }


        if (message != null) {
           // Constants.logNmea(message)
            EventBus.getDefault().post(message)
            nmeaparsemanager.logNmea(message,timestamp)

        }



        //  Log.d("Nmea","Message"+message)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {

        val NOTIFICATION_CHANNEL_ID = "com.accord.nmea"
        val channelName = "Nmea"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val stopSelf = Intent(this, NmeaService::class.java)
        stopSelf.action = "stop"
        val pStopSelf =
            PendingIntent.getService(this, 0, stopSelf, PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.addAction(R.drawable.ic_satilite, "STOP", pStopSelf)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_settings_datums)
            .setContentTitle("Nmea is running")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)

    }

    override fun onLocationChanged(location: Location) {
    }




    inner class LocalBinder : Binder() {
        val service: NmeaService
            get() = this@NmeaService
    }


    fun subscribeToLocationUpdates() {
        // Binding to this service doesn't actually trigger onStartCommand(). That is needed to
        // ensure this Service can be promoted to a foreground service, i.e., the service needs to
        // be officially started (which we do here).
        startService(Intent(applicationContext, NmeaService::class.java))

    }
}