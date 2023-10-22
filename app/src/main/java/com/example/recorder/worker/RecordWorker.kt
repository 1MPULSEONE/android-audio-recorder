package com.example.recorder.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.recorder.AndroidAudioRecorder
import java.io.File
import java.util.*

class RecordWorker(context: Context, params: WorkerParameters) : Worker(context, params) {


    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private var audioFile: File? =
        File("/data/user/0/com.example.recorder/cache/").
        listFiles()[File("/data/user/0/com.example.recorder/cache/").listFiles().size - 1]

    override fun doWork(): Result {
        // Check if it's between 9-10pm
        recordAudio()
        return Result.success()
    }

    private fun getStartTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    private fun getEndTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 22)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    private fun recordAudio() {
        File(applicationContext.cacheDir, "${System.currentTimeMillis()}.mp3").also {
            recorder.start(it)
            audioFile = it
        }
    }

}
