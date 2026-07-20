package gregtech.api.util;

import static gregtech.api.util.tooltip.TooltipMarkupProcessor.INDENT_MARK;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumChatFormatting;

import org.junit.jupiter.api.Test;

import gregtech.api.util.tooltip.DelayedTooltipLocalization;

@SuppressWarnings("deprecation")
class MultiblockTooltipBuilderTest {

    @Test
    void publicStructureApisProduceRecursivelyResolvablePayloads() {
        String[] payloads = buildStructurePayloads();
        Map<String, String> translations = translations("A");

        assertEquals(5, payloads.length);
        assertTrue(
            Arrays.stream(payloads)
                .allMatch(line -> line.startsWith("{\"k\":")));
        assertArrayEquals(expected("A"), resolve(payloads, translations));
    }

    @Test
    void samePayloadsUseUpdatedNameLocationAndTierTranslations() {
        String[] payloads = buildStructurePayloads();
        Map<String, String> translations = translations("A");

        assertArrayEquals(expected("A"), resolve(payloads, translations));

        translations.putAll(translations("B"));
        assertArrayEquals(expected("B"), resolve(payloads, translations));
    }

    @Test
    void casingOverloadsPreserveTheirLegacyLayoutAndColors() {
        String[] payloads = new MultiblockTooltipBuilder().addCasing("1+", "test.casing", true)
            .addCasingInfo("2", "test.casing", false)
            .addCasingInfoExactlyColored("test.casing", EnumChatFormatting.AQUA, 3, EnumChatFormatting.RED, true)
            .addCasingInfoMin("test.casing", 4, true)
            .addCasingInfoMinColored("test.casing", EnumChatFormatting.GREEN, 5, EnumChatFormatting.BLUE, true)
            .addCasingInfoRange("test.casing", 6, 7, true)
            .addCasingInfoRangeColored(
                "test.casing",
                EnumChatFormatting.LIGHT_PURPLE,
                8,
                9,
                EnumChatFormatting.DARK_AQUA,
                false)
            .getStructureInformation();
        Map<String, String> translations = translations("A");

        assertArrayEquals(
            new String[] { EnumChatFormatting.GOLD + "1+ " + EnumChatFormatting.WHITE + "Casing A [Tier A]",
                EnumChatFormatting.GOLD + "2 " + EnumChatFormatting.WHITE + "Casing A",
                EnumChatFormatting.RED + "3 " + EnumChatFormatting.AQUA + "Casing A [Tier A]",
                EnumChatFormatting.GOLD + "4+ " + EnumChatFormatting.WHITE + "Casing A [Tier A]",
                EnumChatFormatting.BLUE + "5 " + EnumChatFormatting.GREEN + "Casing A [Minimum A] [Tier A]",
                EnumChatFormatting.GOLD + "6-7 " + EnumChatFormatting.WHITE + "Casing A [Tier A]",
                EnumChatFormatting.DARK_AQUA + "8-9 " + EnumChatFormatting.LIGHT_PURPLE + "Casing A" },
            resolve(payloads, translations));
    }

    @Test
    void controllerCandidateKeyFallsBackAndCanAppearAfterReload() {
        String payload = new MultiblockTooltipBuilder().addController("front_center")
            .getStructureInformation()[0];
        Map<String, String> translations = translations("A");
        translations.put("front_center", "Fallback A");

        assertEquals(INDENT_MARK + "Controller A @ Fallback A", resolve(new String[] { payload }, translations)[0]);

        translations.put("GT5U.MBTT.Controller", "Controller B");
        translations.put("gt.mbtt.structure.front_center", "Candidate B");
        assertEquals(INDENT_MARK + "Controller B @ Candidate B", resolve(new String[] { payload }, translations)[0]);
    }

    private static String[] buildStructurePayloads() {
        return new MultiblockTooltipBuilder().addCasing("4", "test.casing", true)
            .addCasingInfoMinColored("test.minimum_casing", EnumChatFormatting.AQUA, 3, EnumChatFormatting.RED, true)
            .addMiscHatch("2", "test.misc_hatch", "test.misc_location", 1)
            .addStructurePart("test.structure_part", "<hint>", 1, 3)
            .addMaintenanceHatch("test.legacy_location")
            .getStructureInformation();
    }

    private static Map<String, String> translations(String version) {
        Map<String, String> translations = new HashMap<>();
        translations.put("GT5U.MBTT.Casing.Format", "%1$s%2$s %3$s%4$s%5$s%6$s");
        translations.put("GT5U.MBTT.Casing.MinimumSuffix", " [Minimum %s]");
        translations.put("GT5U.MBTT.Casing.TieredSuffix", " [%s]");
        translations.put("GT5U.MBTT.Minimum", version);
        translations.put("GT5U.MBTT.Tiered", "Tier " + version);
        translations.put(EnumChatFormatting.YELLOW + "%s " + EnumChatFormatting.WHITE + "%s", "%s %s");
        translations.put("GT5U.MBTT.PartInfo", "%s @ %s");
        translations.put("GT5U.MBTT.PartHintDesc", "Hint %s");
        translations.put("GT5U.MBTT.ListSeparator", " / ");
        translations.put("test.casing", "Casing " + version);
        translations.put("test.minimum_casing", "Minimum Casing " + version);
        translations.put("test.misc_hatch", "Misc " + version);
        translations.put("test.misc_location", "Location " + version);
        translations.put("test.structure_part", "Part " + version);
        translations.put("GT5U.MBTT.MaintenanceHatch", "Maintenance " + version);
        translations.put("GT5U.MBTT.Controller", "Controller " + version);
        translations.put("test.legacy_location", "Legacy " + version);
        return translations;
    }

    private static String[] expected(String version) {
        return new String[] {
            EnumChatFormatting.GOLD + "4 " + EnumChatFormatting.WHITE + "Casing " + version + " [Tier " + version + "]",
            EnumChatFormatting.RED + "3 "
                + EnumChatFormatting.AQUA
                + "Minimum Casing "
                + version
                + " [Minimum "
                + version
                + "] [Tier "
                + version
                + "]",
            INDENT_MARK + "2 Misc " + version + " @ Location " + version,
            INDENT_MARK + "Part " + version + " @ Hint 1 / 3",
            INDENT_MARK + "Maintenance " + version + " @ Legacy " + version };
    }

    private static String[] resolve(String[] payloads, Map<String, String> translations) {
        DelayedTooltipLocalization localization = new DelayedTooltipLocalization((key, parameters) -> {
            String format = translations.getOrDefault(key, key);
            return parameters == null || parameters.length == 0 ? format : String.format(format, parameters);
        });
        return Arrays.stream(payloads)
            .map(localization::resolveJsonLocalization)
            .toArray(String[]::new);
    }
}
