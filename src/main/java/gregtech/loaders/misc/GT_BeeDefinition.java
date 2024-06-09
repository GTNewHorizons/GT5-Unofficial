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
import static gregtech.api.enums.Mods.CropsPlusPlus;
import static gregtech.api.enums.Mods.EnderStorage;
import static gregtech.api.enums.Mods.ExtraBees;
import static gregtech.api.enums.Mods.ExtraCells2;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.MagicBees;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.Mods.TaintedMagic;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.ThaumicBases;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.loaders.misc.GT_BeeDefinitionReference.EXTRABEES;
import static gregtech.loaders.misc.GT_BeeDefinitionReference.FORESTRY;
import static gregtech.loaders.misc.GT_BeeDefinitionReference.GENDUSTRY;
import static gregtech.loaders.misc.GT_BeeDefinitionReference.GREGTECH;
import static gregtech.loaders.misc.GT_BeeDefinitionReference.MAGICBEES;

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
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_JubilanceMegaApiary;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.bees.GT_AlleleBeeSpecies;
import gregtech.common.bees.GT_Bee_Mutation;
import gregtech.common.items.CombType;
import gregtech.common.items.DropType;
import gregtech.common.items.PropolisType;
import gregtech.loaders.misc.bees.GT_Flowers;

/**
 * Bride Class for Lambdas
 */
class GT_BeeDefinitionReference {

    protected static final byte FORESTRY = 0;
    protected static final byte EXTRABEES = 1;
    protected static final byte GENDUSTRY = 2;
    protected static final byte MAGICBEES = 3;
    protected static final byte GREGTECH = 4;

    private GT_BeeDefinitionReference() {}
}

public enum GT_BeeDefinition implements IBeeDefinition {

