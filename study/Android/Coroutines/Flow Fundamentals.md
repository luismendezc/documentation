
When we want to get multiple results then use flows.
```kotlin
fun flowDemo() {  
    GlobalScope.launch {  
        flow<Int> {  
            delay(1000L)  
            emit(1)  
            delay(1000L)  
            emit(2)  
            delay(1000L)  
            emit(3)  
        }.collect {  
            println("Current value $it")  
        }  
    }
}
    ```

### The structure of every launched flow:
The flow will never execute unless is "executed" by using these different options:
```kotlin
.collect {

}
//or
.onEach {
	println("Value emitted: $it")
}
.launchIn(this)
//or
.onEach {
	println("Value emitted: $it")
}
.first()
```

### Sharedflow
Sharedflow is a hot flow means that it emits values does not matter if someone is observing, the cold flow emits as it gets observed.
```kotlin
fun sharedFlowDemo() {  
    val sharedFlow = MutableSharedFlow<Int>()  
    GlobalScope.launch {  
        delay(3000L)  
        sharedFlow.onEach {  
            println("Collector 1: $it")  
        }.launchIn(this)  
        sharedFlow.onEach {  
            println("Collector 2: $it")  
        }.launchIn(this)  
    }  
    GlobalScope.launch {  
        repeat(10) {  
            delay(500L)  
            sharedFlow.emit(it)  
        }  
    }}
    ```
```
2024-10-20 09:50:29.951 10881-10916 System.out              com.example.flow_fundamentals        I  Collector 2: 5
2024-10-20 09:50:29.952 10881-10915 System.out              com.example.flow_fundamentals        I  Collector 1: 5
2024-10-20 09:50:30.456 10881-10915 System.out              com.example.flow_fundamentals        I  Collector 1: 6
2024-10-20 09:50:30.457 10881-10916 System.out              com.example.flow_fundamentals        I  Collector 2: 6
2024-10-20 09:50:30.964 10881-11098 System.out              com.example.flow_fundamentals        I  Collector 1: 7
2024-10-20 09:50:30.965 10881-10916 System.out              com.example.flow_fundamentals        I  Collector 2: 7
2024-10-20 09:50:31.473 10881-10914 System.out              com.example.flow_fundamentals        I  Collector 1: 8
2024-10-20 09:50:31.475 10881-10916 System.out              com.example.flow_fundamentals        I  Collector 2: 8
2024-10-20 09:50:31.986 10881-10914 System.out              com.example.flow_fundamentals        I  Collector 1: 9
2024-10-20 09:50:31.988 10881-10916 System.out              com.example.flow_fundamentals        I  Collector 2: 9
```


If we want to add a "buffer" so a "cache" of values we can add the replay:
```kotlin
val sharedFlow = MutableSharedFlow<Int>(  
    replay = 3  
)
```
```
2024-10-20 09:53:00.686 11512-11547 System.out              com.example.flow_fundamentals        I  Collector 1: 2
2024-10-20 09:53:00.687 11512-11545 System.out              com.example.flow_fundamentals        I  Collector 2: 3
2024-10-20 09:53:00.687 11512-11545 System.out              com.example.flow_fundamentals        I  Collector 2: 4
2024-10-20 09:53:00.687 11512-11545 System.out              com.example.flow_fundamentals        I  Collector 2: 5
2024-10-20 09:53:00.688 11512-11547 System.out              com.example.flow_fundamentals        I  Collector 1: 3
2024-10-20 09:53:00.688 11512-11547 System.out              com.example.flow_fundamentals        I  Collector 1: 4
2024-10-20 09:53:00.689 11512-11547 System.out              com.example.flow_fundamentals        I  Collector 1: 5
2024-10-20 09:53:01.192 11512-11558 System.out              com.example.flow_fundamentals        I  Collector 1: 6
2024-10-20 09:53:01.193 11512-11545 System.out              com.example.flow_fundamentals        I  Collector 2: 6
2024-10-20 09:53:01.697 11512-11558 System.out              com.example.flow_fundamentals        I  Collector 1: 7
2024-10-20 09:53:01.697 11512-11545 System.out              com.example.flow_fundamentals        I  Collector 2: 7
2024-10-20 09:53:02.202 11512-11558 System.out              com.example.flow_fundamentals        I  Collector 1: 8
2024-10-20 09:53:02.204 11512-11547 System.out              com.example.flow_fundamentals        I  Collector 2: 8
2024-10-20 09:53:02.710 11512-11558 System.out              com.example.flow_fundamentals        I  Collector 1: 9
2024-10-20 09:53:02.711 11512-11547 System.out              com.example.flow_fundamentals        I  Collector 2: 9
```

