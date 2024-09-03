package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
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
        if (aOreDictName.equals("logRubber")) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(2))
                .itemOutputs(
                    ItemList.IC2_Resin.get(1L),
                    GTModHandler.getIC2Item("plantBall", 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
                .outputChances(5000, 3750, 2500, 2500)
                .fluidOutputs(Materials.Methane.getGas(60L))
                .duration(10 * SECONDS)
                .eut(20)
                .addTo(centrifugeRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L))
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(extractorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 6L), ItemList.IC2_Resin.get(1L))
                .outputChances(10000, 3300)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(1))
                .fluidOutputs(Materials.Methane.getGas(60L))
                .duration(10 * SECONDS)
                .eut(20)
                .addTo(centrifugeRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 6L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
                .outputChances(10000, 8000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }

        GTModHandler.addCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, 2L),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "sLf", 'L', GTUtility.copyAmount(1, aStack) });

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Wood, 4L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
            .duration(8 * SECONDS)
            .eut(7)
            .addTo(latheRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.FR_Stick.get(1L))
            .fluidInputs(Materials.SeedOil.getFluid(50L))
            .duration(16 * TICKS)
            .eut(7)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(8, aStack), GTUtility.getIntegratedCircuit(8))
            .itemOutputs(ItemList.FR_Casing_Impregnated.get(1L))
            .fluidInputs(Materials.SeedOil.getFluid(250L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(GTModHandler.getModItem(Railcraft.ID, "cube", 1L, 8))
            .fluidInputs(Materials.Creosote.getFluid(750L))
            .duration(16 * TICKS)
            .eut(TierEU.ULV)
            .addTo(chemicalBathRecipes);

        short aMeta = (short) aStack.getItemDamage();

        if (aMeta == Short.MAX_VALUE) {
            if ((GTUtility.areStacksEqual(
                GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1, aStack), false, null),
                new ItemStack(Items.coal, 1, 1)))) {
                addPyrolyeOvenRecipes(aStack);
                GTModHandler.removeFurnaceSmelting(GTUtility.copyAmount(1, aStack));
            }
            for (int i = 0; i < 32767; i++) {
                if ((GTUtility.areStacksEqual(
                    GTModHandler.getSmeltingOutput(new ItemStack(aStack.getItem(), 1, i), false, null),
                    new ItemStack(Items.coal, 1, 1)))) {
                    addPyrolyeOvenRecipes(aStack);
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
                            .fluidInputs(Materials.Lubricant.getFluid(1L))
                            .duration(10 * SECONDS)
                            .eut(8)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    GTMod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                    tStack),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                            .fluidInputs(Materials.Water.getFluid(Math.min(1000, 200 * 8 / 320)))
                            .duration(20 * SECONDS)
                            .eut(8)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    GTMod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                    tStack),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                            .fluidInputs(GTModHandler.getDistilledWater(3))
                            .duration(20 * SECONDS)
                            .eut(8)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(new ItemStack(aStack.getItem(), 1, i))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    GTMod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                    tStack),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                            .fluidInputs(Materials.Lubricant.getFluid(1))
                            .duration(10 * SECONDS)
                            .eut(8)
                            .addTo(cutterRecipes);
                        GTModHandler.removeRecipeDelayed(new ItemStack(aStack.getItem(), 1, i));
                        GTModHandler.addCraftingRecipe(
                            GTUtility.copyAmount(
                                GTMod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GTModHandler.RecipeBits.BUFFERED,
                            new Object[] { "s", "L", 'L', new ItemStack(aStack.getItem(), 1, i) });
                        GTModHandler.addShapelessCraftingRecipe(
                            GTUtility
                                .copyAmount(tStack.stackSize / (GTMod.gregtechproxy.mNerfedWoodPlank ? 2 : 1), tStack),
                            GTModHandler.RecipeBits.BUFFERED,
                            new Object[] { new ItemStack(aStack.getItem(), 1, i) });
                    }
                }
            }
        } else {
            if ((GTUtility.areStacksEqual(
                GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1, aStack), false, null),
                new ItemStack(Items.coal, 1, 1)))) {
                addPyrolyeOvenRecipes(aStack);
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
                        .fluidInputs(Materials.Lubricant.getFluid(1L))
                        .duration(10 * SECONDS)
                        .eut(8)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                GTMod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                        .fluidInputs(Materials.Water.getFluid(Math.min(1000, 200 * 8 / 320)))
                        .duration(20 * SECONDS)
                        .eut(8)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                GTMod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                        .fluidInputs(GTModHandler.getDistilledWater(3))
                        .duration(20 * SECONDS)
                        .eut(8)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                GTMod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                                tStack),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 2L))
                        .fluidInputs(Materials.Lubricant.getFluid(1))
                        .duration(10 * SECONDS)
                        .eut(8)
                        .addTo(cutterRecipes);
                    GTModHandler.removeRecipeDelayed(GTUtility.copyAmount(1, aStack));
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(
                            GTMod.gregtechproxy.mNerfedWoodPlank ? tStack.stackSize : tStack.stackSize * 5 / 4,
                            tStack),
                        new Object[] { "s", "L", 'L', GTUtility.copyAmount(1, aStack) });
                    GTModHandler.addShapelessCraftingRecipe(
                        GTUtility.copyAmount(tStack.stackSize / (GTMod.gregtechproxy.mNerfedWoodPlank ? 2 : 1), tStack),
                        new Object[] { GTUtility.copyAmount(1, aStack) });
                }
            }
        }

        if ((GTUtility.areStacksEqual(
            GTModHandler.getSmeltingOutput(GTUtility.copyAmount(1, aStack), false, null),
            new ItemStack(Items.coal, 1, 1)))) {
            addPyrolyeOvenRecipes(aStack);
            GTModHandler.removeFurnaceSmelting(GTUtility.copyAmount(1, aStack));
        }
    }

    public static void addPyrolyeOvenRecipes(ItemStack logStack) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.Creosote.getFluid(4000))
            .duration(32 * SECONDS)
            .eut(64)
            .noOptimize()
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(2))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .fluidOutputs(Materials.Creosote.getFluid(4000))
            .duration(16 * SECONDS)
            .eut(96)
            .noOptimize()
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(3))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.CharcoalByproducts.getGas(4000))
            .duration(32 * SECONDS)
            .eut(64)
            .noOptimize()
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(4))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .fluidOutputs(Materials.CharcoalByproducts.getGas(4000))
            .duration(16 * SECONDS)
            .eut(96)
            .noOptimize()
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(5))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodGas.getGas(1500))
            .duration(32 * SECONDS)
            .eut(64)
            .noOptimize()
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(6))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .fluidOutputs(Materials.WoodGas.getGas(1500))
            .duration(16 * SECONDS)
            .eut(96)
            .noOptimize()
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(7))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodVinegar.getFluid(3000))
            .duration(32 * SECONDS)
            .eut(64)
            .noOptimize()
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(8))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .fluidOutputs(Materials.WoodVinegar.getFluid(3000))
            .duration(16 * SECONDS)
            .eut(96)
            .noOptimize()
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(9))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidOutputs(Materials.WoodTar.getFluid(1500))
            .duration(32 * SECONDS)
            .eut(64)
            .noOptimize()
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(10))
            .itemOutputs(Materials.Charcoal.getGems(20))
            .fluidInputs(Materials.Nitrogen.getGas(1000))
            .fluidOutputs(Materials.WoodTar.getFluid(1500))
            .duration(16 * SECONDS)
            .eut(96)
            .noOptimize()
            .addTo(pyrolyseRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(16, logStack), GTUtility.getIntegratedCircuit(11))
            .itemOutputs(Materials.Ash.getDust(4))
            .fluidOutputs(Materials.OilHeavy.getFluid(200))
            .duration(16 * SECONDS)
            .eut(192)
            .noOptimize()
            .addTo(pyrolyseRecipes);
    }
}
