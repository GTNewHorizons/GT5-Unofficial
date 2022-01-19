package gtPlusPlus.xmod.gregtech.api.gui.automation;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.basic.GT_Container_CropHarvestor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_ElectricAutoWorkbench extends GT_GUIContainerMetaTile_Machine {
	
	private static final String[] mModeText = new String[] {"Normal Crafting Table", "???", "1x1", "2x2", "3x3", "Unifier", "Dust", "???", "Hammer?", "Circle"};
	
    public GT_GUIContainer_ElectricAutoWorkbench(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_ElectricAutoWorkbench(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + "ElectricAutoWorkbench.png");
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		drawTooltip(par1, par2);    	
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    	super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
		if (mContainer != null) {			
			int tMode = ((GT_Container_ElectricAutoWorkbench)mContainer).mMode;
			if (tMode != 0) drawTexturedModalRect(x + 120, y + 40, tMode*18, 166, 18, 18);
			tMode = ((GT_Container_ElectricAutoWorkbench)mContainer).mThroughPut;
			drawTexturedModalRect(x + 120, y + 4, tMode*18, 184, 18, 18);
		}
    }
    
	private void drawTooltip(final int x2, final int y2) {
		final int xStart = (this.width - this.xSize) / 2;
		final int yStart = (this.height - this.ySize) / 2;
		final int x3 = x2 - xStart;
		final int y3 = y2 - yStart + 5;
		List<String> list = new ArrayList<>();
		String[] mModeText = new String[] {"Normal Crafting Table", "???", "1x1", "2x2", "3x3", "Unifier", "Dust", "???", "Hammer?", "Circle"};
		if (y3 >= 45 && y3 <= 62) {
			if (x3 >= 120 && x3 <= 137) {
				list.add("Mode: "+mModeText[((GT_Container_ElectricAutoWorkbench) mContainer).mMode]);
				/*switch (((GT_Container_ElectricAutoWorkbench) mContainer).mMode) {
					case 0:
						list.add("Mode: ");						
				}*/
			}
		}
		if (!list.isEmpty()) {
			drawHoveringText(list, x3, y3, fontRendererObj);
	        RenderHelper.enableGUIStandardItemLighting();
		}
	}
}
