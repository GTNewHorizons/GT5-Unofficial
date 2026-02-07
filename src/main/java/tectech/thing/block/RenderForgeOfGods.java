package tectech.thing.block;

import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_0;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_1;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_2;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;

import java.nio.FloatBuffer;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vbo.VertexBuffer;

import gregtech.common.GTClient;
import tectech.Reference;
import tectech.rendering.EOH.EOHRenderingUtils;
import tectech.thing.metaTileEntity.multi.godforge.structure.ForgeOfGodsRingsStructureString;
import tectech.thing.metaTileEntity.multi.godforge.structure.ForgeOfGodsStructureString;
import tectech.util.StructureVBO;
import tectech.util.TextureUpdateRequester;

public class RenderForgeOfGods extends TileEntitySpecialRenderer {

    private static ShaderProgram starProgram;

    private static boolean initialized = false;
    private static boolean failedInit = false;
    private static int u_Color = -1, u_ModelMatrix = -1, u_Gamma = -1;
    private final Matrix4fStack starModelMatrix = new Matrix4fStack(3);

    private final FloatBuffer softBeamSegmentMatrixBuffer = BufferUtils.createFloatBuffer(maxSegments * 3);
    private final FloatBuffer intenseBeamSegmentMatrixBuffer = BufferUtils.createFloatBuffer(maxSegments * 3);
    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private static ShaderProgram beamProgram;
    private static int a_VertexID = -1;
    private static int u_BeamModelMatrix = -1;
    private static int u_CameraPosition = -1, u_SegmentArray = -1, u_SegmentQuads = -1;
    private static int u_BeamIntensity = -1, u_BeamColor = -1, u_BeamTime = -1;
    private static int beam_vboID = -1;
    private static final int maxSegments = 10;
    private static final int beamSegmentQuads = 16;
    private static final Matrix4fStack beamModelMatrix = new Matrix4fStack(2);
    private static final ResourceLocation beamTexture = new ResourceLocation(Reference.MODID, "models/spaceLayer.png");

    private VertexBuffer ringOne, ringTwo, ringThree;
    // These are nudges/translations for each ring to align with the structure
    private static final Vector3f ringOneNudge = new Vector3f(0, -1, 0);
    private static final Vector3f ringTwoNudge = new Vector3f(0, -1, 0);
    private static final Vector3f ringThreeNudge = new Vector3f(.5f, -1, 0);

    private static ShaderProgram fadeBypassProgram;

    private final Vector4f reusableStarColor = new Vector4f();
    private final Vector3f reusableRotationAxis = new Vector3f();
    private Vector4f reusableCameraPosition = new Vector4f();

    private float cachedRadius = -1f;
    private int cachedRingCount = -1;

