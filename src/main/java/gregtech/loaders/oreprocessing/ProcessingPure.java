package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;

public class ProcessingPure implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingPure() {
        OrePrefixes.crushedPurified.add(this);
        OrePrefixes.cleanGravel.add(this);
        OrePrefixes.reduced.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1, aStack))
            .itemOutputs(OreDictUnificator.get(OrePrefixes.dustPure, aMaterial.mMacerateInto, 1L))
            .duration(10)
            .eut(16)
            .addTo(hammerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1, aStack))
            .itemOutputs(
                OreDictUnificator.get(
                    OrePrefixes.dustPure,
                    aMaterial.mMacerateInto,
                    OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                    1L),
                OreDictUnificator.get(
                    OrePrefixes.dust,
                    GT_Utility.selectItemInList(1, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                    1L))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);
    }
}
