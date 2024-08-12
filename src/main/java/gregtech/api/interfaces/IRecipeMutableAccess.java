package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

/**
 * Mixed-in interface for recipe classes in Forge and Vanilla that allows mutating the input and output items.
 */
public interface IRecipeMutableAccess {

    /** @return Gets the current output item of the recipe */
    ItemStack gt5u$getRecipeOutputItem();

    /** Sets a new output item on the recipe */
    void gt5u$setRecipeOutputItem(ItemStack newItem);

    /** @return The raw list or array of recipe inputs, the exact type depends on the underlying recipe type. */
    Object gt5u$getRecipeInputs();
}
