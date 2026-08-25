package tectech.rendering.EOH;

import static tectech.Reference.MODID;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_0;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_1;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_2;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import cpw.mods.fml.client.FMLClientHandler;
import gregtech.api.enums.Mods;
import gregtech.common.render.shader.MeshBuilder;
import gregtech.common.render.shader.QuadSink;
import gregtech.common.render.shader.RenderState;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.ShaderRecipe;
import gregtech.common.render.shader.SharedShaders;
import gregtech.common.render.shader.Uniform;
import gregtech.common.render.shader.VertexAttribute;
import gtneioreplugin.plugin.block.ModBlocks;
import tectech.TecTech;
import tectech.loader.ConfigHandler;
import tectech.thing.block.TileEntityEyeOfHarmony;

public abstract class EOHRenderingUtils {

    private static final Color EOHStarColour = new Color(1.0f, 0.4f, 0.05f, 1.0f);

    private static final float[] ROTATION_SPEEDS = { 1.5f, 1.2f, 1.6f };
    private static final float[] BASE_ROTATIONS = { 130f, -49f, 67f };
    private static final ResourceLocation SPACE_LAYER_TEXTURE = new ResourceLocation(MODID, "models/spaceLayer.png");

    public static void renderEOHStar(Matrix4fc base, IItemRenderer.ItemRenderType type, float partialTicks,
        double starRadius) {
        renderStar(base, type, EOHStarColour, partialTicks, starRadius);
    }

    // Used for GORGE item renderer only.
    private static final Color GORGEStarColour = new Color(1.0f, 1.0f, 1.0f, 1.0f);

    public static void renderGORGEStar(Matrix4fc base, IItemRenderer.ItemRenderType type, float partialTicks,
        double starRadius) {
        renderStar(base, type, GORGEStarColour, partialTicks, starRadius);
    }

    private static final Matrix4f starBase = new Matrix4f();

    private static void renderStar(Matrix4fc base, IItemRenderer.ItemRenderType type, Color color, float partialTicks,
        double starRadius) {
        if (!shadersReady()) return;

        starBase.set(base);
        if (type == IItemRenderer.ItemRenderType.INVENTORY) starBase.rotateY((float) Math.PI);
        else if (type == IItemRenderer.ItemRenderType.EQUIPPED
            || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
                starBase.translate(0.5f, 0.5f, 0.5f);
                if (type == IItemRenderer.ItemRenderType.EQUIPPED) starBase.rotateY((float) Math.PI / 2f);
            }

        final boolean blendWas = GL11.glGetBoolean(GL11.GL_BLEND);
        final long blendFuncWas = RenderState.savedBlendFunc();

        GL11.glEnable(GL11.GL_BLEND);
        final long cullWas = beginSphereCull(false);

        texturedShader().use();
        eohSphere.bind();

        renderStarLayer(0, STAR_LAYER_0, color, 1.0f, partialTicks, starRadius);
        renderStarLayer(1, STAR_LAYER_1, color, 0.4f, partialTicks, starRadius);
        renderStarLayer(2, STAR_LAYER_2, color, 0.2f, partialTicks, starRadius);

        eohSphere.unbind();
        ShaderProgram.clear();

        endSphereCull(cullWas);
        RenderState.restore(GL11.GL_BLEND, blendWas);
        RenderState.restoreBlendFunc(blendFuncWas);
    }

    private static final Vector3f[] LAYER_AXIS = { new Vector3f(0, 1, 1).normalize(), new Vector3f(1, 1, 0).normalize(),
        new Vector3f(1, 0, 1).normalize() };

    private static final Matrix4f layerMatrix = new Matrix4f();

    private static void renderStarLayer(int layer, ResourceLocation texture, Color color, float alpha,
        float partialTicks, double starRadius) {

        if (layer >= 3) throw new IllegalArgumentException("Star rendering only supports three layers.");

        if (alpha < 1.0f) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        } else {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        FMLClientHandler.instance()
            .getClient()
            .getTextureManager()
            .bindTexture(texture);

        final float rotation = (BASE_ROTATIONS[layer] + ROTATION_SPEEDS[layer] * partialTicks) % 360f;
        final int maxLayer = 2;
        final float scale = (float) (starRadius * Math.pow(0.95f, maxLayer - layer));
        final Vector3f axis = LAYER_AXIS[layer];

        layerMatrix.set(starBase)
            .rotate((float) Math.toRadians(rotation), axis.x, axis.y, axis.z)
            .scale(scale);

        final ShaderHandle shader = texturedShader();
        GL20.glUniform4f(
            shader.loc(SharedShaders.U_TINT),
            color.getRed() / 255f,
            color.getGreen() / 255f,
            color.getBlue() / 255f,
            alpha);
        shader.uploadModel(layerMatrix);
        eohSphere.draw();
    }

