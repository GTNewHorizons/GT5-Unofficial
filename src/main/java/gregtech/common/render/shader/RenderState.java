package gregtech.common.render.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public final class RenderState {

    private RenderState() {}

    public static void restore(int cap, boolean enabled) {
        if (enabled) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }
    }

    public static long savedBlendFunc() {
        return packBlendFunc(
            GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB),
            GL11.glGetInteger(GL14.GL_BLEND_DST_RGB),
            GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA),
            GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA));
    }

    public static void restoreBlendFunc(long saved) {
        GL14.glBlendFuncSeparate(
            (int) (saved >>> 48) & 0xFFFF,
            (int) (saved >>> 32) & 0xFFFF,
            (int) (saved >>> 16) & 0xFFFF,
            (int) saved & 0xFFFF);
    }

    private static long packBlendFunc(int srcRgb, int dstRgb, int srcAlpha, int dstAlpha) {
        return (long) (srcRgb & 0xFFFF) << 48 | (long) (dstRgb & 0xFFFF) << 32
            | (long) (srcAlpha & 0xFFFF) << 16
            | (dstAlpha & 0xFFFF);
    }
}
