package gregtech.api.util;

import gregtech.api.interfaces.ICleanroom;

/** "Cleanroom" Bypass for Dev Work. */
public final class FakeCleanroom implements ICleanroom {

    public static final FakeCleanroom INSTANCE = new FakeCleanroom();

    private FakeCleanroom() {} // Defensive

    public static volatile boolean CLEANROOM_BYPASS = false;
    public static volatile boolean LOWGRAV_BYPASS = false;

    public static boolean isCleanroomBypassEnabled() {
        return CLEANROOM_BYPASS;
    }

    public static boolean isLowGravBypassEnabled() {
        return LOWGRAV_BYPASS;
    }

    @Override
    public int getCleanness() {
        return 10000;
    }

    @Override
    public boolean isValidCleanroom() {
        return true;
    }

    @Override
    public void pollute() {}
}
