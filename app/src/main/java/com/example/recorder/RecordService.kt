package com.example.recorder

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import java.io.File


class RecordService : Service() {

    private var audioFile: File? = null

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private fun recordAudio() {
        File(applicationContext.cacheDir, "${System.currentTimeMillis()}.mp3").also {
            recorder.start(it)
            audioFile = it
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stop() {
        recorder.stop()
    }

    private fun start() {
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Recording in progress")
                .setContentText("Your app is currently recording in the background")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        startForeground(FOREGROUND_SERVICE_ID, notification)

        // Start your recording logic here
        recordAudio()
    }


    companion object {
        const val CHANNEL_ID = "record_channel"
        private const val FOREGROUND_SERVICE_ID = 1234
    }

    enum class Actions {
        START, STOP
    }
}