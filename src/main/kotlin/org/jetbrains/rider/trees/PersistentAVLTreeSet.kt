package org.jetbrains.rider.trees

import kotlin.math.max

/**
 * @author akifev
 *
 * Persistent set based on AVL tree
 */
internal class PersistentAVLTreeSet<K : Comparable<K>>() {
    /**
     * Private class Node for defining AVL tree node
     */
    private class Node<K: Comparable<K>>(
        var key: K,

        var height: Int = 1,
        var size: Int = 1,

        var leftChild: Node<K>? = null,
        var rightChild: Node<K>? = null
    ) {
        constructor(node: Node<K>) : this(node.key, node.height, node.size, node.leftChild, node.rightChild)

        fun heightDifference(): Int = (leftChild?.height ?: 0) - (rightChild?.height ?: 0)

        fun fix() {
            height = max((leftChild?.height ?: 0), (rightChild?.height ?: 0)) + 1
            size = (leftChild?.size ?: 0) + (rightChild?.size ?: 0) + 1
        }
    }

    private var root: Node<K>? = null

    private constructor(node: Node<K>?) : this() { root = node }

    /**
     * Adds a key to the tree.
     * Creates new tree version. Current tree version remains the same.
     *
     * @param key is a key in the tree to be added
     *
     * @return a new instance of class with added key
     */
    fun add(key: K): PersistentAVLTreeSet<K> {
        return PersistentAVLTreeSet(add(root, key))
    }

    /**
     * Returns a key from the tree or null, if there is no such a key.
     *
     * @param key to be found
     *
     * @return found key or null, if there is no such a key
     */
    fun get(key: K): K? {
        return find(key)?.key
    }

    /**
     * Returns true if tree contains a key. Otherwise, returns false.
     *
     * @param key to be found
     *
     * @return true if tree contains a key. Otherwise, returns false
     */
    fun contains(key: K): Boolean {
        return get(key) != null
    }

    /**
     * Retunes the quantity of elements greater than a key or null, if there is no such a key.
     *
     * @param key to be compared
     *
     * @return the quantity of elements greater than a key or null, if there is no such a key
     */
    fun getPosition(key: K): Int? {
        var result = 0
        var current: Node<K>? = root
        while (current != null) {
            when {
                current.key > key -> {
                    result += (current.rightChild?.size ?: 0) + 1
                    current = current.leftChild
                }
                current.key < key -> current = current.rightChild
                else -> return result + (current.rightChild?.size ?: 0) + 1
            }
        }

        return null
    }

    /**
     * Removes a key from the tree.
     * Creates new tree version. Current tree version remains the same.
     *
     * @param key is a key in the tree to be removed
     *
     * @return a new instance of class without removed key
     */
    fun remove(key: K): PersistentAVLTreeSet<K> {
        return PersistentAVLTreeSet(remove(root, key))
    }

    /**
     * Non-recursively finds of node associated with a key. Returns found node or null, if there is no such a key.
     */
    private fun find(key: K) : Node<K>? {
        var current: Node<K>? = root
        while (current != null) {
            current = when {
                current.key > key -> current.leftChild
                current.key < key -> current.rightChild
                else -> return current
            }
        }

        return null
    }

    /**
     * Recursively adds a node associated with a key to the node subtree.
     * Returns a new node subtree with an added node.
     * Creates new tree version. Current tree version remains the same.
     * Creates a logarithm of subtree height of new nodes.
     */
    private fun add(node: Node<K>?, key: K): Node<K> {
        return when {
            node == null -> Node(key)
            node.key > key -> balance(Node(node).also { it.leftChild = add(node.leftChild, key) })
            node.key < key -> balance(Node(node).also { it.rightChild = add(node.rightChild, key) })
            else -> Node(node).also { it.key = key }
        }
    }

    /**
     * Recursively removes a node associated with a key from the node subtree.
     * Returns a new node subtree without a removed node or null, if the last element of subtree has been removed.
     * Creates new tree version. Current tree version remains the same.
     * Creates a logarithm of subtree height of new nodes.
     */
    private fun remove(node: Node<K>?, key: K): Node<K>? {
        when {
            node == null -> return null
            node.key > key -> return balance(Node(node).also { it.leftChild = remove(node.leftChild, key) })
            node.key < key -> return balance(Node(node).also { it.rightChild = remove(node.rightChild, key) })
            else -> {
                return when {
                    node.leftChild == null && node.rightChild == null -> null
                    node.leftChild == null || node.rightChild == null -> node.leftChild ?: node.rightChild
                    else -> {
                        val previous = findMax(node.leftChild!!)

                        balance(Node(previous).also {
                            it.leftChild = remove(node.leftChild, previous.key)
                            it.rightChild = node.rightChild
                        })
                    }
                }
            }
        }
    }

    /**
     * Non-recursively finds the maximum key in the node subtree.
     * Returns a node associated with the key.
     */
    private fun findMax(node: Node<K>): Node<K> {
        var current: Node<K> = node
        while (true) {
            current = current.rightChild ?: return current
        }
    }

    /**
     * Recursively balances the node subtree.
     * Returns balanced node subtree.
     */
    private fun balance(node: Node<K>): Node<K> {
        node.fix()
        return when {
            node.heightDifference() == 2 -> rotateRight(node.also {
                if (it.leftChild!!.heightDifference() < 0) {
                    it.leftChild = rotateLeft(node.leftChild!!)
                }
            })
            node.heightDifference() == -2 -> rotateLeft(node.also {
                if (it.rightChild!!.heightDifference() > 0) {
                    it.rightChild = rotateRight(node.rightChild!!)
                }
            })
            else -> node
        }
    }

    /**
     * Right rotation in AVL tree.
     * Returns rotated node subtree.
     */
    private fun rotateRight(node: Node<K>): Node<K> {
        val next = node.leftChild!!
        node.leftChild = next.rightChild
        next.rightChild = node
        node.fix()
        next.fix()

        return next
    }

    /**
     * Left rotation in AVL tree.
     * Returns rotated node subtree.
     */
    private fun rotateLeft(node: Node<K>): Node<K> {
        val next = node.rightChild!!
        node.rightChild = next.leftChild
        next.leftChild = node
        node.fix()
        next.fix()

        return next
    }
}