package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingPlank implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingPlank() {
        OrePrefixes.plank.add(this);
    }

    // Gross nasty hack to prevent spam registry of oredicted colored woods
    private boolean registeredEXU = false;

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aModName != null && aModName.equals("ExtraUtilities")) {
            if (registeredEXU) return;
            else registeredEXU = true;
        }
        if (aOreDictName.startsWith("plankWood")) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L))
                .duration(10 * TICKS)
                .eut(8)
                .addTo(latheRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(8, aStack), GTUtility.getIntegratedCircuit(8))
                .itemOutputs(new ItemStack(Blocks.chest, 1))
                .duration(40 * SECONDS)
                .eut(4)
                .addTo(assemblerRecipes);

            if (aStack.getItemDamage() == 32767) {
                for (byte i = 0; i < 64; i = (byte) (i + 1)) {
                    ItemStack tStack = GTUtility.copyMetaData(i, aStack);
                    // Get Recipe and Output, add recipe to delayed removal
                    ItemStack tOutput = GTModHandler.getRecipeOutput(tStack, tStack, tStack);
                    if ((tOutput != null) && (tOutput.stackSize >= 3)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, tStack))
                            .itemOutputs(GTUtility.copyAmount(tOutput.stackSize / 3, tOutput))
                            .fluidInputs(Materials.Water.getFluid(4))
                            .duration(2 * 25 * TICKS)
                            .eut(4)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, tStack))
                            .itemOutputs(GTUtility.copyAmount(tOutput.stackSize / 3, tOutput))
                            .fluidInputs(GTModHandler.getDistilledWater(3))
                            .duration(2 * 25 * TICKS)
                            .eut(4)
                            .addTo(cutterRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, tStack))
                            .itemOutputs(GTUtility.copyAmount(tOutput.stackSize / 3, tOutput))
                            .fluidInputs(Materials.Lubricant.getFluid(1))
                            .duration(25 * TICKS)
                            .eut(4)
                            .addTo(cutterRecipes);
                        GTModHandler.removeRecipeDelayed(tStack, tStack, tStack);
                        GTModHandler.addCraftingRecipe(
                            GTUtility.copyAmount(tOutput.stackSize / 3, tOutput),
                            GTModHandler.RecipeBits.BUFFERED,
                            new Object[] { "sP", 'P', tStack });
                    }
                    if ((tStack == null) && (i >= 16)) break;
                }
            } else {
                ItemStack tOutput = !aModName.equalsIgnoreCase("thaumcraft")
                    ? GTModHandler.getRecipeOutput(aStack, aStack, aStack)
                    : GTModHandler.getRecipeOutputNoOreDict(aStack, aStack, aStack);
                if ((tOutput != null) && (tOutput.stackSize >= 3)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(GTUtility.copyAmount(tOutput.stackSize / 3, tOutput))
                        .fluidInputs(Materials.Water.getFluid(4))
                        .duration(2 * 25)
                        .eut(4)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(GTUtility.copyAmount(tOutput.stackSize / 3, tOutput))
                        .fluidInputs(GTModHandler.getDistilledWater(3))
                        .duration(2 * 25)
                        .eut(4)
                        .addTo(cutterRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(GTUtility.copyAmount(tOutput.stackSize / 3, tOutput))
                        .fluidInputs(Materials.Lubricant.getFluid(1))
                        .duration(25)
                        .eut(4)
                        .addTo(cutterRecipes);
                    GTModHandler.removeRecipeDelayed(aStack, aStack, aStack);
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(tOutput.stackSize / 3, tOutput),
                        GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "sP", 'P', aStack });
                }
            }
        }
    }
}
