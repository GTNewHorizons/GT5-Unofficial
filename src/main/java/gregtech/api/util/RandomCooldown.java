package gregtech.api.util;

import java.security.InvalidParameterException;
import java.util.Random;

public class RandomCooldown extends Cooldown {

    public RandomCooldown(int aMinLengthInTicks, int aMaxLengthInTicks) {

        super(aMinLengthInTicks);

        if (aMinLengthInTicks <= 0)
            throw new InvalidParameterException("min length should be a positive non-zero number");
        if (aMaxLengthInTicks <= 0)
            throw new InvalidParameterException("max length should be a positive non-zero number");
        if (aMinLengthInTicks > aMaxLengthInTicks)
            throw new InvalidParameterException("min length should be less or equal to max length");

        this.minLengthInTicks = aMinLengthInTicks;
        this.maxLengthInTicks = aMaxLengthInTicks;
    }

    @Override
    public void set() {

        super.set();
        lengthInTicks = minLengthInTicks + random.nextInt(maxLengthInTicks - minLengthInTicks + 1);
        // GT_Log.out.println(String.format("new random length %d", lengthInTicks));
    }

    private Random random = new Random();
    private int minLengthInTicks;
    private int maxLengthInTicks;
}
