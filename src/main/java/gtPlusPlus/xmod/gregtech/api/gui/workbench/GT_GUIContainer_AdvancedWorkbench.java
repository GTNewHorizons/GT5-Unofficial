package gtPlusPlus.xmod.gregtech.api.gui.workbench;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_AdvancedWorkbench extends GT_GUIContainerMetaTile_Machine {
	
	private final String mLocalName;
	
    public GT_GUIContainer_AdvancedWorkbench(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aLocalName) {
        super(new GT_Container_AdvancedWorkbench(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + "AdvancedCraftingTable.png");
        mLocalName = aLocalName;
    }
    
    public GT_GUIContainer_AdvancedWorkbench(GT_ContainerMetaTile_Machine aContainer, String aLocalName, String aResource) {
        super(aContainer, aResource);
        mLocalName = aLocalName;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        //fontRendererObj.drawString(mLocalName, 8, 4, 4210752);
		this.drawTooltip(par1, par2);
	}

	private void drawTooltip(final int x2, final int y2) {
		final int xStart = (this.width - this.xSize) / 2;
		final int yStart = (this.height - this.ySize) / 2;
		final int x3 = x2 - xStart;
		final int y3 = y2 - yStart + 5;
		final List<String> list = new ArrayList<String>();
		//154 - 172
		
		if (KeyboardUtils.isShiftKeyDown()) {
			if (y3 >= 30 && y3 <= 49) {			
				if (x3 >= 135 && x3 <= 154) {
					list.add("Blueprint Slot");
					list.add("Shift+Lmb Sets to crafting output");	
				}
				if (x3 >= 153 && x3 <= 170) {
					list.add("Extraction Slot");
					list.add("Things can always be pulled from here");	
				}
			}
			if (y3 >= 50 && y3 <= 67) {			
				if (x3 >= 135 && x3 <= 152) {
					list.add("Flush");	
					list.add("Empty crafting grid back to storage");	
				}
				if (x3 >= 153 && x3 <= 170) {
					list.add("Automation");	
					list.add("Allows output while");
					list.add("crafting grid is full");
				}
			}
			if (y3 >= 68 && y3 <= 85){			
				if (x3 >= 135 && x3 <= 152) {
					list.add("Output Slot");	
				}
				if (x3 >= 153 && x3 <= 170) {
					list.add("Free Parking");	
				}
			}
		}
		if (!list.isEmpty()) {
			this.drawHoveringText(list, x3, y3, this.fontRendererObj);
		}
	}
	
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    	super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
