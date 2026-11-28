package gregtech.common.misc;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.ChatMessage;
import gregtech.common.config.Client;

public class GTPowerfailCommandClient extends GTPowerfailCommand {

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        EntityClientPlayerMP player = (EntityClientPlayerMP) sender;

        if (args.length < 1) {
            sendHelpMessage(sender);
            return;
        }

        switch (args[0]) {
            case "show" -> {
                Client.render.renderPowerfailNotifications = true;
                Client.save();

                ChatMessage.PowerfailRenderShown.send(player);
            }
            case "hide" -> {
                Client.render.renderPowerfailNotifications = false;
                Client.save();

                ChatMessage.PowerfailRenderHidden.send(player);
            }
            case "help" -> sendHelpMessage(sender);
            case "clear", "clear-dim", "list" -> // Pass it to the server
                player.sendChatMessage("/" + getCommandName() + " " + String.join(" ", args));
            default -> {
                sendChatToPlayer(
                    sender,
                    new ChatComponentTranslation("GT5U.chat.command.invalid_subcommand", args[0])
                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                sendHelpMessage(sender);
            }
        }
    }
}