The MutableSharedFlow waits until all the collectors have finish processing the value, that could slow the process. we can add the extraBufferCapacity so the slow observers can finish but the quick one finish early.
```kotlin
val sharedFlow = MutableSharedFlow<Int>(  
    extraBufferCapacity = 5  
)
```

And what can we do when that buffer is overpassed? so we can set it back to the default behaviour of waiting by using onBufferOverflow parameter look:
```kotlin
val sharedFlow = MutableSharedFlow<Int>(  
    extraBufferCapacity = 5,  
    onBufferOverflow = BufferOverflow.SUSPEND  
)
```

or we can use DROP_OLDEST
```kotlin
val sharedFlow = MutableSharedFlow<Int>(  
    extraBufferCapacity = 5,  
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)
```

### StateFlow
For MutableStateFlow there only a single value we can pass which is the intial value of the StateFlow because the porpuse of the StateFlow is to hold a state, which is a value that can change over time.
```kotlin
fun stateFlowDemo() {  
    val stateFlow = MutableStateFlow(0)  
    stateFlow.onEach {  
        println("Value is $it")  
    }.launchIn(GlobalScope)  
}
```
```
2024-10-20 10:06:27.625 12169-12201 System.out              com.example.flow_fundamentals        I  Value is 0
```

Here how we change the value:
```kotlin
fun stateFlowDemo() {  
    val stateFlow = MutableStateFlow(0)  
    stateFlow.onEach {  
        println("Value is $it")  
    }.launchIn(GlobalScope)  
  
    GlobalScope.launch {  
        repeat(10){  
            delay(500L)  
            stateFlow.value = it  
        }    }}
        ```
```
2024-10-20 10:08:13.776 12390-12424 System.out              com.example.flow_fundamentals        I  Value is 0
2024-10-20 10:08:13.795 12390-12390 Compatibil...geReporter com.example.flow_fundamentals        D  Compat change id reported: 237531167; UID 10238; state: DISABLED
2024-10-20 10:08:13.968 12390-12394 ow_fundamentals         com.example.flow_fundamentals        I  Compiler allocated 4307KB to compile void android.view.ViewRootImpl.performTraversals()
2024-10-20 10:08:14.783 12390-12423 System.out              com.example.flow_fundamentals        I  Value is 1
2024-10-20 10:08:15.287 12390-12424 System.out              com.example.flow_fundamentals        I  Value is 2
2024-10-20 10:08:15.792 12390-12424 System.out              com.example.flow_fundamentals        I  Value is 3
2024-10-20 10:08:16.297 12390-12424 System.out              com.example.flow_fundamentals        I  Value is 4
2024-10-20 10:08:16.800 12390-12424 System.out              com.example.flow_fundamentals        I  Value is 5
2024-10-20 10:08:17.304 12390-12424 System.out              com.example.flow_fundamentals        I  Value is 6
2024-10-20 10:08:17.808 12390-12423 System.out              com.example.flow_fundamentals        I  Value is 7
2024-10-20 10:08:18.312 12390-12424 System.out              com.example.flow_fundamentals        I  Value is 8
2024-10-20 10:08:18.816 12390-12423 System.out              com.example.flow_fundamentals        I  Value is 9
```
Some example:
```kotlin
class FlowViewModel: ViewModel() {  
    private val _isLoading = MutableStateFlow(false)  
    val isLoading = _isLoading.asStateFlow()  
  
    init {  
        loadProfile()  
    }  
  
    private fun loadProfile() {  
        viewModelScope.launch {  
            _isLoading.value = true  
            delay(3000L)  
            _isLoading.value = false  
        }  
    }  
}  
  
@Composable  
fun LoadingScreen(  
    viewModel: FlowViewModel,  
    modifier: Modifier = Modifier) {  
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()  
    Box(modifier = Modifier.fillMaxSize(),  
        contentAlignment = Alignment.Center  
    ) {  
        if(isLoading) {  
            CircularProgressIndicator()  
        } else {  
            Text(text = "This is the laoded profile.")  
        }  
    }  
}

-------------------------------
// THIS IS MAINACTIVITY
setContent {  
    Flow_FundamentalsTheme {  
        val viewModel = viewModel<FlowViewModel>()  
        LoadingScreen(viewModel = viewModel)  
    }  
}
```


needed to add this in libs.versions.toml
```
androidx-lifecycle-runtime-compose = { module = "androidx.lifecycle:lifecycle-runtime-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { module = "androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
```
and also this in the gradle app:
```
implementation(libs.androidx.lifecycle.viewmodel.compose)  
implementation(libs.androidx.lifecycle.runtime.compose)
```

