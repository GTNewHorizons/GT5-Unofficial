package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.slots.SlotAirFilter;
import net.minecraft.entity.player.InventoryPlayer;


public class CONTAINER_Hatch_Muffler_Advanced extends GT_ContainerMetaTile_Machine {

	public long maxEU = 0;
	public long storedEU = 0;

	public CONTAINER_Hatch_Muffler_Advanced(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public CONTAINER_Hatch_Muffler_Advanced(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final boolean bindInventory) {
		super(aInventoryPlayer, aTileEntity, bindInventory);
	}

	@Override
	public void addSlots(final InventoryPlayer aInventoryPlayer) {
		this.addSlotToContainer(new SlotAirFilter(this.mTileEntity, 1, 80, 35));
	}

	@Override
	public int getSlotCount() {
		return 1;
	}

	@Override
	public int getShiftClickSlotCount() {
		return 1;
	}

	@Override
	public void updateProgressBar(final int id, final int value) {
		super.updateProgressBar(id, value);
		switch (id) {		
		default:
			break;
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();		
	}

}