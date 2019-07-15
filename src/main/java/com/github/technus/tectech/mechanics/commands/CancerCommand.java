package com.github.technus.tectech.mechanics.commands;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.data.PlayerDataMessage;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

import static com.github.technus.tectech.mechanics.anomaly.AnomalyHandler.SPACE_CANCER;

public class CancerCommand implements ICommand {
    ArrayList<String> aliases=new ArrayList<>();

    public CancerCommand(){
        aliases.add("cancer");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP && !sender.getEntityWorld().isRemote) {
            double amount;
            try {
                amount = Double.parseDouble(args[0]);
            }catch (NumberFormatException e){
                sender.addChatMessage(new ChatComponentText("Cannot parse amount!"));
                return;
            }
            if(amount<0||amount>2){
                sender.addChatMessage(new ChatComponentText("Invalid amount provided!"));
                return;
            }
            EntityPlayerMP player=(EntityPlayerMP)sender;
            NBTTagCompound playerTag = TecTech.playerPersistence.getDataOrSetToNewTag(player);
            if(player.capabilities.isCreativeMode){
                sender.addChatMessage(new ChatComponentText("Doesn't really work in creative mode!"));
            }else {
                playerTag.setDouble(SPACE_CANCER, amount);
                TecTech.playerPersistence.saveData(player);
                NetworkDispatcher.INSTANCE.sendTo(new PlayerDataMessage.PlayerDataData(player), player);
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
        return "cancer [Amount 0.0-2.0]";
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
