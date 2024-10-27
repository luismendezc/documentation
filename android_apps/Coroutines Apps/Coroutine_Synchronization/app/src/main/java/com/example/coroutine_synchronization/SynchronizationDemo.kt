package com.example.coroutine_synchronization

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

fun synchronizationDemo() {
    var count = 0
    //val lock = Any()
    val mutex = Mutex()
    GlobalScope.launch(Dispatchers.Main) {
        (1..100_000).map {
            launch {
                /*synchronized(lock) {
                    count ++
                }*/
                mutex.withLock {
                    mutex.withLock {
                        count++
                    }
                }
            }
        }.joinAll()
        println("The count is $count")
    }
}

fun synchronizationDemo2() {
    val normalHashMap = hashMapOf<Int, Int>()
    val concurrentHashMap = ConcurrentHashMap<Int, Int>()
    val concurrentHashMap2 = ConcurrentHashMap<Int, Int>()

    val mutex1 = Mutex()
    val mutex2 = Mutex()
    val mutex3 = Mutex()

    GlobalScope.launch(Dispatchers.IO.limitedParallelism(1)) {
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
            }
        }.joinAll()

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

    }
}