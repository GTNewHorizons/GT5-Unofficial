package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import appeng.core.Api;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;
import gregtech.common.GT_Proxy;

public class ProcessingRound implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingRound() {
        OrePrefixes.round.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (!aMaterial.contains(SubTag.NO_WORKING)) {
            if (OreDictUnificator.get(OrePrefixes.nugget, aMaterial, 1L) != null) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(OreDictUnificator.get(OrePrefixes.nugget, aMaterial, 1L))
                    .itemOutputs(GT_Utility.copyAmount(1, aStack))
                    .duration(((int) Math.max(aMaterial.getMass() / 4L, 1L)) * TICKS)
                    .eut(8)
                    .addTo(latheRecipes);
            }

            if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)) {
                GT_ModHandler.addCraftingRecipe(
                    OreDictUnificator.get(OrePrefixes.round, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "fX", "Xh", 'X', OrePrefixes.nugget.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    OreDictUnificator.get(OrePrefixes.round, aMaterial, 4L),
                    GT_Proxy.tBits,
                    new Object[] { "fXh", 'X', OrePrefixes.ingot.get(aMaterial) });
            }
        }
        Api.INSTANCE.registries()
            .matterCannon()
            .registerAmmo(OreDictUnificator.get(OrePrefixes.round, aMaterial, 1L), aMaterial.getMass());
    }
}
