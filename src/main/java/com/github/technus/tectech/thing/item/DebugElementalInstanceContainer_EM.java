package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.iElementalInstanceContainer;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.tElementalException;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.bTransformationInfo;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;

/**
 * Created by Tec on 15.03.2017.
 */
public final class DebugElementalInstanceContainer_EM extends Item {
    public static DebugElementalInstanceContainer_EM INSTANCE;

    private DebugElementalInstanceContainer_EM() {
        setMaxStackSize(1);
        setUnlocalizedName("em.debugContainer");
        setTextureName(MODID + ":itemDebugContainer");
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aPlayer instanceof EntityPlayerMP) {
            aStack.stackSize = 1;
            if (tTileEntity instanceof IGregTechTileEntity) {
                IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                if (metaTE instanceof iElementalInstanceContainer) {
                    cElementalInstanceStackMap content = ((iElementalInstanceContainer) metaTE).getContainerHandler();
                    if (tNBT.hasKey("content")) {
                        try {
                            content.putUnifyAll(cElementalInstanceStackMap.fromNBT(tNBT.getCompoundTag("content")));
                        } catch (tElementalException e) {
                            if (DEBUG_MODE) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                        ((iElementalInstanceContainer) metaTE).purgeOverflow();
                        tNBT.removeTag("content");
                        tNBT.removeTag("info");
                    } else if (content.hasStacks()) {
                        ((iElementalInstanceContainer) metaTE).purgeOverflow();
                        tNBT.setTag("info", content.getInfoNBT());
                        tNBT.setTag("content", content.toNBT());
                        content.clear();
                    }
                    return true;
                }
            }
        }
        return aPlayer instanceof EntityPlayerMP;
    }

    public ItemStack setContent(ItemStack aStack,cElementalInstanceStackMap content){
        NBTTagCompound tNBT = aStack.getTagCompound();
        if(tNBT==null){
            aStack.setTagCompound(new NBTTagCompound());
        }
        if (tNBT.hasKey("content")) {
            try {
                content.putUnifyAll(cElementalInstanceStackMap.fromNBT(tNBT.getCompoundTag("content")));
            } catch (tElementalException e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
                return aStack;
            }
            tNBT.removeTag("content");
            tNBT.removeTag("info");
        } else if (content.hasStacks()) {
            tNBT.setTag("info", content.getInfoNBT());
            tNBT.setTag("content", content.toNBT());
            content.clear();
        }
        return aStack;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_EM);
        try {
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (tNBT != null && tNBT.hasKey("info")) {
                aList.add("Contains:");
                Collections.addAll(aList, Util.infoFromNBT(tNBT.getCompoundTag("info")));
            } else {
                aList.add("Container for elemental matter");
                aList.add(EnumChatFormatting.BLUE + "Right click on elemental hatches");
            }
        } catch (Exception e) {
            aList.add("---Unexpected Termination---");
        }
    }

    public static void run() {
        INSTANCE = new DebugElementalInstanceContainer_EM();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack that = new ItemStack(this, 1);
        that.setTagCompound(new NBTTagCompound());
        list.add(that);
        for(iElementalDefinition defintion:bTransformationInfo.stacksRegistered){
            list.add(setContent(new ItemStack(this).setStackDisplayName(defintion.getName()+" x"+1),new cElementalInstanceStackMap(new cElementalInstanceStack(defintion,1))));
            list.add(setContent(new ItemStack(this).setStackDisplayName(defintion.getName()+" x"+144),new cElementalInstanceStackMap(new cElementalInstanceStack(defintion,144))));
            list.add(setContent(new ItemStack(this).setStackDisplayName(defintion.getName()+" x"+1000),new cElementalInstanceStackMap(new cElementalInstanceStack(defintion,1000))));
        }
    }
}
