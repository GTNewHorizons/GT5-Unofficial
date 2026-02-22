package gregtech.common.powergoggles;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import gregtech.commands.GTBaseCommand;
import gregtech.common.powergoggles.gui.PowerGogglesGuiHudConfig;
import gregtech.common.powergoggles.handlers.PowerGogglesEventHandler;

public class PowerGogglesCommand extends GTBaseCommand {

    private static final String[] SUBCOMMANDS = { "config", "chart" };

    public PowerGogglesCommand() {
        super("goggles", "pg");
    }

    @Override
    public String getCommandName() {
        return "powergoggles";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /powergoggles <subcommand>. Valid subcommands are: " + String.join(", ", SUBCOMMANDS) + ".";
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
        messages.add(new ChatComponentText(getCommandUsage(null)));
        messages.add(new ChatComponentText(" config - Opens power goggles configuration menu"));
        messages.add(new ChatComponentText(" chart - Toggles the power goggles chart"));
        messages.add(new ChatComponentText(""));
        messages.add(new ChatComponentText("Aliases: powergoggles, goggles, pg"));
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
            default -> {
                sendChatToPlayer(sender, RED + "Illegal subcommand: " + args[0]);
                sendHelpMessage(sender);
            }
        }
    }
}
