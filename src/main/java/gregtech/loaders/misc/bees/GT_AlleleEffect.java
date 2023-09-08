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

    public static IAlleleBeeEffect forestryBaseEffect = (IAlleleBeeEffect) AlleleManager.alleleRegistry
        .getAllele("forestry.effectNone");
    protected boolean combinable;

    public GT_AlleleEffect(String id, boolean isDominant) {
        super("gregtech." + id, "gregtech." + id, isDominant);
        AlleleManager.alleleRegistry.registerAllele(this, EnumBeeChromosome.EFFECT);
        combinable = false;
    }

    @Override
    public boolean isCombinable() {
        return combinable;
    }

    // Not used by treetwisterEffect, but may be used by other custom effects in the future
    @SuppressWarnings("unused")
    public GT_AlleleEffect setIsCombinable(boolean canCombine) {
        combinable = canCombine;
        return this;
    }

    @Override
    public abstract IEffectData validateStorage(IEffectData storedData);

    @Override
    public abstract IEffectData doEffect(IBeeGenome genome, IEffectData storedData, IBeeHousing housing);

    @Override
    public IEffectData doFX(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        return forestryBaseEffect.doFX(genome, storedData, housing);
    }
}
