package gregtech.api.interfaces;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.net.GTPacketUpdateItem;

/**
 * Together with {@link GTPacketUpdateItem} you can request server side to update item in hand with a
 * NBT tag.
 * <p>
 * Usual NBT tag size limit applies.
 */
public interface INetworkUpdatableItem {

    /**
     * Receive update from client. Runs on server thread.
     *
     * @param stack  Stack being updated
     * @param player player holding the stack
     * @param tag    received data
     * @return true if this stack should be kept inside the player inventory. false if this stack should vanish (i.e.
     *         slot content set to null)
     */
    boolean receive(ItemStack stack, EntityPlayerMP player, NBTTagCompound tag);
}
