package tectech.voidcraft.render;

import gregtech.GTLoggers;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.ShaderRecipe;
import gregtech.common.render.shader.Uniform;
import gregtech.common.render.shader.VertexAttribute;
import tectech.Reference;

/**
 * The voidcraft effect shaders (the 3.3 core-profile-first convention with the 2.1 fallback):
 *
 * <ul>
 * <li>{@code voidcraft_color} — a position-only, flat-color mesh, drawn through a per-draw model matrix: the scan
 * cube, the Voidbase site wireframes and fill, the assembler wireframes and scan planes, and the gateway
 * plane and tube.</li>
 * <li>{@code voidcraft_beam} — the mining / construction laser rod, positioned entirely in the shader (endpoints +
 * cross-section axes + half-width as uniforms) so one static VAO serves every beam.</li>
 * <li>{@code voidcraft_ripple} — the Explorer's camera-facing triangle billboards (center + camera axes + scale
 * as uniforms).</li>
 *
 * <p>
 * Re-baked by the resource-reload hook ({@link #reload()}) after the shared shaders have been re-baked; a re-bake
 * may change attribute locations, so every captured / cached VAO built against these (or the shared textured)
 * formats is discarded and rebuilt lazily on the next draw.
 */
public final class VoidcraftShaders {

    private static final ShaderRecipe COLOR = ShaderRecipe.of(Reference.MODID, "voidcraft_color")
        .required("u_Color")
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION);

    private static final ShaderRecipe BEAM = ShaderRecipe.of(Reference.MODID, "voidcraft_beam")
        .required("u_Start", "u_End", "u_P1", "u_P2", "u_HalfWidth", "u_Color")
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION);

    private static final ShaderRecipe RIPPLE = ShaderRecipe.of(Reference.MODID, "voidcraft_ripple")
        .required("u_Center", "u_Right", "u_Up", "u_Scale", "u_Color")
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION);

    public static final Uniform COLOR_COLOR = COLOR.uniform("u_Color");

    public static final Uniform BEAM_START = BEAM.uniform("u_Start");
    public static final Uniform BEAM_END = BEAM.uniform("u_End");
    public static final Uniform BEAM_P1 = BEAM.uniform("u_P1");
    public static final Uniform BEAM_P2 = BEAM.uniform("u_P2");
    public static final Uniform BEAM_HALF_WIDTH = BEAM.uniform("u_HalfWidth");
    public static final Uniform BEAM_COLOR = BEAM.uniform("u_Color");

    public static final Uniform RIPPLE_CENTER = RIPPLE.uniform("u_Center");
    public static final Uniform RIPPLE_RIGHT = RIPPLE.uniform("u_Right");
    public static final Uniform RIPPLE_UP = RIPPLE.uniform("u_Up");
    public static final Uniform RIPPLE_SCALE = RIPPLE.uniform("u_Scale");
    public static final Uniform RIPPLE_COLOR = RIPPLE.uniform("u_Color");

    private static ShaderHandle color;
    private static ShaderHandle beam;
    private static ShaderHandle ripple;

    private VoidcraftShaders() {}

    /** Re-bakes all voidcraft shaders and discards every VAO captured against the (possibly relocated) formats. */
    public static void reload() {
        release();
        color = COLOR.bake();
        beam = BEAM.bake();
        ripple = RIPPLE.bake();
        if (!ready()) {
            GTLoggers.GT_FML_LOGGER
                .error("Voidcraft render shaders failed to load; the voidcraft effects will not render");
        }
        VoidcraftGeometry.release();
        RenderVoidcraftShip.releaseGeometry();
        RenderVoidcraftAssembler.releaseGeometry();
        VoidcraftShipModelCache.clear();
    }

    public static boolean ready() {
        return color != null && color.isValid() && beam != null && beam.isValid() && ripple != null && ripple.isValid();
    }

    public static void release() {
        release(color);
        color = null;
        release(beam);
        beam = null;
        release(ripple);
        ripple = null;
    }

    private static void release(ShaderHandle handle) {
        if (handle != null) {
            handle.release();
        }
    }

    public static ShaderHandle color() {
        return color;
    }

    public static ShaderHandle beam() {
        return beam;
    }

    public static ShaderHandle ripple() {
        return ripple;
    }
}
