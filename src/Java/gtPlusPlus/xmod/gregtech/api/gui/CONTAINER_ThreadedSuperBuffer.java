package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.automation.GT_MetaTileEntity_ChestBuffer;

import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_ThreadedChestBuffer;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;

public class CONTAINER_ThreadedSuperBuffer extends GT_ContainerMetaTile_Machine {
	public CONTAINER_ThreadedSuperBuffer(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public void addSlots(final InventoryPlayer aInventoryPlayer) {
		this.addSlotToContainer((Slot) new GT_Slot_Holo((IInventory) this.mTileEntity, 256, 8, 63, false, true, 1));
		this.addSlotToContainer((Slot) new GT_Slot_Holo((IInventory) this.mTileEntity, 256, 26, 63, false, true, 1));
		this.addSlotToContainer((Slot) new GT_Slot_Holo((IInventory) this.mTileEntity, 256, 44, 63, false, true, 1));
	}

	public ItemStack slotClick(final int aSlotIndex, final int aMouseclick, final int aShifthold,
			final EntityPlayer aPlayer) {
		if (aSlotIndex < 0) {
			return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
		}
		final Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
		if (tSlot != null) {
			if (this.mTileEntity.getMetaTileEntity() == null) {
				return null;
			}
			if (aSlotIndex == 0) {
				((GT_MetaTileEntity_ChestBuffer) this.mTileEntity
						.getMetaTileEntity()).bOutput = !((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity
								.getMetaTileEntity()).bOutput;
				if (((GT_MetaTileEntity_ChestBuffer) this.mTileEntity.getMetaTileEntity()).bOutput) {
					GT_Utility.sendChatToPlayer(aPlayer, this.trans("116", "Emit Energy to Outputside"));
				} else {
					GT_Utility.sendChatToPlayer(aPlayer, this.trans("117", "Don't emit Energy"));
				}
				return null;
			}
			if (aSlotIndex == 1) {
				((GT_MetaTileEntity_ChestBuffer) this.mTileEntity
						.getMetaTileEntity()).bRedstoneIfFull = !((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity
								.getMetaTileEntity()).bRedstoneIfFull;
				if (((GT_MetaTileEntity_ChestBuffer) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull) {
					GT_Utility.sendChatToPlayer(aPlayer, this.trans("118", "Emit Redstone if no Slot is free"));
				} else {
					GT_Utility.sendChatToPlayer(aPlayer, this.trans("119", "Don't emit Redstone"));
				}
				return null;
			}
			if (aSlotIndex == 2) {
				((GT_MetaTileEntity_ChestBuffer) this.mTileEntity
						.getMetaTileEntity()).bInvert = !((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity
								.getMetaTileEntity()).bInvert;
				if (((GT_MetaTileEntity_ChestBuffer) this.mTileEntity.getMetaTileEntity()).bInvert) {
					GT_Utility.sendChatToPlayer(aPlayer, this.trans("120", "Invert Redstone"));
				} else {
					GT_Utility.sendChatToPlayer(aPlayer, this.trans("121", "Don't invert Redstone"));
				}
				return null;
			}
		}
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}

	public int getSlotCount() {
		return 0;
	}

	public int getShiftClickSlotCount() {
		return 0;
	}
}