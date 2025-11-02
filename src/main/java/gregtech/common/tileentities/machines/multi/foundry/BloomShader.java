package gregtech.common.tileentities.machines.multi.foundry;

import static gregtech.api.enums.Mods.GregTech;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.AutoShaderUpdater;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizons.angelica.glsm.GLStateManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BloomShader {

    private static BloomShader instance;
    private static final Minecraft mc = Minecraft.getMinecraft();

    private HDRFramebuffer[] framebuffers;

    private final ShaderProgram downscaleProgram;
    private int uTexelSize_downscale;

    private final ShaderProgram upscaleProgram;
    private int uTexelSize_upscale;

    private float multiplier;

    private boolean needsRendering;

    public BloomShader() {
        downscaleProgram = new ShaderProgram(
            GregTech.resourceDomain,
            "shaders/bloom/downscale.vert.glsl",
            "shaders/bloom/downscale.frag.glsl");
        uTexelSize_downscale = downscaleProgram.getUniformLocation("texelSize");

        // AutoShaderUpdater.getInstance()
        // .registerShaderReload(
        // downscaleProgram,
        // GregTech.resourceDomain,
        // "shaders/bloom/downscale.vert.glsl",
        // "shaders/bloom/downscale.frag.glsl",
        // (shader, vertexFile, fragmentFile) -> {
        // uTexelSize_downscale = shader.getUniformLocation("texelSize");
        // });

        upscaleProgram = new ShaderProgram(
            GregTech.resourceDomain,
            "shaders/bloom/upscale.vert.glsl",
            "shaders/bloom/upscale.frag.glsl");
        uTexelSize_upscale = upscaleProgram.getUniformLocation("texelSize");

        // AutoShaderUpdater.getInstance()
        // .registerShaderReload(
        // upscaleProgram,
        // GregTech.resourceDomain,
        // "shaders/bloom/upscale.vert.glsl",
        // "shaders/bloom/upscale.frag.glsl",
        // (shader, vertexFile, fragmentFile) -> uTexelSize_upscale = shader.getUniformLocation("texelSize"));

        createFramebuffers();
    }

    private void createFramebuffers() {
        float width = mc.displayWidth;
        float height = mc.displayHeight;
        List<HDRFramebuffer> framebufferList = new ArrayList<>();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode dm = gd.getDisplayMode();

        int screenWidth = dm.getWidth();
        int screenHeight = dm.getHeight();

        multiplier = 0.5f;
        if (width < screenWidth || height < screenHeight) {
            float widthMultiplier = width / screenWidth;
            float heightMultiplier = height / screenHeight;

            float avg = (float) Math.sqrt((widthMultiplier + heightMultiplier) / 2);
            multiplier *= avg;
        }

        while (framebufferList.size() < 8 && width + height > 5) {
            final HDRFramebuffer framebuffer;
            if (framebufferList.isEmpty()) {
                framebuffer = new HDRFramebuffer(
                    Math.round(width),
                    Math.round(height),
                    true);
            } else {
                framebuffer = new HDRFramebuffer(
                    Math.round(width),
                    Math.round(height),
                    false);
            }
            framebufferList.add(framebuffer);
            framebuffer.setFramebufferFilter(GL11.GL_LINEAR);

            width /= 2;
            height /= 2;
        }
        framebuffers = framebufferList.toArray(new HDRFramebuffer[0]);
    }

    public void bind() {
        HDRFramebuffer mainFramebuffer = framebuffers[0];
        if (mc.displayWidth != mainFramebuffer.framebufferWidth
            || mc.displayHeight != mainFramebuffer.framebufferHeight) {
            for (HDRFramebuffer framebuffer : framebuffers) {
                framebuffer.deleteFramebuffer();
            }
            createFramebuffers();
            mainFramebuffer = framebuffers[0];
        }

        needsRendering = true;
        mainFramebuffer.bindFramebuffer(false);
        mainFramebuffer.copyDepthFromFramebuffer(mc.getFramebuffer());
        mainFramebuffer.bindFramebuffer(false);
    }

    public static void unbind() {
        mc.getFramebuffer()
            .bindFramebuffer(false);
    }

    @SubscribeEvent
    public void onOverlayDraw(RenderWorldLastEvent event) {
        if (!needsRendering) return;

        needsRendering = false;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GLStateManager.glColor4f(1, 1, 1, 1);
        GLStateManager.disableDepthTest();
        GLStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GLStateManager.disableAlphaTest();

        QuadRenderer.bindFullscreenVAO();

        final HDRFramebuffer mainFramebuffer = framebuffers[0];

        mainFramebuffer.bindFramebufferTexture();
        downscaleProgram.use();

        for (int i = 1; i < framebuffers.length; i++) {
            HDRFramebuffer framebuffer = framebuffers[i];
            framebuffer.clearBindFramebuffer(true);
            GL20.glUniform2f(
                uTexelSize_downscale,
                1f / framebuffer.framebufferWidth,
                1f / framebuffer.framebufferHeight);

            QuadRenderer.drawFullscreenQuad();

            framebuffer.bindFramebufferTexture();
        }

        upscaleProgram.use();

        for (int i = framebuffers.length - 1; i >= 1; i--) {
            HDRFramebuffer framebuffer = framebuffers[i];
            HDRFramebuffer upscaledFramebuffer = framebuffers[i - 1];
            framebuffer.bindFramebufferTexture();
            upscaledFramebuffer.bindFramebuffer(true);
            GL20.glUniform2f(uTexelSize_upscale, 1f / framebuffer.framebufferWidth, 1f / framebuffer.framebufferHeight);

            QuadRenderer.drawFullscreenQuad();
        }

        mc.getFramebuffer()
            .bindFramebuffer(false);
        mainFramebuffer.applyTonemapping(this.multiplier);
        mainFramebuffer.clearBindFramebuffer();
        mc.getFramebuffer()
            .bindFramebuffer(false);

        ShaderProgram.clear();

        GLStateManager.enableDepthTest();
        GLStateManager.enableAlphaTest();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        QuadRenderer.unbind();
    }

    public static BloomShader getInstance() {
        if (instance == null) {
            instance = new BloomShader();
            MinecraftForge.EVENT_BUS.register(instance);
        }
        return instance;
    }
}
