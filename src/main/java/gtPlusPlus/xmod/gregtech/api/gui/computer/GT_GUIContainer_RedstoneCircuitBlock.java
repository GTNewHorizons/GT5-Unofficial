package gtPlusPlus.xmod.gregtech.api.gui.computer;

import gregtech.api.GregTech_API;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_CircuitryBehavior;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
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
}
