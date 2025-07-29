package gregtech.common.misc;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import gregtech.GTMod;
import gregtech.api.enums.ChatMessage;
import gregtech.commands.GTBaseCommand;
import gregtech.common.data.GTPowerfailTracker;

public class GTPowerfailCommand extends GTBaseCommand {

    private static final String[] SUBCOMMANDS = { "clear", "clear-dim", "list", "show", "hide", "help" };

    public GTPowerfailCommand() {
        super("powerfails", "powerfail", "pf");
    }

    @Override
    public String getCommandName() {
        return "powerfails";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /powerfails <subcommand>. Valid subcommands are: " + String.join(", ", SUBCOMMANDS) + ".";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    protected List<IChatComponent> getHelpMessages() {
        // spotless:off
        List<IChatComponent> messages = new ArrayList<>();
        messages.add(new ChatComponentText(getCommandUsage(null)));
        messages.add(new ChatComponentText(" clear - Clears all powerfails for your team"));
        messages.add(new ChatComponentText(" clear-dim - Clears all powerfails for your team in the current dimension"));
        messages.add(new ChatComponentText(" list - Prints all uncleared powerfails for your team"));
        messages.add(new ChatComponentText(" show - Enables in-world powerfail icons"));
        messages.add(new ChatComponentText(" hide - Disables in-world powerfail icons"));
        messages.add(new ChatComponentText(" help - Prints this help text"));
        messages.add(new ChatComponentText(""));
        messages.add(new ChatComponentText("Aliases: powerfails, powerfail, pf"));
        return messages;
        // spotless:on
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
        EntityPlayerMP player = (EntityPlayerMP) sender;

        if (args.length < 1) {
            sendHelpMessage(sender);
            return;
        }

        // Note: show, hide, and help are processed by the client command, so they should never end up here.

        switch (args[0]) {
            case "clear" -> {
                GTMod.proxy.powerfailTracker.clearPowerfails(player, OptionalInt.empty());
                ChatMessage.PowerfailsCleared.send(player);
            }
            case "clear-dim" -> {
                GTMod.proxy.powerfailTracker
                    .clearPowerfails(player, OptionalInt.of(player.worldObj.provider.dimensionId));

                ChatMessage.PowerfailsClearedDim.send(player);
            }
            case "list" -> {
                final UUID playerId = player.getGameProfile()
                    .getId();
                List<GTPowerfailTracker.Powerfail> powerfails = GTMod.proxy.powerfailTracker
                    .getPowerfails(playerId, OptionalInt.empty());

                sendChatToPlayer(sender, "");

                if (powerfails.isEmpty()) {
                    ChatMessage.PowerfailsListNone.send(player);
                    return;
                }

                ChatMessage.PowerfailsListHeader.send(player);

                if (powerfails.size() > 25) {
                    // poor bastard :kekw:

                    for (GTPowerfailTracker.Powerfail powerfail : powerfails.subList(0, 25)) {
                        ChatMessage.PowerfailsListEntry.send(player, powerfail.toDescription());
                    }

                    ChatMessage.PowerfailsListTruncated.send(player, powerfails.size() - 25);
                } else {
                    for (GTPowerfailTracker.Powerfail powerfail : powerfails) {
                        ChatMessage.PowerfailsListEntry.send(player, powerfail.toDescription());
                    }
                }
            }
            default -> {
                sendChatToPlayer(sender, RED + "Illegal subcommand: " + args[0]);
                sendHelpMessage(sender);
            }
        }
    }
}
