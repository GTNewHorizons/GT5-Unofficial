package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import appeng.core.Api;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
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
        if (!MU.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            if (GTOreDictUnificator.get(OrePrefixes.nugget, material, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.nugget, material, 1L))
                    .itemOutputs(GTUtility.copyAmount(1, stack))
                    .duration(((int) Math.max(material.getMass() / 4L, 1L)) * TICKS)
                    .eut(TierEU.RECIPE_ULV)
                    .addTo(latheRecipes);
            }

            if ((material.mUnifiable) && (material.mMaterialInto == material)) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.round, material, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fX", "Xh", 'X', OrePrefixes.nugget.ingredient(material) });
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.round, material, 4L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fXh", 'X', OrePrefixes.ingot.ingredient(material) });
            }
        }
        Api.INSTANCE.registries()
            .matterCannon()
            .registerAmmo(GTOreDictUnificator.get(OrePrefixes.round, material, 1L), material.getMass());
    }
}
