package gregtech.api.recipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Feature flags for ore recipe registration optimizations. Toggle independently for bisection debugging.
 * <ul>
 * <li>{@code -Dgt.recipe.ore.dedupe.enabled=false} — master off (guards disabled; canonical inputs stay on unless
 * overridden)</li>
 * <li>{@code -Dgt.recipe.ore.dedupe.guard.process=false} — allow duplicate ore-dict handler runs</li>
 * <li>{@code -Dgt.recipe.ore.dedupe.guard.reverse=false} — allow duplicate reverse recycling recipes</li>
 * <li>{@code -Dgt.recipe.ore.dedupe.canonical.inputs=false} — use variant {@code aStack} in recipe inputs</li>
 * </ul>
 * Legacy {@link OreRecipeRegistrationGuard#DISABLE_PROPERTY} is equivalent to {@code enabled=false}.
 */
public final class OreRecipeDedupeFlags {

    public static final String ENABLED_PROPERTY = "gt.recipe.ore.dedupe.enabled";
    public static final String GUARD_PROCESS_PROPERTY = "gt.recipe.ore.dedupe.guard.process";
    public static final String GUARD_REVERSE_PROPERTY = "gt.recipe.ore.dedupe.guard.reverse";
    public static final String CANONICAL_INPUTS_PROPERTY = "gt.recipe.ore.dedupe.canonical.inputs";

    private static final Logger LOGGER = LogManager.getLogger("GregTech GTNH");
    private static boolean loggedConfiguration;

    private OreRecipeDedupeFlags() {}

    public static boolean isMasterEnabled() {
        if (Boolean.getBoolean(OreRecipeRegistrationGuard.DISABLE_PROPERTY)) {
            return false;
        }
        String master = System.getProperty(ENABLED_PROPERTY);
        if (master == null) {
            return true;
        }
        return Boolean.parseBoolean(master);
    }

    public static boolean guardProcessEnabled() {
        return isMasterEnabled() && readFlag(GUARD_PROCESS_PROPERTY, true);
    }

    public static boolean guardReverseEnabled() {
        return isMasterEnabled() && readFlag(GUARD_REVERSE_PROPERTY, true);
    }

    public static boolean canonicalInputsEnabled() {
        return readFlag(CANONICAL_INPUTS_PROPERTY, true);
    }

    public static void logConfigurationOnce() {
        if (loggedConfiguration) {
            return;
        }
        loggedConfiguration = true;
        LOGGER.info(
            "GT ore recipe dedupe flags: enabled={} guard.process={} guard.reverse={} canonical.inputs={}",
            isMasterEnabled(),
            guardProcessEnabled(),
            guardReverseEnabled(),
            canonicalInputsEnabled());
    }

    private static boolean readFlag(String property, boolean defaultValue) {
        String value = System.getProperty(property);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
