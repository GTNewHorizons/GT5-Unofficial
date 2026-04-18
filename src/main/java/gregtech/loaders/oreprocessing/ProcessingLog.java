package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.*;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingLog implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingLog() {
        OrePrefixes.log.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GTModHandler.addCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, 2L),
            new Object[] { "sLf", 'L', OrePrefixes.log.get(Materials.Wood) });

        short aMeta = (short) aStack.getItemDamage();

        if (aMeta == Short.MAX_VALUE) {
            if ((GTUtility.areStacksEqual(
                GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1, aStack), false, null),
                new ItemStack(Items.coal, 1, 1)))) {
                GTModHandler.removeFurnaceSmelting(GTUtility.copyAmount(1, aStack));
            }
            for (int i = 0; i < 32767; i++) {
                if ((GTUtility.areStacksEqual(
                    GTModHandler.getSmeltingOutput(new ItemStack(aStack.getItem(), 1, i), false, null),
                    new ItemStack(Items.coal, 1, 1)))) {
                    GTModHandler.removeFurnaceSmelting(new ItemStack(aStack.getItem(), 1, i));
                }
                ItemStack tStack = GTModHandler.getRecipeOutput(new ItemStack(aStack.getItem(), 1, i));
                if (tStack == null) {
                    if (i >= 16) {
                        break;
                    }
                } else {
                    ItemStack tPlanks = GTUtility.copyOrNull(tStack);
                    if (tPlanks != null) {
                        tPlanks.stackSize = (tPlanks.stackSize * 3 / 2);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyOrNull(tPlanks),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
                            .fluidInputs(Materials.DimensionallyShiftedSuperfluid.getFluid(1L))
                            .duration(4 * SECONDS)
                            .eut(8)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyOrNull(tPlanks),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
                            .fluidInputs(Materials.Lubricant.getFluid(1L))
                            .duration(10 * SECONDS)
                            .eut(8)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                    tStack),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                            .fluidInputs(Materials.Water.getFluid(Math.min(1_000, 200 * 8 / 320)))
                            .duration(20 * SECONDS)
                            .eut(8)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                    tStack),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                            .fluidInputs(GTModHandler.getDistilledWater(3))
                            .duration(20 * SECONDS)
                            .eut(8)
                            .addTo(cutterRecipes);
                        GTModHandler.removeRecipeDelayed(new ItemStack(aStack.getItem(), 1, i));
                        GTModHandler.addCraftingRecipe(
                            GTUtility.copyAmount(
                                GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GTModHandler.RecipeBits.BUFFERED,
                            new Object[] { "s", "L", 'L', new ItemStack(aStack.getItem(), 1, i) });
                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility.copyAmount(tStack.stackSize / (GTMod.proxy.mNerfedWoodPlank ? 2 : 1), tStack),
                            GTModHandler.RecipeBits.BUFFERED,
                            new Object[] { new ItemStack(aStack.getItem(), 1, i) });
                    }
                }
            }
        } else {
            if ((GTUtility.areStacksEqual(
                GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1, aStack), false, null),
                new ItemStack(Items.coal, 1, 1)))) {
                GTModHandler.removeFurnaceSmelting(GTUtility.copyAmount(1, aStack));
            }
            ItemStack tStack = GTModHandler.getRecipeOutput(GTUtility.copyAmount(1, aStack));
            if (tStack != null) {
                ItemStack tPlanks = GTUtility.copyOrNull(tStack);
                if (tPlanks != null) {
                    tPlanks.stackSize = (tPlanks.stackSize * 3 / 2);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(
                            GTUtility.copyOrNull(tPlanks),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
                        .fluidInputs(Materials.DimensionallyShiftedSuperfluid.getFluid(1L))
                        .duration(4 * SECONDS)
                        .eut(8)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(
                            GTUtility.copyOrNull(tPlanks),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
                        .fluidInputs(Materials.Lubricant.getFluid(1L))
                        .duration(10 * SECONDS)
                        .eut(8)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                        .fluidInputs(Materials.Water.getFluid(Math.min(1_000, 200 * 8 / 320)))
                        .duration(20 * SECONDS)
                        .eut(8)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                        .fluidInputs(GTModHandler.getDistilledWater(3))
                        .duration(20 * SECONDS)
                        .eut(8)
                        .addTo(cutterRecipes);
                    GTModHandler.removeRecipeDelayed(GTUtility.copyAmount(1, aStack));
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(
                            GTMod.proxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                            tStack),
                        new Object[] { "s", "L", 'L', GTUtility.copyAmount(1, aStack) });
                    GTModHandler.addShapelessCraftingRecipe(
                        GTUtility.copyAmount(tStack.stackSize / (GTMod.proxy.mNerfedWoodPlank ? 2 : 1), tStack),
                        new Object[] { GTUtility.copyAmount(1, aStack) });
                }
            }
        }

        if ((GTUtility.areStacksEqual(
            GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1, aStack), false, null),
            new ItemStack(Items.coal, 1, 1)))) {
            GTModHandler.removeFurnaceSmelting(GTUtility.copyAmount(1, aStack));
        }
    }

    /**
     * Add Pyrolyse Oven Recipes for Logs. Used in NHCoreMod.
     */
    @SuppressWarnings("unused")
    public static void addPyrolyeOvenRecipes(ItemStack logStack) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(1)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.Creosote.getFluid(4_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(2)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1_000))
            .fluidOutputs(Materials.Creosote.getFluid(4_000))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(3)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.CharcoalByproducts.getGas(4_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(4)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1_000))
            .fluidOutputs(Materials.CharcoalByproducts.getGas(4_000))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(5)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodGas.getGas(1_500))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(6)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1_000))
            .fluidOutputs(Materials.WoodGas.getGas(1_500))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(7)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodVinegar.getFluid(3_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(8)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1_000))
            .fluidOutputs(Materials.WoodVinegar.getFluid(3_000))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(9)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodTar.getFluid(1_500))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(10)
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1_000))
            .fluidOutputs(Materials.WoodTar.getFluid(1_500))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack))
            .circuit(11)
            .itemOutputs(Materials.Ash.getDust(4))
            .fluidOutputs(Materials.OilHeavy.getFluid(200))
            .duration(16 * SECONDS)
            .eut(192)
            .addTo(pyrolyseRecipes);
    }
}
