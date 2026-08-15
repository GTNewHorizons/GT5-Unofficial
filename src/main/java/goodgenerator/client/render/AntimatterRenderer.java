package goodgenerator.client.render;

import static gregtech.api.enums.Mods.GoodGenerator;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.model.wavefront.WavefrontVBOBuilder;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import cpw.mods.fml.client.registry.ClientRegistry;
import goodgenerator.blocks.tileEntity.render.TileAntimatter;
import gregtech.GTLoggers;
import gregtech.common.render.shader.RenderState;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.ShaderRecipe;
import gregtech.common.render.shader.Uniform;
import gregtech.common.render.shader.VertexAttribute;

public class AntimatterRenderer extends TileEntitySpecialRenderer {

    private static final Matrix4f modelMatrix = new Matrix4f();

    // Antimatter 'Blob'
    private static final ShaderRecipe ANTIMATTER = ShaderRecipe.of(GoodGenerator.resourceDomain, "antimatter")
        .required("u_Scale", "u_ScaleSnapshot", "u_Time", "u_TimeSnapshot")
        .constant("u_ColorCore", TileAntimatter.coreR, TileAntimatter.coreG, TileAntimatter.coreB)
        .constant("u_ColorSpike", TileAntimatter.spikeR, TileAntimatter.spikeG, TileAntimatter.spikeB)
        .constant("u_Opacity", 1f)
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION);

    private static final Uniform AM_SCALE = ANTIMATTER.uniform("u_Scale");
    private static final Uniform AM_SCALE_SNAPSHOT = ANTIMATTER.uniform("u_ScaleSnapshot");
    private static final Uniform AM_TIME = ANTIMATTER.uniform("u_Time");
    private static final Uniform AM_TIME_SNAPSHOT = ANTIMATTER.uniform("u_TimeSnapshot");

    private static ShaderHandle antimatterShader;

    private static final ShaderRecipe SOLID = ShaderRecipe.of(GoodGenerator.resourceDomain, "solid")
        .required("u_Color")
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION);

    private static final Uniform SOLID_COLOR = SOLID.uniform("u_Color");

    private static ShaderHandle solidShader;

    private static IVertexArrayObject antimatterModel;
    private static IVertexArrayObject containerModel;
    private static IVertexArrayObject ringModel;
    private static final float modelNormalize = .5f;

    // Protomatter Beam
    private static final int particleCount = 32;
    private static final int beamVertexCount = particleCount * 6 * 6;

    private static final float[] promomatterVerticies = {
        // Front Face
        -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
        // Back Face
        -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f,
        // Left face
        -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f,
        // Right face
        0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f,
        // Top face
        -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f,
        // Bottom face
        -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, };

    private static final ShaderRecipe PROTOMATTER = ShaderRecipe.of(GoodGenerator.resourceDomain, "protomatter")
        .required("u_Time", "u_Scale", "u_Color", "u_SpiralRadius")
        .constantArray("u_Vertices", 3, promomatterVerticies)
        .constant("u_CubeCount", particleCount)
        .modelUniform("u_ModelMatrix")
        .attributeless("a_VertexID", beamVertexCount);

    private static final Uniform PM_TIME = PROTOMATTER.uniform("u_Time");
    private static final Uniform PM_SCALE = PROTOMATTER.uniform("u_Scale");
    private static final Uniform PM_COLOR = PROTOMATTER.uniform("u_Color");
    private static final Uniform PM_SPIRAL_RADIUS = PROTOMATTER.uniform("u_SpiralRadius");

    private static ShaderHandle protomatterShader;

    private static boolean initialized = false;
    private static boolean hasFailed = false;

    public AntimatterRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntimatter.class, this);
    }

    private static void loadModels() {
        antimatterModel = WavefrontVBOBuilder.compileToVBO(
            new ResourceLocation(GoodGenerator.resourceDomain, "models/Antimatter.obj"),
            antimatterShader.vertexFormat());

        containerModel = WavefrontVBOBuilder.compileToVBO(
            new ResourceLocation(GoodGenerator.resourceDomain, "models/SmoothSphere.obj"),
            solidShader.vertexFormat());

        ringModel = WavefrontVBOBuilder.compileToVBO(
            new ResourceLocation(GoodGenerator.resourceDomain, "models/GlowRing.obj"),
            solidShader.vertexFormat());
    }

    public static void reload() {
        initialized = false;
        hasFailed = false;
        release();
        antimatterShader = ANTIMATTER.bake();
        if (!antimatterShader.isValid()) {
            fail("antimatter", null);
            return;
        }

        solidShader = SOLID.bake();
        if (!solidShader.isValid()) {
            fail("solid", null);
            return;
        }

        try {
            loadModels();
        } catch (RuntimeException e) {
            fail("antimatter models", e);
            return;
        }

        protomatterShader = PROTOMATTER.bake();
        if (!protomatterShader.isValid()) {
            fail("protomatter", null);
            return;
        }

        initialized = true;
    }

    private static void fail(String stage, Throwable t) {
        GTLoggers.GT_FML_LOGGER.error("Failed to initialize antimatter forge render ({})", stage, t);
        release();
        hasFailed = true;
    }

    private static void release() {
        if (antimatterShader != null) {
            antimatterShader.release();
            antimatterShader = null;
        }
        if (protomatterShader != null) {
            protomatterShader.release();
            protomatterShader = null;
        }
        if (solidShader != null) {
            solidShader.release();
            solidShader = null;
        }
        antimatterModel = deleteModel(antimatterModel);
        containerModel = deleteModel(containerModel);
        ringModel = deleteModel(ringModel);
    }

    private static IVertexArrayObject deleteModel(IVertexArrayObject model) {
        if (model != null) model.delete();
        return null;
    }

    private void renderAntimatter(TileAntimatter tile, double x, double y, double z, float timer) {
        antimatterShader.use();

        float angle = ((timer) % (20 * 60 * 60)) * tile.rotationSpeedMultiplier;

        modelMatrix.translation((float) x, (float) y, (float) z)
            .rotateY(angle / 180f * (float) Math.PI)
            .rotateX(angle / 8f / 180f * (float) Math.PI);

        float snapshotSize = tile.coreScaleSnapshot;
        snapshotSize *= modelNormalize;
        float coreSizeSnapshot = Math.min(snapshotSize, TileAntimatter.maximalRadius / (1 + tile.spikeFactor));
        float targetSize = tile.coreScale;
        targetSize *= modelNormalize;
        float coreSize = Math.min(targetSize, TileAntimatter.maximalRadius / (1 + tile.spikeFactor));

        float realTime = timer / 20;
        float snapTime = tile.timeSnapshot / 20;

        GL20.glUniform1f(antimatterShader.loc(AM_TIME), realTime);
        GL20.glUniform1f(antimatterShader.loc(AM_TIME_SNAPSHOT), snapTime);
        GL20.glUniform1f(antimatterShader.loc(AM_SCALE), coreSize);
        GL20.glUniform1f(antimatterShader.loc(AM_SCALE_SNAPSHOT), coreSizeSnapshot);
        final boolean cullWas = GL11.glGetBoolean(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_CULL_FACE);

        antimatterShader.uploadModel(modelMatrix);
        antimatterModel.render();

        GL11.glEnable(GL11.GL_CULL_FACE);
        modelMatrix.translation((float) x, (float) y, (float) z)
            .scale(-TileAntimatter.maximalRadius);
        solidShader.use();
        GL20.glUniform3f(solidShader.loc(SOLID_COLOR), 0, 0, 0);
        solidShader.uploadModel(modelMatrix);
        containerModel.render();
        ShaderProgram.clear();

        RenderState.restore(GL11.GL_CULL_FACE, cullWas);
    }

    private void renderProtomatterBeam(TileAntimatter tile, double x, double y, double z, float timer) {
        protomatterShader.use();

        GL20.glUniform1f(protomatterShader.loc(PM_TIME), timer);
        GL20.glUniform1f(protomatterShader.loc(PM_SCALE), tile.protomatterScale);
        GL20.glUniform3f(
            protomatterShader.loc(PM_COLOR),
            TileAntimatter.protoR,
            TileAntimatter.protoG,
            TileAntimatter.protoB);
        GL20.glUniform1f(protomatterShader.loc(PM_SPIRAL_RADIUS), tile.getSpiralRadius(modelNormalize));

        tileRotation(tile, x, y, z);
        protomatterShader.uploadModel(modelMatrix);
        protomatterShader.draw();

        ShaderProgram.clear();
    }

    public void renderRing(TileAntimatter tile, double x, double y, double z, float timer) {
        tileRotation(tile, x, y, z);

        solidShader.use();
        GL20.glUniform3f(solidShader.loc(SOLID_COLOR), 0, 1f, 1f);
        solidShader.uploadModel(modelMatrix);
        ringModel.render();
        ShaderProgram.clear();
    }

    private void tileRotation(TileAntimatter tile, double x, double y, double z) {
        modelMatrix.translation((float) x, (float) y, (float) z);
        if (tile.rotationAngle != 0) {
            modelMatrix.rotate(tile.rotationAngle / 180f * (float) Math.PI, tile.rotX, tile.rotY, tile.rotZ);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        if (!(tile instanceof TileAntimatter Antimatter)) return;

        if (!Antimatter.shouldRender) return;

        if (hasFailed || !initialized) return;

        float tx = (float) x + 0.5f;
        float ty = (float) y + 0.5f;
        float tz = (float) z + 0.5f;

        float timer = tile.getWorldObj()
            .getWorldInfo()
            .getWorldTotalTime() + timeSinceLastTick;
        renderAntimatter(Antimatter, tx, ty, tz, timer);

        if (!Antimatter.protomatterRender) return;
        renderProtomatterBeam(Antimatter, tx, ty, tz, timer);

        renderRing(Antimatter, tx, ty, tz, timer);
    }
}
