package tectech.rendering.EOH;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.renderer.vbo.VertexBuffer;

/**
 * Caches unit-radius sphere meshes (radius=1). Render-time scaling handles any radius.
 */
public final class SphereVBOCache {

    private static final Map<Long, VertexBuffer> CACHE = new HashMap<Long, VertexBuffer>();

    private SphereVBOCache() {}

    public static VertexBuffer getOrCreate(int slices, int stacks) {
        long key = (((long) slices) << 32) | (stacks & 0xffffffffL);

        VertexBuffer vbo = CACHE.get(key);
        if (vbo != null && vbo.getId() >= 0) return vbo;

        vbo = buildSphereVBO(slices, stacks);
        CACHE.put(key, vbo);
        return vbo;
    }

    private static VertexBuffer buildSphereVBO(int slices, int stacks) {
        int vertexCount = stacks * slices * 6;

        ByteBuffer buf = ByteBuffer.allocateDirect(vertexCount * SphereVertexFormat.STRIDE_BYTES)
            .order(ByteOrder.nativeOrder());

        // Build a unit sphere (radius 1). Normals point inward.
        for (int i = 0; i < stacks; i++) {
            double v0 = (double) i / (double) stacks;
            double v1 = (double) (i + 1) / (double) stacks;

            double phi0 = Math.PI / 2.0 - i * Math.PI / stacks;
            double phi1 = Math.PI / 2.0 - (i + 1) * Math.PI / stacks;

            double y0 = Math.sin(phi0);
            double y1 = Math.sin(phi1);

            double r0 = Math.cos(phi0);
            double r1 = Math.cos(phi1);

            for (int j = 0; j < slices; j++) {
                double u0 = (double) j / (double) slices;
                double u1 = (double) (j + 1) / (double) slices;

                // Flip U so the texture matches "inside-view" usage
                float uu0 = (float) (1.0 - u0);
                float uu1 = (float) (1.0 - u1);

                double th0 = j * 2.0 * Math.PI / slices;
                double th1 = (j + 1) * 2.0 * Math.PI / slices;

                double x00 = r0 * Math.cos(th0);
                double z00 = r0 * Math.sin(th0);

                double x10 = r1 * Math.cos(th0);
                double z10 = r1 * Math.sin(th0);

                double x11 = r1 * Math.cos(th1);
                double z11 = r1 * Math.sin(th1);

                double x01 = r0 * Math.cos(th1);
                double z01 = r0 * Math.sin(th1);

                // Quad (v0/u0 -> v1/u1) as 2 triangles:
                // tri1: (x00,y0,z00) (x10,y1,z10) (x11,y1,z11)
                putVertex(buf, (float) x00, (float) y0, (float) z00, uu0, (float) v0);
                putVertex(buf, (float) x10, (float) y1, (float) z10, uu0, (float) v1);
                putVertex(buf, (float) x11, (float) y1, (float) z11, uu1, (float) v1);

                // tri2: (x00,y0,z00) (x11,y1,z11) (x01,y0,z01)
                putVertex(buf, (float) x00, (float) y0, (float) z00, uu0, (float) v0);
                putVertex(buf, (float) x11, (float) y1, (float) z11, uu1, (float) v1);
                putVertex(buf, (float) x01, (float) y0, (float) z01, uu1, (float) v0);
            }
        }

        buf.flip();

        VertexBuffer vbo = new VertexBuffer(SphereVertexFormat.INSTANCE, GL11.GL_TRIANGLES);
        vbo.upload(buf, vertexCount);
        return vbo;
    }

    private static void putVertex(ByteBuffer buf, float x, float y, float z, float u, float v) {
        // pos
        buf.putFloat(x);
        buf.putFloat(y);
        buf.putFloat(z);

        // inward normal
        buf.putFloat(-x);
        buf.putFloat(-y);
        buf.putFloat(-z);

        // uv
        buf.putFloat(u);
        buf.putFloat(v);
    }
}
