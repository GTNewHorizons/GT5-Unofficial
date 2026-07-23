package gregtech.api.util.tooltip;

import static gregtech.api.util.tooltip.TooltipMarkupProcessor.INDENT;
import static gregtech.api.util.tooltip.TooltipMarkupProcessor.INDENT_MARK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import gregtech.api.util.GTUtility;

class DelayedTooltipLocalizationTest {

    @Test
    void resolvesRecursivelyNestedParameters() {
        Map<String, String> translations = new HashMap<>();
        translations.put("outer", "Outer [%s]");
        translations.put("middle", "Middle (%s)");
        translations.put("leaf", "Leaf");
        DelayedTooltipLocalization localization = localization(translations);

        String nested = GTUtility
            .nestParamsAlways("outer", GTUtility.nestParamsAlways("middle", GTUtility.nestParamsAlways("leaf")));

        assertEquals("Outer [Middle (Leaf)]", localization.resolveJsonLocalization(nested));
    }

    @Test
    void resolvesNestParamsAlwaysWithoutParameters() {
        Map<String, String> translations = new HashMap<>();
        translations.put("zero.parameters", "No parameters");

        assertEquals(
            "No parameters",
            DelayedTooltipLocalization
                .resolve(GTUtility.nestParamsAlways("zero.parameters"), translator(translations)));
    }

    @Test
    void leavesUntranslatedParametersLiteralAndNullParametersEmpty() {
        Map<String, String> translations = new HashMap<>();
        translations.put("outer", "%s|%s");

        String nested = GTUtility.nestParamsAlways("outer", "literal.fallback", null);

        assertEquals("literal.fallback|", localization(translations).resolveJsonLocalization(nested));
    }

    @Test
    void returnsMalformedPayloadUnchanged() {
        String malformed = "{\"k\":\"missing-params\"}";

        assertEquals(malformed, localization(new HashMap<>()).resolveJsonLocalization(malformed));
    }

    @Test
    void reportsMalformedPayloadWhenAnErrorHandlerIsConfigured() {
        String malformed = "{\"k\":\"missing-params\"}";
        Exception[] reported = new Exception[1];
        String[] reportedPayload = new String[1];
        DelayedTooltipLocalization localization = new DelayedTooltipLocalization(
            translator(new HashMap<>()),
            (payload, exception) -> {
                reportedPayload[0] = payload;
                reported[0] = exception;
            });

        assertEquals(malformed, localization.resolveJsonLocalization(malformed));
        assertEquals(malformed, reportedPayload[0]);
        assertSame(NullPointerException.class, reported[0].getClass());
    }

    @Test
    void observesTranslationMapChangesWithoutRebuildingResolver() {
        Map<String, String> translations = new HashMap<>();
        translations.put("resource.key", "English");
        DelayedTooltipLocalization localization = localization(translations);
        String nested = GTUtility.nestParamsAlways("resource.key");

        assertEquals("English", localization.resolveJsonLocalization(nested));

        translations.put("resource.key", "Second language");
        assertEquals("Second language", localization.resolveJsonLocalization(nested));
    }

    @Test
    void usesFallbackUntilCandidateTranslationLoads() {
        Map<String, String> translations = new HashMap<>();
        translations.put("fallback.key", "Fallback text");
        translations.put("candidate.param", "value");
        DelayedTooltipLocalization localization = localization(translations);
        String payload = DelayedTooltipLocalization.encode("candidate.key", "fallback.key", "candidate.param");

        assertEquals("Fallback text", localization.resolveJsonLocalization(payload));

        translations.put("candidate.key", "Candidate %s");
        assertEquals("Candidate value", localization.resolveJsonLocalization(payload));

        translations.put("fallback.key", "Changed fallback");
        assertEquals("Candidate value", localization.resolveJsonLocalization(payload));
    }

    @Test
    void fallbackSupportsNestedPayloadsAndLiterals() {
        Map<String, String> translations = new HashMap<>();
        translations.put("fallback.outer", "Nested %s");
        translations.put("fallback.param", "value");
        DelayedTooltipLocalization localization = localization(translations);
        String nestedFallback = GTUtility.nestParamsAlways("fallback.outer", "fallback.param");

        assertEquals(
            "Nested value",
            localization.resolveJsonLocalization(DelayedTooltipLocalization.encode("missing", nestedFallback)));
        assertEquals(
            "literal fallback",
            localization.resolveJsonLocalization(DelayedTooltipLocalization.encode("missing", "literal fallback")));
    }

    @Test
    void resolvesGeneratedJsonAndLiteralMarkersInTranslatedOrder() {
        Map<String, String> translations = new HashMap<>();
        translations.put("generated.value", "Localized");
        DelayedTooltipLocalization localization = localization(translations);
        String original = "before%%%" + GTUtility.nestParamsAlways("generated.value") + "%%%after%%%literal%%%";

        assertEquals(
            "Translated Localized / literal",
            localization.resolveGeneratedFormatMarkers(original, "Translated %s / %s"));
    }

    @Test
    void stripsGeneratedMarkersWithoutTreatingReplacementTextAsRegex() {
        assertEquals(
            "prefix $5\\value suffix",
            DelayedTooltipLocalization.stripGeneratedFormatMarkers("prefix %%%$5\\value%%% suffix"));
    }

    @Test
    void preservesIndentMarkerAndFormatsLiteralNewlines() {
        Map<String, String> translations = new HashMap<>();
        translations.put("indented", "First\\nSecond");
        DelayedTooltipLocalization localization = localization(translations);

        String resolved = localization.resolveJsonLocalization(GTUtility.nestParamsAlways(INDENT_MARK + "indented"));

        assertEquals(INDENT_MARK + "First\\nSecond", resolved);
        assertEquals(
            INDENT + "First\\n" + INDENT + "Second",
            TooltipMarkupProcessor.formatTranslatedLine("", resolved));
    }

    private static DelayedTooltipLocalization localization(Map<String, String> translations) {
        return new DelayedTooltipLocalization(translator(translations));
    }

    private static DelayedTooltipLocalization.Translator translator(Map<String, String> translations) {
        return (key, parameters) -> {
            String format = translations.getOrDefault(key, key);
            return parameters == null || parameters.length == 0 ? format : String.format(format, parameters);
        };
    }
}
