Hole project putting in practice:
TimerFlow.kt
```kotlin
fun timeAndEmit(emissionsPerSecond: Float): Flow<Duration> {  
    return flow {  
        var lastEmitTime = System.currentTimeMillis()  
        emit(Duration.ZERO)  
        while(true) {  
            delay((1000L / emissionsPerSecond).roundToLong())  
            val currentTime = System.currentTimeMillis()  
            val elapsedTime = currentTime - lastEmitTime  
            emit(elapsedTime.milliseconds)  
            lastEmitTime = currentTime  
        }  
    }  
}
```
TimerViewModel.kt
```kotlin
  
class TimerViewModel: ViewModel() {  
    val formattedTime = timeAndEmit(10f)  
        .runningReduce { totalElapsedTime, newElapsedTime ->  
            totalElapsedTime + newElapsedTime  
        }.map { totalElapsedTime ->  
            totalElapsedTime.toComponents { hours, minutes, seconds, nanoseconds ->  
                String.format(  
                    "%02d:%02d:%02d:%02d",  
                    hours,  
                    minutes,  
                    seconds,  
                    nanoseconds / (1_000_000L * 10L)  
                )  
            }  
        }.stateIn(  
            viewModelScope,  
            SharingStarted.WhileSubscribed(5000L),  
            "00:00:00:00"  
        )  
  
    val totalProgressTimeMillis = 5000L  
    val progress = timeAndEmit(100f)  
        .runningReduce { totalElapsedTime, newElapsedTime ->  
            totalElapsedTime + newElapsedTime  
        }.map { totalDuration ->  
            (totalDuration.inWholeMilliseconds / totalProgressTimeMillis.toFloat())  
                .coerceIn(0f, 1f)  
        }.filter { progressFraction ->  
            progressFraction in (0f .. 1f)  
        }.stateIn(  
            viewModelScope,  
            SharingStarted.WhileSubscribed(5000L),  
            0f  
  
        )  
}
```


TimerUi.kt
```kotlin
@Composable  
fun TimerUi(  
    viewModel: TimerViewModel = viewModel(),  
    modifier: Modifier = Modifier  
) {  
    val time by viewModel.formattedTime.collectAsStateWithLifecycle()  
    val progress by viewModel.progress.collectAsStateWithLifecycle()  
    Column(  
        modifier = modifier.fillMaxSize(),  
        verticalArrangement = Arrangement.Center,  
        horizontalAlignment = Alignment.CenterHorizontally  
    ) {  
        Text(text = time)  
        Spacer(modifier = Modifier.height(16.dp))  
        LinearProgressIndicator(  
            progress = { progress },  
            modifier = Modifier.padding(16.dp).fillMaxWidth()  
        )  
    }  
}
```

More operations:
```kotlin
.runningReduce
.zip
.onEach
.runningFold
.map
```

How to use combine for example:
```kotlin
private val _email = MutableStateFlow("")
val email = _email.asStateFlow()

private val _password = MutableStateFlow("")
val password = _password.asStateFlow()

val canRegister = email
	.debounce(500L)
	.combine(
		password,
	) { email, password ->
		val isValidEmail = PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
		val isValidPassword = password.any { !it.isLetterOrDigit() } &&
			password.length > 9
		isValidPassword && isValidEmail
	}.stateIn (
		viewModelScope,
		SharingStarted.WhileSubscribed(5000L),
		false
	)

fun onEmailChange(email:String) {
	_email.value = email
}

fun onPasswordChange(password: String) {
	_password.value = password
}
```

### Create a WebSocket

```kotlin
object HttpClientFactory {  
    fun create(): HttpClient {  
        return HttpClient(CIO) {  
            install(WebSockets)  
            install(ContentNegotiation) {  
                json(  
                    json = Json {  
                        ignoreUnknownKeys = true  
                    }  
                )  
            }  
            install(Logging) {  
                level = LogLevel.BODY  
                logger = Logger.ANDROID  
            }  
            defaultRequest {  
                contentType(ContentType.Application.Json)  
            }  
        }    }  
}
```

