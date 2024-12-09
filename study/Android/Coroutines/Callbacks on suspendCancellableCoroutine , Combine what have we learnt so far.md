When we have a callback in a suspend function we can use the *suspendCancellableCoroutine*, *suspendCoroutine* and the *continuation* like this:
```kotlin
package com.example.combininglearning  
  
import android.Manifest  
import android.content.Context  
import android.content.pm.PackageManager  
import android.location.Location  
import android.location.LocationManager  
import android.os.Build  
import android.os.Bundle  
import android.os.CancellationSignal  
import androidx.activity.ComponentActivity  
import androidx.activity.compose.setContent  
import androidx.activity.enableEdgeToEdge  
import androidx.annotation.RequiresApi  
import androidx.compose.runtime.Composable  
import androidx.compose.ui.tooling.preview.Preview  
import androidx.core.app.ActivityCompat  
import androidx.core.content.getSystemService  
import androidx.lifecycle.lifecycleScope  
import com.example.combininglearning.ui.theme.CombiningLearningTheme  
import kotlinx.coroutines.delay  
import kotlinx.coroutines.launch  
import kotlinx.coroutines.suspendCancellableCoroutine  
import java.util.concurrent.Executors  
import kotlin.coroutines.resume  
import kotlin.coroutines.resumeWithException  
  
class MainActivity : ComponentActivity() {  
    @RequiresApi(Build.VERSION_CODES.R)  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
        ActivityCompat.requestPermissions(  
            this,  
            arrayOf(  
                Manifest.permission.ACCESS_FINE_LOCATION,  
                Manifest.permission.ACCESS_COARSE_LOCATION  
            ),  
            0  
        )  
  
        lifecycleScope.launch {  
            val job = launch {  
                val location = getLocation()  
                println("Location: (${location.latitude}, ${location.longitude})")  
            }  
            delay(5L)  
            job.cancel()  
        }  
  
        setContent {  
            CombiningLearningTheme {  
  
            }        }    }  
}  
  
@RequiresApi(Build.VERSION_CODES.R)  
suspend fun Context.getLocation(): Location {  
    return suspendCancellableCoroutine { continuation ->  
        val locationManager = getSystemService<LocationManager>()!!  
        val hasFineLocationPermission = ActivityCompat.checkSelfPermission(  
            this,  
            Manifest.permission.ACCESS_FINE_LOCATION  
            ) == PackageManager.PERMISSION_GRANTED  
        val hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(  
            this,  
            Manifest.permission.ACCESS_COARSE_LOCATION  
        ) == PackageManager.PERMISSION_GRANTED  
  
        val signal = CancellationSignal()  
  
        if(hasFineLocationPermission && hasCoarseLocationPermission) {  
            locationManager.getCurrentLocation(  
                LocationManager.GPS_PROVIDER,  
                signal,  
                mainExecutor  
            ) { location ->  
                println("Got location $location")  
                continuation.resume(location)  
            }  
        } else {  
            continuation.resumeWithException(  
                RuntimeException("Missing location permission")  
            )  
        }  
        continuation.invokeOnCancellation {  
            signal.cancel()  
        }  
    }}  
  
  
@Preview(showBackground = true)  
@Composable  
fun GreetingPreview() {  
    CombiningLearningTheme {  
  
    }}
    ```

Hard Assignment #1  
With your newfound knowledge of coroutines, you decide to replicate an authentication prompt that  you’ve seen on your favorite banking application. Upon testing your code, you’ve found a couple of  bugs...  
  
Instructions  
Closely study the project linked below, which includes a button that, when tapped, will display an authentication prompt to a user. The prompt should be dismissed after a specific time has passed. Locate and fix the following bugs that you’ve discovered:  
  
1. The app crashes when the user has no authentication method enrolled on the device.  
2. The biometric prompt is not automatically cancelled after the timeout period.  
3. The button does not launch the biometric prompt once it has been cancelled once.  
  
https://github.com/philipplackner/CoroutinesMasterclass/tree/combining_what_youve_learnt_so_far/homework/assignment_1

