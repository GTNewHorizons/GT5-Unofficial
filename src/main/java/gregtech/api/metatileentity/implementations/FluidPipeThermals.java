package gregtech.api.metatileentity.implementations;

import org.jetbrains.annotations.Nullable;

import gregtech.api.util.GTUtility;

/**
 * Temperature scales, colours and damage for fluid pipes holding very hot or very cold fluids. A pipe's thermal level
 * is {@link #NONE}, 1 to {@link #HEAT_GLOW_LEVELS} for hot, or -1 to -{@link #FROST_LEVELS} for frosted.
 *
 * @see FluidPipeThermalState
 */
public final class FluidPipeThermals {

    /** Fluids above this temperature (K) burn on contact and give off heat haze. */
    public static final int HEAT_DAMAGE_TEMPERATURE = 320;
    /** Fluids below this temperature (K) freeze on contact and frost the pipe over. */
    public static final int FROST_DAMAGE_TEMPERATURE = 260;
    /** Fluids at or above this temperature (K) give the brightest glow. */
    public static final int MAX_GLOW_TEMPERATURE = 4000;
    /** Number of heat levels between {@link #HEAT_DAMAGE_TEMPERATURE} and {@link #MAX_GLOW_TEMPERATURE}. */
    public static final int HEAT_GLOW_LEVELS = 12;
    /** Fluids at or above this temperature (K) make the pipe glow. */
    public static final int GLOW_START_TEMPERATURE = 800;
    private static final int GLOW_START_LEVEL = getHeatGlowLevel(GLOW_START_TEMPERATURE);
    /** Glow colours from faintest (dull red) to brightest (yellow-white). */
    private static final short[][] HEAT_GLOW_COLORS = { { 150, 25, 8 }, { 225, 60, 12 }, { 255, 135, 30 },
        { 255, 215, 130 } };
    /** Pipe texture tint as it glows. Each stop is {heat, red, green, blue, strength}, heat and strength in 1/1000. */
    private static final int[][] HEAT_TINT_STOPS = { { 364, 150, 35, 15, 450 }, { 550, 225, 80, 20, 500 },
        { 1000, 255, 215, 130, 550 } };
    /** Block light at the faintest and brightest glow. */
    private static final int HEAT_LIGHT_MIN = 3, HEAT_LIGHT_MAX = 15;

    /** Fluids at or below this temperature (K) give the heaviest frost. */
    public static final int MIN_FROST_TEMPERATURE = 4;
    /** Number of frost levels between {@link #FROST_DAMAGE_TEMPERATURE} and {@link #MIN_FROST_TEMPERATURE}. */
    public static final int FROST_LEVELS = 4;
    /** Pipe texture tint as it frosts over, and its strength at the lightest and heaviest frost. */
    private static final short[] FROST_TINT_COLOR = { 215, 235, 255 };
    private static final float FROST_TINT_MIN = 0.15F, FROST_TINT_MAX = 0.55F;

    /** The level of a pipe with nothing too hot or too cold in it. */
    public static final int NONE = 0;

    private FluidPipeThermals() {}

    /** @return the thermal level for fluid at this temperature, or {@link #NONE} if neither hot nor cold enough */
    public static int getLevel(@Nullable Integer temperature) {
        if (temperature == null) return NONE;
        if (temperature > HEAT_DAMAGE_TEMPERATURE) return getHeatGlowLevel(temperature);
        if (temperature < FROST_DAMAGE_TEMPERATURE) return getFrostLevel(temperature);
        return NONE;
    }

    /** @return heat level, 1 to {@link #HEAT_GLOW_LEVELS}, on a log scale above {@link #HEAT_DAMAGE_TEMPERATURE} */
    public static int getHeatGlowLevel(int temperature) {
        final int levels = HEAT_GLOW_LEVELS;
        final double fraction = Math.log((double) temperature / HEAT_DAMAGE_TEMPERATURE)
            / Math.log((double) MAX_GLOW_TEMPERATURE / HEAT_DAMAGE_TEMPERATURE);
        return 1 + GTUtility.clamp((int) (fraction * levels), 0, levels - 1);
    }

    /** @return frost level, -1 to -{@link #FROST_LEVELS}, on a log scale below {@link #FROST_DAMAGE_TEMPERATURE} */
    public static int getFrostLevel(int temperature) {
        final double fraction = Math.log((double) FROST_DAMAGE_TEMPERATURE / Math.max(1, temperature))
            / Math.log((double) FROST_DAMAGE_TEMPERATURE / MIN_FROST_TEMPERATURE);
        return -(1 + GTUtility.clamp((int) (fraction * FROST_LEVELS), 0, FROST_LEVELS - 1));
    }

