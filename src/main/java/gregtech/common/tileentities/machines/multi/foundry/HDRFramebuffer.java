package gregtech.common.tileentities.machines.multi.foundry;

import static gregtech.api.enums.Mods.GregTech;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.AutoShaderUpdater;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizons.angelica.glsm.GLStateManager;

public class HDRFramebuffer extends Framebuffer {

    private int settings;

    public static final int FRAMEBUFFER_DEPTH_ENABLED = 0x1; // This uses a depth renderbuffer by default
    public static final int FRAMEBUFFER_DEPTH_TEXTURE = 0x2 | 0x1;
    public static final int FRAMEBUFFER_STENCIL_BUFFER = 0x4;
    public static final int FRAMEBUFFER_TEXTURE_LINEAR = 0x8;
    public static final int FRAMEBUFFER_ALPHA_CHANNEL = 0x10;
    public static final int FRAMEBUFFER_HDR_COLORS = 0x20;

    private static ShaderProgram tonemapShader;
    private static int uMultiplier;
    private static float tonemapMultiplier;

    public HDRFramebuffer(int width, int height) {
        this(width, height, FRAMEBUFFER_DEPTH_ENABLED);
    }

    public HDRFramebuffer(int width, int height, int settings) {
        super(width, height, (settings & FRAMEBUFFER_DEPTH_ENABLED) != 0);
    }

