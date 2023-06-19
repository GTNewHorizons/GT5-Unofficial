package gregtech.api.interfaces.tileentity;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Single_Recipe_Check;

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

    default GT_Single_Recipe_Check getSingleRecipeCheck() {
        return null;
    }

    default void setSingleRecipeCheck(GT_Single_Recipe_Check recipeCheck) {

    }

    default ArrayList<ItemStack> getStoredInputs() {
        return new ArrayList<>();
    }

    default ArrayList<FluidStack> getStoredFluids() {
        return new ArrayList<>();
    }

    default ItemStack getControllerSlot() {
        return null;
    }

    default GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }
}
