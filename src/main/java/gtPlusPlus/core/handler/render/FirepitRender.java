package gtPlusPlus.core.handler.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
public class FirepitRender extends TileEntitySpecialRenderer{
	ResourceLocation texture = new ResourceLocation("miscutils" + ":"+ "textures/blocks/FirePit/mossyFirepit.png");

	private final FirepitModel model;

	public FirepitRender(){
		this.model = new FirepitModel();
	}

	private void adjustRotatePivotViaMeta(final World world, final int x, final int y, final int z) {
		final int meta = world.getBlockMetadata(x, y, z);
		GL11.glPushMatrix();
		GL11.glRotatef(meta * (-90), 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(final TileEntity entity, final double x, final double y, final double z, final float i) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.1F, (float)y + 1.0F, (float)z + 0.5F);
		GL11.glRotatef(180, 0F, 0F, 1F);

		this.bindTexture(this.texture);
		GL11.glPushMatrix();
		this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glTranslatef(1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	//Set the lighting stuff, so it changes it's brightness properly.
	private void adjustLightFixture(final World world, final int i, final int j, final int k, final Block block) {
		final Tessellator tess = Tessellator.instance;
		//float brightness = block.getBlockBrightness(world, i, j, k);
		//As of MC 1.7+ block.getBlockBrightness() has become block.getLightValue():
		final float brightness = block.getLightValue(world, i, j, k);
		final int skyLight = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
		final int modulousModifier = skyLight % 65536;
		final int divModifier = skyLight / 65536;
		tess.setColorOpaque_F(brightness, brightness, brightness);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,  modulousModifier,  divModifier);
	}
}
