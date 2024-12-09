package com.example.coroutine_error_handling

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
import androidx.lifecycle.lifecycleScope
import com.example.coroutine_error_handling.ui.theme.Coroutine_Error_HandlingTheme
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread

/*
[Medium] Assignment #1

You want to send out a weekly newsletter to your friends and family where you cover some interesting
topics about a specific bird each week. You setup a small program that takes in a list of email
addresses and then sends the newsletter to each of them. You decide to use coroutines to send emails
asynchronously, but you’ve encountered a bug.

Instructions
The program includes a check to see if the email is valid before sending out the email. If it is not
valid, it throws an exception. Fix the program so that an email is sent to all valid email addresses
regardless of whether there are some invalid ones in the list.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /*
        val handler = CoroutineExceptionHandler {coroutineContext, throwable ->
            throwable.printStackTrace()
        }*/
        /*
                lifecycleScope.launch(handler) {
                    launch {
                        delay(1000L)
                        throw Exception("Oops!")
                    }
                    delay(2000L)
                    println("Coroutine finished!")
                }*/
        /*
        val coroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
        coroutineScope.launch(handler) {
            launch {
                delay(1000L)
                throw Exception("Oops!")
            }
            delay(2000L)
            println("Coroutine1 finished!")
        }

        coroutineScope.launch(handler) {
            delay(2000L)
            println("Coroutine2 finished!")
        }*/

        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }

        lifecycleScope.launch(handler) {
            EmailService.addToMailingList(
                listOf(
                    "dancing.dave@email.com",
                    "caffeinated.coder@email.com",
                    "bookworm.betty@email.com",
                    "gardening.guru@email.com",
                    "sleepy.slothemail.com",
                    "hungry.hippo@email.com",
                    "clueless.cathy@email.com",
                    "techy.tom@email.com",
                    "musical.maryemail.com",
                    "adventurous.alice@email.com"
                )
            )
            EmailService.sendNewsletter()
            println("Done sending emails")
        }


        setContent {
            Coroutine_Error_HandlingTheme {

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Coroutine_Error_HandlingTheme {
    }
}