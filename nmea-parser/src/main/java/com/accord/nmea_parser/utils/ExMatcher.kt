package com.accord.nmea_parser.utils

import java.util.regex.Matcher

class ExMatcher(var original: Matcher) {
    var index = 0

    init {
        reset()
    }

    fun reset() {
        index = 1
    }

    fun matches(): Boolean {
        return original.matches()
    }

    fun nextString(name: String?): String? {
        return original.group(index++)
    }

    fun nextFloat(name: String?, defaultValue: Float): Float {
        val next = nextFloat(name)
        return next ?: defaultValue
    }

    fun nextFloat(name: String?): Float? {
        return nextString(name)?.toFloat()
    }

    fun nextInt(name: String?): Int? {
        return nextString(name)?.toInt()
    }

    fun nextHexInt(name: String?): Int? {
        return nextString(name)?.toInt(16)
    }
}