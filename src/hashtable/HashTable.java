package hashtable;


import java.util.*;


// inheritance from Dictionary
// implement means implement all method from a template
public class HashTable<K, V> extends Dictionary<K,V> {
    private int nodeCnt;
    Node<K, V>[] table;
    private final double loadFactor = 1.1;
    private final double reduceFactor = 0.4;
    private int capacity;
    private int binaryCapNum;

    public int getCapacity(){
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


    public HashTable(int cap) {
        if(cap <= 0){
            throw new IllegalArgumentException("Negative Capacity or zero" + cap);
        }
        if(cap < 7){
            cap = 7;
        }
        this.table = new Node[cap];
        this.capacity = cap;
        this.binaryCapNum = 8;
    }

    public HashTable(){
        this(7);
    }

    @Override
    public synchronized V get(Object key) {
        Node<K, V>[] curTable = table;
        int hash = hashCode(key);
        int idx = getIdx(key, hash);

        Node<?, ?> node = curTable[idx];
        while(node != null){
            if(node != null
                    && node.hash == hash
                    && node.getKey().equals(key)){
                return (V) node.getValue();
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public synchronized V put(K key, V value) {
        if(value == null || key == null){
            System.out.println("key and value can not both be null.");
            return null;
        }

        Node<K, V>[] curTable = table;
        int hash = hashCode(key);
        int idx = getIdx(key, hash);

        Node<K, V> node = curTable[idx];
        while (node != null){
            if(node.hash == hash && node.getKey().equals(key)){
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
        int hash = hashCode(key);
        int idx = getIdx(key, hash);

        Node<K, V> node = table[idx];
        Node<K, V> preNode = null;

        while(node != null){
            if(node != null
                    && node.hash == hash
                    && node.getKey().equals(key)){
                if(preNode != null){
                    preNode.next = node.next;
                }else if(preNode == null){
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

    public synchronized int hashCode(Object key){
        int hash;
        hash = key.hashCode();
        return hash;
    }


    public synchronized boolean containsKey(Object key) {
        Node<K, V>[] curTable = table;
        int hash = hashCode(key);
        int idx = getIdx(key, hash);

        Node<K, V> curNode = curTable[idx];
        while(curNode != null){
            if(curNode != null
                    && curNode.hash == hash
                    && curNode.getKey().equals(key)){
                return true;
            }
            curNode = curNode.next;
        }
        return false;
    }

    public synchronized int getIdx(Object key, int hash){
        int idx;
        if(!(key instanceof String)){
            idx = hash % capacity;
        }else {
            idx = hash;
        }
        return idx;
    }

    private synchronized void addNode(K key, V value) {
        int hash = hashCode(key);
        int idx = getIdx(key, hash);
        Node<K, V> node = table[idx];

        Node<K, V> newNode = new Node<>(hash, key, value, node);
        table[idx] = newNode;
        nodeCnt++;


    }




    // don't require by P1 requirement
    @Override
    public Enumeration<K> keys() {
        return null;
    }

    @Override
    public Enumeration<V> elements() {
        return null;
    }


}
