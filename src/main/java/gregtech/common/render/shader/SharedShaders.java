package gregtech.common.render.shader;

import gregtech.api.enums.Mods;

public final class SharedShaders {

    private static final ShaderRecipe TEXTURED = ShaderRecipe.of(Mods.GregTech.resourceDomain, "textured")
        .required("u_Tint")
        .sampler("u_Texture", 0)
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION)
        .attribute("a_UV", VertexAttribute.UV);

    public static final Uniform U_TINT = TEXTURED.uniform("u_Tint");

    private static ShaderHandle textured;

    private SharedShaders() {}

    public static void reload() {
        release();
        textured = TEXTURED.bake();
    }

    public static ShaderHandle textured() {
        return textured;
    }

    public static boolean ready() {
        return textured != null && textured.isValid();
    }

    public static void release() {
        if (textured != null) {
            textured.release();
            textured = null;
        }
    }
}
