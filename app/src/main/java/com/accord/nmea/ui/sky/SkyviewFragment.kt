package com.accord.nmea.ui.sky

import android.view.View
import android.widget.ImageView
import com.accord.nmea.R
import com.accord.nmea.base.BaseFragment
import com.accord.nmea.databinding.ActivityMainBinding
import com.accord.nmea.databinding.GpsSkyCn0IndicatorCardBinding
import com.accord.nmea.databinding.GpsSkyLegendCardBinding
import com.accord.nmea.databinding.GpsSkySignalMeterBinding
import com.accord.nmea.databinding.SkyViewFragmnetBinding

class SkyviewFragment:BaseFragment<SkyviewFragmentViewmodel>() {



    private var mBinding:SkyViewFragmnetBinding ? = null

    private lateinit var legendLines: List<View>
    private lateinit var legendShapes: List<ImageView>

    private lateinit var meter: GpsSkySignalMeterBinding
    private lateinit var legend: GpsSkyLegendCardBinding


    override fun provideLayoutId(): Int = R.layout.sky_view_fragmnet

    override fun setupView(view: View) {

        mBinding = mViewDataBinding as SkyViewFragmnetBinding
        meter = mBinding!!.skyCn0IndicatorCard.gpsSkySignalMeter
        legend = mBinding!!.skyLegendCard

        initLegendViews()

        mBinding!!.skyView.setStarted()

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

}