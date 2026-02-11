package gregtech.api.interfaces.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPickBlockHandler {

    /**
     * Called when the client presses the pick block keybind.
     * 
     * @param itemStack The item stack being used currently by the player.
     *                  Main hand is chosen first, if it's an IPickBlockHandler, otherwise offhand.
     * @param player    The player who pushed the pick block keybind.
     * @return true to cancel all other actions, including the vanilla pick block behavior
     */
    default boolean onPickBlock(ItemStack itemStack, EntityPlayer player) {
        return false;
    }
}
