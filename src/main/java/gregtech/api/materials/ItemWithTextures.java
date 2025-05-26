package gregtech.api.materials;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.IItemTexture;

public interface ItemWithTextures {

    /** texture[layer] */
    IItemTexture[] getTextures(ItemStack stack);

}
