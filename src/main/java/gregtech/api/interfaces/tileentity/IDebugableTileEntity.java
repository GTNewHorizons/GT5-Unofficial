package gregtech.api.interfaces.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public interface IDebugableTileEntity {

    /**
     * Returns a Debug Message, for a generic DebugItem
     *
     * @param aPlayer   the Player, who right-clicked with his Debug Item
     * @param aLogLevel the Log Level of the Debug Item. 0 = Obvious 1 = Visible for the regular Scanner 2 = Only
     *                  visible to more advanced Scanners 3 = Debug ONLY
     * @return a String-Array containing the DebugInfo, every Index is a separate line (0 = first Line)
     *
     * @deprecated This function returns a String directly, preventing the text from being translated on the client.
     *             Please use {@link addDebugInfoComp} instead.
     */
    @Deprecated
    default ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aLogLevel) {
        return new ArrayList<>();
    }

    /**
     * Add Debug Messages, for a generic DebugItem
     *
     * @param aPlayer   the Player, who right-clicked with his Debug Item
     * @param aLogLevel the Log Level of the Debug Item. 0 = Obvious 1 = Visible for the regular Scanner 2 = Only
     *                  visible to more advanced Scanners 3 = Debug ONLY
     * @param aList     the list to which debug information should be added
     */
    default void addDebugInfoComp(EntityPlayer aPlayer, int aLogLevel, ArrayList<IChatComponent> aList) {
        ArrayList<String> chats = this.getDebugInfo(aPlayer, aLogLevel);
        if (chats == null) return;
        for (String chat : chats) {
            aList.add(new ChatComponentText(chat));
        }
    }
}
