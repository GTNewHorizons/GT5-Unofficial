package gregtech.common.gui;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;
import static net.minecraft.util.StatCollector.translateToLocal;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_AdvDebugStructureWriter extends GT_GUIContainerMetaTile_Machine {
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
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        drawTooltip(par1, par2);
    }

    private void drawTooltip(int x2, int y2) {
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        int x = x2 - xStart;
        int y = y2 - yStart + 5;
        List<String> list = new ArrayList<>();
        if (y >= 129 && y <= 147) {
            if (x >= 12 && x <= 30) {
                list.add(translateToLocal("GT5U.machines.advdebugstructurewriter.gui.print"));
            } else if (x >= 33 && x <= 51) {
                list.add(translateToLocal("GT5U.machines.advdebugstructurewriter.gui.transpose"));
            } else if (x >= 54 && x <= 72) {
                list.add(translateToLocal("GT5U.machines.advdebugstructurewriter.gui.highlight"));
            }
        }
        if (!list.isEmpty()) drawHoveringText(list, x2, y2, fontRendererObj);
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
