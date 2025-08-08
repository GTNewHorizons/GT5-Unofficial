package gtPlusPlus.core.client.renderer;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.client.model.ModelStaballoyConstruct;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;

@SideOnly(Side.CLIENT)
public class RenderStaballoyConstruct extends RenderLiving {

    private static final ResourceLocation staballoyGolemTextures = new ResourceLocation(
            GTPlusPlus.ID + ":" + "textures/entity/golemStaballoy.png");
    /** Staballoy Golem's Model. */
    private final ModelStaballoyConstruct staballoyGolemModel;

    public RenderStaballoyConstruct() {
        super(new ModelStaballoyConstruct(), 0.8F);
        this.staballoyGolemModel = (ModelStaballoyConstruct) this.mainModel;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityStaballoyConstruct p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
            float p_76986_8_, float p_76986_9_) {
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityStaballoyConstruct p_110775_1_) {
        return staballoyGolemTextures;
    }

    protected void rotateCorpse(EntityStaballoyConstruct p_77043_1_, float p_77043_2_, float p_77043_3_,
            float p_77043_4_) {
        super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);

        if (p_77043_1_.limbSwingAmount >= 0.01D) {
            float f3 = 13.0F;
            float f4 = p_77043_1_.limbSwing - p_77043_1_.limbSwingAmount * (1.0F - p_77043_4_) + 6.0F;
            float f5 = (Math.abs(f4 % f3 - f3 * 0.5F) - f3 * 0.25F) / (f3 * 0.25F);
            GL11.glRotatef(6.5F * f5, 0.0F, 0.0F, 1.0F);
        }
    }

    protected void renderEquippedItems(EntityStaballoyConstruct p_77029_1_, float p_77029_2_) {
        super.renderEquippedItems(p_77029_1_, p_77029_2_);

        if (p_77029_1_.getHoldRoseTick() != 0) {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glPushMatrix();
            GL11.glRotatef(
                    5.0F + 180.0F * this.staballoyGolemModel.ironGolemRightArm.rotateAngleX / (float) Math.PI,
                    1.0F,
                    0.0F,
                    0.0F);
            GL11.glTranslatef(-0.6875F, 1.25F, -0.9375F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            float f1 = 0.8F;
            GL11.glScalef(f1, -f1, f1);
            int i = p_77029_1_.getBrightnessForRender(p_77029_2_);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindTexture(TextureMap.locationBlocksTexture);
            this.field_147909_c.renderBlockAsItem(Blocks.red_flower, 0, 1.0F);
            GL11.glPopMatrix();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
            float p_76986_8_, float p_76986_9_) {
        this.doRender(
                (EntityStaballoyConstruct) p_76986_1_,
                p_76986_2_,
                p_76986_4_,
                p_76986_6_,
                p_76986_8_,
                p_76986_9_);
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_) {
        this.renderEquippedItems((EntityStaballoyConstruct) p_77029_1_, p_77029_2_);
    }

    @Override
    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        this.rotateCorpse((EntityStaballoyConstruct) p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
            float p_76986_8_, float p_76986_9_) {
        this.doRender(
                (EntityStaballoyConstruct) p_76986_1_,
                p_76986_2_,
                p_76986_4_,
                p_76986_6_,
                p_76986_8_,
                p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityStaballoyConstruct) p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,
            float p_76986_9_) {
        this.doRender(
                (EntityStaballoyConstruct) p_76986_1_,
                p_76986_2_,
                p_76986_4_,
                p_76986_6_,
                p_76986_8_,
                p_76986_9_);
    }
}
