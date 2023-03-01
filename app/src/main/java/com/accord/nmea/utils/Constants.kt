package com.accord.nmea.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.text.SimpleDateFormat
import java.util.*

object Constants {

    private  var file: File? =null

    @RequiresApi(Build.VERSION_CODES.O)
    fun logNmea(str: String) {

        if(!Common._Root_Nmea_v1!!.isDirectory)
        {
            Common._Root_Nmea_v1!!.mkdir()
        }

        if(!Common._Root_Nmea_v1_Logs!!.isDirectory)
        {
            Common._Root_Nmea_v1_Logs!!.mkdir()
        }


        if (file == null) {
           file= File(Common._Root_Nmea_v1,File.separator+Common._Logs_+File.separator+"current.txt")

                if(!file!!.exists())
                {
                    file!!.createNewFile()

                }

        }

        try {
            Files.write(
                file!!.toPath(),
                Arrays.asList(getTime()+ " : " + str),
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun getTime(): String? {
        val time= System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z")
        val date = Date(time)
        return simpleDateFormat.format(date)
    }

}