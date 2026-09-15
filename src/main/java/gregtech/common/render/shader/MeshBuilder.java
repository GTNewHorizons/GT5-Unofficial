package gregtech.common.render.shader;

import static com.gtnewhorizon.gtnhlib.bytebuf.MemoryUtilities.memAlloc;
import static com.gtnewhorizon.gtnhlib.bytebuf.MemoryUtilities.memFree;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.VertexBufferType;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormat;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormatElement;

public final class MeshBuilder implements QuadSink, AutoCloseable {

    private final String label;
    private final VertexFormat format;
    private final int stride;
    private final int capacity;
    private final int[] offsets;

    private ByteBuffer buffer;
    private int vertexCount;

    private final double[] quad = new double[20];
    private int quadVertices;

    private float instanceIndex;

    private MeshBuilder(String label, VertexFormat format, VertexAttribute[] layout, int vertexCapacity) {
        this.label = label;
        this.format = format;
        this.stride = format.getVertexSize();
        this.capacity = vertexCapacity;

        this.offsets = new int[VertexAttribute.values().length];
        Arrays.fill(offsets, -1);

        final VertexFormatElement[] elements = format.elementsArray;
        if (layout.length != elements.length) {
            throw new IllegalArgumentException(
                "layout declares " + layout.length + " attributes, format has " + elements.length);
        }

        int offset = 0;
        for (int i = 0; i < layout.length; i++) {
            if (elements[i].getByteSize() != layout[i].byteSize()) {
                throw new IllegalArgumentException(
                    layout[i] + " is "
                        + layout[i].byteSize()
                        + " bytes, format element "
                        + i
                        + " is "
                        + elements[i].getByteSize());
            }
            offsets[layout[i].ordinal()] = offset;
            offset += elements[i].getByteSize();
        }

        this.buffer = memAlloc(vertexCapacity * stride);
    }

    public static MeshBuilder of(ShaderHandle shader, int vertexCapacity) {
        if (shader.vertexFormat() == null) {
            throw new IllegalArgumentException(
                shader.id() + ": no vertex format; the shader failed to bake, or its recipe declared no attributes");
        }
        return new MeshBuilder(shader.id(), shader.vertexFormat(), shader.layout(), vertexCapacity);
    }

    public static MeshBuilder of(VertexFormat format, VertexAttribute[] layout, int vertexCapacity) {
        return new MeshBuilder("authored mesh", format, layout, vertexCapacity);
    }

    public MeshBuilder instanceIndex(int index) {
        this.instanceIndex = index;
        return this;
    }

    @Override
    public void vertex(double x, double y, double z, double u, double v) {
        final int base = quadVertices * 5;
        quad[base] = x;
        quad[base + 1] = y;
        quad[base + 2] = z;
        quad[base + 3] = u;
        quad[base + 4] = v;

        if (++quadVertices < 4) return;
        quadVertices = 0;

        writeQuadVertex(0);
        writeQuadVertex(1);
        writeQuadVertex(2);
        writeQuadVertex(0);
        writeQuadVertex(2);
        writeQuadVertex(3);
    }

    public void triangleVertex(double x, double y, double z, double u, double v) {
        if (quadVertices != 0) {
            throw new IllegalStateException("a quad is half-emitted; do not mix vertex() and triangleVertex()");
        }
        write(x, y, z, u, v);
    }

    private void writeQuadVertex(int index) {
        final int base = index * 5;
        write(quad[base], quad[base + 1], quad[base + 2], quad[base + 3], quad[base + 4]);
    }

    private void write(double x, double y, double z, double u, double v) {
        if (vertexCount == capacity) {
            throw new IllegalStateException(
                label + ": mesh holds " + capacity + " vertices, a further one was emitted");
        }
        final int base = vertexCount * stride;

        int offset = offsets[VertexAttribute.POSITION.ordinal()];
        if (offset >= 0) {
            buffer.putFloat(base + offset, (float) x);
            buffer.putFloat(base + offset + 4, (float) y);
            buffer.putFloat(base + offset + 8, (float) z);
        }

        offset = offsets[VertexAttribute.UV.ordinal()];
        if (offset >= 0) {
            buffer.putFloat(base + offset, (float) u);
            buffer.putFloat(base + offset + 4, (float) v);
        }

        offset = offsets[VertexAttribute.INSTANCE_INDEX.ordinal()];
        if (offset >= 0) {
            buffer.putFloat(base + offset, instanceIndex);
        }

        vertexCount++;
    }

    public IVertexArrayObject build() {
        if (quadVertices != 0) {
            throw new IllegalStateException(label + ": a quad is half-emitted, " + quadVertices + " of 4 vertices");
        }
        try {
            buffer.position(0)
                .limit(vertexCount * stride);
            return VertexBufferType.IMMUTABLE.allocate(format, GL11.GL_TRIANGLES, buffer, vertexCount);
        } finally {
            close();
        }
    }

    @Override
    public void close() {
        if (buffer != null) {
            memFree(buffer);
            buffer = null;
        }
    }
}
