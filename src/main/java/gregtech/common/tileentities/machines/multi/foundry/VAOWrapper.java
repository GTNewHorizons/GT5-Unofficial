package gregtech.common.tileentities.machines.multi.foundry;

import org.lwjgl.opengl.GL30;

import com.gtnewhorizon.gtnhlib.client.renderer.vbo.VertexBuffer;

public class VAOWrapper {

    private final VertexBuffer underlyingVBO;
    private final int vaoID;

    public VAOWrapper(VertexBuffer vbo) {
        this.underlyingVBO = vbo;
        this.vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);
        vbo.setupState();

        GL30.glBindVertexArray(0);
    }

    public void render() {
        GL30.glBindVertexArray(vaoID);
        underlyingVBO.draw();
        GL30.glBindVertexArray(0);
    }

    public void bind() {
        GL30.glBindVertexArray(vaoID);
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void draw() {
        underlyingVBO.draw();
    }
}
