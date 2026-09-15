package galacticgreg.auxiliary;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

/**
 * Method to easily send chat-messages to EntityPlayer
 *
 * @author Namikon
 *
 */
public class PlayerChatHelper {

    /**
     * Meant for notifications that are being send to an admin/op Color will be GREEN
     *
     * @param pPlayer
     * @param pMessage
     */
    public static void SendInfo(ICommandSender pCommandSender, String pMessage) {
        pCommandSender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + pMessage));
    }

    /**
     * Meant for notifications that are being send to an admin/op Color will be RED
     *
     * @param pPlayer
     * @param pMessage
     */
    public static void SendError(ICommandSender pCommandSender, String pMessage) {
        pCommandSender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + pMessage));
    }

    /**
     * Meant for notifications that are being send to an admin/op Color will be YELLOW
     *
     * @param pPlayer
     * @param pMessage
     */
    public static void SendWarn(ICommandSender pCommandSender, String pMessage) {
        pCommandSender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + pMessage));
    }

    /**
     * Meant for notifications that are being send to an admin/op Color will be GREEN
     *
     * @param pPlayer
     * @param pMessage
     */
    public static void SendInfo(EntityPlayer pPlayer, String pMessage) {
        pPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + pMessage));
    }

    /**
     * Meant for notifications that are being send to an admin/op Color will be RED
     *
     * @param pPlayer
     * @param pMessage
     */
    public static void SendError(EntityPlayer pPlayer, String pMessage) {
        pPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + pMessage));
    }

    /**
     * Meant for notifications that are being send to an admin/op Color will be YELLOW
     *
     * @param pPlayer
     * @param pMessage
     */
    public static void SendWarn(EntityPlayer pPlayer, String pMessage) {
        pPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + pMessage));
    }

    /**
     * Meant for ingame notifications that are being send to a player, not an admin/op Color will be DARK_GREEN
     *
     * @param pPlayer
     * @param pMessage
     */
    public static void SendNotifyPositive(EntityPlayer pPlayer, String pMessage) {
        pPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GREEN + pMessage));
    }

    /**
     * Meant for ingame notifications that are being send to a player, not an admin/op Color will be AQUA
     *
     * @param pPlayer
     * @param pMessage
     */
    public static void SendNotifyNormal(EntityPlayer pPlayer, String pMessage) {
        pPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + pMessage));
    }

    /**
     * Meant for ingame notifications that are being send to a player, not an admin/op Color will be DARK_PURPLE
     *
     * @param pPlayer
     * @param pMessage
     */
    public static void SendNotifyWarning(EntityPlayer pPlayer, String pMessage) {
        pPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + pMessage));
    }

}
