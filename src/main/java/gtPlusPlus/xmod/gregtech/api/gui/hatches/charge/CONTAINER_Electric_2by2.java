package gtPlusPlus.xmod.gregtech.api.gui.hatches.charge;

import net.minecraft.entity.player.InventoryPlayer;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.core.slots.SlotElectric;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.CONTAINER_2by2;

public class CONTAINER_Electric_2by2 extends CONTAINER_2by2{

	public CONTAINER_Electric_2by2(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}
	
	@Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new SlotElectric(mTileEntity, 0, 71, 26));
        addSlotToContainer(new SlotElectric(mTileEntity, 1, 89, 26));
        addSlotToContainer(new SlotElectric(mTileEntity, 2, 71, 44));
        addSlotToContainer(new SlotElectric(mTileEntity, 3, 89, 44));
    }

}
