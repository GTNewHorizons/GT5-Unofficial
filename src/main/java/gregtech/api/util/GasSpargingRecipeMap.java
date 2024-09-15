package gregtech.api.util;

import static gregtech.api.enums.Mods.GregTech;

import java.util.HashSet;

import net.minecraftforge.fluids.FluidStack;

public class GasSpargingRecipeMap {

    public static final HashSet<GasSpargingRecipe> mRecipes = new HashSet<>();
    public static final String mUnlocalizedName = "gtpp.recipe.lftr.sparging";
    public static final String mNEIDisplayName = "LFTR Gas Sparging";
    public static final String mNEIGUIPath = GregTech.getResourcePath("textures", "gui/basicmachines/FissionFuel.png");

    public static boolean addRecipe(FluidStack aSpargeGas, FluidStack aSpentFuel, FluidStack aSpargedFuel,
        FluidStack[] aOutputs, int[] aMaxOutputs) {
        if (aSpargeGas == null || aSpargeGas.amount <= 0
            || aSpentFuel == null
            || aSpentFuel.amount <= 0
            || aSpargedFuel == null
            || aSpargedFuel.amount <= 0
            || aOutputs == null
            || aOutputs.length < 1
            || aMaxOutputs == null
            || aMaxOutputs.length < 1
            || aOutputs.length != aMaxOutputs.length) {
            return false;
        }
        int aMapSize = mRecipes.size();
        GasSpargingRecipe aRecipe = new GasSpargingRecipe(aSpargeGas, aSpentFuel, aSpargedFuel, aOutputs, aMaxOutputs);
        mRecipes.add(aRecipe);
        return mRecipes.size() > aMapSize;
    }

    public static GasSpargingRecipe findRecipe(FluidStack aSpargeGas, FluidStack aSpentFuel) {
        for (GasSpargingRecipe aRecipe : mRecipes) {
            if (aRecipe.containsInputs(aSpargeGas, aSpentFuel)) {
                return aRecipe;
            }
        }
        return null;
    }
}
