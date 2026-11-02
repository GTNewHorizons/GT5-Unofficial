package gregtech.common.render;

import static gregtech.api.enums.Mods.GregTech;
import static java.lang.Math.sin;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.model.wavefront.WavefrontVBOBuilder;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormat;

import gregtech.GTLoggers;
import gregtech.common.render.shader.RenderState;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.SharedShaders;
import gregtech.common.tileentities.render.RenderingTileEntityNanoForge;

public class NanoForgeRenderer extends TileEntitySpecialRenderer {

    private static boolean initialized;
    private static ResourceLocation coreTexture;
    private static ResourceLocation shieldTexture;
    private static ResourceLocation ringTexture;
    private static IVertexArrayObject nanoforgeCoreModel;
    private static IVertexArrayObject nanoforgeShieldModel;
    private static IVertexArrayObject nanoforgeRingOneModel;
    private static IVertexArrayObject nanoforgeRingTwoModel;
    private static IVertexArrayObject nanoforgeRingThreeModel;

    private static final float WARM_UP_TIME = 25.0f;
    private static final float FULL_CHAOS_TIME = 75.0f;
    private static final float SPEED_MULTIPLIER = 10.0f;
    private static final float CHAOS_SPEED_MULTIPLIER = 90.0f;
    private static final float SINUS_DIVIDER = 50.0f;
    private static final float MAX_CHAOS_SPEED_UP = 2.0f;
    private static final float RING_ROTATION_NORMAL = 1.0f;

    private static final Matrix4fStack modelMatrix = new Matrix4fStack(2);

    public static void reload() {
        initialized = false;
        deleteModels();
        if (!SharedShaders.ready()) return;
        final VertexFormat format = SharedShaders.textured()
            .vertexFormat();
        // spotless:off
        try {
            coreTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/Core.png");
            shieldTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/Shield.png");
            ringTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/RING.png");
            nanoforgeCoreModel = WavefrontVBOBuilder.compileToVBO(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-core.obj"), format);
            nanoforgeShieldModel = WavefrontVBOBuilder.compileToVBO(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-shield.obj"), format);
            nanoforgeRingOneModel = WavefrontVBOBuilder.compileToVBO(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-ring-one.obj"), format);
            nanoforgeRingTwoModel = WavefrontVBOBuilder.compileToVBO(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-ring-two.obj"), format);
            nanoforgeRingThreeModel = WavefrontVBOBuilder.compileToVBO(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-ring-three.obj"), format);
        } catch (RuntimeException e) {
            GTLoggers.GT_FML_LOGGER.error("Failed to load nano forge models", e);
            deleteModels();
            return;
        }
        // spotless:on
        initialized = true;
    }

    private static void deleteModels() {
        for (IVertexArrayObject model : new IVertexArrayObject[] { nanoforgeCoreModel, nanoforgeShieldModel,
            nanoforgeRingOneModel, nanoforgeRingTwoModel, nanoforgeRingThreeModel }) {
            if (model != null) model.delete();
        }
        nanoforgeCoreModel = null;
        nanoforgeShieldModel = null;
        nanoforgeRingOneModel = null;
        nanoforgeRingTwoModel = null;
        nanoforgeRingThreeModel = null;
    }

    private void renderNanoForge(RenderingTileEntityNanoForge tile, double x, double y, double z, float deltaT) {
        float timer = tile.getTimer();
        if (!tile.getRunning()) {
            timer -= deltaT;
        } else {
            timer += deltaT;
        }

        if (timer < 0) {
            timer = 0;
        }

        tile.setTimer(timer);

        final ShaderHandle shader = SharedShaders.textured();
        shader.use();

        bindTexture(coreTexture);
        renderCore(shader, x, y, z, timer, tile.getRed(), tile.getGreen(), tile.getBlue());
        bindTexture(ringTexture);
        GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1f, 1f, 1f, 1f);
        renderRingOne(shader, x, y, z, timer);
        renderRingTwo(shader, x, y, z, timer);
        renderRingThree(shader, x, y, z, timer);
        bindTexture(shieldTexture);
        renderShield(shader, x, y, z, timer);

        ShaderProgram.clear();
    }

    private void renderCore(ShaderHandle shader, double x, double y, double z, float timer, float r, float g, float b) {
        float chaos = Math.min(Math.max((timer - WARM_UP_TIME), 0) / FULL_CHAOS_TIME, MAX_CHAOS_SPEED_UP);
        float chaosScale = Math.min(Math.max(chaos, 0.05f), 1);

        GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), r, g, b, 1f);
        modelMatrix.clear();
        modelMatrix.translate((float) x + .5f, (float) y + .5f, (float) z + .5f);
        modelMatrix.scale(chaosScale);
        rotate(
            timer * SPEED_MULTIPLIER + timer * CHAOS_SPEED_MULTIPLIER * chaos,
            (float) (0.3 * sin(timer / SINUS_DIVIDER) + sin(timer / SINUS_DIVIDER * 0.5)
                + 0.5 * sin(timer / SINUS_DIVIDER * 3)),
            (float) (1 * sin(timer / SINUS_DIVIDER * 0.3) + 3 * sin(timer / SINUS_DIVIDER)
                + 0.3 * sin(timer / SINUS_DIVIDER * 3)),
            (float) (2 * sin(timer / SINUS_DIVIDER * 0.4) + sin(timer / SINUS_DIVIDER * 1.5)
                + 1.2 * sin(timer / SINUS_DIVIDER * 1)));

        shader.uploadModel(modelMatrix);
        nanoforgeCoreModel.render();
    }

    private void renderRingOne(ShaderHandle shader, double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - WARM_UP_TIME), 0) / FULL_CHAOS_TIME, MAX_CHAOS_SPEED_UP);

