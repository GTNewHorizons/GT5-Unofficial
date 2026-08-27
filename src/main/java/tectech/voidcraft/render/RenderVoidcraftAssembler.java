package tectech.voidcraft.render;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import tectech.voidcraft.cover.CoverVoidcraftComponent;
import tectech.voidcraft.machine.MTEVoidcraftComponent;
import tectech.voidcraft.multiblock.VoidcraftMultiblockRegistry;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftComponent;

/**
 * World-last renderer for the two assemblers (voidcraft + voidbase):
 * <ul>
 * <li>while the machine's own structure is valid — a transparent, pulsing cyan wireframe around the scan
 * area;</li>
 * <li>while digitizing — two transparent pulsing cyan planes sweeping from both ends of the volume towards
 * the middle;</li>
 * <li>whenever the volume holds components — a rotating 1/4-scale cyan hologram of the current build
 * behind the machine, even while the machine is idle or the build is invalid (the building-progress
 * preview). Its location is set by {@link #PREVIEW_DISTANCE} / {@link #PREVIEW_HEIGHT}.</li>
 * </ul>
 *
 * <p>
 * Drawn from {@code RenderWorldLastEvent} (like {@link RenderVoidcraftShip.BeamWorldLastRenderer}) — after
 * all opaque geometry (world, planets, hulls, EoH space shell) has rendered and written depth — so nothing
 * can overpaint the overlay; the overlay depth-tests against it (LEQUAL) but never writes depth, so it is
 * see-through and never occludes the world. Geometry is drawn in world coordinates under the
 * camera-interpolated translation (the GTWorkAreaRenderer pattern).
 */
@SideOnly(Side.CLIENT)
public class RenderVoidcraftAssembler {

    /** Preview hologram scale: 1/4 of the real build size. */
    private static final double PREVIEW_SCALE = 0.25;

    /** Preview spin period in ticks (one turn per 360 ticks — 5x slower than the in-flight ship's spin). */
    private static final double PREVIEW_SPIN_TICKS = 360.0;

    /** Whether the preview hologram is drawn at all. */
    public static final boolean PREVIEW_ENABLED = true;

    /** Blocks behind the machine's back face where the preview hologram floats. */
    public static final double PREVIEW_DISTANCE = 1.5D;

    /** Blocks above the machine's top where the preview hologram floats (horizontal-facing machines). */
    public static final double PREVIEW_HEIGHT = 1.5D;

    /** Re-scan the build volume (for the preview hologram) at most every N render frames. */
    private static final int PREVIEW_SCAN_INTERVAL_FRAMES = 10;

    /** Full period of the scanning-plane sweep in ticks (ends → middle → ends). */
    private static final double SCAN_PLANE_SWEEP_TICKS = 80.0;

    /** Overlay cyan (the fleet beams' cyan). */
    private static final float CYAN_R = 0.15F, CYAN_G = 0.75F, CYAN_B = 1.0F;

    /** Wireframe alpha at full pulse. */
    private static final float WIREFRAME_ALPHA = 0.55F;

    /** Scanning-plane alpha at full pulse. */
    private static final float PLANE_ALPHA = 0.28F;

    /** Preview hologram tint and opacity (the voidbase blueprint hologram's look). */
    private static final float HOLO_R = 0.4F, HOLO_G = 0.9F, HOLO_B = 1.0F;
    private static final float HOLO_ALPHA = 0.5F;

    private static long frame;

    /** Per-assembler preview state (render thread only). */
    private static final class PreviewState {

        /** Render frame the build volume was last re-scanned (0 = not scanned yet). */
        long lastScanFrame = 0;
        int lastBlueprintHash;
        int cells;
        ShipModel model;
    }

    private static final Map<MetaTileEntity, PreviewState> PREVIEW_STATES = new IdentityHashMap<>();

    public RenderVoidcraftAssembler() {}

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.theWorld;
        if (world == null) {
            return;
        }
        frame++;

        List<AssemblerVisuals.Snapshot> snapshots = AssemblerVisuals.current();
        if (snapshots.isEmpty()) {
            return;
        }

        // The camera position the world pass rendered from (interpolated) — under a single translation,
        // world coordinates then line up with the event's modelview.
        Entity camera = mc.renderViewEntity;
        if (camera == null) {
            return;
        }
        double camX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * event.partialTicks;
        double camY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * event.partialTicks;
        double camZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * event.partialTicks;

