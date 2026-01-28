package gregtech.common.render;

import static gregtech.api.enums.Mods.GregTech;

import java.nio.FloatBuffer;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;

import org.joml.Matrix4fStack;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.CapturingTessellator;
import com.gtnewhorizon.gtnhlib.client.renderer.TessellatorManager;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vbo.IModelCustomExt;
import com.gtnewhorizon.gtnhlib.client.renderer.vbo.VertexBuffer;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.DefaultVertexFormat;

import gregtech.common.tileentities.render.RenderingTileEntityBlackhole;

public class BlackholeRenderer extends TileEntitySpecialRenderer {

    private boolean initialized = false;

    private ShaderProgram blackholeProgram;
    private static int u_CameraPosition = -1, u_Scale = -1, u_Time = -1, u_Stability = -1;
    private static final Matrix4fStack modelMatrixStack = new Matrix4fStack(4);

    private static IModelCustomExt blackholeModel;
    private static ResourceLocation blackholeTexture;
    private static final float modelScale = .5f;

    private ShaderProgram laserProgram;
    private static int u_LaserCameraPosition = -1, u_LaserColor = -1, u_LaserModelMatrix = -1;
    private static VertexBuffer laserVBO;
    private static ResourceLocation laserTexture;

    private static final Matrix4fStack modelMatrix = new Matrix4fStack(2);

    private static final float WIDTH = .1f;
    private static final float EXCLUSION = 1f;

    private void init() {
        try {
            blackholeProgram = new ShaderProgram(
                GregTech.resourceDomain,
                "shaders/blackhole.vert.glsl",
                "shaders/blackhole.frag.glsl");

            u_CameraPosition = blackholeProgram.getUniformLocation("u_CameraPosition");

            u_Scale = blackholeProgram.getUniformLocation("u_Scale");
            u_Time = blackholeProgram.getUniformLocation("u_Time");
            u_Stability = blackholeProgram.getUniformLocation("u_Stability");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        blackholeModel = (IModelCustomExt) AdvancedModelLoader
            .loadModel(new ResourceLocation(GregTech.resourceDomain, "textures/model/blackhole.obj"));
        blackholeTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/blackhole.png");

        blackholeProgram.use();
        GL20.glUniform1f(u_Scale, modelScale);
        GL20.glUniform1f(u_Stability, .1f);
        ShaderProgram.clear();

        try {
            laserProgram = new ShaderProgram(
                GregTech.resourceDomain,
                "shaders/laser.vert.glsl",
                "shaders/laser.frag.glsl");
            u_LaserCameraPosition = laserProgram.getUniformLocation("u_CameraPosition");
            u_LaserColor = laserProgram.getUniformLocation("u_Color");
            u_LaserModelMatrix = laserProgram.getUniformLocation("u_ModelMatrix");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        laserTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/laser.png");

        TessellatorManager.startCapturing();
        CapturingTessellator tess = (CapturingTessellator) TessellatorManager.get();

        tess.startDrawingQuads();

        tess.addVertexWithUV(.5 + 8, 0, -WIDTH, 0, 0);
        tess.addVertexWithUV(.5 + 8, 0, WIDTH, 0, 1);
        tess.addVertexWithUV(EXCLUSION, 0, WIDTH / 5, 1, 1);
        tess.addVertexWithUV(EXCLUSION, 0, -WIDTH / 5, 1, 0);

        tess.addVertexWithUV(-.5 - 8, 0, -WIDTH, 0, 0);
        tess.addVertexWithUV(-.5 - 8, 0, WIDTH, 0, 1);
        tess.addVertexWithUV(-EXCLUSION, 0, WIDTH / 5, 1, 1);
        tess.addVertexWithUV(-EXCLUSION, 0, -WIDTH / 5, 1, 0);

        tess.draw();

        laserVBO = TessellatorManager.stopCapturingToVBO(DefaultVertexFormat.POSITION_TEXTURE_NORMAL);

        initialized = true;
    }

    private void renderBlackHole(RenderingTileEntityBlackhole tile, double x, double y, double z, float timer) {
        blackholeProgram.use();
        bindTexture(blackholeTexture);
        GL20.glUniform1f(u_Stability, tile.getStability());

        float startTime = tile.getStartTime();
        float scaleF = timer - startTime;
        // If this is false we're shrinking, so subtract from 40 to translate to reversed scaling
        if (!tile.getScaling()) {
            scaleF = 40 - scaleF;
        }
        scaleF = MathHelper.clamp_float(scaleF / 40, 0, 1) * modelScale;
        // Smootherstep function to make it scale nicer
        scaleF = scaleF * scaleF * scaleF * (scaleF * (6.0f * scaleF - 15.0f) + 10.0f);
        GL20.glUniform1f(u_Scale, scaleF);

        modelMatrixStack.clear();

        float xLocal = ((float) x + .5f);
        float yLocal = ((float) y + .5f);
        float zLocal = ((float) z + .5f);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL20.glUniform3f(
            u_CameraPosition,
            ActiveRenderInfo.objectX - xLocal,
            ActiveRenderInfo.objectY - yLocal,
            ActiveRenderInfo.objectZ - zLocal);

        GL20.glUniform1f(u_Time, timer);
        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        blackholeModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
        ShaderProgram.clear();
    }

    private void renderLasers(RenderingTileEntityBlackhole tile, double x, double y, double z) {
        laserProgram.use();
        bindTexture(laserTexture);

        float cx = ((float) x + .5f);
        float cy = ((float) y + .5f);
        float cz = ((float) z + .5f);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glPushMatrix();
        GL20.glUniform3f(u_LaserColor, tile.getLaserR(), tile.getLaserG(), tile.getLaserB());

        modelMatrix.clear();
        modelMatrix.translate(cx, cy, cz);

        // First set
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        GL20.glUniformMatrix4(u_LaserModelMatrix, false, modelMatrix.get(matrixBuffer));
        modelMatrix.pushMatrix();
        modelMatrix.invert();
        Vector4f cameraPosition = new Vector4f(
            ActiveRenderInfo.objectX,
            ActiveRenderInfo.objectY,
            ActiveRenderInfo.objectZ,
            1);
        cameraPosition = modelMatrix.transform(cameraPosition);
        GL20.glUniform3f(u_LaserCameraPosition, cameraPosition.x, cameraPosition.y, cameraPosition.z);
        laserVBO.render();

        // Second set

        modelMatrix.popMatrix();
        matrixBuffer.clear();
        modelMatrix.rotate((float) Math.PI / 2, 0, 1, 0);

        GL20.glUniformMatrix4(u_LaserModelMatrix, false, modelMatrix.get(matrixBuffer));

        modelMatrix.invert();
        cameraPosition.set(ActiveRenderInfo.objectX, ActiveRenderInfo.objectY, ActiveRenderInfo.objectZ, 1);
        cameraPosition = modelMatrix.transform(cameraPosition);
        GL20.glUniform3f(u_LaserCameraPosition, cameraPosition.x, cameraPosition.y, cameraPosition.z);
        laserVBO.render();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
        ShaderProgram.clear();
    }

    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof RenderingTileEntityBlackhole blackhole)) return;

        if (!initialized) {
            init();
            if (!initialized) return;
        }
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
