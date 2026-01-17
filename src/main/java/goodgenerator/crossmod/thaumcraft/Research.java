package goodgenerator.crossmod.thaumcraft;

import static gregtech.api.enums.Mods.Automagy;
import static gregtech.api.enums.Mods.ThaumicBases;
import static gregtech.api.enums.Mods.ThaumicEnergistics;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.enums.Mods.WitchingGadgets;
import static thaumcraft.api.ThaumcraftApi.addArcaneCraftingRecipe;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import bartworks.common.loaders.ItemRegistry;
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
        DescTextLocalization.addText("research.ESSENTIA_CELL.page", 1);

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
                        "ESSENTIA_SMELTERY",
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
                        thauminiteBlock),
                    GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                        "ESSENTIA_SMELTERY",
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
                        "ESSENTIA_SMELTERY",
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
                        Ic2Items.advancedMachine) });

        GregTechAPI.sThaumcraftCompat.addResearch(
            "ESSENTIA_CELL",
            "Better Cells",
            "And higher efficiency.",
            new String[] { "ESSENTIA_SMELTERY" },
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
                        new ItemStack(ConfigItems.itemResource, 1, 14),
                        GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Ichorium, 1),
                        new ItemStack(ConfigBlocks.blockJar, 1, 0), ItemList.FluidRegulator_LuV.get(1L) },
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
