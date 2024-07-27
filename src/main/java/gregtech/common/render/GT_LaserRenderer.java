package gregtech.common.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.common.tileentities.render.TileLaser;

// TODO purge the line model

public class GT_LaserRenderer extends TileEntitySpecialRenderer {

    private double zOffset = 0.0F;
    private double xOffset = 0.0F;

    private static final IModelCustom lasermodel = AdvancedModelLoader
        .loadModel(new ResourceLocation("gregtech", "textures/model/laser.obj"));

    public GT_LaserRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileLaser.class, this);
    }

    private void maths(float counter) {
        float tc = (0.05F * counter);
        zOffset = 0.5 + 0.45 * Math.sin(2 * Math.PI * tc);
        xOffset = 0.5 + 0.45 * Math.sin(0.5 * Math.PI * tc);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        final TileLaser ltile = (TileLaser) tile;
        if (ltile.getShouldRender()) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(ltile.getRed(), ltile.getGreen(), ltile.getBlue(), 0.6F); // change this for your colour
            GL11.glLineWidth(5F);
            GL11.glDepthMask(false);

            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawing(GL11.GL_LINES);
            if (ltile.realism) tessellator.addVertex(x + xOffset, y + 4, z + zOffset);
            else tessellator.addVertex(x + 0.5, y + 4, z + 0.5);

            tessellator.addVertex(x + xOffset, y + 0.5, z + zOffset);
            maths(ltile.counter);
            ltile.counter += 0.6F;
            if (ltile.counter >= 80) {
                ltile.counter = 0;
            }
            tessellator.draw();

            GL11.glDepthMask(true);
            GL11.glPopAttrib();
        }
        if (ltile.getShouldRender()) {
            /*
             * GL11.glPushMatrix();
             * GL11.glDrawElements(GL11.GL_LINE, 1, GL11.GL_UNSIGNED_INT, 1);
             * GL11.glTranslated(x + xOffset, y + 0.5, z + zOffset);
             * maths(ltile.counter);
             * ltile.counter += 0.2F;
             * if (ltile.counter >= 80) {
             * ltile.counter = 0;
             * }
             * GL11.glColor3f(ltile.getRed(), ltile.getGreen(), ltile.getBlue());
             * GL11.glDisable(GL11.GL_LIGHTING);
             * GL11.glEnable(GL11.GL_BLEND);
             * bindTexture(new ResourceLocation("gregtech", "textures/model/laser.png"));
             * lasermodel.renderAll();
             * GL11.glPopMatrix();
             */
        }
    }
}
