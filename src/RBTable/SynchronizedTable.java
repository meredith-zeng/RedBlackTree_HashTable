package RBTable;


import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;

public class SynchronizedTable<K, V> extends Dictionary<K, V> {
    private int nodeCnt;
    Node<K, V>[] table;
    private final double loadFactor = 1.1;
    private final double reduceFactor = 0.4;
    private int capacity;
    private int binaryCapNum;

    static class Node<K, V> implements Map.Entry<K, V> {
        public int hash;
        public Node<K, V> next;

        private K key;
        private V value;


        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return this.value;
        }
    }

    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public int size() {
        return this.nodeCnt;
    }

    @Override
    public boolean isEmpty() {
        return this.nodeCnt == 0;
    }




    public SynchronizedTable(int cap) {
        if (cap <= 0) {
            throw new IllegalArgumentException("Negative Capacity or zero" + cap);
        }
        if (cap < 7) {
            cap = 7;
        }
        this.table = new Node[cap];
        this.capacity = cap;
        this.binaryCapNum = 8;
    }

    public SynchronizedTable() {
        this(7);
    }


    private synchronized void reduceRehash(int newCapacity) {
        int oldCap = table.length;
        Node<K, V>[] curTable = table;
        Node<K, V>[] newTable = new Node[newCapacity];

        this.binaryCapNum = (binaryCapNum << 1);
        this.capacity = newCapacity;
        this.table = newTable;

        for (int i = 0; i < oldCap; i++) {
            Node<K, V> oldNode = curTable[i];
            while (oldNode != null) {
                Node<K, V> curNode = oldNode;
                oldNode = oldNode.next;
                int hash = curNode.getKey().hashCode();
                int idx = hash % capacity;
                curNode.next = newTable[idx];
                newTable[idx] = curNode;
            }

        }


    }

    private synchronized void increaseRehash(int newCapacity) {
        int oldCap = table.length;
        Node<K, V>[] curTable = table;
        Node<K, V>[] newTable = new Node[newCapacity];

        this.binaryCapNum = (binaryCapNum >> 1);
        this.capacity = newCapacity;
        this.table = newTable;

        for (int i = 0; i < oldCap; i++) {
            Node<K, V> oldNode = curTable[i];
            while (oldNode != null) {
                Node<K, V> curNode = oldNode;
                oldNode = oldNode.next;

                int idx = curNode.hash % newCapacity;
                curNode.next = newTable[idx];
                newTable[idx] = curNode;
            }

        }
    }


    @Override
    public synchronized V get(Object key) {
        Node<K, V>[] curTable = table;
        int hash = key.hashCode();
        int idx = hash % capacity;

        Node<?, ?> node = curTable[idx];
        while (node != null) {
            if (node != null
                    && node.hash == hash
                    && node.getKey().equals(key)) {
                return (V) node.getValue();
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public synchronized V put(K key, V value) {
        if (value == null || key == null) {
            System.out.println("key and value can not both be null.");
            return null;
        }

        Node<K, V>[] curTable = table;
        int hash = key.hashCode();
        int idx = hash % capacity;

        Node<K, V> node = curTable[idx];
        while (node != null) {
            if (node.hash == hash && node.getKey().equals(key)) {
                V preVal = node.getValue();
                node.setValue(value);
                return preVal;
            }
            node = node.next;
        }
        // current idx doesn't have node yet, just add to the table
        addNode(key, value);
        return null;
    }

    @Override
    public synchronized V remove(Object key) {
        int hash = key.hashCode();
        int idx = hash % capacity;

        Node<K, V> node = table[idx];
        Node<K, V> preNode = null;

        while (node != null) {
            if (node != null
                    && node.hash == hash
                    && node.getKey().equals(key)) {
                if (preNode != null) {
                    preNode.next = node.next;
                } else if (preNode == null) {
                    table[idx] = node.next;
                }
                nodeCnt--;
                V temp = node.getValue();
                node.setValue(null);
                return temp;
            }
            preNode = node;
            node = node.next;

        }
        return null;
    }


    public synchronized boolean containsKey(Object key) {
        Node<K, V>[] curTable = table;
        int hash = key.hashCode();
        int idx = hash % capacity;

        Node<K, V> curNode = curTable[idx];
        while (curNode != null) {
            if (curNode != null
                    && curNode.hash == hash
                    && curNode.getKey().equals(key)) {
                return true;
            }
            curNode = curNode.next;
        }
        return false;
    }

    private synchronized void addNode(K key, V value) {
        int hash = key.hashCode();
        int idx = hash % capacity;
        Node<K, V> node = table[idx];

        Node<K, V> newNode = new Node<>(hash, key, value, node);
        table[idx] = newNode;
        nodeCnt++;

        if (nodeCnt >= capacity * loadFactor) {
            int newCapacity = MathUtil.floorPrime(binaryCapNum);
            if (newCapacity == capacity
                    || (newCapacity / 10 + Integer.MAX_VALUE % 10 >= Integer.MAX_VALUE)
                    || newCapacity < 7) {
                return;
            }
            increaseRehash(newCapacity);
        } else if (nodeCnt >= 7 && nodeCnt < capacity * reduceFactor) {
            int newCapacity = MathUtil.ceilingPrime(binaryCapNum);
            if (newCapacity == capacity
                    || (newCapacity >= Integer.MAX_VALUE)
                    || newCapacity < 7) {
                return;
            }
            reduceRehash(newCapacity);
        }


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