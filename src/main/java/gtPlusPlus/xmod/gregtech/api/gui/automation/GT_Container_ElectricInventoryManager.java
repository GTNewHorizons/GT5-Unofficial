package gtPlusPlus.xmod.gregtech.api.gui.automation;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_ElectricInventoryManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GT_Container_ElectricInventoryManager extends GT_ContainerMetaTile_Machine {

	public int[] mTargetDirections = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	public int[] mRangeDirections = new int[]{0, 0, 0, 0};
	public int mTargetInOut;
	public int mTargetEnergy;

	public GT_Container_ElectricInventoryManager(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public void addSlots(InventoryPlayer aInventoryPlayer) {
		addSlotToContainer(new Slot(mTileEntity, 0, 155, 5));
		addSlotToContainer(new Slot(mTileEntity, 1, 155, 23));
		addSlotToContainer(new Slot(mTileEntity, 2, 155, 41));

		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 3, 5, 5, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 4, 5, 23, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 5, 5, 41, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 6, 61, 5, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 7, 61, 23, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 8, 61, 41, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 9, 80, 5, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 10, 80, 23, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 11, 80, 41, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 12, 136, 5, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 13, 136, 23, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 14, 136, 41, false, true, 1));

		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 24, 5, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 24, 23, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 24, 41, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 42, 5, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 42, 23, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 42, 41, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 99, 5, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 99, 23, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 99, 41, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 117, 5, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 117, 23, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 117, 41, false, true, 1));

		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 24, 60, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 42, 60, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 99, 60, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 117, 60, false, true, 1));

		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 5, 60, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 61, 60, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 80, 60, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 15, 136, 60, false, true, 1));
	}

	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
		if (aSlotIndex < 3 || aSlotIndex >= getAllSlotCount())
			return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
		Slot tSlot = (Slot) inventorySlots.get(aSlotIndex);
		if (tSlot != null) {
			if (mTileEntity.getMetaTileEntity() == null)
				return null;
			if (aSlotIndex < 15) {
				ItemStack tStack = aPlayer.inventory.getItemStack();
				if (tStack != null) {
					tStack = GT_Utility.copy(tStack);
					if (aMouseclick != 0) {
						tStack.setItemDamage(OreDictionary.WILDCARD_VALUE);
					}
					tSlot.putStack(tStack);
				}
				else {
					if (tSlot.getStack() != null) {
						if (aMouseclick == 0) {
							tSlot.getStack().stackSize -= (aShifthold == 1 ? 8 : 1);
							if (tSlot.getStack().stackSize <= 0) {
								tSlot.putStack(null);
							}
						}
						else {
							tSlot.getStack().stackSize += (aShifthold == 1 ? 8 : 1);
							if (tSlot.getStack().stackSize > tSlot.getStack().getMaxStackSize()) {
								tSlot.getStack().stackSize = tSlot.getStack().getMaxStackSize();
							}
						}
					}
				}
				return null;
			}
			else if (aSlotIndex >= 27 && aSlotIndex <= 30) {
				((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).iterateRangeDirection(aSlotIndex - 27);
			}
			else if (aSlotIndex >= 31 && aSlotIndex <= 34) {
				((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).switchRangeEnergy(aSlotIndex - 31);
			}
			else if (aSlotIndex % 3 == 0) {
				if (aMouseclick != 0)
					((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).switchSlot1InOut((aSlotIndex - 15) / 3);
				else
					((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).iterateSlot1Direction((aSlotIndex - 15) / 3);
			}
			else if (aSlotIndex % 3 == 1) {
				if (aMouseclick != 0)
					((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).switchSlot2InOut((aSlotIndex - 16) / 3);
				else
					((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).iterateSlot2Direction((aSlotIndex - 16) / 3);
			}
			else if (aSlotIndex % 3 == 2) {
				if (aMouseclick != 0)
					((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).switchSlot3InOut((aSlotIndex - 17) / 3);
				else
					((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).iterateSlot3Direction((aSlotIndex - 17) / 3);
			}
		}
		return null;
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
			return;
		}
		mTargetDirections = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		mRangeDirections = new int[]{0, 0, 0, 0};

		mRangeDirections[0] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getRangeDirection(0);
		mRangeDirections[1] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getRangeDirection(1);
		mRangeDirections[2] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getRangeDirection(2);
		mRangeDirections[3] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getRangeDirection(3);

		mTargetDirections[0] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot1Direction(0);
		mTargetDirections[1] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot2Direction(0);
		mTargetDirections[2] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot3Direction(0);
		mTargetDirections[3] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot1Direction(1);
		mTargetDirections[4] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot2Direction(1);
		mTargetDirections[5] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot3Direction(1);
		mTargetDirections[6] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot1Direction(2);
		mTargetDirections[7] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot2Direction(2);
		mTargetDirections[8] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot3Direction(2);
		mTargetDirections[9] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot1Direction(3);
		mTargetDirections[10] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot2Direction(3);
		mTargetDirections[11] = ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot3Direction(3);

		mTargetInOut = 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot1InOut(0) ? 1 << 0 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot2InOut(0) ? 1 << 1 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot3InOut(0) ? 1 << 2 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot1InOut(1) ? 1 << 3 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot2InOut(1) ? 1 << 4 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot3InOut(1) ? 1 << 5 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot1InOut(2) ? 1 << 6 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot2InOut(2) ? 1 << 7 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot3InOut(2) ? 1 << 8 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot1InOut(3) ? 1 << 9 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot2InOut(3) ? 1 << 10 : 0;
		mTargetInOut |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getSlot3InOut(3) ? 1 << 11 : 0;

		mTargetEnergy = 0;
		mTargetEnergy |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getRangeEnergy(0) ? 1 << 0 : 0;
		mTargetEnergy |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getRangeEnergy(1) ? 1 << 1 : 0;
		mTargetEnergy |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getRangeEnergy(2) ? 1 << 2 : 0;
		mTargetEnergy |= ((GT_MetaTileEntity_ElectricInventoryManager) mTileEntity.getMetaTileEntity()).getRangeEnergy(3) ? 1 << 3 : 0;

		Iterator var2 = this.crafters.iterator();
		while (var2.hasNext()) {
			ICrafting var1 = (ICrafting) var2.next();
			for (int i = 0; i < 12; i++)
				var1.sendProgressBarUpdate(this, 100 + i, mTargetDirections[i]);
			var1.sendProgressBarUpdate(this, 113, mTargetInOut);
			var1.sendProgressBarUpdate(this, 114, mTargetEnergy);
			for (int i = 0; i < 4; i++)
				var1.sendProgressBarUpdate(this, 115 + i, mRangeDirections[i]);
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
			case 100 :
				mTargetDirections[0] = par2;
				break;
			case 101 :
				mTargetDirections[1] = par2;
				break;
			case 102 :
				mTargetDirections[2] = par2;
				break;
			case 103 :
				mTargetDirections[3] = par2;
				break;
			case 104 :
				mTargetDirections[4] = par2;
				break;
			case 105 :
				mTargetDirections[5] = par2;
				break;
			case 106 :
				mTargetDirections[6] = par2;
				break;
			case 107 :
				mTargetDirections[7] = par2;
				break;
			case 108 :
				mTargetDirections[8] = par2;
				break;
			case 109 :
				mTargetDirections[9] = par2;
				break;
			case 110 :
				mTargetDirections[10] = par2;
				break;
			case 111 :
				mTargetDirections[11] = par2;
				break;

			case 113 :
				mTargetInOut = par2;
				break;
			case 114 :
				mTargetEnergy = par2;
				break;
			case 115 :
				mRangeDirections[0] = par2;
				break;
			case 116 :
				mRangeDirections[1] = par2;
				break;
			case 117 :
				mRangeDirections[2] = par2;
				break;
			case 118 :
				mRangeDirections[3] = par2;
				break;
		}
	}

	public int getSlotCount() {
		return 3;
	}

	public int getShiftClickSlotCount() {
		return 3;
	}
}
