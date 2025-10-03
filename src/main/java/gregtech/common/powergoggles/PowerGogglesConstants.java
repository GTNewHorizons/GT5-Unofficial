package gregtech.common.powergoggles;

public class PowerGogglesConstants {

    private static final int TICKS = 1;
    private static final int SECONDS = 20 * TICKS;
    private static final int MINUTES = 60 * SECONDS;
    private static final int HOURS = 60 * MINUTES;

    public static final int TICKS_BETWEEN_MEASUREMENTS = 100;
    public static final int STORED_MEASUREMENTS = (int) Math.ceil((1.0 * HOURS) / TICKS_BETWEEN_MEASUREMENTS);
}
