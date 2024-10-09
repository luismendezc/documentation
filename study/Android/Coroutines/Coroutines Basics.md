A coroutine runs its instructions sequentially just like a single thread until it hits a so called suspension point.
Suspension Point are any instructions that Require Waiting for a Specific Outcome.
For example a Network call or a large file copy.
![[Pasted image 20241008103749.png]]

Advantages to work with coroutines:
- More lightweight than Threads
- Easy to Work with
![[Pasted image 20241008103903.png]]
- Easy Switching Between Threads

Difference between Suspending code and Blocking code:
![[Pasted image 20241008104059.png]]

parallel is not the same as concurrent, parallel can be used when we have a multiple core cpu, the concurrency just give us the illusion because it switches so quickly from task to task.

#### Example of coroutine:
```kotlin
class MainActivity : ComponentActivity() {  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
        GlobalScope.launch {  
            repeat(100) {  
                println("Hello from coroutine 1")  
            }  
        }        GlobalScope.launch {  
            repeat(100) {  
                println("Hello from coroutine 2")  
            }  
        }  
        setContent {  
            Coroutine_BasicsTheme {  
            }        }    }  
}
```
```
2024-10-08 10:53:06.425 10716-10808 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:53:06.425 10716-10808 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:53:06.425 10716-10808 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:53:06.425 10716-10808 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:53:06.426 10716-10808 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:53:06.426 10716-10808 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:53:06.426 10716-10808 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:53:06.426 10716-10808 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:53:06.426 10716-10808 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:53:06.426 10716-10808 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:53:06.427 10716-10809 System.out              com.example.coroutine_basics         I  Hello from coroutine 2
2024-10-08 10:53:06.427 10716-10809 System.out              com.example.coroutine_basics         I  Hello from coroutine 2
2024-10-08 10:53:06.427 10716-10809 System.out              com.example.coroutine_basics         I  Hello from coroutine 2
2024-10-08 10:53:06.428 10716-10809 System.out              com.example.coroutine_basics         I  Hello from coroutine 2
2024-10-08 10:53:06.428 10716-10809 System.out              com.example.coroutine_basics         I  Hello from coroutine 2
2024-10-08 10:53:06.428 10716-10809 System.out              com.example.coroutine_basics         I  Hello from coroutine 2
2024-10-08 10:53:06.428 10716-10809 System.out              com.example.coroutine_basics         I  Hello from coroutine 2
2024-10-08 10:53:06.428 10716-10809 System.out              com.example.coroutine_basics         I  Hello from coroutine 2
2024-10-08 10:53:06.428 10716-10809 System.out              com.example.coroutine_basics         I  Hello from coroutine 2
2024-10-08 10:53:06.428 10716-10809 System.out              com.example.coroutine_basics         I  Hello from coroutine 2
```

#### Here a example of suspend with delay():
```kotlin
class MainActivity : ComponentActivity() {  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
        GlobalScope.launch {  
            repeat(10) {  
                delay(50L)  
                println("Hello from coroutine 1")  
            }  
        }  
        setContent {  
            Coroutine_BasicsTheme {  
            }        }    }  
}
```
```
2024-10-08 10:55:15.565 14063-14115 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:55:15.616 14063-14115 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:55:15.667 14063-14115 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:55:15.719 14063-14115 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:55:15.770 14063-14115 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:55:15.792 14063-14067 oroutine_basics         com.example.coroutine_basics         I  Compiler allocated 4307KB to compile void android.view.ViewRootImpl.performTraversals()
2024-10-08 10:55:15.820 14063-14115 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:55:15.871 14063-14115 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:55:15.922 14063-14115 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:55:15.973 14063-14115 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
2024-10-08 10:55:16.024 14063-14115 System.out              com.example.coroutine_basics         I  Hello from coroutine 1
```

#### Structured concurrency
We can have a parent and child and the child a child coroutines and so on.

