package GoodGenerator.Common.Container;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

public class NeutronSensorGUIContainer extends GT_ContainerMetaTile_Machine {

    public NeutronSensorGUIContainer(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }
}
