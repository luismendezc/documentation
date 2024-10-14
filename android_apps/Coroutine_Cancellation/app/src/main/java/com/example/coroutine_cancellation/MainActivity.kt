package com.example.coroutine_cancellation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.coroutine_cancellation.assignmenttwo.MoneyTransferScreen
import com.example.coroutine_cancellation.assignmenttwo.MoneyTransferViewModel
import com.example.coroutine_cancellation.assignmenttwo.MoneyTransferViewModelFactory
import com.example.coroutine_cancellation.ui.theme.Coroutine_CancellationTheme

/*
[Easy] Assignment #1

You’ve created a file documenting your thoughts on your bird friends and want to write a short
application to upload it to a server for safekeeping. You write up a small demo project before
integrating the API and notice a bug…

Instructions
Find and fix the bug in the code below to terminate the program when it has been canceled.
----------------------------------------------------------------------------------------------------
[Medium] Assignment #2

You’ve been feeling charitable recently and decided to create a small application to simulate
transferring a donation to a local animal charity. You also ensure to include a cleanup function to
run after each transfer request, regardless of whether it was successful, failed, or canceled.
Upon completion, you encounter some unexpected behaviors.
When you cancel the transfer after money is taken from your account, the money is lost, and the
charity never receives it.
Resources aren’t properly cleaned up when cancelling a transfer

Instructions
Find the bugs and fix them.
Once money has been taken from your account, it has to be added to the charity’s account.
The application should always clean up the resources, whether the transfer succeeded, failed or was
cancelled.
Properly handle any CancellationExceptions

 */
class MainActivity : ComponentActivity() {

    //private val customScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val applicationScope = (applicationContext as MyApplication).applicationScope
        /*
        customScope.launch {
            delay(2000L)
            println("Job finished!")
        }
        lifecycleScope.launch {
            delay(1000L)
            customScope.coroutineContext.cancelChildren()
            customScope.launch {
                println("Hello World!")
            }
        }
         */

        /*
        val fileManager = FileManager(applicationContext)
        lifecycleScope.launch {
            val job = launch {
                fileManager.writeRecordsToFile()
            }
            delay(3000L)
            job.cancel()
        }*/

        setContent {
            Coroutine_CancellationTheme {
                //AssignmentOneScreen()
                val viewModelFactory = MoneyTransferViewModelFactory(applicationScope)
                val viewModel: MoneyTransferViewModel = viewModel(factory = viewModelFactory)

                MoneyTransferScreen(
                    state = viewModel.state,
                    onAction = viewModel::onAction
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Coroutine_CancellationTheme {

    }
}