package gregtech.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.WorldEvent;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.FluidPipeThermalState;
import gregtech.api.metatileentity.implementations.FluidPipeThermals;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.common.config.Client;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

/**
 * Renders glow and particles for hot and cold fluid pipes. Also fades each tracked pipe towards its synced level
 * every tick, and batches the chunk rebuilds those fades need. Frost is drawn with the pipe's block model, by
 * {@link PipeFrostTexture}.
 */
@SideOnly(Side.CLIENT)
public class PipeThermalEffectsRenderer {

    /**
     * Hot and cold pipes in {@link #trackedWorld}, by position. Tracked by position rather than tile entity because
     * resent chunks (e.g. on respawn) create new tile entities without invalidating the old ones.
     */
    private static final Object2ObjectOpenHashMap<ChunkCoordinates, TrackedPipe> TRACKED_PIPES = new Object2ObjectOpenHashMap<>();
    private static final int JOINED_UNKNOWN = -1;
    private static @Nullable World trackedWorld;
    /**
     * Ticks between looking each tracked pipe up in the world again, to catch it being replaced or unloaded. Pipes are
     * spread over these ticks so only a share of them are looked up at once.
     */
    private static final int RECHECK_TICKS = 20;

    /** Chunk sections waiting to be rebuilt for a fade, from {@link #packSection}. */
    private static final LongOpenHashSet PENDING_REBUILDS = new LongOpenHashSet();
    /** Baked glow of the tracked pipes glowing steadily. */
    private static final PipeGlowCache GLOW_CACHE = new PipeGlowCache();
    /**
     * Ticks between rebuilding the sections fades have changed, so pipes fading together in a section rebuild it once.
     * No longer than the quickest fade takes per step, so no step is skipped.
     */
    private static final int REBUILD_TICKS = 5;

    /** How far past the pipe's surface the outermost glow layer reaches, so culling doesn't clip it. */
    static final double CULL_MARGIN = 0.2D;
    /** How far past render distance glow candidates are kept, as the camera moves between ticks. */
    private static final double CANDIDATE_MARGIN = 16.0D;

    /** Pipes per Tessellator draw, so the buffer never has to grow. */
    private static final int PIPES_PER_BATCH = 16;

    /** Offset of each glow layer from the pipe surface. The first is on the surface, the rest form the halo. */
    private static final float[] GLOW_LAYER_OFFSETS = { 0.004F, 0.035F, 0.07F, 0.11F, 0.16F };
    /** Brightness of each glow layer at full glow. */
    private static final float[] GLOW_LAYER_INTENSITIES = { 0.65F, 0.22F, 0.13F, 0.07F, 0.035F };
    /** Brightness at the faintest glow, as a fraction of full. */
    private static final float GLOW_MIN_STRENGTH = 0.3F;
    /** Pulse strength as a fraction of brightness. The pulse is offset by position so runs of pipe ripple. */
    private static final float GLOW_WAVE = 0.18F;
    /** Pulse speed in radians per tick. */
    static final double GLOW_WAVE_SPEED = 0.15D;

    /** Chance per tick of a vapour wisp, by frost stage. */
    private static final float[] MIST_CHANCES = { 0.04F, 0.08F, 0.13F, 0.2F };

    /** Chance per tick of a heat haze wisp, from the lowest heat to the highest. */
    private static final float HAZE_CHANCE_MIN = 0.12F, HAZE_CHANCE_MAX = 0.25F;
    /** Pipes further than this from the viewer don't spawn particles. */
    private static final double PARTICLE_DISTANCE_SQ = 24.0D * 24.0D;
    /**
     * Most particles spawned per tick across all pipes. Past this, every pipe's chance is scaled down evenly. About
     * half the vanilla particle limit stays alive at this rate, so large builds thin out rather than hitting the limit.
     */
    private static final float PARTICLE_BUDGET = 40.0F;
    /** Most pipe particles alive at once. Well above what {@link #PARTICLE_BUDGET} keeps alive, as a backstop. */
    private static final int MAX_PARTICLES = 4000;
    private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");

