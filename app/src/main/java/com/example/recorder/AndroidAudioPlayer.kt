package com.example.recorder

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import com.example.recorder.playback.AudioPlayer
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
) : AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {

        val filePath = file.absolutePath
        Log.d("AndroidAudioPlayer", filePath.length.toString())
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}