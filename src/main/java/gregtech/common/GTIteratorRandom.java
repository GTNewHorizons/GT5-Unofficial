package gregtech.common;

import java.util.Random;

public class GTIteratorRandom extends Random {

    private static final long serialVersionUID = 1L;
    public int mIterationStep = 2147483647;

    @Override
    public int nextInt(int aParameter) {
        if ((this.mIterationStep == 0) || (this.mIterationStep > aParameter)) {
            this.mIterationStep = aParameter;
        }
        return --this.mIterationStep;
    }
}