    private final Random random = new Random();
    private int tick;
    /** Glowing pipes within render distance, found each tick. Each frame only these are checked against the view. */
    private final List<TrackedPipe> glowCandidates = new ArrayList<>();
    /** Glowing pipes in view this frame. */
    private final List<TrackedPipe> hotPipes = new ArrayList<>();
    /** Pipes near the viewer that may give off particles this tick, and their chance of one. */
    private final List<TrackedPipe> particlePipes = new ArrayList<>();
    private final FloatArrayList particleChances = new FloatArrayList();
    /** Heat haze and vapour, kept out of the vanilla particle renderer so they can be drawn after the glow. */
    private final List<EntityFX> particles = new ArrayList<>();

    /** A hot or cold pipe, with what's cached about it between ticks. */
    static final class TrackedPipe {

        final int x, y, z;
        /** Tick, out of {@link #RECHECK_TICKS}, this pipe is looked up again on. */
        final int recheckTick;
        @Nullable
        BaseMetaPipeEntity tile;
        /** {@link #getJoinedSides}, or {@link #JOINED_UNKNOWN} until next drawn. */
        int joined = JOINED_UNKNOWN;
        /** The connections {@link #joined} was found with. */
        int connections = -1;
        /** What {@link PipeGlowCache} should bake for this pipe, or {@link PipeGlowCache#NOT_CACHED}. */
        int cacheKey = PipeGlowCache.NOT_CACHED;
        /** Whether this pipe's glow is in its section's baked buffer, so isn't drawn each frame. */
        boolean inCache;

        TrackedPipe(ChunkCoordinates position) {
            x = position.posX;
            y = position.posY;
            z = position.posZ;
            recheckTick = Math.floorMod(position.hashCode(), RECHECK_TICKS);
        }

        MTEFluidPipe pipe() {
            return (MTEFluidPipe) tile.getMetaTileEntity();
        }
    }

    /** Starts drawing effects on this pipe until it's back to normal. Its tile entity replaces any already tracked. */
    public static void track(IGregTechTileEntity pipe) {
        final World world = pipe.getWorld();
        if (world != trackedWorld) {
            TRACKED_PIPES.clear();
            PENDING_REBUILDS.clear();
            GLOW_CACHE.clear();
            trackedWorld = world;
        }
        final ChunkCoordinates position = new ChunkCoordinates(pipe.getXCoord(), pipe.getYCoord(), pipe.getZCoord());
        final TrackedPipe tracked = TRACKED_PIPES.computeIfAbsent(position, TrackedPipe::new);
        tracked.tile = pipe instanceof BaseMetaPipeEntity pipeTile ? pipeTile : null;
        tracked.joined = JOINED_UNKNOWN;
    }

    /**
     * Rebuilds the chunk section holding this pipe within the next {@link #REBUILD_TICKS} ticks, along with any other
     * pipe in that section that changes in the meantime.
     */
    public static void queueRebuild(IGregTechTileEntity pipe) {
        if (pipe.getWorld() != trackedWorld) return;
        PENDING_REBUILDS.add(packSection(pipe.getXCoord() >> 4, pipe.getYCoord() >> 4, pipe.getZCoord() >> 4));
    }

    static long packSection(int x, int y, int z) {
        return ((long) x << 40) | ((z & 0xFFFFFFL) << 16) | (y & 0xFFFFL);
    }

    /** @return the pipe at this position if it still has effects, otherwise null */
    private static @Nullable BaseMetaPipeEntity getLivePipe(ChunkCoordinates position, World world) {
        if (world != trackedWorld) return null;
        final TileEntity tile = world.getTileEntity(position.posX, position.posY, position.posZ);
        return tile instanceof BaseMetaPipeEntity pipeTile && hasEffects(pipeTile) ? pipeTile : null;
    }

    private static boolean hasEffects(BaseMetaPipeEntity tile) {
        return !tile.isInvalid() && tile.getMetaTileEntity() instanceof MTEFluidPipe pipe
            && pipe.getThermals()
                .isActive();
    }

