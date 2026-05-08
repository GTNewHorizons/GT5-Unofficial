package gregtech.api.util;

import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.util.GTLanguageManager.LOCALE;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.github.bsideup.jabel.Desugar;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.enums.Mods;

public final class GTInflectionManager {

    private static final Type MAP_TYPE = new TypeToken<Map<String, LinkedHashMap<String, String>>>() {}.getType();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(?<!%)%(?:(\\d+)\\$)?s(?:\\{([^}]+)})?");
    private static final Gson GSON = new Gson();
    private static volatile Map<String, List<Rule>> INFLECTION_MAP = new HashMap<>();
    private static final boolean IS_SERVER = FMLCommonHandler.instance()
        .getEffectiveSide()
        .isServer();
    private static final Pattern CAPITALIZE_WORDS_PATTERN = Pattern.compile("\\p{L}+");

    private GTInflectionManager() {}

    public static void loadInflectionJson(String userLang) {
        if (IS_SERVER) {
            return;
        }
        Minecraft minecraft = Minecraft.getMinecraft();
        final ResourceLocation json = new ResourceLocation(Mods.GregTech.ID, "inflection/" + userLang + ".json");
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                minecraft.getResourceManager()
                    .getResource(json)
                    .getInputStream()))) {
            Map<String, LinkedHashMap<String, String>> fileData = GSON.fromJson(reader, MAP_TYPE);

            if (fileData == null || fileData.isEmpty()) {
                GT_FML_LOGGER.warn("Inflection file is empty or does not conform to the format");
                return;
            }

            Map<String, List<Rule>> inflectionTempMap = new HashMap<>();

            for (Entry<String, LinkedHashMap<String, String>> entry : fileData.entrySet()) {
                String typeKey = entry.getKey();
                LinkedHashMap<String, String> stringRules = entry.getValue();
                List<Rule> compiledRules = new ArrayList<>();

                for (Entry<String, String> rule : stringRules.entrySet()) {
                    try {
                        String pattern = rule.getKey();
                        if (!pattern.startsWith("^")) pattern = "^" + pattern;
                        if (!pattern.endsWith("$")) pattern = pattern + "$";
                        compiledRules.add(new Rule(Pattern.compile(pattern), rule.getValue()));
                    } catch (Exception e) {
                        GT_FML_LOGGER.warn("Invalid regex pattern '{}' for type '{}'", rule.getKey(), typeKey, e);
                    }
                }
                inflectionTempMap.put(typeKey, compiledRules);
            }
            INFLECTION_MAP = inflectionTempMap;
            GT_FML_LOGGER.info("Loaded inflection rules for language: {}", userLang);
        } catch (FileNotFoundException ignored) {

        } catch (IOException e) {
            GT_FML_LOGGER.warn("Failed to load inflection file: {}", json, e);
        } catch (JsonParseException e) {
            GT_FML_LOGGER.warn("Successfully found the file: {}, but an error occurred.", json, e);
        }
    }

    private static String unescape(String input) {
        return input.replace("\\{", "{")
            .replace("\\}", "}");
    }

    /**
     * Formats a localized string with inflection (word form changes).
     * <p>
     * Template syntax: <code>%s{key}</code> or <code>%2$s{key}</code>, where {@code key} is the inflection rule type
     * (e.g., plural).<br>
     * Inflection rules are loaded from <code>inflection/&lt;lang&gt;.json</code> and apply regular expression matching
     * and replacement.<br>
     * Special cases can be defined via the localization key <code>formatterKey.key</code> to override the default
     * inflection result.<br>
     * Supports a slash ({@code /}) in {@code rule key} for multiple replacement:<br>
     * {@code part1/part2/part3} will execute the {@code part1} rule, {@code part2} rule, and {@code part3} rule in
     * sequence.<br>
     * See ({@code assets/gregtech/inflection/en_US.example.json} for an example JSON format.<br>
     * See also: {@link <a href="https://gist.github.com/iouter/efcfb45dbc337e5c45f1c177df3cbaa0">document in gist</a>}
     * <p>
     * On the client side, words are replaced according to the inflection rules; on the server side, inflection markers
     * are automatically removed (no inflection applied).<br>
     * If implicit (<code>%s</code>) and explicit (<code>%2$s</code>) placeholders are mixed, or an index is out of
     * bounds, a warning is logged and a fallback result without inflection is returned via {@link String#format}.<br>
     * JSON content that does not conform to the expected format is ignored and logged.
     * <p>
     *
     * @param inputKey     localization key for the template string
     * @param formatterKey varargs of localization keys corresponding to placeholders
     * @return the formatted string
     */
    public static String formatInflection(String inputKey, String... formatterKey) {
        final String input = StatCollector.translateToLocal(inputKey);
        if (!input.contains("s{") || IS_SERVER) {
            try {
                return String.format(
                    unescape(input),
                    Arrays.stream(formatterKey)
                        .map(StatCollector::translateToLocal)
                        .toArray(Object[]::new));
            } catch (IllegalFormatException e) {
                return "Format Error: " + input;
            }
        }

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
        boolean hasImplicit = false;
        boolean hasExplicit = false;
        int sequentialPos = 0;
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String indexPart = matcher.group(1);
            String inflectionKey = matcher.group(2);

            int targetPos;
            if (indexPart != null) {
                try {
                    targetPos = Integer.parseInt(indexPart) - 1;
                } catch (NumberFormatException e) {
                    return "Inflection Format Error: " + input;
                }
                hasExplicit = true;
            } else {
                targetPos = sequentialPos++;
                hasImplicit = true;
            }

            if (targetPos >= formatterKey.length || (hasImplicit && hasExplicit)) {
                return "Inflection Format Error: " + input;
            }

            matcher.appendReplacement(sb, getInflection(formatterKey[targetPos], inflectionKey));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String getInflection(String formatterKey, String key) {
        String word = StatCollector.translateToLocal(formatterKey);
        if (key == null || key.isEmpty()) {
            return word;
        }
        String specialCaseKey = formatterKey;
        for (String targetRule : Splitter.on("/")
            .omitEmptyStrings()
            .split(key)) {
            specialCaseKey += "." + targetRule;
            if (StatCollector.canTranslate(specialCaseKey)) {
                word = StatCollector.translateToLocal(specialCaseKey);
                continue;
            }
            switch (targetRule) {
                case "lowercase" -> {
                    word = word.toLowerCase(LOCALE);
                    continue;
                }
                case "uppercase" -> {
                    word = word.toUpperCase(LOCALE);
                    continue;
                }
                case "capitalize_first" -> {
                    word = word.substring(0, 1)
                        .toUpperCase(LOCALE)
                        + word.substring(1)
                            .toLowerCase(LOCALE);
                    continue;
                }
                case "capitalize_words" -> {
                    Matcher matcher = CAPITALIZE_WORDS_PATTERN.matcher(word);
                    StringBuffer sb = new StringBuffer(word.length());
                    while (matcher.find()) {
                        String matchedWord = matcher.group();
                        String capitalized = matchedWord.substring(0, 1)
                            .toUpperCase(LOCALE)
                            + matchedWord.substring(1)
                                .toLowerCase(LOCALE);
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(capitalized));
                    }
                    matcher.appendTail(sb);
                    word = sb.toString();
                    continue;
                }
            }
            List<Rule> rules = INFLECTION_MAP.get(targetRule);
            if (rules == null || rules.isEmpty()) {
                continue;
            }
            for (Rule rule : rules) {
                Matcher m = rule.pattern()
                    .matcher(word);
                if (m.matches()) {
                    word = m.replaceFirst(rule.replacement());
                    break;
                }
            }
        }
        return word;
    }

    @Desugar
    private record Rule(Pattern pattern, String replacement) {}
}
