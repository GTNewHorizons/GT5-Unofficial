package galacticgreg.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.chunk.IChunkProvider;

import galacticgreg.GalacticGreg;
import galacticgreg.api.Enums.DimensionType;
import galacticgreg.api.ModContainer;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.dynconfig.DynamicDimensionConfig;
import galacticgreg.generators.GenEllipsoid;

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
     * Lookup the registered dimensions and try to find the DimensionDefinition that has the ChunkProvider that we have
     * here
     */
    public static ModDimensionDef getDimensionTypeByChunkGenerator(IChunkProvider chunkProvider) {
        try {
            if (!initializationDone) return null;

            String tFQCPN = chunkProvider.toString()
                .split("@")[0];
            ModDimensionDef tReturnMDD = null;

            for (ModContainer mc : modContainers.values()) {
                for (ModDimensionDef mdd : mc.getDimensionList()) {
                    if (mdd.getChunkProviderName()
                        .equals(tFQCPN)) {
                        tReturnMDD = mdd;
                        break;
                    }
                }
            }

            return tReturnMDD;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
     * Parse modcontainers and search for loaded mods. Enable found mods for generation
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
                    "ModID: [%s] DimName: [%s] ValidBlocks: [%d] Identifier: [%s] Generators: [%d]",
                    mc.getModName(),
                    md.getDimensionName(),
                    md.getReplaceableBlocks()
                        .size(),
                    md.getDimIdentifier(),
                    md.getSpaceObjectGenerators()
                        .size());

                // Register default generator if dimension is asteroid and no generator was added
                if (md.getDimensionType() == DimensionType.Asteroid) {
                    if (md.getSpaceObjectGenerators()
                        .isEmpty()) {
                        GalacticGreg.Logger.debug("No generators found, adding built-in ellipsoid generator");
                        md.registerSpaceObjectGenerator(new GenEllipsoid());
                    }
                    GalacticGreg.Logger.info(
                        "Asteroid-Enabled dimension. Registered Generators: [%d]",
                        md.getSpaceObjectGenerators()
                            .size());
                }

                md.finalizeReplaceableBlocks(mc.getModName());
            }
        }
        initializationDone = true;
    }
}
