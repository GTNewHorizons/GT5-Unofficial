package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

public class GUI_Cyclotron extends GT_GUIContainerMetaTile_Machine
{
    public final String mNEI;
    public final String mName;
    
    public GUI_Cyclotron(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final String aName, final String aNEI) {
        super(new GT_Container_MultiMachine(aInventoryPlayer, aTileEntity, false), "gregtech:textures/gui/multimachines/" + "FusionComputer.png");
        this.mName = aName;
        this.mNEI = aNEI;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
        this.fontRendererObj.drawString(this.mName, 8, -10, 16448255);
        if (this.mContainer != null) {
            if ((((GT_Container_MultiMachine)this.mContainer).mDisplayErrorCode & 0x40) != 0x0) {
                this.fontRendererObj.drawString("Incomplete Structure.", 10, 8, 16448255);
            }
            if (((GT_Container_MultiMachine)this.mContainer).mDisplayErrorCode == 0) {
                if (((GT_Container_MultiMachine)this.mContainer).mActive == 0) {
                    this.fontRendererObj.drawString("Hit with Soft Hammer to (re-)start the Machine if it doesn't start.", -70, 170, 16448255);
                }
                else {
                    this.fontRendererObj.drawString("Running perfectly.", 10, 170, 16448255);
                }
            }            
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        final int x = (this.width - this.xSize) / 2;
        final int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);        
    }
}
