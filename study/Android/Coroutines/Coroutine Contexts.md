Android and coroutine context have nothing in common but they do cover the same concept.

The coroutine context describes the state of a currently running coroutine.
Is a collection of things that a coroutine could consist of, also allows us the control of the coroutine.

No big difference between CoroutineScope and coroutineContext

```kotlin
@file:OptIn(ExperimentalStdlibApi::class)  
  
package com.example.coroutine_contexts  
  
import kotlinx.coroutines.CoroutineDispatcher  
import kotlinx.coroutines.CoroutineExceptionHandler  
import kotlinx.coroutines.CoroutineName  
import kotlinx.coroutines.CoroutineScope  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.Job  
import kotlinx.coroutines.launch  
import kotlin.coroutines.coroutineContext  
  
suspend fun queryDatabase() {  
    val job = coroutineContext[Job]  
    val name = coroutineContext[CoroutineName]  
    val handler = coroutineContext[CoroutineExceptionHandler]  
    val dispatcher = coroutineContext[CoroutineDispatcher]  
  
    println("Job: $job")  
    println("Name: $name")  
    println("Handler: $handler")  
    println("Dispatcher: $dispatcher")  
  
    CoroutineScope(Dispatchers.Main + CoroutineName("Cool coroutine!")).launch {  
        println("Dispatcher: ${coroutineContext[CoroutineDispatcher]}")  
        println("Name: ${coroutineContext[CoroutineName]}")  
    }  
}
```

One of coroutine super powers is that coroutines are able to swtich the contexts at any time, it can switch the thread, the job, the name, the esception handler.

More important is the THREAD!

```kotlin
package com.example.coroutine_contexts  
  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.withContext  
  
suspend fun withContextDemo() {  
    println("Thread: ${Thread.currentThread().name}")  
    withContext(Dispatchers.Main) {  
        println("Thread: ${Thread.currentThread().name}")  
        withContext(Dispatchers.IO){  
            println("Thread: ${Thread.currentThread().name}")  
        }  
    }}
```
```
2024-10-09 11:31:37.204 13797-13829 System.out              com.example.coroutine_contexts       I  Thread: DefaultDispatcher-worker-2
2024-10-09 11:31:37.370 13797-13797 System.out              com.example.coroutine_contexts       I  Thread: main
2024-10-09 11:31:37.371 13797-13829 System.out              com.example.coroutine_contexts       I  Thread: DefaultDispatcher-worker-2
```


Each dispatcher is optimised to do certain task so IO related operations like making a network call or reading from a file is best executed on the IO Dispatcher, the Default Dispatcher is very good for CPU Heavy tasks, and we have the Main Dispatcher which runs on the UI Thread so if we need to update the UI we need the Main Thread.

Dispatchers.IO and Dispatchers.Default use a thread pool.
Basically if you need to wait for something use IO and if you have to do a lot of calculations then Default.

ioDefaultDispatcher.kt (Dispatchers.IO)
```kotlin
package com.example.coroutine_contexts  
  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.GlobalScope  
import kotlinx.coroutines.launch  
import kotlin.system.measureTimeMillis  
  
fun ioDefaultDispatcher() {  
    /*GlobalScope.launch {  
        println("Thread: ${Thread.currentThread().name}")        launch {            println("Thread: ${Thread.currentThread().name}")        }    }*/    val threads = hashMapOf<Long, String>()  
    val job = GlobalScope.launch(Dispatchers.IO) {  
        repeat(100) {  
            launch {  
                threads[Thread.currentThread().id] = Thread.currentThread().name  
                // Blocking network call  
                Thread.sleep(1000L)  
            }  
        }    }    GlobalScope.launch {  
        val timeMillis = measureTimeMillis {  
            job.join()  
        }  
        println("Launcher ${threads.keys.size} threads in $timeMillis ms.")  
    }  
}
```
```
2024-10-09 14:59:29.794  8174-8272  System.out              com.example.coroutine_contexts       I  Launcher 64 threads in 2030 ms.
```

