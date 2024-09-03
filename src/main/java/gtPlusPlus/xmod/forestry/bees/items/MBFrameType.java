package gtPlusPlus.xmod.forestry.bees.items;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeModifier;

public enum MBFrameType implements IBeeModifier {

    // ExtraBees Clone Frames
    // Name, FrameHP, territory (1f), Mutation rate, lifespan rate, production rate, genetic decay (1f)
    COCOA("Chocolate", 240, 1.0f, 1.0f, 0.50f, 0.50f, 1f), // production was 1.5x, now +0.5
    CAGE("Restraint", 240, 0.5f, 1.0f, 0.75f, -0.25f, 1f), // production was 0.75x, now -0.25
    SOUL("Soul", 80, 1.0f, 1.5f, 0.75f, -0.75f, 1f), // production was 0.25x, now -0.75
    CLAY("Healing", 240, 1.0f, 0.5f, 1.50f, -0.25f, 1f), // production was 0.75x, now -0.25
    NOVA("Nova", 240, 1.0f, 100.0f, 0.0001f, 0.00f, 1f), // production was 1x, now +0

    // Name, FrameHP, territory (1f), Mutation rate, lifespan rate, production rate, genetic decay (1f)
    ACCELERATED("Accelerated", 175, 1f, 1.2f, 0.9f, 0.8f, 1f), // production was 1.8x, now +0.8
    VOID("Void", 20, 1f, 1f, 0.0001f, 9f, 1f), // production was 10x, now +9
    MUTAGENIC("Mutagenic", 3, 1f, 5f, 0.0001f, 9f, 1f), // production was 10x, now +9
    BUSY("Busy", 2000, 1f, 0f, 3f, 3f, 1f), // production was 4x, now +3
    USELESS("Useless", 100, 1f, 0f, 1f, 0f, 1f), // production was 1x, now +0

    // Frame Items added by bartimaeusnek
    DECAYING("Decaying", 240, 1f, 1f, 1f, 0f, 10f), // enhanches decay to 10x // production was 1x, now +0
    SLOWING("Slowing", 175, 1f, 0.5f, 2f, -0.5f, 1f), // reduces mutation, production rate and enhanches lifespan //
                                                      // production was 0.5x, now -0.5
    STABILIZING("Stabilizing", 60, 1f, 0.1f, 1f, -0.9f, 0.5f), // reduces mutation, production and decay // production
                                                               // was 0.1x, now -0.9
    ARBORISTS("Arborists", 240, 3f, 0f, 3f, -9001f, 1f); // 3x territory and lifespan, sets mutation and production to
                                                         // zero // production was 0x, now -9001

    private final String frameName;
    public final int maxDamage;

    private final float territoryMod;
    private final float mutationMod;
    private final float lifespanMod;
    private final float productionMod;
    private final float floweringMod;
    private final float geneticDecayMod;
    private final boolean isSealed;
    private final boolean isLit;
    private final boolean isSunlit;
    private final boolean isHellish;

    MBFrameType(final String name, final int damage, final float territory, final float mutation, final float lifespan,
        final float production, final float geneticDecay) {
        this(name, damage, territory, mutation, lifespan, production, 1f, geneticDecay, false, false, false, false);
    }

    MBFrameType(final String name, final int damage, final float territory, final float mutation, final float lifespan,
        final float production, final float flowering, final float geneticDecay, final boolean sealed,
        final boolean lit, final boolean sunlit, final boolean hellish) {
        this.frameName = name;
        this.maxDamage = damage;

        this.territoryMod = territory;
        this.mutationMod = mutation;
        this.lifespanMod = lifespan;
        this.productionMod = production;
        this.floweringMod = flowering;
        this.geneticDecayMod = geneticDecay;
        this.isSealed = sealed;
        this.isLit = lit;
        this.isSunlit = sunlit;
        this.isHellish = hellish;
    }

    public String getName() {
        return this.frameName;
    }

    public String getLocalizedName() {
        return FRStringUtil.getLocalizedString("frame." + this.frameName);
    }

    @Override
    public float getTerritoryModifier(final IBeeGenome genome, final float currentModifier) {
        return this.territoryMod;
    }

    @Override
    public float getMutationModifier(final IBeeGenome genome, final IBeeGenome mate, final float currentModifier) {
        return this.mutationMod;
    }

    @Override
    public float getLifespanModifier(final IBeeGenome genome, final IBeeGenome mate, final float currentModifier) {
        return this.lifespanMod;
    }

    @Override
    public float getProductionModifier(final IBeeGenome genome, final float currentModifier) {
        return this.productionMod;
    }

    @Override
    public float getFloweringModifier(final IBeeGenome genome, final float currentModifier) {
        return this.floweringMod;
    }

    @Override
    public float getGeneticDecay(final IBeeGenome genome, final float currentModifier) {
        return this.geneticDecayMod;
    }

    @Override
    public boolean isSealed() {
        return this.isSealed;
    }

    @Override
    public boolean isSelfLighted() {
        return this.isLit;
    }

    @Override
    public boolean isSunlightSimulated() {
        return this.isSunlit;
    }

    @Override
    public boolean isHellish() {
        return this.isHellish;
    }
}
