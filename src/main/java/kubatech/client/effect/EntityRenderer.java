package kubatech.client.effect;

import static net.minecraft.client.renderer.entity.RenderManager.*;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW_STACK_DEPTH;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kubatech.api.utils.MobUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class EntityRenderer extends EntityFX {
    private EntityLiving entityToRender = null;

    public EntityRenderer(World p_i1218_1_, double x, double y, double z, int age) {
        super(p_i1218_1_, x + 0.5d, y, z + 0.5d);
        this.particleMaxAge = age;
        this.particleAge = 0;
    }

    public EntityRenderer(EntityRenderer r, int age) {
        super(r.worldObj, r.posX, r.posY, r.posZ);
        this.particleMaxAge = age;
        this.particleAge = 0;
        this.ticksExisted = r.ticksExisted;
        this.entityToRender = r.entityToRender;
    }

    @Override
    protected void entityInit() {}

    @Override
    public void onUpdate() {
        if (this.entityToRender == null) return;
        this.ticksExisted++;
        if (ticksExisted % 20 == 0) entityToRender.hurtTime = 10;
        else if (entityToRender.hurtTime > 0) entityToRender.hurtTime--;
        if (this.particleAge++ == this.particleMaxAge) {
            this.setDead();
        }
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 3;
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    public void setEntity(EntityLiving entity) {
        this.entityToRender = entity;
    }

    @Override
    public void renderParticle(
            Tessellator p_70539_1_,
            float p_70539_2_,
            float p_70539_3_,
            float p_70539_4_,
            float p_70539_5_,
            float p_70539_6_,
            float p_70539_7_) {
        if (entityToRender == null) return;

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        entityToRender.worldObj = this.worldObj;
        entityToRender.setPosition(this.posX, this.posY, this.posZ);

        double rotation;
        // double headrotation;
        {
            double x1 = this.posX;
            double x2 = Minecraft.getMinecraft().thePlayer.posX;
            double y1 = this.posZ;
            double y2 = Minecraft.getMinecraft().thePlayer.posZ;
            double k = Math.toDegrees(Math.atan2(x2 - x1, y1 - y2));
            if (k < 0d) k += 360d;
            k -= 180d;
            rotation = k;
        }
        /*
               {
                   double y1 = this.posY;
                   double y2 = Minecraft.getMinecraft().thePlayer.posY;
                   double d = Minecraft.getMinecraft()
                           .thePlayer
                           .getDistance(this.posX, Minecraft.getMinecraft().thePlayer.posY, this.posZ);
                   double k = Math.toDegrees(Math.atan2(y1 - y2, d));
                   if (k < 0d) k += 360d;
                   headrotation = k;
               }

        */

        entityToRender.prevRotationYawHead = entityToRender.rotationYawHead;
        entityToRender.prevRenderYawOffset = entityToRender.renderYawOffset;
        // entityToRender.prevRotationPitch = entityToRender.rotationPitch;
        entityToRender.renderYawOffset = (float) rotation;
        entityToRender.rotationYawHead = (float) rotation;
        // entityToRender.rotationPitch = (float)headrotation;

        float p_147936_2_ = 0.5f;

        float f1 = entityToRender.prevRotationYaw
                + (entityToRender.rotationYaw - entityToRender.prevRotationYaw) * p_147936_2_;
        int i = entityToRender.getBrightnessForRender(p_147936_2_);

        if (entityToRender.isBurning()) {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
        GL11.glColor4f(1f, 1f, 1f, 1F);
        RenderHelper.enableStandardItemLighting();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        int stackdepth = GL11.glGetInteger(GL_MODELVIEW_STACK_DEPTH);
        GL11.glPushMatrix();
        GL11.glTranslatef(
                (float) (this.posX - renderPosX), (float) (this.posY - renderPosY), (float) (this.posZ - renderPosZ));
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float desiredScale = MobUtils.getDesiredScale(entityToRender, 2f);
        if (desiredScale < 1f) GL11.glScalef(desiredScale, desiredScale, desiredScale);
        try {
            instance.renderEntityWithPosYaw(entityToRender, 0f, 0f, 0f, f1, p_147936_2_);
        } catch (Throwable ex) {
            Tessellator tes = Tessellator.instance;
            try {
                tes.draw();
            } catch (Exception ignored) {
            }
        }

        stackdepth -= GL11.glGetInteger(GL_MODELVIEW_STACK_DEPTH);
        if (stackdepth < 0) for (; stackdepth < 0; stackdepth++) GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
    }
}
