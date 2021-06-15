package com.example.notificationmusictest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass : Application() {

     val CHANNEL_ID_1 = "CHANNEL_1"
     val CHANNEL_ID_2 = "CHANNEL_2"
     val ACTION_NEXT = "NEXT"
     val ACTION_PREV = "PREVIOUS"
     val ACTION_PLAY = "PLAY"


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel();
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel1 =
                NotificationChannel(CHANNEL_ID_1, "Channel(1)", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel1.description = "Channel 1 Description"
            val notificationChannel2 =
                NotificationChannel(CHANNEL_ID_2, "Chann el(2)", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel2.description = "Channel 2  Description"
            val notificationManager=getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel1)
            notificationManager.createNotificationChannel(notificationChannel2)
        }
    }

}