```kotlin
class WebSocketClient(  
    private val httpClient: HttpClient  
) {  
    private var session: WebSocketSession? = null  
  
    suspend fun sendMessage(text:String){  
        session?.send(text)  
    }  
  
    fun listenToSocket(url: String): Flow<String> {  
        return callbackFlow {  
            session = httpClient.webSocketSession(urlString = url)  
            session?.let { session ->  
                session  
                    .incoming  
                    .consumeAsFlow()  
                    .filterIsInstance<Frame.Text>()  
                    .collect {  
                        send(it.readText())  
                    }  
            } ?: run {  
                session?.close()  
                session = null  
                close()  
            }  
  
            awaitClose {  
                launch(NonCancellable) {  
                    session?.close()  
                    session = null  
                }  
            }        }    }  
}
```


```kotlin
data class WebSocketLog(  
    val formattedTime: String,  
    val log: String  
)  
  
class WebSocketViewModel: ViewModel() {  
    private val client = WebSocketClient(HttpClientFactory.create())  
  
    @RequiresApi(Build.VERSION_CODES.O)  
    val receivedLogs = client  
        .listenToSocket("wss://echo.websocket.org/")  
        .runningFold(initial = emptyList<WebSocketLog>()) { logs, newLog ->  
            val formattedTime = DateTimeFormatter  
                .ofPattern("dd-MM-yyyyy, hh:mm:ss")  
                .format(LocalDateTime.now())  
            logs + WebSocketLog(formattedTime = formattedTime,  
                log = newLog)  
        }  
        .stateIn(  
            viewModelScope,  
            SharingStarted.WhileSubscribed(5000L),  
            emptyList()  
        )  
  
    fun sendMessage(text: String){  
        viewModelScope.launch {  
            client.sendMessage(text)  
        }  
    }  
}
```


```kotlin
@OptIn(ExperimentalFoundationApi::class)  
@Composable  
fun WebSocketUi(  
    viewModel: WebSocketViewModel = viewModel(),  
    modifier: Modifier = Modifier  
) {  
    val receivedLogs by viewModel.receivedLogs.collectAsStateWithLifecycle()  
  
    LazyColumn(  
        modifier = modifier.fillMaxSize(),  
        contentPadding = PaddingValues(16.dp),  
        verticalArrangement = Arrangement.spacedBy(8.dp)  
    ) {  
        stickyHeader {  
            Row(  
                modifier = Modifier.fillMaxWidth(),  
                verticalAlignment = Alignment.CenterVertically  
            ) {  
                var text by remember {  
                    mutableStateOf("")  
                }  
                TextField(  
                    value = text,  
                    onValueChange = { text = it },  
                    modifier = Modifier.weight(1f)  
                )  
                IconButton(onClick = { viewModel.sendMessage(text) }) {  
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send,  
                        contentDescription = "Send")  
                }  
            }        }        items(receivedLogs) { log ->  
            Text(text = "${log.formattedTime}: ${log.log}")  
        }  
    }}
```


```kotlin
<uses-permission android:name="android.permission.INTERNET"/>
```

```kotlin
[versions]
ktor = "2.3.12"

[libraries]
ktor-client-auth = { module = "io.ktor:ktor-client-auth", version.ref = "ktor" }  
ktor-client-cio = { module = "io.ktor:ktor-client-cio", version.ref = "ktor" }  
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }  
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }  
ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }  
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }  
ktor-client-websockets = { module = "io.ktor:ktor-client-websockets", version.ref = "ktor" }

[bundles]  
ktor = [  
    "ktor-client-auth",  
    "ktor-client-cio",  
    "ktor-client-content-negotiation",  
    "ktor-client-core",  
    "ktor-client-logging",  
    "ktor-serialization-kotlinx-json",  
    "ktor-client-websockets",  
]
```
### Handling Flow Errors & Retrying Failed Flows

Also don't forget that when is cancelled the flow it also has  a cancel exception so if we want to add a try catch block inside the callback flow or something then also take care of that and re throw the cancellation exception.
```kotlin
class WebSocketViewModel: ViewModel() {  
    private val client = WebSocketClient(HttpClientFactory.create())  
  
    @RequiresApi(Build.VERSION_CODES.O)  
    val receivedLogs = client  
        .listenToSocket("wss://echo.websocket.org/")  
        .retry { cause ->  
            cause is UnresolvedAddressException  
        }  
        .catch { cause ->  
            when(cause) {  
                is UnresolvedAddressException -> {  
                    println("Oops, no internet!")  
                }  
            }  
        }  
        .runningFold(initial = emptyList<WebSocketLog>()) { logs, newLog ->  
            val formattedTime = DateTimeFormatter  
                .ofPattern("dd-MM-yyyyy, hh:mm:ss")  
                .format(LocalDateTime.now())  
            logs + WebSocketLog(formattedTime = formattedTime,  
                log = newLog)  
        }  
        .stateIn(  
            viewModelScope,  
            SharingStarted.WhileSubscribed(5000L),  
            emptyList()  
        )  
  
    fun sendMessage(text: String){  
        viewModelScope.launch {  
            client.sendMessage(text)  
        }  
    }  
}
```

