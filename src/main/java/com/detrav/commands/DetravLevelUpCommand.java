package com.detrav.commands;

import com.google.common.collect.HashMultimap;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Detrav on 26.03.2017.
 */
public class DetravLevelUpCommand implements ICommand {

    private List aliases;

    public  DetravLevelUpCommand()
    {
        this.aliases = new ArrayList<String>();
        this.aliases.add("DetravLevelUp");
        this.aliases.add("dlup");
    }

    @Override
    public String getCommandName() {
        return "DetravLevelUp";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "DetravLevelUp";
    }

    @Override
    public List getCommandAliases() {
        return aliases;
    }

    static int level = 0;

    static UUID id = UUID.randomUUID();

    @Override
    public void processCommand(ICommandSender player, String[] p_71515_2_) {
        if(player instanceof EntityPlayerMP)
        {

            level++;
            AttributeModifier mod = new AttributeModifier(id,"detravlevelup",level,0);
            ((EntityPlayerMP) player).getEntityAttribute(
                    SharedMonsterAttributes.maxHealth
            ).removeModifier(mod);
            ((EntityPlayerMP) player).getEntityAttribute(
                    SharedMonsterAttributes.maxHealth
            ).applyModifier(mod);



            /*BaseAttributeMap attrMap = ((EntityPlayerMP) player).getAttributeMap();
            HashMultimap map = HashMultimap.create();
            map.put()
            attrMap.applyAttributeModifiers(map);*/
        }
    }

    private void sendHelpMessage(ICommandSender sender)
    {
        sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
