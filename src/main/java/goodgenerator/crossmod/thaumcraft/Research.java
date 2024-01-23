package goodgenerator.crossmod.thaumcraft;

import static gregtech.api.enums.Mods.Automagy;
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

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.util.DescTextLocalization;
import goodgenerator.util.ItemRefer;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
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
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L)),
                null,
                new Object[] { "research.ESSENTIA_GENERATOR.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_GENERATOR",
                                ItemList.Hull_HV.get(1),
                                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                        new ItemStack(ConfigBlocks.blockJar, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Thaumium, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Emerald, 1L),
                                        new ItemStack(ConfigBlocks.blockWoodenDevice, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Manyullyn, 1L),
                                        Ic2Items.teslaCoil, ItemList.Sensor_MV.get(1) },
                                ItemRefer.Large_Essentia_Generator.get(1),
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 32),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 32))),
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_GENERATOR",
                                ItemList.Hatch_Input_HV.get(1),
                                new ItemStack[] { new ItemStack(ConfigBlocks.blockJar, 1),
                                        ItemRefer.Magic_Casing.get(1), new ItemStack(ConfigBlocks.blockTube, 1),
                                        ItemList.Electric_Pump_MV.get(1L) },
                                ItemRefer.Essentia_Hatch.get(1),
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 64),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 32),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 32))),
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_GENERATOR",
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Thaumium, 1),
                                new ItemStack[] {
                                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ElectricalSteel, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.ElectricalSteel, 1),
                                        ItemList.Electric_Pump_HV.get(1L), new ItemStack(ConfigBlocks.blockTube, 1, 4),
                                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), },
                                ItemRefer.Essentia_Cell_T1.get(1),
                                4,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 32),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 32),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 16))),
                        addArcaneCraftingRecipe(
                                "ESSENTIA_GENERATOR",
                                ItemRefer.Magic_Casing.get(1),
                                new AspectList().add(Aspect.AIR, 50).add(Aspect.FIRE, 50).add(Aspect.ORDER, 50),
                                "SCS",
                                "GAG",
                                "SCS",
                                'S',
                                new ItemStack(ConfigItems.itemResource, 1, 14),
                                'C',
                                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Thaumium, 1),
                                'G',
                                Ic2Items.reinforcedGlass,
                                'A',
                                Ic2Items.advancedMachine),
                        "research.ESSENTIA_GENERATOR.page.1", "research.ESSENTIA_GENERATOR.page.2",
                        "research.ESSENTIA_GENERATOR.page.3" });
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L)),
                null,
                new Object[] { "research.ESSENTIA_CELL.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_CELL",
                                ItemRefer.Essentia_Cell_T1.get(1),
                                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1),
                                        ItemList.Electric_Pump_IV.get(1L), ItemList.QuantumStar.get(1L),
                                        new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                                        ItemList.Reactor_Coolant_Sp_1.get(1L),
                                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Plutonium241, 1), },
                                ItemRefer.Essentia_Cell_T2.get(1),
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 64),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 64),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 32))),
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_CELL",
                                ItemRefer.Essentia_Cell_T2.get(1),
                                new ItemStack[] { ItemList.Field_Generator_IV.get(1L),
                                        ItemList.Electric_Pump_LuV.get(1L),
                                        new ItemStack(ConfigItems.itemResource, 1, 14), Ic2Items.fluidregulator,
                                        new ItemStack(ConfigBlocks.blockJar, 1, 0),
                                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Ichorium, 1) },
                                ItemRefer.Essentia_Cell_T3.get(1),
                                8,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 64))),
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_CELL",
                                ItemRefer.Essentia_Cell_T3.get(1),
                                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Europium, 1),
                                        ItemList.Emitter_LuV.get(1L), new ItemStack(ItemRegistry.bw_realglas, 1, 4),
                                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StellarAlloy, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.cellPlasma, Materials.Helium, 1),
                                        new ItemStack(ConfigItems.itemShard, 1, 6), },
                                ItemRefer.Essentia_Cell_T4.get(1),
                                10,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 256),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 256),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 128))) });
        ItemStack broad = new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6);
        if (NewHorizonsCoreMod.isModLoaded()) broad = GT_ModHandler.getModItem("dreamcraft", "item.ArcaneSlate", 1);
        GregTech_API.sThaumcraftCompat
                .addResearch(
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
                                new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                        null,
                        new Object[] { "research.ESSENTIA_UPGRADE_BLANK.page.0", addArcaneCraftingRecipe(
                                "ESSENTIA_UPGRADE_BLANK",
                                ItemRefer.Essentia_Upgrade_Empty.get(1),
                                new AspectList().add(Aspect.AIR, 80).add(Aspect.ENTROPY, 50).add(Aspect.ORDER, 50)
                                        .add(Aspect.WATER, 80),
                                "AMB",
                                "CZD",
                                "EIF",
                                'A',
                                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.InfusedAir, 1),
                                'B',
                                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.InfusedEarth, 1),
                                'C',
                                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.InfusedFire, 1),
                                'D',
                                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.InfusedWater, 1),
                                'E',
                                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.InfusedOrder, 1),
                                'F',
                                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.InfusedEntropy, 1),
                                'M',
                                new ItemStack(ConfigItems.itemResource, 1, 10),
                                'Z',
                                broad,
                                'I',
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.PulsatingIron, 1)), });
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.AER, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                null,
                new Object[] { "research.ESSENTIA_UPGRADE_AIR.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_UPGRADE_AIR",
                                ItemRefer.Essentia_Upgrade_Empty.get(1),
                                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidAir, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Argon, 1),
                                        WerkstoffLoader.Neon.get(OrePrefixes.cell, 1),
                                        WerkstoffLoader.Krypton.get(OrePrefixes.cell, 1), },
                                ItemRefer.Essentia_Upgrade_Air.get(1),
                                5,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AER, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MOTUS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 128))),
                        "research.ESSENTIA_UPGRADE_AIR.page.1" });
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                null,
                new Object[] { "research.ESSENTIA_UPGRADE_THERMAL.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_UPGRADE_THERMAL",
                                ItemRefer.Essentia_Upgrade_Empty.get(1),
                                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Firestone, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 1),
                                        Ic2Items.reactorPlatingHeat, ItemList.Casing_Coil_Nichrome.get(1),
                                        new ItemStack(ConfigItems.itemResource, 1, 1),
                                        new ItemStack(ConfigItems.itemResource, 1, 0), },
                                ItemRefer.Essentia_Upgrade_Thermal.get(1),
                                5,
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1024))),
                        "research.ESSENTIA_UPGRADE_THERMAL.page.1", "research.ESSENTIA_UPGRADE_THERMAL.page.2" });
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                null,
                new Object[] { "research.ESSENTIA_UPGRADE_UNSTABLE.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_UPGRADE_UNSTABLE",
                                ItemRefer.Essentia_Upgrade_Empty.get(1),
                                new ItemStack[] {
                                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.GasolinePremium, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Unstable, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Void, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEntropy, 1),
                                        Ic2Items.industrialTnt, new ItemStack(ItemRegistry.DESTRUCTOPACK) },
                                ItemRefer.Essentia_Upgrade_Unstable.get(1),
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 128))),
                        "research.ESSENTIA_UPGRADE_UNSTABLE.page.1", "research.ESSENTIA_UPGRADE_UNSTABLE.page.2" });
        ItemStack meatDust = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatRaw, 1);
        if (NewHorizonsCoreMod.isModLoaded()) meatDust = GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 1, 2);
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.VICTUS, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                null,
                new Object[] { "research.ESSENTIA_UPGRADE_VICTUS.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_UPGRADE_VICTUS",
                                ItemRefer.Essentia_Upgrade_Empty.get(1),
                                new ItemStack[] { meatDust, ItemList.Food_Dough_Sugar.get(1),
                                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Calcium, 1),
                                        new ItemStack(Item.getItemById(367), 1),
                                        new ItemStack(ConfigItems.itemResource, 1, 4),
                                        new ItemStack(ConfigBlocks.blockMetalDevice, 1, 8), },
                                ItemRefer.Essentia_Upgrade_Victus.get(1),
                                5,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VICTUS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.SPIRITUS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.CORPUS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.HUMANUS, 128))),
                        "research.ESSENTIA_UPGRADE_VICTUS.page.1", "research.ESSENTIA_UPGRADE_VICTUS.page.2" });
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.MORTUUS, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                null,
                new Object[] { "research.ESSENTIA_UPGRADE_TAINTED.page.0", "research.ESSENTIA_UPGRADE_TAINTED.page.1",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_UPGRADE_TAINTED",
                                ItemRefer.Essentia_Upgrade_Empty.get(1),
                                new ItemStack[] { new ItemStack(ConfigBlocks.blockTaintFibres, 1, 0),
                                        new ItemStack(ConfigBlocks.blockTaintFibres, 1, 2),
                                        new ItemStack(ConfigItems.itemResource, 1, 11),
                                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.NaquadahEnriched, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.EndSteel, 1),
                                        new ItemStack(Block.getBlockById(138), 1), },
                                ItemRefer.Essentia_Upgrade_Tainted.get(1),
                                7,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MORTUUS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.EXANIMIS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VITIUM, 128))),
                        "research.ESSENTIA_UPGRADE_TAINTED.page.2", "research.ESSENTIA_UPGRADE_TAINTED.page.3" });
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                null,
                new Object[] { "research.ESSENTIA_UPGRADE_MECHANICS.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_UPGRADE_MECHANICS",
                                ItemRefer.Essentia_Upgrade_Empty.get(1),
                                new ItemStack[] { new ItemStack(ConfigBlocks.blockTube, 1, 4),
                                        new ItemStack(ConfigBlocks.blockTube, 1, 2),
                                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.VividAlloy, 1),
                                        GT_OreDictUnificator
                                                .get(OrePrefixes.pipeMedium, Materials.Polybenzimidazole, 1),
                                        ItemList.Electric_Motor_IV.get(1), ItemList.Electric_Pump_IV.get(1), },
                                ItemRefer.Essentia_Upgrade_Mechanics.get(1),
                                5,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.LIMUS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128))),
                        "research.ESSENTIA_UPGRADE_MECHANICS.page.1", "research.ESSENTIA_UPGRADE_MECHANICS.page.2" });
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                null,
                new Object[] { "research.ESSENTIA_UPGRADE_SPRITE.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_UPGRADE_SPRITE",
                                ItemRefer.Essentia_Upgrade_Empty.get(1),
                                new ItemStack[] { new ItemStack(ConfigBlocks.blockJar, 1, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.food, Materials.Cheese, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Shadow, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.FierySteel, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Milk, 1),
                                        ItemList.Machine_EV_Scanner.get(1) },
                                ItemRefer.Essentia_Upgrade_Spirit.get(1),
                                5,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.SENSUS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 128))),
                        "research.ESSENTIA_UPGRADE_SPRITE.page.1", "research.ESSENTIA_UPGRADE_SPRITE.page.2" });
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.RADIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                null,
                new Object[] { "research.ESSENTIA_UPGRADE_RADIATION.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_UPGRADE_RADIATION",
                                ItemRefer.Essentia_Upgrade_Empty.get(1),
                                new ItemStack[] { ItemRefer.High_Density_Plutonium.get(1),
                                        ItemRefer.High_Density_Uranium.get(1), ItemRefer.High_Density_Thorium.get(1),
                                        Ic2Items.UranFuel, Ic2Items.MOXFuel,
                                        WerkstoffLoader.Tiberium.get(OrePrefixes.gem, 1) },
                                ItemRefer.Essentia_Upgrade_Radiation.get(1),
                                8,
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.RADIO, 1024))),
                        "research.ESSENTIA_UPGRADE_RADIATION.page.1", "research.ESSENTIA_UPGRADE_RADIATION.page.2" });
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                null,
                new Object[] { "research.ESSENTIA_UPGRADE_ELECTRIC.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_UPGRADE_RADIATION",
                                ItemRefer.Essentia_Upgrade_Empty.get(1),
                                new ItemStack[] {
                                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1),
                                        GT_OreDictUnificator
                                                .get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1) },
                                ItemRefer.Essentia_Upgrade_Electric.get(1),
                                10,
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32768))),
                        "research.ESSENTIA_UPGRADE_ELECTRIC.page.1" });

        ItemStack nodeLinkDevice = ThaumicBases.isModLoaded()
                ? GT_ModHandler.getModItem("thaumicbases", "nodeLinker", 1, 0)
                : new ItemStack(ConfigBlocks.blockStoneDevice, 1, 11);
        ItemStack alchemicalFurnace = ThaumicBases.isModLoaded()
                ? GT_ModHandler.getModItem("thaumicbases", "advAlchFurnace", 1, 0)
                : new ItemStack(ConfigBlocks.blockStoneDevice, 1, 0);
        ItemStack nitor = ThaumicTinkerer.isModLoaded()
                ? GT_ModHandler.getModItem("ThaumicTinkerer", "brightNitor", 1, 0)
                : new ItemStack(ConfigItems.itemResource, 1, 1);
        ItemStack alchemicalBoiler = Automagy.isModLoaded() ? GT_ModHandler.getModItem("Automagy", "blockBoiler", 1, 0)
                : new ItemStack(ConfigBlocks.blockStoneDevice, 1, 1);
        ItemStack essentiaLocus = Automagy.isModLoaded()
                ? GT_ModHandler.getModItem("Automagy", "blockEssentiaLocus", 1, 0)
                : new ItemStack(ConfigBlocks.blockJar, 1, 1);
        ItemStack thauminiteBlock = ThaumicBases.isModLoaded()
                ? GT_ModHandler.getModItem("thaumicbases", "thauminiteBlock", 1, 0)
                : new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 4);
        GregTech_API.sThaumcraftCompat.addResearch(
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
                        new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 10),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 10),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 10),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 10),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.LIMUS, 10)),
                null,
                new Object[] { "research.ESSENTIA_SMELTERY.page.0", GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                        "ESSENTIA_SMELTERY",
                        ItemList.Casing_Firebox_TungstenSteel.get(1),
                        new ItemStack[] { nodeLinkDevice, nitor, alchemicalFurnace, essentiaLocus, alchemicalBoiler,
                                new ItemStack(ConfigBlocks.blockCrystal, 1, 1),
                                new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3), ItemList.Electric_Piston_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.FierySteel, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L) },
                        ItemRefer.Large_Essentia_Smeltery.get(1),
                        16,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 256),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 256),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 256),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 256),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 256),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 256))),
                        "research.ESSENTIA_SMELTERY.page.1",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_GENERATOR",
                                ItemList.Hatch_Output_HV.get(1),
                                new ItemStack[] { new ItemStack(ConfigBlocks.blockJar, 1),
                                        ItemRefer.Magic_Casing.get(1), new ItemStack(ConfigBlocks.blockTube, 1),
                                        ItemList.Electric_Pump_MV.get(1L) },
                                ItemRefer.Essentia_Output_Hatch.get(1),
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 64),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 32),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 32))),
                        "research.ESSENTIA_SMELTERY.page.2",
                        addArcaneCraftingRecipe(
                                "ESSENTIA_SMELTERY",
                                ItemRefer.Essentia_Filter_Casing.get(1),
                                new AspectList().add(Aspect.AIR, 70).add(Aspect.EARTH, 70).add(Aspect.FIRE, 70)
                                        .add(Aspect.WATER, 70).add(Aspect.ORDER, 70).add(Aspect.ENTROPY, 70),
                                "ABA",
                                "CDC",
                                "EFE",
                                'A',
                                new ItemStack(ConfigBlocks.blockTube, 1, 3),
                                'B',
                                new ItemStack(ConfigBlocks.blockStoneDevice, 1, 14),
                                'C',
                                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Void, 1),
                                'D',
                                new ItemStack(GregTech_API.sBlockCasings3, 1, 11),
                                'E',
                                GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.NetherStar, 1),
                                'F',
                                thauminiteBlock) });

        if (ThaumicEnergistics.isModLoaded()) {
            ItemStack essentiaPump = WitchingGadgets.isModLoaded()
                    ? GT_ModHandler.getModItem("WitchingGadgets", "WG_MetalDevice", 1, 0)
                    : new ItemStack(ConfigBlocks.blockTube, 1, 4);
            ItemStack inter = ThaumicTinkerer.isModLoaded()
                    ? GT_ModHandler.getModItem("ThaumicTinkerer", "interface", 1, 0)
                    : new ItemStack(ConfigItems.itemResource, 1, 15);
            GregTech_API.sThaumcraftCompat.addResearch(
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
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 10),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.VINCULUM, 10),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 10),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 10),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10)),
                    null,
                    new Object[] { "research.ESSENTIA_OUTPUT_HATCH_ME.page.0",
                            GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                    "ESSENTIA_OUTPUT_HATCH_ME",
                                    ItemRefer.Essentia_Output_Hatch.get(1),
                                    new ItemStack[] {
                                            GT_ModHandler.getModItem(
                                                    "thaumicenergistics",
                                                    "thaumicenergistics.block.essentia.provider",
                                                    1),
                                            new ItemStack(ConfigBlocks.blockEssentiaReservoir, 1, 0), essentiaPump,
                                            inter, },
                                    ItemRefer.Essentia_Output_Hatch_ME.get(1),
                                    8,
                                    Arrays.asList(
                                            new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 256),
                                            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256),
                                            new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 256),
                                            new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 256))) });
        }
    }
}
