package gtPlusPlus.xmod.gregtech.api.gui;

import net.minecraft.entity.player.InventoryPlayer;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_MassFabricator;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my Basic Machines
 */
public class CONTAINER_MatterFab extends GT_ContainerMetaTile_Machine {

	public int mUUA_USED = ((GregtechMetaTileEntity_MassFabricator)this.mTileEntity.getMetaTileEntity()).getAmplifierUsed();
	public int mUUM_MADE = ((GregtechMetaTileEntity_MassFabricator)this.mTileEntity.getMetaTileEntity()).getMatterProduced();

	public CONTAINER_MatterFab(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	public CONTAINER_MatterFab(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final boolean bindInventory) {
		super(aInventoryPlayer, aTileEntity, bindInventory);
	}
}

/*@Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity, 1, 152, 5));
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 0;
    }
}*/
