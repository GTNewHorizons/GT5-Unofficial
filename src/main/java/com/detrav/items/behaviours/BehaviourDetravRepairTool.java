package com.detrav.items.behaviours;

import com.detrav.DetravScannerMod;
import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.common.items.behaviors.Behaviour_None;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by wital_000 on 08.04.2016.
 */
public class BehaviourDetravRepairTool extends Behaviour_None {
    public ItemStack onItemRightClick(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if(DetravMetaGeneratedTool01.getToolDamage(aStack)< DetravMetaGeneratedTool01.INSTANCE.getToolMaxDamage(aStack)-1) {
            DetravScannerMod.proxy.openRepairToolGui(aPlayer);
        } else DetravMetaGeneratedTool01.INSTANCE.doDamage(aStack,1000);

        return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
        //aPlayer.openContainer
    }
}
