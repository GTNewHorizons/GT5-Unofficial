package gtPlusPlus.xmod.gregtech.api.gui.hatches;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class CONTAINER_2by2 extends GT_ContainerMetaTile_Machine {

    public CONTAINER_2by2(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity, 0, 71, 26));
        addSlotToContainer(new Slot(mTileEntity, 1, 89, 26));
        addSlotToContainer(new Slot(mTileEntity, 2, 71, 44));
        addSlotToContainer(new Slot(mTileEntity, 3, 89, 44));
    }

    @Override
    public int getSlotCount() {
        return 4;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 4;
    }
}