        modelMatrix.clear();
        modelMatrix.translate((float) x + .5f, (float) y + .5f, (float) z + .5f);
        rotate(
            timer * SPEED_MULTIPLIER + timer * CHAOS_SPEED_MULTIPLIER * chaos,
            0f,
            0.5f + RING_ROTATION_NORMAL * chaos,
            0f);
        rotate(timer * CHAOS_SPEED_MULTIPLIER * chaos, applyRotationMajor(timer), 0f, 0f);
        rotate(timer * CHAOS_SPEED_MULTIPLIER * chaos, 0f, 0f, applyRotationMinor(timer));
        shader.uploadModel(modelMatrix);
        nanoforgeRingOneModel.render();
    }

    private void renderRingTwo(ShaderHandle shader, double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - WARM_UP_TIME), 0) / FULL_CHAOS_TIME, MAX_CHAOS_SPEED_UP);

        modelMatrix.clear();
        modelMatrix.translate((float) x + .5f, (float) y + .5f, (float) z + .5f);
        rotate(
            timer * SPEED_MULTIPLIER + timer * CHAOS_SPEED_MULTIPLIER * chaos,
            0.5f + RING_ROTATION_NORMAL * chaos,
            0f,
            0f);
        rotate(timer * CHAOS_SPEED_MULTIPLIER * chaos, 0f, 0f, applyRotationMajor(timer));
        rotate(timer * CHAOS_SPEED_MULTIPLIER * chaos, 0f, applyRotationMinor(timer), 0f);
        shader.uploadModel(modelMatrix);
        nanoforgeRingTwoModel.render();
    }

    private void renderRingThree(ShaderHandle shader, double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - WARM_UP_TIME), 0) / FULL_CHAOS_TIME, MAX_CHAOS_SPEED_UP);

        modelMatrix.clear();
        modelMatrix.translate((float) x + .5f, (float) y + .5f, (float) z + .5f);
        rotate(
            timer * SPEED_MULTIPLIER + timer * CHAOS_SPEED_MULTIPLIER * chaos,
            0f,
            0f,
            0.5f + RING_ROTATION_NORMAL * chaos);
        rotate(timer * CHAOS_SPEED_MULTIPLIER * chaos, 0f, applyRotationMajor(timer), 0f);
        rotate(timer * CHAOS_SPEED_MULTIPLIER * chaos, applyRotationMinor(timer), 0f, 0f);
        shader.uploadModel(modelMatrix);
        nanoforgeRingThreeModel.render();
    }

    private void renderShield(ShaderHandle shader, double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - WARM_UP_TIME), 0) / FULL_CHAOS_TIME, MAX_CHAOS_SPEED_UP);
        final boolean blendWas = GL11.glGetBoolean(GL11.GL_BLEND);
        final boolean depthTestWas = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        final boolean depthMaskWas = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        final long blendFuncWas = RenderState.savedBlendFunc();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false); // Disable depth writing for transparency

        GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1f, 1f, 1f, 0.4f);
        modelMatrix.clear();
        modelMatrix.translate((float) x + .5f, (float) y + .5f, (float) z + .5f);
        rotate(
            timer * SPEED_MULTIPLIER + timer * CHAOS_SPEED_MULTIPLIER * chaos,
            (float) (2 * sin(timer / SINUS_DIVIDER) + sin(timer / SINUS_DIVIDER * 0.5)
                + 0.5 * sin(timer / SINUS_DIVIDER * 3)),
            (float) (1 * sin(timer / SINUS_DIVIDER * 0.3) + 3 * sin(timer / SINUS_DIVIDER)
                + 0.3 * sin(timer / SINUS_DIVIDER * 3)),
            (float) (0.5 * sin(timer / SINUS_DIVIDER * 0.4) + sin(timer / SINUS_DIVIDER * 1.5)
                + 1.2 * sin(timer / SINUS_DIVIDER * 1)));

        shader.uploadModel(modelMatrix);
        nanoforgeShieldModel.render();

        RenderState.restore(GL11.GL_BLEND, blendWas);
        RenderState.restore(GL11.GL_DEPTH_TEST, depthTestWas);
        GL11.glDepthMask(depthMaskWas);
        RenderState.restoreBlendFunc(blendFuncWas);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof RenderingTileEntityNanoForge nanoforge)) return;

        if (!initialized) return;

        // Manually calculating deltaT - annoying minecraft
        long systemTime = System.currentTimeMillis() % (36_000_000);
        long diff = systemTime - nanoforge.getLastSystemTime();
        nanoforge.setLastSystemTime(systemTime);
        float deltaT = diff / 1000.0f;
        // Making sure the first frame doesn't freak out
        if (deltaT > 1) {
            deltaT = 0;
        }

        renderNanoForge(nanoforge, x, y, z, deltaT);
    }

    private static void rotate(float degrees, float x, float y, float z) {
        final float length = (float) Math.sqrt(x * x + y * y + z * z);
        if (length == 0) return;
        modelMatrix.rotate((float) Math.toRadians(degrees), x / length, y / length, z / length);
    }

    private static float applyRotationMajor(float f) {
        return (float) (sin(f / SINUS_DIVIDER) + 1.5 * sin(f / SINUS_DIVIDER * 0.5)
            + 0.5 * sin(f / SINUS_DIVIDER * 0.1));
    }

    private static float applyRotationMinor(float f) {
        return (float) (sin(f / SINUS_DIVIDER) + 1.5 * sin(f / SINUS_DIVIDER * 0.2)
            + 0.5 * sin(f / SINUS_DIVIDER * 0.1));
    }

}
