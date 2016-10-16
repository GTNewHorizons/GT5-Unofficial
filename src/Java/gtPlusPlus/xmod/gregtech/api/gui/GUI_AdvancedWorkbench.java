package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.player.InventoryPlayer;

public class GUI_AdvancedWorkbench
extends GT_GUIContainerMetaTile_Machine
{
	public GUI_AdvancedWorkbench(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity)
	{
		super(new CONTAINER_AdvancedWorkbench(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + "AdvancedCraftingTable.png");
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		//this.fontRendererObj.drawString("Condenser", 8, 4, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}
