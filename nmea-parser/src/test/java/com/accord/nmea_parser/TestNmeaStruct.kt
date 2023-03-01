package com.accord.nmea_parser

import com.accord.nmea_parser.utils.ExMatcher
import com.accord.nmea_parser.utils.NmeaStruct
import org.junit.Assert
import org.junit.Test

class TestNmeaStruct {

    @Test
    fun validate_Nmea_msg_Struct() {

        val p_general_sentence = NmeaStruct.GENERAL_SENTENCE
        val msg_case1 = "\$GNRMC,122507.00,A,1257.45652,N,07738.50299,E,0.042,,130223,,,D,V*18"
        Assert.assertTrue("GENERAL_SENTENCE Test", p_general_sentence.matcher(msg_case1.trim()).matches())

    }

    @Test
    fun validate_RMC_Msg_Pattern() {
        val p_rmc = NmeaStruct.RMC
        val msg_case1 = "122507.00,A,1257.45652,N,07738.50299,E,0.042,,130223,,,D,V"
        Assert.assertTrue("RMC Test", ExMatcher(p_rmc.matcher(msg_case1)).matches())
    }

    @Test
    fun validate_VTG_Msg_Pattern() {
        val p_rmc = NmeaStruct.VTG
        val msg_case1 = "0.234,T,0.234,M,0.042,N,0.078,K,D"
        Assert.assertTrue("VTG TestPassed", p_rmc.matcher(msg_case1).matches())
    }

    @Test
    fun validate_GGA_Msg_Pattern() {
        val p_rmc = NmeaStruct.GGA
        val msg_case1 = "122507.00,1257.45652,N,07738.50299,E,2,12,0.54,908.8,M,-86.4,M,,0000"
        Assert.assertTrue("VTG TestPassed", p_rmc.matcher(msg_case1).matches())
    }

    @Test
    fun validate_GSA_Msg_Pattern() {
        val p_rmc = NmeaStruct.GSA
        val msg_case1 = "A,3,37,05,13,07,30,03,10,08,,,,,0.98,0.54,0.82,4"
        Assert.assertTrue("VTG TestPassed", p_rmc.matcher(msg_case1).matches())
    }

    @Test
    fun validate_GSv_Msg_Pattern() {
        val p_rmc = NmeaStruct.GSV
        val msg_case1 = "3,1,09,70,12,119,25,71,05,165,,73,19,154,40,74,67,154,39,1"
        Assert.assertTrue("VTG TestPassed", p_rmc.matcher(msg_case1).matches())
    }  @Test

    fun validate_GLL_Msg_Pattern() {
        val p_rmc = NmeaStruct.GLL
        val msg_case1 = "1257.45652,N,07738.50299,E,,A,D"
        Assert.assertTrue("GLL TestPassed", p_rmc.matcher(msg_case1).matches())
    }

    @Test
    fun validateRMC()
    {
        val msg_case1 = "\$GNRMC,122507.00,A,1257.45652,N,07738.50299,E,0.042,,130223,,,D,V*18"

        //val parser:RMC=NmeaparseManager.parseRMC(msg_case1)


    }
}