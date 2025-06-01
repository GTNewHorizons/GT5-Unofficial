package gtPlusPlus.xmod.forestry.bees.custom;

import java.util.Arrays;
import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BiomeDictionary.Type;

import org.apache.commons.lang3.text.WordUtils;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IAlleleBeeSpeciesCustom;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeMutationCustom;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.BeeDefinition;
import forestry.apiculture.genetics.IBeeDefinition;
import forestry.core.genetics.alleles.AlleleHelper;
import gregtech.api.util.GTUtility;
import gregtech.loaders.misc.GTBeeDefinition;
import gregtech.loaders.misc.GTBranchDefinition;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;

public enum GTPPBeeDefinition implements IBeeDefinition {

    SILICON(GTBranchDefinition.ORGANIC, "Silicon", true, Utils.rgbtoHexValue(75, 75, 75),
        Utils.rgbtoHexValue(125, 125, 125)) {

        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(getSlagComb(), 0.10f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.SILICON), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(
                GTBeeDefinition.SLIMEBALL.getSpecies(),
                GTBeeDefinition.STICKYRESIN.getSpecies(),
                10);
        }
    },

    RUBBER(GTBranchDefinition.ORGANIC, "Rubber", true, Utils.rgbtoHexValue(55, 55, 55),
        Utils.rgbtoHexValue(75, 75, 75)) {

        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(getSlagComb(), 0.10f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.RUBBER), 0.30f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(
                GTBeeDefinition.SLIMEBALL.getSpecies(),
                GTBeeDefinition.STICKYRESIN.getSpecies(),
                10);
        }
    },

    PLASTIC(GTBranchDefinition.ORGANIC, "Plastic", true, Utils.rgbtoHexValue(245, 245, 245),
        Utils.rgbtoHexValue(175, 175, 175)) {

        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(getStoneComb(), 0.30f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.PLASTIC), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(
                GTPPBeeDefinition.RUBBER.species,
                GTBeeDefinition.OIL.getSpecies(),
                10);
        }
    },

    PTFE(GTBranchDefinition.ORGANIC, "Ptfe", true, Utils.rgbtoHexValue(150, 150, 150),
        Utils.rgbtoHexValue(75, 75, 75)) {

        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(getStoneComb(), 0.30f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.PTFE), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(RUBBER.species, PLASTIC.species, 10);
        }
    },

    PBS(GTBranchDefinition.ORGANIC, "Pbs", true, Utils.rgbtoHexValue(33, 26, 24), Utils.rgbtoHexValue(23, 16, 14)) {

        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(getStoneComb(), 0.30f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.PBS), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(PTFE.species, PLASTIC.species, 10);
        }
    },

    /**
     * Fuels
     */
    BIOMASS(GTBranchDefinition.ORGANIC, "Biomass", true, Utils.rgbtoHexValue(33, 225, 24),
        Utils.rgbtoHexValue(23, 175, 14)) {

        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.SAND), 0.40f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.BIOMASS), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies("Industrious"), getSpecies("Rural"), 10);
            tMutation.restrictBiomeType(Type.FOREST);
        }
    },

    ETHANOL(GTBranchDefinition.ORGANIC, "Ethanol", true, Utils.rgbtoHexValue(255, 128, 0),
        Utils.rgbtoHexValue(220, 156, 32)) {

        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.SAND), 0.40f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.ETHANOL), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(BIOMASS.species, getSpecies("Farmerly"), 5);
            tMutation.restrictBiomeType(Type.FOREST);
        }
    },

    /**
     * Materials
     */
    FLUORINE(GTBranchDefinition.ORGANIC, "Fluorine", true, Utils.rgbtoHexValue(30, 230, 230),
        Utils.rgbtoHexValue(10, 150, 150)) {

        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(getStoneComb(), 0.40f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.FLUORINE), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(
                GTBeeDefinition.LAPIS.getSpecies(),
                GTBeeDefinition.SAPPHIRE.getSpecies(),
                5);
            tMutation.restrictBiomeType(Type.COLD);
        }
    },

    // Coke

    // Force
    FORCE(GTBranchDefinition.METAL, "Force", true, Utils.rgbtoHexValue(250, 250, 20),
        Utils.rgbtoHexValue(200, 200, 5)) {

        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(getStoneComb(), 0.30f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.SAND), 0.25f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.FORCE), 0.25f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.SALT), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(
                GTBeeDefinition.STEEL.getSpecies(),
                GTBeeDefinition.GOLD.getSpecies(),
                10);
            tMutation.restrictBiomeType(Type.HOT);
        }
    },

    // Nikolite
    NIKOLITE(GTBranchDefinition.METAL, "Nikolite", true, Utils.rgbtoHexValue(60, 180, 200),
        Utils.rgbtoHexValue(40, 150, 170)) {

        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(getStoneComb(), 0.30f);
            beeSpecies.addProduct(GTPPBees.combs.getStackForType(CustomCombs.NIKOLITE), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(
                GTBeeDefinition.ALUMINIUM.getSpecies(),
                GTBeeDefinition.SILVER.getSpecies(),
                8);
            tMutation.restrictBiomeType(Type.HOT);
        }
    },

    ;

    private final GTBranchDefinition branch;
    private final IAlleleBeeSpeciesCustom species;

    private IAllele[] template;
    private IBeeGenome genome;

    GTPPBeeDefinition(GTBranchDefinition branch, String binomial, boolean dominant, int primary, int secondary) {
        String lowercaseName = this.toString()
            .toLowerCase(Locale.ENGLISH);
        String species = "species" + WordUtils.capitalize(lowercaseName);

        String uid = "forestry." + species;
        String description = "for.description." + species;
        String name = "for.bees.species." + lowercaseName;

        this.branch = branch;
        this.species = BeeManager.beeFactory.createSpecies(
            uid,
            dominant,
            "Sengir",
            name,
            description,
            branch.getBranch(),
            binomial,
            primary,
            secondary);
    }

    public static void initBees() {
        for (GTPPBeeDefinition bee : values()) {
            bee.init();
        }
        for (GTPPBeeDefinition bee : values()) {
            bee.registerMutations();
        }
    }

    private static IAlleleBeeSpecies getSpecies(String name) {
        return (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele("forestry.species" + name);
    }

    protected abstract void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies);

    protected abstract void setAlleles(IAllele[] template);

    protected abstract void registerMutations();

    private void init() {
        setSpeciesProperties(species);

        template = branch.getTemplate();
        AlleleHelper.instance.set(template, EnumBeeChromosome.SPECIES, species);
        setAlleles(template);

        genome = BeeManager.beeRoot.templateAsGenome(template);

        BeeManager.beeRoot.registerTemplate(template);
    }

    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2,
        int chance) {
        IAllele[] template = getTemplate();
        Logger.DEBUG_BEES("parent1: " + (parent1 != null));
        Logger.DEBUG_BEES("parent2: " + (parent2 != null));
        Logger.DEBUG_BEES("chance: " + (chance));
        Logger.DEBUG_BEES("template: " + (template != null));
        return BeeManager.beeMutationFactory.createMutation(parent1, parent2, template, chance);
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
        IBee bee = getIndividual();
        return BeeManager.beeRoot.getMemberStack(bee, beeType.ordinal());
    }

    private static ItemStack getSlagComb() {
        return GTUtility.copyAmount(1, GTPPBees.Comb_Slag);
    }

    private static ItemStack getStoneComb() {
        return GTUtility.copyAmount(1, GTPPBees.Comb_Stone);
    }
}
