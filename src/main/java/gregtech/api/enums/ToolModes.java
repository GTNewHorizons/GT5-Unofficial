package gregtech.api.enums;

public enum ToolModes {

    REGULAR(0),
    WRENCH_LINE(1),
    WRENCH_PRECISE(2),
    WRENCH_OUTPUT_ITEM(3),
    WRENCH_OUTPUT_FLUID(4),
    WRENCH_OUTPUT_BOTH(5);

    private final byte modeValue;

    ToolModes(int modeValue) {
        this.modeValue = (byte) modeValue;
    }

    public byte get() {
        return modeValue;
    }

}
