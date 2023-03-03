package com.accord.nmea.ui.sky

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import android.hardware.display.DisplayManager
import android.util.Log
import android.view.Display
import android.view.Surface
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ViewCompat.getDisplay
import com.accord.nmea.R
import com.accord.nmea.base.BaseFragment
import com.accord.nmea.databinding.ActivityMainBinding
import com.accord.nmea.databinding.GpsSkyCn0IndicatorCardBinding
import com.accord.nmea.databinding.GpsSkyLegendCardBinding
import com.accord.nmea.databinding.GpsSkySignalMeterBinding
import com.accord.nmea.databinding.SkyViewFragmnetBinding
import com.accord.nmea.utils.LibUIUtils

class SkyviewFragment: BaseFragment<SkyviewFragmentViewmodel>(),SensorEventListener {



    private var mBinding:SkyViewFragmnetBinding ? = null

    private lateinit var legendLines: List<View>
    private lateinit var legendShapes: List<ImageView>

    private lateinit var meter: GpsSkySignalMeterBinding
    private lateinit var legend: GpsSkyLegendCardBinding
    // Holds sensor data
    private val rotationMatrix = FloatArray(16)
    private val remappedMatrix = FloatArray(16)
    private val values = FloatArray(3)
    private val truncatedRotationVector = FloatArray(4)
    private var truncateVector = false
    private lateinit var geomagneticField: GeomagneticField
    private lateinit var mSensorManager : SensorManager
    private var mAccelerometer : Sensor ?= null
    override fun provideLayoutId(): Int = R.layout.sky_view_fragmnet

    override fun setupView(view: View) {

        mBinding = mViewDataBinding as SkyViewFragmnetBinding
        meter = mBinding!!.skyCn0IndicatorCard.gpsSkySignalMeter
        legend = mBinding!!.skyLegendCard

        initLegendViews()

        mBinding!!.skyView.setStarted()
        mSensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (LibUIUtils.isRotationVectorSensorSupported(requireActivity())) {
            // Use the modern rotation vector sensors
            val vectorSensor: Sensor =
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

           // mSensorManager.unregisterListener(this)
            mSensorManager.registerListener(this, vectorSensor, SensorManager.SENSOR_DELAY_FASTEST)

          /*  sensorManager.registerListener(
                callback,
                vectorSensor,
                SensorManager.SENSOR_DELAY_FASTEST
            )*/
        } else {
            // Use the legacy orientation sensors
            val sensor: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
          /*  sensorManager.registerListener(
                callback,
                sensor,
                SensorManager.SENSOR_DELAY_GAME
            )*/

            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)

        }

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    private fun initLegendViews() {
        // Avg C/N0 indicator lines
        val cn0 = meter.signalMeterTicksAndText
        legendLines = listOf(
            cn0.skyLegendCn0LeftLine4,
            cn0.skyLegendCn0LeftLine3,
            cn0.skyLegendCn0LeftLine2,
            cn0.skyLegendCn0LeftLine1,
            cn0.skyLegendCn0CenterLine,
            cn0.skyLegendCn0RightLine1,
            cn0.skyLegendCn0RightLine2,
            cn0.skyLegendCn0RightLine3,
            cn0.skyLegendCn0RightLine4,
            legend.skyLegendShapeLine1a,
            legend.skyLegendShapeLine1b,
            legend.skyLegendShapeLine2a,
            legend.skyLegendShapeLine2b,
            legend.skyLegendShapeLine3a,
            legend.skyLegendShapeLine3b,
            legend.skyLegendShapeLine4a,
            legend.skyLegendShapeLine4b,
            legend.skyLegendShapeLine5a,
            legend.skyLegendShapeLine5b,
            legend.skyLegendShapeLine6a,
            legend.skyLegendShapeLine6b,
            legend.skyLegendShapeLine7a,
            legend.skyLegendShapeLine7b,
            legend.skyLegendShapeLine8a,
            legend.skyLegendShapeLine8b,
            legend.skyLegendShapeLine9a,
            legend.skyLegendShapeLine9b,
            legend.skyLegendShapeLine10a,
            legend.skyLegendShapeLine10b,
            legend.skyLegendShapeLine11a,
            legend.skyLegendShapeLine12a,
            legend.skyLegendShapeLine13a,
            legend.skyLegendShapeLine14a,
            legend.skyLegendShapeLine14b,
            legend.skyLegendShapeLine15a,
            legend.skyLegendShapeLine15b,
            legend.skyLegendShapeLine16a,
            legend.skyLegendShapeLine16b
        )

        // Shape Legend shapes
        legendShapes = listOf(
            legend.skyLegendCircle,
            legend.skyLegendSquare,
            legend.skyLegendPentagon,
            legend.skyLegendTriangle,
            legend.skyLegendHexagon1,
            legend.skyLegendOval,
            legend.skyLegendDiamond1,
            legend.skyLegendDiamond2,
            legend.skyLegendDiamond3,
            legend.skyLegendDiamond4,
            legend.skyLegendDiamond5,
            legend.skyLegendDiamond6,
            legend.skyLegendDiamond7
        )
    }

