package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingSand implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingSand() {
        OrePrefixes.sand.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.equals("sandOil")) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(2, aStack), ItemList.Cell_Empty.get(1))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.cell, Materials.Oil, 1L),
                    new ItemStack(Blocks.sand, 1, 0))
                .duration(50 * SECONDS)
                .eut(5)
                .addTo(centrifugeRecipes);
        }
    }
}
