package gtPlusPlus.xmod.gregtech.api.gui;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GUI_DeluxeTank extends GT_GUIContainerMetaTile_Machine {

	private final String mName;

	public GUI_DeluxeTank(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity, final String aName) {
		super(new CONTAINER_DeluxeTank(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "BasicTank.png");
		this.mName = aName;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, (this.ySize - 96) + 2, 4210752);
		this.fontRendererObj.drawString(this.mName, 8, 6, 4210752);
		if (this.mContainer != null) {
			this.fontRendererObj.drawString("Fuel  | A | B |", 10, 20, 16448255);
			//fontRendererObj.drawString(GT_Utility.parseNumberToString(((CONTAINER_DeluxeTank) mContainer).mContent), 10, 30, 16448255);
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
