package gtPlusPlus.xmod.gregtech.api.gui.hatches;

import gregtech.api.gui.GT_Container_1by1;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GregtechMetaTileEntity_LargerTurbineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CONTAINER_1by1_Turbine extends GT_Container_1by1 {

    public CONTAINER_1by1_Turbine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new SlotTurbine(mTileEntity, 0, 80, 35));
    }

	@Override
	public int getShiftClickSlotCount() {
		return 0;
	}

	@Override
	public boolean canDragIntoSlot(Slot par1Slot) {
		return false;
	}    
    
    public class SlotTurbine extends Slot {
    	public SlotTurbine(final IInventory inventory, final int x, final int y, final int z) {
    		super(inventory, x, y, z);
    	}
    	@Override
    	public boolean isItemValid(final ItemStack itemstack) {
    		return GregtechMetaTileEntity_LargerTurbineBase.isValidTurbine(itemstack);
    	}
    	@Override
    	public int getSlotStackLimit() {
    		return 1;
    	}
		@Override
		public boolean canTakeStack(EntityPlayer p_82869_1_) {
			return false;
		}
		@Override
		public void putStack(ItemStack aStack) {
			if (isItemValid(aStack)) {
				this.inventory.setInventorySlotContents(0, aStack);
			}
		}		
    }    
}
