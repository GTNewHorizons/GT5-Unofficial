package gregtech.common.tileentities.machines.multi.foundry;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.gtnewhorizon.gtnhlib.client.renderer.CapturingTessellator;
import com.gtnewhorizon.gtnhlib.client.renderer.TessellatorManager;
import com.gtnewhorizon.gtnhlib.client.renderer.quad.QuadView;
import com.gtnewhorizon.gtnhlib.client.renderer.vbo.VertexBuffer;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.DefaultVertexFormat;

public class QuadRenderer {

    private static VAOWrapper fullscreenQuadVAO;

    public static void renderFullscreenQuad() {
        if (fullscreenQuadVAO == null) {
            fullscreenQuadVAO = genFullscreenQuadVAO();
        }
        fullscreenQuadVAO.render();
    }

    public static void bindFullscreenVAO() {
        if (fullscreenQuadVAO == null) {
            fullscreenQuadVAO = genFullscreenQuadVAO();
        }
        fullscreenQuadVAO.bind();
    }

    public static void drawFullscreenQuad() {
        fullscreenQuadVAO.draw();
    }

    public static void unbind() {
        GL30.glBindVertexArray(0);
    }

    private static VAOWrapper genFullscreenQuadVAO() {
        TessellatorManager.startCapturing();
        final CapturingTessellator tessellator = (CapturingTessellator) TessellatorManager.get();
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-1, -1, 0, 0, 0);
        tessellator.addVertexWithUV(1, -1, 0, 1, 0);
        tessellator.addVertexWithUV(1, 1, 0, 1, 1);
        tessellator.addVertexWithUV(-1, 1, 0, 0, 1);
        tessellator.draw();

        VertexBuffer vertexBuffer = new VertexBuffer(DefaultVertexFormat.POSITION_TEXTURE, GL11.GL_QUADS);
        List<QuadView> quads = TessellatorManager.stopCapturingToPooledQuads();
        ByteBuffer bytes = CapturingTessellator.quadsToBuffer(quads, DefaultVertexFormat.POSITION_TEXTURE);
        vertexBuffer.upload(bytes);
        tessellator.clearQuads();

        return new VAOWrapper(vertexBuffer);
    }
}
