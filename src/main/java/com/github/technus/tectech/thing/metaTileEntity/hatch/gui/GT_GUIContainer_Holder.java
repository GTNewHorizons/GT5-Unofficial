package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Tec on 09.04.2017.
 */
public class GT_GUIContainer_Holder extends GT_GUIContainerMetaTile_Machine {
    private final String mName;

    public GT_GUIContainer_Holder(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new GT_Container_Holder(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/holder.png");
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(mName, 8, 4, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        if (mContainer != null) {
            if (((GT_Container_Holder) mContainer).mActive == 1) {
                drawTexturedModalRect(x + 151, y + 23, 183, 23, 18, 18);
            }
        }
    }
}
