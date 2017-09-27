package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.elementalMatter.core.cElementalDefinitionStackMap;
import com.github.technus.tectech.elementalMatter.core.tElementalException;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;

import static com.github.technus.tectech.auxiliary.Reference.MODID;
import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;

/**
 * Created by Tec on 15.03.2017.
 */
public class DefinitionContainer_EM extends Item {
    public static DefinitionContainer_EM INSTANCE;

    private DefinitionContainer_EM() {
        super();
        setMaxStackSize(1);
        setUnlocalizedName("em.definitionContainer");
        setTextureName(MODID + ":itemDefinitionContainer");
    }

    //return previous thing
    public static cElementalDefinitionStackMap setContent(ItemStack containerItem, cElementalDefinitionStackMap definitions){
        if(containerItem.getItem() instanceof DefinitionContainer_EM) {
            NBTTagCompound tNBT = containerItem.stackTagCompound;
            if (tNBT == null) tNBT=containerItem.stackTagCompound=new NBTTagCompound();

            cElementalDefinitionStackMap oldMap=null;
            if (tNBT.hasKey("content")) {
                try {
                    oldMap=cElementalDefinitionStackMap.fromNBT(tNBT.getCompoundTag("content"));
                } catch (tElementalException e) {
                    if (DEBUG_MODE) e.printStackTrace();
                }
            }
            tNBT.setTag("info", definitions.getInfoNBT());
            tNBT.setTag("content", definitions.toNBT());
            return oldMap;
        }
        return null;
    }

    public static cElementalDefinitionStackMap getContent(ItemStack containerItem){
        if(containerItem.getItem() instanceof DefinitionContainer_EM){
            NBTTagCompound tNBT = containerItem.stackTagCompound;

            if (tNBT == null || !tNBT.hasKey("content")) return null;
            try {
                return cElementalDefinitionStackMap.fromNBT(tNBT.getCompoundTag("content"));
            } catch (tElementalException e) {
                if (DEBUG_MODE) e.printStackTrace();
            }
        }
        return null;
    }

    public static cElementalDefinitionStackMap clearContent(ItemStack containerItem){
        if(containerItem.getItem() instanceof DefinitionContainer_EM){
            NBTTagCompound tNBT = containerItem.stackTagCompound;
            if (tNBT == null) return null;

            cElementalDefinitionStackMap oldMap=null;
            if (tNBT.hasKey("content")) {
                try {
                    oldMap=cElementalDefinitionStackMap.fromNBT(tNBT.getCompoundTag("content"));
                } catch (tElementalException e) {
                    if (DEBUG_MODE) e.printStackTrace();
                }
            }
            tNBT.removeTag("info");
            tNBT.removeTag("content");
            return oldMap;
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_EM);
        try {
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (tNBT != null && tNBT.hasKey("info")) {
                aList.add("Should Contain:");
                Collections.addAll(aList, cElementalDefinitionStackMap.infoFromNBT(tNBT.getCompoundTag("info")));
            } else {
                aList.add("Recipe Hint");
            }
        } catch (Exception e) {
            aList.add("---Unexpected Termination---");
        }
    }

    public static void run() {
        INSTANCE = new DefinitionContainer_EM();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack that = new ItemStack(this, 1);
        that.setTagCompound(new NBTTagCompound());
        list.add(that);
    }
}
