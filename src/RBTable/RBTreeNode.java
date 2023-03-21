package RBTable;

public class RBTreeNode<K, V> {
    public static final boolean RED = true;
    public static final boolean BLACK = false;

    public K key;
    public V value;
    RBTreeNode left, right;
    boolean color;

    RBTreeNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.color = RED;
    }

    boolean isRed() {
        return color == RED;
    }
}