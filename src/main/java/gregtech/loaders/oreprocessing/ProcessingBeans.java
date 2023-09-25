package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingBeans implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingBeans() {
        OrePrefixes.beans.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.equals("beansCocoa")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1L, aStack))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cocoa, 1L))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }
    }
}
