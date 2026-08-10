package tectech.rendering.EOH;

import static tectech.rendering.EOH.EOHRenderingUtils.renderOuterSpaceShell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import gregtech.common.render.shader.RenderState;
import tectech.Reference;
import tectech.thing.block.TileEntityEyeOfHarmony;

public class EOHTileEntitySR extends TileEntitySpecialRenderer {

    public static final ResourceLocation STAR_LAYER_0 = new ResourceLocation(Reference.MODID, "models/StarLayer0.png");
    public static final ResourceLocation STAR_LAYER_1 = new ResourceLocation(Reference.MODID, "models/StarLayer1.png");
    public static final ResourceLocation STAR_LAYER_2 = new ResourceLocation(Reference.MODID, "models/StarLayer2.png");

    private static final float STAR_RESCALE = 0.2f;
    private static final float SPEED_SCALE = 0.1f; // keep your old tuning

    private static final Matrix4f eyeModel = new Matrix4f();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {

        if (!(tile instanceof TileEntityEyeOfHarmony te)) return;

        World world = te.getWorldObj();
        if (world == null) return; // Just in-case

        // Smooth global animation clock
        float time = world.getTotalWorldTime() + partialTicks;

        eyeModel.translation((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);

        final boolean blendWas = GL11.glGetBoolean(GL11.GL_BLEND);

        GL11.glDisable(GL11.GL_BLEND);

        // For LOD calculations.
        EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
        renderOuterSpaceShell(eyeModel, player.getDistance(x, y, z));
        renderOrbitObjects(te, time);
        EOHRenderingUtils.renderEOHStar(eyeModel, IItemRenderer.ItemRenderType.INVENTORY, time, te.getStarSize());

        RenderState.restore(GL11.GL_BLEND, blendWas);
    }

    private void renderOrbitObjects(TileEntityEyeOfHarmony te, float time) {

        var objects = te.getOrbitingObjects();

        if (objects == null || objects.isEmpty()) {
            te.generateImportantInfo();
            objects = te.getOrbitingObjects();

            if (objects == null || objects.isEmpty()) return;
        }

        bindTexture(TextureMap.locationBlocksTexture);
        EOHRenderingUtils.renderOrbits(eyeModel, objects, time, (float) te.getStarSize(), SPEED_SCALE, STAR_RESCALE);
    }
}
