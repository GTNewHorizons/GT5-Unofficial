package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import appeng.core.Api;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingRound implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingRound() {
        OrePrefixes.round.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (!aMaterial.contains(SubTag.NO_WORKING)) {
            if (GTOreDictUnificator.get(OrePrefixes.nugget, aMaterial, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.nugget, aMaterial, 1L))
                    .itemOutputs(GTUtility.copyAmount(1, aStack))
                    .duration(((int) Math.max(aMaterial.getMass() / 4L, 1L)) * TICKS)
                    .eut(8)
                    .addTo(latheRecipes);
            }

            if ((aMaterial.mUnifiable) && (aMaterial.mMaterialInto == aMaterial)) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.round, aMaterial, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fX", "Xh", 'X', OrePrefixes.nugget.get(aMaterial) });
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.round, aMaterial, 4L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "fXh", 'X', OrePrefixes.ingot.get(aMaterial) });
            }
        }
        Api.INSTANCE.registries()
            .matterCannon()
            .registerAmmo(GTOreDictUnificator.get(OrePrefixes.round, aMaterial, 1L), aMaterial.getMass());
    }
}
