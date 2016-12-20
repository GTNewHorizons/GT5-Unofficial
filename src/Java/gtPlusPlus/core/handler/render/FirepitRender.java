package gtPlusPlus.core.handler.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
public class FirepitRender extends TileEntitySpecialRenderer{
	ResourceLocation texture = new ResourceLocation("miscutils" + ":"+ "textures/blocks/FirePit/mossyFirepit.png");

	private FirepitModel model;

	public FirepitRender(){
		this.model = new FirepitModel();
	}

	private void adjustRotatePivotViaMeta(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		GL11.glPushMatrix();
		GL11.glRotatef(meta * (-90), 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float i) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.1F, (float)y + 1.0F, (float)z + 0.5F);
		GL11.glRotatef(180, 0F, 0F, 1F);

		this.bindTexture(texture);
		GL11.glPushMatrix();
		this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	//Set the lighting stuff, so it changes it's brightness properly.      
	private void adjustLightFixture(World world, int i, int j, int k, Block block) {
		Tessellator tess = Tessellator.instance;
		//float brightness = block.getBlockBrightness(world, i, j, k);
		//As of MC 1.7+ block.getBlockBrightness() has become block.getLightValue():
		float brightness = block.getLightValue(world, i, j, k);
		int skyLight = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
		int modulousModifier = skyLight % 65536;
		int divModifier = skyLight / 65536;
		tess.setColorOpaque_F(brightness, brightness, brightness);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,  (float) modulousModifier,  divModifier);
	}
}
