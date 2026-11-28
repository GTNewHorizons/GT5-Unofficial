package gregtech.common.powergoggles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.commands.GTBaseCommand;
import gregtech.common.powergoggles.gui.PowerGogglesGuiHudConfig;
import gregtech.common.powergoggles.handlers.PowerGogglesEventHandler;

public class PowerGogglesCommand extends GTBaseCommand {

    private static final String[] SUBCOMMANDS = { "config", "chart", "measurements" };

    public PowerGogglesCommand() {
        super("goggles", "pg");
    }

    @Override
    public String getCommandName() {
        return "powergoggles";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/powergoggles <subcommand>";
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

        List<IChatComponent> messages = new ArrayList<>();
        messages.add(
            new ChatComponentTranslation(
                "GT5U.chat.command.usage_with_subcommands",
                getCommandUsage(null),
                String.join(", ", SUBCOMMANDS)));
        messages.add(new ChatComponentTranslation("GT5U.chat.goggles.help.config"));
        messages.add(new ChatComponentTranslation("GT5U.chat.goggles.help.chart"));
        messages.add(new ChatComponentTranslation("GT5U.chat.goggles.help.measurements"));
        messages.add(new ChatComponentText(""));
        messages.add(new ChatComponentTranslation("GT5U.chat.command.aliases", "powergoggles, goggles, pg"));
        return messages;

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

        if (args.length < 1) {
            sendHelpMessage(sender);
            return;
        }

        switch (args[0]) {
            case "config" -> DelayedGuiDisplayTicker.create(new PowerGogglesGuiHudConfig(), 1);
            case "chart" -> PowerGogglesEventHandler.getInstance()
                .toggleChart();
            case "measurements" -> PowerGogglesEventHandler.getInstance()
                .toggleMeasurements();
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