If we want to limit the attempts then we use:
```kotlin
.retryWhen() { cause, attempt ->  
    cause is UnresolvedAddressException && attempt < 4  
}
.catch { cause ->  
            when(cause) {  
                is UnresolvedAddressException -> {  
                    println("Oops, no internet!")  
                }  
            }  
        } 
```

We can also delay the retry like:
```kotlin
.retryWhen() { cause, attempt ->  
    delay(5000L)  
    cause is UnresolvedAddressException && attempt < 4  
}  
.catch { cause ->  
    when(cause) {  
        is UnresolvedAddressException -> {  
            println("Oops, no internet!")  
        }  
    }  
}
```
exponential retry wait time:
```kotlin
.retryWhen() { cause, attempt ->  
    delay(2f.pow(attempt.toInt()).roundToInt() * 1000L)  
    cause is UnresolvedAddressException && attempt < 4  
}  
.catch { cause ->  
    when(cause) {  
        is UnresolvedAddressException -> {  
            println("Oops, no internet!")  
        }  
    }  
}
```

### flatMapConcat, flatMapLatest

flatMapConcat
```kotlin
fun flatMapDemo() {  
    flow<Int> {  
        emit(1)  
        delay(1000L)  
        emit(2)  
        delay(1000L)  
        emit(3)  
    }.flatMapConcat {  
        flow {  
            emit("One")  
            delay(1000L)  
            emit("Two")  
            delay(1000L)  
            emit("Three")  
        }  
    }        .onEach {  
            println("Emission is $it")  
        }  
        .launchIn(GlobalScope)  
}
```
```
2024-10-22 17:40:53.091 24198-24312 System.out              com.example.flows_practice           I  Emission is One
2024-10-22 17:40:54.096 24198-24313 System.out              com.example.flows_practice           I  Emission is Two
2024-10-22 17:40:55.100 24198-24313 System.out              com.example.flows_practice           I  Emission is Three
2024-10-22 17:40:56.106 24198-24313 System.out              com.example.flows_practice           I  Emission is One
2024-10-22 17:40:57.110 24198-24313 System.out              com.example.flows_practice           I  Emission is Two
2024-10-22 17:40:58.114 24198-24313 System.out              com.example.flows_practice           I  Emission is Three
2024-10-22 17:40:59.122 24198-24313 System.out              com.example.flows_practice           I  Emission is One
2024-10-22 17:41:00.127 24198-24313 System.out              com.example.flows_practice           I  Emission is Two
2024-10-22 17:41:01.132 24198-24313 System.out              com.example.flows_practice           I  Emission is Three
```

flatMapLatest
This will emit the latest one so if any other emission was before that will be cancelled and continue with the latests:
```kotlin
fun flatMapDemo() {  
    flow<Int> {  
        emit(1)  
        delay(1000L)  
        emit(2)  
        delay(1000L)  
        emit(3)  
    }.flatMapLatest {  
        flow {  
            emit("One")  
            delay(1000L)  
            emit("Two")  
            delay(1000L)  
            emit("Three")  
        }  
    }        .onEach {  
            println("Emission is $it")  
        }  
        .launchIn(GlobalScope)  
}
```
```
2024-10-22 17:43:05.293 24970-25002 System.out              com.example.flows_practice           I  Emission is One
2024-10-22 17:43:06.303 24970-25001 System.out              com.example.flows_practice           I  Emission is Two
2024-10-22 17:43:06.306 24970-25003 System.out              com.example.flows_practice           I  Emission is One
2024-10-22 17:43:07.313 24970-25002 System.out              com.example.flows_practice           I  Emission is Two
2024-10-22 17:43:07.318 24970-25002 System.out              com.example.flows_practice           I  Emission is One
2024-10-22 17:43:08.326 24970-25002 System.out              com.example.flows_practice           I  Emission is Two
2024-10-22 17:43:09.334 24970-25003 System.out              com.example.flows_practice           I  Emission is Three
```
flatMapMerge
this will work asynchrounous so every emit has its own flow and will be simultaniously.

