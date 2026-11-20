package gregtech.common.tileentities.machines.multi.foundry;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.util.ResourceLocation;

import org.joml.Matrix4f;

import com.gtnewhorizon.gtnhlib.client.model.wavefront.WavefrontVBOBuilder;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import gregtech.GTLoggers;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.ShaderRecipe;
import gregtech.common.render.shader.Uniform;
import gregtech.common.render.shader.VertexAttribute;

public class FoundryRenderUtils {

    public static boolean renderInitialized;
    public static IVertexArrayObject ffpRing; // The universium path draws via FFP
    public static IVertexArrayObject ring;

    public static final ShaderRecipe FOUNDRY = ShaderRecipe.of(GregTech.resourceDomain, "foundry")
        .required("u_Color")
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION);

    public static final Uniform RING_COLOR = FOUNDRY.uniform("u_Color");

    public static ShaderHandle ringShader;

    public static final Matrix4f ringMatrix = new Matrix4f();

    public static void reloadRender() {
        renderInitialized = false;
        releaseRender();
        // spotless:off
        ringShader = FOUNDRY.bake();
        if (!ringShader.isValid()) {
            GTLoggers.GT_FML_LOGGER.error("Failed to initialize exo foundry shader");
            releaseRender();
            return;
        }

        try {
            final ResourceLocation model = new ResourceLocation(
                GregTech.resourceDomain,
                "textures/model/foundry_ring.obj"
            );
            ring = WavefrontVBOBuilder.compileToVBO(model, ringShader.vertexFormat());
            ffpRing = WavefrontVBOBuilder.compileToVBO(model);
        } catch (RuntimeException e) {
            GTLoggers.GT_FML_LOGGER.error("Failed to load exo foundry ring model", e);
            releaseRender();
            return;
        }

        renderInitialized = true;
        // spotless:on
    }

    public static void releaseRender() {
        if (ringShader != null) {
            ringShader.release();
            ringShader = null;
        }
        if (ring != null) {
            ring.delete();
            ring = null;
        }
        if (ffpRing != null) {
            ffpRing.delete();
            ffpRing = null;
        }
    }
}
