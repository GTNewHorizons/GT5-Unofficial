package gregtech.api.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

/**
 * Represents the target of a recipe adding action, usually, but not necessarily, is a recipe map itself.
 */
public interface IRecipeMap {

    /**
     * Actually add the recipe represented by the builder. CAN modify the builder's internal states!!!
     */
    @Nonnull
    Collection<GTRecipe> doAdd(GTRecipeBuilder builder);

    /**
     * Return a variant of this recipe map that will perform a deep copy on input recipe builder before doing anything
     * to it.
     */
    default IRecipeMap deepCopyInput() {
        return newRecipeMap(b -> doAdd(b.copy()));
    }

    static IRecipeMap newRecipeMap(Function<? super GTRecipeBuilder, Collection<GTRecipe>> func) {
        return new IRecipeMap() {

            @Nonnull
            @Override
            public Collection<GTRecipe> doAdd(GTRecipeBuilder builder) {
                List<Collection<GTRecipe>> ret = new ArrayList<>();
                Collection<GTRecipe> out = func.apply(builder);
                ret.add(out);
                builder.clearInvalid();
                return GTUtility.concat(ret);
            }
        };
    }
}
