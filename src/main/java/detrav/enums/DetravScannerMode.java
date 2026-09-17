package detrav.enums;

/**
 * The four things a Prospector's Scanner can be set to look for. Kept as the tool's mode in NBT, and sent to the
 * client in the prospecting packet so the map knows what it is drawing.
 */
public final class DetravScannerMode {

    public static final int BIG_ORES = 0;
    public static final int ALL_ORES = 1;
    public static final int FLUIDS = 2;
    public static final int POLLUTION = 3;

    /** How many modes there are, for the sneak-right-click cycle. */
    public static final int COUNT = 4;

    private DetravScannerMode() {}
}
