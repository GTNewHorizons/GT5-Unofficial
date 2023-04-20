package com.github.technus.tectech.mechanics.elementalMatter.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMIndirectType;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMDefinitionStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.thing.item.DebugElementalInstanceContainer_EM;

/**
 * Created by danie_000 on 30.12.2017.
 */
public class EMGive implements ICommand {

    ArrayList<String> aliases = new ArrayList<>();

    public EMGive() {
        aliases.add("em_give");
        aliases.add("give_em");
        aliases.add("gib_em");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP && !sender.getEntityWorld().isRemote) {
            if (args.length < 3) {
                sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            } else {
                TecTech.LOGGER.info(
                        "Spawninig EM for " + ((EntityPlayerMP) sender).getDisplayName()
                                + " - "
                                + Arrays.toString(args));

                ArrayList<String> list = new ArrayList<>(Arrays.asList(args));
                String energy = list.remove(0);

                EMDefinitionStack def = getDefinitionStack(TecTech.definitionsRegistry, list);
                if (def != null) {
                    EMInstanceStack instanceStack = new EMInstanceStack(def, 1, 0, Long.parseLong(energy));

                    sender.addChatMessage(
                            new ChatComponentText(
                                    instanceStack.getDefinition().getSymbol() + " - "
                                            + instanceStack.getDefinition().getLocalizedName()));

                    EMInstanceStackMap instanceMap = new EMInstanceStackMap(instanceStack);

                    ItemStack itemStack = new ItemStack(DebugElementalInstanceContainer_EM.INSTANCE);
                    DebugElementalInstanceContainer_EM.INSTANCE.setContent(itemStack, instanceMap);

                    ((EntityPlayerMP) sender).inventory.addItemStackToInventory(itemStack);
                }
            }
        }
    }

    private EMDefinitionStack getDefinitionStack(EMDefinitionsRegistry registry, ArrayList<String> args) {
        double amount;
        if (args.size() == 0) {
            return null;
        }
        String defTag = args.remove(0);
        if ("<".equals(defTag)) {
            return null;
        }
        try {
            amount = Double.parseDouble(defTag);
            if (args.size() == 0) {
                return null;
            }
            defTag = args.remove(0);
            if ("<".equals(defTag)) {
                return null;
            }
        } catch (Exception e) {
            amount = 1;
        }
        IEMDefinition definition = registry.getDirectBinds().get(defTag);
        if (definition != null) {
            return definition.getStackForm(amount);
        }
        EMIndirectType emIndirectType = registry.getIndirectBinds().get(defTag);
        if (emIndirectType != null) {
            EMDefinitionStackMap stacks = new EMDefinitionStackMap();
            while (args.size() > 0) {
                EMDefinitionStack definitionStack = getDefinitionStack(registry, args);
                if (definitionStack == null) {
                    break;
                } else {
                    stacks.putUnifyExact(definitionStack);
                }
            }
            return emIndirectType.create(registry, stacks.toNBT(registry)).getStackForm(amount);
        }
        return null;
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
        if (args.length > 2 && args.length % 2 == 1) {
            return new ArrayList<>(TecTech.definitionsRegistry.getBinds().keySet());
        }
        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "em_give Energy Count ClassOrId ( (Count ClassOrId ... <) ...<)";
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
