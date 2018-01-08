package gtPlusPlus.core.client.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.entity.EntityPrimedMiningExplosive;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderMiningExplosivesPrimed extends RenderTNTPrimed {
	private final RenderBlocks blockRenderer = new RenderBlocks();

	public RenderMiningExplosivesPrimed(){
		this.shadowSize = 0.5F;
		Logger.INFO("Rendering Mining Explosion. 1");
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(final EntityPrimedMiningExplosive entity, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_){
		Logger.INFO("Rendering Mining Explosion. 2");
		GL11.glPushMatrix();
		GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
		float f2;

		if (((entity.fuse - p_76986_9_) + 1.0F) < 10.0F)
		{
			f2 = 1.0F - (((entity.fuse - p_76986_9_) + 1.0F) / 10.0F);

			if (f2 < 0.0F)
			{
				f2 = 0.0F;
			}

			if (f2 > 1.0F)
			{
				f2 = 1.0F;
			}

			f2 *= f2;
			f2 *= f2;
			final float f3 = 1.0F + (f2 * 0.3F);
			GL11.glScalef(f3, f3, f3);
		}

		f2 = (1.0F - (((entity.fuse - p_76986_9_) + 1.0F) / 100.0F)) * 0.8F;
		this.bindEntityTexture(entity);
		this.blockRenderer.renderBlockAsItem(ModBlocks.blockMiningExplosive, 0, entity.getBrightness(p_76986_9_));

		if (((entity.fuse / 5) % 2) == 0)
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f2);
			this.blockRenderer.renderBlockAsItem(ModBlocks.blockMiningExplosive, 0, 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		GL11.glPopMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(final EntityPrimedMiningExplosive p_110775_1_){
		return TextureMap.locationBlocksTexture;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(final Entity p_110775_1_){
		Logger.INFO("Rendering Mining Explosion. 4");
		return this.getEntityTexture((EntityPrimedMiningExplosive)p_110775_1_);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_){
		Logger.INFO("Rendering Mining Explosion. 3");
		this.doRender((EntityPrimedMiningExplosive)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}