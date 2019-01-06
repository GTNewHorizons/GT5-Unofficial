package gtPlusPlus.core.client.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.client.model.ModelDecayChest;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderDecayChest extends TileEntitySpecialRenderer {

	private static final ResourceLocation mChestTexture = new ResourceLocation(CORE.MODID, "textures/blocks/TileEntities/DecayablesChest_full.png");
	private ModelDecayChest mChestModel = new ModelDecayChest();
	
	public static RenderDecayChest INSTANCE;
	public final int mRenderID;

	public RenderDecayChest() {
			INSTANCE = this;
			this.mRenderID = RenderingRegistry.getNextAvailableRenderId();
			Logger.INFO("Registered Lead Lined Chest Renderer.");		
	}
	
	public void renderTileEntityAt(TileEntityDecayablesChest p_147500_1_, double p_147500_2_, double p_147500_4_,
			double p_147500_6_, float p_147500_8_) {
		
		int i = 0;

		if (true) {
			this.bindTexture(mChestTexture);
			GL11.glPushMatrix();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) p_147500_2_, (float) p_147500_4_ + 1.0F, (float) p_147500_6_ + 1.0F);
			GL11.glScalef(1.0F, -1.0F, -1.0F);
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			short short1 = 0;

			if (i == 2) {
				short1 = 180;
			}

			if (i == 3) {
				short1 = 0;
			}

			if (i == 4) {
				short1 = 90;
			}

			if (i == 5) {
				short1 = -90;
			}

			GL11.glRotatef((float) short1, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			float f1 = p_147500_1_.prevLidAngle + (p_147500_1_.lidAngle - p_147500_1_.prevLidAngle) * p_147500_8_;			

			f1 = 1.0F - f1;
			f1 = 1.0F - f1 * f1 * f1;
			mChestModel.chestLid.rotateAngleX = -(f1 * CORE.PI / 2.0F);
			mChestModel.renderAll();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_,
			float p_147500_8_) {
		this.renderTileEntityAt((TileEntityDecayablesChest) p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
	}
}