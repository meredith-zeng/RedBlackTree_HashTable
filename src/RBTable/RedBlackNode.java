package RBTable;

public class RedBlackNode<K extends Comparable<K>, V> {
    public static final boolean RED = true;
    public static final boolean BLACK = false;

    public K key;
    public V value;
    public RedBlackNode<K, V> left;
    public RedBlackNode<K, V> right;
    public boolean color;

    public RedBlackNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.color = RED;
        this.left = null;
        this.right = null;
    }

    public boolean isRed() {
        return color == RED;
    }
}
