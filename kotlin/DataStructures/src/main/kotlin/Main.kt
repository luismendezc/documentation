package org.example

import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList


fun main() {
    val pq: HashMap<Int, Int> = HashMap()
    val set: HashSet<Int> = HashSet()
    val arregloEntero: IntArray = IntArray(5)
    val otherIntArr: MutableList<Int> = mutableListOf()
    val cadena = "asd"
    val charArr = cadena.toCharArray()
    val queue: Queue<Int> = LinkedList()
    val asd: Stack<Int> = Stack()
    var deque: ArrayDeque<Int> = ArrayDeque()
    val pair: Pair<Int, Int> = Pair(1,2)
    val mutableList: MutableList<Int> = mutableListOf()
    val arr: List<Int> = ArrayList<Int>(5)
    pair.first
    pair.second

    val data: MutableList<Int> = ArrayList<Int>(5)



    // [1,2,3,4,5,6]
    // [1,3,6,10,15,21]
}

class MovingAverage(val size: Int) {
    val q: Queue<Int> = LinkedList()

    fun next(`val`: Int): Double {
        q.offer(`val`)
        if(q.size > this.size) {
            q.poll()
        }
        var sum: Double = 0.0
        for(v in queue) {
            sum+=v
        }
        return sum / size
    }

}

/**
 * Example:
 * var ti = TreeNode(5)
 * var v = ti.`val`
 * Definition for a binary tree node.
 *
 */
class TreeNode(var `val`: Int) {
         var left: TreeNode? = null
       var right: TreeNode? = null
     }
class Solution {
    fun levelOrder(root: TreeNode?): List<List<Int>> {
        val queue: Queue<TreeNode> = LinkedList()
        val ml: MutableList<List<Int>> = mutableListOf()
        if(root == null) return ml
        queue.offer(root)
        while(!queue.isEmpty()) {
            val size = queue.size
            val arr: MutableList<Int> = ArrayList<Int>(size)
            for(i in 0 until size) {
                val check = queue.poll() as TreeNode
                if(check.left != null) {
                    queue.offer(check.left)
                }
                if(check.right != null) {
                    queue.offer(check.right)
                }

                arr[i] = check.`val`
            }
            ml.add(arr)
        }
        return ml

    }
}