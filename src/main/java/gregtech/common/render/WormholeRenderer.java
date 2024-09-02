package gregtech.common.render;

import static tectech.rendering.EOH.EOHRenderingUtils.addRenderedBlockInWorld;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.common.tileentities.render.TileEntityWormhole;

public class WormholeRenderer extends TileEntitySpecialRenderer {

    public WormholeRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWormhole.class, this);
    }

    private static final double trimPercentage = .95;
    private static final double corePercentage = trimPercentage / Math.sqrt(3);

    private static void render(Block coreBlock, double rotation) {

        GL11.glPushMatrix();
        GL11.glRotated(rotation, 2, 1, 0);
        GL11.glScaled(-1, -1, -1);

        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setColorOpaque_F(1f, 1f, 1f);
        addRenderedBlockInWorld(Blocks.quartz_block, 0, 0, 0, 0);
        Tessellator.instance.draw();

        GL11.glScaled(trimPercentage, trimPercentage, trimPercentage);

        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setColorOpaque_F(0.1f, 0.1f, 0.1f);
        addRenderedBlockInWorld(Blocks.coal_block, 0, 0, 0, 0);
        Tessellator.instance.draw();
        GL11.glPopMatrix();

        if (coreBlock != null) {
            GL11.glPushMatrix();
            GL11.glScaled(corePercentage, corePercentage, corePercentage);
            GL11.glRotated(rotation, 0, -2, .1);
            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setColorOpaque_F(1f, 1f, 1f);
            addRenderedBlockInWorld(coreBlock, 0, 0, 0, 0);
            Tessellator.instance.draw();
            GL11.glPopMatrix();
        }

    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        if (tile instanceof TileEntityWormhole wTile) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glScaled(wTile.targetRadius, wTile.targetRadius, wTile.targetRadius);
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glDisable(GL11.GL_LIGHTING);

            double rotationTimer = wTile.getWorldObj()
                .getWorldInfo()
                .getWorldTotalTime() + timeSinceLastTick;

            this.bindTexture(TextureMap.locationBlocksTexture);
            render(wTile.getBlock(), rotationTimer);

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }
}
