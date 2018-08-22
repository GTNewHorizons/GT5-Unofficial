package gregtech.common.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Output;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_PrimitiveBlastFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class GT_Container_PrimitiveBlastFurnace extends GT_ContainerMetaTile_Machine {

	public GT_Container_PrimitiveBlastFurnace(InventoryPlayer inventoryPlayer, IGregTechTileEntity tileEntity) {
		super(inventoryPlayer, tileEntity);
	}

	public void addSlots(InventoryPlayer aInventoryPlayer) {
		for (int i = 0; i < GT_MetaTileEntity_PrimitiveBlastFurnace.INPUT_SLOTS; i++) {
            addSlotToContainer(new Slot(this.mTileEntity, i, 34, 16 + 18 * i));
		}
		for (int i = 0; i < GT_MetaTileEntity_PrimitiveBlastFurnace.OUTPUT_SLOTS; i++) {
            addSlotToContainer(new GT_Slot_Output(mTileEntity, GT_MetaTileEntity_PrimitiveBlastFurnace.INPUT_SLOTS + i, 86 + i * 18, 25));
		}
	}

	public int getSlotCount() {
		return GT_MetaTileEntity_PrimitiveBlastFurnace.INPUT_SLOTS
				+ GT_MetaTileEntity_PrimitiveBlastFurnace.OUTPUT_SLOTS;
	}

	public int getShiftClickSlotCount() {
		return GT_MetaTileEntity_PrimitiveBlastFurnace.INPUT_SLOTS;
	}
}