ioDefaultDispatcher.kt (Dispatchers.Default)
```kotlin
package com.example.coroutine_contexts  
  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.GlobalScope  
import kotlinx.coroutines.launch  
import kotlin.system.measureTimeMillis  
  
fun ioDefaultDispatcher() {  
    val threads = hashMapOf<Long, String>()  
    val job = GlobalScope.launch(Dispatchers.Default) {  
        repeat(100) {  
            launch {  
                threads[Thread.currentThread().id] = Thread.currentThread().name  
                (1..100_000).map {  
                    it * it  
                }            }        }    }    GlobalScope.launch {  
        val timeMillis = measureTimeMillis {  
            job.join()  
        }  
        println("Launcher ${threads.keys.size} threads in $timeMillis ms.")  
    }  
}
```
```
2024-10-09 14:58:00.773  7954-8010  System.out              com.example.coroutine_contexts       I  Launcher 8 threads in 1901 ms.
```


Main thread, just exists one so that is why is called the UI Thread, the responsability is constantly update App´s UI.

There are 2 different dispatchers.
- Dispatchers.Main
- Dispatchers.Main.immediate

The difference is that the immediate will try to do the ui change immediately meanwhile the Main thread will use the queue and but before the previous tasks.

lifecycleScope and viewModelScope will use the Dispatchers.Main.immediate for example.
```kotlin
package com.example.coroutine_contexts  
  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.withContext  
  
suspend fun mainDispatcher() {  
    withContext(Dispatchers.Main) {  
        withContext(Dispatchers.Main.immediate) {  
  
        }    
    }
}
```

If it is needed at one point you can create your own CoroutineDispatcher:
```kotlin
val dispatcher = object : CoroutineDispatcher() {  
  
}
```


Dispatchers.Unconfined
This Dispatcher will simply enter on the thread that the coroutine was on when we call the withContext(Dispatchers.Unconfined)
Use this Dispatcher when you dont care in which thread runs, is not encouraged from Android.


```kotlin
package com.example.coroutine_contexts  
  
import kotlinx.coroutines.CoroutineDispatcher  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.GlobalScope  
import kotlinx.coroutines.launch  
import kotlinx.coroutines.withContext  
  
fun unconfinedDispatcher() {  
    GlobalScope.launch {  
        withContext(Dispatchers.Main) {  
            println("Thread: ${Thread.currentThread().name}")  
            withContext(Dispatchers.Unconfined) {  
                println("Thread: ${Thread.currentThread().name}")  
                withContext(Dispatchers.IO) {  
                    println("Thread: ${Thread.currentThread().name}")  
                }  
                println("Thread: ${Thread.currentThread().name}")  
            }  
        }    
    }
}
```
```
2024-10-09 17:12:43.795 17516-17516 System.out              com.example.coroutine_contexts       I  Thread: main
2024-10-09 17:12:43.796 17516-17516 System.out              com.example.coroutine_contexts       I  Thread: main
2024-10-09 17:12:43.796 17516-17591 System.out              com.example.coroutine_contexts       I  Thread: DefaultDispatcher-worker-2
2024-10-09 17:12:43.797 17516-17591 System.out              com.example.coroutine_contexts       I  Thread: DefaultDispatcher-worker-2
```

Exercise.

