package gregtech.api.interfaces.tileentity;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import gregtech.api.recipe.RecipeMap;

/**
 * Machines implementing this interface are capable of executing certain recipes provided by {@link RecipeMap}.
 * They will also be automatically registered as NEI recipe catalyst for the corresponding recipemaps.
 */
public interface RecipeMapWorkable {

    /**
     * @return RecipeMap this machine currently can execute. In general, it's allowed to be null.
     */
    RecipeMap<?> getRecipeMap();

    /**
     * @return ItemStack form of this machine.
     */
    ItemStack getStackForm(long amount);

    /**
     * If the machine supports multiple recipemaps by switching mode, override this method so that it will be displayed
     * as NEI recipe catalyst on all the supported recipemaps.
     *
     * @return List of possible {@link RecipeMap}s this machine can execute. Must not contain null element.
     */
    @Nonnull
    default Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        RecipeMap<?> recipeMap = getRecipeMap();
        if (recipeMap != null) {
            return Collections.singletonList(recipeMap);
        }
        return Collections.emptyList();
    }

    /**
     * @return Priority for NEI recipe catalyst. Higher priority comes first.
     */
    default int getRecipeCatalystPriority() {
        return 0;
    }
}
