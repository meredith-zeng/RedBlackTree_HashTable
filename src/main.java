import RBTable.ThreadSafeRedBlackTreeHashTable;

public class main {
    public static void main(String[] args) {
        ThreadSafeRedBlackTreeHashTable<Integer, Integer> hashTable = new ThreadSafeRedBlackTreeHashTable<>(6);
        hashTable.put(1, 1);
        hashTable.put(2, 2);
        int val = hashTable.get(1);
        System.out.println(val);

    }
}
