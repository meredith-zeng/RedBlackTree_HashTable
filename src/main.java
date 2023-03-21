import RBTable.RedWriteLockHashTable;
import RBTable.SynchronizedHashTable;

import java.util.Hashtable;

public class main {
    public static void main(String[] args) {
//        Hashtable<Integer, Integer> map = new Hashtable<>();
//        RedWriteLockHashTable<Integer, Integer> test = new RedWriteLockHashTable<>();
//        for(int i = 0; i < 190000; i++){
//            test.put(i, i);
//        }

        SynchronizedHashTable<Integer, Integer> map = new SynchronizedHashTable<>();
        for(int i = 0; i < 190000; i++){
            map.put(i, i);
        }
        for(int i = 0; i < 11; i++){
            map.get(i);
        }
        map.remove(1);
    }
}
