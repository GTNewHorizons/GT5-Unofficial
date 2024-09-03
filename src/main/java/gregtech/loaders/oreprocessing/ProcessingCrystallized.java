package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingCrystallized implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrystallized() {
        OrePrefixes.crystal.add(this);
        OrePrefixes.crystalline.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aMaterial.mMacerateInto == null) {
            return;
        }

        if (GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1) == null) {
            return;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L))
            .duration(10 * TICKS)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

    }
}
