package gregtech.api.interfaces.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public interface IDebugableTileEntity {

    /**
     * Returns a Debug Message, for a generic DebugItem
     *
     * @param aPlayer   the Player, who rightclicked with his Debug Item
     * @param aLogLevel the Log Level of the Debug Item. 0 = Obvious 1 = Visible for the regular Scanner 2 = Only
     *                  visible to more advanced Scanners 3 = Debug ONLY
     * @return a String-Array containing the DebugInfo, every Index is a separate line (0 = first Line)
     */
    @Deprecated
    default ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aLogLevel) {
        return new ArrayList<>();
    }

    default ArrayList<IChatComponent> getDebugInfoChat(EntityPlayer aPlayer, int aLogLevel) {
        ArrayList<String> chats = this.getDebugInfo(aPlayer, aLogLevel);
        if(chats == null) return new ArrayList<>();
        return chats
            .stream()
            .map(ChatComponentText::new)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
