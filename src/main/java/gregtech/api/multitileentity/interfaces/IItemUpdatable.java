package gregtech.api.multitileentity.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemUpdatable {

    /**
     * Updates the Data of the ItemStack. Not called every tick but instead called whenever something important happens
     * to the Stack.
     */
    void updateItemStack(ItemStack aStack);

    /**
     * Updates the Data of the ItemStack. Not called every tick but instead called whenever something important happens
     * to the Stack.
     */
    void updateItemStack(ItemStack aStack, World aWorld, int aX, int aY, int aZ);
}
