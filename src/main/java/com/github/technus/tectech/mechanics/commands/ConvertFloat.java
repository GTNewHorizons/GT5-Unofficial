package com.github.technus.tectech.mechanics.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.util.TT_Utility;

public class ConvertFloat implements ICommand {

    ArrayList<String> aliases = new ArrayList<>();

    public ConvertFloat() {
        aliases.add("convert_float");
        aliases.add("c_f");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!sender.getEntityWorld().isRemote) {
            if (args.length == 1) {
                try {
                    float value = Float.parseFloat(args[0]);
                    sender.addChatMessage(
                            new ChatComponentText(
                                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                                            + TT_Utility.intBitsToShortString(Float.floatToIntBits(value))
                                            + " "
                                            + EnumChatFormatting.RESET
                                            + EnumChatFormatting.BLUE
                                            + value));
                } catch (Exception e) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Invalid Float " + args[0]));
                }
            } else {
                sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            }
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public String getCommandName() {
        return aliases.get(0);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "c_f Float";
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof ICommand) {
            return getCommandName().compareTo(((ICommand) o).getCommandName());
        }
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
