package gregtech.loaders.misc;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IClassification;
import forestry.apiculture.genetics.alleles.AlleleEffect;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.EnumAllele;

import java.util.Arrays;
import java.util.function.Consumer;

import static gregtech.loaders.misc.GT_BeeDefinition.getEffect;
import static gregtech.loaders.misc.GT_BeeDefinition.getFlowers;
import static gregtech.loaders.misc.GT_BeeDefinitionReference.EXTRABEES;

public enum GT_BranchDefinition {

    ORGANIC("Fuelis", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_2);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.WHEAT);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOW);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
    }
    ),
    IC2("Industrialis", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.UP_1);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.SNOW);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FASTER);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOW);
    }
    ),
    GTALLOY("Amalgamis", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TOLERANT_FLYER, true);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.VANILLA);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.AVERAGE);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
    }
    ),
    THAUMIC("Arcanis", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, getFlowers(EXTRABEES, "book"));
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FASTER);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGEST);
    }
    ),
    GEM("Ornamentis", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.NETHER);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.AVERAGE);
    }
    ),
    METAL("Metaliferis", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.DOWN_2);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.CAVE_DWELLING, true);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.JUNGLE);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWER);
    }
    ),
    RAREMETAL("Mineralis", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.DOWN_1);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.CACTI);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FAST);
    }
    ),
    RADIOACTIVE("Criticalis", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.END);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.AVERAGE);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.SPEED, GT_Bees.speedBlinding);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.SPEED, getEffect(EXTRABEES, "radioactive"));
    }
    ),
    TWILIGHT("Nemoris Obscuri", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.VANILLA);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FASTER);
    }
    ),
    HEE("Finis Expansiones", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_2);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_2);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.NOCTURNAL, true);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, getFlowers(EXTRABEES, "book"));
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOW);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.SPEED, EnumAllele.Speed.FASTEST);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.LARGER);
    }
    ),
    SPACE("Cosmicis", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.DOWN_2);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TOLERANT_FLYER, true);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.NOCTURNAL, true);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FASTEST);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGEST);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.LARGEST);
    }
    ),
    PLANET("Planetaris", alleles -> {
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TOLERANT_FLYER, true);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FASTEST);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.NORMAL);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.SPEED, EnumAllele.Speed.FASTEST);
        AlleleHelper.instance.set(alleles, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.LARGER);
    }
    ),
    ;

    private static IAllele[] defaultTemplate;
    private final IClassification branch;
    private final Consumer<IAllele[]> mBranchProperties;

    GT_BranchDefinition(String scientific, Consumer<IAllele[]> aBranchProperties) {
        this.branch = BeeManager.beeFactory.createBranch(this.name().toLowerCase(), scientific);
        this.mBranchProperties = aBranchProperties;
    }

    private static IAllele[] getDefaultTemplate() {
        if (defaultTemplate == null) {
            defaultTemplate = new IAllele[EnumBeeChromosome.values().length];

            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.FERTILITY, EnumAllele.Fertility.NORMAL);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.NOCTURNAL, false);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.TOLERANT_FLYER, false);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.CAVE_DWELLING, false);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.VANILLA);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWEST);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.AVERAGE);
            AlleleHelper.instance.set(defaultTemplate, EnumBeeChromosome.EFFECT, AlleleEffect.effectNone);
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