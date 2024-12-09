package com.example.flows_practice

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.roundToInt

data class WebSocketLog(
    val formattedTime: String,
    val log: String
)

class WebSocketViewModel: ViewModel() {
    private val client = WebSocketClient(HttpClientFactory.create())

    @RequiresApi(Build.VERSION_CODES.O)
    val receivedLogs = client
        .listenToSocket("wss://echo.websocket.org/")
        .retryWhen() { cause, attempt ->
            delay(2f.pow(attempt.toInt()).roundToInt() * 1000L)
            cause is UnresolvedAddressException && attempt < 4
        }
        .catch { cause ->
            when(cause) {
                is UnresolvedAddressException -> {
                    println("Oops, no internet!")
                }
            }
        }
        .runningFold(initial = emptyList<WebSocketLog>()) { logs, newLog ->
            val formattedTime = DateTimeFormatter
                .ofPattern("dd-MM-yyyyy, hh:mm:ss")
                .format(LocalDateTime.now())
            logs + WebSocketLog(formattedTime = formattedTime,
                log = newLog)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    fun sendMessage(text: String){
        viewModelScope.launch {
            client.sendMessage(text)
        }
    }
}