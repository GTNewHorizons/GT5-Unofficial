package gregtech.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.entity.EntityDrone;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.MathHelper;
import gregtech.common.render.DroneRender;

@SideOnly(Side.CLIENT)
public class RenderDrone extends Render {

    public RenderDrone() {
        this.shadowSize = 0.5F;
        this.renderManager = RenderManager.instance;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        if (entity instanceof EntityDrone drone) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.renderViewEntity instanceof EntityDrone && entity != mc.renderViewEntity) {
                if (!drone.isAutoMode()) {
                    return;
                }
                double dx = entity.posX - mc.renderViewEntity.posX;
                double dy = entity.posY - mc.renderViewEntity.posY;
                double dz = entity.posZ - mc.renderViewEntity.posZ;
                if (dx * dx + dy * dy + dz * dz < 0.01) {
                    return;
                }
            }

            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            GL11.glRotated(180.0D - yaw, 0.0D, 1.0D, 0.0D);
            if (drone.isAutoMode()) {
                float pitch = drone.prevRotationPitch + (drone.rotationPitch - drone.prevRotationPitch) * partialTicks;
                GL11.glRotated(pitch, 1.0D, 0.0D, 0.0D);
            }

            GL11.glScalef(0.3F, 0.3F, 0.3F);

            int bx = MathHelper.floor_double(drone.posX);
            int bz = MathHelper.floor_double(drone.posZ);
            if (drone.worldObj.blockExists(bx, 0, bz)) {
                int by = MathHelper.floor_double(drone.posY + 0.5D);
                int light = drone.worldObj.getLightBrightnessForSkyBlocks(bx, Math.max(0, Math.min(255, by)), bz, 0);
                int u = light % 65536;
                int v = light / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) u, (float) v);
            }

            int level = drone.getDroneLevel();
            if (level <= 0) {
                level = 1;
            }
            DroneRender.renderDrone(-0.5, -0.5, -0.5, partialTicks, level);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        if (entity instanceof EntityDrone drone) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.renderViewEntity instanceof EntityDrone && entity != mc.renderViewEntity) {
                if (!drone.isAutoMode()) {
                    return;
                }
            }
        }
        super.doRenderShadowAndFire(entity, x, y, z, yaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
