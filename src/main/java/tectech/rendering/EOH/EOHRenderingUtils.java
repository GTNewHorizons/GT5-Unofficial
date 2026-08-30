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
import tectech.TecTech;
import tectech.loader.ConfigHandler;
import tectech.thing.block.TileEntityEyeOfHarmony;
import tectech.voidcraft.uss.USSFleetOrbit;

public abstract class EOHRenderingUtils {

    private static final Color EOHStarColour = new Color(1.0f, 0.4f, 0.05f, 1.0f);

    private static final float[] ROTATION_SPEEDS = { 1.5f, 1.2f, 1.6f };
    private static final float[] BASE_ROTATIONS = { 130f, -49f, 67f };
    private static final ResourceLocation SPACE_LAYER_TEXTURE = new ResourceLocation(MODID, "models/spaceLayer.png");

    /**
     * The USS star's base layers — luminance-grayscale versions of the legacy {@code models/StarLayerN.png} (the
     * legacy texture is orange, which skews the per-class tint): the star's color comes entirely from the tint, so
     * the base must be neutral gray.
     */
    private static final ResourceLocation USS_STAR_LAYER_0 = new ResourceLocation(
        MODID,
        "textures/uss/star/StarLayer0.png");
    private static final ResourceLocation USS_STAR_LAYER_1 = new ResourceLocation(
        MODID,
        "textures/uss/star/StarLayer1.png");
    private static final ResourceLocation USS_STAR_LAYER_2 = new ResourceLocation(
        MODID,
        "textures/uss/star/StarLayer2.png");

    private static final ResourceLocation[] LEGACY_STAR_LAYERS = { STAR_LAYER_0, STAR_LAYER_1, STAR_LAYER_2 };
    private static final ResourceLocation[] USS_STAR_LAYERS = { USS_STAR_LAYER_0, USS_STAR_LAYER_1, USS_STAR_LAYER_2 };

    /**
     * Per-layer blend alpha (index 0 = the innermost layer, 2 = the outermost halo): the halo layers carry more
     * alpha on USS stars so the glow reads as light scattering outward.
     */
    private static final float[] LEGACY_STAR_LAYER_ALPHA = { 1.0f, 0.4f, 0.2f };
    private static final float[] USS_STAR_LAYER_ALPHA = { 1.0f, 0.5f, 0.4f };

    /**
     * Per-layer brightness gain (index 0 = the innermost layer, 2 = the outermost halo): values above 1.0
     * overdrive the tint past full-bright — the clipped result reads as a hot glowing core and a blazing halo
     * (the legacy star uses 1.0f).
     */
    private static final float[] NO_LAYER_GAIN = { 1.0f, 1.0f, 1.0f };
    private static final float[] USS_STAR_LAYER_GAIN = { 1.0f, 1.6f, 2.0f };

    /**
     * Per-layer radius multipliers (index 0 = the innermost layer): the core is the SMALLEST sphere and the shell
     * layers grow outward to the star's full radius (the outermost shell is the largest and draws last, over the
     * core — it is the star's visible body).
     */
    private static final float USS_SHELL_STEP = 0.95f;
    private static final float[] USS_STAR_LAYER_SCALE = { USS_SHELL_STEP * USS_SHELL_STEP, USS_SHELL_STEP, 1.0f };

    /**
     * The halo star treatment's per-layer radius multipliers: the core keeps the star's full radius and the shell
     * layers expand PAST its rim. With the shells rendered outside-in (their far hemispheres only — the same
     * inverted winding the space shell uses) the depth test then hides the shells inside the core's disc and only
     * a glow ring beyond the rim is visible, so the dark core reads true instead of overdriven.
     */
    private static final float[] HALO_STAR_LAYER_SCALE = { 1.0f, 1.06f, 1.14f };

    /**
     * The USS star's surface rotates ten times slower than the legacy star's shimmer (legacy uses 1.0f).
     */
    private static final float USS_STAR_ROTATION_SCALE = 0.1f;

    /**
     * The midpoint blend of two colors (the per-channel average) — the USS star's middle shell reads as the
     * transition between the core color and the shell color.
     */
    private static Color midpoint(Color a, Color b) {
        return new Color(
            (a.getRed() + b.getRed()) / 510f,
            (a.getGreen() + b.getGreen()) / 510f,
            (a.getBlue() + b.getBlue()) / 510f,
            1.0f);
    }

    public static void renderEOHStar(Matrix4fc base, IItemRenderer.ItemRenderType type, float partialTicks,
        double starRadius) {
        renderStar(
            base,
            type,
            new Color[] { EOHStarColour, EOHStarColour, EOHStarColour },
            partialTicks,
            starRadius,
            LEGACY_STAR_LAYERS,
            1.0f,
            LEGACY_STAR_LAYER_ALPHA,
            NO_LAYER_GAIN,
            USS_STAR_LAYER_SCALE,
            false);
    }

    /**
     * The star tinted with the color the machine's star type carries (the mesh is a shared texture; the tint is what
     * distinguishes the star classes visually).
     *
     * @param color the opaque ARGB color of the star (the registered definition's color)
     */
    public static void renderEOHStar(Matrix4fc base, IItemRenderer.ItemRenderType type, Color color, float partialTicks,
        double starRadius) {
        renderStar(
            base,
            type,
            new Color[] { color, color, color },
            partialTicks,
            starRadius,
            LEGACY_STAR_LAYERS,
            1.0f,
            LEGACY_STAR_LAYER_ALPHA,
            NO_LAYER_GAIN,
            USS_STAR_LAYER_SCALE,
            false);
    }

    /**
     * The USS star: the three-layer mesh tinted with the star class's registered colors over the neutral-gray USS
     * base layers, so the tint reads true for any class color.
     *
     * The innermost layer keeps the star's CORE color; the middle shell is the midpoint between the core and shell
     * colors (the transition between them); the outermost shell carries the star's SHELL color in full, so dark or
     * exotic class colors keep their color all the way out. Every layer carries a brightness gain
     * ({@link #USS_STAR_LAYER_GAIN}) that overdrives the bright spots past full-bright for the hot-glow look.
     *
     * @param color      the opaque ARGB core color of the star (the registered definition's color)
     * @param shellColor the opaque ARGB shell color (the registered definition's shell color; 0 = unset → the core
     *                   color)
     * @param halo       when true the shell layers render outside-in as a glow ring beyond the core's rim (the
     *                   halo star treatment — see {@link #HALO_STAR_LAYER_SCALE})
     */
    public static void renderUSSStar(Matrix4fc base, IItemRenderer.ItemRenderType type, Color color, int shellColor,
        float partialTicks, double starRadius, boolean halo) {
        final Color shell = shellColor != 0 ? new Color(shellColor) : color;
        renderStar(
            base,
            type,
            new Color[] { color, midpoint(color, shell), shell },
            partialTicks,
            starRadius,
            USS_STAR_LAYERS,
            USS_STAR_ROTATION_SCALE,
            USS_STAR_LAYER_ALPHA,
            USS_STAR_LAYER_GAIN,
            halo ? HALO_STAR_LAYER_SCALE : USS_STAR_LAYER_SCALE,
            halo);
    }

