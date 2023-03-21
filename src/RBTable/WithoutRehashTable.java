package RBTable;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WithoutRehashTable<K extends Comparable<K>, V> extends Dictionary<K, V> {
    private RedBlackTree<K, V>[] table;
    private ReentrantReadWriteLock[] locks;

    private int nodeCnt;
    private int capacity;
    private float loadFactor = 0.75f;
    private int threshold;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    @Override
    public int size() {
        return this.nodeCnt;
    }

    @Override
    public boolean isEmpty() {
        return this.nodeCnt == 0;
    }

    public WithoutRehashTable(int cap) {
        if (cap <= 0) {
            throw new IllegalArgumentException("Negative Capacity or zero" + cap);
        }
        if (cap < 7) {
            cap = 7;
        }
        this.table = (RedBlackTree<K, V>[]) new RedBlackTree[cap];
        this.locks = new ReentrantReadWriteLock[cap];
        for (int i = 0; i < cap; i++) {
            table[i] = new RedBlackTree<>();
            locks[i] = new ReentrantReadWriteLock();
        }
        this.capacity = cap;
        threshold = (int)Math.min(cap * loadFactor, MAX_ARRAY_SIZE + 1);
    }

    public WithoutRehashTable() {
        this(7);
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    @Override
    public V get(Object key) {
        int index = hash((K) key);
        ReentrantReadWriteLock.ReadLock readLock = locks[index].readLock();
        readLock.lock();
        try {
            return table[index].get((K) key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        int index = hash(key);
        ReentrantReadWriteLock.WriteLock writeLock = locks[index].writeLock();
        writeLock.lock();
        boolean opFlag = false;
        try {
            table[index].insert(key, value);
        } finally {
            writeLock.unlock();
            opFlag = true;
            nodeCnt++;
        }
        return opFlag ? value : null;
    }



    @Override
    public V remove(Object key) {
        int index = hash((K) key);
        ReentrantReadWriteLock.WriteLock writeLock = locks[index].writeLock();
        writeLock.lock();
        boolean writeFlag = false;
        V preVal = null;
        try {
            preVal = table[index].delete((K) key);
        } finally {
            writeLock.unlock();
            writeFlag = true;
            nodeCnt--;
        }
        return writeFlag ? preVal : null;
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

