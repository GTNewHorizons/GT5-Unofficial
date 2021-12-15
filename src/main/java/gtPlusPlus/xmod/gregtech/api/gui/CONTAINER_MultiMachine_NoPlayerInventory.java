package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class CONTAINER_MultiMachine_NoPlayerInventory extends CONTAINER_MultiMachine {

	public CONTAINER_MultiMachine_NoPlayerInventory(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public CONTAINER_MultiMachine_NoPlayerInventory(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final boolean bindInventory) {
		super(aInventoryPlayer, aTileEntity, bindInventory);
	}
    
    public boolean doesBindPlayerInventory() {
        return false;
    }

    @Override
    public ItemStack slotClick(final int aSlotIndex, final int aMouseclick, final int aShifthold, final EntityPlayer aPlayer) {
    	return null;
    }
	
}
