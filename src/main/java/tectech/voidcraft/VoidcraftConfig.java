package tectech.voidcraft;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.relauncher.FMLInjectionData;
import tectech.TecTech;

/**
 * Voidcraft rework configuration.
 *
 * <p>
 * {@code config/voidcraft.cfg}:
 * <ul>
 * <li>{@code enabled} — master switch. When false the whole voidcraft module (components, covers, assembler,
 * Unstable Solar System, controller item) is unregistered. Default true.</li>
 * <li>{@code Assembler visuals/preview_enabled} — whether the assemblers draw the rotating preview hologram
 * of the build in their scan volume. Default true.</li>
 * <li>{@code Assembler visuals/preview_distance} — blocks behind the machine's back face where the preview
 * hologram floats. Default 1.5.</li>
 * <li>{@code Assembler visuals/preview_height} — blocks above the machine's top where the preview hologram
 * floats. Default 1.5.</li>
 * </ul>
 *
 * @see docs/Voidcraft_Implementation_Plan.md
 */
public final class VoidcraftConfig {

    /** Master switch: when false the whole voidcraft module is unregistered. */
    public static boolean enabled = true;

    /** Whether the assemblers draw the rotating preview hologram of the build in their scan volume. */
    public static boolean assemblerPreviewEnabled = true;

    /** How far behind the machine's back face the assembler preview hologram floats (blocks). */
    public static double assemblerPreviewDistance = 1.5;

    /** How far above the machine's top the assembler preview hologram floats (blocks). */
    public static double assemblerPreviewHeight = 1.5;

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
        assemblerPreviewEnabled = config
            .get(
                "Assembler visuals",
                "preview_enabled",
                true,
                "Whether the assemblers draw the rotating preview hologram of the build in their scan volume.")
            .getBoolean();
        assemblerPreviewDistance = config
            .get(
                "Assembler visuals",
                "preview_distance",
                1.5D,
                "How far behind the assembler's back face the preview hologram floats (blocks).")
            .getDouble();
        assemblerPreviewHeight = config
            .get(
                "Assembler visuals",
                "preview_height",
                1.5D,
                "How far above the assembler's top the preview hologram floats (blocks).")
            .getDouble();
        if (config.hasChanged()) {
            config.save();
        }
        TecTech.LOGGER.info("Voidcraft rework " + (enabled ? "enabled" : "DISABLED (whole module unregistered)"));
    }
}
