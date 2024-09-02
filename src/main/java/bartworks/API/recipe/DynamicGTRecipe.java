package bartworks.API.recipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.MainMod;
import gregtech.api.util.GTRecipe;

public class DynamicGTRecipe extends GTRecipe {

    public DynamicGTRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems,
        int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue, GTRecipe originalRecipe) {
        super(
            aOptimize,
            aInputs,
            aOutputs,
            aSpecialItems,
            aChances,
            aFluidInputs,
            aFluidOutputs,
            aDuration,
            aEUt,
            aSpecialValue);
        if (originalRecipe != null) {
            this.owners = new ArrayList<>(originalRecipe.owners);
            this.stackTraces = new ArrayList<>(originalRecipe.stackTraces);
            this.setOwner(MainMod.MOD_ID);
        }
    }
}
