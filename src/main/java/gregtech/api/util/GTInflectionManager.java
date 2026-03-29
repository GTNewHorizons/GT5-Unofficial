package gregtech.api.util;

import static gregtech.GTMod.GT_FML_LOGGER;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.enums.Mods;

public final class GTInflectionManager {

    private static final Type MAP_TYPE = new TypeToken<Map<String, LinkedHashMap<String, String>>>() {}.getType();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(?<!%)%(\\d+\\$)?s(?:\\{([^}]+)})?");
    private static final Gson GSON = new Gson();
    private static volatile Map<String, LinkedHashMap<Pattern, String>> INFLECTION_MAP = new ConcurrentHashMap<>();
    private static final boolean IS_SERVER = FMLCommonHandler.instance()
        .getEffectiveSide()
        .isServer();

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

            Map<String, LinkedHashMap<Pattern, String>> inflectionTempMap = new ConcurrentHashMap<>();

            for (Map.Entry<String, LinkedHashMap<String, String>> entry : fileData.entrySet()) {
                String typeKey = entry.getKey();
                LinkedHashMap<String, String> stringRules = entry.getValue();
                LinkedHashMap<Pattern, String> compiledRules = new LinkedHashMap<>();

                for (Map.Entry<String, String> rule : stringRules.entrySet()) {
                    try {
                        String pattern = rule.getKey();
                        if (!pattern.startsWith("^")) pattern = "^" + pattern;
                        if (!pattern.endsWith("$")) pattern = pattern + "$";
                        compiledRules.put(Pattern.compile(pattern), rule.getValue());
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
        }
    }

    private static String escape(String input) {
        return input.replace("\\{", "{")
            .replace("\\}", "}");
    }

    public static String formatInflection(String inputKey, String... formatterKey) {
        final String input = StatCollector.translateToLocal(inputKey);
        if (!input.contains("s{")) {
            return String.format(
                escape(input),
                Arrays.stream(formatterKey)
                    .map(StatCollector::translateToLocal)
                    .toArray(Object[]::new));
        }
        if (IS_SERVER) {
            return String.format(
                escape(input.replaceAll(PLACEHOLDER_PATTERN.pattern(), "%$1s")),
                Arrays.stream(formatterKey)
                    .map(StatCollector::translateToLocal)
                    .toArray(Object[]::new));
        }

        List<String> params = new LinkedList<>();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
        boolean hasImplicit = false;
        boolean hasExplicit = false;
        int sequentialPos = 0;

        while (matcher.find()) {
            String indexPart = matcher.group(1);
            String inflectionKey = matcher.group(2);

            int targetPos;
            if (indexPart != null) {
                targetPos = Integer.parseInt(indexPart.substring(0, indexPart.length() - 1)) - 1;
                hasExplicit = true;
            } else {
                targetPos = sequentialPos++;
                hasImplicit = true;
            }

            if (targetPos >= formatterKey.length) {
                GT_FML_LOGGER.warn(
                    "Inflection Format Error: Placeholder index {} out of bounds for inputKey '{}' (only {} arguments provided)",
                    targetPos + 1,
                    inputKey,
                    formatterKey.length);
                return String.format(
                    escape(input),
                    Arrays.stream(formatterKey)
                        .map(StatCollector::translateToLocal)
                        .toArray(Object[]::new));
            }

            if (hasImplicit && hasExplicit) {
                GT_FML_LOGGER.warn(
                    "Inflection Format Error: Mixed implicit (%s) and explicit (%2$s) placeholders in inflection string: '{}'",
                    inputKey);
                return String.format(
                    escape(input),
                    Arrays.stream(formatterKey)
                        .map(StatCollector::translateToLocal)
                        .toArray(Object[]::new));
            }

            params.add(getInflection(formatterKey[targetPos], inflectionKey));
        }

        Object[] formattedArgs = params.toArray();

        String cleanedPattern = escape(input.replaceAll(PLACEHOLDER_PATTERN.pattern(), "%s"));
        return String.format(cleanedPattern, formattedArgs);
    }

    private static String getInflection(String formatterKey, String key) {
        String word = StatCollector.translateToLocal(formatterKey);
        if (key == null || key.isEmpty()) {
            return word;
        }
        String specialCaseKey = formatterKey + "." + key;
        if (StatCollector.canTranslate(specialCaseKey)) {
            return StatCollector.translateToLocal(specialCaseKey);
        }

        LinkedHashMap<Pattern, String> rules = INFLECTION_MAP.get(key);
        if (rules == null || rules.isEmpty()) {
            return word;
        }

        for (Map.Entry<Pattern, String> rule : rules.entrySet()) {
            Matcher m = rule.getKey()
                .matcher(word);
            if (m.matches()) {
                return m.replaceFirst(rule.getValue());
            }
        }
        return word;
    }
}
