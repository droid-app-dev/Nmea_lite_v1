package com.accord.nmea.utils

import android.os.Build
import android.os.Environment
import java.io.File


class Common {

    companion object {


        val APP_DIRECTORY_v1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path
        val APP_DIRECTORY_v2 = Environment.getExternalStorageDirectory().path
        const val _ROOT_FOLDER_NAME_ = "Nmea_v8"
        const val _Logs_ = "Logs"
        const val _Log_EXTENSION_ = ".txt"
        const val _Current = "current_log$_Log_EXTENSION_"

        val _Root_Nmea_v1: File? = getRootFolderPath()
        val _Root_Nmea_v1_Logs: File? = File(_Root_Nmea_v1,File.separator+ _Logs_)


        fun getRootFolderPath(): File? {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
                return File(APP_DIRECTORY_v1 + File.separator + _ROOT_FOLDER_NAME_)
            } else {
                return File(APP_DIRECTORY_v2 + File.separator + _ROOT_FOLDER_NAME_)
            }
        }

    }

}