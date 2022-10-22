package gregtech.api.gui;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public class GT_GUIContainer_1by1 extends GT_GUIContainerMetaTile_Machine {

    private final String mName;

    public GT_GUIContainer_1by1(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new GT_Container_1by1(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "1by1.png");
        mName = aName;
    }

    public GT_GUIContainer_1by1(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aBackground) {
        super(new GT_Container_1by1(aInventoryPlayer, aTileEntity), RES_PATH_GUI + aBackground + "1by1.png");
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(mName, 8, 4, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
