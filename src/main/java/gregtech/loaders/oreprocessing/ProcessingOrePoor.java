package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

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
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, aMultiplier))
                .duration(10)
                .eut(16)
                .addTo(hammerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 2 * aMultiplier),
                    GTOreDictUnificator.get(
                        OrePrefixes.dustTiny,
                        GTUtility.selectItemInList(0, aMaterial, aMaterial.mOreByProducts),
                        1L),
                    GTOreDictUnificator.getDust(aPrefix.mSecondaryMaterial))
                .outputChances(10000, 100 * 5 * aMultiplier, 10000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);

            if (aMaterial.contains(SubTag.NO_SMELTING)) GTModHandler.addSmeltingRecipe(
                GTUtility.copyAmount(1, aStack),
                GTOreDictUnificator.get(OrePrefixes.nugget, aMaterial.mDirectSmelting, aMultiplier));
        }
    }
}
