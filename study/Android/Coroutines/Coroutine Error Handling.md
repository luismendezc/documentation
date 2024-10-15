This is nothing different than just throwing an exception in a normal thread it will make our app crash if we don't catch it.

```kotlin
lifecycleScope.launch {  
    launch {  
        delay(1000L)  
        try {  
            throw Exception("Oops!")  
        } catch (e: Exception) {  
            ensureActive()  
            e.printStackTrace()  
        }  
    }  
    delay(2000L)  
    println("Coroutine finished!")  
}
```

Different approach if we don't catch in the child coroutine.
CoroutineExceptionHandler (is not common to use that much)

```kotlin
val handler = CoroutineExceptionHandler {coroutineContext, throwable ->  
    throwable.printStackTrace()  
}  
  
lifecycleScope.launch(handler) {  
    launch {  
        delay(1000L)  
        throw Exception("Oops!")  
    }  
    delay(2000L)  
    println("Coroutine finished!")  
}
```
```
2024-10-14 18:21:28.085 16480-16480 System.err              com...mple.coroutine_error_handling  W  java.lang.Exception: Oops!
2024-10-14 18:21:28.085 16480-16480 System.err              com...mple.coroutine_error_handling  W  	at com.example.coroutine_error_handling.MainActivity$onCreate$1$1.invokeSuspend(MainActivity.kt:33)
2024-10-14 18:21:28.085 16480-16480 System.err              com...mple.coroutine_error_handling  W  	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
.
.
.
```



SupervisorJob
This will cause that Coroutine2 dont run because was completely cancelled in the inner launch from the Coroutine 1
```kotlin
val handler = CoroutineExceptionHandler {coroutineContext, throwable ->  
    throwable.printStackTrace()  
}
val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)  
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
}
```
With SupervisorJob children can fail independently.

```kotlin
val handler = CoroutineExceptionHandler {coroutineContext, throwable ->  
    throwable.printStackTrace()  
}
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
}
```

coroutineScope and supervisorScope

are not created as the normal CoroutineScope... bla bla but those work when we want to launch inside a suspend fun.
coroutineScope if one launch (coroutine) is launched and fails then all the scope is cancelled and the other children launchs will be cancelled, in the other hand with supervisorScope if one chil fails no problem the others keep executing.

```kotlin
suspend fun saveBitmap(context: Context, bitmap: Bitmap) {  
    withContext(Dispatchers.IO){  
        val bytes = ByteArrayOutputStream().use { byteStrem ->  
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStrem)  
            yield()  
            byteStrem.toByteArray()  
        }  
        yield()  
        val file = File(context.filesDir, "${bitmap.hashCode()}.jpg")  
        FileOutputStream(file).use { stream ->  
            stream.write(bytes)  
        }  
    }}  
  
suspend fun compressAndSaveImages(  
    context: Context,  
    images: List<Uri>,  
    compressor: BitmapCompressor  
) {  
    supervisorScope {  
        images.forEach { uri ->  
            launch {  
                compressor.compressImage(  
                    contentUri = uri,   
                    compressionThreshold = 1024L  
                )?.let{ bitmap ->  
                    saveBitmap(context, bitmap)  
                }      
            }  
        }    }}
        ```

**`coroutineScope { ... }`**: This suspends the calling coroutine until all the coroutines launched inside it complete.

Homework:

Medium Assignment #1  
  
You want to send out a weekly newsletter to your friends and family where you cover some interesting  
topics about a specific bird each week. You setup a small program that takes in a list of email  
addresses and then sends the newsletter to each of them. You decide to use coroutines to send emails  
asynchronously, but you’ve encountered a bug.  
  
Instructions  
The program includes a check to see if the email is valid before sending out the email. If it is not  
valid, it throws an exception. Fix the program so that an email is sent to all valid email addresses  
regardless of whether there are some invalid ones in the list.

```kotlin
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
  
class MainActivity : ComponentActivity() {  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
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
  
            }        }    }  
}  
  
@Preview(showBackground = true)  
@Composable  
fun GreetingPreview() {  
    Coroutine_Error_HandlingTheme {  
    }}
    ```

```kotlin
package com.example.coroutine_error_handling  
  
import kotlinx.coroutines.coroutineScope  
import kotlinx.coroutines.delay  
import kotlinx.coroutines.launch  
import kotlinx.coroutines.supervisorScope  
import kotlin.random.Random  
  
object EmailService {  
    private val mailingList = mutableListOf<String>()  
  
    fun addToMailingList(emails: List<String>) {  
        mailingList.addAll(emails)  
    }  
  
    suspend fun sendNewsletter() {  
        //Here the solution  
        supervisorScope {  
            mailingList.forEach { emailAddress ->  
                launch {  
                    sendEmail(emailAddress)  
                }  
            }        }    }  
  
    private suspend fun sendEmail(address: String) {  
        println("Sending email to $address")  
        if (!address.contains("@")) {  
            throw Exception("Invalid email address: $address")  
        }  
        delay(Random.nextLong(1500, 4000))  
        println("Email sent to $address")  
    }  
}
```
