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
