package com.github.technus.tectech.mechanics.elementalMatter.core.commands;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMDefinitionStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.EMComplex;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.EMPrimitive;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.IEMDefinition;
import com.github.technus.tectech.thing.item.DebugElementalInstanceContainer_EM;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition.nbtE__;

/**
 * Created by danie_000 on 30.12.2017.
 */
public class EMGive implements ICommand {
    ArrayList<String> aliases=new ArrayList<>();

    public EMGive(){
        aliases.add("em_give");
        aliases.add("give_em");
        aliases.add("gib_em");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP && !sender.getEntityWorld().isRemote) {
            if(args.length < 3) {
                sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            }else{
                TecTech.LOGGER.info("Spawninig EM for "+((EntityPlayerMP) sender).getDisplayName()+" - "+Arrays.toString(args));

                ArrayList<String> list = new ArrayList<>(Arrays.asList(args));
                String energy=list.remove(0);

                EMDefinitionStack def = getDefinitionStack(list);
                if(def!=null) {
                    EMInstanceStack instanceStack = new EMInstanceStack(def, 1D, 0D, Long.parseLong(energy));

                    sender.addChatMessage(new ChatComponentText(instanceStack.getDefinition().getSymbol() + " - " + instanceStack.getDefinition().getLocalizedName()));

                    EMInstanceStackMap instanceMap = new EMInstanceStackMap(instanceStack);

                    ItemStack itemStack = new ItemStack(DebugElementalInstanceContainer_EM.INSTANCE);
                    NBTTagCompound contents = new NBTTagCompound();
                    contents.setTag("info", instanceMap.getInfoNBT());
                    contents.setTag("content", instanceMap.toNBT());
                    itemStack.setTagCompound(contents);

                    ((EntityPlayerMP) sender).inventory.addItemStackToInventory(itemStack);
                }
            }
        }
    }

    private EMDefinitionStack getDefinitionStack(ArrayList<String> args){
        if(args.get(0).equals("<")){
            args.remove(0);
            return null;
        }
        double amount=Double.parseDouble(args.remove(0));
        try{
            int id=Integer.parseInt(args.get(0));
            args.remove(0);
            IEMDefinition primitive = EMPrimitive.getBindsPrimitive().get(id);
            return new EMDefinitionStack(primitive,amount);
        }catch (NumberFormatException e){
            byte clazz = (byte) args.remove(0).charAt(0);
            Method constructor = EMComplex.getBindsComplex().get(clazz);

            EMDefinitionStackMap stacks =new EMDefinitionStackMap();
            while(args.size()>0){
                EMDefinitionStack tempStack =getDefinitionStack(args);
                if(tempStack==null) {
                    break;
                }else {
                    stacks.putUnifyExact(tempStack);
                }
            }

            try {
                return ((IEMDefinition) constructor.invoke(null, stacks.toNBT())).getStackForm(amount);
            } catch (Exception e1) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
                return nbtE__.getStackForm(amount);
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
        if(args.length==2){
            return completionsForClassOrID();
        }
        return null;
    }

    private List<String> completionsForClassOrID(){
        ArrayList<String> strings=new ArrayList<>(8);
        Map<Byte,Method> binds= EMComplex.getBindsComplex();
        for (Map.Entry<Byte,Method> e:binds.entrySet()) {
            strings.add(String.valueOf((char)e.getKey().byteValue()));
        }
        Map<Integer, EMPrimitive> bindsBO = EMPrimitive.getBindsPrimitive();
        for (Map.Entry<Integer, EMPrimitive> e:bindsBO.entrySet()) {
            strings.add(String.valueOf(e.getKey().byteValue()));
        }
        return strings;
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "em_give Energy Count ClassOrId (Count ClassOrId ... <)";
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
