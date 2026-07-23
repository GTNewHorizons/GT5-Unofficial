package gregtech.api.util.tooltip;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.util.GTUtility;

public class TooltipMarkupProcessor {

    public static final String INDENT_MARK = "<INDENT>";
    public static final String INDENT = "   ";
    public static final String SEPARATOR_MARK = "<SEPARATOR>";
    public static final String STRUCTURE_SEPARATOR_MARK = "<S_SEPARATOR>";
    public static final String FINISHER_MARK = "<FINISHER>";
    public static final String LINE_BREAK = "\\n";

    @SideOnly(Side.CLIENT)
    public static void processTooltips(List<String> tooltips) {
        if (tooltips == null || tooltips.isEmpty()) return;

        GTUtility.splitNewlines(tooltips);
        extractSeparatorMarks(tooltips);
        applySeparatorLines(tooltips);
    }

    private static void extractSeparatorMarks(List<String> list) {
        extractSeparatorMark(list, SEPARATOR_MARK);
        extractSeparatorMark(list, STRUCTURE_SEPARATOR_MARK);
        extractSeparatorMark(list, FINISHER_MARK);
    }

    private static void extractSeparatorMark(List<String> list, String mark) {
        for (int i = 0; i < list.size();) {
            String str = list.get(i);
            int markIdx = str.indexOf(mark);

            if (markIdx == -1) {
                i++;
                continue;
            }

            int formattingStart = findFormattingStart(str, markIdx);
            String formattingCodes = str.substring(formattingStart, markIdx);

            List<String> parts = new ArrayList<>();

            if (markIdx > 0) {
                String before = str.substring(0, formattingStart);
                if (!before.trim()
                    .isEmpty()) {
                    parts.add(before);
                }
            }

            parts.add(formattingCodes + mark);

            if (markIdx + mark.length() < str.length()) {
                String after = str.substring(markIdx + mark.length());
                if (mark.equals(STRUCTURE_SEPARATOR_MARK) && !after.startsWith(INDENT)) {
                    after = INDENT + after;
                }
                parts.add(after);
            }

            list.remove(i);
            list.addAll(i, parts);
            if (parts.size() <= 1) i++;
        }
    }

    @SideOnly(Side.CLIENT)
    private static void applySeparatorLines(List<String> tooltips) {
        if (tooltips == null || tooltips.isEmpty()) return;

        SeparatorStyle sepStyle = SeparatorStyle.forSeparator(GTMod.proxy.separatorStyle);
        SeparatorStyle finStyle = SeparatorStyle.forFinisher(GTMod.proxy.tooltipFinisherStyle);

        int dashCount = calculateDashCount(tooltips, 0, 50);
        String baseSep = sepStyle.generate(EnumChatFormatting.GRAY, dashCount, "");
        String finisher = finStyle.generate(EnumChatFormatting.GRAY, dashCount, "");

        int structureDashCount = calculateDashCount(tooltips, getStringWidth(INDENT), 30);
        String structSep = sepStyle.generate(EnumChatFormatting.GRAY, structureDashCount, INDENT);

        for (int i = 0; i < tooltips.size();) {
            String line = tooltips.get(i);
            boolean separatorMarker = containsSeparatorMark(line);

            if (line.contains(SEPARATOR_MARK)) {
                line = replaceSeparator(line, SEPARATOR_MARK, baseSep);
            }
            if (line.contains(STRUCTURE_SEPARATOR_MARK)) {
                line = replaceSeparator(line, STRUCTURE_SEPARATOR_MARK, structSep);
            }
            if (line.contains(FINISHER_MARK)) {
                line = replaceSeparator(line, FINISHER_MARK, finisher);
            }

            if (separatorMarker && line.isEmpty()) {
                tooltips.remove(i);
            } else {
                tooltips.set(i, line);
                i++;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private static int calculateDashCount(List<String> tooltips, int indentWidth, int fallback) {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc == null || mc.fontRenderer == null) return fallback;

            FontRenderer fr = mc.fontRenderer;
            int maxWidth = 0;
            for (String line : tooltips) {
                if (containsSeparatorMark(line)) continue;
                int w = fr.getStringWidth(line);
                if (w > maxWidth) maxWidth = w;
            }

            int dashWidth = fr.getStringWidth("-");
            if (dashWidth <= 0) return fallback;
            int contentWidth = Math.max(0, maxWidth - indentWidth);
            return contentWidth > 0 ? (contentWidth + dashWidth - 1) / dashWidth : fallback;
        } catch (Exception e) {
            return fallback;
        }
    }

    @SideOnly(Side.CLIENT)
    private static int getStringWidth(String text) {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            return mc == null || mc.fontRenderer == null ? 0 : mc.fontRenderer.getStringWidth(text);
        } catch (Exception e) {
            return 0;
        }
    }

