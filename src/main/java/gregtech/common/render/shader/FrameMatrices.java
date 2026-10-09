package gregtech.common.render.shader;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

final class FrameMatrices {

    private static final Matrix4f projection = new Matrix4f();
    private static final Matrix4f modelView = new Matrix4f();
    private static final Matrix4f viewProjection = new Matrix4f();
    private static final Matrix4f mvp = new Matrix4f();

    private static final float[] cachedProjection = new float[16];
    private static final float[] cachedModelView = new float[16];
    private static boolean haveViewProjection;

    private static final FloatBuffer scratch = BufferUtils.createFloatBuffer(16);

    private FrameMatrices() {}

    static void uploadMVP(int location, Matrix4fc model) {
        refreshViewProjection();
        upload(location, viewProjection.mul(model, mvp));
    }

    static void upload(int location, Matrix4fc matrix) {
        scratch.clear();
        matrix.get(scratch);
        GL20.glUniformMatrix4(location, false, scratch);
    }

    private static void refreshViewProjection() {
        scratch.clear();
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, scratch);
        boolean moved = copyIfChanged(scratch, cachedProjection);

        scratch.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, scratch);
        moved |= copyIfChanged(scratch, cachedModelView);

        if (!moved && haveViewProjection) return;

        projection.set(cachedProjection)
            .mul(modelView.set(cachedModelView), viewProjection);
        haveViewProjection = true;
    }

    private static boolean copyIfChanged(FloatBuffer source, float[] cache) {
        boolean changed = false;
        for (int i = 0; i < 16; i++) {
            final float value = source.get(i);
            if (value != cache[i]) {
                cache[i] = value;
                changed = true;
            }
        }
        return changed;
    }
}
