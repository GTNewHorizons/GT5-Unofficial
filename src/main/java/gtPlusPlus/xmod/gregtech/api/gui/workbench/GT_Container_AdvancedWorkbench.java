package gtPlusPlus.xmod.gregtech.api.gui.workbench;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.slots.SlotBlueprint;
import gtPlusPlus.core.slots.SlotElectric;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_AdvancedCraftingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_AdvancedWorkbench extends GT_ContainerMetaTile_Machine {

	public GT_Container_AdvancedWorkbench(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}
	
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity,  0,   8,  8));
        addSlotToContainer(new Slot(mTileEntity,  1,  26,  8));
        addSlotToContainer(new Slot(mTileEntity,  2,  44,  8));
        addSlotToContainer(new Slot(mTileEntity,  3,  62,  8));
        addSlotToContainer(new Slot(mTileEntity,  4,   8, 26));
        addSlotToContainer(new Slot(mTileEntity,  5,  26, 26));
        addSlotToContainer(new Slot(mTileEntity,  6,  44, 26));
        addSlotToContainer(new Slot(mTileEntity,  7,  62, 26));
        addSlotToContainer(new Slot(mTileEntity,  8,   8, 44));
        addSlotToContainer(new Slot(mTileEntity,  9,  26, 44));
        addSlotToContainer(new Slot(mTileEntity, 10,  44, 44));
        addSlotToContainer(new Slot(mTileEntity, 11,  62, 44));
        addSlotToContainer(new Slot(mTileEntity, 12,   8, 62));
        addSlotToContainer(new Slot(mTileEntity, 13,  26, 62));
        addSlotToContainer(new Slot(mTileEntity, 14,  44, 62));
        addSlotToContainer(new Slot(mTileEntity, 15,  62, 62));
        
        addSlotToContainer(new SlotElectric(mTileEntity, 16,  82,  8));
        addSlotToContainer(new SlotElectric(mTileEntity, 17, 100,  8));
        addSlotToContainer(new SlotElectric(mTileEntity, 18, 118,  8));
        addSlotToContainer(new SlotElectric(mTileEntity, 19, 136,  8));
        addSlotToContainer(new SlotElectric(mTileEntity, 20, 154,  8));
        
        addSlotToContainer(new Slot(mTileEntity, 21,  82, 28));
        addSlotToContainer(new Slot(mTileEntity, 22, 100, 28));
        addSlotToContainer(new Slot(mTileEntity, 23, 118, 28));
        addSlotToContainer(new Slot(mTileEntity, 24,  82, 46));
        addSlotToContainer(new Slot(mTileEntity, 25, 100, 46));
        addSlotToContainer(new Slot(mTileEntity, 26, 118, 46));
        addSlotToContainer(new Slot(mTileEntity, 27,  82, 64));
        addSlotToContainer(new Slot(mTileEntity, 28, 100, 64));
        addSlotToContainer(new Slot(mTileEntity, 29, 118, 64));
        
        addSlotToContainer(new Slot(mTileEntity, 33, 154, 28));
        addSlotToContainer(new Slot(mTileEntity, 34, 154, 64));
        
        addSlotToContainer(new SlotBlueprint(mTileEntity, 30, 136, 28));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 31, 136, 64, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 32, 154, 46, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 32, 136, 46, false, false, 1));
        
    }
    
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
    	Logger.INFO("Clicked on slot "+aSlotIndex);
    	if (aSlotIndex < 21 || aSlotIndex > 35) return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    	if (mTileEntity == null || mTileEntity.getMetaTileEntity() == null) return null;
    	try {
		    ItemStack tStack = ((Slot)inventorySlots.get(aSlotIndex)).getStack();
		    if (tStack != null && tStack.stackSize <= 0 && !GT_Utility.areStacksEqual(tStack, aPlayer.inventory.getItemStack())) {
		    	return null;
		    }
		    if (aSlotIndex == 32) {
		    	if (aMouseclick == 0 && aShifthold == 1) {
		    		((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).setBluePrint(null);
		    		return null;
		    	}
		    } else if (aSlotIndex == 33) {
		    	ItemStack tStack2, tCraftedStack = ((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).getCraftingOutput();
		    	if (tCraftedStack != null) {
			    	if (aShifthold == 1) {
				    	for (byte i = 0; i < aPlayer.inventory.mainInventory.length; i++) {
					    	for (byte j = 0; j < tCraftedStack.getMaxStackSize() / tCraftedStack.stackSize && ((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).canDoCraftingOutput(); j++) {
					    		if (!GT_Utility.areStacksEqual(tStack2 = ((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).getCraftingOutput(), tCraftedStack) || tStack.stackSize != tStack2.stackSize) return aPlayer.inventory.getItemStack();
					    		aPlayer.inventory.mainInventory[i] = (((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).consumeMaterials(aPlayer, aPlayer.inventory.mainInventory[i]));
					    	}
				    	}
				    } else {
				    	if (aMouseclick == 0) {
					    	if (((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).canDoCraftingOutput()) aPlayer.inventory.setItemStack(((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).consumeMaterials(aPlayer, aPlayer.inventory.getItemStack()));
					    	return aPlayer.inventory.getItemStack();
				    	} else {
					    	for (int i = 0; i < tCraftedStack.getMaxStackSize() / tCraftedStack.stackSize && ((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).canDoCraftingOutput(); i++) {
					    		if (!GT_Utility.areStacksEqual(tStack2 = ((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).getCraftingOutput(), tCraftedStack) || tStack.stackSize != tStack2.stackSize) return aPlayer.inventory.getItemStack();
					    		aPlayer.inventory.setItemStack(((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).consumeMaterials(aPlayer, aPlayer.inventory.getItemStack()));
					    	}
					    	return aPlayer.inventory.getItemStack();
					    }
				    }
		    	}
		    	return null;
	    	} else if (aSlotIndex == 34) {
	    		((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).mFlushMode = true;
	    		return null;
	    	} else if (aSlotIndex == 35) {
	    		((GT_MetaTileEntity_AdvancedCraftingTable)mTileEntity.getMetaTileEntity()).sortIntoTheInputSlots();
	    		return null;
	    	}
    	} catch(Throwable e) {
    		//GT_Log.log.catching(e);
    	}
	    return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }
    
    public int getSlotCount() {
    	return 33;
    }
    
    public int getShiftClickSlotCount() {
    	return 21;
    }
}