```kotlin
fun flatMapDemo() {  
    flow<Int> {  
        emit(1)  
        delay(1000L)  
        emit(2)  
        delay(1000L)  
        emit(3)  
    }.flatMapMerge {  
        flow {  
            emit("One")  
            delay(1000L)  
            emit("Two")  
            delay(1000L)  
            emit("Three")  
        }  
    }        .onEach {  
            println("Emission is $it")  
        }  
        .launchIn(GlobalScope)  
}
```
```
2024-10-22 17:46:24.586 25649-25727 System.out              com.example.flows_practice           I  Emission is One
2024-10-22 17:46:25.590 25649-25727 System.out              com.example.flows_practice           I  Emission is Two
2024-10-22 17:46:25.590 25649-25727 System.out              com.example.flows_practice           I  Emission is One
2024-10-22 17:46:26.593 25649-25735 System.out              com.example.flows_practice           I  Emission is Three
2024-10-22 17:46:26.593 25649-25735 System.out              com.example.flows_practice           I  Emission is Two
2024-10-22 17:46:26.594 25649-25726 System.out              com.example.flows_practice           I  Emission is One
2024-10-22 17:46:27.596 25649-25726 System.out              com.example.flows_practice           I  Emission is Three
2024-10-22 17:46:27.597 25649-25726 System.out              com.example.flows_practice           I  Emission is Two
2024-10-22 17:46:28.602 25649-25726 System.out              com.example.flows_practice           I  Emission is Three
```


### Handling back pressure

```kotlin
fun backpressureDemo() {  
    flow<Int> {  
        repeat(10) {  
            emit(it)  
            delay(500L)  
        }  
    }.onEach {  
        delay(1000L)  
        println("Processed emission $it")  
    }.launchIn(GlobalScope)  
}
```
```
2024-10-22 18:04:56.746 26271-26304 System.out              com.example.flows_practice           I  Processed emission 0
2024-10-22 18:04:58.253 26271-26304 System.out              com.example.flows_practice           I  Processed emission 1
2024-10-22 18:04:59.761 26271-26304 System.out              com.example.flows_practice           I  Processed emission 2
2024-10-22 18:05:01.276 26271-26304 System.out              com.example.flows_practice           I  Processed emission 3
2024-10-22 18:05:02.791 26271-26304 System.out              com.example.flows_practice           I  Processed emission 4
2024-10-22 18:05:04.303 26271-26304 System.out              com.example.flows_practice           I  Processed emission 5
2024-10-22 18:05:05.816 26271-26304 System.out              com.example.flows_practice           I  Processed emission 6
2024-10-22 18:05:07.326 26271-26304 System.out              com.example.flows_practice           I  Processed emission 7
2024-10-22 18:05:08.838 26271-26304 System.out              com.example.flows_practice           I  Processed emission 8
2024-10-22 18:05:10.347 26271-26304 System.out              com.example.flows_practice           I  Processed emission 9
```
This has to wait and is not so good.

here is the use of buffer:
```kotlin
fun backpressureDemo() {  
    GlobalScope.launch {  
        flow<String> {  
            println("FOOD: Preparing appetizer")  
            delay(1000L)  
            emit("Appetizer")  
  
            println("FOOD: Preparing main dish")  
            delay(1000L)  
            emit("Main dish")  
  
            println("FOOD: Preparing dessert")  
            delay(500L)  
            emit("Dessert")  
        }.buffer().collect { dish ->  
            println("FOOD: Strat eating $dish")  
            delay(2500L)  
            println("FOOD: Finished eating $dish")  
        }  
    }}
    ```
