package gregtech.client;

import static gregtech.api.render.AnimatedTooltipHandler.*;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Arrays;
import java.util.function.Supplier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

public class GT_TooltipEventHandler {

    /*
     * What you can do:
     * - Add simple, unformatted text.
     * - Add formatted text.
     *   Note: you can chain formatting codes but the color must be used first:
     *   e.g. BLACK + ITALIC will create black, italic text but ITALIC + BLACK will only create black text.
     * - add animated text
     * - chain multiple static and/or animated text together using chain()
     *   (Although chaining only static text together is pointless, text() is already able to do that)
     * - add multiple lines by using NEW_LINE (either as String or as Supplier<String>)
     *   Note: formatting only applies for one line
     *   Note: having a NEW_LINE in animated text results in "skipping" of one formatting since NEW_LINE
     *         counts as character but is not displayed
     *
     * Note: adding a tooltip to an item multiple times also creates multiple lines (in the same order they were added)
     *
     * What you cannot do:
     * - add a tooltip depending on NBT, tooltips are only mapped to name and meta
     *
     * This method is executed on postInit
     */
    public static void init() {

        // Tooltip tiers for ALL items given the appropriate tier oredict tag.
        addOredictTooltip("ULV", text(WHITE + "ULV-Tier"));
        addOredictTooltip("LV", text(GRAY + "LV-Tier"));
        addOredictTooltip("MV", text(GOLD + "MV-Tier"));
        addOredictTooltip("HV", text(YELLOW + "HV-Tier"));
        addOredictTooltip("EV", text(DARK_GRAY + "EV-Tier"));
        addOredictTooltip("IV", text(GREEN + "IV-Tier"));
        addOredictTooltip("LuV", text(LIGHT_PURPLE + "LuV-Tier"));
        addOredictTooltip("ZPM", text(AQUA + "ZPM-Tier"));
        addOredictTooltip("UV", text(DARK_GREEN + "UV-Tier"));
        addOredictTooltip("UHV", text(DARK_RED + "UHV-Tier"));
        addOredictTooltip("UEV", text(DARK_PURPLE + "UEV-Tier"));
        addOredictTooltip("UIV", text(DARK_BLUE + BOLD + "UIV-Tier"));
        addOredictTooltip("UMV", text(RED + BOLD + UNDERLINE + "UMV-Tier"));
        addOredictTooltip(
                "UXV", animatedText("UXV-Tier", 1, 100, DARK_PURPLE + BOLD + UNDERLINE, DARK_RED + UNDERLINE + BOLD));
        addOredictTooltip(
                "MAX",
                chain(
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
                                RED + OBFUSCATED + BOLD + UNDERLINE)));
    }

    @SubscribeEvent
    public void renderTooltip(ItemTooltipEvent event) {
        Supplier<String> tooltip = getTooltip(event.itemStack);
        if (tooltip != null) {
            String text = tooltip.get();
            if (!text.isEmpty()) {
                event.toolTip.addAll(Arrays.asList(text.split("\n")));
            }
        }
        ItemStack wildcardItem = event.itemStack.copy();
        wildcardItem.setItemDamage(OreDictionary.WILDCARD_VALUE);
        tooltip = getTooltip(wildcardItem);
        if (tooltip != null) {
            String text = tooltip.get();
            if (!text.isEmpty()) {
                event.toolTip.addAll(Arrays.asList(text.split("\n")));
            }
        }
    }
}
