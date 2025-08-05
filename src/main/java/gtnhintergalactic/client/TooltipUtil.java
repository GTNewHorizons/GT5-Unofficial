package gtnhintergalactic.client;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.*;

import java.util.function.Supplier;

import com.gtnewhorizon.gtnhlib.client.tooltip.LoreHolder;

import gregtech.api.enums.ItemList;

/**
 * Utility functions for tooltips
 *
 * @author minecraft7771
 */
public class TooltipUtil {

    @LoreHolder("gt.blockmachines.multimachine.ig.siphon.lore")
    public static String siphonLoreText;

    @LoreHolder("gt.blockmachines.multimachine.ig.elevator.lore")
    public static String elevatorLoreText;

    @LoreHolder("gt.blockmachines.multimachine.ig.dyson.lore")
    public static String dysonLoreText;

    /**
     * Initialize the tooltip utilities
     */
    public static void postInit() {
        Supplier<String> AUTHOR_GLOWREDMAN = chain(
            translatedText("ig.structure.author"),
            text(": "),
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

        addItemTooltip(ItemList.PlanetaryGasSiphonController.get(1), AUTHOR_GLOWREDMAN);
        addItemTooltip(ItemList.DysonSwarmController.get(1), AUTHOR_GLOWREDMAN);
    }
}
