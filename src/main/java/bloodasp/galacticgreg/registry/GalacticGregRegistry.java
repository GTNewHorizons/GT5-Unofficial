package bloodasp.galacticgreg.registry;

import static bloodasp.galacticgreg.api.enums.ModContainers.Vanilla;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.chunk.IChunkProvider;

import bloodasp.galacticgreg.GalacticGreg;
import bloodasp.galacticgreg.api.Enums.DimensionType;
import bloodasp.galacticgreg.api.ModContainer;
import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.dynconfig.DynamicDimensionConfig;
import bloodasp.galacticgreg.generators.GenEllipsoid;
import cpw.mods.fml.common.Loader;

/**
 * GalacticGregs registry
 *
 */
public class GalacticGregRegistry {

    private static final Map<String, ModContainer> _mModContainers = new HashMap<>();
    public static final String DIM_UNKNOWN = "GGREG_DIMENSION_UNKNOWN";
    private static boolean _mInitializationDone = false;

    /**
     * Register new ModContainer in the registry. Call this after you've populated it with Dimensions and Blocks Must be
     * called from your own PreInit or Init event
     *
     * @param pModContainer
     * @return
     */
    public static boolean registerModContainer(ModContainer pModContainer) {
        if (_mInitializationDone) {
            GalacticGreg.Logger.warn("Initialization is already done, you can't add more ModContainers!");
            return false;
        }

        if (_mModContainers.containsKey(pModContainer.getModName())) {
            GalacticGreg.Logger
                .warn("There is already a mod registered with that name: [%s]", pModContainer.getModName());
            return false;
        } else {
            GalacticGreg.Logger.info(
                "Registered new mod to generate ores: [%s] Dimensions provided: [%d]",
                pModContainer.getModName(),
                pModContainer.getDimensionList()
                    .size());
            _mModContainers.put(pModContainer.getModName(), pModContainer);
            return true;
        }
    }

    /**
     * Lookup the registered dimensions and try to find the DimensionDefinition that has the ChunkProvider that we have
     * here
     *
     * @param pChunkProvider
     * @return
     */
    public static ModDimensionDef getDimensionTypeByChunkGenerator(IChunkProvider pChunkProvider) {
        try {
            if (!_mInitializationDone) return null;

            String tFQCPN = pChunkProvider.toString()
                .split("@")[0];
            ModDimensionDef tReturnMDD = null;

            for (ModContainer mc : _mModContainers.values()) {
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
     *
     * @return
     */
    public static Collection<ModContainer> getModContainers() {
        if (!_mInitializationDone) return null;

        return _mModContainers.values();
    }

    /**
     * Initializes the Registry. Never do this in your code, GalacticGreg will crash if you do so
     */
    public static boolean InitRegistry() {
        if (_mInitializationDone) // never run init twice!
            return false;

        InitModContainers();

        DynamicDimensionConfig.InitDynamicConfig();
        return true;
    }

    /**
     * Parse modcontainers and search for loaded mods. Enable found mods for generation
     */
    private static void InitModContainers() {
        for (ModContainer mc : _mModContainers.values()) {
            // todo: rename Vanilla mod container name from "Vanilla" to "minecraft"
            if (!Loader.isModLoaded(mc.getModName()) && !mc.getModName()
                .equals(Vanilla.modContainer.getModName())) {
                GalacticGreg.Logger.warn(
                    "Ignoring ModRegistration for OreGen: [%s], because mod is not loaded. Did you misspell the name?",
                    mc.getModName());
                mc.setEnabled(false);
            } else {
                GalacticGreg.Logger.info("Mod [%s] is now enabled for OreGen by GalacticGreg", mc.getModName());
                mc.setEnabled(true);
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
                            .size() == 0) {
                            GalacticGreg.Logger.debug("No generators found, adding build-in ellipsoid generator");
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
        }
        _mInitializationDone = true;
    }

    /**
     * Returns ModContainer for given DimensionDefinition
     *
     * @param pDimensionDefinition
     * @return
     */
    public static ModContainer getModContainerForDimension(ModDimensionDef pDimensionDefinition) {
        if (!_mInitializationDone) return null;

        try {
            for (ModContainer mc : _mModContainers.values()) {
                for (ModDimensionDef md : mc.getDimensionList()) {
                    if (pDimensionDefinition.getDimIdentifier()
                        .equals(md.getDimIdentifier())) {
                        return mc;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
