package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sLatheRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

public class ProcessingScrew implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingScrew() {
        OrePrefixes.screw.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (!aMaterial.contains(SubTag.NO_WORKING)) {
            if (GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L))
                    .itemOutputs(GT_Utility.copyAmount(1L, aStack))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(((int) Math.max(aMaterial.getMass() / 8L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(sLatheRecipes);
            }
            if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial))
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "fX", "X ", 'X', OrePrefixes.bolt.get(aMaterial) });
                }
        }
    }
}
