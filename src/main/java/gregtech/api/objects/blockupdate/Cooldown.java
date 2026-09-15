package gregtech.api.objects.blockupdate;

public class Cooldown {

    public Cooldown(int aLengthInTicks) {

        if (aLengthInTicks <= 0) throw new IllegalArgumentException("length should be a positive non-zero number");

        this.lengthInTicks = aLengthInTicks;
        this.lastTimeStarted = 0;
    }

    public void set(long currTickTime) {
        lastTimeStarted = currTickTime;
    }

    public boolean hasPassed(long currTickTime) {
        return currTickTime - lastTimeStarted >= lengthInTicks;
    }

    public long getLastTimeStarted() {
        return lastTimeStarted;
    }

    private long lastTimeStarted;
    protected int lengthInTicks;
}