```kotlin
class MainActivity : ComponentActivity() {  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
        GlobalScope.launch {  
            launch {  
                launch {  
                    delay(1000L)  
                    println("Innermost coroutine finished!")  
                }  
                delay(500L)  
                println("Inner coroutine finished!")  
            }  
            println("Outermost coroutine finished!")  
        }  
  
        setContent {  
            Coroutine_BasicsTheme {  
            }        }    }  
}
```
```
2024-10-08 11:00:30.971 14787-14842 System.out              com.example.coroutine_basics         I  Outermost coroutine finished!
2024-10-08 11:00:31.474 14787-14841 System.out              com.example.coroutine_basics         I  Inner coroutine finished!
2024-10-08 11:00:31.975 14787-14841 System.out              com.example.coroutine_basics         I  Innermost coroutine finished!
```

#### Blocking code:
delays allows other coroutines to execute but Thread.sleep will stop the hole thread in this case the Main UI Thread.

```kotlin
class MainActivity : ComponentActivity() {  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
        GlobalScope.launch(Dispatchers.Main) {  
            // Suspending call
            delay(3000L)  
            // Blocking call
            Thread.sleep(3000L)  
        }  
  
        setContent {  
            Coroutine_BasicsTheme {  
                RotatingBoxScreen()  
            }  
        }    }  
}
```
![video|100x100](videos/rotatingBox.mp4)


Extra.

RotatingBoxScreen.kt
```kotlin
package com.example.coroutine_basics.util  
  
import androidx.compose.animation.core.animateFloat  
import androidx.compose.animation.core.infiniteRepeatable  
import androidx.compose.animation.core.rememberInfiniteTransition  
import androidx.compose.animation.core.tween  
import androidx.compose.foundation.background  
import androidx.compose.foundation.layout.Box  
import androidx.compose.foundation.layout.fillMaxSize  
import androidx.compose.foundation.layout.size  
import androidx.compose.runtime.Composable  
import androidx.compose.runtime.getValue  
import androidx.compose.ui.Alignment  
import androidx.compose.ui.Modifier  
import androidx.compose.ui.graphics.Color  
import androidx.compose.ui.graphics.graphicsLayer  
import androidx.compose.ui.unit.dp  
  
@Composable  
fun RotatingBoxScreen(modifier: Modifier = Modifier){  
    val infiniteTransition = rememberInfiniteTransition(label = "")  
    val angleRatio by infiniteTransition.animateFloat(  
        initialValue = 0f,  
        targetValue = 1f,  
        animationSpec = infiniteRepeatable(  
            animation = tween(durationMillis = 1000)  
        ) , label = ""  
    )  
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {  
        Box(modifier = Modifier  
            .size(100.dp)  
            .graphicsLayer {  
                rotationZ = 360f * angleRatio  
            }.background(Color.Red))  
    }  
}
```

#### Coroutine scopes

- GlobalScope
	- Will never be cancelled until the process dies
- lifecycleScope
	- couples the coroutine that are launched with the lifetime with either the activity or the fragment they are launched in

```kotlin
lifecycleScope.launch {
	delay(5000L)
	println("Lifecycle Scope coroutine finished")
}
```

- viewModelScope
	- Live as long as the view model lives
```kotlin
class MainViewModel: ViewModel() {
	init {
		viewModelScope.launch {
		}
	}
}
```

- CoroutineScope
	- Custom coroutine scope, need to define the thread. We have to also say when to end.
```kotlin
private val customLifecycleScope = CoroutineScope(Dispatchers.Main)

override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	enableEdgeToEdge()
	customLifecycleScope.launch{
		delay(5000L)
		println("Custom Lifecycle Scope coroutine finished")
	}
	setContent {
		CoroutinesMasterclassTheme {
		}
	}
}

override fun onDestroy() {
	super.onDestroy()
	customLifecycleScope.cancel()
}
```

if we cancel a coroutine like:
```kotlin
customLifecycleScope.cancel()
```
we cancel the hole coroutine and not only the children.

