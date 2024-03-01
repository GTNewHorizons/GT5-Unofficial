package gregtech.api.multitileentity.enums;

public enum PartMode {

    NOTHING,
    ITEM_INPUT,
    ITEM_OUTPUT,
    FLUID_INPUT,
    FLUID_OUTPUT,
    ENERGY_INPUT,
    ENERGY_OUTPUT;

    private final int val;

    PartMode() {
        val = 1 << ordinal();
    }

    public int getValue() {
        return val;
    }
}
