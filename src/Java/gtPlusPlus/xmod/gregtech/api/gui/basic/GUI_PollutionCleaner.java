package gtPlusPlus.xmod.gregtech.api.gui.basic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_ControlCore;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaAtmosphericReconditioner;

public class GUI_PollutionCleaner extends GT_GUIContainerMetaTile_Machine {
	public final String mName;
	public final String mNEI;
	public final byte mProgressBarDirection;
	public final byte mProgressBarAmount;
	public int mReduction;

	public GUI_PollutionCleaner(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity,
			final String aName, final String aTextureFile) {
		this(aInventoryPlayer, aTileEntity, aName, aTextureFile, "PollutionCleaner",(byte) 0, (byte) 1);
	}

	public GUI_PollutionCleaner(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity,
			final String aName, final String aTextureFile, final String aNEI, final byte aProgressBarDirection,
			final byte aProgressBarAmount) {
		super(new CONTAINER_PollutionCleaner(aInventoryPlayer, aTileEntity), CORE.RES_PATH_GUI + "PollutionCleaner.png");
		this.mProgressBarDirection = aProgressBarDirection;
		this.mProgressBarAmount = (byte) Math.max(1, aProgressBarAmount);
		this.mName = aName;
		this.mNEI = aNEI;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		this.fontRendererObj.drawString(this.mName, 8, 4, 4210752);
		this.drawTooltip(par1, par2);
	}

	private void drawTooltip(final int x2, final int y2) {
		final int xStart = (this.width - this.xSize) / 2;
		final int yStart = (this.height - this.ySize) / 2;
		final int x3 = x2 - xStart;
		final int y3 = y2 - yStart + 5;
		final List<String> list = new ArrayList<String>();
		if (y3 >= 67 && y3 <= 84) {
			if (x3 >= 7 && x3 <= 24) {
				list.add("Fluid Auto-Output");
			}
			if (x3 >= 25 && x3 <= 42) {
				list.add("Item Auto-Output");
			}
			if (x3 >= 77 && x3 <= 95) {					
				//Do Dumb shit				
				CONTAINER_PollutionCleaner aContainerCast = (CONTAINER_PollutionCleaner) this.mContainer;				
				mReduction = aContainerCast.mReduction;
				list.add("Reduction: "+mReduction);	
			}
		}
		if (!list.isEmpty()) {
			this.drawHoveringText(list, x3, y3, this.fontRendererObj);
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