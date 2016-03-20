package com.detrav.tools;

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
 * Created by wital_000 on 19.03.2016.
 */
public class BehaviourDetravToolElectricProPick extends BehaviourDetravToolProPick
{

    public BehaviourDetravToolElectricProPick(int aCosts) {
        super(aCosts);
    }
}