    /** Drops the tracked pipes with their world, so it isn't kept around after leaving or changing dimension. */
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world != trackedWorld) return;
        TRACKED_PIPES.clear();
        PENDING_REBUILDS.clear();
        GLOW_CACHE.clear();
        glowCandidates.clear();
        hotPipes.clear();
        particles.clear();
        trackedWorld = null;
    }

    /** Remakes the frost sprites each time the block atlas is stitched. */
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 0) PipeFrostTexture.registerSprites(event.map);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!Client.render.renderFluidPipeThermalEffects) return;
        final Minecraft mc = Minecraft.getMinecraft();
        final World world = mc.theWorld;
        final Entity camera = mc.renderViewEntity;
        if (world == null || camera == null) return;

        if (world == trackedWorld && (!glowCandidates.isEmpty() || !GLOW_CACHE.isEmpty())) {
            renderGlowingPipes(mc, world, camera, event.partialTicks);
        }
        // After the glow, as particles must not hide it
        if (!particles.isEmpty()) renderParticles(mc, camera, event.partialTicks);
    }

    private void renderGlowingPipes(Minecraft mc, World world, Entity camera, float partialTicks) {
        final double cameraX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * partialTicks;
        final double cameraY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * partialTicks;
        final double cameraZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * partialTicks;
        final Frustrum frustum = new Frustrum();
        frustum.setPosition(cameraX, cameraY, cameraZ);
        final double maxDistance = mc.gameSettings.renderDistanceChunks * 16.0D;

        collectGlowingPipes(frustum, cameraX, cameraY, cameraZ, maxDistance * maxDistance);
        if (!GTMod.proxy.mRenderGlowTextures) return;
        if (hotPipes.isEmpty() && GLOW_CACHE.isEmpty()) return;

        GL11.glPushAttrib(
            GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT
                | GL11.GL_DEPTH_BUFFER_BIT
                | GL11.GL_CURRENT_BIT
                | GL11.GL_POLYGON_BIT);
        mc.entityRenderer.disableLightmap(partialTicks);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(-1.0F, -1.0F);

        final double time = world.getTotalWorldTime() + (double) partialTicks;
        if (!hotPipes.isEmpty()) drawGlow(cameraX, cameraY, cameraZ, time);
        GLOW_CACHE.render(cameraX, cameraY, cameraZ, frustum, maxDistance, time);

        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }

    /**
     * Draws {@link #particles} the way vanilla draws its main particle layer, but without writing depth. Angelica has
     * vanilla particles write depth, which would hide the glow and frost behind every wisp.
     */
    private void renderParticles(Minecraft mc, Entity camera, float partialTicks) {
        // Particles draw relative to these. Vanilla sets them as it draws its own, which may be skipped
        EntityFX.interpPosX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * partialTicks;
        EntityFX.interpPosY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * partialTicks;
        EntityFX.interpPosZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * partialTicks;
        GL11.glPushAttrib(
            GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_CURRENT_BIT);
        mc.entityRenderer.enableLightmap(partialTicks);
        mc.getTextureManager()
            .bindTexture(PARTICLE_TEXTURES);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glAlphaFunc(GL11.GL_GREATER, 1.0F / 255.0F);

        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        for (EntityFX particle : particles) {
            tessellator.setBrightness(particle.getBrightnessForRender(partialTicks));
            particle.renderParticle(
                tessellator,
                partialTicks,
                ActiveRenderInfo.rotationX,
                ActiveRenderInfo.rotationXZ,
                ActiveRenderInfo.rotationZ,
                ActiveRenderInfo.rotationYZ,
                ActiveRenderInfo.rotationXY);
        }
        tessellator.draw();

        mc.entityRenderer.disableLightmap(partialTicks);
        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }

    /** Fills {@link #hotPipes} with the {@link #glowCandidates} in view. */
    private void collectGlowingPipes(Frustrum frustum, double cameraX, double cameraY, double cameraZ,
        double maxDistanceSq) {
        hotPipes.clear();
        for (TrackedPipe tracked : glowCandidates) {
            final BaseMetaPipeEntity tile = tracked.tile;
            if (tile == null || tile.isInvalid() || !isVisible(tile, frustum, cameraX, cameraY, cameraZ, maxDistanceSq))
                continue;
            if (tracked.joined == JOINED_UNKNOWN) {
                tracked.joined = getJoinedSides(tile, tracked.pipe());
            }
            hotPipes.add(tracked);
        }
    }

    /** Draws the glow of {@link #hotPipes} from scratch. Expects the glow's blending and depth state to be set up. */
    private void drawGlow(double cameraX, double cameraY, double cameraZ, double time) {
        // Avoids float precision issues far from the origin
        final Tessellator tessellator = Tessellator.instance;
        tessellator.setTranslation(-cameraX, -cameraY, -cameraZ);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        for (int i = 0; i < hotPipes.size(); i++) {
            final TrackedPipe tracked = hotPipes.get(i);
            final float wave = (float) Math.sin(time * GLOW_WAVE_SPEED - getGlowPhase(tracked.tile));
            addGlow(tessellator, tracked.tile, tracked.joined, getPulse(wave));
            if ((i + 1) % PIPES_PER_BATCH == 0) {
                tessellator.draw();
                tessellator.startDrawingQuads();
            }
        }
        tessellator.draw();
        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /** @return whether the pipe, glow halo included, is in view and within render distance */
    private static boolean isVisible(BaseMetaPipeEntity tile, Frustrum frustum, double cameraX, double cameraY,
        double cameraZ, double maxDistanceSq) {
        final double dx = tile.xCoord + 0.5D - cameraX;
        final double dy = tile.yCoord + 0.5D - cameraY;
        final double dz = tile.zCoord + 0.5D - cameraZ;
        if (dx * dx + dy * dy + dz * dz > maxDistanceSq) return false;
        return frustum.isBoxInFrustum(
            tile.xCoord - CULL_MARGIN,
            tile.yCoord - CULL_MARGIN,
            tile.zCoord - CULL_MARGIN,
            tile.xCoord + 1 + CULL_MARGIN,
            tile.yCoord + 1 + CULL_MARGIN,
            tile.zCoord + 1 + CULL_MARGIN);
    }

    /** @return where in its pulse this pipe is, in radians. Offset by position so runs of pipe ripple */
    static double getGlowPhase(BaseMetaPipeEntity tile) {
        // Double, as coordinates are too large for float precision
        return ((double) tile.xCoord + tile.yCoord + tile.zCoord) * 0.9D;
    }

    /** @return brightness as a fraction of full, at a point in the pulse from -1 to 1 */
    static float getPulse(float wave) {
        return 1.0F - GLOW_WAVE + GLOW_WAVE * wave;
    }

    /**
     * Adds every layer of a pipe's glow.
     *
     * @param pulse brightness from the pulse, as a fraction of full
     */
    static void addGlow(Tessellator tessellator, BaseMetaPipeEntity tile, int joined, float pulse) {
        final MTEFluidPipe pipe = (MTEFluidPipe) tile.getMetaTileEntity();
        final float level = pipe.getThermals()
            .getShownLevel();
        final float[] color = FluidPipeThermals.getHeatGlowColor(level);
        final float red = color[0] / 255.0F, green = color[1] / 255.0F, blue = color[2] / 255.0F;
        final float glow = FluidPipeThermals.getGlowFraction(level);
        final float strength = (GLOW_MIN_STRENGTH + (1.0F - GLOW_MIN_STRENGTH) * glow) * pulse;
        final BoxVisitor box = (x0, y0, z0, x1, y1, z1,
            faces) -> addPlainBox(tessellator, x0, y0, z0, x1, y1, z1, faces);

        for (int layer = 0; layer < GLOW_LAYER_OFFSETS.length; layer++) {
            tessellator.setColorRGBA_F(red, green, blue, GLOW_LAYER_INTENSITIES[layer] * strength);
            forEachBox(tile, pipe, joined, GLOW_LAYER_OFFSETS[layer], box);
        }
    }

    /**
     * @param stages a value for each frost stage, lightest first
     * @param none   the value with no frost at all
     * @param amount how heavy the frost is, from 0 to {@link FluidPipeThermals#FROST_LEVELS}
     * @return the value for that much frost, blending between stages
     */
    static float getStageValue(float[] stages, float none, float amount) {
        if (amount <= 1.0F) return none + (stages[0] - none) * Math.max(0F, amount);
        final int stage = Math.min((int) amount, stages.length - 1);
        final float along = Math.min(1.0F, amount - stage);
        return stages[stage - 1] + (stages[stage] - stages[stage - 1]) * along;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        final Minecraft mc = Minecraft.getMinecraft();
        final World world = mc.theWorld;
        if (world == null || mc.isGamePaused()) return;
        tick++;
        tickParticles(world);
        // Before the early return, so a pipe's last fade step still rebuilds after it stops being tracked
        if (tick % REBUILD_TICKS == 0) rebuildPendingSections(world);
        // Before pipes are looked at, so those whose glow just left the cache are drawn each frame from this tick on
        GLOW_CACHE.rebuildDirty();
        if (TRACKED_PIPES.isEmpty()) {
            glowCandidates.clear();
            return;
        }

        tickPipes(mc, world);
    }

    /** Moves every particle on a tick and drops those that have died or are left in another world. */
    private void tickParticles(World world) {
        if (particles.isEmpty()) return;
        int kept = 0;
        for (int i = 0; i < particles.size(); i++) {
            final EntityFX particle = particles.get(i);
            if (particle.worldObj == world) particle.onUpdate();
            if (particle.isDead || particle.worldObj != world) continue;
            particles.set(kept++, particle);
        }
        particles.subList(kept, particles.size())
            .clear();
    }

    private void addParticle(EntityFX particle) {
        if (particles.size() >= MAX_PARTICLES) return;
        particles.add(particle);
    }

    private static void rebuildPendingSections(World world) {
        if (PENDING_REBUILDS.isEmpty()) return;
        if (world == trackedWorld) {
            final LongIterator iterator = PENDING_REBUILDS.iterator();
            while (iterator.hasNext()) {
                final long section = iterator.nextLong();
                final int x = (int) (section >> 40) << 4, z = (int) (section << 24 >> 40) << 4,
                    y = (int) (section << 48 >> 48) << 4;
                // Inset by one, as the world widens the range by a block and would rebuild the neighbours too
                world.markBlockRangeForRenderUpdate(x + 1, y + 1, z + 1, x + 14, y + 14, z + 14);
            }
        }
        PENDING_REBUILDS.clear();
    }

    /**
     * Fades every tracked pipe, drops those that are gone or back to normal, and finds the pipes that may glow in
     * view or give off particles.
     */
    private void tickPipes(Minecraft mc, World world) {
        final Entity viewer = mc.renderViewEntity;
        final boolean particles = Client.render.renderFluidPipeThermalEffects && viewer != null
            && mc.gameSettings.particleSetting < 2;
        final float settingFactor = mc.gameSettings.particleSetting == 1 ? 0.4F : 1.0F;
        final double glowDistance = mc.gameSettings.renderDistanceChunks * 16.0D + CANDIDATE_MARGIN;
        final double glowDistanceSq = glowDistance * glowDistance;
        final int recheckTick = tick % RECHECK_TICKS;

        glowCandidates.clear();
        particlePipes.clear();
        particleChances.clear();
        float totalChance = 0;

        final ObjectIterator<Object2ObjectMap.Entry<ChunkCoordinates, TrackedPipe>> iterator = TRACKED_PIPES
            .object2ObjectEntrySet()
            .fastIterator();
        while (iterator.hasNext()) {
            final Object2ObjectMap.Entry<ChunkCoordinates, TrackedPipe> entry = iterator.next();
            final TrackedPipe tracked = entry.getValue();
            final BaseMetaPipeEntity cached = tracked.tile;
            final BaseMetaPipeEntity tile = cached == null || tracked.recheckTick == recheckTick
                ? getLivePipe(entry.getKey(), world)
                : hasEffects(cached) ? cached : null;
            if (tile == null) {
                GLOW_CACHE.update(tracked, PipeGlowCache.NOT_CACHED);
                iterator.remove();
                continue;
            }
            // Neighbours may have changed, so look again once in a while, and whenever this pipe's connections do
            final int connections = tile.getConnections() & 63;
            if (tile != cached || tracked.recheckTick == recheckTick || connections != tracked.connections) {
                tracked.tile = tile;
                tracked.connections = connections;
                tracked.joined = JOINED_UNKNOWN;
            }
            final MTEFluidPipe pipe = tracked.pipe();

            // Fade even with effects off, since block light follows it
            final FluidPipeThermalState thermals = pipe.getThermals();
            thermals.advanceFade(1);
            final float level = thermals.getShownLevel();
            // Hot pipes that don't glow only get heat haze
            final boolean glows = level > 0 && FluidPipeThermals.getGlowFraction(level) > 0;
            // Pipes glowing steadily are baked, those fading change every tick so are drawn each frame
            if (glows && !thermals.isFading()) {
                if (tracked.joined == JOINED_UNKNOWN) tracked.joined = getJoinedSides(tile, pipe);
                GLOW_CACHE.update(tracked, PipeGlowCache.getKey(Math.round(level), tracked.joined, connections));
            } else {
                GLOW_CACHE.update(tracked, PipeGlowCache.NOT_CACHED);
            }
            if (viewer == null) continue;

            final double dx = tile.xCoord + 0.5D - viewer.posX;
            final double dy = tile.yCoord + 0.5D - viewer.posY;
            final double dz = tile.zCoord + 0.5D - viewer.posZ;
            final double distanceSq = dx * dx + dy * dy + dz * dz;
            if (glows && !tracked.inCache && distanceSq <= glowDistanceSq) glowCandidates.add(tracked);
            if (!particles || distanceSq > PARTICLE_DISTANCE_SQ) continue;

            final float chance = level > 0 ? getHeatParticleChance(level) : getColdParticleChance(level);
            if (chance <= 0) continue;
            particlePipes.add(tracked);
            particleChances.add(chance * settingFactor);
            totalChance += chance * settingFactor;
        }

        if (particlePipes.isEmpty()) return;
        final float scale = Math.min(1.0F, PARTICLE_BUDGET / totalChance);
        for (int i = 0; i < particlePipes.size(); i++) {
            if (random.nextFloat() >= particleChances.getFloat(i) * scale) continue;
            final BaseMetaPipeEntity tile = particlePipes.get(i).tile;
            final MTEFluidPipe pipe = particlePipes.get(i)
                .pipe();
            if (pipe.getThermals()
                .getShownLevel() > 0) spawnHeatParticle(mc, world, tile, pipe);
            else spawnColdParticle(mc, world, tile, pipe);
        }
    }

    /** @return chance per tick of a heat haze wisp at this level */
    private static float getHeatParticleChance(float level) {
        final float heat = FluidPipeThermals.getHeatFraction(level);
        // Fewer particles while below level 1
        final float warmth = Math.min(1.0F, level);
        return (HAZE_CHANCE_MIN + (HAZE_CHANCE_MAX - HAZE_CHANCE_MIN) * heat) * warmth;
    }

    /** @return chance per tick of a vapour wisp at this level */
    private static float getColdParticleChance(float level) {
        final float frost = FluidPipeThermals.getFrostAmount(level);
        return frost <= 0 ? 0 : getStageValue(MIST_CHANCES, 0F, frost);
    }

    /** Cold vapour sinking off a frosted pipe. */
    private void spawnColdParticle(Minecraft mc, World world, BaseMetaPipeEntity tile, MTEFluidPipe pipe) {
        final float frost = FluidPipeThermals.getFrostAmount(
            pipe.getThermals()
                .getShownLevel());
        // From the bottom of the pipe, drifting down and out
        final double thickness = Math.min(1.0D, pipe.getThickness());
        final double min = (1.0D - thickness) / 2.0D;
        addParticle(
            new PipeColdMistFX(
                world,
                tile.xCoord + min + random.nextDouble() * thickness,
                tile.yCoord + min + random.nextDouble() * thickness * 0.4D,
                tile.zCoord + min + random.nextDouble() * thickness,
                (random.nextDouble() - 0.5D) * 0.012D,
                -0.004D - random.nextDouble() * 0.01D,
                (random.nextDouble() - 0.5D) * 0.012D,
                frost / FluidPipeThermals.FROST_LEVELS));
    }

    /** Heat haze off the top of a hot pipe. */
    private void spawnHeatParticle(Minecraft mc, World world, BaseMetaPipeEntity tile, MTEFluidPipe pipe) {
        final float heat = FluidPipeThermals.getHeatFraction(
            pipe.getThermals()
                .getShownLevel());
        final double thickness = Math.min(1.0D, pipe.getThickness());
        final double min = (1.0D - thickness) / 2.0D;
        addParticle(
            new PipeHeatHazeFX(
                world,
                tile.xCoord + min + random.nextDouble() * thickness,
                tile.yCoord + 1.0D - min + 0.05D,
                tile.zCoord + min + random.nextDouble() * thickness,
                heat));
    }

    @FunctionalInterface
    private interface BoxVisitor {

        /** A box in world coordinates, and which of its faces (one bit per {@link ForgeDirection}) are outer faces. */
        void visit(double x0, double y0, double z0, double x1, double y1, double z1, int faces);
    }

    /**
     * Splits the pipe's shape, grown by {@code d}, into boxes. Full block pipes are one box, others are a centre plus
     * an arm per connection. Sides in {@code joined} are left open so runs are seamless, other ends are capped.
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
        return pipe
            .getJoinedSides(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, tile.getConnections() & 63);
    }

    private static boolean isConnected(int connections, ForgeDirection side) {
        return (connections & side.flag) != 0;
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
