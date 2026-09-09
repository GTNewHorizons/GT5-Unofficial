package gregtech.common.render.shader;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.opengl.UniversalVAO;

// Runs on 3.3 core w/ Angelica
final class CoreMesh implements ShaderMesh {

    private final int vertexCount;
    private int vao;

    CoreMesh(int vertexCount) {
        this.vertexCount = vertexCount;
        this.vao = UniversalVAO.genVertexArrays();
    }

    @Override
    public void draw() {
        UniversalVAO.bindVertexArray(vao);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
        UniversalVAO.bindVertexArray(0);
    }

    @Override
    public void release() {
        if (vao != 0) {
            UniversalVAO.deleteVertexArrays(vao);
            vao = 0;
        }
    }
}
