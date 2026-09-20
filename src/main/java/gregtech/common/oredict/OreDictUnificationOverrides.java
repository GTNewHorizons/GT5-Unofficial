package gregtech.common.oredict;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.MagicBees;
import static gregtech.api.enums.Mods.Translocator;

import java.util.HashMap;

import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;

public final class OreDictUnificationOverrides {

    // oreName -> modId
    private static final HashMap<String, String> preferredMods = new HashMap<>();
    private static boolean unificationComplete = false;

    // Names of (prefix, material) pairs MaterialLib serves never reach this table: their target is the pair's
    // stack, see GTOreDictUnificator. What remains are other mods' parts of shapes GregTech does not generate.
    static {
        add(Translocator.ID, "nuggetDiamond");
        add(MagicBees.ID, "nuggetEmerald");
    }

    private OreDictUnificationOverrides() {}

    private static void add(String modId, String... oreNames) {
        for (String oreName : oreNames) {
            preferredMods.put(oreName, modId);
        }
    }

    static void handle(OreDictRegistration registration) {
        if (registration.modId == null) return;
        if (!registration.modId.equals(preferredMods.get(registration.oreName))) return;
        if (!registration.prefix.isUnifiable()) return;

        if (unificationComplete) {
            // If you see this log, consider fixing the mod that registered an OreDict entry too late
            GT_FML_LOGGER.warn(
                "Late OreDict override for {} detected after unification completed. Existing recipes may now contain stale items",
                registration.oreName);
        }

        GTOreDictUnificator.set(registration.prefix, registration.material, registration.stack, true, true);
    }

    public static void finalizeUnification() {
        unificationComplete = true;
        GTRecipe.reInit();
    }
}
