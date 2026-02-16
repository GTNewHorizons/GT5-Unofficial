package gregtech.common.render;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.tileentities.render.RenderingTileEntityDrone;

@SideOnly(Side.CLIENT)
public class DroneRender extends TileEntitySpecialRenderer {

    private static final ResourceLocation DroneTexture = new ResourceLocation(GregTech.ID, "textures/model/drone.png");
    private static final IModelCustom Drone = AdvancedModelLoader
        .loadModel(new ResourceLocation(GregTech.ID, "textures/model/drone.obj"));

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof RenderingTileEntityDrone)) return;
        final float size = 1.0f;
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        renderDrone(size);
        renderBlade((Minecraft.getMinecraft().theWorld.getTotalWorldTime() + timeSinceLastTick) * 80f % 360f, size);
        GL11.glPopMatrix();
    }

    private void renderDrone(double size) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(DroneTexture);
        GL11.glScaled(size, size, size);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        Drone.renderOnly("drone", "box", "main");
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void renderBlade(float rotation, double size) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(DroneTexture);
        GL11.glScaled(size, size, size);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        GL11.glPushMatrix();
        GL11.glTranslated(-0.7d * size, -1 * size, -0.7 * size);
        GL11.glRotated(rotation, 0, 1, 0);
        GL11.glTranslated(0.7d * size, 1 * size, 0.7 * size);
        Drone.renderOnly("blade2");
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(-0.7d * size, -1 * size, 0.7 * size);
        GL11.glRotated(rotation, 0, 1, 0);
        GL11.glTranslated(0.7d * size, 1 * size, -0.7 * size);
        Drone.renderOnly("blade3");
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(0.7d * size, -1 * size, -0.7 * size);
        GL11.glRotated(rotation, 0, 1, 0);
        GL11.glTranslated(-0.7d * size, 1 * size, 0.7 * size);
        Drone.renderOnly("blade1");
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(0.7d * size, -1 * size, 0.7 * size);
        GL11.glRotated(rotation, 0, 1, 0);
        GL11.glTranslated(-0.7d * size, 1 * size, -0.7 * size);
        Drone.renderOnly("blade4");
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
