package gtPlusPlus.xmod.gregtech.api.gui.automation;

import com.google.gson.JsonObject;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_ElectricAutoWorkbench;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_CropHarvestor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_ElectricAutoWorkbench extends GT_ContainerMetaTile_Machine {
	
	public int mMode;
	public int mThroughPut;

	public GT_Container_ElectricAutoWorkbench(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity,  0,   8,  5));
        addSlotToContainer(new Slot(mTileEntity,  1,  26,  5));
        addSlotToContainer(new Slot(mTileEntity,  2,  44,  5));
        addSlotToContainer(new Slot(mTileEntity,  3,   8, 23));
        addSlotToContainer(new Slot(mTileEntity,  4,  26, 23));
        addSlotToContainer(new Slot(mTileEntity,  5,  44, 23));
        addSlotToContainer(new Slot(mTileEntity,  6,   8, 41));
        addSlotToContainer(new Slot(mTileEntity,  7,  26, 41));
        addSlotToContainer(new Slot(mTileEntity,  8,  44, 41));

        addSlotToContainer(new GT_Slot_Output(mTileEntity,  9,   8, 60));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 10,  26, 60));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 11,  44, 60));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 12,  62, 60));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 13,  80, 60));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 14,  98, 60));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 15, 116, 60));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 16, 134, 60));
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 17, 152, 60));
        
        addSlotToContainer(new GT_Slot_Output(mTileEntity, 18, 152, 41));
        
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 19,  64,  6, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 20,  81,  6, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 21,  98,  6, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 22,  64, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 23,  81, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 24,  98, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 25,  64, 40, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 26,  81, 40, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 27,  98, 40, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 28, 152,  5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 29, 121, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 29, 121,  5, false, false, 1));
    }

    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
    	if (aSlotIndex < 18) return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	    
    	Slot tSlot = (Slot)inventorySlots.get(aSlotIndex);
	    if (tSlot != null) {
	    	if (mTileEntity.getMetaTileEntity() == null) return null;
		    if (aSlotIndex > 18 && aSlotIndex < 28) {
		    	ItemStack tStack = aPlayer.inventory.getItemStack();
		    	if (tStack != null) {
		    		tStack = GT_Utility.copy(1, tStack);
		    	}
		    	tSlot.putStack(tStack);
		    	return null;
		    }
		    if (aSlotIndex == 28) return null;
		    if (aSlotIndex == 29) {
		    	if (aMouseclick == 0) {
		    		((GT_MetaTileEntity_ElectricAutoWorkbench)mTileEntity.getMetaTileEntity()).switchModeForward();
		    	} else {
		    		((GT_MetaTileEntity_ElectricAutoWorkbench)mTileEntity.getMetaTileEntity()).switchModeBackward();
		    	}
		    	return null;
		    }
		    if (aSlotIndex == 30) {
		    	((GT_MetaTileEntity_ElectricAutoWorkbench)mTileEntity.getMetaTileEntity()).switchThrough();
		    	return null;
		    }
    	}
	    
    	return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }
    
    public int getSlotCount() {
    	return 19;
    }
    
    public int getShiftClickSlotCount() {
    	return 9;
    }
    
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
			return;
		}
		mMode = ((GT_MetaTileEntity_ElectricAutoWorkbench) mTileEntity.getMetaTileEntity()).mMode;
		mThroughPut = ((GT_MetaTileEntity_ElectricAutoWorkbench) mTileEntity.getMetaTileEntity()).mThroughPut;
		for (Object crafter : this.crafters) {
			ICrafting var1 = (ICrafting) crafter;
			var1.sendProgressBarUpdate(this, 103, this.mMode);
			var1.sendProgressBarUpdate(this, 104, this.mThroughPut);
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
			case 103:
				this.mMode = par2;
				break;
			case 104:
				this.mThroughPut = par2;
				break;
		}
	}
	
}
