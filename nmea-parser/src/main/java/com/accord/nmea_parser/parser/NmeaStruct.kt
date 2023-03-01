package com.accord.nmea_parser.parser



    /*
     ** RMC - Recommended Minimum Navigation Information
     **
     **        1         2  3       4 5        6  7   8   9    10   11  12
     **        |         |  |       | |        |  |   |   |    |    |   |
     ** $--RMC,hhmmss.ss,A, llll.ll , a, yyyyy.yy,a,x.x,x.x,xxxx, x.x,  a *hh<CR><LF>
     **
     ** Field Number:
     **  1) UTC Time
     **  2) Status, V = Navigation receiver warning
     **  3) Latitude
     **  4) N or S
     **  5) Longitude
     **  6) E or W
     **  7) Speed over ground, knots
     **  8) Track made good, degrees true
     **  9) Date, ddmmyy
     ** 10) Magnetic Variation, degrees
     ** 11) E or W
     ** 12) FAA Mode (version 2.3)
     ** 13) Navigational Status
     ** 14) Checksum
     */
    data class RMC(
        val utc_time: String,
        val status: String,
        val latitude: String,
        val lat_direction: String,
        val longitude: String,
        val lon_direction: String,
        val Speed: String,
        val trackMode: String,
        val date: String,
        val magnetic_variation: String,
        val magnetic_direction: String,
        val faa_mode: String
    )

    /*
          ** GGA - Global Positioning System Fix Data
          ** Time, Position and fix related data fora GPS receiver.
          **
          **
          **        1         2       3 4        5 6 7  8   9  10  11    12 13  14   15
          **        |         |       | |        | | |  |   |   |  |     | |   |    |
          ** $--GGA,hhmmss.ss,llll.ll,a,yyyyy.yy,a,x,xx,x.x,x.x,M, x.x ,M,x.x,xxxx*hh<CR><LF>
          **
          ** Field Number:
          **  1) Universal Time Coordinated (UTC)
          **  2) Latitude
          **  3) N or S (North or South)
          **  4) Longitude
          **  5) E or W (East or West)
          **  6) GPS Quality Indicator,
          **     0 - fix not available,
          **     1 - GPS fix,
          **     2 - Differential GPS fix
          **  7) Number of satellites in view, 00 - 12
          **  8) Horizontal Dilution of precision
          **  9) Antenna Altitude above/below mean-sea-level (geoid)
          ** 10) Units of antenna altitude, meters
          ** 11) Geoidal separation, the difference between the WGS-84 earth
          **     ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level
          **     below ellipsoid
          ** 12) Units of geoidal separation, meters
          ** 13) Age of differential GPS data, time in seconds since last SC104
          **     type 1 or 9 update, null field when DGPS is not used
          ** 14) Differential reference station ID, 0000-1023
          ** 15) Checksum
          */
    data class GGA(
        val utc_time: String,
        val latitude: String,
        val lat_direction: String,
        val longitude: String,
        val lon_direction: String,
        val fix_quality: String,
        val satellites_in_view: String,
        val hdop: String,
        val altitude: String,
        val altitude_units: String,
        val geoidal_separation: String,
        val units_of_geoidal_separation: String,
        val age_of_differential_gps_data: String,
        val differential_reference_station_id: String,
    )

    /*
           ** GSV - TRANSIT Position - Latitude/Longitude
           ** Location and time of TRANSIT fix at waypoint
           **
           **        1 2 3  4  5  6   7  8  9  10  11 12 13 14  15 16 17 18  19  20
           **        | | |  |  |  |   |  |  |  |   |  |  |  |   |  |  |  |   |   |
           ** $--GSV,x,x,xx,xx,xx,xxx,xx,xx,xx,xxx,xx,xx,xx,xxx,xx,xx,xx,xxx,xx,*hh<CR><LF>
           **
           **  1) Total number of messages, 1-3
           **  2) Message Number, 1-3
           **  3) Total number of satellites in view
           **  4) Satellite Number #1
           **  5) Elevation #1
           **  6) Azimuth, Degrees True #1
           **  7) SNR #1, NULL when not tracking
           **  8) Satellite Number #2
           **  9) Elevation #2
           ** 10) Azimuth, Degrees True #2
           ** 11) SNR #2, NULL when not tracking
           ** 12) Satellite Number #3
           ** 13) Elevation #3
           ** 14) Azimuth, Degrees True #3
           ** 15) SNR #3, NULL when not tracking
           ** 16) Satellite Number #4
           ** 17) Elevation #4
           ** 18) Azimuth, Degrees True #4
           ** 19) SNR #4, NULL when not tracking
           ** 20) Checksum
           */
    data class GSV(
        val number_of_messages: String,
        val message_number: String,
        val total_number_of_satellites_in_view: String,
        val satlistmap: HashMap<Int, List<GSV_satList>> = hashMapOf()
        )

    data class GSV_satList(
        val satellite_number: String,
        val elevation: String,
        val azimuth: String,
        val signalstrength: String
    )

    /*
          ** GLL - Geographic Position - Latitude/Longitude
          ** Latitude, N/S, Longitude, E/W, UTC, Status
          **
          **        1       2 3        4 5         6 7
          **        |       | |        | |         | |
          ** $--GLL,llll.ll,a,yyyyy.yy,a,hhmmss.ss,A*hh<CR><LF>
          **
          ** Field Number:
          **  1) Latitude
          **  2) N or S (North or South)
          **  3) Longitude
          **  4) E or W (East or West)
          **  5) Universal Time Coordinated (UTC)
          **  6) Status A - Data Valid, V - Data Invalid
          **  7) Checksum
          */
    data class GLL(
        val latitude: String,
        val lat_direction: String,
        val longitude: String,
        val lon_direction: String,
        val utc_time: String,
        val status: String,

        )


    /*
             ** VTG - Track made good and Ground speed
             **
             **        1   2 3   4 5	 6 7   8 9
             **        |   | |   | |	 | |   | |
             ** $--VTG,x.x,T,x.x,M,x.x,N,x.x,K*hh<CR><LF>
             **
             ** Field Number:
             **  1) Track Degrees
             **  2) T = True
             **  3) Track Degrees
             **  4) M = Magnetic
             **  5) Speed Knots
             **  6) N = Knots
             **  7) Speed Kilometers Per Hour
             **  8) K = Kilometers Per Hour
             **  9) Checksum
             */
    data class VTG(
        val course_over_ground_true: String,
        val degress_true: String,
        val course_over_ground_magnetic: String,
        val degress_magnetic: String,
        val speed_over_ground_knots: String,
        val konts: String,
        val speed_over_ground_km: String,
        val km: String,
        val mode_indicator: String
    )



