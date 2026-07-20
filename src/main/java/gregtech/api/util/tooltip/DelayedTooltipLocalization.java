package gregtech.api.util.tooltip;

import static gregtech.api.util.tooltip.TooltipMarkupProcessor.INDENT_MARK;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/** Resolves delayed machine-tooltip localization without depending on Minecraft's localization state. */
public final class DelayedTooltipLocalization {

    private static final Pattern GENERATED_FORMAT_MARKER = Pattern.compile("%%%(.*?)%%%");
    private static final String JSON_PREFIX = "{\"k\":";

    private final Translator translator;
    private final BiConsumer<String, Exception> errorHandler;

    public DelayedTooltipLocalization(@Nonnull Translator translator) {
        this(translator, (payload, exception) -> {});
    }

    public DelayedTooltipLocalization(@Nonnull Translator translator,
        @Nonnull BiConsumer<String, Exception> errorHandler) {
        this.translator = translator;
        this.errorHandler = errorHandler;
    }

    /** Resolves one delayed-localization payload with the supplied translation source. */
    @Nonnull
    public static String resolve(@Nonnull String json, @Nonnull Translator translator) {
        return new DelayedTooltipLocalization(translator).resolveJsonLocalization(json);
    }

    /**
     * Encodes a candidate localization key and a value to use until that key is available. Parameters use the same
     * delayed nested-payload convention as {@code GTUtility.nestParamsAlways}.
     */
    @Nonnull
    public static String encode(@Nonnull String key, @Nonnull String fallback, Object... parameters) {
        JsonObject json = new JsonObject();
        json.addProperty("k", key);

        JsonArray paramsArray = new JsonArray();
        if (parameters != null) {
            for (Object parameter : parameters) {
                if (parameter == null) {
                    paramsArray.add(JsonNull.INSTANCE);
                } else {
                    paramsArray.add(new JsonPrimitive(parameter.toString()));
                }
            }
        }
        json.add("p", paramsArray);
        json.addProperty("f", fallback);
        return json.toString();
    }

    /** Resolves a JSON payload created by {@code GTUtility.nestParamsAlways} or {@link #encode}. */
    @Nonnull
    public String resolveJsonLocalization(@Nonnull String json) {
        try {
            JsonObject obj = new JsonParser().parse(json)
                .getAsJsonObject();

            String key = obj.get("k")
                .getAsString();
            JsonArray paramsArray = obj.getAsJsonArray("p");

            boolean hasIndent = key.startsWith(INDENT_MARK);
            String cleanKey = removeStart(key, INDENT_MARK);

            List<Object> resolvedParams = new ArrayList<>();
            for (JsonElement elem : paramsArray) {
                resolvedParams.add(resolveValue(elem));
            }

            String result = translator.translate(cleanKey, resolvedParams.toArray());
            if (result.equals(cleanKey) && obj.has("f")) {
                result = resolveValue(obj.get("f"));
            }
            if (result.isEmpty()) return key;
            return hasIndent && !result.startsWith(INDENT_MARK) ? INDENT_MARK + result : result;
        } catch (Exception exception) {
            errorHandler.accept(json, exception);
            return json;
        }
    }

    @Nonnull
    public String resolveGeneratedFormatMarkers(@Nullable String originalDescription,
        @Nonnull String translatedDescription) {
        if (originalDescription == null || originalDescription.isEmpty() || !originalDescription.contains("%%%")) {
            return stripGeneratedFormatMarkers(translatedDescription);
        }

        String resolvedDescription = translatedDescription;
        Matcher matcher = GENERATED_FORMAT_MARKER.matcher(originalDescription);
        while (matcher.find()) {
            int placeholderIndex = resolvedDescription.indexOf("%s");
            if (placeholderIndex < 0) break;
            resolvedDescription = resolvedDescription.substring(0, placeholderIndex)
                + resolveGeneratedFormatPayload(matcher.group(1))
                + resolvedDescription.substring(placeholderIndex + 2);
        }
        return stripGeneratedFormatMarkers(resolvedDescription);
    }

    @Nonnull
    public String resolveGeneratedFormatPayload(@Nonnull String payload) {
        if (isJsonLocalization(payload)) {
            return stripGeneratedFormatMarkers(resolveJsonLocalization(payload));
        }
        return stripGeneratedFormatMarkers(payload);
    }

    @Nonnull
    public static String stripGeneratedFormatMarkers(@Nonnull String text) {
        if (!text.contains("%%%")) return text;

        Matcher matcher = GENERATED_FORMAT_MARKER.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(matcher.group(1)));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static boolean isJsonLocalization(String text) {
        return text.startsWith(JSON_PREFIX);
    }

    @Nonnull
    private String resolveValue(@Nonnull JsonElement value) {
        if (value.isJsonNull()) return "";

        String text = value.getAsString();
        if (isJsonLocalization(text)) {
            return stripGeneratedFormatMarkers(resolveJsonLocalization(text));
        }

        String translated = translator.translate(text);
        return stripGeneratedFormatMarkers(translated.equals(text) ? text : translated);
    }

    @FunctionalInterface
    public interface Translator {

        @Nonnull
        String translate(@Nonnull String key, Object... parameters);
    }
}
