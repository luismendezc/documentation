```kotlin
lifecycleScope.launch {  
    val job = launch {  
        delay(2000L)  
        println("Coroutine finished!")  
    }  
    delay(1000L)  
    job.cancel()  
    println("Coroutine cancelled!")  
}
```

Cancellation is cooperative.

we can use isActive to determine if the coroutine has been cancelled. one way is also after and before a blocking call to use ensureActive() method.

```kotlin
isActive
ensureActive()
```

 
cancel a job:
```kotlin
lifecycleScope.launch {  
    val job = launch {  
        val innerJob1 = launch {  
            delay(2000L)  
            println("Inner job 1 finished")  
        }  
        val innerJob2 = launch {  
            delay(2000L)  
            println("Inner job 2 finished")  
        }  
        delay(1000L)  
        innerJob1.cancel()  
        println("Inner job 1 cancelled!")  
    }  
}
```
```
2024-10-10 09:32:26.063  6550-6550  System.out              com.example.coroutine_cancellation   I  Inner job 1 cancelled!
2024-10-10 09:32:27.095  6550-6550  System.out              com.example.coroutine_cancellation   I  Inner job 2 finished
```

If a job is cancelled all its children will be cancelled:
```kotlin
lifecycleScope.launch {  
    val job = launch {  
        val innerJob1 = launch {  
            delay(2000L)  
            println("Inner job 1 finished")  
        }  
        val innerJob2 = launch {  
            delay(2000L)  
            println("Inner job 2 finished")  
        }  
    }    delay(1000L)  
    job.cancel()  
    println("Job cancelled!")  
}
```
```
2024-10-10 09:34:36.199  9744-9744  System.out              com.example.coroutine_cancellation   I  Job cancelled!
```

In the moment we cancelled a coroutine scope the hole scope will be cancelled, all the coroutines and jobs will be cancelled but that also means that the scope is utilised and that means that we can't use it anymore to launch any more coroutines with it.


```kotlin
package com.example.coroutine_cancellation  
  
import android.os.Bundle  
import androidx.activity.ComponentActivity  
import androidx.activity.compose.setContent  
import androidx.activity.enableEdgeToEdge  
import androidx.compose.foundation.layout.Column  
import androidx.compose.foundation.layout.fillMaxSize  
import androidx.compose.foundation.layout.padding  
import androidx.compose.material3.Button  
import androidx.compose.material3.Scaffold  
import androidx.compose.material3.Text  
import androidx.compose.runtime.Composable  
import androidx.compose.runtime.getValue  
import androidx.compose.runtime.mutableStateOf  
import androidx.compose.runtime.remember  
import androidx.compose.runtime.setValue  
import androidx.compose.ui.Modifier  
import androidx.compose.ui.tooling.preview.Preview  
import androidx.compose.ui.unit.dp  
import androidx.lifecycle.lifecycleScope  
import com.example.coroutine_cancellation.ui.theme.Coroutine_CancellationTheme  
import kotlinx.coroutines.CoroutineScope  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.cancel  
import kotlinx.coroutines.delay  
import kotlinx.coroutines.launch  
  
class MainActivity : ComponentActivity() {  
  
    private val customScope = CoroutineScope(Dispatchers.Main)  
  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
        customScope.launch {  
            delay(2000L)  
            println("Job finished!")  
        }  
        lifecycleScope.launch {  
            delay(1000L)  
            customScope.cancel()  
            customScope.launch {  
                println("Hello World!")  
            }  
        }  
        setContent {  
            Coroutine_CancellationTheme {  
  
            }        
        }
    }  
}  
  
@Preview(showBackground = true)  
@Composable  
fun GreetingPreview() {  
    Coroutine_CancellationTheme {  
  
    }
}
    ```