    private void init() {
        try {
            starProgram = new ShaderProgram(Reference.MODID, "shaders/star.vert.glsl", "shaders/star.frag.glsl");

            u_Color = starProgram.getUniformLocation("u_Color");
            u_Gamma = starProgram.getUniformLocation("u_Gamma");
            u_ModelMatrix = starProgram.getUniformLocation("u_ModelMatrix");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            beamProgram = new ShaderProgram(
                Reference.MODID,
                "shaders/gorgeBeam.vert.glsl",
                "shaders/gorgeBeam.frag.glsl");

            u_BeamModelMatrix = beamProgram.getUniformLocation("u_ModelMatrix");
            u_CameraPosition = beamProgram.getUniformLocation("u_CameraPosition");
            u_SegmentQuads = beamProgram.getUniformLocation("u_SegmentQuads");
            u_SegmentArray = beamProgram.getUniformLocation("u_SegmentArray");
            u_BeamColor = beamProgram.getUniformLocation("u_Color");
            u_BeamIntensity = beamProgram.getUniformLocation("u_Intensity");
            u_BeamTime = beamProgram.getUniformLocation("u_Time");

            a_VertexID = beamProgram.getAttribLocation("a_VertexID");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        beamProgram.use();
        GL20.glUniform1f(u_SegmentQuads, (float) beamSegmentQuads);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(maxSegments * beamSegmentQuads * 6 * 3);

        for (int i = 0; i < maxSegments; i++) {
            for (int j = 0; j < beamSegmentQuads; j++) {
                for (int v = 0; v < 6; v++) {
                    int segID = i * beamSegmentQuads * 6;
                    int quadID = j * 6;
                    int vertID = segID + quadID + v;
                    buffer.put(vertID);
                    buffer.put(0);
                    buffer.put(0);
                }
            }
        }

        buffer.flip();
        beam_vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, beam_vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(a_VertexID, 1, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        ShaderProgram.clear();
        initialized = true;
    }

    private void initRings() {
        StructureVBO ringStructure = (new StructureVBO()).addMapping('H', BlockGodforgeGlass.INSTANCE, 0)
            .addMapping('B', GodforgeCasings, 0)
            .addMapping('C', GodforgeCasings, 1)
            .addMapping('D', GodforgeCasings, 2)
            .addMapping('E', GodforgeCasings, 3)
            .addMapping('G', GodforgeCasings, 5)
            .addMapping('K', GodforgeCasings, 6)
            .addMapping('I', GodforgeCasings, 7);

        ringOne = ringStructure.assignStructure(ForgeOfGodsStructureString.FIRST_RING)
            .build();
        ringTwo = ringStructure.assignStructure(ForgeOfGodsRingsStructureString.SECOND_RING)
            .build();
        ringThree = ringStructure.assignStructure(ForgeOfGodsRingsStructureString.THIRD_RING)
            .build();

        fadeBypassProgram = new ShaderProgram(
            Reference.MODID,
            "shaders/fadebypass.vert.glsl",
            "shaders/fadebypass.frag.glsl");

        TextureUpdateRequester textureUpdater = ringStructure.getTextureUpdateRequestor();
        textureUpdater.requestUpdate();
    }

    /**
     * <strong>WARNING:</strong> This method is a "dumb" renderer. It doesn't handle its own GL state
     * for transparency (blend, depth mask, etc...). The caller (RenderEntireStar) is responsible
     * for setting all that up beforehand. We're doing it this way to batch the state
     * changes and improve performance.
     */
    public void RenderStarLayer(Vector4f color, ResourceLocation texture, float userScaleFactor, Vector3f rotationAxis,
        float degrees) {
        starModelMatrix.pushMatrix();
        starModelMatrix.rotate((degrees / 180f * (float) Math.PI), rotationAxis.x, rotationAxis.y, rotationAxis.z);
        starModelMatrix.scale(userScaleFactor, userScaleFactor, userScaleFactor);

        this.bindTexture(texture);

        matrixBuffer.clear();
        GL20.glUniformMatrix4(u_ModelMatrix, false, starModelMatrix.get(matrixBuffer));
        GL20.glUniform4f(u_Color, color.x, color.y, color.z, color.w);
        EOHRenderingUtils.renderTessellatedSphere(128, 128, 1);

        starModelMatrix.popMatrix();
    }

    public void RenderEntireStar(TileEntityForgeOfGods tile, double x, double y, double z, float timer) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        starProgram.use();

        float cx = (float) x + .5f;
        float cy = (float) y + .5f;
        float cz = (float) z + .5f;
        starModelMatrix.clear();
        starModelMatrix.translate(cx, cy, cz);

        timer *= tile.getRotationSpeed();

        float r = tile.getColorR(), g = tile.getColorG(), b = tile.getColorB();
        GL20.glUniform1f(u_Gamma, tile.getGamma());

        // Render OPAQUE layer
        RenderStarLayer(
            reusableStarColor.set(r, g, b, 1f),
            STAR_LAYER_0,
            tile.getStarRadius(),
            reusableRotationAxis.set(0F, 1F, 1F)
                .normalize(),
            130 + (timer) % 360000);

        // Setup for TRANSPARENT layers
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        // Render for TRANSPARENT layers
        RenderStarLayer(
            reusableStarColor.set(r, g, b, 0.4f),
            STAR_LAYER_1,
            tile.getStarRadius() * 1.02f,
            reusableRotationAxis.set(1F, 1F, 0F)
                .normalize(),
            -49 + (timer) % 360000);
        RenderStarLayer(
            reusableStarColor.set(r, g, b, 0.2f),
            STAR_LAYER_2,
            tile.getStarRadius() * 1.04f,
            reusableRotationAxis.set(1F, 0F, 1F)
                .normalize(),
            67 + (timer) % 360000);

        ShaderProgram.clear();
        GL11.glPopAttrib();
    }

    public void bufferSoftBeam(TileEntityForgeOfGods tile) {
        float angle = tile.getStartAngle();
        float radius = tile.getStarRadius() * 1.1f;
        float startx = -radius * (float) Math.cos(angle);
        float starty = radius * (float) Math.sin(angle);

        softBeamSegmentMatrixBuffer.clear();

        softBeamSegmentMatrixBuffer.put(starty);
        softBeamSegmentMatrixBuffer.put(startx);
        softBeamSegmentMatrixBuffer.put(0);

        for (int i = tile.getRingCount() - 1; i >= 0; i--) {
            softBeamSegmentMatrixBuffer.put(tile.getLenRadius(i));
            softBeamSegmentMatrixBuffer.put(tile.getLensDistance(i));
            softBeamSegmentMatrixBuffer.put(1f);
        }

        softBeamSegmentMatrixBuffer.put(TileEntityForgeOfGods.BACK_PLATE_RADIUS);
        softBeamSegmentMatrixBuffer.put(TileEntityForgeOfGods.BACK_PLATE_DISTANCE);
        softBeamSegmentMatrixBuffer.put(-.05f);

        softBeamSegmentMatrixBuffer.rewind();
    }

    public void bufferIntenseBeam(TileEntityForgeOfGods tile) {
        float angle = tile.getStartAngle();
        float radius = tile.getStarRadius() * 1.05f;
        float startx = -radius * (float) Math.cos(angle);
        float starty = radius * (float) Math.sin(angle);

        // first lens means the one closest to the star
        int firstLens = tile.getRingCount() - 1;

        float nextx = tile.getLensDistance(firstLens);
        float nexty = tile.getLenRadius(firstLens) * .75f;

        float backx = Math.max(-radius, (nextx + radius) / 2);
        float backy = TileEntityForgeOfGods.interpolate(startx, nextx, starty, nexty, backx);

        intenseBeamSegmentMatrixBuffer.clear();

        intenseBeamSegmentMatrixBuffer.put(backy);
        intenseBeamSegmentMatrixBuffer.put(backx);
        intenseBeamSegmentMatrixBuffer.put(0);

        float transparency = .2f;
        for (int i = tile.getRingCount() - 1; i >= 0; i--) {
            intenseBeamSegmentMatrixBuffer.put(tile.getLenRadius(i) / 2);
            intenseBeamSegmentMatrixBuffer.put(tile.getLensDistance(i));
            intenseBeamSegmentMatrixBuffer.put(transparency);
            transparency += .3f;
        }

        float currx = tile.getLensDistance(0);
        float curry = tile.getLenRadius(0) / 2;
        float lastx = TileEntityForgeOfGods.BACK_PLATE_DISTANCE;
        float lasty = Math.min(tile.getLenRadius(firstLens), TileEntityForgeOfGods.BACK_PLATE_RADIUS);

        float midx = lastx + 8f;
        float midy = TileEntityForgeOfGods.interpolate(currx, lastx, curry, lasty, midx);

        intenseBeamSegmentMatrixBuffer.put(midy);
        intenseBeamSegmentMatrixBuffer.put(midx);
        intenseBeamSegmentMatrixBuffer.put(transparency);

        intenseBeamSegmentMatrixBuffer.put(lasty);
        intenseBeamSegmentMatrixBuffer.put(lastx);
        intenseBeamSegmentMatrixBuffer.put(0f);

        intenseBeamSegmentMatrixBuffer.rewind();
    }

    public void RenderBeamSegment(TileEntityForgeOfGods tile, double x, double y, double z, float timer,
        boolean needsBeamUpdate) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        this.bindTexture(beamTexture);

        float cx = (float) x + .5f;
        float cy = (float) y + .5f;
        float cz = (float) z + .5f;
        beamModelMatrix.clear();
        beamModelMatrix.translate(cx, cy, cz);

        beamModelMatrix.rotate(
            tile.getRotAngle() / 180 * (float) Math.PI,
            tile.getRotAxisX(),
            tile.getRotAxisY(),
            tile.getRotAxisZ());
        beamModelMatrix.rotate((float) Math.PI / 2f, 0, 1, 0);

        beamProgram.use();

        if (needsBeamUpdate) {
            bufferSoftBeam(tile);
            bufferIntenseBeam(tile);
        }

        matrixBuffer.clear();
        GL20.glUniformMatrix4(u_BeamModelMatrix, false, beamModelMatrix.get(matrixBuffer));

        beamModelMatrix.invert();
        reusableCameraPosition.set(ActiveRenderInfo.objectX, ActiveRenderInfo.objectY, ActiveRenderInfo.objectZ, 1);
        reusableCameraPosition = beamModelMatrix.transform(reusableCameraPosition);
        GL20.glUniform3f(
            u_CameraPosition,
            reusableCameraPosition.x,
            reusableCameraPosition.y,
            reusableCameraPosition.z);

        GL20.glEnableVertexAttribArray(a_VertexID);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        // Render Soft Beam
        GL20.glUniform3f(u_BeamColor, tile.getColorR(), tile.getColorG(), tile.getColorB());
        GL20.glUniform1f(u_BeamIntensity, 2);
        GL20.glUniform1f(u_BeamTime, timer);
        softBeamSegmentMatrixBuffer.rewind();
        GL20.glUniform3(u_SegmentArray, softBeamSegmentMatrixBuffer);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, maxSegments * beamSegmentQuads * 6);

