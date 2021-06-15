package com.example.notificationmusictest

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.media.session.MediaSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity(), ActionPlaying, ServiceConnection {

    lateinit var play: ImageView
    lateinit var previous: ImageView
    lateinit var next: ImageView
    lateinit var title: TextView
    var isPlaying = false

    var mediaSession: MediaSessionCompat? = null
     var musicService: MusicService? = null
    val trackList: MutableList<TrackFile> = ArrayList()
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = findViewById(R.id.title)
        play = findViewById(R.id.play)
        previous = findViewById(R.id.previous)
        next = findViewById(R.id.next)

        populatedFiles()
        title.text = trackList[position].title
        mediaSession = MediaSessionCompat(this, "PlayerAudio")



        play.setOnClickListener { playClicked() }
        previous.setOnClickListener { previousClicked() }
        next.setOnClickListener { nextClicked() }

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)

    }

    fun populatedFiles() {
        val trackFile1 = TrackFile("Faded", "Alan Walker", R.drawable.song1)
        val trackFile2 = TrackFile("Girl like you", "Phyo Thiha", R.drawable.song2)
        val trackFile3 = TrackFile("Something just like this", "Chainsmoker", R.drawable.song3)
        trackList.add(trackFile1)
        trackList.add(trackFile2)
        trackList.add(trackFile3)
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(this)
    }

    override fun nextClicked() {
        if (position == 2)
            position = 0
        else position++

        title.text = trackList[position].title
        if (!isPlaying) {
            showNotification(R.drawable.ic_play)
        } else {
            showNotification(R.drawable.ic_pause)
        }
    }

    override fun previousClicked() {
        if (position == 0)
            position = 2
        else position--

        title.text = trackList[position].title
        if (!isPlaying) {
            showNotification(R.drawable.ic_play)
        } else {
            showNotification(R.drawable.ic_pause)
        }
    }

    override fun playClicked() {
        if (!isPlaying) {
            isPlaying = true
            play.setImageResource(R.drawable.ic_pause)
            showNotification(R.drawable.ic_pause)
        } else {
            isPlaying = false
            play.setImageResource(R.drawable.ic_play)
            showNotification(R.drawable.ic_play)
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service!! as (MusicService.MyBinder)
        musicService = binder.getService()
        musicService!!.setCallBack(this@MainActivity)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    fun showNotification(playPauseBtn: Int) {
        val intent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val prevIntent =
            Intent(this, NotificationReceiver::class.java).setAction(ApplicationClass().ACTION_PREV)
        val prevPendingIntent =
            PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent =
            Intent(this, NotificationReceiver::class.java).setAction(ApplicationClass().ACTION_PLAY)
        val playPendingIntent =
            PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent =
            Intent(this, NotificationReceiver::class.java).setAction(ApplicationClass().ACTION_NEXT)
        val nextPendingIntent =
            PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val picture = BitmapFactory.decodeResource(resources, trackList[position].icon)

        val notification = NotificationCompat.Builder(this, ApplicationClass().CHANNEL_ID_2)
            .setSmallIcon(trackList[position].icon).setLargeIcon(picture)
            .setContentTitle(trackList[position].title)
            .setContentText(trackList[position].singer)
            .addAction(R.drawable.ic_previous, "Previous", prevPendingIntent)
            .addAction(playPauseBtn, "Play", playPendingIntent)
            .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession!!.sessionToken)
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(contentIntent)
            .setOnlyAlertOnce(true)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)


    }

}