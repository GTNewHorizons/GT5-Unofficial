package gregtech.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.StatCollector;

import gregtech.GT_Mod;

public class GT_TooltipDataCache {

    public static class TooltipData {

        public List<String> text;
        public List<String> shiftText;

        public TooltipData(List<String> text, List<String> shiftText) {
            this.text = text;
            this.shiftText = shiftText;
        }
    }

    private final Map<String, TooltipData> fetchedTooltipData = new HashMap<>();

    /**
     * Returns tooltip data respecting the user's configured verbosity levels, applying any formatting arguments.
     *
     * @param key  the key to lookup
     * @param args arguments for string formatting (prefer using positional arguments)
     * @return The tooltip data the user asked for
     */
    public TooltipData getData(String key, Object... args) {
        TooltipData tooltipData = fetchedTooltipData.get(key);
        if (tooltipData == null) {
            tooltipData = getUncachedTooltipData(key, args);
            fetchedTooltipData.put(key, tooltipData);
        }
        return tooltipData;
    }

    /**
     * Builds tooltip data respecting the user's configured verbosity levels, applying any formatting arguments.
     *
     * @param key  the key to lookup
     * @param args arguments for string formatting (prefer using positional arguments)
     * @return The tooltip data the user asked for
     */
    public TooltipData getUncachedTooltipData(String key, Object... args) {
        List<String> lines = getAllLines(key, args);
        int normalLines = lines.size();
        if (Math.max(GT_Mod.gregtechproxy.mTooltipVerbosity, GT_Mod.gregtechproxy.mTooltipShiftVerbosity) >= 3) {
            lines.addAll(getAllLines(key + ".extended", args)); // Are extended lines enabled? If so add them to the
                                                                // lines
        }
        if (lines.size() == 0) {
            lines.add(key); // Fallback in case no lines could be found at all
        }
        return new TooltipData(
            lines.subList(0, getVerbosityIndex(GT_Mod.gregtechproxy.mTooltipVerbosity, normalLines, lines.size())),
            lines
                .subList(0, getVerbosityIndex(GT_Mod.gregtechproxy.mTooltipShiftVerbosity, normalLines, lines.size())));
    }

    /**
     * Gets all the lines for the given key and every other subsequent consecutive key with a .n suffix, n in {1,2,3...}
     *
     * @param key  the key to lookup
     * @param args arguments for string formatting (prefer using positional arguments)
     * @return The lines for the key and all of it's subkeys
     */
    private List<String> getAllLines(String key, Object... args) {
        List<String> lines = new ArrayList<>();
        String keyToLookup = key;
        int i = 1; // First loop has no .number postfix
        while (StatCollector.canTranslate(keyToLookup)) {
            lines.add(StatCollector.translateToLocalFormatted(keyToLookup, args));
            keyToLookup = key + "." + i++;
        }
        return lines;
    }

    /**
     * Determines how many lines from a tooltip to include from the full line list to respect a given verbosity level.
     *
     * @param tooltipVerbosity the verbosity level we're applying
     * @param defaultIndex     return if tooltipVerbosity is 2
     * @param maxIndex         return if tooltipVerbosity is greater than 2
     * @return verbosity appropriate index
     */
    private static int getVerbosityIndex(int tooltipVerbosity, int defaultIndex, int maxIndex) {
        int index;
        if (tooltipVerbosity < 1) {
            index = 0;
        } else if (tooltipVerbosity == 1) {
            index = 1;
        } else if (tooltipVerbosity == 2) {
            index = defaultIndex;
        } else {
            index = maxIndex;
        }
        return index;
    }
}
