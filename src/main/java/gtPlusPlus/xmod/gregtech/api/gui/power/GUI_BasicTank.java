package gtPlusPlus.xmod.gregtech.api.gui.power;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GUI_BasicTank extends GT_GUIContainerMetaTile_Machine {
	private final String mName;

	public GUI_BasicTank(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
		super(new CONTAINER_BasicTank(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/BasicTank.png");
		this.mName = aName;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2,
				4210752);
		this.fontRendererObj.drawString(this.mName, 8, 6, 4210752);
		if (this.mContainer != null) {
			this.fontRendererObj.drawString("Liquid Amount", 10, 20, 16448255);
			this.fontRendererObj.drawString(
					GT_Utility.parseNumberToString(((CONTAINER_BasicTank) this.mContainer).mContent), 10, 30,
					16448255);
		}

	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
}