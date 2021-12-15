package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.player.InventoryPlayer;

public class GUI_MultiMachine_Default extends GT_GUIContainerMetaTile_Machine
{
    String mName;
    
    public GUI_MultiMachine_Default(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final String aName, final String aTextureFile) {
        super(new CONTAINER_MultiMachine(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + ((aTextureFile == null) ? "MultiblockDisplay" : aTextureFile));
        this.mName = "";
        this.mName = aName;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
        this.fontRendererObj.drawString(this.mName, 10, 8, 16448255);
        if (this.mContainer != null) {
            if ((((CONTAINER_MultiMachine)this.mContainer).mDisplayErrorCode & 0x1) != 0x0) {
                this.fontRendererObj.drawString(this.trans("132", "Pipe is loose."), 10, 16, 16448255);
            }
            if ((((CONTAINER_MultiMachine)this.mContainer).mDisplayErrorCode & 0x2) != 0x0) {
                this.fontRendererObj.drawString(this.trans("133", "Screws are missing."), 10, 24, 16448255);
            }
            if ((((CONTAINER_MultiMachine)this.mContainer).mDisplayErrorCode & 0x4) != 0x0) {
                this.fontRendererObj.drawString(this.trans("134", "Something is stuck."), 10, 32, 16448255);
            }
            if ((((CONTAINER_MultiMachine)this.mContainer).mDisplayErrorCode & 0x8) != 0x0) {
                this.fontRendererObj.drawString(this.trans("135", "Platings are dented."), 10, 40, 16448255);
            }
            if ((((CONTAINER_MultiMachine)this.mContainer).mDisplayErrorCode & 0x10) != 0x0) {
                this.fontRendererObj.drawString(this.trans("136", "Circuitry burned out."), 10, 48, 16448255);
            }
            if ((((CONTAINER_MultiMachine)this.mContainer).mDisplayErrorCode & 0x20) != 0x0) {
                this.fontRendererObj.drawString(this.trans("137", "That doesn't belong there."), 10, 56, 16448255);
            }
            if ((((CONTAINER_MultiMachine)this.mContainer).mDisplayErrorCode & 0x40) != 0x0) {
                this.fontRendererObj.drawString(this.trans("138", "Incomplete Structure."), 10, 64, 16448255);
            }
            if (((CONTAINER_MultiMachine)this.mContainer).mDisplayErrorCode == 0) {
                if (((CONTAINER_MultiMachine)this.mContainer).mActive == 0) {
                    this.fontRendererObj.drawString(this.trans("139", "Hit with Soft Hammer"), 10, 16, 16448255);
                    this.fontRendererObj.drawString(this.trans("140", "to (re-)start the Machine"), 10, 24, 16448255);
                    this.fontRendererObj.drawString(this.trans("141", "if it doesn't start."), 10, 32, 16448255);
                }
                else {
                    this.fontRendererObj.drawString(this.trans("142", "Running perfectly."), 10, 16, 16448255);
                }
            }
        }
    }
    
    public String trans(final String aKey, final String aEnglish) {
        return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aKey, aEnglish, false);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        final int x = (this.width - this.xSize) / 2;
        final int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
