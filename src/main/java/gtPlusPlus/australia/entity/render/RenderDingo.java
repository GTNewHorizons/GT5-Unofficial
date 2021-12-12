package gtPlusPlus.australia.entity.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.australia.entity.type.EntityDingo;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderDingo extends RenderLiving {

	private static final ResourceLocation dingoTextures = new ResourceLocation(
			CORE.MODID + ":" + "textures/entity/australia/dingo/dingo.png");
	private static final ResourceLocation tamedDingoTextures = new ResourceLocation(
			CORE.MODID + ":" + "textures/entity/australia/dingo/wdingo_tame.png");
	private static final ResourceLocation anrgyDingoTextures = new ResourceLocation(
			CORE.MODID + ":" + "textures/entity/australia/dingo/dingo_angry.png");
	private static final ResourceLocation dingoCollarTextures = new ResourceLocation(
			CORE.MODID + ":" + "textures/entity/australia/wdingoolf/dingo_collar.png");

	public RenderDingo(ModelBase p_i1269_1_, ModelBase p_i1269_2_, float p_i1269_3_) {
		super(p_i1269_1_, p_i1269_3_);
		this.setRenderPassModel(p_i1269_2_);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float handleRotationFloat(EntityDingo p_77044_1_, float p_77044_2_) {
		return p_77044_1_.getTailRotation();
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityDingo p_77032_1_, int p_77032_2_, float p_77032_3_) {
		if (p_77032_2_ == 0 && p_77032_1_.getWolfShaking()) {
			float f1 = p_77032_1_.getBrightness(p_77032_3_) * p_77032_1_.getShadingWhileShaking(p_77032_3_);
			this.bindTexture(dingoTextures);
			GL11.glColor3f(f1, f1, f1);
			return 1;
		} else if (p_77032_2_ == 1 && p_77032_1_.isTamed()) {
			this.bindTexture(dingoCollarTextures);
			int j = p_77032_1_.getCollarColor();
			GL11.glColor3f(EntitySheep.fleeceColorTable[j][0], EntitySheep.fleeceColorTable[j][1],
					EntitySheep.fleeceColorTable[j][2]);
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless
	 * you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityDingo p_110775_1_) {
		return p_110775_1_.isTamed() ? tamedDingoTextures
				: (p_110775_1_.isAngry() ? anrgyDingoTextures : dingoTextures);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_) {
		return this.shouldRenderPass((EntityDingo) p_77032_1_, p_77032_2_, p_77032_3_);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_) {
		return this.handleRotationFloat((EntityDingo) p_77044_1_, p_77044_2_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless
	 * you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityDingo) p_110775_1_);
	}

}