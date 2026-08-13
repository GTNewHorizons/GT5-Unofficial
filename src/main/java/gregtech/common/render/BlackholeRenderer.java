package gregtech.common.render;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.model.wavefront.WavefrontVBOBuilder;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import gregtech.GTMod;
import gregtech.common.render.shader.MeshBuilder;
import gregtech.common.render.shader.RenderState;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.ShaderRecipe;
import gregtech.common.render.shader.Uniform;
import gregtech.common.render.shader.VertexAttribute;
import gregtech.common.tileentities.render.RenderingTileEntityBlackhole;

public class BlackholeRenderer extends TileEntitySpecialRenderer {

    private static boolean initialized = false;

    private static final ShaderRecipe BLACKHOLE = ShaderRecipe.of(GregTech.resourceDomain, "blackhole")
        .required("u_CameraPosition", "u_Scale", "u_Time", "u_Stability")
        .sampler("u_Texture", 0)
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION)
        .attribute("a_UV", VertexAttribute.UV);

    private static final Uniform BH_CAMERA_POSITION = BLACKHOLE.uniform("u_CameraPosition");
    private static final Uniform BH_SCALE = BLACKHOLE.uniform("u_Scale");
    private static final Uniform BH_TIME = BLACKHOLE.uniform("u_Time");
    private static final Uniform BH_STABILITY = BLACKHOLE.uniform("u_Stability");

    private static ShaderHandle blackholeShader;

    private static IVertexArrayObject blackholeModel;
    private static ResourceLocation blackholeTexture;
    private static final float modelScale = .5f;

    private static final ShaderRecipe LASER = ShaderRecipe.of(GregTech.resourceDomain, "laser")
        .required("u_CameraPosition", "u_Color")
        .sampler("u_Texture", 0)
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION)
        .attribute("a_UV", VertexAttribute.UV);

    private static final Uniform LASER_CAMERA_POSITION = LASER.uniform("u_CameraPosition");
    private static final Uniform LASER_COLOR = LASER.uniform("u_Color");

    private static ShaderHandle laserShader;
    private static IVertexArrayObject laserVBO;
    private static ResourceLocation laserTexture;

    private static final Matrix4fStack modelMatrix = new Matrix4fStack(2);
    private static final Matrix4f blackholeMatrix = new Matrix4f();

    private static final Vector4f laserCameraPosition = new Vector4f();

    private static final float WIDTH = .1f;
    private static final float EXCLUSION = 1f;

    public static void reload() {
        initialized = false;
        release();

        blackholeShader = BLACKHOLE.bake();
        if (!blackholeShader.isValid()) {
            GTMod.GT_FML_LOGGER.error("Failed to initialize black hole shader");
            release();
            return;
        }

        try {
            blackholeModel = WavefrontVBOBuilder.compileToVBO(
                new ResourceLocation(GregTech.resourceDomain, "textures/model/blackhole.obj"),
                blackholeShader.vertexFormat());
        } catch (RuntimeException e) {
            GTMod.GT_FML_LOGGER.error("Failed to load black hole model", e);
            release();
            return;
        }
        blackholeTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/blackhole.png");

        laserShader = LASER.bake();
        if (!laserShader.isValid()) {
            GTMod.GT_FML_LOGGER.error("Failed to initialize black hole laser shader");
            release();
            return;
        }

        laserTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/laser.png");

        try (MeshBuilder mesh = MeshBuilder.of(laserShader, 12)) {
            mesh.vertex(.5 + 8, 0, -WIDTH, 0, 0);
            mesh.vertex(.5 + 8, 0, WIDTH, 0, 1);
            mesh.vertex(EXCLUSION, 0, WIDTH / 5, 1, 1);
            mesh.vertex(EXCLUSION, 0, -WIDTH / 5, 1, 0);

            mesh.vertex(-.5 - 8, 0, -WIDTH, 0, 0);
            mesh.vertex(-.5 - 8, 0, WIDTH, 0, 1);
            mesh.vertex(-EXCLUSION, 0, WIDTH / 5, 1, 1);
            mesh.vertex(-EXCLUSION, 0, -WIDTH / 5, 1, 0);

            laserVBO = mesh.build();
        }

        initialized = true;
    }

    private static void release() {
        if (blackholeShader != null) {
            blackholeShader.release();
            blackholeShader = null;
        }
        if (laserShader != null) {
            laserShader.release();
            laserShader = null;
        }
        if (blackholeModel != null) {
            blackholeModel.delete();
            blackholeModel = null;
        }
        if (laserVBO != null) {
            laserVBO.delete();
            laserVBO = null;
        }
    }

    private void renderBlackHole(RenderingTileEntityBlackhole tile, double x, double y, double z, float timer) {
        blackholeShader.use();
        bindTexture(blackholeTexture);
        GL20.glUniform1f(blackholeShader.loc(BH_STABILITY), tile.getStability());

        float startTime = tile.getStartTime();
        float scaleF = timer - startTime;
        // If this is false we're shrinking, so subtract from 40 to translate to reversed scaling
        if (!tile.getScaling()) {
            scaleF = 40 - scaleF;
        }
        scaleF = MathHelper.clamp_float(scaleF / 40, 0, 1) * modelScale;
        // Smootherstep function to make it scale nicer
        scaleF = scaleF * scaleF * scaleF * (scaleF * (6.0f * scaleF - 15.0f) + 10.0f);
        GL20.glUniform1f(blackholeShader.loc(BH_SCALE), scaleF);

        float xLocal = ((float) x + .5f);
        float yLocal = ((float) y + .5f);
        float zLocal = ((float) z + .5f);
        GL20.glUniform3f(
            blackholeShader.loc(BH_CAMERA_POSITION),
            ActiveRenderInfo.objectX - xLocal,
            ActiveRenderInfo.objectY - yLocal,
            ActiveRenderInfo.objectZ - zLocal);

        GL20.glUniform1f(blackholeShader.loc(BH_TIME), timer);
        blackholeMatrix.translation(xLocal, yLocal, zLocal);
        blackholeShader.uploadModel(blackholeMatrix);
        blackholeModel.render();

        ShaderProgram.clear();
    }

    private void renderLasers(RenderingTileEntityBlackhole tile, double x, double y, double z) {
        laserShader.use();
        bindTexture(laserTexture);

        float cx = ((float) x + .5f);
        float cy = ((float) y + .5f);
        float cz = ((float) z + .5f);
        final boolean cullWas = GL11.glGetBoolean(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL20.glUniform3f(laserShader.loc(LASER_COLOR), tile.getLaserR(), tile.getLaserG(), tile.getLaserB());

        modelMatrix.clear();
        modelMatrix.translate(cx, cy, cz);

        // First set
        laserShader.uploadModel(modelMatrix);
        modelMatrix.pushMatrix();
        modelMatrix.invert();
        final Vector4f cameraPosition = laserCameraPosition
            .set(ActiveRenderInfo.objectX, ActiveRenderInfo.objectY, ActiveRenderInfo.objectZ, 1);
        modelMatrix.transform(cameraPosition);
        GL20.glUniform3f(laserShader.loc(LASER_CAMERA_POSITION), cameraPosition.x, cameraPosition.y, cameraPosition.z);
        laserVBO.render();

        // Second set

        modelMatrix.popMatrix();
        modelMatrix.rotate((float) Math.PI / 2, 0, 1, 0);

        laserShader.uploadModel(modelMatrix);

        modelMatrix.invert();
        cameraPosition.set(ActiveRenderInfo.objectX, ActiveRenderInfo.objectY, ActiveRenderInfo.objectZ, 1);
        modelMatrix.transform(cameraPosition);
        GL20.glUniform3f(laserShader.loc(LASER_CAMERA_POSITION), cameraPosition.x, cameraPosition.y, cameraPosition.z);
        laserVBO.render();

        RenderState.restore(GL11.GL_CULL_FACE, cullWas);
        ShaderProgram.clear();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof RenderingTileEntityBlackhole blackhole)) return;

        if (!initialized) return;

        if (blackhole.getLaserRender()) {
            renderLasers(blackhole, x, y, z);
        }

        renderBlackHole(
            blackhole,
            x,
            y,
            z,
            tile.getWorldObj()
                .getTotalWorldTime() + timeSinceLastTick);

    }

}
