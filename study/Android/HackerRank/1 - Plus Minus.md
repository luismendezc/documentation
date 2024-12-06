![[1 - Plus Minus - 1image.png]]
![[1 - Plus Minus - 2image.png]]


```kotlin
import java.io.*

import java.math.*

import java.security.*

import java.text.*

import java.util.*

import java.util.concurrent.*

import java.util.function.*

import java.util.regex.*

import java.util.stream.*

import kotlin.collections.*

import kotlin.comparisons.*

import kotlin.io.*

import kotlin.jvm.*

import kotlin.jvm.functions.*

import kotlin.jvm.internal.*

import kotlin.ranges.*

import kotlin.sequences.*

import kotlin.text.*

  

/*

 * Complete the 'plusMinus' function below.

 *

 * The function accepts INTEGER_ARRAY arr as parameter.

 */

  

fun plusMinus(arr: Array<Int>): Unit {

    // Write your code here

    var p = 0.0

    var n = 0.0

    var o = 0.0

    for(a in arr) {

        if(a > 0) {

            p++

        }else if( a == 0) {

            o++

        } else{

            n++

        }

     }

     println(String.format("%.6f", p/arr.size))

     println(String.format("%.6f", n/arr.size))

     print(String.format("%.6f", o/arr.size))

}

  

fun main(args: Array<String>) {

    val n = readLine()!!.trim().toInt()

  

    val arr = readLine()!!.trimEnd().split(" ").map{ it.toInt() }.toTypedArray()

  

    plusMinus(arr)

}
```