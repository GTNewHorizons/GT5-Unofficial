package gregtech.api.util;

import java.security.InvalidParameterException;
import java.util.ArrayDeque;

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
        values = new ArrayDeque<>(period);
    }

    public void AddValue(long value) {

        if (value == 0) return;

        final int currTick = GetWorldTimeInTicks();

        if (values.isEmpty()) {
            values.addLast(new Measurement(currTick, value));
            isCachedAverageValid = false;
            return;
        }

        Measurement lastMeasurement = values.peekLast();
        final int lastMeasurementTick = lastMeasurement.TimestampInWorldTicks;

        if (currTick == lastMeasurementTick) {
            lastMeasurement.Value = lastMeasurement.Value + value;
            isCachedAverageValid = false;
            return;
        }

        if (currTick > lastMeasurementTick) {
            TrimIrrelevantData(currTick);

            values.addLast(new Measurement(currTick, value));
            isCachedAverageValid = false;
            return;
        }
    }

    public double GetAverage() {

        if (values.isEmpty()) return 0;

        final int currTick = GetWorldTimeInTicks();

        Measurement lastMeasurement = values.peekLast();
        final int lastMeasurementTick = lastMeasurement.TimestampInWorldTicks;

        if (currTick < lastMeasurementTick) return 0;

        if (currTick > lastMeasurementTick) {
            TrimIrrelevantData(currTick);
        }

        if (isCachedAverageValid) return cachedAverage;

        return CalculateAverage();
    }

    public long GetLast() {

        if (values.isEmpty()) return 0;

        final int currTick = GetWorldTimeInTicks();

        Measurement lastMeasurement = values.peekLast();
        final int lastMeasurementTick = lastMeasurement.TimestampInWorldTicks;

        if (currTick == lastMeasurementTick) return values.getLast().Value;

        return 0;
    }

    private double CalculateAverage() {

        isCachedAverageValid = true;
        long sum = 0;

        for (Measurement measurement : values) {
            sum += measurement.Value;
        }

        return sum / (double) period;
    }

    private void TrimIrrelevantData(int currWorldTimeInTicks) {

        if (values.isEmpty()) return;

        int firstMeasurementTick = values.peekFirst().TimestampInWorldTicks;

        while (currWorldTimeInTicks - firstMeasurementTick >= period) {
            values.removeFirst();
            isCachedAverageValid = false;

            if (values.isEmpty()) return;

            firstMeasurementTick = values.peekFirst().TimestampInWorldTicks;
        }
    }

    private int GetWorldTimeInTicks() {
        return MinecraftServer.getServer()
            .getTickCounter();
    }

    private ArrayDeque<Measurement> values;
    private int period;

    private double cachedAverage = 0;
    private boolean isCachedAverageValid = true;

    private class Measurement {

        public int TimestampInWorldTicks;
        public long Value;

        public Measurement(int timestampInWorldTicks, long value) {
            this.TimestampInWorldTicks = timestampInWorldTicks;
            this.Value = value;
        }
    }
}
