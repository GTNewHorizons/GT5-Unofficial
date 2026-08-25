package tectech.voidcraft.uss;

import gregtech.api.enums.Materials;

/**
 * Phase 4 pass 5.1: the display color of a USS planet — the average RGB of its three ore materials (from the GT
 * material registry) as an opaque ARGB int.
 *
 * <p>
 * Pass 9: the USS planets render as TEXTURED CUBES of their IORE dimension-display blocks (see
 * {@code EOHRenderingUtils#renderUSSOrbits}) — the type is its texture. This color is the FALLBACK tint: the
 * proven tinted-sphere fallback when a dimension key cannot resolve to a registered block (mod absent / renamed),
 * and it ties each planet to the ORES it yields (a copper-ore planet reads copper).
 *
 * <p>
 * Pure data: unit-testable in the bare JVM (the {@link Materials} enum is plain registry data; only
 * {@code Fluid}/{@code FluidStack} construction is unsafe there).
 */
public final class USSPlanetColor {

    private USSPlanetColor() {
        throw new AssertionError("Static helpers");
    }

    /**
     * @param definition a registered planet definition (null → white fallback)
     * @return opaque ARGB (alpha 0xFF); white ({@code 0xFFFFFFFF}) when none of the ore materials carries a usable
     *         color
     */
    public static int colorFor(USSPlanetDefinition definition) {
        if (definition == null) {
            return 0xFFFFFFFF;
        }
        int r = 0;
        int g = 0;
        int b = 0;
        int n = 0;
        for (USSPlanetOre ore : definition.getOres()) {
            Materials m = ore.getOreType();
            if (m == null || m == Materials._NULL) {
                continue;
            }
            short[] rgba = m.getRGBA();
            if (rgba == null || rgba.length < 3) {
                continue;
            }
            r += rgba[0] & 0xFF;
            g += rgba[1] & 0xFF;
            b += rgba[2] & 0xFF;
            n++;
        }
        if (n == 0) {
            return 0xFFFFFFFF;
        }
        return 0xFF000000 | (((r / n) & 0xFF) << 16) | (((g / n) & 0xFF) << 8) | ((b / n) & 0xFF);
    }
}