if we dont want that to happend what we can do is to use .couroutineContext.cancelChildren()
```kotlin
package com.example.coroutine_cancellation  
  
import android.os.Bundle  
import androidx.activity.ComponentActivity  
import androidx.activity.compose.setContent  
import androidx.activity.enableEdgeToEdge  
import androidx.compose.foundation.layout.Column  
import androidx.compose.foundation.layout.fillMaxSize  
import androidx.compose.foundation.layout.padding  
import androidx.compose.material3.Button  
import androidx.compose.material3.Scaffold  
import androidx.compose.material3.Text  
import androidx.compose.runtime.Composable  
import androidx.compose.runtime.getValue  
import androidx.compose.runtime.mutableStateOf  
import androidx.compose.runtime.remember  
import androidx.compose.runtime.setValue  
import androidx.compose.ui.Modifier  
import androidx.compose.ui.tooling.preview.Preview  
import androidx.compose.ui.unit.dp  
import androidx.lifecycle.lifecycleScope  
import com.example.coroutine_cancellation.ui.theme.Coroutine_CancellationTheme  
import kotlinx.coroutines.CoroutineScope  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.cancel  
import kotlinx.coroutines.cancelChildren  
import kotlinx.coroutines.delay  
import kotlinx.coroutines.launch  
  
class MainActivity : ComponentActivity() {  
  
    private val customScope = CoroutineScope(Dispatchers.Main)  
  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
        customScope.launch {  
            delay(2000L)  
            println("Job finished!")  
        }  
        lifecycleScope.launch {  
            delay(1000L)  
            customScope.coroutineContext.cancelChildren()  
            customScope.launch {  
                println("Hello World!")  
            }  
        }  
        setContent {  
            Coroutine_CancellationTheme {  
  
            }        }    }  
}  
  
@Preview(showBackground = true)  
@Composable  
fun GreetingPreview() {  
    Coroutine_CancellationTheme {  
  
    }}
```
```
2024-10-10 11:18:37.870 18070-18070 System.out              com.example.coroutine_cancellation   I  Hello World!
```

How to handle exceptions and don't have problems also by cancelling coroutines?:

1st solution
```kotlin
suspend fun pollingTask(client: HttpClient) {
	while (true) {
		try {
			println("Polling network resource...")
			val posts = client.get(
				urlString = "https://jsonplaceholder.org/posts"
			)
			println("Received posts!")
			delay(30000L)
		} catch (e: UnresolvedAdressException) {
			if(e is CancellationException) throw e
			println("Oops, something went wrong, make sure you're connected" +
			"to the internet.")
		}
	}
}
```

2nd solution:
```kotlin
suspend fun pollingTask(client: HttpClient) {
	while (true) {
		try {
			println("Polling network resource...")
			val posts = client.get(
				urlString = "https://jsonplaceholder.org/posts"
			)
			println("Received posts!")
			delay(30000L)
		} catch (e: UnresolvedAdressException) {
			coroutineContext.ensureActive()
			println("Oops, something went wrong, make sure you're connected" +
			"to the internet.")
		}
	}
}
```

Cancellation trap when having Transaction like behaviours.
A transaction like behaviour is for example when we have multiple related database called at once but only really apply the changes when all of these calls succeeded, if one of this calls fails then it wont apply any of these calls in the database to avoid inconsistent states.

Solution is to use a global scope that will outlive and with that even if is cancelled in between we can finish the exectuion until the app is destroyed.
```kotlin
package com.example.coroutine_cancellation  
  
import kotlinx.coroutines.CoroutineScope  
import kotlinx.coroutines.GlobalScope  
import kotlinx.coroutines.delay  
import kotlinx.coroutines.ensureActive  
import kotlinx.coroutines.launch  
import kotlin.coroutines.coroutineContext  
  
class OrdersApi {  
    suspend fun postOrder(): String? {  
        return try {  
            delay(2000L)  
            val orderNumber = "123456"  
            println("Order placed: $orderNumber")  
            orderNumber  
        }catch (e: Exception) {  
            coroutineContext.ensureActive()  
            null  
        }  
    }  
}  
  
class OrdersDao {  
    suspend fun saveTrackingNumber(trackingNumber: String) {  
        delay(2000L)  
        println("Tracking number saved: $trackingNumber")  
    }  
}  
  
class OrdersRepository(  
    private val api: OrdersApi,  
    private val dao: OrdersDao,  
    private val applicationScope: CoroutineScope  
) {  
    suspend fun placeOrder() {  
        println("Placing order...")  
        val trackingNumber = api.postOrder()  
        if (trackingNumber != null) {  
            println("Order placed successfully, saving tracking number: $trackingNumber")  
            applicationScope.launch {  
                dao.saveTrackingNumber((trackingNumber))  
            }.join()  
        }  
    }  
}
```

```kotlin
package com.example.coroutine_cancellation  
  
import android.app.Application  
import kotlinx.coroutines.CoroutineScope  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.SupervisorJob  
  
class MyApplication: Application() {  
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())  
}
```