#### Job
Is an object that just contains the information about the a coroutine.
A job is a cancellable thing with a lifecycle that culminates in its completion.

```kotlin
val job = lifecycleScope.launch {
	val innerJob1 = launch {
		delay(2000L)
		println("Inner job 1 finished")
	}
	val innerJob2 = launch {
		delay(1000L)
		println("Inner job 2 finished")
	}
	delay(1000L)
	innerJob1.cancel()
}
```
A coroutine is nothing else than a state machine, which always is on a specific state.
```kotlin
val job = lifecycleScope.launch {
	val innerJob1 = launch {
		delay(2000L)
		println("Inner job 1 finished")
	}
	val innerJob2 = launch {
		delay(1000L)
		println("Inner job 2 finished")
	}
	delay(500L)
	println("Is inner job 2 still active?: ${innerJob2.isActive}")
	delay(600L)
	println("Is inner job 2 still active?: ${innerJob2.isActive}")
	println("Is inner job 2 completed?: ${innerJob2.isCompleted}")
	println("Is inner job 2 cancelled?: ${innerJob2.isCancelled}")
}
```
##### join
The join will suspend the coroutine they are currently in for as long as the job takes to complete or to be cancelled.
```kotlin
val job = lifecycleScope.launch {
	val innerJob1 = launch {
		delay(2000L)
		println("Inner job 1 finished")
	}
	val innerJob2 = launch {
		delay(1000L)
		println("Inner job 2 finished")
	}
	innerJob1.join()
	innerJob2.join()
}
```
To measure the time it takes:
```kotlin
val timeMillis = measureTimeMillis {
	innerJob1.join()
	innerJob2.join()
}
println("Jobs took $timeMillis milliseconds.")
```

When getting values we use **async**, we use "deferred" because it defers the result because it takes some time.

**await** is equivalent to join, but with the return of result in the async block.

```kotlin
val job = lifecycleScope.launch {
	val profileDeferred = async {
		println("Fetching profile data")
		delay(2000L)
		"profile"
	}
	val postsDeferred = async {
		println("Fetching profile posts...")
		delay(3000L)
		"posts"
	}
	val timeMillis = measureTimeMillis {
		val posts = postsDeferred.await() 	
		val profile = profileDeferred.await()

		println("Profile loaded: $profile, $posts")
	}
	println("Jobs took $timeMillis")
}
```

Exercises:
```kotlin
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
 */class MainActivity : ComponentActivity() {  
    /* Assignment #2  
    private val birdScope = CoroutineScope(Dispatchers.Main)    */  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
        /* Assignment #1  
        GlobalScope.launch {            val job1 =                launch {                    repeat(4) {                        var timeMillis =                            measureTimeMillis {                                delay(1000L)                                println("Coo")                            }                        println("Coo time in milis $timeMillis milliseconds")                    }                }            val job2 =                launch {                    repeat(4) {                        var timeMillis =                            measureTimeMillis {                                delay(2000L)                                println("Caw")                            }                        println("Caw time in milis $timeMillis milliseconds")                    }                }            val job3 =                launch {                    repeat(4) {                        var timeMillis =                            measureTimeMillis {                                delay(3000L)                                println("Chirp")                            }                        println("Chirp time in milis $timeMillis milliseconds")                    }                }            job1.join()            job2.join()            job3.join()        }        */        /* Assignment #2        birdScope.launch {            val job1 =                launch {                    while(isActive){                        delay(1000L)                        println("Coo")                    }                }            val job2 =                launch {                    while(isActive){                        delay(2000L)                        println("Caw")                    }                }            val job3 =                launch {                    while(isActive){                        delay(3000L)                        println("Chirp")                    }                }            delay(10000L)            birdScope.cancel()        }        */  
  
        setContent {  
            Coroutine_BasicsTheme {  
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->  
                    BirdSoundScreen(modifier = Modifier.padding(innerPadding))  
                }  
            }        }    }  
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
    }}  
  
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
```