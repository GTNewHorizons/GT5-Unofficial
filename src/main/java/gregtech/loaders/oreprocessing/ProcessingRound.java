package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import appeng.core.Api;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingRound implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingRound INSTANCE;

    public ProcessingRound() {
        INSTANCE = this;
        OrePrefixes.round.add(this);
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

        if (!MU.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            if (GTOreDictUnificator.get(OrePrefixes.nugget, material, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.nugget, material, 1L))
                    .itemOutputs(GTUtility.copyAmount(1, stack))
                    .duration(((int) Math.max(legacyMaterial.getMass() / 4L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(latheRecipes);
            }

            if (!Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE))
                && (legacyMaterial.mMaterialInto == legacyMaterial)) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.round, material, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fX", "Xh", 'X', MU.craftIngredient(OrePrefixes.nugget, material) });
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.round, material, 4L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fXh", 'X', MU.craftIngredient(OrePrefixes.ingot, material) });
            }
        }
        Api.INSTANCE.registries()
            .matterCannon()
            .registerAmmo(GTOreDictUnificator.get(OrePrefixes.round, material, 1L), legacyMaterial.getMass());
    }
}
