package gregtech.common.gui.modularui.widget.radialmenu;

public class RadialMenuTheme {

    private final float[] defaultBackground;
    private final float[] hoveredBackground;

    public RadialMenuTheme(final float[] defaultBackground, final float[] hoveredBackground) {
        this.defaultBackground = verifyFloatArray(defaultBackground);
        this.hoveredBackground = verifyFloatArray(hoveredBackground);
    }

    public float[] getDefaultBackground() {
        return defaultBackground;
    }

    public float[] getHoveredBackground() {
        return hoveredBackground;
    }

    private static void verifyFloat(float f) {
        if (f < 0f || f > 1.0f) {
            throw new IllegalArgumentException("Color value must be between 0 and 1");
        }
    }

    private static float[] verifyFloatArray(float[] f) {
        if (f.length != 4) {
            throw new IllegalArgumentException("Array passed to RadialMenuTheme must be exactly 4 items");
        }

        for (int i = 0; i < 4; i++) {
            verifyFloat(f[i]);
        }

        return f;
    }
}
