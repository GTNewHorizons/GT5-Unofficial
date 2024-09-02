package goodgenerator.crossmod.thaumcraft;

import static gregtech.api.enums.Mods.Automagy;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.ThaumicBases;
import static gregtech.api.enums.Mods.ThaumicEnergistics;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.enums.Mods.WitchingGadgets;
import static thaumcraft.api.ThaumcraftApi.addArcaneCraftingRecipe;

import java.util.Arrays;
import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import goodgenerator.util.DescTextLocalization;
import goodgenerator.util.ItemRefer;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import ic2.core.Ic2Items;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class Research {

    public static void addResearch() {
        DescTextLocalization.addText("research.ESSENTIA_GENERATOR.page", 4);
        DescTextLocalization.addText("research.ESSENTIA_CELL.page", 1);
        DescTextLocalization.addText("research.ESSENTIA_UPGRADE_BLANK.page", 1);
        DescTextLocalization.addText("research.ESSENTIA_UPGRADE_AIR.page", 2);
        DescTextLocalization.addText("research.ESSENTIA_UPGRADE_THERMAL.page", 3);
        DescTextLocalization.addText("research.ESSENTIA_UPGRADE_UNSTABLE.page", 3);
        DescTextLocalization.addText("research.ESSENTIA_UPGRADE_VICTUS.page", 3);
        DescTextLocalization.addText("research.ESSENTIA_UPGRADE_TAINTED.page", 4);
        DescTextLocalization.addText("research.ESSENTIA_UPGRADE_MECHANICS.page", 3);
        DescTextLocalization.addText("research.ESSENTIA_UPGRADE_SPRITE.page", 3);
        DescTextLocalization.addText("research.ESSENTIA_UPGRADE_RADIATION.page", 3);
        DescTextLocalization.addText("research.ESSENTIA_UPGRADE_ELECTRIC.page", 2);
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_GENERATOR",
            "Combustion Engine in Magic World",
            "Will it cause Flux pollution?",
            new String[] { "INFUSION" },
            "ARTIFICE",
            ItemRefer.Large_Essentia_Generator.get(1),
            3,
            0,
            -9,
            3,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L)),
            null,
            new Object[] { "research.ESSENTIA_GENERATOR.page.0",
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_GENERATOR",
                    ItemList.Hatch_Input_HV.get(1),
                    new ItemStack[] { new ItemStack(ConfigBlocks.blockJar, 1), ItemRefer.Magic_Casing.get(1),
                        new ItemStack(ConfigBlocks.blockTube, 1), ItemList.Electric_Pump_MV.get(1L) },
                    ItemRefer.Essentia_Hatch.get(1),
                    6,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.AQUA, 128),
                        new TCAspects.TC_AspectStack(TCAspects.VACUOS, 64),
                        new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 32),
                        new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 32))),
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_GENERATOR",
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Thaumium, 1),
                    new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 1),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.ElectricalSteel, 1),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.ElectricalSteel, 1),
                        ItemList.Electric_Pump_EV.get(1L), new ItemStack(ConfigBlocks.blockTube, 1, 4),
                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), },
                    ItemRefer.Essentia_Cell_T1.get(1),
                    4,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.AQUA, 32),
                        new TCAspects.TC_AspectStack(TCAspects.VACUOS, 32),
                        new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 16))),
                addArcaneCraftingRecipe(
                    "ESSENTIA_GENERATOR",
                    ItemRefer.Magic_Casing.get(1),
                    new AspectList().add(Aspect.AIR, 50)
                        .add(Aspect.FIRE, 50)
                        .add(Aspect.ORDER, 50),
                    "SCS",
                    "GAG",
                    "SCS",
                    'S',
                    new ItemStack(ConfigItems.itemResource, 1, 14),
                    'C',
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Thaumium, 1),
                    'G',
                    Ic2Items.reinforcedGlass,
                    'A',
                    Ic2Items.advancedMachine),
                "research.ESSENTIA_GENERATOR.page.1", "research.ESSENTIA_GENERATOR.page.2",
                "research.ESSENTIA_GENERATOR.page.3" });
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_CELL",
            "Better Cells",
            "And higher efficiency.",
            new String[] { "ESSENTIA_GENERATOR" },
            "ARTIFICE",
            ItemRefer.Essentia_Cell_T4.get(1),
            2,
            0,
            -10,
            3,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 10L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L)),
            null,
            new Object[] { "research.ESSENTIA_CELL.page.0",
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_CELL",
                    ItemRefer.Essentia_Cell_T1.get(1),
                    new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1),
                        ItemList.Electric_Pump_IV.get(1L), ItemList.QuantumStar.get(1L),
                        new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3), ItemList.Reactor_Coolant_Sp_1.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Plutonium241, 1), },
                    ItemRefer.Essentia_Cell_T2.get(1),
                    6,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.AQUA, 64),
                        new TCAspects.TC_AspectStack(TCAspects.VACUOS, 64),
                        new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 32))),
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_CELL",
                    ItemRefer.Essentia_Cell_T2.get(1),
                    new ItemStack[] { ItemList.Field_Generator_IV.get(1L), ItemList.Electric_Pump_LuV.get(1L),
                        new ItemStack(ConfigItems.itemResource, 1, 14), Ic2Items.fluidregulator,
                        new ItemStack(ConfigBlocks.blockJar, 1, 0),
                        GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Ichorium, 1) },
                    ItemRefer.Essentia_Cell_T3.get(1),
                    8,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.AQUA, 128),
                        new TCAspects.TC_AspectStack(TCAspects.VACUOS, 128),
                        new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 64))),
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_CELL",
                    ItemRefer.Essentia_Cell_T3.get(1),
                    new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.ring, Materials.Europium, 1),
                        ItemList.Emitter_LuV.get(1L), new ItemStack(ItemRegistry.bw_realglas, 1, 4),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.StellarAlloy, 1),
                        GTOreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Helium, 1),
                        new ItemStack(ConfigItems.itemShard, 1, 6), },
                    ItemRefer.Essentia_Cell_T4.get(1),
                    10,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.AQUA, 256),
                        new TCAspects.TC_AspectStack(TCAspects.VACUOS, 256),
                        new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 128))) });
        ItemStack broad = new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6);
        if (NewHorizonsCoreMod.isModLoaded())
            broad = GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ArcaneSlate", 1);
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_UPGRADE_BLANK",
            "Upgrade your generator",
            "Let's try some more dangerous essentia.",
            new String[] { "ESSENTIA_GENERATOR" },
            "ARTIFICE",
            ItemRefer.Essentia_Upgrade_Empty.get(1),
            2,
            0,
            -9,
            4,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.AURAM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { "research.ESSENTIA_UPGRADE_BLANK.page.0",
                addArcaneCraftingRecipe(
                    "ESSENTIA_UPGRADE_BLANK",
                    ItemRefer.Essentia_Upgrade_Empty.get(1),
                    new AspectList().add(Aspect.AIR, 80)
                        .add(Aspect.ENTROPY, 50)
                        .add(Aspect.ORDER, 50)
                        .add(Aspect.WATER, 80),
                    "AMB",
                    "CZD",
                    "EIF",
                    'A',
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.InfusedAir, 1),
                    'B',
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.InfusedEarth, 1),
                    'C',
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.InfusedFire, 1),
                    'D',
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.InfusedWater, 1),
                    'E',
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.InfusedOrder, 1),
                    'F',
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.InfusedEntropy, 1),
                    'M',
                    new ItemStack(ConfigItems.itemResource, 1, 10),
                    'Z',
                    broad,
                    'I',
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.PulsatingIron, 1)), });
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_UPGRADE_AIR",
            "Essentia: AIR",
            "I can feel it on the wind.",
            new String[] { "ESSENTIA_UPGRADE_BLANK" },
            "ARTIFICE",
            ItemRefer.Essentia_Upgrade_Air.get(1),
            1,
            0,
            -9,
            5,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.AER, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { "research.ESSENTIA_UPGRADE_AIR.page.0", GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                "ESSENTIA_UPGRADE_AIR",
                ItemRefer.Essentia_Upgrade_Empty.get(1),
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.cell, Materials.LiquidAir, 1),
                    GTOreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1),
                    GTOreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 1),
                    GTOreDictUnificator.get(OrePrefixes.cell, Materials.Argon, 1),
                    WerkstoffLoader.Neon.get(OrePrefixes.cell, 1), WerkstoffLoader.Krypton.get(OrePrefixes.cell, 1), },
                ItemRefer.Essentia_Upgrade_Air.get(1),
                5,
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AER, 128),
                    new TCAspects.TC_AspectStack(TCAspects.VOLATUS, 128),
                    new TCAspects.TC_AspectStack(TCAspects.MOTUS, 128),
                    new TCAspects.TC_AspectStack(TCAspects.AURAM, 128))),
                "research.ESSENTIA_UPGRADE_AIR.page.1" });
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_UPGRADE_THERMAL",
            "Essentia: THERMAL",
            "Melting down.",
            new String[] { "ESSENTIA_UPGRADE_BLANK" },
            "ARTIFICE",
            ItemRefer.Essentia_Upgrade_Thermal.get(1),
            1,
            0,
            -10,
            5,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { "research.ESSENTIA_UPGRADE_THERMAL.page.0",
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_UPGRADE_THERMAL",
                    ItemRefer.Essentia_Upgrade_Empty.get(1),
                    new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.lens, Materials.Firestone, 1),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 1), Ic2Items.reactorPlatingHeat,
                        ItemList.Casing_Coil_Nichrome.get(1), new ItemStack(ConfigItems.itemResource, 1, 1),
                        new ItemStack(ConfigItems.itemResource, 1, 0), },
                    ItemRefer.Essentia_Upgrade_Thermal.get(1),
                    5,
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1024))),
                "research.ESSENTIA_UPGRADE_THERMAL.page.1", "research.ESSENTIA_UPGRADE_THERMAL.page.2" });
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_UPGRADE_UNSTABLE",
            "Essentia: UNSTABLE",
            "Heart of chaos.",
            new String[] { "ESSENTIA_UPGRADE_BLANK" },
            "ARTIFICE",
            ItemRefer.Essentia_Upgrade_Unstable.get(1),
            1,
            0,
            -11,
            5,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { "research.ESSENTIA_UPGRADE_UNSTABLE.page.0",
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_UPGRADE_UNSTABLE",
                    ItemRefer.Essentia_Upgrade_Empty.get(1),
                    new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.cell, Materials.GasolinePremium, 1),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Unstable, 1),
                        GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Void, 1),
                        GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEntropy, 1), Ic2Items.industrialTnt,
                        GTModHandler.getModItem(ExtraUtilities.ID, "trashcan", 1, 0) },
                    ItemRefer.Essentia_Upgrade_Unstable.get(1),
                    6,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 128),
                        new TCAspects.TC_AspectStack(TCAspects.VACUOS, 128),
                        new TCAspects.TC_AspectStack(TCAspects.VENENUM, 128),
                        new TCAspects.TC_AspectStack(TCAspects.TELUM, 128))),
                "research.ESSENTIA_UPGRADE_UNSTABLE.page.1", "research.ESSENTIA_UPGRADE_UNSTABLE.page.2" });
        ItemStack meatDust = GTOreDictUnificator.get(OrePrefixes.dust, Materials.MeatRaw, 1);
        if (NewHorizonsCoreMod.isModLoaded())
            meatDust = GTModHandler.getModItem(NewHorizonsCoreMod.ID, "GTNHBioItems", 1, 2);
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_UPGRADE_VICTUS",
            "Essentia: VICTUS",
            "Will it bleed?",
            new String[] { "ESSENTIA_UPGRADE_BLANK" },
            "ARTIFICE",
            ItemRefer.Essentia_Upgrade_Victus.get(1),
            1,
            0,
            -12,
            5,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.VICTUS, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { "research.ESSENTIA_UPGRADE_VICTUS.page.0",
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_UPGRADE_VICTUS",
                    ItemRefer.Essentia_Upgrade_Empty.get(1),
                    new ItemStack[] { meatDust, ItemList.Food_Dough_Sugar.get(1),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Calcium, 1),
                        new ItemStack(Item.getItemById(367), 1), new ItemStack(ConfigItems.itemResource, 1, 4),
                        new ItemStack(ConfigBlocks.blockMetalDevice, 1, 8), },
                    ItemRefer.Essentia_Upgrade_Victus.get(1),
                    5,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.VICTUS, 128),
                        new TCAspects.TC_AspectStack(TCAspects.SPIRITUS, 128),
                        new TCAspects.TC_AspectStack(TCAspects.SANO, 128),
                        new TCAspects.TC_AspectStack(TCAspects.CORPUS, 128),
                        new TCAspects.TC_AspectStack(TCAspects.HUMANUS, 128))),
                "research.ESSENTIA_UPGRADE_VICTUS.page.1", "research.ESSENTIA_UPGRADE_VICTUS.page.2" });
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_UPGRADE_TAINTED",
            "Essentia: TAINTED",
            "Dirty Deeds Done Dirt Cheap",
            new String[] { "ESSENTIA_UPGRADE_BLANK" },
            "ARTIFICE",
            ItemRefer.Essentia_Upgrade_Tainted.get(1),
            1,
            0,
            -13,
            5,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.MORTUUS, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { "research.ESSENTIA_UPGRADE_TAINTED.page.0", "research.ESSENTIA_UPGRADE_TAINTED.page.1",
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_UPGRADE_TAINTED",
                    ItemRefer.Essentia_Upgrade_Empty.get(1),
                    new ItemStack[] { new ItemStack(ConfigBlocks.blockTaintFibres, 1, 0),
                        new ItemStack(ConfigBlocks.blockTaintFibres, 1, 2),
                        new ItemStack(ConfigItems.itemResource, 1, 11),
                        GTOreDictUnificator.get(OrePrefixes.spring, Materials.NaquadahEnriched, 1),
                        GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.EndSteel, 1),
                        new ItemStack(Block.getBlockById(138), 1), },
                    ItemRefer.Essentia_Upgrade_Tainted.get(1),
                    7,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.MORTUUS, 128),
                        new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 128),
                        new TCAspects.TC_AspectStack(TCAspects.EXANIMIS, 128),
                        new TCAspects.TC_AspectStack(TCAspects.VITIUM, 128))),
                "research.ESSENTIA_UPGRADE_TAINTED.page.2", "research.ESSENTIA_UPGRADE_TAINTED.page.3" });
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_UPGRADE_MECHANICS",
            "Essentia: MECHANICS",
            "Driven by Ether.",
            new String[] { "ESSENTIA_UPGRADE_BLANK" },
            "ARTIFICE",
            ItemRefer.Essentia_Upgrade_Mechanics.get(1),
            1,
            0,
            -14,
            5,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { "research.ESSENTIA_UPGRADE_MECHANICS.page.0",
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_UPGRADE_MECHANICS",
                    ItemRefer.Essentia_Upgrade_Empty.get(1),
                    new ItemStack[] { new ItemStack(ConfigBlocks.blockTube, 1, 4),
                        new ItemStack(ConfigBlocks.blockTube, 1, 2),
                        GTOreDictUnificator.get(OrePrefixes.rotor, Materials.VividAlloy, 1),
                        GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Polybenzimidazole, 1),
                        ItemList.Electric_Motor_IV.get(1), ItemList.Electric_Pump_IV.get(1), },
                    ItemRefer.Essentia_Upgrade_Mechanics.get(1),
                    5,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.ITER, 128),
                        new TCAspects.TC_AspectStack(TCAspects.LIMUS, 128),
                        new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128))),
                "research.ESSENTIA_UPGRADE_MECHANICS.page.1", "research.ESSENTIA_UPGRADE_MECHANICS.page.2" });
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_UPGRADE_SPRITE",
            "Essentia: SPRITE",
            "Brain in a Machine.",
            new String[] { "ESSENTIA_UPGRADE_BLANK" },
            "ARTIFICE",
            ItemRefer.Essentia_Upgrade_Spirit.get(1),
            1,
            0,
            -15,
            5,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { "research.ESSENTIA_UPGRADE_SPRITE.page.0", GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                "ESSENTIA_UPGRADE_SPRITE",
                ItemRefer.Essentia_Upgrade_Empty.get(1),
                new ItemStack[] { new ItemStack(ConfigBlocks.blockJar, 1, 1),
                    GTOreDictUnificator.get(OrePrefixes.food, Materials.Cheese, 1),
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Shadow, 1),
                    GTOreDictUnificator.get(OrePrefixes.spring, Materials.FierySteel, 1),
                    GTOreDictUnificator.get(OrePrefixes.cell, Materials.Milk, 1), ItemList.Machine_EV_Scanner.get(1) },
                ItemRefer.Essentia_Upgrade_Spirit.get(1),
                5,
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 128),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 128),
                    new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 128),
                    new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 128),
                    new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 128))),
                "research.ESSENTIA_UPGRADE_SPRITE.page.1", "research.ESSENTIA_UPGRADE_SPRITE.page.2" });
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_UPGRADE_RADIATION",
            "Essentia: RADIATION",
            "Atomic Heart",
            new String[] { "ESSENTIA_UPGRADE_BLANK" },
            "ARTIFICE",
            ItemRefer.Essentia_Upgrade_Radiation.get(1),
            1,
            0,
            -16,
            5,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.RADIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { "research.ESSENTIA_UPGRADE_RADIATION.page.0",
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_UPGRADE_RADIATION",
                    ItemRefer.Essentia_Upgrade_Empty.get(1),
                    new ItemStack[] { ItemRefer.High_Density_Plutonium.get(1), ItemRefer.High_Density_Uranium.get(1),
                        ItemRefer.High_Density_Thorium.get(1), Ic2Items.UranFuel, Ic2Items.MOXFuel,
                        WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1) },
                    ItemRefer.Essentia_Upgrade_Radiation.get(1),
                    8,
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.RADIO, 1024))),
                "research.ESSENTIA_UPGRADE_RADIATION.page.1", "research.ESSENTIA_UPGRADE_RADIATION.page.2" });
        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_UPGRADE_ELECTRIC",
            "Essentia: ELECTRIC",
            "Get electricity from... electricity?",
            new String[] { "ESSENTIA_UPGRADE_AIR", "ESSENTIA_UPGRADE_THERMAL", "ESSENTIA_UPGRADE_UNSTABLE",
                "ESSENTIA_UPGRADE_VICTUS", "ESSENTIA_UPGRADE_TAINTED", "ESSENTIA_UPGRADE_MECHANICS",
                "ESSENTIA_UPGRADE_SPRITE", "ESSENTIA_UPGRADE_RADIATION" },
            "ARTIFICE",
            ItemRefer.Essentia_Upgrade_Electric.get(1),
            1,
            0,
            -12,
            7,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 10L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { "research.ESSENTIA_UPGRADE_ELECTRIC.page.0",
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    "ESSENTIA_UPGRADE_RADIATION",
                    ItemRefer.Essentia_Upgrade_Empty.get(1),
                    new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 1),
                        GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 1),
                        GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 1),
                        GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1),
                        GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1),
                        GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1) },
                    ItemRefer.Essentia_Upgrade_Electric.get(1),
                    10,
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32768))),
                "research.ESSENTIA_UPGRADE_ELECTRIC.page.1" });

        ItemStack nodeLinkDevice = ThaumicBases.isModLoaded()
            ? GTModHandler.getModItem(ThaumicBases.ID, "nodeLinker", 1, 0)
            : new ItemStack(ConfigBlocks.blockStoneDevice, 1, 11);
        ItemStack alchemicalFurnace = ThaumicBases.isModLoaded()
            ? GTModHandler.getModItem(ThaumicBases.ID, "advAlchFurnace", 1, 0)
            : new ItemStack(ConfigBlocks.blockStoneDevice, 1, 0);
        ItemStack nitor = ThaumicTinkerer.isModLoaded()
            ? GTModHandler.getModItem(ThaumicTinkerer.ID, "brightNitor", 1, 0)
            : new ItemStack(ConfigItems.itemResource, 1, 1);
        ItemStack alchemicalBoiler = Automagy.isModLoaded() ? GTModHandler.getModItem(Automagy.ID, "blockBoiler", 1, 0)
            : new ItemStack(ConfigBlocks.blockStoneDevice, 1, 1);
        ItemStack essentiaLocus = Automagy.isModLoaded()
            ? GTModHandler.getModItem(Automagy.ID, "blockEssentiaLocus", 1, 0)
            : new ItemStack(ConfigBlocks.blockJar, 1, 1);
        ItemStack thauminiteBlock = ThaumicBases.isModLoaded()
            ? GTModHandler.getModItem(ThaumicBases.ID, "thauminiteBlock", 1, 0)
            : new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 4);
        GregTechAPI.sThaumcraftCompat
            .addResearch(
                "ESSENTIA_SMELTERY",
                "Large Essentia Smeltery",
                "You need a bigger boat.",
                new String[] { "INFUSION" },
                "ARTIFICE",
                ItemRefer.Large_Essentia_Smeltery.get(1),
                4,
                0,
                -16,
                3,
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.IGNIS, 10),
                    new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 10),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 10),
                    new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 10),
                    new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 10),
                    new TCAspects.TC_AspectStack(TCAspects.LIMUS, 10)),
                null,
                new Object[] { "research.ESSENTIA_SMELTERY.page.0",
                    GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                        "ESSENTIA_SMELTERY",
                        ItemList.Casing_Firebox_TungstenSteel.get(1),
                        new ItemStack[] { nodeLinkDevice, nitor, alchemicalFurnace, essentiaLocus, alchemicalBoiler,
                            new ItemStack(ConfigBlocks.blockCrystal, 1, 1),
                            new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3), ItemList.Electric_Piston_IV.get(1),
                            GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.FierySteel, 1L),
                            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L) },
                        ItemRefer.Large_Essentia_Smeltery.get(1),
                        16,
                        Arrays.asList(
                            new TCAspects.TC_AspectStack(TCAspects.AQUA, 256),
                            new TCAspects.TC_AspectStack(TCAspects.IGNIS, 256),
                            new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 256),
                            new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 256),
                            new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 256),
                            new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 256))),
                    "research.ESSENTIA_SMELTERY.page.1",
                    GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                        "ESSENTIA_GENERATOR",
                        ItemList.Hatch_Output_HV.get(1),
                        new ItemStack[] { new ItemStack(ConfigBlocks.blockJar, 1), ItemRefer.Magic_Casing.get(1),
                            new ItemStack(ConfigBlocks.blockTube, 1), ItemList.Electric_Pump_MV.get(1L) },
                        ItemRefer.Essentia_Output_Hatch.get(1),
                        6,
                        Arrays.asList(
                            new TCAspects.TC_AspectStack(TCAspects.AQUA, 128),
                            new TCAspects.TC_AspectStack(TCAspects.VACUOS, 64),
                            new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 32),
                            new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 32))),
                    "research.ESSENTIA_SMELTERY.page.2",
                    addArcaneCraftingRecipe(
                        "ESSENTIA_SMELTERY",
                        ItemRefer.Essentia_Filter_Casing.get(1),
                        new AspectList().add(Aspect.AIR, 70)
                            .add(Aspect.EARTH, 70)
                            .add(Aspect.FIRE, 70)
                            .add(Aspect.WATER, 70)
                            .add(Aspect.ORDER, 70)
                            .add(Aspect.ENTROPY, 70),
                        "ABA",
                        "CDC",
                        "EFE",
                        'A',
                        new ItemStack(ConfigBlocks.blockTube, 1, 3),
                        'B',
                        new ItemStack(ConfigBlocks.blockStoneDevice, 1, 14),
                        'C',
                        GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Void, 1),
                        'D',
                        new ItemStack(GregTechAPI.sBlockCasings3, 1, 11),
                        'E',
                        GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.NetherStar, 1),
                        'F',
                        thauminiteBlock) });

        if (ThaumicEnergistics.isModLoaded()) {
            ItemStack essentiaPump = WitchingGadgets.isModLoaded()
                ? GTModHandler.getModItem(WitchingGadgets.ID, "WG_MetalDevice", 1, 0)
                : new ItemStack(ConfigBlocks.blockTube, 1, 4);
            ItemStack inter = ThaumicTinkerer.isModLoaded()
                ? GTModHandler.getModItem(ThaumicTinkerer.ID, "interface", 1, 0)
                : new ItemStack(ConfigItems.itemResource, 1, 15);
            GregTechAPI.sThaumcraftCompat.addResearch(
                "ESSENTIA_OUTPUT_HATCH_ME",
                "Essentia Output Hatch (ME)",
                "It must exist.",
                new String[] { "INFUSION" },
                "ARTIFICE",
                ItemRefer.Essentia_Output_Hatch_ME.get(1),
                3,
                0,
                -15,
                3,
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 10),
                    new TCAspects.TC_AspectStack(TCAspects.VINCULUM, 10),
                    new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 10),
                    new TCAspects.TC_AspectStack(TCAspects.VACUOS, 10),
                    new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 10)),
                null,
                new Object[] { "research.ESSENTIA_OUTPUT_HATCH_ME.page.0",
                    GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                        "ESSENTIA_OUTPUT_HATCH_ME",
                        ItemRefer.Essentia_Output_Hatch.get(1),
                        new ItemStack[] {
                            GTModHandler
                                .getModItem(ThaumicEnergistics.ID, "thaumicenergistics.block.essentia.provider", 1),
                            new ItemStack(ConfigBlocks.blockEssentiaReservoir, 1, 0), essentiaPump, inter, },
                        ItemRefer.Essentia_Output_Hatch_ME.get(1),
                        8,
                        Arrays.asList(
                            new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 256),
                            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256),
                            new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 256),
                            new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 256))) });
        }
    }
}
