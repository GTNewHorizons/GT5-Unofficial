package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import appeng.core.Api;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingRound implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingRound INSTANCE;

    public ProcessingRound() {
        INSTANCE = this;
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            if (MaterialParts.stack(OrePrefixes.nugget, material, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialParts.stack(OrePrefixes.nugget, material, 1L))
                    .itemOutputs(GTUtility.copyAmount(1, stack))
                    .duration(((int) Math.max(MaterialUtils.mass(material) / 4L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(latheRecipes);
            }

            if (MaterialUtils.unifiable(material)) {
                GTModHandler.addCraftingRecipe(
                    MaterialParts.stack(OrePrefixes.round, material, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fX", "Xh", 'X', MaterialParts.craftIngredient(OrePrefixes.nugget, material) });
                GTModHandler.addCraftingRecipe(
                    MaterialParts.stack(OrePrefixes.round, material, 4L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fXh", 'X', MaterialParts.craftIngredient(OrePrefixes.ingot, material) });
            }
        }
        Api.INSTANCE.registries()
            .matterCannon()
            .registerAmmo(MaterialParts.stack(OrePrefixes.round, material, 1L), MaterialUtils.mass(material));
    }
}
