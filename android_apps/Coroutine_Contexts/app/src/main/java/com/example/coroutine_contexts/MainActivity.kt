package com.example.coroutine_contexts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.coroutine_contexts.ui.theme.Coroutine_ContextsTheme
import com.plcoding.coroutinesmasterclass.homework.AssignmentTwoScreen
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
/*
[Easy] Assignment #1

Throughout your day, you came up with a name for each bird: Tweety, Zazu, and Woodstock.

Instructions

Continuing from the previous program you’ve written in Coroutine Basics, give each coroutine a name
and print them out alongside the sounds they make.
----------------------------------------------------------------------------------------------------
[Medium] Assignment #2

You’ve taken a couple of pictures of the birds you hear each morning, and you decide to write an
application to select one of these pictures and then set the app’s background to the predominant
color in the image.

Instructions

Take the existing project shared below with a single screen containing a button and the RotatingBox
composable. Tapping the button should launch a picker that allows the user to select an image. Once
an image is selected, use the function findDominantColor to get the primary color in the image.
Change the app’s background to this new color.

Fix the program so that when executed, the rotating box runs smoothly after selecting an image.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        GlobalScope.launch {
            val tweety =
                CoroutineScope(Dispatchers.IO + CoroutineName("Tweety")).launch {
                    repeat(4) {
                        var timeMillis =
                            measureTimeMillis {
                                delay(1000L)
                                println("Coo")
                                println(coroutineContext[CoroutineName])
                            }
                        println("Coo time in milis $timeMillis milliseconds")
                    }
                }
            val zazu =
                CoroutineScope(Dispatchers.IO + CoroutineName("Zazu")).launch {
                    repeat(4) {
                        var timeMillis =
                            measureTimeMillis {
                                delay(2000L)
                                println("Caw")
                                println(coroutineContext[CoroutineName])
                            }
                        println("Caw time in milis $timeMillis milliseconds")
                    }
                }
            val woodstock =
                CoroutineScope(Dispatchers.IO + CoroutineName("Woodstock")).launch {
                    repeat(4) {
                        var timeMillis =
                            measureTimeMillis {
                                delay(3000L)
                                println("Chirp")
                                println(coroutineContext[CoroutineName])
                            }
                        println("Chirp time in milis $timeMillis milliseconds")
                    }
                }
            tweety.join()
            zazu.join()
            woodstock.join()
        }


        setContent {
            Coroutine_ContextsTheme {
                AssignmentTwoScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Coroutine_ContextsTheme {

    }
}