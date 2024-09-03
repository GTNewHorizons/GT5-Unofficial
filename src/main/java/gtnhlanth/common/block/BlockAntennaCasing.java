package gtnhlanth.common.block;

public class BlockAntennaCasing extends BlockCasing {

    private int antennaTier;

    public BlockAntennaCasing(int tier) {
        super("antenna_t" + tier);
        this.antennaTier = tier;
    }

    public int getTier() {
        return this.antennaTier;
    }
}
