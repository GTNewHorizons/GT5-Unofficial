package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_AdvancedCraftingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CONTAINER_AdvancedWorkbench
extends GT_ContainerMetaTile_Machine
{
	public CONTAINER_AdvancedWorkbench(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity)
	{
		super(aInventoryPlayer, aTileEntity);
	}

	@Override
	public void addSlots(InventoryPlayer aInventoryPlayer)
	{
		addSlotToContainer(new Slot(this.mTileEntity, 0, 8, 8));
		addSlotToContainer(new Slot(this.mTileEntity, 1, 26, 8));
		addSlotToContainer(new Slot(this.mTileEntity, 2, 44, 8));
		addSlotToContainer(new Slot(this.mTileEntity, 3, 62, 8));
		addSlotToContainer(new Slot(this.mTileEntity, 4, 8, 26));
		addSlotToContainer(new Slot(this.mTileEntity, 5, 26, 26));
		addSlotToContainer(new Slot(this.mTileEntity, 6, 44, 26));
		addSlotToContainer(new Slot(this.mTileEntity, 7, 62, 26));
		addSlotToContainer(new Slot(this.mTileEntity, 8, 8, 44));
		addSlotToContainer(new Slot(this.mTileEntity, 9, 26, 44));
		addSlotToContainer(new Slot(this.mTileEntity, 10, 44, 44));
		addSlotToContainer(new Slot(this.mTileEntity, 11, 62, 44));
		addSlotToContainer(new Slot(this.mTileEntity, 12, 8, 62));
		addSlotToContainer(new Slot(this.mTileEntity, 13, 26, 62));
		addSlotToContainer(new Slot(this.mTileEntity, 14, 44, 62));
		addSlotToContainer(new Slot(this.mTileEntity, 15, 62, 62));

		addSlotToContainer(new Slot(this.mTileEntity, 16, 82, 8));
		addSlotToContainer(new Slot(this.mTileEntity, 17, 100, 8));
		addSlotToContainer(new Slot(this.mTileEntity, 18, 118, 8));
		addSlotToContainer(new Slot(this.mTileEntity, 19, 136, 8));
		addSlotToContainer(new Slot(this.mTileEntity, 20, 154, 8));

		addSlotToContainer(new Slot(this.mTileEntity, 21, 82, 28));
		addSlotToContainer(new Slot(this.mTileEntity, 22, 100, 28));
		addSlotToContainer(new Slot(this.mTileEntity, 23, 118, 28));
		addSlotToContainer(new Slot(this.mTileEntity, 24, 82, 46));
		addSlotToContainer(new Slot(this.mTileEntity, 25, 100, 46));
		addSlotToContainer(new Slot(this.mTileEntity, 26, 118, 46));
		addSlotToContainer(new Slot(this.mTileEntity, 27, 82, 64));
		addSlotToContainer(new Slot(this.mTileEntity, 28, 100, 64));
		addSlotToContainer(new Slot(this.mTileEntity, 29, 118, 64));

		addSlotToContainer(new Slot(this.mTileEntity, 33, 154, 28));
		addSlotToContainer(new Slot(this.mTileEntity, 34, 154, 64));

		addSlotToContainer(new Slot(this.mTileEntity, 30, 136, 28));
		addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 31, 136, 64, false, false, 1));
		addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 32, 154, 46, false, false, 1));
		addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 32, 136, 46, false, false, 1));
	}

	@Override
	public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer)
	{
		if ((aSlotIndex < 21) || (aSlotIndex > 35)) {
			return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
		}
		if ((this.mTileEntity == null) || (this.mTileEntity.getMetaTileEntity() == null)) {
			return null;
		}
		try
		{
			ItemStack tStack = ((Slot)this.inventorySlots.get(aSlotIndex)).getStack();
			if ((tStack != null) && (tStack.stackSize <= 0) && (!GT_Utility.areStacksEqual(tStack, aPlayer.inventory.getItemStack()))) {
				return null;
			}
			if (aSlotIndex == 32)
			{
				if ((aMouseclick == 0) && (aShifthold == 1))
				{
					((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).setBluePrint(null);
					return null;
				}
			}
			else
			{
				if (aSlotIndex == 33)
				{
					ItemStack tCraftedStack = ((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).getCraftingOutput();
					if (tCraftedStack != null) {
						if (aShifthold == 1)
						{
							for (byte i = 0; i < aPlayer.inventory.mainInventory.length; i = (byte)(i + 1)) {
								for (byte j = 0; (j < tCraftedStack.getMaxStackSize() / tCraftedStack.stackSize) && (((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).canDoCraftingOutput()); j = (byte)(j + 1))
								{
									ItemStack tStack2;
									if ((!GT_Utility.areStacksEqual(tStack2 = ((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).getCraftingOutput(), tCraftedStack)) || ((tStack != null) && (tStack.stackSize != tStack2.stackSize))) {
										return aPlayer.inventory.getItemStack();
									}
									aPlayer.inventory.mainInventory[i] = ((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).consumeMaterials(aPlayer, aPlayer.inventory.mainInventory[i]);
								}
							}
						}
						else
						{
							if (aMouseclick == 0)
							{
								if (((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).canDoCraftingOutput()) {
									aPlayer.inventory.setItemStack(((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).consumeMaterials(aPlayer, aPlayer.inventory.getItemStack()));
								}
								return aPlayer.inventory.getItemStack();
							}
							for (int i = 0; (i < tCraftedStack.getMaxStackSize() / tCraftedStack.stackSize) && (((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).canDoCraftingOutput()); i++)
							{
								ItemStack tStack2;
								if ((!GT_Utility.areStacksEqual(tStack2 = ((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).getCraftingOutput(), tCraftedStack)) || ((tStack != null) && (tStack.stackSize != tStack2.stackSize))) {
									return aPlayer.inventory.getItemStack();
								}
								aPlayer.inventory.setItemStack(((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).consumeMaterials(aPlayer, aPlayer.inventory.getItemStack()));
							}
							return aPlayer.inventory.getItemStack();
						}
					}
					return null;
				}
				if (aSlotIndex == 34)
				{
					((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).mFlushMode = true;
					return null;
				}
				if (aSlotIndex == 35)
				{
					((GT_MetaTileEntity_AdvancedCraftingTable)this.mTileEntity.getMetaTileEntity()).sortIntoTheInputSlots();
					return null;
				}
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace(GT_Log.err);
		}
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}

	@Override
	public int getSlotCount()
	{
		return 33;
	}

	@Override
	public int getShiftClickSlotCount()
	{
		return 21;
	}
}
