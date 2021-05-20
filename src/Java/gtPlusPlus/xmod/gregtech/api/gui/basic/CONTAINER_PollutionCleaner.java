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
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.slots.SlotPollutionScrubber;
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

		int tStartIndex = ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).getInputSlot();
		int aTier = ((GT_MetaTileEntity_BasicMachine) mTileEntity.getMetaTileEntity()).mTier;


		//Add 2 Item Slots
		addSlotToContainer(new SlotPollutionScrubber(0, aTier, mTileEntity, tStartIndex++, 53, 25));
		addSlotToContainer(new SlotPollutionScrubber(1, aTier, mTileEntity, tStartIndex++, 107, 25));   
		// Upgrade Slot
		addSlotToContainer(new SlotPollutionScrubber(2, aTier, mTileEntity, tStartIndex++, 125, 63));           	   

	}

	@Override
	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
		Logger.INFO("Clicked on slot "+aSlotIndex);
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) return;

		mReduction = ((GregtechMetaAtmosphericReconditioner) mTileEntity.getMetaTileEntity()).mPollutionReduction;

		Iterator var2 = this.crafters.iterator();
		while (var2.hasNext()) {
			ICrafting var1 = (ICrafting) var2.next();
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
			case 105:
				mReduction = (par2);
				break;
		}
	}

	@Override
	public int getSlotStartIndex() {
		return 0;
	}

	@Override
	public int getShiftClickStartIndex() {
		return 0;
	}

	@Override
	public int getSlotCount() {
		return getShiftClickSlotCount();
	}

	@Override
	public int getShiftClickSlotCount() {
		return 3;
	}
}
