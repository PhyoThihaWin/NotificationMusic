package com.example.notificationmusictest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver : BroadcastReceiver() {

    val ACTION_NEXT = "NEXT"
    val ACTION_PREV = "PREVIOUS"
    val ACTION_PLAY = "PLAY"

    override fun onReceive(context: Context?, intent: Intent?) {
        val intent1 = Intent(context, MusicService::class.java)
        if (intent!!.action != null) {
            when (intent.action) {
                ACTION_PLAY -> {
                    Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show()
                    intent1.putExtra("myActionName", intent.action)
                    context!!.startService(intent1)
                }
                ACTION_NEXT -> {
                    Toast.makeText(context, "Next", Toast.LENGTH_SHORT).show()
                    intent1.putExtra("myActionName", intent.action)
                    context!!.startService(intent1)
                }
                ACTION_PREV -> {
                    Toast.makeText(context, "Previous", Toast.LENGTH_SHORT).show()
                    intent1.putExtra("myActionName", intent.action)
                    context!!.startService(intent1)
                }
            }
        }
    }
}