Example with an object state:
```kotlin
data class UiState(  
    val isLoading: Boolean = false,  
    val username: String? = null  
)  
  
class FlowViewModel: ViewModel() {  
  
    private val _state = MutableStateFlow(UiState())  
    val state = _state.asStateFlow()  
  
    private val _isLoading = MutableStateFlow(false)  
    val isLoading = _isLoading.asStateFlow()  
  
    init {  
        loadProfile()  
    }  
  
    private fun loadProfile() {  
        viewModelScope.launch {  
            _state.update { it.copy(isLoading = true) }  
            delay(3000L)  
            _state.update { it.copy(isLoading = false) }  
        }    }  
}  
  
@Composable  
fun LoadingScreen(  
    viewModel: FlowViewModel,  
    modifier: Modifier = Modifier) {  
    val state by viewModel.state.collectAsStateWithLifecycle()  
    Box(modifier = Modifier.fillMaxSize(),  
        contentAlignment = Alignment.Center  
    ) {  
        if(state.isLoading) {  
            CircularProgressIndicator()  
        } else {  
            Text(text = "This is the laoded profile.")  
        }  
    }  
}
```

Do the change on the main dispatcher, maybe the processing in the default.

### Statein
this works to convert a cold flow to a hot flow like:
```kotlin
fun flowDemo2() {  
    GlobalScope.launch {  
        val flow = flow<Int> {  
            delay(1000L)  
            emit(1)  
            delay(1000L)  
            emit(2)  
            delay(1000L)  
            emit(3)  
        }.stateIn(  
            GlobalScope,  
            SharingStarted.Eagerly,  
            0  
        )  
  
        flow.onEach {  
            println("Collector 1: $it")  
        }.launchIn(GlobalScope)  
  
        GlobalScope.launch {  
            delay(5000L)  
            flow.onEach {  
                println("Collector 2: $it")  
            }.launchIn(GlobalScope)  
        }  
  
    }}
    ```

The 0 is the first value emited, this could be also null.
And the SharingStarted.Eagerly means starts right away dont need to have any subscribers.
Ìf we want to start this with the first subscription then we need:
```kotlin
SharingStarted.Lazily
```

Also there is one option that only continues executing while is subscribed if no subscriber active anymore then ends.
```kotlin
SharingStarted.WhileSubscribed()
```

#### sharein cares about all emissions and state as the latest state

## Easy Assignment #1

_You want to build a small countdown application that starts a countdown and updates the UI every second. First, you want to construct a demo application to determine the logic behind the countdown timer._

### **Instructions**

Create a demo application with a single screen and a button labeled “Start Countdown.” When tapped, trigger a `flow {}` builder that emits the value ten, and then each second reduces that value and emits the new value until 0 is reached. The app should observe the emissions and display them on the screen.

---

## Medium Assignment #2

_Happy with the current implementation of your demo application, you now want to expand it and support configuration changes._

### **Instructions**

Expand your previous application with a new UI component that captures a numeric value as input and uses that value as the starting point of your countdown timer. The countdown timer should not be affected by a configuration change (i.e., device rotation).

```kotlin
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
        //sharedFlowDemo()        //stateFlowDemo()  
        setContent {  
            Flow_FundamentalsTheme {  
                //val viewModel = viewModel<FlowViewModel>()  
                //LoadingScreen(viewModel = viewModel)                CountdownScreen()  
            }  
        }    }  
}  
  
@Preview(showBackground = true)  
@Composable  
fun GreetingPreview() {  
    Flow_FundamentalsTheme {  
    }}  
  
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
            }        }    )  
}
```

```kotlin
package com.example.flow_fundamentals  
  
import androidx.lifecycle.ViewModel  
import androidx.lifecycle.viewModelScope  
import kotlinx.coroutines.Job  
import kotlinx.coroutines.delay  
import kotlinx.coroutines.flow.MutableStateFlow  
import kotlinx.coroutines.flow.asStateFlow  
import kotlinx.coroutines.flow.flow  
import kotlinx.coroutines.flow.update  
import kotlinx.coroutines.launch  
  
class CountDownViewModel: ViewModel() {  
    private val _countdownValue = MutableStateFlow(10)  
    val countdownValue = _countdownValue.asStateFlow()  
      
    private var countDownJob: Job? = null  
  
    fun startCountdown(startValue:Int) {  
        countDownJob?.cancel()  
            countDownJob = viewModelScope.launch {  
                countdownFlow(startValue).collect { value ->  
                    _countdownValue.update { value }  
                }            }  
  
    }  
  
    private fun countdownFlow(startValue: Int) = flow<Int> {  
        var currentValue = startValue  
        while(currentValue >= 0)  {  
            emit(currentValue)  
            delay(1000L)  
            currentValue--  
        }  
    }  
  
}
```

