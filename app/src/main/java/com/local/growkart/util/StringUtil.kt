package com.local.growkart.util

import android.annotation.SuppressLint
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class StringUtil {
    companion object {
        fun getCurrencySymbol(): String? {
            val indianLocale = Locale("hi", "IN")
            val currency = Currency.getInstance(indianLocale)
            return currency.getSymbol(indianLocale)
        }

        fun getCurrentTimeStamp(): Long {
            return System.currentTimeMillis()
        }

        @SuppressLint("SimpleDateFormat")
        fun getDateFromEpochTimeStamp(timeStamp: Long?): String {
            val formatter = SimpleDateFormat("dd/MM/yy hh:mm a")
            try {
                return if (timeStamp != null) formatter.format(Date(timeStamp)) else ""
            } catch (e: Exception) {
                return ""
            }

        }

    }


}