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
     * Add a downstream recipe map that will get to handle the original builder.
     * <p>
     * Downstream recipe maps got passed the recipe builder after parent recipe map is done with its business. Notice
     * at this time the original recipe builder might be modified by the parent recipe map in some form, but it will
     * remain as valid.
     * <p>
     * A downstream will only be invoked if parent recipe map added something.
     *
     * @param downstream the downstream recipe map to add
     */
    void addDownstream(IRecipeMap downstream);

    /**
     * Actually add the recipe represented by the builder. CAN modify the builder's internal states!!!
     */
    @Nonnull
    Collection<GTRecipe> doAdd(GTRecipeBuilder builder);

    /**
     * Return a variant of this recipe map that will perform a deep copy on input recipe builder before doing anything
     * to it.
     * <p>
     * The returned recipe map will not have any downstreams, but can accept new downstreams.
     */
    default IRecipeMap deepCopyInput() {
        return newRecipeMap(b -> doAdd(b.copy()));
    }

    static IRecipeMap newRecipeMap(Function<? super GTRecipeBuilder, Collection<GTRecipe>> func) {
        return new IRecipeMap() {

            private final Collection<IRecipeMap> downstreams = new ArrayList<>();

            @Override
            public void addDownstream(IRecipeMap downstream) {
                downstreams.add(downstream);
            }

            @Nonnull
            @Override
            public Collection<GTRecipe> doAdd(GTRecipeBuilder builder) {
                List<Collection<GTRecipe>> ret = new ArrayList<>();
                Collection<GTRecipe> out = func.apply(builder);
                ret.add(out);
                builder.clearInvalid();
                if (!out.isEmpty()) {
                    for (IRecipeMap downstream : downstreams) {
                        ret.add(downstream.doAdd(builder));
                    }
                }
                return GTUtility.concat(ret);
            }
        };
    }
}
