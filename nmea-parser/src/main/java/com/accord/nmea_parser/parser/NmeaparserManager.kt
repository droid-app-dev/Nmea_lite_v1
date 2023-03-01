package com.accord.nmea_parser.parser

import android.util.Log
import com.accord.nmea_parser.basic.BasicNmeaHandler
import com.accord.nmea_parser.utils.ExMatcher
import com.accord.nmea_parser.utils.Utils
import java.lang.NullPointerException
import java.util.regex.Pattern

class NmeaparserManager(val handler: BasicNmeaHandler?) {

    init {
        if (handler == null) {
            throw NullPointerException()
        }
    }

    companion object{   }

    val gsvsatDatamap = hashMapOf<Int, List<GSV_satList>>()
    val gsasat_countmap = hashMapOf<Int, GSA>()

    var gpgsv_Satlist = mutableListOf<GSV_satList>()
    var gbgsv_Satlist = mutableListOf<GSV_satList>()
    var glgsv_Satlist = mutableListOf<GSV_satList>()
    var gagsv_Satlist = mutableListOf<GSV_satList>()

    var gpgsa_Satlist = mutableListOf<String>()
    var gbgsa_Satlist = mutableListOf<String>()
    var glgsa_Satlist = mutableListOf<String>()
    var gagsa_Satlist = mutableListOf<String>()




    private val HEX_INT = "[0-9a-fA-F]"
    val GENERAL_SENTENCE = Pattern.compile("^\\$(\\w{5}),(.*)[*](" + HEX_INT + "{2})$")

     lateinit var rmc: RMC
     lateinit var vtg: VTG
     lateinit var gga: GGA
     lateinit var gsa: GSA
     lateinit var gsv: GSV
     lateinit var gll: GLL

    fun parser(sentence: String) {
        val matcher = ExMatcher(GENERAL_SENTENCE.matcher(sentence))
        if (matcher.matches()) {
            val type = matcher.nextString("type")!!
            val stringType = Utils.StringType.valueOf(type.substring(2))

            val content = matcher.nextString("content")!!
            val expected_checksum = matcher.nextHexInt("checksum")!!
            val actual_checksum = Utils.calculateChecksum(sentence)
            if (actual_checksum == expected_checksum) {
                when (stringType) {
                    Utils.StringType.RMC -> {


                        parseRMC(content, type)

                    }
                    Utils.StringType.VTG -> {
                        parseVTG(content, type)
                    }
                    Utils.StringType.GGA -> {
                        parseGGA(content, type)

                        // parseGSV(content)
                        handler?.onNmea(rmc, vtg, gga, gsasat_countmap, gsvsatDatamap)


                        gpgsv_Satlist.clear()
                        gagsv_Satlist.clear()
                        gbgsv_Satlist.clear()
                        glgsv_Satlist.clear()

                        gpgsa_Satlist.clear()
                        gagsa_Satlist.clear()
                        glgsa_Satlist.clear()
                        gbgsv_Satlist.clear()
                    }
                    Utils.StringType.GSA -> {
                        parseGSA(content, type)
                    }
                    Utils.StringType.GSV -> {
                        parseGSV(content, type)
                    }
                    Utils.StringType.DTM -> {

                    }
                    Utils.StringType.GLL -> {
                        parseGLL(content, type)
                    }

                    else -> {



                    }
                }


            }
        }
    }


    fun parseGLL(gllMsg: String, type: String) {
        var index: Int = 0;
        val msg = gllMsg.split(",")
        val latitude = msg[index++]
        val lat_direction = msg[index++]
        val longitude = msg[index++]
        val lon_direction = msg[index++]
        val utc_time = msg[index++]
        val status = msg[index++]
        gll = GLL(latitude, lat_direction, longitude, lon_direction, utc_time, status)
    }

    fun gsvparse(msg: String): List<GSV_satList> {
        var lisofsatSinalStrength = mutableListOf<GSV_satList>()
        val msg1 = msg.split(",")
        for (i in 0 until (msg1.size - 3) / 4) {
            val satId = msg1[4 + i * 4]
            val elevation = msg1[5 + i * 4]
            val azimuth = msg1[6 + i * 4]
            val snr = msg1[7 + i * 4]
            lisofsatSinalStrength.add(GSV_satList(satId, elevation, azimuth, snr))

        }
        return lisofsatSinalStrength
    }

    fun parseGSV(gsvMsg: String, type: String) {
        //  var index: Int = 0;
        // val msg = gsvMsg.split(",")
        /* var lisofsatSinalStrength = mutableListOf<GSV_satList>()
         val number_of_messages = msg[index++]
         val message_number = msg[index++]
         val total_number_of_satellites_in_view = msg[index++]*/
        when (type) {

            "GPGSV" -> {
                gpgsv_Satlist.addAll(gsvparse(gsvMsg))
                gsvsatDatamap.put(1, gpgsv_Satlist)


            }
            "GBGSV" -> {
                gbgsv_Satlist.addAll(gsvparse(gsvMsg))
                gsvsatDatamap.put(4, gbgsv_Satlist)

            }
            "GAGSV" -> {
                gagsv_Satlist.addAll(gsvparse(gsvMsg))
                gsvsatDatamap.put(3, gagsv_Satlist)
            }
            "GLGSV" -> {
                glgsv_Satlist.addAll(gsvparse(gsvMsg))
                gsvsatDatamap.put(2, glgsv_Satlist)

            }
            else -> {

            }
        }
    }

