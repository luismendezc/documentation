Video to clarify why use Junit 5 instead of 4 just for reference.
https://www.youtube.com/watch?v=RX_g65J14H0

For suspending functions for example:
```kotlin
suspend fun fib(n: Int): Int {  
    return withContext(Dispatchers.Default) {  
        if(n<=1) {  
            n  
        } else {  
            fib(n-1) + fib(n-2)  
        }  
    }  
}
```

Test:
```kotlin
class FibonacciKtTest {  
    @Test  
    fun testFibonacci() = runBlocking {  
        val result = fib(30)  
        assertk.assertThat(result).isEqualTo(832_040)  
    }  
}
```

So by using runBlocking we got every line run synchronously so it blocks but it will run one by one assuring everything is happening (only use this for tests). But this could cause some problems because if we add a delay then it will wait for that time and then keep going so it could make our tests slow:
```kotlin
suspend fun fib(n: Int): Int {  
    delay(5000L)  
    return withContext(Dispatchers.Default) {  
        if(n<=1) {  
            n  
        } else {  
            fib(n-1) + fib(n-2)  
        }  
    }  
}
```
```kotlin
class FibonacciKtTest {  
    @Test  
    fun testFibonacci() = runBlocking {  
        val result = fib(30)  //will wait 5 seconds everytime
        assertk.assertThat(result).isEqualTo(832_040)  
    }  
}
```

To avoid those delays then we can use runTest like (also blocks the thread and goes one line by one but skips delays):
```kotlin
class FibonacciKtTest {  
    @Test  
    fun testFibonacci() = runTest {  
        val result = fib(30)  
        assertk.assertThat(result).isEqualTo(832_040)  
    }  
}
```

**But that only works if the dispatcher is or unconfined or standard test dispatcher**:
```kotlin
suspend fun fib(n: Int, dispatcher: CoroutineDispatcher): Int {  
    delay(5000L)  
    return withContext(dispatcher) {  
        if(n<=1) {  
            n  
        } else {  
            fib(n-1, dispatcher) + fib(n-2, dispatcher)  
        }  
    }  
}
```
```kotlin
class FibonacciKtTest {  
    @OptIn(ExperimentalStdlibApi::class)  
    @Test  
    fun testFibonacci() = runTest {  
        val dispatcher = coroutineContext[CoroutineDispatcher]  
        val result = fib(30, dispatcher!!)  
        assertk.assertThat(result).isEqualTo(832_040)  
    }  
}
```


