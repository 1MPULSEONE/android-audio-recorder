package com.example.recorder.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}