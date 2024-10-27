package com.example.coroutine_basics

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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coroutine_basics.ui.theme.Coroutine_BasicsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/*
[Easy] Assignment #1
Each morning, you wake up to the sound of birds. Over time, you’ve noticed three unique bird sounds,
each repeating at a different pace. One bird makes a sound every second, the other every 2 seconds,
and the last every 3 seconds.

Instructions:
Recreate the timing of each bird’s sounds using a single coroutine for each bird. Each coroutine
should only print four times before completing.
The first bird makes a “Coo” sound.
The second bird makes a “Caw” sound.
The last bird makes a “Chirp” sound.

Hint: Assignment #1
Ensure you launch new coroutines inside GlobalScope and call join() to keep the program alive until
all the coroutines have been completed.
----------------------------------------------------------------------------------------------------
[Medium] Assignment #2
Once woken up by the birds, you enjoy listening to them for a while. Afterward, you must prepare for
 the day, so after listening to the birds for a while, you close your window and get ready.

Instructions
Extend the previous assignment by removing the limitation of only printing four times to the console;
each coroutine can print indefinitely now. Add a mechanism to cancel all running coroutines after 10
seconds.

Hint: Assignment #2
Make sure you have a reference to the scope used to launch the individual coroutines.
----------------------------------------------------------------------------------------------------
[Hard] Assignment #3
You decide to make a simple mobile application to simulate the birds’ sounds you hear in the morning.
After planning, you’ve decided on a single screen with three buttons. Each button will show the bird's
name on the screen and print its sound to the console. Tapping a new button will replace the previous
bird's name on the screen, and only the new bird’s sounds will print to the console.

Instructions
Create a single-screen app using coroutines in Compose. Add three buttons, each representing a
different bird. Create a composable function for each bird that displays the bird’s name and launches
a coroutine to print the bird's sound to the console. Only render the composable of the bird represented
by the last button tapped and ensure only the selected bird's sounds are printed.

Hint: Assignment #3
1. Use a state variable in the main composable to help you toggle between the bird composables based on
the button tapped.
2. Use a LaunchedEffect inside each bird composable to manage the cancellation of the coroutine and print
out the bird sounds.
 */
class MainActivity : ComponentActivity() {
    /* Assignment #2
    private val birdScope = CoroutineScope(Dispatchers.Main)
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /* Assignment #1
        GlobalScope.launch {
            val job1 =
                launch {
                    repeat(4) {
                        var timeMillis =
                            measureTimeMillis {
                                delay(1000L)
                                println("Coo")
                            }
                        println("Coo time in milis $timeMillis milliseconds")
                    }
                }
            val job2 =
                launch {
                    repeat(4) {
                        var timeMillis =
                            measureTimeMillis {
                                delay(2000L)
                                println("Caw")
                            }
                        println("Caw time in milis $timeMillis milliseconds")
                    }
                }
            val job3 =
                launch {
                    repeat(4) {
                        var timeMillis =
                            measureTimeMillis {
                                delay(3000L)
                                println("Chirp")
                            }
                        println("Chirp time in milis $timeMillis milliseconds")
                    }
                }
            job1.join()
            job2.join()
            job3.join()
        }
        */
        /* Assignment #2
        birdScope.launch {
            val job1 =
                launch {
                    while(isActive){
                        delay(1000L)
                        println("Coo")
                    }
                }
            val job2 =
                launch {
                    while(isActive){
                        delay(2000L)
                        println("Caw")
                    }
                }
            val job3 =
                launch {
                    while(isActive){
                        delay(3000L)
                        println("Chirp")
                    }
                }
            delay(10000L)
            birdScope.cancel()
        }
        */


        setContent {
            Coroutine_BasicsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BirdSoundScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BirdSoundScreen(modifier: Modifier = Modifier) {
    // State variable to track the selected bird
    var selectedBird by remember { mutableStateOf<Bird?>(null) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Buttons to select different birds
        Button(onClick = { selectedBird = Bird.Coo }) {
            Text("Coo Bird")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { selectedBird = Bird.Caw }) {
            Text("Caw Bird")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { selectedBird = Bird.Chirp }) {
            Text("Chirp Bird")
        }

        // Display the selected bird's name and start its coroutine
        selectedBird?.let { bird ->
            BirdSoundComposable(bird = bird)
        }
    }
}

@Composable
fun BirdSoundComposable(bird: Bird) {
    // LaunchEffect ensures that the coroutine is canceled if the composable is recomposed
    LaunchedEffect(bird) {
        while (isActive) {
            delay(bird.delayTime)
            println(bird.sound)
        }
    }

    // Display the selected bird's name
    Text(text = bird.name, modifier = Modifier.padding(top = 32.dp))
}

enum class Bird(val nameBird: String, val sound: String, val delayTime: Long) {
    Coo("Coo Bird", "Coo", 1000L),
    Caw("Caw Bird", "Caw", 2000L),
    Chirp("Chirp Bird", "Chirp", 3000L)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Coroutine_BasicsTheme {
        BirdSoundScreen()
    }
}

