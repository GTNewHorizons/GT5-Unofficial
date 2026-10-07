package gregtech.client;

import static gregtech.api.enums.Mods.GregTech;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Client;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * Heat and frost effects for fluid pipes holding very hot or cold fluids:
 * <ul>
 * <li>Glow: additive layers on and around glowing pipes.</li>
 * <li>Frost: a crust of ice crystals that spreads the colder the pipe gets.</li>
 * <li>Particles: heat haze rising off hot pipes, vapour sinking off cold ones.</li>
 * </ul>
 * Also fades each tracked pipe towards its synced level every tick.
 */
@SideOnly(Side.CLIENT)
public class PipeThermalEffectsRenderer {

    /**
     * Hot and cold pipes in {@link #trackedWorld}, by position. Resent chunks (e.g. on respawn) create new tile
     * entities without invalidating the old ones, so tracking tile entities would draw effects twice.
     */
    private static final Set<ChunkCoordinates> TRACKED_PIPES = new HashSet<>();
    private static @Nullable World trackedWorld;

    /** Pipes per Tessellator draw, so the buffer never has to grow. */
    private static final int PIPES_PER_BATCH = 16;

    // Glow
    /** Offset of each glow layer from the pipe surface: the surface itself, then the halo. */
    private static final float[] GLOW_LAYER_OFFSETS = { 0.004F, 0.035F, 0.07F, 0.11F, 0.16F };
    /** Brightness of each glow layer at full glow. */
    private static final float[] GLOW_LAYER_INTENSITIES = { 0.65F, 0.22F, 0.13F, 0.07F, 0.035F };
    /** Brightness at the faintest glow, as a fraction of full. */
    private static final float GLOW_MIN_STRENGTH = 0.3F;
    /** Pulse strength as a fraction of brightness. The pulse travels through world space, so runs of pipe ripple. */
    private static final float GLOW_WAVE = 0.18F;

    // Frost
    /** Tileable ice crystals, 32 texels to a block. Lower alpha shows only on colder pipes. */
    private static final ResourceLocation FROST_TEXTURE = new ResourceLocation(
        GregTech.resourceDomain,
        "textures/effects/pipe_frost.png");
    /** Frost piled up along an edge, tiling along it and {@link #FROST_EDGE_WIDTH} across. */
    private static final ResourceLocation FROST_EDGE_TEXTURE = new ResourceLocation(
        GregTech.resourceDomain,
        "textures/effects/pipe_frost_edge.png");
    private static final double FROST_EDGE_WIDTH = 0.25D;
    private static final double FROST_OFFSET = 0.003D;
    /** Alpha cut-off per frost stage, lightest first, so the same crystals spread as it gets colder. */
    private static final float[] FROST_STAGE_CUTOFFS = { 0.72F, 0.5F, 0.32F, 0.12F };
    /** Steps per frost stage, so frost creeps on and off as it fades. */
    private static final int FROST_STEPS_PER_STAGE = 4;
    /** Vanilla face shading by {@link ForgeDirection} ordinal, so frost is shaded like the pipe under it. */
    private static final float[] FACE_SHADE = { 0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F };

    /** Chance per tick of a vapour wisp, by frost stage. */
    private static final float[] MIST_CHANCES = { 0.04F, 0.08F, 0.13F, 0.2F };

    // Heat haze
    /** Chance per tick of a heat haze wisp, from the lowest heat to the highest. */
    private static final float HAZE_CHANCE_MIN = 0.06F, HAZE_CHANCE_MAX = 0.25F;
    /** Only pipes this close to the viewer give off particles, further out they're too small to see. */
    private static final double PARTICLE_DISTANCE_SQ = 32.0D * 32.0D;

    private final Random random = new Random();
    private final List<BaseMetaPipeEntity> hotPipes = new ArrayList<>();
    @SuppressWarnings("unchecked")
    private final List<BaseMetaPipeEntity>[] frostPipes = new List[MTEFluidPipe.FROST_LEVELS * FROST_STEPS_PER_STAGE];
    /** {@link #getJoinedSides} for each pipe in {@link #hotPipes} and {@link #frostPipes}, worked out once a frame. */
    private final IntArrayList hotJoined = new IntArrayList();
    private final IntArrayList[] frostJoined = new IntArrayList[frostPipes.length];

