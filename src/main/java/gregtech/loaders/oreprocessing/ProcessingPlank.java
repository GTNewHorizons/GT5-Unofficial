package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingPlank implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingPlank() {
        OrePrefixes.plank.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.startsWith("plankWood")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, aStack))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L))
                .duration(10 * TICKS)
                .eut(8)
                .addTo(latheRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(1, aStack),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iron, 1L))
                .itemOutputs(ItemList.Crate_Empty.get(1L))
                .duration(10 * SECONDS)
                .eut(1)
                .addTo(assemblerRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(1, aStack),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.WroughtIron, 1L))
                .itemOutputs(ItemList.Crate_Empty.get(1L))
                .duration(10 * SECONDS)
                .eut(1)
                .addTo(assemblerRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(1, aStack),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 1L))
                .itemOutputs(ItemList.Crate_Empty.get(1L))
                .duration(10 * SECONDS)
                .eut(1)
                .addTo(assemblerRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(8, aStack), GT_Utility.getIntegratedCircuit(8))
                .itemOutputs(new ItemStack(Blocks.chest, 1))
                .duration(40 * SECONDS)
                .eut(4)
                .addTo(assemblerRecipes);

            if (aStack.getItemDamage() == 32767) {
                for (byte i = 0; i < 64; i = (byte) (i + 1)) {
                    ItemStack tStack = GT_Utility.copyMetaData(i, aStack);
                    // Get Recipe and Output, add recipe to delayed removal
                    ItemStack tOutput = GT_ModHandler.getRecipeOutput(tStack, tStack, tStack);
                    if ((tOutput != null) && (tOutput.stackSize >= 3)) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, tStack))
                            .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                            .fluidInputs(Materials.Water.getFluid(4))
                            .duration(2 * 25 * TICKS)
                            .eut(4)
                            .addTo(cutterRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, tStack))
                            .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                            .fluidInputs(GT_ModHandler.getDistilledWater(3))
                            .duration(2 * 25 * TICKS)
                            .eut(4)
                            .addTo(cutterRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, tStack))
                            .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                            .fluidInputs(Materials.Lubricant.getFluid(1))
                            .duration(25 * TICKS)
                            .eut(4)
                            .addTo(cutterRecipes);
                        GT_ModHandler.removeRecipeDelayed(tStack, tStack, tStack);
                        GT_ModHandler.addCraftingRecipe(
                            GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput),
                            GT_ModHandler.RecipeBits.BUFFERED,
                            new Object[] { "sP", 'P', tStack });
                    }
                    if ((tStack == null) && (i >= 16)) break;
                }
            } else {
                ItemStack tOutput = !aModName.equalsIgnoreCase("thaumcraft")
                    ? GT_ModHandler.getRecipeOutput(aStack, aStack, aStack)
                    : GT_ModHandler.getRecipeOutputNoOreDict(aStack, aStack, aStack);
                if ((tOutput != null) && (tOutput.stackSize >= 3)) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                        .fluidInputs(Materials.Water.getFluid(4))
                        .duration(2 * 25)
                        .eut(4)
                        .addTo(cutterRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                        .fluidInputs(GT_ModHandler.getDistilledWater(3))
                        .duration(2 * 25)
                        .eut(4)
                        .addTo(cutterRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                        .fluidInputs(Materials.Lubricant.getFluid(1))
                        .duration(25)
                        .eut(4)
                        .addTo(cutterRecipes);
                    GT_ModHandler.removeRecipeDelayed(aStack, aStack, aStack);
                    GT_ModHandler.addCraftingRecipe(
                        GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput),
                        GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "sP", 'P', aStack });
                }
            }
        }
    }
}
