package gregtech.common.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.common.tileentities.render.TileLaser;

public class GT_LaserRenderer extends TileEntitySpecialRenderer {

    private double zOffset = 0.0;
    private double xOffset = 0.0;

    // Relative to block size
    private double lineRadius = 0.03;

    // Originally was 0.6F
    private float lineOpacity = 0.7F;

    public GT_LaserRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileLaser.class, this);
    }

    private void maths(float counter) {
        float tc = (0.05F * counter);
        zOffset = 0.5 + 0.45 * Math.sin(2 * Math.PI * tc);
        xOffset = 0.5 + 0.45 * Math.sin(0.5 * Math.PI * tc);
    }

    private void renderFakeLine(TileLaser laser, double x1, double y1, double z1, double x2, double y2, double z2) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(laser.getRed(), laser.getGreen(), laser.getBlue(), lineOpacity);
        tessellator.addVertex(x1 - lineRadius, y1, z1);
        tessellator.addVertex(x1 + lineRadius, y1, z1);
        tessellator.addVertex(x2 + lineRadius, y2, z2);
        tessellator.addVertex(x2 - lineRadius, y2, z2);

        tessellator.addVertex(x1, y1, z1 - lineRadius);
        tessellator.addVertex(x1, y1, z1 + lineRadius);
        tessellator.addVertex(x2, y2, z2 + lineRadius);
        tessellator.addVertex(x2, y2, z2 - lineRadius);
        tessellator.draw();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        final TileLaser ltile = (TileLaser) tile;

        if (ltile.getShouldRender()) {
            // Push GL state
            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);

            // Full brightness on this thing (Emits glow with shaders)
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.f, 240.f);

            if (ltile.realism) {
                renderFakeLine(ltile, x + xOffset, y + 4.0, z + zOffset, x + xOffset, y + 0.5, z + zOffset);
            } else {
                renderFakeLine(ltile, x + 0.5, y + 4.0, z + 0.5, x + xOffset, y + 0.5, z + zOffset);
            }

            // Pop GL state
            GL11.glPopAttrib();
            GL11.glPopMatrix();

            // Movement calculations
            maths(ltile.counter);
            ltile.counter += 0.6F;
            if (ltile.counter >= 80) {
                ltile.counter = 0;
            }
        }
    }
}
