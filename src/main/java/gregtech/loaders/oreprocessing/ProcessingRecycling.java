package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

public class ProcessingRecycling implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingRecycling INSTANCE;

    public ProcessingRecycling() {
        INSTANCE = this;
        for (OrePrefixes tPrefix : OrePrefixes.VALUES)
            if ((tPrefix.isMaterialBased()) && (tPrefix.getMaterialAmount() > 0L) && (tPrefix.isContainer()))
                tPrefix.add(this);
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

        if ((material != MU.material(Materials.Empty)) && (GTUtility.getFluidForFilledItem(stack, true) == null)
            && !MU.hasFlag(material, GTMaterialFlag.SMELTING_TO_FLUID)
            && (GTOreDictUnificator.get(OrePrefixes.dust, material, 1L) != null)) {
            GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
            recipeBuilder.itemInputs(stack);
            if (GTUtility.getContainerItem(stack, true) == null) {
                recipeBuilder.itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, material, prefix.getMaterialAmount() / 3628800L));
            } else {
                recipeBuilder.itemOutputs(
                    GTUtility.getContainerItem(stack, true),
                    GTOreDictUnificator.get(OrePrefixes.dust, material, prefix.getMaterialAmount() / 3628800L));
            }
            recipeBuilder.duration(((int) Math.max(legacyMaterial.getMass() / 2L, 1L)) * TICKS)
                .eut(2)
                .addTo(cannerRecipes);
        }
    }
}
