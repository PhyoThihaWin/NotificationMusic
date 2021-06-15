package com.example.notificationmusictest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicService() : Service() {

    var mBinder = MyBinder()
    val ACTION_NEXT = "NEXT"
    val ACTION_PREV = "PREVIOUS"
    val ACTION_PLAY = "PLAY"
    var actionPlaying: ActionPlaying? = null

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    //
    inner class MyBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val actionName = intent!!.getStringExtra("myActionName")
        if (actionName != null) {
            when (actionName) {
                ACTION_PLAY -> if (actionPlaying != null) {
                    actionPlaying!!.playClicked()
                } else Log.e("error", "error fuck")
                ACTION_NEXT -> if (actionPlaying != null) {
                    actionPlaying!!.nextClicked()
                }
                ACTION_PREV -> if (actionPlaying != null) {
                    actionPlaying!!.previousClicked()
                }
            }
        } else Log.e("error", "error fuck")
        return START_STICKY
    }

    fun setCallBack(ap: ActionPlaying) {
        this.actionPlaying = ap
    }
}