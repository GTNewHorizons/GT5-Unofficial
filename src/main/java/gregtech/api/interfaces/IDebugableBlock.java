package gregtech.api.interfaces;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

/**
 * You are allowed to include this File in your Download, as i will not change it.
 */
public interface IDebugableBlock {

    /**
     * Returns a Debug Message, for a generic DebugItem Blocks have to implement this interface NOT TileEntities!
     *
     * @param aPlayer   the Player, who right-clicked with his Debug Item
     * @param aX        Block-Coordinate
     * @param aY        Block-Coordinate
     * @param aZ        Block-Coordinate
     * @param aLogLevel the Log Level of the Debug Item. 0 = Obvious 1 = Visible for the regular Scanner 2 = Only
     *                  visible to more advanced Scanners 3 = Debug ONLY
     * @return a String-Array containing the DebugInfo, every Index is a separate line (0 = first Line)
     *
     * @deprecated This function returns a String directly, preventing the text from being translated on the client.
     *             Please use {@link addDebugInfoComp} instead. If both methods are overridden, the override of this
     *             method will not take effect.
     */
    @Deprecated
    default ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aX, int aY, int aZ, int aLogLevel) {
        return new ArrayList<>();
    }

    /**
     * Adds debug chat information for a generic DebugItem Blocks have to implement this interface NOT TileEntities!
     *
     * @param player   the Player, who right-clicked with his Debug Item
     * @param x        Block-Coordinate
     * @param y        Block-Coordinate
     * @param z        Block-Coordinate
     * @param logLevel the Log Level of the Debug Item. 0 = Obvious 1 = Visible for the regular Scanner 2 = Only
     *                 visible to more advanced Scanners 3 = Debug ONLY
     * @param list     the list to which debug information should be added
     */
    default void addDebugInfoComp(EntityPlayer player, int x, int y, int z, int logLevel,
        ArrayList<IChatComponent> list) {
        // Fall back to the old method if this method is not overridden.
        ArrayList<String> chats = this.getDebugInfo(player, x, y, z, logLevel);
        if (chats == null) return;
        for (String chat : chats) {
            list.add(new ChatComponentText(chat));
        }
    }
}
