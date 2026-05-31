package gregtech.api.util;

import java.awt.Color;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;

/**
 * Client-tick-driven, synchronized animation colors.
 * <p>
 * All cycles read GregTech's shared animation clock
 * ({@link gregtech.common.GTClient#getAnimationRenderTicks()}), which is advanced by {@link TickEvent.ClientTickEvent}.
 * Because every effect derives its phase from that single counter, any two effects using the
 * same period stay in phase with each other and with all other animations reading the clock.
 */
@SideOnly(Side.CLIENT)
public final class GTAnimatedColor {

    public static final int RAINBOW_CYCLE_TICKS = 160;
    public static final int GLOW_CYCLE_TICKS = 80;

    private static final int GLOW_FRAMES = 200;

    private GTAnimatedColor() {}

    /**
     * @return the position in {@code [0, 1)} within a synced cycle of {@code periodTicks} length,
     *         derived from the shared client-tick animation clock.
     */
    public static float getCyclePosition(int periodTicks) {
        return (GTMod.clientProxy()
            .getAnimationRenderTicks() % periodTicks) / periodTicks;
    }

    /** @return a fully-saturated rainbow color looping every {@link #RAINBOW_CYCLE_TICKS} ticks. */
    public static int getRainbowColor() {
        return Color.HSBtoRGB(getCyclePosition(RAINBOW_CYCLE_TICKS), 1, 1);
    }

    /**
     * Mild glow pulse, looping every {@link #GLOW_CYCLE_TICKS} ticks.
     *
     * @param base RGBA array; only the first three (R, G, B) channels are used.
     * @return the pulsed color packed as {@code 0xRRGGBB}.
     */
    public static int getGlowColor(short[] base) {
        int frame = (int) (getCyclePosition(GLOW_CYCLE_TICKS) * GLOW_FRAMES);
        int value = frame < 50 ? frame + 1 : frame < 100 ? 50 : frame < 150 ? 149 - frame : 0;
        int r = GTUtility.clamp(base[0] + value, 0, 255);
        int g = GTUtility.clamp(base[1] + value, 0, 255);
        int b = GTUtility.clamp(base[2] + value, 0, 255);
        return (r << 16) | (g << 8) | b;
    }
}