```
2024-10-22 18:17:12.733 26639-26670 System.out              com.example.flows_practice           I  FOOD: Preparing appetizer
2024-10-22 18:17:13.738 26639-26670 System.out              com.example.flows_practice           I  FOOD: Preparing main dish
2024-10-22 18:17:13.739 26639-26670 System.out              com.example.flows_practice           I  FOOD: Strat eating Appetizer
2024-10-22 18:17:14.741 26639-26670 System.out              com.example.flows_practice           I  FOOD: Preparing dessert
2024-10-22 18:17:16.242 26639-26670 System.out              com.example.flows_practice           I  FOOD: Finished eating Appetizer
2024-10-22 18:17:16.243 26639-26670 System.out              com.example.flows_practice           I  FOOD: Strat eating Main dish
2024-10-22 18:17:18.748 26639-26670 System.out              com.example.flows_practice           I  FOOD: Finished eating Main dish
2024-10-22 18:17:18.749 26639-26670 System.out              com.example.flows_practice           I  FOOD: Strat eating Dessert
2024-10-22 18:17:21.254 26639-26670 System.out              com.example.flows_practice           I  FOOD: Finished eating Dessert
```

In the case the buffer has too much to buffer, so it is overpassed then we can do this:
```kotlin
.buffer(5).collect { dish ->  
    println("FOOD: Strat eating $dish")  
    delay(2500L)  
    println("FOOD: Finished eating $dish")  
}
```

we can use another strategy called conflate, so if something is happening it will jump to the next emission.
```kotlin
fun backpressureDemo() {  
    GlobalScope.launch {  
        flow<String> {  
            println("FOOD: Preparing appetizer")  
            delay(1000L)  
            emit("Appetizer")  
  
            println("FOOD: Preparing main dish")  
            delay(1000L)  
            emit("Main dish")  
  
            println("FOOD: Preparing dessert")  
            delay(500L)  
            emit("Dessert")  
        }.conflate().collect { dish ->  
            println("FOOD: Strat eating $dish")  
            delay(2500L)  
            println("FOOD: Finished eating $dish")  
        }  
    }}
    ```
```
2024-10-22 18:24:51.420 26853-26887 System.out              com.example.flows_practice           I  FOOD: Preparing appetizer
2024-10-22 18:24:52.425 26853-26887 System.out              com.example.flows_practice           I  FOOD: Preparing main dish
2024-10-22 18:24:52.425 26853-26888 System.out              com.example.flows_practice           I  FOOD: Strat eating Appetizer
2024-10-22 18:24:53.426 26853-26887 System.out              com.example.flows_practice           I  FOOD: Preparing dessert
2024-10-22 18:24:54.930 26853-26887 System.out              com.example.flows_practice           I  FOOD: Finished eating Appetizer
2024-10-22 18:24:54.932 26853-26887 System.out              com.example.flows_practice           I  FOOD: Strat eating Dessert
2024-10-22 18:24:57.437 26853-26887 System.out              com.example.flows_practice           I  FOOD: Finished eating Dessert
```

Other alternative is use collectLatest which will take the last one and cancell the previous one and so on.
```kotlin
fun backpressureDemo() {  
    GlobalScope.launch {  
        flow<String> {  
            println("FOOD: Preparing appetizer")  
            delay(1000L)  
            emit("Appetizer")  
  
            println("FOOD: Preparing main dish")  
            delay(1000L)  
            emit("Main dish")  
  
            println("FOOD: Preparing dessert")  
            delay(500L)  
            emit("Dessert")  
        }.collectLatest { dish ->  
            println("FOOD: Strat eating $dish")  
            delay(2500L)  
            println("FOOD: Finished eating $dish")  
        }  
    }}
```
```
2024-10-22 18:27:43.204 27151-27191 System.out              com.example.flows_practice           I  FOOD: Preparing appetizer
2024-10-22 18:27:44.210 27151-27191 System.out              com.example.flows_practice           I  FOOD: Strat eating Appetizer
2024-10-22 18:27:44.210 27151-27191 System.out              com.example.flows_practice           I  FOOD: Preparing main dish
2024-10-22 18:27:45.224 27151-27191 System.out              com.example.flows_practice           I  FOOD: Strat eating Main dish
2024-10-22 18:27:45.225 27151-27191 System.out              com.example.flows_practice           I  FOOD: Preparing dessert
2024-10-22 18:27:45.736 27151-27191 System.out              com.example.flows_practice           I  FOOD: Strat eating Dessert
2024-10-22 18:27:48.240 27151-27191 System.out              com.example.flows_practice           I  FOOD: Finished eating Dessert
```
