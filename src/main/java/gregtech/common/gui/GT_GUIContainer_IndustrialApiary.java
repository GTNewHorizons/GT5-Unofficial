package gregtech.common.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.gui.widgets.GT_GuiSmartTooltip;
import gregtech.api.gui.widgets.GT_GuiTooltip;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

import java.awt.*;

public class GT_GUIContainer_IndustrialApiary extends GT_GUIContainerMetaTile_Machine {

    GT_GuiTooltip mErrorStatesToolTip;

    public GT_GUIContainer_IndustrialApiary(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_IndustrialApiary(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/basicmachines/IndustrialApiary.png");
    }

    @Override
    protected void setupTooltips() {
        Rectangle tProblemArea = new Rectangle(this.guiLeft + 100, this.guiTop + 63, 9, 18);
        addToolTip(mErrorStatesToolTip = new GT_GuiTooltip(tProblemArea, "") {
            @Override
            protected void onTick() {
                return;
            }
        });
        mErrorStatesToolTip.enabled = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float parTicks) {
        mErrorStatesToolTip.enabled = !getContainer().mErrorStates.isEmpty();
        if(mErrorStatesToolTip.enabled)  mErrorStatesToolTip.setToolTipText(getContainer().mErrorStates.toArray(new String[0]));
        super.drawScreen(mouseX, mouseY, parTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRendererObj.drawString("Industrial Apiary", 8, 4, 4210752);
        this.fontRendererObj.drawString("x", 28, 61, 4210752);
        this.fontRendererObj.drawString((1 << getContainer().mSpeed) + "", 26, 72, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        if (this.mContainer != null) {
            if (getContainer().mItemTransfer)
                drawTexturedModalRect(x + 7, y + 62, 176, 18, 18, 18);
            if(getContainer().mMaxProgressTime > 0) {
                double p = (double) getContainer().mProgressTime / getContainer().mMaxProgressTime;
                drawTexturedModalRect(x+65, y+5, 177, 1, (int)(p*19), 17);
            }
            if(mErrorStatesToolTip.enabled)
                drawTexturedModalRect(x+100, y+63, 177, 37, 8, 18);
        }
    }

    private GT_Container_IndustrialApiary getContainer(){
        return ((GT_Container_IndustrialApiary) this.mContainer);
    }


}
