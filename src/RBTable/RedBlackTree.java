package RBTable;


public class RedBlackTree<K extends Comparable<K>, V> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

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

    private Node root;

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.isRed();
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    public void insert(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        root = insert(root, key, value);
        root.color = BLACK;
    }

    private Node insert(Node h, K key, V value) {
        if (h == null) return new Node(key, value);

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = insert(h.left, key, value);
        else if (cmp > 0) h.right = insert(h.right, key, value);
        else h.value = value;

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

//    public V search(K key) {
//        if (key == null) throw new IllegalArgumentException("Key cannot be null");
//        Node x = root;
//        while (x != null) {
//            int cmp = key.compareTo(x.key);
//            if (cmp < 0) x = x.left;
//            else if (cmp > 0) x = x.right;
//            else return x.value;
//        }
//        return null;
//    }

    private Node fixUp(Node x) {
        if (isRed(x.right)) x = rotateLeft(x);
        if (isRed(x.left) && isRed(x.left.left)) x = rotateRight(x);
        if (isRed(x.left) && isRed(x.right)) flipColors(x);
        return x;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
        }
        return h;
    }

    private Node deleteMin(Node h) {
        if (h.left == null) return null;
        if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
        h.left = deleteMin(h.left);
        return fixUp(h);
    }

    private Node min(Node h) {
        if (h.left == null) return h;
        return min(h.left);
    }

    private Node delete(Node h, K key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left)) h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null)) return null;
            if (!isRed(h.right) && !isRed(h.right.left)) h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                h.value = get(h.right, min(h.right).key);
                h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            } else {
                h.right = delete(h.right, key);
            }
        }
        return fixUp(h);
    }

    public V delete(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        if (!contains(key)) return null;

        V deletedValue = get(key);
        if (!isRed(root.left) && !isRed(root.right)) root.color = RED;
        root = delete(root, key);
        if (root != null) root.color = BLACK;
        return deletedValue;
    }

    private boolean contains(K key) {
        return get(key) != null;
    }

    private V get(Node x, K key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.value;
        }
        return null;
    }

    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return get(root, key);
    }

}