Cancell trap #3, try/finally
usage of withContext(NonCancellable) Google means to use this when using it for cleaning up resources.

```kotlin
package com.example.coroutine_cancellation  
  
import android.content.Context  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.NonCancellable  
import kotlinx.coroutines.delay  
import kotlinx.coroutines.ensureActive  
import kotlinx.coroutines.isActive  
import kotlinx.coroutines.withContext  
import java.io.File  
import kotlin.coroutines.coroutineContext  
  
class FileManager(private val context: Context) {  
    private var tempFile: File? = null  
    suspend fun writeRecordsToFile() {  
        println("Writing records to file...")  
        withContext(Dispatchers.IO){  
            tempFile = File.createTempFile("db_records_", ".txt", context.cacheDir)  
            try {  
                repeat(5) {  
                    println("Writing line $it & isActive: ${coroutineContext.isActive}")  
                    tempFile?.appendText("Database record $it\n")  
                    delay(1000)  
                }  
            }catch (e: Exception) {  
                ensureActive()  
                println("Error writing records to file: $e")  
            } finally {  
                println("Cleaning up temp file...")  
                withContext(NonCancellable){  
                    cleanupTempFile()  
                }  
                println("Cleaned up temp file!")  
            }  
        }  
    }  
  
    suspend fun cleanupTempFile() {  
        println("Cleanup function, is active: ${coroutineContext.isActive}")  
        withContext(Dispatchers.IO) {  
            delay(2000)  
            tempFile?.let { file ->  
                if (file.exists()) {  
                    file.delete()  
                    println("Deleted file")  
                }  
            }  
        }    }  
}
```

```kotlin
package com.example.coroutine_cancellation  
  
import android.os.Bundle  
import androidx.activity.ComponentActivity  
import androidx.activity.compose.setContent  
import androidx.activity.enableEdgeToEdge  
import androidx.compose.foundation.layout.Column  
import androidx.compose.foundation.layout.fillMaxSize  
import androidx.compose.foundation.layout.padding  
import androidx.compose.material3.Button  
import androidx.compose.material3.Scaffold  
import androidx.compose.material3.Text  
import androidx.compose.runtime.Composable  
import androidx.compose.runtime.getValue  
import androidx.compose.runtime.mutableStateOf  
import androidx.compose.runtime.remember  
import androidx.compose.runtime.setValue  
import androidx.compose.ui.Modifier  
import androidx.compose.ui.tooling.preview.Preview  
import androidx.compose.ui.unit.dp  
import androidx.lifecycle.lifecycleScope  
import com.example.coroutine_cancellation.ui.theme.Coroutine_CancellationTheme  
import kotlinx.coroutines.CoroutineScope  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.cancel  
import kotlinx.coroutines.cancelChildren  
import kotlinx.coroutines.delay  
import kotlinx.coroutines.launch  
  
class MainActivity : ComponentActivity() {  
  
    private val customScope = CoroutineScope(Dispatchers.Main)  
  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
        val fileManager = FileManager(applicationContext)  
        lifecycleScope.launch {  
            val job = launch {  
                fileManager.writeRecordsToFile()  
            }  
            delay(3000L)  
            job.cancel()  
        }  
  
        setContent {  
            Coroutine_CancellationTheme {  
  
            }        }    }  
}  
  
@Preview(showBackground = true)  
@Composable  
fun GreetingPreview() {  
    Coroutine_CancellationTheme {  
  
    }}
    ```

ensureActive() vs yield()

yield is suspending so use it in between blocking functions.


Homework:
## Easy Assignment #1

_You’ve created a file documenting your thoughts on your bird friends and want to write a short application to upload it to a server for safekeeping. You write up a small demo project before integrating the API and notice a bug…_

**Instructions**

Find and fix the bug in the code below to terminate the program when it has been canceled.
https://github.com/philipplackner/CoroutinesMasterclass/blob/coroutine_cancellation/homework/assignement_1_v2/app/src/main/java/com/plcoding/coroutinesmasterclass/sections/coroutine_cancellation/homework/AssignmentOneScreen.kt

