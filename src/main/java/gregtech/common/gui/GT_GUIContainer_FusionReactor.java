package gregtech.common.gui;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.nei.NEI_TransferRectHost;
import java.awt.Rectangle;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_FusionReactor extends GT_GUIContainerMetaTile_Machine implements NEI_TransferRectHost {

    public String mNEI;
    String mName = "";

    private final int textColor = this.getTextColorOrDefault("text", 0xFAFAFF),
            textColorTitle = this.getTextColorOrDefault("title", 0xFAFAFF),
            textColorValue = this.getTextColorOrDefault("value", 0xFF0000);

    public GT_GUIContainer_FusionReactor(
            InventoryPlayer aInventoryPlayer,
            IGregTechTileEntity aTileEntity,
            String aName,
            String aTextureFile,
            String aNEI) {
        super(
                new GT_Container_MultiMachine(aInventoryPlayer, aTileEntity, false),
                RES_PATH_GUI + "multimachines/" + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
        mName = aName;
        mNEI = aNEI;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(mName, 8, -10, textColorTitle);

        if (mContainer != null) {
            if ((mContainer.mDisplayErrorCode & 64) != 0)
                fontRendererObj.drawString("Incomplete Structure.", 10, 8, textColor);

            if (mContainer.mDisplayErrorCode == 0) {
                if (mContainer.mActive == 0) {
                    fontRendererObj.drawString(
                            "Hit with Soft Mallet to (re-)start the Machine if it doesn't start.", -70, 170, textColor);
                } else {
                    fontRendererObj.drawString("Running perfectly.", 10, 170, textColor);
                }
            }
            if (this.mContainer.mEnergy > 160000000 && this.mContainer.mEnergy < 160010000)
                fontRendererObj.drawString("160,000,000 EU", 50, 155, textColorValue);
            else if (this.mContainer.mEnergy > 320000000 && this.mContainer.mEnergy < 320010000)
                fontRendererObj.drawString("320,000,000 EU", 50, 155, textColorValue);
            else if (this.mContainer.mEnergy > 640000000 && this.mContainer.mEnergy < 640010000)
                fontRendererObj.drawString("640,000,000 EU", 50, 155, textColorValue);
            else if (this.mContainer.mEnergyLong > 5120000000L && this.mContainer.mEnergyLong < 5120080000L)
                fontRendererObj.drawString("5,120,000,000 EU", 50, 155, textColorValue);
            else
                fontRendererObj.drawString(
                        GT_Utility.formatNumbers(this.mContainer.mEnergyLong) + " EU", 50, 155, textColorValue);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        if (this.mContainer != null) {
            double tScale = (double) this.mContainer.mEnergyLong / (double) this.mContainer.mStorageLong;
            drawTexturedModalRect(x + 5, y + 156, 0, 251, Math.min(147, (int) (tScale * 148)), 5);
        }
    }

    @Override
    public String getNeiTransferRectString() {
        return mNEI;
    }

    @Override
    public String getNeiTransferRectTooltip() {
        return "Recipes";
    }

    @Override
    public Object[] getNeiTransferRectArgs() {
        return new Object[0];
    }

    @Override
    public Rectangle getNeiTransferRect() {
        return new Rectangle(149, -7, 18, 18);
    }
}
