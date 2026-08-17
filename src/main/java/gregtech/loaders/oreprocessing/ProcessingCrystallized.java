package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingCrystallized implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrystallized() {
        OrePrefixes.crystal.add(this);
        OrePrefixes.crystalline.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (material == null) return;

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }

        if (MaterialUtils.macerateInto(material) == null) {
            return;
        }

        if (GTOreDictUnificator.get(OrePrefixes.dust, MaterialUtils.macerateInto(material), 1) == null) {
            return;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, MaterialUtils.macerateInto(material), 1L))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, MaterialUtils.macerateInto(material), 1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

    }

    /// Macerates into `material` itself: a recognition marker carries no macerate target.
    @Override
    public void registerRecognitionOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }

        if (GTOreDictUnificator.get(OrePrefixes.dust, material, 1) == null) {
            return;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 1L))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, material, 1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);
    }
}
