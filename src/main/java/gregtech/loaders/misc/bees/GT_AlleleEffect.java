package gregtech.loaders.misc.bees;

import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeEffect;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IEffectData;
import forestry.core.genetics.alleles.Allele;

// helper class for implementing custom bee effects, based on MagicBees' implementation
public abstract class GT_AlleleEffect extends Allele implements IAlleleBeeEffect {

    public static final IAlleleBeeEffect FORESTRY_BASE_EFFECT = (IAlleleBeeEffect) AlleleManager.alleleRegistry
        .getAllele("forestry.effectNone");
    protected boolean combinable;
    protected int tickThrottle; // If set, this amount of ticks have to pass before an effect ticks

    public GT_AlleleEffect(String id, boolean isDominant, int tickThrottle) {
        super("gregtech." + id, "gregtech." + id, isDominant);
        AlleleManager.alleleRegistry.registerAllele(this, EnumBeeChromosome.EFFECT);
        combinable = false;
        this.tickThrottle = tickThrottle;
    }

    @Override
    public boolean isCombinable() {
        return combinable;
    }

    public GT_AlleleEffect setIsCombinable(boolean canCombine) {
        combinable = canCombine;
        return this;
    }

    @Override
    public abstract IEffectData validateStorage(IEffectData storedData);

    @Override
    public IEffectData doEffect(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        int ticksPassed = storedData.getInteger(0);
        if (ticksPassed >= this.tickThrottle) {
            storedData = this.doEffectTickThrottled(genome, storedData, housing);
        } else {
            storedData.setInteger(0, ticksPassed + 1);
        }
        return storedData;
    }

    protected abstract IEffectData doEffectTickThrottled(IBeeGenome genome, IEffectData storedData,
        IBeeHousing housing);

    @Override
    public IEffectData doFX(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        return FORESTRY_BASE_EFFECT.doFX(genome, storedData, housing);
    }
}
