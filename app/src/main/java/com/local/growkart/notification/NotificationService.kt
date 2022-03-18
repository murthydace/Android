package com.local.growkart.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.system.Os
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.local.growkart.GrowKartActivity
import com.local.growkart.R
import com.local.growkart.util.NotificationUtil
import java.lang.Exception

class NotificationService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }

    override fun onMessageReceived(p0: RemoteMessage) {
        try {
            Log.d("FCCMTOKE", "message recieved !!")
            if (p0.notification != null)
                showNotification(p0.notification!!.title, p0.notification!!.body)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNotification(remoteMessage: String?, body: String?) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = NavDeepLinkBuilder(baseContext)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.cart)
            .setComponentName(GrowKartActivity::class.java)
            .createPendingIntent()
        val notification = NotificationCompat.Builder(
            applicationContext,
            NotificationUtil.NOTIFICATION_CHANNEL_ID
        ).apply {
            setSmallIcon(R.drawable.logo_removebg_preview)
            setOnlyAlertOnce(true)
            setContentTitle(remoteMessage)
            setContentText(body)
            setContentIntent(pendingIntent)
            setSound(defaultSoundUri)
            setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
        }
        notificationManager.notify(0, notification.build())
    }

}

