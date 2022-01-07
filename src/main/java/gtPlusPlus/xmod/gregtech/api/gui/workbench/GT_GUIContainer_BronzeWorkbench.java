package gtPlusPlus.xmod.gregtech.api.gui.workbench;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_BronzeWorkbench extends GT_GUIContainer_AdvancedWorkbench {

    public GT_GUIContainer_BronzeWorkbench(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aLocalName) {
        super(new GT_Container_BronzeWorkbench(aInventoryPlayer, aTileEntity), aLocalName, CORE.RES_PATH_GUI + "BronzeCraftingTable.png");
    }
    
}