    // Used for GORGE item renderer only.
    private static final Color GORGEStarColour = new Color(1.0f, 1.0f, 1.0f, 1.0f);

    public static void renderGORGEStar(Matrix4fc base, IItemRenderer.ItemRenderType type, float partialTicks,
        double starRadius) {
        renderStar(
            base,
            type,
            new Color[] { GORGEStarColour, GORGEStarColour, GORGEStarColour },
            partialTicks,
            starRadius,
            LEGACY_STAR_LAYERS,
            1.0f,
            LEGACY_STAR_LAYER_ALPHA,
            NO_LAYER_GAIN,
            USS_STAR_LAYER_SCALE,
            false);
    }

    private static final Matrix4f starBase = new Matrix4f();

    private static void renderStar(Matrix4fc base, IItemRenderer.ItemRenderType type, Color[] layerColors,
        float partialTicks, double starRadius, ResourceLocation[] layers, float rotationScale, float[] layerAlpha,
        float[] layerGain, float[] layerScale, boolean shellInverted) {
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

        texturedShader().use();
        eohSphere.bind();

        for (int layer = 0; layer < 3; layer++) {
            final boolean invert = shellInverted && layer > 0;
            final long cullWas = beginSphereCull(invert);
            renderStarLayer(
                layer,
                layers[layer],
                layerColors[layer],
                layerAlpha[layer],
                partialTicks,
                starRadius,
                rotationScale,
                layerScale[layer],
                layerGain[layer],
                invert);
            endSphereCull(cullWas);
        }

        eohSphere.unbind();
        ShaderProgram.clear();

        RenderState.restore(GL11.GL_BLEND, blendWas);
        RenderState.restoreBlendFunc(blendFuncWas);
    }

    private static final Vector3f[] LAYER_AXIS = { new Vector3f(0, 1, 1).normalize(), new Vector3f(1, 1, 0).normalize(),
        new Vector3f(1, 0, 1).normalize() };

    private static final Matrix4f layerMatrix = new Matrix4f();

