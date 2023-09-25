package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMap.sCentrifugeRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingSand implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingSand() {
        OrePrefixes.sand.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.equals("sandOil")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Cell_Empty.get(1))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oil, 1L),
                    new ItemStack(Blocks.sand, 1, 0))
                .duration(50 * SECONDS)
                .eut(5)
                .addTo(sCentrifugeRecipes);
        }
    }
}
