package gtPlusPlus.xmod.forestry.bees.registry;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IClassification;
import forestry.apiculture.genetics.alleles.AlleleEffect;
import forestry.core.genetics.alleles.AlleleHelper;

import java.util.Arrays;
import java.util.function.Consumer;

import static forestry.api.apiculture.EnumBeeChromosome.*;
import static forestry.core.genetics.alleles.EnumAllele.*;
import static gtPlusPlus.xmod.forestry.bees.registry.GTPP_BeeDefinition.getEffect;
import static gtPlusPlus.xmod.forestry.bees.registry.GTPP_BeeDefinition.getFlowers;
import static gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees.EXTRABEES;

public enum GTPP_BranchDefinition {

    HEE("Finis Expansiones", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.BOTH_2);
        AlleleHelper.instance.set(alleles, HUMIDITY_TOLERANCE, Tolerance.BOTH_2);
        AlleleHelper.instance.set(alleles, NOCTURNAL, true);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, getFlowers(EXTRABEES, "book"));
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.SHORT);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.SLOW);
        AlleleHelper.instance.set(alleles, SPEED, Speed.FASTEST);
        AlleleHelper.instance.set(alleles, TERRITORY, Territory.LARGER);
    }
    ),
    SPACE("Cosmicis", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.DOWN_2);
        AlleleHelper.instance.set(alleles, TOLERANT_FLYER, true);
        AlleleHelper.instance.set(alleles, NOCTURNAL, true);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.FASTEST);
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.LONGEST);
        AlleleHelper.instance.set(alleles, SPEED, Speed.FAST);
        AlleleHelper.instance.set(alleles, TERRITORY, Territory.LARGEST);
    }
    ),
    ;

    private static IAllele[] defaultTemplate;
    private final IClassification branch;
    private final Consumer<IAllele[]> mBranchProperties;

    GTPP_BranchDefinition(String scientific, Consumer<IAllele[]> aBranchProperties) {
        this.branch = BeeManager.beeFactory.createBranch(this.name().toLowerCase(), scientific);
        this.mBranchProperties = aBranchProperties;
    }

    private static IAllele[] getDefaultTemplate() {
        if (defaultTemplate == null) {
            defaultTemplate = new IAllele[EnumBeeChromosome.values().length];

            AlleleHelper.instance.set(defaultTemplate, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(defaultTemplate, LIFESPAN, Lifespan.SHORTER);
            AlleleHelper.instance.set(defaultTemplate, FERTILITY, Fertility.NORMAL);
            AlleleHelper.instance.set(defaultTemplate, TEMPERATURE_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(defaultTemplate, NOCTURNAL, false);
            AlleleHelper.instance.set(defaultTemplate, HUMIDITY_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(defaultTemplate, TOLERANT_FLYER, false);
            AlleleHelper.instance.set(defaultTemplate, CAVE_DWELLING, false);
            AlleleHelper.instance.set(defaultTemplate, FLOWER_PROVIDER, Flowers.VANILLA);
            AlleleHelper.instance.set(defaultTemplate, FLOWERING, Flowering.SLOWEST);
            AlleleHelper.instance.set(defaultTemplate, TERRITORY, Territory.AVERAGE);
            AlleleHelper.instance.set(defaultTemplate, EFFECT, AlleleEffect.effectNone);
        }
        return Arrays.copyOf(defaultTemplate, defaultTemplate.length);
    }

    protected final void setBranchProperties(IAllele[] template) {
        this.mBranchProperties.accept(template);
    }

    public final IAllele[] getTemplate() {
        IAllele[] template = getDefaultTemplate();
        setBranchProperties(template);
        return template;
    }

    public final IClassification getBranch() {
        return branch;
    }
}
