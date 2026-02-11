package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITSD;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipesMachinesMulti {

    public static void loadRecipes() {
        advHeatExchanger();
        distillus();
        cryoFreezer();
        volcanus();
        steamMultis();

        multiArcFurnace();
        multiDehydrator();
        multiAlloySmelter();
        multiRockBreaker();
        multiFluidHeater();
        multiMassFabricator();
        multiForgeHammer();
        multiReplicator();
        multiChisel();
        multiCentrifuge();
        multiCokeOven();
        multiElectrolyzer();
        multiBender();
        multiMacerator();
        multiWiremill();
        multiSifter();
        multiThermalCentrifuge();
        multiWasher();
        multiCutter();
        multiExtruder();
        multiImplo();
        multiPackager();
        multiMixer();
        multiAssembler();
    }

    private static void multiFluidHeater() {
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_IndustrialFluidHeater.get(1),
            BITSD,
            new Object[] { "PCP", "IHI", "PDP", 'P', MaterialsAlloy.INCONEL_625.getPlate(1), 'C', "circuitElite", 'I',
                "pipeHugeTantalloy60", 'H', ItemList.Machine_IV_FluidHeater.get(1), 'D', "circuitData" });
    }

    private static void advHeatExchanger() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Machine_Multi_HeatExchanger.get(1),
                MaterialsAlloy.ZERON_100.getPlateDouble(8),
                MaterialsAlloy.ZERON_100.getScrew(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.XL_HeatExchanger.get(1))
            .fluidInputs(Materials.TungstenSteel.getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_StableTitanium.get(1),
                MaterialsAlloy.INCONEL_625.getPlate(4),
                MaterialsAlloy.INCONEL_625.getScrew(8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_XL_HeatExchanger.get(1))
            .fluidInputs(Materials.TungstenSteel.getMolten(2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }

    private static void multiForgeHammer() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(2),
                ItemList.Machine_IV_Hammer.get(1),
                MaterialsAlloy.INCOLOY_DS.getPlate(8),
                MaterialsAlloy.ENERGYCRYSTAL.getBolt(32),
                MaterialsElements.getInstance().ZIRCONIUM.getFineWire(32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4L))
            .itemOutputs(GregtechItemList.Controller_IndustrialForgeHammer.get(1))
            .fluidInputs(MaterialsAlloy.INCOLOY_DS.getFluidStack(12 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_IndustrialForgeHammer.get(1),
            BITSD,
            new Object[] { "IBI", "HCH", "IHI", 'I', MaterialsAlloy.INCOLOY_DS.getPlate(1), 'B',
                MaterialsAlloy.BABBIT_ALLOY.getPlate(1), 'C', ItemList.Casing_HeatProof.get(1), 'H',
                MaterialsAlloy.HASTELLOY_X.getRod(1) });
    }

    private static void multiReplicator() {
        // Elemental Duplicator
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_IV_Replicator.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.Hull_ZPM.get(4),
                ItemList.Field_Generator_IV.get(16),
                ItemList.Electric_Motor_ZPM.get(16),
                ItemList.Electric_Piston_ZPM.get(4),
                GregtechItemList.Energy_Core_LuV.get(2),
                MaterialsAlloy.PIKYONIUM.getPlate(16),
                MaterialsAlloy.PIKYONIUM.getScrew(32),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getBolt(32),
                MaterialsAlloy.ZERON_100.getRod(10),
                new Object[] { "circuitUltimate", 20 },
                ItemList.Tool_DataOrb.get(32),
                GregtechItemList.Laser_Lens_Special.get(1))
            .fluidInputs(
                MaterialsAlloy.PIKYONIUM.getFluidStack(32 * INGOTS),
                MaterialsAlloy.LAFIUM.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(16 * INGOTS),
                MaterialsAlloy.BABBIT_ALLOY.getFluidStack(128 * INGOTS))
            .itemOutputs(GregtechItemList.Controller_ElementalDuplicator.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Data Orb Repository
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Modulator_III.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                GregtechItemList.GTPP_Casing_ZPM.get(2),
                ItemList.Field_Generator_EV.get(4),
                GregtechItemList.Energy_Core_EV.get(2),
                MaterialsAlloy.PIKYONIUM.getPlate(8),
                MaterialsAlloy.ZERON_100.getScrew(16),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getBolt(16),
                MaterialsAlloy.INCONEL_625.getRod(16),
                new Object[] { "circuitMaster", 32 },
                ItemList.Tool_DataOrb.get(32))
            .fluidInputs(
                MaterialsAlloy.ZERON_100.getFluidStack(16 * INGOTS),
                MaterialsAlloy.ARCANITE.getFluidStack(8 * INGOTS),
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(8 * INGOTS),
                MaterialsAlloy.BABBIT_ALLOY.getFluidStack(64 * INGOTS))
            .itemOutputs(GregtechItemList.Hatch_Input_Elemental_Duplicator.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Elemental Confinement Shell
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.ResonanceChamber_III.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.Hull_LuV.get(5),
                ItemList.Field_Generator_HV.get(16),
                GregtechItemList.Energy_Core_MV.get(2),
                MaterialsAlloy.PIKYONIUM.getPlate(4),
                MaterialsAlloy.PIKYONIUM.getScrew(4),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getBolt(8),
                MaterialsAlloy.INCONEL_625.getRod(4),
                new Object[] { "circuitElite", 4 },
                ItemList.Tool_DataStick.get(4))
            .fluidInputs(
                MaterialsAlloy.INCONEL_625.getFluidStack(16 * INGOTS),
                MaterialsAlloy.INCONEL_792.getFluidStack(8 * INGOTS),
                MaterialsAlloy.HASTELLOY_N.getFluidStack(8 * INGOTS),
                MaterialsAlloy.BABBIT_ALLOY.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Casing_ElementalDuplicator.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
    }

    private static void multiAlloySmelter() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1),
                ItemList.Machine_IV_AlloySmelter.get(1),
                MaterialsAlloy.TANTALUM_CARBIDE.getGear(16),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Titanium, 64),
                MaterialsAlloy.INCOLOY_DS.getPlate(16))
            .circuit(6)
            .itemOutputs(GregtechItemList.Industrial_AlloySmelter.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_792.getFluidStack(8 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void distillus() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Distillation_Tower.get(2),
                GregtechItemList.GTPP_Casing_IV.get(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 8))
            .itemOutputs(GregtechItemList.Machine_Adv_DistillationTower.get(1))
            .fluidInputs(
                MaterialsAlloy.AQUATIC_STEEL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.BABBIT_ALLOY.getFluidStack(16 * INGOTS),
                Materials.Bronze.getMolten(64 * INGOTS),
                Materials.Kanthal.getMolten(16 * INGOTS))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);
    }

    private static void steamMultis() {
        // Steam Grinder
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamMaceratorMulti.get(1),
            new Object[] { "CDC", "PFP", "CDC", 'C', ItemList.Casing_BronzePlatedBricks, 'D', "gemDiamond", 'P',
                OreDictNames.craftingPiston, 'F', MaterialsAlloy.TUMBAGA.getFrameBox(1) });

        // Steam Purifier
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamWasherMulti.get(1),
            new Object[] { "CPC", "RFR", "CPC", 'C', ItemList.Casing_BronzePlatedBricks, 'P',
                OrePrefixes.plate.get(Materials.WroughtIron), 'R', OrePrefixes.rotor.get(Materials.Tin), 'F',
                MaterialsAlloy.TUMBAGA.getFrameBox(1), });

        // Steam Blender
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamMixerMulti.get(1),
            new Object[] { "CRC", "OFO", "CRC", 'C', ItemList.Casing_BronzePlatedBricks, 'R',
                MaterialsAlloy.TUMBAGA.getRing(1), 'O', MaterialsAlloy.TUMBAGA.getRotor(1), 'F',
                MaterialsAlloy.TUMBAGA.getFrameBox(1) });

        // Water Pump
        GTModHandler.addCraftingRecipe(
            GregtechItemList.WaterPump.get(1),
            new Object[] { "FFF", "FGF", "CCC", 'F', OrePrefixes.frameGt.get(Materials.Bronze), 'G',
                OrePrefixes.gearGt.get(Materials.Bronze), 'C', ItemList.WoodenCasing });

        // Steam Separator
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamCentrifugeMulti.get(1),
            new Object[] { "CPC", "GFG", "CPC", 'C', ItemList.Casing_BronzePlatedBricks, 'P',
                OrePrefixes.plate.get(Materials.WroughtIron), 'G', OrePrefixes.gearGt.get(Materials.Bronze), 'F',
                MaterialsAlloy.TUMBAGA.getFrameBox(1) });

        // Steam Presser
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamForgeHammerMulti.get(1),
            new Object[] { "CPC", "PAP", "CFC", 'C', ItemList.Casing_BronzePlatedBricks, 'P',
                OrePrefixes.plate.get(Materials.WroughtIron), 'A', OreDictNames.craftingAnvil, 'F',
                MaterialsAlloy.TUMBAGA.getFrameBox(1) });

        // Steam Squasher
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_SteamCompressorMulti.get(1),
            new Object[] { "CPC", "GFG", "CPC", 'C', ItemList.Casing_BronzePlatedBricks, 'P',
                OreDictNames.craftingPiston, 'G', MaterialsAlloy.TUMBAGA.getGear(1), 'F',
                MaterialsAlloy.TUMBAGA.getFrameBox(1) });

        if (EtFuturumRequiem.isModLoaded()) {
            // Steam Fuser
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamAlloySmelterMulti.get(1),
                new Object[] { "BTB", "FUF", "BLB", 'B', ItemList.Casing_BronzePlatedBricks.get(1L), 'T',
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bronze, 1L), 'F',
                    getModItem(EtFuturumRequiem.ID, "blast_furnace", 1, 0), 'U', MaterialsAlloy.TUMBAGA.getFrameBox(1),
                    'L', GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Bronze, 1L) });

            // Steam Hearth
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamFurnaceMulti.get(1),
                new Object[] { "RGR", "YBZ", "WFW", 'R', OrePrefixes.plateDouble.get(Materials.Bronze), 'G',
                    MaterialsAlloy.TUMBAGA.getGear(1), 'Y', getModItem(EtFuturumRequiem.ID, "blast_furnace", 1, 0), 'B',
                    ItemList.Machine_HP_Furnace, 'Z', getModItem(EtFuturumRequiem.ID, "smoker", 1, 0), 'W',
                    OrePrefixes.plateDouble.get(Materials.WroughtIron), 'F', MaterialsAlloy.TUMBAGA.getFrameBox(1) });
        }

        // Steam Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Input_Steam.get(1),
            new Object[] { "PBP", "PTP", "PBP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'B',
                OrePrefixes.pipeMedium.get(Materials.Bronze), 'T', GregtechItemList.GTFluidTank_ULV.get(1) });

        // Steam Input Bus
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Input_Bus_Steam.get(1),
            new Object[] { "BTB", "SHS", "BTB", 'B', OrePrefixes.plate.get(Materials.Bronze), 'T',
                MaterialsAlloy.TUMBAGA.getPlate(1), 'S', OrePrefixes.plate.get(Materials.Tin), 'H',
                new ItemStack(Blocks.hopper) });

        // Steam Output Bus
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Output_Bus_Steam.get(1),
            new Object[] { "BSB", "THT", "BSB", 'B', OrePrefixes.plate.get(Materials.Bronze), 'T',
                MaterialsAlloy.TUMBAGA.getPlate(1), 'S', OrePrefixes.plate.get(Materials.Tin), 'H',
                new ItemStack(Blocks.hopper) });
    }

    public static void multiCentrifuge() {
        // Industrial Centrifuge
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_Centrifuge.get(1),
            new Object[] { "ABA", "CDC", "EFE", 'A', "circuitData", 'B',
                OrePrefixes.pipeHuge.get(Materials.StainlessSteel), 'C', MaterialsAlloy.MARAGING250.getPlate(1), 'D',
                ItemList.Machine_EV_Centrifuge, 'E', MaterialsAlloy.INCONEL_792.getPlate(1), 'F', ItemList.Casing_EV });

        // Centrifuge Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Centrifuge1.get(1),
            new Object[] { "ABA", "CBC", "ABA", 'A', MaterialsAlloy.MARAGING250.getPlate(1), 'B',
                MaterialsAlloy.TUMBAGA.getRod(1), 'C', MaterialsAlloy.INCONEL_792.getPlate(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.MARAGING250.getPlate(4),
                MaterialsAlloy.INCONEL_792.getPlate(2),
                MaterialsAlloy.TUMBAGA.getRod(3))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Centrifuge1.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void multiCokeOven() {
        // Industrial Coke Oven
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_CokeOven.get(1),
            new Object[] { "PCP", "HOH", "PCP", 'P', MaterialsAlloy.TANTALLOY_61.getPlate(1), 'C', "circuitData", 'H',
                ItemList.Casing_EV, 'O', ItemList.CokeOvenController });

        // Structural Coke Oven Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_CokeOven.get(1),
            new Object[] { "PRP", "RFR", "PRP", 'P', MaterialsAlloy.TANTALLOY_61.getPlate(1), 'R',
                MaterialsAlloy.TANTALLOY_61.getRod(1), 'F', MaterialsAlloy.TANTALLOY_61.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.TANTALLOY_61.getPlate(4),
                MaterialsAlloy.TANTALLOY_61.getRod(4),
                MaterialsAlloy.TANTALLOY_61.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_CokeOven.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Heat Resistant Coke Oven Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_CokeOven_Coil1.get(1),
            new Object[] { "PPP", "FCF", "PPP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.TPV), 'C', ItemList.Casing_Gearbox_Bronze });

        // Heat Proof Coke Oven Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_CokeOven_Coil2.get(1),
            new Object[] { "PPP", "FCF", "PPP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                OrePrefixes.frameGt.get(Materials.HSSS), 'C', ItemList.Casing_Gearbox_Steel });
    }

    private static void multiElectrolyzer() {
        // Electrolyzer Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Electrolyzer.get(1),
            new Object[] { "PCP", "RFR", "PRP", 'P', MaterialsAlloy.POTIN.getPlate(1), 'C',
                OrePrefixes.stickLong.get(Materials.Chrome), 'R', MaterialsAlloy.POTIN.getLongRod(1), 'F',
                MaterialsAlloy.POTIN.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.POTIN.getPlate(4),
                MaterialsAlloy.POTIN.getLongRod(3),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Chrome, 1),
                MaterialsAlloy.POTIN.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Electrolyzer.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Electrolyzer
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_Electrolyzer.get(1),
            new Object[] { "PCP", "HMH", "PRP", 'P', MaterialsAlloy.STELLITE.getPlate(1), 'C', "circuitElite", 'H',
                ItemList.Casing_IV, 'M', ItemList.Machine_IV_Electrolyzer, 'R', MaterialsAlloy.STELLITE.getRotor(1) });
    }

    private static void multiBender() {
        // Material Press Machine Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MaterialPress.get(1),
            new Object[] { "PBP", "TFT", "PBP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'B',
                MaterialsAlloy.TUMBAGA.getLongRod(1), 'T', MaterialsAlloy.TANTALLOY_60.getRod(1), 'F',
                MaterialsAlloy.TUMBAGA.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 4),
                MaterialsAlloy.TANTALLOY_60.getRod(2),
                MaterialsAlloy.TUMBAGA.getLongRod(2),
                MaterialsAlloy.TUMBAGA.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_MaterialPress.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Material Press
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_PlatePress.get(1),
            new Object[] { "PGP", "MFN", "PRP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'G',
                OrePrefixes.gearGt.get(Materials.Titanium), 'R', MaterialsAlloy.TANTALLOY_60.getGear(1), 'F',
                OrePrefixes.frameGt.get(Materials.Titanium), 'M', ItemList.Machine_EV_Press, 'N',
                ItemList.Machine_EV_Bender });
    }

    private static void multiMacerator() {
        // Maceration Stack Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MacerationStack.get(1),
            new Object[] { "PPP", "RFR", "PXP", 'P', OrePrefixes.plate.get(Materials.Palladium), 'R',
                OrePrefixes.stick.get(Materials.Platinum), 'F', MaterialsAlloy.INCONEL_625.getFrameBox(1), 'X',
                OrePrefixes.stickLong.get(Materials.Palladium) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Palladium, 5),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Platinum, 2),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Palladium, 1),
                MaterialsAlloy.INCONEL_625.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_MacerationStack.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Maceration Stack
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_MacerationStack.get(1),
            new Object[] { "PMP", "MCM", "PMP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'M',
                ItemList.Machine_EV_Macerator, 'C', "circuitData" });

        // Maceration Upgrade Chip
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Maceration_Upgrade_Chip.get(1),
            new Object[] { "PMP", "MCM", "PMP", 'P', OrePrefixes.plate.get(Materials.TungstenCarbide), 'M',
                ItemList.Machine_IV_Macerator, 'C', "circuitUltimate" });
    }

    private static void multiWiremill() {
        // Wire Factory Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_WireFactory.get(1),
            new Object[] { "PRP", "RFR", "PRP", 'P', OrePrefixes.plate.get(Materials.BlueSteel), 'R',
                OrePrefixes.stick.get(Materials.BlueSteel), 'F', OrePrefixes.frameGt.get(Materials.BlueSteel), });

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.BlueSteel, 4),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.BlueSteel, 4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlueSteel, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_WireFactory.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Wire Factory
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialWireFactory.get(1),
            new Object[] { "PHP", "CMC", "PHP", 'P', OrePrefixes.plate.get(Materials.BlueSteel), 'H',
                ItemList.Casing_IV, 'C', "circuitElite", 'M', ItemList.Machine_IV_Wiremill });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.IndustrialWireFactory.get(1),
            new Object[] { GregtechItemList.Industrial_WireFactory });
    }

    private static void multiMassFabricator() {
        // Matter Fabricator CPU
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_MassFab.get(1),
            new Object[] { "PCP", "WHW", "PCP", 'P', MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(1), 'C',
                "circuitSuperconductor", 'W', OrePrefixes.cableGt04.get(Materials.NaquadahAlloy), 'H',
                ItemList.Casing_UV });

        // Matter Fabricator Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MatterFab.get(1),
            new Object[] { "PRP", "RFR", "PRP", 'P', MaterialsAlloy.NIOBIUM_CARBIDE.getPlate(1), 'R',
                MaterialsAlloy.INCONEL_792.getRod(1), 'F', MaterialsAlloy.INCONEL_690.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.NIOBIUM_CARBIDE.getPlate(4),
                MaterialsAlloy.INCONEL_792.getRod(4),
                MaterialsAlloy.INCONEL_690.getFrameBox(1))
            .itemOutputs(GregtechItemList.Casing_MatterFab.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Matter Generation Coil
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MatterGen.get(1),
            new Object[] { "PRP", "FHF", "PRP", 'P', MaterialsAlloy.ZERON_100.getPlate(1), 'R',
                MaterialsAlloy.PIKYONIUM.getPlate(1), 'F', MaterialsAlloy.STELLITE.getFrameBox(1), 'H',
                ItemList.Casing_UV });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_UV.get(1),
                MaterialsAlloy.ZERON_100.getPlate(4),
                MaterialsAlloy.PIKYONIUM.getPlate(2),
                MaterialsAlloy.STELLITE.getFrameBox(2))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_MatterGen.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Research on Containment Fields
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Field_Generator_LuV.get(1), ItemList.Emitter_ZPM.get(2))
            .itemOutputs(
                ItemDummyResearch.getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_1_CONTAINMENT, 1))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(laserEngraverRecipes);

        // Containment Casing
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemDummyResearch.getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_1_CONTAINMENT, 1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                ItemList.Field_Generator_IV.get(32),
                ItemList.Electric_Motor_EV.get(64),
                ItemList.Energy_LapotronicOrb.get(32),
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.YttriumBariumCuprate, 32),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Platinum, 64),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Naquadria, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 16),
                MaterialsAlloy.ARCANITE.getGear(8),
                new Object[] { "circuitElite", 64 },
                new Object[] { "circuitMaster", 32 },
                new Object[] { "circuitUltimate", 16 },
                GregtechItemList.Laser_Lens_Special.get(1),
                GregtechItemList.DehydratorCoilWireZPM.get(64))
            .fluidInputs(
                MaterialsAlloy.NITINOL_60.getFluidStack(36 * INGOTS),
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(1 * STACKS + 8 * INGOTS),
                MaterialsAlloy.TUMBAGA.getFluidStack(4 * STACKS + 32 * INGOTS),
                Materials.Nichrome.getMolten(16 * INGOTS))
            .itemOutputs(new ItemStack(ModBlocks.blockCasings3Misc, 32, 15))
            .eut(TierEU.RECIPE_LuV)
            .duration(20 * MINUTES)
            .addTo(AssemblyLine);
    }

    private static void multiSifter() {
        // Large Sifter Control Block
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_Sifter.get(1),
            new Object[] { "PCP", "WMW", "PCP", 'P', MaterialsAlloy.EGLIN_STEEL.getPlate(1), 'C', "circuitAdvanced",
                'W', OrePrefixes.cableGt04.get(Materials.Gold), 'M', ItemList.Machine_HV_Sifter });

        // Industrial Sieve Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Sifter.get(1),
            new Object[] { "PPP", "PFP", "PPP", 'P', MaterialsAlloy.EGLIN_STEEL.getPlate(1), 'F',
                MaterialsAlloy.TUMBAGA.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsAlloy.EGLIN_STEEL.getPlate(8), MaterialsAlloy.TUMBAGA.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Sifter.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Sieve Grate
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_SifterGrate.get(1),
            new Object[] { "FWF", "WWW", "FWF", 'F', MaterialsAlloy.EGLIN_STEEL.getFrameBox(1), 'W',
                OrePrefixes.wireFine.get(Materials.Steel) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 5),
                MaterialsAlloy.EGLIN_STEEL.getFrameBox(4))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_SifterGrate.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void multiThermalCentrifuge() {
        // Thermal Processing Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_ThermalCentrifuge.get(1),
            new Object[] { "PhP", "PFP", "PwP", 'P', OrePrefixes.plate.get(Materials.RedSteel), 'F',
                OrePrefixes.frameGt.get(Materials.BlackSteel) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.RedSteel, 6),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_ThermalCentrifuge.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Large Thermal Refinery
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_ThermalCentrifuge.get(1),
            new Object[] { "PCP", "RMR", "PGP", 'P', OrePrefixes.plate.get(Materials.RedSteel), 'C', "circuitData", 'R',
                MaterialsAlloy.TALONITE.getRod(1), 'M', ItemList.Machine_EV_ThermalCentrifuge, 'G',
                MaterialsAlloy.TALONITE.getGear(1) });
    }

    private static void multiWasher() {
        // Wash Plant Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_WashPlant.get(1),
            new Object[] { "PhP", "TFT", "PwP", 'P', MaterialsAlloy.LEAGRISIUM.getPlate(1), 'T',
                MaterialsAlloy.TALONITE.getPlate(1), 'F', MaterialsAlloy.LEAGRISIUM.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.LEAGRISIUM.getPlate(4),
                MaterialsAlloy.TALONITE.getPlate(2),
                MaterialsAlloy.LEAGRISIUM.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_WashPlant.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Ore Washing Plant
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_WashPlant.get(1),
            new Object[] { "PAP", "TCT", "PBP", 'P', MaterialsAlloy.LEAGRISIUM.getPlate(1), 'A',
                ItemList.Machine_EV_OreWasher, 'T', MaterialsAlloy.TALONITE.getPlate(1), 'C', "circuitData", 'B',
                ItemList.Machine_EV_ChemicalBath });
    }

    private static void multiCutter() {
        // Cutting Factory Frame
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_CuttingFactoryFrame.get(1),
            new Object[] { "PhP", "SFS", "PwP", 'P', MaterialsAlloy.MARAGING300.getPlate(1), 'S',
                MaterialsAlloy.STELLITE.getPlate(1), 'F', MaterialsAlloy.TALONITE.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.MARAGING300.getPlate(4),
                MaterialsAlloy.STELLITE.getPlate(2),
                MaterialsAlloy.TALONITE.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_CuttingFactoryFrame.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Cutting Factory
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_CuttingFactoryController.get(1),
            new Object[] { "PCP", "WMW", "PCP", 'P', MaterialsAlloy.MARAGING300.getPlate(1), 'C', "circuitData", 'W',
                OrePrefixes.wireFine.get(Materials.Platinum), 'M', ItemList.Machine_IV_Cutter });
    }

    private static void multiExtruder() {
        // Inconel Reinforced Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Extruder.get(1),
            new Object[] { "PhP", "TFT", "PwP", 'P', MaterialsAlloy.INCONEL_690.getPlate(1), 'T',
                MaterialsAlloy.TALONITE.getPlate(1), 'F', MaterialsAlloy.STABALLOY.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.INCONEL_690.getPlate(4),
                MaterialsAlloy.TALONITE.getPlate(2),
                MaterialsAlloy.STABALLOY.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Extruder.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Industrial Extrusion Machine
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_Extruder.get(1),
            new Object[] { "PCP", "IMI", "PCP", 'P', MaterialsAlloy.INCONEL_690.getPlate(1), 'C', "circuitElite", 'I',
                ItemList.Electric_Piston_IV, 'M', ItemList.Machine_IV_Extruder });
    }

    private static void cryoFreezer() {
        // Advanced Cryogenic Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_AdvancedVacuum.get(1),
            new Object[] { "PGP", "AFB", "PGP", 'P', MaterialsAlloy.LEAGRISIUM.getPlateDouble(1), 'G',
                MaterialsAlloy.INCOLOY_MA956.getGear(1), 'A', ItemList.Reactor_Coolant_He_6, 'F',
                MaterialsAlloy.NITINOL_60.getFrameBox(1), 'B', ItemList.Reactor_Coolant_NaK_6.get(1) });

        // Cryogenic Freezer
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_Cryogenic_Freezer.get(1),
            new Object[] { "GCG", "PXP", "DOD", 'G', MaterialsAlloy.INCOLOY_MA956.getGear(1), 'C', "circuitMaster", 'P',
                ItemList.Electric_Piston_IV, 'X', GregtechItemList.Casing_AdvancedVacuum, 'D',
                MaterialsAlloy.LEAGRISIUM.getPlateDouble(1), 'O', GregtechItemList.Gregtech_Computer_Cube });

        // Cryotheum Cooling Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Input_Cryotheum.get(1L),
            new Object[] { "MGM", "CBC", "PHP", 'M', MaterialsAlloy.MARAGING250.getPlate(1), 'G',
                MaterialsAlloy.MARAGING250.getGear(1), 'C', "circuitData", 'B',
                GregtechItemList.Casing_AdvancedVacuum.get(1), 'P', Materials.Aluminium.getPlates(1), 'H',
                ItemList.Hatch_Input_IV.get(1) });
    }

    private static void volcanus() {
        // Volcanus Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Adv_BlastFurnace.get(1),
            new Object[] { "PAP", "BFC", "PGP", 'P', MaterialsAlloy.HASTELLOY_N.getPlateDouble(1), 'A',
                getModItem(Mods.IndustrialCraft2.ID, "reactorHeatSwitchDiamond", 1, 1), 'B',
                getModItem(Mods.IndustrialCraft2.ID, "reactorVentGold", 1, 1), 'C',
                getModItem(Mods.IndustrialCraft2.ID, "reactorVentDiamond", 1, 1), 'F',
                MaterialsAlloy.HASTELLOY_X.getFrameBox(1), 'G', MaterialsAlloy.HASTELLOY_W.getGear(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.HASTELLOY_X.getFrameBox(1),
                MaterialsAlloy.HASTELLOY_N.getPlateDouble(4),
                MaterialsAlloy.HASTELLOY_W.getGear(1),
                getModItem(Mods.IndustrialCraft2.ID, "reactorHeatSwitchDiamond", 1, 1),
                getModItem(Mods.IndustrialCraft2.ID, "reactorVentGold", 1, 1),
                getModItem(Mods.IndustrialCraft2.ID, "reactorVentDiamond", 1, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Adv_BlastFurnace.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Volcanus
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Machine_Adv_BlastFurnace.get(1),
            new Object[] { "GCG", "RXR", "PZP", 'G', MaterialsAlloy.HASTELLOY_W.getGear(1), 'C', "circuitMaster", 'R',
                ItemList.Robot_Arm_IV, 'X', GregtechItemList.Casing_Adv_BlastFurnace, 'P',
                MaterialsAlloy.HASTELLOY_N.getPlateDouble(1), 'Z', GregtechItemList.Gregtech_Computer_Cube });

        // Pyrotheum Heating Vent
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Input_Pyrotheum.get(1L),
            new Object[] { "MGM", "CBC", "MHM", 'M', MaterialsAlloy.MARAGING250.getPlate(1), 'G',
                MaterialsAlloy.MARAGING300.getGear(1), 'C', "circuitElite", 'B',
                GregtechItemList.Casing_Adv_BlastFurnace.get(1), 'H', ItemList.Hatch_Input_IV.get(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_IV.get(1),
                GregtechItemList.Casing_Adv_BlastFurnace.get(1),
                MaterialsAlloy.MARAGING250.getPlate(4),
                MaterialsAlloy.MARAGING300.getGear(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 2))
            .circuit(1)
            .itemOutputs(GregtechItemList.Hatch_Input_Pyrotheum.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void multiImplo() {
        // Density^2
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Machine_Adv_ImplosionCompressor.get(1),
            new Object[] { "GCG", "FHR", "IXI", 'G', MaterialsAlloy.LEAGRISIUM.getGear(1), 'C', "circuitMaster", 'F',
                ItemList.Field_Generator_IV, 'H', ItemList.Hull_ZPM, 'R', ItemList.Robot_Arm_IV, 'I',
                "plateAlloyIridium", 'X', GregtechItemList.Gregtech_Computer_Cube });
    }

    private static void multiPackager() {
        // Supply Depot Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_AmazonWarehouse.get(1),
            new Object[] { "PMP", "wFh", "PCP", 'P', MaterialsAlloy.HASTELLOY_C276.getPlateDouble(1), 'M',
                ItemList.Electric_Motor_HV, 'F', MaterialsAlloy.TUNGSTEN_CARBIDE.getFrameBox(1), 'C',
                ItemList.Conveyor_Module_HV, });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.TUNGSTEN_CARBIDE.getFrameBox(1),
                MaterialsAlloy.HASTELLOY_C276.getPlateDouble(4),
                ItemList.Electric_Motor_HV.get(1),
                ItemList.Conveyor_Module_HV.get(1))
            .itemOutputs(GregtechItemList.Casing_AmazonWarehouse.get(1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Amazon Warehousing Depot
        GTModHandler.addCraftingRecipe(
            ItemList.IndustrialPackager.get(1),
            new Object[] { "DCD", "PMP", "ODO", 'D', GregtechItemList.Casing_AmazonWarehouse, 'C', "circuitElite", 'P',
                ItemList.Electric_Piston_IV, 'M', ItemList.Machine_IV_Boxinator, 'O', ItemList.Conveyor_Module_IV });

        GTModHandler.addShapelessCraftingRecipe(
            ItemList.IndustrialPackager.get(1),
            new Object[] { GregtechItemList.Amazon_Warehouse_Controller });
    }

    private static void multiMixer() {
        // Industrial Mixing Machine
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_Mixer.get(1),
            new Object[] { "PCP", "ZMZ", "PCP", 'P', MaterialsAlloy.STABALLOY.getPlate(1), 'C', "circuitElite", 'Z',
                MaterialsAlloy.ZIRCONIUM_CARBIDE.getPlate(1), 'M', ItemList.Machine_IV_Mixer });

        // Multi-Use Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Multi_Use.get(1),
            new Object[] { "PhP", "SFS", "PwP", 'P', MaterialsAlloy.STABALLOY.getPlate(1), 'S',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'F',
                MaterialsAlloy.ZIRCONIUM_CARBIDE.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.STABALLOY.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2),
                MaterialsAlloy.ZIRCONIUM_CARBIDE.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Multi_Use.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void multiArcFurnace() {
        // Tempered Arc Furnace Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                GregtechItemList.TransmissionComponent_MV.get(2),
                ItemList.Electric_Piston_EV.get(2),
                MaterialsAlloy.INCONEL_625.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.TungstenSteel, 1))
            .itemOutputs(GregtechItemList.Casing_Industrial_Arc_Furnace.get(1))
            .fluidInputs(MaterialsAlloy.ARCANITE.getFluidStack(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // High Current Industrial Arc Furnace
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Industrial_Arc_Furnace.get(1),
                ItemList.Field_Generator_EV.get(2),
                ItemList.Robot_Arm_IV.get(4),
                GregtechItemList.Energy_Core_EV.get(2),
                MaterialsAlloy.ZERON_100.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 8))
            .itemOutputs(GregtechItemList.Industrial_Arc_Furnace.get(1))
            .fluidInputs(MaterialsAlloy.LAFIUM.getFluidStack(20 * INGOTS))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }

    private static void multiDehydrator() {
        // Vacuum Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                ItemList.Casing_Coil_Nichrome.get(1),
                ItemList.Electric_Piston_HV.get(2),
                MaterialsAlloy.ZERON_100.getPlate(4),
                MaterialsAlloy.ZERON_100.getGear(2))
            .itemOutputs(GregtechItemList.Casing_Vacuum_Furnace.get(1))
            .fluidInputs(MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Utupu-Tanuri
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vacuum_Furnace.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.YttriumBariumCuprate, 4),
                ItemList.Robot_Arm_EV.get(4),
                MaterialsAlloy.ZERON_100.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 8))
            .itemOutputs(GregtechItemList.Controller_Vacuum_Furnace.get(1))
            .fluidInputs(MaterialsAlloy.ZERON_100.getFluidStack(20 * INGOTS))
            .duration(12 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }

    private static void multiChisel() {
        // Industrial 3D Copying Machine
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GT_Chisel_HV.get(1),
                MaterialsAlloy.INCOLOY_DS.getPlate(8),
                ItemList.Electric_Motor_EV.get(8),
                ItemList.Conveyor_Module_EV.get(8),
                ItemList.Robot_Arm_EV.get(4))
            .circuit(14)
            .itemOutputs(GregtechItemList.Controller_IndustrialAutoChisel.get(1))
            .fluidInputs(MaterialsAlloy.INCOLOY_DS.getFluidStack(8 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Sturdy Printer Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_SolidSteel.get(2),
                MaterialsAlloy.INCOLOY_DS.getPlate(2),
                MaterialsAlloy.TANTALUM_CARBIDE.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Titanium, 8),
                MaterialsAlloy.EGLIN_STEEL.getRod(4))
            .circuit(14)
            .itemOutputs(GregtechItemList.Casing_IndustrialAutoChisel.get(1))
            .fluidInputs(MaterialsAlloy.EGLIN_STEEL_BASE.getFluidStack(2 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void multiRockBreaker() {
        // Boldarnator
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Machine_EV_RockBreaker.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.StainlessSteel, 4),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Aluminium, 8),
                MaterialsAlloy.EGLIN_STEEL.getScrew(8))
            .circuit(12)
            .itemOutputs(GregtechItemList.Controller_IndustrialRockBreaker.get(1))
            .fluidInputs(Materials.Aluminium.getMolten(8 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void multiAssembler() {
        // Bulk Production Frame
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                ItemList.Block_IridiumTungstensteel.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 16),
                MaterialsAlloy.INCONEL_625.getScrew(32),
                MaterialsAlloy.ENERGYCRYSTAL.getBolt(12),
                MaterialsAlloy.ZERON_100.getPlate(8))
            .itemOutputs(GregtechItemList.Casing_Autocrafter.get(1))
            .fluidInputs(MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(4 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Large Scale Auto-Assembler v1.01
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Refinery_Structural.get(4),
                GregtechItemList.LFTRControlCircuit.get(1),
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.Platinum, 16),
                GregtechItemList.TransmissionComponent_IV.get(2),
                GregtechItemList.Gregtech_Computer_Cube.get(1))
            .itemOutputs(GregtechItemList.GT4_Multi_Crafter.get(1))
            .fluidInputs(MaterialsAlloy.PIKYONIUM.getFluidStack(8 * INGOTS))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }
}
