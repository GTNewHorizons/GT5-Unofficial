package gregtech.common.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
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
        return "/powerfails <subcommand>";
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
        messages.add(new ChatComponentTranslation("GT5U.chat.command.usage_with_subcommands", getCommandUsage(null), String.join(", ", SUBCOMMANDS)));
        messages.add(new ChatComponentTranslation("GT5U.chat.powerfail.help.clear"));
        messages.add(new ChatComponentTranslation("GT5U.chat.powerfail.help.clear_dim"));
        messages.add(new ChatComponentTranslation("GT5U.chat.powerfail.help.list"));
        messages.add(new ChatComponentTranslation("GT5U.chat.powerfail.help.show"));
        messages.add(new ChatComponentTranslation("GT5U.chat.powerfail.help.hide"));
        messages.add(new ChatComponentTranslation("GT5U.chat.powerfail.help.help"));
        messages.add(new ChatComponentText(""));
        messages.add(new ChatComponentTranslation("GT5U.chat.command.aliases", "powerfails, powerfail, pf"));
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
                List<GTPowerfailTracker.Powerfail> powerfails = GTMod.proxy.powerfailTracker
                    .getPowerfails(player, OptionalInt.empty());

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
                sendChatToPlayer(
                    sender,
                    new ChatComponentTranslation("GT5U.chat.command.invalid_subcommand", args[0])
                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                sendHelpMessage(sender);
            }
        }
    }
}