```kotlin
package com.example.coroutine_cancellation  
  
import androidx.compose.foundation.layout.Arrangement  
import androidx.compose.foundation.layout.Column  
import androidx.compose.foundation.layout.fillMaxSize  
import androidx.compose.material3.Button  
import androidx.compose.material3.CircularProgressIndicator  
import androidx.compose.material3.Text  
import androidx.compose.runtime.Composable  
import androidx.compose.runtime.getValue  
import androidx.compose.runtime.mutableStateOf  
import androidx.compose.runtime.remember  
import androidx.compose.runtime.rememberCoroutineScope  
import androidx.compose.runtime.setValue  
import androidx.compose.ui.Alignment  
import androidx.compose.ui.Modifier  
import kotlinx.coroutines.cancelChildren  
import kotlinx.coroutines.launch  
  
@Composable  
fun AssignmentOneScreen() {  
    var isUploading by remember {  
        mutableStateOf(false)  
    }  
    val scope = rememberCoroutineScope()  
  
    Column(  
        modifier = Modifier.fillMaxSize(),  
        horizontalAlignment = Alignment.CenterHorizontally,  
        verticalArrangement = Arrangement.Center  
    ) {  
        if (isUploading) {  
            CircularProgressIndicator()  
            Button(onClick = {  
                scope.coroutineContext.cancelChildren()  
                isUploading = false  
            }) {  
                Text(text = "Cancel Upload")  
            }  
        } else {  
            Button(onClick = {  
                scope.launch {  
                    isUploading = true  
                    RemoteService.uploadFile()  
                    isUploading = false  
                }  
            }) {  
                Text(text = "Upload File")  
            }  
        }  
    }  
}
```

## Medium Assignment #2

_You’ve been feeling charitable recently and decided to create a small application to simulate transferring a donation to a local animal charity. You also ensure to include a cleanup function to run after each transfer request, regardless of whether it was successful, failed, or canceled. Upon completion, you encounter some unexpected behaviors._

1. _When you cancel the transfer after money is taken from your account, the money is lost, and the charity never receives it._
    
2. _Resources aren’t properly cleaned up when cancelling a transfer_
    

### **Instructions**

Find the bugs and fix them.

1. Once money has been taken from your account, it has to be added to the charity’s account.
    
2. The application should always clean up the resources, whether the transfer succeeded, failed or was cancelled.
    
3. Properly handle any CancellationExceptions
https://github.com/philipplackner/CoroutinesMasterclass/tree/coroutine_cancellation/homework/assignment_2

```kotlin
package com.example.coroutine_cancellation  
  
import android.os.Bundle  
import androidx.activity.ComponentActivity  
import androidx.activity.compose.setContent  
import androidx.activity.enableEdgeToEdge  
import androidx.compose.runtime.Composable  
import androidx.lifecycle.viewmodel.compose.viewModel  
import androidx.compose.ui.tooling.preview.Preview  
import com.example.coroutine_cancellation.assignmenttwo.MoneyTransferScreen  
import com.example.coroutine_cancellation.assignmenttwo.MoneyTransferViewModel  
import com.example.coroutine_cancellation.assignmenttwo.MoneyTransferViewModelFactory  
import com.example.coroutine_cancellation.ui.theme.Coroutine_CancellationTheme  
  
/*  
[Easy] Assignment #1  
  
You’ve created a file documenting your thoughts on your bird friends and want to write a short  
application to upload it to a server for safekeeping. You write up a small demo project before  
integrating the API and notice a bug…  
  
Instructions  
Find and fix the bug in the code below to terminate the program when it has been canceled.  
----------------------------------------------------------------------------------------------------  
[Medium] Assignment #2  
  
You’ve been feeling charitable recently and decided to create a small application to simulate  
transferring a donation to a local animal charity. You also ensure to include a cleanup function to  
run after each transfer request, regardless of whether it was successful, failed, or canceled.  
Upon completion, you encounter some unexpected behaviors.  
When you cancel the transfer after money is taken from your account, the money is lost, and the  
charity never receives it.  
Resources aren’t properly cleaned up when cancelling a transfer  
  
Instructions  
Find the bugs and fix them.  
Once money has been taken from your account, it has to be added to the charity’s account.  
The application should always clean up the resources, whether the transfer succeeded, failed or was  
cancelled.  
Properly handle any CancellationExceptions  
  
 */class MainActivity : ComponentActivity() {  
  
    //private val customScope = CoroutineScope(Dispatchers.Main)  
  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
  
        val applicationScope = (applicationContext as MyApplication).applicationScope  
        /*  
        customScope.launch {            delay(2000L)            println("Job finished!")        }        lifecycleScope.launch {            delay(1000L)            customScope.coroutineContext.cancelChildren()            customScope.launch {                println("Hello World!")            }        }         */  
        /*        val fileManager = FileManager(applicationContext)        lifecycleScope.launch {            val job = launch {                fileManager.writeRecordsToFile()            }            delay(3000L)            job.cancel()        }*/  
        setContent {  
            Coroutine_CancellationTheme {  
                //AssignmentOneScreen()  
                val viewModelFactory = MoneyTransferViewModelFactory(applicationScope)  
                val viewModel: MoneyTransferViewModel = viewModel(factory = viewModelFactory)  
  
                MoneyTransferScreen(  
                    state = viewModel.state,  
                    onAction = viewModel::onAction  
                )  
            }  
        }    }  
}  
  
@Preview(showBackground = true)  
@Composable  
fun GreetingPreview() {  
    Coroutine_CancellationTheme {  
  
    }}
    ```