    private static boolean containsSeparatorMark(String line) {
        return line.contains(SEPARATOR_MARK) || line.contains(STRUCTURE_SEPARATOR_MARK) || line.contains(FINISHER_MARK);
    }

    private static String replaceSeparator(String line, String mark, String separator) {
        int markIdx = line.indexOf(mark);
        if (markIdx == -1) return line;

        int formattingStart = findFormattingStart(line, markIdx);
        String formattingCodes = line.substring(formattingStart, markIdx);
        return line.substring(0, formattingStart) + applyFormatting(separator, formattingCodes);
    }

    private static int findFormattingStart(String line, int endIndex) {
        int formattingStart = endIndex;
        while (formattingStart >= 2 && line.charAt(formattingStart - 2) == '§'
            && isFormattingCode(line.charAt(formattingStart - 1))) {
            formattingStart -= 2;
        }
        return formattingStart;
    }

    private static boolean isFormattingCode(char code) {
        char normalized = Character.toLowerCase(code);
        return normalized >= '0' && normalized <= '9' || normalized >= 'a' && normalized <= 'f'
            || normalized >= 'k' && normalized <= 'o'
            || normalized == 'r';
    }

    private static boolean hasColorOrReset(String formattingCodes) {
        for (int i = 1; i < formattingCodes.length(); i += 2) {
            char code = Character.toLowerCase(formattingCodes.charAt(i));
            if (code >= '0' && code <= '9' || code >= 'a' && code <= 'f' || code == 'r') return true;
        }
        return false;
    }

    private static String applyFormatting(String separator, String formattingCodes) {
        if (separator.isEmpty() || formattingCodes.isEmpty()) return separator;

        int indentLength = 0;
        while (indentLength < separator.length() && separator.charAt(indentLength) == ' ') {
            indentLength++;
        }

        String indent = separator.substring(0, indentLength);
        String body = separator.substring(indentLength);
        if (hasColorOrReset(formattingCodes)) {
            if (body.length() >= 2 && body.charAt(0) == '§' && isColorCode(body.charAt(1))) {
                body = body.substring(2);
            }
            return indent + formattingCodes + body;
        }

        if (body.length() >= 2 && body.charAt(0) == '§' && isColorCode(body.charAt(1))) {
            return indent + body.substring(0, 2) + formattingCodes + body.substring(2);
        }
        return indent + formattingCodes + body;
    }

    private static boolean isColorCode(char code) {
        char normalized = Character.toLowerCase(code);
        return normalized >= '0' && normalized <= '9' || normalized >= 'a' && normalized <= 'f';
    }

    public static String formatTranslatedLine(String original, String translated) {
        boolean hasIndent = original.startsWith(INDENT_MARK) || translated.startsWith(INDENT_MARK);
        String cleanTranslated = translated.startsWith(INDENT_MARK) ? translated.substring(INDENT_MARK.length())
            : translated;
        String indent = hasIndent ? INDENT : "";
        return indent + cleanTranslated.replace(LINE_BREAK, hasIndent ? LINE_BREAK + INDENT : LINE_BREAK);
    }
}
