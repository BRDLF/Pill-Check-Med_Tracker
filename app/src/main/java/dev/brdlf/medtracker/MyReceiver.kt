package dev.brdlf.medtracker

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

const val notificationID = 1
const val channelID = "CHANNEL"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?: return
        p0?: return
        val notif : Notification = NotificationCompat.Builder(p0, channelID)
            .setSmallIcon(R.drawable.ic_outline_add_24)
            .setContentTitle(p1.getStringExtra(titleExtra))
            .setContentText(p1.getStringExtra(messageExtra))
            .build()

        val manager = p0.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notif)
    }
}