    fun parseRMC(rmcMsg: String, type: String) {
        var index: Int = 0;
        val msg = rmcMsg.split(",")
        val utc_time = msg[index++]
        val status = msg[index++]
        val latitude = msg[index++]
        val lat_direction = msg[index++]
        val longitude = msg[index++]
        val lon_direction = msg[index++]
        val Speed = msg[index++]
        val trackMode = msg[index++]
        val date = msg[index++]
        val magnetic_variation = msg[index++]
        val magnetic_direction = msg[index++]
        val faa_mode = msg[index]
        rmc = RMC(
            utc_time,
            status,
            latitude,
            lat_direction,
            longitude,
            lon_direction,
            Speed,
            trackMode,
            date,
            magnetic_variation,
            magnetic_direction,
            faa_mode
        )
        Log.d("RMC","$rmc")
    }

    fun parseGGA(ggaMsg: String, type: String) {
        var index: Int = 0;
        val msg = ggaMsg.split(",")
        val utc_time = msg[index++]
        val latitude = msg[index++]
        val lat_direction = msg[index++]
        val longitude = msg[index++]
        val lon_direction = msg[index++]
        val fix_quality = msg[index++]
        val satellites_in_view = msg[index++]
        val hdop = msg[index++]
        val altitude = msg[index++]
        val altitude_units = msg[index++]
        val geoidal_separation = msg[index++]
        val units_of_geoidal_separation = msg[index++]
        val age_of_differential_gps_data = msg[index++]
        val differential_reference_station_id = msg[index++]
        gga = GGA(
            utc_time,
            latitude,
            lat_direction,
            longitude,
            lon_direction,
            fix_quality,
            satellites_in_view,
            hdop,
            altitude,
            altitude_units,
            geoidal_separation,
            units_of_geoidal_separation,
            age_of_differential_gps_data,
            differential_reference_station_id
        )
    }

    fun parseVTG(vtgMsg: String, type: String) {
        var index: Int = 0;
        val msg = vtgMsg.split(",")
        val course_over_ground_true = msg[index++]
        val degress_true = msg[index++]
        val course_over_ground_magnetic = msg[index++]
        val degress_magnetic = msg[index++]
        val speed_over_ground_knots = msg[index++]
        val konts = msg[index++]
        val speed_over_ground_km = msg[index++]
        val km = msg[index++]
        val mode_indicator = msg[index]
        vtg = VTG(
            course_over_ground_true,
            degress_true,
            course_over_ground_magnetic,
            degress_magnetic,
            speed_over_ground_knots,
            konts,
            speed_over_ground_km,
            km,
            mode_indicator
        )
    }

    fun parseGSA(gsaMsg: String, type: String) {
        var index: Int = 0;
        var lisofsvid = mutableListOf<String>()
        val msg = gsaMsg.split(",")
        var operating_mode = msg[index++]
        val fix_mode = msg[index++]
        for (i in 2..13) {
            if (msg[i].isNotEmpty()) {
                lisofsvid.add(msg[i])
            }
            index++;
        }
        val pdop = msg[index++]
        val hdop = msg[index++]
        val vdop = msg[index++]
        val sys_id = msg[index]

        var system_id = 0;
        if (!sys_id.isNullOrBlank()) {
            system_id = sys_id.toInt()
        }


        if (system_id > 0) {
            when (system_id) {
                1 -> {
                    gpgsa_Satlist.addAll(lisofsvid)
                    gsasat_countmap.put(
                        1,
                        GSA(operating_mode, fix_mode, gpgsa_Satlist, pdop, hdop, vdop, 1)
                    )

                }
                2 -> {
                    glgsa_Satlist.addAll(lisofsvid)
                    gsasat_countmap.put(
                        2,
                        GSA(operating_mode, fix_mode, glgsa_Satlist, pdop, hdop, vdop, 2)
                    )

                }
                3 -> {
                    gagsa_Satlist.addAll(lisofsvid)
                    gsasat_countmap.put(
                        3,
                        GSA(operating_mode, fix_mode, gagsa_Satlist, pdop, hdop, vdop, 3)
                    )

                }
                4 -> {
                    gbgsa_Satlist.addAll(lisofsvid)
                    gsasat_countmap.put(
                        4,
                        GSA(operating_mode, fix_mode, gbgsa_Satlist, pdop, hdop, vdop, 4)
                    )
                }

            }
        }
    }


}