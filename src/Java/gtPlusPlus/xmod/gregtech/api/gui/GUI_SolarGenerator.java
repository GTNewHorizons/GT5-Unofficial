package gtPlusPlus.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

public class GUI_SolarGenerator extends GT_GUIContainerMetaTile_Machine {
	public GUI_SolarGenerator(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity,
			final String aTextureName, final int aSteamCapacity) {
		super(new CONTAINER_SolarGenerator(aInventoryPlayer, aTileEntity, aSteamCapacity),
				"gregtech:textures/gui/" + aTextureName);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		if (this.mContainer != null) {
			int tScale = ((CONTAINER_SolarGenerator) this.mContainer).mProcessingEnergy;
			if (tScale > 0) {
				this.drawTexturedModalRect(x + 70, y + 25 + 54 - tScale, 194, 54 - tScale, 10, tScale);
			}
			tScale = ((CONTAINER_SolarGenerator) this.mContainer).mEnergy;
			if (tScale > 0) {
				this.drawTexturedModalRect(x + 83, y + 25 + 54 - tScale, 204, 54 - tScale, 10, tScale);
			}
			tScale = ((CONTAINER_SolarGenerator) this.mContainer).mTemperature;
			if (tScale > 0) {
				this.drawTexturedModalRect(x + 96, y + 25 + 54 - tScale, 214, 54 - tScale, 10, tScale);
			}
			tScale = ((CONTAINER_SolarGenerator) this.mContainer).mProcessingEnergy;
			if (tScale > 0) {
				this.drawTexturedModalRect(x + 117, y + 44 + 14 - tScale, 177, 14 - tScale, 15, tScale + 1);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
		this.fontRendererObj.drawString("Solar Generator", 8, 4, 4210752);
	}
}
