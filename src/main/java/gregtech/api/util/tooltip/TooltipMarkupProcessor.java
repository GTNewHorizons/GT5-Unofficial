package gregtech.api.util.tooltip;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;

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

        splitLinesByMark(tooltips, LINE_BREAK);
        extractSeparatorMarks(tooltips);
        applySeparatorLines(tooltips);
    }

    public static void splitLinesByMark(List<String> list, String mark) {
        if (list == null) return;

        for (int i = 0; i < list.size();) {
            String str = list.get(i);
            if (str.contains(mark)) {
                String[] parts = str.split(Pattern.quote(mark));
                list.remove(i);
                list.addAll(i, Arrays.asList(parts));
            } else {
                i++;
            }
        }
    }

    private static void extractSeparatorMarks(List<String> list) {
        extractSeparatorMark(list, SEPARATOR_MARK);
        extractSeparatorMark(list, STRUCTURE_SEPARATOR_MARK);
    }

    private static void extractSeparatorMark(List<String> list, String mark) {
        for (int i = 0; i < list.size();) {
            String str = list.get(i);
            int markIdx = str.indexOf(mark);

            if (markIdx == -1) {
                i++;
                continue;
            }

            String colorCode = "";
            boolean hasColorCode = false;
            if (markIdx >= 2 && str.charAt(markIdx - 2) == '§') {
                colorCode = str.substring(markIdx - 2, markIdx);
                hasColorCode = true;
            }

            List<String> parts = new ArrayList<>();

            if (markIdx > 0) {
                int endIdx = hasColorCode ? markIdx - 2 : markIdx;
                String before = str.substring(0, endIdx);
                if (!before.trim()
                    .isEmpty()) {
                    parts.add(before);
                }
            }

            parts.add(colorCode + mark);

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

        int dashCount = calculateDashCount(tooltips);
        SeparatorStyle sepStyle = SeparatorStyle.forSeparator(GTMod.proxy.separatorStyle);
        SeparatorStyle finStyle = SeparatorStyle.forFinisher(GTMod.proxy.tooltipFinisherStyle);

        String baseSep = sepStyle.generate(EnumChatFormatting.GRAY, dashCount, "");
        String finisher = finStyle.generate(EnumChatFormatting.GRAY, dashCount, "");

        int structLen = translateToLocal("GT5U.MBTT.Structure.SeeStructure").replaceAll("§[0-9a-fk-or]", "")
            .length() * 7 / 10;
        String structSep = sepStyle.generate(EnumChatFormatting.GRAY, structLen, INDENT);

        for (int i = 0; i < tooltips.size(); i++) {
            String line = tooltips.get(i);

            if (line.contains(SEPARATOR_MARK)) {
                tooltips.set(i, replaceSeparator(line, SEPARATOR_MARK, baseSep));
            }
            if (line.contains(STRUCTURE_SEPARATOR_MARK)) {
                tooltips.set(i, replaceSeparator(line, STRUCTURE_SEPARATOR_MARK, structSep));
            }
            if (line.contains(FINISHER_MARK)) {
                tooltips.set(i, replaceSeparator(line, FINISHER_MARK, finisher));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private static int calculateDashCount(List<String> tooltips) {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc == null || mc.fontRenderer == null) return 50;

            FontRenderer fr = mc.fontRenderer;
            int maxWidth = 0;
            for (String line : tooltips) {
                int w = fr.getStringWidth(line);
                if (w > maxWidth) maxWidth = w;
            }

            int dashWidth = fr.getStringWidth("-");
            return dashWidth > 0 ? maxWidth / dashWidth : 50;
        } catch (Exception e) {
            return 50;
        }
    }

    private static String replaceSeparator(String line, String mark, String separator) {
        int markIdx = line.indexOf(mark);
        if (markIdx == -1) return line;

        String colorCode = "";
        if (markIdx >= 2 && line.charAt(markIdx - 2) == '§') {
            colorCode = line.substring(markIdx - 2, markIdx);
            markIdx -= 2;
        }

        String result = line.substring(0, markIdx);
        if (!colorCode.isEmpty()) {
            result += colorCode + separator.replaceFirst("§[0-9a-fk-or]", "");
        } else {
            result += separator;
        }

        return result;
    }

    public static String formatTranslatedLine(String original, String translated) {
        boolean hasIndent = original.startsWith(INDENT_MARK) || translated.startsWith(INDENT_MARK);
        String cleanTranslated = translated.startsWith(INDENT_MARK) ? translated.substring(INDENT_MARK.length())
            : translated;
        String indent = hasIndent ? INDENT : "";
        return indent + cleanTranslated.replace(LINE_BREAK, hasIndent ? LINE_BREAK + INDENT : LINE_BREAK);
    }
}
