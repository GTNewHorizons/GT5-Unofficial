package gregtech.common.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_Boiler extends GT_GUIContainerMetaTile_Machine {
    public GT_GUIContainer_Boiler(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aTextureName) {
        super(new GT_Container_Boiler(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/" + aTextureName);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString("Boiler", 8, 4, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        if (this.mContainer != null) {
            int tScale = ((GT_Container_Boiler) this.mContainer).mSteamAmount;
            if (tScale > 0) {
                drawTexturedModalRect(x + 70, y + 25 + 54 - tScale, 194, 54 - tScale, 10, tScale);
            }
            tScale = ((GT_Container_Boiler) this.mContainer).mWaterAmount;
            if (tScale > 0) {
                drawTexturedModalRect(x + 83, y + 25 + 54 - tScale, 204, 54 - tScale, 10, tScale);
            }
            tScale = ((GT_Container_Boiler) this.mContainer).mTemperature;
            if (tScale > 0) {
                drawTexturedModalRect(x + 96, y + 25 + 54 - tScale, 214, 54 - tScale, 10, tScale);
            }
            tScale = ((GT_Container_Boiler) this.mContainer).mProcessingEnergy;
            if (tScale > 0) {
                drawTexturedModalRect(x + 117, y + 44 + 14 - tScale, 177, 14 - tScale, 15, tScale + 1);
            }
        }
    }
}