/*
       ** GSA - GPS DOP and Active Satellites
       **
       **        1 2 3  4  5  6  7  8  9  10 11 12 13 14 15  16  17  18
       **        | | |  |  |  |  |  |  |  |  |  |  |  |  |   |   |   |
       ** $--GSA,a,x,xx,xx,xx,xx,xx,xx,xx,xx,xx,xx,xx,xx,x.x,x.x,x.x*hh<CR><LF>
       **
       ** Field Number:
       **  1) Operating Mode, A = Automatic, M = Manual
       **  2) Fix Mode, 1 = Fix not available, 2 = 2D, 3 = 3D
       **  3) Satellite PRN #1
       **  4) Satellite PRN #2
       **  5) Satellite PRN #3
       **  6) Satellite PRN #4
       **  7) Satellite PRN #5
       **  8) Satellite PRN #6
       **  9) Satellite PRN #7
       ** 10) Satellite PRN #8
       ** 11) Satellite PRN #9
       ** 12) Satellite PRN #10
       ** 13) Satellite PRN #11
       ** 14) Satellite PRN #12
       ** 15) PDOP
       ** 16) HDOP
       ** 17) VDOP
       ** 18) System ID
       ** 19) Checksum
       */

    data class GSA(val operating_mode:String,
                   val fix_mode:String,
                   val listofsv: MutableList<String>,
                   val pdop:String,
                   val hdop: String,
                   val vdop:String,
                   val system_id:Int
    )


    data class NMEA(val rmc: RMC, val vtg: VTG, val gga: GGA, val gsa: GSA, val gsv: GSV, val gll: GLL){}

