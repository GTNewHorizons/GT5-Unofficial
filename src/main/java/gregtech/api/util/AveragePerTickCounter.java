package gregtech.api.util;

import java.security.InvalidParameterException;

import net.minecraft.server.MinecraftServer;

public class AveragePerTickCounter {

    /**
     * Averages a value over a certain amount of ticks
     *
     * @param period amount of ticks to average (20 for 1 second)
     *
     */
    public AveragePerTickCounter(int period) throws InvalidParameterException {

        if (period <= 0) throw new InvalidParameterException("period should be a positive non-zero number");

        this.period = period;
        int size = period + 1;
        this.values = new long[size];
        this.timestamps = new int[size];
    }

    public void addValue(long value) {
        if (value == 0) return;

        final int currTick = getWorldTimeInTicks();

        /// sums up values added in the same tick
        /// for example a cable had an amp running through it multiple times in the same tick
        if (currTick == timestamps[currIndex]) {
            values[currIndex] += value;
        } else if (currTick > timestamps[currIndex]) {
            currIndex = (currIndex + 1) % values.length;
            values[currIndex] = value;
            timestamps[currIndex] = currTick;
        }
    }

    public double getAverage() {
        final int currTick = getWorldTimeInTicks();
        if (lastCacheTick == currTick) return cachedAverage;

        if (currTick < timestamps[currIndex]) return 0;

        return calculateAverage();
    }

    public long getLast() {
        final int targetTick = getWorldTimeInTicks() - 1;
        if (timestamps[currIndex] == targetTick) return values[currIndex];

        int prevIndex = (currIndex - 1 + values.length) % values.length;
        if (timestamps[prevIndex] == targetTick) return values[prevIndex];

        return 0;
    }

    private double calculateAverage() {
        long sum = 0;
        int currTick = getWorldTimeInTicks();
        int minTick = currTick - period;
        for (int i = 0; i < values.length; i++) {
            if (timestamps[i] >= minTick && timestamps[i] < currTick) sum += values[i];
        }
        cachedAverage = sum / (double) period;
        lastCacheTick = currTick;
        return cachedAverage;
    }

    private int getWorldTimeInTicks() {
        return MinecraftServer.getServer()
            .getTickCounter();
    }

    private final long[] values;
    private final int[] timestamps;
    private int currIndex = 0;
    private final int period;

    private double cachedAverage = 0;
    private int lastCacheTick = 0;
}
