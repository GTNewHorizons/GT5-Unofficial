package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import gregtech.api.enums.HeatingCoilLevel;

public enum CompressedSpacetimeCoilLevel {
    // Volume in cubic Astronomical Units.
    None,
    AU_1,
    AU_10,
    AU_100,
    AU_1_000,
    AU_10_000,
    AU_100_000,
    AU_1_000_000
    ;


    private static final CompressedSpacetimeCoilLevel[] VALUES = values();

    /**
     * @return the coil heat, used for recipes in the Electronic Blast Furnace for example.
     */
    public long getCompressedVolume() {
        return this == None ? 0 : 1L + (900L * this.ordinal());
    }

    /**
     * @return the coil tier, used for discount in the Pyrolyse Oven for example.
     */
    public long getTier() {
        return (byte) (this.ordinal() - 2);
    }

    public static int size() {
        return VALUES.length;
    }

    public static int getMaxTier() {
        return VALUES.length - 1;
    }

    public static CompressedSpacetimeCoilLevel getFromTier(int tier){
        if (tier < 0 || tier > getMaxTier())
            return CompressedSpacetimeCoilLevel.None;

        return VALUES[tier+2];
    }

}
