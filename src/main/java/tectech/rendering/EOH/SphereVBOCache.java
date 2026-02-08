package tectech.rendering.EOH;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.renderer.DirectTessellator;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.VertexBufferType;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

/**
 * Caches unit-radius sphere meshes (radius=1). Render-time scaling handles any radius.
 */
public final class SphereVBOCache {

    private static final Map<Long, IVertexArrayObject> CACHE = new Long2ObjectOpenHashMap<>();

    private SphereVBOCache() {}

    public static IVertexArrayObject getOrCreate(int slices, int stacks) {
        long key = (((long) slices) << 32) | (stacks & 0xffffffffL);

        IVertexArrayObject vao = CACHE.get(key);
        if (vao != null) {
            return vao;
        }

        vao = buildSphereVBO(slices, stacks);
        CACHE.put(key, vao);
        return vao;
    }

    private static IVertexArrayObject buildSphereVBO(int slices, int stacks) {
        DirectTessellator tessellator = DirectTessellator.startCapturing();
        tessellator.startDrawing(GL11.GL_TRIANGLES);

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

                // tri 1
                addVertex(tessellator, x00, y0, z00, uu0, v0);
                addVertex(tessellator, x10, y1, z10, uu0, v1);
                addVertex(tessellator, x11, y1, z11, uu1, v1);

                // tri 2
                addVertex(tessellator, x00, y0, z00, uu0, v0);
                addVertex(tessellator, x11, y1, z11, uu1, v1);
                addVertex(tessellator, x01, y0, z01, uu1, v0);
            }
        }

        return DirectTessellator.stopCapturingToVBO(VertexBufferType.IMMUTABLE);
    }

    private static void addVertex(DirectTessellator tess, double x, double y, double z, double u, double v) {
        // inward normal
        tess.setNormal((float) -x, (float) -y, (float) -z);
        tess.addVertexWithUV(x, y, z, u, v);
    }

}
