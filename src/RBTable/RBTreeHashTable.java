package RBTable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RBTreeHashTable<K extends Comparable<K>, V> implements Map<K,V> {
    private final int size;
    private final RedBlackTree<K, V>[] hashTable;
    private final ReentrantReadWriteLock[] locks;

    @SuppressWarnings("unchecked")
    public RBTreeHashTable(int size) {
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
            return hashTable[index].get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        int index = hash(key);
        ReentrantReadWriteLock.WriteLock writeLock = locks[index].writeLock();
        writeLock.lock();
        boolean opFlag = false;
        try {
            hashTable[index].insert(key, value);
        } finally {
            writeLock.unlock();
            opFlag = true;
        }
        return opFlag ? value : null;
    }


    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
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
    public int getSize(){
        return size;
    }
}

