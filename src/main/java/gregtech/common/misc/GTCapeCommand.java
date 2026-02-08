package gregtech.common.misc;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import gregtech.api.enums.GTValues;
import gregtech.api.net.cape.GTPacketListCapes;
import gregtech.api.net.cape.GTPacketSetCape;
import gregtech.common.GTCapesLoader;
import gregtech.common.config.Client;

public class GTCapeCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "cape";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/cape <get|list|set>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "get", "list", "set");
        }
        if (args.length == 2 && args[0].equals("set")) {
            return getListOfStringsFromIterableMatchingLastWord(
                args,
                GTCapesLoader.getAvailableCapes(Minecraft.getMinecraft().thePlayer));
        }
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("Usage: " + this.getCommandUsage(sender)));
            return;
        }
        switch (args[0]) {
            case "get" -> sender
                .addChatMessage(new ChatComponentText("Current cape: " + Client.preference.selectedCape));
            case "list" -> GTValues.NW.sendToServer(new GTPacketListCapes());
            case "set" -> {
                if (args.length == 1) {
                    sender.addChatMessage(new ChatComponentText("Usage: /cape set <name>"));
                    return;
                }
                GTValues.NW.sendToServer(new GTPacketSetCape(args[1]));
            }
            default -> sender.addChatMessage(new ChatComponentText("Usage: " + this.getCommandUsage(sender)));
        }
    }
}
