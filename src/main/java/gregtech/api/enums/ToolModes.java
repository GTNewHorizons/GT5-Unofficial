package gregtech.api.enums;

public enum ToolModes {

    REGULAR(0),
    WRENCH_LINE(1),
    WRENCH_PRECISION(2);

    private final byte modeValue;

    ToolModes(int modeValue) {
        this.modeValue = (byte) modeValue;
    }

    public byte get() {
        return modeValue;
    }

}
