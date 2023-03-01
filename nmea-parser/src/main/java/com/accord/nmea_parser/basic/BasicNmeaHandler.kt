package com.accord.nmea_parser.basic

import com.accord.nmea_parser.parser.*

interface  BasicNmeaHandler {
    fun onNmea(rmc: RMC, vtg: VTG, gga: GGA, gsa: HashMap<Int, GSA>, gsv: HashMap<Int, List<GSV_satList>>)
    fun onNmea(rmc:RMC, vtg: VTG, gga: GGA, gsa: HashMap<Int, GSA>, gsv: HashMap<Int, List<GSV_satList>>, gll: GLL)

}