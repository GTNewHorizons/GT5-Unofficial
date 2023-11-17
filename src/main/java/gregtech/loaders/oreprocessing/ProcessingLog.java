package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCutterRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sExtractorRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sLatheRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sPyrolyseRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingLog implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingLog() {
        OrePrefixes.log.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.equals("logRubber")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(
                    ItemList.IC2_Resin.get(1L),
                    GT_ModHandler.getIC2Item("plantBall", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
                .outputChances(5000, 3750, 2500, 2500)
                .fluidOutputs(Materials.Methane.getGas(60L))
                .duration(10 * SECONDS)
                .eut(20)
                .addTo(sCentrifugeRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, aStack))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L))
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(sExtractorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, aStack))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 6L), ItemList.IC2_Resin.get(1L))
                .outputChances(10000, 3300)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);
        } else {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(1))
                .fluidOutputs(Materials.Methane.getGas(60L))
                .duration(10 * SECONDS)
                .eut(20)
                .addTo(sCentrifugeRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, aStack))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 6L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
                .outputChances(10000, 8000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);
        }

        GT_ModHandler.addCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, 2L),
            gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "sLf", 'L', GT_Utility.copyAmount(1, aStack) });

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1, aStack))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
            .duration(8 * SECONDS)
            .eut(7)
            .addTo(sLatheRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.FR_Stick.get(1L))
            .fluidInputs(Materials.SeedOil.getFluid(50L))
            .duration(16 * TICKS)
            .eut(7)
            .addTo(sAssemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(8, aStack), GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.FR_Casing_Impregnated.get(1L))
            .fluidInputs(Materials.SeedOil.getFluid(250L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(sAssemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1, aStack))
            .itemOutputs(GT_ModHandler.getModItem(Railcraft.ID, "cube", 1L, 8))
            .fluidInputs(Materials.Creosote.getFluid(750L))
            .duration(16 * TICKS)
            .eut(TierEU.ULV)
            .addTo(sChemicalBathRecipes);

        short aMeta = (short) aStack.getItemDamage();

        if (aMeta == Short.MAX_VALUE) {
            if ((GT_Utility.areStacksEqual(
                GT_ModHandler.getSmeltingOutput(GT_Utility.copyAmount(1, aStack), false, null),
                new ItemStack(Items.coal, 1, 1)))) {
                addPyrolyeOvenRecipes(aStack);
                if (GregTech_API.sRecipeFile
                    .get(ConfigCategories.Recipes.disabledrecipes, "wood2charcoalsmelting", true)) {
                    GT_ModHandler.removeFurnaceSmelting(GT_Utility.copyAmount(1, aStack));
                }
            }
            for (int i = 0; i < 32767; i++) {
                if ((GT_Utility.areStacksEqual(
                    GT_ModHandler.getSmeltingOutput(new ItemStack(aStack.getItem(), 1, i), false, null),
                    new ItemStack(Items.coal, 1, 1)))) {
                    addPyrolyeOvenRecipes(aStack);
                    if (GregTech_API.sRecipeFile
                        .get(ConfigCategories.Recipes.disabledrecipes, "wood2charcoalsmelting", true)) {
                        GT_ModHandler.removeFurnaceSmelting(new ItemStack(aStack.getItem(), 1, i));
                    }
                }
                ItemStack tStack = GT_ModHandler.getRecipeOutput(new ItemStack(aStack.getItem(), 1, i));
                if (tStack == null) {
                    if (i >= 16) {
                        break;
                    }
                } else {
                    ItemStack tPlanks = GT_Utility.copyOrNull(tStack);
                    if (tPlanks != null) {
                        tPlanks.stackSize = (tPlanks.stackSize * 3 / 2);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GT_Utility.copyOrNull(tPlanks),
                                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
                            .fluidInputs(Materials.Lubricant.getFluid(1L))
                            .duration(10 * SECONDS)
                            .eut(8)
                            .addTo(sCutterRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GT_Utility.copyAmount(
                                    GT_Mod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                    tStack),
                                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                            .fluidInputs(Materials.Water.getFluid(Math.min(1000, 200 * 8 / 320)))
                            .duration(20 * SECONDS)
                            .eut(8)
                            .addTo(sCutterRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GT_Utility.copyAmount(
                                    GT_Mod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                    tStack),
                                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                            .fluidInputs(GT_ModHandler.getDistilledWater(3))
                            .duration(20 * SECONDS)
                            .eut(8)
                            .addTo(sCutterRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GT_Utility.copyAmount(
                                    GT_Mod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                    tStack),
                                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                            .fluidInputs(Materials.Lubricant.getFluid(1))
                            .duration(10 * SECONDS)
                            .eut(8)
                            .addTo(sCutterRecipes);
                        GT_ModHandler.addSawmillRecipe(
                            new ItemStack(aStack.getItem(), 1, i),
                            tPlanks,
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L));
                        GT_ModHandler.removeRecipeDelayed(new ItemStack(aStack.getItem(), 1, i));
                        GT_ModHandler.addCraftingRecipe(
                            GT_Utility.copyAmount(
                                GT_Mod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GT_ModHandler.RecipeBits.BUFFERED,
                            new Object[] { "s", "L", 'L', new ItemStack(aStack.getItem(), 1, i) });
                        GT_ModHandler.addShapelessCraftingRecipe(
                            GT_Utility
                                .copyAmount(tStack.stackSize / (GT_Mod.gregtechproxy.mNerfedWoodPlank ? 2 : 1), tStack),
                            GT_ModHandler.RecipeBits.BUFFERED,
                            new Object[] { new ItemStack(aStack.getItem(), 1, i) });
                    }
                }
            }
        } else {
            if ((GT_Utility.areStacksEqual(
                GT_ModHandler.getSmeltingOutput(GT_Utility.copyAmount(1, aStack), false, null),
                new ItemStack(Items.coal, 1, 1)))) {
                addPyrolyeOvenRecipes(aStack);
                if (GregTech_API.sRecipeFile
                    .get(ConfigCategories.Recipes.disabledrecipes, "wood2charcoalsmelting", true)) {
                    GT_ModHandler.removeFurnaceSmelting(GT_Utility.copyAmount(1, aStack));
                }
            }
            ItemStack tStack = GT_ModHandler.getRecipeOutput(GT_Utility.copyAmount(1, aStack));
            if (tStack != null) {
                ItemStack tPlanks = GT_Utility.copyOrNull(tStack);
                if (tPlanks != null) {
                    tPlanks.stackSize = (tPlanks.stackSize * 3 / 2);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(
                            GT_Utility.copyOrNull(tPlanks),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
                        .fluidInputs(Materials.Lubricant.getFluid(1L))
                        .duration(10 * SECONDS)
                        .eut(8)
                        .addTo(sCutterRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(
                            GT_Utility.copyAmount(
                                GT_Mod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                        .fluidInputs(Materials.Water.getFluid(Math.min(1000, 200 * 8 / 320)))
                        .duration(20 * SECONDS)
                        .eut(8)
                        .addTo(sCutterRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(
                            GT_Utility.copyAmount(
                                GT_Mod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                        .fluidInputs(GT_ModHandler.getDistilledWater(3))
                        .duration(20 * SECONDS)
                        .eut(8)
                        .addTo(sCutterRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(
                            GT_Utility.copyAmount(
                                GT_Mod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                        .fluidInputs(Materials.Lubricant.getFluid(1))
                        .duration(10 * SECONDS)
                        .eut(8)
                        .addTo(sCutterRecipes);
                    GT_ModHandler.addSawmillRecipe(
                        GT_Utility.copyAmount(1, aStack),
                        tPlanks,
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L));
                    GT_ModHandler.removeRecipeDelayed(GT_Utility.copyAmount(1, aStack));
                    GT_ModHandler.addCraftingRecipe(
                        GT_Utility.copyAmount(
                            GT_Mod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                            tStack),
                        new Object[] { "s", "L", 'L', GT_Utility.copyAmount(1, aStack) });
                    GT_ModHandler.addShapelessCraftingRecipe(
                        GT_Utility
                            .copyAmount(tStack.stackSize / (GT_Mod.gregtechproxy.mNerfedWoodPlank ? 2 : 1), tStack),
                        new Object[] { GT_Utility.copyAmount(1, aStack) });
                }
            }
        }

        if ((GT_Utility.areStacksEqual(
            GT_ModHandler.getSmeltingOutput(GT_Utility.copyAmount(1, aStack), false, null),
            new ItemStack(Items.coal, 1, 1)))) {
            addPyrolyeOvenRecipes(aStack);
            if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "wood2charcoalsmelting", true))
                GT_ModHandler.removeFurnaceSmelting(GT_Utility.copyAmount(1, aStack));
        }
    }

    public static void addPyrolyeOvenRecipes(ItemStack logStack) {
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.Creosote.getFluid(4000))
            .duration(32 * SECONDS)
            .eut(64)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .fluidOutputs(Materials.Creosote.getFluid(4000))
            .duration(16 * SECONDS)
            .eut(96)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.CharcoalByproducts.getGas(4000))
            .duration(32 * SECONDS)
            .eut(64)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .fluidOutputs(Materials.CharcoalByproducts.getGas(4000))
            .duration(16 * SECONDS)
            .eut(96)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodGas.getGas(1500))
            .duration(32 * SECONDS)
            .eut(64)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(6))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .fluidOutputs(Materials.WoodGas.getGas(1500))
            .duration(16 * SECONDS)
            .eut(96)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(7))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodVinegar.getFluid(3000))
            .duration(32 * SECONDS)
            .eut(64)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(8))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .fluidOutputs(Materials.WoodVinegar.getFluid(3000))
            .duration(16 * SECONDS)
            .eut(96)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(9))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodTar.getFluid(1500))
            .duration(32 * SECONDS)
            .eut(64)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .fluidOutputs(Materials.WoodTar.getFluid(1500))
            .duration(16 * SECONDS)
            .eut(96)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(16, logStack), GT_Utility.getIntegratedCircuit(11))
            .itemOutputs(Materials.Ash.getDust(4))
            .fluidOutputs(Materials.OilHeavy.getFluid(200))
            .duration(16 * SECONDS)
            .eut(192)
            .noOptimize()
            .addTo(sPyrolyseRecipes);
    }
}
