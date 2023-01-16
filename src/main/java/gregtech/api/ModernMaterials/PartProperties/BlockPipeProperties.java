package gregtech.api.ModernMaterials.PartProperties;

public class BlockPipeProperties {

    private boolean tinyPipeExists;
    private boolean smallPipeExists;
    private boolean normalPipeExists;
    private boolean largePipeExists;
    private boolean hugePipeExists;

    private boolean quadruplePipeExists;
    private boolean nonuplePipeExists;

    public BlockPipeProperties(final boolean initialState) {
        this.tinyPipeExists = initialState;
        this.smallPipeExists = initialState;
        this.normalPipeExists = initialState;
        this.largePipeExists = initialState;
        this.hugePipeExists = initialState;

        this.quadruplePipeExists = initialState;
        this.nonuplePipeExists = initialState;
    }
}
