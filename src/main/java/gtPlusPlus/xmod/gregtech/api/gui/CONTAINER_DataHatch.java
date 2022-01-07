package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.enums.ItemList;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.slots.SlotIntegratedCircuit;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CONTAINER_DataHatch extends GT_ContainerMetaTile_Machine {

    public CONTAINER_DataHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 0, 53, 8));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 1, 71, 8));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 2, 89, 8));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 3, 107, 8));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 4, 53, 26));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 5, 71, 26));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 6, 89, 26));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 7, 107, 26));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 8, 53, 44));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 9, 71, 44));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 10, 89, 44));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 11, 107, 44));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 12, 53, 62));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 13, 71, 62));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 14, 89, 62));
        addSlotToContainer(new GT_Slot_DataOrb(mTileEntity, 15, 107, 62));
        addSlotToContainer(new SlotIntegratedCircuit(mTileEntity, 16, 19, 35));
        
    }

    @Override
    public int getSlotCount() {
        return 17;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 0;
    }
    
	@Override
	public void putStackInSlot(int par1, ItemStack par2ItemStack) {	
		/*Logger.INFO("Slot: "+par1);
		if (par1 < 16 && ItemList.Tool_DataOrb.isStackEqual(par2ItemStack)) {
			super.putStackInSlot(par1, par2ItemStack);
		}
		if (par1 == 16 && ItemUtils.isControlCircuit(par2ItemStack)) {
			super.putStackInSlot(par1, par2ItemStack);			
		}*/		
		super.putStackInSlot(par1, par2ItemStack);			
	}

	@Override
	public boolean canDragIntoSlot(Slot par1Slot) {
		return super.canDragIntoSlot(par1Slot);
	}

    public class GT_Slot_DataOrb extends Slot {
        public GT_Slot_DataOrb(IInventory par1iInventory, int par2, int par3, int par4) {
            super(par1iInventory, par2, par3, par4);
        }

        @Override
        public boolean isItemValid(ItemStack aStack) {
            return ItemList.Tool_DataOrb.isStackEqual(aStack, false, true);
        }
    }



}
