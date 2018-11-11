package gregtech.common.gui;

import gregtech.api.util.GT_Utility;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public class GT_GUIContainer_DigitalTransformer extends GT_GUIContainerMetaTile_Machine {
    public GT_GUIContainer_DigitalTransformer(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_DigitalTransformer(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "Teleporter.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString("Digital Trans", 46, 8, 16448255);
        if (mContainer != null) {
        	GT_Container_DigitalTransformer dpg = (GT_Container_DigitalTransformer) mContainer;
            fontRendererObj.drawString("EUT: " + dpg.EUT, 46, 24, 16448255);
            fontRendererObj.drawString("TIER: " + VN[GT_Utility.getTier(dpg.EUT<0?-dpg.EUT:dpg.EUT)], 46, 32, 16448255);
            fontRendererObj.drawString("AMP: " + dpg.AMP, 46, 40, 16448255);
            fontRendererObj.drawString("SUM: " + (long)dpg.AMP*dpg.EUT, 46, 48, 16448255);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
