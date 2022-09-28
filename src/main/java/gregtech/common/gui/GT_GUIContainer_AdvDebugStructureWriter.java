package gregtech.common.gui;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;
import static net.minecraft.util.StatCollector.translateToLocal;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.gui.widgets.GT_GuiSlotTooltip;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_AdvDebugStructureWriter extends GT_GUIContainerMetaTile_Machine {

    private static final String ADVDEBUGSTRUCTUREWRITER_PRINT_TOOLTIP =
            "GT5U.machines.advdebugstructurewriter.gui.print.tooltip";
    private static final String ADVDEBUGSTRUCTUREWRITER_TRANSPOSE_TOOLTIP =
            "GT5U.machines.advdebugstructurewriter.gui.transpose.tooltip";
    private static final String ADVDEBUGSTRUCTUREWRITER_HIGHLIGHT_TOOLTIP =
            "GT5U.machines.advdebugstructurewriter.gui.highlight.tooltip";

    public GT_GUIContainer_AdvDebugStructureWriter(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(
                new GT_Container_AdvDebugStructureWriter(aInventoryPlayer, aTileEntity),
                RES_PATH_GUI + "AdvDebugStructureWriter.png");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        if (mContainer != null) {
            GT_Container_AdvDebugStructureWriter dsw = (GT_Container_AdvDebugStructureWriter) mContainer;
            if (dsw.numbers == null) {
                return;
            }
            fontRendererObj.drawString(
                    translateToLocal("GT5U.machines.advdebugstructurewriter.gui.origin"), 46, 8, 16448255);
            fontRendererObj.drawString("A: " + dsw.numbers[0], 46, 18, 16448255);
            fontRendererObj.drawString("B: " + dsw.numbers[1], 46, 26, 16448255);
            fontRendererObj.drawString("C: " + dsw.numbers[2], 46, 34, 16448255);

            fontRendererObj.drawString(
                    translateToLocal("GT5U.machines.advdebugstructurewriter.gui.size"), 46, 60, 16448255);
            fontRendererObj.drawString("A: " + dsw.numbers[3], 46, 70, 16448255);
            fontRendererObj.drawString("B: " + dsw.numbers[4], 46, 78, 16448255);
            fontRendererObj.drawString("C: " + dsw.numbers[5], 46, 86, 16448255);
        }
    }

    @Override
    protected void setupTooltips() {
        addToolTip(new GT_GuiSlotTooltip(
                getContainer().printSlot, mTooltipCache.getData(ADVDEBUGSTRUCTUREWRITER_PRINT_TOOLTIP)));
        addToolTip(new GT_GuiSlotTooltip(
                getContainer().transposeSlot, mTooltipCache.getData(ADVDEBUGSTRUCTUREWRITER_TRANSPOSE_TOOLTIP)));
        addToolTip(new GT_GuiSlotTooltip(
                getContainer().highlightSlot, mTooltipCache.getData(ADVDEBUGSTRUCTUREWRITER_HIGHLIGHT_TOOLTIP)));
    }

    private GT_Container_AdvDebugStructureWriter getContainer() {
        return ((GT_Container_AdvDebugStructureWriter) this.mContainer);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        if (mContainer != null) {
            if (((GT_Container_AdvDebugStructureWriter) mContainer).transpose) {
                drawTexturedModalRect(x + 32, y + 128, 176, 0, 18, 18);
            }
            if (((GT_Container_AdvDebugStructureWriter) mContainer).showHighlightBox) {
                drawTexturedModalRect(x + 53, y + 128, 176, 18, 18, 18);
            }
        }
    }
}
