package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingFood implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingFood() {
        OrePrefixes.food.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
            ItemStack aStack) {
        switch (aOreDictName) {
            case "foodCheese" -> {
                GT_Values.RA.addSlicerRecipe(aStack,
                        ItemList.Shape_Slicer_Flat.get(0L),
                        ItemList.Food_Sliced_Cheese.get(4L),
                        64,
                        4);
                GT_OreDictUnificator.addItemData(aStack, new gregtech.api.objects.ItemData(Materials.Cheese, 3628800L));
            }
            case "foodDough" -> {
                GT_ModHandler.removeFurnaceSmelting(aStack);
                GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(1L, aStack),
                        ItemList.Food_Flat_Dough.get(1L),
                        16,
                        4);
                GT_Values.RA.addMixerRecipe(aStack,
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                        null,
                        null,
                        null,
                        null,
                        ItemList.Food_Dough_Sugar.get(2L),
                        32,
                        8);
                GT_Values.RA.addMixerRecipe(aStack,
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cocoa, 1L),
                        null,
                        null,
                        null,
                        null,
                        ItemList.Food_Dough_Chocolate.get(2L),
                        32,
                        8);
                GT_Values.RA.addMixerRecipe(aStack,
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chocolate, 1L),
                        null,
                        null,
                        null,
                        null,
                        ItemList.Food_Dough_Chocolate.get(2L),
                        32,
                        8);
                GT_Values.RA.addFormingPressRecipe(GT_Utility.copyAmount(1L, aStack),
                        ItemList.Shape_Mold_Bun.get(0L),
                        ItemList.Food_Raw_Bun.get(1L),
                        128,
                        4);
                GT_Values.RA.addFormingPressRecipe(GT_Utility.copyAmount(2L, aStack),
                        ItemList.Shape_Mold_Bread.get(0L),
                        ItemList.Food_Raw_Bread.get(1L),
                        256,
                        4);
                GT_Values.RA.addFormingPressRecipe(GT_Utility.copyAmount(3L, aStack),
                        ItemList.Shape_Mold_Baguette.get(0L),
                        ItemList.Food_Raw_Baguette.get(1L),
                        384,
                        4);
            }
        }
    }
}
