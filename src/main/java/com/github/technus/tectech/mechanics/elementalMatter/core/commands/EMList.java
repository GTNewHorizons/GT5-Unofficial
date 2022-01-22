package com.github.technus.tectech.mechanics.elementalMatter.core.commands;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMType;
import com.github.technus.tectech.util.Util;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by danie_000 on 30.12.2017.
 */
public class EMList implements ICommand {
    ArrayList<String> aliases=new ArrayList<>();

    public EMList(){
        aliases.add("em_list");
        aliases.add("list_em");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!sender.getEntityWorld().isRemote) {
            if(args.length == 0) {
                listClasses(sender);
            }else {
                listDefinitions(sender, Util.getConcated(args," "));
            }
        }
    }

    private void listDefinitions(ICommandSender sender, String arg) {
        sender.addChatMessage(new ChatComponentText("    Available Direct: tag - name symbol"));
        for (EMType directType : TecTech.definitionsRegistry.getDirectTypes().values()) {
            if ("*".equals(arg) || arg.equalsIgnoreCase(directType.getLocalizedName())) {
                directType.getDefinitions().forEach((bind, definition) ->
                        sender.addChatMessage(new ChatComponentText(bind + " - " + definition.getLocalizedName() + " " + definition.getSymbol())));
            }
        }
    }

    private void listClasses(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText("    Available Direct: name (use name as parameter to learn more"));
        TecTech.definitionsRegistry.getDirectTypes().forEach((aClass, emDirectType) ->
                sender.addChatMessage(new ChatComponentText(emDirectType.getLocalizedName())));
        sender.addChatMessage(new ChatComponentText("    Available Indirect: tag - name"));
        TecTech.definitionsRegistry.getIndirectBinds().forEach((bind, emIndirectType) ->
                sender.addChatMessage(new ChatComponentText(bind+" - "+emIndirectType.getLocalizedName())));
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
        if(args.length==0){
            return TecTech.definitionsRegistry.getDirectTypes().values().stream().map(EMType::getLocalizedName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "em_list (optional Direct Name or *)";
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
