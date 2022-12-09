package dev.brdlf.medtracker

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import dev.brdlf.medtracker.viewmodel.DEBUG_TAG

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "CHANNEL"
const val TITLE_EXTRA = "titleExtra"
const val MESSAGE_EXTRA = "messageExtra"

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?: return
        p0?: return
        Log.d(DEBUG_TAG, "I am notifying you now")
        val notif : Notification = NotificationCompat.Builder(p0, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_outline_add_24)
            .setContentTitle(p1.getStringExtra(TITLE_EXTRA))
            .setContentText(p1.getStringExtra(MESSAGE_EXTRA))
            .build()

        val manager = p0.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notif)
    }
}