package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BuildCraftSilicon;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.WaferEngravingRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;

public class LaserEngraverRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DTCC.getFluid(1_000))
            .fluidOutputs(Materials.ExcitedDTCC.getFluid(1_000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DTPC.getFluid(1_000))
            .fluidOutputs(Materials.ExcitedDTPC.getFluid(1_000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DTRC.getFluid(1_000))
            .fluidOutputs(Materials.ExcitedDTRC.getFluid(1_000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DTEC.getFluid(1_000))
            .fluidOutputs(Materials.ExcitedDTEC.getFluid(1_000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DTSC.getFluid(1_000))
            .fluidOutputs(Materials.ExcitedDTSC.getFluid(1_000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UIV)
            .addTo(laserEngraverRecipes);

        // From ProcessingCrafting - craftingQuartz
        GTValues.RA.stdBuilder()
            .itemInputs(
                new net.minecraft.item.ItemStack(Blocks.redstone_torch, 3, GTRecipeBuilder.WILDCARD),
                new OreDictItemStack("craftingQuartz", 1))
            .itemOutputs(new net.minecraft.item.ItemStack(Items.comparator, 1, 0))
            .fluidInputs(Materials.Concrete.getMolten(1 * INGOTS))
            .duration(2 * SECONDS)
            .eut(20)
            .addTo(assemblerRecipes);

        // From ProcessingCrafting - craftingLensBlue
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                new OreDictItemStack("craftingLensBlue", 0))
            .itemOutputs(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 13))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.CastIron, 1L),
                new OreDictItemStack("craftingLensBlue", 0))
            .itemOutputs(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 13))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_LapotronCrystal.getWildcard(1L), new OreDictItemStack("craftingLensBlue", 0))
            .itemOutputs(ItemList.Circuit_Parts_Crystal_Chip_Master.get(3L))
            .requiresCleanRoom()
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Chip_CrystalCPU.get(1L), new OreDictItemStack("craftingLensBlue", 0))
            .itemOutputs(ItemList.Circuit_Chip_CrystalSoC.get(1))
            .requiresCleanRoom()
            .duration(30 * SECONDS)
            .eut(40000)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), new OreDictItemStack("craftingLensBlue", 0))
            .itemOutputs(ItemList.Circuit_Wafer_PIC.get(1))
            .requiresCleanRoom()
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), new OreDictItemStack("craftingLensBlue", 0))
            .itemOutputs(ItemList.Circuit_Wafer_PIC.get(4))
            .requiresCleanRoom()
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), new OreDictItemStack("craftingLensBlue", 0))
            .itemOutputs(ItemList.Circuit_Wafer_QPIC.get(1))
            .requiresCleanRoom()
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_UV)
            .addTo(WaferEngravingRecipes);

        // From ProcessingCrafting - craftingLensYellow
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                new OreDictItemStack("craftingLensYellow", 0))
            .itemOutputs(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 14))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.CastIron, 1L),
                new OreDictItemStack("craftingLensYellow", 0))
            .itemOutputs(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 14))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), new OreDictItemStack("craftingLensYellow", 0))
            .itemOutputs(ItemList.Circuit_Wafer_LPIC.get(1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), new OreDictItemStack("craftingLensYellow", 0))
            .itemOutputs(ItemList.Circuit_Wafer_LPIC.get(4))
            .requiresCleanRoom()
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), new OreDictItemStack("craftingLensYellow", 0))
            .itemOutputs(ItemList.Circuit_Wafer_SoC.get(1))
            .requiresCleanRoom()
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), new OreDictItemStack("craftingLensYellow", 0))
            .itemOutputs(ItemList.Circuit_Wafer_SoC.get(4))
            .requiresCleanRoom()
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), new OreDictItemStack("craftingLensYellow", 0))
            .itemOutputs(ItemList.Circuit_Wafer_SoC.get(8))
            .requiresCleanRoom()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(WaferEngravingRecipes);

        // From ProcessingCrafting - craftingLensOrange
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), new OreDictItemStack("craftingLensOrange", 0))
            .itemOutputs(ItemList.Circuit_Wafer_Simple_SoC.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), new OreDictItemStack("craftingLensOrange", 0))
            .itemOutputs(ItemList.Circuit_Wafer_Simple_SoC.get(4))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), new OreDictItemStack("craftingLensOrange", 0))
            .itemOutputs(ItemList.Circuit_Wafer_Simple_SoC.get(16))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), new OreDictItemStack("craftingLensOrange", 0))
            .itemOutputs(ItemList.Circuit_Wafer_Simple_SoC.get(64))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(WaferEngravingRecipes);

        // From ProcessingCrafting - craftingLensCyan
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                new OreDictItemStack("craftingLensCyan", 0))
            .itemOutputs(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 15))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.CastIron, 1L),
                new OreDictItemStack("craftingLensCyan", 0))
            .itemOutputs(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 15))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), new OreDictItemStack("craftingLensCyan", 0))
            .itemOutputs(ItemList.Circuit_Wafer_Ram.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), new OreDictItemStack("craftingLensCyan", 0))
            .itemOutputs(ItemList.Circuit_Wafer_Ram.get(4))
            .requiresCleanRoom()
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), new OreDictItemStack("craftingLensCyan", 0))
            .itemOutputs(ItemList.Circuit_Wafer_Ram.get(8))
            .requiresCleanRoom()
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), new OreDictItemStack("craftingLensCyan", 0))
            .itemOutputs(ItemList.Circuit_Wafer_Ram.get(16))
            .requiresCleanRoom()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), new OreDictItemStack("craftingLensCyan", 0))
            .itemOutputs(ItemList.Circuit_Wafer_Ram.get(32))
            .requiresCleanRoom()
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(WaferEngravingRecipes);

        // From ProcessingCrafting - craftingLensRed
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Redstone, 1L),
                new OreDictItemStack("craftingLensRed", 0))
            .itemOutputs(GTModHandler.getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.RedAlloy, 1L),
                new OreDictItemStack("craftingLensRed", 0))
            .itemOutputs(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EtchedLowVoltageWiring", 1L, 0))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), new OreDictItemStack("craftingLensRed", 0))
            .itemOutputs(ItemList.Circuit_Wafer_ILC.get(1))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), new OreDictItemStack("craftingLensRed", 0))
            .itemOutputs(ItemList.Circuit_Wafer_ILC.get(4))
            .requiresCleanRoom()
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), new OreDictItemStack("craftingLensRed", 0))
            .itemOutputs(ItemList.Circuit_Wafer_ILC.get(8))
            .requiresCleanRoom()
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), new OreDictItemStack("craftingLensRed", 0))
            .itemOutputs(ItemList.Circuit_Wafer_NPIC.get(1))
            .requiresCleanRoom()
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), new OreDictItemStack("craftingLensRed", 0))
            .itemOutputs(ItemList.Circuit_Wafer_NPIC.get(4))
            .requiresCleanRoom()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(WaferEngravingRecipes);

        // From ProcessingCrafting - craftingLensGreen
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Parts_Crystal_Chip_Elite.get(1L), new OreDictItemStack("craftingLensGreen", 0))
            .itemOutputs(ItemList.Circuit_Chip_CrystalCPU.get(1))
            .requiresCleanRoom()
            .duration(30 * SECONDS)
            .eut(10000)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Chip_CrystalSoC.get(1L), new OreDictItemStack("craftingLensGreen", 0))
            .itemOutputs(ItemList.Circuit_Chip_CrystalSoC2.get(1))
            .requiresCleanRoom()
            .duration(60 * SECONDS)
            .eut(80000)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), new OreDictItemStack("craftingLensGreen", 0))
            .itemOutputs(ItemList.Circuit_Wafer_ULPIC.get(2))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), new OreDictItemStack("craftingLensGreen", 0))
            .itemOutputs(ItemList.Circuit_Wafer_ULPIC.get(8))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), new OreDictItemStack("craftingLensGreen", 0))
            .itemOutputs(ItemList.Circuit_Wafer_SoC2.get(1))
            .requiresCleanRoom()
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), new OreDictItemStack("craftingLensGreen", 0))
            .itemOutputs(ItemList.Circuit_Wafer_SoC2.get(2))
            .requiresCleanRoom()
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), new OreDictItemStack("craftingLensGreen", 0))
            .itemOutputs(ItemList.Circuit_Wafer_SoC2.get(4))
            .requiresCleanRoom()
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(WaferEngravingRecipes);

        // From ProcessingCrafting - craftingLensWhite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 19))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.CastIron, 1L),
                new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 19))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new net.minecraft.item.ItemStack(Blocks.sandstone, 1, 2),
                new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(new net.minecraft.item.ItemStack(Blocks.sandstone, 1, 1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new net.minecraft.item.ItemStack(Blocks.stone, 1, 0),
                new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(new net.minecraft.item.ItemStack(Blocks.stonebrick, 1, 3))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new net.minecraft.item.ItemStack(Blocks.quartz_block, 1, 0),
                new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(new net.minecraft.item.ItemStack(Blocks.quartz_block, 1, 1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockQuartz", 1L),
                new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockQuartzChiseled", 1L))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(ItemList.Circuit_Wafer_CPU.get(1))
            .requiresCleanRoom()
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(ItemList.Circuit_Wafer_CPU.get(4))
            .requiresCleanRoom()
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(ItemList.Circuit_Wafer_CPU.get(8))
            .requiresCleanRoom()
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(ItemList.Circuit_Wafer_CPU.get(16))
            .requiresCleanRoom()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(WaferEngravingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), new OreDictItemStack("craftingLensWhite", 0))
            .itemOutputs(ItemList.Circuit_Wafer_CPU.get(32))
            .requiresCleanRoom()
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(WaferEngravingRecipes);

    }
}
