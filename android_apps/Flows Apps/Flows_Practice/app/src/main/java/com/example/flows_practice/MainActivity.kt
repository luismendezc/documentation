package com.example.flows_practice

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
import com.example.flows_practice.ui.theme.Flows_PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        backpressureDemo()
        //flatMapDemo()
        setContent {
            Flows_PracticeTheme {
                /*Scaffold { innerPadding ->
                    WebSocketUi(modifier = Modifier.padding(innerPadding))
                }*/
                //TimerUi()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Flows_PracticeTheme {
    }
}