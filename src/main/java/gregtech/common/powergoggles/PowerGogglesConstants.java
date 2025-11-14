package gregtech.common.powergoggles;

public class PowerGogglesConstants {

    public static final int TICKS = 1;
    public static final int SECONDS = 20 * TICKS;
    public static final int MINUTES = 60 * SECONDS;
    public static final int HOURS = 60 * MINUTES;
    public static final int TICKS_BETWEEN_MEASUREMENTS = 100;

    public static final int STORED_MEASUREMENTS = (int) Math.ceil((1.0 * HOURS) / TICKS_BETWEEN_MEASUREMENTS);
    public static final int MEASUREMENT_COUNT_5M = (int) Math.ceil(5.0 * MINUTES / TICKS_BETWEEN_MEASUREMENTS);
    public static final int MEASUREMENT_COUNT_1H = (int) Math.ceil((1.0 * HOURS) / TICKS_BETWEEN_MEASUREMENTS);

}
