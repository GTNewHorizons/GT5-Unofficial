package com.detrav.gui.containers;

import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.common.items.armor.SlotLocked;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.lwjgl.Sys;

/**
 * Created by wital_000 on 07.04.2016.
 */
public class DetravPortableChargerContainer extends Container {
    public IInventory slots = new InventoryCrafting(this, 1, 1);
    public ItemStack mItem;
    private World worldObj;

    public DetravPortableChargerContainer(InventoryPlayer inventoryPlayer, World aWorld,ItemStack aStack) {
        this.worldObj = aWorld;
        this.addSlotToContainer(new Slot(slots, 0, 80, 35));
        mItem = aStack;
        //new Slot()
        bindPlayerInventory(inventoryPlayer);

        //DetravMetaGeneratedTool01.INSTANCE.getToolStats(mItem).
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                        8 + j * 18, 84 + i * 18));
            }
        }


        for (int i = 0; i < 9; i++) {
            ItemStack stackInSlot = inventoryPlayer.getStackInSlot(i);
            if (mItem!=null && stackInSlot!=null && mItem == stackInSlot) {
                addSlotToContainer(new SlotLocked(inventoryPlayer, i, 8 + i * 18, 142));
            } else {
                addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        if (!this.worldObj.isRemote) {
            //for (int i = 0; i < 9; ++i)
            //{
            ItemStack itemstack = this.slots.getStackInSlotOnClosing(0);

            if (itemstack != null) {
                p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
            }
            //}
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        return null;
    }

    public void onUpdate(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, Entity aPlayer, int aTimer) {
        if(mItem==null) return;

        ItemStack item = this.slots.getStackInSlot(0);
        if(item == null) return;

        Long[] itemStats = DetravMetaGeneratedTool01.INSTANCE.getElectricStats(item);
        if(itemStats == null) return;
        long itemCharge = DetravMetaGeneratedTool01.INSTANCE.getRealCharge(item);
        if(itemCharge <=0) return;
        long needEnergy = itemStats[1] - itemCharge;
        if(needEnergy == 0) return;

        Long[] selfStats = DetravMetaGeneratedTool01.INSTANCE.getElectricStats(mItem);
        if(selfStats == null) return;

        long selfCharge = DetravMetaGeneratedTool01.INSTANCE.getRealCharge(mItem);
        if(selfCharge <=0) return;

        long loss = DetravMetaGeneratedTool01.INSTANCE.getElectricStatsLoss(mItem);
        if(loss<0) return;

        long energyToTransfer = (Math.min(selfStats[1],itemStats[1]) - loss)*aTimer ;

        if(needEnergy<energyToTransfer)
            energyToTransfer = needEnergy;
        long removeEnergy = Math.min(energyToTransfer+loss*aTimer,selfCharge);

        DetravMetaGeneratedTool01.INSTANCE.setCharge(item,itemCharge+energyToTransfer);
        DetravMetaGeneratedTool01.INSTANCE.setCharge(mItem,selfCharge-removeEnergy);
    }

    /*@Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack result = super.transferStackInSlot(par1EntityPlayer,par2);
        par1EntityPlayer.addChatMessage(new ChatComponentText(result.getDisplayName()));

        return result;
    }*/
}