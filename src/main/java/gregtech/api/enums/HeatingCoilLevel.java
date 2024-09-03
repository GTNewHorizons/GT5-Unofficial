package gregtech.api.enums;

import javax.annotation.Nonnull;

import net.minecraft.util.StatCollector;

public enum HeatingCoilLevel {

    None, // 0
    ULV, // Not implemented 901
    LV, // Cupronickel 1801
    MV, // KANTHAL 2701
    HV, // NICHROME 3601
    EV, // TPVALLOY 4501
    IV, // HSSG 5401
    LuV, // HSSS 6301
    ZPM, // NAQUADAH 7201
    UV, // NAQUADAHALLOY 8101
    UHV, // TRINIUM 9001
    UEV, // ELECTRUMFLUX 9901
    UIV, // AWAKENEDDRACONIUM 10801
    UMV, // INFINITY 11701
    UXV, // HYPOGEN 12601
    MAX, // ETERNAL 13501
    ;

    private static final HeatingCoilLevel[] VALUES = values();

    /**
     * @return the coil heat, used for recipes in the Electronic Blast Furnace for example.
     */
    public long getHeat() {
        return this == None ? 0 : 1L + (900L * this.ordinal());
    }

    /**
     * @return the coil tier, used for discount in the Pyrolyse Oven for example. LV == 0
     */
    public byte getTier() {
        return (byte) (this.ordinal() - 2);
    }

    /**
     * @return the coil Level, used for Parallels in the Multi Furnace for example.
     */
    public byte getLevel() {
        return (byte) (1 << Math.min(Math.max(0, this.ordinal() - 2), 4));
    }

    /**
     * @return the coil Discount, used for discount in the Multi Furnace for example
     */
    public int getCostDiscount() {
        return 1 << Math.max(0, this.ordinal() - 5);
    }

    /**
     * @return Translated name of this coil
     */
    public String getName() {
        return StatCollector.translateToLocal("GT5U.coil." + this);
    }

    @Nonnull
    public static HeatingCoilLevel getFromTier(byte tier) {
        if (tier < 0 || tier > getMaxTier()) return HeatingCoilLevel.None;

        return VALUES[tier + 2];
    }

    /**
     * @param applyColor Whether to apply tiered color
     * @return Translated coil name. Heat exceeding MAX is represented as "Eternal+".
     */
    @Nonnull
    public static String getDisplayNameFromHeat(int heat, boolean applyColor) {
        for (HeatingCoilLevel heatLevel : VALUES) {
            if (heatLevel == HeatingCoilLevel.None || heatLevel == HeatingCoilLevel.ULV) continue;
            if (heatLevel.getHeat() >= heat) {
                String name = heatLevel.getName();
                if (applyColor) {
                    name = GTValues.TIER_COLORS[heatLevel.getTier() + 1] + name;
                }
                return name;
            }
        }
        String name = HeatingCoilLevel.MAX.getName() + "+";
        if (applyColor) {
            name = GTValues.TIER_COLORS[HeatingCoilLevel.MAX.getTier() + 1] + name;
        }
        return name;
    }

    public static int size() {
        return VALUES.length;
    }

    public static int getMaxTier() {
        return VALUES.length - 1 - 2;
    }
}
