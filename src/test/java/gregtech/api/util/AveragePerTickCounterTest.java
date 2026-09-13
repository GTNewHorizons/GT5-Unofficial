package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.InvalidParameterException;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.TickTime;

/**
 * Covers the counter backing the cable throughput readout. The tick source is injected so no server is needed.
 */
class AveragePerTickCounterTest {

    /** Mutable stand-in for {@code MinecraftServer.getTickCounter()}. Starts at 1: tick 0 is the empty-slot marker. */
    private int tick = 1;

    private AveragePerTickCounter counter(int period) {
        return new AveragePerTickCounter(period, () -> tick);
    }

    @Test
    void rejectsNonPositivePeriod() {
        assertThrows(InvalidParameterException.class, () -> new AveragePerTickCounter(0, () -> tick));
        assertThrows(InvalidParameterException.class, () -> new AveragePerTickCounter(-1, () -> tick));
    }

    @Test
    void addValueSumsWithinATick() {
        AveragePerTickCounter c = counter(TickTime.SECOND);

        c.addValue(12);
        c.addValue(12);
        c.addValue(11);

        tick++;
        assertEquals(35, c.getLast());
    }

    @Test
    void addMaxValueKeepsTheHighestOfTheTick() {
        AveragePerTickCounter c = counter(TickTime.SECOND);

        c.addMaxValue(8192);
        c.addMaxValue(8192);
        c.addMaxValue(2048);

        tick++;
        // a voltage does not add up when several packets go through in the same tick
        assertEquals(8192, c.getLast());
    }

    @Test
    void getLastOnlyReturnsThePreviousTick() {
        AveragePerTickCounter c = counter(TickTime.SECOND);

        c.addValue(100);
        // still the current tick, nothing completed yet
        assertEquals(0, c.getLast());

        tick++;
        assertEquals(100, c.getLast());

        // an idle tick reports zero rather than a stale value
        tick++;
        assertEquals(0, c.getLast());
    }

    @Test
    void averageDividesByThePeriod() {
        AveragePerTickCounter c = counter(TickTime.SECOND);

        // 20 ticks at 40 each
        for (int i = 0; i < TickTime.SECOND; i++) {
            c.addValue(40);
            tick++;
        }

        assertEquals(40.0, c.getAverage(), 1e-9);
    }

    @Test
    void averageDropsValuesOutOfTheWindow() {
        AveragePerTickCounter c = counter(TickTime.SECOND);

        c.addValue(1000);
        tick += TickTime.SECOND + 1;

        assertEquals(0.0, c.getAverage(), 1e-9);
    }

    /**
     * Regression for the cable readout: energy accumulates per packet, voltage does not. A tick carrying 35 A split
     * into three packets at 8192 V must read 286,720 EU/t at 8192 V - not 3 x 8192.
     */
    @Test
    void cableThroughputOfSplitPacketsIsAmpsTimesVoltage() {
        final long voltage = 8192;
        AveragePerTickCounter amps = counter(TickTime.SECOND);
        AveragePerTickCounter volts = counter(TickTime.SECOND);
        AveragePerTickCounter energy = counter(TickTime.SECOND);

        for (long packet : new long[] { 12, 12, 11 }) {
            amps.addValue(packet);
            volts.addMaxValue(voltage);
            energy.addValue(packet * voltage);
        }

        tick++;
        assertEquals(35, amps.getLast());
        assertEquals(8192, volts.getLast());
        assertEquals(286_720, energy.getLast());
        assertEquals(amps.getLast() * volts.getLast(), energy.getLast());
    }
}