    private fun onOrientationChanged(orientation: Double, tilt: Double) {
        // For performance reasons, only proceed if this fragment is visible
        // TODO - this is now deprecated and a no-op, update to newest code
        if (!userVisibleHint) {
            return
        }
        mBinding?.skyView?.onOrientationChanged((orientation+360)%360, tilt)
    }

    private fun handleRotation(rotation: Int) {
        when (rotation) {
            Surface.ROTATION_0 ->
                // No orientation change, use default coordinate system
                SensorManager.getOrientation(
                    rotationMatrix,
                    values
                )
            Surface.ROTATION_90 -> {
                // Log.d(MODE_MAP, "Rotation-90");
                SensorManager.remapCoordinateSystem(
                    rotationMatrix, SensorManager.AXIS_Y,
                    SensorManager.AXIS_MINUS_X, remappedMatrix
                )
                SensorManager.getOrientation(
                    remappedMatrix,
                    values
                )
            }
            Surface.ROTATION_180 -> {
                // Log.d(MODE_MAP, "Rotation-180");
                SensorManager
                    .remapCoordinateSystem(
                        rotationMatrix, SensorManager.AXIS_MINUS_X,
                        SensorManager.AXIS_MINUS_Y, remappedMatrix
                    )
                SensorManager.getOrientation(
                    remappedMatrix,
                    values
                )
            }
            Surface.ROTATION_270 -> {
                // Log.d(MODE_MAP, "Rotation-270");
                SensorManager
                    .remapCoordinateSystem(
                        rotationMatrix, SensorManager.AXIS_MINUS_Y,
                        SensorManager.AXIS_X, remappedMatrix
                    )
                SensorManager.getOrientation(
                    remappedMatrix,
                    values
                )
            }
            else ->
                // This shouldn't happen - assume default orientation
                SensorManager.getOrientation(
                    rotationMatrix,
                    values
                )
        }
    }
    private fun getDisplay(): Display? {
        val displayManager =
            context?.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        return displayManager.getDisplay(0)
    }


    private fun maybeTruncateVector(event: SensorEvent) {
        if (!truncateVector) {
            try {
                SensorManager.getRotationMatrixFromVector(
                    rotationMatrix,
                    event.values
                )
            } catch (e: IllegalArgumentException) {
                // On some Samsung devices, an exception is thrown if this vector > 4 (see #39)
                // Truncate the array, since we can deal with only the first four values
             /*   Log.e(
                    TAG,
                    "Samsung device error? Will truncate vectors - $e"
                )*/
                truncateVector = true
                // Do the truncation here the first time the exception occurs
                getRotationMatrixFromTruncatedVector(event.values)
            }
        } else {
            // Truncate the array to avoid the exception on some devices (see #39)
            getRotationMatrixFromTruncatedVector(event.values)
        }
    }
    private fun getRotationMatrixFromTruncatedVector(vector: FloatArray) {
        System.arraycopy(vector, 0, truncatedRotationVector, 0, 4)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, truncatedRotationVector)
    }
    @SuppressLint("MissingPermission")
    override fun onSensorChanged(event: SensorEvent?) {

        var orientationX: Double
        var tiltY = Double.NaN
        var yawZ = Double.NaN

        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_ROTATION_VECTOR -> {
                    // Modern rotation vector sensors
                   maybeTruncateVector(event)

                    val display = getDisplay()
                    if (display != null) handleRotation(display.rotation)
                    orientationX = Math.toDegrees(values[0].toDouble()) // azimuth
                    tiltY = Math.toDegrees(values[1].toDouble())
                    yawZ = Math.toDegrees(values[2].toDouble())
                }
                Sensor.TYPE_ORIENTATION ->
                    // Legacy orientation sensors
                    orientationX = event.values[0].toDouble()
                else ->
                    // A sensor we're not using, so return
                    return
            }

            val azimt=Orientation(event.timestamp, doubleArrayOf(orientationX, tiltY, yawZ))
            Log.d("SensorLogs:-",""+azimt.values[0].toString()+"  "+azimt.values[1].toString())

            onOrientationChanged(azimt.values[0],azimt.values[1])

        }
    }



    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}