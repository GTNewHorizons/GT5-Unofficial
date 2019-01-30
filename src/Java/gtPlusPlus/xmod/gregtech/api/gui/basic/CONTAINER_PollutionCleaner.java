package gtPlusPlus.xmod.gregtech.api.gui.basic;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import gregtech.api.gui.*;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaAtmosphericReconditioner;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my Basic Machines
 */
public class CONTAINER_PollutionCleaner extends GT_Container_BasicTank {

    public boolean mFluidTransfer = false, mItemTransfer = false, mStuttering = false;
    public int mReduction = 0;

    public CONTAINER_PollutionCleaner(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 8, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 26, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Render(mTileEntity, 2, 107, 63));

        int tStartIndex = ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).getInputSlot();

        switch (((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mInputSlotCount) {
            case 2: //Pollution Scrubber
            	//Add 2 Item Slots
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 107, 25));            	
                break;            
            default:
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 7));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 25));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 17, 43));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 35, 43));
                addSlotToContainer(new Slot(mTileEntity, tStartIndex++, 53, 43));
                break;
        }

        tStartIndex = ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).getOutputSlot();

        switch (((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mOutputItems.length) {
            case 0:
                break;
            default:
                break;
        }

        addSlotToContainer(new Slot(mTileEntity, 1, 80, 63));
        addSlotToContainer(new Slot(mTileEntity, 3, 125, 63));
        //addSlotToContainer(new GT_Slot_Render(mTileEntity, tStartIndex++, 53, 63));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        switch (aSlotIndex) {
            case 0:
                if (mTileEntity.getMetaTileEntity() == null) return null;
                ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mFluidTransfer = !((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mFluidTransfer;
                return null;
            case 1:
                if (mTileEntity.getMetaTileEntity() == null) return null;
                ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mItemTransfer = !((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mItemTransfer;
                return null;
            default:
                return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) return;

        mFluidTransfer = ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mFluidTransfer;
        mItemTransfer = ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mItemTransfer;
        mStuttering = ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mStuttering;
        mReduction = ((GregtechMetaAtmosphericReconditioner) mTileEntity.getMetaTileEntity()).mPollutionReduction;

        Iterator var2 = this.crafters.iterator();
        while (var2.hasNext()) {
            ICrafting var1 = (ICrafting) var2.next();
            var1.sendProgressBarUpdate(this, 102, mFluidTransfer ? 1 : 0);
            var1.sendProgressBarUpdate(this, 103, mItemTransfer ? 1 : 0);
            var1.sendProgressBarUpdate(this, 104, mStuttering ? 1 : 0);
            var1.sendProgressBarUpdate(this, 105, mReduction);
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 102:
                mFluidTransfer = (par2 != 0);
                break;
            case 103:
                mItemTransfer = (par2 != 0);
                break;
            case 104:
                mStuttering = (par2 != 0);
                break;
            case 105:
            	mReduction = (par2);
                break;
        }
    }

    @Override
    public int getSlotStartIndex() {
        return 3;
    }

    @Override
    public int getShiftClickStartIndex() {
        return 3;
    }

    @Override
    public int getSlotCount() {
        return getShiftClickSlotCount() + ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mOutputItems.length + 2;
    }

    @Override
    public int getShiftClickSlotCount() {
        return ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mInputSlotCount;
    }
}
