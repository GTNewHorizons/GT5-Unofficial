package gregtech.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.entity.EntityCamera;
import gregtech.common.render.DroneRender;

@SideOnly(Side.CLIENT)
public class RenderCamera extends Render {

    public RenderCamera() {
        this.shadowSize = 0.5F;
        this.renderManager = RenderManager.instance;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        if (entity instanceof EntityCamera) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.renderViewEntity instanceof EntityCamera && entity != mc.renderViewEntity) {
                double dx = entity.posX - mc.renderViewEntity.posX;
                double dy = entity.posY - mc.renderViewEntity.posY;
                double dz = entity.posZ - mc.renderViewEntity.posZ;
                if (dx * dx + dy * dy + dz * dz < 0.01) {
                    return; // Do not render our own drone model
                }
            }
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            GL11.glRotated(180.0D - yaw, 0.0D, 1.0D, 0.0D);
            GL11.glScalef(0.3F, 0.3F, 0.3F);
            DroneRender.renderDrone(-0.5, -0.5, -0.5, partialTicks, 1);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
