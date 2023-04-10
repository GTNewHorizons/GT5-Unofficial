package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCompressorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

public class ProcessingSaplings implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingSaplings() {
        OrePrefixes.treeSapling.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GT_Values.RA.addPulveriserRecipe(
            GT_Utility.copyAmount(1L, aStack),
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 2L) },
            new int[] { 10000 },
            100,
            2);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(8L, aStack)
            )
            .itemOutputs(
                ItemList.IC2_Plantball.get(1L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15*SECONDS)
            .eut(2)
            .addTo(sCompressorRecipes);

        GT_Values.RA.addLatheRecipe(
            GT_Utility.copyAmount(1L, aStack),
            GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
            GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Wood, 1L),
            16,
            8);
    }
}
