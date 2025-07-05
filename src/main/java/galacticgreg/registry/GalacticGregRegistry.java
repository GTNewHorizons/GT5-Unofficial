package galacticgreg.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import galacticgreg.GalacticGreg;
import galacticgreg.api.ModContainer;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.dynconfig.DynamicDimensionConfig;

/**
 * GalacticGregs registry
 *
 */
public class GalacticGregRegistry {

    private static final Map<String, ModContainer> modContainers = new HashMap<>();
    private static boolean initializationDone = false;

    /**
     * Register new ModContainer in the registry. Call this after you've populated it with Dimensions and Blocks Must be
     * called from your own PreInit or Init event
     */
    public static void registerModContainer(ModContainer modContainer) {
        if (initializationDone) {
            GalacticGreg.Logger.warn("Initialization is already done, you can't add more ModContainers!");
            return;
        }

        if (modContainers.containsKey(modContainer.getModName())) {
            GalacticGreg.Logger
                .warn("There is already a mod registered with that name: [%s]", modContainer.getModName());
            return;
        }

        GalacticGreg.Logger.info(
            "Registered new mod to generate ores: [%s] Dimensions provided: [%d]",
            modContainer.getModName(),
            modContainer.getDimensionList()
                .size());
        modContainers.put(modContainer.getModName(), modContainer);
    }

    /**
     * Get all registered modcontainers. Can only be done after the initialization process is done
     */
    public static Collection<ModContainer> getModContainers() {
        if (!initializationDone) return null;

        return modContainers.values();
    }

    /**
     * Initializes the Registry. Never do this in your code, GalacticGreg will crash if you do so
     */
    public static boolean InitRegistry() {
        if (initializationDone) // never run init twice!
            return false;

        InitModContainers();

        DynamicDimensionConfig.InitDynamicConfig();
        return true;
    }

    /**
     * Currently only prints some debug logs. Keeping this because it could be useful in the future.
     */
    private static void InitModContainers() {
        for (ModContainer mc : modContainers.values()) {
            // todo: rename Vanilla mod container name from "Vanilla" to "minecraft"
            if (!mc.isModLoaded()) {
                GalacticGreg.Logger.warn(
                    "Ignoring ModRegistration for OreGen: [%s], because mod is not loaded. Did you misspell the name?",
                    mc.getModName());
                continue;
            }

            GalacticGreg.Logger.info("Mod [%s] is now enabled for OreGen by GalacticGreg", mc.getModName());

            for (ModDimensionDef md : mc.getDimensionList()) {
                GalacticGreg.Logger.info(
                    "ModID: [%s] DimName: [%s] Identifier: [%s]",
                    mc.getModName(),
                    md.getDimensionName(),
                    md.getDimIdentifier());
            }
        }
        initializationDone = true;
    }
}