```kotlin
package com.example.coroutine_cancellation  
  
import android.app.Application  
import kotlinx.coroutines.CoroutineScope  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.SupervisorJob  
  
class MyApplication: Application() {  
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())  
}
```

```kotlin
package com.example.coroutine_cancellation.assignmenttwo  
  
sealed interface ProcessingState {  
    data object CheckingFunds : ProcessingState  
    data object DebitingAccount : ProcessingState  
    data object CreditingAccount : ProcessingState  
    data object CleanupResources : ProcessingState  
}
```

```kotlin
package com.example.coroutine_cancellation.assignmenttwo  
import androidx.lifecycle.ViewModel  
import androidx.lifecycle.ViewModelProvider  
import kotlinx.coroutines.CoroutineScope  
  
class MoneyTransferViewModelFactory(  
    private val scope: CoroutineScope  
) : ViewModelProvider.Factory {  
    override fun <T : ViewModel> create(modelClass: Class<T>): T {  
        if(modelClass.isAssignableFrom(MoneyTransferViewModel::class.java)){  
            return MoneyTransferViewModel(scope) as T  
        }  
        throw IllegalArgumentException("Uknown ViewModel class")  
    }  
}
```

```kotlin
package com.example.coroutine_cancellation.assignmenttwo  
  
import androidx.compose.foundation.ExperimentalFoundationApi  
import androidx.compose.runtime.getValue  
import androidx.compose.runtime.mutableStateOf  
import androidx.compose.runtime.setValue  
import androidx.lifecycle.ViewModel  
import androidx.lifecycle.viewModelScope  
import kotlinx.coroutines.CoroutineScope  
import kotlinx.coroutines.Dispatchers  
import kotlinx.coroutines.Job  
import kotlinx.coroutines.NonCancellable  
import kotlinx.coroutines.delay  
import kotlinx.coroutines.ensureActive  
import kotlinx.coroutines.launch  
import kotlinx.coroutines.withContext  
  
class MoneyTransferViewModel(private val applicationScope: CoroutineScope) : ViewModel() {  
  
    var state by mutableStateOf(MoneyTransferState())  
        private set  
  
    private var job: Job? = null  
  
  
  
    @OptIn(ExperimentalFoundationApi::class)  
    fun onAction(action: MoneyTransferAction) {  
        when (action) {  
            MoneyTransferAction.TransferFunds -> transferFunds()  
            MoneyTransferAction.CancelTransfer -> {  
                job?.cancel()  
                state = state.copy(  
                    isTransferring = false,  
                    processingState = null,  
                    resultMessage = null,  
                )  
            }  
        }  
    }  
  
    @OptIn(ExperimentalFoundationApi::class)  
    private fun transferFunds() {  
        job = viewModelScope.launch {  
            withContext(Dispatchers.Default) {  
                try {  
                    state = state.copy(  
                        isTransferring = true,  
                        resultMessage = null,  
                    )  
                    val amountToTransfer = state.transferAmount.text.toString().toDoubleOrNull()  
  
                    if (amountToTransfer == null) {  
                        state = state.copy(  
                            resultMessage = "Invalid Amount"  
                        )  
                        return@withContext  
                    }  
  
                    if (amountToTransfer == 0.0) {  
                        state = state.copy(  
                            resultMessage = "Enter amount greater than 0"  
                        )  
                        return@withContext  
                    }  
  
                    state = state.copy(  
                        processingState = ProcessingState.CheckingFunds,  
                    )  
                    val hasEnoughFunds = checkHasEnoughFunds(state.savingsBalance, amountToTransfer)  
  
                    if (!hasEnoughFunds) {  
                        state = state.copy(  
                            resultMessage = "Insufficient funds",  
                        )  
                        return@withContext  
                    }  
  
                    debitAccount(state.savingsBalance, amountToTransfer)  
                    applicationScope.launch {  
                        creditAccount(state.checkingBalance, amountToTransfer)  
                    }.join()  
  
                    state = state.copy(  
                        resultMessage = "Transfer complete!",  
                    )  
                    println("--------------")  
                    println(state.checkingBalance)  
                    println(state.savingsBalance)  
                    println("--------------")  
  
                } catch (e: Exception) {  
                    coroutineContext.ensureActive()  
                    println("Error processing transfer: ${e.message}")  
                } finally {  
                    withContext(NonCancellable){  
                        cleanupResources()  
                    }  
                    state = state.copy(  
                        processingState = null,  
                        isTransferring = false,  
                    )  
                }  
            }  
        }    }  
  
    @OptIn(ExperimentalFoundationApi::class)  
    private suspend fun creditAccount(toAccountBalance: Double, amount: Double) {  
        state = state.copy(  
            processingState = ProcessingState.CreditingAccount,  
        )  
        println("Crediting account with balance $toAccountBalance and amount $amount")  
        delay(3000)  
        state = state.copy(  
            checkingBalance = toAccountBalance + amount,  
        )  
    }  
  
    @OptIn(ExperimentalFoundationApi::class)  
    private suspend fun debitAccount(fromAccountBalance: Double, amount: Double) {  
        state = state.copy(processingState = ProcessingState.DebitingAccount)  
        delay(3000)  
        state = state.copy(  
            savingsBalance = fromAccountBalance - amount,  
        )  
    }  
  
    private suspend fun checkHasEnoughFunds(fromAccountBalance: Double, amount: Double): Boolean {  
        println("Checking balance with balance $fromAccountBalance and amount $amount")  
        delay(2000)  
        return amount <= fromAccountBalance  
    }  
  
    @OptIn(ExperimentalFoundationApi::class)  
    private suspend fun cleanupResources() {  
        state = state.copy(  
            processingState = ProcessingState.CleanupResources,  
        )  
        delay(2000)  
    }  
}
```

