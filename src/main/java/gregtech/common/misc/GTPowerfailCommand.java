package gregtech.common.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import gregtech.common.data.GTPowerfailTracker;

public class GTPowerfailCommand extends CommandBase {

    private static final String[] SUBCOMMANDS = { "clear", "clear-dim", "list", "show", "hide", "help" };

    @Override
    public String getCommandName() {
        return "powerfails";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("powerfail", "pf");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /powerfails <subcommand>. Valid subcommands are: " + String.join(", ", SUBCOMMANDS) + ".";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    private void printHelp(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        sender.addChatMessage(new ChatComponentText(" clear - Clears all powerfails for your team"));
        sender.addChatMessage(
            new ChatComponentText(" clear-dim - Clears all powerfails for your team in the current dimension"));
        sender.addChatMessage(new ChatComponentText(" list - Prints all uncleared powerfails for your team"));
        sender.addChatMessage(new ChatComponentText(" show - Enables in-world powerfail icons"));
        sender.addChatMessage(new ChatComponentText(" hide - Disables in-world powerfail icons"));
        sender.addChatMessage(new ChatComponentText(" help - Prints this help text"));
        sender.addChatMessage(new ChatComponentText(""));
        sender.addChatMessage(new ChatComponentText("Aliases: powerfails, powerfail, pf"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> l = new ArrayList<>();
        String first = args.length == 0 ? "" : args[0].trim();

        if (first.isEmpty()) {
            l.addAll(Arrays.asList(SUBCOMMANDS));
        } else {
            if (args.length == 1) {
                l.addAll(getListOfStringsMatchingLastWord(args, SUBCOMMANDS));
            }
        }

        return l;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP player)) {
            sender.addChatMessage(new ChatComponentText("This command can only be ran by a player"));
            return;
        }

        if (args.length < 1) {
            printHelp(sender);
            return;
        }

        switch (args[0]) {
            case "clear" -> {
                GTPowerfailTracker.clearPowerfails(player, OptionalInt.empty());
                sender.addChatMessage(new ChatComponentText("Cleared all of your powerfails."));
            }
            case "clear-dim" -> {
                GTPowerfailTracker.clearPowerfails(player, OptionalInt.of(player.worldObj.provider.dimensionId));
                sender
                    .addChatMessage(new ChatComponentText("Cleared all of your powerfails in the current dimension."));
            }
            case "list" -> {
                final UUID playerId = player.getGameProfile()
                    .getId();
                List<GTPowerfailTracker.Powerfail> powerfails = GTPowerfailTracker
                    .getPowerfails(playerId, OptionalInt.empty());

                if (powerfails.isEmpty()) {
                    sender.addChatMessage(new ChatComponentText(""));
                    sender.addChatMessage(new ChatComponentText("No powerfails have occurred."));
                    return;
                }

                sender.addChatMessage(new ChatComponentText(""));
                sender.addChatMessage(new ChatComponentText("Uncleared powerfails:"));

                if (powerfails.size() > 25) {
                    // poor bastard :kekw:

                    for (GTPowerfailTracker.Powerfail powerfail : powerfails.subList(0, 25)) {
                        sender.addChatMessage(new ChatComponentText("- " + powerfail.toString()));
                    }

                    sender
                        .addChatMessage(new ChatComponentText(powerfails.size() + " additional powerfails truncated"));
                } else {
                    for (GTPowerfailTracker.Powerfail powerfail : powerfails) {
                        sender.addChatMessage(new ChatComponentText("- " + powerfail.toString()));
                    }
                }

                sender.addChatMessage(new ChatComponentText("Run /powerfails clear to remove all powerfails"));
                sender.addChatMessage(
                    new ChatComponentText("Run /powerfails clear-dim to remove all powerfails in your current world"));
            }
            case "show" -> {
                GTPowerfailTracker.showPowerfails(player);
                sender.addChatMessage(new ChatComponentText("Enabled powerfail overlay rendering."));
            }
            case "hide" -> {
                GTPowerfailTracker.hidePowerfails(player);
                sender.addChatMessage(new ChatComponentText("Disabled powerfail overlay rendering."));
            }
            default -> {
                printHelp(sender);
            }
        }
    }
}