    public static final Matrix4fc IDENTITY = new Matrix4f();

    private static final int MAX_ORBIT_OBJECTS = 16;

    private static final int VERTICES_PER_BLOCK = 36;

    private static final ShaderRecipe ORBIT = ShaderRecipe.of(Mods.GregTech.resourceDomain, "orbit")
        .required("u_Objects")
        .sampler("u_Texture", 0)
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION)
        .attribute("a_UV", VertexAttribute.UV)
        .attribute("a_Instance", VertexAttribute.INSTANCE_INDEX);

    private static final Uniform ORBIT_OBJECTS = ORBIT.uniform("u_Objects");

    private static ShaderHandle orbitShader;

    private static final int EOH_TESSELLATION = 64;

    private static IVertexArrayObject eohSphere;

    /** One mesh per drawn block sequence. FIFO eviction */
    private static final List<OrbitMesh> ORBIT_MESHES = new ArrayList<>();

    private static boolean warnedOrbitOverflow;

    private static final FloatBuffer orbitTransforms = BufferUtils.createFloatBuffer(MAX_ORBIT_OBJECTS * 6);
    private static final float DEG_TO_RAD = (float) Math.PI / 180f;

    public static void reloadShaders() {
        releaseShaders();

        orbitShader = ORBIT.bake();
        if (SharedShaders.ready()) {
            eohSphere = buildSphere(texturedShader(), EOH_TESSELLATION, EOH_TESSELLATION);
        }
    }

    public static ShaderHandle texturedShader() {
        return SharedShaders.textured();
    }

    private static void releaseShaders() {
        if (eohSphere != null) {
            eohSphere.delete();
            eohSphere = null;
        }
        if (orbitShader != null) {
            orbitShader.release();
            orbitShader = null;
        }
        releaseOrbitMesh();
        // The USS planet cubes were built against the old shader's layout — they go with the orbit meshes.
        for (IVertexArrayObject vao : USS_PLANET_CUBES.values()) {
            vao.delete();
        }
        USS_PLANET_CUBES.clear();
    }

    private static void releaseOrbitMesh() {
        for (int i = 0; i < ORBIT_MESHES.size(); i++) {
            ORBIT_MESHES.get(i).vao.delete();
        }
        ORBIT_MESHES.clear();
    }

    /**
     * The USS planet system — one TEXTURED CUBE per planet (pass 9, user request): the same proven block-cube
     * pipeline the legacy orbit render uses ({@code addRenderedBlockInWorld} + the shared textured shader), but
     * with the USS orbit math the ships track — so no dependency on the legacy orbit shader, and the cubes orbit
     * EXACTLY where {@code USSFleetOrbit.planetAnchorPosition} resolves the ship's hover/beam (radius
     * {@code 0.2 + distance + 0.2·starSize}, angle {@code (0.3·time)/radius} — the pass-30 radius law, a planet
     * at X blocks taking X minutes to orbit; tilts xAngle/zAngle — do not "clean up" the orbit chain without
     * re-pointing the hover/beam).
     *
     * <p>
     * Each cube is the planet's own dimension-display block ({@code spec.dimension} via the IORE
     * {@code ModBlocks} map — the type IS its texture, no tint multiply), sized {@code spec.scale} (a unit cube
     * of ±0.5·scale — the same size the legacy orbit cubes used), and spun on its local axis at
     * {@code spec.rotationSpeed·0.1·time} (the legacy cube look). A spec whose dimension key does not resolve to
     * a registered block (mod absent / renamed) falls back to the pass-5.1 tinted sphere (USSPlanetColor) so the
     * planet still renders.
     *
     * @param base     the star-center model matrix (already translated to the TE position, see {@code EOHTileEntitySR})
     * @param specs    the explicit planet system (colors are the sphere-fallback tint; 0 → white)
     * @param time     world time + partial ticks (the shared animation clock)
     * @param starSize the star size factor (as used for the legacy orbit offset)
     */
    public static void renderUSSOrbits(Matrix4fc base, List<TileEntityEyeOfHarmony.PlanetSpec> specs, float time,
        float starSize) {
        if (!shadersReady() || specs == null || specs.isEmpty()) return;

        final int count = Math.min(specs.size(), MAX_USS_PLANETS);

        final boolean blendWas = GL11.glGetBoolean(GL11.GL_BLEND);
        final long blendFuncWas = RenderState.savedBlendFunc();

        // PASS 16 (user: "planets render slightly transparent — they should be fully opaque"): draw the planets
        // UNBLENDED, exactly like the legacy orbit path (rendered with GL_BLEND disabled). Every planet texture
        // (all 264 IORE dimension blocks + the star-layer fallback, verified alpha 255) is fully opaque, so
        // blending adds nothing here — but with it ON the planet inherits whatever blend FUNC an earlier
        // tile-entity renderer in the same pass left active (an additive (SRC_ALPHA, ONE) makes an alpha-1.0
        // planet read as "slightly transparent": dst = src + dst, the star layer behind bleeds through).
        // Blend OFF is immune to the function and matches the legacy planet look.
        GL11.glDisable(GL11.GL_BLEND);

        final ShaderHandle shader = texturedShader();
        shader.use();
        for (int i = 0; i < count; i++) {
            final TileEntityEyeOfHarmony.PlanetSpec spec = specs.get(i);
            final float radius = 0.2f + spec.distance + 0.2f * starSize;
            // Pass 30 (user: "a planet at X blocks should take X minutes to complete one full rotation"): the orbit
            // angle is the RADIUS law — the SAME law USSFleetOrbit.planetAnchorPosition uses for the ships'
            // hover/beam, so rendered planets and tracking ships never drift apart. (The old spec.orbitSpeed·0.1·time
            // was independent of radius: far planets swept the sky as fast as near ones.)
            final float orbitAngle = (USS_ORBIT_DEG_PER_TICK_PER_BLOCK * time / radius) % 360f;
            // The planet's own SPIN (rotation on its axis) keeps the legacy pace — pass 30 only re-pins the ORBIT.
            final float spinAngle = (spec.rotationSpeed * USS_ORBIT_SPEED_SCALE * time) % 360f;
            final float scale = Math.max(0.05f, spec.scale);

            planetMatrix.set(base)
                .rotate((float) Math.toRadians(spec.xAngle), 1f, 0f, 0f)
                .rotate((float) Math.toRadians(spec.zAngle), 0f, 0f, 1f)
                .rotate((float) Math.toRadians(orbitAngle), 0f, 1f, 0f)
                .translate(radius, 0f, 0f)
                .rotate((float) Math.toRadians(spinAngle), 0f, 1f, 0f)
                .scale(scale);

            // Null-guard: a missing/empty IORE map (mod absent / very early frame) must fall back to the sphere,
            // never crash the render thread.
            // PASS 17/21: the planet's orbit RING — thin, 25%-opaque (pass 21), in the planet's orbit plane.
            // Drawn before the planet so the opaque planet overwrites the ring wherever it is in front.
            renderUSSOrbitRing(base, spec, starSize);

            final Block block = (spec.dimension == null || ModBlocks.blocks == null) ? null
                : ModBlocks.blocks.get(spec.dimension);
            if (block != null) {
                // Textured cube (the user's cube look): its per-face UVs live in the block atlas, and the
                // texture IS the planet type — no tint multiply. The cube's faces are outward-wound, so the
                // world's default cull state (cull BACK, CCW front) is correct — do NOT wrap this in
                // beginSphereCull (that flips the convention for the inverted-wound sphere and would cull the
                // cube's near faces).
                bindTexture(TextureMap.locationBlocksTexture);
                GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1f, 1f, 1f, 1f);
                shader.uploadModel(planetMatrix);
                ussCubeFor(block).render();
            } else {
                // Unresolvable dimension key (mod absent / renamed): the proven pass-5.1 tinted-sphere fallback
                // keeps the planet visible — star layer under the tint for a little surface variation.
                bindTexture(STAR_LAYER_0);
                final int argb = spec.color != 0 ? spec.color : 0xFFFFFFFF;
                GL20.glUniform4f(
                    shader.loc(SharedShaders.U_TINT),
                    ((argb >> 16) & 0xFF) / 255f,
                    ((argb >> 8) & 0xFF) / 255f,
                    (argb & 0xFF) / 255f,
                    1f);
                shader.uploadModel(planetMatrix);
                final long cullWas = beginSphereCull(false);
                eohSphere.render();
                endSphereCull(cullWas);
            }
        }
        ShaderProgram.clear();

        RenderState.restore(GL11.GL_BLEND, blendWas);
        RenderState.restoreBlendFunc(blendFuncWas);
    }

    /**
     * PASS 17 orbit ring: tube radius (a thin circle). PASS 27 (user: "the orbital rings are a bit too
     * prominent — make them half as wide"): 0.025 → 0.0125 (ring body 0.05 → 0.025 blocks).
     */
    private static final float RING_TUBE_RADIUS = 0.0125f;

    /**
     * PASS 21 (user: "make the segment sizes around 8x longer to save vertices") then PASS 22 (user: "4x shorter —
     * they became too blocky"): circumference segments 96 → 12 → 48 (4× shorter than pass 21). The tube
     * cross-section stays 8 so the ring keeps a sliver of presence when viewed edge-on (the pass-11 coplanar
     * orbits are seen nearly edge-on from the ground).
     */
    private static final int RING_SEGMENTS = 48;

    private static final int RING_TUBE_SEGMENTS = 8;

    private static final Matrix4f ringMatrix = new Matrix4f();

    /**
     * PASS 21 ring color: the textured fragment shader is {@code texture × u_Tint}, so the ring samples a 1×1
     * pure-white texture and gets the planet's tint (and alpha) exactly — no atlas pixel to fight.
     */
    private static final ResourceLocation RING_TEXTURE = new ResourceLocation(MODID, "textures/misc/white.png");

    /**
     * One ring mesh per distinct orbit radius — the radius is stable per planet (same expression every frame,
     * like the cube's {@code Block} key), so a cache works like {@link #USS_PLANET_CUBES}.
     */
    private static final Map<Float, IVertexArrayObject> USS_ORBIT_RINGS = new LinkedHashMap<>();

    /**
     * PASS 17 (user request) + PASS 21 (user playtest: the ring "followed the camera"): one thin orbit RING per
     * USS planet — a circle in the EXACT plane the planet orbits in: the same {@code base · rotX(xAngle) ·
     * rotZ(zAngle)} chain and the same radius {@code 0.2 + distance + 0.2·starSize} that {@link #renderUSSOrbits}
     * uses for the planet, so the ring passes through the planet's orbit. A thin torus (not a flat annulus) so
     * it keeps a sliver of presence when viewed edge-on.
     *
     * <p>
     * <b>PASS 21 root cause:</b> the ring was raw Tessellator quads, but at that moment the shared textured GLSL
     * program was STILL BOUND ({@code shader.use()} at the top of {@link #renderUSSOrbits} stays bound until
     * {@code ShaderProgram.clear()}, and a {@code Tessellator.draw()} carries no program of its own) — so the
     * ring's vertices ran through that vertex shader ({@code gl_ModelViewProjectionMatrix * u_ModelMatrix *
     * a_Position}) with the PREVIOUS object's stale {@code u_ModelMatrix} (for the first ring: the dome shell's
     * matrix) — misplaced, and swinging around the scene as that stale planet's orbit angle advanced.
     *
     * <p>
     * <b>Fix:</b> draw the ring through the EXACT textured-shader VAO path the planet's cube uses — the vertices
     * are the local torus (orbit radius baked in), the model uniform carries {@code base · rotX · rotZ}, and the
     * color is {@code u_Tint} over the 1×1 white texture. The ring now sits in the same space as the cube BY
     * CONSTRUCTION — no raw vertex path left in this renderer.
     *
     * <p>
     * The ring is tinted with the planet's own color (its ore average, {@code spec.color}; unset → white), and
     * PASS 21 (user: "25% — more transparent"): alpha 0.25, down from 0.5.
     *
     * @param base     the star-center model matrix (as in {@link #renderUSSOrbits})
     * @param spec     the planet whose orbit to show
     * @param starSize the star size factor (the radius term, as in {@link #renderUSSOrbits})
     */
    public static void renderUSSOrbitRing(Matrix4fc base, TileEntityEyeOfHarmony.PlanetSpec spec, float starSize) {
        final float radius = 0.2f + spec.distance + 0.2f * starSize; // the planet's orbit radius, exactly
        ringMatrix.set(base)
            .rotate((float) Math.toRadians(spec.xAngle), 1f, 0f, 0f)
            .rotate((float) Math.toRadians(spec.zAngle), 0f, 0f, 1f);

        final int argb = spec.color != 0 ? spec.color : 0xFFFFFFFF;

        final boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        final boolean blendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        final long blendFuncWas = RenderState.savedBlendFunc();
        GL11.glDisable(GL11.GL_CULL_FACE); // mixed torus winding — do not rely on the face convention
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // pass 16 lesson: never inherit a prior
                                                                          // renderer's blend function
        GL11.glDepthMask(false); // pure overlay: depth-tested (planet/star occlude it), depth-WRITE off
        try {
            final ShaderHandle shader = texturedShader();
            bindTexture(RING_TEXTURE);
            GL20.glUniform4f(
                shader.loc(SharedShaders.U_TINT),
                ((argb >> 16) & 0xFF) / 255f,
                ((argb >> 8) & 0xFF) / 255f,
                (argb & 0xFF) / 255f,
                0.125f); // PASS 27 (user: "…drop their [transparency] by 0.5×"): 25% → 12.5%
            shader.uploadModel(ringMatrix);
            ussRingFor(radius).render();
        } finally {
            GL11.glDepthMask(true);
            RenderState.restoreBlendFunc(blendFuncWas);
            RenderState.restore(GL11.GL_BLEND, blendOn);
            RenderState.restore(GL11.GL_CULL_FACE, cullOn);
        }
    }

    /** One ring VAO per distinct orbit radius (baked torus) — cached like {@link #ussCubeFor}. */
    private static IVertexArrayObject ussRingFor(float radius) {
        IVertexArrayObject cached = USS_ORBIT_RINGS.get(radius);
        if (cached != null) {
            return cached;
        }
        final IVertexArrayObject built;
        // MeshBuilder capacity counts TRIANGLE vertices: 6 per quad (the cube's 36 = 6 faces × 6).
        try (MeshBuilder mesh = MeshBuilder.of(texturedShader(), RING_SEGMENTS * RING_TUBE_SEGMENTS * 6)) {
            for (int i = 0; i < RING_SEGMENTS; i++) {
                final float a0 = i * 2f * (float) Math.PI / RING_SEGMENTS;
                final float a1 = (i + 1) * 2f * (float) Math.PI / RING_SEGMENTS;
                for (int j = 0; j < RING_TUBE_SEGMENTS; j++) {
                    final float b0 = j * 2f * (float) Math.PI / RING_TUBE_SEGMENTS;
                    final float b1 = (j + 1) * 2f * (float) Math.PI / RING_TUBE_SEGMENTS;
                    ringVertex(mesh, a0, b0, radius);
                    ringVertex(mesh, a1, b0, radius);
                    ringVertex(mesh, a1, b1, radius);
                    ringVertex(mesh, a0, b1, radius);
                }
            }
            built = mesh.build();
        }
        if (USS_ORBIT_RINGS.size() >= MAX_USS_PLANETS) {
            final Iterator<Float> oldest = USS_ORBIT_RINGS.keySet()
                .iterator();
            if (oldest.hasNext()) {
                USS_ORBIT_RINGS.get(oldest.next())
                    .delete();
                oldest.remove();
            }
        }
        USS_ORBIT_RINGS.put(radius, built);
        return built;
    }

    /** One torus point (circle angle {@code a}, tube angle {@code b}); the UV is the white-texture center. */
    private static void ringVertex(MeshBuilder mesh, float a, float b, float radius) {
        final float rr = radius + RING_TUBE_RADIUS * (float) Math.cos(b);
        mesh.vertex(
            rr * (float) Math.cos(a),
            RING_TUBE_RADIUS * (float) Math.sin(b),
            rr * (float) Math.sin(a),
            0.5,
            0.5);
    }

    private static void bindTexture(ResourceLocation location) {
        FMLClientHandler.instance()
            .getClient()
            .getTextureManager()
            .bindTexture(location);
    }

    /** One cube mesh per drawn dimension block (the textured USS planet) — cached like the legacy ORBIT_MESHES. */
    private static final Map<Block, IVertexArrayObject> USS_PLANET_CUBES = new LinkedHashMap<>();

    private static final int MAX_USS_PLANET_CUBES = 16;

    private static IVertexArrayObject ussCubeFor(Block block) {
        IVertexArrayObject cached = USS_PLANET_CUBES.get(block);
        if (cached != null) {
            return cached;
        }
        IVertexArrayObject built;
        try (MeshBuilder mesh = MeshBuilder.of(texturedShader(), VERTICES_PER_BLOCK)) {
            addRenderedBlockInWorld(mesh, block, 0, IDENTITY);
            built = mesh.build();
        }
        if (USS_PLANET_CUBES.size() >= MAX_USS_PLANET_CUBES) {
            final Iterator<Block> oldest = USS_PLANET_CUBES.keySet()
                .iterator();
            if (oldest.hasNext()) {
                USS_PLANET_CUBES.get(oldest.next())
                    .delete();
                oldest.remove();
            }
        }
        USS_PLANET_CUBES.put(block, built);
        return built;
    }

    // PASS 22: systems now carry up to MAX_PLANETS_PER_SYSTEM (9) planets — the cap must cover every planet,
    // else the last one of a 9-planet system would be invisible.
    private static final int MAX_USS_PLANETS = 9;

    /** Orbit angular speed scale (matches the legacy {@code SPEED_SCALE} so USS planets spin at a similar pace). */
    private static final float USS_ORBIT_SPEED_SCALE = 0.1f;

    /**
     * Pass 30 (user: "a planet at X blocks should take X minutes to complete one full rotation"): orbit angular
     * speed per block of orbit radius — a planet at X blocks takes X minutes (X·1200 ticks) to orbit, so its
     * angular speed is 360°/(1200·X) = 0.3/X degrees per tick. MUST stay in sync with
     * {@code USSFleetOrbit.ORBIT_DEG_PER_TICK_PER_BLOCK} (the ships' hover/beam math uses the identical law).
     */
    private static final float USS_ORBIT_DEG_PER_TICK_PER_BLOCK = 0.3f;

    private static final Matrix4f planetMatrix = new Matrix4f();

    public static void renderOrbits(Matrix4fc base, List<TileEntityEyeOfHarmony.OrbitingObject> objects, float time,
        float starSize, float speedScale, float starRescale) {
        if (orbitShader == null || !orbitShader.isValid() || objects.isEmpty()) return;

        final int count = Math.min(objects.size(), MAX_ORBIT_OBJECTS);
        if (objects.size() > MAX_ORBIT_OBJECTS && !warnedOrbitOverflow) {
            warnedOrbitOverflow = true;
            TecTech.LOGGER.warn(
                "Eye of Harmony has {} orbiting objects, only {} can be drawn",
                objects.size(),
                MAX_ORBIT_OBJECTS);
        }

        final IVertexArrayObject mesh = orbitMesh(objects, count);
        if (mesh == null) return;

        orbitTransforms.clear();
        for (int i = 0; i < count; i++) {
            final TileEntityEyeOfHarmony.OrbitingObject obj = objects.get(i);

            final float orbitAngle = (obj.orbitSpeed * speedScale * time) % 360f;
            final float spinAngle = (obj.rotationSpeed * speedScale * time) % 360f;

            orbitTransforms.put(obj.zAngle * DEG_TO_RAD)
                .put(obj.xAngle * DEG_TO_RAD)
                .put(orbitAngle * DEG_TO_RAD)
                .put(spinAngle * DEG_TO_RAD)
                .put(-0.2f - obj.distance - starRescale * starSize)
                .put(obj.scale);
        }
        orbitTransforms.flip();

        orbitShader.use();
        GL20.glUniform3(orbitShader.loc(ORBIT_OBJECTS), orbitTransforms);
        orbitShader.uploadModel(base);
        mesh.render();
        ShaderProgram.clear();
    }

    private static IVertexArrayObject orbitMesh(List<TileEntityEyeOfHarmony.OrbitingObject> objects, int count) {
        for (int i = 0; i < ORBIT_MESHES.size(); i++) {
            final OrbitMesh cached = ORBIT_MESHES.get(i);
            if (cached.matches(objects, count)) return cached.vao;
        }

        final Block[] blocks = new Block[count];
        for (int i = 0; i < count; i++) {
            blocks[i] = objects.get(i).block;
        }

        final IVertexArrayObject built;
        try (MeshBuilder mesh = MeshBuilder.of(orbitShader, count * VERTICES_PER_BLOCK)) {
            for (int i = 0; i < count; i++) {
                mesh.instanceIndex(i);
                addRenderedBlockInWorld(mesh, blocks[i], 0, IDENTITY);
            }
            built = mesh.build();
        }

        final int cap = Math.max(1, ConfigHandler.visual.EOH_ORBIT_MESH_CACHE_SIZE);
        final int excess = ORBIT_MESHES.size() - cap + 1;
        if (excess > 0) {
            for (int i = 0; i < excess; i++) {
                ORBIT_MESHES.get(i).vao.delete();
            }
            ORBIT_MESHES.subList(0, excess)
                .clear();
        }
        ORBIT_MESHES.add(new OrbitMesh(blocks, built));
        return built;
    }

    private static final class OrbitMesh {

        private final Block[] blocks;
        private final IVertexArrayObject vao;

        OrbitMesh(Block[] blocks, IVertexArrayObject vao) {
            this.blocks = blocks;
            this.vao = vao;
        }

        boolean matches(List<TileEntityEyeOfHarmony.OrbitingObject> objects, int count) {
            if (blocks.length != count) return false;
            for (int i = 0; i < count; i++) {
                if (blocks[i] != objects.get(i).block) return false;
            }
            return true;
        }
    }

    private static boolean shadersReady() {
        return SharedShaders.ready() && eohSphere != null;
    }

    static final double[] BLOCK_X = { -0.5, -0.5, +0.5, +0.5, +0.5, +0.5, -0.5, -0.5 };
    static final double[] BLOCK_Y = { +0.5, -0.5, -0.5, +0.5, +0.5, -0.5, -0.5, +0.5 };
    static final double[] BLOCK_Z = { +0.5, +0.5, +0.5, +0.5, -0.5, -0.5, -0.5, -0.5 };

    private static final float[] cornerX = new float[8];
    private static final float[] cornerY = new float[8];
    private static final float[] cornerZ = new float[8];
    private static final Vector3f corner = new Vector3f();

    public static void addRenderedBlockInWorld(final QuadSink sink, final Block block, final int meta,
        final Matrix4fc transform) {
        for (int i = 0; i < 8; i++) {
            corner.set((float) BLOCK_X[i], (float) BLOCK_Y[i], (float) BLOCK_Z[i]);
            transform.transformPosition(corner);
            cornerX[i] = corner.x;
            cornerY[i] = corner.y;
            cornerZ[i] = corner.z;
        }
        emitBlock(sink, block, meta);
    }

    private static void emitBlock(final QuadSink sink, final Block block, final int meta) {
        IIcon texture;

        double minU;
        double maxU;
        double minV;
        double maxV;

        {
            texture = block.getIcon(4, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            sink.vertex(cornerX[1], cornerY[1], cornerZ[1], maxU, maxV);
            sink.vertex(cornerX[0], cornerY[0], cornerZ[0], maxU, minV);
            sink.vertex(cornerX[7], cornerY[7], cornerZ[7], minU, minV);
            sink.vertex(cornerX[6], cornerY[6], cornerZ[6], minU, maxV);
        }

        {
            // Bottom face.
            texture = block.getIcon(0, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            sink.vertex(cornerX[5], cornerY[5], cornerZ[5], maxU, minV);
            sink.vertex(cornerX[2], cornerY[2], cornerZ[2], maxU, maxV);
            sink.vertex(cornerX[1], cornerY[1], cornerZ[1], minU, maxV);
            sink.vertex(cornerX[6], cornerY[6], cornerZ[6], minU, minV);
        }

        {
            texture = block.getIcon(2, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            sink.vertex(cornerX[6], cornerY[6], cornerZ[6], maxU, maxV);
            sink.vertex(cornerX[7], cornerY[7], cornerZ[7], maxU, minV);
            sink.vertex(cornerX[4], cornerY[4], cornerZ[4], minU, minV);
            sink.vertex(cornerX[5], cornerY[5], cornerZ[5], minU, maxV);
        }

        {
            texture = block.getIcon(5, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            sink.vertex(cornerX[5], cornerY[5], cornerZ[5], maxU, maxV);
            sink.vertex(cornerX[4], cornerY[4], cornerZ[4], maxU, minV);
            sink.vertex(cornerX[3], cornerY[3], cornerZ[3], minU, minV);
            sink.vertex(cornerX[2], cornerY[2], cornerZ[2], minU, maxV);
        }

        {
            texture = block.getIcon(1, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            sink.vertex(cornerX[3], cornerY[3], cornerZ[3], maxU, maxV);
            sink.vertex(cornerX[4], cornerY[4], cornerZ[4], maxU, minV);
            sink.vertex(cornerX[7], cornerY[7], cornerZ[7], minU, minV);
            sink.vertex(cornerX[0], cornerY[0], cornerZ[0], minU, maxV);
        }

        {
            texture = block.getIcon(3, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            sink.vertex(cornerX[2], cornerY[2], cornerZ[2], maxU, maxV);
            sink.vertex(cornerX[3], cornerY[3], cornerZ[3], maxU, minV);
            sink.vertex(cornerX[0], cornerY[0], cornerZ[0], minU, minV);
            sink.vertex(cornerX[1], cornerY[1], cornerZ[1], minU, maxV);
        }
    }

    private static final long CULL_ENABLED_BIT = 1L << 32;

    public static long beginSphereCull(boolean invertFrontFace) {
        final long saved = (GL11.glGetBoolean(GL11.GL_CULL_FACE) ? CULL_ENABLED_BIT : 0L)
            | (GL11.glGetInteger(GL11.GL_FRONT_FACE) & 0xFFFFL) << 16
            | (GL11.glGetInteger(GL11.GL_CULL_FACE_MODE) & 0xFFFFL);

        GL11.glEnable(GL11.GL_CULL_FACE);
        if (invertFrontFace) {
            GL11.glFrontFace(GL11.GL_CW);
            GL11.glCullFace(GL11.GL_BACK);
        } else {
            GL11.glFrontFace(GL11.GL_CCW);
            GL11.glCullFace(GL11.GL_FRONT);
        }
        return saved;
    }

    public static void endSphereCull(long saved) {
        GL11.glFrontFace((int) (saved >>> 16) & 0xFFFF);
        GL11.glCullFace((int) saved & 0xFFFF);
        RenderState.restore(GL11.GL_CULL_FACE, (saved & CULL_ENABLED_BIT) != 0);
    }

    public static IVertexArrayObject buildSphere(ShaderHandle shader, int slices, int stacks) {
        try (MeshBuilder mesh = MeshBuilder.of(shader, slices * stacks * 6)) {
            for (int i = 0; i < stacks; i++) {
                final double v0 = (double) i / (double) stacks;
                final double v1 = (double) (i + 1) / (double) stacks;

                final double phi0 = Math.PI / 2.0 - i * Math.PI / stacks;
                final double phi1 = Math.PI / 2.0 - (i + 1) * Math.PI / stacks;

                final double y0 = Math.sin(phi0);
                final double y1 = Math.sin(phi1);

                final double r0 = Math.cos(phi0);
                final double r1 = Math.cos(phi1);

                for (int j = 0; j < slices; j++) {
                    final float uu0 = (float) (1.0 - (double) j / (double) slices);
                    final float uu1 = (float) (1.0 - (double) (j + 1) / (double) slices);

                    final double th0 = j * 2.0 * Math.PI / slices;
                    final double th1 = (j + 1) * 2.0 * Math.PI / slices;

                    final double x00 = r0 * Math.cos(th0);
                    final double z00 = r0 * Math.sin(th0);

                    final double x10 = r1 * Math.cos(th0);
                    final double z10 = r1 * Math.sin(th0);

                    final double x11 = r1 * Math.cos(th1);
                    final double z11 = r1 * Math.sin(th1);

                    final double x01 = r0 * Math.cos(th1);
                    final double z01 = r0 * Math.sin(th1);

                    mesh.triangleVertex(x00, y0, z00, uu0, v0);
                    mesh.triangleVertex(x10, y1, z10, uu0, v1);
                    mesh.triangleVertex(x11, y1, z11, uu1, v1);

                    mesh.triangleVertex(x00, y0, z00, uu0, v0);
                    mesh.triangleVertex(x11, y1, z11, uu1, v1);
                    mesh.triangleVertex(x01, y0, z01, uu1, v0);
                }
            }
            return mesh.build();
        }
    }

    private static final Matrix4f shellMatrix = new Matrix4f();

    public static void renderOuterSpaceShell(Matrix4fc base, double radius) {
        if (!shadersReady()) return;

        FMLClientHandler.instance()
            .getClient()
            .getTextureManager()
            .bindTexture(SPACE_LAYER_TEXTURE);

        // Pass 12: the dome radius comes from the caller (per-machine: legacy EoH 12.95, Voidcraft USS 27.1 since pass
        // 15)
        // instead of the old hardcoded 0.01f * 17.5f * 74f.
        final float scale = (float) radius;
        shellMatrix.set(base)
            .scale(-scale, scale, scale);

        final ShaderHandle shader = texturedShader();
        shader.use();
        GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1f, 1f, 1f, 1f);
        shader.uploadModel(shellMatrix);
        final long cullWas = beginSphereCull(true);
        eohSphere.render();
        endSphereCull(cullWas);
        ShaderProgram.clear();
    }
}
