package org.example.BinarySearchTree

class E2ValidateBinarySearchTree {
}


/**
 * Example:
 * var ti = TreeNode(5)
 * var v = ti.`val`
 * Definition for a binary tree node.
 * class TreeNode(var `val`: Int) {
 *     var left: TreeNode? = null
 *     var right: TreeNode? = null
 * }
 */

class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}


class Solution {
    fun isValidBST(root: TreeNode?): Boolean {
        return  isValidBSTHelper(root, Long.MIN_VALUE, Long.MAX_VALUE)
    }

    fun isValidBSTHelper(node: TreeNode?, min: Long, max: Long): Boolean {
        if (node == null) return true
        if (node.`val` <= min || node.`val` >= max) return false
        return isValidBSTHelper(node.left, min, node.`val`.toLong()) &&
                isValidBSTHelper(node.right, node.`val`.toLong(), max)
    }
}