        GL11.glPushAttrib(
            GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT
                | GL11.GL_DEPTH_BUFFER_BIT
                | GL11.GL_CURRENT_BIT
                | GL11.GL_TEXTURE_BIT);
        GL11.glPushMatrix();
        try {
            GL11.glDisable(GL11.GL_LIGHTING);
            // The overlay draws are color-only — the block and lightmap textures must be off on BOTH units
            // (a single disable would only act on whichever unit the world pass left active, and the
            // tessellator's vertices carry no UVs — a live unit would sample the atlas at (0,0) and
            // multiply the overlay color dark).
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            // The game's default alpha test — nothing needs restoring afterwards.
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glDepthMask(false);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glTranslated(-camX, -camY, -camZ);

            for (AssemblerVisuals.Snapshot snap : snapshots) {
                if (snap.dimensionId != world.provider.dimensionId) {
                    continue; // machine in another dimension
                }
                renderAssembler(mc, world, snap, event.partialTicks);
            }
        } finally {
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    private static void renderAssembler(Minecraft mc, World world, AssemblerVisuals.Snapshot snap,
        double partialTicks) {
        ForgeDirection front = ForgeDirection.getOrientation(snap.facing);
        double[] axes = AssemblerVisuals.scanAxes(snap.facing);
        double bx = snap.x + 0.5, by = snap.y + 0.5, bz = snap.z + 0.5; // controller block center
        int half = snap.volumeX / 2;
        int depth = snap.volumeZ;
        double worldTime = world.getTotalWorldTime() + partialTicks;
        double pulse = 0.85 + 0.15 * Math.sin(worldTime / 2.5); // the fleet beams' pulse

        // The preview first: the wireframe and scan planes are UI overlays and must stay crisp on top of the
        // half-transparent hologram (viewed from behind the machine, the hologram would otherwise veil them).
        if (PREVIEW_ENABLED) {
            drawPreview(mc, world, snap, axes, worldTime, half, depth);
        }
        if (snap.scanning) {
            double phase = (worldTime / SCAN_PLANE_SWEEP_TICKS) % 1.0;
            double t = 0.5 - 0.5 * Math.cos(Math.PI * 2.0 * phase); // 0 → 1 → 0
            double[] d = AssemblerVisuals.planeDepths(t, depth);
            drawScanPlane(bx, by, bz, front, axes, half, d[0], (float) (PLANE_ALPHA * pulse));
            drawScanPlane(bx, by, bz, front, axes, half, d[1], (float) (PLANE_ALPHA * pulse));
        }
        if (snap.machineValid) {
            drawWireframe(bx, by, bz, front, axes, half, depth, (float) (WIREFRAME_ALPHA * pulse));
        }
    }

    /** The scan volume's 12 bounding edges as line segments. */
    private static void drawWireframe(double bx, double by, double bz, ForgeDirection front, double[] axes, int half,
        int depth, float alpha) {
        ensureTextureOff();
        double edge = half + 0.5;
        double near = 0.5, far = depth + 0.5;
        Tessellator tess = Tessellator.instance;
        GL11.glLineWidth(1.5F);
        tess.startDrawing(GL11.GL_LINES);
        tess.setColorRGBA_F(CYAN_R, CYAN_G, CYAN_B, alpha);
        // the four edges along the front (depth) axis
        for (int s = -1; s <= 1; s += 2) {
            for (int t = -1; t <= 1; t += 2) {
                corner(tess, bx, by, bz, front, axes, s * edge, t * edge, near);
                corner(tess, bx, by, bz, front, axes, s * edge, t * edge, far);
            }
        }
        // the four near-face edges and the four far-face edges
        for (double d : new double[] { near, far }) {
            for (int t = -1; t <= 1; t += 2) {
                corner(tess, bx, by, bz, front, axes, -edge, t * edge, d);
                corner(tess, bx, by, bz, front, axes, edge, t * edge, d);
            }
            for (int s = -1; s <= 1; s += 2) {
                corner(tess, bx, by, bz, front, axes, s * edge, -edge, d);
                corner(tess, bx, by, bz, front, axes, s * edge, edge, d);
            }
        }
        tess.draw();
    }

    /** One scan quad (double-sided) perpendicular to the front axis at the given depth. */
    private static void drawScanPlane(double bx, double by, double bz, ForgeDirection front, double[] axes, int half,
        double d, float alpha) {
        ensureTextureOff();
        double edge = half + 0.5;
        Tessellator tess = Tessellator.instance;
        tess.startDrawing(GL11.GL_QUADS);
        tess.setColorRGBA_F(CYAN_R, CYAN_G, CYAN_B, alpha);
        corner(tess, bx, by, bz, front, axes, -edge, -edge, d);
        corner(tess, bx, by, bz, front, axes, edge, -edge, d);
        corner(tess, bx, by, bz, front, axes, edge, edge, d);
        corner(tess, bx, by, bz, front, axes, -edge, edge, d);
        tess.draw();
    }

    /** One vertex at scan coordinate (i, j, depth) — the block-corner offset from the controller block. */
    private static void corner(Tessellator tess, double bx, double by, double bz, ForgeDirection front, double[] axes,
        double i, double j, double d) {
        tess.addVertex(
            bx + front.offsetX * d + axes[0] * i + axes[3] * j,
            by + front.offsetY * d + axes[1] * i + axes[4] * j,
            bz + front.offsetZ * d + axes[2] * i + axes[5] * j);
    }

    /**
     * Color-only Tessellator draws must not sample a texture: their vertices carry no UVs, so a live texture
     * unit would sample the atlas at (0,0) and multiply the draw's color by that texel.
     */
    private static void ensureTextureOff() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    /**
     * The rotating 1/4-scale cyan hologram of the current build, floating behind the machine. The build is
     * re-scanned (render thread) at a throttled rate; the model rebuilds only when the build's shape
     * actually changed.
     */
    private static void drawPreview(Minecraft mc, World world, AssemblerVisuals.Snapshot snap, double[] axes,
        double worldTime, int half, int depth) {
        PreviewState state = PREVIEW_STATES.get(snap.machine);
        if (state == null) {
            state = new PreviewState();
            PREVIEW_STATES.put(snap.machine, state);
        }

        // frame is monotonic within the session, so this difference stays small and never overflows.
        if (frame - state.lastScanFrame >= PREVIEW_SCAN_INTERVAL_FRAMES) {
            state.lastScanFrame = frame;
            VolumeScan scan = scanVolume(world, snap, axes, half, depth);
            int cells = 0;
            for (byte v : scan.grid) {
                if (v != 0) {
                    cells++;
                }
            }
            state.cells = cells;
            if (cells == 0) {
                state.model = null;
                state.lastBlueprintHash = 0;
                return;
            }
            VoidcraftBlueprint blueprint;
            try {
                blueprint = snap.base
                    ? VoidcraftBlueprint
                        .ofBase(snap.volumeX, snap.volumeY, snap.volumeZ, scan.grid, scan.facingGrid, scan.coverGrid)
                    : VoidcraftBlueprint
                        .of(snap.volumeX, snap.volumeY, snap.volumeZ, scan.grid, scan.facingGrid, scan.coverGrid);
            } catch (IllegalArgumentException e) {
                state.model = null;
                state.lastBlueprintHash = 0;
                return;
            }
            int hash = blueprint.hashCode();
            if (hash != state.lastBlueprintHash) {
                state.lastBlueprintHash = hash;
                state.model = VoidcraftShipModelCache.get(blueprint);
            }
        }
        if (state.model == null) {
            return;
        }

        ForgeDirection front = ForgeDirection.getOrientation(snap.facing);
        double[] off = AssemblerVisuals.previewOffset(snap.facing, snap.volumeY, PREVIEW_DISTANCE, PREVIEW_HEIGHT);
        double px = snap.x + 0.5 + off[0];
        double py = snap.y + 0.5 + off[1];
        double pz = snap.z + 0.5 + off[2];

        GL11.glPushMatrix();
        boolean fogOn = GL11.glIsEnabled(GL11.GL_FOG);
        boolean lightmapOn = false;
        boolean blockTexOn = false;
        try {
            GL11.glTranslated(px, py, pz);
            GL11.glRotatef((float) (worldTime * 360.0 / PREVIEW_SPIN_TICKS) % 360.0F, 0F, 1F, 0F);
            GL11.glScalef((float) PREVIEW_SCALE, (float) PREVIEW_SCALE, (float) PREVIEW_SCALE);
            // The blueprint cells span 0..n-1 on each axis — center the model on the preview position.
            GL11.glTranslatef(
                -(state.model.width - 1) / 2.0F,
                -(state.model.height - 1) / 2.0F,
                -(state.model.depth - 1) / 2.0F);

            // The hologram look (the voidbase blueprint hologram): single-texture unit 0 (the VAO has no
            // lightmap UVs, so the world pass's lightmap unit must be neutral for the duration, and the
            // block texture must be explicitly re-enabled here — the handler's outer state has texture
            // 2D off for the wireframe/planes), alpha-blended cyan tint, alpha test lowered so the 0.5-
            // alpha fragments survive, fog off so the world fog cannot tint the hologram, depth writes off
            // so it is see-through both ways.
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            blockTexOn = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            mc.getTextureManager()
                .bindTexture(TextureMap.locationBlocksTexture);
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            lightmapOn = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            if (fogOn) {
                GL11.glDisable(GL11.GL_FOG);
            }
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(HOLO_R, HOLO_G, HOLO_B, HOLO_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            state.model.vao.render();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        } finally {
            if (fogOn) {
                GL11.glEnable(GL11.GL_FOG);
            }
            GL11.glDisable(GL11.GL_LIGHTING);
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            if (lightmapOn) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            if (!blockTexOn) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            // The active unit is left on unit 0 — the world pass's ambient state.
            GL11.glPopMatrix();
        }
    }

    /**
     * Scan the assembler's build volume on the render thread: the component grid, the per-cell facing and the
     * Voidcraft covers mounted on the hull faces — the live build, captured exactly the way the assembler's
     * digitize scan captures it. Cells holding neither air nor a Voidcraft component (foreign blocks) count as
     * empty, so an invalid or half-built structure shows the parts placed so far.
     */
    private static VolumeScan scanVolume(World world, AssemblerVisuals.Snapshot snap, double[] axes, int half,
        int depth) {
        int w = snap.volumeX, h = snap.volumeY;
        VolumeScan scan = new VolumeScan();
        scan.grid = new byte[w * h * depth];
        scan.facingGrid = new byte[w * h * depth];
        scan.coverGrid = new byte[w * h * depth * 6];
        ForgeDirection front = ForgeDirection.getOrientation(snap.facing);
        int fx = front.offsetX, fy = front.offsetY, fz = front.offsetZ;
        int bx = snap.x, by = snap.y, bz = snap.z;
        for (int d = 1; d <= depth; d++) {
            for (int j = -half; j <= half; j++) {
                for (int i = -half; i <= half; i++) {
                    int x = bx + fx * d + (int) (axes[0] * i + axes[3] * j);
                    int y = by + fy * d + (int) (axes[1] * i + axes[4] * j);
                    int z = bz + fz * d + (int) (axes[2] * i + axes[5] * j);
                    if (y < 0 || y >= world.getHeight()) {
                        continue; // out of world counts as empty
                    }
                    if (world.getChunkFromChunkCoords(x >> 4, z >> 4) == null) {
                        continue; // chunk not loaded counts as empty
                    }
                    Block block = world.getBlock(x, y, z);
                    if (block == null || block == Blocks.air) {
                        continue;
                    }
                    IMetaTileEntity mte = GTUtility.getMetaTileEntity(world.getTileEntity(x, y, z));
                    MTEVoidcraftComponent hull = mte instanceof MTEVoidcraftComponent ? (MTEVoidcraftComponent) mte
                        : null;
                    VoidcraftComponent scanned = hull != null ? hull.getComponent()
                        : VoidcraftMultiblockRegistry.componentOf(mte);
                    if (scanned != null) {
                        int idx = AssemblerVisuals.gridIndex(i, j, d, half, w, h);
                        scan.grid[idx] = (byte) scanned.toGridValue();
                        scan.facingGrid[idx] = (byte) (mte.getBaseMetaTileEntity()
                            .getFrontFacing()
                            .ordinal() + 1);
                        if (hull != null && hull.getBaseMetaTileEntity() instanceof ICoverable) {
                            ICoverable coverable = (ICoverable) hull.getBaseMetaTileEntity();
                            for (int worldSide = 0; worldSide < 6; worldSide++) {
                                Cover cover = coverable.getCoverAtSide(ForgeDirection.getOrientation(worldSide));
                                if (cover instanceof CoverVoidcraftComponent) {
                                    CoverVoidcraftComponent vc = (CoverVoidcraftComponent) cover;
                                    if (vc.getComponent() != null) {
                                        scan.coverGrid[idx * 6
                                            + VoidcraftBlueprint.toGridSide(fx, fy, fz, worldSide)] = (byte) vc
                                                .getComponent()
                                                .toGridValue();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return scan;
    }

    /** The live-build scan result: the component grid, the per-cell facing, and the covers on the hull faces. */
    private static final class VolumeScan {

        byte[] grid;
        byte[] facingGrid;
        byte[] coverGrid;
    }
}
