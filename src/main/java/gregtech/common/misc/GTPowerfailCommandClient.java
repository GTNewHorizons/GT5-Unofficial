package gregtech.common.misc;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.RED;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import net.minecraftforge.client.ClientCommandHandler;

import gregtech.common.config.Client;

public class GTPowerfailCommandClient extends GTPowerfailCommand {

    public static void register() {
        ClientCommandHandler.instance.registerCommand(new GTPowerfailCommandClient());
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityClientPlayerMP player)) {
            sender.addChatMessage(new ChatComponentText("This command can only be ran by a player"));
            return;
        }

        sender.addChatMessage(new ChatComponentText("client."));

        if (args.length < 1) {
            sendHelpMessage(sender);
            return;
        }

        switch (args[0]) {
            case "show" -> {
                Client.render.renderPowerfailNotifications = true;
                Client.save();

                sender.addChatMessage(new ChatComponentText("Enabled powerfail overlay rendering."));
            }
            case "hide" -> {
                Client.render.renderPowerfailNotifications = false;
                Client.save();

                sender.addChatMessage(new ChatComponentText("Disabled powerfail overlay rendering."));
            }
            case "help" -> {
                sendHelpMessage(sender);
            }
            case "clear", "clear-dim",  "list" -> {
                // Pass it to the server
                player.sendChatMessage("/" + getCommandName() + " " + String.join(" ", args));
            }
            default -> {
                sendChatToPlayer(sender, RED + "Illegal subcommand: " + args[0]);
                sendHelpMessage(sender);
            }
        }
    }
}
