package gregtech.api.render;

import cpw.mods.fml.common.registry.GameRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.OreDictionary;

public class AnimatedTooltipHandler {

    private static final Map<String, Supplier<String>> tooltipMap = new HashMap<>();

    public static final String BLACK,
            DARK_BLUE,
            DARK_GREEN,
            DARK_AQUA,
            DARK_RED,
            DARK_PURPLE,
            GOLD,
            GRAY,
            DARK_GRAY,
            BLUE,
            GREEN,
            AQUA,
            RED,
            LIGHT_PURPLE,
            YELLOW,
            WHITE,
            OBFUSCATED,
            BOLD,
            STRIKETHROUGH,
            UNDERLINE,
            ITALIC,
            RESET;

    public static final Supplier<String> NEW_LINE;

    /**
     * Helper method to concatenate multiple texts
     * @author glowredman
     */
    @SafeVarargs
    public static Supplier<String> chain(Supplier<String>... parts) {
        return () -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (Supplier<String> text : parts) {
                stringBuilder.append(text.get());
            }
            return stringBuilder.toString();
        };
    }

    /**
     * Helper method to create a static text
     * @author glowredman
     */
    public static Supplier<String> text(String text) {
        return () -> text;
    }

    /**
     * Helper method to create an animated text
     * <p>
     * Taken and adapted from <a href=https://github.com/GTNewHorizons/Avaritia/blob/7b7eaed652f6be320b10f33d8f8e6a04e66ca14f/src/main/java/fox/spiteful/avaritia/LudicrousText.java#L19>Avaritia</a>
     * @param text The text to be animated
     * @param posstep How many steps {@code formattingArray} is shifted each {@code delay}
     * @param delay How many milliseconds are between each shift of {@code formattingArray}
     * @param formattingArray An array of formatting codes. Each char of {@code text} will be prefixed by one entry, depending on {@code posstep} and {@code delay}. Wraps around, if shorter than {@code formattingArray}.
     * @author TTFTCUTS, glowredman
     */
    public static Supplier<String> animatedText(String text, int posstep, int delay, String... formattingArray) {
        if (text == null || text.isEmpty() || formattingArray == null || formattingArray.length == 0) return () -> "";

        final int finalDelay = delay > 0 ? delay : 1;
        final int finalPosstep = posstep >= 0 ? posstep : 0;

        return () -> {
            StringBuilder sb = new StringBuilder(text.length() * 3);
            int offset = (int) ((System.currentTimeMillis() / finalDelay) % formattingArray.length);
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int indexColorArray = (i * finalPosstep + formattingArray.length - offset) % formattingArray.length;
                sb.append(formattingArray[indexColorArray]);
                sb.append(c);
            }
            return sb.toString();
        };
    }

    /**
     * Add {@code tooltip} to all items with {@code oredictName}.
     * <br><b>Note:</b> The items must be registered to the {@link OreDictionary} when this method is called.
     * <br><b>Note:</b> Items with equal registry name and meta but different NBT are considered equal.
     * @author glowredman
     */
    public static void addOredictTooltip(String oredictName, Supplier<String> tooltip) {
        for (ItemStack item : OreDictionary.getOres(oredictName)) {
            addItemTooltip(item, tooltip);
        }
    }

    /**
     * Add {@code tooltip} to item specified by {@code modID}, {@code registryName} and {@code meta}.
     * <br><b>Note:</b> The item must be registered to the {@link GameRegistry} when this method is called.
     * <br><b>Note:</b> Items with equal registry name and meta but different NBT are considered equal.
     * <br><b>Note:</b> Using {@link OreDictionary#WILDCARD_VALUE} as {@code meta} is allowed.
     * @author glowredman
     */
    public static void addItemTooltip(String modID, String registryName, int meta, Supplier<String> tooltip) {
        Item item = GameRegistry.findItem(modID, registryName);
        if (item == null || meta < 0 || meta >= OreDictionary.WILDCARD_VALUE || tooltip == null) return;
        String identifier = item.getUnlocalizedName() + "@" + meta;
        tooltipMap.merge(identifier, tooltip, (a, b) -> chain(a, NEW_LINE, b));
    }

    /**
     * Add {@code tooltip} to {@code item}.
     * <br><b>Note:</b> Items with equal registry name and meta but different NBT are considered equal.
     * <br><b>Note:</b> Using {@link OreDictionary#WILDCARD_VALUE} as meta is allowed.
     * @author glowredman
     */
    public static void addItemTooltip(ItemStack item, Supplier<String> tooltip) {
        if (item == null || tooltip == null) return;
        String identifier = getStackIdentifier(item);
        tooltipMap.merge(identifier, tooltip, (a, b) -> chain(a, NEW_LINE, b));
    }

    public static Supplier<String> getTooltip(ItemStack item) {
        return tooltipMap.get(getStackIdentifier(item));
    }

    private static String getStackIdentifier(ItemStack stack) {
        return stack == null ? "" : stack.getItem().getUnlocalizedName() + "@" + stack.getItemDamage();
    }

    static {
        AQUA = EnumChatFormatting.AQUA.toString();
        BLACK = EnumChatFormatting.BLACK.toString();
        BLUE = EnumChatFormatting.BLUE.toString();
        BOLD = EnumChatFormatting.BOLD.toString();
        DARK_AQUA = EnumChatFormatting.DARK_AQUA.toString();
        DARK_BLUE = EnumChatFormatting.DARK_BLUE.toString();
        DARK_GRAY = EnumChatFormatting.DARK_GRAY.toString();
        DARK_GREEN = EnumChatFormatting.DARK_GREEN.toString();
        DARK_PURPLE = EnumChatFormatting.DARK_PURPLE.toString();
        DARK_RED = EnumChatFormatting.DARK_RED.toString();
        GOLD = EnumChatFormatting.GOLD.toString();
        GRAY = EnumChatFormatting.GRAY.toString();
        GREEN = EnumChatFormatting.GREEN.toString();
        ITALIC = EnumChatFormatting.ITALIC.toString();
        LIGHT_PURPLE = EnumChatFormatting.LIGHT_PURPLE.toString();
        OBFUSCATED = EnumChatFormatting.OBFUSCATED.toString();
        RED = EnumChatFormatting.RED.toString();
        RESET = EnumChatFormatting.RESET.toString();
        STRIKETHROUGH = EnumChatFormatting.STRIKETHROUGH.toString();
        UNDERLINE = EnumChatFormatting.UNDERLINE.toString();
        WHITE = EnumChatFormatting.WHITE.toString();
        YELLOW = EnumChatFormatting.YELLOW.toString();

        NEW_LINE = () -> "\n";
    }
}
