package tectech.voidcraft;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.relauncher.FMLInjectionData;
import tectech.TecTech;

/**
 * Voidcraft rework configuration (EoH rework, Phase 2 vertical slice).
 *
 * <p>
 * {@code config/voidcraft.cfg}:
 * <ul>
 * <li>{@code enabled} — master switch. When false the whole voidcraft module (components, covers, assembler,
 * Unstable Solar System, controller item) is unregistered. Default true.</li>
 * </ul>
 *
 * @see docs/Voidcraft_Implementation_Plan.md
 */
public final class VoidcraftConfig {

    /** Master switch: when false the whole voidcraft module is unregistered. */
    public static boolean enabled = true;

    private static boolean initialized = false;

    private VoidcraftConfig() {
        throw new AssertionError("Configuration holder");
    }

    /**
     * Load {@code config/voidcraft.cfg} exactly once. Called at the very start of the voidcraft preLoad.
     */
    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        // Game dir via injection data (same pattern as goodgenerator.GGConfigLoader — works pre-init, no event needed).
        File gameDir = (File) FMLInjectionData.data()[6];
        Configuration config = new Configuration(new File(new File(gameDir, "config"), "voidcraft.cfg"));
        config.load();
        enabled = config
            .get(
                Configuration.CATEGORY_GENERAL,
                "enabled",
                true,
                "Master switch for the Voidcraft rework (components, covers, assembler, Unstable Solar System). "
                    + "Set to false to unregister the whole module.")
            .getBoolean();
        if (config.hasChanged()) {
            config.save();
        }
        TecTech.LOGGER.info("Voidcraft rework " + (enabled ? "enabled" : "DISABLED (whole module unregistered)"));
    }
}
