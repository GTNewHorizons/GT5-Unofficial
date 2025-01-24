package gregtech.api.interfaces.tileentity;

import net.minecraft.entity.player.EntityPlayer;

public interface IKeyHandlerTile {
    /**
     * Called when a key interaction occurs
     *
     * @param aKey The key identifier (e.g. KEY_CTRL = 1)
     * @param aPressed True if key was pressed, false if released
     * @param aPlayer The player who triggered the key event
     * @return True if the key event was handled
     */
    boolean onKeyInteraction(byte aKey, boolean aPressed, EntityPlayer aPlayer);
}
