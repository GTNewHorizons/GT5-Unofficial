package gregtech.api.objects.blockupdate;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

public class RandomCooldown extends Cooldown {

    public RandomCooldown(int aMinLengthInTicks, int aMaxLengthInTicks) {

        super(aMinLengthInTicks);

        if (aMinLengthInTicks <= 0)
            throw new IllegalArgumentException("min length should be a positive non-zero number");
        if (aMaxLengthInTicks <= 0)
            throw new IllegalArgumentException("max length should be a positive non-zero number");
        if (aMinLengthInTicks > aMaxLengthInTicks)
            throw new IllegalArgumentException("min length should be less or equal to max length");

        this.minLengthInTicks = aMinLengthInTicks;
        this.maxLengthInTicks = aMaxLengthInTicks;
    }

    @Override
    public void set(long currTickTime) {

        super.set(currTickTime);
        lengthInTicks = minLengthInTicks + XSTR_INSTANCE.nextInt(maxLengthInTicks - minLengthInTicks + 1);
    }

    private int minLengthInTicks;
    private int maxLengthInTicks;
}
