package gregtech.api.recipe.maps;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.Mods.GregTech;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe;

public class OilCrackerRecipeMap extends GT_Recipe.GT_Recipe_Map {

    private final Set<String> mValidCatalystFluidNames = new HashSet<>();

    public OilCrackerRecipeMap() {
        super(
            new HashSet<>(70),
            "gt.recipe.craker",
            "Oil Cracker",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "OilCracker"),
            1,
            1,
            1,
            2,
            1,
            E,
            1,
            E,
            true,
            true);
    }

    @Override
    public GT_Recipe add(GT_Recipe aRecipe) {
        GT_Recipe ret = super.add(aRecipe);
        if (ret != null && ret.mFluidInputs != null && ret.mFluidInputs.length > 1 && ret.mFluidInputs[1] != null) {
            mValidCatalystFluidNames.add(
                ret.mFluidInputs[1].getFluid()
                    .getName());
        }
        return ret;
    }

    public boolean isValidCatalystFluid(FluidStack aFluidStack) {
        return mValidCatalystFluidNames.contains(
            aFluidStack.getFluid()
                .getName());
    }
}
