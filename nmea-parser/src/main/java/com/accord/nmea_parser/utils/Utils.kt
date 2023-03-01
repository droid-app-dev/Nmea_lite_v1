package com.accord.nmea_parser.utils

import java.io.UnsupportedEncodingException

class Utils {

    public enum class StringType {
        GGA, RMC, GSV, GSA, VTG, GLL, DTM, GNS
    }


    companion object {




        const val CONSTELLATION_GPS = 1
        const val CONSTELLATION_SBAS = 2
        const val CONSTELLATION_GLONASS = 3
        const val CONSTELLATION_QZSS = 4
        const val CONSTELLATION_BEIDOU = 5
        const val CONSTELLATION_GALILEO = 6
        const val CONSTELLATION_IRNSS = 7
        const val CONSTELLATION_UNKNOWN = 0


        /*  const val CONSTELLATION_GPS = 1
          const val CONSTELLATION_GLONASS = 2
          const val CONSTELLATION_GALILEO = 3
          const val CONSTELLATION_BEIDOU = 4

          //    public static final int CONSTELLATION_QZSS = 5;
          const val CONSTELLATION_SBAS = 5

          const val CONSTELLATION_SPS_L5 = 6
          const val CONSTELLATION_SPS_S = 7
          const val CONSTELLATION_RS_L5 = 8
          const val CONSTELLATION_RS_S = 9
          const val CONSTELLATION_SPS_COMBO = 10
          const val CONSTELLATION_RS_COMBO = 11
  */

        @Throws(UnsupportedEncodingException::class)
        fun calculateChecksum(sentence: String): Int {
            val bytes = sentence.substring(1, sentence.length - 3).toByteArray(charset("US-ASCII"))
            var checksum = 0
            for (b in bytes) {
                checksum = checksum xor b.toInt()
            }
            return checksum
        }
    }
}