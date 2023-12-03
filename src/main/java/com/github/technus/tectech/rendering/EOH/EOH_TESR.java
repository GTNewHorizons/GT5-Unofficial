package com.github.technus.tectech.rendering.EOH;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.rendering.EOH.EOH_RenderingUtils.renderBlockInWorld;
import static com.github.technus.tectech.rendering.EOH.EOH_RenderingUtils.renderOuterSpaceShell;
import static com.github.technus.tectech.rendering.EOH.EOH_RenderingUtils.renderStar;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.github.technus.tectech.thing.block.TileEyeOfHarmony;

public class EOH_TESR extends TileEntitySpecialRenderer {

    public static final ResourceLocation STAR_LAYER_0 = new ResourceLocation(MODID, "models/StarLayer0.png");
    public static final ResourceLocation STAR_LAYER_1 = new ResourceLocation(MODID, "models/StarLayer1.png");
    public static final ResourceLocation STAR_LAYER_2 = new ResourceLocation(MODID, "models/StarLayer2.png");
    public static IModelCustom starModel;
    public static IModelCustom spaceModel;

    public EOH_TESR() {
        starModel = AdvancedModelLoader.loadModel(new ResourceLocation(MODID, "models/Star.obj"));
        spaceModel = AdvancedModelLoader.loadModel(new ResourceLocation(MODID, "models/Space.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEyeOfHarmony EOHRenderTile)) return;

        GL11.glPushMatrix();
        // Required to centre the render to the middle of the block.
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);

        // Space shell.
        renderOuterSpaceShell();

        // Render the planets.
        renderOrbitObjects(EOHRenderTile);

        // Render the star itself.
        renderStar(IItemRenderer.ItemRenderType.INVENTORY);
        GL11.glPopAttrib();

        GL11.glPopMatrix();
    }

    private void renderOrbitObjects(final TileEyeOfHarmony EOHRenderTile) {

        if (EOHRenderTile.getOrbitingObjects() != null) {

            if (EOHRenderTile.getOrbitingObjects().size() == 0) {
                EOHRenderTile.generateImportantInfo();
            }

            for (TileEyeOfHarmony.OrbitingObject t : EOHRenderTile.getOrbitingObjects()) {
                renderOrbit(EOHRenderTile, t);
            }
        }
    }

    void renderOrbit(final TileEyeOfHarmony EOHRenderTile, final TileEyeOfHarmony.OrbitingObject orbitingObject) {
        // Render orbiting body.
        GL11.glPushMatrix();

        GL11.glRotatef(orbitingObject.zAngle, 0, 0, 1);
        GL11.glRotatef(orbitingObject.xAngle, 1, 0, 0);
        GL11.glRotatef((orbitingObject.rotationSpeed * 0.1f * EOHRenderTile.angle) % 360.0f, 0F, 1F, 0F);
        GL11.glTranslated(-0.2 - orbitingObject.distance - STAR_RESCALE * EOHRenderTile.getSize(), 0, 0);
        GL11.glRotatef((orbitingObject.orbitSpeed * 0.1f * EOHRenderTile.angle) % 360.0f, 0F, 1F, 0F);

        this.bindTexture(TextureMap.locationBlocksTexture);
        renderBlockInWorld(orbitingObject.block, 0, orbitingObject.scale);

        GL11.glPopMatrix();
    }

    private static final float STAR_RESCALE = 0.2f;

}
