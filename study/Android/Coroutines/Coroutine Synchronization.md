```kotlin
package com.example.coroutine_synchronization  
  
import kotlinx.coroutines.GlobalScope  
import kotlinx.coroutines.joinAll  
import kotlinx.coroutines.launch  
  
fun synchronizationDemo() {  
    var count = 0  
    GlobalScope.launch {  
        (1..100_000).map {  
            launch {  
                count ++  
            }  
        }.joinAll()  
        println("The count is $count")  
    }  
}
```
```
2024-10-19 09:33:26.636 15423-15463 System.out              com...ple.coroutine_synchronization  I  The count is 99907
```

So the result us not 100,000 because the synchronization is not happening because there are multiple calls to the count so the count++ is actually count = count + 1 so they need to access it and then add 1, so that means that the value could be false because there are multple coroutines accessing at the same time.


## Synchronized & Mutex

the use of synchronized we can pass an instance of any object into the synchronized block and that will make that all the coroutines that share this instance then to run the block synchronously like: 
```kotlin
fun synchronizationDemo() {  
    var count = 0  
    val lock = Any()  
    GlobalScope.launch(Dispatchers.Main) {  
        (1..100_000).map {  
            launch {  
                synchronized(lock) {  
                    count ++  
                }  
            }        }.joinAll()  
        println("The count is $count")  
    }  
}
```

We can use Mutex which will be like a door for entering certain code also like synchornized (also wee need only one Mutex instance), but the usage is like:
```kotlin
fun synchronizationDemo() {  
    var count = 0  
    val mutex = Mutex()  
    GlobalScope.launch(Dispatchers.Main) {  
        (1..100_000).map {  
            launch {  
                mutex.withLock {  
                    count++  
                }  
            }        }.joinAll()  
        println("The count is $count")  
    }  
}
```
Other alternative using mutex is:
```kotlin
fun synchronizationDemo() {  
    var count = 0  
    val mutex = Mutex()  
    GlobalScope.launch(Dispatchers.Main) {  
        (1..100_000).map {  
            launch {  
                mutex.lock()  
                count++  
                mutex.unlock()  
            }  
        }.joinAll()  
        println("The count is $count")  
    }  
}
```

Take care because we can have death locks like this:
```kotlin
mutex.withLock {   
	mutex.withLock {   
			count++  
	    }  
}
```

Resume:
For normal threads (no coroutines) use synchronized
For corotines use mutex


## Concurrent lists and hashmaps
```kotlin
fun synchronizationDemo2() {  
    val normalHashMap = hashMapOf<Int, Int>()  
    val concurrentHashMap = ConcurrentHashMap<Int, Int>()  
    val concurrentHashMap2 = ConcurrentHashMap<Int, Int>()  
  
    val mutex1 = Mutex()  
    val mutex2 = Mutex()  
    val mutex3 = Mutex()  
  
    GlobalScope.launch(Dispatchers.IO) {  
        (1..100000).map {  
            launch {  
                val random = Random.nextInt(1, 9)  
  
                mutex1.withLock {  
                    val concurrentCount = concurrentHashMap[random] ?: 0  
                    concurrentHashMap[random] = concurrentCount + 1  
                }  
  
                mutex2.withLock {  
                    val concurrentCount2 = concurrentHashMap2[random] ?: 0  
                    concurrentHashMap2[random] = concurrentCount2 + 1  
                }  
  
                mutex3.withLock {  
                    val normalCount = normalHashMap[random] ?: 0  
                    normalHashMap[random] = normalCount + 1  
                }  
            }        }.joinAll()  
  
        println("Normal hashmap")  
        normalHashMap.toSortedMap().forEach { (key, count) ->  
            println("$key: $count")  
        }  
  
        println("Concurrent hashmap1")  
        concurrentHashMap.toSortedMap().forEach { (key, count) ->  
            println("$key: $count")  
        }  
  
        println("Concurrent hashmap2")  
        concurrentHashMap2.toSortedMap().forEach { (key, count) ->  
            println("$key: $count")  
        }  
  
    }}
    ```

## Single Thread Dispatcher

```kotlin
GlobalScope.launch(Dispatchers.IO.limitedParallelism(1))
```


## Medium Assignment #1

_In a multiplayer game, the leaderboard needs to be synchronized. Multiple players might score points concurrently, and you need to ensure the board reflects the correct ranking._

### **Instructions**

Design a class to represent a leaderboard, storing player names and their corresponding scores in a `Map<String, Int>`.

Implement a function to add new scores, associating player names with their high scores.

Add a mechanism to register and unregister listeners that will be notified whenever the leaderboard is updated.

Upon adding a new score, the class should update its internal map, calculate the top 3 scores, and notify all registered listeners with this data.

In a dynamic game environment, scores might be updated from various parts of your application, potentially running on different threads, so consider how to handle these updates safely.
https://github.com/philipplackner/CoroutinesMasterclass/tree/coroutine_synchronization/homework/assignment_1

