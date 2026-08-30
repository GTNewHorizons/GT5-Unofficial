package tectech.rendering.EOH;

import static tectech.rendering.EOH.EOHRenderingUtils.renderOuterSpaceShell;

import java.awt.Color;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import gregtech.common.render.shader.RenderState;
import tectech.Reference;
import tectech.thing.block.TileEntityEyeOfHarmony;
import tectech.voidcraft.uss.USSStarRenderType;

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

        // Pass 12: the space-shell radius is a per-machine property (legacy EoH 12.95, Voidcraft USS 27.1 since pass
        // 15).
        renderOuterSpaceShell(eyeModel, te.getDomeRadius());
        renderOrbitObjects(te, time);
        // USS machines (an explicit planet system) render the star over the neutral-gray USS base layers with the
        // star class's registered color; legacy EoH machines keep the orange legacy layers + default orange tint.
        if (te.hasExplicitPlanets()) {
            EOHRenderingUtils.renderUSSStar(
                eyeModel,
                IItemRenderer.ItemRenderType.INVENTORY,
                new Color(te.getStarColor()),
                te.getStarShellColor(),
                time,
                te.getStarSize(),
                te.isStarHalo());
            // Custom render treatments (the star's registered render type): the magnetar's magnetic dipole loops
            // pass through the core — drawn AFTER the star body so the body's written depth occludes the far arcs
            // (that is what makes the field lines read as entering and exiting the core).
            if (te.getStarRenderType() == USSStarRenderType.MAGNETAR) {
                EOHRenderingUtils.renderMagnetarFieldLoops(
                    eyeModel,
                    time,
                    te.getStarSize(),
                    te.getStarColor() != 0 ? te.getStarColor() : te.getStarShellColor());
            }
            // Dyson Swarm (the infrastructure pass): the star's satellite swarm renders as a near-opaque gray
            // triangle shell with a dark blue core in each panel, filling with the satellite count.
            if (te.getSwarmCount() > 0L) {
                EOHRenderingUtils.renderUSSDysonSwarm(
                    eyeModel,
                    time,
                    (float) te.getStarSize(),
                    te.getSwarmCount(),
                    te.getSwarmCapacity());
            }
        } else {
            EOHRenderingUtils.renderEOHStar(eyeModel, IItemRenderer.ItemRenderType.INVENTORY, time, te.getStarSize());
        }

        RenderState.restore(GL11.GL_BLEND, blendWas);
    }

    private void renderOrbitObjects(TileEntityEyeOfHarmony te, float time) {

        // Phase 4 pass 5.1: an EXPLICIT planet system (USS) renders as self-contained tinted spheres — it never
        // falls back to the legacy lazy random fill, and does not depend on the legacy orbit shader or the IORE
        // dimension blocks resolving (the USS planet colors carry the look).
        if (te.hasExplicitPlanets()) {
            EOHRenderingUtils.renderUSSOrbits(eyeModel, te.getPlanetSpecs(), time, (float) te.getStarSize());
            return;
        }

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
