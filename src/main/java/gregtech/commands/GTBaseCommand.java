package gregtech.commands;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.google.common.collect.ImmutableList;

public abstract class GTBaseCommand extends CommandBase {

    protected final @Nullable List<String> ALIASES;

    public GTBaseCommand() {
        ALIASES = null;
    }

    public GTBaseCommand(String... aliases) {
        ALIASES = ImmutableList.copyOf(aliases);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return '/' + this.getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return ALIASES;
    }

    protected List<IChatComponent> getHelpMessages() {
        return null;
    }

    protected void sendHelpMessage(ICommandSender sender) {
        final List<IChatComponent> list = getHelpMessages();
        if (list == null) {
            sendChatToPlayer(sender, getCommandUsage(sender));
        } else {
            list.forEach(sender::addChatMessage);
        }
    }

    protected static void sendChatToPlayer(ICommandSender sender, String message) {
        if (sender == null || message == null) return; // idiot check
        sendChatToPlayer(sender, new ChatComponentText(message));
    }

    protected static void sendChatToPlayer(ICommandSender sender, IChatComponent message) {
        if (sender == null || message == null) return; // idiot check
        sender.addChatMessage(message);
    }

    protected static boolean isOpedPlayer(ICommandSender sender) {
        if (sender instanceof EntityPlayerMP player) {
            // func_152596_g = canSendCommands
            return MinecraftServer.getServer()
                .getConfigurationManager()
                .func_152596_g(player.getGameProfile());
        }
        return false;
    }
}
