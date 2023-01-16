package gregtech.api.ModernMaterials.PartProperties;

public class BlockWirePartsProperties {
    private boolean hasWireFine;
    private boolean hasWire1x;
    private boolean hasWire2x;
    private boolean hasWire4x;
    private boolean hasWire8x;
    private boolean hasWire12x;
    private boolean hasWire16x;

    private boolean hasInsulatedWire1x;
    private boolean hasInsulatedWire2x;
    private boolean hasInsulatedWire4x;
    private boolean hasInsulatedWire8x;
    private boolean hasInsulatedWire12x;
    private boolean hasInsulatedWire16x;

    // Loss etc properties as well probably.


    public BlockWirePartsProperties(boolean initialState) {
        this.hasWireFine = initialState;

        this.hasWire1x = initialState;
        this.hasWire2x = initialState;
        this.hasWire4x = initialState;
        this.hasWire8x = initialState;
        this.hasWire12x = initialState;
        this.hasWire16x = initialState;

        this.hasInsulatedWire1x = initialState;
        this.hasInsulatedWire2x = initialState;
        this.hasInsulatedWire4x = initialState;
        this.hasInsulatedWire8x = initialState;
        this.hasInsulatedWire12x = initialState;
        this.hasInsulatedWire16x = initialState;
    }
}
