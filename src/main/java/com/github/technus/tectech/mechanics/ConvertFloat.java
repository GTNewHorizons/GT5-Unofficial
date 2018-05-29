package com.github.technus.tectech.mechanics;

import com.github.technus.tectech.Util;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class ConvertFloat implements ICommand {
    ArrayList<String> aliases=new ArrayList<>();

    public ConvertFloat(){
        aliases.add("convert_float");
        aliases.add("c_f");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!sender.getEntityWorld().isRemote) {
            if(args.length == 1) {
                try{
                    float value=Float.parseFloat(args[0]);
                    sender.addChatMessage(new ChatComponentText(
                            EnumChatFormatting.AQUA.toString()+ EnumChatFormatting.BOLD +
                                    Util.intBitsToShortString(Float.floatToIntBits(value))+" "+
                                    EnumChatFormatting.RESET+EnumChatFormatting.BLUE +value));
                }catch (Exception e){
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Invalid Float "+args[0]));
                }
            }else{
                sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            }
        }
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
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
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "c_f Float";
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof ICommand){
            return getCommandName().compareTo(((ICommand) o).getCommandName());
        }
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
