package com.example.flow_fundamentals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flow_fundamentals.ui.theme.Flow_FundamentalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //flowDemo()
        //sharedFlowDemo()
        //stateFlowDemo()

        setContent {
            Flow_FundamentalsTheme {
                //val viewModel = viewModel<FlowViewModel>()
                //LoadingScreen(viewModel = viewModel)
                CountdownScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Flow_FundamentalsTheme {
    }
}

@Composable
fun CountdownScreen(viewModel: CountDownViewModel = viewModel()) {
    // Collect the countdown value from ViewModel
    val countdownValue by viewModel.countdownValue.collectAsStateWithLifecycle()
    var inputValue by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Countdown: $countdownValue")
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Enter countdown start value") },
                    modifier = Modifier.width(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val startValue = inputValue.toIntOrNull() ?: 10
                    viewModel.startCountdown(startValue) // Start countdown when button is clicked
                }) {
                    Text(text = "Start Countdown")
                }
            }
        }
    )
}
