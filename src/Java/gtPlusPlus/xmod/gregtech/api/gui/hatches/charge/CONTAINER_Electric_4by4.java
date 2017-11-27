package gtPlusPlus.xmod.gregtech.api.gui.hatches.charge;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.slots.SlotElectric;
import gtPlusPlus.xmod.gregtech.api.gui.hatches.CONTAINER_4by4;
import net.minecraft.entity.player.InventoryPlayer;

public class CONTAINER_Electric_4by4 extends CONTAINER_4by4{

	public CONTAINER_Electric_4by4(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}


    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new SlotElectric(mTileEntity, 0, 53, 8));
        addSlotToContainer(new SlotElectric(mTileEntity, 1, 71, 8));
        addSlotToContainer(new SlotElectric(mTileEntity, 2, 89, 8));
        addSlotToContainer(new SlotElectric(mTileEntity, 3, 107, 8));
        addSlotToContainer(new SlotElectric(mTileEntity, 4, 53, 26));
        addSlotToContainer(new SlotElectric(mTileEntity, 5, 71, 26));
        addSlotToContainer(new SlotElectric(mTileEntity, 6, 89, 26));
        addSlotToContainer(new SlotElectric(mTileEntity, 7, 107, 26));
        addSlotToContainer(new SlotElectric(mTileEntity, 8, 53, 44));
        addSlotToContainer(new SlotElectric(mTileEntity, 9, 71, 44));
        addSlotToContainer(new SlotElectric(mTileEntity, 10, 89, 44));
        addSlotToContainer(new SlotElectric(mTileEntity, 11, 107, 44));
        addSlotToContainer(new SlotElectric(mTileEntity, 12, 53, 62));
        addSlotToContainer(new SlotElectric(mTileEntity, 13, 71, 62));
        addSlotToContainer(new SlotElectric(mTileEntity, 14, 89, 62));
        addSlotToContainer(new SlotElectric(mTileEntity, 15, 107, 62));
    }
	
}
