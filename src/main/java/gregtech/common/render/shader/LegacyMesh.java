package gregtech.common.render.shader;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

// Runs on Opengl 2.1
final class LegacyMesh implements ShaderMesh {

    private final int vertexCount;
    private final int attribLocation;
    private int buffer;

    LegacyMesh(int program, String vertexIdAttrib, int vertexCount) {
        this.vertexCount = vertexCount;
        this.attribLocation = GL20.glGetAttribLocation(program, vertexIdAttrib);

        final FloatBuffer data = BufferUtils.createFloatBuffer(vertexCount * 2);
        for (int i = 0; i < vertexCount; i++) {
            data.put(i * 2, i);
        }

        buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void draw() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);

        if (attribLocation >= 0) {
            GL20.glVertexAttribPointer(attribLocation, 1, GL11.GL_FLOAT, false, 2 * Float.BYTES, 0);
            GL20.glEnableVertexAttribArray(attribLocation);
        }
        GL11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        if (attribLocation >= 0) GL20.glDisableVertexAttribArray(attribLocation);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void release() {
        if (buffer != 0) {
            GL15.glDeleteBuffers(buffer);
            buffer = 0;
        }
    }
}
