package bartworks.API.recipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import bartworks.MainMod;
import gregtech.api.util.GTRecipe;

public class DynamicGTRecipe extends GTRecipe {

    public DynamicGTRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems,
        int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue, @Nullable GTRecipe originalRecipe) {
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
            this.owners = originalRecipe.owners == null ? null : new ArrayList<>(originalRecipe.owners);
            this.stackTraces = originalRecipe.stackTraces == null ? null : new ArrayList<>(originalRecipe.stackTraces);
            this.setOwner(MainMod.MOD_ID);
        }
    }
}