AssignmentTwoScreen.kt
```kotlin
package com.plcoding.coroutinesmasterclass.homework  
  
import android.graphics.BitmapFactory  
import android.net.Uri  
import androidx.activity.compose.rememberLauncherForActivityResult  
import androidx.activity.result.PickVisualMediaRequest  
import androidx.activity.result.contract.ActivityResultContracts  
import androidx.compose.foundation.Image  
import androidx.compose.foundation.background  
import androidx.compose.foundation.layout.Arrangement  
import androidx.compose.foundation.layout.Column  
import androidx.compose.foundation.layout.Spacer  
import androidx.compose.foundation.layout.fillMaxSize  
import androidx.compose.foundation.layout.height  
import androidx.compose.material3.Button  
import androidx.compose.material3.Text  
import androidx.compose.runtime.Composable  
import androidx.compose.runtime.LaunchedEffect  
import androidx.compose.runtime.getValue  
import androidx.compose.runtime.mutableStateOf  
import androidx.compose.runtime.remember  
import androidx.compose.runtime.rememberCoroutineScope  
import androidx.compose.runtime.setValue  
import androidx.compose.ui.Alignment  
import androidx.compose.ui.Modifier  
import androidx.compose.ui.graphics.Color  
import androidx.compose.ui.platform.LocalContext  
import androidx.compose.ui.unit.dp  
import coil.compose.rememberAsyncImagePainter  
import coil.request.ImageRequest  
import com.plcoding.coroutinesmasterclass.util.PhotoProcessor  
import com.plcoding.coroutinesmasterclass.util.RotatingBoxScreen  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.async  
import kotlinx.coroutines.launch  
import kotlinx.coroutines.withContext  
  
@Composable  
fun AssignmentTwoScreen() {  
    var photoUri by remember { mutableStateOf<Uri?>(null) }  
    val context = LocalContext.current  
    val scope = rememberCoroutineScope()  
    var isLoading by remember { mutableStateOf(false) }  
    var backgroundColor by remember {  
        mutableStateOf(Color.White)  
    }  
  
    val imagePicker = rememberLauncherForActivityResult(  
        contract = ActivityResultContracts.PickVisualMedia()  
    ) {  
        photoUri = it  
    }  
    LaunchedEffect(photoUri) {  
        if (photoUri != null) {  
            withContext(Dispatchers.Default) {  
                val bitmap = async(Dispatchers.IO){  
                     context.contentResolver.openInputStream(photoUri!!).use {  
                        BitmapFactory.decodeStream(it)  
                    }  
                }                isLoading = true  
                val dominantColor = PhotoProcessor.findDominantColor(bitmap.await())  
                isLoading = false  
                backgroundColor = Color(dominantColor)  
            }  
  
        }  
    }  
  
    Column(  
        verticalArrangement = Arrangement.Center,  
        horizontalAlignment = Alignment.CenterHorizontally,  
        modifier = Modifier  
            .fillMaxSize()  
            .background(backgroundColor),  
    ) {  
        RotatingBoxScreen()  
        Spacer(modifier = Modifier.height(64.dp))  
        Button(onClick = {  
            scope.launch {  
                withContext(Dispatchers.IO){  
                    imagePicker.launch(  
                        input = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),  
                    )  
                }  
            }        }) {  
            Text(text = "Pick Image")  
        }  
        if (isLoading) {  
            Text(text = "Finding dominant color...")  
        }  
  
        if (photoUri != null) {  
            val painter = rememberAsyncImagePainter(  
                ImageRequest  
                    .Builder(context)  
                    .data(data = photoUri)  
                    .build()  
            )  
  
            Image(  
                painter = painter,  
                contentDescription = null  
            )  
        }  
    }  
}
```
PhotoProcessor.kt
```kotlin
package com.plcoding.coroutinesmasterclass.util  
  
import android.graphics.Bitmap  
  
object PhotoProcessor {  
    fun findDominantColor(image: Bitmap): Int {  
        val colorCounts = mutableMapOf<Int, Int>()  
        for (x in 0 until image.width) {  
            for (y in 0 until image.height) {  
                val pixelColor = image.getPixel(x, y)  
                colorCounts[pixelColor] = colorCounts.getOrDefault(pixelColor, 0) + 1  
            }  
        }  
        return colorCounts.entries.sortedByDescending { it.value }  
            .take(1)  
            .map { it.key }  
            .first()  
    }  
}
```
RotatingBoxScreen.kt
```kotlin
package com.plcoding.coroutinesmasterclass.util  
  
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
fun RotatingBoxScreen(modifier: Modifier = Modifier) {  
    val infiniteTransition = rememberInfiniteTransition(label = "")  
    val angleRatio by infiniteTransition.animateFloat(  
        initialValue = 0f,  
        targetValue = 1f,  
        animationSpec = infiniteRepeatable(  
            animation = tween(durationMillis = 1000)  
        ),  
        label = ""  
    )  
    Box(  
        modifier = modifier,  
        contentAlignment = Alignment.Center  
    ) {  
        Box(  
            modifier = Modifier  
                .size(100.dp)  
                .graphicsLayer {  
                    rotationZ = 360f * angleRatio  
                }  
                .background(Color.Red)  
  
        )  
    }  
}
```
MainActivity.kt
```kotlin
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
 */class MainActivity : ComponentActivity() {  
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
                }            val zazu =  
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
                }            val woodstock =  
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
                }            tweety.join()  
            zazu.join()  
            woodstock.join()  
        }  
  
  
        setContent {  
            Coroutine_ContextsTheme {  
                AssignmentTwoScreen()  
            }  
        }    }  
}  
  
@Preview(showBackground = true)  
@Composable  
fun GreetingPreview() {  
    Coroutine_ContextsTheme {  
  
    }}
    ```
    
