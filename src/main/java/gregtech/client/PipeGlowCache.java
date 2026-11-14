package gregtech.client;

import java.util.ArrayDeque;

import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.texture.DynamicTexture;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.renderer.DirectTessellator;
import com.gtnewhorizon.gtnhlib.client.renderer.TessellatorManager;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.VertexBufferType;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.DefaultVertexFormat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.PipeThermalEffectsRenderer.TrackedPipe;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Glow for hot pipes that aren't fading, baked into one vertex buffer per chunk section so it isn't built and uploaded
 * again every frame. A section is rebuilt when one of its pipes starts or stops glowing steadily, or changes shape.
 * Pipes still fading, or waiting for their section to be rebuilt, are drawn each frame by
 * {@link PipeThermalEffectsRenderer} instead.
 * <p>
 * The pulse can't be baked, so each pipe's phase goes in its texture u, and a texture of the pulse is scrolled past by
 * the texture matrix. The texture is white with the pulse in its alpha, so it scales each layer's alpha exactly as the
 * per-frame glow does.
 */
@SideOnly(Side.CLIENT)
final class PipeGlowCache {

    static final int NOT_CACHED = -1;
    /** Time spent rebuilding sections per tick, after the first. The rest wait for the next tick. */
    private static final long REBUILD_BUDGET_NANOS = 3_000_000L;
    private static final int WAVE_TEXTURE_SIZE = 256;
    private static final double TWO_PI = Math.PI * 2.0D;
    private static final TrackedPipe[] NO_PIPES = new TrackedPipe[0];

    private final Long2ObjectOpenHashMap<Section> sections = new Long2ObjectOpenHashMap<>();
    private final ArrayDeque<Section> dirty = new ArrayDeque<>();
    private @Nullable DynamicTexture waveTexture;

    /** The baked glow of one chunk section. */
    private static final class Section {

        final long key;
        /** Block position of the section's corner, which the buffer is relative to. */
        final int x, y, z;
        /** Pipes that should be baked. */
        final ObjectArrayList<TrackedPipe> pipes = new ObjectArrayList<>();
        /** Pipes in {@link #buffer}. */
        TrackedPipe[] baked = NO_PIPES;
        @Nullable
        IVertexArrayObject buffer;
        boolean dirty;

