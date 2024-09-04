package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingSaplings implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingSaplings() {
        OrePrefixes.treeSapling.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 2L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(8, aStack))
            .itemOutputs(ItemList.IC2_Plantball.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Wood, 1L))
            .duration(16 * TICKS)
            .eut(8)
            .addTo(latheRecipes);
    }
}
