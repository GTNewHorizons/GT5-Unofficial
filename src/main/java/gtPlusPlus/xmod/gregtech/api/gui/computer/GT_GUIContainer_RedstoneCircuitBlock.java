package gtPlusPlus.xmod.gregtech.api.gui.computer;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.GregTech_API;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_CircuitryBehavior;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_RedstoneCircuitBlock extends GT_GUIContainerMetaTile_Machine {

	public GT_GUIContainer_RedstoneCircuitBlock(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(new GT_Container_RedstoneCircuitBlock(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + "RedstoneCircuitBlock.png");
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		GT_CircuitryBehavior tCircuit = GregTech_API.sCircuitryBehaviors.get(((GT_Container_RedstoneCircuitBlock) mContainer).mGate);
		if (tCircuit != null) {
			this.fontRendererObj.drawString(tCircuit.getName(), 46, 8, 16448255);
			this.fontRendererObj.drawString(tCircuit.getDescription(), 46, 19, 16448255);

			this.fontRendererObj.drawString(tCircuit.getDataDescription(((GT_Container_RedstoneCircuitBlock) mContainer).mData, 0), 46, 33, 16448255);
			this.fontRendererObj.drawString(tCircuit.getDataDescription(((GT_Container_RedstoneCircuitBlock) mContainer).mData, 1), 46, 44, 16448255);
			this.fontRendererObj.drawString(tCircuit.getDataDescription(((GT_Container_RedstoneCircuitBlock) mContainer).mData, 2), 46, 55, 16448255);
			this.fontRendererObj.drawString(tCircuit.getDataDescription(((GT_Container_RedstoneCircuitBlock) mContainer).mData, 3), 46, 66, 16448255);

			String tString;
			tString = tCircuit.getDataDisplay(((GT_Container_RedstoneCircuitBlock) mContainer).mData, 0);
			this.fontRendererObj.drawString(tString == null ? GT_Utility.parseNumberToString(((GT_Container_RedstoneCircuitBlock) mContainer).mData[0]) : tString, 99, 33, 16448255);
			tString = tCircuit.getDataDisplay(((GT_Container_RedstoneCircuitBlock) mContainer).mData, 1);
			this.fontRendererObj.drawString(tString == null ? GT_Utility.parseNumberToString(((GT_Container_RedstoneCircuitBlock) mContainer).mData[1]) : tString, 99, 44, 16448255);
			tString = tCircuit.getDataDisplay(((GT_Container_RedstoneCircuitBlock) mContainer).mData, 2);
			this.fontRendererObj.drawString(tString == null ? GT_Utility.parseNumberToString(((GT_Container_RedstoneCircuitBlock) mContainer).mData[2]) : tString, 99, 55, 16448255);
			tString = tCircuit.getDataDisplay(((GT_Container_RedstoneCircuitBlock) mContainer).mData, 3);
			this.fontRendererObj.drawString(tString == null ? GT_Utility.parseNumberToString(((GT_Container_RedstoneCircuitBlock) mContainer).mData[3]) : tString, 99, 66, 16448255);
		}
		this.drawTooltip(par1, par2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if (mContainer != null) {
			if (((GT_Container_RedstoneCircuitBlock) mContainer).mOutput > 0)
				drawTexturedModalRect(x + 151, y + 5, 176, 0, 18, 18);
			if ((((GT_Container_RedstoneCircuitBlock) mContainer).mActive & 1) > 0)
				drawTexturedModalRect(x + 151, y + 23, 176, 18, 18, 18);
			if (((GT_Container_RedstoneCircuitBlock) mContainer).mDisplayErrorCode > 0)
				if ((((GT_Container_RedstoneCircuitBlock) mContainer).mTileEntity.getTimer() / 5) % 2 == 0)
					drawTexturedModalRect(x + 140, y + 9, 194, 0, 7, 7);
				else
					;
			else
				drawTexturedModalRect(x + 140, y + 9, 201, 0, 7, 7);
		}
	}

	private void drawTooltip(final int x2, final int y2) {
		final int xStart = (this.width - this.xSize) / 2;
		final int yStart = (this.height - this.ySize) / 2;
		final int x3 = x2 - xStart;
		final int y3 = y2 - yStart + 5;
		final List<String> list = new ArrayList<String>();

		int y = 10;
		if (x3 >= 151 && x3 <= 168) {
			if (y3 >= y && y3 <= (y + 17)) {
				list.add("Toggle EU Output");
			}
			if (y3 >= (y + 18) && y3 <= (y + 35)) {
				list.add("Toggle Active State");
			}
			if (y3 >= (y + 36) && y3 <= (y + 53)) {
				list.add("Change Redstone Circuit");
			}
		}

		if (!list.isEmpty()) {
	        //RenderHelper.enableGUIStandardItemLighting();
			this.drawHoveringText(list, x3, y3, this.fontRendererObj);
	        RenderHelper.enableGUIStandardItemLighting();
		}
        //RenderHelper.enableStandardItemLighting();
	}
}
