package gregtech.common.gui;

import gregtech.api.enums.GuiColors;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public class GT_GUIContainer_QuantumChest extends GT_GUIContainerMetaTile_Machine {

    private final String mName;
    private final int 
        textColorTitle = GuiColors.quantumChestTitle.getColor(),
        textColorAmount = GuiColors.quantumChestAmount.getColor();


    public GT_GUIContainer_QuantumChest(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new GT_Container_QuantumChest(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "BasicTank.png");
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, textColorTitle);
        fontRendererObj.drawString(mName, 8, 6, textColorTitle);
        if (mContainer != null) {
            fontRendererObj.drawString("Item Amount", 10, 20, textColorAmount);
            fontRendererObj.drawString(GT_Utility.parseNumberToString(((GT_Container_QuantumChest) mContainer).mContent), 10, 30, textColorAmount);
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
