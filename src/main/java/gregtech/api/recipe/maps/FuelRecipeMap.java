package gregtech.api.recipe.maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Just a Recipe Map with Utility specifically for Fuels.
 */
public class FuelRecipeMap extends GT_Recipe.GT_Recipe_Map {

    private final Map<String, GT_Recipe> mRecipesByFluidInput = new HashMap<>();

    public FuelRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName,
        String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
        int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
        String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
        super(
            aRecipeList,
            aUnlocalizedName,
            aLocalName,
            aNEIName,
            aNEIGUIPath,
            aUsualInputCount,
            aUsualOutputCount,
            aMinimalInputItems,
            aMinimalInputFluids,
            aAmperage,
            aNEISpecialValuePre,
            aNEISpecialValueMultiplier,
            aNEISpecialValuePost,
            aShowVoltageAmperageInNEI,
            aNEIAllowed);
        setDisableOptimize(true);
    }

    public GT_Recipe addFuel(ItemStack aInput, ItemStack aOutput, int aFuelValueInEU) {
        return addFuel(aInput, aOutput, null, null, 10000, aFuelValueInEU);
    }

    public GT_Recipe addFuel(ItemStack aInput, ItemStack aOutput, int aChance, int aFuelValueInEU) {
        return addFuel(aInput, aOutput, null, null, aChance, aFuelValueInEU);
    }

    public GT_Recipe addFuel(FluidStack aFluidInput, FluidStack aFluidOutput, int aFuelValueInEU) {
        return addFuel(null, null, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
    }

    public GT_Recipe addFuel(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput,
        int aFuelValueInEU) {
        return addFuel(aInput, aOutput, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
    }

    public GT_Recipe addFuel(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput,
        int aChance, int aFuelValueInEU) {
        return addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput },
            null,
            new int[] { aChance },
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            0,
            0,
            aFuelValueInEU);
    }

    @Override
    public GT_Recipe add(GT_Recipe aRecipe) {
        aRecipe = super.add(aRecipe);
        if (aRecipe.mInputs != null && GT_Utility.getNonnullElementCount(aRecipe.mInputs) == 1
            && (aRecipe.mFluidInputs == null || GT_Utility.getNonnullElementCount(aRecipe.mFluidInputs) == 0)) {
            FluidStack tFluid = GT_Utility.getFluidForFilledItem(aRecipe.mInputs[0], true);
            if (tFluid != null) {
                tFluid.amount = 0;
                mRecipesByFluidInput.put(tFluid.getUnlocalizedName(), aRecipe);
            }
        } else if ((aRecipe.mInputs == null || GT_Utility.getNonnullElementCount(aRecipe.mInputs) == 0)
            && aRecipe.mFluidInputs != null
            && GT_Utility.getNonnullElementCount(aRecipe.mFluidInputs) == 1
            && aRecipe.mFluidInputs[0] != null) {
                mRecipesByFluidInput.put(aRecipe.mFluidInputs[0].getUnlocalizedName(), aRecipe);
            }
        return aRecipe;
    }

    public GT_Recipe findFuel(FluidStack aFluidInput) {
        return mRecipesByFluidInput.get(aFluidInput.getUnlocalizedName());
    }
}
