package gtPlusPlus.xmod.gregtech.api.gui.hatches;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;

public class GUI_1by1_Turbine extends GT_GUIContainerMetaTile_Machine {

    private final String mName;

    public GUI_1by1_Turbine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new CONTAINER_1by1_Turbine(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "1by1.png");
        mName = aName;
    }
    
    public GUI_1by1_Turbine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aBackground) {
        super(new CONTAINER_1by1_Turbine(aInventoryPlayer, aTileEntity), RES_PATH_GUI + aBackground + "1by1.png");
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(mName, 8, 4, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
