package gtPlusPlus.xmod.gregtech.api.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class CONTAINER_IronBlastFurnace extends GT_ContainerMetaTile_Machine {
	public CONTAINER_IronBlastFurnace(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	@Override
	public void addSlots(final InventoryPlayer aInventoryPlayer) {
		this.addSlotToContainer(new Slot(this.mTileEntity, 0, 34, 16));
		this.addSlotToContainer(new Slot(this.mTileEntity, 1, 34, 34));
		this.addSlotToContainer(new GT_Slot_Output(this.mTileEntity, 2, 86, 25));
		this.addSlotToContainer(new GT_Slot_Output(this.mTileEntity, 3, 104, 25));
	}

	@Override
	public int getSlotCount() {
		return 4;
	}

	@Override
	public int getShiftClickSlotCount() {
		return 2;
	}
}
