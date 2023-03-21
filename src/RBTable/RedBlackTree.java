package RBTable;


public class RedBlackTree<K extends Comparable<K>, V> {
    public static final boolean RED = true;
    public static final boolean BLACK = false;
    private Node root;

    private class Node {
        K key;
        V value;
        Node left, right;
        boolean color;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.color = RED;
        }

        boolean isRed() {
            return color == RED;
        }
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

    private Node rotateLeft(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        if(node.left != null){
            node.left.color = !node.left.color;
        }
        if(node.right != null){
            node.right.color = !node.right.color;
        }

    }

    private boolean isRed(Node node) {
        if (node == null){
            return false;
        }
        return node.isRed();
    }

    private Node insertNode(Node node, K key, V value) {
        if (node == null) return new Node(key, value);

        int cmp = key.compareTo(node.key);
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

    private Node fixUp(Node x) {
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

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
        }
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return null;
        if (!isRed(node.left) && !isRed(node.left.left)) node = moveRedLeft(node);
        node.left = deleteMin(node.left);
        return fixUp(node);
    }

    private Node getMin(Node node) {
        if (node.left == null) return node;
        return getMin(node.left);
    }

    private Node delete(Node node, K key) {
        if (key.compareTo(node.key) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left)) node = moveRedLeft(node);
            node.left = delete(node.left, key);
        } else {
            if (isRed(node.left)){
                node = rotateRight(node);
            }
            if (key.compareTo(node.key) == 0 && (node.right == null)){
                return null;
            }
            if (!isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }
            if (key.compareTo(node.key) == 0) {
                node.value = getNode(node.right, getMin(node.right).key);
                node.key = getMin(node.right).key;
                node.right = deleteMin(node.right);
            } else {
                node.right = delete(node.right, key);
            }

        }
        return fixUp(node);
    }



    private boolean contains(K key) {
        return get(key) != null;
    }

    private V getNode(Node x, K key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.value;
        }
        return null;
    }



}
