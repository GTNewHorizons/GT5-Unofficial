package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sHammerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

public class ProcessingCrystallized implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrystallized() {
        OrePrefixes.crystal.add(this);
        OrePrefixes.crystalline.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1L, aStack))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10*TICKS)
            .eut(16)
            .addTo(sHammerRecipes);

        GT_ModHandler.addPulverisationRecipe(
            GT_Utility.copyAmount(1L, aStack),
            GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
            null,
            10,
            false);
    }
}
