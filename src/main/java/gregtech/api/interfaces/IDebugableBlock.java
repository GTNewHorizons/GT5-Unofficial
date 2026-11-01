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
     *             Please use {@link getDebugInfoChat} instead.
     */
    @Deprecated
    default ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aX, int aY, int aZ, int aLogLevel) {
        return new ArrayList<>();
    }

    /**
     * Returns a Debug Message, for a generic DebugItem Blocks have to implement this interface NOT TileEntities!
     *
     * @param aPlayer   the Player, who right-clicked with his Debug Item
     * @param aX        Block-Coordinate
     * @param aY        Block-Coordinate
     * @param aZ        Block-Coordinate
     * @param aLogLevel the Log Level of the Debug Item. 0 = Obvious 1 = Visible for the regular Scanner 2 = Only
     *                  visible to more advanced Scanners 3 = Debug ONLY
     * @return a ChatComponent-Array containing the DebugInfo, every Index is a separate line (0 = first Line)
     */
    default ArrayList<IChatComponent> getDebugInfoChat(EntityPlayer aPlayer, int aX, int aY, int aZ, int aLogLevel) {
        // Fall back to the old method if this method is not overridden.
        ArrayList<String> chats = this.getDebugInfo(aPlayer, aX, aY, aZ, aLogLevel);
        if (chats == null) return new ArrayList<>();
        return chats.stream()
            .map(ChatComponentText::new)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
