package gregtech.api.gui;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;

public class GT_GUIContainer_BasicTank extends GT_GUIContainerMetaTile_Machine {

    private final String mName;
    private final int textColor = this.getTextColorOrDefault("text", 0xFAFAFF),
            textColorTitle = this.getTextColorOrDefault("title", 0x404040),
            textColorValue = this.getTextColorOrDefault("value", 0xFAFAFF);

    public GT_GUIContainer_BasicTank(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new GT_Container_BasicTank(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "BasicTank.png");
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(
                StatCollector.translateToLocal("container.inventory"),
                8,
                ySize - 96 + 2,
                textColorTitle);
        fontRendererObj.drawString(mName, 8, 6, textColorTitle);
        if (mContainer != null) {
            fontRendererObj.drawString("Liquid Amount", 10, 20, textColor);
            fontRendererObj.drawString(
                    GT_Utility.parseNumberToString(((GT_Container_BasicTank) mContainer).mContent),
                    10,
                    30,
                    textColorValue);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
