package com.local.growkart.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import com.local.growkart.GrowKartActivity
import com.local.growkart.R

class NotificationUtil {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "grow_kart-notif-id"
        const val NOTIFICATION_CHANNEL_NAME = "General"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "General updates for GrowKart"

        @JvmStatic
        fun createChannelAndHandleNotifications(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.description = NOTIFICATION_CHANNEL_DESCRIPTION
                channel.setShowBadge(false)
                channel.enableVibration(true)
                channel.setBypassDnd(false)
                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager?.createNotificationChannel(channel)
            }
        }

        @JvmStatic
        fun sendCartReminderNotification(context: Context, navController: NavController){
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val pendingIntent =
                navController.createDeepLink()
                    .setDestination(R.id.cart)
                    .createPendingIntent()
//                NavDeepLinkBuilder(context.applicationContext)
//                .setGraph(R.navigation.nav_graph)
//                .setDestination(R.id.cart)
//                .setComponentName(GrowKartActivity::class.java)
//                .createPendingIntent()
            val notification = NotificationCompat.Builder(
                context.applicationContext,
                NotificationUtil.NOTIFICATION_CHANNEL_ID
            ).apply {
                setSmallIcon(R.drawable.logo_removebg_preview)
                setOnlyAlertOnce(true)
                setContentTitle("Cart is waiting!!")
                setContentText("Your cart has products and is waiting for you!!")
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
}
