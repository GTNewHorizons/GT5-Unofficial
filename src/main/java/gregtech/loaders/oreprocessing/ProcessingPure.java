package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingPure implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingPure INSTANCE;

    public ProcessingPure() {
        INSTANCE = this;
        OrePrefixes.crushedPurified.add(this);
        OrePrefixes.cleanGravel.add(this);
        OrePrefixes.reduced.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        Materials legacyMaterial = MU.materialOf(material);
        if (legacyMaterial == null) return;

        if (MU.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustPure, MU.macerateInto(material), 1L))
            .duration(10)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                GTOreDictUnificator.get(
                    OrePrefixes.dustPure,
                    MU.macerateInto(material),
                    GTOreDictUnificator.get(OrePrefixes.dust, MU.macerateInto(material), 1L),
                    1L),
                GTOreDictUnificator.get(
                    OrePrefixes.dust,
                    GTUtility.selectItemInList(1, MU.macerateInto(legacyMaterial), legacyMaterial.mOreByProducts),
                    1L))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);
    }
}
