package gtPlusPlus.xmod.gregtech.api.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.core.slots.SlotNoInput;

import net.minecraft.entity.player.InventoryPlayer;

public class CONTAINER_PowerSubStation extends GT_Container_MultiMachine {
	public CONTAINER_PowerSubStation(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public CONTAINER_PowerSubStation(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity,
			final boolean bindInventory) {
		super(aInventoryPlayer, aTileEntity, bindInventory);
	}

	public void addSlots(final InventoryPlayer aInventoryPlayer) {
		this.addSlotToContainer(new Slot((IInventory) this.mTileEntity, 1, 155, 5));
		this.addSlotToContainer(new SlotNoInput((IInventory) this.mTileEntity, 2, 155, 23));
	}

	public int getSlotCount() {
		return 2;
	}

	public int getShiftClickSlotCount() {
		return 1;
	}
}