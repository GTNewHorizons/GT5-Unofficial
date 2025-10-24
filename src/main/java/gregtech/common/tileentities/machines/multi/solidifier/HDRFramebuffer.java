package gregtech.common.tileentities.machines.multi.solidifier;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizons.angelica.glsm.GLStateManager;

public class HDRFramebuffer extends Framebuffer {

    private static ShaderProgram tonemapShader;

    public HDRFramebuffer(int width, int height) {
        this(width, height, true);
    }

    public HDRFramebuffer(int width, int height, boolean useDepth) {
        super(width, height, useDepth);
        this.useDepth = useDepth; // TODO remove
        setFramebufferColor(0, 0, 0, 0);
    }

    @Override
    public void createFramebuffer(int p_147605_1_, int p_147605_2_) {
        this.useDepth = true;
        this.framebufferWidth = p_147605_1_;
        this.framebufferHeight = p_147605_2_;
        this.framebufferTextureWidth = p_147605_1_;
        this.framebufferTextureHeight = p_147605_2_;

        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferClear();
        } else {
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
                GL30.GL_RGBA32F,
                this.framebufferTextureWidth,
                this.framebufferTextureHeight,
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
                (ByteBuffer) null);
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, this.framebufferObject);
            OpenGlHelper.func_153188_a(
                OpenGlHelper.field_153198_e,
                OpenGlHelper.field_153200_g,
                3553,
                this.framebufferTexture,
                0);

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

            this.framebufferClear();
            this.unbindFramebufferTexture();
        }
    }

    public void applyTonemapping() {
        /*
         * if (tonemapShader == null) {
         * tonemapShader = new ShaderProgram(
         * GregTech.resourceDomain,
         * "shaders/hdr/tonemap.vert.glsl",
         * "shaders/hdr/tonemap.frag.glsl");
         * tonemapShader.use();
         * tonemapShader.bindTextureSlot("uScene", 0);
         * tonemapShader.bindTextureSlot("uOverlay", 1);
         * AutoShaderUpdater.getInstance()
         * .registerShaderReload(
         * tonemapShader,
         * GregTech.resourceDomain,
         * "shaders/hdr/tonemap.vert.glsl",
         * "shaders/hdr/tonemap.frag.glsl",
         * (shader, vertexFile, fragmentFile) -> {
         * shader.bindTextureSlot("uScene", 0);
         * shader.bindTextureSlot("uOverlay", 1);
         * });
         * ShaderProgram.clear();
         * }
         * tonemapShader.use();
         * GL13.glActiveTexture(GL13.GL_TEXTURE0);
         * Minecraft.getMinecraft()
         * .getFramebuffer()
         * .bindFramebufferTexture();
         * GL13.glActiveTexture(GL13.GL_TEXTURE1);
         * bindFramebufferTexture();
         * GLStateManager.disableBlend();
         * drawTexturedRect();
         * // this.copyTextureToFile("bloomshader", "framebuffer_final.png");
         * GL13.glActiveTexture(GL13.GL_TEXTURE0);
         */
    }

    public void drawTexturedRect() {
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-1, 1, 0, 0, 1);
        tessellator.addVertexWithUV(1, 1, 0, 1, 1);
        tessellator.addVertexWithUV(1, -1, 0, 1, 0);
        tessellator.addVertexWithUV(-1, -1, 0, 0, 0);
        tessellator.draw();
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
        // TODO
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
}
