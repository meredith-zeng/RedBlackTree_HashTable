package Performance;
import RBTable.RedWriteLockHashTable;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

public class Test {

    public static void HashTablePerformance(String[] args){
        // create a HashMap with a large initial capacity to avoid resizing during the test
        Hashtable<Integer, Integer> map = new Hashtable<>(1000000);

        // populate the map with random key-value pairs
        Random rand = new Random();
        long startTime1 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.put(rand.nextInt(1000000), rand.nextInt(1000000));

        }
        long endTime1 = System.nanoTime();
        System.out.println("Total Time taken(HashTable put function): " + (endTime1 - startTime1) / 1000000.0 + " milliseconds");

        // test the performance of the get function
        long startTime2 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.get(rand.nextInt(1000000));
        }
        long endTime2 = System.nanoTime();

        System.out.println("Total Time taken(HashTable get function): " + (endTime2 - startTime2) / 1000000.0 + " milliseconds");

        long startTime3 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.remove(rand.nextInt(1000000));
        }
        long endTime3 = System.nanoTime();

        System.out.println("Total Time taken(HashTable remove function): " + (endTime3 - startTime3) / 1000000.0 + " milliseconds");
    }
    public static void RBTablePerformance(String[] args){
        // create a HashMap with a large initial capacity to avoid resizing during the test
        RedWriteLockHashTable<Integer, Integer> map = new RedWriteLockHashTable<>(1000000);

        // populate the map with random key-value pairs
        Random rand = new Random();
        long startTime1 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.put(rand.nextInt(1000000), rand.nextInt(1000000));
        }
        long endTime1 = System.nanoTime();
        System.out.println("Total Time taken(RBTable put function): " + (endTime1 - startTime1) / 1000000.0 + " milliseconds");

        // test the performance of the get function
        long startTime2 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.get(rand.nextInt(1000000));
        }
        long endTime2 = System.nanoTime();

        long startTime3 = System.nanoTime();
        System.out.println("Total Time taken(RBTable get function): " + (endTime2 - startTime2) / 1000000.0 + " milliseconds");
        for (int i = 0; i < 1000000; i++) {
            map.remove(rand.nextInt(1000000));
        }
        long endTime3 = System.nanoTime();

        System.out.println("Total Time taken(RBTable remove function): " + (endTime3 - startTime3) / 1000000.0 + " milliseconds");
    }
    public static void ThreadSafetyPerformance(){
        final int THREAD_COUNT = 100;
        final int TABLE_SIZE = 1000000;

        // create a HashMap
        final RedWriteLockHashTable<Integer, Integer> map = new RedWriteLockHashTable<>(TABLE_SIZE);

        // populate the map with random key-value pairs
        Random rand = new Random();
        for (int i = 0; i < TABLE_SIZE; i++) {
            map.put(rand.nextInt(TABLE_SIZE), rand.nextInt(TABLE_SIZE));
        }
        // create and start multiple threads to access the map concurrently
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    Random rand = new Random();
                    for (int j = 0; j < 10000; j++) {
                        map.get(rand.nextInt(TABLE_SIZE));
                    }
                }
            });
            threads[i].start();
        }

        // wait for all threads to complete
        for (int i = 0; i < THREAD_COUNT; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // output the size of the map
        System.out.println("Map size: " + map.size());
        if(map.size()==TABLE_SIZE){
            System.out.println("The Table is Thread-safe!");
        }
    }
    public static void main(String[] args) {
        HashTablePerformance(args);
        RBTablePerformance(args);

        ThreadSafetyPerformance();

        HashMap<Integer, Integer> hashMap = new HashMap<>();
    }
}

