package org.example.LinkedList


class E1AddTwoNumbers {
    private fun run() {
        val l1 = ListNode(2)
        l1.next = ListNode(4)
        val c = l1.next
        c!!.next = ListNode(3)

        val l2 = ListNode(5)
        l2.next = ListNode(6)
        val c2 = l2.next
        c2!!.next = ListNode(4)

        val s = Solution()
        val r = s.addTwoNumbers(l1, l2)
        println(r?.`val`)
        var cur = r
        while(cur?.next != null){
            println(cur.next!!.`val`)
            cur = cur.next
        }
    }
}


class ListNode(var `val`: Int) {
    var next: ListNode? = null
}
/**
 * Example:
 * var li = ListNode(5)
 * var v = li.`val`
 * Definition for singly-linked list.
 * class ListNode(var `val`: Int) {
 *     var next: ListNode? = null
 * }
 */
class Solution {
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        var s1 = ""
        var tl1 = l1
        var s2 = ""
        var tl2 = l2

        while(tl1 != null){
            s1 += tl1.`val`
            tl1 = tl1.next
        }
        while(tl2 != null){
            s2 += tl2.`val`
            tl2 = tl2.next
        }
        val r = s1.reversed().toBigInteger() + s2.reversed().toBigInteger()
        val resultStr = r.toString().reversed()

        val dummyHead = ListNode(0)
        var current = dummyHead

        resultStr.forEach { c ->
            current.next = ListNode(c.digitToInt())
            current = current.next!!

        }
        return dummyHead.next

    }
}


//---------------------
internal class Solution2 {
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        var l1 = l1
        var l2 = l2
        val dummyHead = ListNode(0)
        var curr = dummyHead
        var carry = 0
        while (l1 != null || l2 != null || carry != 0) {
            val x = l1?.`val` ?: 0
            val y = l2?.`val` ?: 0
            val sum = carry + x + y
            carry = sum / 10
            curr.next = ListNode(sum % 10)
            curr = curr.next!!
            if (l1 != null) l1 = l1.next
            if (l2 != null) l2 = l2.next
        }
        return dummyHead.next
    }
}