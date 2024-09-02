package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.slicerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingFood implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingFood() {
        OrePrefixes.food.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        switch (aOreDictName) {
            case "foodCheese" -> {
                registerSlicerRecipes(aStack);
                GTOreDictUnificator.addItemData(aStack, new gregtech.api.objects.ItemData(Materials.Cheese, 3628800L));
            }
            case "foodDough" -> {
                GTModHandler.removeFurnaceSmelting(aStack);
                registerBenderRecipes(aStack);
                registerMixerRecipes(aStack);
                registerFormingPressRecipes(aStack);
            }
        }
    }

    private void registerSlicerRecipes(ItemStack stack) {
        GTValues.RA.stdBuilder()
            .itemInputs(stack, ItemList.Shape_Slicer_Flat.get(0L))
            .itemOutputs(ItemList.Food_Sliced_Cheese.get(4L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(4)
            .addTo(slicerRecipes);
    }

    private void registerBenderRecipes(ItemStack stack) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Food_Flat_Dough.get(1L))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(benderRecipes);
    }

    private void registerMixerRecipes(ItemStack stack) {
        GTValues.RA.stdBuilder()
            .itemInputs(stack, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
            .itemOutputs(ItemList.Food_Dough_Sugar.get(2L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(stack, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cocoa, 1L))
            .itemOutputs(ItemList.Food_Dough_Chocolate.get(2L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(stack, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chocolate, 1L))
            .itemOutputs(ItemList.Food_Dough_Chocolate.get(2L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(mixerRecipes);
    }

    private void registerFormingPressRecipes(ItemStack stack) {

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Mold_Bun.get(0L))
            .itemOutputs(ItemList.Food_Raw_Bun.get(1L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(2, stack), ItemList.Shape_Mold_Bread.get(0L))
            .itemOutputs(ItemList.Food_Raw_Bread.get(1L))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(4)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(3, stack), ItemList.Shape_Mold_Baguette.get(0L))
            .itemOutputs(ItemList.Food_Raw_Baguette.get(1L))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(4)
            .addTo(formingPressRecipes);
    }
}
