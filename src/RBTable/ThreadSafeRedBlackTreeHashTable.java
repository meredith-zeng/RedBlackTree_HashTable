package RBTable;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeRedBlackTreeHashTable<K extends Comparable<K>, V> {
    private final int size;
    private final RedBlackTree<K, V>[] hashTable;
    private final ReentrantReadWriteLock[] locks;

    @SuppressWarnings("unchecked")
    public ThreadSafeRedBlackTreeHashTable(int size) {
        this.size = size;
        hashTable = (RedBlackTree<K, V>[]) new RedBlackTree[size];
        locks = new ReentrantReadWriteLock[size];
        for (int i = 0; i < size; i++) {
            hashTable[i] = new RedBlackTree<>();
            locks[i] = new ReentrantReadWriteLock();
        }
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % size;
    }

    public V get(K key) {
        int index = hash(key);
        ReentrantReadWriteLock.ReadLock readLock = locks[index].readLock();
        readLock.lock();
        try {
            return hashTable[index].search(key);
        } finally {
            readLock.unlock();
        }
    }

    public void put(K key, V value) {
        int index = hash(key);
        ReentrantReadWriteLock.WriteLock writeLock = locks[index].writeLock();
        writeLock.lock();
        try {
            hashTable[index].insert(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public void remove(K key) {
        int index = hash(key);
        ReentrantReadWriteLock.WriteLock writeLock = locks[index].writeLock();
        writeLock.lock();
        try {
            hashTable[index].delete(key);
        } finally {
            writeLock.unlock();
        }
    }
}

