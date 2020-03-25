package org.jetbrains.rider.trees

import kotlin.math.max

internal class PersistentAVLTreeSet<K : Comparable<K>>() {
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

    fun add(key: K): PersistentAVLTreeSet<K> {
        return PersistentAVLTreeSet(add(root, key))
    }

    fun get(key: K): K? {
        return find(key)?.key
    }

    fun contains(key: K): Boolean {
        return get(key) != null
    }

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

    fun remove(key: K): PersistentAVLTreeSet<K> {
        return PersistentAVLTreeSet(remove(root, key))
    }

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

    private fun add(node: Node<K>?, key: K): Node<K> {
        return when {
            node == null -> Node(key)
            node.key > key -> balance(Node(node).also { it.leftChild = add(node.leftChild, key) })
            node.key < key -> balance(Node(node).also { it.rightChild = add(node.rightChild, key) })
            else -> Node(node).also { it.key = key }
        }
    }

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

    private fun findMax(node: Node<K>): Node<K> {
        var current: Node<K> = node
        while (true) {
            current = current.rightChild ?: return current
        }
    }

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

    private fun rotateRight(node: Node<K>): Node<K> {
        val next = node.leftChild!!
        node.leftChild = next.rightChild
        next.rightChild = node
        node.fix()
        next.fix()
        return next
    }

    private fun rotateLeft(node: Node<K>): Node<K> {
        val next = node.rightChild!!
        node.rightChild = next.leftChild
        next.leftChild = node
        node.fix()
        next.fix()
        return next
    }
}