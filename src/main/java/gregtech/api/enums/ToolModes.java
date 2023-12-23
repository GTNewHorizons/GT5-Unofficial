package gregtech.api.enums;

public enum ToolModes {

    REGULAR(0),
    WRENCH_LINE(1);

    private final byte modeValue;

    ToolModes(byte modeValue) {
        this.modeValue = modeValue;
    }

    public byte get() {
        return modeValue;
    }

}