    public PipeThermalEffectsRenderer() {
        for (int i = 0; i < frostPipes.length; i++) {
            frostPipes[i] = new ArrayList<>();
            frostJoined[i] = new IntArrayList();
        }
    }

    public static void setTracked(IGregTechTileEntity pipe, boolean tracked) {
        final World world = pipe.getWorld();
        if (world != trackedWorld) {
            TRACKED_PIPES.clear();
            trackedWorld = world;
        }
        final ChunkCoordinates position = new ChunkCoordinates(pipe.getXCoord(), pipe.getYCoord(), pipe.getZCoord());
        if (tracked) TRACKED_PIPES.add(position);
        else TRACKED_PIPES.remove(position);
    }

    /** @return the pipe at this position if it still has effects, otherwise null */
    private static @Nullable BaseMetaPipeEntity getLivePipe(ChunkCoordinates position, World world) {
        if (world != trackedWorld) return null;
        final TileEntity tile = world.getTileEntity(position.posX, position.posY, position.posZ);
        if (!(tile instanceof BaseMetaPipeEntity pipeTile) || pipeTile.isInvalid()) return null;
        if (!(pipeTile.getMetaTileEntity() instanceof MTEFluidPipe pipe) || !pipe.hasThermalEffect()) return null;
        return pipeTile;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (TRACKED_PIPES.isEmpty() || !Client.render.renderFluidPipeThermalEffects) return;
        final Minecraft mc = Minecraft.getMinecraft();
        final World world = mc.theWorld;
        final Entity camera = mc.renderViewEntity;
        if (world == null || camera == null) return;

        final double cameraX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * event.partialTicks;
        final double cameraY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * event.partialTicks;
        final double cameraZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * event.partialTicks;

        hotPipes.clear();
        hotJoined.clear();
        for (int i = 0; i < frostPipes.length; i++) {
            frostPipes[i].clear();
            frostJoined[i].clear();
        }
        boolean anyFrost = false;
        final Iterator<ChunkCoordinates> iterator = TRACKED_PIPES.iterator();
        while (iterator.hasNext()) {
            final BaseMetaPipeEntity pipeTile = getLivePipe(iterator.next(), world);
            if (pipeTile == null) {
                iterator.remove();
                continue;
            }
            final MTEFluidPipe pipe = (MTEFluidPipe) pipeTile.getMetaTileEntity();

            final float level = pipe.getShownThermal();
            if (level > 0) {
                // Hot pipes that don't glow only get heat haze
                if (MTEFluidPipe.getGlowFraction(level) > 0) {
                    hotPipes.add(pipeTile);
                    hotJoined.add(getJoinedSides(pipeTile, pipe));
                }
            } else if (level < 0) {
                final int step = (int) Math.ceil(MTEFluidPipe.getFrostAmount(level) * FROST_STEPS_PER_STAGE);
                final int index = GTUtility.clamp(step, 1, frostPipes.length) - 1;
                frostPipes[index].add(pipeTile);
                frostJoined[index].add(getJoinedSides(pipeTile, pipe));
                anyFrost = true;
            }
        }
        final boolean drawGlow = !hotPipes.isEmpty() && GTMod.proxy.mRenderGlowTextures;
        if (!drawGlow && !anyFrost) return;

        GL11.glPushAttrib(
            GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT
                | GL11.GL_DEPTH_BUFFER_BIT
                | GL11.GL_CURRENT_BIT
                | GL11.GL_POLYGON_BIT);

        // Prevent float rounding issues far from origin.
        final Tessellator tessellator = Tessellator.instance;
        tessellator.setTranslation(-cameraX, -cameraY, -cameraZ);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(-1.0F, -1.0F);

        if (anyFrost) renderFrost(mc, world, event.partialTicks);
        if (drawGlow) renderGlow(mc, event.partialTicks, world.getTotalWorldTime() + event.partialTicks);

        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Glow

    private void renderGlow(Minecraft mc, float partialTicks, float time) {
        // Glow ignores world light entirely
        mc.entityRenderer.disableLightmap(partialTicks);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        for (int i = 0; i < hotPipes.size(); i++) {
            addGlow(tessellator, hotPipes.get(i), hotJoined.getInt(i), time);
            if ((i + 1) % PIPES_PER_BATCH == 0) {
                tessellator.draw();
                tessellator.startDrawingQuads();
            }
        }
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private static void addGlow(Tessellator tessellator, BaseMetaPipeEntity tile, int joined, float time) {
        final MTEFluidPipe pipe = (MTEFluidPipe) tile.getMetaTileEntity();
        final float level = pipe.getShownThermal();
        final float[] color = MTEFluidPipe.getHeatGlowColor(level);
        final float red = color[0] / 255.0F, green = color[1] / 255.0F, blue = color[2] / 255.0F;
        final float glow = MTEFluidPipe.getGlowFraction(level);
        final float phase = (tile.xCoord + tile.yCoord + tile.zCoord) * 0.9F;
        final float strength = (GLOW_MIN_STRENGTH + (1.0F - GLOW_MIN_STRENGTH) * glow)
            * (1.0F - GLOW_WAVE + GLOW_WAVE * MathHelper.sin(time * 0.15F - phase));

        for (int layer = 0; layer < GLOW_LAYER_OFFSETS.length; layer++) {
            tessellator.setColorRGBA_F(red, green, blue, GLOW_LAYER_INTENSITIES[layer] * strength);
            forEachBox(
                tile,
                pipe,
                joined,
                GLOW_LAYER_OFFSETS[layer],
                (x0, y0, z0, x1, y1, z1, faces) -> addPlainBox(tessellator, x0, y0, z0, x1, y1, z1, faces));
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Frost

    private void renderFrost(Minecraft mc, World world, float partialTicks) {
        mc.entityRenderer.enableLightmap(partialTicks);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        final Tessellator tessellator = Tessellator.instance;

        // Crust of crystals over every face, then frost piled up along the edges on top
        for (int pass = 0; pass < 2; pass++) {
            final boolean edges = pass == 1;
            mc.getTextureManager()
                .bindTexture(edges ? FROST_EDGE_TEXTURE : FROST_TEXTURE);
            for (int stage = 0; stage < frostPipes.length; stage++) {
                final List<BaseMetaPipeEntity> pipes = frostPipes[stage];
                final IntArrayList joined = frostJoined[stage];
                if (pipes.isEmpty()) continue;
                GL11.glAlphaFunc(
                    GL11.GL_GREATER,
                    getStageValue(FROST_STAGE_CUTOFFS, 1.0F, (stage + 1) / (float) FROST_STEPS_PER_STAGE));
                tessellator.startDrawingQuads();
                for (int i = 0; i < pipes.size(); i++) {
                    addFrost(tessellator, world, pipes.get(i), joined.getInt(i), edges);
                    if ((i + 1) % PIPES_PER_BATCH == 0) {
                        tessellator.draw();
                        tessellator.startDrawingQuads();
                    }
                }
                tessellator.draw();
            }
        }
        mc.entityRenderer.disableLightmap(partialTicks);
    }

    private static void addFrost(Tessellator tessellator, World world, BaseMetaPipeEntity tile, int joined,
        boolean edges) {
        final MTEFluidPipe pipe = (MTEFluidPipe) tile.getMetaTileEntity();
        tessellator.setBrightness(world.getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord, tile.zCoord, 0));
        forEachBox(tile, pipe, joined, FROST_OFFSET, (x0, y0, z0, x1, y1, z1, faces) -> {
            final double[] min = { x0, y0, z0 }, max = { x1, y1, z1 };
            for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
                if ((faces & face.flag) == 0) continue;
                final float shade = FACE_SHADE[face.ordinal()];
                tessellator.setColorRGBA_F(shade, shade, shade, 1.0F);
                if (!edges) {
                    addFrostFace(tessellator, min, max, face);
                    continue;
                }
                // Frost builds up where this face meets another outer face of the pipe
                for (ForgeDirection edge : ForgeDirection.VALID_DIRECTIONS) {
                    if (edge == face || edge == face.getOpposite() || (faces & edge.flag) == 0) continue;
                    addFrostEdge(tessellator, min, max, face, edge);
                }
            }
        });
    }

    /**
     * @param stages a value for each frost stage, lightest first
     * @param none   the value with no frost at all
     * @param amount how heavy the frost is, from 0 to {@link MTEFluidPipe#FROST_LEVELS}
     * @return the value for that much frost, blending between stages
     */
    private static float getStageValue(float[] stages, float none, float amount) {
        if (amount <= 1.0F) return none + (stages[0] - none) * Math.max(0F, amount);
        final int stage = Math.min((int) amount, stages.length - 1);
        final float along = Math.min(1.0F, amount - stage);
        return stages[stage - 1] + (stages[stage] - stages[stage - 1]) * along;
    }

    private static int axis(ForgeDirection side) {
        return side.offsetX != 0 ? 0 : side.offsetY != 0 ? 1 : 2;
    }

    private static boolean positive(ForgeDirection side) {
        return side.offsetX + side.offsetY + side.offsetZ > 0;
    }

    /** The in-plane axis used for the texture's u, for a face on the given axis: x, or z on x-facing faces. */
    private static int uAxis(int faceAxis) {
        return faceAxis == 0 ? 2 : 0;
    }

    /**
     * One face with the crystal texture mapped in world space, so it tiles seamlessly from pipe to pipe. The texture
     * repeats every block, so whole blocks are taken off the UVs to keep them small and precise.
     */
    private static void addFrostFace(Tessellator t, double[] min, double[] max, ForgeDirection face) {
        final int fa = axis(face);
        final int ua = uAxis(fa);
        final int va = 3 - fa - ua;
        final double plane = positive(face) ? max[fa] : min[fa];
        final double uBase = Math.floor(min[ua]), vBase = Math.floor(min[va]);
        final double[][] corners = { { min[ua], min[va] }, { max[ua], min[va] }, { max[ua], max[va] },
            { min[ua], max[va] } };
        final double[] p = new double[3];
        for (double[] c : corners) {
            p[fa] = plane;
            p[ua] = c[0];
            p[va] = c[1];
            t.addVertexWithUV(p[0], p[1], p[2], c[0] - uBase, c[1] - vBase);
        }
    }

    /** A strip of piled-up frost on {@code face}, running along its edge with {@code edge}. */
    private static void addFrostEdge(Tessellator t, double[] min, double[] max, ForgeDirection face,
        ForgeDirection edge) {
        final int fa = axis(face);
        final int ea = axis(edge);
        final int la = 3 - fa - ea;
        final double plane = positive(face) ? max[fa] : min[fa];
        final double width = Math.min(FROST_EDGE_WIDTH, (max[ea] - min[ea]) / 2.0D);
        final double outer = positive(edge) ? max[ea] : min[ea];
        final double inner = positive(edge) ? outer - width : outer + width;
        final double innerV = width / FROST_EDGE_WIDTH;
        final double[] p = new double[3];
        // u runs along the edge in world space (less whole blocks, as for faces); v runs from the edge (0) inwards
        final double uBase = Math.floor(min[la]);
        p[fa] = plane;
        p[la] = min[la];
        p[ea] = outer;
        t.addVertexWithUV(p[0], p[1], p[2], min[la] - uBase, 0.0D);
        p[la] = max[la];
        t.addVertexWithUV(p[0], p[1], p[2], max[la] - uBase, 0.0D);
        p[ea] = inner;
        t.addVertexWithUV(p[0], p[1], p[2], max[la] - uBase, innerV);
        p[la] = min[la];
        t.addVertexWithUV(p[0], p[1], p[2], min[la] - uBase, innerV);
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Fading and particles

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || TRACKED_PIPES.isEmpty()) return;
        final Minecraft mc = Minecraft.getMinecraft();
        final World world = mc.theWorld;
        if (world == null || mc.isGamePaused()) return;

        final Entity viewer = mc.renderViewEntity;
        final boolean particles = Client.render.renderFluidPipeThermalEffects && viewer != null
            && mc.gameSettings.particleSetting < 2;
        final float settingFactor = mc.gameSettings.particleSetting == 1 ? 0.4F : 1.0F;

        final Iterator<ChunkCoordinates> iterator = TRACKED_PIPES.iterator();
        while (iterator.hasNext()) {
            final BaseMetaPipeEntity pipeTile = getLivePipe(iterator.next(), world);
            if (pipeTile == null) {
                iterator.remove();
                continue;
            }
            final MTEFluidPipe pipe = (MTEFluidPipe) pipeTile.getMetaTileEntity();
            if (pipe == null) return;

            // Fade even with effects off, since block light follows it
            pipe.advanceThermalFade(1);
            if (!particles) continue;

            final double dx = pipeTile.xCoord + 0.5D - viewer.posX;
            final double dy = pipeTile.yCoord + 0.5D - viewer.posY;
            final double dz = pipeTile.zCoord + 0.5D - viewer.posZ;
            if (dx * dx + dy * dy + dz * dz > PARTICLE_DISTANCE_SQ) continue;

            final float level = pipe.getShownThermal();
            if (level > 0) spawnHeatParticles(mc, world, pipeTile, pipe, settingFactor);
            else if (level < 0) spawnColdParticles(mc, world, pipeTile, pipe, settingFactor);
        }
    }

    /** Cold vapour sinking off a frosted pipe. */
    private void spawnColdParticles(Minecraft mc, World world, BaseMetaPipeEntity tile, MTEFluidPipe pipe,
        float settingFactor) {
        final float frost = MTEFluidPipe.getFrostAmount(pipe.getShownThermal());
        if (frost <= 0 || random.nextFloat() >= getStageValue(MIST_CHANCES, 0F, frost) * settingFactor) return;

        // From the lower half of the pipe, drifting down and out
        final double thickness = Math.min(1.0D, pipe.getThickness());
        final double min = (1.0D - thickness) / 2.0D;
        mc.effectRenderer.addEffect(
            new PipeColdMistFX(
                world,
                tile.xCoord + min + random.nextDouble() * thickness,
                tile.yCoord + min + random.nextDouble() * thickness * 0.4D,
                tile.zCoord + min + random.nextDouble() * thickness,
                (random.nextDouble() - 0.5D) * 0.012D,
                -0.004D - random.nextDouble() * 0.01D,
                (random.nextDouble() - 0.5D) * 0.012D,
                frost / MTEFluidPipe.FROST_LEVELS));
    }

    /** Heat haze off the top of any hot pipe. */
    private void spawnHeatParticles(Minecraft mc, World world, BaseMetaPipeEntity tile, MTEFluidPipe pipe,
        float settingFactor) {
        final float level = pipe.getShownThermal();
        final float heat = MTEFluidPipe.getHeatFraction(level);
        // Fewer wisps while fading in or out
        final float warmth = Math.min(1.0F, level);
        final double thickness = Math.min(1.0D, pipe.getThickness());
        final double min = (1.0D - thickness) / 2.0D;
        final double top = tile.yCoord + 1.0D - min;

        if (random.nextFloat()
            < (HAZE_CHANCE_MIN + (HAZE_CHANCE_MAX - HAZE_CHANCE_MIN) * heat) * warmth * settingFactor) {
            mc.effectRenderer.addEffect(
                new PipeHeatHazeFX(
                    world,
                    tile.xCoord + min + random.nextDouble() * thickness,
                    top + 0.05D,
                    tile.zCoord + min + random.nextDouble() * thickness,
                    heat));
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Pipe shape

    @FunctionalInterface
    private interface BoxVisitor {

        /** A box in world coordinates, and which of its faces (one bit per {@link ForgeDirection}) are outer faces. */
        void visit(double x0, double y0, double z0, double x1, double y1, double z1, int faces);
    }

    /**
     * Splits the pipe's visible shape, grown by {@code d}, into boxes: the whole block for full-size pipes, otherwise a
     * centre plus an arm per connection. Sides in {@code joined} ({@link #getJoinedSides}) are left open so runs are
     * seamless; other ends are capped.
     */
    private static void forEachBox(BaseMetaPipeEntity tile, MTEFluidPipe pipe, int joined, double d,
        BoxVisitor visitor) {
        final double x = tile.xCoord, y = tile.yCoord, z = tile.zCoord;
        final int connections = tile.getConnections() & 63;
        final double thickness = Math.min(1.0D, pipe.getThickness());

        if (thickness >= 1.0D) {
            visitor.visit(
                x + (isConnected(joined, ForgeDirection.WEST) ? 0 : -d),
                y + (isConnected(joined, ForgeDirection.DOWN) ? 0 : -d),
                z + (isConnected(joined, ForgeDirection.NORTH) ? 0 : -d),
                x + 1 + (isConnected(joined, ForgeDirection.EAST) ? 0 : d),
                y + 1 + (isConnected(joined, ForgeDirection.UP) ? 0 : d),
                z + 1 + (isConnected(joined, ForgeDirection.SOUTH) ? 0 : d),
                ~joined & 63);
            return;
        }

        final double lo = (1.0D - thickness) / 2.0D - d, hi = 1.0D - (1.0D - thickness) / 2.0D + d;
        visitor.visit(x + lo, y + lo, z + lo, x + hi, y + hi, z + hi, ~connections & 63);
        if (lo <= 0.0D) return;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (!isConnected(connections, side)) continue;
            // Open into another fluid pipe, capped otherwise
            final boolean capped = !isConnected(joined, side);
            final double end = capped ? d : 0;
            final int faces = (63 & ~(side.flag | side.getOpposite().flag)) | (capped ? side.flag : 0);
            visitor.visit(
                x + (side.offsetX > 0 ? hi : side.offsetX < 0 ? -end : lo),
                y + (side.offsetY > 0 ? hi : side.offsetY < 0 ? -end : lo),
                z + (side.offsetZ > 0 ? hi : side.offsetZ < 0 ? -end : lo),
                x + (side.offsetX > 0 ? 1 + end : side.offsetX < 0 ? lo : hi),
                y + (side.offsetY > 0 ? 1 + end : side.offsetY < 0 ? lo : hi),
                z + (side.offsetZ > 0 ? 1 + end : side.offsetZ < 0 ? lo : hi),
                faces);
        }
    }

    /** @return the connected sides that carry on into another fluid pipe, one bit per {@link ForgeDirection} */
    private static int getJoinedSides(BaseMetaPipeEntity tile, MTEFluidPipe pipe) {
        final int connections = tile.getConnections() & 63;
        int joined = 0;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (isConnected(connections, side) && isJoinedToPipeAt(tile, pipe, side)) joined |= side.flag;
        }
        return joined;
    }

    private static boolean isConnected(int connections, ForgeDirection side) {
        return (connections & side.flag) != 0;
    }

    /** @return whether the block on that side is a fluid pipe at least as thick, so the effect carries on into it */
    private static boolean isJoinedToPipeAt(BaseMetaPipeEntity tile, MTEFluidPipe pipe, ForgeDirection side) {
        final TileEntity neighbour = tile.getWorldObj()
            .getTileEntity(tile.xCoord + side.offsetX, tile.yCoord + side.offsetY, tile.zCoord + side.offsetZ);
        return neighbour instanceof BaseMetaPipeEntity pipeTile
            && pipeTile.getMetaTileEntity() instanceof MTEFluidPipe other
            && other.getCollisionThickness() >= pipe.getCollisionThickness() - 0.01F;
    }

    /** Adds the faces of a box picked by {@code faces}, untextured. */
    private static void addPlainBox(Tessellator t, double x0, double y0, double z0, double x1, double y1, double z1,
        int faces) {
        if ((faces & ForgeDirection.DOWN.flag) != 0) {
            t.addVertex(x0, y0, z0);
            t.addVertex(x1, y0, z0);
            t.addVertex(x1, y0, z1);
            t.addVertex(x0, y0, z1);
        }
        if ((faces & ForgeDirection.UP.flag) != 0) {
            t.addVertex(x0, y1, z0);
            t.addVertex(x0, y1, z1);
            t.addVertex(x1, y1, z1);
            t.addVertex(x1, y1, z0);
        }
        if ((faces & ForgeDirection.NORTH.flag) != 0) {
            t.addVertex(x0, y0, z0);
            t.addVertex(x0, y1, z0);
            t.addVertex(x1, y1, z0);
            t.addVertex(x1, y0, z0);
        }
        if ((faces & ForgeDirection.SOUTH.flag) != 0) {
            t.addVertex(x0, y0, z1);
            t.addVertex(x1, y0, z1);
            t.addVertex(x1, y1, z1);
            t.addVertex(x0, y1, z1);
        }
        if ((faces & ForgeDirection.WEST.flag) != 0) {
            t.addVertex(x0, y0, z0);
            t.addVertex(x0, y0, z1);
            t.addVertex(x0, y1, z1);
            t.addVertex(x0, y1, z0);
        }
        if ((faces & ForgeDirection.EAST.flag) != 0) {
            t.addVertex(x1, y0, z0);
            t.addVertex(x1, y1, z0);
            t.addVertex(x1, y1, z1);
            t.addVertex(x1, y0, z1);
        }
    }
}
