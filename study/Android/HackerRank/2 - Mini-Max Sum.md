![[2 - Mini-Max Sum - 1image.png]]
![[2 - Mini-Max Sum - 2image.png]]

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

 * Complete the 'miniMaxSum' function below.

 *

 * The function accepts INTEGER_ARRAY arr as parameter.

 */

  

fun miniMaxSum(arr: Array<Int>): Unit {

    // Write your code here

    var maxValue = BigInteger.valueOf(Long.MIN_VALUE)

    var minValue = BigInteger.valueOf(Long.MAX_VALUE)

    var sum = BigInteger.ZERO

    for(n in arr){

        val newn = n.toBigInteger()

       sum += newn

        if(newn > maxValue ){

            maxValue = newn

        }

        if(newn < minValue){

            minValue = newn

        }

    }

    print("${sum-maxValue} ${sum-minValue}")

}

  

fun main(args: Array<String>) {

  

    val arr = readLine()!!.trimEnd().split(" ").map{ it.toInt() }.toTypedArray()

  

    miniMaxSum(arr)

}
```