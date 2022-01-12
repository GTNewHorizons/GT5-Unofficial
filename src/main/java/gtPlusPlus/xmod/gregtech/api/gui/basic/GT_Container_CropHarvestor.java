package gtPlusPlus.xmod.gregtech.api.gui.basic;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.slots.SlotNoInput;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_CropHarvestor;
import ic2.core.item.ItemIC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_CropHarvestor extends GT_ContainerMetaTile_Machine {

	public boolean mModeAlternative = false;
	public int mWaterAmount = 0;
	public int mWaterRealAmount = 0;

	public GT_Container_CropHarvestor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public void addSlots(InventoryPlayer aInventoryPlayer) {


		int aSlot = 1;

		addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 48, 64, false, true, 1));
		addSlotToContainer(new SlotWeedEx(mTileEntity,  aSlot++,  8,  14));
		addSlotToContainer(new SlotWeedEx(mTileEntity,  aSlot++,  26,  14));
		addSlotToContainer(new SlotFertilizer(mTileEntity,  aSlot++,  8,  32));
		addSlotToContainer(new SlotFertilizer(mTileEntity,  aSlot++,  26,  32));
		addSlotToContainer(new SlotFertilizer(mTileEntity,  aSlot++,  8,  50));
		addSlotToContainer(new SlotFertilizer(mTileEntity,  aSlot++,  26,  50));


		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  61,  7));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  79,  7));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  97,  7));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  115,  7));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  133,  7));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  151,  7));

		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  61,  25));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  79,  25));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  97,  25));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  115,  25));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  133,  25));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  151,  25));

		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  61,  43));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  79,  43));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  97,  43));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  115,  43));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  133,  43));
		addSlotToContainer(new SlotNoInput(mTileEntity,  aSlot++,  151,  43));


	}

	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {	    

		GT_MetaTileEntity_CropHarvestor machine = (GT_MetaTileEntity_CropHarvestor) mTileEntity.getMetaTileEntity();

		if (aSlotIndex == 0) {
			machine.mModeAlternative = !machine.mModeAlternative;
			return null;
		}

		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}    

	@Override
	public int getSlotCount() {
		return 24;
	}

	@Override
	public int getSlotStartIndex() {
		return 1;
	}

	@Override
	public int getShiftClickSlotCount() {
		return 6;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
			return;
		}

		// GT_MetaTileEntity_Boiler.getCapacity() is used for both water and steam capacity.
		int capacity = ((GT_MetaTileEntity_CropHarvestor) this.mTileEntity.getMetaTileEntity()).getCapacity();

		mModeAlternative = ((GT_MetaTileEntity_CropHarvestor) mTileEntity.getMetaTileEntity()).mModeAlternative;
		mWaterRealAmount = ((GT_MetaTileEntity_CropHarvestor) mTileEntity.getMetaTileEntity()).getFluidAmount();
		this.mWaterAmount = Math.min(54, Math.max(0, this.mWaterRealAmount * 54 / (capacity - 100)));
		for (Object crafter : this.crafters) {
			ICrafting var1 = (ICrafting) crafter;
			var1.sendProgressBarUpdate(this, 102, mModeAlternative ? 1 : 0);
			var1.sendProgressBarUpdate(this, 103, this.mWaterAmount);
			var1.sendProgressBarUpdate(this, 104, this.mWaterRealAmount);
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
				mModeAlternative = (par2 != 0);
				break;
			case 103:
				this.mWaterAmount = par2;
				break;
			case 104:
				this.mWaterRealAmount = par2;
				break;
		}
	}

	public static class SlotWeedEx extends Slot {

		public SlotWeedEx(final IInventory inventory, final int slot, final int x, final int y) {
			super(inventory, slot, x, y);

		}

		@Override
		public synchronized boolean isItemValid(final ItemStack itemstack) {
			return isWeedEx(itemstack);
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}

		private static boolean isWeedEx(ItemStack aStack) {		
			if (aStack != null && aStack.getItem() instanceof ItemIC2) {
				if (aStack.getItem().getUnlocalizedName().equals("ic2.itemWeedEx")) {
					return true;
				}
			}		
			return false;
		}
	}

	public class SlotFertilizer extends Slot {

		public SlotFertilizer(final IInventory inventory, final int slot, final int x, final int y) {
			super(inventory, slot, x, y);

		}

		@Override
		public synchronized boolean isItemValid(final ItemStack aStack) {
			if (aStack != null && aStack.getItem() instanceof ItemIC2) {
				if (aStack.getItem().getUnlocalizedName().equals("ic2.itemFertilizer")) {
					return true;
				}
			}		
			return false;
		}

		@Override
		public int getSlotStackLimit() {
			return 64;
		}
		
	}
	
}
