package com.detrav.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Detrav on 30.10.2016.
 */
public class DetravPortableAnvilContainer extends Container {
    public DetravPortableAnvilContainer(InventoryPlayer inventory, World world, ItemStack currentEquippedItem) {

    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
