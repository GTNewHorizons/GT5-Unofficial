package gtPlusPlus.xmod.thaumcraft.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.xmod.thaumcraft.common.tile.TileFastAlchemyFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import thaumcraft.client.lib.UtilsFX;

@SideOnly(Side.CLIENT)
public class GuiFastAlchemyFurnace extends GuiContainer {
	private final TileFastAlchemyFurnace furnaceInventory;

	public GuiFastAlchemyFurnace(final InventoryPlayer par1InventoryPlayer,
			final TileFastAlchemyFurnace par2TileEntityFurnace) {
		super(new ContainerFastAlchemyFurnace(par1InventoryPlayer, par2TileEntityFurnace));
		this.furnaceInventory = par2TileEntityFurnace;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		UtilsFX.bindTexture("textures/gui/gui_alchemyfurnace.png");
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		GL11.glEnable(3042);
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		if (this.furnaceInventory.isBurning()) {
			final int i1 = this.furnaceInventory.getBurnTimeRemainingScaled(20);
			this.drawTexturedModalRect(k + 80, (l + 26 + 20) - i1, 176, 20 - i1, 16, i1);
		}
		int i1 = this.furnaceInventory.getCookProgressScaled(46);
		this.drawTexturedModalRect(k + 106, (l + 13 + 46) - i1, 216, 46 - i1, 9, i1);
		i1 = this.furnaceInventory.getContentsScaled(48);
		this.drawTexturedModalRect(k + 61, (l + 12 + 48) - i1, 200, 48 - i1, 8, i1);
		this.drawTexturedModalRect(k + 60, l + 8, 232, 0, 10, 55);
		GL11.glDisable(3042);
	}
}