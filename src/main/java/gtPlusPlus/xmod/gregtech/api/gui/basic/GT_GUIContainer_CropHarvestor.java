package gtPlusPlus.xmod.gregtech.api.gui.basic;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.gui.GT_Container_Boiler;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_CropHarvestor extends GT_GUIContainerMetaTile_Machine {

	private final String mLocalName;

	public GT_GUIContainer_CropHarvestor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aLocalName) {
		super(new GT_Container_CropHarvestor(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + "CropHarvestor.png");
		mLocalName = aLocalName;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		drawTooltip(par1, par2);
		//fontRendererObj.drawString("Crop Manager ("+this.mContainer.mTileEntity.getInputVoltage()+"v)", 8, 4, 4210752);
		//fontRendererObj.drawString(mLocalName, 70, 69, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if (((GT_Container_CropHarvestor) mContainer).mModeAlternative) {
			drawTexturedModalRect(x + 48, y + 64, 177, 37, 16, 16);
		}

		int tScale = ((GT_Container_CropHarvestor) this.mContainer).mWaterAmount;
		if (tScale > 0) {
			drawTexturedModalRect(x + 47, y + 61 - tScale, 204, 54 - tScale, 10, tScale);
		};

	}

	private void drawTooltip(int x2, int y2) {
		int xStart = (width - xSize) / 2;
		int yStart = (height - ySize) / 2;
		int x = x2 - xStart;
		int y = y2 - yStart + 5;
		List<String> list = new ArrayList<>();
		if (y >= 68 && y <= 85) {
			if (x >= 47 && x <= 64) {
				if (((GT_Container_CropHarvestor) mContainer).mModeAlternative) {
					list.add("Disable Hydration/Fertilizing/Weed-EX");                	
				}
				else {
					list.add("Enable Hydration/Fertilizing/Weed-EX");                	
				}
			}
		}
		if (y >= 12 && y <= 66) {
			if (x >= 47 && x <= 56) {
				int aWater = ((GT_Container_CropHarvestor) mContainer).mWaterRealAmount;
				list.add("Water: "+aWater+"L / "+((GT_Container_CropHarvestor) mContainer).mTileEntity.getMetaTileEntity().getCapacity()+"L");
			}
		}
		if (!list.isEmpty()) {
			drawHoveringText(list, x, y, fontRendererObj);
	        RenderHelper.enableGUIStandardItemLighting();
		}
	}

}
