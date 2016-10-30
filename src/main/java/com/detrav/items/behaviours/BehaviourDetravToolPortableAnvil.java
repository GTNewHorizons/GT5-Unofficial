package com.detrav.items.behaviours;

import com.detrav.DetravScannerMod;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.common.items.behaviors.Behaviour_None;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Detrav on 30.10.2016.
 */
public class BehaviourDetravToolPortableAnvil extends Behaviour_None {

    public ItemStack onItemRightClick(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        DetravScannerMod.proxy.openPortableAnvilGui(aPlayer);
        return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
        //aPlayer.openContainer
    }
}
