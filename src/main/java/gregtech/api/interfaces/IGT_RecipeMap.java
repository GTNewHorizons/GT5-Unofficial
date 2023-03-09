package gregtech.api.interfaces;

import java.util.Collection;

import javax.annotation.Nonnull;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;

/**
 * Represents the target of a recipe adding action, usually, but not necessarily, is a recipe map itself.
 */
public interface IGT_RecipeMap {

    /**
     * Actually add the recipe represented by the builder. CAN modify the builder's internal states!!!
     */
    @Nonnull
    Collection<GT_Recipe> doAdd(GT_RecipeBuilder builder);

    /**
     * Return a variant of this recipe map that will perform a deep copy on input recipe builder before doing anything
     * to it.
     */
    default IGT_RecipeMap deepCopyInput() {
        return b -> doAdd(b.copy());
    }
}
