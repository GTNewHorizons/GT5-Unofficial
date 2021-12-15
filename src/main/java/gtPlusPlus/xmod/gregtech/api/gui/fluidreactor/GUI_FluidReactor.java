package gtPlusPlus.xmod.gregtech.api.gui.fluidreactor;

import gregtech.api.gui.GT_Container_BasicMachine;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.lib.CORE;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.InventoryPlayer;

public class GUI_FluidReactor extends GT_GUIContainerMetaTile_Machine {
	public final String mName;
	public final String mNEI;
	public final byte mProgressBarDirection;
	public final byte mProgressBarAmount;

	public GUI_FluidReactor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName,
			String aTextureFile, String aNEI) {
		this(aInventoryPlayer, aTileEntity, aName, aTextureFile, aNEI, (byte) 0, (byte) 1);
	}

	public GUI_FluidReactor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName,
			String aTextureFile, String aNEI, byte aProgressBarDirection, byte aProgressBarAmount) {
		super(new Container_FluidReactor(aInventoryPlayer, aTileEntity), CORE.MODID+":textures/gui/FluidReactor.png");
		this.mProgressBarDirection = aProgressBarDirection;
		this.mProgressBarAmount = (byte) Math.max(1, aProgressBarAmount);
		this.mName = aName;
		this.mNEI = aNEI;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRendererObj.drawString(this.mName, 82, 5, 4210752);
		this.drawTooltip(par1, par2);
	}

	private void drawTooltip(int x2, int y2) {
		int xStart = (this.width - this.xSize) / 2;
		int yStart = (this.height - this.ySize) / 2;
		int x = x2 - xStart;
		int y = y2 - yStart + 5;
		List<String> list = new ArrayList();
		if (y >= 67 && y <= 84) {
			if (x >= 7 && x <= 24) {
				list.add("Fluid 1 Auto-Output");
			}
			if (x >= 25 && x <= 42) {
				list.add("Fluid 2 Auto-Output");
			}
			if (x >= 43 && x <= 61) {
				list.add("Item Auto-Output");
			}
		}

		if (!list.isEmpty()) {
			this.drawHoveringText(list, x, y, this.fontRendererObj);
		}

	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		if (this.mContainer != null) {
			if (((Container_FluidReactor) this.mContainer).mFluidTransfer_1) {
				this.drawTexturedModalRect(x + 7, y + 62, 176, 18, 18, 18);
			}
			if (((Container_FluidReactor) this.mContainer).mFluidTransfer_2) {
				this.drawTexturedModalRect(x + 25, y + 62, 194, 18, 18, 18);
			}
			if (((GT_Container_BasicMachine) this.mContainer).mItemTransfer) {
				this.drawTexturedModalRect(x + 43, y + 62, 176, 36, 18, 18);
			}

			if (((GT_Container_BasicMachine) this.mContainer).mStuttering) {
				this.drawTexturedModalRect(x + 79, y + 44, 176, 54, 18, 18);
			}

			if (this.mContainer.mMaxProgressTime > 0) {
				int tSize = this.mProgressBarDirection < 2 ? 20 : 18;
				int tProgress = Math
						.max(1, Math
								.min(tSize * this.mProgressBarAmount,
										(this.mContainer.mProgressTime > 0 ? 1 : 0) + this.mContainer.mProgressTime
												* tSize * this.mProgressBarAmount / this.mContainer.mMaxProgressTime))
						% (tSize + 1);
				switch (this.mProgressBarDirection) {
					case 0 :
						this.drawTexturedModalRect(x + 82, y + 24, 176, 0, tProgress, 18);
						break;
					case 1 :
						this.drawTexturedModalRect(x + 82 + 20 - tProgress, y + 24, 196 - tProgress, 0, tProgress, 18);
						break;
					case 2 :
						this.drawTexturedModalRect(x + 82, y + 24, 176, 0, 20, tProgress);
						break;
					case 3 :
						this.drawTexturedModalRect(x + 82, y + 24 + 18 - tProgress, 176, 18 - tProgress, 20, tProgress);
						break;
					case 4 :
						tProgress = 20 - tProgress;
						this.drawTexturedModalRect(x + 82, y + 24, 176, 0, tProgress, 18);
						break;
					case 5 :
						tProgress = 20 - tProgress;
						this.drawTexturedModalRect(x + 82 + 20 - tProgress, y + 24, 196 - tProgress, 0, tProgress, 18);
						break;
					case 6 :
						tProgress = 18 - tProgress;
						this.drawTexturedModalRect(x + 82, y + 24, 176, 0, 20, tProgress);
						break;
					case 7 :
						tProgress = 18 - tProgress;
						this.drawTexturedModalRect(x + 82, y + 24 + 18 - tProgress, 176, 18 - tProgress, 20, tProgress);
				}
			}
		}

	}
}