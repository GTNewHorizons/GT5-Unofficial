package gtPlusPlus.xmod.forestry.bees.registry;

import static forestry.api.apiculture.EnumBeeChromosome.EFFECT;
import static forestry.api.apiculture.EnumBeeChromosome.HUMIDITY_TOLERANCE;
import static forestry.api.apiculture.EnumBeeChromosome.LIFESPAN;
import static forestry.api.apiculture.EnumBeeChromosome.SPECIES;
import static forestry.api.apiculture.EnumBeeChromosome.TEMPERATURE_TOLERANCE;
import static forestry.api.core.EnumHumidity.ARID;
import static gregtech.api.enums.Mods.Forestry;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BiomeDictionary;

import org.apache.commons.lang3.text.WordUtils;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeEffect;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeMutationCustom;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleFlowers;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.BeeVariation;
import forestry.apiculture.genetics.IBeeDefinition;
import forestry.apiculture.genetics.alleles.AlleleEffect;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.EnumAllele.Lifespan;
import forestry.core.genetics.alleles.EnumAllele.Tolerance;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GT_Bees;
import gtPlusPlus.core.material.ELEMENT.STANDALONE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_CombType;

public enum GTPP_BeeDefinition implements IBeeDefinition {

    DRAGONBLOOD(GTPP_BranchDefinition.LEGENDARY, "Dragon Blood", STANDALONE.DRAGON_METAL, true,
            Utils.rgbtoHexValue(220, 20, 20), Utils.rgbtoHexValue(20, 20, 20), beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
                beeSpecies.addSpecialty(GTPP_Bees.combs.getStackForType(GTPP_CombType.DRAGONBLOOD), 0.10f);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            }, template -> {
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectAggressive);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_3);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_3);
            }, dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation("DRAGONESSENCE", "NEUTRONIUM", 2);
                tMutation.restrictHumidity(ARID);
                tMutation.requireResource(STANDALONE.DRAGON_METAL.getBlock(), 1);
                tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
            }),
    FORCE(GTPP_BranchDefinition.LEGENDARY, "Force", STANDALONE.FORCE, true, Utils.rgbtoHexValue(250, 250, 20),
            Utils.rgbtoHexValue(200, 200, 5), beeSpecies -> {
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
                beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALT), 0.15f);
                beeSpecies.addSpecialty(GTPP_Bees.combs.getStackForType(GTPP_CombType.FORCE), 0.10f);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.HOT);
                beeSpecies.setHasEffect();
            }, template -> {
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectAggressive);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
            }, dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation("STEEL", "GOLD", 10);
                tMutation.restrictHumidity(ARID);
                tMutation.restrictBiomeType(BiomeDictionary.Type.HOT);
            }),;

    private final GTPP_BranchDefinition branch;
    private final GTPP_AlleleBeeSpecies species;
    private final Consumer<GTPP_AlleleBeeSpecies> mSpeciesProperties;
    private final Consumer<IAllele[]> mAlleles;
    private final Consumer<GTPP_BeeDefinition> mMutations;
    private IAllele[] template;
    private IBeeGenome genome;

    GTPP_BeeDefinition(GTPP_BranchDefinition branch, String binomial, Materials aMat, boolean dominant, int primary,
            int secondary, Consumer<GTPP_AlleleBeeSpecies> aSpeciesProperties, Consumer<IAllele[]> aAlleles,
            Consumer<GTPP_BeeDefinition> aMutations) {
        this(
                branch,
                binomial,
                MaterialUtils.generateMaterialFromGtENUM(aMat),
                dominant,
                primary,
                secondary,
                aSpeciesProperties,
                aAlleles,
                aMutations);
    }

    GTPP_BeeDefinition(GTPP_BranchDefinition branch, String binomial, Material aMat, boolean dominant, int primary,
            int secondary, Consumer<GTPP_AlleleBeeSpecies> aSpeciesProperties, Consumer<IAllele[]> aAlleles,
            Consumer<GTPP_BeeDefinition> aMutations) {
        this.mAlleles = aAlleles;
        this.mMutations = aMutations;
        this.mSpeciesProperties = aSpeciesProperties;
        String lowercaseName = this.toString().toLowerCase(Locale.ENGLISH);
        String species = WordUtils.capitalize(binomial);
        String uid = "gtpp.bee.species" + species;
        String description = "for.description." + species;
        String name = "for.bees.species." + lowercaseName;
        GT_LanguageManager.addStringLocalization("for.bees.species." + lowercaseName, species, true);
        GTPP_Bees.sMaterialMappings.put(binomial.toLowerCase().replaceAll(" ", ""), aMat);
        this.branch = branch;
        this.species = new GTPP_AlleleBeeSpecies(
                uid,
                dominant,
                name,
                "GT++",
                description,
                branch.getBranch(),
                binomial,
                primary,
                secondary);
    }

    public static void initBees() {
        for (GTPP_BeeDefinition bee : values()) {
            bee.init();
        }
        for (GTPP_BeeDefinition bee : values()) {
            bee.registerMutations();
        }
    }

    private static IAlleleBeeEffect getEffect(byte modid, String name) {
        String s = switch (modid) {
            case GTPP_Bees.EXTRABEES -> "extrabees.effect." + name;
            case GTPP_Bees.GENDUSTRY -> "gendustry.effect." + name;
            case GTPP_Bees.MAGICBEES -> "magicbees.effect" + name;
            case GTPP_Bees.GREGTECH -> "gregtech.effect" + name;
            default -> "forestry.effect" + name;
        };
        return (IAlleleBeeEffect) AlleleManager.alleleRegistry.getAllele(s);
    }

    private static IAlleleFlowers getFlowers(byte modid, String name) {
        String s = switch (modid) {
            case GTPP_Bees.EXTRABEES -> "extrabees.flower." + name;
            case GTPP_Bees.GENDUSTRY -> "gendustry.flower." + name;
            case GTPP_Bees.MAGICBEES -> "magicbees.flower" + name;
            case GTPP_Bees.GREGTECH -> "gregtech.flower" + name;
            default -> "forestry.flowers" + name;
        };
        return (IAlleleFlowers) AlleleManager.alleleRegistry.getAllele(s);
    }

    private static IAlleleBeeSpecies getSpecies(byte modid, String name) {
        String s = switch (modid) {
            case GTPP_Bees.EXTRABEES -> "extrabees.species." + name;
            case GTPP_Bees.GENDUSTRY -> "gendustry.bee." + name;
            case GTPP_Bees.MAGICBEES -> "magicbees.species" + name;
            case GTPP_Bees.GREGTECH -> "gregtech.species" + name;
            default -> "forestry.species" + name;
        };
        IAlleleBeeSpecies ret = (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele(s);
        return ret;
    }

    private final void setSpeciesProperties(GTPP_AlleleBeeSpecies species2) {
        this.mSpeciesProperties.accept(species2);
    }

    private final void setAlleles(IAllele[] template) {
        this.mAlleles.accept(template);
    }

    private final void registerMutations() {
        this.mMutations.accept(this);
    }

    private void init() {
        setSpeciesProperties(species);

        template = branch.getTemplate();
        AlleleHelper.instance.set(template, SPECIES, species);
        setAlleles(template);

        genome = BeeManager.beeRoot.templateAsGenome(template);

        BeeManager.beeRoot.registerTemplate(template);
    }

    private final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2,
            int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    private final IBeeMutationCustom registerMutation(GTPP_BeeDefinition parent1, IAlleleBeeSpecies parent2,
            int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    private final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, GTPP_BeeDefinition parent2,
            int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    private final IBeeMutationCustom registerMutation(GTPP_BeeDefinition parent1, GTPP_BeeDefinition parent2,
            int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    private final IBeeMutationCustom registerMutation(String parent1, String parent2, int chance) {
        return registerMutation(getGregtechBeeType(parent1), getGregtechBeeType(parent2), chance, 1f);
    }

    /**
     * Diese neue Funtion erlaubt Mutationsraten unter 1%. Setze dazu die Mutationsrate als Bruch mit chance /
     * chancedivider This new function allows Mutation percentages under 1%. Set them as a fraction with chance /
     * chancedivider
     */
    private final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2, int chance,
            float chancedivider) {
        return new GTPP_Bee_Mutation(parent1, parent2, this.getTemplate(), chance, chancedivider);
    }

    private final IBeeMutationCustom registerMutation(GTPP_BeeDefinition parent1, IAlleleBeeSpecies parent2, int chance,
            float chancedivider) {
        return registerMutation(parent1.species, parent2, chance, chancedivider);
    }

    private final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, GTPP_BeeDefinition parent2, int chance,
            float chancedivider) {
        return registerMutation(parent1, parent2.species, chance, chancedivider);
    }

    private final IBeeMutationCustom registerMutation(GTPP_BeeDefinition parent1, GTPP_BeeDefinition parent2,
            int chance, float chancedivider) {
        return registerMutation(parent1.species, parent2, chance, chancedivider);
    }

    private final IBeeMutationCustom registerMutation(String parent1, String parent2, int chance, float chancedivider) {
        return registerMutation(getGregtechBeeType(parent1), getGregtechBeeType(parent2), chance, chancedivider);
    }

    @Override
    public final IAllele[] getTemplate() {
        return Arrays.copyOf(template, template.length);
    }

    @Override
    public final IBeeGenome getGenome() {
        return genome;
    }

    @Override
    public final IBee getIndividual() {
        return new Bee(genome);
    }

    @Override
    public final ItemStack getMemberStack(EnumBeeType beeType) {
        return BeeManager.beeRoot.getMemberStack(getIndividual(), beeType.ordinal());
    }

    public final IBeeDefinition getRainResist() {
        return new BeeVariation.RainResist(this);
    }

    private static final Class sGtBees = ReflectionUtils.getClass("gregtech.loaders.misc.GT_BeeDefinition");

    public static IAlleleBeeSpecies getGregtechBeeType(String name) {
        try {
            Enum aBeeObject = ReflectionUtils.getEnum(sGtBees, name);
            Field gtBeesField = ReflectionUtils.getField(sGtBees, "species");
            IAlleleBeeSpecies beeType = ReflectionUtils.getFieldValue(gtBeesField, aBeeObject);
            return beeType != null ? beeType : null;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}