    /** @return base contact damage at this temperature, 0 if safe */
    public static float getContactDamage(int temperature) {
        if (temperature > HEAT_DAMAGE_TEMPERATURE) return (temperature - 300) / 50.0F;
        if (temperature < FROST_DAMAGE_TEMPERATURE) return (270 - temperature) / 25.0F;
        return 0;
    }

    /** @return glow strength from just above 0 (dull red) to 1 (yellow-white), or 0 if not hot enough to glow */
    public static float getGlowFraction(float level) {
        final int start = GLOW_START_LEVEL;
        return Math.max(0F, (Math.min(level, HEAT_GLOW_LEVELS) - start + 1) / (HEAT_GLOW_LEVELS - start + 1));
    }

    /** @return glow colour as {r, g, b} (0 to 255) for a thermal level */
    public static float[] getHeatGlowColor(float level) {
        final int start = GLOW_START_LEVEL;
        final float glow = (GTUtility.clamp(level, start, HEAT_GLOW_LEVELS) - start)
            / Math.max(1, HEAT_GLOW_LEVELS - start);
        final float position = glow * (HEAT_GLOW_COLORS.length - 1);
        final int index = Math.min((int) position, HEAT_GLOW_COLORS.length - 2);
        final float along = position - index;
        final float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            result[i] = HEAT_GLOW_COLORS[index][i]
                + (HEAT_GLOW_COLORS[index + 1][i] - HEAT_GLOW_COLORS[index][i]) * along;
        }
        return result;
    }

    /** @return heat from 0 to 1, or 0 if not hot */
    public static float getHeatFraction(float level) {
        if (level <= NONE) return 0F;
        return Math.max(0F, (Math.min(level, HEAT_GLOW_LEVELS) - 1) / (HEAT_GLOW_LEVELS - 1));
    }

    /** @return frost from 0 (none) to {@link #FROST_LEVELS} (heaviest) */
    public static float getFrostAmount(float level) {
        return level >= NONE ? 0F : Math.min(-level, FROST_LEVELS);
    }

    /** @return block light given off at this level, 0 if not glowing */
    public static int getLightLevel(int level) {
        final float glow = getGlowFraction(level);
        if (glow <= 0) return 0;
        return Math.round(HEAT_LIGHT_MIN + glow * (HEAT_LIGHT_MAX - HEAT_LIGHT_MIN));
    }

    /**
     * @return the pipe colour for this level. Pulled towards blue-white when frosted, towards the glow colour when
     *         glowing, otherwise unchanged
     */
    public static short[] getTint(short[] rgba, int level) {
        if (level < NONE) {
            final float frost = (float) (Math.min(-level, FROST_LEVELS) - 1) / (FROST_LEVELS - 1);
            final float tint = FROST_TINT_MIN + frost * (FROST_TINT_MAX - FROST_TINT_MIN);
            final short[] result = new short[] { 0, 0, 0, rgba[3] };
            for (int i = 0; i < 3; i++) {
                result[i] = (short) Math.round(rgba[i] + (FROST_TINT_COLOR[i] - rgba[i]) * tint);
            }
            return result;
        }
        if (getGlowFraction(level) <= 0) return rgba;

        final float heat = (float) (Math.min(level, HEAT_GLOW_LEVELS) - 1) / (HEAT_GLOW_LEVELS - 1);
        final int milli = Math.round(heat * 1000);
        int index = 0;
        while (index < HEAT_TINT_STOPS.length - 2 && milli > HEAT_TINT_STOPS[index + 1][0]) index++;
        final int[] from = HEAT_TINT_STOPS[index], to = HEAT_TINT_STOPS[index + 1];
        final float along = GTUtility.clamp(milli - from[0], 0, to[0] - from[0]) / (float) (to[0] - from[0]);
        final float tint = (from[4] + (to[4] - from[4]) * along) / 1000.0F;
        final short[] result = new short[] { 0, 0, 0, rgba[3] };
        for (int i = 0; i < 3; i++) {
            final float target = from[i + 1] + (to[i + 1] - from[i + 1]) * along;
            result[i] = (short) Math.round(rgba[i] + (target - rgba[i]) * tint);
        }
        return result;
    }
}
