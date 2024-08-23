package goodgenerator.client.render;

import static gregtech.api.enums.Mods.GoodGenerator;

import java.nio.FloatBuffer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;

import cpw.mods.fml.client.registry.ClientRegistry;
import goodgenerator.blocks.tileEntity.render.TileAntimatter;

public class AntimatterRenderer extends TileEntitySpecialRenderer {

    // Antimatter 'Blob'
    private static ShaderProgram antimatterProgram;
    private static IModelCustom antimatterModel;
    private static IModelCustom containerModel;
    private static final float modelNormalize = .5f;

    private int uColorSpike = -1;
    private int uColorCore = -1;
    private int uSpikeMult = -1;
    private int uScale = -1;
    private int uScaleSnapshot = -1;
    private int uTime = -1;
    private int uTimeSnapshot = -1;
    private int uOpacity = -1;

    // Protomatter Beam
    private static final int particleCount = 32;
    private static ShaderProgram protomatterProgram;
    private int aBeamVertexID;
    private int uProtomatterTime = -1;
    private int uProtomatterScale = -1;
    private int uProtomatterColor = -1;
    private int uProtomatterSpiralRadius = -1;
    private static final int beamVertexCount = particleCount * 6 * 6;

    private boolean initialized = false;

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

    public AntimatterRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileAntimatter.class, this);
    }

    public void loadModels() {
        ResourceLocation antimatterLocation = new ResourceLocation(
            GoodGenerator.resourceDomain,
            "models/Antimatter.obj");
        antimatterModel = (IModelCustom) new WavefrontObject(antimatterLocation);

        ResourceLocation location = new ResourceLocation(GoodGenerator.resourceDomain, "models/SmoothSphere.obj");
        containerModel = (IModelCustom) new WavefrontObject(location);
    }

    private void init() {
        antimatterProgram = new ShaderProgram(
            GoodGenerator.resourceDomain,
            "shaders/antimatter.vert.glsl",
            "shaders/antimatter.frag.glsl");

        antimatterProgram.use();

        try {
            uScale = antimatterProgram.getUniformLocation("u_Scale");
            uScaleSnapshot = antimatterProgram.getUniformLocation("u_ScaleSnapshot");
            uTime = antimatterProgram.getUniformLocation("u_Time");
            uTimeSnapshot = antimatterProgram.getUniformLocation("u_TimeSnapshot");
            uSpikeMult = antimatterProgram.getUniformLocation("u_SpikeMult");
            uOpacity = antimatterProgram.getUniformLocation("u_Opacity");
            uColorCore = antimatterProgram.getUniformLocation("u_ColorCore");
            uColorSpike = antimatterProgram.getUniformLocation("u_ColorSpike");
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            ShaderProgram.clear();
            return;
        }

        GL20.glUniform3f(uColorCore, TileAntimatter.coreR, TileAntimatter.coreG, TileAntimatter.coreB);
        GL20.glUniform3f(uColorSpike, TileAntimatter.spikeR, TileAntimatter.spikeG, TileAntimatter.spikeB);
        GL20.glUniform1f(uOpacity, 1);

        ShaderProgram.clear();

        loadModels();

        protomatterProgram = new ShaderProgram(
            GoodGenerator.resourceDomain,
            "shaders/protomatter.vert.glsl",
            "shaders/protomatter.frag.glsl");

        int uCubeCount;
        int uProtomatterVertices;
        try {
            uProtomatterVertices = protomatterProgram.getUniformLocation("u_Vertices");
            uCubeCount = protomatterProgram.getUniformLocation("u_CubeCount");
            uProtomatterTime = protomatterProgram.getUniformLocation("u_Time");
            uProtomatterScale = protomatterProgram.getUniformLocation("u_Scale");
            uProtomatterColor = protomatterProgram.getUniformLocation("u_Color");
            uProtomatterSpiralRadius = protomatterProgram.getUniformLocation("u_SpiralRadius");
        } catch (NullPointerException e) {
            ShaderProgram.clear();
            return;
        }

        protomatterProgram.use();
        FloatBuffer bufferBeamVertexID = BufferUtils.createFloatBuffer(beamVertexCount * 3);
        for (int i = 0; i < beamVertexCount; i++) {
            bufferBeamVertexID.put(i * 3, i);
        }

        aBeamVertexID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, aBeamVertexID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferBeamVertexID, GL15.GL_STATIC_DRAW);

        FloatBuffer bufferBeamVertex = BufferUtils.createFloatBuffer(promomatterVerticies.length);
        bufferBeamVertex.put(promomatterVerticies)
            .flip();
        GL20.glUniform3(uProtomatterVertices, bufferBeamVertex);
        GL20.glUniform1f(uCubeCount, particleCount);
        ShaderProgram.clear();

        initialized = true;
    }

    private void renderAntimatter(TileAntimatter tile, double x, double y, double z, float timer) {
        antimatterProgram.use();

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        float angle = ((timer) % (20 * 60 * 60)) * tile.rotationSpeedMultiplier;

        GL11.glRotated(angle, 0, 1, 0);
        GL11.glRotated(angle / 8, 1, 0, 0);

        float snapshotSize = tile.coreScaleSnapshot;
        snapshotSize *= modelNormalize;
        float coreSizeSnapshot = Math.min(snapshotSize, TileAntimatter.maximalRadius / (1 + tile.spikeFactor));
        float targetSize = tile.coreScale;
        targetSize *= modelNormalize;
        float coreSize = Math.min(targetSize, TileAntimatter.maximalRadius / (1 + tile.spikeFactor));

        float realTime = timer / 20;
        float snapTime = tile.timeSnapshot / 20;

        GL20.glUniform1f(uTime, realTime);
        GL20.glUniform1f(uTimeSnapshot, snapTime);
        GL20.glUniform1f(uSpikeMult, tile.spikeFactor);
        GL20.glUniform1f(uScale, coreSize);
        GL20.glUniform1f(uScaleSnapshot, coreSizeSnapshot);
        GL11.glDisable(GL11.GL_CULL_FACE);

        antimatterModel.renderAll();

        ShaderProgram.clear();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glColor3f(0, 0, 0);
        GL11.glScalef(
            -coreSize * (1 + tile.spikeFactor),
            -coreSize * (1 + tile.spikeFactor),
            -coreSize * (1 + tile.spikeFactor));
        containerModel.renderAll();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderProtomatterBeam(TileAntimatter tile, double x, double y, double z, float timer) {
        protomatterProgram.use();
        GL11.glPushMatrix();
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL20.glEnableVertexAttribArray(aBeamVertexID);

        GL20.glUniform1f(uProtomatterTime, timer);
        GL20.glUniform1f(uProtomatterScale, tile.protomatterScale);
        GL20.glUniform3f(uProtomatterColor, TileAntimatter.protoR, TileAntimatter.protoG, TileAntimatter.protoB);
        GL20.glUniform1f(uProtomatterSpiralRadius, tile.getSpiralRadius(modelNormalize));

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, aBeamVertexID);
        GL20.glVertexAttribPointer(0, 1, GL11.GL_FLOAT, false, 3 * Float.BYTES, 2 * Float.BYTES);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

        GL11.glTranslated(x, y, z);
        GL11.glRotatef(tile.rotationAngle, tile.rotX, tile.rotY, tile.rotZ);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, beamVertexCount);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL20.glDisableVertexAttribArray(aBeamVertexID);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        ShaderProgram.clear();
        GL11.glPopMatrix();

    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        if (!(tile instanceof TileAntimatter Antimatter)) return;

        if (!Antimatter.shouldRender) return;

        if (!initialized) {
            init();
            if (!initialized) {
                return;
            }
        }
        float tx = (float) x + 0.5f;
        float ty = (float) y + 0.5f;
        float tz = (float) z + 0.5f;

        float timer = tile.getWorldObj()
            .getWorldInfo()
            .getWorldTotalTime() + timeSinceLastTick;
        renderAntimatter(Antimatter, tx, ty, tz, timer);

        if (!Antimatter.protomatterRender) return;
        renderProtomatterBeam(Antimatter, tx, ty, tz, timer);
    }
}
