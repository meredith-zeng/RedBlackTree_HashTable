package RBTable;


import sun.misc.Queue;

import java.awt.*;
import java.util.Deque;
import java.util.LinkedList;

public class RedBlackTree<K extends Comparable<K>, V> {
    public static final boolean RED = true;
    public static final boolean BLACK = false;
    private RBTreeNode<K, V> root;

    public RBTreeNode<K, V>[] traverseWholeTree(){
        LinkedList<RBTreeNode> list = new LinkedList<>();
        RBTreeNode<K, V> cur = root;
        if(root == null){
            return new RBTreeNode[0];
        }
        Deque<RBTreeNode<K, V>> queue = new LinkedList<>();

        queue.offer(cur);
        while (!queue.isEmpty()){
            RBTreeNode<K, V> node = queue.poll();
            list.add(node);
            if(node.left != null){
                queue.offer(node.left);
            }
            if(node.right != null){
                queue.offer(node.right);
            }
        }
        RBTreeNode<K, V>[] ans = new RBTreeNode[list.size()];
        for(int i = 0; i < list.size(); i++){
            ans[i] = list.get(i);
        }
        return ans;
    }

    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return getNode(root, key);
    }

    public void insert(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        root = insertNode(root, key, value);
        root.color = BLACK;
    }

    public V delete(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (!contains(key)){
            return null;
        }

        V deletedValue = (V) get(key);
        if (!isRed(root.left) && !isRed(root.right)){
            root.color = RED;
        }
        root = delete(root, key);
        if (root != null) {
            root.color = BLACK;
        }
        return deletedValue;
    }

    private RBTreeNode rotateLeft(RBTreeNode node) {
        RBTreeNode x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }

    private RBTreeNode rotateRight(RBTreeNode node) {
        RBTreeNode x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }

    private void flipColors(RBTreeNode node) {
        node.color = !node.color;
        if(node.left != null){
            node.left.color = !node.left.color;
        }
        if(node.right != null){
            node.right.color = !node.right.color;
        }

    }

    private boolean isRed(RBTreeNode node) {
        if (node == null){
            return false;
        }
        return node.isRed();
    }

    private RBTreeNode insertNode(RBTreeNode node, K key, V value) {
        if (node == null) return new RBTreeNode(key, value);

        int cmp = key.compareTo((K) node.key);
        if (cmp < 0){
            node.left = insertNode(node.left, key, value);
        } else if (cmp > 0){
            node.right = insertNode(node.right, key, value);
        } else {
            node.value = value;
        }

        if (isRed(node.right) && !isRed(node.left)){
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)){
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

    private RBTreeNode fixUp(RBTreeNode x) {
        if (isRed(x.right)){
            x = rotateLeft(x);
        }
        if (isRed(x.left) && isRed(x.left.left)){
            x = rotateRight(x);
        }
        if (isRed(x.left) && isRed(x.right)) {
            flipColors(x);
        }
        return x;
    }

    private RBTreeNode moveRedLeft(RBTreeNode node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
        }
        return node;
    }

    private RBTreeNode moveRedRight(RBTreeNode node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
        }
        return node;
    }

    private RBTreeNode deleteMin(RBTreeNode node) {
        if (node.left == null){
            return null;
        }
        if (!isRed(node.left) && !isRed(node.left.left)){
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return fixUp(node);
    }

    private RBTreeNode getMin(RBTreeNode node) {
        if (node.left == null) return node;
        return getMin(node.left);
    }

    private RBTreeNode delete(RBTreeNode node, K key) {
        if (key.compareTo((K) node.key) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left)) node = moveRedLeft(node);
            node.left = delete(node.left, key);
        } else {
            if (isRed(node.left)){
                node = rotateRight(node);
            }
            if (key.compareTo((K) node.key) == 0 && (node.right == null)){
                return null;
            }
            if (!isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }
            if (key.compareTo((K) node.key) == 0) {
                node.value = getNode(node.right, (K) getMin(node.right).key);
                node.key = getMin(node.right).key;
                node.right = deleteMin(node.right);
            } else {
                node.right = delete(node.right, key);
            }

        }
        return fixUp(node);
    }

    private V getNode(RBTreeNode x, K key) {
        while (x != null) {
            int cmp = key.compareTo((K) x.key);
            if (cmp < 0){
                x = x.left;
            } else if (cmp > 0){
                x = x.right;
            } else {
                return (V) x.value;
            }
        }
        return null;
    }

    private boolean contains(K key) {
        return get(key) != null;
    }


}
