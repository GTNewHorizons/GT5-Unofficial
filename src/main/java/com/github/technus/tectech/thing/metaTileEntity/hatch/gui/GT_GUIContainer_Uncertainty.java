package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import com.github.technus.tectech.TecTech;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

import static com.github.technus.tectech.TecTech.proxy;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public class GT_GUIContainer_Uncertainty extends GT_GUIContainerMetaTile_Machine {
    public GT_GUIContainer_Uncertainty(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_Uncertainty(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "Uncertainty.png");
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        proxy.renderUnicodeString("Schrödinger", 46, 7, 167, 0xffffff);
        if (this.mContainer != null && ((GT_Container_Uncertainty) this.mContainer).status == 0)
            proxy.renderUnicodeString("Status: OK", 46, 16, 167, 0xffffff);
        else
            proxy.renderUnicodeString("Status: NG", 46, 16, 167, 0xffffff);
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        if (this.mContainer != null && ((GT_Container_Uncertainty) this.mContainer).matrix != null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            final short bU = 0, rU = 70, fU = 192, V = 210, Vs = 216;
            x += 52;
            y += 33;
            final int state = ((GT_Container_Uncertainty) this.mContainer).status;
            switch (((GT_Container_Uncertainty) this.mContainer).mode) {
                case 1://ooo oxo ooo
                    drawTexturedModalRect(x + 12, y + 12,
                            rU + (state == 0 ? 76 : 12),
                            Vs + 12, 10, 10);
                    break;
                case 2://ooo xox ooo
                    drawTexturedModalRect(x, y + 12,
                            rU + ((state & 1) == 0 ? 64 : 0),
                            Vs + 12, 10, 10);
                    drawTexturedModalRect(x + 24, y + 12,
                            rU + ((state & 2) == 0 ? 88 : 24),
                            Vs + 12, 10, 10);
                    break;
                case 3://oxo xox oxo
                    drawTexturedModalRect(x + 12, y,
                            rU + ((state & 1) == 0 ? 76 : 12),
                            Vs, 10, 10);
                    drawTexturedModalRect(x, y + 12,
                            rU + ((state & 2) == 0 ? 64 : 0),
                            Vs + 12, 10, 10);
                    drawTexturedModalRect(x + 24, y + 12,
                            rU + ((state & 4) == 0 ? 88 : 24),
                            Vs + 12, 10, 10);
                    drawTexturedModalRect(x + 12, y + 24,
                            rU + ((state & 8) == 0 ? 76 : 12),
                            Vs + 24, 10, 10);
                    break;
                case 4://xox ooo xox
                    drawTexturedModalRect(x, y,
                            rU + ((state & 1) == 0 ? 64 : 0),
                            Vs, 10, 10);
                    drawTexturedModalRect(x + 24, y,
                            rU + ((state & 2) == 0 ? 88 : 24),
                            Vs, 10, 10);
                    drawTexturedModalRect(x, y + 24,
                            rU + ((state & 4) == 0 ? 64 : 0),
                            Vs + 24, 10, 10);
                    drawTexturedModalRect(x + 24, y + 24,
                            rU + ((state & 8) == 0 ? 88 : 24),
                            Vs + 24, 10, 10);
                    break;
                case 5://xox ooo xox
                    drawTexturedModalRect(x, y,
                            rU + ((state & 1) == 0 ? 64 : 0),
                            Vs, 10, 10);
                    drawTexturedModalRect(x + 24, y,
                            rU + ((state & 2) == 0 ? 88 : 24),
                            Vs, 10, 10);
                    drawTexturedModalRect(x + 12, y + 12,
                            rU + ((state & 4) == 0 ? 76 : 12),
                            Vs + 12, 10, 10);
                    drawTexturedModalRect(x, y + 24,
                            rU + ((state & 8) == 0 ? 64 : 0),
                            Vs + 24, 10, 10);
                    drawTexturedModalRect(x + 24, y + 24,
                            rU + ((state & 16) == 0 ? 88 : 24),
                            Vs + 24, 10, 10);
                    break;
            }
            x -= 6;
            y -= 6;
            for (int i = 0; i < 16; i++)
                if (TecTech.Rnd.nextInt(1000) < ((GT_Container_Uncertainty) this.mContainer).matrix[i])
                    drawTexturedModalRect(x + 12 * (i / 4), y + 12 * (i % 4),
                            fU + 12 * (i / 4), V + 12 * (i % 4), 10, 10);
            if (((GT_Container_Uncertainty) this.mContainer).selection > -1) {
                int sel = ((GT_Container_Uncertainty) this.mContainer).selection;
                drawTexturedModalRect(x + 12 * (sel / 4), y + 12 * (sel % 4),
                        bU + 12 * (sel / 4), V + 12 * (sel % 4), 10, 10);
            }
        }
    }
}
