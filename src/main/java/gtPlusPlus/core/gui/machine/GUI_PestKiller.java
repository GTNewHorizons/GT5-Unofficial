package gtPlusPlus.core.gui.machine;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.container.Container_PestKiller;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.tileentities.machines.TileEntityPestKiller;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;

@SideOnly(Side.CLIENT)
public class GUI_PestKiller extends GuiContainer {

	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(CORE.MODID, "textures/gui/PestKiller.png");
	private final TileEntityPestKiller mTileKiller;

	public GUI_PestKiller(final InventoryPlayer player_inventory, final TileEntityPestKiller te) {
		super(new Container_PestKiller(player_inventory, te));		
		mTileKiller = te;	
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int i, final int j) {
		if (mTileKiller != null) {
			this.fontRendererObj.drawString(I18n.format(mTileKiller.getInventoryName(), new Object[0]), 4, 6, 4210752);
			drawFluidTank(mTileKiller.getTank(), 134, 35);			
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int i, final int j) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.renderEngine.bindTexture(craftingTableGuiTextures);
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

	// This method is called when the Gui is first called!
	@Override
	public void initGui() {
		super.initGui();
	}

	private void drawFluidTank(IFluidTank tank, int x, int y) {
		Color startGrad = new Color(50, 50, 50);
		Color endGrad = new Color(20, 20, 20);
		Container_PestKiller aCont = (Container_PestKiller) this.inventorySlots;
		
		double aPercentage = 0;
		double aDivisor = (100/16);		
		int aFrameHeight = 16;		
		
		boolean didRender = false;
		if (aCont != null) {
			TileEntityPestKiller aTile = mTileKiller;
			if (aTile != null) {
				FluidTank aTank = aTile.getTank();
				int aTier = aTile.getTier();
				drawGradientRect(x, y, x+16, y+16, startGrad.getRGB(), endGrad.getRGB());
				if (aTier <= 0 || aTier > 2) {					
					if (aTank != null && aTank.getFluidAmount() > 0) {
						aPercentage = MathUtils.findPercentage(aTank.getFluidAmount(), aTank.getCapacity());	
						//Logger.INFO("Percent = "+aPercentage);
						aFrameHeight = (int) (aPercentage / aDivisor);	
						//Logger.INFO("Frame Height = "+aFrameHeight);
					}					
					this.fontRendererObj.drawString("Tier: 0", 4, 18, 4210752);
					this.fontRendererObj.drawString("Range: 1x1", 4, 30, 4210752);
					this.fontRendererObj.drawString("Poison: None", 4, 42, 4210752);
					this.fontRendererObj.drawString("Amount: 0", 4, 64, 4210752);
					didRender = true;
				}
				else if (aTier == 1) {
					if (aTank != null && aTank.getFluidAmount() > 0) {
						aPercentage = MathUtils.findPercentage(aTank.getFluidAmount(), aTank.getCapacity());	
						//Logger.INFO("Percent = "+aPercentage);
						aFrameHeight = (int) (aPercentage / aDivisor);	
						//Logger.INFO("Frame Height = "+aFrameHeight);
					}
					startGrad = new Color(240, 50, 240);
					endGrad = new Color(130, 30, 130);
					drawGradientRect(x, y+(16-aFrameHeight), x+16, y+16, startGrad.getRGB(), endGrad.getRGB());
					this.fontRendererObj.drawString("Tier: 1", 4, 18, 4210752);
					this.fontRendererObj.drawString("Range: 5x5", 4, 30, 4210752);
					this.fontRendererObj.drawString("Poison: ", 4, 42, 4210752);
					this.fontRendererObj.drawString(""+aTile.getTank().getFluid().getLocalizedName(), 4, 54, 4210752);
					this.fontRendererObj.drawString("Amount: "+aTile.getTank().getFluidAmount(), 4, 64, 4210752);
					didRender = true;
				}
				else if (aTier == 2) {
					if (aTank != null && aTank.getFluidAmount() > 0) {
						aPercentage = MathUtils.findPercentage(aTank.getFluidAmount(), aTank.getCapacity());	
						//Logger.INFO("Percent = "+aPercentage);
						aFrameHeight = (int) (aPercentage / aDivisor);	
						//Logger.INFO("Frame Height = "+aFrameHeight);
					}
					short[] aRGB = MISC_MATERIALS.HYDROGEN_CYANIDE.getRGB();
					startGrad = new Color(aRGB[0], aRGB[1], aRGB[2]);
					endGrad = new Color(Math.max(aRGB[0], 0), Math.max(aRGB[1], 0), Math.max(aRGB[2], 0));
					drawGradientRect(x, y+(16-aFrameHeight), x+16, y+16, startGrad.getRGB(), endGrad.getRGB());
					this.fontRendererObj.drawString("Tier: 2", 4, 18, 4210752);
					this.fontRendererObj.drawString("Range: 9x9", 4, 30, 4210752);
					this.fontRendererObj.drawString("Poison: ", 4, 42, 4210752);
					this.fontRendererObj.drawString(""+aTile.getTank().getFluid().getLocalizedName(), 4, 54, 4210752);
					this.fontRendererObj.drawString("Amount: "+aTile.getTank().getFluidAmount(), 4, 64, 4210752);
					didRender = true;
				}
			}
		}
		if (!didRender) {
			startGrad = new Color(255, 30, 120);
			endGrad = new Color(255, 0, 50);
			drawGradientRect(x, y, x+16, y+16, startGrad.getRGB(), endGrad.getRGB());
			this.fontRendererObj.drawString("Tier: 0", 4, 18, 4210752);
		}
		
		
		
		
		
		
		/*
		 * FluidStack fluid = tank.getFluid(); TextureManager manager =
		 * mc.getTextureManager(); if (fluid != null) {
		 * manager.bindTexture(manager.getResourceLocation(0)); float amount =
		 * fluid.amount; float capacity = tank.getCapacity(); float scale = amount /
		 * capacity; int fluidTankHeight = 60; int fluidAmount = (int) (scale *
		 * fluidTankHeight); drawFluid(x, y + fluidTankHeight - fluidAmount,
		 * fluid.getFluid().getIcon(fluid), 16, fluidAmount); }
		 */
	}

	private void drawFluid(int x, int y, IIcon icon, int width, int height) {
		int i = 0;
		int j = 0;
		int drawHeight = 0;
		int drawWidth = 0;
		for (i = 0; i < width; i += 16) {
			for (j = 0; j < height; j += 16) {
				drawWidth = Math.min(width - i, 16);
				drawHeight = Math.min(height - j, 16);
				drawTexturedModelRectFromIcon(x + i, y + j, icon, drawWidth, drawHeight);
			}
		}
	}

}