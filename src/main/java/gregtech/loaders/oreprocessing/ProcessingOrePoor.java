package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sHammerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingOrePoor implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingOrePoor() {
        OrePrefixes.orePoor.add(this);
        OrePrefixes.oreSmall.add(this);
        OrePrefixes.oreNormal.add(this);
        OrePrefixes.oreRich.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        int aMultiplier = 1;
        switch (aPrefix) {
            case oreSmall:
                aMultiplier = 1;
                break;
            case orePoor:
                aMultiplier = 2;
                break;
            case oreNormal:
                aMultiplier = 3;
                break;
            case oreRich:
                aMultiplier = 4;
            default:
                break;
        }
        if (aMaterial != null) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1L, aStack))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, aMultiplier))
                .duration(10)
                .eut(16)
                .addTo(sHammerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1L, aStack))
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 2 * aMultiplier),
                    GT_OreDictUnificator.get(
                        OrePrefixes.dustTiny,
                        GT_Utility.selectItemInList(0, aMaterial, aMaterial.mOreByProducts),
                        1L),
                    GT_OreDictUnificator.getDust(aPrefix.mSecondaryMaterial))
                .outputChances(10000, 100 * 5 * aMultiplier, 10000)
                .noFluidInputs()
                .noFluidOutputs()
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);

            if (aMaterial.contains(SubTag.NO_SMELTING)) GT_ModHandler.addSmeltingRecipe(
                GT_Utility.copyAmount(1L, aStack),
                GT_OreDictUnificator.get(OrePrefixes.nugget, aMaterial.mDirectSmelting, aMultiplier));
        }
    }
}
