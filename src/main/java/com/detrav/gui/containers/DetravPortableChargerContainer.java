package com.detrav.gui.containers;

import com.detrav.items.DetravMetaGeneratedTool01;
import com.detrav.net.DetravNetwork;
import com.detrav.net.DetravPortableChargerPacket01;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.items.armor.SlotLocked;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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

        if(GT_ModHandler.isElectricItem(item));

        //GT_ModHandler.ch

        for(int forTry = 0; forTry<4;forTry++){
            Long[] selfStats = DetravMetaGeneratedTool01.INSTANCE.getElectricStats(mItem);
            if (selfStats == null) return;

            long selfCharge = DetravMetaGeneratedTool01.INSTANCE.getRealCharge(mItem);
            if (selfCharge <= 0) return;

            long loss = DetravMetaGeneratedTool01.INSTANCE.getElectricStatsLoss(mItem);
            if (loss < 0) return;

            long energyToTransfer = (selfStats[1] - loss) * aTimer;

            int removeEnergy = GT_ModHandler.chargeElectricItem(item, (int) energyToTransfer, Integer.MAX_VALUE, false, false);
            if (removeEnergy == 0) return;
            GT_ModHandler.dischargeElectricItem(mItem, (int) (removeEnergy + loss * aTimer), Integer.MAX_VALUE, false, false, true);

            if (aWorld.isRemote) return;
            if (aPlayer instanceof EntityPlayerMP) {
                DetravPortableChargerPacket01 packet = new DetravPortableChargerPacket01();
                packet.charge = selfCharge - removeEnergy + loss * aTimer;
                DetravNetwork.INSTANCE.sendToPlayer(packet, (EntityPlayerMP) aPlayer);

                //crafters
            }
        };
        //long max = DetravMetaGeneratedTool01.getToolMaxDamage(mItem);
        //double p = ((double)selfCharge)/((double)selfStats[0]);
        //DetravMetaGeneratedTool01.setToolDamage(mItem,(long) (max * p) + 200);
        //long
    }

    /*@Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack result = super.transferStackInSlot(par1EntityPlayer,par2);
        par1EntityPlayer.addChatMessage(new ChatComponentText(result.getDisplayName()));

        return result;
    }*/
}