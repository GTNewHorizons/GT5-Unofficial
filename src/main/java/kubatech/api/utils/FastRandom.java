package kubatech.api.utils;

import java.util.Random;
import java.util.SplittableRandom;

public class FastRandom extends Random {

    private SplittableRandom realRandom;

    public FastRandom() {
        realRandom = new SplittableRandom();
    }

    public FastRandom(long seed) {
        realRandom = new SplittableRandom(seed);
    }

    @Override
    public synchronized void setSeed(long seed) {
        realRandom = new SplittableRandom(seed);
    }

    @Override
    protected int next(int bits) {
        return (realRandom.nextInt() >>> (32 - bits));
    }
}
