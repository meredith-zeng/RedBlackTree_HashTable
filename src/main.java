import RBTable.RBTreeHashTable;

import java.util.Hashtable;

public class main {
    public static void main(String[] args) {
        Hashtable<Integer, Integer> map = new Hashtable<>();
        RBTreeHashTable<Integer, Integer> test = new RBTreeHashTable<>();
        for(int i = 0; i < 190000; i++){
            test.put(i, i);
        }
    }
}
