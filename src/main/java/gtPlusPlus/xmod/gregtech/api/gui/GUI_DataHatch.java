package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;

public class GUI_DataHatch extends GT_GUIContainerMetaTile_Machine {

    private final String mName;

    public GUI_DataHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new CONTAINER_DataHatch(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "HatchDataOrb.png");
        mName = aName;
    }

    public GUI_DataHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aBackground) {
        super(new CONTAINER_DataHatch(aInventoryPlayer, aTileEntity), RES_PATH_GUI + aBackground + "HatchDataOrb.png");
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(mName, 8, 4, 4210752);
		/*String[] aName = mName.trim().split("\\s+");
		int aY = 4;
		for (String s : aName) {
		    fontRendererObj.drawString(s, 120, (aY += 12), 4210752);    		
		}*/
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
