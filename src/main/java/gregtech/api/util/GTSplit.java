package gregtech.api.util;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import net.minecraft.util.StatCollector;

/**
 * To use uniform line breaker in lang files.
 */
public class GTSplit {

    /**
     * the Line Breaker.
     */
    public static final String LB = "\\n";

    /**
     * split the text
     */
    public static String[] split(String s) {
        return s.split(Pattern.quote(LB));
    }

    /**
     * split the formatted text.
     */
    public static String[] splitFormatted(String s, Object... objects) {
        return split(String.format(s, objects));
    }

    /**
     * split the translated text.
     */
    public static String[] splitLocalized(String key) {
        return split(StatCollector.translateToLocal(key));
    }

    /**
     * split the translated and formatted text.
     */
    public static String[] splitLocalizedFormatted(String key, Object... objects) {
        return split(StatCollector.translateToLocalFormatted(key, objects));
    }

    /**
     * add a line before and after the translated text.
     *
     * @apiNote prefix and suffix should NOT is Hardcoded.
     */
    public static String[] splitLocalizedWithWarped(String key, String prefix, String suffix) {
        return Stream.concat(Stream.concat(Stream.of(prefix), Arrays.stream(splitLocalized(key))), Stream.of(suffix))
            .toArray(String[]::new);
    }

    /**
     * add a line before and after the translated and formatted text.
     *
     * @apiNote prefix and suffix should NOT is Hardcoded.
     */
    public static String[] splitLocalizedFormattedWithWarped(String key, String prefix, String suffix,
        Object... objects) {
        return Stream
            .concat(
                Stream.concat(Stream.of(prefix), Arrays.stream(splitLocalizedFormatted(key, objects))),
                Stream.of(suffix))
            .toArray(String[]::new);
    }

    /**
     * add a line after the translated text.
     *
     * @apiNote suffix should NOT is Hardcoded.
     */
    public static String[] splitLocalizedWithSuffix(String key, String suffix) {
        return Stream.concat(Arrays.stream(splitLocalized(key)), Stream.of(suffix))
            .toArray(String[]::new);
    }

    /**
     * add a line after the translated and formatted text.
     *
     * @apiNote suffix should NOT is Hardcoded.
     */
    public static String[] splitLocalizedFormattedWithSuffix(String key, String suffix, Object... objects) {
        return Stream.concat(Arrays.stream(splitLocalizedFormatted(key, objects)), Stream.of(suffix))
            .toArray(String[]::new);
    }

    /**
     * add a line before the translated text.
     *
     * @apiNote prefix should NOT is Hardcoded.
     */
    public static String[] splitLocalizedWithPrefix(String key, String prefix) {
        return Stream.concat(Stream.of(prefix), Arrays.stream(splitLocalized(key)))
            .toArray(String[]::new);
    }

    /**
     * add a line before the translated and formatted text.
     *
     * @apiNote prefix should NOT is Hardcoded.
     */
    public static String[] splitLocalizedFormattedWithPrefix(String key, String prefix, Object... objects) {
        return Stream.concat(Stream.of(prefix), Arrays.stream(splitLocalizedFormatted(key, objects)))
            .toArray(String[]::new);
    }

    /**
     * add lines before and after the translated text.
     *
     * @apiNote prefixes and suffixes should NOT is Hardcoded.
     */
    public static String[] splitLocalizedWithWarped(String key, String[] prefixes, String[] suffixes) {
        return Stream
            .concat(Stream.concat(Arrays.stream(prefixes), Arrays.stream(splitLocalized(key))), Arrays.stream(suffixes))
            .toArray(String[]::new);
    }

    /**
     * add lines before and after the translated and formatted text.
     *
     * @apiNote prefixes and suffixes should NOT is Hardcoded.
     */
    public static String[] splitLocalizedFormattedWithWarped(String key, String[] prefixes, String[] suffixes,
        Object... objects) {
        return Stream
            .concat(
                Stream.concat(Arrays.stream(prefixes), Arrays.stream(splitLocalizedFormatted(key, objects))),
                Arrays.stream(suffixes))
            .toArray(String[]::new);
    }

    /**
     * add lines after the translated text.
     *
     * @apiNote suffixes should NOT is Hardcoded.
     */
    public static String[] splitLocalizedWithSuffix(String key, String[] suffixes) {
        return Stream.concat(Arrays.stream(splitLocalized(key)), Arrays.stream(suffixes))
            .toArray(String[]::new);
    }

    /**
     * add lines after the translated and formatted text.
     *
     * @apiNote suffixes should NOT is Hardcoded.
     */
    public static String[] splitLocalizedFormattedWithSuffix(String key, String[] suffixes, Object... objects) {
        return Stream.concat(Arrays.stream(splitLocalizedFormatted(key, objects)), Arrays.stream(suffixes))
            .toArray(String[]::new);
    }

    /**
     * add lines before the translated text.
     *
     * @apiNote prefixes should NOT is Hardcoded.
     */
    public static String[] splitLocalizedWithPrefix(String key, String[] prefixes) {
        return Stream.concat(Arrays.stream(prefixes), Arrays.stream(splitLocalized(key)))
            .toArray(String[]::new);
    }

    /**
     * add lines before the translated and formatted text.
     *
     * @apiNote prefixes should NOT is Hardcoded.
     */
    public static String[] splitLocalizedFormattedWithPrefix(String key, String[] prefixes, Object... objects) {
        return Stream.concat(Arrays.stream(prefixes), Arrays.stream(splitLocalizedFormatted(key, objects)))
            .toArray(String[]::new);
    }
}
