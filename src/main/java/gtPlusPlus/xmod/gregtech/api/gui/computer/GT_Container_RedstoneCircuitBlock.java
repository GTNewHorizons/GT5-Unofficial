package gtPlusPlus.xmod.gregtech.api.gui.computer;

import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.tileentities.redstone.GT_MetaTileEntity_RedstoneCircuitBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GT_Container_RedstoneCircuitBlock extends GT_ContainerMetaTile_Machine {

	public GT_Container_RedstoneCircuitBlock(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public void addSlots(InventoryPlayer aInventoryPlayer) {
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 8, 6, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 8, 24, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 8, 42, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 8, 60, false, true, 1));

		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 152, 6, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 152, 24, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 152, 42, false, true, 1));

		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 1, 26, 6, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 24, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 3, 26, 42, false, true, 1));
		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 4, 26, 60, false, true, 1));
	}

	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
		if (aSlotIndex < 0 || aSlotIndex > 6)
			return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);

		Slot tSlot = (Slot) inventorySlots.get(aSlotIndex);
		if (tSlot != null) {
			if (mTileEntity.getMetaTileEntity() == null)
				return null;
			if (aSlotIndex < 4) {
				ItemStack tStack = aPlayer.inventory.getItemStack();
				if (tStack == null) {
					((GT_MetaTileEntity_RedstoneCircuitBlock) mTileEntity.getMetaTileEntity()).changeGateData(aSlotIndex, aMouseclick == 0 ? aShifthold == 0 ? +1 : aShifthold == 1 ? +128 : +16 : aShifthold == 0 ? -1 : aShifthold == 1 ? -128 : -16);
				}
				else {
					tStack = GT_Utility.copy(tStack);
					if (aMouseclick != 0)
						tStack.setItemDamage(OreDictionary.WILDCARD_VALUE);
					((GT_MetaTileEntity_RedstoneCircuitBlock) mTileEntity.getMetaTileEntity()).stackGateData(aSlotIndex, tStack);
				}
				return null;
			}
			else if (aSlotIndex == 4) {
				((GT_MetaTileEntity_RedstoneCircuitBlock) mTileEntity.getMetaTileEntity()).switchOutput();
			}
			else if (aSlotIndex == 5) {
				mTileEntity.setActive(!mTileEntity.isActive());
			}
			else if (aSlotIndex == 6) {
				if (aMouseclick == 0)
					((GT_MetaTileEntity_RedstoneCircuitBlock) mTileEntity.getMetaTileEntity()).switchGateForward(aShifthold != 0);
				else
					((GT_MetaTileEntity_RedstoneCircuitBlock) mTileEntity.getMetaTileEntity()).switchGateBackward(aShifthold != 0);
			}
		}
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}

	public int mData[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0}, mGate = 0;

	@SuppressWarnings("rawtypes")
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null)
			return;
		mGate = ((GT_MetaTileEntity_RedstoneCircuitBlock) mTileEntity.getMetaTileEntity()).mGate;
		mData = ((GT_MetaTileEntity_RedstoneCircuitBlock) mTileEntity.getMetaTileEntity()).mGateData;

		Iterator var2 = this.crafters.iterator();
		while (var2.hasNext()) {
			ICrafting var1 = (ICrafting) var2.next();
			var1.sendProgressBarUpdate(this, 100, mGate & 65535);
			var1.sendProgressBarUpdate(this, 101, mGate >>> 16);
			var1.sendProgressBarUpdate(this, 102, mData[0] & 65535);
			var1.sendProgressBarUpdate(this, 103, mData[0] >>> 16);
			var1.sendProgressBarUpdate(this, 104, mData[1] & 65535);
			var1.sendProgressBarUpdate(this, 105, mData[1] >>> 16);
			var1.sendProgressBarUpdate(this, 106, mData[2] & 65535);
			var1.sendProgressBarUpdate(this, 107, mData[2] >>> 16);
			var1.sendProgressBarUpdate(this, 108, mData[3] & 65535);
			var1.sendProgressBarUpdate(this, 109, mData[3] >>> 16);
			var1.sendProgressBarUpdate(this, 110, mData[4] & 65535);
			var1.sendProgressBarUpdate(this, 111, mData[4] >>> 16);
			var1.sendProgressBarUpdate(this, 112, mData[5] & 65535);
			var1.sendProgressBarUpdate(this, 113, mData[5] >>> 16);
			var1.sendProgressBarUpdate(this, 114, mData[6] & 65535);
			var1.sendProgressBarUpdate(this, 115, mData[6] >>> 16);
			var1.sendProgressBarUpdate(this, 116, mData[7] & 65535);
			var1.sendProgressBarUpdate(this, 117, mData[7] >>> 16);
		}
	}

	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		super.updateProgressBar(par1, par2);
		switch (par1) {
			case 100 :
				mGate = mGate & -65536 | par2;
				break;
			case 101 :
				mGate = mGate & 65535 | par2 << 16;
				break;
			case 102 :
				mData[0] = mData[0] & -65536 | par2;
				break;
			case 103 :
				mData[0] = mData[0] & 65535 | par2 << 16;
				break;
			case 104 :
				mData[1] = mData[1] & -65536 | par2;
				break;
			case 105 :
				mData[1] = mData[1] & 65535 | par2 << 16;
				break;
			case 106 :
				mData[2] = mData[2] & -65536 | par2;
				break;
			case 107 :
				mData[2] = mData[2] & 65535 | par2 << 16;
				break;
			case 108 :
				mData[3] = mData[3] & -65536 | par2;
				break;
			case 109 :
				mData[3] = mData[3] & 65535 | par2 << 16;
				break;
			case 110 :
				mData[4] = mData[4] & -65536 | par2;
				break;
			case 111 :
				mData[4] = mData[4] & 65535 | par2 << 16;
				break;
			case 112 :
				mData[5] = mData[5] & -65536 | par2;
				break;
			case 113 :
				mData[5] = mData[5] & 65535 | par2 << 16;
				break;
			case 114 :
				mData[6] = mData[6] & -65536 | par2;
				break;
			case 115 :
				mData[6] = mData[6] & 65535 | par2 << 16;
				break;
			case 116 :
				mData[7] = mData[7] & -65536 | par2;
				break;
			case 117 :
				mData[7] = mData[7] & 65535 | par2 << 16;
				break;
		}
	}
}
