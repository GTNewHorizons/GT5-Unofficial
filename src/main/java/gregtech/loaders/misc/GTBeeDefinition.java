package gregtech.loaders.misc;

import static forestry.api.apiculture.EnumBeeChromosome.CAVE_DWELLING;
import static forestry.api.apiculture.EnumBeeChromosome.EFFECT;
import static forestry.api.apiculture.EnumBeeChromosome.FERTILITY;
import static forestry.api.apiculture.EnumBeeChromosome.FLOWERING;
import static forestry.api.apiculture.EnumBeeChromosome.FLOWER_PROVIDER;
import static forestry.api.apiculture.EnumBeeChromosome.HUMIDITY_TOLERANCE;
import static forestry.api.apiculture.EnumBeeChromosome.LIFESPAN;
import static forestry.api.apiculture.EnumBeeChromosome.NOCTURNAL;
import static forestry.api.apiculture.EnumBeeChromosome.SPECIES;
import static forestry.api.apiculture.EnumBeeChromosome.SPEED;
import static forestry.api.apiculture.EnumBeeChromosome.TEMPERATURE_TOLERANCE;
import static forestry.api.apiculture.EnumBeeChromosome.TERRITORY;
import static forestry.api.apiculture.EnumBeeChromosome.TOLERANT_FLYER;
import static forestry.api.core.EnumHumidity.ARID;
import static forestry.api.core.EnumHumidity.DAMP;
import static forestry.api.core.EnumTemperature.COLD;
import static forestry.api.core.EnumTemperature.HELLISH;
import static forestry.api.core.EnumTemperature.HOT;
import static forestry.api.core.EnumTemperature.ICY;
import static forestry.api.core.EnumTemperature.NORMAL;
import static forestry.api.core.EnumTemperature.WARM;
import static forestry.core.genetics.alleles.EnumAllele.Fertility;
import static forestry.core.genetics.alleles.EnumAllele.Flowering;
import static forestry.core.genetics.alleles.EnumAllele.Flowers;
import static forestry.core.genetics.alleles.EnumAllele.Lifespan;
import static forestry.core.genetics.alleles.EnumAllele.Speed;
import static forestry.core.genetics.alleles.EnumAllele.Territory;
import static forestry.core.genetics.alleles.EnumAllele.Tolerance;
import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.AvaritiaAddons;
import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.CropsNH;
import static gregtech.api.enums.Mods.EnderStorage;
import static gregtech.api.enums.Mods.ExtraBees;
import static gregtech.api.enums.Mods.ExtraCells2;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.MagicBees;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.Mods.TaintedMagic;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.ThaumicBases;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.loaders.misc.GTBeeDefinitionReference.EXTRABEES;
import static gregtech.loaders.misc.GTBeeDefinitionReference.FORESTRY;
import static gregtech.loaders.misc.GTBeeDefinitionReference.GENDUSTRY;
import static gregtech.loaders.misc.GTBeeDefinitionReference.GREGTECH;
import static gregtech.loaders.misc.GTBeeDefinitionReference.MAGICBEES;

import java.awt.Color;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.common.registry.GameRegistry;
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
import forestry.api.genetics.IMutationCustom;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.IBeeDefinition;
import forestry.apiculture.genetics.alleles.AlleleEffect;
import forestry.core.genetics.alleles.AlleleHelper;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.ColorUtils;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.JubilanceMegaApiary;
import gregtech.common.bees.GTAlleleBeeSpecies;
import gregtech.common.bees.GTBeeMutation;
import gregtech.common.items.CombType;
import gregtech.common.items.DropType;
import gregtech.common.items.PropolisType;
import gregtech.loaders.misc.bees.GTAlleleEffect;
import gregtech.loaders.misc.bees.GTFlowers;
import gtnhlanth.common.register.WerkstoffMaterialPool;

/**
 * Bride Class for Lambdas
 */
class GTBeeDefinitionReference {

    protected static final byte FORESTRY = 0;
    protected static final byte EXTRABEES = 1;
    protected static final byte GENDUSTRY = 2;
    protected static final byte MAGICBEES = 3;
    protected static final byte GREGTECH = 4;

    private GTBeeDefinitionReference() {}
}

public enum GTBeeDefinition implements IBeeDefinition {

