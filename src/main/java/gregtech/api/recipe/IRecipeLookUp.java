package gregtech.api.recipe;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe;

public interface IRecipeLookUp {

    /**
     * @return Entrypoint for fluent API for finding recipe.
     */
    default FindRecipeQuery findRecipeQuery() {
        return new FindRecipeQuery(this);
    }

    int getAmperage();

    Stream<GT_Recipe> matchRecipeStream(ItemStack[] rawItems, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable gregtech.api.util.GT_Recipe cachedRecipe, boolean notUnificated, boolean dontCheckStackSizes,
        boolean forCollisionCheck);
}
