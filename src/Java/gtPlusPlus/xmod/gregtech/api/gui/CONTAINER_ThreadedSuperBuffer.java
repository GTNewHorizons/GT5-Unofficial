package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.util.GT_Utility;

import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ThreadedBuffer;
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
	
	protected int cacheTime = 0;
	
	public CONTAINER_ThreadedSuperBuffer(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
		cacheTime = ((GT_MetaTileEntity_ThreadedBuffer)aTileEntity.getMetaTileEntity()).mThreadTimeLeft;
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
				((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity
						.getMetaTileEntity()).bOutput = !((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity
								.getMetaTileEntity()).bOutput;
				if (((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity.getMetaTileEntity()).bOutput) {
					GT_Utility.sendChatToPlayer(aPlayer, "Emit Energy to Outputside");
				} else {
					GT_Utility.sendChatToPlayer(aPlayer, "Don't emit Energy");
				}
				return null;
			}
			if (aSlotIndex == 1) {
				((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity
						.getMetaTileEntity()).bRedstoneIfFull = !((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity
								.getMetaTileEntity()).bRedstoneIfFull;
				if (((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull) {
					GT_Utility.sendChatToPlayer(aPlayer, "Emit Redstone if no Slot is free");
				} else {
					GT_Utility.sendChatToPlayer(aPlayer, "Don't emit Redstone");
				}
				return null;
			}
			if (aSlotIndex == 2) {
				((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity
						.getMetaTileEntity()).bInvert = !((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity
								.getMetaTileEntity()).bInvert;
				if (((GT_MetaTileEntity_ThreadedChestBuffer) this.mTileEntity.getMetaTileEntity()).bInvert) {
					GT_Utility.sendChatToPlayer(aPlayer, "Invert Redstone");
				} else {
					GT_Utility.sendChatToPlayer(aPlayer, "Don't invert Redstone");
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