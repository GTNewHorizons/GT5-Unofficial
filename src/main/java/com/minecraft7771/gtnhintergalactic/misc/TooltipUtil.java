package com.minecraft7771.gtnhintergalactic.misc;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.*;

import java.util.function.Supplier;

import com.minecraft7771.gtnhintergalactic.item.IGItems;

/**
 * Utility functions for tooltips
 *
 * @author minecraft7771
 */
public class TooltipUtil {

    /** Author string of glowredman */
    public static Supplier<String> AUTHOR_GLOWREDMAN;

    /**
     * Initialize the tooltip utilities
     */
    public static void postInit() {
        Supplier<String> AUTHOR_GLOWREDMAN = chain(
                translatedText("ig.structure.author"),
                text(" "),
                animatedText(
                        "glowredman",
                        1,
                        300,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE,
                        DARK_BLUE + OBFUSCATED));

        addItemTooltip(IGItems.PlanetaryGasSiphon, AUTHOR_GLOWREDMAN);
    }
}
