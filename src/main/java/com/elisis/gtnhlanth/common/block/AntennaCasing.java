package com.elisis.gtnhlanth.common.block;

public class AntennaCasing extends Casing {

    private int antennaTier;

    public AntennaCasing(int tier) {
        super("antenna_t" + tier);
        this.antennaTier = tier;
    }

    public int getTier() {
        return this.antennaTier;
    }
}