    // organic
    CLAY(GT_BranchDefinition.ORGANIC, "Clay", true, new Color(0xC8C8DA), new Color(0x0000FF), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 0), 0.30f);
        beeSpecies.addProduct(new ItemStack(Items.clay_ball, 1), 0.15f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(BiomesOPlenty.ID, "mudball", 1, 0), 0.05f);
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
    SLIMEBALL(GT_BranchDefinition.ORGANIC, "SlimeBall", true, new Color(0x4E9E55), new Color(0x00FF15), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 15), 0.30f);
        beeSpecies.addProduct(new ItemStack(Items.slime_ball, 1), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.STICKY), 0.05f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
        if (TinkerConstruct.isModLoaded()) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(TinkerConstruct.ID, "strangeFood", 1, 0), 0.10f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem(TinkerConstruct.ID, "slime.gel", 1, 2), 0.02f);
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
    PEAT(GT_BranchDefinition.ORGANIC, "Peat", true, new Color(0x906237), new Color(0x58300B), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LIGNIE), 0.30f);
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 0), 0.15f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(Forestry.ID, "peat", 1, 0), 0.15f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(Forestry.ID, "mulch", 1, 0), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
        AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
        AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
        AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
    }, dis -> dis.registerMutation(getSpecies(FORESTRY, "Rural"), CLAY, 10)),
    STICKYRESIN(GT_BranchDefinition.ORGANIC, "StickyResin", true, new Color(0x2E8F5B), new Color(0xDCC289),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.STICKY), 0.15f);
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
    COAL(GT_BranchDefinition.ORGANIC, "Coal", true, new Color(0x666666), new Color(0x525252), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LIGNIE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.COAL), 0.15f);
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
    OIL(GT_BranchDefinition.ORGANIC, "Oil", true, new Color(0x4C4C4C), new Color(0x333333), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 0), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OIL), 0.75f);
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
    SANDWICH(GT_BranchDefinition.ORGANIC, "Sandwich", true, new Color(0x32CD32), new Color(0xDAA520), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 9), 0.15f);
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
    ASH(GT_BranchDefinition.ORGANIC, "Ash", true, new Color(0x1e1a18), new Color(0xc6c6c6), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 9), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ASH), 0.15f);
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
    APATITE(GT_BranchDefinition.ORGANIC, "Apatite", true, new Color(0xc1c1f6), new Color(0x676784), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 9), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.APATITE), 0.15f);
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
    FERTILIZER(GT_BranchDefinition.ORGANIC, "Fertilizer", true, new Color(0x7fcef5), new Color(0x654525),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 9), 0.15f);
            beeSpecies.addSpecialty(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ash, 1), 0.2f);
            beeSpecies.addSpecialty(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1), 0.2f);
            beeSpecies.addSpecialty(ItemList.FR_Fertilizer.get(1), 0.3f);
            beeSpecies.addSpecialty(ItemList.IC2_Fertilizer.get(1), 0.3f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(WARM);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FASTEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.WHEAT);
            AlleleHelper.instance.set(template, FLOWERING, Flowering.FASTER);
        }, dis -> dis.registerMutation(ASH, APATITE, 8)),
    // Phosphorus bee, Humidity: normal, Temperature: Hot, Parents: Apatite & Ash, Mutationrate: 12%, Combrate: 55%
    PHOSPHORUS(GT_BranchDefinition.ORGANIC, "Phosphorus", false, new Color(0xFFC826), new Color(0xC1C1F6),
        beeSpecies -> {
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.PHOSPHORUS), 0.35f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(APATITE, ASH, 12);
            tMutation.restrictTemperature(HOT);
            tMutation.requireResource(GameRegistry.findBlock(GregTech.ID, "gt.blockgem2"), 8);
        }),
    // Tea bee, Humidity: normal, Parents: Ash and Fertilizer, Mutationrate: 10%, combrate: 10%
    TEA(GT_BranchDefinition.ORGANIC, "Tea", false, new Color(0x65D13A), new Color(0x9a9679), beeSpecies -> {
        beeSpecies.addProduct(
            GT_ModHandler.getModItem(PamsHarvestCraft.ID, "tealeafItem", 1, ItemList.Crop_Drop_TeaLeaf.get(1)),
            0.10f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(NORMAL);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST),
        dis -> dis.registerMutation(FERTILIZER, ASH, 10)),
    // Mica bee, Humidity: normal, Parents: Silicon & PEAT, Mutationrate: 15%, Combrate: 25%
    MICA(GT_BranchDefinition.ORGANIC, "Mica", false, new Color(0xFFC826), new Color(0xC1C1F6), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MICA), 0.25f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(HOT);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(PEAT, getSpecies(MAGICBEES, "Silicon"), 15);
        tMutation.requireResource(GameRegistry.findBlock(GregTech.ID, "gt.blockcasings5"), 0);
    }),

    // gems
    REDSTONE(GT_BranchDefinition.GEM, "Redstone", true, new Color(0x7D0F0F), new Color(0xD11919), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDSTONE), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.RAREEARTH), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis
            .registerMutation(getSpecies(FORESTRY, "Industrious"), getSpecies(FORESTRY, "Demonic"), 10);
        tMutation.requireResource("blockRedstone");
    }),
    LAPIS(GT_BranchDefinition.GEM, "Lapis", true, new Color(0x1947D1), new Color(0x476CDA), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LAPIS), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis
            .registerMutation(getSpecies(FORESTRY, "Demonic"), getSpecies(FORESTRY, "Imperial"), 10);
        tMutation.requireResource("blockLapis");
    }),
    CERTUS(GT_BranchDefinition.GEM, "CertusQuartz", true, new Color(0x57CFFB), new Color(0xBBEEFF), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CERTUS), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Hermitic"), LAPIS, 10);
        tMutation.requireResource(GameRegistry.findBlock(AppliedEnergistics2.ID, "tile.BlockQuartz"), 0);
    }),
    FLUIX(GT_BranchDefinition.GEM, "FluixDust", true, new Color(0xA375FF), new Color(0xB591FF), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.FLUIX), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, LAPIS, 7);
        tMutation.requireResource(GameRegistry.findBlock(AppliedEnergistics2.ID, "tile.BlockFluix"), 0);
    }),
    DIAMOND(GT_BranchDefinition.GEM, "Diamond", false, new Color(0xCCFFFF), new Color(0xA3CCCC), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DIAMOND), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(HOT);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(CERTUS, COAL, 3);
        tMutation.requireResource("blockDiamond");
    }),
    RUBY(GT_BranchDefinition.GEM, "Ruby", false, new Color(0xE6005C), new Color(0xCC0052), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.RUBY), 0.15f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.REDGARNET), 0.05f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, DIAMOND, 5);
        tMutation.requireResource("blockRuby");
    }),
    SAPPHIRE(GT_BranchDefinition.GEM, "Sapphire", true, new Color(0x0033CC), new Color(0x00248F), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SAPPHIRE), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(CERTUS, LAPIS, 5);
        tMutation.requireResource(GregTech_API.sBlockGem2, 12);
    }),
    OLIVINE(GT_BranchDefinition.GEM, "Olivine", true, new Color(0x248F24), new Color(0xCCFFCC), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OLIVINE), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MAGNESIUM), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER),
        dis -> dis.registerMutation(CERTUS, getSpecies(FORESTRY, "Ended"), 5)),
    EMERALD(GT_BranchDefinition.GEM, "Emerald", false, new Color(0x248F24), new Color(0x2EB82E), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.EMERALD), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ALUMINIUM), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(OLIVINE, DIAMOND, 4);
        tMutation.requireResource("blockEmerald");
    }),
    REDGARNET(GT_BranchDefinition.GEM, "RedGarnet", false, new Color(0xBD4C4C), new Color(0xECCECE), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDGARNET), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.PYROPE), 0.05f);
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
    YELLOWGARNET(GT_BranchDefinition.GEM, "YellowGarnet", false, new Color(0xA3A341), new Color(0xEDEDCE),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.YELLOWGARNET), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.GROSSULAR), 0.05f);
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
    FIRESTONE(GT_BranchDefinition.GEM, "Firestone", false, new Color(0xC00000), new Color(0xFF0000), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.FIRESTONE), 0.15f);
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

    // Metal Line
    COPPER(GT_BranchDefinition.METAL, "Copper", true, new Color(0xFF6600), new Color(0xE65C00), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.COPPER), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.GOLD), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Majestic"), CLAY, 13);
        tMutation.requireResource("blockCopper");
    }),
    TIN(GT_BranchDefinition.METAL, "Tin", true, new Color(0xD4D4D4), new Color(0xDDDDDD), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TIN), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ZINC), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(CLAY, getSpecies(FORESTRY, "Diligent"), 13);
        tMutation.requireResource("blockTin");
    }),
    LEAD(GT_BranchDefinition.METAL, "Lead", true, new Color(0x666699), new Color(0xA3A3CC), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LEAD), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SULFUR), 0.05f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(WARM);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(COAL, COPPER, 13);
        tMutation.requireResource("blockLead");
    }),
    IRON(GT_BranchDefinition.METAL, "Iron", true, new Color(0xDA9147), new Color(0xDE9C59), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.IRON), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.TIN), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(TIN, COPPER, 13);
        tMutation.requireResource("blockIron");
    }),
    STEEL(GT_BranchDefinition.METAL, "Steel", true, new Color(0x808080), new Color(0x999999), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STEEL), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRON), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(WARM);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(IRON, COAL, 10);
        tMutation.requireResource(GregTech_API.sBlockMetal6, 13);
    }),
    NICKEL(GT_BranchDefinition.METAL, "Nickel", true, new Color(0x8585AD), new Color(0x8585AD), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NICKEL), 0.15f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLATINUM), 0.02f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(IRON, COPPER, 13);
        tMutation.requireResource("blockNickel");
    }),
    ZINC(GT_BranchDefinition.METAL, "Zinc", true, new Color(0xF0DEF0), new Color(0xF2E1F2), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ZINC), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.GALLIUM), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(IRON, TIN, 13);
        tMutation.requireResource("blockZinc");
    }),
    SILVER(GT_BranchDefinition.METAL, "Silver", true, new Color(0xC2C2D6), new Color(0xCECEDE), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SILVER), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SULFUR), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(LEAD, TIN, 10);
        tMutation.requireResource("blockSilver");
    }),
    CRYOLITE(GT_BranchDefinition.METAL, "Cryolite", true, new Color(0xBFEFFF), new Color(0x73B9D0), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CRYOLITE), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SILVER), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(WARM);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.FASTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(LEAD, SILVER, 9);
        tMutation.requireResource("blockCryolite");
    }),
    GOLD(GT_BranchDefinition.METAL, "Gold", true, new Color(0xEBC633), new Color(0xEDCC47), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.GOLD), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NICKEL), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(WARM);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(LEAD, COPPER, 13);
        tMutation.requireResource("blockGold");
        tMutation.restrictTemperature(HOT);
    }),
    ARSENIC(GT_BranchDefinition.METAL, "Arsenic", true, new Color(0x736C52), new Color(0x292412), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ARSENIC), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(WARM);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(ZINC, SILVER, 10);
        tMutation.requireResource("blockArsenic");
    }),

    // Rare Metals
    ALUMINIUM(GT_BranchDefinition.RAREMETAL, "Aluminium", true, new Color(0xB8B8FF), new Color(0xD6D6FF),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ALUMINIUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.BAUXITE), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NICKEL, ZINC, 9);
            tMutation.requireResource("blockAluminium");
        }),
    TITANIUM(GT_BranchDefinition.RAREMETAL, "Titanium", true, new Color(0xCC99FF), new Color(0xDBB8FF), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TITANIUM), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ALMANDINE), 0.05f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, ALUMINIUM, 5);
        tMutation.requireResource(GregTech_API.sBlockMetal7, 9);
    }),
    GLOWSTONE(GT_BranchDefinition.RAREMETAL, "Glowstone", false, new Color(0xE5CA2A), new Color(0xFFBC5E),
        beeSpecies -> {
            beeSpecies.addSpecialty(Materials.Glowstone.getDust(1), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL),
        dis -> dis.registerMutation(REDSTONE, GOLD, 10)),
    SUNNARIUM(GT_BranchDefinition.RAREMETAL, "Sunnarium", false, new Color(0xFFBC5E), new Color(0xE5CA2A),
        beeSpecies -> {
            beeSpecies.addProduct(Materials.Glowstone.getDust(1), 0.30f);
            beeSpecies.addSpecialty(Materials.Sunnarium.getDust(1), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(GLOWSTONE, GOLD, 5);
            tMutation.requireResource(GameRegistry.findBlock(GregTech.ID, "gt.blockcasings"), 15);
        }),
    CHROME(GT_BranchDefinition.RAREMETAL, "Chrome", true, new Color(0xEBA1EB), new Color(0xF2C3F2), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CHROME), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MAGNESIUM), 0.05f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(TITANIUM, RUBY, 5);
        tMutation.requireResource(GregTech_API.sBlockMetal2, 3);
    }),
    MANGANESE(GT_BranchDefinition.RAREMETAL, "Manganese", true, new Color(0xD5D5D5), new Color(0xAAAAAA),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MANGANESE), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRON), 0.05f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(TITANIUM, ALUMINIUM, 5);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 6);
        }),
    TUNGSTEN(GT_BranchDefinition.RAREMETAL, "Tungsten", false, new Color(0x5C5C8A), new Color(0x7D7DA1), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TUNGSTEN), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MOLYBDENUM), 0.05f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Heroic"), MANGANESE, 5);
        tMutation.requireResource(GregTech_API.sBlockMetal7, 11);
    }),
    PLATINUM(GT_BranchDefinition.RAREMETAL, "Platinum", false, new Color(0xE6E6E6), new Color(0xFFFFCC), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLATINUM), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRIDIUM), 0.02f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(DIAMOND, CHROME, 5);
        tMutation.requireResource("blockNickel");
    }),
    IRIDIUM(GT_BranchDefinition.RAREMETAL, "Iridium", false, new Color(0xDADADA), new Color(0xD1D1E0), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.IRIDIUM), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OSMIUM), 0.05f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.PALLADIUM), 0.30f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HELLISH);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(TUNGSTEN, PLATINUM, 5);
        tMutation.requireResource(GregTech_API.sBlockMetal3, 12);
    }),
    OSMIUM(GT_BranchDefinition.RAREMETAL, "Osmium", false, new Color(0x2B2BDA), new Color(0x8B8B8B), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OSMIUM), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRIDIUM), 0.05f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(TUNGSTEN, PLATINUM, 5);
        tMutation.requireResource(GregTech_API.sBlockMetal5, 9);
    }),
    SALTY(GT_BranchDefinition.RAREMETAL, "Salt", true, new Color(0xF0C8C8), new Color(0xFAFAFA), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SALT), 0.35f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LITHIUM), 0.05f);
        beeSpecies.addSpecialty(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Borax, 1L), 0.1f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(WARM);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(CLAY, ALUMINIUM, 5);
        tMutation.requireResource("blockSalt");
    }),
    LITHIUM(GT_BranchDefinition.RAREMETAL, "Lithium", false, new Color(0xF0328C), new Color(0xE1DCFF), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LITHIUM), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SALT), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(SALTY, ALUMINIUM, 5);
        tMutation.requireResource("frameGtLithium");
    }),
    ELECTROTINE(GT_BranchDefinition.RAREMETAL, "Electrotine", false, new Color(0x1E90FF), new Color(0x3CB4C8),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ELECTROTINE), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDSTONE), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(HOT);
        }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWER), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, GOLD, 5);
            tMutation.requireResource("blockElectrotine");
        }),
    // Sulfur bee, Humidity: normal, Temperature: Hot, Parents: PEAT & Ash, Mutationrate: 15%, Combrate: 80%
    SULFUR(GT_BranchDefinition.RAREMETAL, "Sulfur", false, new Color(0x1E90FF), new Color(0x3CB4C8), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SULFUR), 0.70f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.FIRESTONE), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.NORMAL),
        dis -> dis.registerMutation(ASH, PEAT, 15)),

    INDIUM(GT_BranchDefinition.RAREMETAL, "Indium", false, new Color(0xFFA9FF), new Color(0x8F5D99), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INDIUM), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(LEAD, OSMIUM, 1);
        tMutation.requireResource("blockIndium");
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(39, "Venus")); // Venus Dim
        // Harder mutation that isn't dim locked
        tMutation = dis.registerMutation(SILVER, OSMIUM, 1);
        tMutation.requireResource("blockCinobiteA243");
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(60, "Bedrock")); // Thaumic Tinkerer
                                                                                               // Bedrock Dim
    }),

    // IC2
    COOLANT(GT_BranchDefinition.IC2, "Coolant", false, new Color(0x144F5A), new Color(0x2494A2), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 4), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.COOLANT), 0.15f);
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
                GT_ModHandler.getModItem(IndustrialCraft2.ID, "fluidCoolant", 1)
                    .getItem()),
            0);
        tMutation.restrictTemperature(ICY);
    }),
    ENERGY(GT_BranchDefinition.IC2, "Energy", false, new Color(0xC11F1F), new Color(0xEBB9B9), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 12), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENERGY), 0.15f);
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
                GT_ModHandler.getModItem(IndustrialCraft2.ID, "fluidHotCoolant", 1)
                    .getItem()),
            0);
        tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(128, "Boneyard Biome")); // Boneyard Biome
    }),
    LAPOTRON(GT_BranchDefinition.IC2, "Lapotron", false, new Color(0x6478FF), new Color(0x1414FF), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LAPIS), 0.20f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENERGY), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LAPOTRON), 0.10f);
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
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(28, "Moon")); // moon dim
    }),
    PYROTHEUM(GT_BranchDefinition.IC2, "Pyrotheum", false, new Color(0xffebc4), new Color(0xe36400), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ENERGY), 0.20f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.PYROTHEUM), 0.15f);
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
    CRYOTHEUM(GT_BranchDefinition.IC2, "Cryotheum", false, new Color(0x2660ff), new Color(0x5af7ff), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.BLIZZ), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CRYOTHEUM), 0.20f);
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
    Explosive(GT_BranchDefinition.IC2, "explosive", false, new Color(0x7E270F), new Color(0x747474), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getIC2Item("industrialTnt", 1L), 0.2f);
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
    REDALLOY(GT_BranchDefinition.GTALLOY, "RedAlloy", false, new Color(0xE60000), new Color(0xB80000), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDALLOY), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
    }, dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(COPPER, REDSTONE, 10);
        tMutation.requireResource("blockRedAlloy");
    }),
    REDSTONEALLOY(GT_BranchDefinition.GTALLOY, "RedStoneAlloy", false, new Color(0xA50808), new Color(0xE80000),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDSTONEALLOY), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONE, REDALLOY, 8);
            tMutation.requireResource("blockRedstoneAlloy");
        }),
    CONDUCTIVEIRON(GT_BranchDefinition.GTALLOY, "ConductiveIron", false, new Color(0xCEADA3), new Color(0x817671),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CONDUCTIVEIRON), 0.15f);
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
    ENERGETICALLOY(GT_BranchDefinition.GTALLOY, "EnergeticAlloy", false, new Color(0xFF9933), new Color(0xFFAD5C),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENERGETICALLOY), 0.15f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDSTONEALLOY, getSpecies(FORESTRY, "Demonic"), 9);
            tMutation.requireResource("blockEnergeticAlloy");
        }),
    VIBRANTALLOY(GT_BranchDefinition.GTALLOY, "VibrantAlloy", false, new Color(0x86A12D), new Color(0xC4F2AE),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.VIBRANTALLOY), 0.15f);
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
    ELECTRICALSTEEL(GT_BranchDefinition.GTALLOY, "ElectricalSteel", false, new Color(0x787878), new Color(0xD8D8D8),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ELECTRICALSTEEL), 0.15f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTER);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(STEEL, getSpecies(FORESTRY, "Demonic"), 9);
            tMutation.requireResource("blockElectricalSteel");
        }),
    DARKSTEEL(GT_BranchDefinition.GTALLOY, "DarkSteel", false, new Color(0x252525), new Color(0x443B44), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DARKSTEEL), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, Speed.FAST);
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
    }, dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(ELECTRICALSTEEL, getSpecies(FORESTRY, "Demonic"), 7);
        tMutation.requireResource("blockDarkSteel");
    }),
    PULSATINGIRON(GT_BranchDefinition.GTALLOY, "PulsatingIron", false, new Color(0x6DD284), new Color(0x006600),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.PULSATINGIRON), 0.15f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.FAST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(REDALLOY, getSpecies(FORESTRY, "Ended"), 9);
            tMutation.requireResource("blockPulsatingIron");
        }),
    STAINLESSSTEEL(GT_BranchDefinition.GTALLOY, "StainlessSteel", false, new Color(0xC8C8DC), new Color(0x778899),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STEEL), 0.10f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.STAINLESSSTEEL), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CHROME), 0.05f);
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
    ENDERIUM(GT_BranchDefinition.GTALLOY, "Enderium", false, new Color(0x599087), new Color(0x2E8B57), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENDERIUM), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CHROME), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(HOT);
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, GT_Bees.speedBlinding);
        AlleleHelper.instance.set(template, EFFECT, getEffect(EXTRABEES, "teleport"));
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
    }, dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(PLATINUM, getSpecies(FORESTRY, "Phantasmal"), 3);
        tMutation.requireResource("blockEnderium");
    }),
    BEDROCKIUM(GT_BranchDefinition.GTALLOY, "Bedrockium", false, new Color(0x0C0C0C), new Color(0xC6C6C6),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.BEDROCKIUM), 0.55f);
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
    THAUMIUMDUST(GT_BranchDefinition.THAUMIC, "ThaumiumDust", true, new Color(0x7A007A), new Color(0x5C005C),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.THAUMIUMDUST), 0.20f);
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
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(192, "Magical Forest")); // magical
                                                                                                         // forest
        }),
    THAUMIUMSHARD(GT_BranchDefinition.THAUMIC, "ThaumiumShard", true, new Color(0x9966FF), new Color(0xAD85FF),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.THAUMIUMDUST), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.THAUMIUMSHARD), 0.20f);
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
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(192, "Magical Forest")); // magical
                                                                                                         // forest
        }),
    AMBER(GT_BranchDefinition.THAUMIC, "Amber", true, new Color(0xEE7700), new Color(0x774B15), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 3), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.AMBER), 0.20f);
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
    QUICKSILVER(GT_BranchDefinition.THAUMIC, "Quicksilver", true, new Color(0x7A007A), new Color(0x5C005C),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.QUICKSILVER), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.UP_1);
            AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.JUNGLE);
            AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectMiasmic);
        }, dis -> dis.registerMutation(THAUMIUMDUST, SILVER, 10)),
    SALISMUNDUS(GT_BranchDefinition.THAUMIC, "SalisMundus", true, new Color(0xF7ADDE), new Color(0x592582),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.20f);
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
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(192, "Magical Forest")); // magical
                                                                                                         // forest
        }),
    TAINTED(GT_BranchDefinition.THAUMIC, "Tainted", true, new Color(0x904BB8), new Color(0xE800FF), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 3), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.TAINTED), 0.20f);
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
        tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(193, "Tainted Land")); // Tainted Land
    }),
    MITHRIL(GT_BranchDefinition.THAUMIC, "Mithril", true, new Color(0xF0E68C), new Color(0xFFFFD2), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLATINUM), 0.20f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MITHRIL), 0.125f);
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
        public void accept(GT_BeeDefinition dis) {
            IBeeMutationCustom tMutation = dis.registerMutation(IO, PLATINUM, 7);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 10);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(36, "IO")); // IO Dim
        }
    }),
    ASTRALSILVER(GT_BranchDefinition.THAUMIC, "AstralSilver", true, new Color(0xAFEEEE), new Color(0xE6E6FF),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SILVER), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ASTRALSILVER), 0.125f);
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
            tMutation.requireResource(GregTech_API.sBlockMetal1, 6);
        }),
    THAUMINITE(GT_BranchDefinition.THAUMIC, "Thauminite", true, new Color(0x2E2D79), new Color(0x7581E0),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(MagicBees.ID, "comb", 1, 19), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.THAUMINITE), 0.125f);
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
    SHADOWMETAL(GT_BranchDefinition.THAUMIC, "ShadowMetal", true, new Color(0x100322), new Color(0x100342),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(MagicBees.ID, "comb", 1, 20), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SHADOWMETAL), 0.125f);
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
    DIVIDED(GT_BranchDefinition.THAUMIC, "Unstable", true, new Color(0xF0F0F0), new Color(0xDCDCDC), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(ExtraBees.ID, "honeyComb", 1, 61), 0.20f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DIVIDED), 0.125f);
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
    CAELESTIS(GT_BranchDefinition.THAUMIC, "Caelestis", true, new Color(0xF0F0F0), new Color(0xDCDCDC), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CAELESTISRED), 0.60f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CAELESTISBLUE), 0.60f);
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CAELESTISGREEN), 0.60f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
        beeSpecies.setHasEffect();
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
        AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOW);
        AlleleHelper.instance.set(template, NOCTURNAL, true);
    }, dis -> dis.registerMutation(DIAMOND, DIVIDED, 10)),
    SPARKELING(GT_BranchDefinition.THAUMIC, "NetherStar", true, new Color(0x7A007A), new Color(0xFFFFFF),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(MagicBees.ID, "miscResources", 1, 3), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SPARKELING), 0.125f);
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
            tMutation.requireResource(GregTech_API.sBlockGem3, 3);
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(9, "END Biome")); // sky end biome
        }),

    ESSENTIA(GT_BranchDefinition.THAUMIC, "Essentia", true, new Color(0x7A007A), new Color(0xFFFFFF), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(MagicBees.ID, "miscResources", 1, 3), 0.20f);
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

    DRAKE(GT_BranchDefinition.THAUMIC, "Drake", true, new Color(0x100322), new Color(0x7A007A), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.DRACONIC), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.AWAKENEDDRACONIUM), 0.20f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(HELLISH);
        beeSpecies.setJubilanceProvider(GT_JubilanceMegaApiary.instance);
    }, template -> {
        AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.DOWN_3);
        AlleleHelper.instance.set(template, CAVE_DWELLING, false);
        AlleleHelper.instance.set(template, FLOWER_PROVIDER, Flowers.END);
        AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectDrunkard);
    }, dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(ESSENTIA, THAUMINITE, 5);
        tMutation.requireResource(GameRegistry.findBlock(GregTech.ID, "gt.blockcasings5"), 8);
    }),

    // radioctive
    URANIUM(GT_BranchDefinition.RADIOACTIVE, "Uranium", true, new Color(0x19AF19), new Color(0x169E16), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.URANIUM), 0.15f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setNocturnal();
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
    }, dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Avenging"), PLATINUM, 3);
        tMutation.requireResource(GregTech_API.sBlockMetal7, 14);
    }),
    PLUTONIUM(GT_BranchDefinition.RADIOACTIVE, "Plutonium", true, new Color(0x570000), new Color(0x240000),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LEAD), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.PLUTONIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(URANIUM, EMERALD, 3);
            tMutation.requireResource(GregTech_API.sBlockMetal5, 13);
        }),
    NAQUADAH(GT_BranchDefinition.RADIOACTIVE, "Naquadah", false, new Color(0x003300), new Color(0x002400),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAQUADAH), 0.15f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(PLUTONIUM, IRIDIUM, 3);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 12);
        }),
    NAQUADRIA(GT_BranchDefinition.RADIOACTIVE, "Naquadria", false, new Color(0x000000), new Color(0x002400),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAQUADAH), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAQUADRIA), 0.15f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(PLUTONIUM, IRIDIUM, 8, 10);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 15);
        }),
    DOB(GT_BranchDefinition.RADIOACTIVE, "DOB", false, new Color(0x003300), new Color(0x002400), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.DOB), 0.75f);
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
            tMutation.requireResource(GameRegistry.findBlock(AdvancedSolarPanel.ID, "BlockAdvSolarPanel"), 2);
        tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(9, "END Biome")); // sky end biome
    }),
    THORIUM(GT_BranchDefinition.RADIOACTIVE, "Thorium", false, new Color(0x005000), new Color(0x001E00), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.THORIUM), 0.75f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setNocturnal();
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
    }, dis -> {
        IMutationCustom tMutation = dis.registerMutation(COAL, URANIUM, 3)
            .setIsSecret();
        tMutation.requireResource(GregTech_API.sBlockMetal7, 5);
    }),
    LUTETIUM(GT_BranchDefinition.RADIOACTIVE, "Lutetium", false, new Color(0xE6FFE6), new Color(0xFFFFFF),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LUTETIUM), 0.15f);
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
            tMutation.requireResource(GregTech_API.sBlockMetal4, 3);
        }),
    AMERICIUM(GT_BranchDefinition.RADIOACTIVE, "Americium", false, new Color(0xE6E6FF), new Color(0xC8C8C8),
        beeSpecies -> {
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.AMERICIUM), 0.075f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
            // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
            beeSpecies.setJubilanceProvider(GT_JubilanceMegaApiary.instance);
        }, template -> {
            AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
            AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        }, dis -> {
            IMutationCustom tMutation = dis.registerMutation(LUTETIUM, CHROME, 5, 4)
                .setIsSecret();
            tMutation.requireResource(GregTech_API.sBlockMetal1, 2);
        }),
    NEUTRONIUM(GT_BranchDefinition.RADIOACTIVE, "Neutronium", false, new Color(0xFFF0F0), new Color(0xFAFAFA),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NEUTRONIUM), 0.02f);
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
            tMutation.requireResource(GregTech_API.sBlockMetal5, 2);
        }),
    // Twilight
    NAGA(GT_BranchDefinition.TWILIGHT, "Naga", true, new Color(0x0D5A0D), new Color(0x28874B), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.02f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAGA), 0.10f);
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
    LICH(GT_BranchDefinition.TWILIGHT, "Lich", true, new Color(0xC5C5C5), new Color(0x5C605E), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.04f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LICH), 0.10f);
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
    HYDRA(GT_BranchDefinition.TWILIGHT, "Hydra", true, new Color(0x872836), new Color(0xB8132C), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.06f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.HYDRA), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HELLISH);
        beeSpecies.setHasEffect();
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, Speed.FAST);
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
    }, dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(LICH, getSpecies(MAGICBEES, "TCFire"), 6);
        tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(138, "Undergarden")); // undergarden biome
    }),
    URGHAST(GT_BranchDefinition.TWILIGHT, "UrGhast", true, new Color(0xA7041C), new Color(0x7C0618), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.08f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.URGHAST), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HELLISH);
        beeSpecies.setHasEffect();
        beeSpecies.setNocturnal();
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, Speed.FAST);
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
    }, dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(HYDRA, THAUMIUMDUST, 5);
        if (Thaumcraft.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCosmeticSolid"), 4);
        tMutation.restrictTemperature(HELLISH);
    }),
    SNOWQUEEN(GT_BranchDefinition.TWILIGHT, "SnowQueen", true, new Color(0xD02001), new Color(0x9C0018), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SNOWQUEEN), 0.10f);
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
    ENDDUST(GT_BranchDefinition.HEE, "End Dust", true, new Color(0xCC00FA), new Color(0x003A7D), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENDDUST), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Ended"), STAINLESSSTEEL, 8);

        tMutation.restrictHumidity(ARID);
        if (HardcoreEnderExpansion.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "end_powder_ore"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
    }),
    ENDIUM(GT_BranchDefinition.HEE, "Endium", true, new Color(0xa0ffff), new Color(0x2F5A6C), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENDIUM), 0.10f);
        beeSpecies.addSpecialty(GT_Bees.propolis.getStackForType(PropolisType.Endium), 0.15f);
        beeSpecies.addSpecialty(GT_Bees.drop.getStackForType(DropType.ENDERGOO), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.NORMAL), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Ended"), THAUMIUMDUST, 8);
        tMutation.restrictHumidity(ARID);
        if (HardcoreEnderExpansion.isModLoaded()) tMutation.requireResource("blockHeeEndium");
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
    }),
    STARDUST(GT_BranchDefinition.HEE, "Star Dust", true, new Color(0xffff00), new Color(0xDCBE13), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.STARDUST), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
        beeSpecies.setHasEffect();
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, GT_Bees.speedBlinding);
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOWER);
    }, dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(FORESTRY, "Ended"), ZINC, 8);
        tMutation.restrictHumidity(ARID);
        if (HardcoreEnderExpansion.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "stardust_ore"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
    }),
    ECTOPLASMA(GT_BranchDefinition.HEE, "Ectoplasma", true, new Color(0xDCB0E5), new Color(0x381C40), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ECTOPLASMA), 0.10f);
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
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
    }),
    ARCANESHARDS(GT_BranchDefinition.HEE, "Arcane Shards", true, new Color(0x9010AD), new Color(0x333D82),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ARCANESHARD), 0.10f);
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
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    DRAGONESSENCE(GT_BranchDefinition.HEE, "Dragonessence", true, new Color(0xFFA12B), new Color(0x911ECE),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DRAGONESSENCE), 0.10f);
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
                tMutation.requireResource(GameRegistry.findBlock(HardcoreEnderExpansion.ID, "essence_altar"), 1);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
        }),
    FIREESSENCE(GT_BranchDefinition.HEE, "Fireessence", true, new Color(0xD41238), new Color(0xFFA157), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.FIREESSENSE), 0.10f);
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
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
    }),
    ENDERMANHEAD(GT_BranchDefinition.HEE, "Enderman", true, new Color(0x161616), new Color(0x6200e7), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENDERMAN), 0.10f);
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
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
    }),
    SILVERFISH(GT_BranchDefinition.HEE, "Silverfisch", true, new Color(0xEE053D), new Color(0x000000), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SILVERFISH), 0.10f);
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
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
    }),
    RUNE(GT_BranchDefinition.HEE, "Rune", true, new Color(0xE31010), new Color(0x0104D9), beeSpecies -> {
        beeSpecies.addProduct(GT_ModHandler.getModItem(Forestry.ID, "beeCombs", 1, 8), 0.30f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.RUNEI), 0.025f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.RUNEII), 0.0125f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HELLISH);
        beeSpecies.setHasEffect();
    }, template -> {
        AlleleHelper.instance.set(template, LIFESPAN, GT_Bees.superLife);
        AlleleHelper.instance.set(template, EFFECT, getEffect(MAGICBEES, "SlowSpeed"));
        AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.NONE);
        AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.NONE);
    }, dis -> {
        IMutationCustom tMutation = dis.registerMutation(DRAGONESSENCE, STARDUST, 2)
            .setIsSecret();
        tMutation.restrictHumidity(ARID);
        if (EnderStorage.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(EnderStorage.ID, "enderChest"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(1, "End")); // End Dim
    }),
    // Walrus Bee, 100% Combchance, Parents: Catty and Watery
    WALRUS(GT_BranchDefinition.PLANET, "Walrus", true, new Color(0xD6D580), new Color(0xB5CFC9), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.WALRUS), 1.00f);
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
    DIDDY(GT_BranchDefinition.ORGANIC, "Diddy", true, new Color(85, 37, 130), new Color(253, 185, 39), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.DIDDY), 0.2f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(NORMAL);
        beeSpecies.setHasEffect();
    }, template -> {
        AlleleHelper.instance.set(template, SPEED, GT_Bees.speedBlinding);
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
        AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_1);
        AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_1);
        AlleleHelper.instance.set(template, NOCTURNAL, true);
        AlleleHelper.instance.set(template, FLOWER_PROVIDER, GT_Flowers.FLAMING);
        AlleleHelper.instance.set(template, FERTILITY, Fertility.MAXIMUM);
        AlleleHelper.instance.set(template, EFFECT, getEffect(GREGTECH, "MachineBoost"));
    }, dis -> {
        IBeeMutationCustom tMutation = dis
            .registerMutation(getSpecies(FORESTRY, "Industrious"), getSpecies(FORESTRY, "Imperial"), 1);
        tMutation.addMutationCondition(new GT_Bees.ActiveGTMachineMutationCondition());
    }),
    // Space Bees
    SPACE(GT_BranchDefinition.SPACE, "Space", true, new Color(0x003366), new Color(0xC0C0C0), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.02f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
    }, template -> {}, dis -> {
        IBeeMutationCustom tMutation = dis
            .registerMutation(getSpecies(FORESTRY, "Industrious"), getSpecies(FORESTRY, "Heroic"), 10);
        tMutation.restrictTemperature(ICY);
    }),
    METEORICIRON(GT_BranchDefinition.SPACE, "MeteoricIron", true, new Color(0x321928), new Color(0x643250),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.04f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.METEORICIRON), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setNocturnal();
        }, template -> {}, dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(SPACE, IRON, 9);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 7);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(28, "Moon")); // Moon Dim
        }),
    DESH(GT_BranchDefinition.SPACE, "Desh", false, new Color(0x323232), new Color(0x282828), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.06f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DESH), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
        beeSpecies.setNocturnal();
    }, template -> AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectIgnition), new Consumer<>() {

        @Override
        public void accept(GT_BeeDefinition dis) {
            IBeeMutationCustom tMutation = dis.registerMutation(MARS, TITANIUM, 9);
            tMutation.requireResource(GregTech_API.sBlockMetal2, 12);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(29, "Mars")); // Mars Dim
        }
    }),
    LEDOX(GT_BranchDefinition.SPACE, "Ledox", false, new Color(0x0000CD), new Color(0x0074FF), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.10f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LEDOX), 0.10f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, EFFECT, getEffect(EXTRABEES, "freezing")), new Consumer<>() {

        @Override
        public void accept(GT_BeeDefinition dis) {
            IBeeMutationCustom tMutation = dis.registerMutation(CALLISTO, LEAD, 7);
            if (NewHorizonsCoreMod.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "tile.Ledox"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(35, "Europa")); // Europa Dim
        }
    }),
    CALLISTOICE(GT_BranchDefinition.SPACE, "CallistoIce", false, new Color(0x0074FF), new Color(0x1EB1FF),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.10f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CALLISTOICE), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, EFFECT, getEffect(EXTRABEES, "freezing")), new Consumer<>() {

            @Override
            public void accept(GT_BeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(CALLISTO, getSpecies(EXTRABEES, "freezing"), 7);
                if (NewHorizonsCoreMod.isModLoaded())
                    tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "tile.CallistoColdIce"), 0);
                tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(45, "Callisto")); // Callisto
                // Dim
            }
        }),
    MYTRYL(GT_BranchDefinition.SPACE, "Mytryl", false, new Color(0xDAA520), new Color(0xF26404), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.16f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MYTRYL), 0.10f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> {}, new Consumer<>() {

        @Override
        public void accept(GT_BeeDefinition dis) {
            IBeeMutationCustom tMutation = dis.registerMutation(IO, MITHRIL, 6);
            if (NewHorizonsCoreMod.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "tile.Mytryl"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(36, "IO")); // IO Dim
        }
    }),
    QUANTIUM(GT_BranchDefinition.SPACE, "Quantium", false, new Color(0x00FF00), new Color(0x00D10B), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.16f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.QUANTIUM), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> {}, new Consumer<>() {

        @Override
        public void accept(GT_BeeDefinition dis) {
            IBeeMutationCustom tMutation = dis.registerMutation(VENUS, OSMIUM, 6);
            if (NewHorizonsCoreMod.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "tile.Quantinum"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(39, "Venus")); // Venus Dim
        }
    }),
    ORIHARUKON(GT_BranchDefinition.SPACE, "Oriharukon", false, new Color(0x228B22), new Color(0x677D68), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.26f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ORIHARUKON), 0.10f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setHasEffect();
    }, template -> {}, new Consumer<>() {

        @Override
        public void accept(GT_BeeDefinition dis) {
            IBeeMutationCustom tMutation = dis.registerMutation(LEAD, OBERON, 5);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "metalsblock"), 6);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(46, "Oberon")); // Oberon Dim
        }
    }),
    INFUSEDGOLD(GT_BranchDefinition.SPACE, "Infused Gold", false, new Color(0x80641E), new Color(0xFFC83C),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.GOLD), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.INFUSEDGOLD), 0.30f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {}, new Consumer<>() {

            @Override
            public void accept(GT_BeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(GOLD, HAUMEA, 5);
                tMutation.requireResource(GregTech_API.sBlockMetal3, 10);
                tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(83, "Haumea")); // Haumea Dim
            }
        }),
    MYSTERIOUSCRYSTAL(GT_BranchDefinition.SPACE, "MysteriousCrystal", false, new Color(0x3CB371), new Color(0x16856C),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.42f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MYSTERIOUSCRYSTAL), 0.30f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {}, new Consumer<>() {

            @Override
            public void accept(GT_BeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(ENCELADUS, EMERALD, 3);
                if (NewHorizonsCoreMod.isModLoaded()) tMutation
                    .requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "tile.MysteriousCrystal"), 0);
                tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(41, "Enceladus")); // Enceladus
                // Dim
            }
        }),
    BLACKPLUTONIUM(GT_BranchDefinition.SPACE, "BlackPlutonium", false, new Color(0x000000), new Color(0x323232),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.68f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.BLACKPLUTONIUM), 0.10f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> {}, new Consumer<>() {

            @Override
            public void accept(GT_BeeDefinition dis) {
                IBeeMutationCustom tMutation = dis.registerMutation(PLUTO, PLUTONIUM, 2);
                if (NewHorizonsCoreMod.isModLoaded())
                    tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "tile.BlackPlutonium"), 0);
                tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(49, "Pluto")); // Pluto Dim
            }
        }),
    TRINIUM(GT_BranchDefinition.SPACE, "Trinium", false, new Color(0xB0E0E6), new Color(0xC8C8D2), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TRINIUM), 0.75f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.QUANTIUM), 0.10f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, SPEED, GT_Bees.speedBlinding), new Consumer<>() {

        @Override
        public void accept(GT_BeeDefinition dis) {
            IBeeMutationCustom tMutation = dis.registerMutation(ENCELADUS, IRIDIUM, 4);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 9);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(41, "Enceladus")); // Enceladus Dim
        }
    }),
    // Planet Line
    MOON(GT_BranchDefinition.PLANET, "Moon", false, new Color(0x373735), new Color(0x7E7E78), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MOON), 0.35f);
        if (NewHorizonsCoreMod.isModLoaded())
            beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MoonStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(SPACE, CLAY, 25);
        if (GalacticraftCore.isModLoaded()) {
            tMutation.requireResource(GameRegistry.findBlock(GalacticraftCore.ID, "tile.moonBlock"), 4);
        }
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(28, "Moon")); // Moon Dim
    }),
    MARS(GT_BranchDefinition.PLANET, "Mars", false, new Color(0x220D05), new Color(0x3A1505), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MARS), 0.35f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MarsStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(MOON, IRON, 20);
        if (GalacticraftMars.isModLoaded()) {
            tMutation.requireResource(GameRegistry.findBlock(GalacticraftMars.ID, "tile.mars"), 5);
        }
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(29, "Mars")); // Mars Dim
    }),
    PHOBOS(GT_BranchDefinition.PLANET, "Phobos", true, new Color(0x220D05), new Color(0x7a5706), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MARS), 0.25f);
        if (NewHorizonsCoreMod.isModLoaded()) beeSpecies
            .addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.PhobosStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
        beeSpecies.setNocturnal();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(MARS, MOON, 20);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "phobosblocks"), 2);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(38, "Phobos")); // Phobos Dim
    }),
    DEIMOS(GT_BranchDefinition.PLANET, "Deimos", true, new Color(0x220D05), new Color(0x7a3206), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MARS), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.DeimosStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(HOT);
        beeSpecies.setNocturnal();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(MARS, SPACE, 20);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "deimosblocks"), 1);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(40, "Deimos")); // Deimos Dim
    }),
    CERES(GT_BranchDefinition.PLANET, "Ceres", true, new Color(0x3ca5b7), new Color(0x1e7267), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CeresStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
        beeSpecies.setNocturnal();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(MARS, METEORICIRON, 20);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "ceresblocks"), 1);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(42, "Ceres")); // Ceres Dim
    }),
    JUPITER(GT_BranchDefinition.PLANET, "Jupiter", false, new Color(0x734B2E), new Color(0xD0CBC4), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.35f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CallistoStoneDust", 1, 0), 0.05f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CallistoIceDust", 1, 0), 0.05f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.IoStoneDust", 1, 0), 0.05f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EuropaStoneDust", 1, 0), 0.05f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EuropaIceDust", 1, 0), 0.05f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.GanymedeStoneDust", 1, 0), 0.05f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(MARS, DESH, 15);
        if (NewHorizonsCoreMod.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "tile.Ledox"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteroids")); // Asteroid Dim
    }),
    IO(GT_BranchDefinition.PLANET, "IO", true, new Color(0x734B2E), new Color(0xe5701b), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.IoStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HELLISH);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, getSpecies(EXTRABEES, "volcanic"), 15);
        tMutation.restrictTemperature(HELLISH);
        if (GalaxySpace.isModLoaded()) tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "ioblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(36, "IO")); // IO Dim
    }),
    EUROPA(GT_BranchDefinition.PLANET, "Europa", true, new Color(0x5982ea), new Color(0x0b36a3), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EuropaStoneDust", 1, 0), 0.10f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EuropaIceDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, IRON, 15);
        tMutation.restrictTemperature(ICY);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "europagrunt"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(35, "Europa")); // Europa Dim
    }),
    GANYMEDE(GT_BranchDefinition.PLANET, "Ganymede", true, new Color(0x3d1b10), new Color(0x190c07), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.GanymedeStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(COLD);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, TITANIUM, 15);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "ganymedeblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(43, "Ganymede")); // Ganymede Dim
    }),
    CALLISTO(GT_BranchDefinition.PLANET, "Callisto", true, new Color(0x0f333d), new Color(0x0d84a5), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CallistoStoneDust", 1, 0), 0.10f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CallistoIceDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, getSpecies(EXTRABEES, "artic"), 15);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "callistoblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(45, "Callisto")); // Callisto Dim
    }),
    SATURN(GT_BranchDefinition.PLANET, "Saturn", false, new Color(0xD2A472), new Color(0xF8C37B), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SATURN), 0.35f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.TitanStoneDust", 1, 0), 0.05f);
        beeSpecies
            .addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EnceladusStoneDust", 1, 0), 0.05f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EnceladusIceDust", 1, 0), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, LEDOX, 25, 2);
        if (NewHorizonsCoreMod.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "tile.Quantinum"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteriods")); // Asteriods Dim
    }),
    ENCELADUS(GT_BranchDefinition.PLANET, "Enceladus", true, new Color(0xD2A472), new Color(0x193fa0), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SATURN), 0.25f);
        beeSpecies
            .addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EnceladusStoneDust", 1, 0), 0.10f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EnceladusIceDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(SATURN, CHROME, 25, 2);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "enceladusblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(41, "Enceladus")); // Enceladus Dim
    }),
    TITAN(GT_BranchDefinition.PLANET, "Titan", true, new Color(0xa0641b), new Color(0x7c1024), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SATURN), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.TitanStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(SATURN, NICKEL, 25, 2);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "titanblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(44, "Titan")); // Titan Dim
    }),
    URANUS(GT_BranchDefinition.PLANET, "Uranus", false, new Color(0x75C0C9), new Color(0x84D8EC), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.URANUS), 0.35f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MirandaStoneDust", 1, 0), 0.05f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.OberonStoneDust", 1, 0), 0.05f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(SATURN, TRINIUM, 10);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "metalsblock"), 6);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteroids")); // Asteroids Dim
    }),
    MIRANDA(GT_BranchDefinition.PLANET, "Miranda", true, new Color(0x75C0C9), new Color(0x0d211c), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.URANUS), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MirandaStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(URANUS, TIN, 10);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "mirandablocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(86, "Miranda")); // Miranda Dim
    }),
    OBERON(GT_BranchDefinition.PLANET, "Oberon", true, new Color(0x4A4033), new Color(0xB5A288), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.URANUS), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.OberonStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(ICY);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(URANUS, IRIDIUM, 10);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "oberonblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(46, "Oberon")); // Oberon Dim
    }),
    NEPTUNE(GT_BranchDefinition.PLANET, "Neptune", false, new Color(0x334CFF), new Color(0x576DFF), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NEPTUN), 0.35f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ProteusStoneDust", 1, 0), 0.05f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.TritonStoneDust", 1, 0), 0.05f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(URANUS, ORIHARUKON, 7);
        if (NewHorizonsCoreMod.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(NewHorizonsCoreMod.ID, "tile.MysteriousCrystal"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteroids")); // Asteroids Dim
    }),
    PROTEUS(GT_BranchDefinition.PLANET, "Proteus", true, new Color(0x334CFF), new Color(0x592610), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NEPTUN), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ProteusStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setNocturnal();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(NEPTUNE, COPPER, 7);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "proteusblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(47, "Proteus")); // Proteus Dim
    }),
    TRITON(GT_BranchDefinition.PLANET, "Triton", true, new Color(0x334CFF), new Color(0x421118), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NEPTUN), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.TritonStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(NEPTUNE, GOLD, 7);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "tritonblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(48, "Triton")); // Triton Dim
    }),
    PLUTO(GT_BranchDefinition.PLANET, "Pluto", false, new Color(0x34271E), new Color(0x69503D), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLUTO), 0.35f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.PlutoStoneDust", 1, 0), 0.10f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.PlutoIceDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(NEPTUNE, PLUTONIUM, 5);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "plutoblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(49, "Pluto")); // Pluto Dim
    }),
    HAUMEA(GT_BranchDefinition.PLANET, "Haumea", false, new Color(0x1C1413), new Color(0x392B28), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.HAUMEA), 0.35f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.HaumeaStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(ICY);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(PLUTO, NAQUADAH, 7, 2);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "haumeablocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(83, "Haumea")); // Haumea Dim
    }),
    MAKEMAKE(GT_BranchDefinition.PLANET, "MakeMake", false, new Color(0x301811), new Color(0x120A07), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MAKEMAKE), 0.35f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MakeMakeStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(PLUTO, NAQUADRIA, 7, 2);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "makemakegrunt"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(25, "MakeMake")); // MakeMake Dim
    }),
    CENTAURI(GT_BranchDefinition.PLANET, "Centauri", false, new Color(0x2F2A14), new Color(0xB06B32), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CENTAURI), 0.35f);
        beeSpecies
            .addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CentauriASurfaceDust", 1, 0), 0.05f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HELLISH);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(MAKEMAKE, DESH, 3);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "acentauribbgrunt"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(33, "Kuiper Belt")); // Kuiper Belt Dim
    }),
    ACENTAURI(GT_BranchDefinition.PLANET, "aCentauri", false, new Color(0x2F2A14), new Color(0xa01e14), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CENTAURI), 0.25f);
        beeSpecies
            .addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.CentauriASurfaceDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HELLISH);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), new Consumer<>() {

        @Override
        public void accept(GT_BeeDefinition dis) {
            IBeeMutationCustom tMutation = dis.registerMutation(CENTAURI, INFINITYCATALYST, 3);
            if (GalaxySpace.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "acentauribbgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(31, "aCentauri")); // aCentauri Dim
        }
    }),
    TCETI(GT_BranchDefinition.PLANET, "tCeti", false, new Color(0x46241A), new Color(0x7B412F), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TCETI), 0.35f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.TCetiEStoneDust", 1, 0), 0.05f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(MAKEMAKE, HAUMEA, 5, 2);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "tcetieblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(33, "Kuiper Belt")); // Kuiper Belt Dim
    }),
    TCETIE(GT_BranchDefinition.PLANET, "tCetiE", false, new Color(0x2d561b), new Color(0x0c0f60), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TCETI), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.TCetiEStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(EnumTemperature.NORMAL);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(TCETI, getSpecies(MAGICBEES, "TCWater"), 5, 2);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "tcetieblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(85, "tCeti E")); // tCeti E Dim
    }),
    // Parents: TCETI and TCETIE, 25% combchance, needs Damp Humidity and Normal temperature. Is Mycophilic
    SEAWEED(GT_BranchDefinition.PLANET, "SeaWeed", true, new Color(0xCBCBCB), new Color(0x83FF83), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SEAWEED), 0.25f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.TCetiEStoneDust", 1, 0), 0.15f);
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
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(85, "tCeti E")); // tCeti E Dim
    }),

    BARNARDA(GT_BranchDefinition.PLANET, "Barnarda", false, new Color(0x0D5A0D), new Color(0xE6C18D), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.BARNARDA), 0.35f);
        beeSpecies
            .addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.BarnardaEStoneDust", 1, 0), 0.05f);
        beeSpecies
            .addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.BarnardaFStoneDust", 1, 0), 0.05f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(MAKEMAKE, THORIUM, 3, 2);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "barnardaEgrunt"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(33, "Kuiper Belt")); // Kuiper Belt Dim
    }),
    BARNARDAC(GT_BranchDefinition.PLANET, "BarnardaC", false, new Color(0x0D5A0D), new Color(0x473f0a), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.BARNARDA), 0.25f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
    }, template -> {
        AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
        AlleleHelper.instance.set(template, EFFECT, getEffect(GREGTECH, "Treetwister"));
    }, dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(BARNARDA, AMERICIUM, 3, 2);
        if (GalaxySpace.isModLoaded()) {
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "barnardaEgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(32, "Barnarda C")); // Barnarda C Dim
        }
    }),
    BARNARDAE(GT_BranchDefinition.PLANET, "BarnardaE", false, new Color(0x0D5A0D), new Color(0x4c1f0a), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.BARNARDA), 0.25f);
        beeSpecies
            .addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.BarnardaEStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(BARNARDA, DIVIDED, 3, 2);
        if (GalaxySpace.isModLoaded()) {
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "barnardaEgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(81, "Barnard E")); // "Barnard E Dim
        }
    }),
    BARNARDAF(GT_BranchDefinition.PLANET, "BarnardaF", false, new Color(0x0D5A0D), new Color(0x1e0b49), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.BARNARDA), 0.25f);
        beeSpecies
            .addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.BarnardaFStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HOT);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(BARNARDA, NEUTRONIUM, 3, 2);
        if (GalaxySpace.isModLoaded()) {
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "barnardaFgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(82, "Barnard F")); // "Barnard F Dim
        }
    }),
    VEGA(GT_BranchDefinition.PLANET, "Vega", false, new Color(0x1A2036), new Color(0xB5C0DE), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.VEGA), 0.35f);
        beeSpecies.addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.VegaBStoneDust", 1, 0), 0.05f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(MAKEMAKE, NAQUADAH, 2);
        if (GalaxySpace.isModLoaded()) {
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "vegabgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(33, "Kuiper Belt")); // Kuiper Belt
                                                                                                       // Dim
        }
    }),
    VEGAB(GT_BranchDefinition.PLANET, "VegaB", false, new Color(0x1A2036), new Color(0x81e261), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.VEGA), 0.35f);
        if (NewHorizonsCoreMod.isModLoaded()) beeSpecies
            .addSpecialty(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.VegaBStoneDust", 1, 0), 0.10f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(COLD);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(VEGA, NAQUADRIA, 2);
        if (GalaxySpace.isModLoaded()) {
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "vegabgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(84, "VegaB")); // VegaB Dim
        }
    }),
    MERCURY(GT_BranchDefinition.PLANET, "Mercury", false, new Color(0x4A4033), new Color(0xB5A288), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MERCURY), 0.35f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HELLISH);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, TUNGSTEN, 25, 2);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "mercuryblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(37, "Mercury")); // Mercury Dim
    }),
    VENUS(GT_BranchDefinition.PLANET, "Venus", false, new Color(0x4A4033), new Color(0xB5A288), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.VENUS), 0.35f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setTemperature(HELLISH);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(JUPITER, MITHRIL, 25, 2);
        if (GalaxySpace.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(GalaxySpace.ID, "venusblocks"), 0);
        tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(39, "Venus")); // Venus Dim
    }),

    // Infinity Line
    COSMICNEUTRONIUM(GT_BranchDefinition.PLANET, "CosmicNeutronium", false, new Color(0x484848), new Color(0x323232),
        beeSpecies -> {
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.COSMICNEUTRONIUM), 0.375f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
            // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
            beeSpecies.setJubilanceProvider(GT_JubilanceMegaApiary.instance);
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NEUTRONIUM, BARNARDAF, 7, 10);
            if (Avaritia.isModLoaded())
                tMutation.requireResource(GameRegistry.findBlock(Avaritia.ID, "Resource_Block"), 0);
        }),
    INFINITYCATALYST(GT_BranchDefinition.PLANET, "InfinityCatalyst", false, new Color(0xFFFFFF), new Color(0xFFFFFF),
        beeSpecies -> {
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.INFINITYCATALYST), 0.015f);
            beeSpecies.setHumidity(DAMP);
            beeSpecies.setTemperature(HELLISH);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
            // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
            beeSpecies.setJubilanceProvider(GT_JubilanceMegaApiary.instance);
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
    INFINITY(GT_BranchDefinition.PLANET, "Infinity", false, new Color(0xFFFFFF), new Color(0xFFFFFF), beeSpecies -> {
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.INFINITY), 0.015f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
        // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
        beeSpecies.setJubilanceProvider(GT_JubilanceMegaApiary.instance);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(INFINITYCATALYST, COSMICNEUTRONIUM, 1, 10);
        if (AvaritiaAddons.isModLoaded()) {
            tMutation.requireResource(GameRegistry.findBlock(AvaritiaAddons.ID, "InfinityChest"), 0);
        }
    }),
    KEVLAR(GT_BranchDefinition.IC2, "Kevlar", false, new Color(0x2d542f), new Color(0xa2baa3), beeSpecies -> {
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.KEVLAR), 0.075f);
        beeSpecies.addSpecialty(MaterialsKevlar.Kevlar.getNuggets(1), 0.01f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setTemperature(COLD);
        beeSpecies.setHasEffect();
        beeSpecies.setJubilanceProvider(GT_JubilanceMegaApiary.instance);
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
        GregTech_API.sGTCompleteLoad.add(() -> tMutation.requireResource(GregTech_API.sBlockMachines, 11003));
    }),

    // Noble Gas Line
    // Helium bee, Humidity: normal, Temperature: Icy, Parents: Space & Mars, Mutationrate: 10%, Combrate: 50%
    HELIUM(GT_BranchDefinition.NOBLEGAS, "Helium", false, new Color(0xFFA9FF), new Color(0xC8B8B4), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.HELIUM), 0.35f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "Watery"), ENDDUST, 10);
        tMutation.restrictTemperature(ICY);
    }),
    // Argon bee, Humidity: normal, Temperature: Icy, Parents: Helium & Phobos, Mutationrate: 8%, Combrate: 50%
    ARGON(GT_BranchDefinition.NOBLEGAS, "Argon", false, new Color(0x89D9E1), new Color(0xBDA5C2), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ARGON), 0.35f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(HELIUM, LICH, 8);
        tMutation.restrictTemperature(ICY);
    }),
    // Neon bee, Humidity: normal, Temperature: Icy, Parents: Xenon & Ceres, Mutationrate: 6%, Combrate: 50%
    NEON(GT_BranchDefinition.NOBLEGAS, "Neon", false, new Color(0xFFC826), new Color(0xFF7200), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NEON), 0.35f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(ARGON, HYDRA, 6);
        tMutation.restrictTemperature(ICY);
    }),
    // Krypton bee, Humidity: normal, Temperature: Icy, Parents: Neon & Jupiter, Mutationrate: 4%, Combrate: 50%
    KRYPTON(GT_BranchDefinition.NOBLEGAS, "Krypton", false, new Color(0x8A97B0), new Color(0x160822), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.KRYPTON), 0.35f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(NEON, URGHAST, 4);
        tMutation.restrictTemperature(ICY);
    }),
    // Xenon bee, Humidity: normal, Temperature: Icy, Parents: Argon & Deimos, Mutationrate: 6%, Combrate: 50%
    XENON(GT_BranchDefinition.NOBLEGAS, "Xenon", false, new Color(0x8A97B0), new Color(0x160822), beeSpecies -> {
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.XENON), 0.525f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
        // Makes it only work in the Mega Apiary NOTE: COMB MUST BE SPECIALITY COMB
        beeSpecies.setJubilanceProvider(GT_JubilanceMegaApiary.instance);
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(KRYPTON, SNOWQUEEN, 2);
        tMutation.restrictTemperature(ICY);
    }),
    // Oxygen bee, Humidity: normal, Temperature: Icy, Parents: Space & Callisto, Mutationrate: 15%, Combrate: 50%
    OXYGEN(GT_BranchDefinition.NOBLEGAS, "Oxygen", false, new Color(0xFFFFFF), new Color(0x8F8FFF), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.OXYGEN), 0.45f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.HYDROGEN), 0.20f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(HELIUM, DRAGONESSENCE, 15);
        tMutation.restrictTemperature(ICY);
    }),
    // Hydrogen bee, Humidity: normal, Temperature: Icy, Parents: Oxygen & Watery, Mutationrate: 15%, Combrate: 50%
    HYDROGEN(GT_BranchDefinition.NOBLEGAS, "Oxygen", false, new Color(0xFFFFFF), new Color(0xFF1493), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.HYDROGEN), 0.45f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NITROGEN), 0.20f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(OXYGEN, getSpecies(MAGICBEES, "Watery"), 15);
        tMutation.restrictTemperature(ICY);
    }),
    // Nitrogen bee, Humidity: normal, Temperature: Icy, Parents: Oxygen & Hydrogen, Mutationrate: 15%, Combrate: 50%
    NITROGEN(GT_BranchDefinition.NOBLEGAS, "Nitrogen", false, new Color(0xFFC832), new Color(0xA52A2A), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NITROGEN), 0.45f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.FLUORINE), 0.20f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(OXYGEN, HYDROGEN, 15);
        tMutation.restrictTemperature(ICY);
    }),
    // Fluorine bee, Humidity: normal, Temperature: Icy, Parents: Nitrogen & Hydrogen, Mutationrate: 15%, Combrate: 50%
    FLUORINE(GT_BranchDefinition.NOBLEGAS, "Fluorine", false, new Color(0x86AFF0), new Color(0xFF6D00), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.FLUORINE), 0.45f);
        beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OXYGEN), 0.20f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setTemperature(ICY);
        beeSpecies.setNocturnal();
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(NITROGEN, HYDROGEN, 15);
        tMutation.restrictTemperature(ICY);
    }),
    // infused Shards line
    AIR(GT_BranchDefinition.INFUSEDSHARD, "Air", false, new Color(0xFFFF7E), new Color(0x60602F), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INFUSEDAER), 0.30f);
        beeSpecies.setHumidity(DAMP);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis
            .registerMutation(getSpecies(MAGICBEES, "Supernatural"), getSpecies(MAGICBEES, "Windy"), 15);
        tMutation.restrictTemperature(HOT);
        if (Thaumcraft.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 0);
    }),
    FIRE(GT_BranchDefinition.INFUSEDSHARD, "Fire", false, new Color(0xED3801), new Color(0x3B0E00), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INFUSEDIGNIS), 0.30f);
        beeSpecies.setHumidity(ARID);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(getSpecies(MAGICBEES, "Supernatural"), AIR, 15);
        tMutation.restrictTemperature(HELLISH);
        if (Thaumcraft.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 1);
    }),
    WATER(GT_BranchDefinition.INFUSEDSHARD, "Water", false, new Color(0x0090FF), new Color(0x002542), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INFUSEDAQUA), 0.30f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(FIRE, AIR, 15);
        tMutation.restrictTemperature(ICY);
        if (Thaumcraft.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 2);
    }),
    EARTH(GT_BranchDefinition.INFUSEDSHARD, "Earth", false, new Color(0x008600), new Color(0x003300), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INFUSEDTERRA), 0.30f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(WATER, FIRE, 15);
        tMutation.restrictTemperature(WARM);
        if (Thaumcraft.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 3);
    }),
    ORDER(GT_BranchDefinition.INFUSEDSHARD, "Order", false, new Color(0x8A97B0), new Color(0x5C5F62), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INFUSEDORDO), 0.30f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(EARTH, FIRE, 15);
        tMutation.restrictTemperature(ICY);
        if (Thaumcraft.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 4);
    }),
    CHAOS(GT_BranchDefinition.INFUSEDSHARD, "Chaos", false, new Color(0x2E2E41), new Color(0x232129), beeSpecies -> {
        beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INFUSEDPERDITIO), 0.30f);
        beeSpecies.setHumidity(EnumHumidity.NORMAL);
        beeSpecies.setHasEffect();
    }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
        IBeeMutationCustom tMutation = dis.registerMutation(ORDER, FIRE, 15);
        tMutation.restrictTemperature(ICY);
        if (Thaumcraft.isModLoaded())
            tMutation.requireResource(GameRegistry.findBlock(Thaumcraft.ID, "blockCrystal"), 5);
    }),
    NETHERSHARD(GT_BranchDefinition.INFUSEDSHARD, "Nethershard", false, new Color(0xBE0135), new Color(0x350211),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NETHERSHARD), 0.30f);
            beeSpecies.setHumidity(ARID);
            beeSpecies.setTemperature(HOT);
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(CHAOS, FIRE, 15);
            tMutation.requireResource(GameRegistry.findBlock(GregTech.ID, "gt.blockgem3"), 3);
        }),
    ENDSHARD(GT_BranchDefinition.INFUSEDSHARD, "Endshard", false, new Color(0x2E2E41), new Color(0x232129),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ENDSHARD), 0.30f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(NETHERSHARD, ENDDUST, 15);
            tMutation.restrictTemperature(ICY);
            tMutation.requireResource(GameRegistry.findBlock(GregTech.ID, "gt.blockgem1"), 7);
        }),
    // Organic branch 2.0
    UNKNOWNWATER(GT_BranchDefinition.ORGANIC, "UnknownWater", false, new Color(0x4333A5), new Color(0x36ABFF),
        beeSpecies -> {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.UNKNOWNWATER), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST),
        dis -> dis.registerMutation(INFINITYCATALYST, MYSTERIOUSCRYSTAL, 5)),
    // Endgame bees
    JAEGERMEISTER(GT_BranchDefinition.ENDGAME, "JaegerMeister", false, new Color(0x05AD18), new Color(0xE7DAC3),
        beeSpecies -> {
            beeSpecies.addProduct(GT_ModHandler.getModItem(CropsPlusPlus.ID, "BppPotions", 1L, 8), 0.01f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }, template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST), dis -> {
            IBeeMutationCustom tMutation = dis.registerMutation(INFINITYCATALYST, NAQUADRIA, 5);
            tMutation.requireResource(GregTech_API.sBlockMachines, 4684);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(100, "Deep Dark")); // Deep Dark
                                                                                                      // dim
        });

    private final GT_BranchDefinition branch;
    private final GT_AlleleBeeSpecies species;
    private final Consumer<GT_AlleleBeeSpecies> mSpeciesProperties;
    private final Consumer<IAllele[]> mAlleles;
    private final Consumer<GT_BeeDefinition> mMutations;
    private IAllele[] template;
    private IBeeGenome genome;

    GT_BeeDefinition(GT_BranchDefinition branch, String binomial, boolean dominant, Color primary, Color secondary,
        Consumer<GT_AlleleBeeSpecies> aSpeciesProperties, Consumer<IAllele[]> aAlleles,
        Consumer<GT_BeeDefinition> aMutations) {
        this.mAlleles = aAlleles;
        this.mMutations = aMutations;
        this.mSpeciesProperties = aSpeciesProperties;
        String lowercaseName = this.toString()
            .toLowerCase(Locale.ENGLISH);
        String species = WordUtils.capitalize(lowercaseName);

        String uid = "gregtech.bee.species" + species;
        String description = "for.description." + species;
        String name = "for.bees.species." + lowercaseName;
        GT_LanguageManager.addStringLocalization("for.bees.species." + lowercaseName, species);

        this.branch = branch;
        this.species = new GT_AlleleBeeSpecies(
            uid,
            dominant,
            name,
            "GTNH",
            description,
            branch.getBranch(),
            binomial,
            primary,
            secondary);
    }

    public static void initBees() {
        for (GT_BeeDefinition bee : values()) {
            bee.init();
        }
        for (GT_BeeDefinition bee : values()) {
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
        return (IAlleleBeeEffect) AlleleManager.alleleRegistry.getAllele(s);
    }

    static IAlleleFlowers getFlowers(byte modid, String name) {
        String s = switch (modid) {
            case EXTRABEES -> "extrabees.flower." + name;
            case GENDUSTRY -> "gendustry.flower." + name;
            case MAGICBEES -> "magicbees.flower" + name;
            case GREGTECH -> "gregtech.flower" + name;
            default -> "forestry.flowers" + name;
        };
        return (IAlleleFlowers) AlleleManager.alleleRegistry.getAllele(s);
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
            ret = NAQUADRIA.species;
        }

        return ret;
    }

    private void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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

    private IBeeMutationCustom registerMutation(GT_BeeDefinition parent1, IAlleleBeeSpecies parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    private IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, GT_BeeDefinition parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    private IBeeMutationCustom registerMutation(GT_BeeDefinition parent1, GT_BeeDefinition parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    /**
     * Diese neue Funtion erlaubt Mutationsraten unter 1%. Setze dazu die Mutationsrate als Bruch mit chance /
     * chanceDivider This new function allows Mutation percentages under 1%. Set them as a fraction with chance /
     * chanceDivider
     */
    private IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2, int chance,
        float chanceDivider) {
        return new GT_Bee_Mutation(parent1, parent2, this.getTemplate(), chance, chanceDivider);
    }

    private IBeeMutationCustom registerMutation(GT_BeeDefinition parent1, IAlleleBeeSpecies parent2, int chance,
        float chanceDivider) {
        return registerMutation(parent1.species, parent2, chance, chanceDivider);
    }

    private IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, GT_BeeDefinition parent2, int chance,
        float chanceDivider) {
        return registerMutation(parent1, parent2.species, chance, chanceDivider);
    }

    private IBeeMutationCustom registerMutation(GT_BeeDefinition parent1, GT_BeeDefinition parent2, int chance,
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
}
