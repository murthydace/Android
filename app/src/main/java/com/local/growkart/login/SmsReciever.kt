package com.local.growkart.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsReciever : BroadcastReceiver() {
    private lateinit var listener: SmsListener
    fun updateListener(smsListener: SmsListener) {
        listener = smsListener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    Log.e("Message", message)
                    val splitMessages = message.split(" is ")
                    if (this::listener.isInitialized)
                        listener.onOTPRecieved(splitMessages[0])
                }
                CommonStatusCodes.TIMEOUT -> {

                }
            }
        }
    }

    interface SmsListener {
        fun onOTPRecieved(otp: String)
    }
}