    private static void renderStarLayer(int layer, ResourceLocation texture, Color color, float alpha,
        float partialTicks, double starRadius, float rotationScale, float scale, float gain, boolean invertWinding) {

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

        final float rotation = (BASE_ROTATIONS[layer] + ROTATION_SPEEDS[layer] * rotationScale * partialTicks) % 360f;
        final float radius = (float) (starRadius * scale);
        final Vector3f axis = LAYER_AXIS[layer];

        // A negative X inverts the mesh's winding (the space shell's trick): paired with the inverted cull mode
        // (beginSphereCull(true) in renderStar) only the far hemisphere draws — the layer renders "outside-in"
        // and the depth test hides whatever the core's surface stands between the camera and.
        layerMatrix.set(starBase)
            .rotate((float) Math.toRadians(rotation), axis.x, axis.y, axis.z)
            .scale(invertWinding ? -radius : radius, radius, radius);

        // The gain may push the tint channels past 1.0: the framebuffer clips them to full-bright, which is the
        // hot-glow look the overdriven layers want.
        final ShaderHandle shader = texturedShader();
        GL20.glUniform4f(
            shader.loc(SharedShaders.U_TINT),
            color.getRed() / 255f * gain,
            color.getGreen() / 255f * gain,
            color.getBlue() / 255f * gain,
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
        for (IVertexArrayObject vao : DYSON_SHELLS.values()) {
            vao.delete();
        }
        DYSON_SHELLS.clear();
    }

    private static void releaseOrbitMesh() {
        for (int i = 0; i < ORBIT_MESHES.size(); i++) {
            ORBIT_MESHES.get(i).vao.delete();
        }
        ORBIT_MESHES.clear();
    }

    /**
     * The USS planet system — one TEXTURED CUBE per planet: a shared cube (the cross-layout
     * {@code stitched.png} UVs, see {@link #ussStitchedCube()}) bound to each planet's own texture
     * ({@code spec.texture}), with the USS orbit math the ships track — so the cubes orbit EXACTLY where
     * {@code USSFleetOrbit.planetAnchorPosition} resolves the ship's hover/beam (radius
     * {@code 0.2 + distance + 0.2·starSize}, angle {@code (0.3·time)/radius} — the shared orbit law, a planet at
     * X blocks taking X minutes to orbit; tilts xAngle/zAngle — do not change the orbit chain without re-pointing
     * the hover/beam).
     *
     * <p>
     * Each cube is sized {@code spec.scale} (a unit cube of ±0.5·scale — the scale the definition's tier sets,
     * base ±10%), and spun on its local axis at {@code spec.rotationSpeed·0.1·time}. A spec whose texture is
     * missing (empty) falls back to the tinted sphere ({@code spec.color}, from USSPlanetColor) so the planet
     * still renders.
     *
     * @param base     the star-center model matrix (already translated to the TE position, see {@code EOHTileEntitySR})
     * @param specs    the explicit planet system (colors are the sphere-fallback tint; 0 → white)
     * @param time     world time + partial ticks (the shared animation clock)
     * @param starSize the star size factor (as used for the orbit offset)
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
            final float orbitAngle = (USSFleetOrbit.ORBIT_DEG_PER_TICK_PER_BLOCK * time / radius) % 360f;
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

            // The planet's orbit RING — a thin semi-transparent circle in the planet's orbit plane, drawn for every
            // planet. Drawn before the planet so the opaque planet overwrites the ring wherever it is in front.
            renderUSSOrbitRing(base, spec, starSize);

            final String texturePath = spec.texture;
            if (texturePath != null && !texturePath.isEmpty()) {
                // Textured cube: the planet's own bundled texture (the cross-layout stitched.png) bound over the
                // shared cube, no tint multiply. The cube's faces are outward-wound, so the world's default cull
                // state (cull BACK, CCW front) is correct — do NOT wrap this in beginSphereCull (that flips the
                // convention for the inverted-wound sphere and would cull the cube's near faces).
                bindTexture(textureLocation(texturePath));
                GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1f, 1f, 1f, 1f);
                shader.uploadModel(planetMatrix);
                ussStitchedCube().render();
            } else {
                // Unresolvable dimension key (mod absent / renamed): the proven pass-5.1 tinted-sphere fallback
                // keeps the planet visible — the neutral-gray USS star layer under the tint for a little surface
                // variation (the orange legacy layer would skew the planet's tint).
                bindTexture(USS_STAR_LAYER_0);
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

            // The planet's own RING TEXTURE (when this planet has one) — a flat ring image hugging the planet,
            // centered on it and locked to its spin. Drawn AFTER the planet, depth-tested with depth writes off:
            // the near side of the ring blends over the planet, and the far side (behind the planet) is rejected
            // by the depth test against the planet's written depth.
            if (spec.ringTexture != null && !spec.ringTexture.isEmpty()) {
                renderUSSPlanetRing(base, spec, starSize, orbitAngle, spinAngle, scale);
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

    /**
     * 1×1 pure-white orbit-ring tint texture: the textured fragment shader is {@code texture × u_Tint}, so the
     * orbit ring samples white and gets the planet's tint (and alpha) exactly — no atlas pixel to fight.
     */
    private static final ResourceLocation RING_TEXTURE = new ResourceLocation(MODID, "textures/misc/white.png");

    private static final Matrix4f ringMatrix = new Matrix4f();

    /**
     * One torus mesh per distinct orbit radius — the radius is stable per planet (same expression every frame), so a
     * cache works like {@link #USS_PLANET_CUBES}.
     */
    private static final Map<Float, IVertexArrayObject> USS_ORBIT_RINGS = new LinkedHashMap<>();

    /**
     * One thin orbit RING per USS planet — a circle in the exact plane the planet orbits in: the same
     * {@code base · rotX(xAngle) · rotZ(zAngle)} chain and the same radius {@code 0.2 + distance + 0.2·starSize}
     * that {@link #renderUSSOrbits} uses for the planet, so the ring passes through the planet's orbit. A thin
     * torus (not a flat annulus) so it keeps a sliver of presence when viewed edge-on (the coplanar orbits are
     * seen nearly edge-on from the ground).
     *
     * <p>
     * Drawn through the exact textured-shader VAO path the planet's cube uses, not raw {@code Tessellator} quads:
     * at this point the shared textured GLSL program is still bound ({@code shader.use()} at the top of
     * {@link #renderUSSOrbits} runs until {@code ShaderProgram.clear()}, and a raw draw carries no program of its
     * own), so raw vertices would run that shader with the previous object's stale {@code u_ModelMatrix} —
     * misplaced, and swinging around the scene as the camera moves. The torus vertices are local (the orbit radius
     * is baked in), the model uniform carries {@code base · rotX · rotZ}, and the color is {@code u_Tint} over the
     * 1×1 white texture — the ring sits in the same space as the cube by construction.
     *
     * <p>
     * Tinted with the planet's own color (its ore average, {@code spec.color}; unset → white) at 12.5% alpha.
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
        final boolean alphaTestOn = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        final boolean blendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        final long blendFuncWas = RenderState.savedBlendFunc();
        GL11.glDisable(GL11.GL_CULL_FACE); // mixed torus winding — do not rely on the face convention
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // never inherit a prior renderer's blend
                                                                          // function (the game loop does not reset
                                                                          // GL state between tile-entity renderers)
        GL11.glDepthMask(false); // pure overlay: depth-tested (planet/star occlude it), depth-WRITE off
        // The 0.125-alpha ring tint is below the world pass 0.5 GL_GREATER alpha-test reference (the vanilla
        // block-cutout state) and would be discarded entirely: the ring is a blended overlay, so it draws with
        // the test disabled.
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        try {
            final ShaderHandle shader = texturedShader();
            bindTexture(RING_TEXTURE);
            GL20.glUniform4f(
                shader.loc(SharedShaders.U_TINT),
                ((argb >> 16) & 0xFF) / 255f,
                ((argb >> 8) & 0xFF) / 255f,
                (argb & 0xFF) / 255f,
                0.125f);
            shader.uploadModel(ringMatrix);
            ussRingFor(radius).render();
        } finally {
            GL11.glDepthMask(true);
            RenderState.restoreBlendFunc(blendFuncWas);
            RenderState.restore(GL11.GL_BLEND, blendOn);
            RenderState.restore(GL11.GL_ALPHA_TEST, alphaTestOn);
            RenderState.restore(GL11.GL_CULL_FACE, cullOn);
        }
    }

    /**
     * The ring image's outer annulus edge, in units of the image's half-width (1 = the square's edge): every ring
     * texture places its outer edge at ~1.38 (measured per pixel across the whole set: 1.35–1.40).
     */
    private static final float RING_TEXTURE_OUTER_U = 1.38f;

    /**
     * The ring's outer edge world radius as a multiple of the planet's {@code scale}: the planet's cube spans
     * ±0.5·scale, so 1.8·scale puts the ring's outer edge at 3.6× the planet surface, leaving a visible gap
     * between the planet and the ring's inner hole (the image's hole otherwise sits at/below the planet surface).
     */
    private static final float RING_OUTER_EDGE = 1.8f;

    /**
     * The planet's own RING TEXTURE (when {@code spec.ringTexture} is set) — a flat ring image in the planet's own
     * plane: the same {@code base · rotX(xAngle) · rotZ(zAngle) · rotY(orbit) · translate(radius, 0, 0) ·
     * rotY(spin)} chain the planet's cube uses, so the ring is centered on the planet and locked to its spin. The
     * shared unit disc maps the image 1:1 onto a square of radius 1 and is scaled so the ring's OUTER edge (the
     * image's ~1.38·half-width, see {@link #RING_TEXTURE_OUTER_U}) lands at {@code RING_OUTER_EDGE · scale}.
     *
     * <p>
     * Drawn after the planet (depth-tested, depth writes off): the near side of the ring blends over the planet,
     * the far side is rejected by the planet's written depth; the image's transparent center keeps the planet
     * visible through the ring's hole.
     *
     * @param base       the star-center model matrix (as in {@link #renderUSSOrbits})
     * @param spec       the ringed planet
     * @param starSize   the star size factor (the orbit-radius term)
     * @param orbitAngle the planet's current orbit angle (degrees)
     * @param spinAngle  the planet's current spin angle (degrees)
     * @param scale      the planet's rendered scale (the cube spans ±0.5·scale)
     */
    public static void renderUSSPlanetRing(Matrix4fc base, TileEntityEyeOfHarmony.PlanetSpec spec, float starSize,
        float orbitAngle, float spinAngle, float scale) {
        final float radius = 0.2f + spec.distance + 0.2f * starSize; // the planet's orbit radius, exactly
        final float ringScale = RING_OUTER_EDGE * scale / RING_TEXTURE_OUTER_U;
        ringMatrix.set(base)
            .rotate((float) Math.toRadians(spec.xAngle), 1f, 0f, 0f)
            .rotate((float) Math.toRadians(spec.zAngle), 0f, 0f, 1f)
            .rotate((float) Math.toRadians(orbitAngle), 0f, 1f, 0f)
            .translate(radius, 0f, 0f)
            .rotate((float) Math.toRadians(spinAngle), 0f, 1f, 0f)
            .scale(ringScale);

        final boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        final boolean alphaTestOn = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        final boolean blendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        final long blendFuncWas = RenderState.savedBlendFunc();
        GL11.glDisable(GL11.GL_CULL_FACE); // a flat disc is single-wound — do not rely on the face convention
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // never inherit a prior renderer's blend
                                                                          // function (the game loop does not reset
                                                                          // GL state between tile-entity renderers)
        GL11.glDepthMask(false); // pure overlay: depth-tested (planet/star occlude it), depth-WRITE off
        // The ring image alpha shapes the ring; its sub-0.5 pixels would be clipped by the world pass 0.5
        // GL_GREATER alpha-test reference (the vanilla block-cutout state): the ring is a blended overlay, so
        // it draws with the test disabled.
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        try {
            final ShaderHandle shader = texturedShader();
            bindTexture(textureLocation(spec.ringTexture));
            GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1f, 1f, 1f, 1f); // the ring image's own alpha shapes it
            shader.uploadModel(ringMatrix);
            ussRingDisc().render();
        } finally {
            GL11.glDepthMask(true);
            RenderState.restoreBlendFunc(blendFuncWas);
            RenderState.restore(GL11.GL_BLEND, blendOn);
            RenderState.restore(GL11.GL_ALPHA_TEST, alphaTestOn);
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

    /**
     * Emit a flat unit ring-disc: a square in the xz plane (y = 0) from (-1,-1) to (1,1), mapped 1:1 to the planet's
     * ring image ({@link #renderUSSPlanetRing} scales it to the planet).
     */
    private static void emitRingDisc(QuadSink sink) {
        sink.vertex(-1f, 0f, -1f, 0f, 0f);
        sink.vertex(1f, 0f, -1f, 1f, 0f);
        sink.vertex(1f, 0f, 1f, 1f, 1f);
        sink.vertex(-1f, 0f, 1f, 0f, 1f);
    }

    /** The one shared ring-disc VAO (a unit square); the bound ring image and the per-planet scale vary per planet. */
    private static IVertexArrayObject USS_RING_DISC;

    private static IVertexArrayObject ussRingDisc() {
        if (USS_RING_DISC != null) {
            return USS_RING_DISC;
        }
        try (MeshBuilder mesh = MeshBuilder.of(texturedShader(), 6)) {
            emitRingDisc(mesh);
            USS_RING_DISC = mesh.build();
        }
        return USS_RING_DISC;
    }

    // ============================= Magnetar field loops (the MAGNETAR render type) =============================

    /**
     * The magnetar's field-line PAIR count (three pairs at 120° around the magnetic axis — six ovals in total):
     * each pair is two ovals in the same plane, one bulging to each side of the axis — the ()() dipole field shape.
     */
    private static final int MAGNETAR_LOOP_PAIRS = 3;

    /**
     * The loop's vertical radius (along the magnetic axis), × the star radius — the loop tips extend well past the
     * star at the poles, and the steep sides make the lines dive into the core at a near-vertical angle.
     */
    private static final float MAGNETAR_LOOP_POLE = 1.6f;

    /**
     * The loop's center offset from the magnetic axis, × the star radius — each loop of a pair is a distinct oval
     * on one side of the axis (the ()() shape); the inner edge (OFFSET − WIDTH) passes through the core. Keeping
     * OFFSET close to WIDTH puts each line's polar entry on the same side of the axis as its bulge — the six lines
     * fan out in their own azimuth bands instead of crossing above the poles.
     */
    private static final float MAGNETAR_LOOP_OFFSET = 0.9f;

    /**
     * The loop's horizontal radius (in the plane, perpendicular to the axis), × the star radius — the bulge: the
     * outer edge reaches OFFSET + WIDTH past the star's body.
     */
    private static final float MAGNETAR_LOOP_WIDTH = 1.0f;

    /**
     * The ovals' uniform size scales — the "dissipating" field: each successive oval in a plane is larger (tips
     * higher above the poles, bulge wider); the uniform scaling keeps each oval's polar entry in its own azimuth
     * band, so the nested ovals fan out without crossing.
     */
    private static final float[] MAGNETAR_LOOP_SCALES = { 1.0f, 1.5f, 2.0f };

    /**
     * The ovals' alpha multipliers (the field dissipates — the larger ovals fade).
     */
    private static final float[] MAGNETAR_LOOP_ALPHA_SCALES = { 1.0f, 0.7f, 0.5f };

    /**
     * The field-line tube radius, × the star radius (the orbit ring's {@link #RING_TUBE_RADIUS} is absolute —
     * scaled by the star radius the lines stay thin on small stars).
     */
    private static final float MAGNETAR_LOOP_TUBE = 0.04f;

    /**
     * The ovals' tube-radius multipliers (the field thins as it dissipates) — each band's VAO bakes in
     * {@code MAGNETAR_LOOP_TUBE} × this, and the per-draw model scale multiplies it like the oval's size.
     */
    private static final float[] MAGNETAR_LOOP_TUBE_SCALES = { 0.7f, 0.5f, 0.3f };

    /** The loop's oval path / tube cross-section segment counts (the orbit ring uses the same 48×8). */
    private static final int MAGNETAR_LOOP_SEGMENTS = 48;
    private static final int MAGNETAR_LOOP_TUBE_SEGMENTS = 8;

    /**
     * The loops' precession about the magnetic axis (degrees per tick) — the slow cage twist (the star's own
     * surface layers turn at 0.12–0.16°/tick).
     */
    private static final float MAGNETAR_LOOP_PRECESSION = 0.1f;

    /**
     * The field-line tint alpha (the orbit ring uses 0.125 — the field lines need to read clearly against the
     * core).
     */
    private static final float MAGNETAR_LOOP_ALPHA = 0.5f;

    private static final Matrix4f magnetarMatrix = new Matrix4f();

    /**
     * The magnetar's magnetic dipole field loops (the {@code MAGNETAR} render type): thin transparent tubes (the
     * orbit ring's look — the same white texture and tint path) tracing dipole field lines. Each PAIR of loops
     * shares a plane through the star's magnetic axis, one loop on each side of it — the ()() dipole field shape.
     * Each loop is a tall oval offset from the axis: the tips extend well past the star at the poles, the sides
     * dive steeply into the core at the poles, the inner edge passes through the core (hidden by the opaque
     * surface), and the outer edge bulges beyond the star's body. Each oval is nested inside larger siblings in
     * its plane at uniform scales (1×, 1.5×, 2× — fading with size), so the field dissipates outward. The pairs
     * precess about the axis at a shared slow pace, so the cage twists.
     *
     * <p>
     * Drawn after the star body as a pure overlay (the orbit ring's GL treatment): depth-tested — the star's
     * written depth occludes the parts of each loop behind the body, which is what makes the lines read as passing
     * through the core — with depth writes off, blended, and cull and alpha-test disabled.
     *
     * @param base         the star-center model matrix (as in {@link #renderUSSStar})
     * @param partialTicks the smooth animation clock (as in {@link #renderUSSStar})
     * @param starRadius   the star's render radius (as in {@link #renderUSSStar})
     * @param tint         the loops' opaque ARGB tint (the star's core color; 0 → white)
     */
    public static void renderMagnetarFieldLoops(Matrix4fc base, float partialTicks, double starRadius, int tint) {
        if (!shadersReady() || starRadius <= 0.0) return;

        final int argb = tint != 0 ? tint : 0xFFFFFFFF;

        final boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        final boolean alphaTestOn = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        final boolean blendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        final long blendFuncWas = RenderState.savedBlendFunc();
        GL11.glDisable(GL11.GL_CULL_FACE); // the tube's winding is mixed — do not rely on the face convention
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // never inherit a prior renderer's blend
                                                                          // function (the game loop does not reset
                                                                          // GL state between tile-entity renderers)
        GL11.glDepthMask(false); // pure overlay: the star's written depth occludes the far arcs, depth-WRITE off
        GL11.glDisable(GL11.GL_ALPHA_TEST); // the sub-0.5 tint would be discarded by the world pass cutout test
        try {
            final ShaderHandle shader = texturedShader();
            shader.use();
            bindTexture(RING_TEXTURE); // 1×1 white — the tint alone shapes the lines
            final float precession = (MAGNETAR_LOOP_PRECESSION * partialTicks) % 360f;
            for (int pair = 0; pair < MAGNETAR_LOOP_PAIRS; pair++) {
                for (int loop = 0; loop < 2; loop++) {
                    for (int oval = 0; oval < MAGNETAR_LOOP_SCALES.length; oval++) {
                        // The pair's plane azimuth, plus the loop's 180° flip (one bulge per side of the axis — the
                        // ()() shape); the ovals' uniform scales nest them without crossing.
                        magnetarMatrix.set(base)
                            .rotate(
                                (float) Math.toRadians(precession + pair * 360f / MAGNETAR_LOOP_PAIRS + loop * 180f),
                                0f,
                                1f,
                                0f)
                            .scale((float) starRadius * MAGNETAR_LOOP_SCALES[oval]);
                        GL20.glUniform4f(
                            shader.loc(SharedShaders.U_TINT),
                            ((argb >> 16) & 0xFF) / 255f,
                            ((argb >> 8) & 0xFF) / 255f,
                            (argb & 0xFF) / 255f,
                            MAGNETAR_LOOP_ALPHA * MAGNETAR_LOOP_ALPHA_SCALES[oval]);
                        shader.uploadModel(magnetarMatrix);
                        magnetarLoop(oval).render();
                    }
                }
            }
        } finally {
            GL11.glDepthMask(true);
            RenderState.restoreBlendFunc(blendFuncWas);
            RenderState.restore(GL11.GL_BLEND, blendOn);
            RenderState.restore(GL11.GL_ALPHA_TEST, alphaTestOn);
            RenderState.restore(GL11.GL_CULL_FACE, cullOn);
            ShaderProgram.clear();
        }
    }

    /**
     * The field-loop VAOs — one per dissipation band: the unit dipole loop (a tall ellipse in the XY plane,
     * centered OFFSET from the Y axis, the part passing through the core hidden by the opaque surface) with the
     * band's tube radius baked in. Each oval's 180°-rotated twin is the pair's second oval; the VAO is scaled to
     * the star per draw.
     */
    private static final IVertexArrayObject[] MAGNETAR_LOOP_VAOS = new IVertexArrayObject[MAGNETAR_LOOP_SCALES.length];

    private static IVertexArrayObject magnetarLoop(int oval) {
        final IVertexArrayObject vao = MAGNETAR_LOOP_VAOS[oval];
        if (vao != null) {
            return vao;
        }
        // MeshBuilder capacity counts TRIANGLE vertices: 6 per quad (the orbit ring's arithmetic).
        try (MeshBuilder mesh = MeshBuilder
            .of(texturedShader(), MAGNETAR_LOOP_SEGMENTS * MAGNETAR_LOOP_TUBE_SEGMENTS * 6)) {
            final float tube = MAGNETAR_LOOP_TUBE * MAGNETAR_LOOP_TUBE_SCALES[oval];
            for (int i = 0; i < MAGNETAR_LOOP_SEGMENTS; i++) {
                final float t0 = i * 2f * (float) Math.PI / MAGNETAR_LOOP_SEGMENTS;
                final float t1 = (i + 1) * 2f * (float) Math.PI / MAGNETAR_LOOP_SEGMENTS;
                for (int j = 0; j < MAGNETAR_LOOP_TUBE_SEGMENTS; j++) {
                    final float p0 = j * 2f * (float) Math.PI / MAGNETAR_LOOP_TUBE_SEGMENTS;
                    final float p1 = (j + 1) * 2f * (float) Math.PI / MAGNETAR_LOOP_TUBE_SEGMENTS;
                    magnetarLoopVertex(mesh, t0, p0, tube);
                    magnetarLoopVertex(mesh, t1, p0, tube);
                    magnetarLoopVertex(mesh, t1, p1, tube);
                    magnetarLoopVertex(mesh, t0, p1, tube);
                }
            }
            MAGNETAR_LOOP_VAOS[oval] = mesh.build();
        }
        return MAGNETAR_LOOP_VAOS[oval];
    }

    /**
     * One field-loop point: the loop's oval (center OFFSET from the axis, horizontal radius on X, vertical radius
     * on Y, path angle {@code pathAngle}) plus the circular tube offset of radius {@code tube} — the tube's
     * cross-section frame is the path tangent, the binormal tangent × plane normal (0,0,1), and the plane normal
     * itself.
     */
    private static void magnetarLoopVertex(MeshBuilder mesh, float pathAngle, float tubeAngle, float tube) {
        final float cx = MAGNETAR_LOOP_OFFSET + MAGNETAR_LOOP_WIDTH * (float) Math.cos(pathAngle);
        final float cy = MAGNETAR_LOOP_POLE * (float) Math.sin(pathAngle);
        // The oval's tangent (d/dθ of (OFFSET + WIDTH·cosθ, POLE·sinθ, 0)), normalized
        final float tx = -MAGNETAR_LOOP_WIDTH * (float) Math.sin(pathAngle);
        final float ty = MAGNETAR_LOOP_POLE * (float) Math.cos(pathAngle);
        final float tLen = (float) Math.sqrt(tx * tx + ty * ty);
        final float nx = tx / tLen;
        final float ny = ty / tLen;
        // The tube offset tube·(cosφ·normal + sinφ·binormal) with binormal = tangent × (0,0,1) = (ny, −nx, 0)
        final float cosP = (float) Math.cos(tubeAngle);
        final float sinP = (float) Math.sin(tubeAngle);
        mesh.vertex(cx + tube * sinP * ny, cy - tube * sinP * nx, tube * cosP, 0.5, 0.5);
    }

    private static void bindTexture(ResourceLocation location) {
        FMLClientHandler.instance()
            .getClient()
            .getTextureManager()
            .bindTexture(location);
    }

    /**
     * A texture {@link ResourceLocation} for the mod domain. The texture manager looks the location up verbatim, so a
     * stored path without its {@code .png} extension (old tile data) still resolves.
     */
    private static ResourceLocation textureLocation(String path) {
        return new ResourceLocation(MODID, path.endsWith(".png") ? path : path + ".png");
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

    // --- The shared textured planet cube (the cross-layout stitched.png) -----------------------------
    //
    // Every planet texture uses the same cross layout (verified from the images): the middle row, left to right,
    // is back, left, front, right; the top face sits above front (column 2, row 0) and the bottom below it
    // (column 2, row 2). A face cell is 1/4 of the texture width by 1/3 of its height. Because the layout is
    // identical for every planet, ONE cube mesh is shared by all of them — only the bound texture changes per
    // planet, so the cube is baked once and reused.
    private static final int[][] STITCH_FACE_CORNERS = { { 1, 0, 7, 6 }, // left (X = -0.5)
        { 5, 2, 1, 6 }, // bottom (Y = -0.5)
        { 6, 7, 4, 5 }, // front (Z = -0.5)
        { 5, 4, 3, 2 }, // right (X = +0.5)
        { 3, 4, 7, 0 }, // top (Y = +0.5)
        { 2, 3, 0, 1 } // back (Z = +0.5)
    };

    /** The stitched cell (column, row) per face, in the same order as {@link #STITCH_FACE_CORNERS}. */
    private static final int[][] STITCH_FACE_CELL = { { 1, 1 }, // left
        { 2, 2 }, // bottom
        { 2, 1 }, // front
        { 3, 1 }, // right
        { 2, 0 }, // top
        { 0, 1 } // back
    };

    /**
     * Per-face, per-vertex U selection (1 = maxU, 0 = minU) in {@link #STITCH_FACE_CORNERS} order. The UVs
     * place each cell's PHYSICAL image edges on the cube edges they sit on when the cross net is folded, so
     * the texture wraps the cube exactly as it is laid out: the middle row's physical seams (back|left,
     * left|front, front|right, right|back) and the front|top / front|bottom seams are the stitched seams of
     * the image, and the top/bottom cells' remaining edges fold onto the side faces' top/bottom edges.
     */
    private static final int[][] STITCH_FACE_U = { { 0, 0, 1, 1 }, // left
        { 1, 1, 0, 0 }, // bottom
        { 0, 0, 1, 1 }, // front
        { 0, 0, 1, 1 }, // right
        { 1, 1, 0, 0 }, // top
        { 0, 0, 1, 1 } // back
    };

    /** Per-face, per-vertex V selection (1 = maxV, 0 = minV) in {@link #STITCH_FACE_CORNERS} order. */
    private static final int[][] STITCH_FACE_V = { { 1, 0, 0, 1 }, // left
        { 0, 1, 1, 0 }, // bottom
        { 1, 0, 0, 1 }, // front
        { 1, 0, 0, 1 }, // right
        { 0, 1, 1, 0 }, // top
        { 1, 0, 0, 1 } // back
    };

    private static final float[] STITCH_CUBE_X = { -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f };
    private static final float[] STITCH_CUBE_Y = { 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f };
    private static final float[] STITCH_CUBE_Z = { 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f };

    /** Emit the shared planet cube: six faces, each mapped to its stitched cell (see the layout above). */
    private static void emitStitchedCube(QuadSink sink) {
        for (int face = 0; face < 6; face++) {
            final int col = STITCH_FACE_CELL[face][0];
            final int row = STITCH_FACE_CELL[face][1];
            final float minU = col / 4f;
            final float maxU = (col + 1) / 4f;
            final float minV = row / 3f;
            final float maxV = (row + 1) / 3f;
            final int[] corners = STITCH_FACE_CORNERS[face];
            for (int i = 0; i < 4; i++) {
                final float u = STITCH_FACE_U[face][i] == 1 ? maxU : minU;
                final float v = STITCH_FACE_V[face][i] == 1 ? maxV : minV;
                sink.vertex(STITCH_CUBE_X[corners[i]], STITCH_CUBE_Y[corners[i]], STITCH_CUBE_Z[corners[i]], u, v);
            }
        }
    }

    /** The one shared cube VAO (identical cross-layout UVs for every planet); the bound texture varies per planet. */
    private static IVertexArrayObject USS_STITCHED_CUBE;

    private static IVertexArrayObject ussStitchedCube() {
        if (USS_STITCHED_CUBE != null) {
            return USS_STITCHED_CUBE;
        }
        try (MeshBuilder mesh = MeshBuilder.of(texturedShader(), 6 * 6)) {
            emitStitchedCube(mesh);
            USS_STITCHED_CUBE = mesh.build();
        }
        return USS_STITCHED_CUBE;
    }

    // PASS 22: systems now carry up to MAX_PLANETS_PER_SYSTEM (9) planets — the cap must cover every planet,
    // else the last one of a 9-planet system would be invisible.
    private static final int MAX_USS_PLANETS = 9;

    /** Orbit angular speed scale (matches the legacy {@code SPEED_SCALE} so USS planets spin at a similar pace). */
    private static final float USS_ORBIT_SPEED_SCALE = 0.1f;

    // Pass 30 (orbit pace) + pass 37 (10x slower): the orbit angular speed per block of radius lives in the SHARED
    // constant USSFleetOrbit.ORBIT_DEG_PER_TICK_PER_BLOCK (0.03/X deg per tick) — referenced directly above, so the
    // rendered planets, the ships' hover/beam math and the server's destination math all share ONE law and can
    // never drift apart. (The old 0.3/X local copy is gone.)

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

    // region Dyson Swarm shell (the Voidcraft infrastructure pass — the star's satellite swarm)

    /**
     * The shell's radial offset from the star's rendered surface (blocks) — the swarm's cage sits a little OUTSIDE
     * the star's surface so the star stays clearly visible through the shell.
     */
    public static final float DYSON_SHELL_MARGIN = 0.35f;

    /**
     * The nominal triangle edge (blocks) — the triangle SIZE is constant across star sizes (the user spec): a
     * bigger star gets a bigger shell and MORE triangles, not bigger ones. The shell is a triangle TILING, not a
     * quad grid: each latitude row's height is the equilateral-triangle height for this edge ((√3/2) × edge) and
     * each row is staggered half a segment off the one below it, so the panels are equilateral. ~900 triangles at
     * a main-star-sized star (shell radius ~1.5), ~2000 at the largest.
     */
    public static final float DYSON_TRIANGLE_EDGE = 0.12f;

    /**
     * Per-triangle scale toward its own centroid (0..1): the triangles are shrunk toward their centers to leave
     * the gaps between them (the shell reads as separate panels, not a closed skin).
     */
    public static final float DYSON_TRIANGLE_GAP = 0.9f;

    /** The shell's spin speed (radians per world tick) — a slow rotation (one full turn every ~1050 ticks, ~52 s). */
    public static final float DYSON_ROTATION_SPEED = 0.002f;

    /** A fixed tilt (radians) so the shell's poles never face the camera head-on. */
    public static final float DYSON_TILT = 0.35f;

    /** The shell's tint (near-opaque gray — 90% alpha) — packed ARGB, fed to the textured shader's u_Tint. */
    public static final int DYSON_SHELL_TINT = 0xFF3C3C3C;

    /** The accent layer's size relative to the gray panel (the dark blue core inside each panel). */
    public static final float DYSON_ACCENT_SCALE = 0.85f;

    /**
     * The accent layer's outward offset from the gray panel (blocks) — a hair outside the frame: the core stays on
     * the exterior of the shell, and being strictly in front of the gray avoids any z-fighting between the two
     * layers.
     */
    public static final float DYSON_ACCENT_OFFSET = 0.001f;

    /** The accent layer's tint (very dark blue, near-opaque) — packed ARGB, fed to the textured shader's u_Tint. */
    public static final int DYSON_ACCENT_TINT = 0xE6081536;

    /** Shell-VAO cache cap (FIFO eviction, the same policy as the orbit rings). */
    public static final int DYSON_SHELL_CACHE_MAX = 8;

    private static final Matrix4f dysonMatrix = new Matrix4f();

    /** One shell VAO per distinct shell radius (quantized to a cm) — the radius is stable per star. */
    private static final Map<Integer, IVertexArrayObject> DYSON_SHELLS = new LinkedHashMap<>();

    /**
     * The Dyson Swarm shell: a sphere of near-opaque gray triangles around the star, each carrying a slightly
     * smaller dark blue core (the accent layer, {@code DYSON_ACCENT_SCALE} of the panel, a hair outside the panel
     * surface — see {@code DYSON_ACCENT_OFFSET}), drawn with a fraction ({@code count / capacity}) of its panels
     * — a sparse patchwork that fills in as satellites join the swarm. The panels emit in a deterministic MIXED
     * order (a finalizer-scattered hash of the panel triple), so any prefix of them reads as a uniform random
     * patchwork (no sector-by-sector fill).
     *
     * <p>
     * The same textured-shader VAO path the orbit ring uses (the shared program is bound until
     * {@code ShaderProgram.clear()}); the VAO holds the gray half then the accent half, tinted
     * {@code DYSON_SHELL_TINT} / {@code DYSON_ACCENT_TINT} over the 1×1 white texture.
     *
     * @param base     the star-center model matrix (already translated to the TE position)
     * @param time     world time + partial ticks (the shared animation clock)
     * @param starSize the star's render size (blocks)
     * @param count    satellites currently in the swarm (0 → nothing drawn)
     * @param capacity the star's satellite capacity (the full-shell count; &le; 0 → nothing drawn)
     */
    public static void renderUSSDysonSwarm(Matrix4fc base, float time, float starSize, long count, long capacity) {
        if (!shadersReady() || count <= 0L || capacity <= 0L) return;

        FMLClientHandler.instance()
            .getClient()
            .getTextureManager()
            .bindTexture(RING_TEXTURE);

        final float radius = starSize + DYSON_SHELL_MARGIN;
        final IVertexArrayObject shell = dysonShellFor(radius);

        // The fraction of the shell drawn this frame (clamped to the full mesh).
        final int total = dysonTriangleCount(radius);
        int draw = (int) (total * Math.min(1.0, (double) count / (double) capacity));
        if (draw > total) draw = total;
        if (draw < 1) draw = 1;

        final boolean blendWas = GL11.glGetBoolean(GL11.GL_BLEND);
        final long blendFuncWas = RenderState.savedBlendFunc();
        final boolean cullWas = GL11.glGetBoolean(GL11.GL_CULL_FACE);
        final boolean alphaTestWas = GL11.glGetBoolean(GL11.GL_ALPHA_TEST);

        try {
            if (cullWas) GL11.glDisable(GL11.GL_CULL_FACE); // double-sided panels
            if (alphaTestWas) GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(false);

            // tilt (fixed) then spin (the world clock) — the shell keeps a readable face while turning.
            dysonMatrix.set(base)
                .rotate(DYSON_TILT, 1f, 0f, 0f)
                .rotate((float) (time * DYSON_ROTATION_SPEED), 0f, 1f, 0f);

            final ShaderHandle shader = texturedShader();
            shader.use();
            shader.uploadModel(dysonMatrix);

            // The gray panels, then the accent cores (the VAO's second half, the same mixed prefix).
            dysonTint(shader, DYSON_SHELL_TINT);
            shell.render(0, draw * 3);
            dysonTint(shader, DYSON_ACCENT_TINT);
            shell.render(total * 3, draw * 3);
        } finally {
            ShaderProgram.clear();
            RenderState.restore(GL11.GL_BLEND, blendWas);
            RenderState.restoreBlendFunc(blendFuncWas);
            RenderState.restore(GL11.GL_ALPHA_TEST, alphaTestWas);
            RenderState.restore(GL11.GL_CULL_FACE, cullWas);
        }
    }

    /** Unpacks a packed-ARGB tint into the textured shader's u_Tint (the shell's two layers). */
    private static void dysonTint(ShaderHandle shader, int tint) {
        GL20.glUniform4f(
            shader.loc(SharedShaders.U_TINT),
            ((tint >> 16) & 0xFF) / 255f,
            ((tint >> 8) & 0xFF) / 255f,
            (tint & 0xFF) / 255f,
            ((tint >> 24) & 0xFF) / 255f);
    }

    /**
     * The shell's latitude rows: the pole-to-pole span split so each row's arc height equals the equilateral
     * triangle's height for {@link #DYSON_TRIANGLE_EDGE} — (√3/2) × edge — so the panels keep their shape from
     * equator to pole.
     */
    private static int dysonRowCount(float radius) {
        final float rowHeight = (float) Math.sqrt(3) * 0.5f * DYSON_TRIANGLE_EDGE;
        return Math.max(2, (int) Math.round(Math.PI * radius / rowHeight));
    }

    /**
     * A row's longitude segments: its mid-latitude circumference over the nominal edge, minimum 6 (the pole rows
     * close the shell in a six-panel cap).
     */
    private static int dysonRowSegments(float radius, int row) {
        final float mid = -((float) Math.PI / 2f) + (float) Math.PI * (row + 0.5f) / dysonRowCount(radius);
        final int segments = (int) Math
            .round(2f * (float) Math.PI * radius * (float) Math.cos(mid) / DYSON_TRIANGLE_EDGE);
        return Math.max(6, segments);
    }

    /**
     * A row's emitted triangles: an interior row emits two per segment (the up and down panels); a pole row
     * emits one — its degenerate twin collapses to the pole vertex and is dropped.
     */
    private static int dysonRowTriangles(int segs, int row, int rows) {
        if (rows > 2 && row > 0 && row < rows - 1) {
            return 2 * segs;
        }
        return segs;
    }

    /** The shell's full triangle count for a radius (the rows' emitted triangles — the mesh's total). */
    private static int dysonTriangleCount(float radius) {
        final int rows = dysonRowCount(radius);
        int total = 0;
        for (int row = 0; row < rows; row++) {
            total += dysonRowTriangles(dysonRowSegments(radius, row), row, rows);
        }
        return total;
    }

    /** One shell VAO per distinct shell radius (baked triangles) — cached like {@link #ussRingFor}. */
    private static IVertexArrayObject dysonShellFor(float radius) {
        final Integer key = Math.round(radius * 100f);
        IVertexArrayObject cached = DYSON_SHELLS.get(key);
        if (cached != null) {
            return cached;
        }
        final int rows = dysonRowCount(radius);
        final int[] segs = new int[rows];
        final float[] offsets = new float[rows];
        int total = 0;
        for (int row = 0; row < rows; row++) {
            segs[row] = dysonRowSegments(radius, row);
            total += dysonRowTriangles(segs[row], row, rows);
            if (row > 0) {
                // Half the PREVIOUS row's segment off its bottom edge — the stagger that makes the lattice a
                // triangle tiling (each row's top edge sits between the row below's segments).
                offsets[row] = offsets[row - 1] + (float) Math.PI / segs[row - 1];
            }
        }
        // The (row, column, direction) triples in DETERMINISTIC INTERLEAVED order: a hash of the triple,
        // sorted — any prefix of the mesh then samples the whole shell evenly.
        final int[] eRow = new int[total];
        final int[] eCol = new int[total];
        final int[] eDir = new int[total];
        int[] hashes = new int[total];
        for (int i = 0; i < total; i++) {
            int rest = i;
            int row = 0;
            while (rest >= dysonRowTriangles(segs[row], row, rows)) {
                rest -= dysonRowTriangles(segs[row], row, rows);
                row++;
            }
            eRow[i] = row;
            if (rows > 2 && row > 0 && row < rows - 1) {
                eCol[i] = rest / 2;
                eDir[i] = rest % 2;
            } else {
                eCol[i] = rest;
                eDir[i] = row == rows - 1 ? 0 : 1;
            }
            hashes[i] = dysonHash(eRow[i], eCol[i], eDir[i]);
        }
        final Integer[] byHash = new Integer[total];
        for (int i = 0; i < total; i++) {
            byHash[i] = i;
        }
        java.util.Arrays.sort(byHash, (a, b) -> Integer.compare(hashes[a], hashes[b]));

        final IVertexArrayObject built;
        // The capacity is in VERTICES (one per emitted corner) — each panel emits three for the gray layer and
        // three for the accent.
        try (MeshBuilder mesh = MeshBuilder.of(texturedShader(), total * 6)) {
            for (int i = 0; i < total; i++) {
                final int idx = byHash[i];
                dysonTriangle(mesh, radius, eRow[idx], eCol[idx], eDir[idx], offsets[eRow[idx]], false);
            }
            for (int i = 0; i < total; i++) {
                final int idx = byHash[i];
                dysonTriangle(mesh, radius, eRow[idx], eCol[idx], eDir[idx], offsets[eRow[idx]], true);
            }
            built = mesh.build();
        }
        if (DYSON_SHELLS.size() >= DYSON_SHELL_CACHE_MAX) {
            final Iterator<Integer> oldest = DYSON_SHELLS.keySet()
                .iterator();
            if (oldest.hasNext()) {
                DYSON_SHELLS.get(oldest.next())
                    .delete();
                oldest.remove();
            }
        }
        DYSON_SHELLS.put(key, built);
        return built;
    }

    /**
     * A well-mixed 32-bit hash of a panel triple: the raw XOR keeps the triple's low-bit structure, so sorted
     * prefixes would fill the shell sector by sector — the finalizer scatters it into a uniform patchwork.
     */
    private static int dysonHash(int row, int col, int dir) {
        int h = (row * 73856093) ^ (col * 19349663) ^ (dir * 83492791);
        h ^= h >>> 16;
        h *= 0x7feb352d;
        h ^= h >>> 15;
        h *= 0x846ca68b;
        h ^= h >>> 16;
        return h;
    }

    /**
     * One shell panel: row {@code row} × segment {@code segment}, direction {@code direction} (0 = the up panel —
     * apex on the row's top edge, 1 = the down panel — apex on its bottom edge), each corner pulled toward the
     * panel's OWN centroid — by {@link #DYSON_TRIANGLE_GAP} for the gray layer, by
     * {@code GAP × DYSON_ACCENT_SCALE} for the accent (the dark blue core, emitted on the sphere pushed out by
     * {@link #DYSON_ACCENT_OFFSET}). The top edge is staggered half a segment off the bottom edge (see
     * {@code offsets} in {@link #dysonShellFor}), so the panels are equilateral.
     */
    private static void dysonTriangle(MeshBuilder mesh, float radius, int row, int segment, int direction,
        float bottomOffset, boolean accent) {
        final float layerRadius = accent ? radius + DYSON_ACCENT_OFFSET : radius;
        final float scale = accent ? DYSON_TRIANGLE_GAP * DYSON_ACCENT_SCALE : DYSON_TRIANGLE_GAP;
        final float rowStep = (float) Math.PI / dysonRowCount(radius);
        final float th0 = -((float) Math.PI / 2f) + row * rowStep;
        final float th1 = th0 + rowStep;
        final int segs = dysonRowSegments(radius, row);
        final float segStep = 2f * (float) Math.PI / segs;
        final float ph0 = bottomOffset + segment * segStep;
        final float ph1 = ph0 + segStep;
        final float tph0 = bottomOffset + segStep * 0.5f + segment * segStep;
        final float tph1 = tph0 + segStep;

        final float[] b0 = dysonPoint(layerRadius, th0, ph0);
        final float[] b1 = dysonPoint(layerRadius, th0, ph1);
        final float[] t0 = dysonPoint(layerRadius, th1, tph0);
        final float[] t1 = dysonPoint(layerRadius, th1, tph1);

        final float[][] corners = direction == 0 ? new float[][] { b0, b1, t0 } : new float[][] { t0, t1, b1 };

        final float cx = (corners[0][0] + corners[1][0] + corners[2][0]) / 3f;
        final float cy = (corners[0][1] + corners[1][1] + corners[2][1]) / 3f;
        final float cz = (corners[0][2] + corners[1][2] + corners[2][2]) / 3f;

        for (int v = 0; v < 3; v++) {
            final float[] c = corners[v];
            mesh.triangleVertex(
                cx + (c[0] - cx) * scale,
                cy + (c[1] - cy) * scale,
                cz + (c[2] - cz) * scale,
                0.5f,
                0.5f);
        }
    }

    /** A point on the shell sphere: latitude {@code theta} (−π/2..π/2), longitude {@code phi}. */
    private static float[] dysonPoint(float radius, float theta, float phi) {
        final float ct = (float) Math.cos(theta);
        return new float[] { radius * ct * (float) Math.cos(phi), radius * (float) Math.sin(theta),
            radius * ct * (float) Math.sin(phi) };
    }

    // endregion

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