```kotlin
package com.example.coroutine_cancellation.assignmenttwo  
  
import androidx.compose.foundation.ExperimentalFoundationApi  
import androidx.compose.foundation.text2.input.TextFieldState  
  
data class MoneyTransferState @OptIn(ExperimentalFoundationApi::class) constructor(  
    val savingsBalance: Double = 1000.0,  
    val checkingBalance: Double = 500.0,  
    val transferAmount: TextFieldState = TextFieldState(),  
    val isTransferring: Boolean = false,  
    val resultMessage: String? = null,  
    val processingState: ProcessingState? = null,  
)
```

```kotlin
package com.example.coroutine_cancellation.assignmenttwo  
  
import androidx.compose.foundation.ExperimentalFoundationApi  
import androidx.compose.foundation.background  
import androidx.compose.foundation.layout.Arrangement  
import androidx.compose.foundation.layout.Box  
import androidx.compose.foundation.layout.Column  
import androidx.compose.foundation.layout.IntrinsicSize  
import androidx.compose.foundation.layout.Row  
import androidx.compose.foundation.layout.Spacer  
import androidx.compose.foundation.layout.fillMaxSize  
import androidx.compose.foundation.layout.height  
import androidx.compose.foundation.layout.padding  
import androidx.compose.foundation.layout.size  
import androidx.compose.foundation.shape.RoundedCornerShape  
import androidx.compose.foundation.text.KeyboardOptions  
import androidx.compose.foundation.text2.BasicTextField2  
import androidx.compose.material3.Button  
import androidx.compose.material3.ButtonDefaults  
import androidx.compose.material3.CircularProgressIndicator  
import androidx.compose.material3.MaterialTheme  
import androidx.compose.material3.Text  
import androidx.compose.runtime.Composable  
import androidx.compose.ui.Alignment  
import androidx.compose.ui.Modifier  
import androidx.compose.ui.draw.alpha  
import androidx.compose.ui.draw.clip  
import androidx.compose.ui.graphics.Color  
import androidx.compose.ui.platform.LocalFocusManager  
import androidx.compose.ui.text.input.ImeAction  
import androidx.compose.ui.text.input.KeyboardType  
import androidx.compose.ui.unit.dp  
  
@OptIn(ExperimentalFoundationApi::class)  
@Composable  
fun MoneyTransferScreen(  
    state: MoneyTransferState,  
    onAction: (MoneyTransferAction) -> Unit,  
) {  
    val focusManager = LocalFocusManager.current  
  
    val primaryGreen = Color(0xFF4CAF50)  
    val secondaryGreen = Color(0xFF81C784)  
    val background = Color(0xFFE8F5E9)  
  
    Column(  
        modifier = Modifier  
            .fillMaxSize()  
            .background(background)  
            .padding(16.dp),  
        horizontalAlignment = Alignment.CenterHorizontally,  
        verticalArrangement = Arrangement.Center  
    ) {  
  
        Text(  
            text = "Savings: $${String.format("%.2f", state.savingsBalance)}",  
            color = primaryGreen,  
            style = MaterialTheme.typography.titleMedium  
        )  
        Spacer(modifier = Modifier.height(8.dp))  
        Text(  
            text = "Checking: $${String.format("%.2f", state.checkingBalance)}",  
            color = primaryGreen,  
            style = MaterialTheme.typography.titleMedium  
        )  
        Spacer(modifier = Modifier.height(16.dp))  
  
        BasicTextField2(  
            state = state.transferAmount,  
            keyboardOptions = KeyboardOptions(  
                keyboardType = KeyboardType.Number,  
                imeAction = ImeAction.Done  
            ),  
            enabled = !state.isTransferring,  
            modifier = Modifier  
                .clip(RoundedCornerShape(8.dp))  
                .background(Color.White)  
                .padding(16.dp),  
            decorator = { innerBox ->  
                Row(  
                    verticalAlignment = Alignment.CenterVertically,  
                ) {  
                    Box(modifier = Modifier.weight(1f)) {  
                        if (state.transferAmount.text.isEmpty()) {  
                            Text(  
                                text = "Enter amount",  
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(  
                                    alpha = 0.4f  
                                )  
                            )  
                        }  
                        innerBox()  
                    }  
                }            }        )  
        Spacer(modifier = Modifier.height(16.dp))  
  
        Button(  
            onClick = {  
                focusManager.clearFocus()  
                onAction(MoneyTransferAction.TransferFunds)  
            },  
            colors = ButtonDefaults.buttonColors(  
                containerColor = secondaryGreen  
            ),  
            modifier = Modifier  
                .height(IntrinsicSize.Min)  
        ) {  
            Box(  
                contentAlignment = Alignment.Center  
            ) {  
                CircularProgressIndicator(  
                    modifier = Modifier  
                        .size(15.dp)  
                        .alpha(if (state.isTransferring) 1f else 0f),  
                    strokeWidth = 1.5.dp,  
                    color = MaterialTheme.colorScheme.onPrimary  
                )  
                Text(  
                    text = "Transfer Funds",  
                    color = Color.White,  
                    modifier = Modifier  
                        .alpha(if (state.isTransferring) 0f else 1f)  
                )  
            }  
        }        if (state.isTransferring) {  
            Button(  
                onClick = {  
                    onAction(MoneyTransferAction.CancelTransfer)  
                },  
                colors = ButtonDefaults.buttonColors(  
                    containerColor = secondaryGreen  
                ),  
            ) {  
                Text(text = "Cancel Transfer")  
            }  
        }  
        val processingStateText = when (state.processingState) {  
            ProcessingState.CheckingFunds -> {  
                "Checking if there is enough funds..."  
            }  
            ProcessingState.CreditingAccount -> {  
                "Depositing money to checking account..."  
            }  
            ProcessingState.DebitingAccount -> {  
                "Withdrawing money from savings account..."  
            }  
            ProcessingState.CleanupResources -> {  
                "Cleaning up resources used..."  
            }  
            else -> null  
        }  
  
        if (processingStateText != null) {  
            Text(text = processingStateText)  
        }  
        if (state.resultMessage != null) {  
            Text(text = state.resultMessage)  
        }  
    }  
}
```

```kotlin
package com.example.coroutine_cancellation.assignmenttwo  
  
sealed interface MoneyTransferAction {  
    data object TransferFunds : MoneyTransferAction  
    data object CancelTransfer : MoneyTransferAction  
}
```


I had to add also this line in the build.gradle.kts (Module :app)
```kotlin
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
```


