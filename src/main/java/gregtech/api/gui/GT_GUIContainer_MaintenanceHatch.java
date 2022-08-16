package gregtech.api.gui;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public class GT_GUIContainer_MaintenanceHatch extends GT_GUIContainerMetaTile_Machine {
    private final int 
        textColor = this.getTextColorOrDefault("text", 0x404040),
        textColorTitle = this.getTextColorOrDefault("title", 0x404040);


    public GT_GUIContainer_MaintenanceHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_MaintenanceHatch(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "Maintenance.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Maintenance Hatch", 8, 4, textColorTitle);
        fontRendererObj.drawString("Click with Tool to repair.", 8, 12, textColor);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
