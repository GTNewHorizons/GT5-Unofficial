package gregtech.crossmod.backhand;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.Mods;
import xonin.backhand.api.core.BackhandUtils;

public class Backhand {

    /**
     * Gets the offhand item.
     * 
     * @param player the player to check
     * @return The offhand item stack, or null if the player isn't using the offhand or Backhand isn't loaded
     */
    @Nullable
    public static ItemStack getOffhandItem(EntityPlayer player) {
        if (Mods.Backhand.isModLoaded()) {
            return BackhandUtils.getOffhandItem(player);
        }

        return null;
    }

    /**
     * Returns the offhand slot number.
     * 
     * @param player The player to check
     * @return The offhand slot number or -1 if Backhand isn't loaded
     */
    public static int getOffhandSlot(EntityPlayer player) {
        if (Mods.Backhand.isModLoaded()) {
            return BackhandUtils.getOffhandSlot(player);
        }

        return -1;
    }
}
