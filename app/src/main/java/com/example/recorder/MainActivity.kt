package com.example.recorder

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.work.*
import com.example.recorder.ui.theme.RecorderTheme
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )
        setContent {
            RecorderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            Intent(applicationContext, RecordService::class.java).also {
                                it.action = RecordService.Actions.START.toString()
                                startService(it)
                            }
                        })
                        { Text("Начать слежку") }
                        Button(onClick = {
                            Intent(applicationContext, RecordService::class.java).also {
                                it.action = RecordService.Actions.STOP.toString()
                                startService(it)
                                stopService(it)
                            }
                        }) {
                            Text(text = "Stop recording")
                        }
                        Button(onClick = {
                            val fileSize =
                                File(applicationContext.cacheDir.toString()).listFiles()?.size
                            if (fileSize != null) {
                                player.playFile(
                                    File(applicationContext.cacheDir.toString()).listFiles()?.get(fileSize - 1)
                                        ?: return@Button
                                )
                            }
                        }) {
                            Text(text = "Play")
                        }
                        Button(onClick = {
                            player.stop()
                        }) {
                            Text(text = "Stop playing")
                        }
                        SwitchMinimalExample()
                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwitchMinimalExample() {
    var checked by remember { mutableStateOf(true) }
    if (checked) {
        //TODO: Add interval logic with server
    }
    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        },
    )
    Text(
        text = if (checked) "Слежка активна" else "Слежка не активна",
        fontSize = 24.sp
    )
}

private fun getDelay(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    val startTime = calendar.timeInMillis
    val currentTime = Calendar.getInstance().timeInMillis
    return if (startTime > currentTime) {
        startTime - currentTime
    } else {
        startTime + TimeUnit.DAYS.toMillis(1) - currentTime
    }
}

