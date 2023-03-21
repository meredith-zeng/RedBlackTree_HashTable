package Performance;

import java.util.Random;

public class MathUtil {

    public static int getRandomOperation(Random random){
        int curRand = random.nextInt(100);
        int randomOperation;
        if(curRand > 0 && curRand < 70){
            // get
            randomOperation = 0;
        }else if(curRand >= 70 && curRand < 90){
            // put
            randomOperation = 1;
        }else{
            // delete
            randomOperation = 2;
        }
        return randomOperation;
    }
}