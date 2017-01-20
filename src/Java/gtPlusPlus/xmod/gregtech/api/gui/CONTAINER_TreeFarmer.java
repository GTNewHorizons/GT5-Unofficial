package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.slots.SlotBuzzSaw;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my Basic Machines
 */
public class CONTAINER_TreeFarmer extends GT_ContainerMetaTile_Machine {
    public CONTAINER_TreeFarmer(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    public CONTAINER_TreeFarmer(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, boolean bindInventory) {
        super(aInventoryPlayer, aTileEntity, bindInventory);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new SlotBuzzSaw(mTileEntity, 1, 80, 35));
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 0;
    }
}
