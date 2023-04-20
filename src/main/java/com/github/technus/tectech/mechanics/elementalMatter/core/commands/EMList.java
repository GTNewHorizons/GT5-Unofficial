package com.github.technus.tectech.mechanics.elementalMatter.core.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMType;
import com.github.technus.tectech.util.TT_Utility;

/**
 * Created by danie_000 on 30.12.2017.
 */
public class EMList implements ICommand {

    ArrayList<String> aliases = new ArrayList<>();

    public EMList() {
        aliases.add("em_list");
        aliases.add("list_em");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!sender.getEntityWorld().isRemote) {
            if (args.length == 0) {
                listClasses(sender);
            } else {
                String concated = TT_Utility.getConcated(args, " ");
                listDefinitions(sender, concated, concated.replaceAll(" ", "_"), concated.replaceAll("_", " "));
            }
        }
    }

    private void listDefinitions(ICommandSender sender, String raw, String unlocalized, String localized) {
        sender.addChatMessage(new ChatComponentText("    Available Direct: tag - name symbol"));
        for (EMType directType : TecTech.definitionsRegistry.getDirectTypes().values()) {
            if ("*".equals(raw) || localized.equalsIgnoreCase(directType.getLocalizedName())
                    || unlocalized.equalsIgnoreCase(directType.getUnlocalizedName())) {
                directType.getDefinitions().forEach(
                        (bind, definition) -> sender.addChatMessage(
                                new ChatComponentText(
                                        bind + " - " + definition.getLocalizedName() + " " + definition.getSymbol())));
            }
        }
    }

    private void listClasses(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText("    Available Direct: name (use as parameter to learn more"));
        TecTech.definitionsRegistry.getDirectTypes().forEach(
                (aClass, emDirectType) -> sender
                        .addChatMessage(new ChatComponentText(emDirectType.getLocalizedName())));
        sender.addChatMessage(new ChatComponentText("    Available Indirect: tag - name"));
        TecTech.definitionsRegistry.getIndirectBinds().forEach(
                (bind, emIndirectType) -> sender
                        .addChatMessage(new ChatComponentText(bind + " - " + emIndirectType.getLocalizedName())));
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
        if (args.length == 1) {
            return TecTech.definitionsRegistry.getDirectTypes().values().stream().map(EMType::getLocalizedName)
                    .map(s -> s.replaceAll(" ", "_")).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "em_list (optional Direct Name or *)";
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
