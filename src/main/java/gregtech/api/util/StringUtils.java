package gregtech.api.util;

import java.util.Arrays;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.GTValues;

public class StringUtils {

    // spotless:off
    private static final String[] rainbow = new String[] {
        EnumChatFormatting.DARK_RED.toString(),
        EnumChatFormatting.RED.toString(),
        EnumChatFormatting.GOLD.toString(),
        EnumChatFormatting.YELLOW.toString(),
        EnumChatFormatting.DARK_GREEN.toString(),
        EnumChatFormatting.GREEN.toString(),
        EnumChatFormatting.AQUA.toString(),
        EnumChatFormatting.DARK_AQUA.toString(),
        EnumChatFormatting.DARK_BLUE.toString(),
        EnumChatFormatting.BLUE.toString(),
        EnumChatFormatting.LIGHT_PURPLE.toString(),
        EnumChatFormatting.DARK_PURPLE.toString(),
        EnumChatFormatting.WHITE.toString(),
        EnumChatFormatting.GRAY.toString(),
        EnumChatFormatting.DARK_GRAY.toString(),
        EnumChatFormatting.BLACK.toString()};
    private static final char[] SUPER_SCRIPTS = new char[]{
        CustomGlyphs.SUPERSCRIPT0.charAt(0),
        CustomGlyphs.SUPERSCRIPT1.charAt(0),
        CustomGlyphs.SUPERSCRIPT2.charAt(0),
        CustomGlyphs.SUPERSCRIPT3.charAt(0),
        CustomGlyphs.SUPERSCRIPT4.charAt(0),
        CustomGlyphs.SUPERSCRIPT5.charAt(0),
        CustomGlyphs.SUPERSCRIPT6.charAt(0),
        CustomGlyphs.SUPERSCRIPT7.charAt(0),
        CustomGlyphs.SUPERSCRIPT8.charAt(0),
        CustomGlyphs.SUPERSCRIPT9.charAt(0)};
    private static final char[] SUB_SCRIPTS = new char[]{
        CustomGlyphs.SUBSCRIPT0.charAt(0),
        '\u2081',
        '\u2082',
        '\u2083',
        '\u2084',
        '\u2085',
        '\u2086',
        '\u2087',
        '\u2088',
        '\u2089'
    };
    // spotless:on

    public static @NotNull String applyRainbow(@NotNull String str, int offset, String additional) {
        StringBuilder final_string = new StringBuilder();
        int i = offset;
        for (char c : str.toCharArray()) final_string.append(rainbow[i++ % rainbow.length])
            .append(additional)
            .append(c);
        return final_string.toString();
    }

    public static @NotNull String applyRainbow(@NotNull String str, int offset) {
        return applyRainbow(str, offset, "");
    }

    public static @NotNull String applyRainbow(@NotNull String str) {
        return applyRainbow(str, 0, "");
    }

    public static @NotNull String voltageTooltipFormatted(int tier) {
        return GTValues.TIER_COLORS[tier] + GTValues.VN[tier] + EnumChatFormatting.GRAY;
    }

    public static String getRepetitionOf(char c, int length) {
        final char[] chars = new char[length];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    public static String getLocalizedString(final String key) {
        if (StatCollector.canTranslate(key)) {
            return StatCollector.translateToLocal(key);
        }
        return StatCollector.translateToFallback(key);
    }

    public static String superscript(String input) {
        final char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= '0' && c <= '9') {
                chars[i] = SUPER_SCRIPTS[c - '0'];
            }
        }
        return new String(chars);
    }

    public static String subscript(String input) {
        final char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= '0' && c <= '9') {
                chars[i] = SUB_SCRIPTS[c - '0'];
            } else if (c == '?') {
                chars[i] = CustomGlyphs.SUBSCRIPT_QUESTION_MARK.charAt(0);
            }
        }
        return new String(chars);
    }

    public static String capitalize(String string) {
        return org.apache.commons.lang3.StringUtils.capitalize(string);
    }

    public static String splitAndUppercase(String aInput) {
        String[] split = aInput.split("\\.");
        if (split.length == 0) {
            return aInput;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            s = sanitizeString(s);
            s = capitalize(s);
            sb.append(s);
        }
        return sb.toString();
    }

    public static long uppercaseCount(String input) {
        final char[] chars = input.toCharArray();
        int count = 0;
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                count++;
            }
        }
        return count;
    }

    public static String sanitizeStringKeepDashes(final String input) {
        final char[] chars = input.toCharArray();
        int i = 0;
        for (final char c : chars) {
            switch (c) {
                case ' ':
                case '~':
                case '?':
                case '!':
                case '@':
                case '#':
                case '$':
                case '%':
                case '^':
                case '&':
                case '*':
                case '(':
                case ')':
                case '{':
                case '}':
                case '[':
                case ']':
                    continue;
            }
            chars[i++] = c;
        }
        return new String(chars, 0, i);
    }

    public static String sanitizeString(final String input) {
        final char[] chars = input.toCharArray();
        int i = 0;
        for (final char c : chars) {
            switch (c) {
                case ' ':
                case '-':
                case '_':
                case '?':
                case '!':
                case '@':
                case '#':
                case '(':
                case ')':
                case '{':
                case '}':
                case '[':
                case ']':
                    continue;
            }
            chars[i++] = c;
        }
        return new String(chars, 0, i);
    }

    public static String sanitizeStringKeepBrackets(final String input) {
        final char[] chars = input.toCharArray();
        int i = 0;
        for (final char c : chars) {
            switch (c) {
                case ' ':
                case '-':
                case '_':
                case '?':
                case '!':
                case '@':
                case '#':
                    continue;
            }
            chars[i++] = c;
        }
        return new String(chars, 0, i);
    }

    public static String sanitizeStringKeepBracketsQuestion(final String input) {
        final char[] chars = input.toCharArray();
        int i = 0;
        for (final char c : chars) {
            switch (c) {
                case ' ':
                case '-':
                case '_':
                case '!':
                case '@':
                case '#':
                    continue;
            }
            chars[i++] = c;
        }
        return new String(chars, 0, i);
    }

    public static String formatList(String... strings) {
        if (strings == null || strings.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] != null) sb.append(strings[i]);
            if (i < strings.length - 2) sb.append(", ");
            else if (i == strings.length - 2) sb.append(" & ");
        }
        return sb.toString();
    }
}
