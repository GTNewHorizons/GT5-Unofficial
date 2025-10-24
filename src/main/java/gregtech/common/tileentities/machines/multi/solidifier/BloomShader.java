package gregtech.common.tileentities.machines.multi.solidifier;

import static gregtech.api.enums.Mods.GregTech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.*;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizons.angelica.glsm.GLStateManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BloomShader {

    private static BloomShader instance;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static int ticks;

    private HDRFramebuffer[] framebuffers;

    private ShaderProgram downscaleProgram;
    private int uTexelSize;

    private ShaderProgram upscaleProgram;

    private boolean needsRendering;

    public BloomShader() {
        createFramebuffers();
        downscaleProgram = new ShaderProgram(
            GregTech.resourceDomain,
            "shaders/bloom/downscale.vert.glsl",
            "shaders/bloom/downscale.frag.glsl");
        uTexelSize = downscaleProgram.getUniformLocation("texelSize");
        /*
         * AutoShaderUpdater.getInstance()
         * .registerShaderReload(
         * downscaleProgram,
         * GregTech.resourceDomain,
         * "shaders/bloom/downscale.vert.glsl",
         * "shaders/bloom/downscale.frag.glsl",
         * (shader, vertexFile, fragmentFile) -> { uTexelSize = shader.getUniformLocation("texelSize"); });
         */
        upscaleProgram = new ShaderProgram(
            GregTech.resourceDomain,
            "shaders/bloom/upscale.vert.glsl",
            "shaders/bloom/upscale.frag.glsl");
        /*
         * AutoShaderUpdater.getInstance()
         * .registerShaderReload(
         * upscaleProgram,
         * GregTech.resourceDomain,
         * "shaders/bloom/upscale.vert.glsl",
         * "shaders/bloom/upscale.frag.glsl");
         */
    }

    private void createFramebuffers() {
        float width = mc.displayWidth;
        float height = mc.displayHeight;
        List<HDRFramebuffer> framebufferList = new ArrayList<>();
        while (width + height > 80) {
            HDRFramebuffer framebuffer = new HDRFramebuffer(Math.round(width), Math.round(height));
            framebufferList.add(framebuffer);
            framebuffer.setFramebufferFilter(GL11.GL_LINEAR);
            System.out.println(
                "Creating framebuffer (" + Math
                    .round(width) + ", " + Math.round(height) + ") " + framebufferList.size());

            width /= 2;
            height /= 2;
        }
        framebuffers = framebufferList.toArray(new HDRFramebuffer[0]);
    }

    public void bind() {
        final HDRFramebuffer mainFramebuffer = framebuffers[0];
        if (mc.displayWidth != mainFramebuffer.framebufferWidth
            || mc.displayHeight != mainFramebuffer.framebufferHeight) {
            for (HDRFramebuffer framebuffer : framebuffers) {
                framebuffer.deleteFramebuffer();
            }
            createFramebuffers();
        }

        needsRendering = true;
        mainFramebuffer.bindFramebuffer(true);
        mainFramebuffer.copyDepthFromFramebuffer(mc.getFramebuffer());
        mainFramebuffer.bindFramebuffer(true);
    }

    public void unbind() {
        mc.getFramebuffer()
            .bindFramebuffer(true);
    }

    @SubscribeEvent
    public void onOverlayDraw(RenderWorldLastEvent event) {
        if (!needsRendering) return;

        needsRendering = false;
        if (++ticks >= 100) {
            // remove the comment below to view the framebuffer debug
            // ticks = 0;
        }

        GLStateManager.glColor4f(1, 1, 1, 1);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GLStateManager.disableDepthTest();
        GLStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GLStateManager.disableAlphaTest();
        GLStateManager.disableCull();
        GLStateManager.disableLighting();

        framebuffers[0].bindFramebufferTexture();
        // framebuffers[0].copyTextureToFile("bloomshader", "framebuffer_main.png");
        downscaleProgram.use();
        for (int i = 1; i < framebuffers.length; i++) {
            HDRFramebuffer framebuffer = framebuffers[i];
            framebuffer.framebufferClear();
            framebuffer.bindFramebuffer(true);
            GL20.glUniform2f(uTexelSize, 1f / framebuffer.framebufferWidth, 1f / framebuffer.framebufferHeight);

            framebuffer.drawTexturedRect();

            framebuffer.bindFramebufferTexture();
            if (ticks == 0) {
                framebuffer.copyTextureToFile("bloomshader", "framebuffer_downsample_" + i + ".png");
            }
        }

        upscaleProgram.use();
        for (int i = framebuffers.length - 1; i >= 1; i--) {
            HDRFramebuffer framebuffer = framebuffers[i];
            HDRFramebuffer upscaledFramebuffer = framebuffers[i - 1];
            framebuffer.bindFramebufferTexture();
            upscaledFramebuffer.bindFramebuffer(true);

            upscaledFramebuffer.drawTexturedRect();

            if (ticks == 0) {
                upscaledFramebuffer.copyTextureToFile("bloomshader", "framebuffer_upsample_" + i + ".png");
            }
        }

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        mc.getFramebuffer()
            .bindFramebuffer(true);
        final HDRFramebuffer mainFramebuffer = framebuffers[0];
        mainFramebuffer.applyTonemapping();
        mainFramebuffer.framebufferClear();
        mc.getFramebuffer()
            .bindFramebuffer(true);

        ShaderProgram.clear();
        GL11.glPopAttrib();

        /*
         * GLStateManager.enableLighting();
         * GLStateManager.enableDepthTest();
         */
    }

    public static BloomShader getInstance() {
        if (instance == null) {
            instance = new BloomShader();
            MinecraftForge.EVENT_BUS.register(instance);
        }
        return instance;
    }
}
