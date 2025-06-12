package gregtech.common.misc;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Stream;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import gregtech.common.data.GTPowerfailTracker;

public class GTPowerfailCommand extends CommandBase {

    private static final String[] SUBCOMMANDS = { "clear", "clear-dim", "list", "show", "hide" };

    @Override
    public String getCommandName() {
        return "powerfails";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return GRAY + "Usage: /powerfails <subcommand>. Valid subcommands are: " + String.join(", ", SUBCOMMANDS) + ".";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    private void printHelp(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        sender.addChatMessage(new ChatComponentText(GRAY + " clear - Clears all powerfails for your team"));
        sender.addChatMessage(
            new ChatComponentText(GRAY + " clear-dim - Clears all powerfails for your team in the current dimension"));
        sender.addChatMessage(new ChatComponentText(GRAY + " list - Prints all uncleared powerfails for your team"));
        sender.addChatMessage(new ChatComponentText(GRAY + " show - Enables in-world powerfail icons"));
        sender.addChatMessage(new ChatComponentText(GRAY + " hide - Disables in-world powerfail icons"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> l = new ArrayList<>();
        String first = args.length == 0 ? "" : args[0].trim();

        if (first.isEmpty()) {
            l.addAll(Arrays.asList(SUBCOMMANDS));
        } else {
            if (args.length == 1) {
                if (Stream.of(SUBCOMMANDS)
                    .anyMatch(s -> s.startsWith(first))) {
                    Stream.of(SUBCOMMANDS)
                        .filter(s -> s.startsWith(first))
                        .forEach(l::add);
                }
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
                sender.addChatMessage(new ChatComponentText(GRAY + "Cleared all of your powerfails."));
            }
            case "clear-dim" -> {
                GTPowerfailTracker.clearPowerfails(player, OptionalInt.of(player.worldObj.provider.dimensionId));
                sender.addChatMessage(
                    new ChatComponentText(GRAY + "Cleared all of your powerfails in the current dimension."));
            }
            case "list" -> {
                final UUID playerId = player.getGameProfile()
                    .getId();
                List<GTPowerfailTracker.Powerfail> powerfails = GTPowerfailTracker
                    .getPowerfails(playerId, OptionalInt.empty());

                if (powerfails.isEmpty()) {
                    sender.addChatMessage(new ChatComponentText(""));
                    sender.addChatMessage(new ChatComponentText(GRAY + "No powerfails have occurred."));
                    return;
                }

                sender.addChatMessage(new ChatComponentText(""));
                sender.addChatMessage(new ChatComponentText(GRAY + "Uncleared powerfails:"));

                if (powerfails.size() > 25) {
                    // poor bastard :kekw:

                    for (GTPowerfailTracker.Powerfail powerfail : powerfails.subList(0, 25)) {
                        sender.addChatMessage(new ChatComponentText(GRAY + "- " + powerfail.toString()));
                    }

                    sender
                        .addChatMessage(new ChatComponentText(powerfails.size() + " additional powerfails truncated"));
                } else {
                    for (GTPowerfailTracker.Powerfail powerfail : powerfails) {
                        sender.addChatMessage(new ChatComponentText("- " + powerfail.toString()));
                    }
                }

                sender.addChatMessage(new ChatComponentText(GRAY + "Run /powerfails clear to remove all powerfails"));
                sender.addChatMessage(
                    new ChatComponentText(
                        GRAY + "Run /powerfails clear-dim to remove all powerfails in your current world"));
            }
            case "show" -> {
                GTPowerfailTracker.showPowerfails(player);
                sender.addChatMessage(new ChatComponentText(GRAY + "Enabled powerfail overlay rendering."));
            }
            case "hide" -> {
                GTPowerfailTracker.hidePowerfails(player);
                sender.addChatMessage(new ChatComponentText(GRAY + "Disabled powerfail overlay rendering."));
            }
        }
    }
}
