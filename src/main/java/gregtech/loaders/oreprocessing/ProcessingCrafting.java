package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BuildCraftSilicon;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingCrafting implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrafting() {
        OrePrefixes.crafting.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        switch (aOreDictName) {
            case "craftingQuartz" -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.redstone_torch, 3, 32767), GT_Utility.copyAmount(1L, aStack))
                    .itemOutputs(new ItemStack(net.minecraft.init.Items.comparator, 1, 0))
                    .fluidInputs(Materials.Concrete.getMolten(144L))
                    .noFluidOutputs()
                    .duration(2 * SECONDS)
                    .eut(20)
                    .addTo(sAssemblerRecipes);
            }
            case "craftingLensBlue" -> {

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 13))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 13))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.IC2_LapotronCrystal.getWildcard(1L), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Parts_Crystal_Chip_Master.get(3L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(45 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Chip_CrystalCPU.get(1L), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Chip_CrystalSoC.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(30 * SECONDS)
                    .eut(40000)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_PIC.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(60 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_PIC.get(4))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(40 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_QPIC.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(2 * MINUTES)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(sLaserEngraverRecipes);

            }
            case "craftingLensYellow" -> {

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 14))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 14))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_LPIC.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(40 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_LPIC.get(4))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(30 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_SoC.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(45 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_SoC.get(4))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(30 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_SoC.get(8))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(15 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(sLaserEngraverRecipes);
            }
            case "craftingLensOrange" -> {

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_Simple_SoC.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(15 * SECONDS)
                    .eut(64)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_Simple_SoC.get(4))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(15 * SECONDS)
                    .eut(256)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_Simple_SoC.get(16))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(15 * SECONDS)
                    .eut(1024)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_Simple_SoC.get(64))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(15 * SECONDS)
                    .eut(4096)
                    .addTo(sLaserEngraverRecipes);

            }
            case "craftingLensCyan" -> {

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 15))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 15))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_Ram.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(60 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_Ram.get(4))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(45 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_Ram.get(8))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(30 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_Ram.get(16))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(15 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_Ram.get(32))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(7 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(sLaserEngraverRecipes);

            }
            case "craftingLensRed" -> {

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Redstone, 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(2 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.RedAlloy, 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EtchedLowVoltageWiring", 1L, 0))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(10 * SECONDS)
                    .eut(16)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_ILC.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(60 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_ILC.get(4))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(45 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_ILC.get(8))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(30 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_NPIC.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(1 * MINUTES + 30 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_NPIC.get(4))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(1 * MINUTES + 15 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(sLaserEngraverRecipes);

            }
            case "craftingLensGreen" -> {

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Parts_Crystal_Chip_Elite.get(1L), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Chip_CrystalCPU.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(30 * SECONDS)
                    .eut(10000)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Chip_CrystalSoC.get(1L), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Chip_CrystalSoC2.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(60 * SECONDS)
                    .eut(80000)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_ULPIC.get(2))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(30 * SECONDS)
                    .eut(30)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_ULPIC.get(8))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(30 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_SoC2.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(1 * MINUTES + 30 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_SoC2.get(2))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(1 * MINUTES + 15 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_SoC2.get(4))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(60 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(sLaserEngraverRecipes);
            }
            case "craftingLensWhite" -> {

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 19))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 19))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.sandstone, 1, 2), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(new ItemStack(Blocks.sandstone, 1, 1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(2 * SECONDS + 10 * TICKS)
                    .eut(16)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.stone, 1, 0), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(new ItemStack(Blocks.stonebrick, 1, 3))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(2 * SECONDS + 10 * TICKS)
                    .eut(16)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.quartz_block, 1, 0), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(new ItemStack(Blocks.quartz_block, 1, 1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(2 * SECONDS + 10 * TICKS)
                    .eut(16)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockQuartz", 1L),
                        GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockQuartzChiseled", 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(2 * SECONDS + 10 * TICKS)
                    .eut(16)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_CPU.get(1))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(60 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_CPU.get(4))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(45 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_CPU.get(8))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(30 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_CPU.get(16))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(15 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(sLaserEngraverRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1), GT_Utility.copyAmount(0L, aStack))
                    .itemOutputs(ItemList.Circuit_Wafer_CPU.get(32))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .requiresCleanRoom()
                    .duration(7 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(sLaserEngraverRecipes);

            }
        }
    }
}
