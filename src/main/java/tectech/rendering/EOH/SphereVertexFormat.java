package tectech.rendering.EOH;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.gtnewhorizon.gtnhlib.client.renderer.vertex.DefaultVertexFormat;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormat;

/**
 * VBO vertex layout: [pos.xyz][normal.xyz][uv.xy] all floats.
 * Stride = 8 floats = 32 bytes
 */
public final class SphereVertexFormat extends VertexFormat {

    private static final VertexFormat OLD_FLAGS0 = DefaultVertexFormat.ALL_FORMATS[0];

    public static final SphereVertexFormat INSTANCE = new SphereVertexFormat();

    public static final int FLOATS_PER_VERTEX = 8;
    public static final int STRIDE_BYTES = FLOATS_PER_VERTEX * 4;

    public static final long POS_OFFSET = 0L; // 3 floats
    public static final long NORMAL_OFFSET = 3L * 4L; // next 3 floats
    public static final long UV_OFFSET = 6L * 4L; // last 2 floats

    private SphereVertexFormat() {
        super();
        DefaultVertexFormat.ALL_FORMATS[0] = OLD_FLAGS0; // restore immediately
    }

    static {
        VertexFormat.registerSetupBufferStateOverride((format, basePtr) -> {
            if (format != INSTANCE) return false;

            int oldClientActive = GL11.glGetInteger(GL13.GL_CLIENT_ACTIVE_TEXTURE);
            GL13.glClientActiveTexture(GL13.GL_TEXTURE0);

            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

            GL11.glVertexPointer(3, GL11.GL_FLOAT, STRIDE_BYTES, basePtr + POS_OFFSET);
            GL11.glNormalPointer(GL11.GL_FLOAT, STRIDE_BYTES, basePtr + NORMAL_OFFSET);
            GL11.glTexCoordPointer(2, GL11.GL_FLOAT, STRIDE_BYTES, basePtr + UV_OFFSET);

            GL13.glClientActiveTexture(oldClientActive);
            return true;
        });

        VertexFormat.registerClearBufferStateOverride(format -> {
            if (format != INSTANCE) return false;

            int oldClientActive = GL11.glGetInteger(GL13.GL_CLIENT_ACTIVE_TEXTURE);
            GL13.glClientActiveTexture(GL13.GL_TEXTURE0);

            GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

            GL13.glClientActiveTexture(oldClientActive);
            return true;
        });
    }
}