        Section(long key, int x, int y, int z) {
            this.key = key;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    /**
     * @return the key to bake a steadily glowing pipe with. Changes whenever its glow would look different.
     */
    static int getKey(int level, int joined, int connections) {
        return level | joined << 4 | connections << 10;
    }

    boolean isEmpty() {
        return sections.isEmpty();
    }

    /** Sets what's baked for a pipe, and marks its section to be rebuilt if that changed. */
    void update(TrackedPipe pipe, int key) {
        if (key == pipe.cacheKey) return;
        final long sectionKey = PipeThermalEffectsRenderer.packSection(pipe.x >> 4, pipe.y >> 4, pipe.z >> 4);
        Section section = sections.get(sectionKey);
        if (section == null) {
            section = new Section(sectionKey, pipe.x & ~15, pipe.y & ~15, pipe.z & ~15);
            sections.put(sectionKey, section);
        }
        if (pipe.cacheKey == NOT_CACHED) section.pipes.add(pipe);
        else if (key == NOT_CACHED) section.pipes.remove(pipe);
        pipe.cacheKey = key;
        if (!section.dirty) {
            section.dirty = true;
            dirty.add(section);
        }
    }

    /** Rebuilds sections whose pipes changed, at least one and then until the time budget runs out. */
    void rebuildDirty() {
        final long start = System.nanoTime();
        while (!dirty.isEmpty()) {
            final Section section = dirty.poll();
            section.dirty = false;
            rebuild(section);
            if (System.nanoTime() - start > REBUILD_BUDGET_NANOS) break;
        }
    }

    private void rebuild(Section section) {
        for (TrackedPipe pipe : section.baked) pipe.inCache = false;
        section.baked = NO_PIPES;
        if (section.buffer != null) {
            section.buffer.delete();
            section.buffer = null;
        }
        if (section.pipes.isEmpty()) {
            sections.remove(section.key);
            return;
        }

        final DirectTessellator tessellator = TessellatorManager
            .startCapturingDirect(DefaultVertexFormat.POSITION_TEXTURE_COLOR);
        boolean stopped = false;
        try {
            tessellator.startDrawingQuads();
            tessellator.setTranslation(-section.x, -section.y, -section.z);
            for (TrackedPipe pipe : section.pipes) {
                if (pipe.tile == null) continue;
                // Phase as a fraction of a turn, so the pulse texture repeats across it
                final double phase = PipeThermalEffectsRenderer.getGlowPhase(pipe.tile) / TWO_PI;
                tessellator.setTextureUV(phase - Math.floor(phase), 0.5D);
                PipeThermalEffectsRenderer.addGlow(tessellator, pipe.tile, pipe.joined, 1.0F);
            }
            tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            if (tessellator.getVertexCount() > 0) {
                section.buffer = TessellatorManager.stopCapturingDirectToVBO(VertexBufferType.IMMUTABLE);
                stopped = true;
            }
        } finally {
            tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            if (!stopped) TessellatorManager.stopCapturingDirect();
        }
        section.baked = section.pipes.toArray(NO_PIPES);
        for (TrackedPipe pipe : section.baked) pipe.inCache = true;
    }

    /**
     * Draws every baked section in view. Expects the glow's blending and depth state to be set up, and the texture
     * matrix mode to be free to use.
     *
     * @param time world time in ticks, with the partial tick
     */
    void render(double cameraX, double cameraY, double cameraZ, Frustrum frustum, double maxDistance, double time) {
        if (sections.isEmpty()) return;
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getWaveTexture().getGlTextureId());
        // Moves the pulse along by the same amount for every pipe, as the per-frame glow does with time
        final double turns = time * PipeThermalEffectsRenderer.GLOW_WAVE_SPEED / TWO_PI;
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glTranslated(-(turns - Math.floor(turns)), 0.0D, 0.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        // A section's centre is up to 14 blocks from its furthest pipe's glow
        final double reach = maxDistance + 14.0D;
        final double reachSq = reach * reach;
        final double margin = PipeThermalEffectsRenderer.CULL_MARGIN;
        for (Section section : sections.values()) {
            if (section.buffer == null) continue;
            final double dx = section.x + 8.0D - cameraX, dy = section.y + 8.0D - cameraY,
                dz = section.z + 8.0D - cameraZ;
            if (dx * dx + dy * dy + dz * dz > reachSq || !frustum.isBoxInFrustum(
                section.x - margin,
                section.y - margin,
                section.z - margin,
                section.x + 16 + margin,
                section.y + 16 + margin,
                section.z + 16 + margin)) continue;
            GL11.glPushMatrix();
            GL11.glTranslated(section.x - cameraX, section.y - cameraY, section.z - cameraZ);
            section.buffer.render();
            GL11.glPopMatrix();
        }

        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * @return a row of the pulse, as the alpha of a white texture. Across the texture is one turn of the pulse, so the
     *         phase in turns picks the point on it
     */
    private DynamicTexture getWaveTexture() {
        if (waveTexture != null) return waveTexture;
        final DynamicTexture texture = new DynamicTexture(WAVE_TEXTURE_SIZE, 1);
        final int[] data = texture.getTextureData();
        for (int i = 0; i < WAVE_TEXTURE_SIZE; i++) {
            final double turn = (i + 0.5D) / WAVE_TEXTURE_SIZE;
            // The per-frame glow's pulse, sin(time * speed - phase), with the texture scrolled back by the time
            final float pulse = PipeThermalEffectsRenderer.getPulse((float) Math.sin(-TWO_PI * turn));
            data[i] = Math.round(pulse * 255.0F) << 24 | 0xFFFFFF;
        }
        texture.updateDynamicTexture();
        // Smooth between samples, where the upload leaves it blocky. Wraps, as the upload leaves it
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlTextureId());
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        waveTexture = texture;
        return texture;
    }

    /** Frees every buffer, as when leaving the world. */
    void clear() {
        for (Section section : sections.values()) {
            if (section.buffer != null) section.buffer.delete();
            for (TrackedPipe pipe : section.baked) pipe.inCache = false;
        }
        sections.clear();
        dirty.clear();
    }
}
