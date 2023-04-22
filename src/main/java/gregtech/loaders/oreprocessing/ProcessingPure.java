package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sHammerRecipes;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

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
            .itemInputs(GT_Utility.copyAmount(1L, aStack))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dustPure, aMaterial.mMacerateInto, 1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10)
            .eut(16)
            .addTo(sHammerRecipes);

        GT_ModHandler.addPulverisationRecipe(
            GT_Utility.copyAmount(1L, aStack),
            GT_OreDictUnificator.get(
                OrePrefixes.dustPure,
                aMaterial.mMacerateInto,
                GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                1L),
            GT_OreDictUnificator.get(
                OrePrefixes.dust,
                GT_Utility.selectItemInList(1, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                1L),
            10,
            false);
    }
}
