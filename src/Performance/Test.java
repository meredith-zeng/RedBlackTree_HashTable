package Performance;
import hashtable.HashTable;
import RBTable.ThreadSafeRedBlackTreeHashTable;
import java.util.Random;

public class Test {
    static double getDiscrepancy=0;
    static double putDiscrepancy=0;
    static double removeDiscrepancy=0;


    public static void HashTablePerformance(String[] args){
        // create a HashMap with a large initial capacity to avoid resizing during the test
        HashTable<Integer, Integer> map = new HashTable<>(1000000);

        // populate the map with random key-value pairs
        Random rand = new Random();
        long startTime1 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.put(rand.nextInt(1000000), rand.nextInt(1000000));
        }
        long endTime1 = System.nanoTime();
        putDiscrepancy-=(endTime1 - startTime1) / 1000000.0;
        System.out.println("Total Time taken(HashTable put function): " + (endTime1 - startTime1) / 1000000.0 + " milliseconds");

        // test the performance of the get function
        long startTime2 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.get(rand.nextInt(1000000));
        }
        long endTime2 = System.nanoTime();
        getDiscrepancy-=(endTime2 - startTime2) / 1000000.0;
        System.out.println("Total Time taken(HashTable get function): " + (endTime2 - startTime2) / 1000000.0 + " milliseconds");

        long startTime3 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.remove(rand.nextInt(1000000));
        }
        long endTime3 = System.nanoTime();
        removeDiscrepancy-=(endTime3 - startTime3) / 1000000.0;
        System.out.println("Total Time taken(HashTable remove function): " + (endTime3 - startTime3) / 1000000.0 + " milliseconds");
    }
    public static void RBTablePerformance(String[] args){
        // create a HashMap with a large initial capacity to avoid resizing during the test
        ThreadSafeRedBlackTreeHashTable<Integer, Integer> map = new ThreadSafeRedBlackTreeHashTable<>(1000000);

        // populate the map with random key-value pairs
        Random rand = new Random();
        long startTime1 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.put(rand.nextInt(1000000), rand.nextInt(1000000));
        }
        long endTime1 = System.nanoTime();
        putDiscrepancy+=(endTime1 - startTime1) / 1000000.0;
        System.out.println("Total Time taken(RBTable put function): " + (endTime1 - startTime1) / 1000000.0 + " milliseconds");

        // test the performance of the get function
        long startTime2 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.get(rand.nextInt(1000000));
        }
        long endTime2 = System.nanoTime();
        getDiscrepancy+=(endTime2 - startTime2) / 1000000.0;
        System.out.println("Total Time taken(RBTable get function): " + (endTime2 - startTime2) / 1000000.0 + " milliseconds");

        long startTime3 = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            map.remove(rand.nextInt(1000000));
        }
        long endTime3 = System.nanoTime();
        removeDiscrepancy+=(endTime3 - startTime3) / 1000000.0;
        System.out.println("Total Time taken(RBTable remove function): " + (endTime3 - startTime3) / 1000000.0 + " milliseconds");
    }
    public static void ThreadSafetyPerformance(){
        final int THREAD_COUNT = 100;
        final int TABLE_SIZE = 1000000;

        // create a HashMap
        final ThreadSafeRedBlackTreeHashTable<Integer, Integer> map = new ThreadSafeRedBlackTreeHashTable<>(TABLE_SIZE);

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
        System.out.println("ideal Map size: " + TABLE_SIZE);
        System.out.println("actual Map size: " + map.getSize());
        System.out.println("safety loss : " + (TABLE_SIZE-map.getSize()));
        if(map.getSize()==TABLE_SIZE){
            System.out.println("The Table is Thread-safe!");
        }
    }
    public static void main(String[] args) {
        final int TEST_ROUND=20;

        for(int i=0;i<TEST_ROUND;i++){
            System.out.println("*****************************The "+(i+1)+"th round of Testing:**********************************");
            System.out.println("****************************************************************************************");
            System.out.println();
            HashTablePerformance(args);
            RBTablePerformance(args);
            System.out.println();
        }
        System.out.println("******************************Total Discrepancy:**********************************");
        System.out.println("Average Discrepancy of Get() function: " + getDiscrepancy/TEST_ROUND+ " milliseconds");
        System.out.println("Average Discrepancy of Put() function: " + putDiscrepancy/TEST_ROUND+ " milliseconds");
        System.out.println("Average Discrepancy of Remove() function: " + removeDiscrepancy/TEST_ROUND+ " milliseconds");
        System.out.println();
        System.out.println("******************************Testing the Thread Safety::**********************************");
        ThreadSafetyPerformance();
    }
}

