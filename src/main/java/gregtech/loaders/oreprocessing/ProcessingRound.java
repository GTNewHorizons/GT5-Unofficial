package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMap.sLatheRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import appeng.core.Api;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

public class ProcessingRound implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingRound() {
        OrePrefixes.round.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (!aMaterial.contains(SubTag.NO_WORKING)) {
            if (GT_OreDictUnificator.get(OrePrefixes.nugget, aMaterial, 1L) != null) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(OrePrefixes.nugget, aMaterial, 1L))
                    .itemOutputs(GT_Utility.copyAmount(1L, aStack))
                    .duration(((int) Math.max(aMaterial.getMass() / 4L, 1L)) * TICKS)
                    .eut(8)
                    .addTo(sLatheRecipes);
            }

            if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)) {
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.round, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "fX", "Xh", 'X', OrePrefixes.nugget.get(aMaterial) });
                GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.round, aMaterial, 4L),
                    GT_Proxy.tBits,
                    new Object[] { "fXh", 'X', OrePrefixes.ingot.get(aMaterial) });
            }
        }
        Api.INSTANCE.registries()
            .matterCannon()
            .registerAmmo(GT_OreDictUnificator.get(OrePrefixes.round, aMaterial, 1L), aMaterial.getMass());
    }
}
