package gregtech.client;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.AQUA;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BLUE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.DARK_BLUE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.DARK_GRAY;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.DARK_GREEN;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.DARK_PURPLE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.DARK_RED;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GREEN;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.LIGHT_PURPLE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.OBFUSCATED;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.RED;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.UNDERLINE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.WHITE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.YELLOW;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.addItemTooltip;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.animatedText;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.chain;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.text;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

public class GTTooltipHandler {

    public static void registerTieredTooltip(ItemStack item, Tier tier) {
        addItemTooltip(item, tier.tooltip);
    }

    public enum Tier {

        ULV(text(WHITE + "ULV-Tier")),
        LV(text(GRAY + "LV-Tier")),
        MV(text(GOLD + "MV-Tier")),
        HV(text(YELLOW + "HV-Tier")),
        EV(text(DARK_GRAY + "EV-Tier")),
        IV(text(GREEN + "IV-Tier")),
        LuV(text(LIGHT_PURPLE + "LuV-Tier")),
        ZPM(text(AQUA + "ZPM-Tier")),
        UV(text(DARK_GREEN + "UV-Tier")),
        UHV(text(DARK_RED + "UHV-Tier")),
        UEV(text(DARK_PURPLE + "UEV-Tier")),
        UIV(text(DARK_BLUE + BOLD + "UIV-Tier")),
        UMV(text(RED + BOLD + UNDERLINE + "UMV-Tier")),
        UXV(animatedText("UXV-Tier", 1, 100, DARK_PURPLE + BOLD + UNDERLINE, DARK_RED + UNDERLINE + BOLD)),
        MAX(chain(
            animatedText(
                "X",
                1,
                100,
                LIGHT_PURPLE + BOLD + OBFUSCATED + UNDERLINE,
                RED + BOLD + OBFUSCATED + UNDERLINE,
                GOLD + OBFUSCATED + BOLD + UNDERLINE,
                YELLOW + OBFUSCATED + BOLD + UNDERLINE,
                GREEN + OBFUSCATED + BOLD + UNDERLINE,
                AQUA + OBFUSCATED + BOLD + UNDERLINE,
                BLUE + OBFUSCATED + BOLD + UNDERLINE),
            animatedText(
                "MAX-Tier",
                1,
                100,
                RED + BOLD + UNDERLINE,
                GOLD + BOLD + UNDERLINE,
                YELLOW + BOLD + UNDERLINE,
                GREEN + BOLD + UNDERLINE,
                AQUA + BOLD + UNDERLINE,
                BLUE + BOLD + UNDERLINE,
                LIGHT_PURPLE + BOLD + UNDERLINE),
            animatedText(
                "X",
                1,
                100,
                GOLD + OBFUSCATED + BOLD + UNDERLINE,
                YELLOW + OBFUSCATED + BOLD + UNDERLINE,
                GREEN + OBFUSCATED + BOLD + UNDERLINE,
                AQUA + OBFUSCATED + BOLD + UNDERLINE,
                BLUE + OBFUSCATED + BOLD + UNDERLINE,
                LIGHT_PURPLE + OBFUSCATED + BOLD + UNDERLINE,
                RED + OBFUSCATED + BOLD + UNDERLINE))),
        ERV(chain(
            animatedText(
                "E",
                1,
                100,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_GRAY + OBFUSCATED + BOLD + UNDERLINE),
            animatedText(
                "R",
                1,
                100,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_GRAY + OBFUSCATED + BOLD + UNDERLINE),
            animatedText(
                "R",
                1,
                200,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_GRAY + OBFUSCATED + BOLD + UNDERLINE),
            animatedText(
                "O",
                1,
                150,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_GRAY + OBFUSCATED + BOLD + UNDERLINE),
            animatedText(
                "R",
                1,
                150,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_GRAY + OBFUSCATED + BOLD + UNDERLINE),
            animatedText(
                "-",
                1,
                150,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_GRAY + OBFUSCATED + BOLD + UNDERLINE),
            animatedText(
                "T",
                1,
                200,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_GRAY + OBFUSCATED + BOLD + UNDERLINE),
            animatedText(
                "i",
                1,
                100,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_GRAY + OBFUSCATED + BOLD + UNDERLINE),
            animatedText(
                "e",
                1,
                150,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_GRAY + OBFUSCATED + BOLD + UNDERLINE),
            animatedText(
                "r",
                1,
                100,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_RED + BOLD + UNDERLINE,
                DARK_GRAY + OBFUSCATED + BOLD + UNDERLINE)));

        private final Supplier<String> tooltip;

        Tier(Supplier<String> tooltip) {
            this.tooltip = tooltip;
        }
    }
}
