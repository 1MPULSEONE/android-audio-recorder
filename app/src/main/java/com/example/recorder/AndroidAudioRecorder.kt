package com.example.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import com.example.recorder.record.AudioRecorder
import java.io.*

class AndroidAudioRecorder(
    private val context: Context
) : AudioRecorder {

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)
            val listFileSize = File(context.cacheDir.toString()).listFiles()?.size
            Log.d("ВАЖНО!!!", "$listFileSize")
            Log.d("ВАЖНО!!!", "СЛЕЖКА НАЧАЛАСЬ!!!!")
            prepare()
            start()

            recorder = this
        }
    }


    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }


}