package gregtech.api.interfaces.tileentity;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.SingleRecipeCheck;

/**
 * Machines implementing this interface can have logic to lock to a single recipe.
 */
public interface IRecipeLockable {

    /**
     * @return if this machine supports single recipe locking.
     */
    boolean supportsSingleRecipeLocking();

    /**
     * @return true if recipe locking is enabled, else false. This is getter is used for displaying the icon in the GUI
     */
    boolean isRecipeLockingEnabled();

    void setRecipeLocking(boolean enabled);

    default boolean getDefaultRecipeLockingMode() {
        return false;
    }

    default SingleRecipeCheck getSingleRecipeCheck() {
        return null;
    }

    default void setSingleRecipeCheck(SingleRecipeCheck recipeCheck) {}

    RecipeMap getRecipeMap();
}