    @Override
    public void createFramebuffer(int p_147605_1_, int p_147605_2_) {
        this.useDepth = true; // TODO
        this.framebufferWidth = p_147605_1_;
        this.framebufferHeight = p_147605_2_;
        this.framebufferTextureWidth = p_147605_1_;
        this.framebufferTextureHeight = p_147605_2_;

        this.framebufferObject = OpenGlHelper.func_153165_e();
        this.framebufferTexture = TextureUtil.glGenTextures();

        if (useDepth) {
            this.depthBuffer = OpenGlHelper.func_153185_f();
        }

        this.setFramebufferFilter(GL11.GL_LINEAR);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.framebufferTexture);
        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL30.GL_RGB32F,
            this.framebufferTextureWidth,
            this.framebufferTextureHeight,
            0,
            GL11.GL_RGB,
            GL11.GL_UNSIGNED_BYTE,
            (ByteBuffer) null);
        OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, this.framebufferObject);
        OpenGlHelper
            .func_153188_a(OpenGlHelper.field_153198_e, OpenGlHelper.field_153200_g, 3553, this.framebufferTexture, 0);

        if (useDepth) {
            OpenGlHelper.func_153176_h(OpenGlHelper.field_153199_f, this.depthBuffer);
            if (net.minecraftforge.client.MinecraftForgeClient.getStencilBits() == 0) {
                OpenGlHelper.func_153186_a(
                    OpenGlHelper.field_153199_f,
                    33190,
                    this.framebufferTextureWidth,
                    this.framebufferTextureHeight);
                OpenGlHelper.func_153190_b(
                    OpenGlHelper.field_153198_e,
                    OpenGlHelper.field_153201_h,
                    OpenGlHelper.field_153199_f,
                    this.depthBuffer);
            } else {
                OpenGlHelper.func_153186_a(
                    OpenGlHelper.field_153199_f,
                    org.lwjgl.opengl.EXTPackedDepthStencil.GL_DEPTH24_STENCIL8_EXT,
                    this.framebufferTextureWidth,
                    this.framebufferTextureHeight);
                OpenGlHelper.func_153190_b(
                    OpenGlHelper.field_153198_e,
                    org.lwjgl.opengl.EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT,
                    OpenGlHelper.field_153199_f,
                    this.depthBuffer);
                OpenGlHelper.func_153190_b(
                    OpenGlHelper.field_153198_e,
                    org.lwjgl.opengl.EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT,
                    OpenGlHelper.field_153199_f,
                    this.depthBuffer);
            }
        }

        setFramebufferColor(0, 0, 0, 0);
        this.framebufferClear();
        this.unbindFramebufferTexture();
    }

    public void applyTonemapping(float multiplier) {
        if (tonemapShader == null) {
            tonemapShader = new ShaderProgram(
                GregTech.resourceDomain,
                "shaders/hdr/tonemap.vert.glsl",
                "shaders/hdr/tonemap.frag.glsl");
            tonemapShader.use();
            uMultiplier = tonemapShader.getUniformLocation("multiplier");
            tonemapShader.bindTextureSlot("uScene", 0);
            tonemapShader.bindTextureSlot("uOverlay", 1);
            AutoShaderUpdater.getInstance()
                .registerShaderReload(
                    tonemapShader,
                    GregTech.resourceDomain,
                    "shaders/hdr/tonemap.vert.glsl",
                    "shaders/hdr/tonemap.frag.glsl",
                    (shader, vertexFile, fragmentFile) -> {
                        uMultiplier = tonemapShader.getUniformLocation("multiplier");
                        tonemapMultiplier = 0;
                        shader.bindTextureSlot("uScene", 0);
                        shader.bindTextureSlot("uOverlay", 1);
                    });
            ShaderProgram.clear();
        }

        tonemapShader.use();
        if (tonemapMultiplier != multiplier) {
            GL20.glUniform1f(uMultiplier, multiplier);
            tonemapMultiplier = multiplier;
        }
        Minecraft.getMinecraft()
            .getFramebuffer()
            .bindFramebufferTexture();
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        bindFramebufferTexture();
        GLStateManager.disableBlend();
        QuadRenderer.renderFullscreenQuad();
        // this.copyTextureToFile("bloomshader", "framebuffer_final.png");
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    public void copyTextureToFile(String category, String filename) {
        File dir = new File(new File(Minecraft.getMinecraft().mcDataDir, "debug"), category);
        dir.mkdirs();
        copyTextureToFile(new File(dir, filename));
    }

    public void copyTextureToFile(File file) {
        final int width = this.framebufferWidth;
        final int height = this.framebufferHeight;
        BufferedImage bufferedimage = new BufferedImage(width, height, 1);
        final int[] pixelValues = ((DataBufferInt) bufferedimage.getRaster()
            .getDataBuffer()).getData();
        IntBuffer pixelBuffer = BufferUtils.createIntBuffer(pixelValues.length);
        GLStateManager.glBindTexture(GL11.GL_TEXTURE_2D, framebufferTexture);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        pixelBuffer.get(pixelValues);

        // Flip textures
        final int halfHeight = height / 2;
        final int[] rowBuffer = new int[width];
        for (int y = 0; y < halfHeight; y++) {
            final int top = y * width;
            final int bottom = (height - 1 - y) * width;

            System.arraycopy(pixelValues, top, rowBuffer, 0, width);
            System.arraycopy(pixelValues, bottom, pixelValues, top, width);
            System.arraycopy(rowBuffer, 0, pixelValues, bottom, width);
        }

        copyBufferedImageToFile(bufferedimage, file);
        bufferedimage.flush();
    }

    private static void copyBufferedImageToFile(BufferedImage image, File file) {
        try {
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyDepthFromFramebuffer(Framebuffer other) {
        OpenGlHelper.func_153171_g(GL30.GL_READ_FRAMEBUFFER, other.framebufferObject);
        OpenGlHelper.func_153171_g(GL30.GL_DRAW_FRAMEBUFFER, framebufferObject);
        GL30.glBlitFramebuffer(
            0,
            0,
            other.framebufferWidth,
            other.framebufferHeight,
            0,
            0,
            framebufferWidth,
            framebufferHeight,
            GL11.GL_DEPTH_BUFFER_BIT,
            GL11.GL_NEAREST);

    }

    public void clearBindFramebuffer() {
        bindFramebuffer(false);
        GL11.glClearDepth(1.0D);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void clearBindFramebuffer(boolean viewport) {
        bindFramebuffer(viewport);
        GL11.glClearDepth(1.0D);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void framebufferClear() {
        super.framebufferClear();
    }

    public void setViewport() {
        GL11.glViewport(0, 0, this.framebufferWidth, this.framebufferHeight);
    }
}
