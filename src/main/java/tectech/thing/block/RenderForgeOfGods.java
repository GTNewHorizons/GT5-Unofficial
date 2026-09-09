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

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormat;

import gregtech.GTMod;
import gregtech.common.render.shader.RenderState;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.ShaderRecipe;
import gregtech.common.render.shader.Uniform;
import gregtech.common.render.shader.VertexAttribute;
import tectech.Reference;
import tectech.TecTech;
import tectech.rendering.EOH.EOHRenderingUtils;
import tectech.thing.metaTileEntity.multi.godforge.structure.ForgeOfGodsRingsStructureString;
import tectech.thing.metaTileEntity.multi.godforge.structure.ForgeOfGodsStructureString;
import tectech.util.StructureVBO;
import tectech.util.TextureUpdateRequester;

public class RenderForgeOfGods extends TileEntitySpecialRenderer {

    private static final int STAR_TESSELLATION = 128;

    private static final ShaderRecipe STAR = ShaderRecipe.of(Reference.MODID, "star")
        .required("u_Color", "u_Gamma")
        .sampler("u_Texture", 0)
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION)
        .attribute("a_UV", VertexAttribute.UV);

    private static final Uniform STAR_COLOR = STAR.uniform("u_Color");
    private static final Uniform STAR_GAMMA = STAR.uniform("u_Gamma");

    private static ShaderHandle starShader;
    private static IVertexArrayObject starSphere;

    private static boolean initialized = false;
    private static boolean failedInit = false;
    private final Matrix4fStack starModelMatrix = new Matrix4fStack(3);

    private final FloatBuffer softBeamSegmentMatrixBuffer = BufferUtils.createFloatBuffer(maxSegments * 3);
    private final FloatBuffer intenseBeamSegmentMatrixBuffer = BufferUtils.createFloatBuffer(maxSegments * 3);

    private static final int maxSegments = 10;
    private static final int beamSegmentQuads = 16;

    private static final ShaderRecipe BEAM = ShaderRecipe.of(Reference.MODID, "gorgeBeam")
        .required("u_CameraPosition", "u_SegmentArray", "u_Color", "u_Intensity", "u_Time")
        .constant("u_SegmentQuads", beamSegmentQuads)
        .modelUniform("u_ModelMatrix")
        .coreOnly("u_CameraAngle")
        .attributeless("a_VertexID", maxSegments * beamSegmentQuads * 6);

    private static final Uniform BEAM_CAMERA_POSITION = BEAM.uniform("u_CameraPosition");
    private static final Uniform BEAM_SEGMENT_ARRAY = BEAM.uniform("u_SegmentArray");
    private static final Uniform BEAM_COLOR = BEAM.uniform("u_Color");
    private static final Uniform BEAM_INTENSITY = BEAM.uniform("u_Intensity");
    private static final Uniform BEAM_TIME = BEAM.uniform("u_Time");
    private static final Uniform BEAM_CAMERA_ANGLE = BEAM.uniform("u_CameraAngle");

    private static ShaderHandle beamShader;

    private static final Matrix4fStack beamModelMatrix = new Matrix4fStack(2);
    private static final ResourceLocation beamTexture = new ResourceLocation(Reference.MODID, "models/spaceLayer.png");

    private static IVertexArrayObject ringOne, ringTwo, ringThree;
    // These are nudges/translations for each ring to align with the structure
    private static final Vector3f ringOneNudge = new Vector3f(0, -1, 0);
    private static final Vector3f ringTwoNudge = new Vector3f(0, -1, 0);
    private static final Vector3f ringThreeNudge = new Vector3f(.5f, -1, 0);

    private static final ShaderRecipe FADE_BYPASS = ShaderRecipe.of(Reference.MODID, "fadebypass")
        .sampler("u_Texture", 0)
        .modelUniform("u_ModelMatrix")
        .attribute("a_Position", VertexAttribute.POSITION)
        .attribute("a_UV", VertexAttribute.UV);

    private static ShaderHandle fadeBypassShader;

    private final Matrix4f ringMatrix = new Matrix4f();

    private final Vector4f reusableStarColor = new Vector4f();
    private final Vector3f reusableRotationAxis = new Vector3f();
    private Vector4f reusableCameraPosition = new Vector4f();

    private float cachedRadius = -1f;
    private int cachedRingCount = -1;

    public static void reload() {
        initialized = false;
        failedInit = false;
        release();
        init();
        if (!initialized) {
            failedInit = true;
            return;
        }
        try {
            initRings();
        } catch (Exception e) {
            TecTech.LOGGER.error("Failed to initialize Forge of Gods rings", e);
            release();
            initialized = false;
            failedInit = true;
        }
    }

    private static void release() {
        if (starShader != null) {
            starShader.release();
            starShader = null;
        }
        if (beamShader != null) {
            beamShader.release();
            beamShader = null;
        }
        if (fadeBypassShader != null) {
            fadeBypassShader.release();
            fadeBypassShader = null;
        }
        if (starSphere != null) {
            starSphere.delete();
            starSphere = null;
        }
        if (ringOne != null) {
            ringOne.delete();
            ringOne = null;
        }
        if (ringTwo != null) {
            ringTwo.delete();
            ringTwo = null;
        }
        if (ringThree != null) {
            ringThree.delete();
            ringThree = null;
        }
    }

    private static void init() {
        starShader = STAR.bake();
        if (!starShader.isValid()) {
            TecTech.LOGGER.error("Failed to initialize Forge of Gods star shader");
            return;
        }
        starSphere = EOHRenderingUtils.buildSphere(starShader, STAR_TESSELLATION, STAR_TESSELLATION);

        beamShader = BEAM.bake();
        if (!beamShader.isValid()) {
            TecTech.LOGGER.error("Failed to initialize Forge of Gods beam shader");
            return;
        }

        initialized = true;
    }

    private static void initRings() {
        StructureVBO ringStructure = (new StructureVBO()).addMapping('H', BlockGodforgeGlass.INSTANCE, 0)
            .addMapping('B', GodforgeCasings, 0)
            .addMapping('C', GodforgeCasings, 1)
            .addMapping('D', GodforgeCasings, 2)
            .addMapping('E', GodforgeCasings, 3)
            .addMapping('G', GodforgeCasings, 5)
            .addMapping('K', GodforgeCasings, 6)
            .addMapping('I', GodforgeCasings, 7);

        fadeBypassShader = FADE_BYPASS.bake();
        if (!fadeBypassShader.isValid()) throw new IllegalStateException("fadebypass shader did not bake");

        final VertexFormat format = fadeBypassShader.vertexFormat();
        ringOne = ringStructure.assignStructure(ForgeOfGodsStructureString.FIRST_RING)
            .build(format);
        ringTwo = ringStructure.assignStructure(ForgeOfGodsRingsStructureString.SECOND_RING)
            .build(format);
        ringThree = ringStructure.assignStructure(ForgeOfGodsRingsStructureString.THIRD_RING)
            .build(format);

        TextureUpdateRequester textureUpdater = ringStructure.getTextureUpdateRequestor();
        textureUpdater.requestUpdate();
    }

    /**
     * <strong>WARNING:</strong> This method is a "dumb" renderer. It doesn't handle its own GL state
     * for transparency (blend, depth mask, etc...). The callers (renderStarOpaquePass and
     * renderStarTransparentPass) are responsible for setting all that up beforehand. We're doing it
     * this way to batch the state changes and improve performance.
     */
    public void RenderStarLayer(Vector4f color, ResourceLocation texture, float userScaleFactor, Vector3f rotationAxis,
        float degrees) {
        starModelMatrix.pushMatrix();
        starModelMatrix.rotate((degrees / 180f * (float) Math.PI), rotationAxis.x, rotationAxis.y, rotationAxis.z);
        starModelMatrix.scale(userScaleFactor, userScaleFactor, userScaleFactor);

        this.bindTexture(texture);

        starShader.uploadModel(starModelMatrix);
        GL20.glUniform4f(starShader.loc(STAR_COLOR), color.x, color.y, color.z, color.w);
        starSphere.draw();

        starModelMatrix.popMatrix();
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
        final boolean alphaTestWas = GL11.glGetBoolean(GL11.GL_ALPHA_TEST);
        final boolean blendWas = GL11.glGetBoolean(GL11.GL_BLEND);
        final boolean depthTestWas = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        final boolean depthMaskWas = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        final long blendFuncWas = RenderState.savedBlendFunc();

        GL11.glDisable(GL11.GL_ALPHA_TEST);
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

        beamShader.use();

        if (needsBeamUpdate) {
            bufferSoftBeam(tile);
            bufferIntenseBeam(tile);
        }

        beamShader.uploadModel(beamModelMatrix);

        beamModelMatrix.invert();
        reusableCameraPosition.set(ActiveRenderInfo.objectX, ActiveRenderInfo.objectY, ActiveRenderInfo.objectZ, 1);
        reusableCameraPosition = beamModelMatrix.transform(reusableCameraPosition);
        GL20.glUniform3f(
            beamShader.loc(BEAM_CAMERA_POSITION),
            reusableCameraPosition.x,
            reusableCameraPosition.y,
            reusableCameraPosition.z);
        if (beamShader.has(BEAM_CAMERA_ANGLE)) {
            GL20.glUniform1f(
                beamShader.loc(BEAM_CAMERA_ANGLE),
                (float) Math.atan2(reusableCameraPosition.y, reusableCameraPosition.x));
        }

        final int uColor = beamShader.loc(BEAM_COLOR);
        final int uIntensity = beamShader.loc(BEAM_INTENSITY);
        final int uSegmentArray = beamShader.loc(BEAM_SEGMENT_ARRAY);

        // Render Soft Beam
        GL20.glUniform3f(uColor, tile.getColorR(), tile.getColorG(), tile.getColorB());
        GL20.glUniform1f(uIntensity, 2);
        GL20.glUniform1f(beamShader.loc(BEAM_TIME), timer);
        softBeamSegmentMatrixBuffer.rewind();
        GL20.glUniform3(uSegmentArray, softBeamSegmentMatrixBuffer);
        beamShader.draw();

        // Render Intense Beam
        GL20.glUniform3f(uColor, 1, 1, 1);
        GL20.glUniform1f(uIntensity, 4);
        intenseBeamSegmentMatrixBuffer.rewind();
        GL20.glUniform3(uSegmentArray, intenseBeamSegmentMatrixBuffer);
        beamShader.draw();

        ShaderProgram.clear();

        RenderState.restore(GL11.GL_ALPHA_TEST, alphaTestWas);
        RenderState.restore(GL11.GL_BLEND, blendWas);
        RenderState.restore(GL11.GL_DEPTH_TEST, depthTestWas);
        GL11.glDepthMask(depthMaskWas);
        RenderState.restoreBlendFunc(blendFuncWas);
    }

    private void renderRings(TileEntityForgeOfGods tile, double x, double y, double z, float timer) {
        final boolean blendWas = GL11.glGetBoolean(GL11.GL_BLEND);
        final boolean depthTestWas = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        final boolean depthMaskWas = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        final long blendFuncWas = RenderState.savedBlendFunc();

        // Critical: Rings must participate in depth properly
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        bindTexture(TextureMap.locationBlocksTexture);
        fadeBypassShader.use();

        renderRing(tile, ringOne, x, y, z, timer / 6 * 7, ringOneNudge);

        if (tile.getRingCount() > 1) {
            renderRing(tile, ringTwo, x, y, z, -timer / 4 * 5, ringTwoNudge);

            if (tile.getRingCount() > 2) {
                renderRing(tile, ringThree, x, y, z, timer * 3, ringThreeNudge);
            }
        }

        ShaderProgram.clear();

        RenderState.restore(GL11.GL_BLEND, blendWas);
        RenderState.restore(GL11.GL_DEPTH_TEST, depthTestWas);
        GL11.glDepthMask(depthMaskWas);
        RenderState.restoreBlendFunc(blendFuncWas);
    }

    private void renderRing(TileEntityForgeOfGods tile, IVertexArrayObject ring, double x, double y, double z,
        float spinDegrees, Vector3f nudge) {
        ringMatrix.translation((float) x + .5f, (float) y + .5f, (float) z + .5f)
            .rotate(
                tile.getRotAngle() / 180f * (float) Math.PI,
                tile.getRotAxisX(),
                tile.getRotAxisY(),
                tile.getRotAxisZ())
            .rotate(spinDegrees / 180f * (float) Math.PI, 1, 0, 0)
            .translate(nudge);

        fadeBypassShader.uploadModel(ringMatrix);
        ring.render();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (failedInit) return;
        if (!(tile instanceof TileEntityForgeOfGods forgeTile)) return;
        if (forgeTile.getRingCount() < 1) return;

        if (!initialized) return;

        forgeTile.incrementColors();

        boolean needsBeamUpdate = false;
        if (forgeTile.getStarRadius() != this.cachedRadius || forgeTile.getRingCount() != this.cachedRingCount) {
            needsBeamUpdate = true;
            this.cachedRadius = forgeTile.getStarRadius();
            this.cachedRingCount = forgeTile.getRingCount();
        }

        float timer = GTMod.clientProxy()
            .getAnimationRenderTicks();

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
        final boolean blendWas = GL11.glGetBoolean(GL11.GL_BLEND);
        final boolean depthTestWas = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        final boolean depthMaskWas = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);

        GL11.glDisable(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        starShader.use();
        final long cullWas = EOHRenderingUtils.beginSphereCull(false);
        starSphere.bind();

        float cx = (float) x + .5f;
        float cy = (float) y + .5f;
        float cz = (float) z + .5f;

        starModelMatrix.clear();
        starModelMatrix.translate(cx, cy, cz);

        timer *= tile.getRotationSpeed();

        float r = tile.getColorR(), g = tile.getColorG(), b = tile.getColorB();
        GL20.glUniform1f(starShader.loc(STAR_GAMMA), tile.getGamma());

        // Render OPAQUE layer (writes to depth)
        RenderStarLayer(
            reusableStarColor.set(r, g, b, 1f),
            STAR_LAYER_0,
            tile.getStarRadius(),
            reusableRotationAxis.set(0F, 1F, 1F)
                .normalize(),
            130 + (timer) % 360000);

        starSphere.unbind();
        EOHRenderingUtils.endSphereCull(cullWas);
        ShaderProgram.clear();

        RenderState.restore(GL11.GL_BLEND, blendWas);
        RenderState.restore(GL11.GL_DEPTH_TEST, depthTestWas);
        GL11.glDepthMask(depthMaskWas);
    }

    private void renderStarTransparentPass(TileEntityForgeOfGods tile, double x, double y, double z, float timer) {
        final boolean blendWas = GL11.glGetBoolean(GL11.GL_BLEND);
        final boolean depthTestWas = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        final boolean depthMaskWas = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        final long blendFuncWas = RenderState.savedBlendFunc();

        // Transparent shells should depth-test but not write depth
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        starShader.use();
        final long cullWas = EOHRenderingUtils.beginSphereCull(false);
        starSphere.bind();

        float cx = (float) x + .5f;
        float cy = (float) y + .5f;
        float cz = (float) z + .5f;

        starModelMatrix.clear();
        starModelMatrix.translate(cx, cy, cz);

        timer *= tile.getRotationSpeed();

        float r = tile.getColorR(), g = tile.getColorG(), b = tile.getColorB();
        GL20.glUniform1f(starShader.loc(STAR_GAMMA), tile.getGamma());

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

        starSphere.unbind();
        EOHRenderingUtils.endSphereCull(cullWas);
        ShaderProgram.clear();

        RenderState.restore(GL11.GL_BLEND, blendWas);
        RenderState.restore(GL11.GL_DEPTH_TEST, depthTestWas);
        GL11.glDepthMask(depthMaskWas);
        RenderState.restoreBlendFunc(blendFuncWas);
    }
}
