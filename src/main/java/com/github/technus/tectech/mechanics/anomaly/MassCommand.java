package com.github.technus.tectech.mechanics.anomaly;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import com.github.technus.tectech.TecTech;

public class MassCommand implements ICommand {

    ArrayList<String> aliases = new ArrayList<>();

    public MassCommand() {
        aliases.add("mass_EM");
        aliases.add("mass");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP && !sender.getEntityWorld().isRemote) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (args == null || args.length == 0) {
                sender.addChatMessage(new ChatComponentText("Msdd amount: " + TecTech.anomalyHandler.getMass(player)));
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[0]);
            } catch (NumberFormatException e) {
                sender.addChatMessage(new ChatComponentText("Cannot parse amount!"));
                return;
            }
            if (player.capabilities.isCreativeMode) {
                sender.addChatMessage(new ChatComponentText("Doesn't really work in creative mode!"));
            } else {
                TecTech.anomalyHandler.setMass(player, amount);
                sender.addChatMessage(new ChatComponentText("Mass set to: " + amount));
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
        return "mass_EM [Amount]";
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
