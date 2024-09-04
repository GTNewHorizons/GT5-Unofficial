package gregtech.loaders.misc;

import static forestry.api.apiculture.EnumBeeChromosome.CAVE_DWELLING;
import static forestry.api.apiculture.EnumBeeChromosome.EFFECT;
import static forestry.api.apiculture.EnumBeeChromosome.FERTILITY;
import static forestry.api.apiculture.EnumBeeChromosome.FLOWERING;
import static forestry.api.apiculture.EnumBeeChromosome.FLOWER_PROVIDER;
import static forestry.api.apiculture.EnumBeeChromosome.HUMIDITY_TOLERANCE;
import static forestry.api.apiculture.EnumBeeChromosome.LIFESPAN;
import static forestry.api.apiculture.EnumBeeChromosome.NOCTURNAL;
import static forestry.api.apiculture.EnumBeeChromosome.SPEED;
import static forestry.api.apiculture.EnumBeeChromosome.TEMPERATURE_TOLERANCE;
import static forestry.api.apiculture.EnumBeeChromosome.TERRITORY;
import static forestry.api.apiculture.EnumBeeChromosome.TOLERANT_FLYER;
import static forestry.core.genetics.alleles.EnumAllele.Fertility;
import static forestry.core.genetics.alleles.EnumAllele.Flowering;
import static forestry.core.genetics.alleles.EnumAllele.Flowers;
import static forestry.core.genetics.alleles.EnumAllele.Lifespan;
import static forestry.core.genetics.alleles.EnumAllele.Speed;
import static forestry.core.genetics.alleles.EnumAllele.Territory;
import static forestry.core.genetics.alleles.EnumAllele.Tolerance;
import static gregtech.loaders.misc.GTBeeDefinition.getEffect;
import static gregtech.loaders.misc.GTBeeDefinition.getFlowers;
import static gregtech.loaders.misc.GTBeeDefinitionReference.EXTRABEES;
import static gregtech.loaders.misc.GTBeeDefinitionReference.MAGICBEES;

import java.util.Arrays;
import java.util.function.Consumer;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IClassification;
import forestry.apiculture.genetics.alleles.AlleleEffect;
import forestry.core.genetics.alleles.AlleleHelper;

public enum GTBranchDefinition {

    ORGANIC("Fuelis", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.NONE);
        AlleleHelper.instance.set(alleles, HUMIDITY_TOLERANCE, Tolerance.BOTH_2);
        AlleleHelper.instance.set(alleles, NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, Flowers.WHEAT);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.SLOW);
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.SHORTER);
        AlleleHelper.instance.set(alleles, SPEED, Speed.SLOWEST);
    }),
    IC2("Industrialis", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
        AlleleHelper.instance.set(alleles, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, Flowers.SNOW);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.FASTER);
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.SHORT);
        AlleleHelper.instance.set(alleles, SPEED, Speed.SLOW);
    }),
    GTALLOY("Amalgamis", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.NONE);
        AlleleHelper.instance.set(alleles, TOLERANT_FLYER, true);
        AlleleHelper.instance.set(alleles, NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, Flowers.VANILLA);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.AVERAGE);
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.SHORTEST);
        AlleleHelper.instance.set(alleles, SPEED, Speed.FAST);
    }),
    THAUMIC("Arcanis", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, getFlowers(EXTRABEES, "book"));
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.FASTER);
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.LONGEST);
    }),
    GEM("Ornamentis", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.NONE);
        AlleleHelper.instance.set(alleles, NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, Flowers.NETHER);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.AVERAGE);
    }),
    METAL("Metaliferis", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.DOWN_2);
        AlleleHelper.instance.set(alleles, CAVE_DWELLING, true);
        AlleleHelper.instance.set(alleles, NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, Flowers.JUNGLE);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.SLOWER);
    }),
    RAREMETAL("Mineralis", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.DOWN_1);
        AlleleHelper.instance.set(alleles, NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, Flowers.CACTI);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.FAST);
    }),
    RADIOACTIVE("Criticalis", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.NONE);
        AlleleHelper.instance.set(alleles, NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, Flowers.END);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.AVERAGE);
        AlleleHelper.instance.set(alleles, SPEED, GTBees.speedBlinding);
        AlleleHelper.instance.set(alleles, SPEED, getEffect(EXTRABEES, "radioactive"));
    }),
    TWILIGHT("Nemoris Obscuri", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, NOCTURNAL, false);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, Flowers.VANILLA);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.FASTER);
    }),
    HEE("Finis Expansiones", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.BOTH_2);
        AlleleHelper.instance.set(alleles, HUMIDITY_TOLERANCE, Tolerance.BOTH_2);
        AlleleHelper.instance.set(alleles, NOCTURNAL, true);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, getFlowers(EXTRABEES, "book"));
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.SHORT);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.SLOW);
        AlleleHelper.instance.set(alleles, SPEED, Speed.FASTEST);
        AlleleHelper.instance.set(alleles, TERRITORY, Territory.LARGER);
    }),
    SPACE("Cosmicis", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.DOWN_2);
        AlleleHelper.instance.set(alleles, TOLERANT_FLYER, true);
        AlleleHelper.instance.set(alleles, NOCTURNAL, true);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.FASTEST);
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.LONGEST);
        AlleleHelper.instance.set(alleles, SPEED, Speed.FAST);
        AlleleHelper.instance.set(alleles, TERRITORY, Territory.LARGEST);
    }),
    PLANET("Planetaris", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, TOLERANT_FLYER, true);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.FASTEST);
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.NORMAL);
        AlleleHelper.instance.set(alleles, SPEED, Speed.FASTEST);
        AlleleHelper.instance.set(alleles, TERRITORY, Territory.LARGER);
    }),
    NOBLEGAS("Nobilis Gasorum", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.BOTH_2);
        AlleleHelper.instance.set(alleles, TOLERANT_FLYER, true);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.FASTEST);
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.NORMAL);
        AlleleHelper.instance.set(alleles, SPEED, Speed.FASTEST);
        AlleleHelper.instance.set(alleles, TERRITORY, Territory.AVERAGE);
    }),
    INFUSEDSHARD("Infusa Shard", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
        AlleleHelper.instance.set(alleles, TOLERANT_FLYER, true);
        AlleleHelper.instance.set(alleles, FLOWER_PROVIDER, getFlowers(MAGICBEES, "rock"));
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.FASTEST);
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.SHORTEST);
        AlleleHelper.instance.set(alleles, SPEED, Speed.FASTEST);
        AlleleHelper.instance.set(alleles, TERRITORY, Territory.LARGEST);
    }),
    ENDGAME("ENDUS GAMUS", alleles -> {
        AlleleHelper.instance.set(alleles, TEMPERATURE_TOLERANCE, Tolerance.BOTH_5);
        AlleleHelper.instance.set(alleles, TOLERANT_FLYER, true);
        AlleleHelper.instance.set(alleles, FLOWERING, Flowering.FASTEST);
        AlleleHelper.instance.set(alleles, LIFESPAN, Lifespan.SHORTEST);
        AlleleHelper.instance.set(alleles, SPEED, Speed.FASTEST);
        AlleleHelper.instance.set(alleles, TERRITORY, Territory.LARGEST);
    });

    private static IAllele[] defaultTemplate;
    private final IClassification branch;
    private final Consumer<IAllele[]> mBranchProperties;

    GTBranchDefinition(String scientific, Consumer<IAllele[]> aBranchProperties) {
        this.branch = BeeManager.beeFactory.createBranch(
            this.name()
                .toLowerCase(),
            scientific);
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

    void setBranchProperties(IAllele[] template) {
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