        // Render Intense Beam
        GL20.glUniform3f(u_BeamColor, 1, 1, 1);
        GL20.glUniform1f(u_BeamIntensity, 4);
        intenseBeamSegmentMatrixBuffer.rewind();
        GL20.glUniform3(u_SegmentArray, intenseBeamSegmentMatrixBuffer);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, maxSegments * beamSegmentQuads * 6);

        GL20.glDisableVertexAttribArray(a_VertexID);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

        ShaderProgram.clear();

        GL11.glPopAttrib();
    }

    private void renderRings(TileEntityForgeOfGods tile, double x, double y, double z, float timer) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);

        GL11.glDisable(GL11.GL_LIGHTING);

        // Critical: Rings must participate in depth properly
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        bindTexture(TextureMap.locationBlocksTexture);
        fadeBypassProgram.use();

        GL11.glPushMatrix();
        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glRotatef(tile.getRotAngle(), tile.getRotAxisX(), tile.getRotAxisY(), tile.getRotAxisZ());
        GL11.glRotatef(timer / 6 * 7, 1, 0, 0);
        GL11.glTranslated(ringOneNudge.x, ringOneNudge.y, ringOneNudge.z);
        ringOne.render();
        GL11.glPopMatrix();

        if (tile.getRingCount() > 1) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + .5f, y + .5f, z + .5f);
            GL11.glRotatef(tile.getRotAngle(), tile.getRotAxisX(), tile.getRotAxisY(), tile.getRotAxisZ());
            GL11.glRotatef(-timer / 4 * 5, 1, 0, 0);
            GL11.glTranslated(ringTwoNudge.x, ringTwoNudge.y, ringTwoNudge.z);
            ringTwo.render();
            GL11.glPopMatrix();

            if (tile.getRingCount() > 2) {
                GL11.glPushMatrix();
                GL11.glTranslated(x + .5f, y + .5f, z + .5f);
                GL11.glRotatef(tile.getRotAngle(), tile.getRotAxisX(), tile.getRotAxisY(), tile.getRotAxisZ());
                GL11.glRotatef(timer * 3, 1, 0, 0);
                GL11.glTranslated(ringThreeNudge.x, ringThreeNudge.y, ringThreeNudge.z);
                ringThree.render();
                GL11.glPopMatrix();
            }
        }

        ShaderProgram.clear();
        GL11.glPopAttrib();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (failedInit) return;
        if (!(tile instanceof TileEntityForgeOfGods forgeTile)) return;
        if (forgeTile.getRingCount() < 1) return;

        if (!initialized) {
            init();
            if (!initialized) {
                failedInit = true;
                return;
            }
            try {
                initRings();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                failedInit = true;
                return;
            }
        }

        forgeTile.incrementColors();

        boolean needsBeamUpdate = false;
        if (forgeTile.getStarRadius() != this.cachedRadius || forgeTile.getRingCount() != this.cachedRingCount) {
            needsBeamUpdate = true;
            this.cachedRadius = forgeTile.getStarRadius();
            this.cachedRingCount = forgeTile.getRingCount();
        }

        float timer = GTClient.getAnimationRenderTicks();

        // Correct order for transparency/depth:
        // 1) Opaque star writes depth
        renderStarOpaquePass(forgeTile, x, y, z, timer);

        // 2) Rings render next and write depth
        renderRings(forgeTile, x, y, z, timer);

        // 3) Transparent star shells render last and blend correctly (no depth write)
        renderStarTransparentPass(forgeTile, x, y, z, timer);

        // Beam last
        RenderBeamSegment(forgeTile, x, y, z, timer, needsBeamUpdate);
    }

    private void renderStarOpaquePass(TileEntityForgeOfGods tile, double x, double y, double z, float timer) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        starProgram.use();

        float cx = (float) x + .5f;
        float cy = (float) y + .5f;
        float cz = (float) z + .5f;

        starModelMatrix.clear();
        starModelMatrix.translate(cx, cy, cz);

        timer *= tile.getRotationSpeed();

        float r = tile.getColorR(), g = tile.getColorG(), b = tile.getColorB();
        GL20.glUniform1f(u_Gamma, tile.getGamma());

        // Render OPAQUE layer (writes to depth)
        RenderStarLayer(
            reusableStarColor.set(r, g, b, 1f),
            STAR_LAYER_0,
            tile.getStarRadius(),
            reusableRotationAxis.set(0F, 1F, 1F)
                .normalize(),
            130 + (timer) % 360000);

        ShaderProgram.clear();
        GL11.glPopAttrib();
    }

    private void renderStarTransparentPass(TileEntityForgeOfGods tile, double x, double y, double z, float timer) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);

        GL11.glDisable(GL11.GL_LIGHTING);

        // Transparent shells should depth-test but not write depth
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        starProgram.use();

        float cx = (float) x + .5f;
        float cy = (float) y + .5f;
        float cz = (float) z + .5f;

        starModelMatrix.clear();
        starModelMatrix.translate(cx, cy, cz);

        timer *= tile.getRotationSpeed();

        float r = tile.getColorR(), g = tile.getColorG(), b = tile.getColorB();
        GL20.glUniform1f(u_Gamma, tile.getGamma());

        // Render TRANSPARENT layers last, so they correctly blend over rings when in front
        RenderStarLayer(
            reusableStarColor.set(r, g, b, 0.4f),
            STAR_LAYER_1,
            tile.getStarRadius() * 1.02f,
            reusableRotationAxis.set(1F, 1F, 0F)
                .normalize(),
            -49 + (timer) % 360000);

        RenderStarLayer(
            reusableStarColor.set(r, g, b, 0.2f),
            STAR_LAYER_2,
            tile.getStarRadius() * 1.04f,
            reusableRotationAxis.set(1F, 0F, 1F)
                .normalize(),
            67 + (timer) % 360000);

        ShaderProgram.clear();
        GL11.glPopAttrib();
    }
}
