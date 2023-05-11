package net.glease.ggfab.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OverclockHelperTest {

    @Test
    void normalOverclockImperfect() {
        // fails recipe
        assertNull(OverclockHelper.normalOverclock(10000, 10000, 1, false));
        // no overclock
        assertEquals(new OverclockHelper.OverclockOutput(30, 64), OverclockHelper.normalOverclock(30, 64, 32, false));
        // imperfect overclock
        assertEquals(new OverclockHelper.OverclockOutput(120, 32), OverclockHelper.normalOverclock(30, 64, 128, false));
        // lots of overclock
        assertEquals(
                new OverclockHelper.OverclockOutput(30720, 2),
                OverclockHelper.normalOverclock(30, 64, 32768, false));
        // do not overclock beyond useful
        assertEquals(
                new OverclockHelper.OverclockOutput(122880, 1),
                OverclockHelper.normalOverclock(30, 64, 524288, false));
    }

    @Test
    void laserOverclock() {
        // fails recipe
        assertNull(OverclockHelper.laserOverclock(10000, 10000, 1, 5));
        // no overclock
        assertEquals(new OverclockHelper.OverclockOutput(30, 64), OverclockHelper.laserOverclock(30, 64, 32, 0.5f));
        // 0.3 amp overclock. 0.25 amp would be not in current tier so no point in testing that
        assertEquals(new OverclockHelper.OverclockOutput(10, 64), OverclockHelper.laserOverclock(10, 64, 32, 0.5f));
        // laser overclock
        assertEquals(
                new OverclockHelper.OverclockOutput(135, 32),
                OverclockHelper.laserOverclock(30, 64, 32 * 16, 0.5f));
        // lots of overclock
        assertEquals(
                new OverclockHelper.OverclockOutput(22272, 4),
                OverclockHelper.laserOverclock(30, 64, 32 * 1024, 0.5f));
        // do not overclock beyond useful
        assertEquals(new OverclockHelper.OverclockOutput(135, 1), OverclockHelper.laserOverclock(30, 2, 32 * 16, 0.5f));
    }
}
