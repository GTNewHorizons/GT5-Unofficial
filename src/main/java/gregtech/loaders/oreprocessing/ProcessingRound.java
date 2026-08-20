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
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingRound implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingRound INSTANCE;

    public ProcessingRound() {
        INSTANCE = this;
        OrePrefixes.round.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            if (GTOreDictUnificator.get(OrePrefixes.nugget, material, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.nugget, material, 1L))
                    .itemOutputs(GTUtility.copyAmount(1, stack))
                    .duration(((int) Math.max(MaterialUtils.mass(material) / 4L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(latheRecipes);
            }

            if (MaterialUtils.unifiable(material)) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.round, material, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fX", "Xh", 'X', MaterialParts.craftIngredient(OrePrefixes.nugget, material) });
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.round, material, 4L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fXh", 'X', MaterialParts.craftIngredient(OrePrefixes.ingot, material) });
            }
        }
        Api.INSTANCE.registries()
            .matterCannon()
            .registerAmmo(GTOreDictUnificator.get(OrePrefixes.round, material, 1L), MaterialUtils.mass(material));
    }
}
