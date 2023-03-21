package RBTable;

import RBTable.RBTreeNode;
import RBTable.RedBlackTree;

import java.util.Dictionary;
import java.util.Enumeration;

public class SynchronizedHashTable<K extends Comparable<K>, V> extends Dictionary<K, V> {
    private RedBlackTree<K, V>[] table;

    private int nodeCnt;
    private int capacity;
    private float loadFactor = 0.75f;
    private int threshold;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    @Override
    public synchronized int size() {
        return this.nodeCnt;
    }

    @Override
    public synchronized boolean isEmpty() {
        return this.nodeCnt == 0;
    }

    public SynchronizedHashTable(int cap) {
        if (cap <= 0) {
            throw new IllegalArgumentException("Negative Capacity or zero" + cap);
        }
        if (cap < 7) {
            cap = 7;
        }
        this.table = (RedBlackTree<K, V>[]) new RedBlackTree[cap];
        for (int i = 0; i < cap; i++) {
            table[i] = new RedBlackTree<>();
        }
        this.capacity = cap;
        threshold = (int)Math.min(cap * loadFactor, MAX_ARRAY_SIZE + 1);
    }

    public SynchronizedHashTable() {
        this(7);
    }

    private synchronized int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    @Override
    public synchronized V get(Object key) {
        int index = hash((K) key);
        return table[index].get((K) key);
    }

    @Override
    public synchronized V put(K key, V value) {
        int index = hash(key);
        table[index].insert(key, value);
        nodeCnt++;
        if (nodeCnt >= threshold) {
            // Rehash the table if the threshold is exceeded
            rehash();
        }
        return value;
    }

    private synchronized void rehash() {
        int oldCapacity = table.length;
        RedBlackTree<K,V>[] oldMap = table;

        // overflow-conscious code
        int newCapacity = (oldCapacity << 1) + 1;
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            if (oldCapacity == MAX_ARRAY_SIZE)
                // Keep running with MAX_ARRAY_SIZE buckets
                return;
            newCapacity = MAX_ARRAY_SIZE;
        }
        RedBlackTree<?,?>[] newMap = new RedBlackTree<?,?>[newCapacity];
        threshold = (int)Math.min(newCapacity * loadFactor, MAX_ARRAY_SIZE + 1);

        for (int i = 0; i < newCapacity; i++) {
            newMap[i] = new RedBlackTree<>();
        }
        table = (RedBlackTree<K, V>[]) newMap;
        for (int i = oldCapacity ; i-- > 0 ;) {
            RBTreeNode<K, V>[] arr = oldMap[i].traverseWholeTree();
            for(RBTreeNode<K, V> node : arr){
                int index = (node.hashCode() & 0x7FFFFFFF) % newCapacity;
                table[index].insert((K) node.key, (V) node.value);
            }

        }
    }

    @Override
    public synchronized V remove(Object key) {
        int index = hash((K) key);
        boolean writeFlag = false;
        V preVal = table[index].delete((K) key);
        nodeCnt--;
        return preVal;
    }


    // do not include in term project range
    @Override
    public Enumeration<K> keys() {
        return null;
    }

    @Override
    public Enumeration<V> elements() {
        return null;
    }

}