    // organic
    CLAY(GTBranchDefinition.ORGANIC, "Clay", true, new Color(ColorUtils.bee1Clay.getColor()),
        new Color(ColorUtils.bee2Clay.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addProduct(new ItemStack(Items.clay_ball, 1), 0.15f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(BiomesOPlenty.ID, "mudball", 1, 0), 0.05f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWER);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.VANILLA);
        }, dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(FORESTRY, "Industrious"), getSpecies(FORESTRY, "Diligent"), 10);
            tMutation.requireResource(Blocks.clay, 0); // blockStainedHardenedClay
        }),
    SLIMEBALL(GTBranchDefinition.ORGANIC, "SlimeBall", true, new Color(ColorUtils.bee1Slimeball.getColor()),
        new Color(ColorUtils.bee2Slimeball.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 15), 0.30f);
            beeSpecies.addProduct(new ItemStack(Items.slime_ball, 1), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.STICKY), 0.05f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            if (TinkerConstruct.isModLoaded()) {
                beeSpecies.addProduct(GTModHandler.getModItem(TinkerConstruct.ID, "strangeFood", 1, 0), 0.10f);
                beeSpecies.addSpecialty(GTModHandler.getModItem(TinkerConstruct.ID, "slime.gel", 1, 2), 0.02f);
            }
        }, template -> {
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.MUSHROOMS);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWER);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "water"));
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Marshy"), CLAY, 7);
            if (TinkerConstruct.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(TinkerConstruct.ID, "slime.gel"), 1);
        }),
    PEAT(GTBranchDefinition.ORGANIC, "Peat", true, new Color(ColorUtils.bee1Peat.getColor()),
        new Color(ColorUtils.bee2Peat.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.LIGNIE), 0.30f);
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 0), 0.15f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(Forestry.ID, "peat", 1, 0), 0.15f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(Forestry.ID, "mulch", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
        }, dis -> dis.registerMutation(getSpecies(FORESTRY, "Rural"), CLAY, 10)),
    STICKYRESIN(GTBranchDefinition.ORGANIC, "StickyResin", true, new Color(ColorUtils.bee1Stickyresin.getColor()),
        new Color(ColorUtils.bee2Stickyresin.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.STICKY), 0.15f);
            beeSpecies.addSpecialty(ItemList.IC2_Resin.get(1), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWER);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(SLIMEBALL, PEAT, 15);
            tMutation.requireResource("logRubber");
        }),
    COAL(GTBranchDefinition.ORGANIC, "Coal", true, new Color(ColorUtils.bee1Coal.getColor()),
        new Color(ColorUtils.bee2Coal.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.LIGNIE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.COAL), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.CACTI);
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.DOWN_2);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.DOWN_1);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectCreeper);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Industrious"), PEAT, 9);
            tMutation.requireResource("blockCoal");
        }),
    OIL(GTBranchDefinition.ORGANIC, "Oil", true, new Color(ColorUtils.bee1Oil.getColor()),
        new Color(ColorUtils.bee2Oil.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.OIL), 0.75f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWER);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL);
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "water"));
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
        }, dis -> dis.registerMutation(COAL, STICKYRESIN, 4)),
    SANDWICH(GTBranchDefinition.ORGANIC, "Sandwich", true, new Color(ColorUtils.bee1Sandwich.getColor()),
        new Color(ColorUtils.bee2Sandwich.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 9), 0.15f);
            beeSpecies.addSpecialty(ItemList.Food_Sliced_Cucumber.get(1), 0.05f);
            beeSpecies.addSpecialty(ItemList.Food_Sliced_Onion.get(1), 0.05f);
            beeSpecies.addSpecialty(ItemList.Food_Sliced_Tomato.get(1), 0.05f);
            beeSpecies.addSpecialty(ItemList.Food_Sliced_Cheese.get(1), 0.05f);
            beeSpecies.addSpecialty(new ItemStack(Items.cooked_porkchop, 1, 0), 0.05f);
            beeSpecies.addSpecialty(new ItemStack(Items.cooked_beef, 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOW);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_2);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectFertile);
            AlleleHelper.instance.set(template, TERRITORY, Territory.LARGE);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
        }, dis -> dis.registerMutation(getSpecies(FORESTRY, "Agrarian"), getSpecies(MAGICBEES, "TCBatty"), 10)),
    ASH(GTBranchDefinition.ORGANIC, "Ash", true, new Color(ColorUtils.bee1Ash.getColor()),
        new Color(ColorUtils.bee2Ash.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 9), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ASH), 0.15f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.NORMAL);
            AlleleHelper.instance.set(template, TERRITORY, Territory.LARGE);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(COAL, CLAY, 10);
            tMutation.restrictTemperature(HELLISH);
        }),
    APATITE(GTBranchDefinition.ORGANIC, "Apatite", true, new Color(ColorUtils.bee1Apatite.getColor()),
        new Color(ColorUtils.bee2Apatite.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 9), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.APATITE), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(WARM);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FASTEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(ASH, COAL, 10);
            tMutation.requireResource("blockApatite");
        }),
    FERTILIZER(GTBranchDefinition.ORGANIC, "Fertilizer", true, new Color(ColorUtils.bee1Fertilizer.getColor()),
        new Color(ColorUtils.bee2Fertilizer.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 9), 0.15f);
            beeSpecies.addSpecialty(GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ash, 1), 0.2f);
            beeSpecies.addSpecialty(GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.AshDark, 1), 0.2f);
            beeSpecies.addSpecialty(ItemList.FR_Fertilizer.get(1), 0.3f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(WARM);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FASTEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
        }, dis -> dis.registerMutation(ASH, APATITE, 8)),
    // Phosphorus bee, Humidity: normal, Temperature: Hot, Parents: Apatite & Ash, Mutationrate: 12%, Combrate: 55%
    PHOSPHORUS(GTBranchDefinition.ORGANIC, "Phosphorus", false, new Color(ColorUtils.bee1Phosphorus.getColor()),
        new Color(ColorUtils.bee2Phosphorus.getColor()), beeSpecies -> {
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.PHOSPHORUS), 0.35f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(APATITE, ASH, 12);
            tMutation.restrictTemperature(HOT);
            GregTechAPI.sGTCompleteLoad.add(() -> tMutation.requireResource(GregTechAPI.sBlockGem2, 8));
        }),
    // Tea bee, Humidity: normal, Parents: Ash and Fertilizer, Mutationrate: 10%, combrate: 10%
    TEA(GTBranchDefinition.ORGANIC, "Tea", false, new Color(ColorUtils.bee1Tea.getColor()),
        new Color(ColorUtils.bee2Tea.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(PamsHarvestCraft.ID, "tealeafItem", 1), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(NORMAL);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST),
        dis -> dis.registerMutation(FERTILIZER, ASH, 10)),
    // Mica bee, Humidity: normal, Parents: Silicon & PEAT, Mutationrate: 15%, Combrate: 25%
    MICA(GTBranchDefinition.ORGANIC, "Mica", false, new Color(ColorUtils.bee1Mica.getColor()),
        new Color(ColorUtils.bee2Mica.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MICA), 0.25f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(PEAT, getSpecies(MAGICBEES, "Silicon"), 15);
            GregTechAPI.sGTCompleteLoad.add(() -> tMutation.requireResource(GregTechAPI.sBlockCasings5, 0));
        }),

    // gems
    REDSTONE(GTBranchDefinition.GEM, "Redstone", true, new Color(ColorUtils.bee1Redstone.getColor()),
        new Color(ColorUtils.bee2Redstone.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.REDSTONE), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.RAREEARTH), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(FORESTRY, "Industrious"), getSpecies(FORESTRY, "Demonic"), 10);
            tMutation.requireResource("blockRedstone");
        }),
    LAPIS(GTBranchDefinition.GEM, "Lapis", true, new Color(ColorUtils.bee1Lapis.getColor()),
        new Color(ColorUtils.bee2Lapis.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.LAPIS), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(FORESTRY, "Demonic"), getSpecies(FORESTRY, "Imperial"), 10);
            tMutation.requireResource("blockLapis");
        }),
    CERTUS(GTBranchDefinition.GEM, "CertusQuartz", true, new Color(ColorUtils.bee1Certus.getColor()),
        new Color(ColorUtils.bee2Certus.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.CERTUS), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Hermitic"), LAPIS, 10);
            tMutation.requireResource(GameRegistry.findBlock(AppliedEnergistics2.ID, "tile.BlockQuartz"), 0);
        }),
    FLUIX(GTBranchDefinition.GEM, "FluixDust", true, new Color(ColorUtils.bee1Fluix.getColor()),
        new Color(ColorUtils.bee2Fluix.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.FLUIX), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, LAPIS, 7);
            tMutation.requireResource(GameRegistry.findBlock(AppliedEnergistics2.ID, "tile.BlockFluix"), 0);
        }),
    DIAMOND(GTBranchDefinition.GEM, "Diamond", false, new Color(ColorUtils.bee1Diamond.getColor()),
        new Color(ColorUtils.bee2Diamond.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.DIAMOND), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(CERTUS, COAL, 3);
            tMutation.requireResource("blockDiamond");
        }),
    RUBY(GTBranchDefinition.GEM, "Ruby", false, new Color(ColorUtils.bee1Ruby.getColor()),
        new Color(ColorUtils.bee2Ruby.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.RUBY), 0.15f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.REDGARNET), 0.05f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, DIAMOND, 5);
            tMutation.requireResource("blockRuby");
        }),
    SAPPHIRE(GTBranchDefinition.GEM, "Sapphire", true, new Color(ColorUtils.bee1Sapphire.getColor()),
        new Color(ColorUtils.bee2Sapphire.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SAPPHIRE), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(CERTUS, LAPIS, 5);
            tMutation.requireResource(GregTechAPI.sBlockGem2, 12);
        }),
    OLIVINE(GTBranchDefinition.GEM, "Olivine", true, new Color(ColorUtils.bee1Olivine.getColor()),
        new Color(ColorUtils.bee2Olivine.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.OLIVINE), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.MAGNESIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER),
        dis -> dis.registerMutation(CERTUS, getSpecies(FORESTRY, "Ended"), 5)),
    EMERALD(GTBranchDefinition.GEM, "Emerald", false, new Color(ColorUtils.bee1Emerald.getColor()),
        new Color(ColorUtils.bee2Emerald.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.EMERALD), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ALUMINIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(OLIVINE, DIAMOND, 4);
            tMutation.requireResource("blockEmerald");
        }),
    REDGARNET(GTBranchDefinition.GEM, "RedGarnet", false, new Color(ColorUtils.bee1Redgarnet.getColor()),
        new Color(ColorUtils.bee2Redgarnet.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.REDGARNET), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.PYROPE), 0.05f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(WARM);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(DIAMOND, RUBY, 4);
            tMutation.requireResource("blockGarnetRed");
        }),
    YELLOWGARNET(GTBranchDefinition.GEM, "YellowGarnet", false, new Color(ColorUtils.bee1Yellowgarnet.getColor()),
        new Color(ColorUtils.bee2Yellowgarnet.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.YELLOWGARNET), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.GROSSULAR), 0.05f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(WARM);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(EMERALD, REDGARNET, 3);
            tMutation.requireResource("blockGarnetYellow");
        }),
    FIRESTONE(GTBranchDefinition.GEM, "Firestone", false, new Color(ColorUtils.bee1Firestone.getColor()),
        new Color(ColorUtils.bee2Firestone.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.FIRESTONE), 0.20f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(WARM);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, RUBY, 4);
            tMutation.requireResource("blockFirestone");
        }),
    PRISMATIC(GTBranchDefinition.GEM, "Prismatic", false, new Color(ColorUtils.bee1Prismatic.getColor()),
        new Color(ColorUtils.bee2Prismatic.getColor()), beeSpecies -> {
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.PRISMATIC), 0.1f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.NORMAL);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL);
            AlleleHelper.instance.set(template, TOLERANT_FLYER, true);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, EFFECT, getEffect(MAGICBEES, "SlowSpeed"));
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.END);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(CERTUS, getSpecies(EXTRABEES, "ocean"), 10);
            tMutation.restrictHumidity(DAMP);
            if (Botania.isModLoaded()) {
                tMutation.requireResource(
                    Block.getBlockFromItem(
                        GTModHandler.getModItem(Botania.ID, "prismarine", 1)
                            .getItem()),
                    0);
            } else {
                tMutation.requireResource("blockPrismarine");
            }
        }),
    // Metal Line
    COPPER(GTBranchDefinition.METAL, "Copper", true, new Color(ColorUtils.bee1Copper.getColor()),
        new Color(ColorUtils.bee2Copper.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.COPPER), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.GOLD), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Majestic"), CLAY, 13);
            tMutation.requireResource("blockCopper");
        }),
    TIN(GTBranchDefinition.METAL, "Tin", true, new Color(ColorUtils.bee1Tin.getColor()),
        new Color(ColorUtils.bee2Tin.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.TIN), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ZINC), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(CLAY, getSpecies(FORESTRY, "Diligent"), 13);
            tMutation.requireResource("blockTin");
        }),
    LEAD(GTBranchDefinition.METAL, "Lead", true, new Color(ColorUtils.bee1Lead.getColor()),
        new Color(ColorUtils.bee2Lead.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.LEAD), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SULFUR), 0.05f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(WARM);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(COAL, COPPER, 13);
            tMutation.requireResource("blockLead");
        }),
    IRON(GTBranchDefinition.METAL, "Iron", true, new Color(ColorUtils.bee1Iron.getColor()),
        new Color(ColorUtils.bee2Iron.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.IRON), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.TIN), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(TIN, COPPER, 13);
            tMutation.requireResource("blockIron");
        }),
    STEEL(GTBranchDefinition.METAL, "Steel", true, new Color(ColorUtils.bee1Steel.getColor()),
        new Color(ColorUtils.bee2Steel.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STEEL), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.IRON), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(WARM);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(IRON, COAL, 10);
            tMutation.requireResource(GregTechAPI.sBlockMetal6, 13);
        }),
    NICKEL(GTBranchDefinition.METAL, "Nickel", true, new Color(ColorUtils.bee1Nickel.getColor()),
        new Color(ColorUtils.bee2Nickel.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.NICKEL), 0.15f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.PLATINUM), 0.02f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(IRON, COPPER, 13);
            tMutation.requireResource("blockNickel");
        }),
    ZINC(GTBranchDefinition.METAL, "Zinc", true, new Color(ColorUtils.bee1Zinc.getColor()),
        new Color(ColorUtils.bee2Zinc.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.ZINC), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.GALLIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(IRON, TIN, 13);
            tMutation.requireResource("blockZinc");
        }),
    SILVER(GTBranchDefinition.METAL, "Silver", true, new Color(ColorUtils.bee1Silver.getColor()),
        new Color(ColorUtils.bee2Silver.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SILVER), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SULFUR), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(LEAD, TIN, 10);
            tMutation.requireResource("blockSilver");
        }),
    CRYOLITE(GTBranchDefinition.METAL, "Cryolite", true, new Color(ColorUtils.bee1Cryolite.getColor()),
        new Color(ColorUtils.bee2Cryolite.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.CRYOLITE), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SILVER), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(WARM);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.FASTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(LEAD, SILVER, 9);
            tMutation.requireResource("blockCryolite");
        }),
    GOLD(GTBranchDefinition.METAL, "Gold", true, new Color(ColorUtils.bee1Gold.getColor()),
        new Color(ColorUtils.bee2Gold.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.GOLD), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.NICKEL), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(WARM);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(LEAD, COPPER, 13);
            tMutation.requireResource("blockGold");
            tMutation.restrictTemperature(HOT);
        }),
    ARSENIC(GTBranchDefinition.METAL, "Arsenic", true, new Color(ColorUtils.bee1Arsenic.getColor()),
        new Color(ColorUtils.bee2Arsenic.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.ARSENIC), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(WARM);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(ZINC, SILVER, 10);
            tMutation.requireResource("blockArsenic");
        }),

    // Rare Metals
    ALUMINIUM(GTBranchDefinition.RAREMETAL, "Aluminium", true, new Color(ColorUtils.bee1Aluminium.getColor()),
        new Color(ColorUtils.bee2Aluminium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.ALUMINIUM), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.BAUXITE), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NICKEL, ZINC, 9);
            tMutation.requireResource("blockAluminium");
        }),
    TITANIUM(GTBranchDefinition.RAREMETAL, "Titanium", true, new Color(ColorUtils.bee1Titanium.getColor()),
        new Color(ColorUtils.bee2Titanium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.TITANIUM), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ALMANDINE), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, ALUMINIUM, 5);
            tMutation.requireResource(GregTechAPI.sBlockMetal7, 9);
        }),
    GLOWSTONE(GTBranchDefinition.RAREMETAL, "Glowstone", false, new Color(ColorUtils.bee1Glowstone.getColor()),
        new Color(ColorUtils.bee2Glowstone.getColor()), beeSpecies -> {
            beeSpecies.addSpecialty(Materials.Glowstone.getDust(1), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL),
        dis -> dis.registerMutation(REDSTONE, GOLD, 10)),
    SUNNARIUM(GTBranchDefinition.RAREMETAL, "Sunnarium", false, new Color(ColorUtils.bee1Sunnarium.getColor()),
        new Color(ColorUtils.bee2Sunnarium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(Materials.Glowstone.getDust(1), 0.30f);
            beeSpecies.addSpecialty(Materials.Sunnarium.getDust(1), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(GLOWSTONE, GOLD, 5);
            GregTechAPI.sGTCompleteLoad.add(() -> tMutation.requireResource(GregTechAPI.sBlockCasings1, 15));
        }),
    CHROME(GTBranchDefinition.RAREMETAL, "Chrome", true, new Color(ColorUtils.bee1Chrome.getColor()),
        new Color(ColorUtils.bee2Chrome.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.CHROME), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.MAGNESIUM), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(TITANIUM, RUBY, 5);
            tMutation.requireResource(GregTechAPI.sBlockMetal2, 3);
        }),
    MANGANESE(GTBranchDefinition.RAREMETAL, "Manganese", true, new Color(ColorUtils.bee1Manganese.getColor()),
        new Color(ColorUtils.bee2Manganese.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MANGANESE), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.IRON), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(TITANIUM, ALUMINIUM, 5);
            tMutation.requireResource(GregTechAPI.sBlockMetal4, 6);
        }),
    TUNGSTEN(GTBranchDefinition.RAREMETAL, "Tungsten", false, new Color(ColorUtils.bee1Tungsten.getColor()),
        new Color(ColorUtils.bee2Tungsten.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.TUNGSTEN), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.MOLYBDENUM), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Heroic"), MANGANESE, 5);
            tMutation.requireResource(GregTechAPI.sBlockMetal7, 11);
        }),
    PLATINUM(GTBranchDefinition.RAREMETAL, "Platinum", false, new Color(ColorUtils.bee1Platinum.getColor()),
        new Color(ColorUtils.bee2Platinum.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.PLATINUM), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.IRIDIUM), 0.02f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(DIAMOND, CHROME, 5);
            tMutation.requireResource("blockPlatinum");
        }),
    IRIDIUM(GTBranchDefinition.RAREMETAL, "Iridium", false, new Color(ColorUtils.bee1Iridium.getColor()),
        new Color(ColorUtils.bee2Iridium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.IRIDIUM), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.OSMIUM), 0.05f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.PALLADIUM), 0.30f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(TUNGSTEN, PLATINUM, 5);
            tMutation.requireResource(GregTechAPI.sBlockMetal3, 12);
        }),
    OSMIUM(GTBranchDefinition.RAREMETAL, "Osmium", false, new Color(ColorUtils.bee1Osmium.getColor()),
        new Color(ColorUtils.bee2Osmium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.OSMIUM), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.IRIDIUM), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(TUNGSTEN, PLATINUM, 5);
            tMutation.requireResource(GregTechAPI.sBlockMetal5, 9);
        }),
    SALTY(GTBranchDefinition.RAREMETAL, "Salt", true, new Color(ColorUtils.bee1Salty.getColor()),
        new Color(ColorUtils.bee2Salty.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SALT), 0.35f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.LITHIUM), 0.05f);
            beeSpecies.addSpecialty(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Borax, 1L), 0.1f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(WARM);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(CLAY, ALUMINIUM, 5);
            tMutation.requireResource("blockSalt");
        }),
    LITHIUM(GTBranchDefinition.RAREMETAL, "Lithium", false, new Color(ColorUtils.bee1Lithium.getColor()),
        new Color(ColorUtils.bee2Lithium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.LITHIUM), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SALT), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(SALTY, ALUMINIUM, 5);
            tMutation.requireResource("frameGtLithium");
        }),
    ELECTROTINE(GTBranchDefinition.RAREMETAL, "Electrotine", false, new Color(ColorUtils.bee1Electrotine.getColor()),
        new Color(ColorUtils.bee2Electrotine.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ELECTROTINE), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.REDSTONE), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, GOLD, 5);
            tMutation.requireResource("blockElectrotine");
        }),
    // Sulfur bee, Humidity: normal, Temperature: Hot, Parents: PEAT & Ash, Mutationrate: 15%, Combrate: 80%
    SULFUR(GTBranchDefinition.RAREMETAL, "Sulfur", false, new Color(ColorUtils.bee1Sulfur.getColor()),
        new Color(ColorUtils.bee2Sulfur.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SULFUR), 0.70f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.PYRITE), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.FIRESTONE), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.NORMAL),
        dis -> dis.registerMutation(ASH, PEAT, 15)),

    INDIUM(GTBranchDefinition.RAREMETAL, "Indium", false, new Color(ColorUtils.bee1Indium.getColor()),
        new Color(ColorUtils.bee2Indium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.INDIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(LEAD, OSMIUM, 1);
            tMutation.requireResource("blockIndium");
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(39, "Venus")); // Venus Dim
            // Earlier mutation for geting ichorium
            tMutation = dis.registerMutation(SILVER, OSMIUM, 1);
            tMutation.requireResource("blockArcanite");
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(60, "Bedrock")); // Thaumic Tinkerer
            // Bedrock Dim
        }),
    Netherite(GTBranchDefinition.RAREMETAL, "Netherite", false, new Color(ColorUtils.bee1Netherite.getColor()),
        new Color(ColorUtils.bee2Netherite.getColor()), beeSpecies -> {
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.NETHERITE), 0.1f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature((HELLISH));
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectCreeper);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.NETHER);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Demonic"), DIAMOND, 3);
            tMutation.restrictTemperature(HELLISH);
            tMutation.requireResource(GregTechAPI.sBlockMetal9, 12);
        }),

    // IC2
    COOLANT(GTBranchDefinition.IC2, "Coolant", false, new Color(ColorUtils.bee1Coolant.getColor()),
        new Color(ColorUtils.bee2Coolant.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 4), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.COOLANT), 0.15f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOW);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.SNOW);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectGlacial);
        }, dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(FORESTRY, "Icy"), getSpecies(FORESTRY, "Glacial"), 10);
            tMutation.requireResource(
                Block.getBlockFromItem(
                    GTModHandler.getModItem(IndustrialCraft2.ID, "fluidCoolant", 1)
                        .getItem()),
                0);
            tMutation.restrictTemperature(ICY);
        }),
    ENERGY(GTBranchDefinition.IC2, "Energy", false, new Color(ColorUtils.bee1Energy.getColor()),
        new Color(ColorUtils.bee2Energy.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 12), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ENERGY), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(WARM);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectIgnition);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.DOWN_2);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.NETHER);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.AVERAGE);
        }, dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(FORESTRY, "Demonic"), getSpecies(EXTRABEES, "volcanic"), 10);
            tMutation.requireResource(
                Block.getBlockFromItem(
                    GTModHandler.getModItem(IndustrialCraft2.ID, "fluidHotCoolant", 1)
                        .getItem()),
                0);
            tMutation.addMutationCondition(new GTBees.BiomeIDMutationCondition(128, "Boneyard Biome")); // Boneyard
                                                                                                        // Biome
        }),
    LAPOTRON(GTBranchDefinition.IC2, "Lapotron", false, new Color(ColorUtils.bee1Lapotron.getColor()),
        new Color(ColorUtils.bee2Lapotron.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.LAPIS), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ENERGY), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.LAPOTRON), 0.10f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectIgnition);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.SNOW);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.AVERAGE);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(LAPIS, ENERGY, 6);
            tMutation.requireResource("blockLapis");
            tMutation.restrictTemperature(ICY);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(28, "Moon")); // moon dim
        }),
    PYROTHEUM(GTBranchDefinition.IC2, "Pyrotheum", false, new Color(ColorUtils.bee1Pyrotheum.getColor()),
        new Color(ColorUtils.bee2Pyrotheum.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.ENERGY), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.PYROTHEUM), 0.15f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FASTEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectIgnition);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.NETHER);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.AVERAGE);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, ENERGY, 4);
            tMutation.restrictTemperature(HELLISH);
        }),
    CRYOTHEUM(GTBranchDefinition.IC2, "Cryotheum", false, new Color(ColorUtils.bee1Cryotheum.getColor()),
        new Color(ColorUtils.bee2Cryotheum.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.BLIZZ), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.CRYOTHEUM), 0.20f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectSnowing);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.SNOW);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.AVERAGE);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, COOLANT, 4);
            tMutation.restrictTemperature(ICY);
        }),
    Explosive(GTBranchDefinition.IC2, "explosive", false, new Color(ColorUtils.bee1Explosive.getColor()),
        new Color(ColorUtils.bee2Explosive.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getIC2Item("industrialTnt", 1L), 0.2f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectSnowing);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.SNOW);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.AVERAGE);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(FIRESTONE, COAL, 4);
            tMutation.requireResource(GameRegistry.findBlock(IndustrialCraft2.ID, "blockITNT"), 0);
        }),
    // Alloy
    REDALLOY(GTBranchDefinition.GTALLOY, "RedAlloy", false, new Color(ColorUtils.bee1Redalloy.getColor()),
        new Color(ColorUtils.bee2Redalloy.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.REDALLOY), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(COPPER, REDSTONE, 10);
            tMutation.requireResource("blockRedAlloy");
        }),
    REDSTONEALLOY(GTBranchDefinition.GTALLOY, "RedStoneAlloy", false,
        new Color(ColorUtils.bee1Redstonealloy.getColor()), new Color(ColorUtils.bee2Redstonealloy.getColor()),
        beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.REDSTONEALLOY), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, REDALLOY, 8);
            tMutation.requireResource("blockRedstoneAlloy");
        }),
    CONDUCTIVEIRON(GTBranchDefinition.GTALLOY, "ConductiveIron", false,
        new Color(ColorUtils.bee1Conductiveiron.getColor()), new Color(ColorUtils.bee2Conductiveiron.getColor()),
        beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.CONDUCTIVEIRON), 0.15f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(WARM);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONEALLOY, IRON, 8);
            tMutation.requireResource("blockConductiveIron");
        }),
    ENERGETICALLOY(GTBranchDefinition.GTALLOY, "EnergeticAlloy", false,
        new Color(ColorUtils.bee1Energeticalloy.getColor()), new Color(ColorUtils.bee2Energeticalloy.getColor()),
        beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ENERGETICALLOY), 0.15f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONEALLOY, getSpecies(FORESTRY, "Demonic"), 9);
            tMutation.requireResource("blockEnergeticAlloy");
        }),
    VIBRANTALLOY(GTBranchDefinition.GTALLOY, "VibrantAlloy", false, new Color(ColorUtils.bee1Vibrantalloy.getColor()),
        new Color(ColorUtils.bee2Vibrantalloy.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.VIBRANTALLOY), 0.15f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.FAST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(ENERGETICALLOY, getSpecies(FORESTRY, "Phantasmal"), 6);
            tMutation.requireResource("blockVibrantAlloy");
            tMutation.restrictTemperature(HOT, HELLISH);
        }),
    ELECTRICALSTEEL(GTBranchDefinition.GTALLOY, "ElectricalSteel", false,
        new Color(ColorUtils.bee1Electricalsteel.getColor()), new Color(ColorUtils.bee2Electricalsteel.getColor()),
        beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ELECTRICALSTEEL), 0.15f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(STEEL, getSpecies(FORESTRY, "Demonic"), 9);
            tMutation.requireResource("blockElectricalSteel");
        }),
    DARKSTEEL(GTBranchDefinition.GTALLOY, "DarkSteel", false, new Color(ColorUtils.bee1Darksteel.getColor()),
        new Color(ColorUtils.bee2Darksteel.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.DARKSTEEL), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(ELECTRICALSTEEL, getSpecies(FORESTRY, "Demonic"), 7);
            tMutation.requireResource("blockDarkSteel");
        }),
    PULSATINGIRON(GTBranchDefinition.GTALLOY, "PulsatingIron", false,
        new Color(ColorUtils.bee1Pulsatingiron.getColor()), new Color(ColorUtils.bee2Pulsatingiron.getColor()),
        beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.PULSATINGIRON), 0.15f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDALLOY, getSpecies(FORESTRY, "Ended"), 9);
            tMutation.requireResource("blockPulsatingIron");
        }),
    STAINLESSSTEEL(GTBranchDefinition.GTALLOY, "StainlessSteel", false,
        new Color(ColorUtils.bee1Stainlesssteel.getColor()), new Color(ColorUtils.bee2Stainlesssteel.getColor()),
        beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.STEEL), 0.10f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.STAINLESSSTEEL), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.CHROME), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectIgnition);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(CHROME, STEEL, 9);
            tMutation.requireResource("blockStainlessSteel");
        }),
    ENDERIUM(GTBranchDefinition.GTALLOY, "Enderium", false, new Color(ColorUtils.bee1Enderium.getColor()),
        new Color(ColorUtils.bee2Enderium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ENDERIUM), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.CHROME), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, GTBees.speedBlinding);
            AlleleHelper.instance.set(template, EFFECT, getEffect(EXTRABEES, "teleport"));
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(PLATINUM, getSpecies(FORESTRY, "Phantasmal"), 3);
            tMutation.requireResource("blockEnderium");
        }),
    BEDROCKIUM(GTBranchDefinition.GTALLOY, "Bedrockium", false, new Color(ColorUtils.bee1Bedrockium.getColor()),
        new Color(ColorUtils.bee2Bedrockium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.BEDROCKIUM), 0.55f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOW);
            AlleleHelper.instance.set(template, EFFECT, getEffect(EXTRABEES, "gravity"));
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(Explosive, DIAMOND, 2);
            if (ExtraUtilities.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(ExtraUtilities.ID, "block_bedrockium"), 0);
        }),

    // thaumic
    THAUMIUMDUST(GTBranchDefinition.THAUMIC, "ThaumiumDust", true, new Color(ColorUtils.bee1Thaumiumdust.getColor()),
        new Color(ColorUtils.bee2Thaumiumdust.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.THAUMIUMDUST), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_2);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectExploration);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.JUNGLE);
        }, dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(MAGICBEES, "TCFire"), getSpecies(FORESTRY, "Edenic"), 10);
            tMutation.requireResource("blockThaumium");
            tMutation.addMutationCondition(new GTBees.BiomeIDMutationCondition(192, "Magical Forest")); // magical
                                                                                                        // forest
        }),
    THAUMIUMSHARD(GTBranchDefinition.THAUMIC, "ThaumiumShard", true, new Color(ColorUtils.bee1Thaumiumshard.getColor()),
        new Color(ColorUtils.bee2Thaumiumshard.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.THAUMIUMDUST), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.THAUMIUMSHARD), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.SNOW);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectGlacial);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(THAUMIUMDUST, getSpecies(MAGICBEES, "TCWater"), 10);
            tMutation.addMutationCondition(new GTBees.BiomeIDMutationCondition(192, "Magical Forest")); // magical
                                                                                                        // forest
        }),
    AMBER(GTBranchDefinition.THAUMIC, "Amber", true, new Color(ColorUtils.bee1Amber.getColor()),
        new Color(ColorUtils.bee2Amber.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.AMBER), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(THAUMIUMDUST, STICKYRESIN, 10);
            tMutation.requireResource("blockAmber");
        }),
    QUICKSILVER(GTBranchDefinition.THAUMIC, "Quicksilver", true, new Color(ColorUtils.bee1Quicksilver.getColor()),
        new Color(ColorUtils.bee2Quicksilver.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.QUICKSILVER), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.JUNGLE);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectMiasmic);
        }, dis -> dis.registerMutation(THAUMIUMDUST, SILVER, 10)),
    SALISMUNDUS(GTBranchDefinition.THAUMIC, "SalisMundus", true, new Color(ColorUtils.bee1Salismundus.getColor()),
        new Color(ColorUtils.bee2Salismundus.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SALISMUNDUS), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.JUNGLE);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectMiasmic);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(THAUMIUMDUST, THAUMIUMSHARD, 8);
            tMutation.addMutationCondition(new GTBees.BiomeIDMutationCondition(192, "Magical Forest")); // magical
                                                                                                        // forest
        }),
    TAINTED(GTBranchDefinition.THAUMIC, "Tainted", true, new Color(ColorUtils.bee1Tainted.getColor()),
        new Color(ColorUtils.bee2Tainted.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.TAINTED), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            AlleleHelper.instance.set(template, TOLERANT_FLYER, true);
            AlleleHelper.instance.set(template, FERTILITY, Fertility.LOW);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(THAUMIUMDUST, THAUMIUMSHARD, 7);
            tMutation.addMutationCondition(new GTBees.BiomeIDMutationCondition(193, "Tainted Land")); // Tainted Land
        }),
    MITHRIL(GTBranchDefinition.THAUMIC, "Mithril", true, new Color(ColorUtils.bee1Mithril.getColor()),
        new Color(ColorUtils.bee2Mithril.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.PLATINUM), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.MITHRIL), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            AlleleHelper.instance.set(template, TOLERANT_FLYER, true);
            AlleleHelper.instance.set(template, FERTILITY, Fertility.LOW);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        }, new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(IO, PLATINUM, 7);
                tMutation.requireResource(GregTechAPI.sBlockMetal4, 10);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(36, "IO")); // IO Dim
            }
        }),
    ASTRALSILVER(GTBranchDefinition.THAUMIC, "AstralSilver", true, new Color(ColorUtils.bee1Astralsilver.getColor()),
        new Color(ColorUtils.bee2Astralsilver.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SILVER), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ASTRALSILVER), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            AlleleHelper.instance.set(template, TOLERANT_FLYER, true);
            AlleleHelper.instance.set(template, FERTILITY, Fertility.LOW);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(SILVER, IRON, 3);
            tMutation.requireResource(GregTechAPI.sBlockMetal1, 6);
        }),
    THAUMINITE(GTBranchDefinition.THAUMIC, "Thauminite", true, new Color(ColorUtils.bee1Thauminite.getColor()),
        new Color(ColorUtils.bee2Thauminite.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(MagicBees.ID, "comb", 1, 19), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.THAUMINITE), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOW);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "TCOrder"), THAUMIUMDUST, 8);
            if (ThaumicBases.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(ThaumicBases.ID, "thauminiteBlock"), 0);
        }),
    SHADOWMETAL(GTBranchDefinition.THAUMIC, "ShadowMetal", true, new Color(ColorUtils.bee1Shadowmetal.getColor()),
        new Color(ColorUtils.bee2Shadowmetal.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(MagicBees.ID, "comb", 1, 20), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SHADOWMETAL), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOW);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
        }, dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(MAGICBEES, "TCChaos"), getSpecies(MAGICBEES, "TCVoid"), 6);
            if (TaintedMagic.isModLoaded()) {
                tMutation.requireResource("blockShadow");
            }
        }),
    DIVIDED(GTBranchDefinition.THAUMIC, "Unstable", true, new Color(ColorUtils.bee1Divided.getColor()),
        new Color(ColorUtils.bee2Divided.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 61), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.DIVIDED), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOW);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(DIAMOND, IRON, 3);
            if (ExtraUtilities.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(ExtraUtilities.ID, "decorativeBlock1"), 5);
        }),
    CAELESTIS(GTBranchDefinition.THAUMIC, "Caelestis", true, new Color(ColorUtils.bee1Caelestis.getColor()),
        new Color(ColorUtils.bee2Caelestis.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.CAELESTISRED), 0.60f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.CAELESTISBLUE), 0.60f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.CAELESTISGREEN), 0.60f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOW);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
        }, dis -> dis.registerMutation(DIAMOND, DIVIDED, 10)),
    SPARKELING(GTBranchDefinition.THAUMIC, "NetherStar", true, new Color(ColorUtils.bee1Sparkeling.getColor()),
        new Color(ColorUtils.bee2Sparkeling.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(MagicBees.ID, "miscResources", 1, 3), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SPARKLING), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.DOWN_2);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.NETHER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectAggressive);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.AVERAGE);
        }, dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(MAGICBEES, "Withering"), getSpecies(MAGICBEES, "Draconic"), 1);
            tMutation.requireResource(GregTechAPI.sBlockGem3, 3);
            tMutation.addMutationCondition(new GTBees.BiomeIDMutationCondition(9, "END Biome")); // sky end biome
        }),

    ESSENTIA(GTBranchDefinition.THAUMIC, "Essentia", "Reanimus", true, new Color(ColorUtils.bee1Essentia.getColor()),
        new Color(ColorUtils.bee2Essentia.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(MagicBees.ID, "miscResources", 1, 3), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.DOWN_2);
            AlleleHelper.instance.set(template, CAVE_DWELLING, true);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.VANILLA);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectReanimation);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(SHADOWMETAL, SPARKELING, 5);
            if (Thaumcraft.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 6);
        }),

    DRAKE(GTBranchDefinition.THAUMIC, "Drake", true, new Color(ColorUtils.bee1Drake.getColor()),
        new Color(ColorUtils.bee2Drake.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.DRACONIC), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.AWAKENEDDRACONIUM), 0.20f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setJubilanceProvider(JubilanceMegaApiary.instance);
        }, template -> {
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.DOWN_3);
            AlleleHelper.instance.set(template, CAVE_DWELLING, false);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.END);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectDrunkard);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(ESSENTIA, THAUMINITE, 5);
            GregTechAPI.sGTCompleteLoad.add(() -> tMutation.requireResource(GregTechAPI.sBlockCasings5, 8));
        }),

    // radioctive
    URANIUM(GTBranchDefinition.RADIOACTIVE, "Uranium", true, new Color(ColorUtils.bee1Uranium.getColor()),
        new Color(ColorUtils.bee2Uranium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.URANIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setNocturnal();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Avenging"), PLATINUM, 3);
            tMutation.requireResource(GregTechAPI.sBlockMetal7, 14);
        }),
    PLUTONIUM(GTBranchDefinition.RADIOACTIVE, "Plutonium", true, new Color(ColorUtils.bee1Plutonium.getColor()),
        new Color(ColorUtils.bee2Plutonium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.LEAD), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.PLUTONIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(URANIUM, EMERALD, 3);
            tMutation.requireResource(GregTechAPI.sBlockMetal5, 13);
        }),
    NAQUADAH(GTBranchDefinition.RADIOACTIVE, "Naquadah", false, new Color(ColorUtils.bee1Naquadah.getColor()),
        new Color(ColorUtils.bee2Naquadah.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.NAQUADAH), 0.15f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(PLUTONIUM, IRIDIUM, 3);
            tMutation.requireResource(GregTechAPI.sBlockMetal4, 12);
        }),
    NAQUADRIA(GTBranchDefinition.RADIOACTIVE, "Naquadria", false, new Color(ColorUtils.bee1Naquadria.getColor()),
        new Color(ColorUtils.bee2Naquadria.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.NAQUADAH), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.NAQUADRIA), 0.15f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(PLUTONIUM, IRIDIUM, 8, 10);
            tMutation.requireResource(GregTechAPI.sBlockMetal4, 15);
        }),
    DOB(GTBranchDefinition.RADIOACTIVE, "DOB", false, new Color(ColorUtils.bee1Dob.getColor()),
        new Color(ColorUtils.bee2Dob.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.DOB), 0.75f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NAQUADAH, THAUMIUMSHARD, 2);
            if (AdvancedSolarPanel.isModLoaded())
                tMutation.requireResource(GregTechAPI.sBlockMachines, MetaTileEntityIDs.SOLAR_PANEL_HV.ID);
            tMutation.addMutationCondition(new GTBees.BiomeIDMutationCondition(9, "END Biome")); // sky end biome
        }),
    THORIUM(GTBranchDefinition.RADIOACTIVE, "Thorium", false, new Color(ColorUtils.bee1Thorium.getColor()),
        new Color(ColorUtils.bee2Thorium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.THORIUM), 0.75f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setNocturnal();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IMutationCustom tMutation = dis.registerMutation(COAL, URANIUM, 3)
                .setIsSecret();
            tMutation.requireResource(GregTechAPI.sBlockMetal7, 5);
        }),
    LUTETIUM(GTBranchDefinition.RADIOACTIVE, "Lutetium", false, new Color(ColorUtils.bee1Lutetium.getColor()),
        new Color(ColorUtils.bee2Lutetium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.LUTETIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IMutationCustom tMutation = dis.registerMutation(THORIUM, getSpecies(EXTRABEES, "rotten"), 1)
                .setIsSecret();
            tMutation.requireResource(GregTechAPI.sBlockMetal4, 3);
        }),
    AMERICIUM(GTBranchDefinition.RADIOACTIVE, "Americium", false, new Color(ColorUtils.bee1Americium.getColor()),
        new Color(ColorUtils.bee2Americium.getColor()), beeSpecies -> {
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.AMERICIUM), 0.075f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
            // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
            beeSpecies.setJubilanceProvider(JubilanceMegaApiary.instance);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IMutationCustom tMutation = dis.registerMutation(LUTETIUM, CHROME, 5, 4)
                .setIsSecret();
            tMutation.requireResource(GregTechAPI.sBlockMetal1, 2);
        }),
    NEUTRONIUM(GTBranchDefinition.RADIOACTIVE, "Neutronium", false, new Color(ColorUtils.bee1Neutronium.getColor()),
        new Color(ColorUtils.bee2Neutronium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.NEUTRONIUM), 0.02f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
        }, dis -> {
            IMutationCustom tMutation = dis.registerMutation(NAQUADRIA, AMERICIUM, 2, 2)
                .setIsSecret();
            tMutation.requireResource(GregTechAPI.sBlockMetal5, 2);
        }),
    // Twilight
    NAGA(GTBranchDefinition.TWILIGHT, "Naga", true, new Color(ColorUtils.bee1Naga.getColor()),
        new Color(ColorUtils.bee2Naga.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SALISMUNDUS), 0.02f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.NAGA), 0.10f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(MAGICBEES, "Eldritch"), getSpecies(FORESTRY, "Imperial"), 8);
            tMutation.restrictHumidity(DAMP);
        }),
    LICH(GTBranchDefinition.TWILIGHT, "Lich", true, new Color(ColorUtils.bee1Lich.getColor()),
        new Color(ColorUtils.bee2Lich.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SALISMUNDUS), 0.04f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.LICH), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "Supernatural"), NAGA, 7);
            tMutation.restrictHumidity(ARID);
        }),
    HYDRA(GTBranchDefinition.TWILIGHT, "Hydra", true, new Color(ColorUtils.bee1Hydra.getColor()),
        new Color(ColorUtils.bee2Hydra.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SALISMUNDUS), 0.06f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.HYDRA), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(LICH, getSpecies(MAGICBEES, "TCFire"), 6);
            tMutation.addMutationCondition(new GTBees.BiomeIDMutationCondition(138, "Undergarden")); // undergarden
                                                                                                     // biome
        }),
    URGHAST(GTBranchDefinition.TWILIGHT, "UrGhast", true, new Color(ColorUtils.bee1Urghast.getColor()),
        new Color(ColorUtils.bee2Urghast.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SALISMUNDUS), 0.08f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.URGHAST), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
            beeSpecies.setNocturnal();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(HYDRA, THAUMIUMDUST, 5);
            tMutation.requireResource("blockThaumium");
            tMutation.restrictTemperature(HELLISH);
        }),
    SNOWQUEEN(GTBranchDefinition.TWILIGHT, "SnowQueen", true, new Color(ColorUtils.bee1Snowqueen.getColor()),
        new Color(ColorUtils.bee2Snowqueen.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SALISMUNDUS), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SNOWQUEEN), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setHasEffect();
            beeSpecies.setNocturnal();
        }, template -> {}, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(URGHAST, SALISMUNDUS, 4);
            if (ThaumicBases.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(ThaumicBases.ID, "blockSalisMundus"), 0);
            tMutation.restrictTemperature(ICY);
        }),
    // HEE
    ENDDUST(GTBranchDefinition.HEE, "End Dust", true, new Color(ColorUtils.bee1Enddust.getColor()),
        new Color(ColorUtils.bee2Enddust.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ENDDUST), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Ended"), STAINLESSSTEEL, 8);

            tMutation.restrictHumidity(ARID);
            if (HardcoreEnderExpansion.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "end_powder_ore"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    ENDIUM(GTBranchDefinition.HEE, "Endium", true, new Color(ColorUtils.bee1Endium.getColor()),
        new Color(ColorUtils.bee2Endium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ENDIUM), 0.10f);
            beeSpecies.addSpecialty(GTBees.propolis.getStackForType(PropolisType.Endium), 0.15f);
            beeSpecies.addSpecialty(GTBees.drop.getStackForType(DropType.ENDERGOO), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Ended"), THAUMIUMDUST, 8);
            tMutation.restrictHumidity(ARID);
            if (HardcoreEnderExpansion.isModLoaded()) tMutation.requireResource("blockHeeEndium");
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    STARDUST(GTBranchDefinition.HEE, "Star Dust", true, new Color(ColorUtils.bee1Stardust.getColor()),
        new Color(ColorUtils.bee2Stardust.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.STARDUST), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, GTBees.speedBlinding);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWER);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Ended"), ZINC, 8);
            tMutation.restrictHumidity(ARID);
            if (HardcoreEnderExpansion.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "stardust_ore"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    ECTOPLASMA(GTBranchDefinition.HEE, "Ectoplasma", true, new Color(ColorUtils.bee1Ectoplasma.getColor()),
        new Color(ColorUtils.bee2Ectoplasma.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ECTOPLASMA), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Ended"), ENDDUST, 5);
            tMutation.restrictHumidity(ARID);
            if (HardcoreEnderExpansion.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "spooky_log"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    ARCANESHARDS(GTBranchDefinition.HEE, "Arcane Shards", true, new Color(ColorUtils.bee1Arcaneshards.getColor()),
        new Color(ColorUtils.bee2Arcaneshards.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ARCANESHARD), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONG);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(THAUMIUMSHARD, ENDDUST, 5);
            tMutation.restrictHumidity(ARID);
            if (HardcoreEnderExpansion.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "laboratory_floor"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    DRAGONESSENCE(GTBranchDefinition.HEE, "Dragonessence", true, new Color(ColorUtils.bee1Dragonessence.getColor()),
        new Color(ColorUtils.bee2Dragonessence.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.DRAGONESSENCE), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectBeatific);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_3);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_3);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(ECTOPLASMA, ARCANESHARDS, 4);
            tMutation.restrictHumidity(ARID);
            if (HardcoreEnderExpansion.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "essence_altar"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    FIREESSENCE(GTBranchDefinition.HEE, "Fireessence", true, new Color(ColorUtils.bee1Fireessence.getColor()),
        new Color(ColorUtils.bee2Fireessence.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.FIREESSENSE), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.ELONGATED);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectIgnition);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_3);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.DOWN_3);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(FIRESTONE, ARCANESHARDS, 4);
            tMutation.restrictHumidity(ARID);
            if (HardcoreEnderExpansion.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "essence_altar"), 2);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    ENDERMANHEAD(GTBranchDefinition.HEE, "EndermanHead", true, new Color(ColorUtils.bee1Endermanhead.getColor()),
        new Color(ColorUtils.bee2Endermanhead.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ENDERMAN), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, EFFECT, getEffect(EXTRABEES, "teleport"));
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.UP_1);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(ENDERIUM, STARDUST, 4);
            tMutation.restrictHumidity(ARID);
            if (HardcoreEnderExpansion.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "ender_goo"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    SILVERFISH(GTBranchDefinition.HEE, "Silverfisch", true, new Color(ColorUtils.bee1Silverfish.getColor()),
        new Color(ColorUtils.bee2Silverfish.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.SILVERFISH), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, EFFECT, getEffect(MAGICBEES, "SlowSpeed"));
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.DOWN_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.DOWN_1);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(ECTOPLASMA, STARDUST, 5);
            tMutation.restrictHumidity(ARID);
            if (HardcoreEnderExpansion.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "ender_goo"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    RUNE(GTBranchDefinition.HEE, "Rune", true, new Color(ColorUtils.bee1Rune.getColor()),
        new Color(ColorUtils.bee2Rune.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.RUNEI), 0.025f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.RUNEII), 0.0125f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, GTBees.superLife);
            AlleleHelper.instance.set(template, EFFECT, getEffect(MAGICBEES, "SlowSpeed"));
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
        }, dis -> {
            IMutationCustom tMutation = dis.registerMutation(DRAGONESSENCE, STARDUST, 2)
                .setIsSecret();
            tMutation.restrictHumidity(ARID);
            if (EnderStorage.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(EnderStorage.ID, "enderChest"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    // Walrus Bee, 100% Combchance, Parents: Catty and Watery
    WALRUS(GTBranchDefinition.PLANET, "Walrus", true, new Color(ColorUtils.bee1Walrus.getColor()),
        new Color(ColorUtils.bee2Walrus.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.WALRUS), 1.00f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FASTEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
        }, dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(MAGICBEES, "Watery"), getSpecies(MAGICBEES, "Catty"), 45, 2);
            if (ExtraCells2.isModLoaded()) {
                tMutation.requireResource(GameRegistry.findBlock(ExtraCells2.ID, "walrus"), 0);
            }
        }),
    MACHINIST(GTBranchDefinition.ORGANIC, "Machinist", true, new Color(ColorUtils.bee1Machinist.getColor()),
        new Color(ColorUtils.bee2Machinist.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MACHINIST), 0.2f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FASTEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, GTFlowers.FLAMING);
            AlleleHelper.instance.set(template, FERTILITY, Fertility.MAXIMUM);
            AlleleHelper.instance.set(template, EFFECT, getEffect(GREGTECH, "MachineBoost"));
        }, dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(FORESTRY, "Industrious"), getSpecies(FORESTRY, "Imperial"), 1);
            tMutation.addMutationCondition(new GTBees.ActiveGTMachineMutationCondition());
        }),
    // Space Bees
    SPACE(GTBranchDefinition.SPACE, "Space", true, new Color(ColorUtils.bee1Space.getColor()),
        new Color(ColorUtils.bee2Space.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SPACE), 0.02f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
        }, template -> {}, dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(FORESTRY, "Industrious"), getSpecies(FORESTRY, "Heroic"), 10);
            tMutation.restrictTemperature(ICY);
        }),
    METEORICIRON(GTBranchDefinition.SPACE, "MeteoricIron", true, new Color(ColorUtils.bee1Meteoriciron.getColor()),
        new Color(ColorUtils.bee2Meteoriciron.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SPACE), 0.04f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.METEORICIRON), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
        }, template -> {}, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(SPACE, IRON, 9);
            tMutation.requireResource(GregTechAPI.sBlockMetal4, 7);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(28, "Moon")); // Moon Dim
        }),
    DESH(GTBranchDefinition.SPACE, "Desh", false, new Color(ColorUtils.bee1Desh.getColor()),
        new Color(ColorUtils.bee2Desh.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SPACE), 0.06f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.DESH), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
        }, template -> AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectIgnition), new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(MARS, TITANIUM, 9);
                tMutation.requireResource(GregTechAPI.sBlockMetal2, 12);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(29, "Mars")); // Mars Dim
            }
        }),
    LEDOX(GTBranchDefinition.SPACE, "Ledox", false, new Color(ColorUtils.bee1Ledox.getColor()),
        new Color(ColorUtils.bee2Ledox.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SPACE), 0.10f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.LEDOX), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, EFFECT, getEffect(FORESTRY, "Glacial")), new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(CALLISTO, LEAD, 7);
                if (NewHorizonsCoreMod.isModLoaded())
                    tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "Ledox"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(35, "Europa")); // Europa Dim
            }
        }),
    CALLISTOICE(GTBranchDefinition.SPACE, "CallistoIce", false, new Color(ColorUtils.bee1Callistoice.getColor()),
        new Color(ColorUtils.bee2Callistoice.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SPACE), 0.10f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.CALLISTOICE), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, EFFECT, getEffect(FORESTRY, "Glacial")), new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(CALLISTO, getSpecies(EXTRABEES, "freezing"), 7);
                if (NewHorizonsCoreMod.isModLoaded())
                    tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "CallistoColdIce"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(45, "Callisto")); // Callisto
                // Dim
            }
        }),
    MYTRYL(GTBranchDefinition.SPACE, "Mytryl", false, new Color(ColorUtils.bee1Mytryl.getColor()),
        new Color(ColorUtils.bee2Mytryl.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SPACE), 0.16f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.MYTRYL), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {}, new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(IO, MITHRIL, 6);
                if (NewHorizonsCoreMod.isModLoaded())
                    tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "Mytryl"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(36, "IO")); // IO Dim
            }
        }),
    QUANTIUM(GTBranchDefinition.SPACE, "Quantium", false, new Color(ColorUtils.bee1Quantium.getColor()),
        new Color(ColorUtils.bee2Quantium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SPACE), 0.16f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.QUANTIUM), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {}, new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(VENUS, OSMIUM, 6);
                if (NewHorizonsCoreMod.isModLoaded())
                    tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "Quantinum"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(39, "Venus")); // Venus Dim
            }
        }),
    ORIHARUKON(GTBranchDefinition.SPACE, "Oriharukon", false, new Color(ColorUtils.bee1Oriharukon.getColor()),
        new Color(ColorUtils.bee2Oriharukon.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SPACE), 0.26f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.ORIHARUKON), 0.10f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setHasEffect();
        }, template -> {}, new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(LEAD, OBERON, 5);
                if (GalaxySpace.isModLoaded())
                    tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "metalsblock"), 6);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(46, "Oberon")); // Oberon Dim
            }
        }),
    INFUSEDGOLD(GTBranchDefinition.SPACE, "Infused Gold", false, new Color(ColorUtils.bee1Infusedgold.getColor()),
        new Color(ColorUtils.bee2Infusedgold.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.GOLD), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.INFUSEDGOLD), 0.30f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {}, new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(GOLD, HAUMEA, 5);
                tMutation.requireResource(GregTechAPI.sBlockMetal3, 10);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(83, "Haumea")); // Haumea Dim
            }
        }),
    MYSTERIOUSCRYSTAL(GTBranchDefinition.SPACE, "MysteriousCrystal", false,
        new Color(ColorUtils.bee1Mysteriouscrystal.getColor()), new Color(ColorUtils.bee2Mysteriouscrystal.getColor()),
        beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SPACE), 0.42f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.MYSTERIOUSCRYSTAL), 0.30f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {}, new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(ENCELADUS, EMERALD, 3);
                if (NewHorizonsCoreMod.isModLoaded()) tMutation
                    .requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "MysteriousCrystalBlock"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(41, "Enceladus")); // Enceladus
                // Dim
            }
        }),
    BLACKPLUTONIUM(GTBranchDefinition.SPACE, "BlackPlutonium", false,
        new Color(ColorUtils.bee1Blackplutonium.getColor()), new Color(ColorUtils.bee2Blackplutonium.getColor()),
        beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SPACE), 0.68f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.BLACKPLUTONIUM), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {}, new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(PLUTO, PLUTONIUM, 2);
                if (NewHorizonsCoreMod.isModLoaded())
                    tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "BlackPlutonium"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(49, "Pluto")); // Pluto Dim
            }
        }),
    TRINIUM(GTBranchDefinition.SPACE, "Trinium", false, new Color(ColorUtils.bee1Trinium.getColor()),
        new Color(ColorUtils.bee2Trinium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.TRINIUM), 0.75f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.QUANTIUM), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, SPEED, GTBees.speedBlinding), new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(ENCELADUS, IRIDIUM, 4);
                tMutation.requireResource(GregTechAPI.sBlockMetal4, 9);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(41, "Enceladus")); // Enceladus Dim
            }
        }),
    // Planet Line
    MOON(GTBranchDefinition.PLANET, "Moon", false, new Color(ColorUtils.bee1Moon.getColor()),
        new Color(ColorUtils.bee2Moon.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MOON), 0.35f);
            if (NewHorizonsCoreMod.isModLoaded())
                beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MoonStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(SPACE, CLAY, 25);
            if (GalacticraftCore.isModLoaded()) {
                tMutation.requireResource(GameRegistry.findBlock(GalacticraftCore.ID, "tile.moonBlock"), 4);
            }
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(28, "Moon")); // Moon Dim
        }),
    MARS(GTBranchDefinition.PLANET, "Mars", false, new Color(ColorUtils.bee1Mars.getColor()),
        new Color(ColorUtils.bee2Mars.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MARS), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MarsStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(MOON, IRON, 20);
            if (GalacticraftMars.isModLoaded()) {
                tMutation.requireResource(GameRegistry.findBlock(GalacticraftMars.ID, "tile.mars"), 5);
            }
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(29, "Mars")); // Mars Dim
        }),
    PHOBOS(GTBranchDefinition.PLANET, "Phobos", true, new Color(ColorUtils.bee1Phobos.getColor()),
        new Color(ColorUtils.bee2Phobos.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MARS), 0.25f);
            if (NewHorizonsCoreMod.isModLoaded())
                beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "PhobosStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(MARS, MOON, 20);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "phobosblocks"), 2);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(38, "Phobos")); // Phobos Dim
        }),
    DEIMOS(GTBranchDefinition.PLANET, "Deimos", true, new Color(ColorUtils.bee1Deimos.getColor()),
        new Color(ColorUtils.bee2Deimos.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MARS), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "DeimosStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(MARS, SPACE, 20);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "deimosblocks"), 1);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(40, "Deimos")); // Deimos Dim
        }),
    CERES(GTBranchDefinition.PLANET, "Ceres", true, new Color(ColorUtils.bee1Ceres.getColor()),
        new Color(ColorUtils.bee2Ceres.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.JUPITER), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CeresStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(MARS, METEORICIRON, 20);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "ceresblocks"), 1);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(42, "Ceres")); // Ceres Dim
        }),
    JUPITER(GTBranchDefinition.PLANET, "Jupiter", false, new Color(ColorUtils.bee1Jupiter.getColor()),
        new Color(ColorUtils.bee2Jupiter.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.JUPITER), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CallistoStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(Materials.CallistoIce.getDust(1), 0.05f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "IoStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EuropaStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EuropaIceDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "GanymedeStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(MARS, DESH, 15);
            if (NewHorizonsCoreMod.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "Ledox"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(30, "Asteroids")); // Asteroid Dim
        }),
    IO(GTBranchDefinition.PLANET, "IO", true, new Color(ColorUtils.bee1Io.getColor()),
        new Color(ColorUtils.bee2Io.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.JUPITER), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "IoStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, getSpecies(EXTRABEES, "volcanic"), 15);
            tMutation.restrictTemperature(HELLISH);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "ioblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(36, "IO")); // IO Dim
        }),
    EUROPA(GTBranchDefinition.PLANET, "Europa", true, new Color(ColorUtils.bee1Europa.getColor()),
        new Color(ColorUtils.bee2Europa.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.JUPITER), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EuropaStoneDust", 1, 0), 0.10f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EuropaIceDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, IRON, 15);
            tMutation.restrictTemperature(ICY);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "europagrunt"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(35, "Europa")); // Europa Dim
        }),
    GANYMEDE(GTBranchDefinition.PLANET, "Ganymede", true, new Color(ColorUtils.bee1Ganymede.getColor()),
        new Color(ColorUtils.bee2Ganymede.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.JUPITER), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "GanymedeStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(COLD);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, TITANIUM, 15);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "ganymedeblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(43, "Ganymede")); // Ganymede Dim
        }),
    CALLISTO(GTBranchDefinition.PLANET, "Callisto", true, new Color(ColorUtils.bee1Callisto.getColor()),
        new Color(ColorUtils.bee2Callisto.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.JUPITER), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CallistoStoneDust", 1, 0), 0.10f);
            beeSpecies.addSpecialty(Materials.CallistoIce.getDust(1), 0.10f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, getSpecies(EXTRABEES, "artic"), 15);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "callistoblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(45, "Callisto")); // Callisto Dim
        }),
    SATURN(GTBranchDefinition.PLANET, "Saturn", false, new Color(ColorUtils.bee1Saturn.getColor()),
        new Color(ColorUtils.bee2Saturn.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SATURN), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "TitanStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EnceladusStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EnceladusIceDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, LEDOX, 25, 2);
            if (NewHorizonsCoreMod.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "Quantinum"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(30, "Asteriods")); // Asteriods Dim
        }),
    ENCELADUS(GTBranchDefinition.PLANET, "Enceladus", true, new Color(ColorUtils.bee1Enceladus.getColor()),
        new Color(ColorUtils.bee2Enceladus.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SATURN), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EnceladusStoneDust", 1, 0), 0.10f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EnceladusIceDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(SATURN, CHROME, 25, 2);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "enceladusblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(41, "Enceladus")); // Enceladus Dim
        }),
    TITAN(GTBranchDefinition.PLANET, "Titan", true, new Color(ColorUtils.bee1Titan.getColor()),
        new Color(ColorUtils.bee2Titan.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SATURN), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "TitanStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(SATURN, NICKEL, 25, 2);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "titanblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(44, "Titan")); // Titan Dim
        }),
    URANUS(GTBranchDefinition.PLANET, "Uranus", false, new Color(ColorUtils.bee1Uranus.getColor()),
        new Color(ColorUtils.bee2Uranus.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.URANUS), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MirandaStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "OberonStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(SATURN, TRINIUM, 10);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "metalsblock"), 6);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(30, "Asteroids")); // Asteroids Dim
        }),
    MIRANDA(GTBranchDefinition.PLANET, "Miranda", true, new Color(ColorUtils.bee1Miranda.getColor()),
        new Color(ColorUtils.bee2Miranda.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.URANUS), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MirandaStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(URANUS, TIN, 10);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "mirandablocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(86, "Miranda")); // Miranda Dim
        }),
    OBERON(GTBranchDefinition.PLANET, "Oberon", true, new Color(ColorUtils.bee1Oberon.getColor()),
        new Color(ColorUtils.bee2Oberon.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.URANUS), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "OberonStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(URANUS, IRIDIUM, 10);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "oberonblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(46, "Oberon")); // Oberon Dim
        }),
    NEPTUNE(GTBranchDefinition.PLANET, "Neptune", false, new Color(ColorUtils.bee1Neptune.getColor()),
        new Color(ColorUtils.bee2Neptune.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.NEPTUNE), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "ProteusStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "TritonStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(URANUS, ORIHARUKON, 7);
            if (NewHorizonsCoreMod.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "MysteriousCrystalBlock"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(30, "Asteroids")); // Asteroids Dim
        }),
    PROTEUS(GTBranchDefinition.PLANET, "Proteus", true, new Color(ColorUtils.bee1Proteus.getColor()),
        new Color(ColorUtils.bee2Proteus.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.NEPTUNE), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "ProteusStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setNocturnal();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NEPTUNE, COPPER, 7);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "proteusblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(47, "Proteus")); // Proteus Dim
        }),
    TRITON(GTBranchDefinition.PLANET, "Triton", true, new Color(ColorUtils.bee1Triton.getColor()),
        new Color(ColorUtils.bee2Triton.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.NEPTUNE), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "TritonStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NEPTUNE, GOLD, 7);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "tritonblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(48, "Triton")); // Triton Dim
        }),
    PLUTO(GTBranchDefinition.PLANET, "Pluto", false, new Color(ColorUtils.bee1Pluto.getColor()),
        new Color(ColorUtils.bee2Pluto.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.PLUTO), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "PlutoStoneDust", 1, 0), 0.10f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "PlutoIceDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NEPTUNE, PLUTONIUM, 5);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "plutoblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(49, "Pluto")); // Pluto Dim
        }),
    HAUMEA(GTBranchDefinition.PLANET, "Haumea", false, new Color(ColorUtils.bee1Haumea.getColor()),
        new Color(ColorUtils.bee2Haumea.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.HAUMEA), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HaumeaStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(PLUTO, NAQUADAH, 7, 2);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "haumeablocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(83, "Haumea")); // Haumea Dim
        }),
    MAKEMAKE(GTBranchDefinition.PLANET, "MakeMake", false, new Color(ColorUtils.bee1Makemake.getColor()),
        new Color(ColorUtils.bee2Makemake.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MAKEMAKE), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MakeMakeStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(PLUTO, NAQUADRIA, 7, 2);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "makemakegrunt"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(25, "MakeMake")); // MakeMake Dim
        }),
    CENTAURI(GTBranchDefinition.PLANET, "Centauri", false, new Color(ColorUtils.bee1Centauri.getColor()),
        new Color(ColorUtils.bee2Centauri.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.CENTAURI), 0.35f);
            beeSpecies
                .addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CentauriASurfaceDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(MAKEMAKE, DESH, 3);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "acentauribbgrunt"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(33, "Kuiper Belt")); // Kuiper Belt Dim
        }),
    ACENTAURI(GTBranchDefinition.PLANET, "aCentauri", false, new Color(ColorUtils.bee1Acentauri.getColor()),
        new Color(ColorUtils.bee2Acentauri.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.CENTAURI), 0.25f);
            beeSpecies
                .addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CentauriASurfaceDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), new Consumer<>() {

            @Override
            public void accept(GTBeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(CENTAURI, INFINITYCATALYST, 3);
                if (GalaxySpace.isModLoaded())
                    tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "acentauribbgrunt"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(31, "aCentauri")); // aCentauri Dim
            }
        }),
    TCETI(GTBranchDefinition.PLANET, "tCeti", false, new Color(ColorUtils.bee1Tceti.getColor()),
        new Color(ColorUtils.bee2Tceti.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.TCETI), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "TCetiEStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(MAKEMAKE, HAUMEA, 5, 2);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "tcetieblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(33, "Kuiper Belt")); // Kuiper Belt Dim
        }),
    TCETIE(GTBranchDefinition.PLANET, "tCetiE", false, new Color(ColorUtils.bee1Tcetie.getColor()),
        new Color(ColorUtils.bee2Tcetie.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.TCETI), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "TCetiEStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(TCETI, getSpecies(MAGICBEES, "TCWater"), 5, 2);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "tcetieblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(85, "tCeti E")); // tCeti E Dim
        }),
    // Parents: TCETI and TCETIE, 25% combchance, needs Damp Humidity and Normal temperature. Is Mycophilic
    SEAWEED(GTBranchDefinition.PLANET, "SeaWeed", true, new Color(ColorUtils.bee1Seaweed.getColor()),
        new Color(ColorUtils.bee2Seaweed.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.SEAWEED), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "TCetiEStoneDust", 1, 0), 0.15f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.DOWN_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.DOWN_1);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectMycophilic);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(TCETI, TCETIE, 5, 2);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "tcetieblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(85, "tCeti E")); // tCeti E Dim
        }),

    BARNARDA(GTBranchDefinition.PLANET, "Barnarda", false, new Color(ColorUtils.bee1Barnarda.getColor()),
        new Color(ColorUtils.bee2Barnarda.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.BARNARDA), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "BarnardaEStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "BarnardaFStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(MAKEMAKE, THORIUM, 3, 2);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "barnardaEgrunt"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(33, "Kuiper Belt")); // Kuiper Belt Dim
        }),
    BARNARDAC(GTBranchDefinition.PLANET, "BarnardaC", false, new Color(ColorUtils.bee1Barnardac.getColor()),
        new Color(ColorUtils.bee2Barnardac.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.BARNARDA), 0.25f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, EFFECT, getEffect(GREGTECH, "Treetwister"));
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(BARNARDA, AMERICIUM, 3, 2);
            if (GalaxySpace.isModLoaded()) {
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "barnardaEgrunt"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(32, "Barnarda C")); // Barnarda C
                                                                                                         // Dim
            }
        }),
    BARNARDAE(GTBranchDefinition.PLANET, "BarnardaE", false, new Color(ColorUtils.bee1Barnardae.getColor()),
        new Color(ColorUtils.bee2Barnardae.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.BARNARDA), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "BarnardaEStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(BARNARDA, DIVIDED, 3, 2);
            if (GalaxySpace.isModLoaded()) {
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "barnardaEgrunt"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(81, "Barnard E")); // "Barnard E
                                                                                                        // Dim
            }
        }),
    BARNARDAF(GTBranchDefinition.PLANET, "BarnardaF", false, new Color(ColorUtils.bee1Barnardaf.getColor()),
        new Color(ColorUtils.bee2Barnardaf.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.BARNARDA), 0.25f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "BarnardaFStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(BARNARDA, NEUTRONIUM, 3, 2);
            if (GalaxySpace.isModLoaded()) {
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "barnardaFgrunt"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(82, "Barnard F")); // "Barnard F
                                                                                                        // Dim
            }
        }),
    VEGA(GTBranchDefinition.PLANET, "Vega", false, new Color(ColorUtils.bee1Vega.getColor()),
        new Color(ColorUtils.bee2Vega.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.VEGA), 0.35f);
            beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "VegaBStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(MAKEMAKE, NAQUADAH, 2);
            if (GalaxySpace.isModLoaded()) {
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "vegabgrunt"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(33, "Kuiper Belt")); // Kuiper Belt
                                                                                                          // Dim
            }
        }),
    VEGAB(GTBranchDefinition.PLANET, "VegaB", false, new Color(ColorUtils.bee1Vegab.getColor()),
        new Color(ColorUtils.bee2Vegab.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.VEGA), 0.35f);
            if (NewHorizonsCoreMod.isModLoaded())
                beeSpecies.addSpecialty(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "VegaBStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(COLD);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(VEGA, NAQUADRIA, 2);
            if (GalaxySpace.isModLoaded()) {
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "vegabgrunt"), 0);
                tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(84, "VegaB")); // VegaB Dim
            }
        }),
    MERCURY(GTBranchDefinition.PLANET, "Mercury", false, new Color(ColorUtils.bee1Mercury.getColor()),
        new Color(ColorUtils.bee2Mercury.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MERCURY), 0.35f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, TUNGSTEN, 25, 2);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "mercuryblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(37, "Mercury")); // Mercury Dim
        }),
    VENUS(GTBranchDefinition.PLANET, "Venus", false, new Color(ColorUtils.bee1Venus.getColor()),
        new Color(ColorUtils.bee2Venus.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.VENUS), 0.35f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, MITHRIL, 25, 2);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "venusblocks"), 0);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(39, "Venus")); // Venus Dim
        }),

    // Infinity Line
    COSMICNEUTRONIUM(GTBranchDefinition.PLANET, "CosmicNeutronium", false,
        new Color(ColorUtils.bee1Cosmicneutronium.getColor()), new Color(ColorUtils.bee2Cosmicneutronium.getColor()),
        beeSpecies -> {
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.COSMICNEUTRONIUM), 0.375f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
            // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
            beeSpecies.setJubilanceProvider(JubilanceMegaApiary.instance);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NEUTRONIUM, BARNARDAF, 7, 10);
            if (Avaritia.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(Avaritia.ID, "Resource_Block"), 0);
        }),
    INFINITYCATALYST(GTBranchDefinition.PLANET, "InfinityCatalyst", false,
        new Color(ColorUtils.bee1Infinitycatalyst.getColor()), new Color(ColorUtils.bee2Infinitycatalyst.getColor()),
        beeSpecies -> {
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.INFINITYCATALYST), 0.015f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
            // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
            beeSpecies.setJubilanceProvider(JubilanceMegaApiary.instance);
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, EFFECT, getEffect(EXTRABEES, "blindness"));
        }, dis -> {
            IMutationCustom tMutation = dis.registerMutation(DOB, COSMICNEUTRONIUM, 3, 10)
                .setIsSecret();
            if (Avaritia.isModLoaded()) {
                tMutation.requireResource(GameRegistry.findBlock(Avaritia.ID, "Resource_Block"), 1);
            }
        }),
    INFINITY(GTBranchDefinition.PLANET, "Infinity", false, new Color(ColorUtils.bee1Infinity.getColor()),
        new Color(ColorUtils.bee2Infinity.getColor()), beeSpecies -> {
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.INFINITY), 0.015f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
            // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
            beeSpecies.setJubilanceProvider(JubilanceMegaApiary.instance);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(INFINITYCATALYST, COSMICNEUTRONIUM, 1, 10);
            if (AvaritiaAddons.isModLoaded()) {
                tMutation.requireResource(GameRegistry.findBlock(AvaritiaAddons.ID, "InfinityChest"), 0);
            }
        }),
    KEVLAR(GTBranchDefinition.IC2, "Kevlar", false, new Color(ColorUtils.bee1Kevlar.getColor()),
        new Color(ColorUtils.bee2Kevlar.getColor()), beeSpecies -> {
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.KEVLAR), 0.075f);
            beeSpecies.addSpecialty(Materials.Kevlar.getNuggets(1), 0.01f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(COLD);
            beeSpecies.setHasEffect();
            beeSpecies.setJubilanceProvider(JubilanceMegaApiary.instance);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectSnowing);
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
            AlleleHelper.instance.set(template, NOCTURNAL, true);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.SNOW);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.AVERAGE);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(OIL, INFINITYCATALYST, 4);
            // UHV Replicator (UU-Matter)
            GregTechAPI.sGTCompleteLoad.add(() -> tMutation.requireResource(GregTechAPI.sBlockMachines, 11003));
        }),

    // Noble Gas Line
    // Helium bee, Humidity: normal, Temperature: Icy, Parents: Space & Mars, Mutationrate: 10%, Combrate: 50%
    HELIUM(GTBranchDefinition.NOBLEGAS, "Helium", false, new Color(ColorUtils.bee1Helium.getColor()),
        new Color(ColorUtils.bee2Helium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.HELIUM), 0.35f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "Watery"), ENDDUST, 10);
            tMutation.restrictTemperature(ICY);
        }),
    // Argon bee, Humidity: normal, Temperature: Icy, Parents: Helium & Phobos, Mutationrate: 8%, Combrate: 50%
    ARGON(GTBranchDefinition.NOBLEGAS, "Argon", false, new Color(ColorUtils.bee1Argon.getColor()),
        new Color(ColorUtils.bee2Argon.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.ARGON), 0.35f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(HELIUM, LICH, 8);
            tMutation.restrictTemperature(ICY);
        }),
    // Neon bee, Humidity: normal, Temperature: Icy, Parents: Xenon & Ceres, Mutationrate: 6%, Combrate: 50%
    NEON(GTBranchDefinition.NOBLEGAS, "Neon", false, new Color(ColorUtils.bee1Neon.getColor()),
        new Color(ColorUtils.bee2Neon.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.NEON), 0.35f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(ARGON, HYDRA, 6);
            tMutation.restrictTemperature(ICY);
        }),
    // Krypton bee, Humidity: normal, Temperature: Icy, Parents: Neon & Jupiter, Mutationrate: 4%, Combrate: 50%
    KRYPTON(GTBranchDefinition.NOBLEGAS, "Krypton", false, new Color(ColorUtils.bee1Krypton.getColor()),
        new Color(ColorUtils.bee2Krypton.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.KRYPTON), 0.35f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NEON, URGHAST, 4);
            tMutation.restrictTemperature(ICY);
        }),
    // Xenon bee, Humidity: normal, Temperature: Icy, Parents: Argon & Deimos, Mutationrate: 6%, Combrate: 50%
    XENON(GTBranchDefinition.NOBLEGAS, "Xenon", false, new Color(ColorUtils.bee1Xenon.getColor()),
        new Color(ColorUtils.bee2Xenon.getColor()), beeSpecies -> {
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.XENON), 0.525f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
            // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
            beeSpecies.setJubilanceProvider(JubilanceMegaApiary.instance);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(KRYPTON, SNOWQUEEN, 2);
            tMutation.restrictTemperature(ICY);
        }),
    // Oxygen bee, Humidity: normal, Temperature: Icy, Parents: Space & Callisto, Mutationrate: 15%, Combrate: 50%
    OXYGEN(GTBranchDefinition.NOBLEGAS, "Oxygen", false, new Color(ColorUtils.bee1Oxygen.getColor()),
        new Color(ColorUtils.bee2Oxygen.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.OXYGEN), 0.45f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.HYDROGEN), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(HELIUM, DRAGONESSENCE, 15);
            tMutation.restrictTemperature(ICY);
        }),
    // Hydrogen bee, Humidity: normal, Temperature: Icy, Parents: Oxygen & Watery, Mutationrate: 15%, Combrate: 50%
    HYDROGEN(GTBranchDefinition.NOBLEGAS, "Oxygen", false, new Color(ColorUtils.bee1Hydrogen.getColor()),
        new Color(ColorUtils.bee2Hydrogen.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.HYDROGEN), 0.45f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.NITROGEN), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(OXYGEN, getSpecies(MAGICBEES, "Watery"), 15);
            tMutation.restrictTemperature(ICY);
        }),
    // Nitrogen bee, Humidity: normal, Temperature: Icy, Parents: Oxygen & Hydrogen, Mutationrate: 15%, Combrate: 50%
    NITROGEN(GTBranchDefinition.NOBLEGAS, "Nitrogen", false, new Color(ColorUtils.bee1Nitrogen.getColor()),
        new Color(ColorUtils.bee2Nitrogen.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.NITROGEN), 0.45f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.FLUORINE), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(OXYGEN, HYDROGEN, 15);
            tMutation.restrictTemperature(ICY);
        }),
    // Fluorine bee, Humidity: normal, Temperature: Icy, Parents: Nitrogen & Hydrogen, Mutationrate: 15%, Combrate: 50%
    FLUORINE(GTBranchDefinition.NOBLEGAS, "Fluorine", false, new Color(ColorUtils.bee1Fluorine.getColor()),
        new Color(ColorUtils.bee2Fluorine.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.FLUORINE), 0.45f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.OXYGEN), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NITROGEN, HYDROGEN, 15);
            tMutation.restrictTemperature(ICY);
        }),
    // Europium line, needed after fluorine definition
    RAREEARTH(GTBranchDefinition.RAREMETAL, "RareEarth", false, new Color(ColorUtils.bee1Rareearth.getColor()),
        new Color(ColorUtils.bee2Rareearth.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.RAREEARTH), 0.20f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.REFINEDRAREEARTH), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(NORMAL);
            // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
            beeSpecies.setJubilanceProvider(JubilanceMegaApiary.instance);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST),
        dis -> dis.registerMutation(FLUORINE, REDSTONE, 10)),

    NEODYMIUM(GTBranchDefinition.RAREMETAL, "Neodymium", false, new Color(ColorUtils.bee1Neodymium.getColor()),
        new Color(ColorUtils.bee2Neodymium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.RAREEARTH), 0.15f);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.NEODYMIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(RAREEARTH, IRON, 10);
            tMutation.requireResource(GregTechAPI.sBlockMetal5, 0);
        }),
    EUROPIUM(GTBranchDefinition.RAREMETAL, "Europium", false, new Color(ColorUtils.bee1Europium.getColor()),
        new Color(ColorUtils.bee2Europium.getColor()), beeSpecies -> {
            beeSpecies.addProduct(WerkstoffMaterialPool.EuropiumIIIOxide.get(OrePrefixes.dust, 1), 0.10F);
            beeSpecies.addSpecialty(GTBees.combs.getStackForType(CombType.EUROPIUM), 0.075f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
            // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
            beeSpecies.setJubilanceProvider(JubilanceMegaApiary.instance);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NEODYMIUM, HYDROGEN, 5, 4);
            tMutation.requireResource(GregTechAPI.sBlockMetal3, 3);
        }),
    // infused Shards line
    AIR(GTBranchDefinition.INFUSEDSHARD, "Air", false, new Color(ColorUtils.bee1Air.getColor()),
        new Color(ColorUtils.bee2Air.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.INFUSEDAER), 0.30f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis
                .registerMutation(getSpecies(MAGICBEES, "Supernatural"), getSpecies(MAGICBEES, "Windy"), 15);
            tMutation.restrictTemperature(HOT);
            if (Thaumcraft.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 0);
        }),
    FIRE(GTBranchDefinition.INFUSEDSHARD, "Fire", false, new Color(ColorUtils.bee1Fire.getColor()),
        new Color(ColorUtils.bee2Fire.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.INFUSEDIGNIS), 0.30f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "Supernatural"), AIR, 15);
            tMutation.restrictTemperature(HELLISH);
            if (Thaumcraft.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 1);
        }),
    WATER(GTBranchDefinition.INFUSEDSHARD, "Water", false, new Color(ColorUtils.bee1Water.getColor()),
        new Color(ColorUtils.bee2Water.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.INFUSEDAQUA), 0.30f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(FIRE, AIR, 15);
            tMutation.restrictTemperature(ICY);
            if (Thaumcraft.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 2);
        }),
    EARTH(GTBranchDefinition.INFUSEDSHARD, "Earth", false, new Color(ColorUtils.bee1Earth.getColor()),
        new Color(ColorUtils.bee2Earth.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.INFUSEDTERRA), 0.30f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(WATER, FIRE, 15);
            tMutation.restrictTemperature(WARM);
            if (Thaumcraft.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 3);
        }),
    ORDER(GTBranchDefinition.INFUSEDSHARD, "Order", false, new Color(ColorUtils.bee1Order.getColor()),
        new Color(ColorUtils.bee2Order.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.INFUSEDORDO), 0.30f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(EARTH, FIRE, 15);
            tMutation.restrictTemperature(ICY);
            if (Thaumcraft.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 4);
        }),
    CHAOS(GTBranchDefinition.INFUSEDSHARD, "Chaos", false, new Color(ColorUtils.bee1Chaos.getColor()),
        new Color(ColorUtils.bee2Chaos.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.INFUSEDPERDITIO), 0.30f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(ORDER, FIRE, 15);
            tMutation.restrictTemperature(ICY);
            if (Thaumcraft.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 5);
        }),
    NETHERSHARD(GTBranchDefinition.INFUSEDSHARD, "NetherShard", false, new Color(ColorUtils.bee1Nethershard.getColor()),
        new Color(ColorUtils.bee2Nethershard.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.NETHERSHARD), 0.30f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(CHAOS, FIRE, 15);
            GregTechAPI.sGTCompleteLoad.add(() -> tMutation.requireResource(GregTechAPI.sBlockGem3, 3));
        }),
    ENDSHARD(GTBranchDefinition.INFUSEDSHARD, "EnderShard", false, new Color(ColorUtils.bee1Endshard.getColor()),
        new Color(ColorUtils.bee2Endshard.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.ENDERSHARD), 0.30f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NETHERSHARD, ENDDUST, 15);
            tMutation.restrictTemperature(ICY);
            GregTechAPI.sGTCompleteLoad.add(() -> tMutation.requireResource(GregTechAPI.sBlockGem1, 7));
        }),
    // Organic branch 2.0
    UNKNOWNWATER(GTBranchDefinition.ORGANIC, "UnknownLiquid", false, new Color(ColorUtils.bee1Unknownwater.getColor()),
        new Color(ColorUtils.bee2Unknownwater.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.UNKNOWNLIQUID), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST),
        dis -> dis.registerMutation(INFINITYCATALYST, MYSTERIOUSCRYSTAL, 5)),

    // Botanic bees
    MANASTEEL(GTBranchDefinition.BOTANIC, "ManaSteel", true, new Color(ColorUtils.bee1Manasteel.getColor()),
        new Color(ColorUtils.bee2Manasteel.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MANASTEEL), 0.30f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST),
        dis -> dis.registerMutation(getSpecies(MAGICBEES, "BotAlfheim"), STEEL, 15)
            .requireResource("blockManasteel")),
    MMM(GTBranchDefinition.BOTANIC, "MMM", true, new Color(ColorUtils.bee1Mmm.getColor()),
        new Color(ColorUtils.bee2Mmm.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.MMM), 0.33f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, SPEED, GTBees.speedBlinding);
        }, dis -> dis.registerMutation(MANASTEEL, MACHINIST, 1)
            .requireResource("blockManaDiamond")),
    ELVEN(GTBranchDefinition.BOTANIC, "Elven", true, new Color(ColorUtils.bee1Elven.getColor()),
        new Color(ColorUtils.bee2Elven.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.ELVEN), 0.33f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST),
        dis -> dis.registerMutation(MANASTEEL, getSpecies(MAGICBEES, "TCVoid"), 5)
            .requireResource("blockElvenElementium")),
    TERRASTEEL(GTBranchDefinition.BOTANIC, "TerraSteel", true, new Color(ColorUtils.bee1Terrasteel.getColor()),
        new Color(ColorUtils.bee2Terrasteel.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.TERRASTEEL), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER),
        dis -> dis.registerMutation(ELVEN, SHADOWMETAL, 5)
            .requireResource("blockTerrasteel")),
    GAIASPIRIT(GTBranchDefinition.BOTANIC, "GAIASPIRIT", true, new Color(ColorUtils.bee1Gaiaspirit.getColor()),
        new Color(ColorUtils.bee2Gaiaspirit.getColor()), beeSpecies -> {
            beeSpecies.addProduct(GTBees.combs.getStackForType(CombType.GAIASPIRIT), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, LIFESPAN, GTBees.blinkLife);
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
        }, dis -> dis.registerMutation(NAQUADAH, TERRASTEEL, 1, 3)
            .requireResource("frameGtGaiaSpirit")),

    // Endgame bees
    JAEGERMEISTER(GTBranchDefinition.ENDGAME, "JaegerMeister", false,
        new Color(ColorUtils.bee1Jaegermeister.getColor()), new Color(ColorUtils.bee2Jaegermeister.getColor()),
        beeSpecies -> {
            if (CropsNH.isModLoaded()) {
                beeSpecies.addProduct(GTModHandler.getModItem(CropsNH.ID, "bottledAlcohol", 1L, 8), 0.01f);
            }
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(INFINITYCATALYST, NAQUADRIA, 5);
            tMutation.requireResource(GregTechAPI.sBlockMachines, 4684);
            tMutation.addMutationCondition(new GTBees.DimensionMutationCondition(100, "Deep Dark")); // Deep Dark
                                                                                                     // dim
        });

    private final GTBranchDefinition branch;
    private final GTAlleleBeeSpecies species;
    private final Consumer<GTAlleleBeeSpecies> mSpeciesProperties;
    private final Consumer<IAllele[]> mAlleles;
    private final Consumer<GTBeeDefinition> mMutations;
    private IAllele[] template;
    private IBeeGenome genome;

    GTBeeDefinition(GTBranchDefinition branch, String binomial, boolean dominant, Color primary, Color secondary,
        Consumer<GTAlleleBeeSpecies> aSpeciesProperties, Consumer<IAllele[]> aAlleles,
        Consumer<GTBeeDefinition> aMutations) {
        this.mAlleles = aAlleles;
        this.mMutations = aMutations;
        this.mSpeciesProperties = aSpeciesProperties;
        String lowercaseName = this.toString()
            .toLowerCase(Locale.ENGLISH);
        String species = WordUtils.capitalize(lowercaseName);

        String uid = "gregtech.bee.species" + species;
        String description = "for.description." + lowercaseName;
        String name = "for.bees.species." + lowercaseName;
        GTLanguageManager.addStringLocalization("for.bees.species." + lowercaseName, species);

        String authority = GTLanguageManager.getTranslation("for.bees.authority." + lowercaseName);
        if (authority.equals("for.bees.authority." + lowercaseName)) {
            authority = "GTNH";
        }
        this.branch = branch;
        this.species = new GTAlleleBeeSpecies(
            uid,
            dominant,
            name,
            authority,
            description,
            branch.getBranch(),
            binomial,
            primary,
            secondary);
    }

    GTBeeDefinition(GTBranchDefinition branch, String binomial, String locName, boolean dominant, Color primary,
        Color secondary, Consumer<GTAlleleBeeSpecies> aSpeciesProperties, Consumer<IAllele[]> aAlleles,
        Consumer<GTBeeDefinition> aMutations) {
        this.mAlleles = aAlleles;
        this.mMutations = aMutations;
        this.mSpeciesProperties = aSpeciesProperties;
        String lowercaseName = this.toString()
            .toLowerCase(Locale.ENGLISH);
        String species = WordUtils.capitalize(lowercaseName);

        String uid = "gregtech.bee.species" + species;
        String description = "for.description." + lowercaseName;
        String name = "for.bees.species." + lowercaseName;
        GTLanguageManager.addStringLocalization("for.bees.species." + lowercaseName, locName);

        String authority = GTLanguageManager.getTranslation("for.bees.authority." + lowercaseName);
        if (authority.equals("for.bees.authority." + lowercaseName)) {
            authority = "GTNH";
        }
        this.branch = branch;
        this.species = new GTAlleleBeeSpecies(
            uid,
            dominant,
            name,
            authority,
            description,
            branch.getBranch(),
            binomial,
            primary,
            secondary);
    }

    public static void initBees() {
        for (GTBeeDefinition bee : values()) {
            bee.init();
        }
        for (GTBeeDefinition bee : values()) {
            bee.registerMutations();
        }
    }

    static IAlleleBeeEffect getEffect(byte modid, String name) {
        String s = switch (modid) {
            case EXTRABEES -> "extrabees.effect." + name;
            case GENDUSTRY -> "gendustry.effect." + name;
            case MAGICBEES -> "magicbees.effect" + name;
            case GREGTECH -> "gregtech.effect" + name;
            default -> "forestry.effect" + name;
        };
        IAlleleBeeEffect allele = (IAlleleBeeEffect) AlleleManager.alleleRegistry.getAllele(s);
        if (allele == null) {
            GTMod.GT_FML_LOGGER.warn("Attempted to get unknown bee effect: " + s);
            allele = GTAlleleEffect.FORESTRY_BASE_EFFECT;
        }
        return allele;
    }

    static IAlleleFlowers getFlowers(byte modid, String name) {
        String s = switch (modid) {
            case EXTRABEES -> "extrabees.flower." + name;
            case GENDUSTRY -> "gendustry.flower." + name;
            case MAGICBEES -> "magicbees.flower" + name;
            case GREGTECH -> "gregtech.flower" + name;
            default -> "forestry.flowers" + name;
        };
        IAlleleFlowers allele = (IAlleleFlowers) AlleleManager.alleleRegistry.getAllele(s);
        if (allele == null) {
            GTMod.GT_FML_LOGGER.warn("Attempted to get unknown bee flower: " + s);
        }
        return allele;
    }

    private static IAlleleBeeSpecies getSpecies(byte modid, String name) {
        String s = switch (modid) {
            case EXTRABEES -> "extrabees.species." + name;
            case GENDUSTRY -> "gendustry.bee." + name;
            case MAGICBEES -> "magicbees.species" + name;
            case GREGTECH -> "gregtech.species" + name;
            default -> "forestry.species" + name;
        };
        IAlleleBeeSpecies ret = (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele(s);
        if (ret == null) {
            GTMod.GT_FML_LOGGER.warn("Attempted to get unknown bee species: " + s);
            ret = NAQUADRIA.species;
        }

        return ret;
    }

    private void setSpeciesProperties(GTAlleleBeeSpecies beeSpecies) {
        this.mSpeciesProperties.accept(beeSpecies);
    }

    private void setAlleles(IAllele[] template) {
        this.mAlleles.accept(template);
    }

    private void registerMutations() {
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

    private IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    private IBeeMutationCustom registerMutation(GTBeeDefinition parent1, IAlleleBeeSpecies parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    private IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, GTBeeDefinition parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    private IBeeMutationCustom registerMutation(GTBeeDefinition parent1, GTBeeDefinition parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    /**
     * Diese neue Funtion erlaubt Mutationsraten unter 1%. Setze dazu die Mutationsrate als Bruch mit chance /
     * chanceDivider This new function allows Mutation percentages under 1%. Set them as a fraction with chance /
     * chanceDivider
     */
    private IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2, int chance,
        float chanceDivider) {
        return new GTBeeMutation(parent1, parent2, this.getTemplate(), chance, chanceDivider);
    }

    private IBeeMutationCustom registerMutation(GTBeeDefinition parent1, IAlleleBeeSpecies parent2, int chance,
        float chanceDivider) {
        return registerMutation(parent1.species, parent2, chance, chanceDivider);
    }

    private IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, GTBeeDefinition parent2, int chance,
        float chanceDivider) {
        return registerMutation(parent1, parent2.species, chance, chanceDivider);
    }

    private IBeeMutationCustom registerMutation(GTBeeDefinition parent1, GTBeeDefinition parent2, int chance,
        float chanceDivider) {
        return registerMutation(parent1.species, parent2, chance, chanceDivider);
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

    public GTAlleleBeeSpecies getSpecies() {
        return species;
    }
}
