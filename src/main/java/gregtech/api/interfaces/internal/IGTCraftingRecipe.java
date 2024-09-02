package gregtech.api.interfaces.internal;

import net.minecraft.item.crafting.IRecipe;

public interface IGTCraftingRecipe extends IRecipe {

    boolean isRemovable();
}
