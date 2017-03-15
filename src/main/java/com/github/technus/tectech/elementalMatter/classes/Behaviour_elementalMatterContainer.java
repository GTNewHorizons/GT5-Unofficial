package com.github.technus.tectech.elementalMatter.classes;


import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.commonValues;
import com.github.technus.tectech.elementalMatter.interfaces.iElementalInstanceContainer;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.items.behaviors.Behaviour_None;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

/**
 * Created by danie_000 on 11.12.2016.
 * used in custom GT build! debug purposes
 */
public final class Behaviour_elementalMatterContainer extends Behaviour_None {
    public static final IItemBehaviour<GT_MetaBase_Item> INSTANCE = new Behaviour_elementalMatterContainer();
    private final String mTooltip = GT_LanguageManager.addStringLocalization("gt.behaviour.emc", "Container for elemental matter");

    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aPlayer instanceof EntityPlayerMP) {
            aStack.stackSize = 1;
            if (tTileEntity != null && tTileEntity instanceof IGregTechTileEntity) {
                IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                if (metaTE != null && metaTE instanceof iElementalInstanceContainer) {
                    cElementalInstanceStackTree content = ((iElementalInstanceContainer) metaTE).getContainerHandler();
                    if (tNBT.hasKey("content")) {
                        try {
                            content.putUnifyAll(cElementalInstanceStackTree.fromNBT(tNBT.getCompoundTag("content")));
                        } catch (tElementalException e) {
                            if (TecTech.ModConfig.DEBUG_MODE) e.printStackTrace();
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
                }
                return true;
            }
        }
        return aPlayer instanceof EntityPlayerMP;
    }

    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        aList.add(commonValues.tecMark);
        aList.add(mTooltip);
        try {
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (tNBT.hasKey("info")) {
                aList.add("Contains:");
                Collections.addAll(aList, cElementalInstanceStackTree.infoFromNBT(tNBT.getCompoundTag("info")));
            }
        } catch (Exception e) {
            aList.add("---Unexpected Termination---");
        }
        return aList;
    }
}
